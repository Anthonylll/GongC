package com.gcapp.tc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 功能：自定义GridView
 * 
 * @author lenovo
 * 
 */
public class AllGridView extends GridView {

	public AllGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AllGridView(Context context) {
		super(context);
	}

	public AllGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
