package com.gcapp.tc.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ParseException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gcapp.tc.dataaccess.AppObject;
import com.gcapp.tc.dataaccess.DtMatch;
import com.gcapp.tc.dataaccess.DtMatchBJDC;
import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.dataaccess.Users;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * App的 工具类
 * 
 * @author SLS003
 */
public class AppTools {
	private static final boolean DEBUG = true;
	private static final String TAG = "AppTools";
	public static String version;
	public static int flag = 0;
	// 用户
	public static Users user = null;
	public static AppObject appobject = new AppObject();
	public static String[] Imagearray;
	public static long width;
	public static long height;
	public static boolean isShow = true;
	public static String imei = "";
	public static int index = 1; // 第几次进入主界面
	public static int schemeId; // 方案id
	public final static String MD5_key = "phRXtyop97Ssfd5g5erD98Uwe55Kv9TT";
	// key值，校验用
	public static String key = MD5.md5(AppTools.MD5_key);
	/** 百度推送的userId */
	public static String push_userId = "";
	/** 百度推送的channelId */
	public static String push_channelId = "";
	/** 百度推送的DeviceType */
	public static String push_DeviceType = "3";
	/** 0为离线 1为在线 */
	public static String Status = "1";
	public static String APP_NAME = "佳衡";// 软件名称
	public static String SERVICE_PHONE = "0571-00000000";
	public static long totalCount; // 用户所选总注数
	public static List<SelectedNumbers> list_numbers;
	// 用户所投的集合
	public static Lottery lottery; // 彩种对象
	public static int bei = 1;
	public static int qi; // 追多少期
	public static String type; // 竞彩过关类型
	public static String ball; // 竞彩过关数据---
	public static boolean isCanBet = true; // 高频彩是否能投注
	public static String followCommissionScale = ""; // 合买佣金比列
	public static String followLeastBuyScale = ""; // 合买最少购买比列
	public static List<List<DtMatch>> list_Matchs; // 比赛对阵信息
	public static List<List<DtMatch_Basketball>> DtMatch_Basketball; // 比赛对阵信息
	public static List<List<DtMatchBJDC>> list_Matchs_bjdc; // 比赛对阵信息
	public static List<List<DtMatch>> list_singlepass_Matchs; // 单关比赛对阵信息
	public static List<List<DtMatch_Basketball>> DtMatch_Basketball_single; // 篮球单关比赛对阵信息
	public static String username, userpass;// 没有记住密码，但是登录未退出刷新个人中心数据 
	public static boolean canAlipay = true;// APP端是否有支付方式
	/** 正式环境*/
//	public final static String url = "http://45.116.164.137";
//	public final static String url = "http://gzshengrong.cn";
	public final static String url = "http://120.27.129.3";
	public final static String zfbpath = url
			+ "/Home/Room/OnlinePay/Swiftpass/Send.aspx";
	public final static String path = url + "/ajax/AppGateway.ashx";
	/** 比分直播*/
	public final static String livePath = "http://cpapi.xiudongdianzi.cn/api/lottery/selectLotInfo";
	
	public final static String Qpath = url + "/clientsoft/download.aspx";
	public final static String ylPath = url
			+ "/Home/Room/OnlinePay/YLAPP/purchase.aspx";
	public final static String server_url = "https://msp.alipay.com/x.htm";
	public static String serverTime = "";
	public static String winToday = "";
	public final static String[] names = { "opt", "auth", "info" };
	public final static String[] otherNames = { "opt", "mid"};
	// 标准版JC - 29时时彩
	public final static String lotteryIds = "72,73,74,75,45";
	// 彩种ID 对应 的 图片
	public static Map<String, Integer> allLotteryLogo = null;
	// 彩种ID 对应的 名称
	public static Map<String, String> allLotteryName = null;
	public static String lotterysId = "";// 用于保存所有的彩种位置
	public static long sum_Income_Money = 0; // 总收入
	public static long sum_Expense_Money = 0; // 总支出
	public static long sum_Bonus_Money = 0; // 中奖
	public static boolean isVibrator;
	public static boolean isSensor;
	public static String isPushKJ;
	public static String isPushZJ;
	public static final int BASE_ID = 0;
	public static final int RQF_PAY = BASE_ID + 1;
	public static final String VERSION = "version";
	public static final String partner = "partner";
	public static final String action = "action";
	public static final String actionUpdate = "update";
	public static final String data = "data";
	public static final String platform = "platform";
	public static final int Lottery_ALL = 1; // 个人中心 查看类型 为 全部
	public static final int Lottery_WIN = 2; // 个人中心 查看类型 为 中奖
	public static final int Lottery_WAIT = 3; // 个人中心 查看类型 为 待开奖
	public static final int Lottery_FOLLOW = 4; // 个人中心 查看类型 为 追号
	public static final int Lottery_CHIPPED = 5; // 个人中心 查看类型 为 合买
	public static final int ERROR_SUCCESS = 0;
	public static final int ERROR_UNLONGIN = -100;
	public static final int ERROR_TOTAL = -102;
	public static final int ERROR_MONEY = -134;

	/** 绑定银行信息的类型 **/
	public static final int BANK_TYPE = 1;
	public static final int BANK_TYPE_TWO = 11;
	public static final int PROVINCE_TYPE = 2;
	public static final int PROVINCE_TYPE_TWO = 12;
	public static final int CITY_TYPE = 3;
	public static final int CITY_TYPE_TWO = 13;
	public static final int ZHI_TYPE = 4;
	public static final int QUESTION_TYPE = 5;
	public static final int QUESTION_TYPE2 = 6;
	public static final int MONEY_TYPE = 7;
	public static ArrayList<Integer> level_star_list = null;
	public static ArrayList<Integer> level_medal_list = null;
	public static ArrayList<Integer> level_cup_list = null;
	public static ArrayList<Integer> level_crown_list = null;
	public static int currentScoring;
	public static int totalScoring;
	public static int totalConversionScoring;
	public static int scoringExchangerate;
	public static int optScoringOfSelfBuy;
	// 新增字段
	public static String isSale = "";
	/** 中奖信息*/
	public static String winMessage = "";

	/**
	 * 设置ListView 的高度
	 */
	public static void setHight(BaseAdapter adapter, ListView listView) {
		int totalHeight = 0;
		for (int i = 0, len = adapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 设置战绩等级图片
	 */
	public static void setLevelList() {
		level_star_list = new ArrayList<Integer>();
		int[] star = { R.drawable.star_1, R.drawable.star_2, R.drawable.star_3,
				R.drawable.star_4, R.drawable.star_5, R.drawable.star_6,
				R.drawable.star_7, R.drawable.star_8, R.drawable.star_9 };
		for (int aStar : star) {
			level_star_list.add(aStar);
		}
		level_medal_list = new ArrayList<Integer>();
		int[] medal = { R.drawable.star_1, R.drawable.star_2,
				R.drawable.star_3, R.drawable.star_4, R.drawable.star_5,
				R.drawable.star_6, R.drawable.star_7, R.drawable.star_8,
				R.drawable.star_9 };
		for (int aMedal : medal) {
			level_medal_list.add(aMedal);
		}
		level_cup_list = new ArrayList<Integer>();
		int[] cup = { R.drawable.star_1, R.drawable.star_2, R.drawable.star_3,
				R.drawable.star_4, R.drawable.star_5, R.drawable.star_6,
				R.drawable.star_7, R.drawable.star_8, R.drawable.star_9 };
		for (int aCup : cup) {
			level_cup_list.add(aCup);
		}
		level_crown_list = new ArrayList<Integer>();
		int[] crown = { R.drawable.star_1, R.drawable.star_2,
				R.drawable.star_3, R.drawable.star_4, R.drawable.star_5,
				R.drawable.star_6, R.drawable.star_7, R.drawable.star_8,
				R.drawable.star_9 };
		for (int aCrown : crown) {
			level_crown_list.add(aCrown);
		}
	}

	/**
	 * 设置彩种ID和对应的彩种图片logo
	 */
	public static void setLogo() {
		allLotteryLogo = new LinkedHashMap<String, Integer>();
		/** 双色球 **/
		AppTools.allLotteryLogo.put("5", R.drawable.log_lottery_ssq);
		/** 大乐透 **/
		AppTools.allLotteryLogo.put("39", R.drawable.log_lottery_dlt);
		/** 时时彩 **/
		AppTools.allLotteryLogo.put("28", R.drawable.log_lottery_ssc);
		/** 竞彩足球 **/
		AppTools.allLotteryLogo.put("72", R.drawable.log_lottery_jczq);
		/** 竞彩篮球 **/
		AppTools.allLotteryLogo.put("73", R.drawable.log_lottery_jclq);
		/** 江苏快3 **/
		AppTools.allLotteryLogo.put("83", R.drawable.log_lottery_k3);
		/** 胜负彩 **/
		AppTools.allLotteryLogo.put("74", R.drawable.log_lottery_sfc);
		/** 任选九 **/
		AppTools.allLotteryLogo.put("75", R.drawable.log_lottery_rx9);
		/** 广东11选5 **/
		AppTools.allLotteryLogo.put("78", R.drawable.log_lottery_gd11x5);
		/** 北京单场 **/
		AppTools.allLotteryLogo.put("45", R.drawable.log_lottery_gd11x5);
		addlottery();
	}

	/**
	 * 要求的彩种的几种排序方式
	 */
	public static void addlottery() {
		int a = 0;
		allLotteryName = new LinkedHashMap<String, String>();
		if (lotterysId != null && !"".equals(lotterysId)) {
			String[] ids = lotterysId.split(",");

			for (int i = 0; i < ids.length; i++) {
				String name = LotteryUtils.getTitleText(ids[i]);
				if (name.equals("竞足单关")) {
					allLotteryName.put(name, "72");
				} else if (name.equals("竞篮单关")) {
					allLotteryName.put(name, "73");
				} else {
					allLotteryName.put(name, ids[i]);
				}
			}
		} else {
			allLotteryName.put("竞彩足球", "72");
			allLotteryName.put("竞足单关", "72");
			allLotteryName.put("竞彩篮球", "73");
			allLotteryName.put("竞篮单关", "73");
			allLotteryName.put("北京单场", "45");
//			allLotteryName.put("广东11选5", "78");
//			allLotteryName.put("双色球", "5");
//			allLotteryName.put("胜负彩", "74");
//			allLotteryName.put("任选九", "75");
//			allLotteryName.put("大乐透", "39");
//			allLotteryName.put("重庆时时彩", "28");
//			allLotteryName.put("江苏快3", "83");
		}
	}

	/**
	 * 将Set进行排序 转为list
	 * 
	 * @param set
	 *            set对象
	 * @return
	 */
	public static List<String> sortSet(Set<String> set) {
		List<String> list = new ArrayList<String>();
		List<Integer> list1 = new ArrayList<Integer>();
		for (String str : set) {
			list1.add(Integer.parseInt(str));
		}
		Collections.sort(list1);
		for (int i = 0; i < list1.size(); i++) {
			int num = list1.get(i);
			String number = num + "";
			if (!set.contains(num + "")) {
				number = "0" + num;
			}
			list.add(number);
		}
		return list;
	}

	/**
	 * 将值转换成 毫秒
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getTimestamp(String date) throws ParseException,
			java.text.ParseException {
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		long l = date1.getTime();
		return l;
	}

	/**
	 * 根据年月 得到当月的共有几天
	 */
	public static int getLastDay(int year, int month) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.set(Calendar.YEAR, year); // year为 int
		time.set(Calendar.MONTH, month - 1);// 注意,Calendar对象默认一月为0
		int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
		return day;
	}

	/**
	 * 传入一个毫秒类型转成 mm:ss 的类型的字符串
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		String showDays = "0";
		String showHour = "0";
		String showMinutes = "0";
		String showSeconds = "0";
		if (days < 10) {
			showDays = "0" + days;
		} else {
			showDays = days + "";
		}
		if (hours < 10) {
			showHour = "0" + hours;
		} else {
			showHour = "" + hours;
		}
		if (minutes < 10) {
			showMinutes = "0" + minutes;
		} else {
			showMinutes = "" + minutes;
		}
		if (seconds < 10) {
			showSeconds = "0" + seconds;
		} else {
			showSeconds = "" + seconds;
		}

		if (mss > 1000 * 60 * 60 * 24)
			return showDays + "天" + showHour + "时";
		else if (mss > 1000 * 60 * 60)
			return showHour + "时" + showMinutes + "分";
		else if (mss > 1000 * 60)
			return showMinutes + "分" + showHour + "秒";
		else
			return showMinutes + "分" + showSeconds + "秒";
	}

	/**
	 * 解析后台获取的所有彩种数据
	 * 
	 * @param isusesInfo
	 *            ：JSON对象
	 * @return
	 */
	public static String getDate(JSONObject isusesInfo) {
		try {
			if (null != isusesInfo) {
				AppTools.serverTime = isusesInfo.optString("serverTime");
				AppTools.winToday = isusesInfo.optString("winToday");
				JSONArray array = new JSONArray(
						isusesInfo.getString("isusesInfo"));
				for (int i = 0; i < array.length(); i++) {
					if (array.get(i).toString().length() < 5) {
						continue;
					}
					JSONObject object = array.getJSONObject(i);
					if (null != object) {
						for (int j = 0; j < HallFragment.listLottery.size(); j++) {
							if (HallFragment.listLottery.get(j).getLotteryID()
									.equals(object.optString("lotteryid"))) {
								// 绑定值
								HallFragment.listLottery.get(j).setLotteryName(
										HallFragment.listLottery.get(j)
												.getLotteryName());
								HallFragment.listLottery.get(j).setIsuseId(
										object.optString("isuseId"));
								HallFragment.listLottery.get(j).setAddaward(
										object.optString("addaward"));
								HallFragment.listLottery.get(j).setAddawardsingle(
										object.optString("addawardsingle"));
								HallFragment.listLottery
										.get(j)
										.setLotteryDescription(
												object.optString("Description"));
								HallFragment.listLottery.get(j)
										.setLotteryAgainst(
												object.optString("Against"));
								HallFragment.listLottery.get(j).setIsuseName(
										object.optString("isuseName"));// 当前期
								HallFragment.listLottery.get(j).setIsSale(
										object.optString("isSale"));
								HallFragment.listLottery.get(j).setStarttime(
										object.optString("starttime"));
								HallFragment.listLottery.get(j).setEndtime(
										object.optString("endtime"));
								HallFragment.listLottery
										.get(j)
										.setLastIsuseName(
												object.optString("lastIsuseName"));
								HallFragment.listLottery
										.get(j)
										.setLastWinNumber(
												object.optString("lastWinNumber"));
								HallFragment.listLottery
										.get(j)
										.setOriginalTime(
												object.optString("originalTime"));
								HallFragment.listLottery.get(j).setExplanation(
										object.optString("explanation"));
								long nowTime = 0;
								try {
									long endtime = AppTools
											.getTimestamp(HallFragment.listLottery
													.get(j).getEndtime());
									long originalTime = AppTools
											.getTimestamp(HallFragment.listLottery
													.get(j).getOriginalTime());

									long se = AppTools
											.getTimestamp(AppTools.serverTime);
									nowTime = System.currentTimeMillis();
									HallFragment.listLottery.get(j)
											.setDistanceTime(
													endtime - se + nowTime);
									HallFragment.listLottery
											.get(j)
											.setDistanceTime2(
													originalTime - se + nowTime);
								} catch (Exception ex) {
									HallFragment.listLottery.get(j)
											.setDistanceTime(0);
									ex.printStackTrace();
								}
								String dtCanChase = object
										.optString("dtCanChaseIsuses");
								List<String> dt = new ArrayList<String>();
								if (dtCanChase.length() != 0) {
									JSONArray arr = new JSONArray(dtCanChase);
									for (int k = 0; k < arr.length(); k++) {
										JSONObject ob = arr.getJSONObject(k);
										dt.add(ob.optString("isuseId"));
									}
								}
								String detail = object.optString("dtMatch");
								JSONArray array2 = new JSONArray(new JSONArray(
										detail).toString());
								if (array2.length() != 0) {
									HallFragment.listLottery
											.get(j)
											.setDtmatch(
													array2.getJSONObject(0)
															.optString(
																	"mainTeam")
															+ "vs"
															+ array2.getJSONObject(
																	0)
																	.optString(
																			"guestTeam"));
								}
								HallFragment.listLottery.get(j)
										.setDtCanChaseIsuses(dt);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("HallActivity", "大厅得到数据出错：" + e.getMessage());
			return "-1001";
		}
		return "0";
	}

	/**
	 * 自动登录
	 **/
	public static String doLogin(JSONObject item) {
		try {
			if ("0".equals(item.optString("error"))) {
				AppTools.user = new Users();
				AppTools.user.setUid(item.optString("uid"));
				if (!item.optString("url").contains(AppTools.url)) {
					AppTools.user.setImage_url(AppTools.url
							+ item.optString("headUrl"));
				} else {
					AppTools.user.setImage_url(item.optString("headUrl"));
				}
				AppTools.user.setName(item.optString("name"));
				AppTools.user.setRealityName(item.optString("realityName"));
				AppTools.user.setBalance(item.optDouble("balance"));
				AppTools.user.setMinWithdraw(item.optDouble("minimumMoney"));
				AppTools.user.setFreeze(item.optDouble("freeze"));
				AppTools.user.setEmail(item.optString("email"));
				AppTools.user.setIdcardnumber(item.optString("idcardnumber"));
				AppTools.user.setMobile(item.optString("mobile"));
				AppTools.user.setBankCard(item.optString("bankcard"));
				AppTools.user.setMsgCount(item.optInt("msgCount"));
				AppTools.user.setMsgCountAll(item.optInt("msgCountAll"));
				AppTools.user.setScoring(item.optInt("scoring"));
				AppTools.user.setHandselAmount(item.optDouble("handselAmount"));
				AppTools.user.setTotalWinMoney(item.optDouble("totalwinmoney"));
				AppTools.user.setIsgreatMan(item.optString("isManito"));
				AppTools.user.setExtractMoney(item.optDouble("disdillMoney"));
				// 用户密码 （没加密的）
				return AppTools.ERROR_SUCCESS + "";
			} else {
				if (DEBUG)
					Log.i(TAG, item.optString("msg"));
				return item.optString("error");
			}
		} catch (Exception ex) {
			if (DEBUG)
				Log.i(TAG, "登录异常---" + ex.getMessage());
			ex.printStackTrace();
			return "-110";
		}
	}

	/**
	 * 获取屏幕信息
	 * 
	 * @param context
	 * @return
	 */
	public static float getDpi(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density; // 密度（0.75 / 1.0 / 1.5）
		return density;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.eims.mcp_app.activity", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("erro", e.getMessage());
		}
		return verName;
	}

	/**
	 * 改变字体的颜色
	 * 
	 * @param color
	 *            字体颜色
	 * @param content
	 *            内容
	 * @return
	 */
	public static String changeStringColor(String color, String content) {
		return "<font color='" + color + "'>" + content + "</FONT>";
	}

	/**
	 * 当前期号是否截止
	 */
	public static boolean isIsuseNotExpired(Lottery mLottery) {
		return mLottery != null
				&& mLottery.getDistanceTime() > System.currentTimeMillis();
	}

	public static Lottery getLotteryById(String lotteryId) {
		Lottery mLottery = null;
		for (Lottery lottery : HallFragment.listLottery) {
			if (lottery.getLotteryID().equals(lotteryId)
					&& (isIsuseNotExpired(lottery))) {
				mLottery = lottery;
				break;
			}
		}
		return mLottery;
	}
}
