package com.gcapp.tc.view;

import com.gcapp.tc.utils.AppTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

/**
 * 功能：自定义传感器类
 */
public class VibratorView {
	private static Vibrator vibrator;

	public static void init(Context context) {
		SharedPreferences settings = context
				.getSharedPreferences("app_user", 0);
		AppTools.isVibrator = settings.getBoolean("isVibrator", true);
	}

	public static synchronized Vibrator getVibrator(Context context) {
		init(context);
		if (!AppTools.isVibrator) {
			return null;
		}
		if (null == vibrator) {
			vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
		}
		return vibrator;
	}

}
