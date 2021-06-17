package com.gcapp.tc.sd.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.utils.VerificationCodeUtils;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：重置密码类
 * 
 * @author lenovo
 * 
 */
public class ResetPasswordActivity extends Activity implements
		View.OnClickListener {
	private final static String TAG = "ResetPasswordActivity";
	private Context context = ResetPasswordActivity.this;
	private ImageButton btn_back;
	private EditText reset_password_edit, reset_password_comfir_edit,
			reset_password_checkma_edit;
	private Button reset_password_check_button, reset_password_ok;
	private VerificationCodeUtils verificationCodeUtils;
	private String pass, re_pass, check_ma;
	private String moblie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		App.activityS.add(this);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		moblie = getIntent().getStringExtra("moblie");
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		reset_password_edit = (EditText) findViewById(R.id.reset_password_edit);
		reset_password_comfir_edit = (EditText) findViewById(R.id.reset_password_comfir_edit);
		reset_password_checkma_edit = (EditText) findViewById(R.id.reset_password_checkma_edit);
		reset_password_check_button = (Button) findViewById(R.id.reset_password_check_button);
		reset_password_ok = (Button) findViewById(R.id.reset_password_ok);
	}

	/**
	 * 初始化数据和请求
	 */
	private void init() {
		verificationCodeUtils = new VerificationCodeUtils(context,
				new VerificationCodeUtils.VerificationCodeListener() {
					@Override
					public void onCheckProgress(int status, int progress) {
						if (status == VerificationCodeUtils.FINISHED
								&& progress == -1) {
							reset_password_check_button.setText("重新获取验证码");
						} else if (status == VerificationCodeUtils.RUNNING) {
							reset_password_check_button.setText("(" + progress
									+ ")");
						}
					}

					@Override
					public void onCheckComplete(int params, String result,
							String msg) {
						switch (params) {
						case 0: // 请求短信验证码

							break;
						case 1: // 验证短信验证码
							if (result.equals(AppTools.ERROR_SUCCESS + "")) {
								ResetPass();
								// myAsynTask = new MyAsynTask();
								// myAsynTask.execute();
							}
							break;
						}
					}
				});
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		reset_password_check_button.setOnClickListener(this);
		reset_password_ok.setOnClickListener(this);
	}

	/**
	 * 重写菜单创建方法
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_reset_password, menu);
		return true;
	}

	/**
	 * 重写菜单监听方法
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.reset_password_check_button:
			verificationCodeUtils.startCheck(0, moblie);
			break;
		case R.id.reset_password_ok:
			submit();
			break;
		}
	}

	/**
	 * 提交请求
	 */
	private void submit() {
		pass = reset_password_edit.getText().toString();
		re_pass = reset_password_comfir_edit.getText().toString();
		check_ma = reset_password_checkma_edit.getText().toString();

		if (TextUtils.isEmpty(pass)) {
			MyToast.getToast(this, "请输入新的密码");
			return;
		}
		if (TextUtils.isEmpty(re_pass)) {
			MyToast.getToast(this, "请再次确认新的密码");
			return;
		}

		if (pass.trim().length() < 6) {
			MyToast.getToast(this, "输入密码不能至少6位");
			return;
		}
		if (!pass.equals(re_pass)) {
			MyToast.getToast(this, "两次输入的密码不一致");
			return;
		}
		if (TextUtils.isEmpty(check_ma)) {
			MyToast.getToast(this, "请输入短信验证码");
			return;
		}

		if (NetWork.isConnect(this)) {
			verificationCodeUtils.startCheck(1, moblie, check_ma);
		} else {
			MyToast.getToast(this, "请检查网络");
		}
	}

	/**
	 * 重置密码请求
	 */
	public void ResetPass() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在重置...") {
			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "重置密码请求结果==》" + ob);

				try {
					if ("0".equals(ob.getString("error"))) {
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				} catch (JSONException e) {
					MyToast.getToast(context, "密码重置失败");
				}

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
		requestUtil.resetPass(moblie, MD5.md5(pass + AppTools.MD5_key));
	}

}
