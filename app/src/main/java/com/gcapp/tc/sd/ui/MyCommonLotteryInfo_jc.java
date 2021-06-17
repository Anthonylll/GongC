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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.dataaccess.ShowDtMatch;
import com.gcapp.tc.dataaccess.ShowDtMatchOptimize;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.adapter.OrderInfoJcPlayTypeAdapter;
import com.gcapp.tc.sd.ui.adapter.OrderInfoJcPlayTypeAdapter_optimize;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：个人中心的竞彩的投注详情类
 * 
 * @author lenovo
 * 
 */
public class MyCommonLotteryInfo_jc extends Activity implements OnClickListener {
	private Context context = MyCommonLotteryInfo_jc.this;
	private final static String TAG = "MyCommonLotteryInfo_jc";
	private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney,tv_rewardMoney, tv_count,
			tv_time, tv_orderId, tv_betType, tv_lotteryNum, tv_name, tv_yong,
			tv_scheme, tv_buyCount, tv_title, tv_content, tv_show,
			tv_lotteryName_issue, tv_info_tip, tv_bei, tv_orderType;
	private ImageView img_logo, ll_divider;
	private ImageButton betinfo_hide_btn, btn_back;
	private LinearLayout ll_numberCount;
	private MyListView2 mListView;
	private Schemes scheme;
	private RelativeLayout rl_join1, rl_join2;
	private LinearLayout ll_winNumbet;
	private TextView tv_secretLevel;

	/** 竞彩 */
	private List<ShowDtMatch> list_show;
	private List<ShowDtMatchOptimize> list_show_optimize;
	private List<String> playtypes = new ArrayList<String>();
	private List<String> playResult = new ArrayList<String>();
	private List<String> playLet = new ArrayList<String>();
	private List<List<Map<String, Object>>> playitems = new ArrayList<List<Map<String, Object>>>();
	// 进行优化
	private List<List<String>> playtypesList = null;
	private List<List<String>> playResultList = null;
	private List<List<String>> playLetList = null;
	private List<List<List<Map<String, Object>>>> playitemsList = null;

	private List<String> buyContent = null;
	private List<String> result = null;
	private List<Integer> markRed = null;
	private List<List<String>> buyContents = null; // 奖金优化后的投注详情
	private List<List<String>> results = null; // 比赛结果
	private List<List<Integer>> markReds = null; // 是否标红

	private String opt = "46"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	// private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	private Button btn_jixu;
	private Button btn_touzhu;
	private RelativeLayout rl_optimize;
	private LinearLayout ll_optimize;
	private ImageButton betinfo_optimize_hide_btn;
	private ImageView ll_divider1;
	private MyListView2 lv_betInfo_optimize;
	private TextView tv_show233;
	private String preBetType; // 优化方案
	private TextView tv_preBetType;
	private int isHide;// 方案是否可见
	private String secretMsg;// 显示信息
	private TextView tv_secretMsg;// 显示方案信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_betlottey_info);
		findView();
		initView();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		scheme = (Schemes) getIntent().getSerializableExtra("scheme");
		getJCBetData();
		
		tv_secretLevel = (TextView) findViewById(R.id.tv_secretLevel);
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		tv_money = (TextView) findViewById(R.id.tv_money2);
		tv_state = (TextView) findViewById(R.id.tv_state2);
		tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
		tv_rewardMoney = (TextView) findViewById(R.id.tv_rewardMoney2);
		tv_count = (TextView) findViewById(R.id.tv_numberCount);
		tv_time = (TextView) findViewById(R.id.tv_time2);
		tv_orderId = (TextView) findViewById(R.id.tv_orderId2);
		tv_betType = (TextView) findViewById(R.id.tv_orderType2);
		tv_lotteryNum = (TextView) findViewById(R.id.tv_num1);
		tv_show = (TextView) findViewById(R.id.tv_show);
		ll_winNumbet = (LinearLayout) findViewById(R.id.ll_winNumbet);
		tv_show.setVisibility(View.GONE);
		ll_winNumbet.setVisibility(View.GONE);

		tv_name = (TextView) findViewById(R.id.tv_name2);
		tv_yong = (TextView) findViewById(R.id.tv_yong2);
		tv_scheme = (TextView) findViewById(R.id.tv_scheme2);
		tv_buyCount = (TextView) findViewById(R.id.tv_buy2);
		tv_title = (TextView) findViewById(R.id.tv_schemetitle2);
		tv_content = (TextView) findViewById(R.id.tv_schemeContent2);
		tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);
		tv_info_tip = (TextView) findViewById(R.id.tv_info_tip);
		tv_bei = (TextView) findViewById(R.id.tv_bei);
		tv_orderType = (TextView) findViewById(R.id.tv_orderType);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		mListView = (MyListView2) findViewById(R.id.lv_betInfo);
		mListView.setBackgroundResource(R.color.my_center_bg2);
		int px = dp2px(1);
		mListView.setPadding(px, 0, px, px);

		rl_join1 = (RelativeLayout) findViewById(R.id.rl_joinInfo);
		rl_join2 = (RelativeLayout) findViewById(R.id.rl_joinInfo2);

		btn_jixu = (Button) findViewById(R.id.btn_jixu);
		btn_touzhu = (Button) findViewById(R.id.btn_touzhu);

		betinfo_hide_btn = (ImageButton) findViewById(R.id.betinfo_hide_btn);
		ll_numberCount = (LinearLayout) findViewById(R.id.ll_numberCount);
		ll_divider = (ImageView) findViewById(R.id.ll_divider);
		btn_back = (ImageButton) findViewById(R.id.btn_back);

		tv_show233 = (TextView) findViewById(R.id.tv_show233);
		// 奖金优化部分
		rl_optimize = (RelativeLayout) findViewById(R.id.rl_optimize);
		ll_optimize = (LinearLayout) findViewById(R.id.ll_optimize);
		betinfo_optimize_hide_btn = (ImageButton) findViewById(R.id.betinfo_optimize_hide_btn);
		tv_preBetType = (TextView) findViewById(R.id.tv_preBetType);

		ll_divider1 = (ImageView) findViewById(R.id.ll_divider1);
		lv_betInfo_optimize = (MyListView2) findViewById(R.id.lv_betInfo_optimize);
		lv_betInfo_optimize.setBackgroundResource(R.color.my_center_bg2);
		lv_betInfo_optimize.setPadding(px, 0, px, px);
		tv_secretMsg = (TextView) findViewById(R.id.tv_secretMsg);
	}

	/**
	 * dp单位转PX
	 * 
	 * @param dp
	 *            ：dp参数
	 * @return
	 */
	private int dp2px(int dp) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
		return (int) px;
	}

	/**
	 * 绑定监听，给控件赋初始值
	 */
	@SuppressLint("NewApi")
	private void initView() {
		btn_back.setOnClickListener(this);
		betinfo_hide_btn.setOnClickListener(this);
		betinfo_optimize_hide_btn.setOnClickListener(this);
		ll_numberCount.setOnClickListener(this);
		ll_optimize.setOnClickListener(this);
		btn_jixu.setOnClickListener(this);
		btn_touzhu.setOnClickListener(this);
		btn_jixu.setVisibility(View.GONE);
		myHandler = new MyHandler();
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();

		if (null == scheme)
			return;
		/** 合买订单状态 */
		if ("false".equalsIgnoreCase(scheme.getIsPurchasing())) {
			tv_info_tip.setText("合买投注详情");
		} else if ("true".equalsIgnoreCase(scheme.getIsPurchasing())) {
			/** 普通订单状态 */
			if (scheme.getIsChase() == 0) {
				tv_info_tip.setText("普通投注详情");
			} else {
				/** 追号订单状态 */
				tv_info_tip.setText("追号投注详情");
			}
		}
		rl_join1.setVisibility(View.GONE);
		rl_join2.setVisibility(View.GONE);
		
		if (scheme.getSecretMsg() != null
		&& scheme.getSecretMsg().length() != 0) {
			tv_secretLevel.setText(scheme.getSecretMsg());
		}

		tv_bei.setText(scheme.getMultiple() + "倍");

		btn_touzhu.setText(scheme.getLotteryName() + "投注");
		tv_lotteryName
				.setText(LotteryUtils.getTitleText(scheme.getLotteryID()));

		btn_touzhu.setText(scheme.getLotteryName() + "投注");
		tv_lotteryName
				.setText(LotteryUtils.getTitleText(scheme.getLotteryID()));
		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme
				.getLotteryID()));
		tv_lotteryName_issue.setText(scheme.getIsuseName() == null ? ""
				: scheme.getIsuseName() + "期");
		tv_money.setText(scheme.getMoney() + "元");
		tv_time.setText(scheme.getDateTime());
		tv_orderId.setText(scheme.getSchemeNumber());

		if (scheme.getFromClient() == 1)
			tv_betType.setText("网页投注");
		else if (scheme.getFromClient() == 2)
			tv_betType.setText("手机APP投注");

		tv_winMoney.setText("--");
		tv_rewardMoney.setText("--");

		// 是否合买
		if (scheme.getIsPurchasing().equals("False")) {
			rl_join1.setVisibility(View.VISIBLE);
			rl_join2.setVisibility(View.VISIBLE);

			tv_name.setText(scheme.getInitiateName());
			tv_yong.setText((scheme.getSchemeBonusScale() * 100) + "%");
			tv_scheme.setText(scheme.getShare() + "份,共" + scheme.getMoney()
					+ "元,每份" + scheme.getShareMoney() + "元");
			tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
					+ scheme.getMyBuyMoney() + "元");
			tv_title.setText(scheme.getTitle());
			tv_content.setText(scheme.getDescription());
		}

		String status = scheme.getSchemeStatus();
		if (scheme.getWinMoneyNoWithTax() > 0) {
			tv_state.setText("中奖");
			tv_winMoney.setText(sub(scheme.getWinMoneyNoWithTax(),scheme.getRewardMoney()) + "元");
			tv_rewardMoney.setText(scheme.getRewardMoney() + "元");
		} else {
			tv_state.setText(status);
		}

		if (0 != scheme.getQuashStatus()) {
			tv_state.setText("已撤单");
		} else {
			if ("False".equals(scheme.getSchemeIsOpened())) {
				if (scheme.getSurplusShare() > 0) {
				} else if (scheme.getBuyed().equals("True")) {
				}
			} else if ("True".equals(scheme.getSchemeIsOpened())) {
				if (scheme.getWinMoneyNoWithTax() > 0) {
					tv_winMoney.setText(sub(scheme.getWinMoneyNoWithTax(),scheme.getRewardMoney()) + "元");
					tv_rewardMoney.setText(scheme.getRewardMoney() + "元");
				} else {
					// tv_state.setText("未中奖");
				}
			}
		}
	}
	
	public static Double sub(Double v1,Double v2){
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提交竞彩投注详情请求
	 */
	public void getJCBetData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "竞彩投注详情请求结果" + object);
				try {
					// error = object.getString("error");
					if ("0".equals(object.getString("error"))) {
						String s = object.optString("informationId");
						isHide = (Integer) object.opt("isHide");
						secretMsg = (String) object.opt("secretMsg");
						JSONArray array = new JSONArray(s);
						if (array.length() == 0)
							myHandler.sendEmptyMessage(-1);

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
							dt.setMainLoseball(item.optString("MainLoseball"));
							dt.setLetScore(item.optDouble("LetScore"));
							dt.setBigSmallScore(item.optDouble("BigSmallScore"));
							String investContent = item
									.getString("investContent");
							String Ways = item.getString("Content");
							String[] st = investContent.split(",");

							// 胜-1.31,平-4.20,胜-2.12,平-3.40
							String[] select = new String[st.length];
							double[] odds = new double[st.length];

							for (int j = 0; j < st.length; j++) {
								String[] st2 = LotteryUtils.getRexArray(st[j]);
								Log.i("x", "选的结果---" + st2[0]);
								select[j] = st2[0];
								Log.i("x", "赔率---" + st2[1]);
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
						// 奖金优化部分
						String optimize = object.optString("optimization");
						if (!"".equals(optimize)) {
							preBetType = object.optString("preBetType");
							JSONArray array1 = new JSONArray(optimize);
							list_show_optimize = new ArrayList<ShowDtMatchOptimize>();
							for (int i = 0; i < array1.length(); i++) {
								ShowDtMatchOptimize dt = new ShowDtMatchOptimize();
								JSONObject item = array1.getJSONObject(i);

								dt.setGgWay(item.optString("ggWay"));
								dt.setInvestNum(item.optString("investNum"));
								dt.setBuyContent(item.optString("buyContent"));
								dt.setResult(item.optString("result"));
								dt.setWinMoney(item.optString("winMoney"));
								dt.setMarkRed(item.optString("markRed"));
								list_show_optimize.add(dt);
							}
						}
						// 结果处理
						myHandler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					MyToast.getToast(context, "数据解析异常");
					Log.e("x", "数据解析报错--->" + e.getMessage());
					// error = "-1";
				}

				if (object.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getBetInfo_jc(scheme.getId() + "");
	}

	/**
	 * 处理页面显示，刷新页面数据
	 * 
	 * @author lenovo
	 * 
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Intent intent = null;
			switch (msg.what) {
			case -1:
				// MyToast.getToast(getApplicationContext(), "没有数据");
				break;
			case 0:
				if (isHide == 0) {
					if (list_show_optimize.size() > 0) {
						tv_show233.setText("优化前的方案:");
						rl_optimize.setVisibility(View.VISIBLE);

						betinfo_hide_btn.setSelected(true);
						ll_divider.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);

						betinfo_optimize_hide_btn.setSelected(false);
						ll_divider1.setVisibility(View.VISIBLE);
						lv_betInfo_optimize.setVisibility(View.VISIBLE);
						tv_preBetType.setText(preBetType);

						setData(); // 将在adapter中处理的数据拿出来处理 改完需求再优化
						setOptimizeData();

						mListView.setAdapter(new MyAdapter(
								getApplicationContext()));
						lv_betInfo_optimize.setAdapter(new MyAdapter_optimize(
								getApplicationContext()));
					} else {
						betinfo_hide_btn.setSelected(false);
						tv_show233.setText("选号详情:");
						rl_optimize.setVisibility(View.GONE);
						setData();
						mListView.setAdapter(new MyAdapter(
								getApplicationContext()));

					}
				} else if (isHide == 1) {
					if (list_show_optimize.size() > 0) {
						betinfo_hide_btn.setSelected(true);
						betinfo_optimize_hide_btn.setSelected(true);
						ll_divider.setVisibility(View.GONE);

						betinfo_hide_btn.setClickable(false);
						betinfo_optimize_hide_btn.setClickable(false);
					} else {
						betinfo_hide_btn.setSelected(false);
						betinfo_hide_btn.setClickable(false);
					}
					tv_secretMsg.setText(secretMsg);
				}
				tv_count.setText(list_show.size() + "场");
				if (!TextUtils.isEmpty(list_show.get(0).getPassType())) {
					tv_orderType.setVisibility(View.VISIBLE);
					tv_orderType.setText("过关方式："
							+ list_show.get(0).getPassType());
				}
				break;

			case 100:
				intent = new Intent(MyCommonLotteryInfo_jc.this,
						Select_JCZQ_Activity.class);
				MyCommonLotteryInfo_jc.this.startActivity(intent);
				break;

			case 110:
				intent = new Intent(MyCommonLotteryInfo_jc.this,
						Select_JCLQ_Activity.class);
				intent.putExtra("from", "MyCommonLotteryInfo_jc");
				MyCommonLotteryInfo_jc.this.startActivity(intent);
				break;
			}
		}

		private void setOptimizeData() {
			buyContents = new ArrayList<List<String>>();
			results = new ArrayList<List<String>>();
			markReds = new ArrayList<List<Integer>>();
			String buyContentStr = null;
			String resultStr = null;
			String markRedStr = null;

			for (int i = 0; i < list_show_optimize.size(); i++) {
				buyContent = new ArrayList<String>();
				result = new ArrayList<String>();
				markRed = new ArrayList<Integer>();
				// 处理投注内容
				buyContentStr = list_show_optimize.get(i).getBuyContent()
						.trim();
				String[] buyContentStrs = buyContentStr.split(",");
				for (int j = 0; j < buyContentStrs.length; j++) {
					buyContent.add(buyContentStrs[j]);
				}
				buyContents.add(buyContent);
				// 处理投注结果
				resultStr = list_show_optimize.get(i).getResult().trim();
				String[] resultStrs = resultStr.split(",");
				for (int j = 0; j < resultStrs.length; j++) {
					result.add(resultStrs[j]);
				}
				results.add(result);
				// 处理标红信息
				markRedStr = list_show_optimize.get(i).getMarkRed().trim();
				String[] markRedStrs = markRedStr.split(",");
				for (int j = 0; j < markRedStrs.length; j++) {
					markRed.add(Integer.parseInt(markRedStrs[j]));
				}
				markReds.add(markRed);
			}
		}

		private void setData() {
			// 开始优化
			playtypesList = new ArrayList<List<String>>();
			playLetList = new ArrayList<List<String>>();
			playResultList = new ArrayList<List<String>>();
			playitemsList = new ArrayList<List<List<Map<String, Object>>>>();
			for (int i = 0; i < list_show.size(); i++) {
				setMixedBetingType(list_show.get(i));
				playtypesList.add(playtypes);
				playLetList.add(playLet);
				playResultList.add(playResult);
				playitemsList.add(playitems);
			}
		}
	}

	/**
	 * 界面布局适配器
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
			int id = showDtmatch.getPlayType();
			holder.orderinfo_jc_title
					.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
			holder.tv_time.setText(Html.fromHtml(showDtmatch.getMatchNumber()
					.substring(0, 2)
					+ "<br>"
					+ showDtmatch.getMatchNumber().substring(2, 5)));
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

			// TODO 为足彩时传入值为null 可以进行优化
			holder.orderinfo_jc_listview
					.setAdapter(new OrderInfoJcPlayTypeAdapter(
							MyCommonLotteryInfo_jc.this, playtypesList
									.get(position), playResultList
									.get(position), playitemsList.get(position)));

			return view;
		}
	}

	/**
	 * 奖金优化的Adapter
	 * 
	 * @author lenovo
	 * 
	 */
	// 这个是修改后的
	class MyAdapter_optimize extends BaseAdapter {
		private Context context;

		public MyAdapter_optimize(Context _context) {
			this.context = _context;
		}

		@Override
		public int getCount() {
			return list_show_optimize.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_show_optimize.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder_Optimize holder;
			ShowDtMatchOptimize showDtmatch = list_show_optimize.get(position);
			if (null == view) {
				holder = new ViewHolder_Optimize();
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.orderinfo_jc_item_52_optimize,
						null);
				holder.orderinfo_jc_title_optimize = (LinearLayout) view
						.findViewById(R.id.orderinfo_jc_title_optimize);
				holder.tv_type_optimize = (TextView) view
						.findViewById(R.id.tv_type_optimize);
				holder.tv_invest_optimize = (TextView) view
						.findViewById(R.id.tv_invest_optimize);
				holder.orderinfo_jc_listview_optimize = (MyListView2) view
						.findViewById(R.id.orderinfo_jc_listview_optimize);
				view.setTag(holder);
			} else {
				holder = (ViewHolder_Optimize) view.getTag();
			}

			holder.orderinfo_jc_title_optimize
					.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
			holder.tv_type_optimize.setText(showDtmatch.getGgWay());
			holder.tv_invest_optimize.setText(showDtmatch.getInvestNum() + "注");

			// 进行了数据处理的优化
			holder.orderinfo_jc_listview_optimize
					.setAdapter(new OrderInfoJcPlayTypeAdapter_optimize(
							MyCommonLotteryInfo_jc.this, buyContents
									.get(position), results.get(position),
							markReds.get(position)));

			return view;
		}
	}

	/**
	 * 奖金优化的布局控件类
	 * 
	 * @author lenovo
	 * 
	 */
	private class ViewHolder_Optimize {
		LinearLayout orderinfo_jc_title_optimize;
		TextView tv_type_optimize, tv_invest_optimize;
		MyListView2 orderinfo_jc_listview_optimize;
	}

	/**
	 * 普通投注的布局控件类
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
	 * 公用监听
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_touzhu:// 去往本彩种投注
			String lotteryID = scheme.getLotteryID();
			goToSelectLottery(lotteryID);
			break;
		case R.id.betinfo_hide_btn:
		case R.id.ll_numberCount:
			if (!betinfo_hide_btn.isSelected()) {
				betinfo_hide_btn.setSelected(true);
				ll_divider.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);

			} else {
				betinfo_hide_btn.setSelected(false);
				ll_divider.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.betinfo_optimize_hide_btn:
		case R.id.ll_optimize:
			if (!betinfo_optimize_hide_btn.isSelected()) {
				betinfo_optimize_hide_btn.setSelected(true);
				ll_divider1.setVisibility(View.GONE);
				lv_betInfo_optimize.setVisibility(View.GONE);

			} else {
				betinfo_optimize_hide_btn.setSelected(false);
				ll_divider1.setVisibility(View.VISIBLE);
				lv_betInfo_optimize.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 根据彩种id跳转不同选号页面
	 * 
	 * @param lotteryID
	 *            ： 彩种id
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

							if (list_show.get(0).getPassType().contains("单关")) {
								new HallFragment().getBallData(
										MyCommonLotteryInfo_jc.this, 1);
							} else {
								new HallFragment().getBallData(
										MyCommonLotteryInfo_jc.this, 0);
							}
							break;
						} else if (73 == id
								&& "73".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩篮球

							if (list_show.get(0).getPassType().contains("单关")) {
								new HallFragment().getBasketball(
										MyCommonLotteryInfo_jc.this, 1);
							} else {
								new HallFragment().getBasketball(
										MyCommonLotteryInfo_jc.this, 0);
							}
							break;
						} 
					} else {
						MyToast.getToast(MyCommonLotteryInfo_jc.this, "没有对阵信息")
								;
					}
				}
			}
		}
	}

	/**
	 * 将投注的竞彩详情拆分成相应格式的键值对
	 * 
	 * @param dtMatch
	 *            ：对阵详情对象
	 */
	private void setMixedBetingType(ShowDtMatch dtMatch) {
		playtypes = null;
		playtypes = new ArrayList<String>();
		playLet = null;
		playLet = new ArrayList<String>();
		playitems = null;
		playitems = new ArrayList<List<Map<String, Object>>>();
		playResult = null;
		playResult = new ArrayList<String>();

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
							dtMatch.getOdds()[i], item_result,
							dtMatch.getMainLoseball() + "");
					break;
				case 5:
					str_type = "胜平负";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result, "0");
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
							dtMatch.getOdds()[i], item_result,
							dtMatch.getMainLoseball() + "");
					break;
				case 4:
				case 5:
				case 6:
					str_type = "胜平负";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result, "0");
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
				if (!TextUtils.isEmpty(dtMatch.getResult())) {
					if(dtMatch.getResult().contains(",")){
						item_result = dtMatch.getResult().split(",")[i];
					}else{
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
							dtMatch.getOdds()[i], item_result,
							dtMatch.getMainLoseball() + "");
					break;
				case 2:
					str_type = "总进球数";
					items2 = setItems(str_type, items2, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result, "0");
					break;
				case 3:
					str_type = "比分";
					items3 = setItems(str_type, items3, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result, "0");
					break;
				case 4:
					str_type = "半全场";
					items4 = setItems(str_type, items4, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result, "0");
					break;
				case 5:
					str_type = "胜平负";
					items5 = setItems(str_type, items5, dtMatch.getSelect()[i],
							dtMatch.getOdds()[i], item_result, "0");
					break;
				}
			}
			for (int i = 0; i < playtypes.size(); i++) {
//				if (playtypes.get(i).equals("让球")) {
				if (playtypes.get(i).contains("让球")) {
					playitems.add(items1);
				} else if (playtypes.get(i).equals("总进球数")) {
					playitems.add(items2);
				} else if (playtypes.get(i).equals("比分")) {
					playitems.add(items3);
				} else if (playtypes.get(i).equals("半全场")) {
					playitems.add(items4);
				} else {
					playitems.add(items5);
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
				if (!TextUtils.isEmpty(dtMatch.getResult())) {
					if (dtMatch.getResult().contains(",")){
						item_result = dtMatch.getResult().split(",")[i];
					}else{
						item_result = dtMatch.getResult();
					}
				}
				switch (dtMatch.getInvestWay()[i] / 100) {
				case 1: // 胜负
					str_type = "胜负";
					basket_items1 = setItems(str_type, basket_items1,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result, "0");
					break;
				case 2:// 让分
					str_type = "让分";
					basket_items2 = setItems(str_type, basket_items2,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result, dtMatch.getLetScore() + "");
					break;
				case 3:// 胜分差
					str_type = "胜分差";
					basket_items3 = setItems(str_type, basket_items3,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result, "0");
					break;

				case 4:// 大小分
					str_type = "大小分";
					basket_items4 = setItems(str_type, basket_items4,
							dtMatch.getSelect()[i], dtMatch.getOdds()[i],
							item_result, dtMatch.getBigSmallScore() + "");
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
		case 4501: // 足球 让球胜平负
			if (type_normal == null) {
				type_normal = "让球胜平负" + "(" + dtMatch.getMainLoseball() + ")";
				;
			}

		case 4502: // 足球 胜平负
			if (type_normal == null) {
				type_normal = "总进球";
			}

		case 4503: // 足球 比分
			if (type_normal == null) {
				type_normal = "上下单双";
			}
		case 4504: // 足球 总进球
			if (type_normal == null) {
				type_normal = "比分";
			}

		case 4505: // 足球 半全场
			if (type_normal == null) {
				type_normal = "半全场";
			}
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

		case 7207: // 足球 胜平负
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
			List<Map<String, Object>> items = null;
			String let = "0";
			if (type_normal.equals("让球")) {
				let = dtMatch.getMainLoseball() + "";
			} else if (type_normal.equals("让分")) {
				let = dtMatch.getLetScore() + "";
			} else if (type_normal.equals("大小分")) {
				let = dtMatch.getBigSmallScore() + "";
			}
			for (int i = 0; i < dtMatch.getInvestWay().length; i++) {
				items = setItems(type_normal, items, dtMatch.getSelect()[i],
						dtMatch.getOdds()[i], dtMatch.getResult(), let);
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
	private List<Map<String, Object>> setItems(String playtypename,
			List<Map<String, Object>> items, String select, double odd,
			String result, String concedePoints) {
		if (!playtypes.contains(playtypename)) {
			playtypes.add(playtypename);
			playResult.add(TextUtils.isEmpty(result) ? "待定" : result);
			playLet.add(concedePoints);
			items = new ArrayList<Map<String, Object>>();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("select", select);
		map.put("odd", odd);
		items.add(map);
		return items;
	}

}
