package com.gcapp.tc.fragment;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.gcapp.tc.dataaccess.GreatMan;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.FollowExplainActivity;
import com.gcapp.tc.sd.ui.adapter.GreatManAdapter;
import com.gcapp.tc.sd.ui.adapter.ProgrammeAdapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.AutoMoveTextView;
import com.gcapp.tc.view.DragGridView;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * @author dm 
 * 跟单大厅
 * 修改日期 2017-12-3
 */
@SuppressLint("NewApi")
public class FollowFragment extends Fragment implements OnClickListener,
		OnScrollListener {

	private final static String TAG = "FollowFragment";
	/** 返回*/
	private ImageButton btn_back;
	/** 玩法提示图标*/
	private ImageView iv_up_down;
	/** 玩法*/
	private TextView btn_playtype;
	/** 帮助*/
	private ImageButton btn_help;
	private AutoMoveTextView roll_text;
	public static String Message;
	private Context mContext;
	public static List<Schemes> listSchemes = new ArrayList<Schemes>();
	/** 查询合买的彩种ID*/
	public static String lotteryId;
	/** 判断是否数据已经加载完毕*/
	public int max = 0;
	/** 存储滚轮的下标*/
	public static Integer index;
	private int pageIndex = 1;
	private int pageSize = 10;

	private DragGridView great_man_gridview;
	private List<GreatMan> graetmanlist;
	private GreatMan greatMan;
	private GreatManAdapter manAdapter;
	private ProgrammeAdapter programmeAdapter;
	private PullToRefreshListView programme_list;
	private RelativeLayout no_data_layout;
	private TextView follow_loading;
	private View headerView;
	public List<Schemes> temp_listSchemes = new ArrayList<Schemes>();
	private MySingleton mySingleton;

	private ListView mlistview;
	private String toastMsg = "----暂无中奖信息----";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("myfragment","follow");
		AppTools.setLevelList();
		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View v = localInflater.inflate(R.layout.activity_follow, container,
				false);

		mContext = getActivity();
		initView(v);
		initListener();
		if(temp_listSchemes.size() == 0) {
			initData();
		}
		return v;
	}

	private void initView(View v) {
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.follow_list_header, null);
		great_man_gridview = (DragGridView) headerView
				.findViewById(R.id.great_man_gridview);

		btn_playtype = (TextView) v.findViewById(R.id.btn_playtype);
		iv_up_down = (ImageView) v.findViewById(R.id.iv_up_down);
		btn_help = (ImageButton) v.findViewById(R.id.btn_help);
		btn_back = (ImageButton) v.findViewById(R.id.btn_back);
		roll_text = (AutoMoveTextView) headerView.findViewById(R.id.roll_text);
		programme_list = (PullToRefreshListView) v
				.findViewById(R.id.programme_list);
		no_data_layout = (RelativeLayout) v.findViewById(R.id.no_data_layout);
		follow_loading = (TextView) v.findViewById(R.id.follow_loading);
		mlistview = programme_list.getRefreshableView();
		mlistview.addHeaderView(headerView);
		programme_list.setMode(Mode.BOTH);

		btn_playtype.setText("跟单");
		iv_up_down.setVisibility(View.GONE);
		btn_help.setVisibility(View.VISIBLE);
		btn_back.setVisibility(View.GONE);
	}

	private void initListener() {
		btn_help.setOnClickListener(this);
		btn_help.setImageResource(R.drawable.explain_follow);
		programme_list
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						programme_list
								.getLoadingLayoutProxy(true, false)
								.setLastUpdatedLabel(
										"最近更新: "
												+ LotteryUtils
														.Long2DateStr_detail(System
																.currentTimeMillis()));
						if (NetWork.isConnect(mContext)) {
							graetmanlist.clear();
							temp_listSchemes.clear();
							pageIndex = 1;
							initData();
						} else {
							MyToast.getToast(mContext, "网络连接异常，请检查网络");
							programme_list.onRefreshComplete();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载
						if (NetWork.isConnect(mContext)) {
							pageIndex++;
							initData();
						} else {
							MyToast.getToast(mContext, "网络连接异常，请检查网络");
							programme_list.onRefreshComplete();
						}
					}
				});
	}

	private void initData() {
		graetmanlist = new ArrayList<GreatMan>();
		getGreatManData();

		mySingleton = MySingleton.getInstance(mContext);
		getBuyData(true);
		getWinMessage();
	}

	/**
	 * 初始化大神list的adapter
	 */
	private void initAdapter() {
		if (graetmanlist.size() != 0) {
			manAdapter = new GreatManAdapter(mContext, graetmanlist);
			great_man_gridview.setAdapter(manAdapter);
		} else {
			headerView.setVisibility(View.GONE);
			programme_list.setVisibility(View.GONE);
			follow_loading.setVisibility(View.GONE);
			no_data_layout.setVisibility(View.VISIBLE);
		}
	}

	/** 获取大神数据 */
	private void getGreatManData() {
		RequestUtil requestUtil = new RequestUtil(mContext, true,
				Request.CONFIG_CACHE_SHORT, true, "加载中...") {

			@Override
			public void responseCallback(JSONObject item) {
				JSONArray jsonArray;
				try {
					jsonArray = item.getJSONArray("info");
					for (int i = 0; i < jsonArray.length(); i++) {
						greatMan = new GreatMan();
						JSONObject jsonobject = (JSONObject) jsonArray.opt(i);
						greatMan.setImg(AppTools.url
								+ jsonobject.getString("UserImage"));
						greatMan.setName(jsonobject.getString("UserName"));
						greatMan.setManID(jsonobject.getString("UserID"));
						greatMan.setOrderCount(jsonobject.getString("ScheCount"));
						graetmanlist.add(greatMan);
					}
					if (pageIndex > 1) {
						manAdapter.notifyDataSetChanged();
					} else {
						initAdapter();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void responseError(VolleyError error) {

			}
		};
		requestUtil.getGreatMan();
	}

	/**
	 * 获取跟单的数据
	 * 
	 * @param force
	 *            是否删除之前的缓存
	 */
	private void getBuyData(boolean force) {
		if (NetWork.isConnect(mContext)) {
			String info = RspBodyBaseBean.changeJoin_info(AppTools.lotteryIds,
					pageIndex, pageSize, 4, 0);
			String opt = "14";
			JsonObjectRequest request = new JsonObjectRequest(AppTools.path,
					RequestParams.convertParams(mContext, opt, info),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.i(TAG, "JSONObject response==>" + response);
							programme_list.onRefreshComplete();
							int i = parseJsonObject(response);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							programme_list.onRefreshComplete();
							programme_list.setMode(Mode.PULL_FROM_START);
							RequestParams.convertError(mContext, error, true);
						}
					});
			request.setTag(TAG);
			request.setCacheTime(JsonRequest.CONFIG_CACHE_SHORT);
			if (force) {// 删除缓存
				mySingleton.dropCache(request.getCacheKey());
			}
			mySingleton.addToRequestQueue(request);
		} else {
			MyToast.getToast(mContext, "网络连接异常，请检查网络");
		}
	}

	private int parseJsonObject(JSONObject response) {
		if ("0".equals(response.opt("error"))) {
			if (response.optString("schemeList").equals("[]")) {
				if (pageIndex > 1) {
					MyToast.getToast(mContext, "暂无更多数据啦！");
				} else {
					initProgrammeAdapter();
				}
				return -2;
			}
			JSONArray array;
			try {
				array = new JSONArray(response.optString("schemeList"));
			} catch (JSONException e) {
				VolleyLog
						.e("new JSONArray(response.optString(\"schemeList\")) 出现了 %s",
								e.toString());
				return -1001;
			}
			Schemes schemes;
			for (int i = 0; i < array.length(); i++) {
				JSONObject items;
				try {
					items = array.getJSONObject(i);
				} catch (JSONException e) {
					VolleyLog.e("array.getJSONObject(i)  出现了 %s", e.toString());
					continue;
				}

				schemes = new Schemes();
				schemes.setTotalmoney(items.optDouble("money")); // 总金额
				schemes.setSelfmoney(items.optDouble("mybuySumMoney")); // 自购金额
				schemes.setGreatmanOdds(items.optDouble("winNing")); 
				schemes.setPlaytypeName(items.optString("playName"));// 玩法
				schemes.setRengou_peoplesum(items.optInt("buyCount"));// 认购人数
				schemes.setRengou_money(items.optInt("buySumMoney"));// 购买总金额
				schemes.setFollow_state(items.optString("labState"));// 状态

//				schemes.setIsHide(items.optInt("isHide"));
				schemes.setSecretMsg(items.optString("secretMsg"));
				schemes.setId(items.optString("ID"));
				schemes.setPlayTypeName(items.optString("PassType"));
//				schemes.setCountNotes(items.optString("countNotes"));// 获取玩法注数
				schemes.setEndTime(items.optString("StopSellingTime")
						.substring(5, 16));
				schemes.setLotteryID(items.optString("lotteryID"));
				schemes.setLotteryName(items.optString("lotteryName"));
				schemes.setInitiateUserID(items.optString("initiateUserID"));

				schemes.setInitiateName(items.optString("initiateName"));
				schemes.setMoney(items.optLong("money")); // 中奖金额
				schemes.setShareMoney(items.optInt("shareMoney"));
				schemes.setInitiateImg(AppTools.url
						+ items.optString("HeadUrl"));
//				schemes.setShare(items.optInt("share"));
//				schemes.setIsPurchasing("True");
				schemes.setSurplusShare(items.optInt("surplusShare"));
				schemes.setMultiple(items.optInt("multiple"));
//				schemes.setAssureShare(items.optInt("assureShare"));
				schemes.setAssureMoney(items.optDouble("assureMoney")); // baoerjin
				schemes.setSchemeBonusScale(items.optDouble("schemeBonusScale"));
//				schemes.setSecrecyLevel(items.optInt("secrecyLevel"));
//				schemes.setQuashStatus(items.optInt("quashStatus"));
				schemes.setLevel(items.optInt("level"));
//				schemes.setWinMoneyNoWithTax(items
//						.optDouble("winMoneyNoWithTax"));

				// 以下是防止出现小于1，items.optInt成0的情况。
				schemes.setSchedule((int) Math.floor(items
						.optDouble("schedule")));
//				schemes.setSchemeIsOpened(items.optString("schemeIsOpened"));
				schemes.setTitle(items.optString("title"));
				schemes.setLotteryNumber(items.optString("lotteryNumber"));
				schemes.setPlayTypeID(items.optInt("PlayTypeID"));
//				schemes.setSerialNumber(items.optInt("SerialNumber"));
//				schemes.setRecordCount(items.optInt("RecordCount"));
				schemes.setDescription(items.optString("description"));

				// 这里把buyContent可能为空的可能性去除
				Log.i(TAG, items.optString("buyContent"));
				if (!"".equals(items.optString("buyContent"))
						|| !"[]".equals(items.optString("buyContent"))) {
					JSONArray array_contents;
					try {
						array_contents = new JSONArray(
								items.optString("buyContent"));
					} catch (JSONException e) {
						VolleyLog.e(
								"new JSONArray(items.optString(\"buyContent\"))"
										+ "出现了 %s", e.toString());
						continue;
					}
					List<LotteryContent> contents = new ArrayList<LotteryContent>();
					LotteryContent lotteryContent;
					for (int k = 0; k < array_contents.length(); k++) {
						lotteryContent = new LotteryContent();
						try {
							JSONArray array2 = new JSONArray(
									array_contents.optString(k));
							lotteryContent.setLotteryNumber(array2
									.getJSONObject(0)
									.optString("lotteryNumber"));
							lotteryContent.setPlayType(array2.getJSONObject(0)
									.optString("playType"));
							lotteryContent.setSumMoney(array2.getJSONObject(0)
									.optString("sumMoney"));
							lotteryContent.setSumNum(array2.getJSONObject(0)
									.optString("sumNum"));
							contents.add(lotteryContent);
						} catch (JSONException e) { // 兼容处理
							JSONObject array2 = null;
							try {
								array2 = new JSONObject(
										array_contents.optString(k));
							} catch (JSONException e1) {

							}
							if (array2 != null) {
								lotteryContent.setLotteryNumber(array2
										.optString("lotteryNumber"));
								lotteryContent.setPlayType(array2
										.optString("playType"));
								lotteryContent.setSumMoney(array2
										.optString("sumMoney"));
								lotteryContent.setSumNum(array2
										.optString("sumNum"));
							}
							contents.add(lotteryContent);
						}
					}
					schemes.setBuyContent(contents);
				}
				temp_listSchemes.add(schemes);
			}
			if (pageIndex > 1 && temp_listSchemes.size() != 0) {
				programmeAdapter.notifyDataSetChanged();
			} else {
				initProgrammeAdapter();
			}
			return 0;
		}
		return -1001;
	}

	/**
	 * 初始化大神订单adapter
	 */
	private void initProgrammeAdapter() {
		if (temp_listSchemes.size() != 0 || graetmanlist.size() != 0) {
			programmeAdapter = new ProgrammeAdapter(mContext, temp_listSchemes);
			programme_list.setAdapter(programmeAdapter);
		} else {
			headerView.setVisibility(View.GONE);
			programme_list.setVisibility(View.GONE);
			follow_loading.setVisibility(View.GONE);
			no_data_layout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_help:
			Intent intent = new Intent(mContext, FollowExplainActivity.class);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
	/**
	 * 获取中奖播报信息
	 */
	public void getWinMessage() {
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0) {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "获取中奖播报信息" + reponseJson.toString());
				// 获取中奖播报信息{"error":"-5103","msg":"无中奖信息"}
				if (null != reponseJson && !"".equals(reponseJson)) {
					if (reponseJson.optString("error").equals("0")) {
						FollowFragment.Message = reponseJson.optString("winInfo");
						if (FollowFragment.Message != null
								&& !FollowFragment.Message.equals("")) {
							roll_text.setText(FollowFragment.Message);

						} else {
							roll_text.setText(toastMsg);
						}
					} else {
						roll_text.setText(toastMsg);
					}
				} else {
					roll_text.setText(toastMsg);
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取中奖播报信息失败" + error.getMessage());
				roll_text.setText(toastMsg);
			}
		};
		requestUtil.getWinMessage();
	}

}
