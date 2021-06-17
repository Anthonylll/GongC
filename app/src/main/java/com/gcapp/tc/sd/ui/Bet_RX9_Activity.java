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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.dataaccess.TeamArray;
import com.gcapp.tc.sd.ui.adapter.MyBetLotterListRX9Adapter;
import com.gcapp.tc.sd.ui.adapter.RX9_TeamArrayAdapter;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 功能：任选9的投注页面
 * 
 */
public class Bet_RX9_Activity extends Activity implements OnClickListener {
	private Context context = Bet_RX9_Activity.this;
	private final static String TAG = "Bet_RX9_Activity";
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
	private ImageView bet_btn_deleteall;// 清空
	private TextView bet_tv_guize; // 委托投注规则

	private MyListView2 bet_lv_scheme;// 自定义listview
	public MyBetLotterListRX9Adapter adapter;
	private Intent intent;
	private int type;

	private long sumCount, totalMoney; // 方案总注数 // 方案总金额
	private EditText et_bei, et_qi; // 用户输入的倍数 // 用户输入的期数
	/** 普通投注玩法ID **/
	private final int playType = 7501;
	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;
	private boolean backHome = false;
	private DecimalFormat format = new DecimalFormat("#####0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet_sfcrx);

		App.activityS.add(this);
		App.activityS1.add(this);
		if (AppTools.list_numbers == null)
			AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		findView();
		setListener();
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
		btn_playtype.setText("任选九投注");
		adapter = new MyBetLotterListRX9Adapter(Bet_RX9_Activity.this,
				Select_RX9_Activity.lisTeamArrays, AppTools.list_numbers);
		dialog = new ConfirmDialog(this, R.style.dialog);
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
		if (RX9_TeamArrayAdapter.btnMap.size() != 0) {
			SelectedNumbers numbers = new SelectedNumbers();
			List<Map<Integer, String>> List_map = new ArrayList<Map<Integer, String>>();
			if (RX9_TeamArrayAdapter.btnMap.size() > 0) {
				List_map.add(RX9_TeamArrayAdapter.btnMap);
			}
			numbers.setList_Map(List_map);
			Set<Map.Entry<Integer, String>> entry = RX9_TeamArrayAdapter.btnMap
					.entrySet();
			String num = NumberTools.ChangeRX9(RX9_TeamArrayAdapter.btnMap);
			numbers.setCount(AppTools.totalCount);
			numbers.setMoney(AppTools.totalCount * 2);
			numbers.setPlayType(playType);
			numbers.setLotteryNumber(num);
			if (AppTools.list_numbers.size() > 0) {
				AppTools.list_numbers.clear();
			}

			AppTools.list_numbers.add(0, numbers);
		}
		changeTextShow();
		btn_submit.setText("付款");
	}

	/**
	 * 当选号界面数据变化时更新界面数据
	 */
	public void setTextShow() {
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money
				.setText((AppTools.totalCount * 2 * AppTools.bei * AppTools.qi)
						+ "");
		init();
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_continue_select.setOnClickListener(this);
		bet_btn_deleteall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
//		btn_follow.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		et_qi.setOnClickListener(this);
		bet_lv_scheme.setAdapter(adapter);
		et_bei.addTextChangedListener(bei_textWatcher);
		bet_lv_scheme.setOnItemClickListener(new listItemClick());
		bet_tv_guize.setOnClickListener(this);
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
			intent = new Intent(Bet_RX9_Activity.this,
					Select_RX9_Activity.class);
			Bundle bundle = new Bundle();
			AppTools.totalCount = num.getCount();
			List<String> list_key = new ArrayList<String>();
			List<String> list_value = new ArrayList<String>();
			Map<Integer, String> btnMap = num.getList_Map().get(0);

			for (Object o : btnMap.entrySet()) {
				Entry entry = (Entry) o;
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				list_key.add(key);
				list_value.add(value);
			}
			bundle.putStringArrayList("Key", (ArrayList<String>) list_key);
			bundle.putStringArrayList("Value", (ArrayList<String>) list_value);
			intent.putExtra("bundle", bundle);
			AppTools.list_numbers.remove(position);
			Bet_RX9_Activity.this.startActivity(intent);
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
					MyToast.getToast(getApplicationContext(), "最大倍数为999");
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
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			if (AppTools.list_numbers.size() == 0) {
				MyToast.getToast(Bet_RX9_Activity.this, "请先选择投注内容");
				break;
			}
			dialog.show();
			dialog.setDialogContent("是否清空投注单号码");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						RX9_TeamArrayAdapter.btnMap.clear();
						AppTools.list_numbers.clear();
						adapter.setNumberList(new ArrayList<TeamArray>());
						adapter.notifyDataSetChanged();
						AppTools.totalCount = 0;
						changeTextShow();
					}
				}
			});
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
			Intent intent = new Intent(Bet_RX9_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_RX9_Activity.this.startActivity(intent);
			break;
		case R.id.et_qi:
			MyToast.getToast(this, "此彩种不支持追号");
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
						Log.e("RX9--result", object.toString());
						if (RequestUtil.DEBUG)
							Log.i(TAG, "任选九投注支付结果" + object);
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
							RX9_TeamArrayAdapter.btnMap.clear();
							Intent intent = new Intent(getApplicationContext(),
									PaySuccessActivity.class);
							intent.putExtra("paymoney", totalMoney);

							intent.putExtra("currentMoeny", SucJinECost);
							intent.putExtra("currentHandsel", SucCaiJinCost);
							intent.putExtra("handselMoney", SuccMoney);
							Bet_RX9_Activity.this.startActivity(intent);
						} else if (error.equals(AppTools.ERROR_MONEY + "")) {
							LotteryUtils.showMoneyLess(Bet_RX9_Activity.this,
									totalMoney);
						} else if (error.equals("-500")) {
							MyToast.getToast(Bet_RX9_Activity.this, "连接超时");
						} else {
							MyToast.getToast(Bet_RX9_Activity.this, resultMsg);
						}
					}

					@Override
					public void responseError(VolleyError error) {
						// setEnabled(true);
						MyToast.getToast(Bet_RX9_Activity.this, "抱歉，支付出现未知错误..");
						if (RequestUtil.DEBUG)
							Log.e(TAG, "投注支付报错" + error.getMessage());
					}
				};
				requestUtil.commitBetting(sumCount, totalMoney, isStopChase,"");
			} else {
				MyToast.getToast(Bet_RX9_Activity.this, "请先登陆");
				intent = new Intent(Bet_RX9_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_RX9_Activity.this.startActivity(intent);
			}
		} else {
			MyToast.getToast(Bet_RX9_Activity.this, "请至少选择一注");
			intent = new Intent(Bet_RX9_Activity.this,
					Select_RX9_Activity.class);
			intent.putExtra("loginType", "bet");
			Bet_RX9_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 设置控件是否可用
	 * 
	 */
	private void setEnabled(boolean isEna) {
		btn_submit.setEnabled(isEna);
		bet_btn_deleteall.setEnabled(isEna);
		btn_continue_select.setEnabled(isEna);
//		btn_follow.setEnabled(isEna);
		bet_cb_stopfollow.setEnabled(isEna);
		et_bei.setEnabled(isEna);
		et_qi.setEnabled(isEna);
	}

	/**
	 * 跳到合买
	 */
	private void join() {
		int total = 0; // 总金额
		total = Integer.parseInt(tv_tatol_money.getText().toString().trim());

		if (total == 0) {
			MyToast.getToast(getApplicationContext(), "您还没有选择对阵");
			return;
		}
		intent = new Intent(Bet_RX9_Activity.this, JoinActivity.class);
		intent.putExtra("totalMoney", total + "");
		Bet_RX9_Activity.this.startActivity(intent);
	}

	/**
	 * 手选按钮点击事件
	 * 
	 */
	private void btn_handClick() {
		Intent intent = new Intent(Bet_RX9_Activity.this,
				Select_RX9_Activity.class);
		Bet_RX9_Activity.this.startActivity(intent);
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
						RX9_TeamArrayAdapter.btnMap.clear();
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			AppTools.list_numbers.clear();
			AppTools.totalCount = 0;
			RX9_TeamArrayAdapter.btnMap.clear();
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}

	}

	/** 重写返回键事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPre();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 重写返回activity方法
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (!backHome) {
			init();
			changeTextShow();
		}
		backHome = false;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		backHome = true;
	}
}
