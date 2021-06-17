package com.gcapp.tc.viewpager;

import java.lang.reflect.Field;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

public class AutoLoopViewPager extends ViewPager {
	public static final int DEFAULT_INTERVAL = 5000;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public static final int SLIDE_BORDER_MODE_NONE = 0;
	public static final int SLIDE_BORDER_MODE_CYCLE = 1;
	public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;

	private long interval = DEFAULT_INTERVAL;
	private int direction = RIGHT;
	private boolean isCycle = true;
	private boolean stopScrollWhenTouch = true;
	private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
	private boolean isBorderAnimation = true;
	private double autoScrollFactor = 1.0;
	private double swipeScrollFactor = 1.0;

	private Handler handler;
	private boolean isAutoScroll = false;
	private boolean isStopByTouch = false;
	private float touchX = 0f, downX = 0f;
	private CustomDurationScroller scroller = null;

	public static final int SCROLL_WHAT = 0;
	private static final boolean DEFAULT_BOUNDARY_CASHING = false;

	OnPageChangeListener mOuterPageChangeListener;
	private LoopPagerAdapterWrapper mAdapter;
	private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;

	public static int toRealPosition(int position, int count) {
		position = position - 1;
		if (position < 0) {
			position += count;
		} else {
			position = position % count;
		}
		return position;
	}

	public void setBoundaryCaching(boolean flag) {
		mBoundaryCaching = flag;
		if (mAdapter != null) {
			mAdapter.setBoundaryCaching(flag);
		}
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		mAdapter = new LoopPagerAdapterWrapper(adapter);
		mAdapter.setBoundaryCaching(mBoundaryCaching);
		super.setAdapter(mAdapter);
		setCurrentItem(0, false);
	}

	/**
	 * 返回 表面的adapter
	 */
	@Override
	public PagerAdapter getAdapter() {
		return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
	}

	/**
	 * 获得当前表位置
	 */
	@Override
	public int getCurrentItem() {
		return mAdapter != null ? mAdapter.toRealPosition(super
				.getCurrentItem()) : 0;
	}

	/**
	 * 设置当前表位置
	 * 
	 * @param item
	 *            表位置
	 */
	public void setCurrentItem(int item, boolean smoothScroll) {
		if (mAdapter.getCount() > 1) {
			int realItem = mAdapter.toInnerPosition(item);
			super.setCurrentItem(realItem, smoothScroll);
		}
	}

	@Override
	public void setCurrentItem(int item) {
		if (getCurrentItem() != item) {
			setCurrentItem(item, true);
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mOuterPageChangeListener = listener;
	};

	public AutoLoopViewPager(Context context) {
		super(context);
		init();
		initScrollfunction();
	}

	public AutoLoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		initScrollfunction();
	}

	private void init() {
		super.setOnPageChangeListener(onPageChangeListener);
	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		private float mPreviousOffset = -1;
		private float mPreviousPosition = -1;

		@Override
		public void onPageSelected(int position) {

			int realPosition = mAdapter.toRealPosition(position);
			if (mPreviousPosition != realPosition) {
				mPreviousPosition = realPosition;
				if (mOuterPageChangeListener != null) {
					mOuterPageChangeListener.onPageSelected(realPosition);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			int realPosition = position;
			if (mAdapter != null) {
				realPosition = mAdapter.toRealPosition(position);

				if (positionOffset == 0
						&& mPreviousOffset == 0
						&& (position == 0 || position == mAdapter.getCount() - 1)) {
					setCurrentItem(realPosition, false);
				}
			}

			mPreviousOffset = positionOffset;
			if (mOuterPageChangeListener != null) {
				if (realPosition != mAdapter.getRealCount() - 1) {
					mOuterPageChangeListener.onPageScrolled(realPosition,
							positionOffset, positionOffsetPixels);
				} else {
					if (positionOffset > .5) {
						mOuterPageChangeListener.onPageScrolled(0, 0, 0);
					} else {
						mOuterPageChangeListener.onPageScrolled(realPosition,
								0, 0);
					}
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (mAdapter != null) {
				int position = AutoLoopViewPager.super.getCurrentItem();
				int realPosition = mAdapter.toRealPosition(position);
				if (state == ViewPager.SCROLL_STATE_IDLE
						&& (position == 0 || position == mAdapter.getCount() - 1)) {
					setCurrentItem(realPosition, false);
				}
			}
			if (mOuterPageChangeListener != null) {
				mOuterPageChangeListener.onPageScrollStateChanged(state);
			}
		}
	};

	// ////////////////////// auto scroll View
	private void initScrollfunction() {
		handler = new MyHandler();
		setViewPagerScroller();
	}

	/**
	 * start auto scroll, first scroll delay time is {@link #getInterval()}
	 */
	public void startAutoScroll() {
		isAutoScroll = true;
		sendScrollMessage(interval);
	}

	/**
	 * start auto scroll
	 * 
	 * @param delayTimeInMills
	 *            first scroll delay time
	 */
	public void startAutoScroll(int delayTimeInMills) {
		isAutoScroll = true;
		sendScrollMessage(delayTimeInMills);
	}

	/**
	 * stop auto scroll
	 */
	public void stopAutoScroll() {
		isAutoScroll = false;
		handler.removeMessages(SCROLL_WHAT);
	}

	/**
	 * set the factor by which the duration of sliding animation will change
	 * while swiping
	 */
	public void setSwipeScrollDurationFactor(double scrollFactor) {
		swipeScrollFactor = scrollFactor;
	}

	/**
	 * set the factor by which the duration of sliding animation will change
	 * while auto scrolling
	 */
	public void setAutoScrollDurationFactor(double scrollFactor) {
		autoScrollFactor = scrollFactor;
	}

	private void sendScrollMessage(long delayTimeInMills) {
		/** remove messages before, keeps one message is running at most **/
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}

	/**
	 * set ViewPager scroller to change animation duration when sliding
	 */
	private void setViewPagerScroller() {
		try {
			Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
			scrollerField.setAccessible(true);
			Field interpolatorField = ViewPager.class
					.getDeclaredField("sInterpolator");
			interpolatorField.setAccessible(true);

			scroller = new CustomDurationScroller(getContext(),
					(Interpolator) interpolatorField.get(null));
			scrollerField.set(this, scroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * scroll only once
	 */
	public void scrollOnce() {
		PagerAdapter adapter = getAdapter();
		int currentItem = getCurrentItem();

		int totalCount;
		if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
			return;
		}

		int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;

		setCurrentItem(nextItem, isBorderAnimation);
	}

	/**
	 * <ul>
	 * if stopScrollWhenTouch is true
	 * <li>if event is down, stop auto scroll.</li>
	 * <li>if event is up, start auto scroll again.</li>
	 * </ul>
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);

		if (stopScrollWhenTouch) {
			if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
				isStopByTouch = true;
				stopAutoScroll();
			} else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
				startAutoScroll();
			}
		}
		// if(action == MotionEvent.ACTION_UP)
		// getParent().requestDisallowInterceptTouchEvent(true);
		// if(action == MotionEvent.ACTION_DOWN)
		// getParent().requestDisallowInterceptTouchEvent(false);
		return super.dispatchTouchEvent(ev);
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case SCROLL_WHAT:
				scroller.setScrollDurationFactor(autoScrollFactor);
				scrollOnce();
				scroller.setScrollDurationFactor(swipeScrollFactor);
				sendScrollMessage(interval);
			default:
				break;
			}
		}
	}

	/**
	 * get auto scroll time in milliseconds, default is
	 * {@link #DEFAULT_INTERVAL}
	 * 
	 * @return the interval
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * set auto scroll time in milliseconds, default is
	 * {@link #DEFAULT_INTERVAL}
	 * 
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * get auto scroll direction
	 * 
	 * @return {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
	 */
	public int getDirection() {
		return (direction == LEFT) ? LEFT : RIGHT;
	}

	/**
	 * set auto scroll direction
	 * 
	 * @param direction
	 *            {@link #LEFT} or {@link #RIGHT}, default is {@link #RIGHT}
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * whether automatic cycle when auto scroll reaching the last or first item,
	 * default is true
	 * 
	 * @return the isCycle
	 */
	public boolean isCycle() {
		return isCycle;
	}

	/**
	 * set whether automatic cycle when auto scroll reaching the last or first
	 * item, default is true
	 * 
	 * @param isCycle
	 *            the isCycle to set
	 */
	public void setCycle(boolean isCycle) {
		this.isCycle = isCycle;
	}

	/**
	 * whether stop auto scroll when touching, default is true
	 * 
	 * @return the stopScrollWhenTouch
	 */
	public boolean isStopScrollWhenTouch() {
		return stopScrollWhenTouch;
	}

	/**
	 * set whether stop auto scroll when touching, default is true
	 * 
	 * @param stopScrollWhenTouch
	 */
	public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
		this.stopScrollWhenTouch = stopScrollWhenTouch;
	}

	/**
	 * get how to process when sliding at the last or first item
	 * 
	 * @return the slideBorderMode {@link #SLIDE_BORDER_MODE_NONE},
	 *         {@link #SLIDE_BORDER_MODE_TO_PARENT},
	 *         {@link #SLIDE_BORDER_MODE_CYCLE}, default is
	 *         {@link #SLIDE_BORDER_MODE_NONE}
	 */
	public int getSlideBorderMode() {
		return slideBorderMode;
	}

	/**
	 * set how to process when sliding at the last or first item
	 * 
	 * @param slideBorderMode
	 *            {@link #SLIDE_BORDER_MODE_NONE},
	 *            {@link #SLIDE_BORDER_MODE_TO_PARENT},
	 *            {@link #SLIDE_BORDER_MODE_CYCLE}, default is
	 *            {@link #SLIDE_BORDER_MODE_NONE}
	 */
	public void setSlideBorderMode(int slideBorderMode) {
		this.slideBorderMode = slideBorderMode;
	}

	/**
	 * whether animating when auto scroll at the last or first item, default is
	 * true
	 * 
	 * @return
	 */
	public boolean isBorderAnimation() {
		return isBorderAnimation;
	}

	/**
	 * set whether animating when auto scroll at the last or first item, default
	 * is true
	 * 
	 * @param isBorderAnimation
	 */
	public void setBorderAnimation(boolean isBorderAnimation) {
		this.isBorderAnimation = isBorderAnimation;
	}

}
