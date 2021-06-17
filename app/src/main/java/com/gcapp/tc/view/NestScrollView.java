package com.gcapp.tc.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 功能：自定义ScrollView
 * 
 * @author lenovo
 * 
 */
public class NestScrollView extends ScrollView {

	private static long curDate1;
	private static long curDate2;

	public NestScrollView(Context context) {
		super(context);
	}

	public NestScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NestScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private float xDistance, yDistance;
	private float xLast, yLast;

	// 判断是否是长按
	private boolean isLongPressed(long lastDownTime, long thisEventTime,
			long longPressTime) {
		long diff = thisEventTime - lastDownTime;

		if (diff >= longPressTime) {
			return true;
		}
		return false;

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Date curDates = new Date(System.currentTimeMillis());// 获取当前时间
			curDate1 = curDates.getTime();

			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			Date curDates1 = new Date(System.currentTimeMillis());// 获取当前时间
			curDate2 = curDates1.getTime();

			final float curX = ev.getX();
			final float curY = ev.getY();
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			Boolean mIsLongPressed = isLongPressed(curDate1, curDate2, 200);
			if (mIsLongPressed) {
				return false;
			}
			if (xDistance > yDistance) {
				return false;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

}
