package com.gcapp.tc.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-9-7 下午1:53:43
 * @version 1.1.3 
 * @Description 简易跑马灯
 */
public class AutoMoveTextView extends TextView{

	public AutoMoveTextView(Context context) {
        super(context);
    }

    public AutoMoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        // DO NOTHING
    }
	
}
