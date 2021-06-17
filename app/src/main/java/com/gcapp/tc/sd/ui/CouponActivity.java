package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.gcapp.tc.utils.App;
import com.gcapp.tc.wheel.widget.TabPageIndicatorTwo;
import com.gcapp.tc.R;

/**
 * @author anthony
 * @date 2018-7-19 下午2:59:38
 * @version 1.0.5 
 * @Description 我的优惠券
 */
public class CouponActivity extends FragmentActivity implements OnClickListener{

	private Context mContext = CouponActivity.this;
	private static final String[] CouponTITLES = new String[] { "未使用","已过期"};
	private ImageButton btn_back;
	private TabPageIndicatorTwo coupon_fragment_indicator;
	private ViewPager coupon_fragment_viewPager;
	private List<View> couponListViews;
	private View view_unused,view_expired;
	private LayoutInflater mInflater;
	private CouponPackage couponPackage; 
	private int currentPage = 0;
	private String consumptionMoney = "-1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coupon);
		App.activityS.add(this);

		// 1、初始化视图-
		initView();
		// 2、初始化监听事件
		initListener();
		// 3、初始化数据
		initData();
	}

	private void initView() {
		coupon_fragment_viewPager = (ViewPager) this.findViewById(R.id.coupon_fragment_viewPager);
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		initViewPager();
		
		coupon_fragment_viewPager.setAdapter(new MyPagerAdapter());
		coupon_fragment_indicator = (TabPageIndicatorTwo) findViewById(R.id.coupon_fragment_indicator);
		coupon_fragment_indicator.setViewPager(coupon_fragment_viewPager, currentPage);
		coupon_fragment_indicator.setTitles(CouponTITLES);
	}

	private void initListener() {
		btn_back.setOnClickListener(this);
	}

	private void initData() {
		Intent intent = getIntent();
		if(intent.getStringExtra("money") != null) {
			consumptionMoney = intent.getStringExtra("money");
		}else{
			consumptionMoney = "-1";
		}
	}
	
	/**
	 * 初始化viewPager的5个选项
	 */
	private void initViewPager() {
		couponListViews = new ArrayList<View>();
		mInflater = getLayoutInflater();

		view_unused = mInflater.inflate(R.layout.coupon_package_layout, null);
		view_unused.setTag("unused");

		view_expired = mInflater.inflate(R.layout.coupon_package_layout, null);
		view_expired.setTag("expired");

		couponListViews.add(view_unused);
		couponListViews.add(view_expired);
	}
	
	/**
	 * pager的适配器类
	 */
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return couponListViews.size();
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
			((ViewPager) collection).addView(couponListViews.get(position), 0);

			View v = couponListViews.get(position);
			String tag = (String) v.getTag();
			// 接口问题导致ViewPager显示混乱。。。
			if ("unused".equals(tag)) {
				if (null == couponPackage) {
					couponPackage = new CouponPackage(mContext,CouponActivity.this, v,"0",consumptionMoney);
					couponPackage.init();
				}
			}
			
			if ("expired".equals(tag)) {
				if (null == couponPackage) {
					couponPackage = new CouponPackage(mContext,CouponActivity.this, v,"1",consumptionMoney);
					couponPackage.init();
				}else{
					couponPackage = null;
					couponPackage = new CouponPackage(mContext,CouponActivity.this, v,"1",consumptionMoney);
					couponPackage.init();
				}
			}
			return v;
		}
		
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView(couponListViews.get(position));
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
			return CouponTITLES[position % CouponTITLES.length];
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 返回键监听
	 */
	public void onclickBack() {
//		for (Activity activity : App.activityS1) {
//			activity.finish();
//		}
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			onclickBack();
//		}
		return super.onKeyDown(keyCode, event);
	}
	
}
