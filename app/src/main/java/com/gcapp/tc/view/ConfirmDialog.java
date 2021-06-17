package com.gcapp.tc.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gcapp.tc.R;

/**
 * 功能：公用的自定义提示框
 * 
 * @author lenovo
 * 
 */
@SuppressLint("UseSparseArrays")
public class ConfirmDialog extends Dialog implements OnClickListener {
	private final static String TAG = "ConfirmDialog";

//	private Context context;
	private TextView tv_title;// 提示
	private TextView dl_tv_content;// 内容
	private Button dl_btn_cancel;// 取消
	private Button dl_btn_confirm;// 确定
	private Button dl_btn_update;

	public ConfirmDialog(Context context) {
		super(context);
	}

	public ConfirmDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public ConfirmDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_confirm);
		this.setCancelable(false);
		findView();
		setListner();
	}

	/**
	 * 设置确认按钮的文字
	 */
	public void setDialogConfirmText(String confirmText) {
		dl_btn_confirm.setText(confirmText);
	}

	/**
	 * 设置取消按钮的文字
	 */
	public void setDialogCancelText(String cancelText) {
		dl_btn_cancel.setText(cancelText);
	}
	
	/**
	 * 设置更新的文字
	 */
	public void setDialogUpdateText(String updateText) {
		dl_btn_update.setText(updateText);
	}
	
	/**
	 * 隐藏取消按钮
	 */
	public void hideCancelBtn(boolean flag) {
		if(flag) {
			dl_btn_cancel.setVisibility(View.GONE);
			dl_btn_confirm.setVisibility(View.GONE);
			dl_btn_update.setVisibility(View.VISIBLE);
		}else {
			dl_btn_cancel.setVisibility(View.VISIBLE);
			dl_btn_confirm.setVisibility(View.VISIBLE);
			dl_btn_update.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setDialogTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 */
	public void setDialogContent(String content) {
		Log.i(TAG, content);
		Log.i(TAG, dl_tv_content + "");
		dl_tv_content.setText(content);
	}

	public void findView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		dl_tv_content = (TextView) findViewById(R.id.dl_tv_content);
		dl_btn_cancel = (Button) findViewById(R.id.dl_btn_cancel);
		dl_btn_confirm = (Button) findViewById(R.id.dl_btn_confirm);
		dl_btn_update = (Button) findViewById(R.id.dl_btn_update);
	}

	/** 绑定监听 **/
	private void setListner() {
		dl_btn_cancel.setOnClickListener(this);
		dl_btn_confirm.setOnClickListener(this);
		dl_btn_update.setOnClickListener(this);
	}

	/** 点击监听 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dl_btn_cancel:
			if (null != this.listener) {
				listener.getResult(0);
			}
			ConfirmDialog.this.dismiss();
			break;
		case R.id.dl_btn_confirm:
			if (null != this.listener) {
				listener.getResult(1);
			}
			ConfirmDialog.this.dismiss();
			break;
		case R.id.dl_btn_update:
			if (null != this.listener) {
				listener.getResult(2);
			}
			ConfirmDialog.this.dismiss();
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
