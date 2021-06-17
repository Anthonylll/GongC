package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.adapter.MyFollowNumberAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：个人中心的普通彩种的合买详情类
 * 
 * @author lenovo
 * 
 */
public class MyCommonLotteryInfo_joindetail extends Activity implements
		OnClickListener {
	private ScrollView scrollView;
	private TextView tv_lotteryName, tv_money, tv_state, tv_winMoney, tv_count,
			tv_time, tv_orderId, tv_tishi, tv_name, tv_yong, tv_buyCount,
			tv_title, tv_content, tv_bei, tv_lotteryName_issue, tv_info_tip;
	private ImageView img_logo;
	private ImageButton btn_back;
	private TextView info_tv_totalMoney;

	private Schemes scheme;
	private String[] numbers;
	private List<String> show_numbers;
	private List<Integer> max;

	private int pageindex = 1;
	private int pagesize = 10;

	private Button btn_jixu;
	private Button btn_touzhu;
	private TextView footer;
	private int temp_length = 0;

	private ImageButton ib_schemeinfo, ib_betinfo;
	private LinearLayout ll_schemeInfo, ll_betInfo;
	private LinearLayout btn_info;// 认购列表

	private MyListView2 listView;
	private MyFollowNumberAdapter fAdapter;
	private TextView tv_touzhu;// 显示注数
	private LinearLayout ll_betinfo_detail, ll_follow1, ll_follow2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_betlottey_join);
		App.activityS.add(this);
		scheme = (Schemes) getIntent().getSerializableExtra("scheme");
		findView();
		initView();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		ll_betinfo_detail = (LinearLayout) this
				.findViewById(R.id.ll_betinfo_detail);

		ib_schemeinfo = (ImageButton) this.findViewById(R.id.ib_follow1);
		ll_follow1 = (LinearLayout) this.findViewById(R.id.ll_follow1);
		ll_follow2 = (LinearLayout) this.findViewById(R.id.ll_follow2);

		ib_betinfo = (ImageButton) this.findViewById(R.id.ib_follow2);
		ll_schemeInfo = (LinearLayout) this.findViewById(R.id.ll_schemeInfo);
		ll_betInfo = (LinearLayout) this.findViewById(R.id.ll_betInfo);

		btn_info = (LinearLayout) this.findViewById(R.id.btn_numberInfo);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		tv_lotteryName = (TextView) findViewById(R.id.tv_lotteryName);
		tv_money = (TextView) findViewById(R.id.tv_money2);
		info_tv_totalMoney = (TextView) findViewById(R.id.info_tv_totalMoney);
		tv_state = (TextView) findViewById(R.id.tv_state2);
		tv_winMoney = (TextView) findViewById(R.id.tv_winMoney2);
		tv_count = (TextView) findViewById(R.id.tv_numberCount);
		tv_time = (TextView) findViewById(R.id.tv_time2);
		tv_orderId = (TextView) findViewById(R.id.tv_orderId2);
		tv_tishi = (TextView) findViewById(R.id.tv_wShow2);
		tv_lotteryName_issue = (TextView) findViewById(R.id.tv_lotteryName_issue);
		tv_info_tip = (TextView) findViewById(R.id.tv_info_tip);
		tv_name = (TextView) findViewById(R.id.tv_name2);
		tv_yong = (TextView) findViewById(R.id.tv_yong2);
		tv_buyCount = (TextView) findViewById(R.id.tv_buy2);
		tv_title = (TextView) findViewById(R.id.tv_schemetitle2);
		tv_content = (TextView) findViewById(R.id.tv_schemeContent2);
		// tv_bei = (TextView) findViewById(R.id.tv_bei);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		btn_jixu = (Button) findViewById(R.id.btn_jixu);
		btn_touzhu = (Button) findViewById(R.id.btn_touzhu);
		btn_back = (ImageButton) findViewById(R.id.btn_back);

		tv_touzhu = (TextView) this.findViewById(R.id.tv_touzhu);
		tv_touzhu.setText("投注号码");
		listView = (MyListView2) this.findViewById(R.id.followinfo_jc_listView);
		fAdapter = new MyFollowNumberAdapter(this, scheme.getBuyContent(),
				scheme.getMultiple());
		listView.setAdapter(fAdapter);
		fAdapter.notifyDataSetChanged();
	}

	/**
	 * 给控件赋值
	 */
	private void initView() {
		btn_back.setOnClickListener(this);
		btn_jixu.setOnClickListener(this);
		btn_touzhu.setOnClickListener(this);

		ib_schemeinfo.setOnClickListener(this);
		ib_betinfo.setOnClickListener(this);
		ll_follow1.setOnClickListener(this);
		ll_follow2.setOnClickListener(this);
		btn_info.setOnClickListener(this);

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

		if ("72".equals(scheme.getLotteryID())
				|| "73".equals(scheme.getLotteryID())
				|| "45".equals(scheme.getLotteryID())
				|| "74".equals(scheme.getLotteryID())
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
		tv_money.setText(scheme.getMyBuyMoney() + "元");
		info_tv_totalMoney.setText(scheme.getMoney() + "元");

		tv_time.setText(scheme.getDateTime());
		tv_orderId.setText(scheme.getSchemeNumber());

		tv_count.setText(scheme.getSecretMsg());
		// tv_bei.setVisibility(View.GONE);
		String status = scheme.getSchemeStatus();
		// 将设置中奖号码的方法提出来
		if (status.equals("已中奖")) {
			tv_state.setTextColor(Color.RED);
			tv_state.setText("中奖");
//			tv_winMoney.setText(scheme.getWinMoneyNoWithTax() + "元");
			tv_winMoney.setText(scheme.getShareWinMoney() + "元");
		} else {
			tv_state.setText(status);
		}

		if (0 != scheme.getQuashStatus()
				|| (scheme.getQuashStatus() + "").length() == 0) {
			// tv_state.setText("已撤单");
			tv_name.setText(scheme.getInitiateName());
			tv_yong.setText((int) (scheme.getSchemeBonusScale() * 100) + "%");
//			tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
//					+ scheme.getMyBuyMoney() + "元");
			tv_buyCount.setText("点击右侧查看");
			tv_title.setText(scheme.getTitle());
			tv_content.setText(scheme.getDescription());
		} else {
			// 合买
			if (scheme.getIsPurchasing().equalsIgnoreCase("False")) {

				tv_name.setText(scheme.getInitiateName());
				tv_yong.setText((int) (scheme.getSchemeBonusScale() * 100)
						+ "%");
//				tv_buyCount.setText(scheme.getMyBuyShare() + "份  共"
//						+ scheme.getMyBuyMoney() + "元");
				tv_buyCount.setText("点击右侧查看");
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
	 * 公用监听方法
	 */
	@Override
	public void onClick(View arg0) {
		String lotteryID = scheme.getLotteryID();
		switch (arg0.getId()) {
		case R.id.btn_jixu:// 继续本次号码投注
			goToBetLottery(lotteryID);
			break;
		case R.id.btn_touzhu:// 去往本彩种投注
			if (lotteryID.equals("72") || lotteryID.equals("73")
					|| lotteryID.equals("45")) {
				goToSelectLottery(lotteryID);
			} else {
				LotteryUtils.goToSelectLottery(this, lotteryID);
			}
			break;

		/** 点击方案信息的下拉列表 **/
		case R.id.ib_follow1:
		case R.id.ll_follow1:
			if (!ib_schemeinfo.isSelected()) {
				ib_schemeinfo.setSelected(true);
				ll_schemeInfo.setVisibility(View.GONE);

			} else {
				ib_schemeinfo.setSelected(false);
				ll_schemeInfo.setVisibility(View.VISIBLE);
			}
			break;

		/** 查看认购列表详情 **/
		case R.id.btn_numberInfo:
			Intent intent = new Intent(MyCommonLotteryInfo_joindetail.this,
					FollowPurchase_Activity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("schem", scheme);
			intent.putExtra("bundle", bundle);

			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;

		/** 点击投注信息的下拉列表 **/
		case R.id.ib_follow2:
		case R.id.ll_follow2:
			if (!ib_betinfo.isSelected()) {
				ib_betinfo.setSelected(true);
				ll_betinfo_detail.setVisibility(View.GONE);

			} else {
				ib_betinfo.setSelected(false);
				ll_betinfo_detail.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 去各个的彩种类投注
	 * 
	 * @param lotteryID
	 *            ：彩种id
	 */
	private void goToSelectLottery(String lotteryID) {
		int id = Integer.parseInt(lotteryID);
		Intent intent = null;
		switch (id) {
		case 72:// 竞彩足球
		case 73:// 竞彩篮球
		case 45:
			long currentTime_jc = System.currentTimeMillis();
			for (int i = 0; i < HallFragment.listLottery.size(); i++) {
				if ("72".equals(HallFragment.listLottery.get(i).getLotteryID())
						|| "73".equals(HallFragment.listLottery.get(i)
								.getLotteryID())
						|| "45".equals(HallFragment.listLottery.get(i)
								.getLotteryID())) {
					if (HallFragment.listLottery.get(i).getDistanceTime()
							- currentTime_jc <= 0) {
						MyToast.getToast(getApplicationContext(),
								"该奖期已结束，请等下一期");
						return;
					}
					AppTools.lottery = HallFragment.listLottery.get(i);

					HallFragment.refreType = true;
					// 对阵信息是否为空
					if (AppTools.lottery.getDtmatch() != null
							&& AppTools.lottery.getDtmatch().length() != 0) {
						if (72 == id
								&& "72".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩足球
							new HallFragment().getBallData(
									MyCommonLotteryInfo_joindetail.this, 0);
							break;
						} else if (73 == id
								&& "73".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩篮球
							new HallFragment().getBasketball(
									MyCommonLotteryInfo_joindetail.this, 0);
							break;
						}
					} else {
						MyToast.getToast(MyCommonLotteryInfo_joindetail.this,
								"没有对阵信息");
					}
				}
			}
		}
	}

	/**
	 * 根据彩种id跳转不同投注页面
	 * 
	 * @param lotteryID
	 *            ： 彩种id
	 */
	private void goToBetLottery(String lotteryID) {
		System.out.println("彩种id-----" + lotteryID);
		System.out.println("玩法id-----" + scheme.getPlayTypeID());
		if (TextUtils.isEmpty(lotteryID)) {
			MyToast.getToast(MyCommonLotteryInfo_joindetail.this,
					"lotteryID 为空");
			return;
		}
		int id = Integer.parseInt(lotteryID);
		Intent intent;
		if (!NumberTools.changeSchemesToSelectedNumbers(scheme)) {
			MyToast.getToast(MyCommonLotteryInfo_joindetail.this, "内容记录错误")
					;
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
			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;
		case 39:// 大乐透
			intent = new Intent(getApplicationContext(), Bet_DLT_Activity.class);
			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;
		case 78:
		case 62:// 十一运夺金
			intent = new Intent(getApplicationContext(),
					Bet_11x5_Activity.class);
			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;
		case 70:// 11选5
			intent = new Intent(getApplicationContext(),
					Bet_11x5_Activity.class);
			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;
		case 28:// 时时彩
			intent = new Intent(getApplicationContext(), Bet_SSC_Activity.class);
			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;
		case 83:// 江苏快3
			intent = new Intent(getApplicationContext(), Bet_k3_Activity.class);
			MyCommonLotteryInfo_joindetail.this.startActivity(intent);
			break;
		}
	}

}
