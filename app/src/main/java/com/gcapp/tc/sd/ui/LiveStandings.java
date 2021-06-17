package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.MatchInformation;
import com.gcapp.tc.dataaccess.TeamStandings;
import com.gcapp.tc.sd.ui.adapter.standingsExpandableListAdapter;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * @author anthony
 * @date 2018-5-7 下午4:48:57
 * @version 5.6.20 
 * @Description 赛事战绩
 */
public class LiveStandings {
	
	private View view;
	private Context mContext;
	private ExpandableListView standings_match_list;
	private List<String> grouplist ;
	private standingsExpandableListAdapter standingsAdapter;
	private List<List<TeamStandings>> childlist;
	private List<TeamStandings> matchlist0;
	private List<TeamStandings> matchlist1;
	/** 未来赛事list(含标题)*/
	private List<TeamStandings> matchlist2;
	private List<TeamStandings> matchlist;
	private TeamStandings teamStandings;
	private MatchInformation matchInformation;
	private List<MatchInformation> informationlist0;
	private List<MatchInformation> informationlist1;
	private List<MatchInformation> informationlist2;
	/** 未来赛事list*/
	private List<MatchInformation> informationlist3;
	private List<MatchInformation> informationlist4;
	private List<MatchInformation> informationlist;
	private int matchId;
	private String opt;
	
	public LiveStandings(Context context, View v,int id,String opt) {
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
		standings_match_list = (ExpandableListView)view.findViewById(R.id.standings_match_list);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		grouplist = new ArrayList<String>();
		matchlist1 = new ArrayList<TeamStandings>();
		matchlist0 = new ArrayList<TeamStandings>();
		matchlist2 = new ArrayList<TeamStandings>();
		childlist = new ArrayList<List<TeamStandings>>();
		informationlist0 = new ArrayList<MatchInformation>();
		informationlist1 = new ArrayList<MatchInformation>();
		informationlist2 = new ArrayList<MatchInformation>();
		informationlist3 = new ArrayList<MatchInformation>();
		informationlist4 = new ArrayList<MatchInformation>();
		grouplist.add("历史交锋");
		grouplist.add("近期比赛");
		if(opt.equals("1002")){
			grouplist.add("未来赛事");
		}
		getStandingsData();
	}
	
	/**
	 * 获取队伍赔率
	 */
	private void getStandingsData() {
//		String opt ="1002";
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
//					if (item.getString("error").equals("0")) {
						JSONObject ajsonObtect = item.getJSONObject("vsList");
						setStandingsData(ajsonObtect,"0");
						
						JSONObject bjsonObtect = item.getJSONObject("hostList");
						setStandingsData(bjsonObtect,"1");
						
						JSONObject cjsonObtect = item.getJSONObject("visitList");
						setStandingsData(cjsonObtect,"2");
						
						if(opt.equals("1002")) {
							JSONObject djsonObtect = item.getJSONObject("hfList");
							setStandingsData2(djsonObtect,"3");
							
							JSONObject ejsonObtect = item.getJSONObject("afList");
							setStandingsData2(ejsonObtect,"4");
						}
//					}

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
		requestUtil.getStandingsInformation(matchId+"",opt);
	}
	
	/**
	 * 组装数据
	 */
	private void setStandingsData(JSONObject jsonObtect,String tag) {
		if (tag.equals("0")) {
			matchlist = matchlist0;
			informationlist = informationlist0;
		} else if (tag.equals("1")) {
			matchlist = matchlist1;
			informationlist = informationlist1;
		}else{
			matchlist = matchlist1;
			informationlist = informationlist2;
		}
		
		try {
			teamStandings = new TeamStandings();
			teamStandings.setMatchCount(jsonObtect.getString("matches"));
			teamStandings.setTeamName(jsonObtect.getString("name"));
			if(opt.equals("1002")) {
				teamStandings.setWinCount(jsonObtect.getString("mc").substring(0,2));
				teamStandings.setBisectionCount(jsonObtect.getString("mc").substring(2,4));
				teamStandings.setLoseCount(jsonObtect.getString("mc").substring(4,6));
			}else{
				teamStandings.setWinCount(jsonObtect.getString("mc").substring(0,2));
				teamStandings.setBisectionCount("0平");
				teamStandings.setLoseCount(jsonObtect.getString("mc").substring(2,4));
			}
			JSONArray jsonArray = jsonObtect.getJSONArray("match");
			for (int i = 0; i < jsonArray.length(); i++) {
				matchInformation = new MatchInformation();
				JSONObject jsonObtect2 = jsonArray.getJSONObject(i);
				matchInformation.setMatchTime(jsonObtect2.getString("koTime").substring(2,10));
				matchInformation.setMatchOrganization(jsonObtect2.getString("ln"));
				matchInformation.setMatchScore(jsonObtect2.getString("bf"));
				matchInformation.setGuestTeam(jsonObtect2.getString("an"));
				matchInformation.setHostTeam(jsonObtect2.getString("hn"));
				matchInformation.setMatchResult(jsonObtect2.getString("result"));
				informationlist.add(matchInformation);
			}
			teamStandings.setMatchList(informationlist);
			matchlist.add(teamStandings);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 组装数据(未来赛事)
	 */
	private void setStandingsData2(JSONObject jsonObtect,String tag) {
		if (tag.equals("3")) {
			matchlist = matchlist2;
			informationlist = informationlist3;
		}else{
			matchlist = matchlist2;
			informationlist = informationlist4;
		}
		
		try {
			teamStandings = new TeamStandings();
			teamStandings.setMatchCount("");
			teamStandings.setTeamName(jsonObtect.getString("name"));
				teamStandings.setWinCount("");
				teamStandings.setBisectionCount("");
				teamStandings.setLoseCount("");
			if(jsonObtect.has("match")) {
				JSONArray jsonArray = jsonObtect.getJSONArray("match");
				for (int i = 0; i < jsonArray.length(); i++) {
					matchInformation = new MatchInformation();
					JSONObject jsonObtect2 = jsonArray.getJSONObject(i);
					matchInformation.setMatchTime(jsonObtect2.getString("koTime").substring(2,10));
					matchInformation.setMatchOrganization(jsonObtect2.getString("ln"));
					matchInformation.setMatchScore("VS");
					matchInformation.setGuestTeam(jsonObtect2.getString("an"));
					matchInformation.setHostTeam(jsonObtect2.getString("hn"));
					matchInformation.setNeedDay(jsonObtect2.getString("subDay"));
					matchInformation.setMatchResult(jsonObtect2.getString("subDay"));
//					matchInformation.setMatchResult("");
					if(!jsonObtect2.getString("subDay").equals("0")) {
						informationlist.add(matchInformation);
					}
				}
				teamStandings.setMatchList(informationlist);
				matchlist.add(teamStandings);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化adapter
	 */
	private void initAdapter() {
		childlist.add(matchlist0);
		childlist.add(matchlist1);
		childlist.add(matchlist2);
		standingsAdapter = new standingsExpandableListAdapter(mContext, grouplist, childlist);
		standings_match_list.setAdapter(standingsAdapter);
		int groupCount = standings_match_list.getCount();
		for (int i = 0; i < groupCount; i++) {
			standings_match_list.expandGroup(i);
		}
	}
	
}
