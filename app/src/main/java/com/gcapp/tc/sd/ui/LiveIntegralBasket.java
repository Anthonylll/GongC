package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.IntegralModel;
import com.gcapp.tc.dataaccess.TotalIntegralModel;
import com.gcapp.tc.sd.ui.adapter.BasketballIntegralAdapter;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * @author dm
 * @date 2019-7-5 下午3:17:47
 * @version 5.5.0 
 * @Description 
 */
public class LiveIntegralBasket {

	private View view;
	private Context mContext;
	private ExpandableListView basket_integral_list;
	private List<String> grouplist;
	private BasketballIntegralAdapter basketballAdapter;
	private List<List<TotalIntegralModel>> childlist;
	private List<TotalIntegralModel> integrallist1;
	private List<TotalIntegralModel> integrallist;
	private TotalIntegralModel tTntegralModel;
	private IntegralModel detailModel;
	private List<IntegralModel> detaillist3;
	private List<IntegralModel> detaillist4;
	private List<IntegralModel> detaillist;
	private int matchId;
	private String opt;

	public LiveIntegralBasket(Context context, View v, int id, String opt) {
		this.view = v;
		this.mContext = context;
		this.matchId = id;
		this.opt = opt;
	}

	/**
	 * 初始化控件和监听事件
	 */
	public void init() {
		initView();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		basket_integral_list = (ExpandableListView) view
				.findViewById(R.id.basket_integral_list);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		grouplist = new ArrayList<String>();
		integrallist1 = new ArrayList<TotalIntegralModel>();
		childlist = new ArrayList<List<TotalIntegralModel>>();
		detaillist3 = new ArrayList<IntegralModel>();
		detaillist4 = new ArrayList<IntegralModel>();
		getIntegralData();
	}

	/**
	 * 获取队伍积分
	 */
	private void getIntegralData() {
		if (opt.equals("1002")) {
			opt = "1003";
		} else {
			opt = "3003";
		}

		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
					JSONObject jsonObtect = item.getJSONObject("data");
					grouplist.add(item.getString("l_cn"));
					if (jsonObtect.has("westList")) {
						JSONArray westJson = jsonObtect
								.getJSONArray("westList");
						setStandingsData2(westJson, "3");
					}
					if (jsonObtect.has("eastList")) {
						JSONArray westJson = jsonObtect
								.getJSONArray("eastList");
						setStandingsData2(westJson, "4");
					}

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

	/**
	 * 组装数据(未来赛事)
	 */
	private void setStandingsData2(JSONArray jsonArray, String tag) {
		if (tag.equals("3")) {
			integrallist = integrallist1;
			detaillist = detaillist3;
		} else {
			integrallist = integrallist1;
			detaillist = detaillist4;
		}
		try {
			tTntegralModel = new TotalIntegralModel();
			if(tag.equals("3")) {
				tTntegralModel.setteamArea("西部排名");
			}else{
				tTntegralModel.setteamArea("东部排名");
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				detailModel = new IntegralModel();
				JSONObject jsonObtect = jsonArray.getJSONObject(i);

				detailModel.setLeagueRanking(jsonObtect.getString("rank"));
				detailModel.setTeamName(jsonObtect.getString("cn_abbr"));
				detailModel.setWinCount(jsonObtect.getString("win"));
				detailModel.setDefeatCount(jsonObtect.getString("lose"));
				detailModel.setWinProbability(jsonObtect.getString("winrate"));
				detailModel.setWinState(jsonObtect.getString("winstatus"));
				detailModel.setWinContent(jsonObtect.getString("wintext"));
				detaillist.add(detailModel);
			}
			tTntegralModel.setIntegralList(detaillist);
			integrallist.add(tTntegralModel);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化adapter
	 */
	private void initAdapter() {
		childlist.add(integrallist1);
		basketballAdapter = new BasketballIntegralAdapter(mContext, grouplist,
				childlist);
		basket_integral_list.setAdapter(basketballAdapter);
		int groupCount = basket_integral_list.getCount();
		for (int i = 0; i < groupCount; i++) {
			basket_integral_list.expandGroup(i);
		}
	}
	
}
