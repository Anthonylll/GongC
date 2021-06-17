package com.gcapp.tc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * @author anthony
 * @Time 2018/03/12
 * 替代UpdateService，做更新用
 */
public class UpdateVersion {

	private Context 			mContext;
	private ProgressDialog 		progressBar;
	
	@SuppressLint("NewApi") public void downFile(final String url,Context context) { 
		mContext = context;
		//进度条，在下载的时候实时更新进度，提高用户友好度
		progressBar = new ProgressDialog(mContext);
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setTitle("正在下载");
		progressBar.setMessage("请稍候...");
		progressBar.setProgress(0);
		progressBar.setProgressNumberFormat("%1d kb/%2d kb");  
		progressBar.show();
		progressBar.setCancelable(false);
		new Thread() {
			public void run() {        
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					//获取文件大小
					int length = (int) entity.getContentLength();
					//设置进度条的总长度
					progressBar.setMax(length/1024);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"sls_app.apk");
						fileOutputStream = new FileOutputStream(file);
						//这个是缓冲区
                        byte[] buf = new byte[256];   
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {       
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							//实时更新进度
							progressBar.setProgress(process/1024);
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}
	
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			
		};
	};
	
	private void down() {
		handler1.post(new Runnable() {
			public void run() {
				progressBar.cancel();
				update();
			}
		});
	}

	private void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "sls_app.apk")),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
	
}
