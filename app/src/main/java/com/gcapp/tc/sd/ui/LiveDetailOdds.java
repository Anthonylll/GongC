package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.OddsModel;
import com.gcapp.tc.sd.ui.adapter.OddsListAdapter;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-5-10 下午3:04:15
 * @version 5.5.0 
 * @Description 比分直播：赔率
 */
public class LiveDetailOdds{

	private View view;
	private Context mContext;
	private int matchId;
	private OddsModel oddsModel;
	private List<OddsModel> epOddslist;
	private List<OddsModel> anOddslist;
	private List<OddsModel> ouOddslist;
	private List<OddsModel> otherOddslist;
	private OddsListAdapter oddsListAdapter;
	private ListView odds_list;
	private TextView europe_odds_text;
	private TextView asian_odds_text;
	private TextView size_odds_text;
	private TextView odds_nodata_text;
	private String opt; 
	private String oddsFlag = "0";
	
	public LiveDetailOdds(Context context, View v,int id,String opt) {
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
		odds_list = (ListView) view.findViewById(R.id.odds_list);
		europe_odds_text = (TextView) view.findViewById(R.id.europe_odds_text);
		asian_odds_text = (TextView) view.findViewById(R.id.asian_odds_text);
		size_odds_text = (TextView) view.findViewById(R.id.size_odds_text);
		odds_nodata_text = (TextView) view.findViewById(R.id.odds_nodata_text);
		if(opt.equals("3002")) {
			europe_odds_text.setText("胜负");
			asian_odds_text.setText("让分胜负");
			size_odds_text.setText("大小分");
		}
	}
	
	private void initListener() {
		europe_odds_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				otherOddslist = epOddslist;
				oddsFlag = "0";
				initAdapter();
				europe_odds_text.setBackgroundResource(R.color.main_red);
				europe_odds_text.setTextColor(mContext.getResources().getColor(R.color.white));
				asian_odds_text.setBackgroundResource(R.color.white);
				asian_odds_text.setTextColor(mContext.getResources().getColor(R.color.gray));
				size_odds_text.setBackgroundResource(R.color.white);
				size_odds_text.setTextColor(mContext.getResources().getColor(R.color.gray));
			}
		});
		
		asian_odds_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				otherOddslist = anOddslist;
				oddsFlag = "1";
				initAdapter();
				asian_odds_text.setBackgroundResource(R.color.main_red);
				asian_odds_text.setTextColor(mContext.getResources().getColor(R.color.white));
				europe_odds_text.setBackgroundResource(R.color.white);
				europe_odds_text.setTextColor(mContext.getResources().getColor(R.color.gray));
				size_odds_text.setBackgroundResource(R.color.white);
				size_odds_text.setTextColor(mContext.getResources().getColor(R.color.gray));
			}
		});
		
		size_odds_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				otherOddslist = ouOddslist;
				oddsFlag = "1";
				initAdapter();
				size_odds_text.setBackgroundResource(R.color.main_red);
				size_odds_text.setTextColor(mContext.getResources().getColor(R.color.white));
				europe_odds_text.setBackgroundResource(R.color.white);
				europe_odds_text.setTextColor(mContext.getResources().getColor(R.color.gray));
				asian_odds_text.setBackgroundResource(R.color.white);
				asian_odds_text.setTextColor(mContext.getResources().getColor(R.color.gray));
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		epOddslist = new ArrayList<OddsModel>();
		anOddslist = new ArrayList<OddsModel>();
		ouOddslist = new ArrayList<OddsModel>();
		otherOddslist = new ArrayList<OddsModel>();
		getOddsData();
	}

	private void getOddsData() {
		if(opt.equals("1002")) {
			opt = "1004";
		}else{
			opt = "3004";
		}
		otherOddslist.clear();
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
					if (item.getString("error").equals("0")) {
						
						if(item.has("epList")) {
							JSONArray jsonArray = item.getJSONArray("epList");
							setOddsData(jsonArray,"0");
						}
						if(item.has("anList")) {
							JSONArray jsonArray1 = item.getJSONArray("anList");
							setOddsData(jsonArray1,"1");
						}
//						if(opt.equals("3003")){
						if(item.has("ouList")) {
							JSONArray jsonArray2 = item.getJSONArray("ouList");
							setOddsData(jsonArray2,"2");
						}
//						}
					}
					otherOddslist = epOddslist;
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
//		if(opt.equals("3002")){
//			opt = "3003";
//		}
		requestUtil.getStandingsInformation(matchId+"",opt);
	}

	protected void initAdapter() {
		oddsListAdapter = new OddsListAdapter(mContext, R.layout.live_odds_list, otherOddslist,opt,oddsFlag);
		odds_list.setAdapter(oddsListAdapter);
		if(otherOddslist == null || otherOddslist.size() == 0) {
			odds_nodata_text.setVisibility(View.VISIBLE);
		}else{
			odds_nodata_text.setVisibility(View.GONE);
		}
	}
	
	private void setOddsData(JSONArray jsonArray,String flag) {
		for (int i = 0; i < jsonArray.length(); i++) {
			oddsModel = new OddsModel();
			JSONObject jsonObtect;
			try {
				jsonObtect = jsonArray.getJSONObject(i);
				oddsModel.setOddsCompany(jsonObtect.getString("company"));
				if(!jsonObtect.getString("cpHost").equals("")){
					oddsModel.setOddsInitialWin(jsonObtect.getString("cpHost"));
				}else{
					oddsModel.setOddsInitialWin("暂无");
				}
				if(jsonObtect.has("cpVs")) {
					if(!jsonObtect.getString("cpVs").equals("")){
						oddsModel.setOddsInitialFlat(jsonObtect.getString("cpVs"));
					}else{
						oddsModel.setOddsInitialFlat("暂无");
					}
					if(!jsonObtect.getString("jsVs").equals("")){
						oddsModel.setOddsInstantFlat(jsonObtect.getString("jsVs"));
					}else{
						oddsModel.setOddsInstantFlat("暂无");
					}
				}else{
					oddsModel.setOddsInitialFlat("暂无");
					oddsModel.setOddsInstantFlat("暂无");
				}
				if(!jsonObtect.getString("cpVisit").equals("")){
					oddsModel.setOddsInitialLose(jsonObtect.getString("cpVisit"));
				}else{
					oddsModel.setOddsInitialLose("暂无");
				}
				if(!jsonObtect.getString("jsHost").equals("")){
					oddsModel.setOddsInstantWin(jsonObtect.getString("jsHost"));
				}else{
					oddsModel.setOddsInstantWin("暂无");
				}
				if(!jsonObtect.getString("jsVisit").equals("")){
					oddsModel.setOddsInstantLose(jsonObtect.getString("jsVisit"));
				}else{
					oddsModel.setOddsInstantLose("暂无");
				}
				oddsModel.sethChange(jsonObtect.getString("host_change"));
				oddsModel.setgChange(jsonObtect.getString("visit_change"));
				oddsModel.setfChange(jsonObtect.getString("vs_change"));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(flag.equals("0")) {
				epOddslist.add(oddsModel);
			}else if(flag.equals("1")) {
				anOddslist.add(oddsModel);
			}else if(flag.equals("2")) {
				ouOddslist.add(oddsModel);
			}
		}
	}
}
