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
import com.gcapp.tc.sd.ui.adapter.GridView_k3Adapter;
import com.gcapp.tc.sd.ui.adapter.MyBetLotteryList_k3Adapter;
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
 * 功能：快3的投注页面
 * 
 * @author Kinwee 修改日期2015-1-6
 */
public class Bet_k3_Activity extends Activity implements OnClickListener {
	private Context context = Bet_k3_Activity.this;
	private final static String TAG = "Bet_k3Activity";
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
	public MyBetLotteryList_k3Adapter adapter;
	private List<String> list = new ArrayList<String>();
	private int playType = 8301;
	private Intent intent;
	private int type;
	private List<String> list_red, list_blue;
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

		btn_playtype.setText("江苏快3投注");

		adapter = new MyBetLotteryList_k3Adapter(Bet_k3_Activity.this,
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
	 * 初始化属性
	 */
	private void init() {
		bet_lv_scheme.scrollTo(0, 0);
		/* 幸运彩十一运夺金 玩法传递 */
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0)
			playType = AppTools.list_numbers.get(0).getPlayType();

		Bundle bundle = getIntent().getBundleExtra("bundle");
		if (null != bundle) {
			playType = bundle.getInt("playType");
			if (playType == 8309 || playType == 8310) {
				btn_automatic_select.setVisibility(View.GONE);
			} else {
				btn_automatic_select.setVisibility(View.VISIBLE);
			}
			String one = bundle.getString("one");
			String two = bundle.getString("two");
			String three = bundle.getString("three");
			if (null != one && !one.equals("")) {
				SelectedNumbers numbers = new SelectedNumbers();
				if (playType == 8301) {
					numbers.setShowLotteryNumber(one.replace(",", ""));
					numbers.setLotteryNumber(one.replace(",", "\n"));
				} else if (playType == 8303) {
					String[] array = one.replace(" ", "").split(",");
					String endnumber = null;
					for (String string : array) {
						if (endnumber == null)
							endnumber = string + string + string;
						else
							endnumber += string + string + string;
						if (array.length != 1)
							endnumber = endnumber + "\n";
					}
					numbers.setShowLotteryNumber(endnumber != null ? endnumber
							.replace("\n", " ") : null);
					numbers.setLotteryNumber(endnumber);
				} else if (playType == 8305 || 8309 == playType
						|| 8310 == playType) {
					String[] array = one.replace(" ", "").split(",");
					two = two.replace(",", "");
					String endnumber = null;
					for (String string : array) {
						if (8309 == playType || 8310 == playType) {
							numbers.setShowLotteryNumber(one.replace(",", "")
									+ " , " + two);
							numbers.setLotteryNumber(one.replace(",", "")
									+ " , " + two);
						} else {
							two = two.replace(" ", "");
							if (endnumber == null)
								endnumber = "(" + string + string + ")" + "("
										+ two + ")";
							else
								endnumber += "(" + string + string + ")" + "("
										+ two + ")";
							if (array.length != 1)
								endnumber = endnumber + "\n";
							numbers.setShowLotteryNumber(endnumber.replace(
									"\n", " "));
							numbers.setLotteryNumber(endnumber);
						}
					}
				} else if (playType == 8306 || playType == 8307) {
					numbers.setShowLotteryNumber(one.replace(",", ""));
					numbers.setLotteryNumber(one.replace(",", " "));
				}
				if (null != three && !three.equals("")) {
					if (playType == 8303 || playType == 8306) {
						numbers.setCount(AppTools.totalCount - 1);
						numbers.setMoney((AppTools.totalCount - 1) * 2);
					} else if (playType == 8305) {
						int tempcount = three.replace(" ", "").split(",").length;
						numbers.setCount(AppTools.totalCount - tempcount);
						numbers.setMoney((AppTools.totalCount - tempcount) * 2);
					}
				} else {
					numbers.setCount(AppTools.totalCount);
					numbers.setMoney(AppTools.totalCount * 2);
				}
				numbers.setPlayType(playType);
				AppTools.list_numbers.add(0, numbers);
			}
			if (null != three && !three.equals("")) {
				SelectedNumbers numbers = new SelectedNumbers();
				int count = 1;
				int playtype1 = 8302;
				if (playType == 8303) {
					playtype1 = 8302;
					count = 1;
					numbers.setShowLotteryNumber("三同号通选");
					numbers.setLotteryNumber("三同号通选");
				} else if (playType == 8305) {
					playtype1 = 8304;
					String[] array = three.replace(" ", "").split(",");
					String endnumber = null;
					for (String string : array) {
						if (endnumber == null)
							endnumber = string + string + "*";
						else
							endnumber += string + string + "*";
						if (array.length != 1)
							endnumber = endnumber + ",";
					}
					count = array.length;
					numbers.setShowLotteryNumber(endnumber != null ? endnumber
							.replace(",", " ") : null);
					numbers.setLotteryNumber(endnumber);
				} else if (playType == 8306) {
					playtype1 = 8308;
					numbers.setShowLotteryNumber("三连号通选");
					numbers.setLotteryNumber("三连号通选");
					count = 1;
				}
				numbers.setPlayType(playtype1);
				numbers.setCount(count);
				numbers.setMoney(2 * count);
				AppTools.list_numbers.add(0, numbers);
			}

			adapter.notifyDataSetChanged();
			changeTextShow();
			getIntent().replaceExtras(bundle);
		} else {
			adapter.notifyDataSetChanged();
		}
		changeTextShow();
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
	 * 复选框的监听
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
	 * 
	 */
	class listItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			SelectedNumbers num = AppTools.list_numbers.get(position);
			// 跳转页面并传值
			intent = new Intent(Bet_k3_Activity.this, Select_K3_Activity.class);
			Bundle bundle = new Bundle();
			AppTools.totalCount = num.getCount();
			int clickType = num.getPlayType();
			if (clickType == 8302) {
				bundle.putInt("type", 8303);
			} else if (clickType == 8308) {
				bundle.putInt("type", 8306);
			} else if (clickType == 8304) {
				bundle.putInt("type", 8305);
			} else {
				bundle.putInt("type", num.getPlayType());
			}
			bundle.putInt("shouji", 1);
			System.out.println(num.getPlayType() + "==="
					+ num.getShowLotteryNumber());
			ArrayList<String> listOne = null;
			ArrayList<String> listTwo = null;
			ArrayList<String> listThree = null;

			if (clickType == 8301 || clickType == 8307 || clickType == 8306) {
				String[] str = num.getShowLotteryNumber().split(" ");
				listOne = new ArrayList<String>();
				for (String s : str) {
					listOne.add(s.trim());
				}
				bundle.putStringArrayList("oneSet", listOne);
			} else if (clickType == 8303) {
				String[] str = num.getShowLotteryNumber().split(" ");
				listOne = new ArrayList<String>();
				for (String s : str) {
					listOne.add(s.substring(0, 1).trim());

				}
				bundle.putStringArrayList("oneSet", listOne);
			} else if (clickType == 8304) {
				String[] str = num.getShowLotteryNumber().split(" ");
				listThree = new ArrayList<String>();
				for (String s : str) {
					listThree.add(s.substring(0, 1).trim());
				}
				bundle.putStringArrayList("threeSet", listThree);
			} else if (clickType == 8302 || clickType == 8308) {
				listThree = new ArrayList<String>();
				listThree.add("1");
				bundle.putStringArrayList("threeSet", listThree);
			} else if (clickType == 8305) {
				String str1 = num.getShowLotteryNumber().replace(" ", "");
				String[] str = str1.replace("(", "").replace(")", " ")
						.split(" ");
				listOne = new ArrayList<String>();
				listTwo = new ArrayList<String>();
				for (int i = 0; i < str.length; i++) {
					if (i % 2 == 0) {
						listOne.add(str[i].substring(1, 2));
					} else if (i % 2 != 0 && listTwo.size() == 0) {
						for (int j = 0; j < str[i].length(); j++) {
							listTwo.add(str[i].substring(j, j + 1));
						}
					}
				}
				bundle.putStringArrayList("oneSet", listOne);
				bundle.putStringArrayList("twoSet", listTwo);
			} else if (clickType == 8309 || clickType == 8310) {
				String str1 = num.getShowLotteryNumber().replace(" ", "");
				String[] str = str1.replace("(", "").replace(")", " ")
						.split(",");
				listOne = new ArrayList<String>();
				listTwo = new ArrayList<String>();
				for (int i = 0; i < str.length; i++) {
					if (i % 2 == 0) {
						if (str[i].trim().length() > 1) {
							listOne.add(str[i].substring(0, 1));
							listOne.add(str[i].substring(1, 2));
						} else {
							listOne.add(str[i].substring(0, 1));
						}
					} else {
						for (int j = 0; j < str[i].length(); j++) {
							listTwo.add(str[i].substring(j, j + 1));
						}
					}
				}
				bundle.putStringArrayList("oneSet", listOne);
				bundle.putStringArrayList("twoSet", listTwo);
			}
			// 将Bundle 放入Intent
			intent.putExtra("k_3Bundle", bundle);
			AppTools.list_numbers.remove(position);
			Bet_k3_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 监听期号文本框的值改变
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
	 * 监听倍数文本框的值改变
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
					MyToast.getToast(getApplicationContext(), "最大倍数为9999")
							;
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
	 * 设置文本框的焦点
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
	 * 公用按钮的点击监听
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
				dialog.show();;
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
//			commit();
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
			Intent intent = new Intent(Bet_k3_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_k3_Activity.this.startActivity(intent);
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
						if (RequestUtil.DEBUG)
							Log.i(TAG, "快3投注支付结果" + object);
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
							Bet_k3_Activity.this.startActivity(intent);
						} else if (error.equals(AppTools.ERROR_MONEY + "")) {
							LotteryUtils.showMoneyLess(Bet_k3_Activity.this,
									totalMoney);
						} else if (error.equals("-500")) {
							MyToast.getToast(Bet_k3_Activity.this, "连接超时")
									;
						} else {
							MyToast.getToast(Bet_k3_Activity.this, resultMsg)
									;
						}
					}

					@Override
					public void responseError(VolleyError error) {
						// setEnabled(true);
						MyToast.getToast(Bet_k3_Activity.this, "抱歉，支付出现未知错误..")
								;
						if (RequestUtil.DEBUG)
							Log.e(TAG, "投注支付报错" + error.getMessage());
					}
				};
				requestUtil.commitBetting(sumCount, totalMoney, isStopChase,"");
			} else {
				MyToast.getToast(Bet_k3_Activity.this, "请先登陆");
				intent = new Intent(Bet_k3_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_k3_Activity.this.startActivity(intent);
			}
		} else {
			MyToast.getToast(Bet_k3_Activity.this, "请至少选择一注");
			intent = new Intent(Bet_k3_Activity.this, Select_K3_Activity.class);
			intent.putExtra("loginType", "bet");
			Bet_k3_Activity.this.startActivity(intent);
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
						intent = new Intent(Bet_k3_Activity.this,
								JoinActivity.class);
						intent.putExtra("totalMoney", total + "");
						Bet_k3_Activity.this.startActivity(intent);
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
			intent = new Intent(Bet_k3_Activity.this, JoinActivity.class);
			intent.putExtra("totalMoney", total + "");
			Bet_k3_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 手选按钮点击事件
	 */
	private void btn_handClick() {
		GridView_k3Adapter.oneSet.clear();
		AppTools.totalCount = 0;
		intent = new Intent(Bet_k3_Activity.this, Select_K3_Activity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", playType);
		bundle.putInt("shouji", 0);
		intent.putExtra("k_3Bundle", bundle);
		Bet_k3_Activity.this.startActivity(intent);
	}

	/**
	 * 机选按钮点击事件
	 */
	private void btn_randomClick(int count) {
		String Mathnumber, Mathnumber1;
		list.clear();
		switch (playType) {
		case 8301:
			list = NumberTools.getRandomNumk_3(1, 4, 17); // 随机1个数 大于等3 小于19
			break;
		case 8302:
			list.add("三同号通选");
			break;
		case 8303:
			Mathnumber = NumberTools.getRandomNumk_3(1, 1, 6).get(0);
			list.add(Mathnumber + Mathnumber + Mathnumber);
			break;
		case 8304:
			Mathnumber = NumberTools.getRandomNumk_3(1, 1, 6).get(0);
			list.add(Mathnumber + Mathnumber + "*");
			break;
		case 8305:
			List<String> list1 = NumberTools.getRandomNumk_3(2, 1, 6);
			Mathnumber = list1.get(0);
			Mathnumber1 = list1.get(1);
			list.add("(" + Mathnumber + Mathnumber + ")");
			list.add("(" + Mathnumber1 + ")");
			list1 = null;
			break;
		case 8306:
			list = NumberTools.getRandomNumk_3(3, 1, 6);
			break;
		case 8307:
			list = NumberTools.getRandomNumk_3(2, 1, 6);
			break;
		case 8308:
			list.add("三连号通选");
			break;
		default:
			break;
		}
		String number = list.toString();
		if (8306 != playType && 8307 != playType)
			number = number.replace("[", "").replace("]", "").replace(",", "")
					.replace(" ", "");
		else
			number = number.replace("[", "").replace("]", "").replace(",", "");

		SelectedNumbers numbers = new SelectedNumbers();
		// 排序 设置机选参数
		numbers.setPlayType(playType);
		numbers.setCount(1);
		numbers.setMoney(2);
		numbers.setShowLotteryNumber(number);
		numbers.setLotteryNumber(number);
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
						GridView_k3Adapter.oneSet.clear();
						AppTools.totalCount = 0;
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			GridView_k3Adapter.oneSet.clear();
			AppTools.totalCount = 0;
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}
	}

	/**
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
	 * activity重新进入时初始化数据
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
