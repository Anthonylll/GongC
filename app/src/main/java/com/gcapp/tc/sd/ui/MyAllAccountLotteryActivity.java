package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.wheel.widget.TabPageIndicator;
import com.gcapp.tc.R;

/**
 * 功能：账户明细的总类
 */
public class MyAllAccountLotteryActivity extends FragmentActivity implements
		OnClickListener {
	private ViewPager mViewPager;
	private ImageButton btn_back;
	private TabPageIndicator my_all_lottery_indicator;
	private List<View> listViews;
	private View view_all, view_bet, view_win, view_alipay, view_drawmony;
	private LayoutInflater mInflater;
	private AllAccountBetLottery allBet = null;
	private BuyAccountBetLottery buyBet = null;
	private WinAccountBetLottery winBet = null;
	private AlipayAccountBetLottery alipayBet = null;
	private DrawMoneyAccountBetLottery withdrawBet = null;
	private ImageButton imageDateButton;
	private int currentPage = 0;
	private static final String[] TITLES = new String[] { "全部", "购彩", "充值",
			"中奖", "提款" };
	private TextView tvAccount;
	private int year;
	private int month;
	private int day;
	private int year_week;
	private int month_week;
	private int day_week;
	private int year_oneMonth;
	private int month_oneMonth;
	private int day_oneMonth;
	private int year_threeMonth;
	private int month_threeMonth;
	private int day_threeMonth;
	private LinearLayout ll_date;
	private TextView tv_date;
	private PopupWindow popWindow;
	private Button btn_today, btn_week, btn_oneMonth, btn_threeMonth,
			btn_cancel;// 玩法说明，开奖详情，显示遗漏值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_all_account);
		App.activityS.add(this);
		init();
	}

	/**
	 * 数据初始化
	 */
	private void init() {
		currentPage = getIntent().getIntExtra("index", 0);
		ll_date = (LinearLayout) this.findViewById(R.id.ll_date);
		tv_date = (TextView) this.findViewById(R.id.tv_date);
		mViewPager = (ViewPager) this.findViewById(R.id.vp_myViewPager);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		imageDateButton = (ImageButton) findViewById(R.id.funds_date_btn);
		imageDateButton.setVisibility(View.VISIBLE);
		imageDateButton.setOnClickListener(this);
		ll_date.setOnClickListener(this);
		tv_date.setOnClickListener(this);
		initViewPager();

		mViewPager.setAdapter(new MyPagerAdapter());
		my_all_lottery_indicator = (TabPageIndicator) findViewById(R.id.my_all_lottery_indicator);
		my_all_lottery_indicator.setViewPager(mViewPager, currentPage);

		Calendar now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		day = now.get(Calendar.DATE);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -1); // 得到前一周
		year_week = calendar.get(Calendar.YEAR);
		month_week = calendar.get(Calendar.MONTH) + 1;
		day_week = calendar.get(Calendar.DATE);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(calendar2.MONTH, -1); // 得到前一个月
		year_oneMonth = calendar2.get(Calendar.YEAR);
		month_oneMonth = calendar2.get(Calendar.MONTH) + 1;
		day_oneMonth = calendar2.get(Calendar.DATE);

		Calendar calendar3 = Calendar.getInstance();
		calendar3.add(calendar3.MONTH, -3); // 得到前三个月
		year_threeMonth = calendar3.get(Calendar.YEAR);
		month_threeMonth = calendar3.get(Calendar.MONTH) + 1;
		day_threeMonth = calendar3.get(Calendar.DATE);
		tvAccount = (TextView) findViewById(R.id.tv_account);
		tvAccount.setText("账单明细");
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化viewPager的5个选项
	 */
	private void initViewPager() {
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();

		view_all = mInflater.inflate(R.layout.center_all_account_lottery, null);
		view_all.setTag("all");

		view_bet = mInflater.inflate(R.layout.center_all_account_lottery, null);
		view_bet.setTag("buy");

		view_alipay = mInflater.inflate(R.layout.center_all_account_lottery,
				null);
		view_alipay.setTag("alipay");

		view_win = mInflater.inflate(R.layout.center_all_account_lottery, null);
		view_win.setTag("win");

		view_drawmony = mInflater.inflate(R.layout.center_all_account_lottery,
				null);
		view_drawmony.setTag("withdraw");

		listViews.add(view_all);
		listViews.add(view_bet);
		listViews.add(view_alipay);
		listViews.add(view_win);
		listViews.add(view_drawmony);
	}

	/**
	 * 适配器
	 * 
	 * @author lenovo
	 * 
	 */
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return listViews.size();
		}

		/**
		 * 从指定的position创建page
		 * 
		 * @param collection
		 *            ViewPager容器
		 * @param position
		 *            The page position to be instantiated.
		 * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			((ViewPager) collection).addView(listViews.get(position), 0);

			View v = listViews.get(position);
			String tag = (String) v.getTag();
			if ("all".equals(tag)) {
				if (null == allBet) {
					allBet = new AllAccountBetLottery(
							MyAllAccountLotteryActivity.this, v, year_oneMonth,
							month_oneMonth, day_oneMonth);
					allBet.init();
				}
			}
			if ("buy".equals(tag)) {
				if (null == buyBet) {
					buyBet = new BuyAccountBetLottery(
							MyAllAccountLotteryActivity.this, v, year_oneMonth,
							month_oneMonth, day_oneMonth);
					buyBet.init();
				}
			}
			if ("alipay".equals(tag)) {
				if (null == alipayBet) {
					alipayBet = new AlipayAccountBetLottery(
							MyAllAccountLotteryActivity.this, v, year_oneMonth,
							month_oneMonth, day_oneMonth);
					alipayBet.init();
				}
			}

			if ("win".equals(tag)) {
				if (null == winBet) {
					winBet = new WinAccountBetLottery(
							MyAllAccountLotteryActivity.this, v, year_oneMonth,
							month_oneMonth, day_oneMonth);
					winBet.init();
				}
			}

			if ("withdraw".equals(tag)) {
				if (null == withdrawBet) {
					withdrawBet = new DrawMoneyAccountBetLottery(
							MyAllAccountLotteryActivity.this, v, year_oneMonth,
							month_oneMonth, day_oneMonth);
					withdrawBet.init();
				}
			}
			return v;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView(listViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position % TITLES.length];
		}
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.funds_date_btn:
		case R.id.ll_date:
		case R.id.tv_date:
			createPopWindow();
			popWindow.showAtLocation(v, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
					0.5f);
			break;

		default:
			break;
		}
	}

	/**
	 * 创建时间选择器的dialog
	 */
	private void createPopWindow() {
		LayoutInflater inflact = LayoutInflater
				.from(MyAllAccountLotteryActivity.this);
		View view = inflact.inflate(R.layout.pop_item_date, null);
		btn_today = (Button) view.findViewById(R.id.btn_today);
		btn_week = (Button) view.findViewById(R.id.btn_week);
		btn_oneMonth = (Button) view.findViewById(R.id.btn_oneMonth);// 近1月
		btn_threeMonth = (Button) view.findViewById(R.id.btn_threeMonth);// 近3月
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);// 取消
		popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		// 设置之后点击返回键 popwindow 会消失
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
						1.0f);
			}
		});
		// 设置popwindow的消失事件
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
						LotteryUtils.backgroundAlpaha(
								MyAllAccountLotteryActivity.this, 1.0f);
					}
					break;
				}
				return true;
			}
		};

		btn_today.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh(year, month, day);
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
						1.0f);
				tv_date.setText("当天");
			}
		});
		btn_week.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh(year_week, month_week, day_week);
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
						1.0f);
				tv_date.setText("最近一周");
			}
		});

		btn_oneMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh(year_oneMonth, month_oneMonth, day_oneMonth);
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
						1.0f);
				tv_date.setText("最近一月");
			}
		});

		btn_threeMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh(year_threeMonth, month_threeMonth, day_threeMonth);
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
						1.0f);
				tv_date.setText("最近三月");
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(MyAllAccountLotteryActivity.this,
						1.0f);
			}
		});

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					popWindow = null;
				}
				return true;
			}
		});
	}

	/**
	 * 根据日期刷新数据请求
	 * 
	 * @param year
	 *            ：年
	 * @param month
	 *            ：月
	 * @param day
	 *            ：日
	 */
	public void refresh(int year, int month, int day) {
		if (listViews.size() > 0) {
			for (View v : listViews) {
				String tag = (String) v.getTag();
				if (tag.equals("all")) {
					if (null != allBet) {
						allBet.Refresh(year, month, day);
					}
				} else if (tag.equals("buy")) {
					if (null != buyBet) {
						buyBet.Refresh(year, month, day);
					}
				} else if (tag.equals("alipay")) {
					if (null != alipayBet) {
						alipayBet.Refresh(year, month, day);
					}
				} else if (tag.equals("win")) {
					if (null != winBet) {
						winBet.Refresh(year, month, day);
					}
				} else if (tag.equals("withdraw")) {
					if (null != withdrawBet) {
						withdrawBet.Refresh(year, month, day);
					}
				}
			}
		}
	}
}
