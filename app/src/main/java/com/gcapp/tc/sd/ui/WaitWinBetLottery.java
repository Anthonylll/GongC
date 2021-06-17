package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.CenterLotteryAdapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;

/**
 * 功能：个人中心的我的未开奖彩票记录
 * 
 * @author lenovo
 * 
 */
public class WaitWinBetLottery implements OnScrollListener {
	private final static String TAG = "WaitWinBetLottery";
	private View view;
	private PullToRefreshListView all_listView;
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
	private int sort = 5; // 排序方式
	private int isPurchasing = 3; // 返回类型

	/** 要更改的 **/
	private LinearLayout ll;
	private ProgressBar pb;
	private int isEnd = 0;

	//** 最后可见条目的索引 */
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
	 *            ：布局视图
	 */
	public WaitWinBetLottery(Context context, View v) {
		this.view = v;
		this.context = context;
		myHandler = new MyHandler();
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(context);
		key = AppTools.key;
	}

	/**
	 * 初始化控件和监听事件
	 */
	public void init() {
		all_listView = (PullToRefreshListView) view
				.findViewById(R.id.ptr_MyAccount);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		all_listView.setMode(Mode.BOTH);
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
		all_listView.setAdapter(adapter);

		getBetLotteryData();
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
//							listAllSchemes.clear();
//							list.clear();
							refreshFlag = 1;
							pageIndex = 1;
							mark = 0;
							getBetLotteryData();

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
							pageIndex++;
							mark = 0;
							getBetLotteryData();
						} else {
							MyToast.getToast(context, "网络连接异常，请检查网络");
							all_listView.onRefreshComplete();
						}
					}
				});
	}

	private List<Schemes> listDetail;

	/**
	 * 提交我的未开奖彩票记录请求
	 */
	public void getBetLotteryData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在加载中...",mark) {
			@Override
			public void responseCallback(JSONObject item) {
				if(refreshFlag == 1){
					listAllSchemes.clear();
					list.clear();
				}
				if (RequestUtil.DEBUG)
					Log.i(TAG, "未开奖投注记录请求结果：" + item);
				try {
					if ("0".equals(item.optString("error"))) {
						String schemeList = item.optString("schemeList");
						JSONArray array = new JSONArray(schemeList);
						JSONArray jsonArray2 = new JSONArray(array.toString());
						Schemes scheme;
						boolean falg;
						// 循环得到每个对象
						for (int i = 0; i < jsonArray2.length(); i++) {
							JSONObject items = jsonArray2.getJSONObject(i);
							String date = items.getString("date");
							String mDate = date.split(" ")[0];
							if (!list.contains(mDate)) {
								falg = false;
								list.add(mDate);
								listDetail = new ArrayList<Schemes>();
							} else
								falg = true;
							JSONArray detail = new JSONArray(
									items.getString("dateDetail"));

							for (int j = 0; j < detail.length(); j++) {
								JSONObject items2 = detail.getJSONObject(j);

								scheme = new Schemes();

								scheme.setId(items2.optString("id"));
								scheme.setSchemeNumber(items2
										.optString("schemeNumber"));

								scheme.setAssureMoney(items2
										.optDouble("assureMoney"));
								scheme.setAssureShare(items2
										.optInt("assureShare"));
								scheme.setBuyed(items2.optString("buyed"));
								scheme.setInitiateName(items2
										.optString("initiateName"));
								scheme.setInitiateUserID(items2
										.optString("initiateUserID"));
								scheme.setIsPurchasing(items2
										.optString("isPurchasing"));
								scheme.setDetailMoney(items2
										.optDouble("detailMoney"));
								scheme.setCouponMoney((int)Double.parseDouble(items2.optString("VoucherMoney")));
								Log.e("detialMoney",
										items2.optDouble("detailMoney") + "");
								scheme.setHandselMoney(items2
										.optDouble("handselMoney"));
								Log.e("handselMoney",
										items2.optDouble("handselMoney") + "");

								scheme.setIsuseID(items2.optString("isuseID"));
								scheme.setIsuseName(items2
										.optString("isuseName"));
								scheme.setLevel(items2.optInt("level"));
								scheme.setLotteryID(items2
										.optString("lotteryID"));
								scheme.setLotteryName(items2
										.optString("lotteryName"));
								scheme.setLotteryNumber(items2
										.optString("lotteryNumber"));
								JSONArray array_contents = new JSONArray(
										items2.optString("buyContent"));
								List<LotteryContent> contents = new ArrayList<LotteryContent>();
								LotteryContent lotteryContent = null;
								if (array_contents != null
										&& array_contents.length() != 0) {
									for (int k = 0; k < array_contents.length(); k++) {
										lotteryContent = new LotteryContent();
										try {
											JSONArray array2 = new JSONArray(
													array_contents.optString(k));

											lotteryContent
													.setLotteryNumber(array2
															.getJSONObject(0)
															.optString(
																	"lotteryNumber"));
											lotteryContent.setPlayType(array2
													.getJSONObject(0)
													.optString("playType"));
											lotteryContent.setSumMoney(array2
													.getJSONObject(0)
													.optString("sumMoney"));
											lotteryContent.setSumNum(array2
													.getJSONObject(0)
													.optString("sumNum"));
											contents.add(lotteryContent);
										} catch (Exception e) {
											JSONObject array2 = new JSONObject(
													array_contents.optString(k));

											lotteryContent
													.setLotteryNumber(array2
															.optString("lotteryNumber"));
											lotteryContent.setPlayType(array2
													.optString("playType"));
											lotteryContent.setSumMoney(array2
													.optString("sumMoney"));
											lotteryContent.setSumNum(array2
													.optString("sumNum"));
											contents.add(lotteryContent);
										}
									}
									scheme.setBuyContent(contents);
								}

								scheme.setMoney(items2.optInt("money"));
								scheme.setPlayTypeName(items2
										.optString("playTypeName"));
								scheme.setQuashStatus(items2
										.optInt("quashStatus"));
								scheme.setRecordCount(items2
										.optInt("RecordCount"));
								scheme.setSchedule(items2.optInt("schedule"));
								scheme.setSchemeBonusScale(items2
										.optDouble("schemeBonusScale"));
								scheme.setSchemeIsOpened(items2
										.optString("schemeIsOpened"));
								scheme.setChaseTaskID(items2
										.getInt("chaseTaskID"));
								scheme.setSecrecyLevel(items2
										.optInt("secrecyLevel"));
								scheme.setSerialNumber(items2
										.optInt("SerialNumber"));
								scheme.setShare(items2.optInt("share"));
								scheme.setShareMoney(items2
										.optInt("shareMoney"));
								scheme.setSurplusShare(items2
										.optInt("surplusShare"));
								scheme.setTitle(items2.optString("title"));
								scheme.setWinMoneyNoWithTax(items2
										.optDouble("winMoneyNoWithTax"));
								scheme.setShareWinMoney(items2
										.optDouble("shareWinMoney"));
								scheme.setWinMoney(items2
										.optDouble("Winmoney"));
								scheme.setWinNumber(items2
										.optString("winNumber"));

								scheme.setDateTime(items2.optString("datetime"));

								scheme.setDescription(items2
										.optString("description"));

								scheme.setIsChase(items2.optInt("isChase"));

								scheme.setMultiple(items2.optInt("multiple"));
								try {
									scheme.setFromClient(items2
											.getInt("FromClient"));
								} catch (JSONException e) {
									scheme.setFromClient(items2
											.getInt("fromClient"));
								}
								scheme.setMyBuyMoney(items2
										.getInt("myBuyMoney") + "");

								scheme.setMyBuyShare(items2
										.optInt("myBuyShare"));
								scheme.setSchemeStatus(items2
										.optString("schemeStatus"));
								scheme.setIsHide(items2.optInt("isHide"));
								scheme.setSecretMsg(items2
										.optString("secretMsg"));
								listDetail.add(scheme);
							}
							if (!falg)
								listAllSchemes.add(listDetail);
						}

					}
					// if (this.isCancelled()) {
					// Log.i("x", "取消了异步。。。。");
					// return null;
					// }
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("x", "myAllLottery 错误" + e.getMessage());
				}

				all_listView.onRefreshComplete();
				tv_title.setVisibility(View.GONE);

				adapter.setDate(list, listAllSchemes);
				adapter.notifyDataSetChanged();
				all_listView.setOnScrollListener(WaitWinBetLottery.this);

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
		requestUtil.getBetInfo(AppTools.lotteryIds, pageIndex, pageSize, sort,
				isPurchasing, 2);
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
				// getThreeMonth();
				adapter.setDate(list, listAllSchemes);
				adapter.notifyDataSetChanged();
				all_listView.setOnScrollListener(WaitWinBetLottery.this);
				if (getTotalCount() % 30 != 0)
					myHandler.sendEmptyMessage(-1);
				if (!LotteryUtils.checkThreeMonth(list)) {
					myHandler.sendEmptyMessage(-1);
				}
				break;
			case -1:
				all_listView.setOnScrollListener(null);
				break;
			default:
				all_listView.setOnScrollListener(null);
				break;
			}
			super.handleMessage(msg);
		}
	}

	// private void getThreeMonth() {
	// String now = LotteryUtils.getSchemeTime(System.currentTimeMillis());
	// for (int i = 0; i < list.size(); i++) {
	// if (!LotteryUtils.compareMonth(now, list.get(i))) {
	// list.remove(i);
	// listAllSchemes.remove(i);
	// }
	// }
	// }

	/**
	 * listview的子项点击监听
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
			Intent intent = null;
			if ("False".equalsIgnoreCase(scheme.getIsPurchasing())) {
				if ("72".equals(scheme.getLotteryID())
						|| "73".equals(scheme.getLotteryID())
						|| "45".equals(scheme.getLotteryID())) {
					intent = new Intent(context,
							MyCommonLotteryInfo_joindetail_jc.class);
				} else {
					intent = new Intent(context,
							MyCommonLotteryInfo_joindetail.class);
				}
			} else {
				if (scheme.getIsChase() == 0)
					if ("72".equals(scheme.getLotteryID())
							|| "73".equals(scheme.getLotteryID())
							|| "45".equals(scheme.getLotteryID())) {
						intent = new Intent(context,
								MyCommonLotteryInfo_jc.class);
					} else {
						if (scheme.getIsHide() == 0) {
							intent = new Intent(context,
									MyCommonLotteryInfo.class);
						} else {
							intent = new Intent(context,
									MyCommonLotteryInfo_join.class);
						}
					}
				else
					intent = new Intent(context, MyFollowLotteryInfo.class);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", scheme);
			context.startActivity(intent);
		}
	}

	/**
	 * 得到投注记录数目
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/**
	 * 滑动监听
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == adapter.getCount()) {
			if (isEnd != getTotalCount()) {
				Log.i("x", "滑到最底部");
//				pageIndex++;
				isEnd = getTotalCount();
//				getBetLotteryData();
			} else {
				myHandler.sendEmptyMessage(-1);
			}
		}
	}

}
