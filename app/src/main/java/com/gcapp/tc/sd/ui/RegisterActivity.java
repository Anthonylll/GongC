package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Users;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.MyPushTask;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：注册界面，实现注册操作 版本
 * 2018-01-10(该界面不再使用，只使用手机注册)
 * @author lenovo
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {
	private final static String TAG = "RegisterActivity";
	private Context context = RegisterActivity.this;
	private ConfirmDialog dialog;// 提示框
	private Button btn_login, btn_reg;
	private ImageButton btn_back;
	private EditText et_name, et_pass, et_upass, et_qq;
	private String userName, userPass, userRePass, userqq;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	// private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	private ProgressDialog mProgress = null;
	private SharedPreferences settings;
	private TextView reg_tip, reg_phone_num_button;
	private TextView tv_passSecurityLevel;// 密码安全度判断显示
	private LinearLayout ll_passSecurityLevel;
	private TextView tv_weaK, tv_middle, tv_strong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		App.activityS.add(this);
		init();
		findView();
		setListener();
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		myHandler = new MyHandler();
		et_qq = (EditText) this.findViewById(R.id.reg_et_userQQ);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		tv_passSecurityLevel = (TextView) this
				.findViewById(R.id.tv_passSecurityLevel);// 密码安全度显示
		ll_passSecurityLevel = (LinearLayout) this
				.findViewById(R.id.ll_passSecurityLevel);// 密码安全度显示
		ll_passSecurityLevel.setVisibility(View.INVISIBLE);
		tv_weaK = (TextView) findViewById(R.id.tv_weak);
		tv_middle = (TextView) findViewById(R.id.tv_middle);
		tv_strong = (TextView) findViewById(R.id.tv_strong);
		btn_login = (Button) this.findViewById(R.id.reg_top_btn_login);
		btn_reg = (Button) this.findViewById(R.id.reg_btn_reg);
		et_name = (EditText) this.findViewById(R.id.reg_et_userName);
		et_pass = (EditText) this.findViewById(R.id.reg_et_userPass);// 密码
		et_upass = (EditText) this.findViewById(R.id.reg_et_reUserPass);
		reg_tip = (TextView) findViewById(R.id.reg_tip);
		reg_phone_num_button = (TextView) findViewById(R.id.reg_phone_num_button);
		reg_tip.setText(Html.fromHtml("注册即表示同意" + "<font color='#FF6E78'>《"
				+ getResources().getString(R.string.app_logo)
				+ "彩票软件用户注册协议》</font>"));
		// 显示下划线
		reg_phone_num_button.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	/**
	 * 初始化参数
	 */
	private void init() {
		opt = "1";
		dialog = new ConfirmDialog(this, R.style.dialog);
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(RegisterActivity.this);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_reg.setOnClickListener(this);
		et_pass.addTextChangedListener(new EditChangedListener());
		reg_tip.setOnClickListener(this);
		reg_phone_num_button.setOnClickListener(this);
	}

	/**
	 * 公用监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_top_btn_login:
			doLogin();
			break;
		case R.id.reg_btn_reg:
			if (NetWork.isConnect(RegisterActivity.this)) {
				if (check()) {
					register_username();
					// myAsynTask = new MyAsynTask();
					// myAsynTask.execute();
				}
			} else {
				MyToast.getToast(RegisterActivity.this, "网络连接异常，注册失败！");
			}
			break;
		case R.id.btn_back:
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			RegisterActivity.this.startActivity(intent);
			RegisterActivity.this.finish();
			break;
		case R.id.reg_tip:
			Intent intent2 = new Intent(RegisterActivity.this,
					Regist_Agreement.class);
			RegisterActivity.this.startActivity(intent2);
			break;
		case R.id.reg_phone_num_button:
			Intent intent1 = new Intent(RegisterActivity.this,
					Regist_phoneNum_Activity.class);
			RegisterActivity.this.startActivity(intent1);
			RegisterActivity.this.finish();
			break;
		}
	}

	/**
	 * 校验用户名和密码
	 */
	private boolean check() {
		boolean isTrue = true;
		/**
		 * 判断是否含非法字符
		 */
		String all = "^[\\u4e00-\\u9fa5\\w]+$";// 匹配中文、数字、字母
		String regexqq = "[1-9][0-9]{4,14}"; // QQ号码
		String username = et_name.getText().toString().trim();
		if ("".equals(username)) {
			MyToast.getToast(context, "用户名不能为空");
			return false;
		}
		Pattern pattern = Pattern.compile(all);
		boolean isPassUserName = pattern.matcher(username).matches();
		if (!isPassUserName) {
			MyToast.getToast(context, "用户名只能为数字、英文、中文");
			return false;
		}

		int charLength = LotteryUtils.getRexStrLength(username);
		if (charLength < 6 || charLength > 16) {
			MyToast.getToast(context, "用户名长度为6-16个字符");
			return false;
		}

		/**
		 * 验证qq
		 */
//		String qqnumber = et_qq.getText().toString().trim();
//		if ("".equals(qqnumber)) {
//			MyToast.getToast(context, "请输入QQ号码");
//			return false;
//		}
//		Pattern pattern2 = Pattern.compile(regexqq);
//		boolean isQQ = pattern2.matcher(qqnumber).matches();
//		if (!isQQ) {
//			MyToast.getToast(context, "QQ号码不合规范");
//			return false;
//		}

		/**
		 * 验证密码
		 */
		String pwd = et_pass.getText().toString();
		if ("".equals(pwd)) {
			MyToast.getToast(context, "请输入密码");
			return false;
		}
		if (pwd.length() < 6 || pwd.length() > 16) {
			MyToast.getToast(context, "请输入6-16位数字、字母或常用符号，字母区分大小写");
			return false;
		}

		if (quchong(pwd).size() < 3 || !ruoCheck(pwd)) {
			MyToast.getToast(context, "您输入的密码强度过弱，请重新输入，试试字母、数字、常用符号的组合")
					;
			return false;
		}
		/**
		 * 确认密码
		 */
		String pwd1 = et_upass.getText().toString();
		if ("".equals(pwd1)) {
			MyToast.getToast(context, "请输入确认密码");
			return false;
		}
		if (!pwd.equals(pwd1)) {
			MyToast.getToast(context, "两次密码输入不一致，请重新输入");
			return false;
		}
		return true;
	}

	/**
	 * 检验是否密码强度过弱
	 */
	public boolean ruoCheck(String password) {
		if (password.equals("123456") || password.equals("654321")
				|| password.equals("123456789") || password.equals("987654321")
				|| password.equals("12345678") || password.equals("87654321")
				|| password.equals("1234567") || password.equals("7654321"))
			return false;
		else
			return true;
	}

	/**
	 * 是否包含特殊字符
	 * 
	 * @param password
	 *            ：密码值
	 * @return
	 */
	public boolean checkSpecial(String password) {
		String all = "(\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\_|\\-|\\=|\\+|\\\\|\\||\\[|\\]|\\{|\\}|\\;|\\'|\\:|\\\"|\\,|\\.|\\/|\\<|\\>|\\?)";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 是否包含数字
	 * 
	 * @param password
	 *            ：密码值
	 * @return
	 */
	public boolean checkContainsNum(String password) {
		String all = "[0-9]";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 是否包含字母
	 * 
	 * @param password
	 *            ：密码值
	 * @return
	 */
	public boolean checkContainsLetter(String password) {
		String all = "[a-zA-Z]";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 去除重复字符方法
	 * 
	 * @param password
	 *            ：密码值
	 * @return
	 */
	public List quchong(String password) {
		List list = new ArrayList();
		for (int i = 0; i < password.length(); i++) {
			if (!list.contains(password.substring(i, i + 1))) {
				list.add(password.substring(i, i + 1));
			}
		}
		return list;
	}

	/**
	 * 跳到登录页面
	 */
	private void doLogin() {
		Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
		RegisterActivity.this.startActivity(intent);
		this.finish();
	}

	/**
	 * 改变强度
	 * 
	 * @param tv
	 */
	public void changeTextView(TextView tv) {
		tv_weaK.setBackgroundResource(R.color.hall_bg_grey2);
		tv_middle.setBackgroundResource(R.color.hall_bg_grey2);
		tv_strong.setBackgroundResource(R.color.hall_bg_grey2);
		// tv_weaK.setTextColor(Color.BLACK);
		// tv_middle.setTextColor(Color.BLACK);
		// tv_strong.setTextColor(Color.BLACK);

		tv.setBackgroundResource(R.color.common_bg_yellow);
		tv.setTextColor(Color.WHITE);
	}

	/**
	 * 监听密码文本输入
	 */
	class EditChangedListener implements TextWatcher {
		@Override
		public void afterTextChanged(Editable et) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// 输入文本之前的状态
		}

		@Override
		public void onTextChanged(CharSequence et, int start, int count,
				int after) {
			ll_passSecurityLevel.setVisibility(View.INVISIBLE);
			// 输入文字中的状态，count是一次性输入字符数
			String str = et.toString();
			if (str.length() < 6 || str.length() > 16) {
				// tv_passAlert.setVisibility(View.VISIBLE);
				// tv_passAlert.setText("请输入6-16位数字、字母或常用符号，字母区分大小写");
			}

			if (quchong(str).size() < 3 || !ruoCheck(str)) {
				// tv_passAlert.setVisibility(View.VISIBLE);
				// tv_passAlert.setText("您输入的密码强度过弱，请重新输入，试试字母、数字、常用符号的组合");
			}

			if (null == str || "".equals(str)) {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("弱");
				changeTextView(tv_weaK);

			} else if (str.length() < 6 || quchong(str).size() < 3
					|| !ruoCheck(str)) {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("弱");
				changeTextView(tv_weaK);
				// 如果密码长度大于等于6 且 去重后密码长度大于等于3 且 密码中包含特殊字符 且 密码中包含英文字符 且 密码中包含数字
				// 的才为强密码。
			} else if (str.length() > 5 && quchong(str).size() > 2
					&& checkSpecial(str) && checkContainsLetter(str)
					&& checkContainsNum(str)) {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("强");
				changeTextView(tv_strong);

			} else {
				ll_passSecurityLevel.setVisibility(View.VISIBLE);
				tv_passSecurityLevel.setText("中");
				changeTextView(tv_middle);
			}

		}
	}

	/**
	 * 用户名注册
	 */
	public void register_username() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"注册中...") {
			String error = "-1";

			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "注册请求结果==》" + ob);
				try {
					error = ob.getString("error");
					if ("0".equals(error)) {
						AppTools.user = new Users();
						AppTools.user.setUid(ob.optString("uid"));
						AppTools.user.setName(ob.optString("name"));
						AppTools.user.setRealityName(ob
								.optString("realityName"));
						AppTools.user.setBalance(ob.optLong("balance"));
						AppTools.user.setFreeze(ob.optDouble("freeze"));
						AppTools.user.setEmail(ob.optString("email"));
						AppTools.user.setIdcardnumber(ob
								.optString("idcardnumber"));
						AppTools.user.setMobile(ob.optString("mobile"));
						AppTools.user.setMsgCount(ob.optInt("msgCount"));
						AppTools.user.setMsgCountAll(ob.optInt("msgCountAll"));
						AppTools.user.setScoring(ob.optInt("scoring"));
						// 用户密码 （没加密的）
						AppTools.user.setUserPass(userPass);
						// return AppTools.ERROR_SUCCESS + "";
					}
				} catch (Exception e) {
					e.printStackTrace();
					error = "-1";
				}
				myHandler.sendEmptyMessage(Integer.parseInt(error));
				if (ob.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		userName = et_name.getText().toString().trim();
		userPass = et_pass.getText().toString().trim();
		userqq = et_qq.getText().toString().trim();
		requestUtil.register_phone("0", userName,
				MD5.md5(userPass + AppTools.MD5_key), "", userqq);
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			RegisterActivity.this.startActivity(intent);
			RegisterActivity.this.finish();
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
			if (null != mProgress && mProgress.isShowing()) {
				mProgress.dismiss();
			}
			switch (msg.what) {
			case 0:
				AppTools.Status = "1";
				MyPushTask.newInstances(RegisterActivity.this).commit();

				// 将用户名记住
				settings = getSharedPreferences("app_user", 0);
				Editor editor = settings.edit();
				editor.putString("name", userName);
				editor.putString("pass", userPass);
				editor.putBoolean("isLogin", true);
				editor.commit();

				dialog.show()	;
				dialog.setDialogContent("注册成功！为了账户安全，马上完善个人信息");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							Intent intent = new Intent(context,
									AccountInformationActivity.class);
							context.startActivity(intent);
							RegisterActivity.this.finish();
						} else {
							RegisterActivity.this.finish();
							MainActivity.toCenter();
						}
					}
				});
				break;
			case 1:
				MyToast.getToast(RegisterActivity.this, "网络不稳定，请稍后再试");
				break;
			case -2:
				MyToast.getToast(RegisterActivity.this, "两次密码不正确，请重新输入");
				break;
			case -10:
				MyToast.getToast(RegisterActivity.this, "密码长度必须大于等于6");
				break;
			case -11:
				MyToast.getToast(RegisterActivity.this, "用户名不能为空");
				break;
			case -12:
				MyToast.getToast(RegisterActivity.this, "用户名不符合规则");
				break;
			case -500:
				MyToast.getToast(RegisterActivity.this, "连接超时");
				break;
			case -110:
				MyToast.getToast(RegisterActivity.this, "该用户名已存在。注册失败");
				break;
			case -108:
				MyToast.getToast(RegisterActivity.this, "邀请码不存在！");
				break;
			default:
				MyToast.getToast(RegisterActivity.this, "注册失败");
				break;
			}
			super.handleMessage(msg);
		}
	}
}
