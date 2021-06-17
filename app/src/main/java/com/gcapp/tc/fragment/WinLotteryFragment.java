package com.gcapp.tc.fragment;

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
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.LoginActivity;
import com.gcapp.tc.sd.ui.SettingActivity;
import com.gcapp.tc.sd.ui.WinLotteryInfoActivity;
import com.gcapp.tc.sd.ui.WinLottery_jc_Activity;
import com.gcapp.tc.sd.ui.adapter.WinLotteryAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.view.MyToast;

/**
 * 开奖公告
 */
@SuppressLint("NewApi")
public class WinLotteryFragment extends Fragment {
	private String TAG = "WinLotteryFragment";
	private PullToRefreshListView listView;
	private TextView win_lottery_hint;
	private WinLotteryAdapter adapter;
	private Context context;
	private Intent intent;
	private List<Map<String, String>> list_open;
	private List<Map<String, String>> list_open_temp;
	private MySingleton mySingleton;
//	private GifView gif_hotlottery;
	private int hotSaleLotteryId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("myfragment","win");
		setHasOptionsMenu(true);
		View v = inflater.inflate(R.layout.activity_lottery, container, false);
		context = getActivity();
		mySingleton = MySingleton.getInstance(context);
		findView(v);
		setListener();
		return v;
	}

	/**
	 * 初始化UI
	 */
	private void findView(View v) {
		getHttpRes(true);
//		gif_hotlottery = (GifView) v.findViewById(R.id.gif_hotlottery);

		listView = (PullToRefreshListView) v
				.findViewById(R.id.win_lottery_listView);
		win_lottery_hint = (TextView) v.findViewById(R.id.win_lottery_hint);
		list_open = new ArrayList<Map<String, String>>();
		list_open_temp = new ArrayList<Map<String, String>>();
		adapter = new WinLotteryAdapter(context, list_open, getActivity());
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new MyItemsClickListener());
//		gif_hotlottery.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//
//				if (hotSaleLotteryId == 72 || hotSaleLotteryId == 45
//						|| hotSaleLotteryId == 73) {
//					Lottery mLottery = AppTools.getLotteryById(hotSaleLotteryId
//							+ "");
//					if (mLottery == null) {
//						MyToast.getToast(context, "该奖期已结束，请等下一期");
//						return;
//					} else {
//						LotteryUtils.goToSelectLottery_jc(context,
//								hotSaleLotteryId + "");
//					}
//
//				} else {
//					LotteryUtils.goToSelectLottery(context, hotSaleLotteryId
//							+ "");
//				}
//			}
//		});
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				listView.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
				getHttpRes(true);
			}
		});
	}

	/**
	 * 获取网络开奖公告的数据
	 * 
	 * @param force
	 *            ：是否清除缓存参数
	 */
	private void getHttpRes(boolean force) {
		if (NetWork.isConnect(context)) {
			String opt = "13";
			String info = RspBodyBaseBean
					.changeLottery_info(AppTools.lotteryIds);
			JsonObjectRequest request = new JsonObjectRequest(AppTools.path,
					RequestParams.convertParams(context, opt, info),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {

							Log.i(TAG, "返回的开奖信息====>" + response);
							String error = response.optString("error");
							hotSaleLotteryId = response
									.optInt("hotSaleLotteryId");// 热卖彩种ID

//							if (hotSaleLotteryId != 0 && hotSaleLotteryId != -1) {
//								gif_hotlottery
//										.setMovieResource(R.raw.hot_lottery);// 加载热卖彩种
//							}

							int result = parseJsonObject(response);
							if (error.equals("-109")) {
								result = -2;
							}
							listView.onRefreshComplete();

							switch (result) {

							case -1:
								win_lottery_hint.setText("数据解析失败");
								break;
							case 0:
								win_lottery_hint.setText("暂时没有开奖信息");
								break;
							case -2:
								win_lottery_hint.setText("无任何开奖号码信息");
								break;
							case 1:
								list_open.clear();
								sort(list_open_temp);
								adapter.notifyDataSetChanged();
								break;
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							listView.onRefreshComplete();
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

	@Override
	public void onStop() {
		super.onStop();
		if (mySingleton != null) {
			mySingleton.cancelAll(TAG);
		}
	}

	/**
	 * 菜单选项的点击事件
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/** 刷新 **/
		case 1:
			if (NetWork.isConnect(context)) {
				getHttpRes(false);
			} else {
				MyToast.getToast(context, "网络连接异常，请检查网络");
			}
			break;

		/** 设置 **/
		case 2:
			intent = new Intent(context, SettingActivity.class);
			context.startActivity(intent);
			break;

		/** 更改账户 **/
		case 3:
			Intent intent = new Intent(context, LoginActivity.class);
			intent.putExtra("loginType", "genggai");
			context.startActivity(intent);
			break;
		/** 退出 **/
		case 4:
			for (Activity activity : App.activityS) {
				activity.finish();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 重写菜单选项
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		int sysVersion = Integer.parseInt(VERSION.SDK);
		if (sysVersion > 10) {
			menu.add(1, 1, 0, "刷新");
			menu.add(1, 2, 0, "设置");
			menu.add(1, 3, 0, "更换账户");
			menu.add(1, 4, 0, "退出");
		} else {
			menu.add(1, 1, 0, "").setIcon(R.drawable.menu_refresh_select);
			menu.add(1, 2, 0, "").setIcon(R.drawable.menu_setting_select);
			menu.add(1, 3, 0, "").setIcon(R.drawable.menu_changeuser_select);
			menu.add(1, 4, 0, "").setIcon(R.drawable.menu_exit_select);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	/**
	 * ListView 的子项点击监听
	 */
	class MyItemsClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			position = position - 1;
			Map<String, String> item = list_open.get(position);
			if (item.get("isOpen").equals("0")) {
				MyToast.getToast(context, "没有该期开奖信息");
				return;
			}
			if ("72".equals(item.get("lotteryId"))
					|| "73".equals(item.get("lotteryId"))
					|| "45".equals(item.get("lotteryId"))) {
				intent = new Intent(context, WinLottery_jc_Activity.class);
				intent.putExtra("lotteryId", item.get("lotteryId"));
				intent.putExtra("date", item.get("name"));
				context.startActivity(intent);
			} else {
				intent = new Intent(context, WinLotteryInfoActivity.class);
				intent.putExtra("lotteryId", item.get("lotteryId"));
				context.startActivity(intent);
			}
		}
	}

	/**
	 * 解析获取的JSONObject对象
	 * 
	 * @return 返回状态信息 int
	 */
	private int parseJsonObject(JSONObject result) {
		try {
			if (result != null) {
				list_open_temp.clear();
				if(result.has("dtOpenInfo")) {
					JSONArray dtOpenInfo = new JSONArray(
							result.getString("dtOpenInfo"));
					Map<String, String> map = null;

					for (int i = 0; i < dtOpenInfo.length(); i++) {
						map = new HashMap<String, String>();
						map.put("lotteryId",
								dtOpenInfo.getJSONObject(i).optString("lotteryId"));
						map.put("name",
								dtOpenInfo.getJSONObject(i).optString("name"));
						map.put("EndTime",
								dtOpenInfo.getJSONObject(i).optString("EndTime"));

						map.put("winLotteryNumber", dtOpenInfo.getJSONObject(i)
								.optString("winLotteryNumber"));
						map.put("isOpen", "1");
						list_open_temp.add(map);
					}
				}
				JSONArray dtMatch = new JSONArray(result.getString("dtMatch"));
				if (dtMatch != null && dtMatch.length() != 0) {
					Map<String, String> map1 = new HashMap<String, String>();
					map1.put("lotteryId", "72");
					map1.put("result",
							dtMatch.getJSONObject(0).optString("result"));
					map1.put("name",
							dtMatch.getJSONObject(0).optString("matchDate")
									.split(" ")[0]);
					map1.put("matchNumber",
							dtMatch.getJSONObject(0).optString("matchNumber")
									.split(" ")[0]);
					map1.put("mainTeam",
							dtMatch.getJSONObject(0).optString("mainTeam"));
					map1.put("guestTeam",
							dtMatch.getJSONObject(0).optString("guestTeam"));
					map1.put("matchId",
							dtMatch.getJSONObject(0).optString("matchId")
									.split(" ")[0]);
					map1.put("isOpen", "1");
					list_open_temp.add(map1);
				}

				JSONArray dtMatchBasket = new JSONArray(
						result.getString("dtMatchBasket"));
				if (dtMatchBasket != null && dtMatchBasket.length() != 0) {
					Map<String, String> map2 = new HashMap<String, String>();
					map2.put("lotteryId", "73");
					map2.put("result", dtMatchBasket.getJSONObject(0)
							.optString("result"));
					map2.put(
							"name",
							dtMatchBasket.getJSONObject(0)
									.optString("matchDate").split(" ")[0]);
					map2.put("matchNumber", dtMatchBasket.getJSONObject(0)
							.optString("matchNumber").split(" ")[0]);
					map2.put("mainTeam", dtMatchBasket.getJSONObject(0)
							.optString("mainTeam"));
					map2.put("guestTeam", dtMatchBasket.getJSONObject(0)
							.optString("guestTeam"));
					map2.put("matchId", dtMatchBasket.getJSONObject(0)
							.optString("matchId").split(" ")[0]);
					map2.put("isOpen", "1");
					list_open_temp.add(map2);
				}

				JSONArray dtMatch_bjdc = new JSONArray(
						result.getString("dtMatchBjSing"));
				if (dtMatch_bjdc != null && dtMatch_bjdc.length() != 0) {
					Map<String, String> map3 = new HashMap<String, String>();
					map3.put("lotteryId", "45");
					map3.put("result",
							dtMatch_bjdc.getJSONObject(0).optString("result"));
					map3.put("name",
							dtMatch_bjdc.getJSONObject(0)
									.optString("matchDate").split(" ")[0]);
					map3.put("mainTeam", dtMatch_bjdc.getJSONObject(0)
							.optString("mainTeam"));
					map3.put("guestTeam", dtMatch_bjdc.getJSONObject(0)
							.optString("guestTeam"));
					map3.put("matchNumber", dtMatch_bjdc.getJSONObject(0)
							.optString("matchNumber"));
					map3.put("isOpen", "1");
					list_open_temp.add(map3);
				}
				addOther();
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 添加 除去竞彩的其他彩种的开奖信息
	 */
	private void addOther() {
		Map<String, String> map = null;
		for (Lottery lottery : HallFragment.listLottery) {
			boolean contain = false;
			for (int i = 0; i < list_open_temp.size(); i++) {
				if (list_open_temp.get(i).get("lotteryId")
						.equals(lottery.getLotteryID())) {
					contain = true;
					break;
				}
			}
			if (!contain) {
				map = new HashMap<String, String>();
				map.put("lotteryId", lottery.getLotteryID() == null ? "0"
						: lottery.getLotteryID());
				map.put("name", lottery.getLastIsuseName() == null ? ""
						: lottery.getLastIsuseName());
				map.put("winLotteryNumber",
						lottery.getLastWinNumber() == null ? "" : lottery
								.getLastWinNumber());
				map.put("isOpen", "0");
				list_open_temp.add(map);
			}
		}
	}

	/**
	 * 将开奖公告的彩种按顺序排列
	 * 
	 * @param temp
	 *            需要排序的list
	 */
	private void sort(List<Map<String, String>> temp) {
		String[] sortByLotteryId = AppTools.lotteryIds.replace(" ", "").split(
				",");
		if (temp != null) {
			for (int i = 0; i < sortByLotteryId.length; i++) {
				for (int j = 0; j < temp.size(); j++) {
					if (sortByLotteryId[i].equals(temp.get(j).get("lotteryId"))) {
						list_open.add(temp.get(j));
					}
				}
			}
		}
	}
}
