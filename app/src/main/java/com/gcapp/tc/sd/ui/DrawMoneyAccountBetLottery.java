package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.gcapp.tc.dataaccess.AccountDetails;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.sd.ui.adapter.AccountDeatialAdapter;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;

/**
 * 功能：账户明细提现模块
 * 
 * @author lenovo
 * 
 */
public class DrawMoneyAccountBetLottery implements OnScrollListener {
	private final static String TAG = "DrawMoneyAccountBetLottery";
	private View view;
	private PullToRefreshListView all_listView;
	// private CenterAccountLotteryAdapter adapter;
	private MyAllAccountLotteryActivity context;
	private TextView tv_title;

	private MyHandler myHandler;
	private List<List<Schemes>> listAllSchemes = new ArrayList<List<Schemes>>();
	private List<String> list = new ArrayList<String>();
	private int pageIndex = 1; // 页码
	private int pageSize = 20;
	/** 要更改的 **/
	private ProgressBar pb;
	private int isEnd = 0;

	// 最后可见条目的索引
	private int lastVisibleIndex;
	// 新增参数
	private int searchCondition = 4;

	public int year;
	public int month;
	public int day;

	private AccountDeatialAdapter deatialAdapter;

	private final List<AccountDetails> mAccountData = new ArrayList<AccountDetails>();
	private String myMsg;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            ：上下文对象
	 * @param v
	 *            ：视图
	 */
	public DrawMoneyAccountBetLottery(MyAllAccountLotteryActivity context,
			View v) {
		this.view = v;
		this.context = context;
		myHandler = new MyHandler();
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            :上下文对象
	 * @param v
	 * @param year
	 *            :年
	 * @param month
	 *            ：月
	 * @param day
	 *            ：日
	 */
	public DrawMoneyAccountBetLottery(MyAllAccountLotteryActivity context,
			View v, int year, int month, int day) {
		this.view = v;
		this.context = context;
		myHandler = new MyHandler();
		this.year = year;
		this.month = month;
		this.day = day;
	}

	/**
	 * 初始化变量和界面
	 */
	public void init() {
		all_listView = (PullToRefreshListView) view
				.findViewById(R.id.ptr_MyAccount);
		all_listView.setMode(Mode.BOTH);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("正在加载中..");
		tv_title.setVisibility(View.VISIBLE);

		/** 要更改的 新加的加载图片 **/

		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();
		getAccountData();
		all_listView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {

						// 下拉刷新
						all_listView
								.getLoadingLayoutProxy(true, false)
								.setLastUpdatedLabel(
										"最近更新: "
												+ LotteryUtils
														.Long2DateStr_detail(System
																.currentTimeMillis()));
						if (NetWork.isConnect(context)) {

							// if (myAsynTask != null) {
							// myAsynTask.cancel(true);
							// }
							mAccountData.clear();
							pageIndex = 1;
							getAccountData();

							// myAsynTask = new MyAsynTask();
							// myAsynTask.execute();
						} else {
							MyToast.getToast(context, "网络连接异常，请检查网络");
							all_listView.onRefreshComplete();
						}

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载
						if (NetWork.isConnect(context)) {
							// if (myAsynTask != null) {
							// myAsynTask.cancel(true);
							// }
							pageIndex++;
							getAccountData();
							// myAsynTask = new MyAsynTask();
							// myAsynTask.execute();
						} else {
							MyToast.getToast(context, "网络连接异常，请检查网络");
							all_listView.onRefreshComplete();
						}
					}
				});

	}

	/**
	 * 提交提款账户信息请求
	 */
	public void getAccountData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在加载中...") {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "提款账户明细请求结果：" + jsonObject);

				try {
					if (jsonObject != null
							&& jsonObject.optString("error").equals("0")) {
						String detailsTable = jsonObject
								.optString("detailsTable");

						JSONObject content = new JSONObject(detailsTable);
						JSONArray array = content.getJSONArray("details");

						if (array.length() > 0) {
							for (int i = 0; i < array.length(); i++) {
								JSONObject item = array.getJSONObject(i);
								AccountDetails Info = new AccountDetails();
								String detailTime = item.optString("dateTime");
								if (null != detailTime
										&& !"".equals(detailTime)) {
									Info.setDate(detailTime.split(" ")[0]);
									Info.setTime(detailTime.split(" ")[1]);
								}
								Info.setBetType(item.optString("typeNmae"));
								Info.setMoney(item.optDouble("money"));
								Info.setImg_type(item.optInt("OperatorType"));
								Info.setStatus(item.optString("status"));
								mAccountData.add(Info);
							}
						}
					} else {
						myMsg = jsonObject.optString("msg");
					}

					tv_title.setVisibility(View.GONE);
					deatialAdapter = new AccountDeatialAdapter(context,
							mAccountData, searchCondition);
					all_listView.setAdapter(deatialAdapter);
					all_listView.onRefreshComplete();

				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (jsonObject.toString().equals("-500")) {
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

		requestUtil.getAccoutInfo(searchCondition, pageIndex, pageSize, year
				+ "-" + month + "-" + day + " 00:00:00");
	}

	/**
	 * 进行页面刷新
	 * 
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// case 0:
			// //getThreeMonth();
			// // adapter.setDate(list, listAllSchemes);
			// adapter.notifyDataSetChanged();
			// all_listView.setOnScrollListener(AllAccountBetLottery.this);
			// System.out.println("getTotalCount111" + getTotalCount());
			// if (getTotalCount() % 30 != 0) {
			// myHandler.sendEmptyMessage(-1);
			// }
			// if (!LotteryUtils.checkThreeMonth(list)) {
			// myHandler.sendEmptyMessage(-1);
			// }
			// break;
			// case -1:
			// all_listView.removeFooterView(ll);
			// all_listView.setOnScrollListener(null);
			// break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 得到所有的信息数量
	 * 
	 * @return
	 */
	private int getTotalCount() {
		int total = 0;
		for (List<Schemes> list : listAllSchemes) {
			total += list.size();
			System.out.println(" list.size()" + list.size());
		}
		return total;
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
				&& lastVisibleIndex == deatialAdapter.getCount()) {
			if (isEnd != getTotalCount()) {
				pageIndex++;
				isEnd = getTotalCount();
				getAccountData();
				// myAsynTask = new MyAsynTask();
				// myAsynTask.execute();
			} else {
				myHandler.sendEmptyMessage(-1);
			}
		}
	}

	/**
	 * 刷新数据
	 * 
	 * @param year
	 *            ：年份参数
	 * @param month
	 *            ：月
	 * @param day
	 *            ：日
	 */
	public void Refresh(int year, int month, int day) {
		// if (null != myAsynTask) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.pageIndex = 1;
		// myAsynTask.cancel(true);
		mAccountData.clear();
		deatialAdapter.notifyDataSetChanged();
		getAccountData();
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();
		// }
	}
}
