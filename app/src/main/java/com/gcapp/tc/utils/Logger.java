package com.gcapp.tc.utils;

import android.util.Log;

public class Logger {

	private static final int Level = 0; // Debug模式下打印Log日志

	private static final int Model_V = 1;

	private static final int Model_D = 2;

	private static final int Model_I = 3;

	private static final int Model_W = 4;

	private static final int Model_E = 5;

	public static void Log_v(String Tag, String msg) {

		if (Model_V > Level) {

			Log.v(Tag, msg);
		}

	}

	public static void Log_d(String Tag, String msg) {

		if (Model_D > Level) {

			Log.d(Tag, msg);

		}

	}

	public static void Log_i(String Tag, String msg) {

		if (Model_I > Level) {

			Log.i(Tag, msg);

		}

	}

	public static void Log_w(String Tag, String msg) {

		if (Model_W > Level) {

			Log.w(Tag, msg);

		}
	}

	public static void Log_e(String Tag, String msg) {

		if (Model_E > Level) {

			Log.e(Tag, msg);
		}

	}

}
