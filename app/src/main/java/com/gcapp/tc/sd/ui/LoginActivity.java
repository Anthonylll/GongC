package com.gcapp.tc.sd.ui;

import java.lang.reflect.Method;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Users;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.MyPushTask;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：用户登录界面，实现登录
 */
public class LoginActivity extends Activity implements OnClickListener {
	private final static String TAG = "LoginActivity";
	private Context context = LoginActivity.this;
	public static final int SHOW_MSG = 233;
	private EditText et_name, et_pass;
	private Button btn_login;
	private TextView btn_reg;
	private LinearLayout ll_reg;
	private ImageView activity_login_check;// 判断是否记住用户名与密码
	private TextView activity_login_forgetpass;
	private String userName, userPass; // 获得用户输入的用户名 // 获得用户输入的密码
	private String opt, auth, info, time, imei; // 格式化后的参数
	private SharedPreferences settings;
	private ProgressDialog mProgress = null;
	private Intent intent;
//	private String tagName = "loginTag";
//	private String pass;
	private MyHandler myHandler;
	/** 登录后返回标志*/
	private String mark = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		App.activityS.add(this);
		findView();
		init();
		setListener();
	}

	/**
	 * 判断用户是否登陆过
	 */
	private void init() {
		Intent markintent = getIntent();
		mark = markintent.getStringExtra("mark");
		
		settings = this.getSharedPreferences("app_user", 0);
		boolean isLogin = false;// 没有登录的标识
		String name = "";
		String pass = "";
		if (settings.contains("isLogin")) {
			isLogin = settings.getBoolean("isLogin", false);
		}
		if (settings.contains("name")) {
			name = settings.getString("name", null);
		}
		// 是否记录密码
		boolean isRecord = settings.getBoolean("isRecord", false);

		if (isRecord) {
			et_pass.setText(settings.getString("pass", null));
		}
		activity_login_check.setSelected(isRecord);
		// 显示下划线
		// activity_login_forgetpass.getPaint()
		// .setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// 判断是否有存 用户名
		if (isLogin) {
			// 判断是否有存 密码
			if (settings.contains("pass")) {
				pass = settings.getString("pass", null);
			}
			et_pass.setText(pass);
		}
		et_name.setText(name);
		et_name.setSelection(et_name.getText().length());
		if (null != pass && name != null && !"".equals(name)
				&& !"".equals(pass) && isLogin) {
			String type = getIntent().getStringExtra("loginType");
			if (!"genggai".equals(type))
				doLogin();
		}
	}

	/**
	 * 初始化
	 */
	private void findView() {
		myHandler = new MyHandler();
		et_name = (EditText) this.findViewById(R.id.et_userName);
		et_pass = (EditText) this.findViewById(R.id.et_userPass);
		btn_login = (Button) this.findViewById(R.id.login_btn_login);
		btn_reg = (TextView) this.findViewById(R.id.login_top_btn_reg);
		ll_reg = (LinearLayout) this.findViewById(R.id.ll_reg);
		btn_reg.setText("还没有" + getResources().getString(R.string.app_logo)
				+ "账号？");
		activity_login_check = (ImageView) this
				.findViewById(R.id.activity_login_check);
		activity_login_forgetpass = (TextView) this
				.findViewById(R.id.activity_login_forgetpass);
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(LoginActivity.this);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_login.setOnClickListener(this);
		ll_reg.setOnClickListener(this);
		activity_login_check.setOnClickListener(this);
		activity_login_forgetpass.setOnClickListener(this);
	}

	/**
	 * 设置软键盘
	 */
	private void setting() {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			et_name.setInputType(InputType.TYPE_NULL);
			et_pass.setInputType(InputType.TYPE_NULL); // 关闭软键盘
		} else {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setSoftInputShownOnFocus;
				setSoftInputShownOnFocus = cls.getMethod(
						"setSoftInputShownOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(et_name, false);
				setSoftInputShownOnFocus.invoke(et_pass, false);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_name.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 公共点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_login:
			doLogin();
			break;
		case R.id.ll_reg:
			doRegister();
			break;
		case R.id.activity_login_forgetpass:// 忘记密码
			Intent intent1 = new Intent(this, ForgetPasswordActivity.class);
			startActivity(intent1);
			break;
		case R.id.activity_login_check:
			boolean isSelect = activity_login_check.isSelected();
			activity_login_check.setSelected(!isSelect);
			Editor editor = settings.edit();
			editor.putBoolean("isRecord", !isSelect);
			editor.commit();
			break;
		}
	}

	/**
	 * 去登录
	 */
	private void doLogin() {
		userName = et_name.getText().toString().trim();
		userPass = et_pass.getText().toString().trim();
		if (userName.length() == 0) {
			MyToast.getToast(LoginActivity.this, "用户名不能为空");
			return;
		}
		if (userPass.length() == 0) {
			MyToast.getToast(LoginActivity.this, "密码不能为空");
			return;
		}

		if (NetWork.isConnect(LoginActivity.this)) {
			ToLogin();
		} else {
			MyToast.getToast(LoginActivity.this, "网络异常,登陆失败");
		}
	}

	/**
	 * 跳到注册页面
	 */
	private void doRegister() {
		intent = new Intent(LoginActivity.this, Regist_phoneNum_Activity.class);
		intent.putExtra("mark", mark);
		LoginActivity.this.startActivity(intent);
		LoginActivity.this.finish();
	}

	/**
	 * 提交登录请求
	 */
	public void ToLogin() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在登录...") {
			@Override
			public void responseCallback(JSONObject item) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "登录结果===》" + item);
				try {
					if ("0".equals(item.optString("error"))) {
						AppTools.user = new Users();
						AppTools.user.setUid(item.optString("uid"));
						if (!item.optString("url").contains(AppTools.url)) {
							AppTools.user.setImage_url(AppTools.url
									+ item.optString("headUrl"));
						} else {
							AppTools.user.setImage_url(item
									.optString("headUrl"));
						}
						AppTools.user.setName(item.optString("name"));
						AppTools.user.setRealityName(item
								.optString("realityName"));
						AppTools.user.setBalance(item.optDouble("balance"));
						AppTools.user.setMinWithdraw(item.optDouble("minimumMoney"));
						AppTools.user.setFreeze(item.optDouble("freeze"));
						AppTools.user.setEmail(item.optString("email"));
						AppTools.user.setIdcardnumber(item
								.optString("idcardnumber"));
						AppTools.user.setMobile(item.optString("mobile"));
						AppTools.user.setBankCard(item.optString("bankcard"));
						AppTools.user.setMsgCount(item.optInt("msgCount"));
						AppTools.user
								.setMsgCountAll(item.optInt("msgCountAll"));
						AppTools.user.setScoring(item.optInt("scoring"));
						AppTools.user.setHandselAmount(item
								.optDouble("handselAmount"));
						AppTools.user.setTotalWinMoney(item.optDouble("totalwinmoney"));
						
						AppTools.user.setIsgreatMan(item.optString("isManito"));
						AppTools.user.setExtractMoney(item.optDouble("disdillMoney"));
						// 用户密码 （没加密的）
						AppTools.user.setUserPass(userPass);
						String test = AppTools.user.getIsgreatMan();
						if (AppTools.user != null) {
							AppTools.Status = "1";
							MyPushTask.newInstances(LoginActivity.this)
									.commit();
							String str = getIntent()
									.getStringExtra("loginType");
							if (str == null ) {
								if(mark != null && mark.equals("1")){
									MainActivity.toFollow();
								}else{
									MainActivity.toCenter();
								}
							}
							// 将用户名记住
							if (activity_login_check.isSelected()) {
								settings = getSharedPreferences("app_user", 0);
								Editor editor = settings.edit();
								editor.putString("name", userName);
								editor.putString("pass", userPass);
								editor.putString("uid", AppTools.user.getUid());
								editor.putBoolean("isLogin", true);
								editor.commit();
							} else {
								AppTools.username = userName;
								AppTools.userpass = userPass;
							}
							MyToast.getToast(LoginActivity.this, "登录成功");
							// 登录成功 关闭Activity
							startActivity(new Intent(LoginActivity.this,MainActivity.class));
							LoginActivity.this.finish();
						}
					} else {
						MyToast.getToast(LoginActivity.this,
								item.optString("msg"));
					}
				} catch (Exception ex) {
					Log.i("login", "登录异常---" + ex.getMessage());
					MyToast.getToast(LoginActivity.this, "登录失败");
				}

				if (item.toString().equals("-500")) {
					MyToast.getToast(LoginActivity.this, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(LoginActivity.this, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil
				.commit_login(userName, MD5.md5(userPass + AppTools.MD5_key));
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			switch (msg.what) {
			case 0:
				if (AppTools.user != null) {
					AppTools.Status = "1";
					MyPushTask.newInstances(LoginActivity.this).commit();
					String str = getIntent().getStringExtra("loginType");
					if (str == null) {
						MainActivity.toCenter();
					}
					// 将用户名记住
					if (activity_login_check.isSelected()) {
						settings = getSharedPreferences("app_user", 0);
						Editor editor = settings.edit();
						editor.putString("name", userName);
						editor.putString("pass", userPass);
						editor.putString("uid", AppTools.user.getUid());
						editor.putBoolean("isLogin", true);
						editor.commit();
					} else {
						AppTools.username = userName;
						AppTools.userpass = userPass;
					}
					MyToast.getToast(LoginActivity.this, "登录成功");
					// 登录成功 关闭Activity
					LoginActivity.this.finish();
				}
				break;
			case -1:
				MyToast.getToast(LoginActivity.this, "登录失败");
				break;
			case -110:
				MyToast.getToast(LoginActivity.this, "登录失败");
				break;
			case -500:
				MyToast.getToast(LoginActivity.this, "连接超时");
				break;
			case -113:
				MyToast.getToast(LoginActivity.this, "用户密码错误");
				break;
			case -112:
				MyToast.getToast(LoginActivity.this, "用户名不存在");
				break;
			case SHOW_MSG:
				String msgStr = (String) msg.obj;
				MyToast.getToast(LoginActivity.this, msgStr);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

}
