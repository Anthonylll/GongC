package com.gcapp.tc.sd.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.sd.ui.adapter.GreatManDetailAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 大神详情页面
 */
public class GreatManActivity extends Activity implements OnScrollListener{

	private int pageIndex = 1; // 页码
	private int pageSize = 10; // 每页显示行数
	private int sort = 5; // 排序方式
	private int isPurchasing = 1; // 返回类型
	private List<Schemes> listSchemeDetail = new ArrayList<Schemes>();
	private Context mContext= GreatManActivity.this;;
	private String TAG = "GreatManActivity";
	private PullToRefreshListView man_programme_list;
	private TextView recently_hit_text;
	private TextView great_man_user_name;
	private TextView winning_probability;
	private ImageView great_man_user_avatar;
	private GreatManDetailAdapter manDetailAdapter;
	private String greatmanId = "";
	private String greatmanName = "";
	private String greatmanImg = "";
	private Bitmap bitmap_loadimage;
	private MyHandler_image mHandler_image = new MyHandler_image();
	private MyAsynTask_loadImage task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_great_man);
		App.activityS.add(this);

		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化视图
	 */
	public void initView() {
		man_programme_list = (PullToRefreshListView) findViewById(R.id.man_programme_list);
		recently_hit_text = (TextView) findViewById(R.id.recently_hit_text);
		great_man_user_name = (TextView) findViewById(R.id.great_man_user_name);
		winning_probability = (TextView) findViewById(R.id.winning_probability);
		great_man_user_avatar = (ImageView) findViewById(R.id.great_man_user_avatar);
		
		man_programme_list.setMode(Mode.BOTH);
	}
	
	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		OnItemClickListener listener = new MyItemCLickListener();
		man_programme_list.setOnItemClickListener(listener);
		
		man_programme_list
		.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				man_programme_list
						.getLoadingLayoutProxy(true, false)
						.setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
				if (NetWork.isConnect(mContext)) {
					listSchemeDetail.clear();
					pageIndex = 1;
					initData();
				} else {
					MyToast.getToast(mContext, "网络连接异常，请检查网络");
					man_programme_list.onRefreshComplete();
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
					man_programme_list.onRefreshComplete();
				}
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		Intent intent = getIntent();
        if (intent != null) {
        	greatmanId = intent.getStringExtra("manID");
        	greatmanName = intent.getStringExtra("greatmanName");
        }
		
		getBetLotteryData();
	}

	/**
	 * 提交我的发单投注记录请求
	 */
	public void getBetLotteryData() {
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"正在加载中...", 1) {
			@Override
			public void responseCallback(JSONObject item) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "合买投注记录请求结果：" + item);
				try {
					if ("0".equals(item.optString("error"))) {
						initTopData(item);
						String schemeList = item.optString("schemeList");
						JSONArray array = new JSONArray(schemeList);
						if(array.length() == 0) {
							MyToast.getToast(mContext, "没有更多数据啦！");
						}else{
						JSONArray jsonArray2 = new JSONArray(array.toString());

						Schemes scheme ;
						for (int i = 0; i < jsonArray2.length(); i++) {
							JSONObject items = jsonArray2.getJSONObject(i);
							scheme = new Schemes();
							JSONArray detail = new JSONArray(
									items.getString("dateDetail"));

							for (int j = 0; j < detail.length(); j++) {
								JSONObject items2 = detail.getJSONObject(j);

								scheme.setId(items2.optString("ID"));
								scheme.setSchemeNumber(items2
										.optString("schemeNumber"));

								scheme.setAssureMoney(items2
										.optDouble("assureMoney"));
								scheme.setAssureShare(items2
										.optInt("assureShare"));
								scheme.setBuyed(items2.optString("buyed"));
								scheme.setInitiateName(LotteryUtils
										.addRexStar(items2
												.optString("initiateName")));
								scheme.setInitiateUserID(items2
										.optString("initiateUserID"));
								scheme.setIsPurchasing(items2
										.optString("isPurchasing"));

								String optString = items2
										.optString("detailMoney");
								if ("".equals(optString)) {
									optString = "0";
								}
								double detailMoney = Double
										.parseDouble(optString);
								scheme.setDetailMoney(detailMoney);
								String optString2 = items2
										.optString("handselMoney");
								if ("".equals(optString2)) {
									optString2 = "0";
								}
								double handselMoney = Double
										.parseDouble(optString2);
								scheme.setHandselMoney(handselMoney);

								scheme.setIsuseID(items2.optString("isuseID"));
								scheme.setIsuseName(items2
										.optString("issueName"));
								scheme.setLevel(items2.optInt("level"));
								scheme.setLotteryID(items2
										.optString("lotteryID"));
								scheme.setLotteryName(items2
										.optString("lotteryName"));
								scheme.setLotteryNumber(items2
										.optString("lotteryNumber"));
								// 注意这里的buycontent内容中，包含的是JSONObject
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
								scheme.setTotalmoney(items2.optInt("money"));
								scheme.setSelfmoney(items2.optDouble("myBuyMoney"));
								scheme.setPlaytypeName(items2
										.optString("playName"));
								scheme.setPlayTypeName(items2.optString("PassType"));
								scheme.setQuashStatus(items2
										.optInt("quashStatus"));
								scheme.setRecordCount(items2
										.optInt("RecordCount"));
								scheme.setSchedule(items2.optInt("schedule"));
								scheme.setSchemeBonusScale(items2
										.optDouble("schemeBonusScale"));
								scheme.setSchemeCommission(items2
										.optDouble("SumBonus"));
								scheme.setSchemeIsOpened(items2
										.optString("schemeIsOpened"));
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
								scheme.setRewardMoney(items2
										.optDouble("AddWinMoney"));
								scheme.setShareWinMoney(items2
										.optDouble("shareWinMoney"));
								scheme.setWinNumber(items2
										.optString("winNumber"));

								scheme.setDateTime(items2.optString("datetime"));

								scheme.setDescription(items2
										.optString("description"));

								scheme.setIsChase(items2.optInt("isChase"));
								scheme.setChaseTaskID(items2
										.getInt("chaseTaskID"));
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
//								scheme.setSchemeStatus(items2
//										.optString("schemeStatus"));
								scheme.setSchemeStatus(items2
										.optString("labState"));
								scheme.setFollow_state(items2.optString("labState"));
								scheme.setIsHide(items2.optInt("isHide"));
								scheme.setSecretMsg(items2
										.optString("secretMsg"));
							}
							listSchemeDetail.add(scheme);
						}

					}}
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("x", "myAllLottery 错误" + e.getMessage());
				}
				
				if(pageIndex >1) {
					manDetailAdapter.notifyDataSetChanged();
				}else{
					initAdapter();
				}
				
				if (item.toString().equals("-500")) {
					MyToast.getToast(mContext, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(mContext, "抱歉，请求出现未知错误..");
				man_programme_list.onRefreshComplete();
				man_programme_list.setMode(Mode.PULL_FROM_START);
				if (RequestUtil.DEBUG)
					Log.e("", "请求报错" + error.getMessage());
			}
		};
		requestUtil.getBetInfo(AppTools.lotteryIds, pageIndex, pageSize, sort,
				isPurchasing, 4,greatmanId);
	}
	
	/**
	 * 初始化adapter
	 */
	private void initAdapter() {
		manDetailAdapter = new GreatManDetailAdapter(mContext, listSchemeDetail);
		man_programme_list.setAdapter(manDetailAdapter);
	}
	
	/**
	 * 大神相关信息设置
	 */
	private void initTopData(JSONObject item) {
		man_programme_list.onRefreshComplete();
		try {
			if(item.has("winExplain")) {
				recently_hit_text.setText(item.getString("winExplain"));
			}
			if(item.has("HeadUrl")) {
				greatmanImg = AppTools.url+item.getString("HeadUrl");
			}
			if(item.has("Winning")) {
				if(!item.getString("Winning").equals("")){
					winning_probability.setText(item.getString("Winning")+"%");
				}
			}
			great_man_user_name.setText(greatmanName);
			task = new MyAsynTask_loadImage();
			task.execute();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * listview的子项点击监听
	 */
	class MyItemCLickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			
			Schemes scheme = listSchemeDetail.get(position-1);
			Intent intent = null;
			if ("False".equalsIgnoreCase(scheme.getIsPurchasing())) {
				if ("72".equals(scheme.getLotteryID())
						|| "73".equals(scheme.getLotteryID())
						|| "45".equals(scheme.getLotteryID())) {
					intent = new Intent(mContext,
							MyCommonLotteryInfo_joindetail_jc.class);
				} else {
					intent = new Intent(mContext,
							MyCommonLotteryInfo_joindetail.class);
				}
			} else {
				if (scheme.getIsChase() == 0)
					if ("72".equals(scheme.getLotteryID())
							|| "73".equals(scheme.getLotteryID())
							|| "45".equals(scheme.getLotteryID())) {
						intent = new Intent(mContext,
								MyCommonLotteryInfo_jc.class);
					} else {
						if (scheme.getIsHide() == 0) {
							intent = new Intent(mContext,
									MyCommonLotteryInfo.class);
						} else {
							intent = new Intent(mContext,
									MyCommonLotteryInfo_join.class);
						}
					}
				else
					intent = new Intent(mContext, MyFollowLotteryInfo.class);
			}

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", scheme);
			mContext.startActivity(intent);
		}
	}
	
	/**
	 * 异步任务 用来后台获取数据
	 * 
	 */
	class MyAsynTask_loadImage extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			URL myFileUrl = null;
			HttpURLConnection conn = null;
			InputStream is = null;
			try {
				myFileUrl = new URL(greatmanImg);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				conn = (HttpURLConnection) myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
				bitmap_loadimage = BitmapFactory.decodeStream(is);
				if (null == bitmap_loadimage) {
					return "-1";
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(is != null && conn != null){
						is.close();
						conn.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "11";
		}

		@Override
		protected void onPostExecute(String result) {
			mHandler_image.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}
	
	/**
	 * 处理异步请求结果
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler_image extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -500:
				break;
			case 11:// 成功
				great_man_user_avatar.setImageBitmap(bitmap_loadimage);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	public void back(View view) {
		this.finish();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

}
