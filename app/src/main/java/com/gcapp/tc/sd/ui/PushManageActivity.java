package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.MyPushTask;
import com.gcapp.tc.R;

/**
 * 功能：设置里面的推送管理类
 */
public class PushManageActivity extends Activity implements OnClickListener {
	private Context context = PushManageActivity.this;
	private ImageButton setting_btn_kj, setting_btn_zj;
	private ImageButton btn_back;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_apppush);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		setting_btn_kj = (ImageButton) this.findViewById(R.id.setting_btn_kj);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);

		setting_btn_zj = (ImageButton) this.findViewById(R.id.setting_btn_zj);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		settings = getSharedPreferences("app_user", 0);
		editor = settings.edit();
		AppTools.isPushKJ = settings.getString("isPushKJ", "1");
		AppTools.isPushZJ = settings.getString("isPushZJ", "1");

		if (!AppTools.isPushKJ.equals("1")) {
			setting_btn_kj.setSelected(false);
		} else {
			setting_btn_kj.setSelected(true);
		}
		if (!AppTools.isPushZJ.equals("1")) {
			setting_btn_zj.setSelected(false);
		} else {
			setting_btn_zj.setSelected(true);
		}
	}

	/**
	 * 绑定页面控件的监听
	 */
	private void setListener() {
		setting_btn_kj.setOnClickListener(this);
		setting_btn_zj.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 开奖推送 **/
		case R.id.setting_btn_kj:
			// TODO 这里重新开启了事务 开奖勾选
			editor = settings.edit();
			if (!AppTools.isPushKJ.equals("1"))
				AppTools.isPushKJ = "1";
			else
				AppTools.isPushKJ = "0";
			editor.putString("isPushKJ", AppTools.isPushKJ);
			editor.commit();
			setBtnText(setting_btn_kj, AppTools.isPushKJ.equals("1"));
			break;
		/** 中奖推送 **/
		case R.id.setting_btn_zj:
			editor = settings.edit();
			if (!AppTools.isPushZJ.equals("1"))
				AppTools.isPushZJ = "1";
			else
				AppTools.isPushZJ = "0";
			editor.putString("isPushZJ", AppTools.isPushZJ);
			editor.commit();
			setBtnText(setting_btn_zj, AppTools.isPushZJ.equals("1"));
			break;
		case R.id.btn_back:
			MyPushTask.newInstances(context).commit();
			finish();
			break;
		}
	}

	/**
	 * 设置按钮时候可用
	 * 
	 * @param btn
	 *            ：按钮对象
	 * @param isOpen
	 *            ：是否可用参数
	 */
	private void setBtnText(ImageButton btn, boolean isOpen) {
		if (isOpen)
			btn.setSelected(true);
		else
			btn.setSelected(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		MyPushTask.newInstances(context).commit();
		super.onBackPressed();
	}
}
