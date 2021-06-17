package com.gcapp.tc.sd.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.BaseHelper;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author anthony 2018/03/14 申请界面
 */
public class ApplyActivity extends Activity implements OnClickListener {

	private TextView apply_condition;
	private TextView apply_remark;
	private ImageButton apply_btn_back;
	private Button apply_btn;
	private ImageView graet_man_image;
	private Context mContext = ApplyActivity.this;
	private MyHandler2 myHandler2;
	private String imgUrl;
	private ProgressDialog mProgress = null;
	private Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_apply);

		// 1、初始化视图
		initView();
		// 2、初始化监听事件
		initListener();
		// 3、初始化数据
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		apply_condition = (TextView) findViewById(R.id.apply_condition);
		apply_remark = (TextView) findViewById(R.id.apply_remark);
		apply_btn_back = (ImageButton) findViewById(R.id.apply_btn_back);
		apply_btn = (Button) findViewById(R.id.apply_btn);
		graet_man_image = (ImageView) findViewById(R.id.graet_man_image);
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		apply_btn_back.setOnClickListener(this);
		apply_btn.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
		imgUrl = intent.getStringExtra("img");
		
		Spanned tip = Html
				.fromHtml("<font color='#000000'>申请条件：</font><br/>"
						+ "<font color='#000000'>1、平台累积购买3单以上</font><br/>"
						+ "<font color='#000000'>2、平台累积购买5000元以上</font><br/>");
		
		Spanned tip2 = Html
				.fromHtml("<font color='#666666'>备注：</font><br/>"
						+ "<font color='#666666'>· 申请提交后，工作人员会在1-3个工作日内进行审核；</font><br/>"
						+ "<font color='#666666'>· 跟单大厅禁止同一场比赛在两单内出现，多次违规将取消发单权限；</font><br/>"
						+ "<font color='#666666'>· 若有疑问，请联系QQ客服；</font><br/>");
		
		apply_condition.setText(tip);
		apply_remark.setText(tip2);
		
		myHandler2 = new MyHandler2();
		if (imgUrl != null && !"".equals(imgUrl)) {
			mProgress = BaseHelper.showProgress(ApplyActivity.this,
					null, "正在加载...", false, true);
			MyAsynTask2 myAsynTask2 = new MyAsynTask2();
			myAsynTask2.execute();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_btn_back:
			finish();
			break;
		case R.id.apply_btn:
			if (AppTools.user == null) {
				MyToast.getToast(mContext, "未登录账号");
				Intent intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
			} else {
				submitApply();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 提交大神申请
	 */
	private void submitApply() {
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"正在提交申请...") {

			@Override
			public void responseCallback(JSONObject item) {
				try {
					String msg = item.getString("msg");
					MyToast.getToast(mContext, msg);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void responseError(VolleyError error) {

			}
		};
		requestUtil.submitApplyRequest();
	}
	
	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask2 extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			URL myFileUrl = null;
			try {
				myFileUrl = new URL(imgUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				if (null == bitmap) {
					return "-1";
				}
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "11";
		}

		@Override
		protected void onPostExecute(String result) {
			myHandler2.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler2 extends Handler {
		@Override
		public void handleMessage(Message msg) {
			closeProgress();
			
			switch (msg.what) {
			case 11:// 成功
				graet_man_image.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	/** 关闭进度框 */
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			closeProgress();
		}
		return super.onKeyDown(keyCode, event);
	}
}
