package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.CenterLotteryAdapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;

/***
 * 功能：所有追号的投注信息
 * 
 * @author Administrator
 */
public class FollowBetLottery implements OnScrollListener {
	private final static String TAG = "FollowBetLottery";
	private View view;
	private PullToRefreshListView listView;
	private CenterLotteryAdapter adapter;
	private Context context;
	private TextView tv_title;

	private MyHandler myHandler;
	private List<List<Schemes>> listAllSchemes = new ArrayList<List<Schemes>>();
	private List<String> list = new ArrayList<String>();

	private String opt, auth, info, time, imei, crc, key; // 格式化后的参数

	private int size = 12;
	private int pageIndex = 1; // 页码
	private int pageSize = size; // 每页显示行数

	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;
	private int isEnd = 0;

	/** 最后可见条目的索引 */
	private int lastVisibleIndex;
	/** 是否弹窗 */
	private int mark = 1;
	/** 是否清空 */
	private int refreshFlag = 0;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            ：上下文对象
	 * @param v
	 *            ：视图对象
	 */
	public FollowBetLottery(Context context, View v) {
		this.view = v;
		this.context = context;
		myHandler = new MyHandler();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(context);
		key = AppTools.key;
	}

	/**
	 * 初始化数据
	 */
	public void init() {
		listView = (PullToRefreshListView) view
				.findViewById(R.id.ptr_MyAccount);
		tv_title = (TextView) view.findViewById(R.id.tv_title);

		listView.setOnScrollListener(FollowBetLottery.this);

		listView.setMode(Mode.BOTH);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("正在加载中..");
		tv_title.setVisibility(View.VISIBLE);

		/** 要更改的 新加的加载图片 **/
		ll = new LinearLayout(context);
		pb = new ProgressBar(context);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		/** 要加在 setAdapter之前 **/

		adapter = new CenterLotteryAdapter(context, list, listAllSchemes,
				new MyItemCLickListener());
		listView.setAdapter(adapter);
		getBetLotteryData();

		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				listView.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
				if (NetWork.isConnect(context)) {
//					listAllSchemes.clear();
//					list.clear();
					refreshFlag = 1;
					pageIndex = 1;
					mark = 0;
					getBetLotteryData();

				} else {
					MyToast.getToast(context, "网络连接异常，请检查网络");
					listView.onRefreshComplete();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载
				if (NetWork.isConnect(context)) {
					pageIndex++;
					mark = 0;
					getBetLotteryData();
				} else {
					MyToast.getToast(context, "网络连接异常，请检查网络");
					listView.onRefreshComplete();
				}
			}
		});
	}

	private List<Schemes> listDetail;

	/**
	 * 提交我的追号记录请求
	 */
	public void getBetLotteryData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在加载中...",mark) {
			@Override
			public void responseCallback(JSONObject item) {
				boolean falg;
				if(refreshFlag == 1){
					listAllSchemes.clear();
					list.clear();
				}
				if (RequestUtil.DEBUG)
					Log.i(TAG, "我的追号请求结果：" + item);
				try {
					if ("0".equals(item.optString("error"))) {
						JSONArray array = new JSONArray(
								item.optString("chaseTaskDetailsList"));
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							JSONArray array2 = new JSONArray(
									object.getString("chaseTaskDetail"));
							
							if (!list.contains(object.getString("date").split(" ")[0])) {
								falg = false;
								list.add(object.getString("date").split(" ")[0]);
								listDetail = new ArrayList<Schemes>();
							} else
								falg = true;
							Schemes scheme;
							for (int j = 0; j < array2.length(); j++) {
								JSONObject items = array2.getJSONObject(j);
								scheme = new Schemes();
								scheme.setId(items.optString("id"));
								scheme.setChaseTaskID(items.optInt("id"));
								scheme.setInitiateUserID("userID");
								scheme.setDateTime(items
										.optString("chaseDateTime"));
								scheme.setLotteryID(items
										.optString("lotteryID"));
								scheme.setQuashStatus(items
										.optInt("quashStatus"));
								scheme.setLotteryNumber(items
										.optString("lotteryNumber"));
								scheme.setDetailMoney(items
										.optDouble("detailMoney"));

								scheme.setHandselMoney(items
										.optDouble("handselMoney"));

								scheme.setMoney(items
										.getDouble("sumChaseMoney"));
								scheme.setSumSchemeMoney(items
										.getDouble("sumSchemeMoney"));
								scheme.setWinMoneyNoWithTax(items
										.optDouble("winMoneyNoWithTax"));
								scheme.setShareWinMoney(items
										.optDouble("shareWinMoney"));
								scheme.setWinMoney(items
										.optDouble("Winmoney"));
								scheme.setStopWhenWinMoney(items
										.optDouble("stopWhenWinMoney"));
								scheme.setLotteryName(items
										.optString("lotteryName"));
								scheme.setInitiateName(items
										.optString("userName"));
								scheme.setSchemeIsOpened(items
										.optString("isOpened"));
								scheme.setSumChaseCount(items
										.optInt("sumChaseCount"));
								scheme.setIsPurchasing("True");
								scheme.setIsChase(1);
								scheme.setSchemeStatus(items
										.optString("schemeStatus"));
								listDetail.add(scheme);
							}
							if (!falg)
								listAllSchemes.add(listDetail);
						}

					}
				} catch (Exception e) {
					Log.i("x", "错误信息     " + e.getMessage());
				}

				listView.onRefreshComplete();
				tv_title.setVisibility(View.GONE);

				adapter.setDate(list, listAllSchemes);
				adapter.notifyDataSetChanged();
				listView.setOnScrollListener(FollowBetLottery.this);

				if (item.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e("", "请求报错" + error.getMessage());
			}
		};
		requestUtil.getBetInfo_Chase(AppTools.lotteryIds, pageIndex, pageSize);
	}

	/**
	 * 进行页面刷新
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				adapter.setDate(list, listAllSchemes);
				adapter.notifyDataSetChanged();
				listView.setOnScrollListener(FollowBetLottery.this);
				if (getTotalCount() < 20)
					myHandler.sendEmptyMessage(-1);

				if (!LotteryUtils.checkThreeMonth(list)) {
					myHandler.sendEmptyMessage(-1);
				}
				break;

			default:
				listView.setOnScrollListener(null);
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 得到数据量
	 * 
	 * @return
	 */
	private int getTotalCount() {
		int total = 0;
		for (List<Schemes> list : listAllSchemes) {
			total += list.size();
		}
		return total;
	}

	/**
	 * listView的子项点击监听
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemCLickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			TextView tv_id = (TextView) view.findViewById(R.id.tv_id);
			int itemId = Integer.parseInt(tv_id.getText().toString());
			Schemes scheme = listAllSchemes.get(itemId).get(position);
			Intent intent = new Intent(context, MyFollowLotteryInfo.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", scheme);
			context.startActivity(intent);
		}
	}

	/**
	 * 重写滚动事件
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/**
	 * 滑动监听，请求数据
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == adapter.getCount()) {
			if (isEnd != getTotalCount()) {
//				pageIndex++;
				isEnd = getTotalCount();
//				getBetLotteryData();
			} else {
				myHandler.sendEmptyMessage(-1);
			}
		}
	}

}
