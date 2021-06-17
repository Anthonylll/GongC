package com.gcapp.tc.sd.ui;

/**
 * 功能：个人中心的签到模块
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog_signSuccess;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.SignCalendarView;
import com.gcapp.tc.R;

public class SignCalendar_Activity extends Activity {
	private ImageButton btn_back;
	private static final String TAG = "SignCalendar_Activity";
	private TextView tv_year, tv_month, tv_day;
	private ConfirmDialog_signSuccess dialog;// 提示框
	private SignCalendarView calendar;
	private Button btn_signIn;// 签到监听按钮
	private List<String> list = new ArrayList<String>(); // 设置标记列表
	private String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	private TextView tv_tip_info; // 温馨提示
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	private String myMsg;
	private int score = 0;// 签到送的积分
	private Context context = SignCalendar_Activity.this;
	private ProgressDialog proDialog;
	private int isScore = 0;// 标识是否今天签过到（1是签过到）
	private List<String> dataList = new ArrayList<String>();
	private TextView tv_week;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		findView();
		setListner();
		getsignRecord();
	}

	/**
	 * 初始化UI控件和界面数据
	 */
	private void findView() {
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		tv_week = (TextView) findViewById(R.id.tv_week);
		tv_year = (TextView) findViewById(R.id.tv_year);
		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_day = (TextView) findViewById(R.id.tv_day);

		calendar = (SignCalendarView) findViewById(R.id.popupwindow_calendar);
		btn_signIn = (Button) findViewById(R.id.btn_signIn);
		tv_tip_info = (TextView) this.findViewById(R.id.tv_tip_sign);

		tv_year.setText(calendar.getCalendarYear() + "");
		tv_month.setText(+calendar.getCalendarMonth() + "");

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(d);
		System.out.println("格式化后的日期：" + dateString);

		Date todaydate = null;
		try {
			todaydate = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(todaydate);
		int weekOfMonth = calendar2.get(Calendar.WEEK_OF_YEAR);

		tv_week.setText("第" + weekOfMonth + "周");
		tv_tip_info.setText("签到规则：\n1、用户签到需要完善账户信息。\n"
				+ "\n2、第一天签到送10积分，第二天20积分，以此类推，第七天70积分；此后每天都送70积分。"
				+ "签到以月为单位，即下月第一天默认为第一天签到，送10积分。\n" + "\n3、若某天没有签到，则重新累计。\n\n");

		Date today = calendar.getThisday();
		calendar.setCalendarDayBgColor(today, R.drawable.sign_bg_today);

		if (null != dateString) {
			String day = dateString.split("-")[2];
			tv_day.setText(day);
		}
		dialog = new ConfirmDialog_signSuccess(this, R.style.dialog);
	}

	/**
	 * 绑定监听
	 */
	private void setListner() {
		btn_back.setOnClickListener(new MyClickListener());
		btn_signIn.setOnClickListener(new MyClickListener());
	}

	/**
	 * 公用点击监听
	 */
	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;

			case R.id.btn_signIn:// 签到
				goSign_today();
				break;
			}
		}
	}

	/**
	 * 提交查询签到列表请求
	 */
	public void getsignRecord() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "查询签到列表result：" + object);
				try {
					String error = object.optString("success");
					isScore = object.optInt("isScore");
					myMsg = object.optString("info");
					if (error.equals("0")) {
						JSONArray array = new JSONArray(
								object.getString("List"));
						if (array.length() > 0) {
							if (dataList.size() > 0) {
								dataList.clear();
							}
						}
						for (int i = 0; i < array.length(); i++) {
							JSONObject item = array.getJSONObject(i);
							if (null != item) {
								dataList.add(item.optString("date"));
							}
						}
						calendar.addMarks(dataList, 0);
						if (isScore == 1) {
							btn_signIn.setText("已签到");
							btn_signIn.setEnabled(false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

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
		requestUtil.getSignInfo();
	}

	/**
	 * 提交今天签到请求
	 */
	public void goSign_today() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "今天签到result：" + object);
				try {
					String error = object.optString("success");
					myMsg = object.optString("info");
					score = object.optInt("score");
					if (error.equals("0")) {
						Date today = calendar.getThisday();
						calendar.addMark(today, 0);
						calendar.setCalendarDayBgColor(today,
								R.drawable.sign_bg_today);
						showDialog();// 弹出对话框
						btn_signIn.setText("已签到");
						btn_signIn.setEnabled(false);
					} else {
						MyToast.getToast(context, myMsg);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

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
		requestUtil.goSign_today();
	}

	/**
	 * 签到成功的赠送积分的窗口提示
	 */
	private void showDialog() {
		dialog.show();
		dialog.setJifen(score);
		dialog.setDialogResultListener(new ConfirmDialog_signSuccess.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) { // 确定
					// clear();
				}
			}
		});
	}

}
