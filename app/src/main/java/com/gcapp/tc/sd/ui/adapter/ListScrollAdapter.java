package com.gcapp.tc.sd.ui.adapter;

import com.gcapp.tc.wheel.widget.WheelAdapter;

/**
 * 功能：滑动控件adapter
 * 
 * @author lenovo
 * 
 */
public class ListScrollAdapter implements WheelAdapter {
	private static final String TAG = "ListScrollAdapter";
	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;
	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;
	// Values
	private int minValue;
	private int maxValue;
	// format
	private String format;
	private String[] showBody;

	/**
	 * Default constructor
	 */
	public ListScrollAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 */
	public ListScrollAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, (String[]) null);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 * @param format
	 *            the format string
	 */
	public ListScrollAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *            最小值
	 * @param maxValue
	 *            最大值
	 * @param showBody
	 *            显示内容
	 */
	public ListScrollAdapter(int minValue, int maxValue, String[] showBody) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.showBody = showBody;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount() + 1) {
			int value = minValue + index;
			return showBody != null ? showBody[index % showBody.length]
					: Integer.toString(value);

		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}

	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}
