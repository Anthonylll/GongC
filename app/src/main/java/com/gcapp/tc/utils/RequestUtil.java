package com.gcapp.tc.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.dataaccess.Show_JC_Details;
import com.gcapp.tc.protocol.MD5;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据请求类 功能：封装所有数据请求，一个方法提交一类请求
 */
public abstract class RequestUtil {
	private Dialog dialog;
	private Context context;
	private Request jsonObjectRequest;
	public static final boolean DEBUG = true;
	private static final String TAG = "RequestUtil";
	private boolean isShowDialog = false;// 是否弹窗
	private boolean isShouldCache = false;// 是否设置缓存
	private long cacheTime;// 缓存时间

	/**
	 * @param context
	 *            上下文
	 * @param isShouldCache
	 *            是否设置缓存
	 * @param cacheTime
	 *            缓存时间
	 */
	public RequestUtil(Context context, boolean isShouldCache, long cacheTime) {
		this.context = context;
		this.isShouldCache = isShouldCache;
		this.cacheTime = cacheTime;
	}

	/**
	 * 带弹窗的请求类构造器
	 * 
	 * @param context
	 *            上下文
	 * @param isShouldCache
	 *            是否设置缓存
	 * @param cacheTime
	 *            缓存时间
	 * @param isCancelDialog
	 *            是否可取消弹窗 true为可取消，false则不可取消
	 * @param dialogTitle
	 *            弹窗提示
	 */
	public RequestUtil(Context context, boolean isShouldCache, long cacheTime,
			boolean isCancelDialog, String dialogTitle) {
		this.context = context;
		dialog = createLoadingDialog(context, dialogTitle, isCancelDialog);
		isShowDialog = true;
		this.isShouldCache = isShouldCache;
		this.cacheTime = cacheTime;
	}

	/**
	 * 带弹窗的请求类构造器
	 * 
	 * @param context
	 *            上下文
	 * @param isShouldCache
	 *            是否设置缓存
	 * @param cacheTime
	 *            缓存时间
	 * @param isCancelDialog
	 *            是否可取消弹窗 true为可取消，false则不可取消
	 * @param dialogTitle
	 *            弹窗提示
	 * @param Mark
	 *            是否弹窗
	 * 
	 */
	public RequestUtil(Context context, boolean isShouldCache, long cacheTime,
			boolean isCancelDialog, String dialogTitle, int Mark) {
		this.context = context;
		dialog = createLoadingDialog(context, dialogTitle, isCancelDialog);
		if(Mark == 1) {
			isShowDialog = true;
		}
		this.isShouldCache = isShouldCache;
		this.cacheTime = cacheTime;
	}

	/******************************** MainActivity ************************************************/

	/**
	 * 检查app更新
	 */
	public void checkUpdateApp() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = "{\"identify\":\"android\",\"appversion\":\""
				+ AppTools.getVerName(context) + "\"}";
		if (DEBUG)
			Log.i(TAG, "检查app更新====   " + info);
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "0";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 下载图片
	 */
	public void DownloadImagview() {

		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = "";
		if (DEBUG)
			Log.i(TAG, "检查app更新====   " + info);
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "73";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取最近一条中奖信息
	 * 
	 * @param uid
	 *            ：用户ID
	 */
	public void getCurrentWinInfo(String uid) {
		String key = MD5.md5(AppTools.MD5_key);
		String info = "{\"uid\":\"" + uid + "\"}";
		if (DEBUG)
			Log.i(TAG, "获取最近一条中奖信息====   " + info);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, uid);
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);
		String opt = "61";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取中奖详情
	 */
	public void getWinDetail() {
		String key = MD5.md5(AppTools.MD5_key);
		String info = RspBodyBaseBean.changeMyLotteryInfo_Info(
				AppTools.lotteryIds + ",72,73", 1, 2, 5, 3, 3);
		if (DEBUG)
			Log.i(TAG, "获取中奖详情====   " + info);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "18";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取合买比列和认购比例
	 */
	public void getBuyParams() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = "";
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "53";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取站点名称和客服电话
	 */
	public void getSiteNameAndPhone() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = "";
		if (DEBUG)
			Log.i(TAG, "获取站点名称和客服电话====   " + info);
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "63";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 
	 * 获取提款前用户信息
	 */
	public void getInitParams41() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = "";
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "41";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);

	}

	/**
	 * 获取充值赠送彩金的状态类型类型
	 * 
	 */
	public void getRechargeParams71() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = "";
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "71";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取购彩大厅所有数据（竞彩数据除外） opt="10"
	 * 
	 * @param lottryids
	 *            彩种id
	 */
	public void getLotteryData(String lottryids) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = RspBodyBaseBean.changeLottery_info(lottryids);
		if (DEBUG)
			Log.i(TAG, "获取购彩大厅所有数据====   " + info);
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "10";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 自动登录 opt = "2"
	 */
	public void doAutoLogin() {
		SharedPreferences settings = context
				.getSharedPreferences("app_user", 0);
		boolean isLogin = false;
		String name = "";
		String pass = "";
		if (settings.contains("isLogin")) {
			isLogin = settings.getBoolean("isLogin", false);
		}
		if (settings.contains("name")) {
			name = settings.getString("name", null);
		}
		// 判断是否有存 用户名
		if (isLogin) {
			// 判断是否有存 密码
			if (settings.contains("pass")) {
				pass = settings.getString("pass", null);
			}
		}
		if (null != pass && null != name && !"".equals(name)
				&& !"".equals(pass) && name.length() != 0 && pass.length() != 0) {
			// 对密码进行MD5加密
			String pass2 = MD5.md5(pass + AppTools.MD5_key);
			String time = RspBodyBaseBean.getTime();
			String imei = RspBodyBaseBean.getIMEI(context);
			String opt = "2";
			String info = RspBodyBaseBean.changeLogin_Info(name, pass2);
			String auth = RspBodyBaseBean.changeLogin_Auth(
					RspBodyBaseBean.getCrc(time, imei,
							MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);
			String[] values = { opt, auth, info };
			if (DEBUG)
				Log.i(TAG, "自动登录====   " + info);
			commonSet(AppTools.names, values);

		} else if (!isLogin && null != AppTools.username
				&& null != AppTools.userpass && !"".equals(AppTools.username)
				&& !"".equals(AppTools.userpass)
				&& AppTools.username.length() != 0
				&& AppTools.userpass.length() != 0) {
			// 对密码进行MD5加密
			String pass2 = MD5.md5(AppTools.userpass + AppTools.MD5_key);
			String time = RspBodyBaseBean.getTime();
			String imei = RspBodyBaseBean.getIMEI(context);
			String opt = "2";
			String info = RspBodyBaseBean.changeLogin_Info(AppTools.username,
					pass2);
			String auth = RspBodyBaseBean.changeLogin_Auth(
					RspBodyBaseBean.getCrc(time, imei,
							MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);
			String[] values = { opt, auth, info };
			if (DEBUG)
				Log.i(TAG, "个人中心刷新登录info====   " + info);
			commonSet(AppTools.names, values);
		}
	}

	/**
	 * 竞彩的开奖详情信息
	 * 
	 * @param lotteryId
	 *            ：彩种ID
	 * @param date
	 *            ：日期
	 */
	public void getWinInfo_jc(String lotteryId, String date) {
		String key = MD5.md5(AppTools.MD5_key);
		String info = RspBodyBaseBean.changeJcLottery_info(lotteryId, date);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "47";
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, " opt =47; 竞彩的开奖公告信息info----" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取竞彩对阵信息 opt = "10"
	 * 
	 * @param jcLottryid
	 *            竞彩彩种id 72:足球 73：篮球
	 */
	public void getJCData(String jcLottryid) {
		String key = MD5.md5(AppTools.MD5_key);
		String info = RspBodyBaseBean.changeLottery_info(jcLottryid, "-1");
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "10";
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, " 获取竞彩对阵信息----" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 请求广播消息
	 */
	public void getWinMessage() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String info = "{\"lotteryIds\":\"" + AppTools.lotteryIds + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "51";
		String[] values = { opt, auth, info }; // 请求广播消息，opt=“51” info ="5,6,7"
		if (DEBUG)
			Log.i(TAG, "请求广播消息的info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 版本更新
	 * 
	 * @param name
	 *            ：版本号
	 */
	public void getVersion(String name) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String info = "{\"identify\":\"android\",\"appversion\":\"" + name
				+ "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "0";
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "版本更新的info  ==》 " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 普通彩种的开奖信息
	 * 
	 * @param type
	 *            ：-1
	 * @param lotteryID
	 *            ：彩种ID
	 * @param pageIndex
	 *            ：页码
	 * @param pageSize
	 *            ：每页数目
	 * @param sort
	 *            ：1
	 * @param sortType
	 *            :0
	 * @param searchTotal
	 *            :10
	 * @param startTime
	 *            ：为空
	 * @param endTime
	 *            ：为空
	 */
	public void getWinLotteryInfo(int type, String lotteryID, int pageIndex,
			int pageSize, int sort, int sortType, int searchTotal,
			String startTime, String endTime) {
		String mImei = RspBodyBaseBean.getIMEI(context);
		String mTime = RspBodyBaseBean.getTime();
		String uid = "-1";
		String opt = "25";
		if (null != AppTools.user) {
			uid = AppTools.user.getUid();
		}
		String info = RspBodyBaseBean.changeWinLottery_info(type, lotteryID,
				pageIndex, pageSize, sort, sortType, searchTotal, startTime,
				endTime);
		String mCrc = RspBodyBaseBean.getCrc(mTime, mImei, AppTools.key, info,
				uid);
		String auth = RspBodyBaseBean.getAuth(mCrc, mTime, mImei, uid);

		String[] values = { opt, auth, info }; // 请求广播消息，opt=“51” info ="5,6,7"
		if (DEBUG)
			Log.i(TAG, "opt = 25;开奖信息的info  ==》 " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 修改密码
	 * 
	 * @param id
	 *            ：用户ID
	 * @param oldPass
	 *            ：旧密码
	 * @param newPass
	 *            ：新密码
	 */
	public void commit_updatePass(String id, String oldPass, String newPass) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "60";
		String info = "{\"uid\":\"" + id + "\",\"password\":\"" + oldPass
				+ "\",\"theNewPassword\":\"" + newPass + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=60;修改密码的info  ==》 " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 去签到
	 */
	public void goSign_today() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "78";
		String info = "";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=78,去签到info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 查看未读消息数目
	 */
	public void getNewsCount() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String key = MD5.md5(AppTools.MD5_key);
		String opt = "16";
		String info = RspBodyBaseBean.changeMsg_info(-1, 1, 10, 0);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=16,查看信息info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 提交反馈建议
	 * 
	 * @param tel
	 *            :手机号
	 * @param email
	 *            ：邮箱
	 * @param content
	 *            ：正文
	 * @param title
	 *            ：标题
	 */
	public void getSuggestInfo(String tel, String email, String content,
			String title) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "48";
		String info = RspBodyBaseBean.changeSuggest_info(tel, email, content,
				title);
		String key = MD5.md5(AppTools.MD5_key);
		String uid = "-1";
		if (null != AppTools.user) {
			uid = AppTools.user.getUid();
		}
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, uid);
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=48,提交建议info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 绑定账户信息
	 * 
	 * @param mobile
	 *            ：手机号
	 * @param uname
	 *            ：用户名
	 * @param qqnumber
	 *            ：QQ
	 * @param idCardnumber
	 *            ：身份证号
	 * @param bankType
	 *            ：银行卡类型
	 * @param branchBankName
	 *            ：开户支行
	 * @param bankCardNumber
	 *            ：银行卡号
	 * @param bankInProvinceId
	 *            ：银行卡开户省份
	 * @param bankInCityId
	 *            ：银行卡开户城市ID
	 * @param realityName
	 *            ：真实姓名
	 * @param securityquestion
	 *            ：安全问题
	 * @param securityQuestionAnswer
	 *            ：安全问题答案
	 */
	public void getBindInfo(String mobile, String uname,
			String idCardnumber, String bankType, String branchBankName,
			String bankCardNumber, String bankInProvinceId,
			String bankInCityId, String realityName,String zfbNumber) {

		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "36";
		String info = RspBodyBaseBean.changeBankinfo_info(mobile, uname,
				idCardnumber, bankType, branchBankName,
				bankCardNumber, bankInProvinceId, bankInCityId, realityName,zfbNumber);

		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=36,绑定账户信息info=======" + info);
		commonSetThree(AppTools.names, values);
	}

	/**
	 * 请求消息
	 * 
	 * @param id
	 *            ：消息ID
	 * @param type
	 *            ：0
	 */
	public void commit_msg(int id, int type) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "28";
		String info = RspBodyBaseBean.changeMsg_info(id, type);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "开奖号码请求info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 得到我的签到情况
	 */
	public void getSignInfo() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "79";
		String info = "";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=79,查询签到列表info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 合买大厅的竞彩详情
	 * 
	 * @param schemeId
	 *            :方案ID
	 */
	public void getFollowInfo_jc(String schemeId) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String key = MD5.md5(AppTools.MD5_key);
		String opt = "46";
		String uid = "-1";
		if (null != AppTools.user && !"".equals(AppTools.user)) {
			uid = AppTools.user.getUid();
		}
		String info = RspBodyBaseBean.changeJC_info(schemeId);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, uid);
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=46,合买大厅的竞彩详情info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 登录
	 * 
	 * @param name
	 *            ：用户名
	 * @param pass
	 *            ：加密后的密码
	 */
	public void commit_login(String name, String pass) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "2";
		String info = RspBodyBaseBean.changeLogin_Info(name, pass);
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "登录请求info==》   " + info);
		commonSet(AppTools.names, values);
	}
	
	/**
	 * 接口测试方法
	 */
	public void test_interface() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "85";
		String info = "";
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}
	
	/**
	 * 修改用户昵称
	 */
	public void modification_nmae(String userName) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "83";
		String uid = "-1";
		if (null != AppTools.user && !"".equals(AppTools.user)) {
			uid = AppTools.user.getUid();
		}
		String info = "";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,uid);
		String auth = RspBodyBaseBean.nicknmae(crc, time, imei,uid,userName);
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);;
	}

	/**
	 * 竞彩合买投注详情
	 * 
	 * @param schemeId
	 *            ：方案ID
	 */
	public void getFollowbetInfo_jc(String schemeId) {

		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "46";
		String uid = "-1";
		if (null != AppTools.user && !"".equals(AppTools.user)) {
			uid = AppTools.user.getUid();
		}
		String info = RspBodyBaseBean.changeJC_info(schemeId);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, uid);
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=46,竞彩合买投注详情info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 合买投注详情
	 * 
	 * @param userID
	 *            ：用户ID
	 * @param id
	 *            ：方案ID
	 */
	public void getFollowbetInfo(String userID, int id) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "49";
		String uid = "-1";
		if (null != AppTools.user && !"".equals(AppTools.user)) {
			uid = AppTools.user.getUid();
		}
		String info = "{\"uid\":\"" + userID + "\",\"id\":\"" + id + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, uid);
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, uid);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=49,合买投注详情info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取遗漏值
	 * 
	 * @param playType
	 *            ：玩法ID
	 */
	public void commit_yilou(int playType) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "74";
		String info = RspBodyBaseBean.getForgotNumber(playType);
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "遗漏值请求info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 验证手机号码
	 * 
	 * @param phoneNumber
	 *            ：手机号
	 */
	public void commit_checkPhoneNumber(String phoneNumber) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "59";
		String info = "{\"mobile\":\"" + phoneNumber + "\"}";
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "验证手机号码info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 忘记密码的手机验证
	 * 
	 * @param mobile
	 *            ：手机号
	 */
	public void forgotPass_checkphone(String mobile) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String key = MD5.md5(AppTools.MD5_key);
		String opt = "59";
		String info = "{\"mobile\":\"" + mobile + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "忘记密码的手机验证请求info==>" + info);
		commonSet(AppTools.names, values);
	}

	
	/**
	 * 获取近5期的开奖号码
	 * @param lotteryID
	 *  ：彩种id
	 */
	public void commit_winNumber(String lotteryID) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "75";
		String info = lotteryID;
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=75;开奖号码请求info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 提款请求
	 * 
	 * @param money
	 *            ：提款金额
	 * @param securityQuestionId
	 *            ：安全问题ID
	 * @param securityQuestionAnswer
	 *            ：安全问题答案
	 * @param moneyType
	 *            ：提款类型
	 */
	public void withDrawMoney(String money, String moneyType) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "37";
		String key = MD5.md5(AppTools.MD5_key);
		String info = RspBodyBaseBean.changeWithdrawal_info(money, moneyType);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=37;提款请求info   " + info);
		commonSetThree(AppTools.names, values);
	}

	/**
	 * 重置密码
	 * 
	 * @param mobile
	 *            ：手机号
	 * @param pass
	 *            ：密码
	 */
	public void resetPass(String mobile, String pass) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String key = MD5.md5(AppTools.MD5_key);
		String opt = "57";
		String info = "{\"mobile\":\"" + mobile + "\",\"password\":\"" + pass
				+ "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=57;重置密码请求info ==》  " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 验证手机号码是否绑定
	 * 
	 * @param phone
	 *            ：手机号
	 */
	public void check_phone(String phone) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String key = MD5.md5(AppTools.MD5_key);
		String opt = "59";
		String info = "{\"mobile\":\"" + phone + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=59;验证手机的请求info ===》  " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 手机号注册
	 * 
	 * @param type
	 *            ：区分注册类型（手机注册还是用户名注册）
	 * @param name
	 *            ：用户名
	 * @param password
	 *            ：密码
	 * @param mobile
	 *            ：手机
	 * @param qq
	 *            ：QQ
	 */
	public void register_phone(String type, String name, String password,
			String mobile, String qq) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "1";
		String key = MD5.md5(AppTools.MD5_key);
		String info = RspBodyBaseBean.changeRegister_info(type, name, password,
				mobile, qq);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=1;手机注册请求info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 委托投注规则
	 */
	public void commit_rule() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "52";
		String info = "";
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=52;委托投注规则请求info ===》  " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 投注成功页面的查看投注详情
	 * 
	 * @param schId
	 *            ：方案ID
	 */
	public void commit_betDetailInfo(int schId) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "15";
		String info = "{\"id\":" + schId + "}";
		String key = MD5.md5(AppTools.MD5_key);

		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "投注详情请求info ===》  " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 认购列表
	 * 
	 * @param schemeID
	 *            ：方案ID
	 */
	public void getSubscribeInfo(String schemeID) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "80";
		String info = RspBodyBaseBean.getfollowTable_info(schemeID);
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "认购列表请求info   " + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取近5期的开奖号码
	 */
	public void getImageInfo() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "73";
		String info = "";
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=73,广告轮播图请求info   " + info);
		commonSetFour(AppTools.names, values);
	}

	/**
	 * 得到我的购彩记录
	 * 
	 * @param lotteryID
	 *            :彩种ID
	 * @param pageIndex
	 *            ：页码
	 * @param pageSize
	 *            ：每页显示数目
	 * @param sort
	 *            ：排序方式
	 * @param isPurchasing
	 *            ：返回类型
	 * @param status
	 *            ：查询条件码
	 */
	public void getBetInfo(String lotteryID, int pageIndex, int pageSize,
			int sort, int isPurchasing, int status) {

		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "18";
		String info = RspBodyBaseBean.changeMyLotteryInfo_Info(lotteryID,
				pageIndex, pageSize, sort, isPurchasing, status);
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=18,购彩记录请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 大神合买单记录查询
	 * 
	 * @param lotteryID
	 *            :彩种ID
	 * @param pageIndex
	 *            ：页码
	 * @param pageSize
	 *            ：每页显示数目
	 * @param sort
	 *            ：排序方式（0:参与进度，1：金钱，2：每分金额，3：状态，4：战绩，5：时间）
	 * @param isPurchasing
	 *            ：返回类型 (0:不是合买，1：合买)
	 * @param status
	 *            ：查询条件码(1：已中奖，2：未中奖，3：追号，4：合买)
	 * @param userID
	 *            ：大神ID
	 */
	public void getBetInfo(String lotteryID, int pageIndex, int pageSize,
			int sort, int isPurchasing, int status,String userID) {

		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "82";
		String info = RspBodyBaseBean.changeMyLotteryInfo_Info(lotteryID,
				pageIndex, pageSize, sort, isPurchasing, status,userID);
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				"-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				"-1");
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 得到我的购彩记录
	 */
	public void getAlipayType() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "76";
		String info = "";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=76,充值方式请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 判断银行卡是否绑定成功
	 */
	public void getBindOrnotInfo() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "41";
		String info = "{}";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=41,点击提款请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 竞彩的投注详情
	 * 
	 * @param scheId
	 *            ：方案ID
	 */
	public void getBetInfo_jc(String scheId) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "46";
		String info = RspBodyBaseBean.changeJC_info(scheId);
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=46,竞彩投注详情请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 请求系统和推送消息
	 * 
	 * @param typeId
	 *            ：查询的类型条件
	 * @param pageIndex
	 *            ：页码
	 * @param pageSize
	 *            ：每页显示数目
	 * @param isRead
	 *            ：是否已读
	 */
	public void getMessageInfo(int typeId, int pageIndex, int pageSize,
			int isRead) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);

		String key = MD5.md5(AppTools.MD5_key);
		String info;
		String opt = "16";

		if (typeId == 2) {
			info = RspBodyBaseBean.changeMsg_info(typeId, pageIndex, pageSize,
					-1);
		} else {
			// 查询推送消息
			opt = "62";
			info = "{\"UserId\":\"" + AppTools.user.getUid() + "\"}";
		}
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "消息请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 得到我的追号购彩记录
	 * 
	 * @param lotteryID
	 *            ：彩种ID
	 * @param pageIndex
	 *            ：页码
	 * @param pageSize
	 *            ：每页显示数目
	 */
	public void getBetInfo_Chase(String lotteryID, int pageIndex, int pageSize) {

		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "50";
		String info = "{\"lotteryId\":\"" + AppTools.lotteryIds
				+ "\", \"pageIndex\":\"" + pageIndex + "\",\"pageSize\":\""
				+ pageSize + "\"," + "\"sort\":\"" + 0
				+ "\", \"sortType\":\"0\"}";

		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=18,追号记录请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 获取账户明细
	 * 
	 * @param searchCondition
	 *            ：查询条件参数
	 * @param pageIndex
	 *            ：页码
	 * @param pageSize
	 *            ：每页显示数目
	 * @param startTime
	 *            ：时间
	 */
	public void getAccoutInfo(int searchCondition, int pageIndex, int pageSize,
			String startTime) {
		Calendar now = Calendar.getInstance();
		int today_year = now.get(Calendar.YEAR);
		int today_month = now.get(Calendar.MONTH) + 1;
		int today_day = now.get(Calendar.DATE);

		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "44";
		String info = RspBodyBaseBean.changeFundsInfo_info(searchCondition,
				pageIndex, pageSize, startTime, today_year + "-" + today_month
						+ "-" + today_day + " 23:59:59");

		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=44,账户明细请求info=======" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 提交认购投注请求
	 * 
	 * @param totalCount
	 *            ：总注数
	 * @param totalMoney
	 *            ：金额
	 * @param isStopChase
	 *            ：是否中奖停止追号
	 * @param voucherID
	 *            ：优惠券ID
	 */
	public void commitBetting(long totalCount, long totalMoney, int isStopChase,String voucherID) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String lottryId = AppTools.lottery.getLotteryID();
		String info;
		AppTools.addlottery();
		Set set = AppTools.allLotteryName.keySet();
		Iterator it = set.iterator();
		String lottryName = "";
		while (it.hasNext()) {
			String name = (String) it.next();
			String id = AppTools.allLotteryName.get(name);
			if (id.equals(lottryId)) {
				lottryName = name;
				break;
			}
		}
		if ("73".equals(lottryId)) {// 竞彩篮球
			info = RspBodyBaseBean.changeBet_info(lottryId,voucherID,"-1", AppTools.bei,
					1, 1, 0, 0, "", "", 0, (totalCount * 2 * AppTools.bei),
					totalCount, 0, 0, AppTools.ball, AppTools.lottery.getType()
							+ "");
		} else if ("28".equals(lottryId) || "66".equals(lottryId)) {// 时时彩
			info = RspBodyBaseBean.changeBet_info_ssc(lottryId,
					AppTools.lottery.getIsuseId(), AppTools.bei, 1, 1, 0, 0,
					"", "", 0, totalMoney / AppTools.qi, totalCount,
					AppTools.qi > 1 ? AppTools.qi : 0, AppTools.qi == 1 ? 0
							: totalMoney, AppTools.list_numbers, isStopChase);
		} else if ("72".equals(lottryId)) { // 竞彩足球 此处修改期号为"-1";

			info = RspBodyBaseBean.changeBet_info2(lottryId, AppTools.lottery
					.getIsuseId(),voucherID, AppTools.bei, 1, 1, 0, 0, "", "", 0,
					totalMoney / AppTools.qi, totalCount,
					AppTools.qi > 1 ? AppTools.qi : 0, AppTools.qi == 1 ? 0
							: totalMoney, AppTools.list_numbers, isStopChase);

		} else {
			info = RspBodyBaseBean.changeBet_info2(lottryId, AppTools.lottery
					.getIsuseId(),voucherID,AppTools.bei, 1, 1, 0, 0, "", "", 0,
					totalMoney / AppTools.qi, totalCount,
					AppTools.qi > 1 ? AppTools.qi : 0, AppTools.qi == 1 ? 0
							: totalMoney, AppTools.list_numbers, isStopChase);
		}
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "11";
		String[] values = { opt, auth, info }; // 请求广播消息，opt=“51” info ="5,6,7"
		if (DEBUG)
			Log.i(TAG, lottryName + "提交投注请求info   " + info);
		commonSetThree(AppTools.names, values);
	}

	/**
	 * 提交奖金优化的投注请求
	 * 
	 * @param totalMoney
	 *            ：总注数
	 * @param isStopChase
	 *            ：是否中奖停止追号
	 * @param list_Show
	 *            ： 奖金优化数据
	 * @param youhua_type
	 *            :优化类型
	 * @param title
	 *            ：传""
	 * @param SchemeContent
	 *            ：传""
	 * @param baoCount
	 *            :0
	 * @param allCount
	 *            :1
	 * @param count
	 *            :1
	 * @param secrecyLevel
	 *            :0
	 * @param Bonus
	 *            ：0
	 */
	public void commitBetting_jc_voptimization(long totalMoney,
			int isStopChase, List<Show_JC_Details> list_Show, int youhua_type,
			String title, String SchemeContent, int baoCount, int allCount,
			int count, int secrecyLevel, int Bonus) {

		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String lottryId = AppTools.lottery.getLotteryID();
		String info = "";
		AppTools.addlottery();
		Set set = AppTools.allLotteryName.keySet();
		Iterator it = set.iterator();
		String lottryName = "";
		while (it.hasNext()) {
			String name = (String) it.next();
			Log.i(TAG, "IT.NEXT----" + name);
			String id = AppTools.allLotteryName.get(name);
			Log.i(TAG, "id----" + id);
			if (id.equals(lottryId)) {
				lottryName = name;
				break;
			}
		}
		if ("73".equals(lottryId)) {// 竞彩篮球
			info = RspBodyBaseBean.changeBet_info2_jclq_voptimization(lottryId,
					AppTools.bei, allCount, count, 0, title, SchemeContent,
					secrecyLevel, AppTools.qi > 1 ? AppTools.qi : 0,
					AppTools.qi == 1 ? 0 : totalMoney, AppTools.ball,
					isStopChase, list_Show, youhua_type, baoCount, Bonus); // 最后的0优化类别
		} else if ("72".equals(lottryId) || "73".equals(lottryId)) {
			info = RspBodyBaseBean.changeBet_info2_jc_voptimization(lottryId,
					AppTools.bei, allCount, count, 0, title, SchemeContent,
					secrecyLevel, AppTools.qi > 1 ? AppTools.qi : 0,
					AppTools.qi == 1 ? 0 : totalMoney, AppTools.list_numbers,
					isStopChase, list_Show, youhua_type, baoCount, Bonus); // 最后的0优化类别
		}
		System.out.println("info===" + info);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "72";
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, lottryName + "提交投注请求－－－info   " + info);
		commonSetThree(AppTools.names, values);
	}

	/**
	 * 获取竞彩任选9和胜负彩对阵 opt = "10"
	 * 
	 * @param jcLottryid
	 *            竞彩彩种id 74:胜负彩 75:任选9
	 */
	public void getRx9AndSfcData(String jcLottryid) {
		String key = MD5.md5(AppTools.MD5_key);
		String info = RspBodyBaseBean.changeLottery_infoAgainst(jcLottryid);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info, "-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei, "-1");
		String opt = "10";
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, " 获取竞彩任选9和胜负彩对阵信息----" + info);
		commonSet(AppTools.names, values);
	}

	/**
	 * 提交发单投注请求
	 * @param totalMoney
	 *            总金额
	 * @param shareMoney
	 *            每份金额
	 * @param count
	 *            购买的份数
	 * @param baoCount
	 *            订单类型
	 * @param Bonus
	 *            佣金
	 * @param title
	 *            方案名称
	 * @param content
	 *            方案内容
	 * @param secrecyLevel
	 *            保密设置
	 * @param flag
	 *            是否追号。
	 */
	public void commitFollow(int totalMoney, double shareMoney,
			int baoCount, int Bonus, String title, String content,
			int secrecyLevel, boolean flag) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String lottryId = AppTools.lottery.getLotteryID();
		String info;
		AppTools.addlottery();
		Set set = AppTools.allLotteryName.keySet();
		Iterator it = set.iterator();
		String lottryName = "";
		while (it.hasNext()) {
			String name = (String) it.next();
			Log.i(TAG, "IT.NEXT----" + name);
			String id = AppTools.allLotteryName.get(name);
			Log.i(TAG, "id----" + id);
			if (id.equals(lottryId)) {
				lottryName = name;
				break;
			}
		}
		if ("73".equals(AppTools.lottery.getLotteryID())) {

			info = RspBodyBaseBean.changeBet_info(
					AppTools.lottery.getLotteryID(), "","-1", AppTools.bei,
					(int) (totalMoney / shareMoney), (int) (totalMoney / shareMoney), baoCount, Bonus,
					title, content, secrecyLevel, totalMoney, totalMoney
							/ (2 * AppTools.bei), 0, 0, AppTools.ball,
					AppTools.lottery.getType() + "");

		} else if ("72".equals(AppTools.lottery.getLotteryID())) {
			info = RspBodyBaseBean.changeBet_info_jczq(
					AppTools.lottery.getLotteryID(), "-1", AppTools.bei,
					(int) (totalMoney / shareMoney), (int) (totalMoney / shareMoney), baoCount, Bonus,
					title, content, secrecyLevel, totalMoney, totalMoney
							/ (2 * AppTools.bei), 0, 0, AppTools.list_numbers,
					AppTools.lottery.getType() + "");

		} else {
			if (flag) { // 大乐透追加玩法发起合买的时候
				info = RspBodyBaseBean.changeBet_info2(
						AppTools.lottery.getLotteryID(),
						AppTools.lottery.getIsuseId(), "",AppTools.bei,
						(int) (totalMoney / shareMoney), (int) (totalMoney / shareMoney), baoCount,
						Bonus, title, content, secrecyLevel, totalMoney,
						totalMoney / (3 * AppTools.bei), 0, 0,
						AppTools.list_numbers, 0);
			} else {
				//合买该值为"",voucherID
				info = RspBodyBaseBean.changeBet_info2(
						AppTools.lottery.getLotteryID(),
						AppTools.lottery.getIsuseId(), "",AppTools.bei,
						(int) (totalMoney / shareMoney), (int) (totalMoney / shareMoney), baoCount,
						Bonus, title, content, secrecyLevel, totalMoney,
						totalMoney / (2 * AppTools.bei), 0, 0,
						AppTools.list_numbers, 0);
				if ("28".equals(AppTools.lottery.getLotteryID())) {// 时时彩
					info = RspBodyBaseBean.changeBet_info_ssc(
							AppTools.lottery.getLotteryID(),
							AppTools.lottery.getIsuseId(), AppTools.bei,
							(int) (totalMoney / shareMoney), (int) (totalMoney / shareMoney),
							baoCount, Bonus, title, content, secrecyLevel,
							totalMoney, totalMoney / (2 * AppTools.bei), 0, 0,
							AppTools.list_numbers, 0);
				}
			}
		}
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "11";
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, lottryName + "提交合买投注请求info   " + info);
		commonSetThree(AppTools.names, values);
	}

	/******************************** 购彩大厅 ************************************************/

	/**
	 * 加入合买 opt="12"
	 * 
	 * @param schemesId
	 *            方案id
	 * @param buyShare
	 *            购买份数
	 * @param shareMoney
	 *            购买金额
	 */
	public void joinFollow(String schemesId, int buyShare, long shareMoney) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String info = RspBodyBaseBean.changeFollow_info(schemesId, buyShare,
				shareMoney);
		if (DEBUG)
			Log.i(TAG, "加入合买====   " + info);
		String imei = RspBodyBaseBean.getIMEI(context);
		String crc = RspBodyBaseBean.getCrc(time, imei, AppTools.key, info,
				AppTools.user.getUid());
		// String crc = RspBodyBaseBean.getCrc(time, imei,
		// MD5.md5(AppTools.user.getUserPass() + AppTools.MD5_key), info,
		// AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "12";
		String[] values = { opt, auth, info };
		commonSetThree(AppTools.names, values);
	}

	/******************************** 个人中心 ************************************************/

	/**
	 * 获取积分参数 opt 65
	 */
	public void getIntegralParams() {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String info = "";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "65";
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 兑换积分
	 * 
	 * @param score
	 *            :积分
	 */
	public void exchangeIntegral(double score) {
		String key = MD5.md5(AppTools.MD5_key);
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String info = "{\"score\":\"" + score + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String opt = "26";
		if (DEBUG)
			Log.i(TAG, "兑换积分====   " + info);
		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}
	
	//以下为改版后新增请求---------------------------------2018-01-24-----------------------------------------------
	//以下为改版后新增请求---------------------------------2018-01-24-----------------------------------------------
	//以下为改版后新增请求---------------------------------2018-01-24-----------------------------------------------
	
	/**
	 * 获取牛人数据  
	 * opt 81
	 */
	public void getGreatMan() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "81";
		String info = "";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,"-1");
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,"-1");

		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}
	
	/**
	 * 提交大神申请 
	 * opt 84
	 */
	public void submitApplyRequest() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "84";
		String info = "";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,AppTools.user.getUid());

		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * .NET通用设置1
	 * 
	 * @param names
	 *            请求参数名
	 * @param values
	 *            请求参数值
	 */
	public void commonSet(String[] names, String[] values) {
		Map<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			params.put(names[i], values[i]);
		}
		MySingleton mySingleton = MySingleton.getInstance(context);
		jsonObjectRequest = new JsonObjectRequest(AppTools.path, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						responseCallback(response);
						if (DEBUG)
							Log.i(TAG,
									"getHttpRes==>response"
											+ response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						RequestParams.convertError(context, error, true);
						responseError(error);
						if (DEBUG) {
							String className = error.getClass().getName();
							Log.e(TAG, "className" + className);
							Log.e(TAG,
									"getHttpRes==>onErrorResponse()"
											+ error.getMessage());
						}
					}
				});
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,
				1.0f));
		jsonObjectRequest.setShouldCache(isShouldCache);// 是否设置缓存
		jsonObjectRequest.setCacheTime(cacheTime);// 设置缓存时间
		mySingleton.addToRequestQueue(jsonObjectRequest);
		if (isShowDialog) {
			dialog.show();
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					jsonObjectRequest.cancel();// 取消请求
				}
			});
		}
	}
	
	/**
	 * .NET通用设置4
	 * 禁止弹窗
	 * @param names
	 *            请求参数名
	 * @param values
	 *            请求参数值
	 */
	public void commonSetFour(String[] names, String[] values) {
		Map<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			params.put(names[i], values[i]);
		}
		MySingleton mySingleton = MySingleton.getInstance(context);
		jsonObjectRequest = new JsonObjectRequest(AppTools.path, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						responseCallback(response);
						if (DEBUG)
							Log.i(TAG,
									"getHttpRes==>response"
											+ response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						RequestParams.convertError(context, error, false);
						responseError(error);
						if (DEBUG) {
							String className = error.getClass().getName();
							Log.e(TAG, "className" + className);
							Log.e(TAG,
									"getHttpRes==>onErrorResponse()"
											+ error.getMessage());
						}
					}
				});
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,
				1.0f));
		jsonObjectRequest.setShouldCache(isShouldCache);// 是否设置缓存
		jsonObjectRequest.setCacheTime(cacheTime);// 设置缓存时间
		mySingleton.addToRequestQueue(jsonObjectRequest);
		if (isShowDialog) {
			dialog.show();
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					jsonObjectRequest.cancel();// 取消请求
				}
			});
		}
	}
	
	/**
	 * .NET通用设置2,付款不进行多次请求
	 * 
	 * @param names
	 *            请求参数名
	 * @param values
	 *            请求参数值
	 */
	public void commonSetThree(String[] names, String[] values) {
		Map<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			params.put(names[i], values[i]);
		}
		MySingleton mySingleton = MySingleton.getInstance(context);
		jsonObjectRequest = new JsonObjectRequest(AppTools.path, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						responseCallback(response);
						if (DEBUG)
							Log.i(TAG,
									"getHttpRes==>response"
											+ response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						RequestParams.convertError(context, error, true);
						responseError(error);
						if (DEBUG) {
							String className = error.getClass().getName();
							Log.e(TAG, "className" + className);
							Log.e(TAG,
									"getHttpRes==>onErrorResponse()"
											+ error.getMessage());
						}
					}
				});
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0,
				1.0f));
		jsonObjectRequest.setShouldCache(isShouldCache);// 是否设置缓存
		jsonObjectRequest.setCacheTime(cacheTime);// 设置缓存时间
		mySingleton.addToRequestQueue(jsonObjectRequest);
		if (isShowDialog) {
			dialog.show();
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					jsonObjectRequest.cancel();// 取消请求
				}
			});
		}
	}
	
	/**
	 * java后台通用设置
	 * 
	 * @param names
	 *            请求参数名
	 * @param values
	 *            请求参数值
	 */
	public void commonSetTwo(String[] names, String[] values) {
		Map<String, String> params = new HashMap<String, String>();
		for (int i = 0; i < names.length; i++) {
			params.put(names[i], values[i]);
		}
		MySingleton mySingleton = MySingleton.getInstance(context);
		jsonObjectRequest = new JsonObjectRequest(AppTools.livePath, params,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						responseCallback(response);
						if (DEBUG)
							Log.i(TAG,
									"getHttpRes==>response"
											+ response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (null != dialog && dialog.isShowing())
							dialog.dismiss();
						RequestParams.convertError(context, error, true);
						responseError(error);
						if (DEBUG) {
							String className = error.getClass().getName();
							Log.e(TAG, "className" + className);
							String msg = error.getMessage();
							Log.e(TAG,
									"getHttpRes==>onErrorResponse()"
											+ error.getMessage());
						}
					}
				});
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1,
				1.0f));
		jsonObjectRequest.setShouldCache(isShouldCache);
		jsonObjectRequest.setCacheTime(cacheTime);
		mySingleton.addToRequestQueue(jsonObjectRequest);
		if (isShowDialog) {
			dialog.show();
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					jsonObjectRequest.cancel();
				}
			});
		}
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg,
			boolean iscancel) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.layout_progressbar, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.layout_progress_horizontal);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.iv_progress);
		TextView tipTextView = (TextView) v.findViewById(R.id.tv_progress);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.progress_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(iscancel);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	/**
	 * 获取当前请求对象
	 * 
	 * @return jsonObjectRequest请求对象
	 */
	public Request getRequest() {
		return jsonObjectRequest;
	}

	/**
	 * JsonObjectRequest回调方法
	 * 
	 * @param reponseJson
	 *            回调json对象
	 */
	public abstract void responseCallback(JSONObject reponseJson);

	/**
	 * JsonObjectRequest错误回调方法
	 * 
	 * @param error
	 *            VolleyError对象
	 */
	public abstract void responseError(VolleyError error);
	
	
	//----------------------------新增2018-03------------------------
	/**
	 * 比分直播赛事
	 */
	public void getMatchData(String opt,String date) {
		String[] matchNames = { "opt","date"};
		String[] values = { opt,date};
		commonSetTwo(matchNames, values);
	}
	
	/**
	 * 队伍战绩信息
	 */
	public void getStandingsInformation(String matchId,String opt) {
		String[] values = { opt,matchId };
		commonSetTwo(AppTools.otherNames, values);
	}
	
	/**
	 * 提现时间获取
	 */
	public void getWithdrawTime() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "85";
		String info = "";
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}
	
	/**
	 * 充值方式
	 */
	public void getRecharge() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "86";
		String info = "";
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}

	/**
	 * 支付信息
	 */
	public void getPayinfo(String money,String way) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "88";
		String info = "{\"userId\":\""+ AppTools.user.getUid()  + "\",\"money\":\"" + money +
			"\",\"way\":\""+ way + "\"}";
		String key = MD5.md5(AppTools.MD5_key);
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };

//		String time = RspBodyBaseBean.getTime();
//		String imei = RspBodyBaseBean.getIMEI(context);
//		String opt = "88";
//		String info = "{\"userId\":\""+ couponMoney + "\"}";
//		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
//				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);
//
//		String[] values = { opt, auth, info };
		commonSetThree(AppTools.names, values);
	}
	
	/**
	 * 新闻资讯
	 */
	public void getInformation() {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String opt = "45";
		String info = RspBodyBaseBean.getLotteryInformation(1,5,2);
		String auth = RspBodyBaseBean.changeLogin_Auth(RspBodyBaseBean.getCrc(
				time, imei, MD5.md5(AppTools.MD5_key), info, "-1"), time, imei);

		String[] values = { opt, auth, info };
		commonSet(AppTools.names, values);
	}
	
	/**
	 * 优惠券
	 */
	public void getCouponDatas(String couponMoney) {
		String time = RspBodyBaseBean.getTime();
		String imei = RspBodyBaseBean.getIMEI(context);
		String key = MD5.md5(AppTools.MD5_key);
		String opt = "87";
		String info = "{\"BuyMoney\":\""+ couponMoney + "\"}";
		String crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		String auth = RspBodyBaseBean.getAuth(crc, time, imei,
				AppTools.user.getUid());
		String[] values = { opt, auth, info };
		if (DEBUG)
			Log.i(TAG, "opt=87,查看优惠券info=======" + info);
		commonSet(AppTools.names, values);
	}
}
