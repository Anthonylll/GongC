package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.dataaccess.ShowDtMatch;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：投注成功显示的类
 * 
 * @author lenovo
 * 
 */
public class PaySuccessActivity extends Activity implements OnClickListener {
	private static final String TAG = "PaySuccessActivity";
	private Context context = PaySuccessActivity.this;
	private Button btn_select_schemeinfo;// 订单详情
	private Button btn_continue_bet;// 继续投注
	private Button btn_back_tohall;// 返回首页
	private String lotteryId;// 彩种id
	private int schemeId;// 方案id
	// private ProgressDialog pd;

	private MyHandler myHandler;
	private long paymoney;// 花费的钱
	private String balance;// 余额

	private String JinECost; // 金额消费
	private String CaiJinCost;// 彩金消费
	private String voucherMoney;// 优惠金额
	private String reMainCaiJin;// 剩余彩金

	private TextView tvJinECost;

	private TextView tvCaiJinCost;
	private TextView tvBalance; // 剩余金额
	private TextView tvremainCaijin;// 剩余彩金
	private TextView tv_coupon;
	Schemes scheme;
	private boolean isJoin;
	/**
	 * 竞彩
	 */
	private List<ShowDtMatch> list_show;
	private int passtype;
	private int passtype_lanqiu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paysuc);
		App.activityS.add(this);
		findView();
		setListener();
		init();
	}

	/**
	 * 初始化UI控件
	 */
	public void findView() {
		btn_select_schemeinfo = (Button) findViewById(R.id.btn_select_schemeinfo);
		btn_continue_bet = (Button) findViewById(R.id.btn_continue_bet);
		btn_back_tohall = (Button) findViewById(R.id.btn_back_tohall);
		// 新增控件
		tvJinECost = (TextView) findViewById(R.id.tv_successJinE);
		tvCaiJinCost = (TextView) findViewById(R.id.tv_successCaiJin);
		tvBalance = (TextView) findViewById(R.id.tv_remainBalance);
		tvremainCaijin = (TextView) findViewById(R.id.tv_remainCaijin);
		tv_coupon = (TextView) findViewById(R.id.tv_coupon);
		scheme = new Schemes();
	}

	/**
	 * 绑定页面监听
	 */
	public void setListener() {
		btn_select_schemeinfo.setOnClickListener(this);
		btn_continue_bet.setOnClickListener(this);
		btn_back_tohall.setOnClickListener(this);
	}

	/**
	 * 初始化数据，给控件赋值
	 */
	public void init() {
		Log.i(TAG, HallFragment.listLottery + "");
		myHandler = new MyHandler();
		lotteryId = AppTools.lottery.getLotteryID();// 获取彩种id
		schemeId = AppTools.schemeId;// 获取方案id
		paymoney = getIntent().getLongExtra("paymoney", 0);// 获取花费钱
		JinECost = getIntent().getStringExtra("currentMoeny");
		voucherMoney = getIntent().getStringExtra("voucherMoney");
		passtype = getIntent().getIntExtra("passtype", 0);// 获取竞彩足球的投注方式，是单关还是其他
		passtype_lanqiu = getIntent().getIntExtra("passtype_lanqiu", 0);// 获取竞彩篮球的投注方式，是单关还是其他
		if (JinECost == null) {

			tvJinECost.setText("0.00");
		} else {
			double amount = Double.valueOf(JinECost)-Double.valueOf(voucherMoney);
			tvJinECost.setText(amount+"");
		}
		if (voucherMoney == null) {
			tv_coupon.setText("0.00");
		} else {

			tv_coupon.setText(voucherMoney);
		}

		CaiJinCost = getIntent().getStringExtra("currentHandsel");

		if (CaiJinCost == null) {

			tvCaiJinCost.setText("0.00");

		} else {

			tvCaiJinCost.setText(CaiJinCost);
		}

		reMainCaiJin = getIntent().getStringExtra("handselMoney");
		if (reMainCaiJin == null) {
			tvremainCaijin.setText("0.00");
		} else {

			tvremainCaijin.setText(reMainCaiJin);
		}
		isJoin = getIntent().getBooleanExtra("isJoin", true);// 获取是否是合买
		balance = AppTools.user.getBalance();// 获取方案id
		tvBalance.setText(balance);
	}

	/**
	 * 公用点击监听
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_select_schemeinfo:// 订单详情
			Log.i(TAG, "期号" + AppTools.lottery.getIsuseId());
			Log.i(TAG, "彩种id" + AppTools.lottery.getLotteryID());
			if (AppTools.qi > 1) {// 追号
				AppTools.lottery.setIsChase(1);
				if (TextUtils.isEmpty(AppTools.lottery.getLotteryID())) {
					MyToast.getToast(context, "LotteryID为空");
					return;
				}
				if (TextUtils.isEmpty(AppTools.lottery.getChaseTaskID() + "")) {
					MyToast.getToast(context, "ChaseTaskID为空");
					return;
				}
				scheme.setIsuseID(AppTools.lottery.getIsuseId());
				scheme.setIsuseName(AppTools.lottery.getIsuseName());
				scheme.setLotteryID(AppTools.lottery.getLotteryID());
				scheme.setLotteryName(AppTools.lottery.getLotteryName());
				scheme.setLotteryName("");
				scheme.setIsPurchasing("true");
				scheme.setIsChase(AppTools.lottery.getIsChase());
				scheme.setMultiple(AppTools.bei);
				scheme.setMoney((double) paymoney);
				scheme.setChaseTaskID(AppTools.lottery.getChaseTaskID());
				scheme.setSchemeIsOpened("False");
				scheme.setQuashStatus(0);
				scheme.setSchemeStatus("未出票");
				Intent intent = new Intent(context, MyFollowLotteryInfo.class);
				intent.putExtra("scheme", scheme);
				context.startActivity(intent);
				finish();
			} else {
				AppTools.lottery.setIsChase(0);
				getBetDetailnfo();
				// myAsynTask = new MyAsynTask();
				// myAsynTask.execute();
			}
			break;
		case R.id.btn_continue_bet:// 继续投注
			goToSelectLottery();
			break;
		case R.id.btn_back_tohall:// 返回首页
			backToHall();
			break;
		}
	}

	/**
	 * 重写返回键监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToHall();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 提交查看投注详情请求
	 */
	public void getBetDetailnfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"数据加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				String error = "-1";
				if (RequestUtil.DEBUG)
					Log.i(TAG, "投注详情请求结果==》" + item);

				if (null == item)
					error = "-1";
				try {
					error = item.optString("error");
					if ("0".equals(item.optString("error"))) {
						String schemeList = item.optString("schemeList");
						JSONArray array = new JSONArray(schemeList);
						JSONArray jsonArray2 = new JSONArray(array.toString());
						if (jsonArray2.length() == 0)
							error = "-1";
						// 如果取消了 则停止
						// if (this.isCancelled()) {
						// Log.i("x", "取消了异步。。。。");
						// return null;
						// }
						JSONObject items2 = jsonArray2.getJSONObject(0);
						scheme.setId(items2.optString("id"));
						// 设置订单编号
						String schemeNum = items2.optString("schemeNumber");
						scheme.setSchemeNumber(schemeNum.equals("") ? LotteryUtils
								.getSchemeNum(lotteryId,
										AppTools.lottery.getIsuseName(),
										schemeId) : schemeNum);
						scheme.setIsHide(items2.optInt("isHide"));
						scheme.setSecretMsg(items2.optString("secretMsg"));
						scheme.setAssureMoney(items2.optDouble("assureMoney"));
						scheme.setAssureShare(items2.optInt("assureShare"));
						scheme.setBuyed(items2.optString("buyed"));
						scheme.setInitiateName(items2.optString("initiateName"));
						scheme.setInitiateUserID(items2
								.optString("initiateUserID"));
						String isPurchasing = items2.optString("isPurchasing");
						if ("".equals(isPurchasing)) {
							isPurchasing = isJoin + "";
						}
						scheme.setIsPurchasing(isPurchasing);

						// 设置订单期号
						scheme.setIsuseID(AppTools.lottery.getIsuseId());
						scheme.setIsuseName(AppTools.lottery.getIsuseName());

						scheme.setLevel(items2.optInt("level"));
						scheme.setLotteryID(items2.optString("lotteryID"));
						scheme.setLotteryName(items2.optString("lotteryName"));
						scheme.setLotteryNumber(items2
								.optString("lotteryNumber"));
						scheme.setMoney(items2.optInt("money"));
						scheme.setPlayTypeID(items2.optInt("playTypeID"));
						scheme.setPlayTypeName(items2.optString("playTypeName"));
						scheme.setQuashStatus(items2.optInt("quashStatus"));
						scheme.setRecordCount(items2.optInt("recordCount"));
						scheme.setSchedule(items2.optInt("schedule"));
						scheme.setSchemeBonusScale(items2
								.optDouble("schemeBonusScale"));
						scheme.setSchemeIsOpened(items2
								.optString("schemeIsOpened"));
						scheme.setSecrecyLevel(items2.optInt("secrecyLevel"));
						scheme.setSerialNumber(items2.optInt("serialNumber"));
						scheme.setShare(items2.optInt("share"));
						scheme.setShareMoney(items2.optInt("shareMoney"));
						scheme.setSurplusShare(items2.optInt("surplusShare"));
						scheme.setTitle(items2.optString("title"));
						scheme.setWinMoneyNoWithTax(items2
								.optInt("winMoneyNoWithTax"));
						scheme.setWinNumber(items2.optString("winNumber"));

						// 设置订单下单时间
						scheme.setDateTime(LotteryUtils.getSchemeDetailTime(System
								.currentTimeMillis()));

						scheme.setDescription(items2.optString("description"));
						scheme.setIsChase(items2.optInt("isChase"));
						scheme.setChaseTaskID(items2.optInt("chaseTaskID"));
						// 设置倍数
						scheme.setMultiple(AppTools.bei);

						scheme.setFromClient(items2.optInt("fromClient"));
						scheme.setMyBuyMoney(item.optInt("myBuyMoney") + "");
						scheme.setMyBuyShare(item.optInt("myBuyShare"));
						String schemeStatus = "未出票";
						try {
							schemeStatus = items2.getString("schemeStatus");
						} catch (JSONException e) {
							// do nothing
						}
						scheme.setSchemeStatus(schemeStatus);

						JSONArray array_contents = new JSONArray(
								items2.optString("buyContent"));
						List<LotteryContent> contents = new ArrayList<LotteryContent>();
						LotteryContent lotteryContent;
						for (int k = 0; k < array_contents.length(); k++) {
							lotteryContent = new LotteryContent();
							try {
								JSONArray array2 = new JSONArray(
										array_contents.optString(k));

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
										array_contents.optString(k));

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

						// if (this.isCancelled()) {
						// pd.dismiss();
						// Log.i("x", "取消了异步。。。。");
						// return null;
						// }
					}
				} catch (Exception e) {
					Log.i("x", "myAllLottery 错误" + e.getMessage());
					error = "-1";
				}
				myHandler.sendEmptyMessage(Integer.parseInt(error));

				if (item.toString().equals("-500")) {
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
		requestUtil.commit_betDetailInfo(schemeId);
	}

	/**
	 * 进行页面刷新
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Intent intent_info;
				if ("False".equalsIgnoreCase(scheme.getIsPurchasing())) {
					if ("72".equals(scheme.getLotteryID())
							|| "73".equals(scheme.getLotteryID())
							|| "45".equals(scheme.getLotteryID())) {
						intent_info = new Intent(context,
								MyCommonLotteryInfo_joindetail_jc.class);
					} else {
						intent_info = new Intent(context,
								MyCommonLotteryInfo_joindetail.class);
					}

				} else {
					if ("72".equals(scheme.getLotteryID())
							|| "73".equals(scheme.getLotteryID())
							|| "45".equals(scheme.getLotteryID()))
						intent_info = new Intent(context,
								MyCommonLotteryInfo_jc.class);
					else
						intent_info = new Intent(context,
								MyCommonLotteryInfo.class);
				}
				intent_info.putExtra("scheme", scheme);
				context.startActivity(intent_info);
				break;

			case -1:
				MyToast.getToast(getApplicationContext(), "没有数据");
				break;
			case -500:
				MyToast.getToast(getApplicationContext(), "连接超时");
				break;
			case 100:
				backToHall();
				Intent intent = new Intent(PaySuccessActivity.this,
						Select_JCZQ_Activity.class);
				PaySuccessActivity.this.startActivity(intent);
				break;
			case 110:
				intent = new Intent(PaySuccessActivity.this,
						Select_JCLQ_Activity.class);
				intent.putExtra("from", "pay_success");
				PaySuccessActivity.this.startActivity(intent);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 返回首页
	 */
	public void backToHall() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	/**
	 * 根据彩种id跳转不同选号页面
	 */
	private void goToSelectLottery() {
		Intent intent = null;
		int lotteryId = Integer.parseInt(this.lotteryId);

		switch (lotteryId) {
		case 5:// 双色球
			long currentTime_ssq = System.currentTimeMillis();
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					if (lottery.getDistanceTime() - currentTime_ssq <= 0) {
						MyToast.getToast(PaySuccessActivity.this,
								"该奖期已结束，请等下一期");
						return;
					}
				}
			}
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					AppTools.lottery = lottery;
					intent = new Intent(PaySuccessActivity.this,
							Select_SSQ_Activity.class);
				}
			}
			PaySuccessActivity.this.startActivity(intent);
			PaySuccessActivity.this.finish();
			break;
		case 39:// 大乐透
			long currentTime_dlt = System.currentTimeMillis();
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					if (lottery.getDistanceTime() - currentTime_dlt <= 0) {
						MyToast.getToast(PaySuccessActivity.this,
								"该奖期已结束，请等下一期");
						return;
					}
				}
			}
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					AppTools.lottery = lottery;
				}
			}
			intent = new Intent(getApplicationContext(),
					Select_DLT_Activity.class);
			PaySuccessActivity.this.startActivity(intent);
			PaySuccessActivity.this.finish();

			break;
		case 75:// 任选九
		case 74:// 胜负彩
			long currentTime_sfc = System.currentTimeMillis();
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					if (lottery.getDistanceTime() - currentTime_sfc <= 0) {
						MyToast.getToast(PaySuccessActivity.this,
								"该奖期已结束，请等下一期");
						return;
					}
				}
			}
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					AppTools.lottery = lottery;
				}
			}
			if (AppTools.lottery.getLotteryID().equals("74")) {
				intent = new Intent(PaySuccessActivity.this,
						Select_SFC_Activity.class);
			} else if (AppTools.lottery.getLotteryID().equals("75")) {
				intent = new Intent(PaySuccessActivity.this,
						Select_RX9_Activity.class);
			}
			PaySuccessActivity.this.startActivity(intent);
			PaySuccessActivity.this.finish();
			break;
		case 62:// 十一运夺金
		case 70:// 江西11选5
		case 78:// 广东11选5
			long currentTime_11x5 = System.currentTimeMillis();
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					if (lottery.getDistanceTime() - currentTime_11x5 <= 0) {
						MyToast.getToast(PaySuccessActivity.this,
								"该奖期已结束，请等下一期");
						return;
					} else {
						AppTools.lottery = lottery;
					}
				}
			}
			intent = new Intent(PaySuccessActivity.this,
					Select_11x5_Activity.class);
			PaySuccessActivity.this.startActivity(intent);
			PaySuccessActivity.this.finish();
			break;
		case 28:// 重庆时时彩
			// backToHall();
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					AppTools.lottery = lottery;
				}
			}
			if ("28".equals(AppTools.lottery.getLotteryID())) {
				intent = new Intent(PaySuccessActivity.this,
						Select_SSC_Activity.class);
			} else {
				MyToast.getToast(PaySuccessActivity.this, "该奖期已结束，请等下一期")
						;
				break;
			}
			PaySuccessActivity.this.startActivity(intent);
			PaySuccessActivity.this.finish();
			break;

		case 83:// 江苏快3
			// backToHall();
			long currentTime_k3 = System.currentTimeMillis();
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals(lotteryId + "")) {
					if (lottery.getDistanceTime() - currentTime_k3 <= 0) {
						MyToast.getToast(PaySuccessActivity.this,
								"该奖期已结束，请等下一期");
						return;
					}
				}
			}
			for (Lottery lottery : HallFragment.listLottery) {
				if (lottery.getLotteryID().equals("83")) {
					AppTools.lottery = lottery;
				}
			}
			intent = new Intent(PaySuccessActivity.this,
					Select_K3_Activity.class);
			PaySuccessActivity.this.startActivity(intent);
			PaySuccessActivity.this.finish();
			break;

		case 72:// 竞彩足球
		case 73:// 竞彩篮球
		case 45:// 北京单场
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
						if (72 == lotteryId
								&& "72".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩足球
							Intent mIntent;
							if (passtype != 0) {
								mIntent = new Intent(context,
										Select_JCZQ_DAN_Activity.class);
							} else {
								mIntent = new Intent(context,
										Select_JCZQ_Activity.class);
							}
							mIntent.putExtra("isEmpty", true);
							startActivity(mIntent);
							PaySuccessActivity.this.finish();
							break;

						} else if (73 == lotteryId
								&& "73".equals(HallFragment.listLottery.get(i)
										.getLotteryID())) {// 竞彩篮球
							Intent mIntent;
							if (passtype_lanqiu != 0) {
								mIntent = new Intent(context,
										Select_JCLQ_DAN_Activity.class);
							} else {
								mIntent = new Intent(context,
										Select_JCLQ_Activity.class);
							}

							mIntent.putExtra("from", "pay_success_continue");
							startActivity(mIntent);
							PaySuccessActivity.this.finish();
							break;
						} 
					} else {
						MyToast.getToast(PaySuccessActivity.this, "没有对阵信息")
								;
					}
				}
			}
			break;
		}
	}

}
