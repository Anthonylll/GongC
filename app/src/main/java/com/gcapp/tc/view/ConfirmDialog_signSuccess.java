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
 * 功能：签到成功的提示框
 * 
 * @author lenovo
 * 
 */
@SuppressLint("UseSparseArrays")
public class ConfirmDialog_signSuccess extends Dialog implements
		OnClickListener {
	private final static String TAG = "ConfirmDialog_signSuccess";
	private Context context;
	private TextView sign_tv_jifen;// 送的积分
	// private Button dl_btn_confirm;// 确定
	private TextView tv_close;// 送的积分

	public ConfirmDialog_signSuccess(Context context) {
		super(context);
	}

	public ConfirmDialog_signSuccess(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public ConfirmDialog_signSuccess(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_confirm_sign);
		findView();
		setListner();
	}

	public void setJifen(int jifen) {
		sign_tv_jifen.setText("" + jifen);
	}

	/**
	 * 设置取消按钮的文字
	 */
	public void setDialogCancelText(String cancelText) {
		// dl_btn_cancel.setText(cancelText);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setDialogTitle(String title) {
		// tv_title.setText(title);
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 */
	// public void setDialogContent(String content) {
	// Log.i(TAG, content);
	// // dl_tv_content.setText(content);
	// }

	public void findView() {
		sign_tv_jifen = (TextView) findViewById(R.id.sign_tv_jifen);
		tv_close = (TextView) findViewById(R.id.tv_close);
		// dl_tv_content = (TextView) findViewById(R.id.dl_tv_content);
		// dl_btn_cancel = (Button) findViewById(R.id.dl_btn_cancel);
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
			ConfirmDialog_signSuccess.this.dismiss();
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
