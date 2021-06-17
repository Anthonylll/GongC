package com.gcapp.tc.utils;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.view.MyToast;

/**
 * 手机验证码工具类
 */
public class VerificationCodeUtils {
	private final String TAG = "VerificationCodeUtils";
	private Context context;

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int FINISHED = 2;

	private int status;

	private String opt, auth, info, time, imei, crc; // 格式化后的参数

	private MyAsynTask_check myAsynTask;
	private MyHandler handler;
	private MyTimeTask timeTask;
	private boolean cancell = false;
	private final int ALLTIME = 48;
	private int count = ALLTIME; // 60秒超时
	private final Timer timer = new Timer();
	private int params = 0; // 验证码查询和验证参数
	private String moblie;
	private String code;
	private String msg;// 返回信息

	private VerificationCodeListener verificationCodeListener;

	public VerificationCodeUtils(Context context,
			VerificationCodeListener verificationCodeListener) {
		this.context = context;
		this.verificationCodeListener = verificationCodeListener;
		init();
	}

	private void init() {
		status = START;
		handler = new MyHandler();
		myAsynTask = new MyAsynTask_check();
		timeTask = new MyTimeTask();
	}

	// 获取验证码线程
	class MyAsynTask_check extends AsyncTask<Integer, Integer, String> {
		private ProgressDialog mProgress;

		@Override
		protected void onPreExecute() {
			mProgress = BaseHelper.showProgress(context, null, "请稍等...", true,
					true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			switch (params[0]) {
			case 0: // 查询验证码
				String error;
				opt = "54";
				time = RspBodyBaseBean.getTime();
				imei = RspBodyBaseBean.getIMEI(context);

				String key = MD5.md5(AppTools.MD5_key);
				info = "{\"mobile\":\"" + moblie + "\"}";
				Log.i(TAG, "info ==54>" + info);
				crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
				auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

				String values[] = { opt, auth, info };
				String result = HttpUtils.doPost(AppTools.names, values,
						AppTools.path);

				Log.i(TAG, "result ==>" + result);

				if ("-500".equals(result))
					return result;

				try {
					JSONObject ob = new JSONObject(result);
					error = ob.getString("error");
					msg = ob.getString("msg");
					if ("0".equals(error)) {

						return AppTools.ERROR_SUCCESS + "";
					}
				} catch (JSONException e) {
					Log.w("TAG", "JSONException==>" + e.toString());
					error = "-1";
				}
				return error;

			case 1: // 提交验证验证码
				String error1;
				opt = "55";
				time = RspBodyBaseBean.getTime();
				imei = RspBodyBaseBean.getIMEI(context);

				String key1 = MD5.md5(AppTools.MD5_key);
				info = "{\"mobile\":\"" + moblie + "\",\"code\":\"" + code
						+ "\"}";
				Log.i(TAG, "info ==55>" + info);
				crc = RspBodyBaseBean.getCrc(time, imei, key1, info, "-1");
				auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

				String values1[] = { opt, auth, info };
				String result1 = HttpUtils.doPost(AppTools.names, values1,
						AppTools.path);

				Log.i(TAG, "result ==>" + result1);

				if ("-500".equals(result1))
					return "-1500";

				try {
					JSONObject ob = new JSONObject(result1);
					error1 = ob.getString("error");
					msg = ob.getString("msg");
					if ("0".equals(error1)) {

						return AppTools.ERROR_SUCCESS + "";
					}
				} catch (JSONException e) {
					Log.w("TAG", "JSONException==>" + e.toString());
					error1 = "-11";
				}
				return error1;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			mProgress.dismiss();
			dealResult(params, s);

		}
	}

	private void dealResult(int params, String s) {
		switch (Integer.valueOf(s)) {
		case 0:
			if (params == 0)
				startCount();
			else
				stopCount();
			verificationCodeListener.onCheckComplete(params, s, msg);
			break;
		case -1:
			// MyToast.getToast(context, "获取验证码失败");
			MyToast.getToast(context, msg);
			break;
		case -3:
			// MyToast.getToast(context, "验证码验证失败");
			MyToast.getToast(context, msg);
			break;
		case -500:
			MyToast.getToast(context, "获取验证码超时");
			break;
		case -11:
			// MyToast.getToast(context, "验证码验证失败");
			MyToast.getToast(context, msg);
			break;
		case -1500:
			MyToast.getToast(context, "验证码验证超时");
			break;

		}
	}

	private class MyHandler extends android.os.Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				verificationCodeListener.onCheckProgress(status, msg.arg1);
				break;
			case 1:
				stopCount();
				verificationCodeListener.onCheckProgress(status, -1);

				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 开始计数
	 */
	private void startCount() {
		status = RUNNING;
		timeTask = new MyTimeTask();
		timer.schedule(timeTask, 1000, 1000);
	}

	/**
	 * 停止计数
	 */
	private void stopCount() {
		status = FINISHED;
		timeTask.cancel();
		count = ALLTIME;
	}

	/**
	 * 取消
	 */
	public void cancle() {
		stopCount();
	}

	/**
	 * 恢复
	 */
	public void resume() {
		startCount();
	}

	public int getStatus() {
		return status;
	}

	private class MyTimeTask extends TimerTask {
		@Override
		public void run() {
			Message message = new Message();
			if (status == RUNNING && count > 0) {
				message.what = 0;
				message.arg1 = count--;
				handler.sendMessage(message);
			} else {
				message.what = 1;
				message.arg1 = count = ALLTIME;
				handler.sendMessage(message);
			}
		}
	}

	/**
	 * 开始验证
	 * 
	 * @param param
	 *            0:请求短信验证码 ; 1:检查验证码
	 */

	public void startCheck(int param, String moblie) {

		if (myAsynTask == null
				|| myAsynTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
			return;
		}

		if (param == 0) {
			if (status == RUNNING)
				return;
			this.params = 0;

		} else
			this.params = 1;
		this.moblie = moblie;
		myAsynTask = new MyAsynTask_check();
		myAsynTask.execute(params);
	}

	public void startCheck(int param, String moblie, String code) {
		// if (status==FINISHED){
		// MyToast.getToast(context,"验证码超时");
		// return;
		// }
		this.code = code;
		startCheck(param, moblie);
	}

	public interface VerificationCodeListener {
		/**
		 * 监听计数过程
		 * 
		 * @param progress
		 *            -1：计数结束
		 */
		void onCheckProgress(int status, int progress);

		/**
		 * 返回监听结果
		 * 
		 * @param params
		 *            0:请求短信验证码，1：验证验证码
		 * @param result
		 */
		void onCheckComplete(int params, String result, String msg);
	}
}
