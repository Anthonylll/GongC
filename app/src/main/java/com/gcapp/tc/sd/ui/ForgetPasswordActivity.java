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
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.Code;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：忘记密码类
 * 
 * @author lenovo
 * 
 */
public class ForgetPasswordActivity extends Activity implements
		View.OnClickListener {
	private final static String TAG = "ForgetPasswordActivity";
	private Context context = ForgetPasswordActivity.this;
	private ImageButton btn_back;
	private EditText forgetpassword_username, forgetpassword_checkma;
	private ImageView forgetpassword_checkma_show;
	private Button forgetpassword_ok;
	private String username;
	private String CheckCode;
	private String mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		App.activityS.add(this);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化自定义控件
	 */
	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		forgetpassword_username = (EditText) findViewById(R.id.forgetpassword_username);
		forgetpassword_checkma = (EditText) findViewById(R.id.forgetpassword_checkma);
		forgetpassword_checkma_show = (ImageView) findViewById(R.id.forgetpassword_checkma_show);
		forgetpassword_ok = (Button) findViewById(R.id.forgetpassword_ok);
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		forgetpassword_checkma_show.setImageBitmap(Code.getInstance()
				.createBitmap());
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		forgetpassword_checkma_show.setOnClickListener(this);
		forgetpassword_ok.setOnClickListener(this);
	}

	/**
	 * 重写菜单布局
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_forget_password, menu);
		return true;
	}

	/**
	 * 重写菜单点击监听
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
	 * 公用的点击监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.forgetpassword_checkma_show:
			forgetpassword_checkma_show.setImageBitmap(Code.getInstance()
					.createBitmap());
			break;
		case R.id.forgetpassword_ok:
			submit();
			break;
		}
	}

	/**
	 * 验证手机号码
	 */
	private void submit() {
		mobile = forgetpassword_username.getText().toString();
		CheckCode = forgetpassword_checkma.getText().toString();
		if (!LotteryUtils.checkPhoneREX(mobile)) {
			MyToast.getToast(this, "手机号码格式不正确");
			forgetpassword_checkma_show.setImageBitmap(Code.getInstance()
					.createBitmap());
			return;
		}
		if (TextUtils.isEmpty(CheckCode)) {
			MyToast.getToast(this, "请输入验证码");
			forgetpassword_checkma_show.setImageBitmap(Code.getInstance()
					.createBitmap());
			return;
		}

		if (!CheckCode.toLowerCase().equals(
				Code.getInstance().getCode().toLowerCase())) {
			MyToast.getToast(this, "验证码错误，请重新输入");
			forgetpassword_checkma_show.setImageBitmap(Code.getInstance()
					.createBitmap());
			return;
		}

		if (NetWork.isConnect(this)) {
			checkMobile();
			// myAsynTask = new MyAsynTask();
			// myAsynTask.execute();
		} else {
			MyToast.getToast(this, "请检查网络");
		}

	}

	/**
	 * 忘记密码时的手机验证请求
	 */
	public void checkMobile() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject ob) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "忘记密码时的手机验证result：" + ob);

				try {
					String hasPhoneNumber = ob.getString("hasPhoneNumber");
					if ("1".equals(hasPhoneNumber)) {
						forgetpassword_checkma_show.setImageBitmap(Code
								.getInstance().createBitmap());
						Intent intent = new Intent(context,
								ResetPasswordActivity.class);
						intent.putExtra("moblie", mobile);
						startActivity(intent);
						finish();

						// return AppTools.ERROR_SUCCESS + "";
					} else {
						MyToast.getToast(context, ob.getString("msg"));
					}
				} catch (JSONException e) {
					Log.w("TAG", "JSONException==>" + e.toString());
					forgetpassword_checkma_show.setImageBitmap(Code
							.getInstance().createBitmap());
					MyToast.getToast(context, "该手机号码未绑定");
					// error = "-1";
				}

				if (ob.toString().equals("-500")) {
					MyToast.getToast(ForgetPasswordActivity.this, "连接超时")
							;
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(ForgetPasswordActivity.this, "抱歉，请求出现未知错误..")
						;
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.forgotPass_checkphone(mobile);
	}

}
