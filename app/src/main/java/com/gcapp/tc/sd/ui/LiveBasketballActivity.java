package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.gcapp.tc.dataaccess.LiveMatch;
import com.gcapp.tc.sd.ui.adapter.LiveListAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyDateTimeDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.wheel.widget.NumericWheelAdapter;
import com.gcapp.tc.R;

/**
 * @author dm
 * @date 2018-7-10 下午5:17:52
 * @version 5.5.0 
 * @Description 篮球比分直播界面
 */
public class LiveBasketballActivity extends Activity implements OnClickListener{

	private Context mContext = LiveBasketballActivity.this;
	private ImageButton btn_back;
	private TextView not_finished_text;
	private TextView finished_text;
	/** 转换用 */
	private List<LiveMatch> matchlist;
	private LiveMatch liveMatch;
	private PullToRefreshListView match_list;
	private LiveListAdapter liveadapter;
	/** 比赛状态标志;0,未结束；1，结束 */
	private String stateTag = "0";
	private TextView nodata_text;
	private TextView live_time_text;
	private ImageButton btn_live_date;
	private MyDateTimeDialog livedateDialog;
	private int year, month, days;
	private String date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_live_match);
		App.activityS.add(this);

		// 1、初始化视图-
		initView();
		// 2、初始化监听事件
		initListener();
		// 3、初始化数据
		initData();
	}
	
	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		not_finished_text = (TextView) findViewById(R.id.not_finished_text);
		nodata_text = (TextView) findViewById(R.id.nodata_text);
		finished_text = (TextView) findViewById(R.id.finished_text);
		live_time_text = (TextView) findViewById(R.id.live_time_text);
		match_list = (PullToRefreshListView) findViewById(R.id.match_list);
		btn_live_date = (ImageButton) findViewById(R.id.btn_live_date);
		match_list.setMode(Mode.PULL_FROM_START);
		initTopLayout();
	}
	
	private void initListener() {
		btn_back.setOnClickListener(this);
		not_finished_text.setOnClickListener(this);
		finished_text.setOnClickListener(this);
		btn_live_date.setOnClickListener(this);
		
		match_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext,LiveDetailBasketActivity.class);
				LiveMatch match = matchlist.get(position-1);
				int matchId = match.getMatchId();
				String hostTeam = match.getHostTeam();
				String guestTeam = match.getGuestTeam();
				String gRanking = match.getGuestRanking();
				String hRanking = match.getHostRanking();
				String league = match.getMatchOrganization();
				intent.putExtra("matchId", matchId);
				intent.putExtra("hostTeam", hostTeam);
				intent.putExtra("guestTeam", guestTeam);
				intent.putExtra("gRanking", gRanking);
				intent.putExtra("hRanking", hRanking);
				intent.putExtra("league", league);
				intent.putExtra("opt", "3002");
				mContext.startActivity(intent);
			}
		});
		
		match_list
		.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				match_list
						.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
				if (NetWork.isConnect(mContext)) {
					matchlist.clear();
					getBasketballMatch();
				} else {
					MyToast.getToast(mContext, "网络连接异常，请检查网络");
					match_list.onRefreshComplete();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载
				
			}
		});

	}
	
	private void initData() {
		matchlist = new ArrayList<LiveMatch>();

		initDialog();
	}
	
	/**
	 * 初始化时间选择对话框的值
	 */
	private void initDialog() {
		
		Calendar now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		days = now.get(Calendar.DATE);
		setDays(year,month,days);
		
		NumericWheelAdapter mYearAdapter = new NumericWheelAdapter(2008, 2050);
		NumericWheelAdapter mMonthAdapter = new NumericWheelAdapter(1, 12);
		NumericWheelAdapter mDayAdapter = new NumericWheelAdapter(1,
				AppTools.getLastDay(year, month));
		livedateDialog = new MyDateTimeDialog(this,
				R.style.dialog, mYearAdapter, mMonthAdapter, mDayAdapter,
				new MyClickListener());
		livedateDialog.initDay(year, month, days);
		getBasketballMatch();
	}
	
	/**
	 * 获取篮球比赛数据
	 */
	private void getBasketballMatch() {
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
					if(item.has("match")) {
						JSONArray ajsonArray = item.getJSONArray("match");
						initMatchData(ajsonArray);
					}
					
					if(liveadapter != null) {
						initListView();
						liveadapter.notifyDataSetChanged();
					}else{
						initAdapter();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(mContext, "数据异常，请稍后重试！");
			}
		};
		requestUtil.getMatchData("3001",date);
	}
	
	/**
	 * 未开赛数据(篮球把主客队调换)
	 */
	private void initMatchData(JSONArray jsonArray) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				liveMatch = new LiveMatch();
				JSONObject bjsonObtect2 = jsonArray.getJSONObject(i);
				if (!bjsonObtect2.getString("st").equals("完场")
						&& stateTag.equals("0")) {
					//状态为非"完"的比赛
					liveMatch.setHostGrade(bjsonObtect2.getString("as"));
					liveMatch.setGuestGrade(bjsonObtect2.getString("hs"));
					String firsthost = bjsonObtect2.getString("ahs");
					String firsguest = bjsonObtect2.getString("hhs");
					//上半场时半场比分为""
					if(firsthost.equals("") || firsguest.equals("")) {
						liveMatch.setHostFirstGrade("0");
						liveMatch.setGuestFirstGrade("0");
					}else{
						liveMatch.setHostFirstGrade(firsthost);
						liveMatch.setGuestFirstGrade(firsguest);
					}
					liveMatch.setMatchDate(bjsonObtect2.getString("gt"));
					liveMatch.setMatchState(bjsonObtect2.getString("st"));
					
					liveMatch.setMatchId(bjsonObtect2.getInt("id"));
					liveMatch.setMatchTime(bjsonObtect2.getString("dt"));
					liveMatch.setMatchNumber(bjsonObtect2.getString("no"));
					liveMatch
							.setMatchOrganization(bjsonObtect2.getString("ln"));
					liveMatch.setHostTeam(bjsonObtect2.getString("an2"));
					liveMatch.setGuestTeam(bjsonObtect2.getString("hn2"));
					liveMatch.setGuestTeamImg(bjsonObtect2.getString("a_pic"));
					liveMatch.setHostTeamImg(bjsonObtect2.getString("h_pic"));
					liveMatch.setHostRanking(bjsonObtect2.getString("table_a"));
					liveMatch.setGuestRanking(bjsonObtect2.getString("table_h"));
					matchlist.add(liveMatch);
				} else if (bjsonObtect2.getString("st").equals("完场")
						&& stateTag.equals("1")) {
					//状态为"完"的比赛
					liveMatch.setHostGrade(bjsonObtect2.getString("as"));
					liveMatch.setGuestGrade(bjsonObtect2.getString("hs"));
					
					liveMatch.setHostFirstGrade(bjsonObtect2.getString("ahs"));
					liveMatch.setGuestFirstGrade(bjsonObtect2.getString("hhs"));

					liveMatch.setMatchId(bjsonObtect2.getInt("id"));
					liveMatch.setMatchTime(bjsonObtect2.getString("dt"));
					liveMatch.setMatchNumber(bjsonObtect2.getString("no"));
					liveMatch
							.setMatchOrganization(bjsonObtect2.getString("ln"));
					liveMatch.setHostTeam(bjsonObtect2.getString("an2"));
					liveMatch.setGuestTeam(bjsonObtect2.getString("hn2"));
					liveMatch.setMatchState(bjsonObtect2.getString("st"));
					liveMatch.setGuestTeamImg(bjsonObtect2.getString("a_pic"));
					liveMatch.setHostTeamImg(bjsonObtect2.getString("h_pic"));
					liveMatch.setHostRanking(bjsonObtect2.getString("table_a"));
					liveMatch.setGuestRanking(bjsonObtect2.getString("table_h"));
					matchlist.add(liveMatch);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.not_finished_text:
			stateTag = "0";
			initTopLayout();
			matchlist.clear();
			getBasketballMatch();
			break;
		case R.id.finished_text:
			stateTag = "1";
			initTopLayout();
			matchlist.clear();
			getBasketballMatch();
			break;
		case R.id.btn_live_date:
			livedateDialog.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化页面顶部视图
	 */
	private void initTopLayout() {
		if(stateTag.equals("0")) {
			GradientDrawable gdLeft = (GradientDrawable) not_finished_text
					.getBackground();
			gdLeft.setColor(this.getResources().getColor(R.color.white));
			not_finished_text.setTextColor(this.getResources().getColor(
					R.color.main_red));

			GradientDrawable gdRight = (GradientDrawable) finished_text
					.getBackground();
			gdRight.setColor(this.getResources().getColor(R.color.main_red));
			finished_text.setTextColor(this.getResources().getColor(
					R.color.white));
		}else{
			GradientDrawable gdLeft2 = (GradientDrawable) not_finished_text
					.getBackground();
			gdLeft2.setColor(this.getResources().getColor(R.color.main_red));
			not_finished_text.setTextColor(this.getResources().getColor(
					R.color.white));

			GradientDrawable gdRight2 = (GradientDrawable) finished_text
					.getBackground();
			gdRight2.setColor(this.getResources().getColor(R.color.white));
			finished_text.setTextColor(this.getResources().getColor(
					R.color.main_red));
		}
	}
	
	/**
	 * 初始化adapter
	 */
	private void initAdapter() {
		liveadapter = new LiveListAdapter(mContext,R.layout.activity_live_basket_child_item,
				matchlist);
		match_list.setAdapter(liveadapter);
		initListView();
	}
	
	private void initListView() {
		match_list.onRefreshComplete();
		if(matchlist.size() == 0){
			nodata_text.setVisibility(View.VISIBLE);
			match_list.setVisibility(View.GONE);
		}else{
			nodata_text.setVisibility(View.GONE);
			match_list.setVisibility(View.VISIBLE);
		}
		live_time_text.setText(date+" "+matchlist.size()+"场比赛");
	}
	
	/**
	 * 日期 dialog 点击监听
	 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.funds_btn_ok:
				livedateDialog.dismiss();
				setDays(livedateDialog.y, livedateDialog.m, livedateDialog.d);
				matchlist.clear();
				getBasketballMatch();
				break;
			case R.id.funds_btn_no:
				livedateDialog.dismiss();
				break;
			}
			setDays();
			livedateDialog.setCheckItem();
		}
	}
	
	/**
	 * 设置日期 *
	 */
	public void setDays(int y, int m, int d) {
		this.year = y;
		this.month = m;
		this.days = d;
		String mMonth = "";
		String mDay = "";
		if(month < 10){
			mMonth = "0"+month;
		}else{
			mMonth = ""+month;
		}
		if(days < 10){
			mDay = "0"+days;
		}else{
			mDay = ""+days;
		}
		
		date = year + "-" + mMonth + "-" + mDay;
	}

	/**
	 * 初始化日期选择框的数据
	 */
	public void setDays() {
		livedateDialog.initDay(year, month, days);
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
