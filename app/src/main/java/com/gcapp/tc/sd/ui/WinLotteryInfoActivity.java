package com.gcapp.tc.sd.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.WinDetail;
import com.gcapp.tc.dataaccess.WinLottery;
import com.gcapp.tc.sd.ui.adapter.WinLotteryInfoExpandAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.SlideView;
import com.gcapp.tc.view.SlideView.SlideListener;

/**
 * 功能：开奖大厅的普通彩种的开奖详情模块
 * 
 * @author echo
 */
public class WinLotteryInfoActivity extends Activity {
	private static final String TAG = "WinLotteryInfoActivity";
	private Context context = WinLotteryInfoActivity.this;
	private PullToRefreshExpandableListView listView;
	private String myMsg;
	private MyHandler handler;
	private List<WinLottery> listWinLottery = new ArrayList<WinLottery>();
	private String opt;
	private String auth;
	private String info;
	private int pageIndex = 1; // 查询页码
	private int pageSize = 10; // 查询条数
	private String lotteryId;
	private ImageButton btn_back;
	private SlideView slide_touzhu;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_win_lotteryinfo);
		App.activityS.add(this);
		lotteryId = this.getIntent().getStringExtra("lotteryId");
		findView();
		setListener();
		getWinBetInfo();
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		handler = new MyHandler();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		listView = (PullToRefreshExpandableListView) this
				.findViewById(R.id.win_listView_lottery);
		slide_touzhu = (SlideView) findViewById(R.id.slide_touzhu);
		slide_touzhu.setSlideListener(new SlideListener() {
			@Override
			public void onDone() {
				Lottery mLottery = AppTools.getLotteryById(lotteryId);
				if (mLottery != null) {
					AppTools.lottery = mLottery;
					LotteryUtils.goToSelectLottery(context, lotteryId);
				} else {
					MyToast.getToast(context, "该奖期已结束，请等下一期");
					slide_touzhu.reset();
					return;
				}
			}
		});

		listView.getRefreshableView().setGroupIndicator(null);
		TextView mHall_tv_advertisement = (TextView) this
				.findViewById(R.id.hall_tv_advertisement);
		String titleText = setTitleText(lotteryId);
		mHall_tv_advertisement.setText(titleText);
	}

	@Override
	protected void onResume() {
		slide_touzhu.reset();
		super.onResume();
	}

	/**
	 * 设置界面的标题
	 * 
	 * @param name
	 *            :彩种ID
	 * @return
	 */
	private String setTitleText(String name) {
		String lotteryNameString;
		lotteryNameString = LotteryUtils.getTitleText(name);
		return lotteryNameString + "开奖详情";
	}

	/**
	 * 绑定监听
	 */
	@SuppressWarnings("unchecked")
	private void setListener() {
		listView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				listView.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ Long2DateStr_detail(System
												.currentTimeMillis()));
				if (NetWork.isConnect(context)) {
					pageIndex = 1;
					// initHttpRes();
					getWinBetInfo();
				} else {
					MyToast.getToast(getApplicationContext(), "网络连接异常，请检查网络")
							;
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (NetWork.isConnect(context)) {
					pageIndex++;
					// initHttpRes();
					getWinBetInfo();
				} else {
					MyToast.getToast(getApplicationContext(), "网络连接异常，请检查网络")
							;
				}
			}
		});

		listView.getRefreshableView().setOnGroupExpandListener(
				new ExpandableListView.OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int groupPosition) {
						int length = listView.getRefreshableView().getAdapter()
								.getCount();
						for (int i = 0; i < length; i++) {
							if (i != groupPosition)
								listView.getRefreshableView().collapseGroup(i);
						}
					}
				});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	/**
	 * 格式化时间格式
	 * 
	 * @param time
	 *            ：时间
	 * @return
	 */
	public String Long2DateStr_detail(long time) {
		String format = "yyyy-M-d HH:mm";
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 请求普通彩种的开奖信息
	 */
	public void getWinBetInfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"数据加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "开奖信息的result：" + item);
				String error = "-1";
				try {
					error = item.optString("error");
					myMsg = item.optString("msg");

					if ("0".equals(error)) {
						String array = item.optString("dtWinNumberInfo");
						JSONArray array2 = new JSONArray(array);
						WinLottery winLottery;
						if (pageIndex == 1) {
							listWinLottery = null;
							listWinLottery = new ArrayList<WinLottery>();
						}

						for (int i = 0; i < array2.length(); i++) {
							JSONObject items = array2.getJSONObject(i);
							if (null != items) {
								winLottery = new WinLottery();
								winLottery.setId(items.optString("id"));
								winLottery.setName(items.optString("name"));
								winLottery.setLotteryId(lotteryId);
								winLottery.setEndTime(items
										.getString("EndTime"));
								winLottery.setStateUpdateTime(items
										.getString("StateUpdateTime"));
								String winDetail = items.optString("WinDetail");
								List<WinDetail> listWinDetail = new ArrayList<WinDetail>();
								if (!"".equals(winDetail) && null != winDetail) {
									JSONArray winDe = new JSONArray(winDetail);
									WinDetail wDetail;
									for (int j = 0; j < winDe.length(); j++) {
										wDetail = new WinDetail();

										JSONObject itemsDetail = winDe
												.getJSONObject(j);
										wDetail.setBonusName(itemsDetail
												.optString("bonusName"));
										wDetail.setBonusValue(itemsDetail
												.optString("bonusValue"));
										wDetail.setWinningCount(itemsDetail
												.optInt("winningCount"));
										listWinDetail.add(wDetail);
									}
								} else {
									WinDetail wDetail = new WinDetail();
									wDetail.setBonusName("-");
									wDetail.setBonusValue("-");
									wDetail.setWinningCount(-1);
									listWinDetail.add(wDetail);
								}
								winLottery.setListWinDetail(listWinDetail);
								winLottery.setTotalMoney(items
										.optString("TotalMoney"));
								winLottery.setSales(items.optString("Sales"));
								String winLotteryNumber = items
										.optString("winLotteryNumber");
								if (winLotteryNumber.contains("+")) {
									String[] str = winLotteryNumber
											.split("\\+");
									winLottery.setRedNum(str[0]);
									winLottery.setBlueNum(str[1]);
								} else {
									winLottery.setRedNum(winLotteryNumber);
								}
								listWinLottery.add(winLottery);
							}
						}
						// return "0";
					} else {
						error = "-100";
					}

				} catch (Exception ex) {
					VolleyLog.e("Exception==>%s", ex.getMessage());
					error = "1";
				}
				if (item.toString().equals("-500")) {
					error = "-1";
					MyToast.getToast(context, "连接超时");
				}
				handler.sendEmptyMessage(Integer.parseInt(error));

			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getWinLotteryInfo(-1, lotteryId, pageIndex, pageSize, 1, 0,
				10, "", "");
	}

	/**
	 * 处理界面数据刷新
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (pageIndex == 1
						&& listView.getMode().equals(Mode.PULL_FROM_START)) {
					listView.setMode(Mode.BOTH);
				}
				WinLotteryInfoExpandAdapter mAdapter = new WinLotteryInfoExpandAdapter(
						context, listWinLottery);
				if ("28".equals(lotteryId) || "70".equals(lotteryId)
						|| "66".equals(lotteryId) || "62".equals(lotteryId)
						|| "78".equals(lotteryId) || "83".equals(lotteryId)
						|| "61".equals(lotteryId)) {// 高频彩不展开子项
					mAdapter.setChildrenCount(0);
				}
				listView.getRefreshableView().setAdapter(mAdapter);
				listView.getRefreshableView().expandGroup(0);
				break;
			case 1:
				if (pageIndex != 1) {
					listView.setMode(Mode.PULL_FROM_START);
				}
				MyToast.getToast(context, "没有更多开奖信息");
				break;
			case -1:
				MyToast.getToast(WinLotteryInfoActivity.this, "连接超时");
				break;

			case -100:
				MyToast.getToast(WinLotteryInfoActivity.this, myMsg);
				break;
			}
			if (listWinLottery.size() < pageSize) {
				listView.setMode(Mode.PULL_FROM_START);
			}
			if (listView.isRefreshing()) {
				listView.onRefreshComplete();
			}
			super.handleMessage(msg);
		}
	}
}
