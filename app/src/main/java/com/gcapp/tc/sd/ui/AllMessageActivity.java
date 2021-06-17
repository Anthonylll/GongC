package com.gcapp.tc.sd.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.gcapp.tc.wheel.widget.TabPageIndicator;
import com.gcapp.tc.R;

/**
 * 消息中心
 * 
 * @author lenovo
 * 
 */
public class AllMessageActivity extends FragmentActivity implements
		View.OnClickListener,
		AllMessageItemFragment.OnFragmentInteractionListener {
	private final String[] TITLES = { "系统消息", "推送消息" };
	private final int[] NEWTYPES = { 2, 1 };
	private ImageButton btn_back;
	private TabPageIndicator all_message_indicator;
	private ViewPager all_message_viewPager;
	private MyPagerAdapter myPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_message);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		all_message_indicator = (TabPageIndicator) findViewById(R.id.all_message_indicator);
		all_message_viewPager = (ViewPager) findViewById(R.id.all_message_viewPager);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		all_message_viewPager.setAdapter(myPagerAdapter);
		all_message_indicator.setViewPager(all_message_viewPager, 0);
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
	}

	/**
	 * 自定义菜单布局
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_all_message, menu);
		return true;
	}

	/**
	 * 菜单按钮点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 点击监听事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}

	/**
	 * 系统消息和推送消息的adapter
	 * 
	 * @author lenovo
	 * 
	 */
	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return AllMessageItemFragment.newInstance(NEWTYPES[arg0]);
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
}
