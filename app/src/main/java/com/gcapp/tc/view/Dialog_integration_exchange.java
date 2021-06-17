package com.gcapp.tc.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gcapp.tc.R;

/**
 * 功能：积分兑换的提示框
 * 
 * @author lenovo
 * 
 */
@SuppressLint("UseSparseArrays")
public class Dialog_integration_exchange extends Dialog implements
		OnClickListener {
	private final static String TAG = "Dialog_integration_exchange";
	private Context context;
	private TextView tv_close;// 送的积分

	public Dialog_integration_exchange(Context context) {
		super(context);
	}

	public Dialog_integration_exchange(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public Dialog_integration_exchange(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_confirm_exchange);
		findView();
		setListner();
	}

	public void findView() {
		tv_close = (TextView) findViewById(R.id.tv_close);
	}

	/** 绑定监听 **/
	private void setListner() {
		tv_close.setOnClickListener(this);
	}

	/** 点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_close:
			Dialog_integration_exchange.this.dismiss();
			break;
		}
	}

	private DialogResultListener listener;

	public void setDialogResultListener(DialogResultListener listener) {
		this.listener = listener;
	}

	public interface DialogResultListener {
		/**
		 * 获取结果的方法
		 * 
		 * @param resultCode
		 *            0.取消 1.确定
		 */
		void getResult(int resultCode);
	}

}
