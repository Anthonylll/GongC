package com.gcapp.tc.sd.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：设置里面的反馈建议模块
 * 
 * @author lenovo
 * 
 */
public class SuggestActivity extends Activity implements OnClickListener {
	private static final String TAG = "SuggestActivity";
	private Context context = SuggestActivity.this;
	private String opt = "48"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private EditText et_content, et_tel, et_email, et_title;
	private Button btn;
	private ImageButton btn_back;
	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
		myHandler = new MyHandler();
		findView();
	}

	/**
	 * 初始化UI控件和绑定监听
	 */
	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		et_title = (EditText) this.findViewById(R.id.suggest_et_title);
		et_content = (EditText) this.findViewById(R.id.suggest_et_context);
		et_tel = (EditText) this.findViewById(R.id.suggest_et_phoneNumber);
		et_email = (EditText) this.findViewById(R.id.suggest_et_email);
		btn = (Button) this.findViewById(R.id.suggest_btn_ok);
		btn.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	/**
	 * 公用点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suggest_btn_ok:
			if (TextUtils.isEmpty(et_title.getText().toString().trim())) {
				MyToast.getToast(getApplicationContext(), "标题内容不能为空..");
				return;
			}
			if (LotteryUtils.checkPhoneREX(et_tel.getText().toString())) {
				if (et_content.getText().toString().trim().length() == 0) {
					MyToast.getToast(SuggestActivity.this, "反馈意见不能为空。");
				} else {
					getDeviceInfo();
					// MyAsynTask myAsynTask = new MyAsynTask();
					// myAsynTask.execute();
				}

			} else {
				MyToast.getToast(getApplicationContext(), "请输入正确的手机号码");
			}
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	private String errorMsg;// 错误信息

	/**
	 * 提交反馈建议请求
	 */
	public void getDeviceInfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"提交中...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, " 反馈建议result：" + object);
				String error;
				try {
					error = object.getString("error");
					errorMsg = object.getString("msg");
				} catch (JSONException e) {
					e.printStackTrace();
					error = "-200";
				}
				myHandler.sendEmptyMessage(Integer.parseInt(error));

				if (object.toString().equals("-500")) {
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
		requestUtil.getSuggestInfo(et_tel.getText().toString(), et_email
				.getText().toString(), et_content.getText().toString(),
				et_title.getText().toString().trim());
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
				MyToast.getToast(SuggestActivity.this, "反馈提交成功");
				SuggestActivity.this.finish();
				break;
			case -500:
				MyToast.getToast(SuggestActivity.this, "连接超时");
				break;
			case -4801:
				MyToast.getToast(SuggestActivity.this, "距离上次反馈不足半个小时，请稍后再反馈。")
						;
				SuggestActivity.this.finish();
				break;
			case -300:
				MyToast.getToast(SuggestActivity.this, "反馈意见不能为空。");
				break;
			default:
				MyToast.getToast(SuggestActivity.this, errorMsg);
				break;
			}
		}
	}

}
