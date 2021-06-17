package com.gcapp.tc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 功能：自定义ScrollView
 * 
 * @author lenovo
 * 
 */
public class MyScrollView extends ScrollView {

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		View view = getChildAt(0);
		if (view.getMeasuredHeight() <= getScrollY() + getHeight()) {
			// 触底
			onScrollListener.onBottom();
		} else if (getScrollY() == 0) {
			onScrollListener.onTop();
		} else {
			onScrollListener.onScroll();
		}
	}

	public interface OnMyScrollListener {
		void onBottom();

		void onTop();

		void onScroll();
	}

	private OnMyScrollListener onScrollListener;

	public void setOnScrollListener(OnMyScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
}
