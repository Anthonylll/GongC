package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.sd.ui.adapter.MyBetLotteryList11X5Adapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：11选5的投注页面
 */
public class Bet_11x5_Activity extends Activity implements OnClickListener {
	private Context context = Bet_11x5_Activity.this;
	private final static String TAG = "Bet_11x5Activity";
	/* 头部 */
//	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框
	/* 尾部 */
//	private TextView btn_follow; // 发起合买
	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 付款
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数
	private TextView coupon_text;
	private RelativeLayout coupon_layout;
	private LinearLayout btn_continue_select;// 继续投注
	private LinearLayout btn_automatic_select;// 机选1注
	private ImageView bet_btn_deleteall;// 清空
	private TextView bet_tv_guize; // 委托投注规则
	private MyListView2 bet_lv_scheme;// 自定义listview
	public MyBetLotteryList11X5Adapter adapter;
	private List<String> list;
	private Intent intent;
//	private int type;
	private int playType = 6201;
	private int play1, play2;
	private long sumCount, totalMoney; // 方案总注数 // 方案总金额
	private EditText et_bei, et_qi; // 用户输入的倍数 // 用户输入的期数
	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;
	private ScrollView bet_sv;
	private DecimalFormat format = new DecimalFormat("#####0.00");
	/** intent回传值*/
	private int Result_OK = 1;
	/** 优惠券Id*/
	private String voucherID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet);
		App.activityS.add(this);
		App.activityS1.add(this);
		if (AppTools.list_numbers == null)
			AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		findView();
		setListener();
		changeTextShow();
		init();
	}

	/**
	 * 初始化自定义控件
	 */
	private void findView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		coupon_text = (TextView) this.findViewById(R.id.coupon_text);
		coupon_layout = (RelativeLayout) this.findViewById(R.id.coupon_layout);
		btn_continue_select = (LinearLayout) this
				.findViewById(R.id.btn_continue_select);
		btn_automatic_select = (LinearLayout) this
				.findViewById(R.id.btn_automatic_select);
		bet_btn_deleteall = (ImageView) this
				.findViewById(R.id.bet_btn_deleteall);
//		btn_follow = (TextView) this.findViewById(R.id.btn_follow);
		et_bei = (EditText) this.findViewById(R.id.et_bei);
		et_qi = (EditText) this.findViewById(R.id.et_qi);

		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		bet_cb_stopfollow = (CheckBox) this
				.findViewById(R.id.bet_cb_stopfollow);

		bet_tv_guize = (TextView) this.findViewById(R.id.bet_tv_guize);
		bet_sv = (ScrollView) this.findViewById(R.id.bet_sv);

		bet_lv_scheme = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		adapter = new MyBetLotteryList11X5Adapter(Bet_11x5_Activity.this,
				AppTools.list_numbers);
		// 隐藏与显示
		btn_help.setVisibility(View.GONE);
		btn_clearall.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
//		if(AppTools.user != null) {
//			if(AppTools.user.getIsgreatMan().equals("True")) {
//				btn_follow.setVisibility(View.VISIBLE);
//			}
//		}
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		Bundle bundle = getIntent().getBundleExtra("bundle");
		/* 幸运彩十一运夺金 玩法传递 */
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0)
			playType = AppTools.list_numbers.get(0).getPlayType();
		setplay();
		if (null != bundle) {
			String one = bundle.getString("one");
			String two = bundle.getString("two");
			String three = bundle.getString("three");
			playType = bundle.getInt("playType");
			setplay();
			SelectedNumbers numbers = new SelectedNumbers();
			// 直选二
			if (playType == (play1 * 100 + 9)) {
				numbers.setShowLotteryNumber(one.replace(",", "") + ","
						+ two.replace(",", ""));
				numbers.setLotteryNumber(one.replace(",", "") + ","
						+ two.replace(",", ""));
			}
			// 前一
			else if (playType == (play1 * 100 + 1)) {
				numbers.setShowLotteryNumber(one.replace(",", " "));
				numbers.setLotteryNumber(one.replace(",", "\n"));
			}
			// 组二
			else if (playType == (play1 * 100 + 11)
					|| playType == (play1 * 100 + 12)) {
				numbers.setShowLotteryNumber(one.replace(" ", "").replace(",",
						" "));
				numbers.setLotteryNumber(one.replace(" ", "").replace(",", " "));
			} else if (playType == (play1 * 100 + 22)) {
				numbers.setShowLotteryNumber(one + " " + two);
				numbers.setLotteryNumber(one + " " + two);

			} else if (playType == (play1 * 100 + 23)) {
				numbers.setShowLotteryNumber(one + " " + two + " " + three);
				numbers.setLotteryNumber(one + " " + two + " " + three);
			} else if (playType >= (play1 * 100 + 24)) {
				numbers.setShowLotteryNumber(one.replace(" ", "").replace(",",
						" "));
				numbers.setLotteryNumber(one.replace(" ", "").replace(",", " "));
			}
			// 直选三
			else if (playType == (play1 * 100 + 10)) {
				numbers.setShowLotteryNumber(one.replace(",", "") + ","
						+ two.replace(",", "") + "," + three.replace(",", ""));
				numbers.setLotteryNumber(one.replace(",", "") + ","
						+ two.replace(",", "") + "," + three.replace(",", ""));
			}
			// 如果为胆拖的话
			else if (playType > (play1 * 100 + 12)
					&& playType < (play1 * 100 + 22)) {
				numbers.setShowLotteryNumber("(" + one.replace(",", "") + ")"
						+ two.replace(",", ""));
				numbers.setLotteryNumber(one.replace(",", "") + " , "
						+ two.replace(",", ""));
			} else {
				numbers.setShowLotteryNumber(one.replace(",", "") + " "
						+ two.replace(",", "") + " " + three.replace(",", ""));
				numbers.setLotteryNumber(one.replace(",", "") + " "
						+ two.replace(",", "") + " " + three.replace(",", ""));
			}

			numbers.setNumber(one.replace(",", "") + "-" + two.replace(",", "")
					+ "-" + three.replace(",", ""));
			numbers.setLotteryId(play1 + "");
			numbers.setPlayType(playType);
			numbers.setCount(AppTools.totalCount);
			numbers.setMoney(AppTools.totalCount * 2);
			AppTools.list_numbers.add(0, numbers);
			adapter.notifyDataSetChanged();
			changeTextShow();
			getIntent().replaceExtras(bundle);
		}
		if (play2 > 12 && play2 < 22) {
			btn_automatic_select.setVisibility(View.GONE);
		} else {
			btn_automatic_select.setVisibility(View.VISIBLE);
		}
		if (play1 == 70)
			btn_playtype.setText("江西11选5投注");
		else if (play1 == 62)
			btn_playtype.setText("十一运夺金投注");
		else if (play1 == 78)
			btn_playtype.setText("广东11选5投注");
		changeTextShow();
		isStopChase = 1;
		btn_submit.setText("付款");
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 得到区分不同11选5的玩法ID
	 */
	private void setplay() {
		play1 = playType / 100;
		play2 = playType % 100;
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		btn_continue_select.setOnClickListener(this);
		btn_automatic_select.setOnClickListener(this);
		bet_btn_deleteall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		coupon_layout.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		bet_lv_scheme.setAdapter(adapter);
		bet_lv_scheme.setOnItemClickListener(new listItemClick());
		et_bei.addTextChangedListener(bei_textWatcher);
		et_qi.addTextChangedListener(qi_textWatcher);
		bet_tv_guize.setOnClickListener(this);
		bet_cb_stopfollow.setOnCheckedChangeListener(new MyCheckChange());
	}

	/**
	 * 复选框 监听事件
	 */
	class MyCheckChange implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked)
				isStopChase = 1;
			else
				isStopChase = 0;
		}
	}

	/**
	 * listView 的 item 点击监听
	 */
	class listItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			SelectedNumbers num = AppTools.list_numbers.get(position);
			// 跳转页面并传值
			intent = new Intent(Bet_11x5_Activity.this,
					Select_11x5_Activity.class);
			Bundle bundle = new Bundle();
			AppTools.totalCount = num.getCount();

			bundle.putInt("playtype", num.getPlayType());

			String[] str = num.getNumber().split("-");

			ArrayList<String> listOne = new ArrayList<String>();

			for (String s : str[0].split(" ")) {
				listOne.add(s.trim());
			}
			bundle.putStringArrayList("oneSet", listOne);

			if (str.length > 1) {
				ArrayList<String> listTwo = new ArrayList<String>();
				for (String s : str[1].split(" ")) {
					listTwo.add(s.trim());
				}
				bundle.putStringArrayList("twoSet", listTwo);
			}
			if (str.length == 3) {
				if (str[2].length() > 1) {
					ArrayList<String> listThree = new ArrayList<String>();
					for (String s : str[2].split(" ")) {
						listThree.add(s.trim());
					}
					bundle.putStringArrayList("threeSet", listThree);
				}
			}
			// 将Bundle 放入Intent
			intent.putExtra("11X5Bundle", bundle);
			AppTools.list_numbers.remove(position);
			Bet_11x5_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 监听期号文本的值改变
	 */
	private TextWatcher qi_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > AppTools.lottery
						.getDtCanChaseIsuses().size()) {
					et_qi.setText(AppTools.lottery.getDtCanChaseIsuses().size()
							+ "");
					et_qi.setSelection(et_qi.getText().length());
					MyToast.getToast(getApplicationContext(), "最多可追"
							+ AppTools.lottery.getDtCanChaseIsuses().size()
							+ "期");
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					et_qi.setText("1");
					et_qi.setSelection(et_qi.getText().length());
					MyToast.getToast(getApplicationContext(), "至少可追1期");
				}
				if (edt.toString().substring(0, 1).equals("0")) {
					et_qi.setText(edt.toString().subSequence(1,
							edt.toString().length()));
					et_qi.setSelection(0);
				}
			}
			changeTextShow();
			setCursorPosition(et_qi);
		}
	};

	/**
	 * 监听倍数文本的值改变
	 */
	private TextWatcher bei_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if (edt.toString().trim().length() != 0) {
				if (Integer.parseInt(edt.toString().trim()) > 9999) {
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为9999");
					et_bei.setText("9999");
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					et_bei.setText("1");
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最小为1倍");
				}
				if (edt.toString().substring(0, 1).equals("0")) {
					et_bei.setText(edt.toString().subSequence(1,
							edt.toString().length()));
					et_bei.setSelection(0);
				}
			}
			changeTextShow();
			setCursorPosition(et_bei);
		}
	};

	/**
	 * 设置输入焦点
	 * 
	 * @param et
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text != null) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/**
	 * 点击监听事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 手选 **/
		case R.id.btn_continue_select:
			btn_handClick();
			break;
		/** 返回 **/
		case R.id.btn_back:
			backToPre();
			break;
		/** 机选 **/
		case R.id.btn_automatic_select:
			btn_randomClick();
			break;
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			if (null != AppTools.list_numbers
					&& 0 != AppTools.list_numbers.size()) {
				dialog.show();
				dialog.setDialogContent("是否清空投注单号码");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							if (AppTools.list_numbers != null) {
								AppTools.list_numbers.clear();
							}
							adapter.notifyDataSetChanged();
							changeTextShow();
						}
					}
				});
			} else {
				MyToast.getToast(this, "请先选择投注内容！");
			}
			break;
		/** 付款 **/
		case R.id.btn_submit:
			if(totalMoney >= 10) {
				dialog.show();
				dialog.setDialogContent("确认付款？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							commit();
						}
					}
				});
			}else{
				MyToast.getToast(context, "投注金额不能低于10元");
			}
			break;
		/** 发起合买 **/
//		case R.id.btn_follow:
//			join();
//			break;
		case R.id.bet_tv_guize:
			Intent intent = new Intent(Bet_11x5_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_11x5_Activity.this.startActivity(intent);
			break;
		case R.id.coupon_layout:
			if(AppTools.user != null && totalMoney>0){
				Intent intentCoupon = new Intent(Bet_11x5_Activity.this,
						CouponActivity.class);
				intentCoupon.putExtra("money",""+totalMoney );
				startActivityForResult(intentCoupon,Result_OK);
			}else if(totalMoney <= 0) {
				MyToast.getToast(context, "请选投注倍数!");
			}else{
				Intent intentLogin = new Intent(Bet_11x5_Activity.this,LoginActivity.class);
				startActivity(intentLogin);
			}
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Result_OK){
			String couponMoney = data.getStringExtra("money");
			coupon_text.setText("优惠"+couponMoney+"元");
			tv_tatol_money.setText(totalMoney -Integer.parseInt(couponMoney)+ "");
			voucherID = data.getStringExtra("voucherID");
			et_bei.setEnabled(false);
			et_qi.setEnabled(false);
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		if (AppTools.list_numbers.size() > 0) {
			if (AppTools.user != null) {
				RequestUtil requestUtil = new RequestUtil(context, false, 0,
						true, "正在支付...") {
					@Override
					public void responseCallback(JSONObject object) {
						if (RequestUtil.DEBUG)
							Log.i(TAG, "11选5投注支付结果" + object);
						String error = object.optString("error");
						String resultMsg = object.optString("msg");
						if ("0".equals(error)) {
							AppTools.user.setBalance(object
									.optDouble("balance"));
							AppTools.user.setFreeze(object.optDouble("freeze"));
							AppTools.schemeId = object.optInt("schemeids");
							AppTools.lottery.setChaseTaskID(object
									.optInt("chasetaskids"));
							if (AppTools.list_numbers != null) {
								AppTools.list_numbers.clear();
							}
							String SucJinECost = format.format(object
									.optDouble("currentMoeny"));
							String SucCaiJinCost = format.format(object
									.optDouble("currentHandsel"));
							String SuccMoney = format.format(object
									.optDouble("handselMoney"));
							String voucherMoney = format.format(object
									.optDouble("voucherMoney"));
							AppTools.totalCount = 0;
							Intent intent = new Intent(getApplicationContext(),
									PaySuccessActivity.class);
							intent.putExtra("paymoney", totalMoney);
							intent.putExtra("currentMoeny", SucJinECost);
							intent.putExtra("currentHandsel", SucCaiJinCost);
							intent.putExtra("handselMoney", SuccMoney);
							intent.putExtra("voucherMoney", voucherMoney);

							Bet_11x5_Activity.this.startActivity(intent);
						} else if (error.equals(AppTools.ERROR_MONEY + "")) {
							LotteryUtils.showMoneyLess(Bet_11x5_Activity.this,
									totalMoney);
						} else if (error.equals("-500")) {
							MyToast.getToast(Bet_11x5_Activity.this, "连接超时");
						} else {
							MyToast.getToast(Bet_11x5_Activity.this, resultMsg);
						}
					}

					@Override
					public void responseError(VolleyError error) {
						MyToast.getToast(Bet_11x5_Activity.this,
								"抱歉，支付出现未知错误..");
						if (RequestUtil.DEBUG)
							Log.e(TAG, "11选5投注支付报错" + error.getMessage());
					}
				};
				requestUtil.commitBetting(sumCount, totalMoney, isStopChase,"");
			} else {
				MyToast.getToast(Bet_11x5_Activity.this, "请先登陆");
				intent = new Intent(Bet_11x5_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_11x5_Activity.this.startActivity(intent);
			}
		} else {
			MyToast.getToast(Bet_11x5_Activity.this, "请至少选择一注");
			intent = new Intent(Bet_11x5_Activity.this,
					Select_11x5_Activity.class);
			intent.putExtra("loginType", "bet");
			Bet_11x5_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 跳到合买
	 */
	private void join() {
		if (AppTools.qi > 1) {
			dialog.show();
			dialog.setDialogContent("发起合买时不能追号，是否只追一期并继续发起合买？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						et_qi.setText("1");
						AppTools.qi = 1;
						int total = 0; // 总金额
						for (SelectedNumbers num : AppTools.list_numbers) {
							total += num.getMoney();
						}
						total = total * AppTools.bei;
						intent = new Intent(Bet_11x5_Activity.this,
								JoinActivity.class);
						intent.putExtra("totalMoney", total + "");
						Bet_11x5_Activity.this.startActivity(intent);
					}
				}
			});
		} else {
			int total = 0; // 总金额
			for (SelectedNumbers num : AppTools.list_numbers) {
				total += num.getMoney();
			}
			if (total == 0) {
				MyToast.getToast(getApplicationContext(), "您还没有选择号码");
				return;
			}
			total = total * AppTools.bei;
			intent = new Intent(Bet_11x5_Activity.this, JoinActivity.class);
			intent.putExtra("totalMoney", total + "");
			Bet_11x5_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 手选按钮点击事件
	 * 
	 */
	private void btn_handClick() {
		AppTools.totalCount = 0;
		intent = new Intent(Bet_11x5_Activity.this, Select_11x5_Activity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", playType);
		intent.putExtra("11X5Bundle", bundle);
		Bet_11x5_Activity.this.startActivity(intent);
	}

	/**
	 * 机选按钮点击事件
	 * 
	 */
	private void btn_randomClick() {
		String number = null;
		String Lotterynumber = null;
		String ShowLotterynumber = null;
		String number1 = null;
		switch (play2) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			list = NumberTools.getRandomNum7(play2, 11, true);
			number = list.toString();
			System.out.println("....." + number);
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			number1 = Lotterynumber;
			break;
		case 9:
			list = NumberTools.getRandomNum7(2, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", ",");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", ",");
			number1 = Lotterynumber;
			break;
		case 10:
			list = NumberTools.getRandomNum7(3, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", ",");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", ",");
			number1 = Lotterynumber.replace("|", "-");
			break;
		case 11:
			list = NumberTools.getRandomNum7(2, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			number1 = Lotterynumber;
			break;
		case 12:
			list = NumberTools.getRandomNum7(3, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			number1 = Lotterynumber;
			break;

		case 22:
			list = NumberTools.getRandomNum7(2, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", " ");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", " ");
			number1 = number.replace("[", "").replace("]", "")
					.replace(", ", "-");
			break;

		case 23:
			list = NumberTools.getRandomNum7(3, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", " ");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(", ", " ");
			number1 = number.replace("[", "").replace("]", "")
					.replace(", ", "-");
			break;

		case 24:
			list = NumberTools.getRandomNum7(4, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			number1 = Lotterynumber;
			break;
		case 25:
			list = NumberTools.getRandomNum7(5, 11, true);
			number = list.toString();
			Lotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			ShowLotterynumber = number.replace("[", "").replace("]", "")
					.replace(",", "");
			number1 = Lotterynumber;
			break;
		default:
			break;
		}
		SelectedNumbers numbers = new SelectedNumbers();
		// 排序 设置机选参数
		numbers.setNumber(number1);
		numbers.setLotteryId(play1 + "");
		numbers.setPlayType(playType);
		if (playType % 100 == 22 || playType % 100 == 23) {
			numbers.setCount(3);
			numbers.setMoney(6);
		} else if (playType % 100 == 24) {
			numbers.setCount(5);
			numbers.setMoney(10);
		} else if (playType % 100 == 25) {
			numbers.setCount(7);
			numbers.setMoney(14);
		} else {
			numbers.setCount(1);
			numbers.setMoney(2);
		}
		numbers.setShowLotteryNumber(ShowLotterynumber);
		numbers.setLotteryNumber(Lotterynumber);
		AppTools.list_numbers.add(0, numbers);
		// 刷新Adapter
		adapter.notifyDataSetChanged();
		changeTextShow();
	}

	/**
	 * 改变文本的值显示出来
	 */
	public void changeTextShow() {
		if (et_bei.getText().toString().trim().length() == 0)
			AppTools.bei = 1;
		else
			AppTools.bei = Integer.parseInt(et_bei.getText().toString().trim());

		if (et_qi.getText().toString().trim().length() == 0)
			AppTools.qi = 1;
		else
			AppTools.qi = Integer.parseInt(et_qi.getText().toString().trim());

		sumCount = 0; // 总注数
		for (SelectedNumbers num : AppTools.list_numbers) {
			sumCount += num.getCount();
		}

		if (sumCount != 0) {
			totalMoney = sumCount * 2 * AppTools.bei * AppTools.qi;
			tv_tatol_count.setText(sumCount + "");
			tv_tatol_money.setText(totalMoney + "");
		} else {
			tv_tatol_count.setText("0");
			tv_tatol_money.setText("0");
		}
		bet_sv.scrollTo(0, 0);
	}

	/**
	 * 返回
	 */
	public void backToPre() {
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0) {
			dialog.show();
			dialog.setDialogContent("您退出后号码将会清空！");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						AppTools.list_numbers.clear();
						AppTools.totalCount = 0;
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			AppTools.totalCount = 0;
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}
	}

	/**
	 * 重写返回键事件
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPre();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

}
