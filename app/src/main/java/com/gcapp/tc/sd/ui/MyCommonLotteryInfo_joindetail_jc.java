package com.gcapp.tc.sd.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.dataaccess.ShowDtMatch;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.adapter.OrderInfoJcPlayTypeAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：个人中心的竞彩合买详情类
 * 
 * @author lenovo
 * 
 */
public class MyCommonLotteryInfo_joindetail_jc extends Activity implements
		OnClickListener {
	private final static String TAG = "MyCommonLotteryInfo_joindetail_jc";
	private Context context = MyCommonLotteryInfo_joindetail_jc.this;

	private ScrollView scrollView;
	private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney,tv_rewardMoney, tv_count,
			tv_time, tv_orderId, tv_tishi, tv_name, tv_yong,tv_yong3, tv_buyCount,
			tv_title, tv_content, tv_bei, tv_lotteryName_issue, tv_info_tip;
	private TextView tv_type;
	private ImageView img_logo;
	private ImageButton btn_back;
//	private TextView info_tv_totalMoney;

	private Schemes scheme;
	private String[] numbers;
	private List<String> show_numbers;
	private List<Integer> max;

	private int pageindex = 1;
	private int pagesize = 10;

	private Button btn_jixu;
	private Button btn_touzhu;
	private TextView footer;
	private int temp_length = 0;

	private ImageButton ib_schemeinfo, ib_betinfo;
	private LinearLayout ll_schemeInfo, ll_betInfo;
	private LinearLayout btn_info;// 认购列表

	private MyListView2 listView;
	private MyAdapter adapter;
	private List<ShowDtMatch> list_show;
	private List<String> playtypes = new ArrayList<String>();
	private List<String> playResult = new ArrayList<String>();
	private List<List<Map<String, Object>>> playitems = new ArrayList<List<Map<String, Object>>>();
	private LinearLayout ll_betinfo_detail, ll_follow1, ll_follow2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_betlottey_join_jc);
		App.activityS.add(this);
		scheme = (Schemes) getIntent().getSerializableExtra("scheme");
		findView();
		initView();
	}

	/**
	 * 初始化adapter
	 */
	private void findView2() {
		listView = (MyListView2) this.findViewById(R.id.followinfo_jc_listView);
		adapter = new MyAdapter(MyCommonLotteryInfo_joindetail_jc.this);
		listView.setAdapter(adapter);
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		ll_betinfo_detail = (LinearLayout) this
				.findViewById(R.id.ll_betinfo_detail);
		ib_schemeinfo = (ImageButton) this.findViewById(R.id.ib_follow1);
		ll_follow1 = (LinearLayout) this.findViewById(R.id.ll_follow1);
		ll_follow2 = (LinearLayout) this.findViewById(R.id.ll_follow2);

		ib_betinfo = (ImageButton) this.findViewById(R.id.ib_follow2);
		ll_schemeInfo = (LinearLayout) this.findViewById(R.id.ll_schemeInfo);
		ll_betInfo = (LinearLayout) this.findViewById(R.id.ll_betInfo);

		btn_info = (LinearLayout) this.findViewById(R.id.btn_numberInfo);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		tv_money = (TextView) findViewById(R.id.tv_money2);
//		info_tv_totalMoney = (TextView) findViewById(R.id.info_tv_totalMoney);
		tv_state = (TextView) findViewById(R.id.tv_state2);
		tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
		tv_rewardMoney = (TextView) findViewById(R.id.tv_rewardMoney2);
		tv_count = (TextView) findViewById(R.id.tv_numberCount);
		tv_time = (TextView) findViewById(R.id.tv_time2);
		tv_orderId = (TextView) findViewById(R.id.tv_orderId2);
		tv_tishi = (TextView) findViewById(R.id.tv_wShow2);
		tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);
		tv_info_tip = (TextView) findViewById(R.id.tv_info_tip);
		tv_name = (TextView) findViewById(R.id.tv_name2);
		tv_yong = (TextView) findViewById(R.id.tv_yong2);
		tv_yong3 = (TextView) findViewById(R.id.tv_yong3);
		tv_buyCount = (TextView) findViewById(R.id.tv_buy2);
		tv_type = (TextView) findViewById(R.id.tv_schemetype2);
		tv_title = (TextView) findViewById(R.id.tv_schemetitle2);
		tv_content = (TextView) findViewById(R.id.tv_schemeContent2);
		// tv_bei = (TextView) findViewById(R.id.tv_bei);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		btn_jixu = (Button) findViewById(R.id.btn_jixu);
		btn_touzhu = (Button) findViewById(R.id.btn_touzhu);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
	}

	/**
	 * 提交竞彩的合买投注详情请求
	 */
	public void getFollowinfo_jc() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "竞彩的合买投注详情结果===》" + object);
				try {
					// error = object.getString("error");
					if ("0".equals(object.getString("error"))) {
						String s = object.optString("informationId");
						JSONArray array = new JSONArray(s);
//						if (array.length() == 0)
//							MyToast.getToast(getApplicationContext(), "方案暂未公开");
						// return "-1";
						list_show = new ArrayList<ShowDtMatch>();
						for (int i = 0; i < array.length(); i++) {
							ShowDtMatch dt = new ShowDtMatch();
							JSONObject item = array.getJSONObject(i);
							//设置玩法
							scheme.setPlayTypeName(item.getString("PassType"));
							tv_type.setText(scheme.getPlayTypeName());
							dt.setSchemeId(item.getInt("SchemeID"));
							dt.setPlayType(item.getInt("PlayType"));
							dt.setMatchNumber(item.getString("MatchNumber"));
							dt.setGame(item.getString("Game"));
							dt.setMainTeam(item.getString("MaiTeam"));
							dt.setGuestTeam(item.getString("GuestTeam"));
							dt.setStopSelling(item.getString("StopSelling"));
							dt.setLetBile(item.getInt("LetBile"));
							dt.setScore(item.optString("Score"));
							dt.setResult(item.optString("Results"));
							dt.setPassType(item.optString("PassType"));
							String investContent = item
									.getString("investContent");
							String Ways = item.getString("Content");
							dt.setMainLoseball(item.getString("MainLoseball"));
							String[] st = investContent.split(",");

							String[] select = new String[st.length];
							double[] odds = new double[st.length];

							for (int j = 0; j < st.length; j++) {
//								String[] st2 = st[j].split("-");
//								if(st2.length >2) {
//									select[j] = st2[0]+"-"+st2[1];
//								}else{
//									select[j] = st2[0];
//								}
//								odds[j] = Double.parseDouble(st2[st2.length-1].replace(
//										"*", ""));// *后台返回 带＊表示这个结果开奖中了
//													// 本处后台处理正常是不会带＊
								String[] st2 = LotteryUtils.getRexArray(st[j]);
								select[j] = st2[0];
								odds[j] = Double.parseDouble(st2[1]);
							}

							String[] invest_way = Ways.split(",");
							int[] investway = new int[invest_way.length];
							boolean isMixed = false;
							for (int k = 0; k < invest_way.length; k++) {
								String[] temp = invest_way[k].split("-");
								investway[k] = Integer.valueOf(temp[0]);
								if (k == 0 && !(investway[k] / 100 < 0)) {
									isMixed = true;
								}
							}
							dt.setSelect(select);
							dt.setOdds(odds);
							dt.setInvestWay(investway);
							dt.setMixed(isMixed);
							list_show.add(dt);
						}

						findView2();
						adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("x", "拿对阵报错--->" + e.getMessage());
					MyToast.getToast(getApplicationContext(), "没有数据");
					// error = "-1";
				}

				if (object.toString().equals("-500")) {
					MyToast.getToast(MyCommonLotteryInfo_joindetail_jc.this,
							"连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(MyCommonLotteryInfo_joindetail_jc.this,
						"抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getFollowbetInfo_jc(scheme.getId() + "");
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				MyToast.getToast(getApplicationContext(), "没有数据");
				break;
			case 0:
				findView2();
				adapter.notifyDataSetChanged();
				break;
			case -500:
				MyToast.getToast(MyCommonLotteryInfo_joindetail_jc.this, "连接超时")
						;
				break;
			}
		}
	}

	/**
	 * 给控件赋值
	 */
	private void initView() {
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();
		getFollowinfo_jc();
		btn_back.setOnClickListener(this);
		btn_jixu.setOnClickListener(this);
		btn_touzhu.setOnClickListener(this);
		ib_schemeinfo.setOnClickListener(this);
		ib_betinfo.setOnClickListener(this);
		ll_follow1.setOnClickListener(this);
		ll_follow2.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_info.setOnClickListener(this);

		if (null == scheme)
			return;

		/** 合买订单状态 */
		if ("false".equalsIgnoreCase(scheme.getIsPurchasing())) {
			tv_info_tip.setText("跟单投注详情");
		} else if ("true".equalsIgnoreCase(scheme.getIsPurchasing())) {
			/** 普通订单状态 */
			if (scheme.getIsChase() == 0) {
				tv_info_tip.setText("普通投注详情");
			} else {
				/** 追号订单状态 */
				tv_info_tip.setText("追号投注详情");
			}
		}

		if ("72".equals(scheme.getLotteryID())
				|| "73".equals(scheme.getLotteryID())
				|| "45".equals(scheme.getLotteryID())
				|| "74".equals(scheme.getLotteryID())
				|| "75".equals(scheme.getLotteryID())) {
			btn_jixu.setVisibility(View.GONE);
		} else
			btn_jixu.setVisibility(View.VISIBLE);

		btn_touzhu.setText(LotteryUtils.getTitleText(scheme.getLotteryID())
				+ "投注");
		tv_lotteryName
				.setText(LotteryUtils.getTitleText(scheme.getLotteryID()));

		tv_lotteryName_issue.setText(scheme.getIsuseName() == null ? ""
				: scheme.getIsuseName() + "期");
		if (AppTools.allLotteryLogo == null) {
			AppTools.setLogo();
		}
		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme
				.getLotteryID()));
//		tv_money.setText(scheme.getMoney() + "元");
		tv_money.setText(scheme.getMyBuyMoney() + "元");
//		info_tv_totalMoney.setText(scheme.getMoney() + "元");

		tv_time.setText(scheme.getDateTime());
		tv_orderId.setText(scheme.getSchemeNumber());

		tv_count.setText(scheme.getSecretMsg());
		// tv_bei.setVisibility(View.GONE);
		String status = scheme.getSchemeStatus();
		if (status.equals("已中奖")) {
			tv_state.setTextColor(Color.RED);
			tv_state.setText("中奖");
			tv_winMoney.setText(sub(scheme.getShareWinMoney(),scheme.getRewardMoney(),scheme.getSchemeCommission()) + "元");
			tv_rewardMoney.setText(scheme.getRewardMoney() + "元");
		} else {
			tv_state.setText(status);
		}

		if (0 != scheme.getQuashStatus()
				|| (scheme.getQuashStatus() + "").length() == 0) {
			// tv_state.setText("已撤单");
			tv_name.setText(scheme.getInitiateName());
			tv_yong.setText((int) (scheme.getSchemeBonusScale() * 100) + "%");
//			tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
//					+ scheme.getMyBuyMoney() + "元");
			tv_title.setText(scheme.getTitle());
			tv_type.setText(scheme.getPlaytypeName());
			tv_content.setText(scheme.getDescription());
		} else {
			// 合买
			if (scheme.getIsPurchasing().equalsIgnoreCase("False")) {

				tv_name.setText(scheme.getInitiateName());
				tv_yong.setText((int) (scheme.getSchemeBonusScale() * 100)
						+ "%");
				tv_yong3.setText(scheme.getSchemeCommission()+"元");
//				tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
//						+ scheme.getMyBuyMoney() + "元");
				tv_title.setText(scheme.getTitle());
				tv_type.setText(scheme.getPlaytypeName());
				tv_content.setText(scheme.getDescription());
			}
		}
		String numbers = scheme.getLotteryNumber();
	}
	
	public static Double sub(Double v1,Double v2,Double v3){
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		BigDecimal b3 = new BigDecimal(v3.toString());
		
//		return b1.subtract(b2).doubleValue();
		return (b1.subtract(b2)).subtract(b3).doubleValue();
	}

	@Override
	protected void onResume() {
		super.onResume();
		scrollView.smoothScrollTo(0, 0);
	}

	/**
	 * 适配器
	 * 
	 * @author lenovo
	 * 
	 */
	class MyAdapter extends BaseAdapter {
		private Context context;

		public MyAdapter(Context _context) {
			this.context = _context;
		}

		@Override
		public int getCount() {
			return list_show.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_show.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			ShowDtMatch showDtmatch = list_show.get(position);
			if (null == view) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.orderinfo_jc_item_52, null);
				holder.orderinfo_jc_title = (LinearLayout) view
						.findViewById(R.id.orderinfo_jc_title);
				holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
				holder.tv_team = (TextView) view.findViewById(R.id.tv_team);
				holder.guest_vs_host = (TextView) view.findViewById(R.id.guest_vs_host);
				holder.orderinfo_jc_listview = (MyListView2) view
						.findViewById(R.id.orderinfo_jc_listview);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.orderinfo_jc_title
					.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
			if (showDtmatch.getPlayType() / 100 == 45) {// 北单这个字段和竞彩足球这个字段完全显示不同
				holder.tv_time.setText(showDtmatch.getMatchNumber());
			} else {
				holder.tv_time.setText(Html.fromHtml(showDtmatch
						.getMatchNumber().substring(0, 2)
						+ "<br>"
						+ showDtmatch.getMatchNumber().substring(2, 5)));
			}
			String score = showDtmatch.getScore();
			String type = String.valueOf(showDtmatch.getPlayType()).substring(0,2);
			if(!score.equals("") && score!=null) {
				if(type.equals("73")) {
					holder.guest_vs_host.setText("客队vs主队");
					holder.tv_team.setText(Html.fromHtml(showDtmatch.getGuestTeam()
							+ "<br>"+showDtmatch.getScore()+"<br>" + showDtmatch.getMainTeam()));
				}else {
					holder.tv_team.setText(Html.fromHtml(showDtmatch.getMainTeam()
							+ "<br>"+showDtmatch.getScore()+"<br>" + showDtmatch.getGuestTeam()));
				}
			}else{
				if(type.equals("73")) {
					holder.guest_vs_host.setText("客队vs主队");
					holder.tv_team.setText(Html.fromHtml(showDtmatch.getGuestTeam()
							+"<br>VS<br>" + showDtmatch.getMainTeam()));
				}else {
					holder.tv_team.setText(Html.fromHtml(showDtmatch.getMainTeam()
							+ "<br>VS<br>" + showDtmatch.getGuestTeam()));
				}
			}
			setMixedBetingType(showDtmatch);
			holder.orderinfo_jc_listview
					.setAdapter(new OrderInfoJcPlayTypeAdapter(
							MyCommonLotteryInfo_joindetail_jc.this, playtypes,
							playResult, playitems));
			return view;
		}
	}

	/**
	 * 适配器的布局控件类
	 * 
	 * @author lenovo
	 * 
	 */
	private class ViewHolder {
		LinearLayout orderinfo_jc_title;
		TextView tv_time, tv_team,guest_vs_host;
		MyListView2 orderinfo_jc_listview;
	}

	/**
	 * 将投注的竞彩详情拆分成相应格式的键值对
	 * 
	 * @param dtMatch
	 *            ：对阵信息
	 */
	private void setMixedBetingType(ShowDtMatch dtMatch) {
		playtypes.clear();
		playitems.clear();
		playResult.clear();
		String type_normal = null;
		switch (dtMatch.getPlayType()) {

		case 7209: {// 2选1
			List<Map<String, Object>> items1 = null;
			List<Map<String, Object>> items2 = null;
			String str_type = null;
			String item_result = null;

			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())) {
					if (dtMatch.getResult().contains(",")) {
						item_result = dtMatch.getResult().split(",")[i];
					} else {
						item_result = dtMatch.getResult();
					}
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1:
//					str_type = "让球";
					String loseball = dtMatch.getMainLoseball();
					if(loseball.contains("-") || loseball.contains("+")){
						str_type = "让球\n("+loseball+")";
					}else{
						str_type = "让球\n(+"+loseball+")";
					}
					items1 = setItems(str_type, items1, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 5:
					str_type = "胜平负";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				}
			}
			for (int i = 0; i < playtypes.size(); i++) {
//				if (playtypes.get(i).equals("让球")) {
				if (playtypes.get(i).contains("让球")) {
					playitems.add(items1);
				} else if (playtypes.get(i).equals("胜平负")) {
					playitems.add(items2);
				}
			}
		}
			break;

		case 7208: {// 胜平负/让球
			List<Map<String, Object>> items1 = null;
			List<Map<String, Object>> items2 = null;
			String str_type = null;
			String item_result = null;

			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())) {
					if (dtMatch.getResult().contains(",")) {
						item_result = dtMatch.getResult().split(",")[i];
					} else {
						item_result = dtMatch.getResult();
					}
				}
				switch (dtMatch.getInvestWay()[i]) {
				case 1:
				case 2:
				case 3:
//					str_type = "让球";
					String loseball = dtMatch.getMainLoseball();
					if(loseball.contains("-") || loseball.contains("+")){
						str_type = "让球\n("+loseball+")";
					}else{
						str_type = "让球\n(+"+loseball+")";
					}
					items1 = setItems(str_type, items1, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 4:
				case 5:
				case 6:
					str_type = "胜平负";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				}
			}
			for (int i = 0; i < playtypes.size(); i++) {
//				if (playtypes.get(i).equals("让球")) {
				if (playtypes.get(i).contains("让球")) {
					playitems.add(items1);
				} else if (playtypes.get(i).equals("胜平负")) {
					playitems.add(items2);
				}
			}
		}

		// 竞彩足球 混合投注
		case 7206: {
			List<Map<String, Object>> items1 = null;
			List<Map<String, Object>> items2 = null;
			List<Map<String, Object>> items3 = null;
			List<Map<String, Object>> items4 = null;
			List<Map<String, Object>> items5 = null;
			String str_type = null;
			String item_result = null;
			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())
						&& dtMatch.getResult().contains(",")) {
					item_result = dtMatch.getResult().split(",")[i];
				}else {
					item_result = dtMatch.getResult();
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1:
//					str_type = "让球";
					String loseball = dtMatch.getMainLoseball();
					if(loseball.contains("-") || loseball.contains("+")){
						str_type = "让球\n("+loseball+")";
					}else{
						str_type = "让球\n(+"+loseball+")";
					}
					items1 = setItems(str_type, items1, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 2:
					str_type = "总进球";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 3:
					str_type = "比分";
					items3 = setItems(str_type, items3, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 4:
					str_type = "半全场";
					items4 = setItems(str_type, items4, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				case 5:
					str_type = "胜平负";
					items5 = setItems(str_type, items5, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result);
					break;
				}
			}
			for (int i = 0; i < playtypes.size(); i++) {
				if (playtypes.get(i).equals("胜平负")) {
					playitems.add(items5);
//				} else if (playtypes.get(i).equals("让球")) {
				} else if (playtypes.get(i).contains("让球")) {
					playitems.add(items1);
				} else if (playtypes.get(i).equals("总进球")) {
					playitems.add(items2);
				} else if (playtypes.get(i).equals("比分")) {
					playitems.add(items3);
				} else {
					playitems.add(items4);
				}
			}
		}
			break;
		// 竞彩篮球 混合投注
		case 7306: {
			List<Map<String, Object>> basket_items1 = null;
			List<Map<String, Object>> basket_items2 = null;
			List<Map<String, Object>> basket_items3 = null;
			List<Map<String, Object>> basket_items4 = null;
			String str_type = null;
			String item_result = null;
			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				if (!TextUtils.isEmpty(dtMatch.getResult())
						&& dtMatch.getResult().contains(",")) {
					item_result = dtMatch.getResult().split(",")[i];
				}else {
					item_result = dtMatch.getResult();
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1: // 胜负
					str_type = "胜负";
					basket_items1 = setItems(str_type, basket_items1,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				case 2:// 让分
					str_type = "让分";
					basket_items2 = setItems(str_type, basket_items2,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				case 3:// 胜分差
					str_type = "胜分差";
					basket_items3 = setItems(str_type, basket_items3,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				case 4:// 大小分
					str_type = "大小分";
					basket_items4 = setItems(str_type, basket_items4,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result);
					break;
				}
			}

			for (int i = 0; i < playtypes.size(); i++) {
				if (playtypes.get(i).equals("胜负")) {
					playitems.add(basket_items1);
				} else if (playtypes.get(i).equals("让分")) {
					playitems.add(basket_items2);
				} else if (playtypes.get(i).equals("胜分差")) {
					playitems.add(basket_items3);
				} else {
					playitems.add(basket_items4);
				}
			}
		}
			break;
		// 非混合投注
		case 7201: // 足球 让球胜平负
			if (type_normal == null) {
//				type_normal = "让球";
				String loseball = dtMatch.getMainLoseball();
				if(loseball.contains("-") || loseball.contains("+")){
					type_normal = "让球\n("+loseball+")";
				}else{
					type_normal = "让球\n(+"+loseball+")";
				}
			}

		case 7207: // 足球胜平负
			if (type_normal == null) {
				type_normal = "胜平负";
			}

		case 7202: // 足球 比分
			if (type_normal == null) {
				type_normal = "比分";
			}
		case 7203: // 足球 总进球
			if (type_normal == null) {
				type_normal = "总进球";
			}

		case 7204: // 足球 半全场
			if (type_normal == null) {
				type_normal = "半全场";
			}
		case 7301: // 篮球 胜负
			if (type_normal == null) {
				type_normal = "胜负";
			}
		case 7302: // 篮球 让分
			if (type_normal == null) {
				type_normal = "让分";
			}
		case 7303: // 篮球 胜分差
			if (type_normal == null) {
				type_normal = "胜分差";
			}
		case 7304: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "大小分";
			}
		case 4501: // 篮球 大小分
			if (type_normal == null) {
				if (null != dtMatch.getMainLoseball()
						&& !"".equals(dtMatch.getMainLoseball()))
					type_normal = "让球胜平负" + "(" + dtMatch.getMainLoseball()
							+ ")";
				else
					type_normal = "让球胜平负";
			}
		case 4502: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "总进球";
			}
		case 4503: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "上下单双";
			}
		case 4504: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "比分";
			}
		case 4505: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "半全场";
			}
		case 4506: // 篮球 大小分
			if (type_normal == null) {
				type_normal = "胜负过关";
			}

			List<Map<String, Object>> items = null;

			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				items = setItems(type_normal, items, dtMatch.getSelect()[i],
						dtMatch.getOdds()[i], dtMatch.getResult());
			}
			playitems.add(items);
			break;
		}
	}

	/**
	 * 设置每条选号记录
	 * 
	 * @param playtypename
	 *            ：玩法名称
	 * @param items
	 *            ：list对象
	 * @param select
	 *            ：选号结果
	 * @param odd
	 *            ：比分
	 * @param result
	 *            ：赛果
	 * @param concedePoints
	 *            ：让球数
	 * @return
	 */
	private List<Map<String, Object>> setItems(String str_type,
			List<Map<String, Object>> items, String select, double odd,
			String result) {
		if (!playtypes.contains(str_type)) {
			playtypes.add(str_type);
			String Result = TextUtils.isEmpty(result) ? "待定" : result;
			if (Result.equals(" "))
				Result = "待定";
			playResult.add(Result);
			items = new ArrayList<Map<String, Object>>();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("select", select);
		map.put("odd", odd);
		items.add(map);
		return items;
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View arg0) {
		String lotteryID = scheme.getLotteryID();
		switch (arg0.getId()) {
		case R.id.btn_jixu:// 继续本次号码投注
			goToBetLottery(lotteryID);
			break;
		case R.id.btn_touzhu:// 去往本彩种投注
			if (lotteryID.equals("72") || lotteryID.equals("73")
					|| lotteryID.equals("45")) {
				goToSelectLottery(lotteryID);
			} else {
				LotteryUtils.goToSelectLottery(this, lotteryID);
			}
			break;

		/** 点击方案信息的下拉列表 **/
		case R.id.ib_follow1:
		case R.id.ll_follow1:
			if (!ib_schemeinfo.isSelected()) {
				ib_schemeinfo.setSelected(true);
				ll_schemeInfo.setVisibility(View.GONE);

			} else {
				ib_schemeinfo.setSelected(false);
				ll_schemeInfo.setVisibility(View.VISIBLE);
			}
			break;

		/** 查看认购列表详情 **/
		case R.id.btn_numberInfo:
			Intent intent = new Intent(MyCommonLotteryInfo_joindetail_jc.this,
					FollowPurchase_Activity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("schem", scheme);
			intent.putExtra("bundle", bundle);

			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;

		/** 点击投注信息的下拉列表 **/
		case R.id.ib_follow2:
		case R.id.ll_follow2:
			if (!ib_betinfo.isSelected()) {
				ib_betinfo.setSelected(true);
				ll_betinfo_detail.setVisibility(View.GONE);

			} else {
				ib_betinfo.setSelected(false);
				ll_betinfo_detail.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 根据ID跳转到投注类
	 * 
	 * @param lotteryID
	 *            ：彩种ID
	 */
	private void goToSelectLottery(String lotteryID) {
		int id = Integer.parseInt(lotteryID);
		Intent intent = null;
		switch (id) {
		case 72:// 竞彩足球
		case 73:// 竞彩篮球
		case 45:
			long currentTime_jc = System.currentTimeMillis();
			for (int i = 0; i < HallFragment.listLottery.size(); i++) {
				if ("72".equals(HallFragment.listLottery.get(i).getLotteryID())
						|| "73".equals(HallFragment.listLottery.get(i)
								.getLotteryID())
						|| "45".equals(HallFragment.listLottery.get(i)
								.getLotteryID())) {
					if (HallFragment.listLottery.get(i).getDistanceTime()
							- currentTime_jc <= 0) {
						MyToast.getToast(getApplicationContext(),
								"该奖期已结束，请等下一期");
						return;
					}
					AppTools.lottery = HallFragment.listLottery.get(i);

					HallFragment.refreType = true;
					// 对阵信息是否为空
					if (AppTools.lottery.getDtmatch() != null
							&& AppTools.lottery.getDtmatch().length() != 0) {
						if (72 == id
								&& "72".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩足球
							new HallFragment().getBallData(
									MyCommonLotteryInfo_joindetail_jc.this, 0);
							break;
						} else if (73 == id
								&& "73".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩篮球
							new HallFragment().getBasketball(
									MyCommonLotteryInfo_joindetail_jc.this, 0);
							break;
						} 
					} else {
						MyToast.getToast(
								MyCommonLotteryInfo_joindetail_jc.this,
								"没有对阵信息");
					}
				}
			}
		}
	}

	/**
	 * 根据彩种id跳转不同投注页面
	 * 
	 * @param lotteryID
	 *            ： 彩种id
	 */
	private void goToBetLottery(String lotteryID) {
		System.out.println("彩种id-----" + lotteryID);
		System.out.println("玩法id-----" + scheme.getPlayTypeID());
		if (TextUtils.isEmpty(lotteryID)) {
			MyToast.getToast(MyCommonLotteryInfo_joindetail_jc.this,
					"lotteryID 为空");
			return;
		}
		int id = Integer.parseInt(lotteryID);
		Intent intent;
		if (!NumberTools.changeSchemesToSelectedNumbers(scheme)) {
			MyToast.getToast(MyCommonLotteryInfo_joindetail_jc.this, "内容记录错误")
					;
			return;
		}
		Lottery mLottery = AppTools.getLotteryById(lotteryID);
		if (mLottery != null) {
			AppTools.lottery = mLottery;
		} else {
			MyToast.getToast(this, "该奖期已结束，请等下一期");
			return;
		}
		switch (id) {
		case 5:// 双色球
			intent = new Intent(getApplicationContext(), Bet_SSQ_Activity.class);
			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;
		case 39:// 大乐透
			intent = new Intent(getApplicationContext(), Bet_DLT_Activity.class);
			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;
		case 78:
		case 62:// 十一运夺金
			intent = new Intent(getApplicationContext(),
					Bet_11x5_Activity.class);
			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;
		case 70:// 11选5
			intent = new Intent(getApplicationContext(),
					Bet_11x5_Activity.class);
			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;
		case 28:// 时时彩
			intent = new Intent(getApplicationContext(), Bet_SSC_Activity.class);
			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;
		case 83:// 江苏快3
			intent = new Intent(getApplicationContext(), Bet_k3_Activity.class);
			MyCommonLotteryInfo_joindetail_jc.this.startActivity(intent);
			break;
		}
	}

}
