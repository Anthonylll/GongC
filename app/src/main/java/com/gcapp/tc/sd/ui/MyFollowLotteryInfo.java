package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.sd.ui.adapter.Follow_lottery_detail_adapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：个人中心的追号投注详情类
 * 
 * @author lenovo
 * 
 */
public class MyFollowLotteryInfo extends Activity implements OnClickListener {
	private final static String TAG = "MyFollowLotteryInfo";
	private Context context = MyFollowLotteryInfo.this;
	private ScrollView bet_follow_scrollview;
	private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney,
			tv_followInfo, tv_show, tv_show2, tv_lotteryName_issue;
	private ImageView img_logo;
	private MyListView2 mListView, mListView2;

	private ImageButton img_btn, img_btn2, btn_back;
	private ImageView rl_divider, rl_divider2;
	private RelativeLayout rl_tv_show, rl_tv_show2,rl_follow2;

	private Schemes myScheme; // 总追号 订单

	private Follow_lottery_detail_adapter adapter, adapter2;
	private List<String> listDate = new ArrayList<String>();
	private List<Schemes> listCompleted = new ArrayList<Schemes>();

	private List<String> listDate2 = new ArrayList<String>();
	private List<Schemes> listUnCompleted = new ArrayList<Schemes>();

	private List<Schemes> show_listCompleted = new ArrayList<Schemes>();
	private List<Schemes> show_listUnCompleted = new ArrayList<Schemes>();
//	private String opt = "49"; // 格式化后的 opt

//	private String auth, info, time, imei, crc; // 格式化后的参数
	// private MyAsynTask myAsynTask;
	private MyHandler myHandler;
	private TextView footer, un_footer;
	private int pageIndex = 1;
	private int un_pageIndex = 1;
	private int pageSize = 10;
	private int length = 0;
	private int un_length = 0;
	private int flag = 0; // 0为完成期数刷新，1为未完成期数刷新
	private int sumUnCompletedCount, sumCompletedCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet_followinfo);
		findView();
		initView();
		setListener();
	}

	/**
	 * 重写onResume方法
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mListView.setFocusable(true);
		mListView2.setFocusable(true);
		bet_follow_scrollview.smoothScrollTo(0, 0);
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		myHandler = new MyHandler();
		bet_follow_scrollview = (ScrollView) findViewById(R.id.bet_follow_scrollview);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		tv_money = (TextView) findViewById(R.id.tv_money2);
		tv_state = (TextView) findViewById(R.id.tv_state2);
		tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
		tv_followInfo = (TextView) findViewById(R.id.tv_followInfo);
		tv_show = (TextView) findViewById(R.id.tv_show);
		tv_show2 = (TextView) findViewById(R.id.tv_show2);
		tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);

		img_logo = (ImageView) findViewById(R.id.img_logo);
		mListView = (MyListView2) findViewById(R.id.lv_followInfo);
		mListView2 = (MyListView2) findViewById(R.id.lv_followInfo2);
		img_btn = (ImageButton) findViewById(R.id.btn_jiantou);
		img_btn2 = (ImageButton) findViewById(R.id.btn_jiantou2);
		rl_tv_show = (RelativeLayout) findViewById(R.id.rl_tv_show);
		rl_tv_show2 = (RelativeLayout) findViewById(R.id.rl_tv_show2);
		rl_follow2 = (RelativeLayout) findViewById(R.id.rl_follow2);
		rl_divider = (ImageView) findViewById(R.id.rl_divider);
		rl_divider2 = (ImageView) findViewById(R.id.rl_divider2);

		adapter = new Follow_lottery_detail_adapter(getApplicationContext(),
				show_listCompleted, 2);
		adapter2 = new Follow_lottery_detail_adapter(getApplicationContext(),
				show_listUnCompleted, 2);
		mListView.setOnItemClickListener(new MyItemCLickListener());
		mListView2.setOnItemClickListener(new MyItemCLickListener2());

		footer = new TextView(MyFollowLotteryInfo.this);
		footer.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, getResources()
						.getDimensionPixelSize(
								R.dimen.bet_lottery_item_title_height)));
		footer.setTextSize(14);
		footer.setGravity(Gravity.CENTER);
		footer.setTextColor(getResources().getColor(
				R.color.vpi_text_unselected_gray));
		footer.setText("下一页");
		footer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pageIndex++;
				flag = 0;
				getFollowBetinfo();
				// myAsynTask = new MyAsynTask();
				// myAsynTask.execute();
			}
		});
		mListView.addFooterView(footer);
		un_footer = new TextView(MyFollowLotteryInfo.this);
		un_footer.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, getResources()
						.getDimensionPixelSize(
								R.dimen.bet_lottery_item_title_height)));
		un_footer.setTextSize(14);
		un_footer.setGravity(Gravity.CENTER);
		un_footer.setTextColor(getResources().getColor(
				R.color.vpi_text_unselected_gray));
		un_footer.setText("下一页");
		un_footer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				un_pageIndex++;
				flag = 1;
				getFollowBetinfo();
				// myAsynTask = new MyAsynTask();
				// myAsynTask.execute();
			}
		});
		mListView2.addFooterView(un_footer);
	}

	/**
	 * 给控件赋值
	 */
	private void initView() {
		myScheme = (Schemes) getIntent().getSerializableExtra("scheme");
		if (null == myScheme)
			return;

		Log.i("x",
				"追号订单ID " + myScheme.getId() + "===订单号="
						+ myScheme.getSchemeNumber() + "  倍数===="
						+ myScheme.getMultiple());

		mListView.setAdapter(adapter);
		mListView2.setAdapter(adapter2);
		tv_followInfo.setVisibility(View.VISIBLE);
		tv_lotteryName.setText(LotteryUtils.getTitleText(myScheme
				.getLotteryID()));
		tv_lotteryName_issue.setText("");
		tv_money.setText(myScheme.getMoney() + "元");
		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(myScheme
				.getLotteryID()));
		tv_winMoney.setText("--");

		String status = myScheme.getSchemeStatus();
		if (status.equals("已中奖")||myScheme.getWinMoneyNoWithTax()>0) {
			tv_state.setTextColor(Color.RED);
			tv_state.setText("中奖");
			tv_winMoney.setText(myScheme.getWinMoneyNoWithTax() + "元");
		} else {
			tv_state.setText(status);
		}
		getFollowBetinfo();
		// myAsynTask = new MyAsynTask();
		// myAsynTask.execute();
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		img_btn.setOnClickListener(this);
		img_btn2.setOnClickListener(this);
		rl_tv_show.setOnClickListener(this);
		rl_tv_show2.setOnClickListener(this);
	}

	/**
	 * 已完成的listView的子项点击监听
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemCLickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Schemes scheme = listCompleted.get(position);
			Intent intent = new Intent(MyFollowLotteryInfo.this,
					MyCommonLotteryInfo.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", scheme);
			MyFollowLotteryInfo.this.startActivity(intent);
		}
	}

	/**
	 * 未完成的listView的子项点击监听
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemCLickListener2 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Schemes scheme = listUnCompleted.get(position);
			Intent intent = new Intent(MyFollowLotteryInfo.this,
					MyCommonLotteryInfo.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", scheme);
			MyFollowLotteryInfo.this.startActivity(intent);
		}
	}

	/**
	 * 提交追号投注详情请求
	 */
	public void getFollowBetinfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "追号投注详情结果===》" + object);
				String error = "-1";
				try {
					error = object.getString("error");
					sumCompletedCount = object.getInt("sumCompletedCount");
					sumUnCompletedCount = object.getInt("sumUnCompletedCount");
					if ("0".equals(object.getString("error"))) {
						JSONObject item = new JSONObject(
								object.optString("chaseTaskDetailsList"));

						String com = item.optString("completed");
						if (com.length() > 5) {
							JSONArray array2 = new JSONArray(com);
							Log.i("x", "completed  " + array2.toString());
							getList(array2, listCompleted, listDate);
						}

						String unCom = item.optString("unCompleted");
						if (unCom.length() > 5) {
							JSONArray array3 = new JSONArray(unCom);
							getList(array3, listUnCompleted, listDate2);
						}

						length = setShowListData(listCompleted,
								show_listCompleted, pageIndex, pageSize);
						un_length = setShowListData(listUnCompleted,
								show_listUnCompleted, un_pageIndex, pageSize);

					}
				} catch (Exception e) {
					e.printStackTrace();
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

		if (pageIndex == 1 && un_pageIndex == 1) {
			requestUtil.getFollowbetInfo(AppTools.user.getUid(),
					myScheme.getChaseTaskID());
		} else {
			switch (flag) {
			case 0:
				length = setShowListData(listCompleted, show_listCompleted,
						pageIndex, pageSize);
				myHandler.sendEmptyMessage(0);
				break;

			case 1:
				un_length = setShowListData(listUnCompleted,
						show_listUnCompleted, un_pageIndex, pageSize);
				myHandler.sendEmptyMessage(0);
				break;
			}
		}
	}

	/**
	 * 解析Json格式 数据
	 */
	private void getList(JSONArray array, List<Schemes> listScheme,
			List<String> listStr) {
		if (listScheme == null) {
			return;
		}

		Log.i("x", "解析Json  " + array.toString());
		if (array.toString().length() < 5)
			return;
		try {
			for (int j = 0; j < array.length(); j++) {
				JSONObject obj = array.getJSONObject(j);

				JSONArray arr = new JSONArray(obj.getString("list"));
				Schemes scheme = null;
				for (int k = 0; k < arr.length(); k++) {
					JSONObject items = arr.getJSONObject(k);

					if (items.getString("date").length() > 2)
						listStr.add(items.getString("date"));

					JSONArray detail = new JSONArray(items.getString("detail"));

					Log.i("x", "数据集合长度  " + detail.length());
					for (int a = 0; a < detail.length(); a++) {
						scheme = new Schemes();
						JSONObject list = detail.getJSONObject(a);
						scheme.setLotteryID(myScheme.getLotteryID());
						scheme.setLotteryNumber(myScheme.getLotteryNumber());
						scheme.setLotteryName(myScheme.getLotteryName());
						scheme.setIsPurchasing("true");
						scheme.setSchemeIsOpened(list.getString("isOpened"));
						scheme.setIsChase(myScheme.getIsChase());
						scheme.setMultiple(list.getInt("multiple"));
						scheme.setSchemeNumber(list.getString("schemeNumber"));
						scheme.setDateTime(list.getString("schemeDateTime"));
						scheme.setStopWhenWinMoney(list
								.getDouble("stopWhenWinMoney"));
						scheme.setChaseTaskID(list.getInt("chaseTaskID"));
						scheme.setMoney(list.getDouble("money"));
						scheme.setId(list.getString("schemeID"));
						scheme.setIsuseID(list.getString("isuseID"));
						scheme.setQuashStatus(list.getInt("quashStatus"));
						scheme.setExecuted(list.getBoolean("executed"));
						scheme.setIsuseName(list.getString("issueName"));
						scheme.setWinMoneyNoWithTax(list
								.getDouble("winMoneyNoWithTax"));
						scheme.setWinNumber(list.getString("winLotteryNumber"));
						scheme.setSchemeStatus(list.getString("schemeStatus"));
						JSONArray array_contents = new JSONArray(
								list.optString("buyContent"));
						List<LotteryContent> contents = new ArrayList<LotteryContent>();
						LotteryContent lotteryContent = null;
						for (int m = 0; m < array_contents.length(); m++) {
							lotteryContent = new LotteryContent();
							try {
								JSONArray array2 = new JSONArray(
										array_contents.optString(m));

								lotteryContent.setLotteryNumber(array2
										.getJSONObject(0).optString(
												"lotteryNumber"));
								lotteryContent
										.setPlayType(array2.getJSONObject(0)
												.optString("playType"));
								lotteryContent
										.setSumMoney(array2.getJSONObject(0)
												.optString("sumMoney"));
								lotteryContent.setSumNum(array2
										.getJSONObject(0).optString("sumNum"));
								contents.add(lotteryContent);
							} catch (Exception e) {
								JSONObject array2 = new JSONObject(
										array_contents.optString(m));

								lotteryContent.setLotteryNumber(array2
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

						listScheme.add(scheme);
					}
				}
			}
			System.out.println("......." + listScheme.size());
		} catch (Exception e) {
			Log.i("x", "拿追号详情报错 ==== " + e.getMessage());
		}

	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				MyToast.getToast(getApplicationContext(), "没有数据");
				break;
			case 0:
				footer.setText("下一页 (" + show_listCompleted.size() + "/"
						+ listCompleted.size() + ")");
				un_footer.setText("下一页 (" + show_listUnCompleted.size() + "/"
						+ listUnCompleted.size() + ")");

				adapter.setData(show_listCompleted);
				adapter.notifyDataSetChanged();
				adapter2.setData(show_listUnCompleted);
				adapter2.notifyDataSetChanged();
				if (pageIndex == 1 && un_pageIndex == 1) {
					tv_show.setText("已完成" + sumCompletedCount + "期");
					if(un_length == 0) {
						rl_follow2.setVisibility(View.GONE);
					}else{
						tv_show2.setText("未完成" + sumUnCompletedCount + "期");
					}
					int i = sumCompletedCount + sumUnCompletedCount;
					if (myScheme.getStopWhenWinMoney() == 1)
						tv_followInfo.setText("共" + i + "期，中奖后停止");
					else if (myScheme.getStopWhenWinMoney() > 1)
						tv_followInfo.setText("共" + i + "后停止");
					else
						tv_followInfo.setText("共" + i + "期");
				}

				break;
			case -500:
				MyToast.getToast(getApplicationContext(), "连接超时");
				break;
			}

			if (length < pageSize || listCompleted.size() == pageSize) {
				mListView.removeFooterView(footer);
			}
			if (un_length < pageSize || listUnCompleted.size() == pageSize) {
				mListView2.removeFooterView(un_footer);
			}
		}
	}

	/**
	 * 公用的点击监听方法
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_tv_show:
		case R.id.btn_jiantou:
			if (img_btn.isSelected()) {
				img_btn.setSelected(false);
				rl_divider.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
			} else {
				img_btn.setSelected(true);
				rl_divider.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
			}
			break;
		case R.id.rl_tv_show2:
		case R.id.btn_jiantou2:
			if (img_btn2.isSelected()) {
				img_btn2.setSelected(false);
				rl_divider2.setVisibility(View.VISIBLE);
				mListView2.setVisibility(View.VISIBLE);
			} else {
				img_btn2.setSelected(true);
				rl_divider2.setVisibility(View.GONE);
				mListView2.setVisibility(View.GONE);
			}
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 分页追号数量
	 * 
	 * @param source
	 *            ：已经完成的期号list
	 * @param show
	 *            ：显示的期号详情list
	 * @param pageindex
	 *            ：页面index
	 * @param pageSize
	 *            ：页面显示的数据大小
	 * @return
	 */
	private int setShowListData(List<Schemes> source, List<Schemes> show,
			int pageindex, int pageSize) {
		int length = Math.min(source.size() - pageSize * (pageindex - 1),
				pageSize);
		for (int i = 0; i < length; i++) {
			int index = i + pageSize * (pageindex - 1);
			show.add(source.get(index));
		}
		return length;
	}
}
