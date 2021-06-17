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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.sd.ui.adapter.MyBetLotterySSCAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.Logger;
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
import java.util.Collections;
import java.util.List;

/**
 * 
 * 功能：重庆时时彩的投注页面
 */
public class Bet_SSC_Activity extends Activity implements OnClickListener {
	private Context context = Bet_SSC_Activity.this;
	private final static String TAG = "Bet_SSCActivity";
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
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

	private String opt = "11"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private LinearLayout btn_continue_select;// 继续投注
	private LinearLayout btn_automatic_select;// 机选1注
	private ImageView bet_btn_deleteall;// 清空
	private TextView bet_tv_guize; // 委托投注规则

	private MyListView2 bet_lv_scheme;// 自定义listview
	public MyBetLotterySSCAdapter adapter;

	private List<String> list = new ArrayList<String>();

	private Intent intent;
	private int type = 1, playType;
	private long sumCount, totalMoney; // 方案总注数 // 方案总金额

	private EditText et_bei, et_qi; // 用户输入的倍数 // 用户输入的期数

	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;

	private ScrollView bet_sv;

	private DecimalFormat format = new DecimalFormat("#####0.00");

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
		bet_lv_scheme.scrollTo(0, 0);
		setListener();
		changeTextShow();
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

		bet_lv_scheme = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		bet_sv = (ScrollView) this.findViewById(R.id.bet_sv);

		btn_playtype.setText("重庆时时彩投注");

		adapter = new MyBetLotterySSCAdapter(Bet_SSC_Activity.this,
				AppTools.list_numbers, AppTools.lottery.getLotteryID());
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
	 * 初始化界面数据
	 */
	private void init() {
		Bundle bundle;
		/* 专用幸运选号里面跳转过来的 */
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0) {
			playType = AppTools.list_numbers.get(0).getPlayType();
			type = AppTools.list_numbers.get(0).getType();
		}
		bundle = getIntent().getBundleExtra("bundle");
		if (null != bundle) {
			String one = (bundle.getString("one")).replace(", ", "").trim();
			String two = (bundle.getString("two")).replace(", ", "").trim();
			String three = (bundle.getString("three")).replace(", ", "").trim();
			String four = (bundle.getString("four")).replace(", ", "").trim();
			String five = (bundle.getString("five")).replace(", ", "").trim();

			String dxds = bundle.getString("dxds");

			type = bundle.getInt("type");
			playType = bundle.getInt("playType");

			SelectedNumbers numbers = new SelectedNumbers();
			System.out.println("++++" + playType);
			if (playType == 2803 || type == 8) {
				if (one.length() > 1)
					one = "(" + one + ")";
				if (two.length() > 1)
					two = "(" + two + ")";
				if (three.length() > 1)
					three = "(" + three + ")";
				if (four.length() > 1)
					four = "(" + four + ")";
				if (five.length() > 1)
					five = "(" + five + ")";

			}

			if (type == 9) {
				numbers.setShowLotteryNumber(dxds);
				numbers.setLotteryNumber(dxds);
			} else if (type == 5 || type == 6 || type == 3) {
				numbers.setLotteryNumber(one + two + three + four + five);
				numbers.setShowLotteryNumber(one + two + three + four + five);
			} else {
				if (two.length() == 0) {
					numbers.setLotteryNumber("----" + one);
				} else if (three.length() == 0) {
					numbers.setLotteryNumber("---" + one + two);
				} else if (five.length() == 0) {
					numbers.setLotteryNumber("--" + one + two + three);
				} else {
					numbers.setLotteryNumber(one + two + three + four + five);
				}
				numbers.setShowLotteryNumber(one + two + three + four + five);
			}

			numbers.setNumber((one + "-" + two + "-" + three + "-" + four + "-" + five)
					.replace("(", "").replace(")", ""));
			if (playType == 2814) {
				String num = "";
				if ("".equals(one)) {
					num = two;
				} else if ("".equals(two)) {
					num = one;
				} else {
					num = one + "," + two;
				}
				numbers.setNumber(num);
				numbers.setShowLotteryNumber(num);
				numbers.setLotteryNumber(num);
			}
			if (playType == 2815) {
				String num = (bundle.getString("one")).replace(", ", ",")
						.trim();
				numbers.setNumber(num);
				numbers.setShowLotteryNumber(num);
				numbers.setLotteryNumber(num);
			}
			numbers.setType(type);
			numbers.setPlayType(playType);
			numbers.setCount(AppTools.totalCount);
			numbers.setMoney(AppTools.totalCount * 2);

			AppTools.list_numbers.add(0, numbers);

			adapter.notifyDataSetChanged();

			changeTextShow();
			getIntent().replaceExtras(bundle);
		}
		btn_automatic_select.setVisibility(View.GONE);
		if (playType == 2803 || playType == 2805)
			btn_automatic_select.setVisibility(View.VISIBLE);
		isStopChase = 1;
		btn_submit.setText("付款");
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_continue_select.setOnClickListener(this);
		btn_automatic_select.setOnClickListener(this);
		bet_btn_deleteall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
//		btn_follow.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		bet_lv_scheme.setAdapter(adapter);
		bet_lv_scheme.setOnItemClickListener(new listItemClick());

		et_bei.addTextChangedListener(bei_textWatcher);
		et_qi.addTextChangedListener(qi_textWatcher);
		bet_tv_guize.setOnClickListener(this);

		bet_cb_stopfollow.setOnCheckedChangeListener(new MyCheckChange());
	}

	/**
	 * 复选框监听
	 * 
	 * @author lenovo
	 * 
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
	 * 
	 * @author lenovo
	 */
	class listItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			SelectedNumbers num = AppTools.list_numbers.get(position);

			playType = num.getPlayType(); // 幸运彩中需要添加的
			// 跳转页面并传值
			intent = new Intent(Bet_SSC_Activity.this,
					Select_SSC_Activity.class);
			Bundle bundle = new Bundle();
			AppTools.totalCount = num.getCount();

			bundle.putInt("type", num.getType());

			if (num.getType() == 9) {
				bundle.putString("Bet_dxds", num.getShowLotteryNumber());
			} else {
				if (num.getType() == 11) {
					ArrayList<String> listOne = new ArrayList<String>();
					if (num.getNumber().contains(",")) {
						String[] str = num.getNumber().split(",");
						Collections.addAll(listOne, str);
					} else {
						listOne.add(num.getNumber());
					}

					bundle.putStringArrayList("oneSet", listOne);
				} else if (num.getType() == 10) {
					ArrayList<String> listOne = new ArrayList<String>();
					ArrayList<String> listTwo = new ArrayList<String>();
					if (num.getNumber().contains(",")) {
						String[] str = num.getNumber().split(",");
						listOne.add(str[0]);
						listTwo.add(str[1]);
					} else {
						listOne.add(num.getNumber());
					}
					bundle.putStringArrayList("oneSet", listOne);
					bundle.putStringArrayList("twoSet", listTwo);
				} else {
					String[] str = num.getNumber().split("-");
					ArrayList<String> listOne = new ArrayList<String>();

					for (int i = 0; i < str[0].length(); i++) {
						listOne.add(str[0].substring(i, i + 1));
					}

					bundle.putStringArrayList("oneSet", listOne);

					if (str.length > 1) {

						ArrayList<String> listTwo = new ArrayList<String>();
						for (int i = 0; i < str[1].length(); i++) {
							listTwo.add(str[1].substring(i, i + 1));
						}
						bundle.putStringArrayList("twoSet", listTwo);
					}

					if (str.length > 2) {
						ArrayList<String> listThree = new ArrayList<String>();

						for (int i = 0; i < str[2].length(); i++) {
							listThree.add(str[2].substring(i, i + 1));
						}
						bundle.putStringArrayList("threeSet", listThree);
					}
					if (str.length > 3) {
						ArrayList<String> listFour = new ArrayList<String>();
						for (int i = 0; i < str[3].length(); i++) {
							listFour.add(str[3].substring(i, i + 1));
						}
						bundle.putStringArrayList("fourSet", listFour);
					}
					if (str.length > 4) {
						ArrayList<String> listFive = new ArrayList<String>();
						for (int i = 0; i < str[4].length(); i++) {
							listFive.add(str[4].substring(i, i + 1));
						}
						bundle.putStringArrayList("fiveSet", listFive);
					}
				}

			}
			// 将Bundle 放入Intent
			intent.putExtra("SSCBundle", bundle);

			AppTools.list_numbers.remove(position);

			Bet_SSC_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 期号文本框的输入监听
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
					MyToast.getToast(
							getApplicationContext(),
							"最多可追"
									+ AppTools.lottery.getDtCanChaseIsuses()
											.size() + "期");
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
	 * 倍数文本框的输入监听
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
				if (Integer.parseInt(edt.toString().trim()) > 999) {
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为999")
							;
					et_bei.setText("999");
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
	 * 文本框的焦点设置
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/**
	 * 公用根据id处理监听结果
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
			btn_randomClick(1);
			break;
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			if (null != AppTools.list_numbers
					&& 0 != AppTools.list_numbers.size()) {
				dialog.show()	;
				dialog.setDialogContent("是否清空投注单号码");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							AppTools.list_numbers.clear();
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
			commit();
			break;
		/** 发起合买 **/
//		case R.id.btn_follow:
//			join();
//			break;
		case R.id.bet_tv_guize:
			Intent intent = new Intent(Bet_SSC_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_SSC_Activity.this.startActivity(intent);
			break;
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		if (AppTools.list_numbers.size() > 0) {
			if (AppTools.user != null) {
				// setEnabled(false);
				RequestUtil requestUtil = new RequestUtil(context, false, 0,
						true, "正在支付...") {
					@Override
					public void responseCallback(JSONObject object) {
						// setEnabled(true);
						Logger.Log_e("江西时时彩", object.toString());
						String error = object.optString("error");
						String resultMsg = object.optString("msg");
						if ("0".equals(error)) {
							AppTools.user.setBalance(object
									.optDouble("balance"));
							AppTools.user.setFreeze(object.optDouble("freeze"));
							AppTools.schemeId = object.optInt("schemeids");
							AppTools.lottery.setChaseTaskID(object
									.optInt("chasetaskids"));
							AppTools.list_numbers.clear();

							String SucJinECost = format.format(object
									.optDouble("currentMoeny"));
							String SucCaiJinCost = format.format(object
									.optDouble("currentHandsel"));
							String SuccMoney = format.format(object
									.optDouble("handselMoney"));
							AppTools.totalCount = 0;
							Intent intent = new Intent(getApplicationContext(),
									PaySuccessActivity.class);
							intent.putExtra("paymoney", totalMoney);
							Log.e("currentHandsel", SucCaiJinCost);
							intent.putExtra("currentMoeny", SucJinECost);
							intent.putExtra("currentHandsel", SucCaiJinCost);
							intent.putExtra("handselMoney", SuccMoney);
							Bet_SSC_Activity.this.startActivity(intent);
						} else if (error.equals(AppTools.ERROR_MONEY + "")) {
							LotteryUtils.showMoneyLess(Bet_SSC_Activity.this,
									totalMoney);
						} else if (error.equals("-500")) {
							MyToast.getToast(Bet_SSC_Activity.this, "连接超时")
									;
						} else {
							Toast.makeText(Bet_SSC_Activity.this, resultMsg,
									Toast.LENGTH_SHORT);
						}
					}

					@Override
					public void responseError(VolleyError error) {
						// setEnabled(true);
						MyToast.getToast(Bet_SSC_Activity.this, "抱歉，支付出现未知错误..")
								;
						if (RequestUtil.DEBUG)
							Log.e(TAG, "投注支付报错" + error.getMessage());
					}
				};
				requestUtil.commitBetting(sumCount, totalMoney, isStopChase,"");
			} else {
				MyToast.getToast(Bet_SSC_Activity.this, "请先登陆");
				intent = new Intent(Bet_SSC_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_SSC_Activity.this.startActivity(intent);
			}
		} else {
			MyToast.getToast(Bet_SSC_Activity.this, "请至少选择一注");
			intent = new Intent(Bet_SSC_Activity.this,
					Select_SSC_Activity.class);
			intent.putExtra("loginType", "bet");
			Bet_SSC_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 设置按钮是否可用
	 * 
	 * @param isEna
	 *            ：参数
	 */
	private void setEnabled(boolean isEna) {
		btn_submit.setEnabled(isEna);
		bet_btn_deleteall.setEnabled(isEna);
		btn_continue_select.setEnabled(isEna);
		btn_automatic_select.setEnabled(isEna);
//		btn_follow.setEnabled(isEna);
		bet_cb_stopfollow.setEnabled(isEna);
		et_bei.setEnabled(isEna);
		et_qi.setEnabled(isEna);
	}

	/**
	 * 跳到合买
	 * 
	 */
	private void join() {
		if (AppTools.qi > 1) {
			dialog.show()	;
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
						intent = new Intent(Bet_SSC_Activity.this,
								JoinActivity.class);
						intent.putExtra("totalMoney", total + "");
						Bet_SSC_Activity.this.startActivity(intent);
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
			intent = new Intent(Bet_SSC_Activity.this, JoinActivity.class);
			intent.putExtra("totalMoney", total + "");
			Bet_SSC_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 
	 * 手选按钮点击事件
	 */
	private void btn_handClick() {
		AppTools.totalCount = 0;
		intent = new Intent(Bet_SSC_Activity.this, Select_SSC_Activity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putInt("playtype", playType);
		intent.putExtra("SSCBundle", bundle);
		Bet_SSC_Activity.this.startActivity(intent);
	}

	/**
	 * 机选按钮点击事件
	 */
	private void btn_randomClick(int count) {
		System.out.println("---" + playType);
		String number = null;
		String Lotterynumber = null;
		String ShowLotterynumber = null;
		String number1 = null;
		if (playType == 2803 || playType == 2805) {
			switch (type) {
			case 1:
				list = NumberTools.getRandomNum(1, 9);
				Collections.sort(list);
				number = list.toString();
				Lotterynumber = "----"
						+ number.replace("[", "").replace("]", "")
								.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = Lotterynumber;
				break;
			case 2:
				list = NumberTools.getRandomNum(2, 9);
				Collections.sort(list);
				number = list.toString().trim();
				Lotterynumber = "---"
						+ number.replace("[", "").replace("]", "")
								.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = number.replace("[", "").replace("]", "")
						.replace(" ", "").split(",")[0]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[1];
				break;
			case 4:
				list = NumberTools.getRandomNum(3, 9);
				Collections.sort(list);
				number = list.toString().trim();
				Lotterynumber = "--"
						+ number.replace("[", "").replace("]", "")
								.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = number.replace("[", "").replace("]", "")
						.replace(" ", "").split(",")[0]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[1]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[2];
				break;
			case 7:
			case 8:
				list = NumberTools.getRandomNum(5, 9);
				Collections.sort(list);
				number = list.toString().trim();
				Lotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				ShowLotterynumber = number.replace("[", "").replace("]", "")
						.replace(",", "").replace(" ", "");
				number1 = number.replace("[", "").replace("]", "")
						.replace(" ", "").split(",")[0]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[1]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[2]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[3]
						+ "-"
						+ number.replace("[", "").replace("]", "")
								.replace(" ", "").split(",")[4];
				break;
			default:
				break;
			}
		}
		SelectedNumbers numbers = new SelectedNumbers();
		// 排序 设置机选参数
		numbers.setNumber(number1);
		numbers.setPlayType(playType);
		numbers.setType(type);
		numbers.setCount(1);
		numbers.setMoney(2);
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
			dialog.show()	;
			dialog.setDialogContent("您退出后号码将会清空！");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						AppTools.list_numbers.clear();
						AppTools.totalCount = 0;
						finish();
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			AppTools.totalCount = 0;
			finish();
		}
	}

	/**
	 * 
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPre();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 重写onResume方法
	 */
	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}
}
