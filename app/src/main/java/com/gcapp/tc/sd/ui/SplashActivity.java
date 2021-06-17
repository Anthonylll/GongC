package com.gcapp.tc.sd.ui;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * @author anthony
 * @date 2018-6-4 上午11:46:47
 * @version 5.5.0
 * @Description
 */
public class SplashActivity extends Activity implements OnClickListener {

	private static final String TAG = "SplashActivity";
	private Context mContext = SplashActivity.this;
	private RelativeLayout skip_layout;
	private Boolean skipFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		App.activityS.add(this);

		// 1、初始化视图-
		initView();
		// 2、初始化监听事件
		initListener();
		// 3、初始化数据
		initData();
	}

	private void initView() {
		skip_layout = (RelativeLayout) findViewById(R.id.skip_layout);
	}

	private void initListener() {
		skip_layout.setOnClickListener(this);
	}

	private void initData() {
		skipFlag = true;
		getWinMessage();
		if (AppTools.isShow) {
			initThread();
		} else {
			skipTo();
		}
	}

	private void initThread() {
		Thread myThread = new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
					if (skipFlag) {
						skipTo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		myThread.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.skip_layout:
			skipFlag = false;
			skipTo();
			break;
		default:
			break;
		}
	}

	/**
	 * 页面跳转
	 */
	private void skipTo() {
		Intent intent;
		if (null == AppTools.user) {
			intent = new Intent(this, LoginActivity.class);
			intent.putExtra("mark", "1");
		}else {
			intent = new Intent(this, MainActivity.class);
		}
		startActivity(intent);
		finish();
	}

	/**
	 * 获取中奖播报信息
	 */
	public void getWinMessage() {
		RequestUtil requestUtil = new RequestUtil(mContext, true,
				Request.CONFIG_CACHE_SHORT) {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "获取中奖播报信息" + reponseJson.toString());
				// 获取中奖播报信息{"error":"-5103","msg":"无中奖信息"}
				if (null != reponseJson && !"".equals(reponseJson)) {
					if (reponseJson.optString("error").equals("0")) {
						AppTools.winMessage = reponseJson.optString("winInfo");
					}
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取中奖播报信息失败" + error.getMessage());
			}
		};
		requestUtil.getWinMessage();
	}

}
