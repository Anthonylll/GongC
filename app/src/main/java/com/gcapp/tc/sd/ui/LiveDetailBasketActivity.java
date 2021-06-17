package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import com.gcapp.tc.utils.App;
import com.gcapp.tc.wheel.widget.TabPageIndicatorTwo;
import com.gcapp.tc.R;

import android.app.Activity;
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
import android.widget.TextView;

/**
 * @author dm
 * @date 2019-7-5 下午3:15:40
 * @version 5.5.0 
 * @Description 
 */
public class LiveDetailBasketActivity extends FragmentActivity implements OnClickListener{

	private Context mContext = LiveDetailBasketActivity.this;
	private ViewPager live_fragment_viewPager;
	private ImageButton btn_back;
	private TextView detail_host_team,detail_guest_team;
	private TextView league_text,ranking_host_team,ranking_guest_team;
	private TabPageIndicatorTwo live_fragment_indicator;
	private List<View> listViews;
	private View view_standings, view_odds, view_integral;

	private LayoutInflater mInflater;
	private LiveStandings liveStandings = null;
	private LiveDetailOdds liveOdds = null;
	private LiveIntegralBasket liveIntegral = null;
	private int currentPage = 0;
	private static final String[] TITLES = new String[] { "战绩", "赔率", "积分"};
	private int matchId;
	private String opt="1002";
	private String hostTeam,guestTeam;
	private String league,hRanking,gRanking;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_detail);
		App.activityS.add(this);
		init();
		// 1、初始化视图
		initView();
		// 2、初始化监听事件
		initListener();
		// 3、初始化数据
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
//		currentPage = getIntent().getIntExtra("index", 0);
		live_fragment_viewPager = (ViewPager) this.findViewById(R.id.live_fragment_viewPager);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		detail_host_team = (TextView) findViewById(R.id.detail_host_team);
		detail_guest_team = (TextView) findViewById(R.id.detail_guest_team);
		league_text = (TextView) findViewById(R.id.league_text);
		ranking_host_team = (TextView) findViewById(R.id.ranking_host_team);
		ranking_guest_team = (TextView) findViewById(R.id.ranking_guest_team);
		initViewPager();
		
		live_fragment_viewPager.setAdapter(new MyPagerAdapter());
		live_fragment_indicator = (TabPageIndicatorTwo) findViewById(R.id.live_fragment_indicator);
		live_fragment_indicator.setViewPager(live_fragment_viewPager, currentPage);
		live_fragment_indicator.setTitles(TITLES);
	}
	
	private void initView() {
		
	}

	private void initListener() {
		btn_back.setOnClickListener(this);
	}

	private void initData() {
		Intent intent = getIntent();
		matchId = intent.getIntExtra("matchId", 0);
		opt = intent.getStringExtra("opt");
		hostTeam = intent.getStringExtra("hostTeam");
		detail_host_team.setText(hostTeam);
		guestTeam = intent.getStringExtra("guestTeam");
		detail_guest_team.setText(guestTeam);
		league = intent.getStringExtra("league");
		league_text.setText(league);
		hRanking = intent.getStringExtra("hRanking");
		gRanking = intent.getStringExtra("gRanking");
		
		if(opt.equals("1002")){
			if(!hRanking.equals("") & hRanking != null) {
				ranking_host_team.setText("联赛排名："+hRanking);
				ranking_guest_team.setText("联赛排名："+gRanking);
			}else{
				ranking_host_team.setText("联赛排名：暂无");
				ranking_guest_team.setText("联赛排名：暂无");
			}
		}else{
//			String hArea = intent.getStringExtra("hArea");
//			String gArea = intent.getStringExtra("gArea");
			if(!gRanking.equals("")) {
				ranking_host_team.setText(hRanking);
				ranking_guest_team.setText(gRanking);
			}else{
				ranking_host_team.setText("排名：暂无");
				ranking_guest_team.setText("排名：暂无");
			}
		}
	}

	/**
	 * 初始化viewPager的5个选项
	 */
	private void initViewPager() {
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();

		view_standings = mInflater.inflate(R.layout.live_standings_layout, null);
		view_standings.setTag("standings");

		view_odds = mInflater.inflate(R.layout.live_odds_layout, null);
		view_odds.setTag("odds");

		view_integral = mInflater.inflate(R.layout.live_integral_basketball_layout, null);
		view_integral.setTag("integral");

		listViews.add(view_standings);
		listViews.add(view_odds);
		listViews.add(view_integral);
	}

	/**
	 * pager的适配器类
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
			// 接口问题导致ViewPager显示混乱。。。
			if ("standings".equals(tag)) {
				if (null == liveStandings) {
					liveStandings = new LiveStandings(mContext, v,matchId,opt);
					liveStandings.init();
				}
			}
			if ("odds".equals(tag)) {
				if (null == liveOdds) {
					liveOdds = new LiveDetailOdds(mContext, v,matchId,opt);
					liveOdds.init();
				}
			}
			if ("integral".equals(tag)) {
				if (null == liveIntegral) {
					liveIntegral = new LiveIntegralBasket(mContext, v,matchId,opt);
					liveIntegral.init();
				}
			}
			return v;
		}

		/**
		 * 55. * <span style='font-family:
		 * "Droid Sans";'>从指定的position销毁page</span> 56. * 57. * 58. *<span
		 * style='font-family: "Droid Sans";'>参数同上</span> 59.
		 */
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
	
}
