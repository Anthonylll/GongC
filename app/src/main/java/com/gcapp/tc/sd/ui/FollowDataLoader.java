package com.gcapp.tc.sd.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.MyFollowAdapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.view.MyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：合买大厅的数据请求和处理
 * 
 * @author lenovo
 * 
 */
public class FollowDataLoader {
	private static final String TAG = "FollowDataLoader";
	private View view;
	private PullToRefreshListView followListView;
	private TextView follow_hint;
	private MyFollowAdapter fAdapter;
	private Context context;
	private int pageIndex = 1;
	private int sort; // 类型 0.进度 1.金额 2.每份金额 3.战绩
	private int sortType; // 排序方式 0.升序 1.降序
	public String lotteryId; // 查询合买的彩种ID
	public int max = 0; // 判断是否数据已经加载完毕
	public List<Schemes> listSchemes = new ArrayList<Schemes>();
	public List<Schemes> temp_listSchemes = new ArrayList<Schemes>();
	private MySingleton mySingleton;
	private LinearLayout ll_show_img;

	/**
	 * 构造方法
	 * 
	 * @param lotteryId
	 *            ：请求的彩种ID
	 * @param context
	 *            ：上下文
	 * @param v
	 *            ：视图
	 * @param sort
	 *            ：类型
	 * @param sortType
	 *            ：排序方式
	 */
	public FollowDataLoader(String lotteryId, Context context, View v,
			int sort, int sortType) {
		this.lotteryId = lotteryId;
		this.sortType = sortType;
		this.view = v;
		this.sort = sort;
		this.context = context;
		init();
		setListener();
		mySingleton = MySingleton.getInstance(context);
		getHttpRes(true);
	}

	/**
	 * 绑定监听
	 */
	@SuppressWarnings("unchecked")
	private void setListener() {
		listSchemes.clear();
		fAdapter.setList(listSchemes);
		followListView.setAdapter(fAdapter);
		followListView.setOnItemClickListener(new listItemClicl());
		followListView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				followListView.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
				listSchemes.clear();
				pageIndex = 1;
				getHttpRes(true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				pageIndex++;
				getHttpRes(true);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	public void init() {
		followListView = (PullToRefreshListView) view
				.findViewById(R.id.followListView);
		follow_hint = (TextView) view.findViewById(R.id.follow_hint);
		ll_show_img = (LinearLayout) view.findViewById(R.id.ll_show_img);
		fAdapter = new MyFollowAdapter(context);
		fAdapter.setList(listSchemes);
		followListView.setAdapter(fAdapter);
	}

	/**
	 * 获取网络的数据
	 * 
	 * @param force
	 *            是否删除之前的缓存
	 */
	private void getHttpRes(boolean force) {
		if (NetWork.isConnect(context)) {
			int pageSize = 20;

			String info = RspBodyBaseBean.changeJoin_info(lotteryId, pageIndex,
					pageSize, sort, sortType);
			System.out.println("+++opt =14++++++++合买info" + info);
			String opt = "14";
			JsonObjectRequest request = new JsonObjectRequest(AppTools.path,
					RequestParams.convertParams(context, opt, info),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.i(TAG, "JSONObject response==>" + response);
							followListView.onRefreshComplete();
							dealResponse(parseJsonObject(response));
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							followListView.onRefreshComplete();
							followListView.setMode(Mode.PULL_FROM_START);
							RequestParams.convertError(context, error, true);
						}
					});
			request.setTag(TAG);
			request.setCacheTime(JsonRequest.CONFIG_CACHE_SHORT);
			if (force) {// 删除缓存
				mySingleton.dropCache(request.getCacheKey());
			}
			mySingleton.addToRequestQueue(request);
		} else {
			MyToast.getToast(context, "网络连接异常，请检查网络");
		}
	}

	/**
	 * 解析获取的JSONObject对象
	 * 
	 * @param response
	 *            获得的JSONObject
	 * @return 返回状态信息 int
	 */
	private int parseJsonObject(JSONObject response) {
		if ("0".equals(response.opt("error"))) {
			if (response.optString("schemeList").equals("[]"))
				return -2;
			JSONArray array;
			try {
				array = new JSONArray(response.optString("schemeList"));
			} catch (JSONException e) {
				VolleyLog
						.e("new JSONArray(response.optString(\"schemeList\")) 出现了 %s",
								e.toString());
				return -1001;
			}
			temp_listSchemes.clear();
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
				schemes.setPlaytypeName(items.optString("playName"));// 玩法
				schemes.setRengou_peoplesum(items.optInt("buyCount"));// 认购人数
				schemes.setRengou_money(items.optInt("buySumMoney"));// 购买总金额
				schemes.setFollow_state(items.optString("labState"));// 状态

				schemes.setIsHide(items.optInt("isHide"));
				schemes.setSecretMsg(items.optString("secretMsg"));
				schemes.setId(items.optString("id"));
				schemes.setPlayTypeName(items.optString("PlayTypeName"));// 获取玩法名称
				schemes.setCountNotes(items.optString("countNotes"));// 获取玩法注数
				schemes.setLotteryID(items.optString("lotteryID"));
				schemes.setLotteryName(items.optString("lotteryName"));
				schemes.setInitiateUserID(items.optString("initiateUserID"));

				// 对人名用***加密
				schemes.setInitiateName(LotteryUtils.addRexStar(items
						.optString("initiateName")));
				schemes.setMoney(items.optLong("money")); // 中奖金额
				schemes.setShareMoney(items.optInt("shareMoney"));
				schemes.setShare(items.optInt("share"));
				schemes.setIsPurchasing("True");
				schemes.setSurplusShare(items.optInt("surplusShare"));
				schemes.setMultiple(items.optInt("multiple"));
				schemes.setAssureShare(items.optInt("assureShare"));
				schemes.setAssureMoney(items.optDouble("assureMoney")); // baoerjin
				schemes.setSchemeBonusScale(items.optDouble("schemeBonusScale"));
				schemes.setSecrecyLevel(items.optInt("secrecyLevel"));
				schemes.setQuashStatus(items.optInt("quashStatus"));
				schemes.setLevel(items.optInt("level"));
				schemes.setWinMoneyNoWithTax(items
						.optDouble("winMoneyNoWithTax"));

				// 以下是防止出现小于1，items.optInt成0的情况。
				schemes.setSchedule((int) Math.floor(items
						.optDouble("schedule")));
				schemes.setSchemeIsOpened(items.optString("schemeIsOpened"));
				schemes.setTitle(items.optString("title"));
				schemes.setLotteryNumber(items.optString("lotteryNumber"));
				schemes.setPlayTypeID(items.optInt("PlayTypeID"));
				schemes.setSerialNumber(items.optInt("SerialNumber"));
				schemes.setRecordCount(items.optInt("RecordCount"));
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
			return 0;
		}
		return -1001;
	}

	/**
	 * 处理解析后的响应
	 * 
	 * @param code
	 *            解析后的状态码
	 */
	private void dealResponse(int code) {
		switch (code) {
		case 0:
			if (pageIndex == 1) {
				listSchemes.clear();
				followListView.setMode(Mode.BOTH);
			}
			for (Schemes scheme : temp_listSchemes) {
				listSchemes.add(scheme);
			}
			fAdapter.setList(listSchemes);
			fAdapter.notifyDataSetChanged();
			ll_show_img.setVisibility(View.GONE);

			if (listSchemes.size() == 0) {
				followListView.setMode(Mode.PULL_FROM_START);
				follow_hint.setText("暂时没有该项数据");
				follow_hint.setVisibility(View.GONE);
			}
			break;
		case 2:
			listSchemes.clear();
			fAdapter.clear();
			fAdapter.notifyDataSetChanged();
			ll_show_img.setVisibility(View.GONE);
			break;
		case -2:
			follow_hint.setText("没有更多数据");
			follow_hint.setVisibility(View.GONE);
			if (pageIndex == 1) {
				ll_show_img.setVisibility(View.VISIBLE);
			}
			
			followListView.setMode(Mode.PULL_FROM_START);
			break;
		default:
			follow_hint.setText("数据加载失败");
			ll_show_img.setVisibility(View.GONE);
			followListView.setMode(Mode.PULL_FROM_START);
			break;
		}
	}

	/**
	 * 合买大厅的每一栏的点击监听
	 * 
	 * @author lenovo
	 * 
	 */
	class listItemClicl implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intents;
			if (fAdapter.getList().size() != 0) {
				Schemes schemes = fAdapter.getList().get(position - 1);
				if ("73".equals(schemes.getLotteryID())
						|| "72".equals(schemes.getLotteryID())
						|| "45".equals(schemes.getLotteryID())) {
					intents = new Intent(context, FollowInfo_jc_Activity.class);
				} else {
					intents = new Intent(context, FollowInfoActivity.class);
				}
				Bundle bundle = new Bundle();
				bundle.putSerializable("schem", schemes);
				intents.putExtra("bundle", bundle);
				context.startActivity(intents);
			} else {
				System.out.println("FollowSchemeActivity点击了。。。。");
			}
		}
	}

	/**
	 * 刷新listview的数据
	 * 
	 * @param sortType
	 *            ：排序方式参数
	 * @param lotteryId
	 *            ：彩种id
	 */
	public void updateListview(int sortType, String lotteryId) {
		this.sortType = sortType;
		this.lotteryId = lotteryId;
		listSchemes.clear();
		finish();
		fAdapter.setList(listSchemes);
		fAdapter.notifyDataSetChanged();
		pageIndex = 1;
		getHttpRes(false);
	}

	/**
	 * 进入页面时刷新
	 */
	public void updateData() {
		followListView.setRefreshing(false);
	}

	/**
	 * 初始化请求框
	 */
	public void finish() {
		follow_hint.setText("正在加载中...");
		if (followListView != null) {
			followListView.onRefreshComplete();
			followListView.setMode(Mode.DISABLED);
		}
	}
}
