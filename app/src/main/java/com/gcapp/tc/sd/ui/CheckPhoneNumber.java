package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.VerificationCodeUtils;
import com.gcapp.tc.R;

/**
 * 功能： 验证手机号码类
 * 
 * @author lenovo
 * 
 */
public class CheckPhoneNumber extends Activity implements View.OnClickListener {
	private final static String TAG = "CheckPhoneNumber";
	private Context context = CheckPhoneNumber.this;
	private ImageButton btn_back;
	private EditText check_phone_num_edit, check_phone_ma_edit;
	private Button check_phone_ma_button, check_phone_ok_button;
	private VerificationCodeUtils verificationCodeUtils;
	private int param;
	private String tag ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_phone_number);
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
		check_phone_num_edit = (EditText) findViewById(R.id.check_phone_num_edit);
		check_phone_ma_edit = (EditText) findViewById(R.id.check_phone_ma_edit);
		check_phone_ma_button = (Button) findViewById(R.id.check_phone_ma_button);
		check_phone_ok_button = (Button) findViewById(R.id.check_phone_ok_button);
		
		Intent typeIntent = getIntent();
		tag = typeIntent.getStringExtra("type");
		if(tag.equals("bank")) {
			check_phone_ok_button.setText("下一步");
			check_phone_num_edit.setText(AppTools.user.getMobile());
			check_phone_num_edit.setEnabled(false);
		}
	}
	
	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		check_phone_ma_button.setOnClickListener(this);
		check_phone_ok_button.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		verificationCodeUtils = new VerificationCodeUtils(
				CheckPhoneNumber.this,
				new VerificationCodeUtils.VerificationCodeListener() {
					@Override
					public void onCheckProgress(int status, int progress) {
						if (status == VerificationCodeUtils.FINISHED
								&& progress == -1) {
							check_phone_ma_button.setText("重新获取验证码");
						} else if (status == VerificationCodeUtils.RUNNING) {
							check_phone_ma_button.setText(progress + "秒");
						}
					}

					@Override
					public void onCheckComplete(int params, String result,
							String msg) {
						switch (params) {
						case 0: // 请求短信验证码

							break;
						case 1: // 检查短信验证码
							if (result.equals("0")) {
								setResult();
							}
							break;
						}
					}
				});
	}

	/**
	 * 传递数据
	 */
	private void setResult() {
		if(tag.equals("phone")) {
			Intent bundle = new Intent();
			bundle.putExtra("mobile", check_phone_num_edit.getText().toString());
			setResult(Activity.RESULT_OK, bundle);
			finish();
		}else if(tag.equals("bank")) {
			Intent bankIntent = new Intent(context, AlterBankCardActivity.class);
			startActivity(bankIntent);
			finish();
		}
	}

	/**
	 * 重写菜单布局
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_check_phone_number, menu);
		return true;
	}

	/**
	 * 重写菜单监听
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
	 * 公用点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.check_phone_ma_button:// 获取短信验证码
			if (checkEdit(check_phone_num_edit, false)) {
				// new MyAsynTask().execute(0);
				checkPhoneNumber();
				param = 0;
			}
			break;
		case R.id.check_phone_ok_button:// 确定
			if (checkEdit(check_phone_num_edit, false)
					&& checkEdit(check_phone_ma_edit, false)) {
				// new MyAsynTask().execute(1);
				checkPhoneNumber();
				param = 1;
			}
			break;
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
		case R.id.check_phone_num_edit:
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
					return true;
				}
			}
			break;
		case R.id.check_phone_ma_edit:
			if (!hasFocus) {
				String str = ((EditText) v).getText().toString();
				if (null == str || str.equals("")) {
					((EditText) v).setError("验证码不能为空");
					((EditText) v).setCompoundDrawablesWithIntrinsicBounds(0,
							0, R.drawable.ic_action_error, 0);
				} else {
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * 验证手机号码
	 */
	public void checkPhoneNumber() {
		
		if (param == 0) {
			verificationCodeUtils.startCheck(0,
					check_phone_num_edit.getText().toString());
		} else if (param == 1) {
			verificationCodeUtils.startCheck(1,
					check_phone_num_edit.getText().toString(),
					check_phone_ma_edit.getText().toString());
		}
		
//		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
//				"正在请求...", 0) {
//			@Override
//			public void responseCallback(JSONObject ob) {
//				if (RequestUtil.DEBUG)
//					Log.i(TAG, "验证号码的请求结果" + ob);
//				try {
//					String hasPhoneNumber = ob.getString("hasPhoneNumber");
//					if ("0".equals(hasPhoneNumber)) {
//						if (param == 0) {
//							verificationCodeUtils.startCheck(0,
//									check_phone_num_edit.getText().toString());
//						} else if (param == 1) {
//							verificationCodeUtils.startCheck(1,
//									check_phone_num_edit.getText().toString(),
//									check_phone_ma_edit.getText().toString());
//						}
//					} else {
//						check_phone_num_edit.setError(ob.getString("msg"));
//						check_phone_num_edit
//								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//										R.drawable.ic_action_error, 0);
//					}
//				} catch (JSONException e) {
//					Log.w("TAG", "JSONException==>" + e.toString());
//					MyToast.getToast(context, "数据异常,请重试！");
//				}
//				if (ob.toString().equals("-500")) {
//					MyToast.getToast(context, "验证超时！");
//				}
//			}
//
//			@Override
//			public void responseError(VolleyError error) {
//				MyToast.getToast(context, "抱歉，请求出现未知错误..");
//				if (RequestUtil.DEBUG)
//					Log.e(TAG, "请求报错" + error.getMessage());
//			}
//		};
//		requestUtil.commit_checkPhoneNumber(check_phone_num_edit.getText()
//				.toString());
	}

}
