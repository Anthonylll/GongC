package com.gcapp.tc.sd.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.gcapp.tc.service.UpdateVersion;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 功能：需要版本更新时通知更新
 * 
 * @author lenovo
 * 
 */
public class UpdateManager {
	private Context mContext;
	private ConfirmDialog dialog;// 提示框
	// 传入的下载地址
	private String apkUrl;
	private Dialog downloadDialog;
	/* 路径 */
	private static final String savePath = "/sdcard/updatedemo/";
	private static final String saveFileName = savePath
			+ "UpdateDemoRelease.apk";
	/* 进度 */
	private ProgressBar mProgress; // 进度
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private boolean interceptFlag = false;
	private UpdateVersion updateVersion;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				// 开始安装APK
				downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            ：上下文对象
	 * @param url
	 *            ：下载新版本的路径
	 */
	public UpdateManager(Context context, String url) {
		this.mContext = context;
		this.apkUrl = url;
	}

	/**
	 * 弹出下载的提示框
	 */
	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	/**
	 * 版本更新的 提示框
	 */
	private void showNoticeDialog() {
		dialog = new ConfirmDialog(mContext, R.style.dialog);
//		AlertDialog.Builder builder = new Builder(mContext);
		dialog.show();
		dialog.setDialogTitle("更新提示");
		dialog.hideCancelBtn(true);
		dialog.setDialogUpdateText("立即更新");
		dialog.setDialogContent("亲，有新版本可以下载啦！");
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (2 == resultCode) {
					
					updateVersion = new UpdateVersion();
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						updateVersion.downFile(AppTools.appobject.getDownLoadUrl(),mContext);
					} else {
						MyToast.getToast(mContext, "SD卡不可用，请插入SD卡");
					}
				}
			}
		});
	}


	/**
	 * 线程下载apk
	 */
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 开始下载
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 读取完毕
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 安装װapk
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
