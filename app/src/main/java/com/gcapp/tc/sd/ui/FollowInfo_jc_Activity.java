package com.gcapp.tc.sd.ui;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.dataaccess.ShowDtMatch;
import com.gcapp.tc.fragment.FollowFragment;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.OrderInfoJcPlayTypeAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：点击合买大厅进入的竞彩方案详情页面
 * 
 * @author lenovo
 * 
 */
@SuppressLint("HandlerLeak")
public class FollowInfo_jc_Activity extends Activity implements OnClickListener {
	private static final String TAG = "FollowInfo_jc_Activity";
	private Context context = FollowInfo_jc_Activity.this;
	private Schemes schemes;
	private TextView follow_lv_tv_username, tv_yong, tv_title,
			tv_content,info_tv_lotterytype,
			follow_lv_iv_eachmoney, // 彩种名称// //每份金额
			follow_lv_iv_money,
			tv_playType2; // 剩余份数// 总金额
	private ImageView follow_lv_iv_user_record1, follow_lv_iv_user_record2,
			follow_lv_iv_user_record3, follow_lv_iv_user_record4;
	private TextView btn_submit; // 付款
	private LinearLayout btn_info;// 查看详情
	private EditText et_count; // 购买份数
	/** 应付金额*/
	private TextView follow_detail_tv_remain;
	private LinearLayout btn_back;// 返回
	private ImageButton ib_back;
	private Intent intent;
	private Bundle bundle;
	private int buyShare = 1, remainShare = -1, schedule = 1;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
//	private RoundProgressBar RoundPr;
	private ImageButton ib_schemeinfo, ib_betinfo;
	private LinearLayout ll_schemeInfo;
//	private int baodiPercent = 0;// 保底百分百数
//	private TextView tv_totalmoney;
	private TextView tv_rengouinfo;
	private TextView tv_state;
	private TextView info_tv_passtype;
	private TextView order_time_text;

	// 非竞彩的选号详情
	private MyListView2 listView;
	private MyAdapter adapter;
	private List<ShowDtMatch> list_show;
	private List<String> playtypes = new ArrayList<String>();
	private List<String> playResult = new ArrayList<String>();
	private List<List<Map<String, Object>>> playitems = new ArrayList<List<Map<String, Object>>>();
	private LinearLayout ll_betinfo_detail, ll_follow1, ll_follow2;
	private MyHandler mhandler;
	private ConfirmDialog dialog;
	/** 彩种图片*/
	private ImageView match_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_followinfo2_jc);
		App.activityS.add(this);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化适配器
	 */
	private void findView2() {
		listView = (MyListView2) this.findViewById(R.id.followinfo_jc_listView);
		adapter = new MyAdapter(FollowInfo_jc_Activity.this);
		listView.setAdapter(adapter);
	}

	/**
	 * 初始化自定义控件
	 */
	private void findView() {
		ll_follow1 = (LinearLayout) this.findViewById(R.id.ll_follow1);
		ll_follow2 = (LinearLayout) this.findViewById(R.id.ll_follow2);
		ib_schemeinfo = (ImageButton) this.findViewById(R.id.ib_follow1);
		ib_betinfo = (ImageButton) this.findViewById(R.id.ib_follow2);

		ll_betinfo_detail = (LinearLayout) this
				.findViewById(R.id.ll_betinfo_detail);
		ll_schemeInfo = (LinearLayout) this.findViewById(R.id.ll_schemeInfo);
		tv_rengouinfo = (TextView) this.findViewById(R.id.info_tv_rengouInfo);// 认购人次
		tv_state = (TextView) this.findViewById(R.id.info_tv_state);// 状态
		info_tv_passtype = (TextView) this.findViewById(R.id.info_tv_passtype);// 过关方式
		follow_lv_iv_eachmoney = (TextView) this
				.findViewById(R.id.follow_lv_iv_eachmoney);
		follow_lv_iv_money = (TextView) this
				.findViewById(R.id.follow_lv_iv_money);
		follow_lv_tv_username = (TextView) this
				.findViewById(R.id.follow_lv_tv_username);
		info_tv_lotterytype = (TextView) this.findViewById(R.id.info_tv_lotterytype);
		tv_yong = (TextView) this.findViewById(R.id.info_tv_yongjin2);
		tv_title = (TextView) this.findViewById(R.id.info_tv_title2);
		tv_content = (TextView) this.findViewById(R.id.info_tv_content2);
		tv_playType2 = (TextView) this.findViewById(R.id.follow_tv_playType2);
		order_time_text = (TextView) this.findViewById(R.id.order_time_text);
		follow_detail_tv_remain = (TextView) this
				.findViewById(R.id.follow_detail_tv_remain);
		btn_back = (LinearLayout) this.findViewById(R.id.btn_back);
		ib_back = (ImageButton) this.findViewById(R.id.ib_back);
		follow_lv_iv_user_record1 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record1);
		follow_lv_iv_user_record2 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record2);
		follow_lv_iv_user_record3 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record3);
		follow_lv_iv_user_record4 = (ImageView) this
				.findViewById(R.id.follow_lv_iv_user_record4);
		et_count = (EditText) this.findViewById(R.id.et_count);

		btn_info = (LinearLayout) this.findViewById(R.id.btn_numberInfo);
		btn_submit = (TextView) this.findViewById(R.id.btn_submit);
		match_type = (ImageView) this.findViewById(R.id.match_type);
	}

	/**
	 * 自定义适配器
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
			holder.tv_team.setText(Html.fromHtml(showDtmatch.getMainTeam()
					+ "<br>vs<br>" + showDtmatch.getGuestTeam()));
			setMixedBetingType(showDtmatch);
			holder.orderinfo_jc_listview
					.setAdapter(new OrderInfoJcPlayTypeAdapter(
							FollowInfo_jc_Activity.this, playtypes, playResult,
							playitems));
			return view;
		}
	}

	/**
	 * 请求 合买大厅的竞彩详情
	 */
	public void getFollowInfo_jc() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "竞彩的合买详情结果===" + object);

				try {
					if ("0".equals(object.getString("error"))) {
						if(object.has("datetimeOrder")) {
							schemes.setOrderTime(object.optString("datetimeOrder"));
							mhandler.sendEmptyMessage(0);
						}
						String s = object.optString("informationId");
						JSONArray array = new JSONArray(s);
						list_show = new ArrayList<ShowDtMatch>();
						for (int i = 0; i < array.length(); i++) {
							ShowDtMatch dt = new ShowDtMatch();
							JSONObject item = array.getJSONObject(i);
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
								String[] st2 = st[j].split("-");
								select[j] = st2[0];
								odds[j] = Double.parseDouble(st2[st2.length-1].replace(
										"*", ""));// *后台返回 带＊表示这个结果开奖中了
													// 本处后台处理正常是不会带＊
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
					MyToast.getToast(FollowInfo_jc_Activity.this, "没有数据");
				}

				if (object.toString().equals("-500")) {
					MyToast.getToast(FollowInfo_jc_Activity.this, "连接超时");
				}

			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(FollowInfo_jc_Activity.this, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "查看信息报错" + error.getMessage());
			}
		};
		requestUtil.getFollowInfo_jc(schemes.getId() + "");
	}

	/**
	 * 定义控件变量
	 * 
	 * @author lenovo
	 * 
	 */
	private class ViewHolder {
		LinearLayout orderinfo_jc_title;
		TextView tv_time, tv_team;
		MyListView2 orderinfo_jc_listview;
	}

	/**
	 * 将投注的竞彩详情拆分成相应格式的键值对
	 * 
	 * @param dtMatch
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
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1:
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
		case 7201: // 足球胜平负
			if (type_normal == null) {
//				type_normal = "让球";
				String loseball = dtMatch.getMainLoseball();
				if(loseball.contains("-") || loseball.contains("+")){
					type_normal = "让球\n("+loseball+")";
				}else{
					type_normal = "让球\n(+"+loseball+")";
				}
			}

		case 7207: // 足球 让球胜平负
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
	 * 设置每一栏竞彩数据
	 * 
	 * @param str_type
	 *            ：玩法类型
	 * @param items
	 *            ：对阵数据
	 * @param select
	 *            ：投注结果
	 * @param odd
	 *            ：投注结果
	 * @param result
	 *            ：赛果
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
	 * 初始化属性
	 */
	private void init() {
		mhandler = new MyHandler();
		intent = getIntent();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(getApplicationContext());

		bundle = intent.getBundleExtra("bundle");
		schemes = (Schemes) bundle.getSerializable("schem");
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();
		getFollowInfo_jc();

		follow_lv_tv_username.setText(schemes.getInitiateName());
		info_tv_lotterytype.setText(schemes.getLotteryName());

		tv_yong.setText((int) (schemes.getSchemeBonusScale() * 100) + "%");
//		int remain = schemes.getSurplusShare();
		follow_detail_tv_remain.setText(schemes.getShareMoney() + "");
		follow_lv_iv_eachmoney.setText(schemes.getShareMoney() + "元");
		follow_lv_iv_money.setText(schemes.getSelfmoney() + "元");
		tv_playType2.setText(schemes.getPlayTypeName());

		if(schemes.getRengou_peoplesum() > 0){
			tv_rengouinfo.setText("共"+ schemes.getRengou_money() + "元");
		}else{
			tv_rengouinfo.setText("点击右侧查看");
		}

		if (!"".equals(schemes.getFollow_state())) {
			String test = schemes.getFollow_state();
			tv_state.setText(schemes.getFollow_state() + "("+schemes.getSecretMsg()+")");
//			tv_state.setBackgroundResource(R .color.main_red);
		} else {
			tv_state.setBackgroundResource(R.color.white);
		}

		info_tv_passtype.setText(schemes.getPlayTypeName());
		// 先让奖牌不可见
		follow_lv_iv_user_record1.setVisibility(View.GONE);
		follow_lv_iv_user_record2.setVisibility(View.GONE);
		follow_lv_iv_user_record3.setVisibility(View.GONE);
		follow_lv_iv_user_record4.setVisibility(View.GONE);
		if (schemes.getLevel() == 0)
			follow_lv_iv_user_record1.setVisibility(View.VISIBLE);
		else if (schemes.getLevel() != 1) {
			follow_lv_iv_user_record1.setVisibility(View.VISIBLE);

			int level = schemes.getLevel();
			if (9999 < level) {// 1000以上
				setxin(follow_lv_iv_user_record1, follow_lv_iv_user_record2,
						follow_lv_iv_user_record3, follow_lv_iv_user_record4,
						View.VISIBLE);
				follow_lv_iv_user_record1
						.setBackgroundResource(AppTools.level_crown_list.get(8));
				follow_lv_iv_user_record2
						.setBackgroundResource(AppTools.level_cup_list.get(8));
				follow_lv_iv_user_record3
						.setBackgroundResource(AppTools.level_medal_list.get(8));
				follow_lv_iv_user_record4
						.setBackgroundResource(AppTools.level_star_list.get(8));
			} else if (level <= 9999 && level > 999) {// 含皇冠,奖杯，奖牌，星星
				setxin(follow_lv_iv_user_record1, follow_lv_iv_user_record2,
						follow_lv_iv_user_record3, follow_lv_iv_user_record4,
						View.VISIBLE);
				int crown = level / 1000;// 皇冠个数
				follow_lv_iv_user_record1
						.setBackgroundResource(AppTools.level_crown_list
								.get(crown - 1));
				int cup = (level - 1000 * crown) / 100;// 奖杯个数
				// 从第二个图标开始，有可能取0个，需要进行判断，往下同理
				if (cup != 0) {
					follow_lv_iv_user_record2
							.setBackgroundResource(AppTools.level_cup_list
									.get(cup - 1));
				} else {
					follow_lv_iv_user_record2.setVisibility(View.GONE);
				}
				int medal = (level - 1000 * crown - cup * 100) / 10;// 奖杯个数
				if (medal != 0) {
					follow_lv_iv_user_record3
							.setBackgroundResource(AppTools.level_medal_list
									.get(medal - 1));
				} else {
					follow_lv_iv_user_record3.setVisibility(View.GONE);
				}
				int star = level - 1000 * crown - cup * 100 - medal * 10;// 星星个数
				if (star != 0) {
					follow_lv_iv_user_record4
							.setBackgroundResource(AppTools.level_star_list
									.get(star - 1));
				} else {
					follow_lv_iv_user_record4.setVisibility(View.GONE);
				}
			} else if (level <= 999 && level > 99) {// 含奖杯，奖牌，星星
				follow_lv_iv_user_record2.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
				int cup = level / 100;// 奖杯个数
				follow_lv_iv_user_record2
						.setBackgroundResource(AppTools.level_cup_list
								.get(cup - 1));
				int medal = (level - cup * 100) / 10;// 奖杯个数
				if (medal != 0) {
					follow_lv_iv_user_record3
							.setBackgroundResource(AppTools.level_medal_list
									.get(medal - 1));
				} else {
					follow_lv_iv_user_record3.setVisibility(View.GONE);
				}
				int star = level - cup * 100 - medal * 10;// 星星个数
				if (star != 0) {
					follow_lv_iv_user_record4
							.setBackgroundResource(AppTools.level_star_list
									.get(star - 1));
				} else {
					follow_lv_iv_user_record4.setVisibility(View.GONE);
				}
			} else if (level <= 99 && level > 9) {// 含奖牌，星星
				follow_lv_iv_user_record3.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
				int medal = level / 10;// 奖杯个数
				follow_lv_iv_user_record3
						.setBackgroundResource(AppTools.level_medal_list
								.get(medal - 1));
				int star = level - medal * 10;// 星星个数
				if (star != 0) {
					follow_lv_iv_user_record4
							.setBackgroundResource(AppTools.level_star_list
									.get(star - 1));
				} else {
					follow_lv_iv_user_record4.setVisibility(View.GONE);
				}
			} else if (level >= 1) {
				follow_lv_iv_user_record4.setVisibility(View.VISIBLE);
				follow_lv_iv_user_record4
						.setBackgroundResource(AppTools.level_star_list
								.get(level - 1));
			}
		}
		btn_info.setVisibility(View.VISIBLE);
		// fAdapter.notifyDataSetChanged();
		tv_title.setText(schemes.getTitle());
		tv_content.setText(schemes.getDescription());
		dialog = new ConfirmDialog(this, R.style.dialog);
		
		String lotteryId = schemes.getLotteryID();
		if(lotteryId.equals("72")) {
			match_type.setImageResource(R.drawable.log_lottery_jczq);
		}else{
			match_type.setImageResource(R.drawable.log_lottery_jclq);
		}
	}

	/**
	 * 设置奖牌
	 * 
	 * @param v
	 *            ：第一颗奖牌
	 * @param v1
	 *            ：第2颗奖牌
	 * @param v2
	 *            ：3颗奖牌
	 * @param v3
	 *            ：第4颗奖牌
	 * @param visible
	 *            :控制奖牌显示
	 */
	public void setxin(View v, View v1, View v2, View v3, int visible) {
		v.setVisibility(visible);
		v1.setVisibility(visible);
		v2.setVisibility(visible);
		v3.setVisibility(visible);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_info.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		ib_schemeinfo.setOnClickListener(this);
		ib_betinfo.setOnClickListener(this);
		ll_follow2.setOnClickListener(this);
		ll_follow1.setOnClickListener(this);
		et_count.addTextChangedListener(watcher);
	}

	/**
	 * 监听购买份数的文本值的改变
	 * 
	 */
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > 9999) {
					et_count.setText("9999");
					MyToast.getToast(getApplicationContext(),"最大投注倍数为9999");
				}
				
				if (edt.toString().substring(0, 1).equals("0")) {
					et_count.setText(edt.toString().substring(1,
							edt.toString().length()));
					String money = et_count.getText().toString();
					int sMoney = schemes.getShareMoney();
					if(!money.equals("")) {
						sMoney = Integer.parseInt(money)*schemes.getShareMoney();
					}
					follow_detail_tv_remain.setText(sMoney+"");
				}else{
					int sMoney = Integer.parseInt(et_count.getText().toString())*schemes.getShareMoney();
					follow_detail_tv_remain.setText(sMoney+"");
				}
			}
			show();
		}
	};

	/**
	 * 公用按钮点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 查看跟单列表详情 **/
		case R.id.btn_numberInfo:
			betInfo();
			break;
		/** 付款 **/
		case R.id.btn_submit:
			dopay();
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

		case R.id.btn_back:// 返回
		case R.id.tv_back:
		case R.id.ib_back:
			FollowInfo_jc_Activity.this.finish();
			break;
		}
	}

	/**
	 * 设置购买份数
	 */
	private void show() {
		if (et_count.getText().toString().trim().length() == 0)
			buyShare = 1;
		else
			buyShare = Integer.parseInt(et_count.getText().toString().trim());
	}

	/**
	 * 购买付款
	 */
	private void dopay() {
		if (AppTools.user != null) {
			dialog.show();
			dialog.setDialogContent("确认付款？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						commit();
					}
				}
			});
		} else {
			MyToast.getToast(FollowInfo_jc_Activity.this, "请先登陆");
			Intent intent = new Intent(FollowInfo_jc_Activity.this,
					LoginActivity.class);
			intent.putExtra("loginType", "bet");
			FollowInfo_jc_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在支付...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "加入合买结果---" + object);
				if (null != object) {
					try {
						if ("0".equals(object.optString("error"))) {
							if (RequestUtil.DEBUG)
								Log.i(TAG, "加入合买成功  ");
							AppTools.user.setBalance(object.getLong("balance"));
							AppTools.user.setFreeze(object.getDouble("freeze"));
							remainShare = object.getInt("remainShare");
							schedule = object.getInt("currentSchedule");
							MyToast.getToast(FollowInfo_jc_Activity.this,
									"跟单成功!");
							for (int i = 0; i < FollowFragment.listSchemes
									.size(); i++) {
								if (FollowFragment.listSchemes.get(i).getId()
										.equals(schemes.getId())) {
									FollowFragment.listSchemes.get(i)
											.setSurplusShare(remainShare);
									FollowFragment.listSchemes.get(i)
											.setSchedule(schedule);
									if (schedule == 100)
										FollowFragment.listSchemes.remove(i);
								}
							}
							FollowInfo_jc_Activity.this.finish();
						} else if ("-115".equals(object.optString("error"))) {
							String msg = object.optString("msg");
							Toast.makeText(FollowInfo_jc_Activity.this, msg,
									Toast.LENGTH_SHORT);
							intent = new Intent(FollowInfo_jc_Activity.this,
									SelectRechargeTypeActivity.class);
							FollowInfo_jc_Activity.this.startActivity(intent);
						} else {
							String msg = object.optString("msg");
							MyToast.getToast(context, msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						MyToast.getToast(context, "很抱歉,系统异常...");
						if (RequestUtil.DEBUG)
							Log.e(TAG, e.getMessage());
					}
				} else {
					MyToast.getToast(context, "很抱歉,系统异常...");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "很抱歉,系统异常...");
				if (RequestUtil.DEBUG)
					Log.e(TAG, error.getMessage());
			}
		};
		requestUtil.joinFollow(schemes.getId(), buyShare,
				schemes.getShareMoney());
	}

	/**
	 * 查看认购列表
	 */
	private void betInfo() {
		intent = new Intent(FollowInfo_jc_Activity.this,
				FollowPurchase_Activity.class);
		intent.putExtra("bundle", bundle);
		FollowInfo_jc_Activity.this.startActivity(intent);
	}
	
	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
//			if (null != dialog) {
//				dialog.dismiss();
//			}
			switch (msg.what) {
			case 0:
				order_time_text.setText(schemes.getOrderTime());
				break;
			default:
//				MyToast.getToast(FollowPurchase_Activity.this, "网络异常");
				break;
			}
		}
	}

}
