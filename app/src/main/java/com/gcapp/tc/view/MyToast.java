package com.gcapp.tc.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 功能：公用的Toast类
 * 
 * @author lenovo
 * 
 */
public class MyToast {
	private static Toast toast = null;

	@SuppressLint("ShowToast")
	public static void getToast(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(msg);
		}
		showMyToast(toast, 2000);
	}

	public static void showMyToast(final Toast toast, final int cnt) {
				toast.show();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
			}
		}, cnt);
	}
}
