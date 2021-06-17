package com.gcapp.tc.sd.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.JC_Details;
import com.gcapp.tc.dataaccess.Show_JC_Details;
import com.gcapp.tc.dataaccess.TicketAssign;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：竞彩足球的奖金优化类
 */
public class Bonus_JCZQ_Activity extends Activity implements OnClickListener {
	private final static String TAG = "Bonus_JCZQ_Activity";
	private Context context;
	private ListView listView1;
	public static List<Show_JC_Details> list_Show = new ArrayList<Show_JC_Details>();
	private long currentAmount;
	private Button follow_btn_sub_yj, follow_btn_add_yj;
	private TextView summoney;
	private TextView win_fanwei;
	private EditText editAmount;
	private EditText sum_bei;
	private java.text.DecimalFormat df;
	/** 1平均优化 3博热优化 2搏冷优化*/
	public static int youhua_type = 1;
	private Button follow_btn_public, follow_btn_toend, follow_btn_afterwin;
	private Show_Adapter adapetr;
	private TextView btn_playtype;// 玩法
	private Button btn_submit, btn_join;
	private ImageButton btn_back, btn_help;
	private ImageView iv_up_down;
	private Intent intent;
	private Map<String, String> max_peilv = new HashMap<String, String>();
	private Double max_ = 0.0;
	private DecimalFormat format = new DecimalFormat("#####0.00");
	private List<TicketAssign> tickets;
	private int MinAmount;
	private boolean heheda;
	private ConfirmDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bonus);
		heheda = getIntent().getBooleanExtra("multipleFreePass", false);
		youhua_type = 1;
		findView();
		setListener();
		init();
	}

	/**
	 * 初始化自定义控件
	 */
	private void findView() {
		listView1 = (ListView) this.findViewById(R.id.listView1);
		editAmount = (EditText) this.findViewById(R.id.follow_et_yj);
		follow_btn_sub_yj = (Button) this.findViewById(R.id.follow_btn_sub_yj);
		follow_btn_add_yj = (Button) this.findViewById(R.id.follow_btn_add_yj);
		follow_btn_public = (Button) this.findViewById(R.id.follow_btn_public);
		follow_btn_toend = (Button) this.findViewById(R.id.follow_btn_toend);
		follow_btn_afterwin = (Button) this
				.findViewById(R.id.follow_btn_afterwin);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_join = (Button) findViewById(R.id.btn_join);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		summoney = (TextView) findViewById(R.id.tv_tatol_money);
		win_fanwei = (TextView) findViewById(R.id.win_fanwei);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		sum_bei = (EditText) this.findViewById(R.id.sun_bei);
		btn_playtype.setText("奖金优化");
		btn_join.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		btn_help.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化界面数据
	 */
	private void init() {
		get_max_peilv_list();
		context = Bonus_JCZQ_Activity.this;
		df = new java.text.DecimalFormat("#.00");
		setDate();
		currentAmount = getIntent().getLongExtra("totalMoney", 0);
		adapetr = new Show_Adapter(this, list_Show);
		listView1.setAdapter(adapetr);
		setbei((int) (currentAmount / 2), list_Show);
		show();
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 得到选号的最大赔率集合
	 */
	private void get_max_peilv_list() {
		for (int i = 0; i < Bet_JCZQ_Activity.list_JC_Details.size(); i++) {
			if (max_peilv.containsKey(Bet_JCZQ_Activity.list_JC_Details.get(i)
					.getMatchID())) {
				if (Double.parseDouble(Bet_JCZQ_Activity.list_JC_Details.get(i)
						.getPeilv()) > Double.parseDouble(max_peilv
						.get(Bet_JCZQ_Activity.list_JC_Details.get(i)
								.getMatchID()))) {
					max_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
							.getMatchID(), Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv());
				} else
					continue;
			} else
				max_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
						.getMatchID(), Bet_JCZQ_Activity.list_JC_Details.get(i)
						.getPeilv());
		}
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		follow_btn_sub_yj.setOnClickListener(this);
		follow_btn_add_yj.setOnClickListener(this);
		follow_btn_public.setOnClickListener(this);
		follow_btn_toend.setOnClickListener(this);
		follow_btn_afterwin.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_join.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_help.setOnClickListener(this);
		editAmount.addTextChangedListener(title_money_textWatcher);
		sum_bei.addTextChangedListener(sunbei_textWatcher);
	}

	/**
	 * 公用的监听处理方法
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.follow_btn_sub_yj:
			if (currentAmount > list_Show.size() * 2) {
				currentAmount -= 2;
			} else {
				MyToast.getToast(this, "必须1倍以上金额优化");
			}
			setbei((int) (currentAmount / 2), list_Show);
			show();
			break;

		case R.id.follow_btn_add_yj:
			if (currentAmount + 2 > 999999)
				return;
			currentAmount += 2;
			setbei((int) (currentAmount / 2), list_Show);
			show();
			break;

		case R.id.follow_btn_public:
			youhua_type = 1;
			setDate();
			setbei((int) (currentAmount / 2), list_Show);
			follow_btn_public.setBackgroundResource(R.color.bet_btn_text);
			if (!heheda) {
				follow_btn_toend
						.setBackgroundResource(R.drawable.follow_btn_bg);
				follow_btn_afterwin
						.setBackgroundResource(R.drawable.follow_btn_bg);
			}
			show();
			break;

		case R.id.follow_btn_toend:
			if (!heheda) {
				youhua_type = 2;
				setDate();
				setbei((int) (currentAmount / 2), list_Show);
				follow_btn_public
						.setBackgroundResource(R.drawable.follow_btn_bg);
				follow_btn_toend.setBackgroundResource(R.color.bet_btn_text);
				follow_btn_afterwin
						.setBackgroundResource(R.drawable.follow_btn_bg);
				show();
			} else {
				MyToast.getToast(this, "暂不支持组合玩法");
			}
			break;

		case R.id.follow_btn_afterwin:
			if (!heheda) {
				youhua_type = 3;
				setDate();
				setbei((int) (currentAmount / 2), list_Show);
				follow_btn_public
						.setBackgroundResource(R.drawable.follow_btn_bg);
				follow_btn_toend
						.setBackgroundResource(R.drawable.follow_btn_bg);
				follow_btn_afterwin.setBackgroundResource(R.color.bet_btn_text);
				show();
			} else {
				MyToast.getToast(this, "暂不支持组合玩法");
			}
			break;

		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_help:
			Intent intent = new Intent(Bonus_JCZQ_Activity.this,
					BonusHelpActivity.class);
			intent.putExtra("type", 1);
			Bonus_JCZQ_Activity.this.startActivity(intent);
			break;
		case R.id.btn_submit:
			long money = currentAmount
					* Integer.parseInt(sum_bei.getText().toString().equals("") ? "0"
							: sum_bei.getText().toString());
			if (currentAmount > list_Show.size() * 2) {
				if(money < 10) {
					MyToast.getToast(context, "投注金额不能低于10元");
				}else{
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
				}
			} else {
				MyToast.getToast(this, "必须1倍以上金额优化");
			}
			break;
		case R.id.btn_join:
			intent = new Intent(Bonus_JCZQ_Activity.this, JoinActivity.class);
			intent.putExtra(
					"totalMoney",
					currentAmount
							* Integer.parseInt(sum_bei.getText().toString())
							+ "");
			intent.putExtra("join_type", 1);
			Bonus_JCZQ_Activity.this.startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		if (AppTools.list_numbers.size() > 0) {
			if (AppTools.user != null) {
				setEnabled(false);
				final long totalMoney = currentAmount
						* Integer.parseInt(sum_bei.getText().toString()
								.equals("") ? "0" : sum_bei.getText()
								.toString());
				RequestUtil requestUtil = new RequestUtil(context, false, 0,
						true, "正在支付...") {
					@Override
					public void responseCallback(JSONObject object) {
						setEnabled(true);
						if (RequestUtil.DEBUG)
							Log.i(TAG, "竞彩足球投注支付结果" + object);
						String error = object.optString("error");
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
							// 清空所有数据
							AppTools.list_numbers.clear();
							AppTools.totalCount = 0;
							Bet_JCZQ_Activity.clear();
							Intent intent = new Intent(getApplicationContext(),
									PaySuccessActivity.class);
							intent.putExtra("paymoney", totalMoney);

							intent.putExtra("currentMoeny", SucJinECost);
							intent.putExtra("currentHandsel", SucCaiJinCost);
							intent.putExtra("handselMoney", SuccMoney);
							intent.putExtra("voucherMoney", "0.00");
							Bonus_JCZQ_Activity.this.startActivity(intent);
						} else if (error.equals(AppTools.ERROR_MONEY + "")) {
							LotteryUtils.showMoneyLess(
									Bonus_JCZQ_Activity.this, totalMoney);
						} else if (error.equals("-500")) {
							MyToast.getToast(Bonus_JCZQ_Activity.this, "连接超时")
									;
						} else {
							Toast.makeText(Bonus_JCZQ_Activity.this,
									"网络异常，购买失败。请重新点击付款购买。", Toast.LENGTH_SHORT)
									;
						}
					}

					@Override
					public void responseError(VolleyError error) {
						setEnabled(true);
						MyToast.getToast(Bonus_JCZQ_Activity.this,
								"抱歉，支付出现未知错误..");
						if (RequestUtil.DEBUG)
							Log.e(TAG, "投注支付报错" + error.getMessage());
					}
				};
				AppTools.list_numbers.get(0).setCount(list_Show.size());// 设置投注注数
				AppTools.list_numbers.get(0).setMoney(totalMoney);// 设置投注钱数
				AppTools.bei = Integer.parseInt(sum_bei.getText().toString());
				requestUtil.commitBetting_jc_voptimization(totalMoney, 1,
						list_Show, youhua_type, "", "", 0, 1, 1, 0, 0);
			} else {
				MyToast.getToast(Bonus_JCZQ_Activity.this, "请先登陆");
				intent = new Intent(Bonus_JCZQ_Activity.this,
						LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bonus_JCZQ_Activity.this.startActivity(intent);
			}
		} else {
			MyToast.getToast(Bonus_JCZQ_Activity.this, "请至少选择一注");
		}
	}

	/**
	 * 设置控件是否可用
	 * 
	 */
	private void setEnabled(boolean isEna) {
		// btn_submit.setEnabled(isEna);
		// bet_btn_deleteall.setEnabled(isEna);
		// btn_continue_select.setEnabled(isEna);
		// btn_follow.setEnabled(isEna);
		// bet_cb_stopfollow.setEnabled(isEna);
		// et_bei.setEnabled(isEna);
	}

	/**
	 * 显示界面数据
	 */
	private void show() {
		editAmount.setText(currentAmount + "");
		adapetr.notifyDataSetChanged();
		summoney.setText("总金额："
				+ currentAmount
				* Integer
						.parseInt(sum_bei.getText().toString().equals("") ? "0"
								: sum_bei.getText().toString()) + "元");
		show_win();
	}

	/**
	 * 显示变化的奖金数据
	 */
	private void show_win() {
		double min_win = Double.parseDouble(list_Show.get(0).getWinmony());
		if (list_Show.size() > 0) {
			min_win = 10000000000.00;
			for (int i = 0; i < list_Show.size(); i++) {
				if (list_Show.get(i).getSum_peilv() * 2
						* list_Show.get(i).getBei() < min_win)
					min_win = list_Show.get(i).getSum_peilv() * 2
							* list_Show.get(i).getBei();
			}
		}
		win_fanwei.setText("奖金范围"
				+ df.format(min_win
						* Integer.parseInt(sum_bei.getText().toString()
								.equals("") ? "0" : sum_bei.getText()
								.toString()))
				+ "~"
				+ df.format(getmax_()
						* Integer.parseInt(sum_bei.getText().toString()
								.equals("") ? "0" : sum_bei.getText()
								.toString())));
		// 设置赔率
		for (int i = 0; i < list_Show.size(); i++) {
//			list_Show.get(i).setWinmony(
//					(int) (list_Show.get(i).getBei()
//							* list_Show.get(i).getSum_peilv() * 2 * 100)
//							/ 100.00 + "");
			list_Show.get(i).setWinmony(
					df.format((list_Show.get(i).getBei()
					* list_Show.get(i).getSum_peilv() * 2 * 100)
					/ 100.00));
		}
	}

	/**
	 * 得到最大优化奖金
	 * 
	 * @return
	 */
	private Double getmax_() {
		Double money = 0.0;
		List<Show_JC_Details> list_Show2 = new ArrayList<Show_JC_Details>();
		List<Show_JC_Details> list_Show3 = new ArrayList<Show_JC_Details>();
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < list_Show.size(); i++) {
			list_Show2.add(list_Show.get(i));
		}
		int s = list_Show2.size();
		for (int p = 0; p < s; p++) {

			for (int j = 0; j < list_Show2.size(); j++) {
				for (int i = 0; i < list_Show2.size() - 1; i++) {
					if (Double.parseDouble(list_Show2.get(i).getWinmony()) < Double
							.parseDouble(list_Show2.get(i + 1).getWinmony())) {
						Show_JC_Details jc = list_Show2.get(i);
						list_Show2.set(i, list_Show2.get(i + 1));
						list_Show2.set(i + 1, jc);
					}
				}
			}
			if (list_Show2.size() != 0) {
				Show_JC_Details show_jc = list_Show2.get(0);
				for (int i = 0; i < show_jc.getList_JC_Details().size(); i++) {
					map.put(show_jc.getList_JC_Details().get(i).getMatchID(),
							show_jc.getList_JC_Details().get(i).getPeilv());
				}
				list_Show3.add(list_Show2.get(0));
				list_Show2.remove(0);
			}
//			for (int i = 0; i < list_Show2.size(); i++) {
//				Show_JC_Details jc = list_Show2.get(i);
//				for (int j = 0; j < jc.getList_JC_Details().size(); j++) {
//					if (map.containsKey(jc.getList_JC_Details().get(j)
//							.getMatchID())) {
//						if (!map.get(
//								jc.getList_JC_Details().get(j).getMatchID())
//								.equals(jc.getList_JC_Details().get(j)
//										.getPeilv())) {
//							list_Show2.remove(i);
//							break;
//						}
//					}
//				}
//			}
		}
		for (int i = 0; i < list_Show3.size(); i++) {
			money += Double.parseDouble(list_Show3.get(i).getWinmony());
		}
		if (max_ > money)
			return max_;
		return money;
	}

	/**
	 * 根据玩法参数计算奖金
	 */
	private void setDate() {
		list_Show.clear();
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("A0")) {
			add_dg();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("AA")) {
			add2x1();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("AB")) {
			add3x1();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("AE")) {
			add4x1();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("AJ")) {
			add5x1();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("AQ")) {
			add6x1();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("BA")) {
			add7x1();
		}
		if (AppTools.list_numbers.get(0).getLotteryNumber().split(";")[2]
				.contains("BG")) {
			add8x1();
		}
		list_Show = getshuj(list_Show);
		//
		MinAmount = list_Show.size() * 2;
		tickets = new ArrayList<TicketAssign>();
		for (int i = 0; i < list_Show.size(); i++) {
			TicketAssign tk = new TicketAssign();
			tk.setId(i);
			tk.setWeight((float) list_Show.get(i).getSum_peilv());
			tickets.add(tk);
		}
	}

	/**
	 * 筛选类型不是奖金最多的数据
	 * 
	 * @param array
	 *            ：对阵集合
	 * @return
	 */
	public List<Show_JC_Details> getshuj(List<Show_JC_Details> array) {
		Show_JC_Details aa;
		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < array.size() - 1; j++) {
				if (array.get(j).getSum_peilv() > array.get(j + 1)
						.getSum_peilv()) {
					aa = array.get(j);
					array.set(j, array.get(j + 1));
					array.set(j + 1, aa);
				}

			}
		}
		return array;
	}

	/**
	 * 适配器
	 * 
	 * @author lenovo
	 * 
	 */
	class Show_Adapter extends BaseAdapter {
		Context context;
		List<Show_JC_Details> list;

		public Show_Adapter(Context context, List<Show_JC_Details> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int arg0) {

			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int index, View view, ViewGroup arg2) {

			final ViewHolder holder;
			// 判断View是否为空
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				view = inflater.inflate(R.layout.jc_optimization_items, null);
				// 得到控件
				holder.ll_info = (LinearLayout) view.findViewById(R.id.ll_info);
				holder.tv_pass = (TextView) view.findViewById(R.id.tv_1);
				holder.tv1 = (TextView) view.findViewById(R.id.tv_2_1);
				holder.tv2 = (TextView) view.findViewById(R.id.tv_2_2);
				holder.imup = (ImageView) view.findViewById(R.id.im_2_3);
				holder.tv_bei = (EditText) view.findViewById(R.id.ed_3);
				holder.follow_btn_sub_yj = (Button) view
						.findViewById(R.id.follow_btn_sub_yj);
				holder.follow_btn_add_yj = (Button) view
						.findViewById(R.id.follow_btn_add_yj);
				holder.tv_money = (TextView) view.findViewById(R.id.tv_4);
				holder.tiem_line = (TextView) view.findViewById(R.id.tiem_line);
				holder.gone = (LinearLayout) view.findViewById(R.id.gone);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_pass.setText(list.get(index).getPass());

			holder.tv1.setText(list.get(index).getList_result().get(0)
					.split(",")[1]
					+ "|"
					+ list.get(index).getList_result().get(0).split(",")[3]);
			if (!list.get(index).getPass().equals("单关"))
				holder.tv2
						.setText(list.get(index).getList_result().get(1)
								.split(",")[1]
								+ "|"
								+ list.get(index).getList_result().get(1)
										.split(",")[3]);

			holder.tv_bei.setText(list.get(index).getBei() + "");
			holder.tv_money.setText(list.get(index).getWinmony() + "");
			holder.gone.removeAllViews();
			for (int i = 0; i < list.get(index).getList_result().size(); i++) {
				String[] gong_list = list.get(index).getList_result().get(i)
						.split(",");
				LayoutInflater inflater = LayoutInflater.from(context);
				View view2 = inflater.inflate(
						R.layout.jc_optimization_gone_items, null);
				TextView lotteryNumber = (TextView) view2
						.findViewById(R.id.tv_1);
				TextView mainteam = (TextView) view2.findViewById(R.id.tv_2);
				TextView guesteam = (TextView) view2.findViewById(R.id.tv_3);
				TextView result_peilv = (TextView) view2
						.findViewById(R.id.tv_4);
				lotteryNumber.setText(gong_list[0]);
				mainteam.setText(gong_list[1]);
				guesteam.setText(gong_list[2]);
				result_peilv.setText(gong_list[3] + " "
						+ df.format(Double.parseDouble(gong_list[4])));
				holder.gone.addView(view2);
			}

			// 为editText设置TextChangedListener，每次改变的值设置到hashMap
			// 我们要拿到里面的值根据position拿值
			holder.tv_bei.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					// 将editText中改变的值设置的HashMap中
				}
			});

			holder.ll_info.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					if (holder.gone.isShown()) {
						holder.gone.setVisibility(View.GONE);
						holder.tiem_line.setVisibility(View.GONE);
						holder.imup
								.setBackgroundResource(R.drawable.win_lottery_detail_down);
					} else {
						holder.gone.setVisibility(View.VISIBLE);
						holder.tiem_line.setVisibility(View.VISIBLE);
						holder.imup
								.setBackgroundResource(R.drawable.win_lottery_detail_up);
					}
				}
			});

			holder.follow_btn_sub_yj.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					editAmount
							.removeTextChangedListener(title_money_textWatcher);
					if (list_Show.get(index).getBei() > 1) {
						list_Show.get(index).setBei(
								list_Show.get(index).getBei() - 1);
						Double aa = 1.00;
						for (int i = 0; i < list_Show.get(index)
								.getList_JC_Details().size(); i++) {
							aa *= Double.parseDouble(list_Show.get(index)
									.getList_JC_Details().get(i).getPeilv());
						}
//						list_Show
//								.get(index)
//								.setWinmony(
//										(int) (aa * 2
//												* list_Show.get(index).getBei() * 100)
//												/ 100.00 + "");
						list_Show
								.get(index)
								.setWinmony(
								df.format((aa * 2
										* list_Show.get(index).getBei() * 100)
										/ 100.00));
						currentAmount -= 2;
						follow_btn_public
								.setBackgroundResource(R.drawable.follow_btn_bg);
						follow_btn_toend
								.setBackgroundResource(R.drawable.follow_btn_bg);
						follow_btn_afterwin
								.setBackgroundResource(R.drawable.follow_btn_bg);
					}
					show_();
					editAmount.addTextChangedListener(title_money_textWatcher);
				}
			});
			holder.follow_btn_add_yj.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					editAmount
							.removeTextChangedListener(title_money_textWatcher);
					list_Show.get(index).setBei(
							list_Show.get(index).getBei() + 1);
					Double aa = 1.00;
					for (int i = 0; i < list_Show.get(index)
							.getList_JC_Details().size(); i++) {
						aa *= Double.parseDouble(list_Show.get(index)
								.getList_JC_Details().get(i).getPeilv());
					}
//					list_Show
//							.get(index)
//							.setWinmony(
//									(int) (aa * 2
//											* list_Show.get(index).getBei() * 100)
//											/ 100.00 + "");
					list_Show
					.get(index)
					.setWinmony(df.format((aa * 2
							* list_Show.get(index).getBei() * 100)
							/ 100.00));
					currentAmount += 2;
					follow_btn_public
							.setBackgroundResource(R.drawable.follow_btn_bg);
					follow_btn_toend
							.setBackgroundResource(R.drawable.follow_btn_bg);
					follow_btn_afterwin
							.setBackgroundResource(R.drawable.follow_btn_bg);
					show_();
					editAmount.addTextChangedListener(title_money_textWatcher);
				}
			});
			return view;
		}

		private void show_() {
			editAmount.setText(currentAmount + "");
			adapetr.notifyDataSetChanged();
			summoney.setText("总金额："
					+ currentAmount
					* Integer
							.parseInt(sum_bei.getText().toString().equals("") ? "0"
									: sum_bei.getText().toString()) + "元");

			double min_win = Double.parseDouble(list_Show.get(0).getWinmony());
			if (list_Show.size() > 0) {
				min_win = 10000000000.00;
				for (int i = 0; i < list_Show.size(); i++) {
					if (list_Show.get(i).getSum_peilv() * 2
							* list_Show.get(i).getBei() < min_win)
						min_win = list_Show.get(i).getSum_peilv() * 2
								* list_Show.get(i).getBei();
				}
			}
			win_fanwei.setText("奖金范围"
					+ df.format(min_win
							* Integer.parseInt(sum_bei.getText().toString()
									.equals("") ? "0" : sum_bei.getText()
									.toString()))
					+ "~"
					+ df.format(getmax_()
							* Integer.parseInt(sum_bei.getText().toString()
									.equals("") ? "0" : sum_bei.getText()
									.toString())));
		}
	}

	/**
	 * 静态类
	 * 
	 */
	static class ViewHolder {
		LinearLayout ll_info;
		TextView tv_pass;
		TextView tv1, tv2;
		ImageView imup;
		EditText tv_bei;
		TextView tv_money;
		TextView tiem_line;
		LinearLayout gone;
		Button follow_btn_sub_yj;
		Button follow_btn_add_yj;
	}

	/**
	 * 根据类型区分奖金优化数据
	 * 
	 * @param type
	 *            ：优化类型
	 * @param totalTickets
	 *            ：倍数
	 */
	public void extremeBonusAllocate(int type, int totalTickets) {
		for (int j = 0; j < list_Show.size(); j++) {
			list_Show.get(j).setBei(1);
//			list_Show.get(j).setWinmony(
//					list_Show.get(j).getBei() * list_Show.get(j).getSum_peilv()
//							* 2 + "");
			list_Show.get(j).setWinmony(
					df.format(list_Show.get(j).getBei() * list_Show.get(j).getSum_peilv()
							* 2));
		}
		for (int i = 0; i < totalTickets - list_Show.size(); i++) {
			int min = 0;
			int max = 0;
			int min_pei = 0;
			int max_pei = 0;
			for (int j = 0; j < list_Show.size() - 1; j++) {
				for (int k = 0; k < list_Show.size(); k++) {
//					list_Show.get(k).setWinmony(
//							list_Show.get(k).getBei()
//									* list_Show.get(k).getSum_peilv() * 2 + "");
					list_Show.get(k).setWinmony(df.format(
							list_Show.get(k).getBei()
							* list_Show.get(k).getSum_peilv() * 2));
				}
				if (Double.parseDouble(list_Show.get(max).getWinmony().trim()) < Double
						.parseDouble(list_Show.get(j + 1).getWinmony().trim())) {
					max = j + 1;
				}
				if (Double.parseDouble(list_Show.get(min).getWinmony().trim()) > Double
						.parseDouble(list_Show.get(j + 1).getWinmony().trim())) {
					min = j + 1;
				}
				if (list_Show.get(max_pei).getSum_peilv() < list_Show
						.get(j + 1).getSum_peilv()) {
					max_pei = j + 1;
				}
				if (list_Show.get(min_pei).getSum_peilv() > list_Show
						.get(j + 1).getSum_peilv()) {
					min_pei = j + 1;
				}
			}

			if (Double.parseDouble(list_Show.get(min).getWinmony().trim()) < totalTickets * 2
					|| type == 1) {
				list_Show.get(min).setBei(list_Show.get(min).getBei() + 1);
			} else {
				switch (type) {
				case 2:
					list_Show.get(min_pei).setBei(
							list_Show.get(min_pei).getBei() + 1);
					break;
				case 3:
					list_Show.get(max_pei).setBei(
							list_Show.get(max_pei).getBei() + 1);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 得到各个奖金优化数据
	 * 
	 * @param bei
	 * @param array
	 *            ：对阵集合
	 */
	private void setbei(int bei, List<Show_JC_Details> array) {
		int index = 0;
		double a = 0;
		int size = array.size();
		int[] inta = new int[size];
		for (int i = 0; i < inta.length; i++) {
			inta[i] = 1;
		}
		extremeBonusAllocate(youhua_type, bei);
	}

	/**
	 * 当文本的值改变时
	 */
	private TextWatcher title_money_textWatcher = new TextWatcher() {
		private int amount;

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
			if (edt.length() == 0) {
				amount = MinAmount;
			} else {
				amount = Math.max(Integer.parseInt(edt.toString()), MinAmount);
			}
			if (amount % 2 > 0) {
				amount--;
			}
			editAmount.setSelection(edt.length());
			currentAmount = amount;
			setbei((int) (amount / 2), list_Show);
			adapetr.notifyDataSetChanged();
			summoney.setText("总金额："
					+ currentAmount
					* Integer
							.parseInt(sum_bei.getText().toString().equals("") ? "0"
									: sum_bei.getText().toString()) + "元");
			show_win();

		}
	};

	/**
	 * 设置输入框焦点
	 * 
	 * @param et
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/**
	 * 
	 * 当倍数文本的值改变时
	 */
	private TextWatcher sunbei_textWatcher = new TextWatcher() {
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
					sum_bei.setSelection(sum_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为999")
							;
					sum_bei.setText("999");
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					sum_bei.setText("1");
					sum_bei.setSelection(sum_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最低一倍");
				} else
					show();
			}
		}
	};

	/**
	 * 添加list的基础方法
	 * 
	 * @param array
	 * @return
	 */
	private List<JC_Details> add_jc_de(int[] array) {
		List<JC_Details> list = new ArrayList<JC_Details>();
		for (int i = 0; i < array.length; i++) {
			list.add(Bet_JCZQ_Activity.list_JC_Details.get(array[i]));
		}
		return list;
	}

	// 单关
	private void add_dg() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size(); j++) {
			Show_JC_Details show_details = new Show_JC_Details();
			show_details.setPass("单关");
			show_details.setList_result(addlist(new int[] { j }));
			show_details.setSum_peilv(getpeilv(new int[] { j }));
			show_details.setList_JC_Details(add_jc_de(new int[] { j }));
			list_Show.add(show_details);
		}
	}

	/**
	 * 2串1的奖金分布算法
	 */
	private void add2x1() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 1; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size(); j2++) {
				if (!getboolean(new int[] { j, j2 })) {
					continue;
				}
				Show_JC_Details show_details = new Show_JC_Details();
				show_details.setPass("2串1");
				show_details.setList_result(addlist(new int[] { j, j2 }));
				show_details.setSum_peilv(getpeilv(new int[] { j, j2 }));
				show_details.setList_JC_Details(add_jc_de(new int[] { j, j2 }));
				list_Show.add(show_details);
			}
		}
	}

	/**
	 * 3串1的奖金分布算法
	 */
	private void add3x1() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 2; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size() - 1; j2++) {
				for (int j3 = j2 + 1; j3 < Bet_JCZQ_Activity.list_JC_Details
						.size(); j3++) {
					if (!getboolean(new int[] { j, j2, j3 })) {
						continue;
					}
					Show_JC_Details show_details = new Show_JC_Details();
					show_details.setPass("3串1");
					show_details
							.setList_result(addlist(new int[] { j, j2, j3 }));
					show_details
							.setSum_peilv(getpeilv(new int[] { j, j2, j3 }));
					show_details.setList_JC_Details(add_jc_de(new int[] { j,
							j2, j3 }));
					list_Show.add(show_details);
				}
			}
		}

	}

	/**
	 * 4串1的奖金分布算法
	 */
	private void add4x1() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 3; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size() - 2; j2++) {
				for (int j3 = j2 + 1; j3 < Bet_JCZQ_Activity.list_JC_Details
						.size() - 1; j3++) {
					for (int j4 = j3 + 1; j4 < Bet_JCZQ_Activity.list_JC_Details
							.size(); j4++) {
						if (!getboolean(new int[] { j, j2, j3, j4 })) {
							continue;
						}
						Show_JC_Details show_details = new Show_JC_Details();
						show_details.setPass("4串1");
						show_details.setList_result(addlist(new int[] { j, j2,
								j3, j4 }));
						show_details.setList_JC_Details(add_jc_de(new int[] {
								j, j2, j3, j4 }));
						show_details.setSum_peilv(getpeilv(new int[] { j, j2,
								j3, j4 }));
						list_Show.add(show_details);
					}

				}
			}
		}

	}

	/**
	 * 5串1的奖金分布算法
	 */
	private void add5x1() {
		int l = 0;
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 4; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size() - 3; j2++) {
				for (int j3 = j2 + 1; j3 < Bet_JCZQ_Activity.list_JC_Details
						.size() - 2; j3++) {
					for (int j4 = j3 + 1; j4 < Bet_JCZQ_Activity.list_JC_Details
							.size() - 1; j4++) {
						for (int j5 = j4 + 1; j5 < Bet_JCZQ_Activity.list_JC_Details
								.size(); j5++) {
							if (!getboolean(new int[] { j, j2, j3, j4, j5 })) {
								continue;
							}
							Show_JC_Details show_details = new Show_JC_Details();
							show_details.setPass("5串1");
							show_details.setList_result(addlist(new int[] { j,
									j2, j3, j4, j5 }));
							show_details
									.setList_JC_Details(add_jc_de(new int[] {
											j, j2, j3, j4, j5 }));
							show_details.setSum_peilv(getpeilv(new int[] { j,
									j2, j3, j4, j5 }));
							list_Show.add(show_details);
						}
					}
				}
			}
		}
	}

	/**
	 * 6串1的奖金分布算法
	 */
	private void add6x1() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 5; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size() - 4; j2++) {
				for (int j3 = j2 + 1; j3 < Bet_JCZQ_Activity.list_JC_Details
						.size() - 3; j3++) {
					for (int j4 = j3 + 1; j4 < Bet_JCZQ_Activity.list_JC_Details
							.size() - 2; j4++) {
						for (int j5 = j4 + 1; j5 < Bet_JCZQ_Activity.list_JC_Details
								.size() - 1; j5++) {
							for (int j6 = j5 + 1; j6 < Bet_JCZQ_Activity.list_JC_Details
									.size(); j6++) {
								if (!getboolean(new int[] { j, j2, j3, j4, j5,
										j6 })) {
									continue;
								}
								Show_JC_Details show_details = new Show_JC_Details();
								show_details.setPass("6串1");
								show_details.setList_result(addlist(new int[] {
										j, j2, j3, j4, j5, j6 }));
								show_details
										.setList_JC_Details(add_jc_de(new int[] {
												j, j2, j3, j4, j5, j6 }));
								show_details.setSum_peilv(getpeilv(new int[] {
										j, j2, j3, j4, j5, j6 }));
								list_Show.add(show_details);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 7串1的奖金分布算法
	 */
	private void add7x1() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 6; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size() - 5; j2++) {
				for (int j3 = j2 + 1; j3 < Bet_JCZQ_Activity.list_JC_Details
						.size() - 4; j3++) {
					for (int j4 = j3 + 1; j4 < Bet_JCZQ_Activity.list_JC_Details
							.size() - 3; j4++) {
						for (int j5 = j4 + 1; j5 < Bet_JCZQ_Activity.list_JC_Details
								.size() - 2; j5++) {
							for (int j6 = j5 + 1; j6 < Bet_JCZQ_Activity.list_JC_Details
									.size() - 1; j6++) {
								for (int j7 = j6 + 1; j7 < Bet_JCZQ_Activity.list_JC_Details
										.size(); j7++) {
									if (!getboolean(new int[] { j, j2, j3, j4,
											j5, j6, j7 })) {
										continue;
									}
									Show_JC_Details show_details = new Show_JC_Details();
									show_details.setPass("7串1");
									show_details
											.setList_result(addlist(new int[] {
													j, j2, j3, j4, j5, j6, j7 }));
									show_details
											.setList_JC_Details(add_jc_de(new int[] {
													j, j2, j3, j4, j5, j6, j7 }));
									show_details
											.setSum_peilv(getpeilv(new int[] {
													j, j2, j3, j4, j5, j6, j7 }));
									list_Show.add(show_details);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 8串1的奖金分布算法
	 */
	private void add8x1() {
		for (int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size() - 7; j++) {
			for (int j2 = j + 1; j2 < Bet_JCZQ_Activity.list_JC_Details.size() - 6; j2++) {
				for (int j3 = j2 + 1; j3 < Bet_JCZQ_Activity.list_JC_Details
						.size() - 5; j3++) {
					for (int j4 = j3 + 1; j4 < Bet_JCZQ_Activity.list_JC_Details
							.size() - 4; j4++) {
						for (int j5 = j4 + 1; j5 < Bet_JCZQ_Activity.list_JC_Details
								.size() - 3; j5++) {
							for (int j6 = j5 + 1; j6 < Bet_JCZQ_Activity.list_JC_Details
									.size() - 2; j6++) {
								for (int j7 = j6 + 1; j7 < Bet_JCZQ_Activity.list_JC_Details
										.size() - 1; j7++) {
									for (int j8 = j7 + 1; j8 < Bet_JCZQ_Activity.list_JC_Details
											.size(); j8++) {
										if (!getboolean(new int[] { j, j2, j3,
												j4, j5, j6, j7, j8 })) {
											continue;
										}
										Show_JC_Details show_details = new Show_JC_Details();
										show_details.setPass("8串1");
										show_details
												.setList_result(addlist(new int[] {
														j, j2, j3, j4, j5, j6,
														j7, j8 }));
										show_details
												.setList_JC_Details(add_jc_de(new int[] {
														j, j2, j3, j4, j5, j6,
														j7, j8 }));
										show_details
												.setSum_peilv(getpeilv(new int[] {
														j, j2, j3, j4, j5, j6,
														j7, j8 }));
										list_Show.add(show_details);
									}
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * 判断是否加入赔率集合
	 * 
	 * @param pass
	 *            ：赔率集合
	 * @return
	 */
	public boolean getboolean(int[] pass) {
		boolean flag = true;
		for (int i = 0; i < pass.length - 1; i++) {
			for (int j = i + 1; j < pass.length; j++) {
				if (Bet_JCZQ_Activity.list_JC_Details
						.get(pass[i])
						.getMatchID()
						.equals(Bet_JCZQ_Activity.list_JC_Details.get(pass[j])
								.getMatchID())) {
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * 得到选号结果
	 * 
	 * @param index
	 *            ：下标集合
	 * @return
	 */
	private List<String> addlist(int[] index) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < index.length; i++) {
			list.add(Bet_JCZQ_Activity.list_JC_Details.get(index[i])
					.getMatchNumber()
					+ ","
					+ Bet_JCZQ_Activity.list_JC_Details.get(index[i])
							.getMainTeam()
					+ ","
					+ Bet_JCZQ_Activity.list_JC_Details.get(index[i])
							.getGuestTeam()
					+ ","
					+ Bet_JCZQ_Activity.list_JC_Details.get(index[i])
							.getShowresult()
					+ ","
					+ Bet_JCZQ_Activity.list_JC_Details.get(index[i])
							.getPeilv()
					+ ","
					+ Bet_JCZQ_Activity.list_JC_Details.get(index[i])
							.getMatchID());
		}
		return list;
	}

	/**
	 * 得到赔率算法
	 * 
	 * @param index
	 *            ：下标集合
	 * @return
	 */
	private double getpeilv(int[] index) {
		BigDecimal pei = new BigDecimal(1);
		for (int i = 0; i < index.length; i++) {
			pei = pei.multiply(new BigDecimal((Bet_JCZQ_Activity.list_JC_Details.get(
					index[i]).getPeilv())));
		}
		return pei.doubleValue();
	}
}
