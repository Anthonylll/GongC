package com.gcapp.tc.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * 功能：带有加载提示框的请求
 * 
 * @author lenovo
 * 
 */
public abstract class MyAsynTaskWithProgress extends
		AsyncTask<Integer, Integer, String> {
	private static final String TAG = "MyAsynTaskWithProgress";
	private Dialog pd;
	private Context context;
	private String title;

	/**
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            进度框显示文字
	 */
	public MyAsynTaskWithProgress(Context context, String title) {
		this.context = context;
		this.title = title;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = RequestUtil.createLoadingDialog(context, title, true);
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (this != null
						&& MyAsynTaskWithProgress.this.getStatus() == MyAsynTaskWithProgress.Status.RUNNING) {
					cancel(true); // 如果Task还在运行，则先取消它
				}
			}
		});
		pd.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		pd.dismiss();
	}

	/**
	 * 显示进度条
	 * 
	 * @param context
	 *            环境
	 * @param title
	 *            标题
	 * @param message
	 *            信息
	 * @param indeterminate
	 *            确定性
	 * @param cancelable
	 *            可撤销
	 * @return
	 */
	public ProgressDialog showProgress(Context context, CharSequence title,
			CharSequence message, boolean indeterminate, boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (null != MyAsynTaskWithProgress.this)
					MyAsynTaskWithProgress.this.cancel(true);
			}
		});
		dialog.show();
		return dialog;
	}

}
