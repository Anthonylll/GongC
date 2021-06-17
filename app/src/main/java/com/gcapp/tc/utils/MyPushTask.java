package com.gcapp.tc.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.protocol.RspBodyBaseBean;

/**
 * 提交推送返回的userId，channeId等 Created by Mike on 2015/1/28.
 */
public class MyPushTask {
	private static MyPushTask myPushTask;
	private Context context;

	public static MyPushTask newInstances(Context context) {
		if (myPushTask == null)
			myPushTask = new MyPushTask(context);
		return myPushTask;
	}

	public MyPushTask(Context context) {
		this.context = context;
	}

	/** 开启提交线程 */
	public void commit() {
		if (context != null) {
			new MyAsynTask().execute();
		}
	}

	// 重置密码请求
	private class MyAsynTask extends AsyncTask<Integer, Integer, String> {
		private String opt, auth, info, time, imei, crc; // 格式化后的参数

		@Override
		protected String doInBackground(Integer... params) {
			String error;
			opt = "58";
			time = RspBodyBaseBean.getTime();
			imei = RspBodyBaseBean.getIMEI(context);
			String key = MD5.md5(AppTools.MD5_key);

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"app_user", 0);
			String isPushKJ = sharedPreferences.getString("isPushKJ", "1");
			// String isPushZJ = sharedPreferences.getString("isPushZJ ", "1");

			Editor edit = sharedPreferences.edit();
			edit.putString("isPushZJ", AppTools.isPushZJ);
			edit.commit();
			SharedPreferences sharedPreferences1 = context
					.getSharedPreferences("app_user", 0);
			String isPushZJ = sharedPreferences1.getString("isPushZJ", "1");
			// Log.i("push之前的中奖推送", isPushZJ);

			String uid = AppTools.user == null ? "-1" : AppTools.user.getUid();

			info = "{\"UserId\":\"" + uid + "\",\"ClientUserId\":\""
					+ AppTools.push_userId + "\",\"ChannelId\":\""
					+ AppTools.push_channelId + "\",\"DeviceType\":\""
					+ AppTools.push_DeviceType + "\",\"Status\":\""
					+ AppTools.Status + "\",\"IsOpen\":\"" + isPushKJ
					+ "\",\"IsWin\":\"" + isPushZJ + "\"}";
			Log.i("x", "提交pushID  info---------" + info);
			crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
			auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
			String values[] = { opt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);

			Log.i("x", "提交pushID  result----" + result);

			if ("-500".equals(result))
				return result;
			try {
				JSONObject ob = new JSONObject(result);
				error = ob.getString("error");
				if ("0".equals(error)) {
					Log.i("推送任务。。", "success");
					return AppTools.ERROR_SUCCESS + "";
				}
			} catch (JSONException e) {
				Log.w("TAG", "JSONException==>" + e.toString());
				error = "-1";
			}

			return error;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Log.i("返回推送参数", "error ==>" + s);
		}
	}

}
