package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.gcapp.tc.fragment.InformationItemFragment;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.wheel.widget.TabPageIndicatorTwo;
import com.gcapp.tc.R;

/**
 * @author anthony
 * @date 2018-4-19 下午2:49:47
 * @version 5.6.10 
 * @Description 资讯
 */
public class InformationActivity extends FragmentActivity implements OnClickListener{

	private final String[] TITLES = new String[] { "热点资讯", "专家推荐", "站点公告" };
	private final int[] NEWTYPES = new int[] { 2, 3, 1 };
	private TabPageIndicatorTwo information_indicator;
	private ViewPager information_viewPager;
	private MyPagerAdapter adapter;
	private ImageButton lottery_btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.information_fragment);
		App.activityS.add(this);
		
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		information_indicator = (TabPageIndicatorTwo)findViewById(R.id.information_fragment_indicator);
		information_viewPager = (ViewPager)findViewById(R.id.information_fragment_viewPager);
		lottery_btn_back = (ImageButton) findViewById(R.id.lottery_btn_back);
	}
	
	private void initListener() {
		lottery_btn_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lottery_btn_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void initData() {
		int postion = getIntent().getIntExtra("index", 0);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		information_viewPager.setAdapter(adapter);
		information_indicator.setViewPager(information_viewPager,postion);
		information_indicator.setTitles(TITLES);
	}
	
	private class MyPagerAdapter extends FragmentPagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return InformationItemFragment.newInstance(arg0, NEWTYPES[arg0]);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position % TITLES.length];
		}

	}

	/**
	 * 返回键监听
	 */
	public void onclickBack() {
		for (Activity activity : App.activityS1) {
			activity.finish();
		}
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onclickBack();
		}
		return super.onKeyDown(keyCode, event);
	}

}
