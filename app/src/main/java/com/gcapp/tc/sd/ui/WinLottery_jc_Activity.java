package com.gcapp.tc.sd.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.LotteryDtMatch;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapter_lottery;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.IphoneTreeView;
import com.gcapp.tc.view.MyDateTimeDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.SlideView;
import com.gcapp.tc.view.SlideView.SlideListener;
import com.gcapp.tc.wheel.widget.NumericWheelAdapter;
import com.gcapp.tc.R;

/**
 * 功能：开奖大厅的竞彩类的开奖详情模块
 * 
 * @author echo
 * 
 */
public class WinLottery_jc_Activity extends Activity implements OnClickListener {
	private Context context = WinLottery_jc_Activity.this;
	private static final String TAG = "WinLottery_jc_Activity";
	private String auth, info, time, imei, crc; // 格式化后的参数
	private String date, lotteryId;
	private TextView tv_title;
	private SlideView slide_touzhu;
	private IphoneTreeView exList;
	private ExpandAdapter_lottery exAdapter;
	private int year, month, days;
	private List<List<LotteryDtMatch>> listMatch;
	private MyHandler myHandler;
	private MyDateTimeDialog dateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_winlottery_number_jc);
		App.activityS.add(this);
		findView();
		init();
		initDialog();
	}

	/**
	 * 初始化时间选择对话框的值
	 */
	private void initDialog() {
		NumericWheelAdapter mYearAdapter = new NumericWheelAdapter(2008, 2050);
		NumericWheelAdapter mMonthAdapter = new NumericWheelAdapter(1, 12);
		NumericWheelAdapter mDayAdapter = new NumericWheelAdapter(1,
				AppTools.getLastDay(year, month));
		dateDialog = new MyDateTimeDialog(WinLottery_jc_Activity.this,
				R.style.dialog, mYearAdapter, mMonthAdapter, mDayAdapter,
				new MyClickListener());
		dateDialog.initDay(year, month, days);
	}

	/**
	 * 日期 dialog 点击监听
	 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.funds_btn_ok:
				dateDialog.dismiss();
				setDays(dateDialog.y, dateDialog.m, dateDialog.d);
				updateAdapter();
				break;
			case R.id.funds_btn_no:
				dateDialog.dismiss();
				break;
			}
			setDays();
			dateDialog.setCheckItem();
		}
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		ImageButton mBtn_back = (ImageButton) this.findViewById(R.id.btn_back);
		ImageButton mBtn_date = (ImageButton) this.findViewById(R.id.btn_date);
		tv_title = (TextView) this.findViewById(R.id.tv_head);
		slide_touzhu = (SlideView) findViewById(R.id.slide_touzhu);
		exList = (IphoneTreeView) this.findViewById(R.id.jc_exListView);
		exList.setHeaderView(getLayoutInflater().inflate(
				R.layout.win_lottery_jc_detail_header, exList, false));
		mBtn_date.setOnClickListener(this);
		mBtn_back.setOnClickListener(this);
		slide_touzhu.setSlideListener(new SlideListener() {
			@Override
			public void onDone() {
				Lottery mLottery = AppTools.getLotteryById(lotteryId);
				if (mLottery == null) {
					MyToast.getToast(context, "该奖期已结束，请等下一期");
					slide_touzhu.reset();
					return;
				} else {
					LotteryUtils.goToSelectLottery_jc(context, lotteryId);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		slide_touzhu.reset();
		super.onResume();
	}

	/**
	 * 初始化属性
	 */
	private void init() {
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(WinLottery_jc_Activity.this);
		lotteryId = getIntent().getStringExtra("lotteryId");
		date = getIntent().getStringExtra("date");
		if (TextUtils.isEmpty(date)) {
			// 得到Calendar类的实例。
			Calendar now = Calendar.getInstance();
			year = now.get(Calendar.YEAR);
			month = now.get(Calendar.MONTH) + 1;
			days = now.get(Calendar.DATE);
			date = year + "-" + month + "-" + days;
		} else {
			// 模拟运用站点数据都是 2015/09/25
			year = Integer.valueOf(date.split("-|/")[0]);
			month = Integer.valueOf(date.split("-|/")[1]);
			days = Integer.valueOf(date.split("-|/")[2]);
		}
		if ("73".equals(lotteryId))
			tv_title.setText("竞彩篮球开奖详情");
		if ("72".equals(lotteryId))
			tv_title.setText("竞彩足球开奖详情");
		if ("45".equals(lotteryId))
			tv_title.setText("北京单场开奖详情");
		myHandler = new MyHandler();

		getWinBetInfo_jc();
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();
	}

	/**
	 * 将返回的数据解析
	 * 
	 * @param arr
	 *            ：返回的数据
	 * @return
	 */
	private List<LotteryDtMatch> setList(String arr) {
		List<LotteryDtMatch> list_m = new ArrayList<LotteryDtMatch>();
		if (arr.length() > 5) {
			JSONArray Arr;
			try {
				Arr = new JSONArray(arr);
				LotteryDtMatch dtmatch;
				for (int j = 0; j < Arr.length(); j++) {
					JSONObject item = Arr.getJSONObject(j);
					dtmatch = new LotteryDtMatch();
					dtmatch.setId(item.optString("id"));
					dtmatch.setMatchNumber(item.optString("matchNumber"));
					dtmatch.setWeekDay(item.getString("weekDay"));
					dtmatch.setGame(item.getString("game"));
					dtmatch.setGuestTeam(item.optString("guestTeam"));
					dtmatch.setMainTeam(item.getString("mainTeam"));
					dtmatch.setStopSellTime(item.optString("stopSellTime"));
					dtmatch.setMatchDate(item.optString("matchDate"));
					if ("72".equals(lotteryId)) {
						dtmatch.setSpfResult(item.getString("spfResult"));// 胜平负
						dtmatch.setSpfSp(item.getString("spfSp"));
						dtmatch.setRqspfResult(item.getString("rqspfResult"));// 让球胜平负
						dtmatch.setRqspfSp(item.getString("rqspfSp"));
						dtmatch.setZjqResult(item.getString("zjqResult"));// 总进球
						dtmatch.setZjqSp(item.getString("zjqSp"));
						dtmatch.setBfResult(item.getString("bfResult"));// 比分
						dtmatch.setBfSp(item.getString("bfSp"));
						dtmatch.setBqcResult(item.getString("bqcResult"));// 半全场
						dtmatch.setBqcSp(item.getString("bqcSp"));
						dtmatch.setLoseBall(item.getInt("mainLoseBall"));
						dtmatch.setAllResult(item.getString("cbfResult"));
						dtmatch.setHalfResult(item.getString("halfResult"));

					} else if ("73".equals(lotteryId)) {
						dtmatch.setSfResult(item.getString("sfResult")); // 胜负赛果
						dtmatch.setSfSp(item.getString("sfSp"));
						dtmatch.setDxResult(item.getString("dxfResult")); // 大小分赛果
						dtmatch.setDxfSp(item.getString("dxfSp"));
						dtmatch.setRfsfResult(item.getString("rfsfResult")); // 让分胜负赛果
						dtmatch.setRfsfSp(item.getString("rfsfSp"));
						dtmatch.setSfcResult(item.getString("sfcResult")); // 胜分差赛果
						dtmatch.setSfcSp(item.getString("sfcSp"));
						dtmatch.setLoseScore(item.getString("giveWinLoseScore"));
						dtmatch.setResult(item.getString("result"));

					} else if ("45".equals(lotteryId)) {
						dtmatch.setSxdsResult(item.getString("sxdsResult"));
						dtmatch.setSxdsSp(item.getString("sxdsSp"));
						dtmatch.setBqcResult(item.getString("bqcResult"));
						dtmatch.setBqcSp(item.getString("bqcSp"));
						dtmatch.setZjqResult(item.getString("zjqsResult"));
						dtmatch.setZjqSp(item.getString("zjqsSp"));
						dtmatch.setCbfResult(item.getString("bfResult"));
						dtmatch.setBfSp(item.getString("bfSp"));
						dtmatch.setSpfResult(item.getString("rqspfResult"));
						dtmatch.setRqspfSp(item.getString("rqspfSp"));

						dtmatch.setLoseScore(item.getString("giveBall"));
						dtmatch.setAllResult(item.getString("fullScore"));
						dtmatch.setHalfResult(item.getString("halfCourtScore"));
					}
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
	 * 得到竞彩彩种的开奖详情
	 */
	public void getWinBetInfo_jc() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"数据加载中...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "竞彩开奖公告的详情的result：" + object);
				String error = "-1";
				listMatch = new ArrayList<List<LotteryDtMatch>>();

				try {
					if (null != object) {
						error = object.optString("error");
						if ("0".equals(error)) {
							String detail = object.optString("dtMatch");
							if (detail.length() < 5) {
								error = "-1";
							} else {
								// 拿到对阵信息组
								JSONArray array = new JSONArray(new JSONArray(
										detail).toString());

								for (int i = 0; i < array.length(); i++) {

									JSONObject ob = array.getJSONObject(i);
									String arr3 = ob.optString("table1");
									String arr2 = ob.optString("table2");
									String arr = ob.optString("table3");

									// 判断对阵 是否有
									listMatch.add(setList(arr));
									listMatch.add(setList(arr2));
									listMatch.add(setList(arr3));
								}
							}
						}
					} else {
						Log.i("x", "拿竞彩足球数据为空");
					}
				} catch (Exception e) {
					System.out.println("错误" + e.getMessage());
					error = "-1";
				}

				myHandler.sendEmptyMessage(Integer.parseInt(error));
				if (object.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getWinInfo_jc(lotteryId, date);
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
			String date1 = "1970-01-01";
			switch (msg.what) {
			case 0:
				try {
					Date firstDate = format2.parse(date);
					date1 = format1.format(firstDate);
				} catch (ParseException e) {
					date1 = date;
					e.printStackTrace();
				}
				exAdapter = new ExpandAdapter_lottery(
						WinLottery_jc_Activity.this, listMatch,
						Integer.parseInt(lotteryId), exList, date1);
				exList.setAdapter(exAdapter);
				exList.expandGroup(0);
				break;
			case -500:
				MyToast.getToast(WinLottery_jc_Activity.this, "连接超时");
				break;
			case -1:
				MyToast.getToast(WinLottery_jc_Activity.this, "没有开奖信息");
				try {
					Date firstDate = format2.parse(date);
					date1 = format1.format(firstDate);
				} catch (ParseException e) {
					date1 = date;
					e.printStackTrace();
				}
				exAdapter = new ExpandAdapter_lottery(
						WinLottery_jc_Activity.this, listMatch,
						Integer.parseInt(lotteryId), exList, date1);
				exList.setAdapter(exAdapter);
				break;
			}
		}
	}

	/**
	 * 公用点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_date:
			dateDialog.show();
			break;
		}
	}

	/**
	 * 设置日期 *
	 */
	public void setDays(int y, int m, int d) {
		this.year = y;
		this.month = m;
		this.days = d;
		date = year + "-" + month + "-" + days;
	}

	/**
	 * 初始化日期选择框的数据
	 */
	public void setDays() {
		dateDialog.initDay(year, month, days);
	}

	/**
	 * 更新请求和竞彩数据
	 */
	public void updateAdapter() {
		getWinBetInfo_jc();
		exAdapter.notifyDataSetChanged();
	}
}
