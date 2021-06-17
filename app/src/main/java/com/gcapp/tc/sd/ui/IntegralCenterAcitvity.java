package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.fragment.IntegralFragment;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import org.json.JSONObject;

import butterknife.InjectView;

/**
 * 功能：积分中心类
 * 
 * @author lenovo
 * 
 */
public class IntegralCenterAcitvity extends FragmentActivity implements
		IntegralFragment.OnFragmentInteractionListener {
	private Context context = IntegralCenterAcitvity.this;
	private static final String TAG = "IntegralDetailActivity";
	private FragmentTransaction transaction;
	private String exchangeScale;

	@InjectView(R.id.btn_back)
	ImageButton btn_back;

	@InjectView(R.id.btn_integral_exchange)
	TextView btn_integral_exchange;

	@InjectView(R.id.btn_integral_detail)
	TextView btn_integral_detail;

	@InjectView(R.id.tv_integral_tip)
	TextView tv_integral_tip;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_integral_detail);
		init();
	}

	/**
	 * 跳转到积分明细
	 */
	protected void onclickDetail() {
		intent = new Intent(this, IntegralDetailActivity.class);
		this.startActivity(intent);
	}

	/**
	 * 跳转到积分明细
	 */
	public void onclickDetail(View view) {
		intent = new Intent(this, IntegralDetailActivity.class);
		this.startActivity(intent);
	}

	/**
	 * 兑换积分
	 */
	public void onclickExchange(View view) {
		intent = new Intent(this, IntegralExchangeActivity.class);
		this.startActivity(intent);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		// 初始化控件
		initView();
		RequestUtil requestUtil = new RequestUtil(context, false, 0, false,
				"正在加载...") {
			@Override
			public void responseCallback(JSONObject reponseJson) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "获取积分参数返回结果：" + reponseJson);
				String error = reponseJson.optString("error");
				if (null != error && error.equals("0")) {// 正常返回
					AppTools.currentScoring = reponseJson
							.optInt("currentScoring");
					AppTools.totalScoring = reponseJson.optInt("totalScoring");
					AppTools.totalConversionScoring = reponseJson
							.optInt("totalConversionScoring");
					AppTools.scoringExchangerate = reponseJson
							.optInt("scoringExchangerate");
					AppTools.optScoringOfSelfBuy = reponseJson
							.optInt("optScoringOfSelfBuy");
					exchangeScale = AppTools.scoringExchangerate + "";
				} else {
					AppTools.currentScoring = 0;
					AppTools.totalScoring = 0;
					AppTools.totalConversionScoring = 0;
					AppTools.scoringExchangerate = 0;
					AppTools.optScoringOfSelfBuy = 0;
					exchangeScale = AppTools.scoringExchangerate + "";
					String msg = reponseJson.optString("msg");
					if (RequestUtil.DEBUG)
						Log.e(TAG, "积分参数请求错误" + msg);
					MyToast.getToast(context, msg);
				}
				setParams();
			}

			@Override
			public void responseError(VolleyError error) {
				AppTools.currentScoring = 0;
				AppTools.totalScoring = 0;
				AppTools.totalConversionScoring = 0;
				AppTools.scoringExchangerate = 0;
				AppTools.optScoringOfSelfBuy = 0;
				exchangeScale = AppTools.scoringExchangerate + "";
				setParams();
				if (RequestUtil.DEBUG)
					Log.e(TAG, "积分参数请求错误" + error.getClass().getName() + "");
				MyToast.getToast(context, "数据请求错误，请稍后重试...");
			}
		};
		requestUtil.getIntegralParams();
		App.activityS.add(this);
		App.activityS1.add(this);
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);

		btn_integral_exchange = (TextView) findViewById(R.id.btn_integral_exchange);

		btn_integral_detail = (TextView) findViewById(R.id.btn_integral_detail);

		tv_integral_tip = (TextView) findViewById(R.id.tv_integral_tip);

	}

	/**
	 * 返回键监听
	 */
	protected void onclickBack() {
		for (Activity activity : App.activityS1) {
			activity.finish();
		}
	}

	/**
	 * 返回键监听
	 */
	public void onclickBack(View view) {
		for (Activity activity : App.activityS1) {
			activity.finish();
		}
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onclickBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 设置界面显示
	 */
	public void setParams() {
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.frame_integral_detail, IntegralFragment
				.newInstance(AppTools.currentScoring + "",
						AppTools.totalScoring + "",
						AppTools.totalConversionScoring + ""));
		transaction.commit();

		Spanned tip = Html
				.fromHtml("<font color='#888474'>1、</font>"
						+ "<font color='#000000'>我的积分</font><br/>"
						+ "<font color='#888474'>我的积分是对用户参与签到进行奖励的积分机制。</font><br/>"
						+ "<br/>"
//						+ "<font color='#888474'>2、</font>"
//						+ "<font color='#000000'>我的购彩积分:</font><br/>"
//						+ "    <font color='#888474'>在本站参与代购、合买彩票的用户，每成功购买满 </font> "
//
//						+ AppTools.changeStringColor("#eb1827", "1")
//						+ "    <font color='#888474'>元（撤单、方案未成功不积分），积</font> "
//
//						+ AppTools.changeStringColor("#eb1827",
//								AppTools.optScoringOfSelfBuy + "")
//						+ " <font color='#888474'>分，单次投注不满 </font>"
//
//						+ AppTools.changeStringColor("#eb1827", "1")
//						+ " <font color='#888474'>元不积分。</font><br/>"
//						+ "<br/>"
//
//						+ "<font color='#888474'>3、</font>"
//						+ "<font color='#000000'>我的中奖积分：</font><br/>"
//						+ "    <font color='#888474'>本站后台根据不同彩种以及不同玩法设置了一定的积分比例，代购或者参与合买的用户，将根据奖金的金额比例获得对应的积分。</font><br/>"
//						+ "<br/>"
						
						+ "<font color='#888474'>2、</font>"
						+ "<font color='#000000'>我的签到积分：</font><br/>"
						+ "    <font color='#888474'>每日签到可获得相应积分，保持连续签到习惯的用户可获得更多的积分；在'账户'页面右上角可进行签到。</font><br/>"
						+ "<br/>"

						+ "<font color='#888474'>3、</font>"
						+ "<font color='#000000'>积分兑换:</font><br/>"
						+ "    <font color='#888474'>积分满</font> "
						+ AppTools.changeStringColor("#eb1827", exchangeScale
								+ "")
						+ " <font color='#888474'>分，用户可以进行积分兑换，兑换以</font> "
						+ AppTools.changeStringColor("#eb1827", exchangeScale
								+ "")
						+ " <font color='#888474'>分为一个兑换单位，超过</font> "
						+ AppTools.changeStringColor("#eb1827", exchangeScale
								+ "")
						+ " <font color='#888474'>分兑换奖励的用户，兑换积分必须是</font> "
						+ AppTools.changeStringColor("#eb1827", exchangeScale
								+ "")
						+ " <font color='#888474'>的整数倍，不够</font> "
						+ AppTools.changeStringColor("#eb1827", exchangeScale
								+ "")
						+ " <font color='#888474'>分不能兑换。</font>"
						+ AppTools.changeStringColor("#eb1827", exchangeScale
								+ "")
						+ " <font color='#888474'>分兑换</font>"
						+ AppTools.changeStringColor("#eb1827", "1")
						+ " <font color='#888474'> 元，兑换后此款项自动增加到用户的可用资金中，但不能对积分兑换的金额进行提款。</font>");

		tv_integral_tip.setText(tip);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		onclickDetail();
	}
}
