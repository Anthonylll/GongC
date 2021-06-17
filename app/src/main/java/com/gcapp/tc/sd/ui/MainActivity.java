package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.fragment.FollowFragment;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.fragment.MyCenterFragment;
import com.gcapp.tc.fragment.WinLotteryFragment;
import com.gcapp.tc.sd.ui.win_dialog.widget.Effectstype;
import com.gcapp.tc.sd.ui.win_dialog.widget.NiftyDialogBuilder;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.PushUtil;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.VibratorView;
import com.testin.agent.TestinAgent;
import com.gcapp.tc.R;

public class MainActivity extends FragmentActivity implements OnClickListener {
	/**
	 * 功能：第一次打开软件进入的类，控制页面的5个板块
	 */
	public static final String TAG = "MainActivity";
	private Context context = MainActivity.this;
	boolean falg = true; // 标签，对象是否为同一天发起。
	public static Fragment scrollView;

	private ImageView iv_tab_hall, iv_tab_follow, iv_tab_win, iv_tab_center;// 主页面图标
	private LinearLayout layout_tab_hall, layout_tab_follow, layout_tab_win,
			layout_tab_center;// 图标布局

	private TextView tv_tab_hall, tv_tab_follow, tv_tab_win, tv_tab_center;
	private Intent intent;
	public static FragmentTransaction transaction;
//	public static LinearLayout ll_main;
	private UpdateManager mUpdateManager;
	private ConfirmDialog dialog;// 提示框
	private Effectstype effect;
	public Vibrator vibrator; // 震动器
	private SharedPreferences settings;// 保存购彩大厅彩种id位置
	private List<List<Schemes>> listAllSchemes = new ArrayList<List<Schemes>>();
	private List<String> list = new ArrayList<String>();
	private List<Schemes> listDetail;
	public static boolean isWin = false;
	private List<RequestUtil> requestUtils;
	private int indexId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		App.activityS.add(this);

		getVersion(); // 得到版本号
		vibrator = VibratorView.getVibrator(getApplicationContext());
//		startPush();
		TestinAgent.init(context, "80cfb8a537e67d8802816c451b5082f5",
				"wandoujia");
		init();
//	    InterfaceTest.postTest(context);
	}

	/**
	 * 清空选号数据
	 */
	@Override
	protected void onResumeFragments() {
		if (AppTools.list_numbers != null && AppTools.totalCount != 0) {
			AppTools.list_numbers.clear();
			AppTools.totalCount = 0;
		}
		if (AppTools.list_numbers != null) {
			AppTools.list_numbers.clear();
		}
		if (AppTools.totalCount != 0) {
			AppTools.totalCount = 0;
		}
		super.onResumeFragments();
	}

	/**
	 * 初始化整个程序需要的数据,并设置fragment
	 */
	private void init() {
		settings = this.getSharedPreferences("hall_lotterys", 0);
		boolean isSaved = false;// 有没有保存的标识
		if (settings.contains("isSavelottery")) {
			isSaved = settings.getBoolean("isSavelottery", false);
		}
		if (settings.contains("lotterysId")) {
			AppTools.lotterysId = settings.getString("lotterysId", null);
		}
		if (AppTools.allLotteryLogo == null) {
			AppTools.setLogo();
		}
		findView(); // 得到控件
		setListener(); // 设置监听
		setFragment();
		if (null == requestUtils)
			requestUtils = new ArrayList<RequestUtil>();
//		updateversions();// 判断执行版本更新
		getScaleParams();// 获取佣金比例和认购比例
	}

	/**
	 * 设置推送入口
	 */
	public void startPush() {
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
		// "api_key")
		String key = PushUtil.getMetaValue(MainActivity.this, "api_key");
		Log.i(TAG, "API_KEY" + key);
		// String pkgName = this.getPackageName();
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, key);
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());

		// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
		// CustomPushNotificationBuilder cBuilder = new
		// CustomPushNotificationBuilder(
		// getApplicationContext(), 0,
		// 0,
		// 0,
		// 0);
		// cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		// cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
		// | Notification.DEFAULT_VIBRATE);
		// cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
		// // cBuilder.setLayoutDrawable(resource.getIdentifier(
		// // "simple_notification_icon", "drawable", pkgName));
		// PushManager.setNotificationBuilder(this, 1, cBuilder);
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		// 图标
		iv_tab_hall = (ImageView) findViewById(R.id.iv_tab_hall);
		iv_tab_follow = (ImageView) findViewById(R.id.iv_tab_follow);
		iv_tab_win = (ImageView) findViewById(R.id.iv_tab_win);
		// iv_tab_info = (ImageView) findViewById(R.id.iv_tab_info);
		iv_tab_center = (ImageView) findViewById(R.id.iv_tab_center);

		// 图标布局
		layout_tab_hall = (LinearLayout) findViewById(R.id.layout_tab_hall);
		layout_tab_follow = (LinearLayout) findViewById(R.id.layout_tab_follow);
		// layout_tab_info = (LinearLayout) findViewById(R.id.layout_tab_info);
		layout_tab_win = (LinearLayout) findViewById(R.id.layout_tab_win);
		layout_tab_center = (LinearLayout) findViewById(R.id.layout_tab_center);

		// 文字
		tv_tab_hall = (TextView) findViewById(R.id.tv_tab_hall);
		tv_tab_follow = (TextView) findViewById(R.id.tv_tab_follow);
		tv_tab_win = (TextView) findViewById(R.id.tv_tab_win);
		tv_tab_center = (TextView) findViewById(R.id.tv_tab_center);

		// 这里是启动图片,找了半天
		// ll_main = (LinearLayout) this.findViewById(R.id.main);
		// if (AppTools.isShow) {
		// ll_main.setVisibility(View.VISIBLE);
		// // 让splash页面消失
		// handler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// if (ll_main.getVisibility() == 0) {
		// ll_main.setVisibility(View.GONE);
		// }
		// }
		// }, 5000);
		// }
		setFocuse();
	}

	// private Handler handler = new Handler() {
	//
	// };

	/**
	 * 弹出中奖提示框
	 */
	public void showWinDialog() {
		if (null != vibrator) {
			vibrator.vibrate(500);
		}
		NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
		effect = Effectstype.Shake;
		dialogBuilder.show();
	}

	/**
	 * 请求中奖详情
	 */
	public void toFindWininfo() {
		RequestUtil requestUtil = new RequestUtil(MainActivity.this, false, 0,
				true, "数据加载中") {
			@Override
			public void responseCallback(JSONObject item) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "查看中奖详情jsonObject--- " + item.toString());
				try {
					if ("0".equals(item.optString("error"))) {
						String schemeList = item.optString("schemeList");
						JSONArray array = new JSONArray(schemeList);
						JSONArray jsonArray2 = new JSONArray(array.toString());
						Schemes scheme = null;
						if (jsonArray2.length() == 0)
							return;
						// 循环得到每个对象
						for (int i = 0; i < jsonArray2.length(); i++) {
							// 如果取消了 则停止
							JSONObject items = jsonArray2.getJSONObject(i);
							String date = items.getString("date");
							if (!list.contains(date)) {
								falg = false;
								list.add(date);
								listDetail = new ArrayList<Schemes>();
							} else
								falg = true;
							JSONArray detail = new JSONArray(
									items.getString("dateDetail"));

							for (int j = 0; j < detail.length(); j++) {
								JSONObject items2 = detail.getJSONObject(j);
								scheme = new Schemes();
								scheme.setId(items2.optString("Id"));
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
								// 添加buyContent
								JSONArray array_contents = new JSONArray(
										items2.optString("buyContent"));
								if (array_contents != null) {
									List<LotteryContent> contents = new ArrayList<LotteryContent>();
									LotteryContent lotteryContent = null;
									for (int m = 0; m < array_contents.length(); m++) {
										lotteryContent = new LotteryContent();
										try {
											JSONArray array2 = new JSONArray(
													array_contents.optString(m));

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
													array_contents.optString(m));

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
								scheme.setPlayTypeID(items2
										.optInt("playTypeID"));
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
										.optInt("winMoneyNoWithTax"));
								scheme.setChaseTaskID(items2
										.getInt("chaseTaskID"));
								scheme.setWinNumber(items2
										.optString("winNumber"));

								scheme.setDateTime(items2.optString("datetime"));
								scheme.setDescription(items2
										.optString("description"));
								scheme.setIsChase(items2.optInt("isChase"));
								scheme.setMultiple(items2.optInt("multiple"));
								scheme.setFromClient(items2
										.getInt("FromClient"));
								scheme.setMyBuyMoney(items2
										.getInt("myBuyMoney") + "");
								scheme.setMyBuyShare(items2
										.optInt("myBuyShare"));

								listDetail.add(scheme);
							}
							if (!falg)
								listAllSchemes.add(listDetail);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					if (RequestUtil.DEBUG)
						Log.e(TAG, "查看中奖详情错误" + e.getMessage());
				}
				if (listAllSchemes == null || listAllSchemes.size() == 0
						|| listAllSchemes.get(0).size() == 0) {
					return;
				}
				Schemes scheme = listAllSchemes.get(0).get(0);
				Intent intent = null;

				if (scheme.getIsChase() == 0)
					if ("72".equals(scheme.getLotteryID())
							|| "73".equals(scheme.getLotteryID())
							|| "45".equals(scheme.getLotteryID()))
						intent = new Intent(context,
								MyCommonLotteryInfo_jc.class);
					else
						intent = new Intent(context, MyCommonLotteryInfo.class);
				else
					intent = new Intent(context, MyFollowLotteryInfo.class);

				intent.putExtra("scheme", scheme);
				startActivity(intent);
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "查看中奖详情出错--- " + error.getMessage());
			}
		};
		requestUtil.getWinDetail();
		requestUtils.add(requestUtil);
	}

	/**
	 * 得到当前版本号
	 */
	public void getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			AppTools.version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置初始化的fragment
	 */
	private void setFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.main_center, new HallFragment(), "hall");
		indexId = R.id.layout_tab_hall;
		transaction.commitAllowingStateLoss();
		setFocuse();
		iv_tab_hall.setBackgroundResource(R.drawable.log_hall_selected);
		tv_tab_hall.setTextColor(getResources().getColor(R.color.main_red));
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		dialog = new ConfirmDialog(this, R.style.dialog);
		layout_tab_hall.setOnClickListener(this);
		layout_tab_follow.setOnClickListener(this);
		layout_tab_win.setOnClickListener(this);
		// layout_tab_info.setOnClickListener(this);
		layout_tab_center.setOnClickListener(this);
	}

	/**
	 * 检测版本的更新
	 */
	private void updateversions() {
		RequestUtil requestUtil = new RequestUtil(MainActivity.this, false, 0) {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "更新jsonObject--- " + jsonObject.toString());
				AppTools.appobject.setUpgrade(jsonObject.optString("upgrade"));
				// AppTools.appobject.setApkName(jsonObject.optString("apkName"));
				AppTools.appobject.setVersionCode(jsonObject
						.optString("currentappversion"));
				AppTools.appobject.setDownLoadUrl(jsonObject
						.optString("downLoadUrl"));

				if (AppTools.appobject.getUpgrade().equals("True")) {
					// 这里来检测版本是否需要更新
					mUpdateManager = new UpdateManager(MainActivity.this,
							AppTools.appobject.getDownLoadUrl());
					if (RequestUtil.DEBUG)
						Log.i(TAG,
								"更新url--- "
										+ AppTools.appobject.getDownLoadUrl());
					mUpdateManager.checkUpdateInfo();

				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "检查更新出错--- " + error.getMessage());
			}
		};
		requestUtil.checkUpdateApp();
		requestUtils.add(requestUtil);

	}

	/**
	 * 获取合买佣金比列和合买最少购买比列
	 */
	public void getScaleParams() {
		RequestUtil requestUtil = new RequestUtil(MainActivity.this, false, 0) {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG,
							"获取合买佣金比列和合买最少购买比列jsonObject--- "
									+ jsonObject.toString());
				if (null != jsonObject) {
					if ("0".equals(jsonObject.optString("error"))) {
						AppTools.followCommissionScale = jsonObject
								.optString("yongjin"); // 合买佣金比列
						AppTools.followLeastBuyScale = jsonObject
								.optString("rengou"); // 合买最少购买比列
					}
				} else {
					if (RequestUtil.DEBUG)
						Log.e(TAG, "获取合买佣金比列和合买最少购买比列失败---返回结果为空");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取合买佣金比列和合买最少购买比列出错--- " + error.getMessage());
			}
		};
		requestUtil.getBuyParams();
		requestUtils.add(requestUtil);
	}

	/**
	 * 获取最近一条中奖信息
	 */
	public void getCurrentWinInfo(String uid) {
		RequestUtil requestUtil = new RequestUtil(MainActivity.this, false, 0) {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG,
							"获取最近一条中奖信息jsonObject--- " + jsonObject.toString());
				if (null != jsonObject) {
					if ("0".equals(jsonObject.optString("error"))) {// 无中奖
						isWin = false;
//						showWinDialog();
					} else if ("1".equals(jsonObject.optString("error"))) {// 有中奖信息
						isWin = true;
//						showWinDialog();
					}
				}

			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取最近一条中奖信息出错--- " + error.getMessage());
				isWin = false;
			}
		};
		requestUtil.getCurrentWinInfo(uid);
		requestUtils.add(requestUtil);
	}

	/**
	 * 公用监听方法
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		transaction = getSupportFragmentManager().beginTransaction();

		switch (v.getId()) {
		// 购彩大厅
		case R.id.layout_tab_hall:
			if (R.id.layout_tab_hall != indexId) {
				indexId = R.id.layout_tab_hall;
				setFocuse();
				iv_tab_hall.setBackgroundResource(R.drawable.log_hall_selected);
				tv_tab_hall.setTextColor(getResources().getColor(
						R.color.main_red));
				transaction.replace(R.id.main_center, new HallFragment(),
						"hall");
				transaction.commitAllowingStateLoss();
			}
			break;
		// 合买大厅
		case R.id.layout_tab_follow:
			if (R.id.layout_tab_follow != indexId) {
				indexId = R.id.layout_tab_follow;
				setFocuse();
				iv_tab_follow
						.setBackgroundResource(R.drawable.log_follow_selected);
				tv_tab_follow.setTextColor(getResources().getColor(
						R.color.main_red));
				toFollow();
			}
			break;
		// 开奖公告
		case R.id.layout_tab_win:
			if (R.id.layout_tab_win != indexId) {
				indexId = R.id.layout_tab_win;
				setFocuse();
				iv_tab_win.setBackgroundResource(R.drawable.log_win_selected);
				tv_tab_win.setTextColor(getResources().getColor(
						R.color.main_red));
				transaction.replace(R.id.main_center, new WinLotteryFragment(),
						"winLottery");
				transaction.commitAllowingStateLoss();
			}
			break;
		// 个人中心
		case R.id.layout_tab_center:
			if (R.id.layout_tab_center != indexId) {
				indexId = R.id.layout_tab_center;
				setFocuse();
				iv_tab_center
						.setBackgroundResource(R.drawable.log_center_selected);
				tv_tab_center.setTextColor(getResources().getColor(
						R.color.main_red));
				ToMyCenterActivity();
			}
			break;
		}
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dialog.show();
			dialog.setDialogContent("您确认退出" + getText(R.string.app_name)
					+ "系统？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
						manager.restartPackage(getPackageName());
						for (Activity activity : App.activityS) {
							activity.finish();
						}
						MainActivity.this.finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
					}
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 点击跟单
	 */
//	private void ToFollow() {
//		if (null == AppTools.user) {
//			intent = new Intent(MainActivity.this, LoginActivity.class);
//			intent.putExtra("mark", "1");
//			MainActivity.this.startActivity(intent);
//		} else {
//			transaction.replace(R.id.main_center, new FollowFragment(),
//					"follow");
//			transaction.commitAllowingStateLoss();
//
//		}
//	}

	/**
	 * 点击个人中心
	 */
	private void ToMyCenterActivity() {
		if (null == AppTools.user) {
			intent = new Intent(MainActivity.this, LoginActivity.class);
			intent.putExtra("mark", "0");
			MainActivity.this.startActivity(intent);
		} else {
			transaction.replace(R.id.main_center, new MyCenterFragment(),
					"center");
			transaction.commitAllowingStateLoss();

		}
	}

	/**
	 * 跳转到个人中心
	 */
	public static void toCenter() {
		try {
			transaction.replace(R.id.main_center, new MyCenterFragment(),
					"center");
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			Log.i("x", "登陆错误" + e.getMessage());
		}
	}
	
	/**
	 * 跳转到跟单
	 */
	public static void toFollow() {
		try {
			transaction.replace(R.id.main_center, new FollowFragment(),
					"follow");
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			Log.i("x", "登陆错误" + e.getMessage());
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		cancelAll();
	}

	/**
	 * 取消所有请求
	 */
	private void cancelAll() {
		if (null != requestUtils && 0 != requestUtils.size()) {
			for (int i = 0; i < requestUtils.size(); i++) {
				RequestUtil requestUtil = requestUtils.get(i);
				if (null != requestUtil) {
					Request request = requestUtil.getRequest();
					if (null != request)
						request.cancel();
				}
			}
		}
	}

	/**
	 * 设置未选中的状态
	 */
	private void setFocuse() {
		iv_tab_hall.setBackgroundResource(R.drawable.log_hall_unselected);
		tv_tab_hall.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_follow.setBackgroundResource(R.drawable.log_follow_unselected);
		tv_tab_follow.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_win.setBackgroundResource(R.drawable.log_win_unselected);
		tv_tab_win.setTextColor(getResources().getColor(R.color.main_gray));
		// iv_tab_info.setBackgroundResource(R.drawable.log_info_unselected);
		// tv_tab_info.setTextColor(getResources().getColor(R.color.main_gray));
		iv_tab_center.setBackgroundResource(R.drawable.log_center_unselected);
		tv_tab_center.setTextColor(getResources().getColor(R.color.main_gray));
	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		setFragment();
		super.onNewIntent(intent);
	}

	/**
	 * 重写onResume方法
	 */
	@Override
	protected void onResume() {
		super.onResume();
		updateversions();
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.main_center);
		if (fragment instanceof FollowFragment)
			indexId = R.id.layout_tab_follow;
		if (fragment instanceof MyCenterFragment)
			indexId = R.id.layout_tab_center;
		if (fragment instanceof WinLotteryFragment)
			indexId = R.id.layout_tab_win;
		if (fragment instanceof HallFragment)
			indexId = R.id.layout_tab_hall;
		SharedPreferences settings = context
				.getSharedPreferences("app_user", 0);
		String uid = settings.getString("uid", "");
		if (!"".equals(uid) && !"-1".equals(uid)) {
			getCurrentWinInfo(uid);
		}
	}

	/**
	 * 更新界面
	 */
	public void update() {
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.main_center);
		if (fragment instanceof HallFragment)
			((HallFragment) fragment).update();
	}

}
