package com.gcapp.tc.sd.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：个人中心的合买投注详情类
 */
public class MyCommonLotteryInfo_join extends Activity implements
		OnClickListener {
	private ScrollView scrollView;
	private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney, tv_count,
			tv_time, tv_orderId, tv_betType, tv_tishi, tv_lotteryNum, tv_name,
			tv_yong, tv_scheme, tv_buyCount, tv_title, tv_content, tv_bei,
			tv_lotteryName_issue, tv_info_tip;
	private ImageView img_logo, ll_divider;
	private LinearLayout ll_numberCount;
	private ImageButton betinfo_hide_btn, btn_back;
	private MyListView2 mListView;

	private MyGridView gv_winNumber;

	private Schemes scheme;

	private RelativeLayout rl_join1, rl_join2;

	private String[] numbers;
	private List<String> show_numbers;
	private List<Integer> max;

	private int pageindex = 1;
	private int pagesize = 10;

	private Button btn_jixu;
	private Button btn_touzhu;
	private TextView footer;
	private int temp_length = 0;
	private TextView tv_secretMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_betlottey_info);
		App.activityS.add(this);
		scheme = (Schemes) getIntent().getSerializableExtra("scheme");
		findView();
		initView();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		tv_money = (TextView) findViewById(R.id.tv_money2);
		tv_state = (TextView) findViewById(R.id.tv_state2);
		tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
		tv_count = (TextView) findViewById(R.id.tv_numberCount);
		tv_time = (TextView) findViewById(R.id.tv_time2);
		tv_orderId = (TextView) findViewById(R.id.tv_orderId2);
		tv_betType = (TextView) findViewById(R.id.tv_orderType2);
		tv_tishi = (TextView) findViewById(R.id.tv_wShow2);
		tv_lotteryNum = (TextView) findViewById(R.id.tv_num1);
		tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);
		tv_info_tip = (TextView) findViewById(R.id.tv_info_tip);
		tv_name = (TextView) findViewById(R.id.tv_name2);
		tv_yong = (TextView) findViewById(R.id.tv_yong2);
		tv_scheme = (TextView) findViewById(R.id.tv_scheme2);
		tv_buyCount = (TextView) findViewById(R.id.tv_buy2);
		tv_title = (TextView) findViewById(R.id.tv_schemetitle2);
		tv_content = (TextView) findViewById(R.id.tv_schemeContent2);
		tv_bei = (TextView) findViewById(R.id.tv_bei);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		mListView = (MyListView2) findViewById(R.id.lv_betInfo);

		gv_winNumber = (MyGridView) findViewById(R.id.gv_winNumber);

		rl_join1 = (RelativeLayout) findViewById(R.id.rl_joinInfo);
		rl_join2 = (RelativeLayout) findViewById(R.id.rl_joinInfo2);

		btn_jixu = (Button) findViewById(R.id.btn_jixu);
		btn_touzhu = (Button) findViewById(R.id.btn_touzhu);

		betinfo_hide_btn = (ImageButton) findViewById(R.id.betinfo_hide_btn);
		ll_numberCount = (LinearLayout) findViewById(R.id.ll_numberCount);
		ll_divider = (ImageView) findViewById(R.id.ll_divider);
		btn_back = (ImageButton) findViewById(R.id.btn_back);

		tv_secretMsg = (TextView) findViewById(R.id.tv_secretMsg);
		gv_winNumber.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
	}

	/**
	 * 设置开奖号码格式
	 */
	private void setWinNumber() {

		if (scheme.getLotteryID() == null)
			return;
		if (null == scheme.getWinNumber())
			return;
		Log.i("x", "彩种id   " + scheme.getLotteryID());
		Spanned number;
		String temp_win = scheme.getWinNumber().replaceAll("\\s?[\\+-]\\s?",
				"+");
		if (!temp_win.contains("+")) {
			number = Html.fromHtml("<font color='#EB1827'>"
					+ scheme.getWinNumber() + "</FONT>");
			tv_lotteryNum.setText(number);
			return;
		}

		String red = temp_win.split("\\+")[0];
		String blue = "";
		if (temp_win.split("\\+").length == 2)
			blue = temp_win.split("\\+")[1];

		number = Html.fromHtml("<font color='#EB1827'>" + red + "</FONT>"
				+ "<font color='#4060ff'>" + " " + blue + "</FONT>");
		tv_lotteryNum.setText(number);
	}

	/**
	 * 给控件赋值
	 */
	private void initView() {
		btn_back.setOnClickListener(this);
		betinfo_hide_btn.setOnClickListener(this);
		ll_numberCount.setOnClickListener(this);
		btn_jixu.setOnClickListener(this);
		btn_touzhu.setOnClickListener(this);

		if (null == scheme)
			return;
		/** 合买订单状态 */
		if ("false".equalsIgnoreCase(scheme.getIsPurchasing())) {
			tv_info_tip.setText("合买投注详情");
		} else if ("true".equalsIgnoreCase(scheme.getIsPurchasing())) {
			/** 普通订单状态 */
			if (scheme.getIsChase() == 0) {
				tv_info_tip.setText("普通投注详情");
			} else {
				/** 追号订单状态 */
				tv_info_tip.setText("追号投注详情");
			}
		}
		rl_join1.setVisibility(View.GONE);
		rl_join2.setVisibility(View.GONE);
		if ("74".equals(scheme.getLotteryID())
				|| "75".equals(scheme.getLotteryID())) {
			btn_jixu.setVisibility(View.GONE);
		} else
			btn_jixu.setVisibility(View.VISIBLE);
		btn_touzhu.setText(LotteryUtils.getTitleText(scheme.getLotteryID())
				+ "投注");
		tv_lotteryName
				.setText(LotteryUtils.getTitleText(scheme.getLotteryID()));

		tv_lotteryName_issue.setText(scheme.getIsuseName() == null ? ""
				: scheme.getIsuseName() + "期");
		if (AppTools.allLotteryLogo == null) {
			AppTools.setLogo();
		}
		img_logo.setBackgroundResource(AppTools.allLotteryLogo.get(scheme
				.getLotteryID()));
		tv_money.setText(scheme.getMoney() + "元");
		tv_time.setText(scheme.getDateTime());
		tv_orderId.setText(scheme.getSchemeNumber());

		if (scheme.getFromClient() == 1)
			tv_betType.setText("网页投注");
		else if (scheme.getFromClient() == 2)
			tv_betType.setText("手机APP投注");

		tv_winMoney.setText("--");
		tv_count.setText(scheme.getSecretMsg());
		tv_bei.setVisibility(View.GONE);
		String status = scheme.getSchemeStatus();
		// 将设置中奖号码的方法提出来
		if (scheme.getSchemeIsOpened().equals("True")) {
			setWinNumber();
		}
		if (status.equals("已中奖")) {
			tv_state.setTextColor(Color.RED);
			tv_state.setText("中奖");
			setWinNumber();
			tv_winMoney.setText(scheme.getWinMoneyNoWithTax() + "元");
		} else {
			tv_state.setText(status);
		}

		if (0 != scheme.getQuashStatus()
				|| (scheme.getQuashStatus() + "").length() == 0) {
			// tv_state.setText("已撤单");
			setWinNumber();
		} else {
			// 合买
			if (scheme.getIsPurchasing().equalsIgnoreCase("False")) {
				rl_join1.setVisibility(View.VISIBLE);
				rl_join2.setVisibility(View.VISIBLE);

				tv_name.setText(scheme.getInitiateName());
				tv_yong.setText((int) (scheme.getSchemeBonusScale() * 100)
						+ "%");
				tv_scheme.setText(scheme.getShare() + "份,共" + scheme.getMoney()
						+ "元,每份" + scheme.getShareMoney() + "元");
				tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
						+ scheme.getMyBuyMoney() + "元");
				tv_title.setText(scheme.getTitle());
				tv_content.setText(scheme.getDescription());
			}
		}
		String numbers = scheme.getLotteryNumber();
	}

	@Override
	protected void onResume() {
		super.onResume();
		scrollView.smoothScrollTo(0, 0);
	}

	/**
	 * 公用点击监听
	 */
	@Override
	public void onClick(View arg0) {
		String lotteryID = scheme.getLotteryID();
		switch (arg0.getId()) {
		case R.id.btn_jixu:// 继续本次号码投注
			goToBetLottery(lotteryID);
			break;
		case R.id.btn_touzhu:// 去往本彩种投注
			LotteryUtils.goToSelectLottery(this, lotteryID);
			break;
		case R.id.betinfo_hide_btn:
		case R.id.ll_numberCount:
			if (!betinfo_hide_btn.isSelected()) {
				betinfo_hide_btn.setSelected(true);
				ll_divider.setVisibility(View.GONE);

			} else {
				betinfo_hide_btn.setSelected(false);
				ll_divider.setVisibility(View.VISIBLE);

			}
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 继续投注：根据彩种id跳转不同投注页面
	 * 
	 * @param lotteryID
	 *            ： 彩种id
	 */
	private void goToBetLottery(String lotteryID) {
		if (TextUtils.isEmpty(lotteryID)) {
			MyToast.getToast(MyCommonLotteryInfo_join.this, "lotteryID 为空")
					;
			return;
		}
		int id = Integer.parseInt(lotteryID);
		Intent intent;
		if (!NumberTools.changeSchemesToSelectedNumbers(scheme)) {
			MyToast.getToast(MyCommonLotteryInfo_join.this, "内容记录错误");
			return;
		}
		Lottery mLottery = AppTools.getLotteryById(lotteryID);
		if (mLottery != null) {
			AppTools.lottery = mLottery;
		} else {
			MyToast.getToast(this, "该奖期已结束，请等下一期");
			return;
		}
		switch (id) {
		case 5:// 双色球
			intent = new Intent(getApplicationContext(), Bet_SSQ_Activity.class);
			MyCommonLotteryInfo_join.this.startActivity(intent);
			break;
		case 39:// 大乐透
			intent = new Intent(getApplicationContext(), Bet_DLT_Activity.class);
			MyCommonLotteryInfo_join.this.startActivity(intent);
			break;
		case 78:
		case 62:// 十一运夺金
		case 70:// 11选5
			intent = new Intent(getApplicationContext(),
					Bet_11x5_Activity.class);
			MyCommonLotteryInfo_join.this.startActivity(intent);
			break;
		
		case 28:// 时时彩
			intent = new Intent(getApplicationContext(), Bet_SSC_Activity.class);
			MyCommonLotteryInfo_join.this.startActivity(intent);
			break;
		case 83:// 江苏快3
			intent = new Intent(getApplicationContext(), Bet_k3_Activity.class);
			MyCommonLotteryInfo_join.this.startActivity(intent);
			break;
		}
	}

}
