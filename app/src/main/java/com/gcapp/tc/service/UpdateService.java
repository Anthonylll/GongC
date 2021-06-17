package com.gcapp.tc.service;

/**
 * 版本更新service类
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.gcapp.tc.sd.ui.MainActivity;
import com.gcapp.tc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateService extends Service {
	private static final String TAG = "TAG";

	private static final String savePath = "/sdcard/updatedemo/";

	private static final String saveFileName = savePath
			+ "UpdateDemoRelease.apk";
	// 标题
	private int titleId = 0;

	// 文件存储
	private File updateDir = null;
	private File updateFile = null;
	// 下载状态
	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAIL = 1;
	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	// 通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;
	private String downloadURL;
	/**
	 * 创建通知栏
	 */
	RemoteViews contentView;
	// 这样的下载代码很多，我就不做过多的说明
	int downloadCount = 0;
	int currentSize = 0;
	long totalSize = 0;
	int updateTotalSize = 0;

	// 在onStartCommand()方法中准备相关的下载工作：
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 获取传值
		titleId = intent.getIntExtra("titleId", 0);
		downloadURL = intent.getStringExtra("downloadURL");
		// 创建文件
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(),
					saveFileName);
			updateFile = new File(updateDir.getPath(), getResources()
					.getString(titleId) + ".apk");
		}

		this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.updateNotification = new Notification();

		// 设置下载过程中，点击通知栏，回到主界面
		updateIntent = new Intent(this, MainActivity.class);
		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
				0);
		// 设置通知栏显示内容
		updateNotification.icon = R.drawable.main_logo;
		updateNotification.tickerText = "开始下载";
		updateNotification.setLatestEventInfo(this,
				getResources().getString(titleId), "0%", updatePendingIntent);
		// 发出通知
		updateNotificationManager.notify(0, updateNotification);

		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		new Thread(new updateRunnable()).start();// 这个是下载的重点，是下载的过程

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// 点击安装PendingIntent
				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");

				updatePendingIntent = PendingIntent.getActivity(
						UpdateService.this, 0, installIntent, 0);
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
				updateNotification.setLatestEventInfo(UpdateService.this,
						getResources().getString(titleId), "下载完成,点击安装。",
						updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);

				// 停止服务
				stopService(updateIntent);
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				updateNotification.setLatestEventInfo(UpdateService.this,
						getResources().getString(titleId), "下载完成,点击安装。",
						updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);
				break;
			default:
				stopService(updateIntent);
				break;
			}
		}
	};

	public long downloadUpdateFile(String downloadUrl, File saveFile)
			throws Exception {

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(downloadUrl);
			// URL url = new
			// URL("http://192.168.13.35:8080/ztzq_web/upload/main-trunk5.2.9.apk");
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			httpConnection.setRequestProperty("Accept-Encoding", "identity");
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("fail!");
			}
			Log.i(TAG, "updateTotalSize--->" + updateTotalSize);
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[4096];
			int readsize;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
				// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
				Log.i(TAG, "downloadCount--->" + downloadCount);
				Log.i(TAG, "totalSize--->" + totalSize);
				Log.i(TAG, "(int) (totalSize * 100 / updateTotalSize) - 10--->"
						+ ((int) (totalSize * 100 / updateTotalSize) - 10));
				if ((downloadCount == 0)
						|| (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
					downloadCount += 10;
					// updateNotification.setLatestEventInfo(UpdateService.this,
					// "正在下载", (int) totalSize * 100 / updateTotalSize
					// + "%", updatePendingIntent);

					/***
					 * 在这里我们用自定的view来显示Notification
					 */
					updateNotification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
					updateNotification.contentView = new RemoteViews(
							getPackageName(), R.layout.notification_item);
					int progress = (int) totalSize * 100 / updateTotalSize;
					if (progress > 100)
						progress = 100;
					updateNotification.contentView.setTextViewText(
							R.id.notificationTitle, progress + "%");
					updateNotification.contentView.setProgressBar(
							R.id.notificationProgress, 100, downloadCount,
							false);

					updateNotificationManager.notify(0, updateNotification);
				}
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	class updateRunnable implements Runnable {
		Message message = updateHandler.obtainMessage();

		public void run() {
			message.what = DOWNLOAD_COMPLETE;

			try {
				// 增加权限<USES-PERMISSION
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				// 增加权限<USES-PERMISSION
				// android:name="android.permission.INTERNET">;
				long downloadSize = downloadUpdateFile(downloadURL, updateFile);
				if (downloadSize > 0) {
					// 下载成功
					updateHandler.sendMessage(message);
					clearAll();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				message.what = DOWNLOAD_FAIL;
				// 下载失败
				updateHandler.sendMessage(message);
			}
		}
	}

	public void clearAll() {
		downloadCount = 0;
		currentSize = 0;
		totalSize = 0;
		updateTotalSize = 0;
	}
}
