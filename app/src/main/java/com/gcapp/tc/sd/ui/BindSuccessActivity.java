package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.R;

/**
 * 功能：绑定账户信息成功页面
 * 
 * @author lenovo
 * 
 */
public class BindSuccessActivity extends Activity implements OnClickListener {
	private static final String TAG = "PaySuccessActivity";
	private Context context = BindSuccessActivity.this;
	private TextView tv_tip;// 提示
	private Button btn_perfectinfo;// 完善资料
	private Button btn_goto_recharge;// 我要充值
	private Button btn_back_tohall;// 返回首页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bindsuc);
		App.activityS.add(this);
		findView();
		setListener();
	}

	/**
	 * 初始化自定义控件
	 */
	public void findView() {
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		btn_perfectinfo = (Button) findViewById(R.id.btn_perfectinfo);
		btn_goto_recharge = (Button) findViewById(R.id.btn_goto_recharge);
		btn_back_tohall = (Button) findViewById(R.id.btn_back_tohall);
	}

	/**
	 * 绑定监听
	 */
	public void setListener() {
		btn_perfectinfo.setOnClickListener(this);
		btn_goto_recharge.setOnClickListener(this);
		btn_back_tohall.setOnClickListener(this);
	}

	/**
	 * 公用监听处理方法
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_perfectinfo:// 查看账户信息资料
			Intent intent = new Intent(context,
					AccountInformationActivity.class);
			context.startActivity(intent);
			finish();
			break;
		case R.id.btn_goto_recharge:// 我要充值
			LotteryUtils.toRecharge(context, AppTools.canAlipay);
			finish();
			break;
		case R.id.btn_back_tohall:// 返回首页
			backToHall();
			break;
		}
	}

	/**
	 * 重新返回键按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 返回个人中心
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 返回首页
	 */
	public void backToHall() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

}
