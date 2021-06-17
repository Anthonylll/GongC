package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.IntegralModel;
import com.gcapp.tc.sd.ui.adapter.LiveIntegralListAdapter;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * @author anthony
 * @date 2018-8-8 下午2:08:08
 * @version 5.5.0
 * @Description 直播积分
 */
public class LiveIntegral {

	private View view;
	private Context mContext;
	private int matchId;
	private IntegralModel integralModel;
	private List<IntegralModel> toIntegrallist;
	private List<IntegralModel> hoIntegrallist;
	private List<IntegralModel> guIntegrallist;
	private List<IntegralModel> otherOddslist;
	private LiveIntegralListAdapter integralAdapter;
	private ListView integral_list;
	private TextView total_integral_text;
	private TextView host_integral_text;
	private TextView guest_integral_text;
	private TextView integral_league_text;
	private LinearLayout integral_middle_layout;
	private TextView nodata_text;
	private String opt;

	public LiveIntegral(Context context, View v, int id, String opt) {
		this.view = v;
		this.mContext = context;
		this.matchId = id;
		this.opt = opt;
	}

	public void init() {
		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		integral_list = (ListView) view.findViewById(R.id.integral_list);
		total_integral_text = (TextView) view
				.findViewById(R.id.total_integral_text);
		host_integral_text = (TextView) view
				.findViewById(R.id.host_integral_text);
		guest_integral_text = (TextView) view
				.findViewById(R.id.guest_integral_text);
		integral_league_text = (TextView) view
				.findViewById(R.id.integral_league_text);
		nodata_text= (TextView) view.findViewById(R.id.nodata_text);
		integral_middle_layout = (LinearLayout) view.findViewById(R.id.integral_middle_layout);
	}

	private void initListener() {
		total_integral_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				otherOddslist = toIntegrallist;
				initAdapter();
				total_integral_text.setBackgroundResource(R.color.main_red);
				total_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.white));
				host_integral_text.setBackgroundResource(R.color.white);
				host_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
				guest_integral_text.setBackgroundResource(R.color.white);
				guest_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
			}
		});

		host_integral_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				otherOddslist = hoIntegrallist;
				initAdapter();
				host_integral_text.setBackgroundResource(R.color.main_red);
				host_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.white));
				total_integral_text.setBackgroundResource(R.color.white);
				total_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
				guest_integral_text.setBackgroundResource(R.color.white);
				guest_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
			}
		});

		guest_integral_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				otherOddslist = guIntegrallist;
				initAdapter();
				guest_integral_text.setBackgroundResource(R.color.main_red);
				guest_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.white));
				total_integral_text.setBackgroundResource(R.color.white);
				total_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
				host_integral_text.setBackgroundResource(R.color.white);
				host_integral_text.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		toIntegrallist = new ArrayList<IntegralModel>();
		hoIntegrallist = new ArrayList<IntegralModel>();
		guIntegrallist = new ArrayList<IntegralModel>();
		otherOddslist = new ArrayList<IntegralModel>();
		getOddsData();
	}

	private void getOddsData() {
		if (opt.equals("1002")) {
			opt = "1003";
		}else{
			opt = "3003";
		}
		otherOddslist.clear();
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
					if (item.getString("error").equals("0")) {
						if(item.has("vsList")){
							JSONObject jsonObtect = item.getJSONObject("vsList");
							setOddsData(jsonObtect, "0");
						}
						if(item.has("hostList")) {
							JSONObject jsonObtect1 = item.getJSONObject("hostList");
							setOddsData(jsonObtect1, "1");
						}
						if(item.has("visitList")) {
							JSONObject jsonObtect2 = item.getJSONObject("visitList");
							setOddsData(jsonObtect2, "2");
						}
						
//						JSONObject jsonObtect = item.getJSONObject("vsList");
//						if (jsonObtect.getString("error").equals("0")) {
//							setOddsData(jsonObtect, "0");
//						}
//						JSONObject jsonObtect1 = item.getJSONObject("hostList");
//						if (jsonObtect1.getString("error").equals("0")) {
//							setOddsData(jsonObtect1, "1");
//						}
//						JSONObject jsonObtect2 = item
//								.getJSONObject("visitList");
//						if (jsonObtect2.getString("error").equals("0")) {
//							setOddsData(jsonObtect2, "2");
//						}
						
						integral_league_text.setText(item.getString("l_cn"));
					}
					otherOddslist = toIntegrallist;
					initAdapter();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(mContext, "异常");
			}
		};
		requestUtil.getStandingsInformation(matchId + "", opt);
	}

	protected void initAdapter() {
		integralAdapter = new LiveIntegralListAdapter(mContext,
				R.layout.live_integral_list, otherOddslist);
		integral_list.setAdapter(integralAdapter);
		if(otherOddslist.size() == 0 || otherOddslist == null) {
			integral_middle_layout.setVisibility(View.GONE);
			nodata_text.setVisibility(View.VISIBLE);
		}else{
			integral_middle_layout.setVisibility(View.VISIBLE);
			nodata_text.setVisibility(View.GONE);
		}
	}

	private void setOddsData(JSONObject getObtect, String flag) {
		try {
			JSONArray jsonArray = getObtect.getJSONArray("scores");
			for (int i = 0; i < jsonArray.length(); i++) {
				integralModel = new IntegralModel();
				JSONObject jsonObtect = jsonArray.getJSONObject(i);
				integralModel.setLeagueRanking(jsonObtect.getString("rank"));
				integralModel.setTeamName(jsonObtect.getString("cn_abbr"));
				integralModel.setMatchCount(jsonObtect.getString("count"));
				integralModel.setWinCount(jsonObtect.getString("win"));
				integralModel.setFlatCount(jsonObtect.getString("draw"));
				integralModel.setDefeatCount(jsonObtect.getString("lose"));
				integralModel.setEnterCount(jsonObtect.getString("goal"));
				integralModel.setLoseCount(jsonObtect.getString("losegoal"));
				integralModel.setEntirelyCount(jsonObtect.getString("income"));
				integralModel.setTotalIntegral(jsonObtect.getString("score"));
				integralModel.setTeamType(jsonObtect.getString("is_ha"));
				if (flag.equals("0")) {
					toIntegrallist.add(integralModel);
				} else if (flag.equals("1")) {
					hoIntegrallist.add(integralModel);
				} else if (flag.equals("2")) {
					guIntegrallist.add(integralModel);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
