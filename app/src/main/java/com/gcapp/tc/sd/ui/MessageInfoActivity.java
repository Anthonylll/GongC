package com.gcapp.tc.sd.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.MyMessage;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：站内信详情
 */
public class MessageInfoActivity extends Activity implements OnClickListener {
	private final static String TAG = "MessageInfoActivity";
	private Context context = MessageInfoActivity.this;
	private String opt = "28"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private MyMessage m;
	private ImageButton btn_back;
	private boolean ispush;// 是否是推送跳转

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_msg_info);
		super.onCreate(savedInstanceState);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
		TextView tv_content = (TextView) this.findViewById(R.id.tv_content);
		btn_back.setOnClickListener(this);
		Intent intent = getIntent();
		ispush = intent.getBooleanExtra("ispush", false);
		if (ispush) {
			String title = intent.getStringExtra("title");
			String descrip = intent.getStringExtra("descrip");
			tv_title.setText(title);
			tv_content.setText("   " + descrip);
			return;
		}
		m = AllMessageItemFragment.mes;
		if (null != m) {
			tv_title.setText(m.getTitle());
			tv_content.setText("   " + m.getContent());
			if (!m.isRead()) {
				getMessage();
				// MyAsynTask myAsynTask = new MyAsynTask();
				// myAsynTask.execute();
			}
		}
	}

	/**
	 * 提交消息请求
	 */
	public void getMessage() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "请求结果" + isusesInfo);
				m.setRead(true);
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(MessageInfoActivity.this, "抱歉，请求出现未知错误..");
						
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.commit_msg(m.getId(), 0);
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			if (ispush) {
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
				MainActivity.toCenter();
			}
			finish();
			break;
		}
	}
}
