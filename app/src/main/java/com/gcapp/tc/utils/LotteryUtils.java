package com.gcapp.tc.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.SelectRechargeTypeActivity;
import com.gcapp.tc.sd.ui.Select_11x5_Activity;
import com.gcapp.tc.sd.ui.Select_DLT_Activity;
import com.gcapp.tc.sd.ui.Select_K3_Activity;
import com.gcapp.tc.sd.ui.Select_RX9_Activity;
import com.gcapp.tc.sd.ui.Select_SFC_Activity;
import com.gcapp.tc.sd.ui.Select_SSC_Activity;
import com.gcapp.tc.sd.ui.Select_SSQ_Activity;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;
/**
 * 功能：彩种的 通用方法类
 */
public class LotteryUtils {
	/**
	 * 设置添加屏幕的背景透明度
	 * **/
	public static void backgroundAlpaha(Activity context, float bgAlpha) {
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		context.getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		context.getWindow().setAttributes(lp);
	}

	public static void addNumTextView(LinearLayout layout, Context context) {

		TextView red_ball = (TextView) LayoutInflater.from(context).inflate(
				R.layout.win_lottery_addtextview, null);
		red_ball.setText("暂无开奖号码");
		layout.addView(red_ball);
	}

	/**
	 * 添加数字求
	 * 
	 * @param layout
	 * @param nums
	 * @param num
	 * @param flag
	 *            0为红球，1为蓝球，2为矩形
	 */
	public static void addNumBall(LinearLayout layout, String[] nums,
			String num, int flag, Context context) {
		if (nums == null || nums.length == 0) {
			if (TextUtils.isEmpty(num))
				return;

			StringBuilder buffer = new StringBuilder();
			for (int i = 0; i < num.length(); i++) {
				buffer.append(num.substring(i, i + 1));
				if (i != (num.length() - 1)) {
					buffer.append("-");
				}
			}
			nums = buffer.toString().split("-");
		}
		switch (flag) {
		case 0: // 添加红球
			TextView red_ball = null;
			for (int i = 0; i < nums.length; i++) {
				red_ball = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_lottery_ball, null);
				red_ball.setText(nums[i]);
				layout.addView(red_ball);
			}

			break;

		case 1:// 添加蓝球
			TextView blue_ball = null;
			for (int i = 0; i < nums.length; i++) {
				blue_ball = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_lottery_ball, null);
				blue_ball
						.setBackgroundResource(R.drawable.win_lottery_blue_ball_shape);
				blue_ball.setText(nums[i]);
				layout.addView(blue_ball);
			}
			break;
		case 2:// 添加矩形
			TextView rectangle = null;
			for (int i = 0; i < nums.length; i++) {
				rectangle = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_lottery_ball, null);
				rectangle
						.setBackgroundResource(R.drawable.win_lottery_rectangle_shape);
				rectangle.setText(nums[i]);
				layout.addView(rectangle);
			}
			break;
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * 格式化时间
	 */
	private static final String TAG = "LotteryUtils";

	public static Bitmap getBitmap(String path) throws IOException {

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}
		return null;
	}

	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 日期格式化
	 * 
	 * @param time
	 *            ：时间
	 * @return
	 */
	public static String Long2DateStr_detail(long time) {
		String format = "yyyy-M-d HH:mm";
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String long_time = sdf.format(date);
		return long_time;
	}

	/**
	 * 正则匹配电话号码
	 * 
	 * @param value
	 *            :手机号码参数
	 * @return
	 */
	public static boolean checkQQREX(String value) {
		String regex = "[1-9][0-9]{4,14}"; // QQ号码
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.find();
	}

	/**
	 * 根据日期推出具体星期
	 * 
	 * @param dt
	 *            ：日期
	 * @return
	 * @throws Exception
	 */
	public static String getWeekOfDate(String dt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(dt);
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	/**
	 * 根据LotteryId得彩种名称
	 * 
	 * @param LotteryId
	 *            ：彩种ID
	 * @return
	 */

	public static String getTitleText(String LotteryId) {
		String lotteryNameString = "";
		if ("6".equals(LotteryId)) {
			lotteryNameString = "3D";
		} else if ("5".equals(LotteryId)) {
			lotteryNameString = "双色球";
		} else if ("63".equals(LotteryId)) {
			lotteryNameString = "排列三";
		} else if ("78".equals(LotteryId)) {
			lotteryNameString = "广东11选5";
		} else if ("64".equals(LotteryId)) {
			lotteryNameString = "排列五";
		} else if ("3".equals(LotteryId)) {
			lotteryNameString = "七星彩";
		} else if ("39".equals(LotteryId)) {
			lotteryNameString = "大乐透";
		} else if ("13".equals(LotteryId)) {
			lotteryNameString = "七乐彩";
		} else if ("74".equals(LotteryId)) {
			lotteryNameString = "胜负彩";
		} else if ("75".equals(LotteryId)) {
			lotteryNameString = "任九场";
		} else if ("62".equals(LotteryId)) {
			lotteryNameString = "十一运夺金";
		} else if ("82".equals(LotteryId)) {
			lotteryNameString = "幸运彩";
		} else if ("28".equals(LotteryId)) {
			lotteryNameString = "重庆时时彩";
		} else if ("66".equals(LotteryId)) {
			lotteryNameString = "新疆时时彩";
		} else if ("70".equals(LotteryId)) {
			lotteryNameString = "江西11选5";
		} else if ("83".equals(LotteryId)) {
			lotteryNameString = "江苏快3";
		} else if ("72".equals(LotteryId)) {
			lotteryNameString = "竞彩足球";
		} else if ("73".equals(LotteryId)) {
			lotteryNameString = "竞彩篮球";
		} else if ("45".equals(LotteryId)) {
			lotteryNameString = "北京单场";
		} else if ("61".equals(LotteryId)) {
			lotteryNameString = "江西时时彩";
		} else if ("7201".equals(LotteryId)) {
			lotteryNameString = "竞足单关";
		} else if ("7301".equals(LotteryId)) {
			lotteryNameString = "竞篮单关";
		} else if ("111".equals(LotteryId)) {
			lotteryNameString = "11选5系列";
		} else if ("222".equals(LotteryId)) {
			lotteryNameString = "时时彩系列";
		}
		return lotteryNameString;
	}

	/**
	 * 充值提示框
	 * 
	 * @param context
	 *            :上下文对象
	 * @param totalMoney
	 *            ：花费的金额
	 */
	public static void showMoneyLess(final Context context,
			final long totalMoney) {
		ConfirmDialog dialog = new ConfirmDialog(context, R.style.dialog);
		dialog.show();
		dialog.setDialogContent(context.getResources().getString(
				R.string.recharge));
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) {// 确定
					toRecharge(context, AppTools.canAlipay);
				}
			}
		});
	}

	/**
	 * 充值跳转方法
	 * 
	 * @param context
	 *            ：上下文
	 * @param flag
	 *            ：有无充值方式参数
	 */
	public static void toRecharge(final Context context, boolean flag) {
		if (flag) {
			Intent intent = new Intent(context,
					SelectRechargeTypeActivity.class);
			context.startActivity(intent);
		} else {
			MyToast.getToast(context, "APP端暂未开通支付！");
		}
	}

	/**
	 * 三个月以内的消息
	 * 
	 * @param list
	 *            ：数据集合
	 * @return
	 */
	public static boolean checkThreeMonth(List<String> list) {
		try {
			if (list != null && list.size() != 0) {
				int topYear = Integer.valueOf(list.get(0).split("-")[0]);
				int topMonth = Integer.valueOf(list.get(0).split("-")[1]);

				int lowYear = Integer.valueOf(list.get(list.size() - 1).split(
						"-")[0]);
				int lowMonth = Integer.valueOf(list.get(list.size() - 1).split(
						"-")[1]);
				if (topYear - lowYear > 1) {
					return false;
				} else if (topYear - lowYear == 1) {
					// 三个月以内
					if ((topMonth + 12) - lowMonth < 3) {
						return true;
					}
				} else {
					if ((topMonth - lowMonth) < 3) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("checkThreeMonth  :" + e.toString());
		}
		return false;
	}

	/**
	 * 比较时间，相差三个月返回true
	 * 
	 * @param date
	 *            :当前日期
	 * @param date1
	 *            ：得到的日期
	 * @return
	 */
	public static boolean compareMonth(String date, String date1) {
		try {
			if (!TextUtils.isEmpty(date) && date.contains("-")
					&& !TextUtils.isEmpty(date1) && date1.contains("-")) {
				int topYear = Integer.valueOf(date.split("-")[0]);
				int topMonth = Integer.valueOf(date.split("-")[1]);

				int lowYear = Integer.valueOf(date1.split("-")[0]);
				int lowMonth = Integer.valueOf(date1.split("-")[1]);

				if (topYear - lowYear > 1) {
					return false;
				} else if (topYear - lowYear == 1) {
					// 三个月以内
					if ((topMonth + 12) - lowMonth < 3) {
						return true;
					}
				} else {
					if ((topMonth - lowMonth) < 3) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("checkThreeMonth  :" + e.toString());
		}
		return false;
	}

	/**
	 * 拼接订单编号
	 * 
	 * @param lotteryId
	 *            ：彩种id
	 * @param isuseName
	 *            ：方案name
	 * @param schemeids
	 *            ：方案ID
	 * @return
	 */
	public static String getSchemeNum(String lotteryId, String isuseName,
			int schemeids) {
		String schemeNumMiddle = "";
		if ("6".equals(lotteryId)) {
			schemeNumMiddle = "3D";
		} else if ("63".equals(lotteryId)) {
			schemeNumMiddle = "PL3";
		} else if ("5".equals(lotteryId)) {
			schemeNumMiddle = "SSQ";
		} else if ("62".equals(lotteryId)) {
			schemeNumMiddle = "";
		} else if ("64".equals(lotteryId)) {
			schemeNumMiddle = "PL5";
		} else if ("3".equals(lotteryId)) {
			schemeNumMiddle = "QXC";
		} else if ("39".equals(lotteryId)) {
			schemeNumMiddle = "TCCJDLT";
		} else if ("13".equals(lotteryId)) {
			schemeNumMiddle = "QLC";
		} else if ("74".equals(lotteryId)) {
			schemeNumMiddle = "SFC";
		} else if ("75".equals(lotteryId)) {
			schemeNumMiddle = "RJC";
		} else if ("82".equals(lotteryId)) {
			schemeNumMiddle = "XYC";
		} else if ("28".equals(lotteryId)) {
			schemeNumMiddle = "CQSSC";
		} else if ("66".equals(lotteryId)) {
			schemeNumMiddle = "XJSSC";
		} else if ("61".equals(lotteryId)) {
			schemeNumMiddle = "JXSSC";
		} else if ("70".equals(lotteryId)) {
			schemeNumMiddle = "JX11X5";
		} else if ("83".equals(lotteryId)) {
			schemeNumMiddle = "JSK3";
		} else if ("72".equals(lotteryId)) {
			schemeNumMiddle = "JCZQ";
		} else if ("73".equals(lotteryId)) {
			schemeNumMiddle = "JCLQ";
		}
		return isuseName + schemeNumMiddle + schemeids;
	}

	/**
	 * 返回当前的"yyyy-MM-dd"
	 * @param now：日期
	 * @return
	 */
	public static String getSchemeTime(long now) {
		String time;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		time = dateFormat.format(new Date(now));
		return time;
	}
	
	/**
	 * 返回当前的"yyyy-MM-dd HH-mm-ss"
	 * @param now：日期
	 * @return
	 */
	public static String getSchemeDetailTime(long now) {
		String time;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		time = dateFormat.format(new Date(now));
		return time;
	}

	/**
	 * 根据玩法ID得到玩法名称
	 * 
	 * @param playID
	 *            ：玩法id
	 * @param LotteryNum
	 *            ：投注号码
	 * @return
	 */

	public static String transPlayName(String playID, String LotteryNum) {
		String str = null;
		String name = LotteryHelp.getNameByID(playID, LotteryNum);
		if (name != null)
			return name;

		if ((Integer.valueOf(playID) / 100 != 62)
				&& (Integer.valueOf(playID) / 100 != 78)
				&& (Integer.valueOf(playID) / 100 != 70)) {
			try {
				switch (Integer.valueOf(playID)) {
				// 江西时时彩的直选。。
				case 61031:
					str = "一星直选";
					break;
				case 61032:
					str = "二星直选";
					break;
				case 61033:
					str = "三星直选";
					break;
				case 61034:
					str = "四星直选";
					break;
				case 61035:
					str = "五星直选";
					break;

				// 大乐透
				case 3901:
					str = "普通投注";
					break;
				case 3902:
					str = "普通追加投注";
					break;
				case 3903:
					str = "胆拖投注";
					break;
				case 3904:
					str = "胆拖追加投注";
					break;
				case 3906:
					str = "后区胆拖";
					break;
				case 3908:
					str = "后区胆拖追加投注";
					break;
				case 3907:
					str = "双区胆拖";
					break;
				case 3909:
					str = "双区胆拖追加投注";
					break;

				// 双色球
				case 501:
					str = "普通投注";
					break;
				case 502:
					str = "胆拖投注";
					break;

				// 3D
				case 601:
					str = "普通投注";
					break;
				case 602:
					str = "组三";
					break;
				case 603:
					str = "组六";
					break;
				case 604:
					str = "1D";
					break;
				case 605:
					str = "猜1D";
					break;
				case 606:
					str = "2D";
					break;
				case 607:
					str = "猜2D两同号";
					break;
				case 608:
					str = "猜2D两不同";
					break;
				case 609:
					str = "通选";
					break;
				case 610:
					str = "和数";
					break;
				case 611:
					str = "包选三";
					break;
				case 612:
					str = "包选六";
					break;
				case 613:
					str = "猜大小";
					break;
				case 614:
					str = "猜三同";
					break;
				case 615:
					str = "拖拉机";
					break;
				case 616:
					str = "猜奇偶";
					break;
				// 胜负彩
				case 7401:
					// 任选九
				case 7501:
					str = "普通投注";
					break;
				// 排列3
				case 6301:
					str = "普通投注";
					break;
				case 6302:
					if (LotteryNum.substring(0, 1).equals(
							LotteryNum.substring(1, 2))) {
						str = "组三单式";
					} else
						str = "组六单式";
					break;
				case 6303:
					str = "组选六复式";
					break;
				case 6304:
					str = "组选三复式";
					break;
				case 6305:
					str = "直选和值";
					break;
				case 6306:
					str = "组选和值";
					break;
				// 排列5
				case 6401:
					str = "普通投注";
					break;
				// 七乐彩
				case 1301:
					str = "普通投注";
					break;
				// 七星彩
				case 301:
					str = "普通投注";
					break;

				// 重庆时时彩
				case 2803:
				case 6603:
					int index = LotteryNum.trim().replace(" ", "")
							.lastIndexOf("-");
					if (index == 3) {
						str = "一星直选";
					} else if (index == 2) {
						str = "二星直选";
					} else if (index == 1) {
						str = "三星直选";
					} else if (index == 0) {
						str = "四星直选";
					} else if (index == -1) {
						str = "五星直选";
					}
					break;
				case 2804:
				case 6604:
					str = "大小单双";
					break;
				case 2805:
				case 6605:
					str = "五星通选";
					break;
				case 2806:
				case 6606:
					str = "二星组选";
					break;
				case 2811:
				case 6611:
					str = "三星组三";
					break;
				case 2812:
				case 6612:
					str = "三星组六";
					break;
				case 2814:
				case 6614:
					str = "组三包胆";
					break;
				case 2815:
				case 6615:
					str = "组三和值";
					break;
				case 6619:
					str = "三星任选";
					break;
				case 6620:
					str = "二星任选";
					break;

				// 江苏快三
				case 8301:
					str = "和值";
					break;
				case 8302:
					str = "三同号通选";
					break;
				case 8303:
					str = "三同号单选";
					break;
				case 8304:
					str = "二同号复选";
					break;
				case 8305:
					str = "二同号单选";
					break;
				case 8306:
					str = "三不同号";
					break;
				case 8307:
					str = "二不同号";
					break;
				case 8308:
					str = "三连号通选";
					break;

				default:
					break;
				}
			} catch (NumberFormatException e) {
				return "普通投注";
			}

		} else {
			// 江西11选5/广东11选5/山东11选5
			if ((Integer.valueOf(playID) / 100 == 62)
					|| (Integer.valueOf(playID) / 100 == 78)
					|| (Integer.valueOf(playID) / 100 == 70)) {

				switch (Integer.valueOf(playID) % 100) {
				case 1:
					str = "前一";
					break;
				case 2:
					str = "任选二";
					break;
				case 3:
					str = "任选三";
					break;
				case 4:
					str = "任选四";
					break;
				case 5:
					str = "任选五";
					break;
				case 6:
					str = "任选六";
					break;
				case 7:
					str = "任选七";
					break;
				case 8:
					str = "任选八";
					break;
				case 9:
					str = "前二直选";
					break;
				case 10:
					str = "前三直选";
					break;
				case 11:
					str = "前二组选";
					break;
				case 12:
					str = "前三组选";
					break;
				case 13:
					str = "前二胆拖";
					break;
				case 14:
					str = "前三胆拖";
					break;
				case 15:
					str = "任选二胆拖";
					break;
				case 16:
					str = "任选三胆拖";
					break;
				case 17:
					str = "任选四胆拖";
					break;
				case 18:
					str = "任选五胆拖";
					break;
				case 19:
					str = "任选六胆拖";
					break;
				case 20:
					str = "任选七胆拖";
					break;
				case 21:
					str = "任选八胆拖";
					break;
				case 22:
					str = "乐选二";
					break;
				case 23:
					str = "乐选三";
					break;
				case 24:
					str = "乐选四";
					break;
				case 25:
					str = "乐选五";
					break;
				}
			}
		}
		return str;
	}

	/**
	 * 正则匹配电话号码 三大运营商段号：移动（133、130、131、132、134、135、136、137、138、139、（1349卫通）133
	 * 号段主要用作无线网卡号。） 147、147（数据卡号段）、联通（145（数据卡号段）
	 * 150、151、152、157（TD）、158、159、155、156、153、 178、1705,1700（虚拟运营商电信号段）。，
	 * 177、176、1709（虚拟运营商联通号段））
	 * 180、181、189、、182、183、184、187、188.185、186、其中189是3G（CDMA2000）
	 */
	public static boolean checkPhoneREX(String value) {
		// String regExp = "^((1[0-9]))\\d{9}$";
		String regExp = "^((13[0-9])|(14[5-9])|(15[^4,\\D])|(16[5,6])|(17[^9,\\D])|(18[0-9])|(19[1,5,8,9]))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.find();
	}

	/**
	 * 转换方案的状态显示
	 * 
	 * @param Share
	 *            ：总份数
	 * @param BuyedShare
	 *            ：已经买的份数
	 * @param buyed
	 *            ：是否已经出票
	 * @param quashStatus
	 *            ：方案是否已经程度的状态值
	 * @param isopened
	 *            ：是否公开
	 * @param winmoney
	 *            ：中奖金额
	 * @return
	 */
	public static String convertSchemeState(int Share, int BuyedShare,
			boolean buyed, int quashStatus, boolean isopened, double winmoney) {
		String SchemeState = "";
		if (Share > BuyedShare && !buyed) {
			SchemeState = "招募中";
		} else if (Share <= BuyedShare && !buyed && !isopened) {
			SchemeState = "未出票";
		} else if (Share <= BuyedShare && buyed && !isopened) {
			SchemeState = "已出票";
		} else if (Share <= BuyedShare && !buyed && isopened) {
			SchemeState = "已流单";
		} else if (Share <= BuyedShare && buyed && isopened && winmoney == 0) {
			SchemeState = "未中奖";
		} else if (Share <= BuyedShare && buyed && isopened && winmoney > 0) {
			SchemeState = "已中奖";
		}
		if (quashStatus > 0) {
			SchemeState = "已撤单";
		}
		return SchemeState;
	}

	private static final String BOUNDARY = "--WebKitFormBoundaryT1HoybnYeFOGFlBR";

	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param uploadFile
	 *            需要上传的文件名
	 * @param fileFormName
	 *            需要上传文件表单中的名字
	 * @param newFileName
	 *            上传的文件名称，不填写将为uploadFile的名称
	 * @param urlStr
	 *            上传的服务器的路径
	 * @throws IOException
	 */
	public static void uploadForm(Map<String, String> params,
			String fileFormName, File uploadFile, String newFileName,
			String urlStr) throws IOException {
		if (newFileName == null || newFileName.trim().equals("")) {
			newFileName = uploadFile.getName();
		}

		StringBuilder sb = new StringBuilder();
		/**
		 * 普通的表单数据
		 */
		for (String key : params.keySet()) {
			sb.append("--" + BOUNDARY + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + key + "\""
					+ "\r\n");
			sb.append("\r\n");
			sb.append(params.get(key) + "\r\n");
		}
		/**
		 * 上传文件的头
		 */
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName
				+ "\"; filename=\"" + newFileName + "\"" + "\r\n");
		sb.append("Content-Type: text/plain" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("\r\n");

		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty(
				"Content-Length",
				String.valueOf(headerInfo.length + uploadFile.length()
						+ endInfo.length));
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();
		InputStream in = new FileInputStream(uploadFile);
		out.write(headerInfo);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
 
    	out.write(endInfo);
		in.close();
		out.close();
		if (conn.getResponseCode() == 200) {
			InputStream in1 = new BufferedInputStream(conn.getInputStream());
			String result = readInStream(in1);
			System.out.println(result);
			uploadFile.delete();
		}
	}

	/**
	 * 读取数据
	 * 
	 * @param in
	 *            ：输入流
	 * @return
	 */
	private static String readInStream(InputStream in) {
		Scanner scanner = new Scanner(in).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}

	/**
	 * 根据彩种id跳转不同选号页面
	 * 
	 * @param lotteryID
	 *            ： 彩种id
	 * 
	 */
	public static void goToSelectLottery(Context context, String lotteryID) {
		int id = Integer.parseInt(lotteryID);
		Intent intent = null;
		Lottery mLottery = AppTools.getLotteryById(lotteryID);
		if (mLottery != null) {
			AppTools.lottery = mLottery;
		} else {
			MyToast.getToast(context, "该奖期已结束，请等下一期");
			return;
		}
		switch (id) {
		case 5:// 双色球
			intent = new Intent(context, Select_SSQ_Activity.class);
			break;
		case 39:// 大乐透
			intent = new Intent(context, Select_DLT_Activity.class);
			break;
		case 75:// 任选九
			intent = new Intent(context, Select_RX9_Activity.class);
			break;
		case 74:// 胜负彩
			intent = new Intent(context, Select_SFC_Activity.class);
			break;
		case 62:// 十一运夺金
		case 70:
		case 78:// 11选5
			intent = new Intent(context, Select_11x5_Activity.class);
			break;
		case 28:// 时时彩
			intent = new Intent(context, Select_SSC_Activity.class);
			break;
		case 83:// 江苏快3
			intent = new Intent(context, Select_K3_Activity.class);
			break;
		}
		context.startActivity(intent);
	}

	/**
	 * 根据彩种id跳转不同选号页面
	 * 
	 * @param lotteryID
	 *            彩种id
	 */
	public static void goToSelectLottery_jc(Context context, String lotteryID) {
		Lottery mLottery = AppTools.getLotteryById(lotteryID);
		if (mLottery == null) {
			MyToast.getToast(context, "该奖期已结束，请等下一期");
			return;
		}
		AppTools.lottery = mLottery;
		HallFragment.refreType = true;
		if (AppTools.lottery.getDtmatch() != null
				&& AppTools.lottery.getDtmatch().length() != 0) {
			if (lotteryID.equals("72")) {// 竞彩足球
				new HallFragment().getBallData(context, 0);
			} else if (lotteryID.equals("73")) {// 竞彩篮球
				new HallFragment().getBasketball(context, 0);
			} 
		} else {
			MyToast.getToast(context, "没有对阵信息");
		}
	}

	/**
	 * 格式转换
	 * 
	 * @param input
	 *            ：字符串
	 * @param key
	 *            ：秘钥
	 * @return
	 */
	public static String decrypt3DES(String input, String key) {
		return decrypt3DES(input, key, Charset.forName("GB2312"));
	}

	/**
	 * 格式转换
	 * 
	 * @param input
	 *            ：字符串
	 * @param key
	 *            ：：秘钥
	 * @param charset
	 *            ：charset对象
	 * @return
	 */
	public static String decrypt3DES(String input, String key, Charset charset) {
		try {
			return new String(Des.decrypt(Byte.hex2byte(input.getBytes()),
					key.getBytes()), charset.name());
		} catch (Exception localException) {
		}
		return "";
	}

	/**
	 * 格式转换
	 * 
	 * @param input
	 *            ：字符串
	 * @param key
	 *            ：秘钥
	 * @return
	 */
	public static String encrypt3DES(String input, String key) {
		return encrypt3DES(input, key, Charset.forName("GB2312"));
	}

	/**
	 * 格式转换
	 * 
	 * @param input
	 *            ：字符串
	 * @param key
	 *            ：：秘钥
	 * @param charset
	 *            ：charset对象
	 * @return
	 */
	public static String encrypt3DES(String input, String key, Charset charset) {
		try {
			return Byte.byte2hex(Des.encrypt(input.getBytes(charset.name()),
					key.getBytes()));
		} catch (Exception localException) {
		}
		return "";
	}

	/**
	 * 处理字符串
	 * 
	 * @param str
	 *            ：字符串参数
	 * @return
	 */
	public static String getRexQue(String str) {
		Pattern p = Pattern.compile("\\(.*?\\)");// 查找规则公式中大括号以内的字符
		Matcher m = p.matcher(str);
		String param = null;
		while (m.find()) {// 遍历找到的所有大括号
			param = m.group().replaceAll("\\(\\)", "");// 去掉括号
		}
		return param;
	}

	/**
	 * 获得“-”分割的数组
	 * 
	 * @param str
	 *            ：string对象
	 * @return
	 */
	public static String[] getRexArray(String str) {
		if (!str.contains("-"))
			return new String[] { str, "" };
		else {
			String str_pre = str.substring(0, str.lastIndexOf("-"));
			String str_suffix = str.substring(str.lastIndexOf("-") + 1,
					str.length());
			return new String[] { str_pre, str_suffix };
		}
	}

	/**
	 * 得到屏幕信息
	 * 
	 * @param context
	 *            ：上下文对象
	 * @return
	 */
	public static int[] getScreenWidthAndHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeigh = dm.heightPixels;
		return new int[] { screenWidth, screenHeigh };
	}

	/**
	 * 将中奖轮播中的人名信息加密***
	 * 
	 * @param target
	 *            :中奖信息info
	 * @return
	 */
	public static String addRexStarMarquee(String target) {
		Pattern pattern = Pattern
				.compile("[A-Za-z0-9\\u4e00-\\u9fa5]{3,16}\\s[\\u4e00-\\u9fa5]{2}");
		Matcher matcher = pattern.matcher(target);
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
			String find = matcher.group();
			find = addRexStar(find.substring(0, find.indexOf(" ")));
			if (find != null)
				matcher.appendReplacement(sbr, find + " 喜中");
		}
		matcher.appendTail(sbr);
		System.out.println(sbr.toString());
		return sbr.toString();
	}

	/**
	 * 对人名（自己除外）已***加密
	 * 
	 * @param name
	 *            ：用户名
	 * @return
	 */
	public static String addRexStar(String name) {
		if (TextUtils.isEmpty(name))
			return null;
		if (AppTools.user != null && name.equals(AppTools.user.getName())) {
			return name;
		} else {
			// //版本修改，此处不做加密处理，直接取后台的返回数据.隐藏该条数据
			return name;
		}
	}

	/**
	 * 判断字符长度
	 * 
	 * @param target
	 *            :字符串参数
	 * @return
	 */
	public static int getRexStrLength(String target) {
		int charLength = 0;
		String chinese = "^[\\u4e00-\\u9fa5]+$";
		String num_enchar = "^\\w+$";
		Pattern pattern_chinese = Pattern.compile(chinese);
		Pattern pattern_num_enchar = Pattern.compile(num_enchar);
		for (String str : target.split("")) {
			if (!"".equals(str)) {
				if (pattern_chinese.matcher(str).matches()) {
					charLength += 2;
				} else if (pattern_num_enchar.matcher(str).matches()) {
					charLength += 1;
				}
			}
		}
		return charLength;
	}
}
