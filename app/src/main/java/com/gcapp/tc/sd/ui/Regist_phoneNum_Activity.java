package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Users;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.MyPushTask;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.utils.VerificationCodeUtils;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：通过手机号码注册类
 * 
 * @author lenovo
 * 
 */
public class Regist_phoneNum_Activity extends Activity implements
		View.OnClickListener {
	private final static String TAG = "Regist_phoneNum_Activity";
	private Context context = Regist_phoneNum_Activity.this;
	private ConfirmDialog dialog;// 提示框
	private Button btn_login, btn_reg, check_phone_ma_button_phone;
	private ImageButton btn_back;
	private EditText et_name, et_pass, et_upass, check_phone_ma_edit_phone,
			et_invitation;
	private String userName, userPass, userRePass, userInvitation;
//	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	private MyHandler myHandler;
//	private ProgressDialog mProgress = null;
	private TextView reg_tip;
	private VerificationCodeUtils verificationCodeUtils;
	private long lastRegistTime = 0;

	private LinearLayout ll_passSecurityLevel;
	private TextView tv_passSecurityLevel;// 密码安全度判断显示
	private TextView tv_weaK, tv_middle, tv_strong;
	private int param;
	private String mark = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist_phone_num);
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
		et_invitation = (EditText) this.findViewById(R.id.invitation_code);
		tv_passSecurityLevel = (TextView) this
				.findViewById(R.id.tv_passSecurityLevel);// 密码安全度显示
		ll_passSecurityLevel = (LinearLayout) this
				.findViewById(R.id.ll_passSecurityLevel);// 密码安全度显示

		tv_weaK = (TextView) findViewById(R.id.tv_weak);
		tv_middle = (TextView) findViewById(R.id.tv_middle);
		tv_strong = (TextView) findViewById(R.id.tv_strong);

		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_login = (Button) this.findViewById(R.id.reg_top_btn_login_phone);
		btn_reg = (Button) this.findViewById(R.id.reg_btn_reg_phone);

		et_name = (EditText) this.findViewById(R.id.reg_et_userName_phone);
		et_pass = (EditText) this.findViewById(R.id.reg_et_userPass_phone);
		et_upass = (EditText) this.findViewById(R.id.reg_et_reUserPass_phone);

		reg_tip = (TextView) findViewById(R.id.reg_tip_phone);

		reg_tip.setText(Html.fromHtml("注册即表示同意" + "<font color='#FF6E78'>《"
				+ getResources().getString(R.string.app_logo)
				+ "软件用户注册协议》</font>"));
		// 显示下划线
		check_phone_ma_button_phone = (Button) findViewById(R.id.check_phone_ma_button_phone);
		check_phone_ma_edit_phone = (EditText) findViewById(R.id.check_phone_ma_edit_phone);

		verificationCodeUtils = new VerificationCodeUtils(this,
				new VerificationCodeUtils.VerificationCodeListener() {
					@Override
					public void onCheckProgress(int status, int progress) {
						if (status == VerificationCodeUtils.FINISHED
								&& progress == -1) {
							check_phone_ma_button_phone.setText("重新获取验证码");
						} else if (status == VerificationCodeUtils.RUNNING) {
							check_phone_ma_button_phone.setText("(" + progress
									+ ")");
						}
					}

					@Override
					public void onCheckComplete(int params, String result,
							String msg) {
						switch (params) {
						case 0: // 请求短信验证码

							break;
						case 1: // 检查短信验证码

							if (LessThan2Min()) {
								MyToast.getToast(context, "请两分钟后再注册");
							} else {
								if (result.equals(AppTools.ERROR_SUCCESS + "")) {
									// new MyAsynTask().execute();
									register_phone();
								} else {
									MyToast.getToast(context, "msg");
								}
							}
							break;
						}
					}
				});
	}

	/**
	 * 初始化参数
	 */
	private void init() {
		dialog = new ConfirmDialog(this, R.style.dialog);
//		time = RspBodyBaseBean.getTime();
//		imei = RspBodyBaseBean.getIMEI(context);
		Intent markintent = getIntent();
		mark = markintent.getStringExtra("mark");
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_reg.setOnClickListener(this);
		et_name.setOnFocusChangeListener(new MyEditTextFocusChangeListener());
		et_invitation.setOnFocusChangeListener(new MyEditTextFocusChangeListener());

		et_pass.addTextChangedListener(new EditChangedListener());
		et_upass.setOnFocusChangeListener(new MyEditTextFocusChangeListener());
		reg_tip.setOnClickListener(this);
		check_phone_ma_button_phone.setOnClickListener(this);
//		reg_phone_num_text_username.setOnClickListener(this);
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
	 * @return
	 */
	public boolean checkSpecial(String password) {
		String all = "(\\~|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\_|\\-|\\=|\\+|\\\\|\\||\\[|\\]|\\{|\\}|\\;|\\'|\\:|\\\"|\\,|\\.|\\/|\\<|\\>|\\?)";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 去除重复字符方法
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
	 * 监听密码文本输入
	 */
	class EditChangedListener implements TextWatcher {
		@Override
		public void afterTextChanged(Editable et) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence et, int start, int count,
				int after) {
			// tv_passAlert.setVisibility(View.GONE);
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
	 * 是否包含数字
	 * 
	 * @param password
	 *            ：密码文本
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
	 *            ：密码文本
	 * @return
	 */
	public boolean checkContainsLetter(String password) {
		String all = "[a-zA-Z]";
		Pattern pattern = Pattern.compile(all, Pattern.CASE_INSENSITIVE);
		boolean isSpecial = pattern.matcher(password).find();
		return isSpecial;
	}

	/**
	 * 改变文本样式
	 * 
	 * @param tv
	 *            ：文本对象
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
	 * 公用监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_top_btn_login_phone:
			doLogin();
			break;

		case R.id.reg_btn_reg_phone:
			userInvitation = et_invitation.getText().toString().trim();
			userName = et_name.getText().toString().trim();
			userPass = et_pass.getText().toString().trim();
			userRePass = check_phone_ma_edit_phone.getText().toString().trim();

			if (NetWork.isConnect(context) && checkEdit(et_name, false)
					&& checkEdit(et_pass, false)&&checkEdit(et_upass, false) && checkEdit(et_invitation, false)
					&& checkEdit(check_phone_ma_edit_phone, false)) {
				param = 1;
				checkPhoneNumber();
			} else {
				if (!checkEdit(et_name, false)) {
					et_name.requestFocus();
					return;
				}

				if (!checkEdit(et_invitation, false)) {
					et_invitation.requestFocus();
					return;
				}
				if (!checkEdit(et_pass, false)) {
					et_pass.requestFocus();
					return;
				}
				if (!checkEdit(et_upass, false)) {
					et_upass.requestFocus();
					return;
				}
				if (!checkEdit(check_phone_ma_edit_phone, false)) {
					check_phone_ma_edit_phone.requestFocus();
					return;
				}
			}
			break;
		case R.id.btn_back:
			Intent intent = new Intent(context, LoginActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.reg_tip_phone:
			Intent intent2 = new Intent(context, Regist_Agreement.class);
			startActivity(intent2);
			break;
//		case R.id.reg_phone_num_text_username:
//			Intent intent1 = new Intent(context, RegisterActivity.class);
//			startActivity(intent1);
//			finish();
//			break;
		case R.id.check_phone_ma_button_phone:
			userName = et_name.getText().toString().trim();
			if (checkEdit(et_name, false))
				param = 0;
			checkPhoneNumber();
			break;
		}
	}

	/**
	 * 跳到登录页面
	 */
	private void doLogin() {
		Intent intent = new Intent(context, LoginActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	/**
	 * 当文本改变时 *
	 */
	class MyEditTextFocusChangeListener implements View.OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			checkEdit(v, hasFocus);
		}
	}

	/**
	 * 检查输入情况
	 * 
	 * @param v
	 *            指定的控件
	 * @param hasFocus
	 *            true获得焦点，false失去焦点
	 */
	private boolean checkEdit(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.reg_et_userName_phone:
			if (!hasFocus) {
				String str = ((EditText) v).getText().toString();
				if (null == str || str.equals("")) {
					((EditText) v).setError("手机号不能为空");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else if (!LotteryUtils.checkPhoneREX(str)) {
					((EditText) v).setError("手机号码不符合规则");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else {
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					return true;
				}
			}
			break;

		case R.id.invitation_code:
			if (!hasFocus) {
				String str = ((EditText) v).getText().toString();
				if (null == str || str.equals("")) {
					((EditText) v).setError("邀请码不能为空");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} 
//				else if (!LotteryUtils.checkQQREX(str)) {
//					((EditText) v).setError("邀请码不符合规则");
//					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
//							0, R.drawable.ic_action_error, 0);
//				} 
				else {
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					return true;
				}
			}
			break;

		case R.id.reg_et_userPass_phone:
			if (!hasFocus) {
				String str = ((EditText) v).getText().toString();
				if (null == str || str.equals("")) {
					((EditText) v).setError("密码不能为空");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else if (str.length() < 6) {
					((EditText) v).setError("密码至少6位");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else if (str.length() > 16) {
					((EditText) v).setError("密码最多16位");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else {
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					return true;
				}
			}
			break;
		case R.id.reg_et_reUserPass_phone:
			String pwd = et_pass.getText().toString();
			String pwd1 = et_upass.getText().toString();
			if ("".equals(pwd1)) {
				MyToast.getToast(context, "请输入确认密码");
				return false;
			}
			if (!pwd.equals(pwd1)) {
				MyToast.getToast(context, "两次密码输入不一致，请重新输入");
				return false;
			}
			if(!("".equals(pwd1)) && pwd.equals(pwd1)) {
				return true;
			}
			break;
		case R.id.check_phone_ma_edit_phone:
			if (!hasFocus) {
				String str = ((EditText) v).getText().toString();
				if (null == str || str.equals("")) {
					((EditText) v).setError("验证码不能为空");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else {
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, 0, 0);
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * 发送手机号注册请求
	 * 注：弹窗不可取消是为了防止反复注册
	 */
	public void register_phone() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, false,
				"注册中...") {
			String error = "-1";

			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "手机号注册请求结果==》" + ob);

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
						AppTools.user.setIsgreatMan(ob.optString("isManito"));
						// 用户密码 （没加密的）
						AppTools.user.setUserPass(userPass);
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
		requestUtil.register_phone("1", userName,
				MD5.md5(userPass + AppTools.MD5_key), userName, userInvitation);
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(context, LoginActivity.class);
			this.startActivity(intent);
			this.finish();
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
			switch (msg.what) {
			case 0:
				AppTools.Status = "1";
				MyPushTask.newInstances(context).commit();
				// 将用户名记住
				SharedPreferences mSettings = getSharedPreferences("app_user",
						0);
				SharedPreferences.Editor editor = mSettings.edit();
				editor.putString("name", userName);
				editor.putString("pass", userPass);
				editor.putBoolean("isLogin", true);
				editor.commit();

				dialog.show();
				dialog.setDialogContent("注册成功！");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						Regist_phoneNum_Activity.this.finish();
						if(mark != null && mark.equals("1")){
							MainActivity.toFollow();
						}else{
							MainActivity.toCenter();
						}
					}
				});
				break;
			case 1:
				MyToast.getToast(context, "网络不稳定，请稍后再试");
				break;
			case -2:
				MyToast.getToast(context, "两次密码不正确，请重新输入");
				break;
			case -10:
				MyToast.getToast(context, "密码长度必须大于等于6");
				break;
			case -11:
				MyToast.getToast(context, "手机号码不能为空");
				break;
			case -12:
				MyToast.getToast(context, "手机号码不符合规则");
				break;
			case -13:
				MyToast.getToast(context, "验证码不能为空");
				break;
			case -500:
				MyToast.getToast(context, "连接超时");
				break;
			case -110:
				MyToast.getToast(context, "该手机号码已存在。注册失败");
				break;
			case -108:
				MyToast.getToast(context, "邀请码不存在！");
				break;
			default:
				MyToast.getToast(context, "注册失败");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 2分钟不重复提交注册
	 * 
	 * @return
	 */
	private boolean LessThan2Min() {
		long now = System.currentTimeMillis();
		if (now - lastRegistTime < 2 * 60 * 1000) {
			return true;
		}
		lastRegistTime = now;
		return false;
	}

	/**
	 * 判断是否绑定了手机号码
	 */
	public void checkPhoneNumber() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			String error = "-1";

			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "验证手机号码的请求结果==》" + ob);

				try {
					String hasPhoneNumber = ob.getString("hasPhoneNumber");
					if ("0".equals(hasPhoneNumber)) {
						if (param == 0) {
							verificationCodeUtils.startCheck(0, userName);
						} else if (param == 1) {
							verificationCodeUtils.startCheck(1, userName,
									userRePass);
						}
					}else if("1".equals(hasPhoneNumber)) {
						et_name.setError("该手机号码已被注册");
						et_name.setCompoundDrawablesWithIntrinsicBounds(0, 0,
								R.drawable.ic_action_error, 0);
					}
				} catch (JSONException e) {
					error = "2";

				}
				if ("2".equals(error)) {
					et_name.setError("数据异常");
					et_name.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.ic_action_error, 0);
				}

				if (ob.toString().equals("-500")) {
					MyToast.getToast(context, "网络超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.check_phone(et_name.getText().toString().trim());
	}

}
