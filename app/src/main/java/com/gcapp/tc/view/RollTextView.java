package com.gcapp.tc.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 功能：实现跑马灯自定义TextVew
 * 
 * @author Administrator
 */
public class RollTextView extends TextView {

	public RollTextView(Context context) {
		super(context);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	public RollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isFocused() {
		return true;

	}
}
