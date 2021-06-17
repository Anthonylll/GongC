package com.gcapp.tc.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gcapp.tc.dataaccess.DtMatch;
import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.dataaccess.ImageViews;
import com.gcapp.tc.dataaccess.Information;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.sd.ui.ApplyActivity;
import com.gcapp.tc.sd.ui.InformationActivity;
import com.gcapp.tc.sd.ui.Information_Detail;
import com.gcapp.tc.sd.ui.LiveBasketballActivity;
import com.gcapp.tc.sd.ui.LiveMatchActivity;
import com.gcapp.tc.sd.ui.LoginActivity;
import com.gcapp.tc.sd.ui.LuckyCenterActivity;
import com.gcapp.tc.sd.ui.MainActivity;
import com.gcapp.tc.sd.ui.Select_11x5_Activity;
import com.gcapp.tc.sd.ui.Select_DLT_Activity;
import com.gcapp.tc.sd.ui.Select_JCLQ_Activity;
import com.gcapp.tc.sd.ui.Select_JCLQ_DAN_Activity;
import com.gcapp.tc.sd.ui.Select_JCZQ_Activity;
import com.gcapp.tc.sd.ui.Select_JCZQ_DAN_Activity;
import com.gcapp.tc.sd.ui.Select_K3_Activity;
import com.gcapp.tc.sd.ui.Select_RX9_Activity;
import com.gcapp.tc.sd.ui.Select_SFC_Activity;
import com.gcapp.tc.sd.ui.Select_SSC_Activity;
import com.gcapp.tc.sd.ui.Select_SSQ_Activity;
import com.gcapp.tc.sd.ui.SettingActivity;
import com.gcapp.tc.sd.ui.adapter.GridView_HallLotteryAdapter;
import com.gcapp.tc.sd.ui.adapter.InformationAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.ImageManager;
import com.gcapp.tc.utils.MyPushTask;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.AutoMoveTextView;
import com.gcapp.tc.view.DragGridView;
import com.gcapp.tc.view.GifView;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.PullToRefreshView;
import com.gcapp.tc.view.PullToRefreshView.OnHeaderRefreshListener;
import com.gcapp.tc.viewpager.AutoLoopViewPager;
import com.gcapp.tc.viewpager.CirclePageIndicator;
import com.gcapp.tc.R;

/**
 ** 大厅
 */
@SuppressLint({ "NewApi", "DefaultLocale" })
public class HallFragment extends Fragment implements OnClickListener,
		OnHeaderRefreshListener {
	
	/** 保存购彩大厅彩种id位置*/
	private SharedPreferences settings;
	private static final String TAG = "HallFragment";
	public static String Message;
	private GridView_HallLotteryAdapter gvAdapter;
	private PullToRefreshView mPullToRefreshView;
	private DragGridView gv_hall_lottry;
//	private ImageView tv_service;
	private ImageButton live_football_btn;
	private ImageButton live_basketball_btn;
	public static List<Lottery> listLottery;
	public static List<Lottery> listLottery2;
	public static boolean refreType = true;
	private Context context;
	private Intent intent;
	private MainActivity activity;
	private List<RequestUtil> requestUtils;
	private List<Integer> menuList = new ArrayList<Integer>();
	private AutoLoopViewPager loopView;
	/** 广告条数据*/
	private ArrayList<ImageViews> imageviews;
	private CirclePageIndicator indy;
	private AutoMoveTextView rollTextView;
	private boolean flags_11x5 = true;
	private boolean flags_ssc = true;
	private GifView gif_advertisement;
	private MyListView2 main_information_list;
	private InformationAdapter informAdapter;
	private List<Information> informations = new ArrayList<Information>();
	private List<Information> informations_temp = new ArrayList<Information>();
	private int isEnd = 5;
	private TextView information_more;
	/** 站点公告开关*/
	private String noticeOpen = "0"; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_hall, container, false);
		init();
		findView(v);
		setListener();
		return v;
	}

	/**
	 * 初始化变量
	 */
	private void init() {
		context = getActivity();
		activity = (MainActivity) getActivity();
		if (informations.isEmpty()) {
			getHttpRes(false);
		}
	}

	/**
	 * 初始化自定义控件
	 * 
	 * @param v
	 */
	private void findView(View v) {
		main_information_list = (MyListView2) v.findViewById(R.id.main_information_list);
		informAdapter = new InformationAdapter(activity, informations);
		main_information_list.setAdapter(informAdapter);
		main_information_list.setOnItemClickListener(new MyItemsClickListener());
		main_information_list.setFocusable(false);
		information_more = (TextView) v.findViewById(R.id.information_more);
		
		gif_advertisement = (GifView) v
				.findViewById(R.id.hall_img_advertisement);
		gif_advertisement.setMovieResource(R.raw.gif_ad);// 加载动态广播
		rollTextView = (AutoMoveTextView) v.findViewById(R.id.rolltextView1);
		imageviews = new ArrayList<ImageViews>();
		gv_hall_lottry = (DragGridView) v.findViewById(R.id.gv_hall_lottry);
		mPullToRefreshView = (PullToRefreshView) v
				.findViewById(R.id.mPullToRefreshView);
		if (listLottery2 != null && listLottery2.size() > 0) {
			gvAdapter = new GridView_HallLotteryAdapter(context, listLottery2);
		} else {
			gvAdapter = new GridView_HallLotteryAdapter(context, getData(
					flags_11x5, "0"));
		}
		gv_hall_lottry.setAdapter(gvAdapter);
		gv_hall_lottry.setFocusable(false);
		gv_hall_lottry.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mPullToRefreshView.setOnHeaderRefreshListener(this);
//		tv_service = (ImageView) v.findViewById(R.id.tv_service);
		live_football_btn = (ImageButton) v.findViewById(R.id.live_football_btn);
		live_basketball_btn = (ImageButton) v.findViewById(R.id.live_basketball_btn);
		loopView = (AutoLoopViewPager) v.findViewById(R.id.autoLoop);
		indy = (CirclePageIndicator) v.findViewById(R.id.indy);
		
		/** 要更改的 **/
		menuList.add(R.drawable.menu_refresh_change);
		menuList.add(R.drawable.menu_setting_change);
		menuList.add(R.drawable.menu_changeuser_change);
		menuList.add(R.drawable.menu_exit_change);

		if (NetWork.isConnect(context)) {
			if (null == requestUtils)
				requestUtils = new ArrayList<RequestUtil>();
			getLotteryData();
			doAutoLogin();
			LoadImageview();
			if(AppTools.winMessage != null && !AppTools.winMessage.equals("")){
				SetWinText(AppTools.winMessage);
			}else{
				getWinMessage();
			}
		} else {
			MyToast.getToast(context, "网络连接异常，请检查网络!");
		}
	}
	
	/**
	 * 获取资讯数据
	 * 
	 * @param force
	 *            是否删除之前的缓存
	 */
	private void getHttpRes(boolean force) {
		
		RequestUtil requestUtil = new RequestUtil(context, true, Request.CONFIG_CACHE_SHORT, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject response) {
				dealResult(parserJSONObject(response));
			}
			
			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "资讯数据异常!");
			}
		};
		requestUtil.getInformation();
	}
	
	/**
	 * 解析网络获取的json
	 * 
	 * @param result
	 * @return
	 */
	private int parserJSONObject(JSONObject result) {
		if ("0".equals(result.optString("error"))) {
			String array = result.optString("dtInformationTitles");
			JSONArray array2 = null;
			try {
				array2 = new JSONArray(array);
			} catch (JSONException e) {
				VolleyLog.e("JSONException ==> %s", e.toString());
			}
			isEnd = array2 != null ? array2.length() : 0;
			if (isEnd == 0) {
				return 1;
			}
			informations_temp.clear();
			for (int i = 0; i < array2.length(); i++) {
				JSONObject items = null;
				try {
					items = array2.getJSONObject(i);
				} catch (JSONException e) {
					VolleyLog.e("JSONException ==> %s", e.toString());
				}
				if (null != items) {
					addInformation(items);
				}
			}
			return 0;
		} else {
			return Integer.valueOf(result.optString("error"));
		}
	}

	/**
	 * 整理信息集合
	 * 
	 * @param items
	 */
	private void addInformation(JSONObject items) {
		Information information = new Information();
		String titleString = items.optString("title");
		String color = items.optString("color");
		String newTypeNameString = getNewTypeStr(titleString);
		information.setId(items.optLong("id"));
		information.setNewType(newTypeNameString);
		information.setParentTypeId(2);
		information.setDateTime(items.optString("dateTime"));
		information.setDescribe(items.optString("Abstractt"));
		String  imgUrl=items.optString("PicUrlForApp");
		
		if(imgUrl.startsWith("/")){
			information.setImg_url(AppTools.url + items.optString("PicUrlForApp"));
		}else{
			information.setImg_url(AppTools.url +"/"+ items.optString("PicUrlForApp"));
		}
		information.setColor(color);

		if (!newTypeNameString.equals("")) {
			information.setTitle(titleString.replace(newTypeNameString, ""));
		} else {
			information.setTitle(titleString);
		}
		String title = information.getTitle();
		title = title.replace("&quot;", "\"");
		information.setTitle(title);

		information.setSerialNumber(items.optInt("SerialNumber"));
		information.setRecordCount(items.optString("RecordCount"));
		informations_temp.add(information);
	}
	
	private String getNewTypeStr(String str) {
		String resultString = "";
		Pattern p = Pattern.compile(".*?\\[(.*?)\\].*?");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			resultString = "[" + m.group(1) + "]"; // 匹配第1项
		}
		return resultString;
	}

	/**
	 * 处理请求结果
	 * 
	 * @param result
	 */
	private void dealResult(int result) {
		switch (result) {
		case 0:
				informations.clear();
				setStatus(true, "加载完毕", false);
			for (Information information : informations_temp) {
				informations.add(information);
			}
			informAdapter.notifyDataSetChanged();

			if (informations.size() == 0) {
				setStatus(false, "暂无资讯", false);
			}
			break;
		case 1:
			setStatus(false, "没有更多数据", true);
			break;
		case -1:
			setStatus(false, "数据加载出错，请重试", false);
			break;
		case -500:
			setStatus(false, "连接超时，请重试", false);
		case -4504:
			setStatus(false, "暂无资讯", false);
			break;
		}
	}
	
	/**
	 * 处理请求结果的提示信息
	 */
	private void setStatus(boolean status, String msg, boolean toast) {
		if (toast) {
			MyToast.getToast(activity, msg);
		}
	}
	
	/**
	 * 子项点击事件
	 * 
	 * @author lenovo
	 * 
	 */
	private class MyItemsClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			skipToInformation(position);
		}
	}
	
	/**
	 * 跳转咨询详情
	 */
	private void skipToInformation(int position) {
		int count = informations.size();
		if (count > 0) {
			long[] ids = new long[informations.size()];
			long currentNewId;
			currentNewId = informations.get(position).getId();
			for (int i = 0; i < informations.size(); i++) {
				ids[i] = informations.get(i).getId();
			}
			Intent intent = new Intent(activity, Information_Detail.class);
			Bundle bundle = new Bundle();
			bundle.putLongArray("ids", ids);
			bundle.putInt("newIndex", position);
			bundle.putLong("currentNewId", currentNewId);
			bundle.putInt("newType", 2);
			intent.putExtra("informationList", bundle);
			startActivity(intent);
		}
	}
	
	/**
	 * 获取中奖播报信息
	 */
	public void getWinMessage() {
		RequestUtil requestUtil = new RequestUtil(context, true, Request.CONFIG_CACHE_SHORT) {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "获取中奖播报信息" + reponseJson.toString());
				// 获取中奖播报信息{"error":"-5103","msg":"无中奖信息"}
				if (null != reponseJson && !"".equals(reponseJson)) {
					if (reponseJson.optString("error").equals("0")) {
						HallFragment.Message = reponseJson.optString("winInfo");
						if (HallFragment.Message != null
								&& !HallFragment.Message.equals("")) {
							SetWinText(HallFragment.Message);

						} else {
							SetWinText("----暂无中奖信息----");
						}
					} else {
						SetWinText("----暂无中奖信息----");
					}
				} else {
					SetWinText("----暂无中奖信息----");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取中奖播报信息失败" + error.getMessage());
				SetWinText("----暂无中奖信息----");
			}
		};
		requestUtil.getWinMessage();
	}
	
	private void SetWinText(String winMsg) {
		if(winMsg != null && !winMsg.equals("")) {
			rollTextView.setText(winMsg);
//			rollTextView.setSpeed(1.5f);
//			rollTextView.init(300);
//			rollTextView.startScroll();
		}
	}

	/**
	 * 请求图片轮播图
	 */
	private void LoadImageview() {
		if (imageviews.size() == 0) {
			imageviews.clear();
			getImageInfo();
		}
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
//		tv_service.setOnClickListener(this);
		live_football_btn.setOnClickListener(this);
		live_basketball_btn.setOnClickListener(this);
		information_more.setOnClickListener(this);
		
		gv_hall_lottry.setAdapter(gvAdapter);
		gv_hall_lottry.setOnItemClickListener(new ItemsClickListener());
	}

	/**
	 * 自动登录
	 */
	public void doAutoLogin() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				String retult = AppTools.doLogin(reponseJson);
				if ("0".equals(retult)) {
					settings = context.getSharedPreferences(
							"app_user", 0);
					boolean isLogin = false;
					String pass = "";
					if (settings.contains("isLogin")) {
						isLogin = settings.getBoolean("isLogin", false);
					}
					if (isLogin) {
						// 判断是否有存 密码
						if (settings.contains("pass")) {
							pass = settings.getString("pass", null);
						}
					}
					if (null != AppTools.user)
						AppTools.user.setUserPass(pass);
					if (RequestUtil.DEBUG)
						Log.i(TAG, "自动登录成功...");
					MyPushTask.newInstances(getActivity()).commit();
				} else {
					if (RequestUtil.DEBUG)
						Log.i(TAG, "自动登录失败" + retult);
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "自动登录失败" + error.getMessage());
			}
		};
		requestUtil.doAutoLogin();
		requestUtils.add(requestUtil);
	}

	/**
	 * 得到竞彩 篮球数据
	 */
	public void getBasketball(final Context context, final int mark) {
		RequestUtil requestUtil = new RequestUtil(context, true,
				Request.CONFIG_CACHE_MEDIUM, true, "正在加载对阵...") {
			@Override
			public void responseCallback(JSONObject object2) {
				try {
					if (null != object2) {
						String error2 = object2.optString("error");
						if ("0".equals(error2)) {
							String detail2 = object2.optString("dtMatch");
							String detail_singlepass2 = object2
									.optString("Singlepass");
							if (detail2 == null || detail2.length() == 0) {
								Log.i("x", "无数据");
							} else {
								// 拿到对阵信息组
								JSONArray array2 = new JSONArray(new JSONArray(
										detail2).toString());
								List<List<DtMatch_Basketball>> listMatch = new ArrayList<List<DtMatch_Basketball>>();
								for (int i = 0; i < array2.length(); i++) {
									JSONObject ob = array2.getJSONObject(i);

									for (int j = 0; j < 100; j++) {
										String arr_4 = ob.optString("table"
												+ (j + 1));
										if (null == arr_4 || "".equals(arr_4)) {
											break;
										}
										listMatch.add(setList2(arr_4));
									}
								}

								for (int i = 0; i < listLottery2.size(); i++) {
									if ("73".equals(listLottery2.get(i)
											.getLotteryID())) {
										JSONArray array_single2 = new JSONArray(
												new JSONArray(
														detail_singlepass2)
														.toString());

										List<List<DtMatch_Basketball>> listMatch_single = new ArrayList<List<DtMatch_Basketball>>();
										for (int j = 0; j < array_single2
												.length(); j++) {
											JSONObject ob = array_single2
													.getJSONObject(j);
											for (int k = 0; k < 100; k++) {
												String arr1 = ob
														.optString("table"
																+ (k + 1));
												if (null == arr1
														|| "".equals(arr1)) {
													break;
												}
												listMatch_single
														.add(setList2(arr1));
											}
										}
										AppTools.DtMatch_Basketball = listMatch;
										AppTools.DtMatch_Basketball_single = listMatch_single;
										listLottery2
												.get(i)
												.setExplanation(
														object2.optString("explanation"));
									}
								}
							}

							if (!checkBasketBallDtMatch_IsEmpty(
									AppTools.DtMatch_Basketball,
									AppTools.DtMatch_Basketball_single)) {
								if (mark == 1) {
									intent = new Intent(context,
											Select_JCLQ_DAN_Activity.class);
								} else {
									intent = new Intent(context,
											Select_JCLQ_Activity.class);
								}
								context.startActivity(intent);
							} else
								MyToast.getToast(context, "暂无篮球对阵信息!");
						}else{
							MyToast.getToast(context, "数据异常,请稍后重试!");
						}
					} else {
						if (RequestUtil.DEBUG)
							Log.i(TAG, "拿竞彩篮球数据为空");
						MyToast.getToast(context, "抱歉,服务器异常,请稍后重试!");
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (RequestUtil.DEBUG)
						Log.i(TAG, "对阵获取失败，" + e.getMessage());
					MyToast.getToast(context, "抱歉,数据获取错误!");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "对阵获取失败，" + error.getMessage());
				MyToast.getToast(context, "抱歉,服务器异常,请稍后重试...");
			}
		};
		requestUtil.getJCData("73");
	}

	/**
	 * 判断篮球对阵是否为空
	 * 
	 * @param data
	 *            对阵信息
	 * @param data_single
	 *            单关对阵信息
	 * @return 判断结果 true为空 false不为空
	 */
	public boolean checkBasketBallDtMatch_IsEmpty(
			List<List<DtMatch_Basketball>> data,
			List<List<DtMatch_Basketball>> data_single) {
		boolean isEmpty = true;
		if (0 != data.size()) {
			for (int i = 0; i < data.size(); i++) {
				if (0 != data.get(i).size()) {
					isEmpty = false;
				}
			}

		}

		if (0 != data_single.size()) {
			for (int i = 0; i < data_single.size(); i++) {
				if (0 != data_single.get(i).size()) {
					isEmpty = false;
				}
			}
		}
		return isEmpty;
	}

	/**
	 * 判断足球对阵是否为空
	 * 
	 * @param data
	 *            对阵信息
	 * @param data_single
	 *            单关对阵信息
	 * @return 判断结果 true为空 false不为空
	 */
	public boolean checkFootBallDtMatch_IsEmpty(List<List<DtMatch>> data,
			List<List<DtMatch>> data_single) {
		boolean isEmpty = true;
		if (0 != data.size()) {
			for (int i = 0; i < data.size(); i++) {
				if (0 != data.get(i).size()) {
					isEmpty = false;
				}
			}
		}

		if (0 != data_single.size()) {
			for (int i = 0; i < data_single.size(); i++) {
				if (0 != data_single.get(i).size()) {
					isEmpty = false;
				}
			}
		}
		return isEmpty;
	}

	/**
	 * 篮球绑定数据 *
	 */
	private List<DtMatch_Basketball> setList2(String arr) {
		List<DtMatch_Basketball> list_m = new ArrayList<DtMatch_Basketball>();
		if (arr!=null && arr.length() > 5) {
			JSONArray Arr;
			try {
				Arr = new JSONArray(arr);
				DtMatch_Basketball dtmatch = null;
				for (int j = 0; j < Arr.length(); j++) {
					JSONObject item = Arr.getJSONObject(j);
					dtmatch = new DtMatch_Basketball();
					dtmatch.setMatchId(item.optString("matchId"));
					dtmatch.setMatchNumber(item.optString("matchNumber"));
					dtmatch.setMatchDate(item.optString("matchDate"));
					dtmatch.setGame(item.getString("game"));
					dtmatch.setGuestTeam(item.optString("guestTeam"));
					dtmatch.setMainTeam(item.getString("mainTeam"));
					dtmatch.setStopSellTime(item.optString("stopSellTime"));
					dtmatch.setDay(item.optString("day"));
					dtmatch.setMatchDate1(item.optString("matchDate1"));
					dtmatch.setMainLose(item.getString("mainLose"));
					dtmatch.setMainWin(item.getString("mainWin"));
					dtmatch.setSmall(item.getString("small"));
					dtmatch.setBigSmallScore(item.getString("bigSmallScore"));
					dtmatch.setBig(item.getString("big"));
					dtmatch.setLetScore(item.getString("letScore"));
					dtmatch.setLetMainLose(item.getString("letMainLose"));
					dtmatch.setLetMainWin(item.getString("letMainWin"));
					dtmatch.setMatchDate2(item.getString("matchWeek"));
					dtmatch.setDifferGuest1_5(item.optString("differGuest1_5"));
					dtmatch.setDifferGuest6_10(item
							.optString("differGuest6_10"));
					dtmatch.setDifferGuest11_15(item
							.optString("differGuest11_15"));
					dtmatch.setDifferGuest16_20(item
							.optString("differGuest16_20"));
					dtmatch.setDifferGuest21_25(item
							.optString("differGuest21_25"));
					dtmatch.setDifferGuest26(item.optString("differGuest26"));
					dtmatch.setDifferMain1_5(item.optString("differMain1_5"));
					dtmatch.setDifferMain6_10(item.optString("differMain6_10"));
					dtmatch.setDifferMain11_15(item
							.optString("differMain11_15"));
					dtmatch.setDifferMain16_20(item
							.optString("differMain16_20"));
					dtmatch.setDifferMain21_25(item
							.optString("differMain21_25"));
					dtmatch.setDifferMain26(item.optString("differMain26"));
					dtmatch.setSF(Boolean.parseBoolean(item.getString("isSF")
							.toLowerCase()));
					dtmatch.setDXF(Boolean.parseBoolean(item.getString("isDXF")
							.toLowerCase()));
					dtmatch.setRFSF(Boolean.parseBoolean(item.getString(
							"isRFSF").toLowerCase()));
					dtmatch.setSFC(Boolean.parseBoolean(item.getString("isSFC")
							.toLowerCase()));
					list_m.add(dtmatch);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("x", "HallFragment错误" + e.getMessage());
			}
		}
		return list_m;
	}

	/**
	 * 菜单选项点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/** 刷新 **/
		case 1:
			update();
			break;
		/** 设置 **/
		case 2:
			intent = new Intent(context, SettingActivity.class);
			context.startActivity(intent);
			break;
		/** 更改账户 **/
		case 3:
			intent = new Intent(context, LoginActivity.class);
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
	 * lsitview 的点击监听事件
	 */
	class ItemsClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			if (0 == listLottery2.size() % 2) {
				itemOnclick(position);
			} else {
				if (listLottery2.size() != position) {
					itemOnclick(position);
				}
			}
		}
	}

	/**
	 * 点击进入不同选号界面方法
	 * 
	 * @param position
	 */
	private void itemOnclick(int position) {
		long currentTime = System.currentTimeMillis();

		if ("111".equals(listLottery2.get(position).getLotteryID())) {
			flags_11x5 = !flags_11x5;
			gvAdapter = new GridView_HallLotteryAdapter(context, getData(
					flags_11x5, listLottery2.get(position).getLotteryID()));
			gvAdapter.updateUI("111", flags_ssc, flags_11x5);
			gv_hall_lottry.setAdapter(gvAdapter);
			gvAdapter.notifyDataSetChanged();
		} else if ("222".equals(listLottery2.get(position).getLotteryID())) {
			flags_ssc = !flags_ssc;
			gvAdapter = new GridView_HallLotteryAdapter(context, getData(
					flags_ssc, listLottery2.get(position).getLotteryID()));
			gvAdapter.updateUI("222", flags_ssc, flags_11x5);
			gv_hall_lottry.setAdapter(gvAdapter);
			gvAdapter.notifyDataSetChanged();
		} else {
			if (!"73".equals(listLottery2.get(position).getLotteryID())
					&& !"45".equals(listLottery2.get(position).getLotteryID())
					&& !"72".equals(listLottery2.get(position).getLotteryID())) {
				if ((listLottery2.get(position).getDistanceTime() - currentTime <= 0)
						&& (!"61".equals(listLottery2.get(position)
								.getLotteryID()))) {
					if (null != listLottery2.get(position).getLotteryID()) {
						MyToast.getToast(context, "该奖期已结束，请等下一期");
						 return;
					}
				}
			}

			if (listLottery2.size() > 0) {
				AppTools.lottery = listLottery2.get(position); // 对应id
				if (null != AppTools.lottery && !"".equals(AppTools.lottery)
						&& null != AppTools.lottery.getLotteryID()) {
					 if (null != AppTools.lottery.getIsuseId()
					 && !"".equals(AppTools.lottery.getIsuseId())
					 && !"72".equals(AppTools.lottery.getLotteryID())
					 && !"73".equals(AppTools.lottery.getLotteryID())
					 && !"45".equals(AppTools.lottery.getLotteryID())) {

						if (AppTools.lottery.getLotteryID().equals("5")) {
							intent = new Intent(context,
									Select_SSQ_Activity.class);
						} else if (AppTools.lottery.getLotteryID().equals("39")) {
							intent = new Intent(context,
									Select_DLT_Activity.class);
						} else if (AppTools.lottery.getLotteryID().equals("74")) {
							intent = new Intent(context,
									Select_SFC_Activity.class);// 胜负彩
						} else if (AppTools.lottery.getLotteryID().equals("75")) {
							intent = new Intent(context,
									Select_RX9_Activity.class);
						} else if (AppTools.lottery.getLotteryID().equals("62")
								|| AppTools.lottery.getLotteryID().equals("70")
								|| AppTools.lottery.getLotteryID().equals("78")) {
							intent = new Intent(context,
									Select_11x5_Activity.class);
						} else if (AppTools.lottery.getLotteryID().equals("28")) {
							intent = new Intent(context,
									Select_SSC_Activity.class);
						} else if (AppTools.lottery.getLotteryID().equals("83")) {
							intent = new Intent(context,
									Select_K3_Activity.class);
						} else {
							MyToast.getToast(context, "正在开发");
							return;
						}
						context.startActivity(intent);
					} else {
						refreType = true;
						if ("72".equals(AppTools.lottery.getLotteryID())) {
							if ("竞足单关"
									.equals(AppTools.lottery.getLotteryName())) {
								getBallData(getActivity(), 1);
							} else {
								getBallData(getActivity(), 0);
							}
						} else if ("73".equals(AppTools.lottery.getLotteryID())) {
							if ("竞篮单关"
									.equals(AppTools.lottery.getLotteryName())) {
								getBasketball(getActivity(), 1);
							} else {
								getBasketball(getActivity(), 0);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获得所有彩种名称 以及 彩种ID
	 */
	private List<Lottery> getData(boolean flag, String lotteryID) {
		if (listLottery == null)
			listLottery = new ArrayList<Lottery>();
		if (listLottery2 == null)
			listLottery2 = new ArrayList<Lottery>();
		if (listLottery2.size() > 0) {
			listLottery2.clear();
		}

		if (listLottery.size() == 0) {
			// 清空数据
			listLottery.clear();
			Lottery lottery = null;
			Iterator iterator = null;
			if (null != AppTools.allLotteryName) {
				iterator = AppTools.allLotteryName.entrySet().iterator();
			}
			if (null != iterator)
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();
					lottery = new Lottery();
					lottery.setLotteryID(value);
					lottery.setLotteryName(key);
					listLottery.add(lottery);
				}
		}

		if (flag) {
			if (lotteryID.equals("0")) {
				for (Lottery ll : listLottery) {
					if (!ll.getLotteryID().equals("62")
							&& !ll.getLotteryID().equals("70")
							&& !ll.getLotteryID().equals("66")) {
						listLottery2.add(ll);
					}
				}
			} else if (lotteryID.equals("222")) {
				for (Lottery ll : listLottery) {
					if (!ll.getLotteryID().equals("28")
							&& !ll.getLotteryID().equals("66")) {
						if (!flags_11x5) {

							if (ll.getLotteryID().equals("78")) {
								listLottery2.add(ll);
								Lottery lls = new Lottery();
								listLottery2.add(lls);
							} else {
								listLottery2.add(ll);
							}

						} else {
							if (!ll.getLotteryID().equals("62")
									&& !ll.getLotteryID().equals("78")
									&& !ll.getLotteryID().equals("70")) {
								listLottery2.add(ll);
							}
						}
					}
				}
			} else if (lotteryID.equals("111")) {
				for (Lottery ll : listLottery) {
					if (!ll.getLotteryID().equals("62")
							&& !ll.getLotteryID().equals("78")
							&& !ll.getLotteryID().equals("70")) {

						if (!flags_ssc) {
							listLottery2.add(ll);
						} else {
							if (!ll.getLotteryID().equals("28")
									&& !ll.getLotteryID().equals("66")) {
								listLottery2.add(ll);
							}
						}
					}
				}
			}

		} else {
			if (lotteryID.equals("222")) {
				if (flags_11x5) {
					for (Lottery ll : listLottery) {
						if (!ll.getLotteryID().equals("62")
								&& !ll.getLotteryID().equals("78")
								&& !ll.getLotteryID().equals("70")) {
							listLottery2.add(ll);
						}
					}
				} else {
					for (Lottery ll : listLottery) {
						if (ll.getLotteryID().equals("78")) {
							listLottery2.add(ll);
							Lottery lls = new Lottery();
							listLottery2.add(lls);
						} else {
							listLottery2.add(ll);
						}

					}
				}
			} else if (lotteryID.equals("111")) {
				if (flags_ssc) {
					for (Lottery ll : listLottery) {
						if (!ll.getLotteryID().equals("66")
								&& !ll.getLotteryID().equals("28")) {
							if (ll.getLotteryID().equals("78")) {
								listLottery2.add(ll);
								Lottery lls = new Lottery();
								listLottery2.add(lls);
							} else {
								listLottery2.add(ll);
							}
						}
					}
				} else {
					for (Lottery ll : listLottery) {
						if (ll.getLotteryID().equals("78")) {
							listLottery2.add(ll);
							Lottery lls = new Lottery();
							listLottery2.add(lls);
						} else {
							listLottery2.add(ll);
						}

					}
				}

			}

		}
		return listLottery2;
	}

	/**
	 * 后台获得所有彩票信息
	 */
	public void getLotteryData() {
		if (listLottery == null)
			listLottery = new ArrayList<Lottery>();

		if (refreType) {
			RequestUtil requestUtil = new RequestUtil(context, false, 0) {
				@Override
				public void responseCallback(JSONObject reponseJson) {
					Log.e("后台所有彩种信息", reponseJson.toString());
					String result = AppTools.getDate(reponseJson);
					if ("0".equals(result)) {
						update();
						/** 是否第一次进入程序 不是的话就自动登录 */
						if (AppTools.index <= 1) {
							AppTools.isShow = false;
							AppTools.index++;
							if (MainActivity.isWin) {
								activity.toFindWininfo();
							}
						}
					} else if ("-1001".equals(result)) {
						if (RequestUtil.DEBUG) {
							Log.e(TAG, "获取购彩大厅数据出错");
						}
						update();
						/** 是否第一次进入程序 不是的话就自动登录 */
						if (AppTools.index <= 1) {
							AppTools.isShow = false;
							AppTools.index++;
							if (MainActivity.isWin) {
								activity.toFindWininfo();
							}
						}
						MyToast.getToast(context, "数据获取失败,请手动刷新");
					}
				}

				@Override
				public void responseError(VolleyError error) {
					Log.e(TAG, "出错了," + error.getMessage());
					update();
					/** 是否第一次进入程序 不是的话就自动登录 */
					if (AppTools.index <= 1) {
						AppTools.isShow = false;
						AppTools.index++;
						if (MainActivity.isWin) {
							activity.toFindWininfo();
						}
					}
					MyToast.getToast(context, "数据获取失败,请手动刷新");
				}
			};
			requestUtil.getLotteryData(AppTools.lotteryIds);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.tv_service:
//			intent = new Intent(context, AboutAppActivity.class);
//			context.startActivity(intent);
//			break;
		case R.id.live_football_btn:
			intent = new Intent(context, LiveMatchActivity.class);
			context.startActivity(intent);
			break;
		case R.id.live_basketball_btn:
			intent = new Intent(context, LiveBasketballActivity.class);
			context.startActivity(intent);
			break;
		case R.id.information_more:
			Intent intent1 = new Intent(getActivity(),InformationActivity.class);
			intent1.putExtra("index", 0);
            startActivity(intent1);
			break;
		}
	}

	/**
	 * 跳到幸运数字页面
	 */
	private void ToLuckActivity() {
		intent = new Intent(context, LuckyCenterActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 得到竞彩 足球 数据
	 */
	public void getBallData(final Context context, final int mark) {
		if (refreType) {
			RequestUtil requestUtil = new RequestUtil(context, false,
					Request.CONFIG_CACHE_MEDIUM, true, "正在加载对阵...\n"
							+ "对阵较多时加载会较慢...") {
				@Override
				public void responseCallback(JSONObject object) {
					try {
						if (null != object) {
							String error = object.optString("error");
							Log.d("damao",object.toString());
							Log.e("BaseBall", error);
							if ("0".equals(error)) {
								String detail = object.optString("dtMatch");
								String detail_singlepass = object
										.optString("Singlepass");
								if (detail == null || detail.length() == 0) {
									Log.i("x", "无数据");
								} else {
									// 拿到对阵信息组
									JSONArray array = new JSONArray(
											new JSONArray(detail).toString());
									List<List<DtMatch>> listMatch = new ArrayList<List<DtMatch>>();
									for (int i = 0; i < array.length(); i++) {
										JSONObject ob = array.getJSONObject(i);

										for (int j = 0; j < 100; j++) {

											String arr = ob.optString("table"
													+ (j + 1));
											if (null == arr || "".equals(arr)) {
												break;
											}
											listMatch.add(setList(arr));

										}
									}

									for (int i = 0; i < listLottery2.size(); i++) {
										if ("72".equals(listLottery2.get(i)
												.getLotteryID())) {
											// 拿到单关对阵信息组
											JSONArray array_singlepass = new JSONArray(
													new JSONArray(
															detail_singlepass)
															.toString());
											List<List<DtMatch>> listMatch_singlepass = new ArrayList<List<DtMatch>>();
											for (int j = 0; j < array_singlepass
													.length(); j++) {
												JSONObject ob = array_singlepass
														.getJSONObject(j);

												for (int k = 0; k < 100; k++) {
													String arr = ob
															.optString("table"
																	+ (k + 1));
													if (null == arr
															|| "".equals(arr)) {
														break;
													}
													if (!"".equals(arr)) {// 如果第一个不是空的
														listMatch_singlepass
																.add(setList(arr));
													}
												}

											}
											AppTools.list_singlepass_Matchs = listMatch_singlepass;
											AppTools.list_Matchs = listMatch;
											listLottery2
													.get(i)
													.setExplanation(
															object.optString("explanation"));
											AppTools.lottery = listLottery2
													.get(i);
										}
									}
								}

								if (!checkFootBallDtMatch_IsEmpty(
										AppTools.list_Matchs,
										AppTools.list_singlepass_Matchs)) {
									if (mark == 1) {
										intent = new Intent(context,
												Select_JCZQ_DAN_Activity.class);
									} else {
										intent = new Intent(context,
												Select_JCZQ_Activity.class);
									}

									intent.putExtra("isEmpty", false);
									context.startActivity(intent);
								} else
									MyToast.getToast(context, "暂无足球对阵信息!");
							}else{
								MyToast.getToast(context, "抱歉,数据异常,请稍后重试!");
							}
						} else {
							MyToast.getToast(context, "抱歉,服务器异常,请稍后重试!");
							if (RequestUtil.DEBUG)
								Log.i(TAG, "拿竞彩足球数据为空");
						}
					} catch (Exception e) {
						e.printStackTrace();
						MyToast.getToast(context, "抱歉,数据获取错误!");
						if (RequestUtil.DEBUG)
							Log.i(TAG, "对阵获取失败，" + e.getMessage());
					}
				}

				@Override
				public void responseError(VolleyError error) {
					MyToast.getToast(context, "抱歉,服务器异常,请稍后重试...");
					Log.e(TAG, "对阵获取失败，" + error.getMessage());
				}
			};
			requestUtil.getJCData("72");
		}
	}

	/**
	 * 将对阵信息绑定 *
	 */
	@SuppressLint("DefaultLocale")
	private List<DtMatch> setList(String arr) {
		List<DtMatch> list_m = new ArrayList<DtMatch>();
		if (arr != null && arr.length() > 5) {
			JSONArray Arr;
			try {
				Arr = new JSONArray(arr);
				DtMatch dtmatch = null;
				for (int j = 0; j < Arr.length(); j++) {
					JSONObject item = Arr.getJSONObject(j);
					dtmatch = new DtMatch();
					dtmatch.setMatchId(item.optString("matchId"));
					dtmatch.setMatchNumber(item.optString("matchNumber"));
					dtmatch.setGame(item.getString("game"));
					dtmatch.setGuestTeam(item.optString("guestTeam"));
					dtmatch.setMainTeam(item.getString("mainTeam"));
					dtmatch.setStopSellTime(item.optString("stopSellTime"));
					dtmatch.setDay(item.optString("day"));
					dtmatch.setMainLoseBall(item.optInt("mainLoseBall"));
					dtmatch.setMatchWeek(item.optString("matchWeek"));
					/** 胜平负 */
					dtmatch.setSpfwin(item.optString("spfwin"));
					dtmatch.setSpfflat(item.optString("spfflat"));
					dtmatch.setSpflose(item.optString("spflose"));
					/** 让球胜平负 */
					dtmatch.setWin(item.optString("win"));
					dtmatch.setFlat(item.optString("flat"));
					dtmatch.setLose(item.optString("lose"));
					dtmatch.setMatchDate(item.optString("matchDate"));
					/** 猜比分 **/
					dtmatch.setSother(item.getString("sother"));
					dtmatch.setS10(item.optString("s10"));
					dtmatch.setS20(item.optString("s20"));
					dtmatch.setS21(item.optString("s21"));
					dtmatch.setS30(item.optString("s30"));
					dtmatch.setS31(item.optString("s31"));
					dtmatch.setS32(item.optString("s32"));
					dtmatch.setS40(item.optString("s40"));
					dtmatch.setS41(item.optString("s41"));
					dtmatch.setS42(item.optString("s42"));
					dtmatch.setS50(item.optString("s50"));
					dtmatch.setS51(item.optString("s51"));
					dtmatch.setS52(item.optString("s52"));
					dtmatch.setPother(item.getString("pother"));
					dtmatch.setP00(item.getString("p00"));
					dtmatch.setP11(item.getString("p11"));
					dtmatch.setP22(item.getString("p22"));
					dtmatch.setP33(item.getString("p33"));
					dtmatch.setFother(item.getString("fother"));
					dtmatch.setF01(item.getString("f01"));
					dtmatch.setF02(item.getString("f02"));
					dtmatch.setF12(item.getString("f12"));
					dtmatch.setF03(item.getString("f03"));
					dtmatch.setF13(item.getString("f13"));
					dtmatch.setF23(item.getString("f23"));
					dtmatch.setF04(item.getString("f04"));
					dtmatch.setF14(item.getString("f14"));
					dtmatch.setF24(item.getString("f24"));
					dtmatch.setF05(item.getString("f05"));
					dtmatch.setF15(item.getString("f15"));
					dtmatch.setF25(item.getString("f25"));

					/** 半全场 **/
					dtmatch.setSs(item.getString("ss"));
					dtmatch.setSp(item.getString("sp"));
					dtmatch.setSf(item.getString("sf"));
					dtmatch.setPs(item.getString("ps"));
					dtmatch.setPp(item.getString("pp"));
					dtmatch.setPf(item.getString("pf"));
					dtmatch.setFs(item.getString("fs"));
					dtmatch.setFp(item.getString("fp"));
					dtmatch.setFf(item.getString("ff"));

					/** 总进球 **/
					dtmatch.setIn0(item.getString("in0"));
					dtmatch.setIn1(item.getString("in1"));
					dtmatch.setIn2(item.getString("in2"));
					dtmatch.setIn3(item.getString("in3"));
					dtmatch.setIn4(item.getString("in4"));
					dtmatch.setIn5(item.getString("in5"));
					dtmatch.setIn6(item.getString("in6"));
					dtmatch.setIn7(item.getString("in7"));

					dtmatch.setSPF(Boolean.parseBoolean(item.getString("isSPF")
							.toLowerCase()));
					dtmatch.setCBF(Boolean.parseBoolean(item.getString("isCBF")
							.toLowerCase()));
					dtmatch.setZJQ(Boolean.parseBoolean(item.getString("isZJQ")
							.toLowerCase()));
					dtmatch.setNewSPF(Boolean.parseBoolean(item.getString(
							"isNewSPF").toLowerCase()));
					dtmatch.setBQC(Boolean.parseBoolean(item.getString("isBQC")
							.toLowerCase()));

					list_m.add(dtmatch);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("x", "错误" + e.getMessage());
			}
		}
		return list_m;
	}

	/**
	 * 刷新界面数据
	 */
	public void update() {
		if (null == gvAdapter) {
			if (null == gv_hall_lottry)
				return;
			gvAdapter = new GridView_HallLotteryAdapter(context, listLottery2);
			gv_hall_lottry.setAdapter(gvAdapter);
		} else {
			gvAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 下拉刷新事件
	 */
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (NetWork.isConnect(context)) {
					refreType = true;
					getLotteryData();
					LoadImageview();
					getWinMessage();// 获取中奖播报信息
				} else {
					MyToast.getToast(context, "网络连接异常，请检查网络");
				}
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1500);
	}

	/**
	 * 取消所有请求
	 */
	private void cancelAll() {
		if (null != requestUtils && 0 != requestUtils.size()) {
			int requestCount = requestUtils.size();
			for (int i = 0; i < requestCount; i++) {
				RequestUtil requestUtil = requestUtils.get(i);
				if (null != requestUtil) {
					Request request = requestUtil.getRequest();
					if (null != request)
						request.cancel();
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

	@Override
	public void onStop() {
		super.onStop();
		cancelAll();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * 请求图片信息
	 */
	public void getImageInfo() {
		RequestUtil requestUtil = new RequestUtil(context, true, Request.CONFIG_CACHE_SHORT, 
				true,"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "图片轮播请求结果====" + isusesInfo);
				String error = "0";
				try {
					error = isusesInfo.optString("error");
					String msgstr = isusesInfo.optString("msg");
					noticeOpen = isusesInfo.optString("broadcast");
					Message msg = handler.obtainMessage();
					if ("0".equals(error)) {
						imageviews = (ArrayList<ImageViews>) JSON.parseArray(
								isusesInfo.getJSONArray("picList").toString(),
								ImageViews.class);
						msg.what = 0;
						handler.sendMessage(msg);
					} else {
						msg.what = 2;
						msg.obj = msgstr;
						handler.sendMessage(msg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求轮播图出现未知错误!");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getImageInfo();
	}

	/**
	 * 处理图片请求Handler
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				loopView.setAdapter(new LocalAdp(context, imageviews));
				loopView.setBoundaryCaching(true);
				loopView.setAutoScrollDurationFactor(10d);
				loopView.setInterval(5000);
				loopView.startAutoScroll();
				indy.setViewPager(loopView);
				indy.requestLayout();
				break;
			case 2:
				if (msg.obj != null) {
					MyToast.getToast(context, String.valueOf(msg.obj));
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 图片轮播适配器
	 * 
	 * @author lenovo
	 * 
	 */
	private class LocalAdp extends PagerAdapter {
		private int count = 100;
		private Queue<View> views;
		private List<ImageViews> data;
		private LayoutInflater lay;
		private Context context;

		LocalAdp(Context ct, List<ImageViews> listNews) {
			views = new LinkedList<View>();
			data = listNews;
			lay = LayoutInflater.from(ct);
			context = ct;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View mage = views.poll();
			if (mage == null) {
				mage = lay.inflate(R.layout.item_image, null);
				mage.setId(count++);
			}
			ImageView iv = (ImageView) mage.findViewById(R.id.mage);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					skipToActivity(position,data.get(position).getPicUrl());
				}
			});
			
			ImageManager.getInstance().displayImage(
					data.get(position).getPicUrl(), iv,
					ImageManager.getViewsHeadOptions());
			container.addView(mage);
			return mage;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View mage = (View) object;
			views.add(mage);
			container.removeView(mage);
		}
		
		public void skipToActivity(int position,String imgUrl){
			switch (position) {
//			case 1:
//				getBasketball(getActivity(), 0);
//				break;
			case 0:
				if(noticeOpen.equals("0")) {
//					getBallData(getActivity(), 0);
				}else{
					skipToInformation(0);
				}
				break;
			case 2:
				Intent intent1 = new Intent(context,ApplyActivity.class);
				intent1.putExtra("img", imgUrl);
				context.startActivity(intent1);
				break;
			default:
				break;
			}
		}
	}
	
}
