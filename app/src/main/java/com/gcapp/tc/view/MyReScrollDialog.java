package com.gcapp.tc.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.gcapp.tc.wheel.widget.OnWheelChangedListener;
import com.gcapp.tc.wheel.widget.OnWheelScrollListener;
import com.gcapp.tc.wheel.widget.WheelAdapter;
import com.gcapp.tc.wheel.widget.WheelView;
import com.gcapp.tc.R;

/**
 * 功能：自定义Dialog
 * 
 * @author lenovo
 * 
 */
public class MyReScrollDialog extends Dialog {

	private Context context;
	private WheelAdapter year_adapter;
	private OnWheelScrollListener scrollListYear;
	private Button btn_ok;
	private Button btn_quit;
	private WheelView wheel_year;
	public int year_currentId = 0;
	public String content;
	private String title;
	public String showContent;
	private android.view.View.OnClickListener clickListener;

	public MyReScrollDialog(Context context, int theme,
			WheelAdapter year_adapter,
			android.view.View.OnClickListener _clickLister, String title) {
		super(context, theme);
		this.context = context;
		this.year_adapter = year_adapter;
		this.clickListener = _clickLister;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_rescroll);
		findView();
		setListener();
	}

	public void initShowContent(String show) {
		showContent = show;
	}

	/** 初始化UI */
	private void findView() {
		wheel_year = (WheelView) this.findViewById(R.id.year_wheel);
		wheel_year.setAdapter(year_adapter);
		btn_ok = (Button) this.findViewById(R.id.funds_btn_ok1);
		btn_quit = (Button) this.findViewById(R.id.funds_btn_no1);
		TextView mLl_fundinfo_title = (TextView) this
				.findViewById(R.id.ll_fundinfo_title);
		wheel_year.setCyclic(true);
		mLl_fundinfo_title.setText(title);
		scrollListYear = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				year_currentId = wheel.getCurrentItem() + 1;
			}
		};

		wheel_year.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (year_adapter.getItem(newValue).length() == 0)
					return;
				showContent = year_adapter.getItem(newValue);

			}
		});
		setCheckItem();
	}

	public void setCheckItem() {
		for (int i = 0; i < year_adapter.getItemsCount(); i++) {
			if (year_adapter.getItem(i).equals(showContent)) {
				wheel_year.setCurrentItem(i);
			}
		}
	}

	/** 绑定监听 */
	private void setListener() {
		btn_ok.setOnClickListener(clickListener);
		btn_quit.setOnClickListener(clickListener);
		wheel_year.addScrollingListener(scrollListYear);
	}
}
