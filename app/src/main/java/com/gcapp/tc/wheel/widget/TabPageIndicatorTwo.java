package com.gcapp.tc.wheel.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-4-20 下午2:26:31
 * @version 5.5.0
 * @Description
 */
public class TabPageIndicatorTwo extends HorizontalScrollView implements
		ViewPager.OnPageChangeListener {

	private static final int COLOR_TEXT_NORMAL = 0xFF000000;
	private static final int COLOR_INDICATOR_COLOR = 0xFFEB1827;
	private final int[] COLORS = new int[] { COLOR_TEXT_NORMAL, COLOR_TEXT_NORMAL, COLOR_TEXT_NORMAL }; 

	private Context context;
	private int tabWidth;
	private String[] titles;
	private int count;
	private Paint mPaint;
	private float mTranslationX;
	private ViewPager viewPager;
	private int SCREEN_WIDTH;
	private float lineheight = 3.0f;
	private int LENGTH = 3;

	public TabPageIndicatorTwo(Context context) {
		this(context, null);
	}

	public TabPageIndicatorTwo(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TabPageIndicatorTwo(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		mPaint = new Paint();
		mPaint.setColor(COLOR_INDICATOR_COLOR);
		// 底部指示线的宽度
		mPaint.setStrokeWidth(lineheight);
		setHorizontalScrollBarEnabled(false);
		SCREEN_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
	}

	public void setLineheight(float height) {
		this.lineheight = height;
		// 底部指示线的宽度
		mPaint.setStrokeWidth(lineheight);
	}

	public void setViewPager(ViewPager viewPager, int initialPosition) {
		this.viewPager = viewPager;
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(initialPosition);
		COLORS[initialPosition] = COLOR_INDICATOR_COLOR;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
		this.LENGTH = titles.length;
		count = titles.length;
		tabWidth = SCREEN_WIDTH / LENGTH;
		generateTitleView();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		tabWidth = SCREEN_WIDTH / LENGTH;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		canvas.save();
		canvas.translate(mTranslationX, getHeight() - lineheight);
		canvas.drawLine(0, 0, tabWidth, 0, mPaint);
		canvas.restore();
	}

	public void scroll(int position, float offset) {
		mTranslationX = tabWidth * (position + offset);
		scrollTo((int) mTranslationX - (SCREEN_WIDTH - tabWidth) / 2, 0);
		invalidate();
	}

	private void generateTitleView() {
		if (getChildCount() > 0)
			this.removeAllViews();
		count = titles.length;

		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(count
				* tabWidth, LinearLayout.LayoutParams.MATCH_PARENT));
		for (int i = 0; i < count; i++) {
			final TextView tv = new TextView(getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					tabWidth, LinearLayout.LayoutParams.MATCH_PARENT);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(COLOR_TEXT_NORMAL);
			tv.setText(titles[i]);
			// 字体大小
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tv.setLayoutParams(lp);
			final int finalI = i;
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (viewPager != null) {
						viewPager.setCurrentItem(finalI);
//						generateTitleView();
					}
				}
			});
			linearLayout.addView(tv);
		}
		addView(linearLayout);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		scroll(position, positionOffset);
//		generateTitleView();
	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < count; i++) {
			if(i == position) {
				COLORS[i] = COLOR_INDICATOR_COLOR;
			}else{
				COLORS[i] = COLOR_TEXT_NORMAL;
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

}
