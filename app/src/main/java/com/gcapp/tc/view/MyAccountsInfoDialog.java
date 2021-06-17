package com.gcapp.tc.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gcapp.tc.sd.ui.MyAllAccountLotteryActivity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.wheel.widget.OnWheelScrollListener;
import com.gcapp.tc.wheel.widget.WheelAdapter;
import com.gcapp.tc.wheel.widget.WheelView;
import com.gcapp.tc.R;

/**
 * 功能：自定义日期对话框 用于账户明细
 */
public class MyAccountsInfoDialog extends Dialog implements
		View.OnClickListener {

	private Context context;

	private WheelAdapter year_adapter, month_adapter;
	private OnWheelScrollListener scrollListYear, scrollListMonth;

	private Button btn_ok, btn_quit;

	private WheelView wheel_year, wheel_month;
	private MyAllAccountLotteryActivity fActivity;

	public int year_currentId = 0, month_currentId = 0;

	public String content, content2;

	private String y, m;

	public MyAccountsInfoDialog(Context context, int theme,
			WheelAdapter year_adapter, WheelAdapter month_adapter, int y, int m) {
		super(context, theme);
		this.context = context;
		this.year_adapter = year_adapter;
		this.month_adapter = month_adapter;
		fActivity = (MyAllAccountLotteryActivity) context;
		initDate(y, m);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fundinfo_dialog);
		findView();
		setListener();
	}

	private void initDate(int y, int m) {
		this.y = y + "";
		this.m = m + "";
	}

	/** 初始化UI */
	private void findView() {
		wheel_year = (WheelView) this.findViewById(R.id.year_wheel);
		wheel_year.setDEF_VISIBLE_ITEMS(3);
		wheel_year.setAdapter(year_adapter);
		wheel_month = (WheelView) this.findViewById(R.id.month_wheel);
		wheel_month.setDEF_VISIBLE_ITEMS(3);
		wheel_month.setAdapter(month_adapter);

		btn_ok = (Button) this.findViewById(R.id.funds_btn_ok);
		btn_quit = (Button) this.findViewById(R.id.funds_btn_no);

		wheel_year.setCyclic(true);
		wheel_month.setCyclic(true);

		scrollListYear = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				year_currentId = wheel.getCurrentItem();
			}
		};

		scrollListMonth = new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				month_currentId = wheel.getCurrentItem();
			}
		};
		setCheckItem();
	}

	public void setCheckItem() {
		for (int i = 0; i < year_adapter.getItemsCount(); i++) {
			if (year_adapter.getItem(i).equals(this.y)) {
				year_currentId = i;
				wheel_year.setCurrentItem(i);
			}
		}
		for (int i = 0; i < month_adapter.getItemsCount(); i++) {
			if (month_adapter.getItem(i).equals(m)) {
				month_currentId = i;
				wheel_month.setCurrentItem(i);
			}
		}
	}

	/** 绑定监听 */
	private void setListener() {
		btn_ok.setOnClickListener(this);
		btn_quit.setOnClickListener(this);

		wheel_year.addScrollingListener(scrollListYear);
		wheel_month.addScrollingListener(scrollListMonth);
	}

	/** 按钮点击 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.funds_btn_ok:
			MyAccountsInfoDialog.this.dismiss();

			if (year_currentId != -1)
				wheel_year.setCurrentItem(year_currentId);
			if (month_currentId != -1)
				wheel_month.setCurrentItem(month_currentId);

			content = year_adapter.getItem(year_currentId);
			content2 = month_adapter.getItem(month_currentId);
			int month = Integer.parseInt(content2);
			int year = Integer.parseInt(content);
			int day = AppTools.getLastDay(year, month);
			fActivity.refresh(year, month, day);
			break;
		case R.id.funds_btn_no:
			MyAccountsInfoDialog.this.dismiss();
			break;
		}
	}
}
