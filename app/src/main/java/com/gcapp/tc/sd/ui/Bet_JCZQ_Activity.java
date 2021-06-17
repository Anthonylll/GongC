package com.gcapp.tc.sd.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.DtMatch;
import com.gcapp.tc.dataaccess.JC_Details;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapterJCZQPassMore;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapterJCZQPassSingle;
import com.gcapp.tc.sd.ui.adapter.MyBetLotteryJCZQAdapter;
import com.gcapp.tc.sd.ui.adapter.SelectPasstype_List_JCAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.ColorUtil;
import com.gcapp.tc.utils.JiangjinTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：竞彩足球投注页面
 * 
 * @author Kinwee
 */
public class Bet_JCZQ_Activity extends Activity implements OnClickListener {

	private Context mContext = Bet_JCZQ_Activity.this;
	private final static String TAG = "Bet_JCZQ_Activity";
	private LinearLayout ll_pass2;
	// 新添加的
	private ListView passType_list;
	private SelectPasstype_List_JCAdapter listAdapter;
	/* 头部 */
//	private RelativeLayout layout_main;// 主
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框
	private ConfirmDialog orderdialog;
	/* 尾部 */
	private TextView btn_follow; // 发起合买
	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 付款
	public TextView tv_tatol_count;// 总注数
	public TextView tv_jiangjin;// 总奖金
	private static LinearLayout layout_tip_jiangjin;
	private RelativeLayout coupon_layout;
	private LinearLayout layout1;
	private TextView coupon_text;
	public TextView tv_tatol_money;// 总钱数

//	private String opt = "11"; // 格式化后的 opt
//	private String auth, info, time, imei, crc; // 格式化后的参数
	private LinearLayout btn_continue_select;// 继续选择比赛
	private ImageView bet_btn_deleteall;// 清空
	private TextView bet_tv_guize; // 委托投注规则
	private RelativeLayout layout_notjc;// 非竞彩布局
	private LinearLayout layout_jc;// 竞彩布局
	private TextView tv_show_passway, tv_show_passway2, tv_show_passway3;// 过关
	private RelativeLayout layout_cbs;// 复选框布局
	private ImageView line_shade2;
	private MyListView2 bet_lv_scheme;// 自定义listview
	public MyBetLotteryJCZQAdapter adapter;
	private Intent intent;
	/** 2比分；5混合投注*/
	private int playtype;
	private int passtype;
	private long sumCount, totalMoney; // 方案总注数 // 方案总金额
	private EditText et_bei; // 用户输入的倍数

//	private java.text.DecimalFormat df;
	private CheckBox bet_cb_stopfollow;
	private int isStopChase = 1;
//	private Double max_ = 0.0;
	/** 选择的过关方式*/
	private static ArrayList<String> selectPasstype = new ArrayList<String>();
	private static int viewPagerCurrentIndex = 0;// 过关类型 0.多串1 1.多串多
	private List<String> eachSelectDT;
	private int countDan;// 胆的个数
	private int dtCount;// 获取已选择的比赛场次
	public String passStr = "";
	private DecimalFormat format = new DecimalFormat("#####0.00");
	private RelativeLayout rlBonus;
	// 返回数据源
	public List<DtMatch> newListMatchs = new ArrayList<DtMatch>();
//	private StringBuffer buffer = new StringBuffer();
//	private Map<Integer, ArrayList<String>> maps = new HashMap<Integer, ArrayList<String>>();
	public static ArrayList<JC_Details> list_JC_Details;
//	private List<Show_JC_Details> list_Show = new ArrayList<Show_JC_Details>();// 用来计算奖金的
	private Map<String, String> max_peilv = new HashMap<String, String>();
	private Map<String, String> min_peilv = new HashMap<String, String>();
	private List<Double> list_max_jiangjin = new ArrayList<Double>();
	private List<Double> list_min_jiangjin = new ArrayList<Double>();
	private double max_jiangjin, min_jiangjin;

	public String[] showData = new String[] { "3串3", "3串4", "4串4", "4串5",
			"4串6", "4串11", "5串5", "5串6", "5串10", "5串16", "5串20", "5串26", "6串6",
			"6串7", "6串15", "6串20", "6串22", "6串35", "6串42", "6串50", "6串57",
			"7串7", "7串8", "7串21", "7串35", "7串120", "8串8", "8串9", "8串28",
			"8串56", "8串70", "8串247" };
	/** 最大投注倍数*/
	private int maxMultiple = 9999;
	/** intent回传值*/
	private int Result_OK = 1;
	/** 优惠券Id*/
	private String voucherID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet_jc);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		if (selectPasstype != null) {
			selectPasstype.clear();
		}
		
		init();
		setListener();
		if (1 == passtype) {// 单关
			initPassType();// 初始化过关方式
			setSelectNumAndGetCount();// 设置投注格式和注数
		} else {
			selectPassType();
		}
		changeTextShow();
		getJiangjin();
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		passType_list = (ListView) findViewById(R.id.select_middle);
		passType_list.setVisibility(View.GONE);
		ll_pass2 = (LinearLayout) findViewById(R.id.ll_pass2);
		listAdapter = new SelectPasstype_List_JCAdapter(Bet_JCZQ_Activity.this,
				countDan, dtCount, viewPagerCurrentIndex);
		passType_list.setAdapter(listAdapter);
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
		btn_follow = (TextView) this.findViewById(R.id.btn_follow);
		et_bei = (EditText) this.findViewById(R.id.et_bei);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_jiangjin = (TextView) this.findViewById(R.id.tv_jiangjin); // 奖金
		layout_tip_jiangjin = (LinearLayout) findViewById(R.id.layout_tip_jiangjin); // 奖金
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		bet_cb_stopfollow = (CheckBox) this
				.findViewById(R.id.bet_cb_stopfollow);
		bet_tv_guize = (TextView) this.findViewById(R.id.bet_tv_guize);
		bet_lv_scheme = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		layout_notjc = (RelativeLayout) this.findViewById(R.id.layout_notjc);
		layout_jc = (LinearLayout) this.findViewById(R.id.layout_jc);
//		layout_main = (RelativeLayout) this.findViewById(R.id.layout_main);
		coupon_layout = (RelativeLayout) this.findViewById(R.id.coupon_layout);
		layout1 = (LinearLayout) this.findViewById(R.id.layout1);
		coupon_text = (TextView) this.findViewById(R.id.coupon_text);
		tv_show_passway = (TextView) this.findViewById(R.id.tv_show_passway);
		tv_show_passway2 = (TextView) this.findViewById(R.id.tv_show_passway2);
		tv_show_passway3 = (TextView) this.findViewById(R.id.tv_show_passway3);

		tv_show_passway.setVisibility(View.VISIBLE);
		tv_show_passway2.setVisibility(View.VISIBLE);
		tv_show_passway3.setVisibility(View.GONE);
		layout_cbs = (RelativeLayout) this.findViewById(R.id.layout_cbs);
		line_shade2 = (ImageView) this.findViewById(R.id.line_shade2);
		btn_playtype.setText("竞彩足球投注");
//		layoutTop = (RelativeLayout) this.findViewById(R.id.include_top);
		rlBonus = (RelativeLayout) this.findViewById(R.id.rl_bonus);
		rlBonus.setVisibility(View.VISIBLE);

		// 隐藏与显示
		btn_help.setVisibility(View.GONE);
		btn_clearall.setVisibility(View.GONE);
		iv_up_down.setVisibility(View.GONE);
		layout_notjc.setVisibility(View.GONE);
		layout_cbs.setVisibility(View.GONE);
		line_shade2.setVisibility(View.GONE);
		layout_jc.setVisibility(View.VISIBLE);
		if(AppTools.user != null) {
			if(AppTools.user.getIsgreatMan().equals("True")) {
				btn_follow.setVisibility(View.VISIBLE);
				maxMultiple = 99999;
			}
		}
	}

	/**
	 * 初始化属性
	 * 
	 */
	private void init() {
		AppTools.qi = 1;
//		df = new java.text.DecimalFormat("#.00");
		playtype = getIntent().getIntExtra("playtype", 5);
		passtype = getIntent().getIntExtra("passtype", 0);
		adapter = new MyBetLotteryJCZQAdapter(Bet_JCZQ_Activity.this, playtype,
				passtype);
//		if (playtype == 5) {
//			// 混合投注时，隐藏奖金优化
//			rlBonus.setVisibility(View.INVISIBLE);
//		}
		changeTextShow();
		initPassType();
		btn_submit.setText("付款");
		dialog = new ConfirmDialog(this, R.style.dialog);
		orderdialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 初始化过关方式
	 */
	public void initPassType() {
		if (0 == passtype) {// 过关
			if (0 != selectPasstype.size()) {
				Spanned text = Html.fromHtml(AppTools.changeStringColor(
						"#e3393c", passStr));
				tv_show_passway.setText(text);// 过关
				tv_show_passway2.setVisibility(View.GONE);
				ll_pass2.setVisibility(View.GONE);
			} else {
				Spanned text = Html.fromHtml(AppTools.changeStringColor(
						"#808080", "过关方式"));
				tv_show_passway.setText(text);// 过关
				ll_pass2.setVisibility(View.VISIBLE);
			}
			tv_show_passway.setEnabled(true);// 可用
			layout_jc.setEnabled(true);// 可用
		} else {// 单关
			passStr = "单关";
			tv_show_passway.setText(passStr);// 过关
			tv_show_passway.setTextColor(ColorUtil.BET_GRAY);
			ll_pass2.setVisibility(View.GONE);
			tv_show_passway2.setVisibility(View.GONE);
//			tv_show_passway.setEnabled(false);
//			layout_jc.setEnabled(false);
		}
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		btn_continue_select.setOnClickListener(this);
		bet_btn_deleteall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_follow.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		bet_lv_scheme.setAdapter(adapter);
		et_bei.addTextChangedListener(bei_textWatcher);
		bet_tv_guize.setOnClickListener(this);
		tv_show_passway.setOnClickListener(this);
		layout_jc.setOnClickListener(this);
		coupon_layout.setOnClickListener(this);
		rlBonus.setOnClickListener(this);
		bet_cb_stopfollow.setOnCheckedChangeListener(new MyCheckChange());
	}

	/**
	 * 复选框
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
	 * 当文本的值改变时
	 * 
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
				if (Integer.parseInt(edt.toString().trim()) > maxMultiple) {
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为"+maxMultiple);

					et_bei.setText(""+maxMultiple);
				} else if ("0".equals(et_bei.getText().toString().trim())) {
					et_bei.setText("1");
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最小为1倍");
				}

			}

			if (0 != sumCount) {
				setSelectNumAndGetCount();
			}
			changeTextShow();
			getJiangjin();
			setCursorPosition(et_bei);
		}
	};

	/**
	 * 设置焦点
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/**
	 * 公用按钮点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 奖金优化跳转页面 **/
		case R.id.tv_bonus:
		case R.id.rl_bonus:
			if (0 == selectPasstype.size() && passtype != 1) {
				// MyToast.getToast(context, "未选择过关方式");
				selectPassType();
			} else if (passtype == 1
					&& "0".equals(tv_tatol_count.getText().toString().trim())) {
				MyToast.getToast(mContext, "未选择比赛");
			} else if (countDan > 0) {
				MyToast.getToast(mContext, "不支持胆拖玩法");
			} else if (viewPagerCurrentIndex == 1) {
				MyToast.getToast(mContext, "该串关不支持奖金优化");
			} else if (playtype == 7 || playtype == 8) {
				MyToast.getToast(mContext, "该玩法不支持奖金优化");
			} else {
				getBonusOptimization();
				intent = new Intent(Bet_JCZQ_Activity.this,
						Bonus_JCZQ_Activity.class);
				intent.putExtra("multipleFreePass",
						selectPasstype.size() > 1 ? true : false);
				intent.putExtra("totalMoney", totalMoney);
				Bet_JCZQ_Activity.this.startActivity(intent);
			}
			break;

		/** 手选 **/
		case R.id.btn_continue_select:
			if ("0".equals(tv_tatol_count.getText().toString().trim())
					&& adapter.bet_List_Matchs.size() == 0) {
				MyToast.getToast(Bet_JCZQ_Activity.this, "请返回投注界面投注");
				if (0 == passtype) { // 多关
					Intent intent2 = new Intent(Bet_JCZQ_Activity.this,
							Select_JCZQ_Activity.class);
					intent2.putExtra("isEmpty", true);
					Bet_JCZQ_Activity.this.startActivity(intent2);
				} else {
					Intent intent2 = new Intent(Bet_JCZQ_Activity.this,
							Select_JCZQ_DAN_Activity.class);
					intent2.putExtra("isEmpty", true);
					Bet_JCZQ_Activity.this.startActivity(intent2);
				}
				return;
			}
			btn_handClick();
			if (0 == passtype) { // 多关
				selectPasstype.clear();// 清空过关方式
				initPassType();// 初始化过关方式
			}
			break;

		/** 返回 **/
		case R.id.btn_back:
			backToPre();
			break;
		/** 过关方式 **/
		case R.id.tv_show_passway:
		case R.id.layout_jc:
			selectPassType();
			break;

		/** 清空 **/
		case R.id.bet_btn_deleteall:
			dialog.show();
			dialog.setDialogContent("是否清空投注单号码");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						ExpandAdapterJCZQPassMore.clearAllDate();
						ExpandAdapterJCZQPassSingle.clearAllDate();
						AppTools.totalCount = 0;
						adapter.index = new HashMap<Integer, HashMap<Integer, Integer>>();
						adapter.bet_List_Matchs = new ArrayList<DtMatch>();
						adapter.setBetListMatchs();
						adapter.notifyDataSetChanged();
						selectPasstype.clear();// 清空过关方式
						initPassType();// 初始化过关方式
						sumCount = 0;
						layout_tip_jiangjin.setVisibility(View.GONE);
						changeTextShow();
					}
				}
			});
			break;
		/** 付款 **/
		case R.id.btn_submit:
			if (0 == passtype) {// 过关
				if (0 != selectPasstype.size()) {
					// 传递了是否被清空的参数 在选择了过关方式后才有效。。
					if ("0".equals(tv_tatol_count.getText().toString().trim())) {
						MyToast.getToast(Bet_JCZQ_Activity.this, "请返回投注界面投注");
						Intent intent2 = new Intent(Bet_JCZQ_Activity.this,
								Select_JCZQ_Activity.class);
						intent2.putExtra("isEmpty", true);
						Bet_JCZQ_Activity.this.startActivity(intent2);
						return;
					}
					if(AppTools.user != null){
						if(totalMoney >= 10) {
							dealwithCommit();
						}else{
							MyToast.getToast(mContext, "投注金额不能低于10元");
						}
					}else{
						MyToast.getToast(Bet_JCZQ_Activity.this, "请先登陆");
						intent = new Intent(Bet_JCZQ_Activity.this, LoginActivity.class);
						intent.putExtra("loginType", "bet");
						Bet_JCZQ_Activity.this.startActivity(intent);
					}
				} else {// 未选过关方式
					selectPassType();
				}
			} else {// 单关
				if ("0".equals(tv_tatol_count.getText().toString().trim())) {
					MyToast.getToast(Bet_JCZQ_Activity.this, "请至少选择一场比赛");
				} else {
					if(totalMoney >= 10) {
						dealwithCommit();
					}else{
						MyToast.getToast(mContext, "投注金额不能低于10元");
					}
				}

			}
			break;
		/** 发起合买 **/
		case R.id.btn_follow:
			if (0 == passtype) {// 过关
				if (0 != selectPasstype.size()) {
					join();
				} else {// 未选过关方式
					selectPassType();
				}
			} else {// 单关
				join();
			}
			break;
		case R.id.bet_tv_guize:
			Intent intent = new Intent(Bet_JCZQ_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_JCZQ_Activity.this.startActivity(intent);
			break;
		case R.id.coupon_layout:
			if(AppTools.user != null && totalMoney>0){
				Intent intentCoupon = new Intent(Bet_JCZQ_Activity.this,
						CouponActivity.class);
				intentCoupon.putExtra("money",""+totalMoney );
				startActivityForResult(intentCoupon,Result_OK);
			}else if(totalMoney <= 0) {
				MyToast.getToast(mContext, "请选择过关方式!");
			}else{
				Intent intentLogin = new Intent(Bet_JCZQ_Activity.this,LoginActivity.class);
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
			tv_tatol_money.setText(totalMoney-Integer.parseInt(couponMoney)+"");
			voucherID = data.getStringExtra("voucherID");
			et_bei.setEnabled(false);
			rlBonus.setEnabled(false);
			tv_show_passway.setEnabled(false);
			layout_jc.setEnabled(false);
			bet_btn_deleteall.setEnabled(false);
			if (passType_list.getVisibility() == View.VISIBLE) {
				selectPassType();
			}
		}
	}
	
	private void dealwithCommit() {
		orderdialog.show();
		orderdialog.setDialogContent("确认付款？");
		orderdialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) {// 确定
					commit();
				}
			}
		});
	}

	/**
	 * 得到adapter中处理的数据集合
	 * 
	 * @return
	 */
	private List<DtMatch> getList() {
		List<DtMatch> list = new ArrayList<DtMatch>();
		if (adapter != null) {
			list = adapter.getBet_List_Matchs();
			Log.e("Math", list.size() + "");
			return list;
		}
		return null;
	}

	/**
	 * 遍历数据源拆分投注数据
	 */
	private void getBonusData() {
		int playType = 0;
		switch (playtype) {
		case 1:// 让球胜平负
			playType = 7201;
			break;
		case 2:// 比分
			playType = 7202;
			break;
		case 3:// 总进球
			playType = 7203;
			break;
		case 4:// 胜平负
			playType = 7207;
			break;
		case 5:// 混合投注
			playType = 7206;
			break;
		case 6:// 半全场
			playType = 7204;
			break;
		case 7:// 胜平负/让球
			playType = 7208;
			break;
		case 8:// 2选1
			playType = 7209;
			break;
		}
		if (0 == passtype) {// 过关
			for (int i = 0; i < ExpandAdapterJCZQPassMore.list_Matchs.size(); i++) {// 遍历过关所有对阵
				LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
				switch (playtype) { // 获取选中的map信息
				case 1:// 让球胜平负
				case 4:// 胜平负
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_spf;
					break;
				case 2:// 比分
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_cbf;
					break;
				case 3:// 总进球
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_zjq;
					break;
				case 5:// 混合投注
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				case 6:// 半全场
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_bqc;
					break;
				case 7:// 胜平负/让球
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				case 8:// 2选1
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				}
				if (map_hashMap.containsKey(i)) {// 包含父下标
					HashMap<Integer, Integer> cids = new HashMap<Integer, Integer>();
					Map<Integer, ArrayList<String>> map = map_hashMap.get(i);
					Set<Integer> cset = map.keySet();
					int cursor = 0;
					if (0 == countDan) { // 没有设胆
						for (Integer cid : cset) { // 遍历已选每一个对阵
							DtMatch dm = ExpandAdapterJCZQPassMore.list_Matchs
									.get(i).get(cid);

							ArrayList<String> selectNum = map.get(cid);// 一场比赛选的结果
							dm.setSelectNum(selectNum);
						}
					}
				}
			}
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		if (AppTools.list_numbers.size() > 0) {
			if (AppTools.user != null) {
				btn_submit.setEnabled(false);// 控制按钮重复点击
				RequestUtil requestUtil = new RequestUtil(mContext, false, 0,
						true, "正在支付...") {
					@Override
					public void responseCallback(JSONObject object) {
						// setEnabled(true);
						btn_submit.setEnabled(true);// 控制按钮重复点击
						if (RequestUtil.DEBUG)
							Log.i(TAG, "竞彩足球投注支付结果" + object);
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

							// 清空所有数据
							ExpandAdapterJCZQPassMore.clearAllDate();
							ExpandAdapterJCZQPassSingle.clearAllDate();
							selectPasstype.clear();// 清除过关方式
							Intent intent = new Intent(getApplicationContext(),
									PaySuccessActivity.class);
							viewPagerCurrentIndex = 0;// 过关类型 0.多串1 1.多串多
							intent.putExtra("paymoney", totalMoney);
							intent.putExtra("currentMoeny", SucJinECost);
							intent.putExtra("currentHandsel", SucCaiJinCost);
							intent.putExtra("handselMoney", SuccMoney);
							intent.putExtra("voucherMoney", voucherMoney);
							intent.putExtra("passtype", passtype);

							Bet_JCZQ_Activity.this.startActivity(intent);

						} else if (error.equals(AppTools.ERROR_MONEY + "")) {
							LotteryUtils.showMoneyLess(Bet_JCZQ_Activity.this,
									totalMoney);
						} else if (error.equals("-500")) {
							MyToast.getToast(Bet_JCZQ_Activity.this, "连接超时");
						} else {
							MyToast.getToast(Bet_JCZQ_Activity.this, resultMsg);
						}
					}

					@Override
					public void responseError(VolleyError error) {
						btn_submit.setEnabled(true);// 控制按钮重复点击
						MyToast.getToast(Bet_JCZQ_Activity.this,
								"抱歉，支付出现未知错误..");
						if (RequestUtil.DEBUG)
							Log.e(TAG, "投注支付报错" + error.getMessage());
					}
				};

				requestUtil.commitBetting(sumCount, totalMoney, isStopChase,voucherID);
			} else {
				MyToast.getToast(Bet_JCZQ_Activity.this, "请先登陆");
				intent = new Intent(Bet_JCZQ_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_JCZQ_Activity.this.startActivity(intent);
			}
		} else {
			MyToast.getToast(Bet_JCZQ_Activity.this, "请至少选择一注");
		}
	}

	/**
	 * 清空过关方式
	 */
	public void clearPassType() {
		selectPasstype.clear();
		layout_tip_jiangjin.setVisibility(View.GONE);
		initPassType();
		selectPassType();
		sumCount = 0;
		totalMoney = 0;
		changeTextShow();
	}

	public void closePasstype() {
		if (tv_show_passway.getText().toString().trim().equals("过关方式")) {
			tv_show_passway2.setVisibility(View.VISIBLE);
			ll_pass2.setVisibility(View.VISIBLE);
		} else {
			tv_show_passway2.setVisibility(View.GONE);
			ll_pass2.setVisibility(View.GONE);
		}
		passType_list.setVisibility(View.GONE);
		tv_show_passway.setVisibility(View.VISIBLE);
		tv_show_passway3.setVisibility(View.GONE);
	}

	/**
	 * 选择过关方式
	 */
	public void selectPassType() {
		LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
		if (0 == passtype) { // 过关
			switch (playtype) {// 获取选中的map信息
			case 1:// 让球胜平负
			case 4:// 胜平负
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_spf;
				break;
			case 2:// 比分
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_cbf;
				break;
			case 3:// 总进球
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_zjq;
				break;
			case 5:// 混合投注
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
				break;
			case 6:// 半全场
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_bqc;
				break;
			case 7:// 胜平负/让球
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
				break;
			case 8:// 2选1
				map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
				break;
			}
		} else if (1 == passtype) { // 单关
			switch (playtype) {// 获取选中的map信息
			case 1:// 让球胜平负
			case 4:// 胜平负
				map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_spf;
				break;
			case 2:// 比分
				map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_cbf;
				break;
			case 3: // 总进球
				map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_zjq;
				break;
			case 6:// 半全场
				map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_bqc;
				break;

			}
		}
		countDan = adapter.selectDanCount;// 胆的个数
		dtCount = adapter.getSelectDtMatchCount(map_hashMap);// 获取已选择的比赛场次
		Set<Integer> keyset = map_hashMap.keySet();
		Iterator<Integer> it = keyset.iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			Map<Integer, ArrayList<String>> map = map_hashMap.get(key);
			Set<Integer> keyset2 = map.keySet();
			Iterator<Integer> it2 = keyset2.iterator();
			while (it2.hasNext()) {
				Integer key2 = it2.next();
				ArrayList<String> list = map.get(key2);
				for (int i = 0; i < list.size(); i++) {
					int temp = Integer.parseInt(list.get(i));
					if (dtCount > 6) {
						if (temp > 200 && temp < 209) {
							dtCount = 6;
						}
					}
					if (dtCount > 4) {
						if (temp > 300 && temp < 332 || temp > 400
								&& temp < 410) {
							dtCount = 4;
						}
					}
				}
			}
		}

		if (passType_list.getVisibility() == View.GONE) {
			passType_list.setVisibility(View.VISIBLE);
			tv_show_passway.setVisibility(View.GONE);
			tv_show_passway2.setVisibility(View.GONE);
			tv_show_passway3.setVisibility(View.VISIBLE);

			listAdapter = new SelectPasstype_List_JCAdapter(
					Bet_JCZQ_Activity.this, countDan, dtCount,
					viewPagerCurrentIndex);
			passType_list.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
		} else {
			if (tv_show_passway.getText().toString().trim().equals("过关方式")) {
				tv_show_passway2.setVisibility(View.VISIBLE);
				ll_pass2.setVisibility(View.VISIBLE);
			} else {
				tv_show_passway2.setVisibility(View.GONE);
				ll_pass2.setVisibility(View.GONE);
			}
			passType_list.setVisibility(View.GONE);
			tv_show_passway.setVisibility(View.VISIBLE);
			tv_show_passway3.setVisibility(View.GONE);
		}
		if (0 != selectPasstype.size()) {// 不为空
			listAdapter.setSelectPassType(selectPasstype);
		}else{
			listAdapter.setSelectPassType(new ArrayList<String>());
		}
		listAdapter
				.setDialogResultListener(new SelectPasstype_List_JCAdapter.DialogResultListener() {
					@Override
					public void getResult(int resultCode,
							ArrayList<String> selectResult, int type) {
						if (1 == resultCode) {// 确定
							viewPagerCurrentIndex = type;
							selectPasstype = selectResult;
							if (0 != selectPasstype.size()) {// 不为空
								StringBuilder show = new StringBuilder();
								for (int i = 0; i < selectPasstype.size(); i++) {
									HashMap<String, String> passtypeMap = listAdapter
											.getPASSTYPE_MAP();
									show.append(",").append(
											passtypeMap.get(selectPasstype
													.get(i)));
								}
								passStr = show.substring(1);
								if (null != passStr && !"".equals(passStr)) {
									Spanned text = Html.fromHtml(AppTools
											.changeStringColor("#e3393c",
													passStr));
									tv_show_passway.setText(text);// 过关
									tv_show_passway2.setVisibility(View.GONE);
									ll_pass2.setVisibility(View.GONE);
								} else {
									Spanned text = Html.fromHtml(AppTools
											.changeStringColor("#808080",
													"过关方式"));
									tv_show_passway.setText(text);// 过关
									ll_pass2.setVisibility(View.VISIBLE);
								}
								if (passStr.contains("3串3")) {
								} else if (playtype == 5 || playtype == 7
										|| playtype == 8) {
									// 混合投注时，隐藏奖金优化
								}
								// 设置背景
								setSelectNumAndGetCount();// 设置投注格式,计算注数
								changeTextShow();
								getJiangjin();
							} else {
								sumCount = 0;
								layout_tip_jiangjin.setVisibility(View.GONE);
								changeTextShow();
								initPassType();
							}
						}
					}
				});
	}

	/**
	 * 计算预测奖金方法
	 */
	private void getJiangjin() {
		if (min_peilv.size() > 0) {
			min_peilv.clear();
		}
		if (max_peilv.size() > 0) {
			max_peilv.clear();
		}

		getBonusOptimization();
		get_min_peilv_list();
		get_max_peilv_list();
		
		max_jiangjin = JiangjinTools.getjczq_jiangjin("max", passStr,
				list_max_jiangjin) * (AppTools.bei);
		min_jiangjin = JiangjinTools.getjczq_jiangjin("min", passStr,
				list_min_jiangjin) * (AppTools.bei);
		DecimalFormat df = new DecimalFormat("########0.00");
		String result = df.format(max_jiangjin);
		max_jiangjin = Double.parseDouble(result);

		String result2 = df.format(min_jiangjin);
		min_jiangjin = Double.parseDouble(result2);
		
//		max_jiangjin = JiangjinTools.getjczq_jiangjin("max", passStr,
//				list_max_jiangjin);
//		min_jiangjin = JiangjinTools.getjczq_jiangjin("min", passStr,
//				list_min_jiangjin);

		if (max_jiangjin > 0) {
			layout_tip_jiangjin.setVisibility(View.VISIBLE);
			if (max_jiangjin == min_jiangjin) {
				tv_jiangjin.setText(max_jiangjin + "");
			} else {
				tv_jiangjin.setText(min_jiangjin + "~" + max_jiangjin);
			}
		} else {
			layout_tip_jiangjin.setVisibility(View.GONE);
		}
	}

	/**
	 * 计算注数
	 */
	public void setSelectNumAndGetCount() {
		sumCount = 0;
		eachSelectDT = new ArrayList<String>();
		List<String> dan_list = new ArrayList<String>(); // 存放胆的集合
		StringBuffer sb = new StringBuffer();
		int playType = 0;
		switch (playtype) {
		case 1:// 让球胜平负
			playType = 7201;
			break;
		case 2:// 比分
			playType = 7202;
			break;
		case 3:// 总进球
			playType = 7203;
			break;
		case 4:// 胜平负
			playType = 7207;
			break;
		case 5:// 混合投注
			playType = 7206;
			break;
		case 6:// 半全场
			playType = 7204;
			break;
		case 7:// 胜平负/让球
			playType = 7208;
			break;
		case 8:// 2选1
			playType = 7209;
			break;
		}
		sb.append(playType + ";");
		StringBuilder notdan_sb = new StringBuilder();// 用来拼接
		StringBuilder dan_sb = new StringBuilder();// 用来拼接
		if (0 == passtype) {// 过关
			sb.append("[");
			for (int i = 0; i < ExpandAdapterJCZQPassMore.list_Matchs.size(); i++) {// 遍历过关所有对阵
				LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
				switch (playtype) {// 获取选中的map信息
				case 1:// 让球胜平负
				case 4:// 胜平负
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_spf;
					break;
				case 2:// 比分
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_cbf;
					break;
				case 3:// 总进球
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_zjq;
					break;
				case 5:// 混合投注
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				case 6:// 半全场
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_bqc;
					break;

				case 7:// 胜平负/让球
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				case 8:// 2选1
					map_hashMap = ExpandAdapterJCZQPassMore.map_hashMap_hhtz;
					break;
				}
				// 拼接投注格式
				if (map_hashMap.containsKey(i)) {// 包含父下标
					HashMap<Integer, Integer> cids = new HashMap<Integer, Integer>();
					Map<Integer, ArrayList<String>> map = map_hashMap.get(i);
					Set<Integer> cset = map.keySet();
					int cursor = 0;
					if (0 == countDan) {// 没有设胆
						for (Integer cid : cset) { // 遍历已选每一个对阵
							DtMatch dm = ExpandAdapterJCZQPassMore.list_Matchs
									.get(i).get(cid);
							ArrayList<String> selectNum = map.get(cid);// 一场比赛选的结果
							StringBuilder each = new StringBuilder();
							sb.append(dm.getMatchId()).append("(");
							// 用于混合投注拼接特殊字符串计算注数
							StringBuilder each1 = new StringBuilder();
							StringBuilder each2 = new StringBuilder();
							StringBuilder each3 = new StringBuilder();
							StringBuilder each4 = new StringBuilder();
							StringBuilder each5 = new StringBuilder();
							for (int j = 0; j < selectNum.size(); j++) {
								String str = selectNum.get(j);
								if (7206 != playType && 7208 != playType
										&& 7209 != playType) { // 非混合投注
									each.append("1");
									for (int n = 101; n <= 103; n++) {// 胜平负
										if (str.contains(n + "")) {// 如果包含则替换
											String old = n + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int h = 201; h <= 208; h++) {// 总进球数
										if (str.contains(h + "")) {// 如果包含则替换
											String old = h + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int k = 301; k <= 331; k++) {// 比分
										if (str.contains(k + "")) {// 如果包含则替换
											String old = k + "";
											if (k < 310) {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											} else {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 2,
																		old.length()));
											}
										}
									}
									for (int l = 401; l <= 409; l++) {// 半全场
										if (str.contains(l + "")) {// 如果包含则替换
											String old = l + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int m = 501; m <= 503; m++) {// 让球胜平负
										if (str.contains(m + "")) {// 如果包含则替换
											String old = m + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
								} else {// 混合投注
									for (int n = 101; n <= 103; n++) {// 胜平负
										if (str.contains(n + "")) {// 如果包含则替换
											each1.append("1,");
											String old = n + "";
											str = str
													.replace(old, (n - 1) + "");
										}
									}

									for (int h = 201; h <= 208; h++) {// 总进球数
										if (str.contains(h + "")) {// 如果包含则替换
											each2.append("2,");
											String old = h + "";
											str = str
													.replace(old, (h - 1) + "");
										}
									}
									for (int k = 301; k <= 331; k++) {// 比分
										if (str.contains(k + "")) {// 如果包含则替换
											each3.append("3,");
											String old = k + "";
											str = str
													.replace(old, (k - 1) + "");
										}
									}
									for (int l = 401; l <= 409; l++) {// 半全场
										if (str.contains(l + "")) {// 如果包含则替换
											each4.append("4,");
											String old = l + "";
											str = str
													.replace(old, (l - 1) + "");
										}
									}
									for (int m = 501; m <= 503; m++) {// 让球胜平负
										if (str.contains(m + "")) {// 如果包含则替换
											each5.append("5,");
											String old = m + "";
											str = str
													.replace(old, (m - 1) + "");
										}
									}
								}
								// 这里需要将100转换成500,500装换成100.混合竞彩让分和非让分后台数据混乱
								if (Integer.valueOf(str) / 100 == 1) {
									str = "5" + (Integer.valueOf(str) / 10)
											% 10 + (Integer.valueOf(str) % 10);
								} else if (Integer.valueOf(str) / 100 == 5) {
									str = "1" + (Integer.valueOf(str) / 10)
											% 10 + (Integer.valueOf(str) % 10);
								}
								if (0 == j) {
									sb.append(str);
								} else {
									sb.append("," + str);
								}
							}
							if (0 != each1.length()) {// 胜平负
								String str1 = "";
								if (0 == each2.length() && 0 == each3.length()
										&& 0 == each4.length()
										&& 0 == each5.length()) {// 只有胜平负
									str1 = each1.substring(0,
											each1.length() - 1);
								} else {
									str1 = each1.substring(0,
											each1.length() - 1) + "|";
								}

								each.append(str1);
							}
							if (0 != each2.length()) {// 总进球数
								String str2 = "";
								if (0 == each3.length() && 0 == each4.length()
										&& 0 == each5.length()) {// 没有比分，半全场，让球胜平负
									str2 = each2.substring(0,
											each2.length() - 1);
								} else {
									str2 = each2.substring(0,
											each2.length() - 1) + "|";
								}
								each.append(str2);
							}
							if (0 != each3.length()) {// 比分
								String str3 = "";
								if (0 == each4.length() && 0 == each5.length()) {// 没有半全场，让球胜平负
									str3 = each3.substring(0,
											each3.length() - 1);
								} else {
									str3 = each3.substring(0,
											each3.length() - 1) + "|";
								}
								each.append(str3);
							}
							if (0 != each4.length()) {// 半全场
								String str4 = "";
								if (0 == each5.length()) {// 没有半全场，让球胜平负
									str4 = each4.substring(0,
											each4.length() - 1);
								} else {
									str4 = each4.substring(0,
											each4.length() - 1) + "|";
								}
								each.append(str4);
							}
							if (0 != each5.length()) {// 让球胜平负
								String str5 = each5.substring(0,
										each5.length() - 1);
								each.append(str5);
							}
							if (7206 == playType || 7208 == playType
									|| 7209 == playType)
								Log.i(TAG, "混合投注拼接字符串" + each);
							eachSelectDT.add(each.toString());// 添加每一场对阵选的结果
							sb.append(")|");
						}
					} else {// 已设胆
						for (Integer cid : cset) {// 遍历已选每一个对阵
							DtMatch dm = ExpandAdapterJCZQPassMore.list_Matchs
									.get(i).get(cid);
							ArrayList<String> selectNum = map.get(cid);
							if (selectNum.contains("1")) {// 选胆了
								dan_sb.append(dm.getMatchId() + "(");
								StringBuffer dan_sb_list = new StringBuffer();// 加入danlist
								for (int j = 0; j < selectNum.size(); j++) {// 一场对阵
									String str = selectNum.get(j);// 对阵的一个结果
									if (!"1".equals(str)) {// 非胆
										for (int n = 101; n <= 103; n++) {// 胜平负
											if (str.contains(n + "")) {// 如果包含则替换
												String old = n + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										for (int h = 201; h <= 208; h++) {// 总进球数
											if (str.contains(h + "")) {// 如果包含则替换
												String old = h + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										for (int k = 301; k <= 331; k++) {// 比方
											if (str.contains(k + "")) {// 如果包含则替换
												String old = k + "";
												if (k < 310) {
													str = str
															.replace(
																	old,
																	old.substring(
																			old.length() - 1,
																			old.length()));
												} else {
													str = str
															.replace(
																	old,
																	old.substring(
																			old.length() - 2,
																			old.length()));
												}
											}
										}
										for (int l = 401; l <= 409; l++) {// 半全场
											if (str.contains(l + "")) {// 如果包含则替换
												String old = l + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										for (int m = 501; m <= 503; m++) {// 让球胜平负
											if (str.contains(m + "")) {// 如果包含则替换
												String old = m + "";
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											}
										}
										dan_sb_list.append("1");// 将每一个结果拼接
										if (0 == j) {
											dan_sb.append(str);
										} else {
											dan_sb.append("," + str);
										}
									}
								}
								dan_sb.append(")|");// 一场比赛的所有结果
								dan_list.add(dan_sb_list.toString());// 将拼接的所选胆的结果放入集合dan_list
							} else {// 未选胆的
								notdan_sb.append(dm.getMatchId() + "(");
								StringBuffer notdan_sb_list = new StringBuffer(); // 加入list
								for (int j = 0; j < selectNum.size(); j++) {// 一场对阵
									String str = selectNum.get(j);// 对阵的一个结果

									for (int n = 101; n <= 103; n++) {// 胜平负
										if (str.contains(n + "")) {// 如果包含则替换
											String old = n + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int h = 201; h <= 208; h++) {// 总进球数
										if (str.contains(h + "")) {// 如果包含则替换
											String old = h + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int k = 301; k <= 331; k++) {// 比分
										if (str.contains(k + "")) {// 如果包含则替换
											String old = k + "";
											if (k < 310) {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 1,
																		old.length()));
											} else {
												str = str
														.replace(
																old,
																old.substring(
																		old.length() - 2,
																		old.length()));
											}
										}
									}
									for (int l = 401; l <= 409; l++) {// 半全场
										if (str.contains(l + "")) {// 如果包含则替换
											String old = l + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									for (int m = 501; m <= 503; m++) {// 让球胜平负
										if (str.contains(m + "")) {// 如果包含则替换
											String old = m + "";
											str = str.replace(old, old
													.substring(
															old.length() - 1,
															old.length()));
										}
									}
									notdan_sb_list.append("1");// 将每一个结果拼接
									if (0 == j) {
										notdan_sb.append(str);
									} else {
										notdan_sb.append("," + str);
									}
								}
								notdan_sb.append(")|");// 一场比赛的所有结果
								eachSelectDT.add(notdan_sb_list.toString());// 将拼接的所选胆的结果放入集合dan_list
							}
						}
					}
				}
			}
			if (0 != dan_sb.length() && 0 != notdan_sb.length()) {
				String dan = dan_sb.toString();
				dan = dan_sb.substring(0, dan_sb.length() - 1);// 去除最后一个“|”
				sb.append(dan + "][");// 加胆右边中括号和非胆左中括号
				String notdan = notdan_sb.toString();// 去除最后一个“|”
				sb.append(notdan);
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));// 去掉“|”
			Log.i(TAG, "sb" + sb);
			sb.append("]");
		} else if (1 == passtype) {// 单关
			Log.i(TAG, "单关计算注数");
			sb.append("[");
			for (int i = 0; i < ExpandAdapterJCZQPassSingle.list_Matchs.size(); i++) {// 遍历单关所有对阵
				LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
				switch (playtype) {// 获取选中的map信息
				case 1:// 让球胜平负
				case 4:// 胜平负
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_spf;
					break;
				case 2:// 比分
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_cbf;
					break;
				case 3:// 总进球
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_zjq;

					break;
				case 6:// 半全场
					map_hashMap = ExpandAdapterJCZQPassSingle.map_hashMap_bqc;
					break;
				}
				if (map_hashMap.containsKey(i)) {// 包含父下标
					HashMap<Integer, Integer> cids = new HashMap<Integer, Integer>();
					Map<Integer, ArrayList<String>> map = map_hashMap.get(i);
					Set<Integer> cset = map.keySet();
					int cursor = 0;
					for (Integer cid : cset) {
						DtMatch dm = ExpandAdapterJCZQPassSingle.list_Matchs
								.get(i).get(cid);
						StringBuffer each = new StringBuffer();
						sb.append(dm.getMatchId() + "(");
						ArrayList<String> selectNum = map.get(cid);
						for (int j = 0; j < selectNum.size(); j++) {
							String str = selectNum.get(j);
							// 非混合投注
							for (int n = 101; n <= 103; n++) {// 胜平负
								if (str.contains(n + "")) {// 如果包含则替换
									String old = n + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							for (int h = 201; h <= 208; h++) {// 总进球数
								if (str.contains(h + "")) {// 如果包含则替换
									String old = h + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							for (int k = 301; k <= 331; k++) {// 比分
								if (str.contains(k + "")) {// 如果包含则替换
									String old = k + "";
									if (k < 310) {
										str = str
												.replace(old, old.substring(
														old.length() - 1,
														old.length()));
									} else {
										str = str
												.replace(old, old.substring(
														old.length() - 2,
														old.length()));
									}
								}
							}
							for (int l = 401; l <= 409; l++) {// 半全场
								if (str.contains(l + "")) {// 如果包含则替换
									String old = l + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							for (int m = 501; m <= 503; m++) {// 让球胜平负
								if (str.contains(m + "")) {// 如果包含则替换
									String old = m + "";
									str = str.replace(
											old,
											old.substring(old.length() - 1,
													old.length()));
								}
							}
							each.append("1");
							if (0 == j) {
								sb.append(str);
							} else {
								sb.append("," + str);
							}
						}
						eachSelectDT.add(each.toString());
						sb.append(")|");
						Log.i(TAG, "每一次sb的值" + sb.toString());
					}
					Log.i(TAG, "拼接之后sb的值" + sb.toString());
				}
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));// 去掉“|”
			sb.append("]");
		}
		StringBuffer passtype = new StringBuffer();
		for (int i = 0; i < selectPasstype.size(); i++) {
			if (0 == i) {
				passtype.append(selectPasstype.get(i) + AppTools.bei);
			} else {

				passtype.append("," + selectPasstype.get(i) + AppTools.bei);
			}
		}
		if (1 == this.passtype) {// 单关
			passtype.append("A0" + AppTools.bei);
		}
		String str = "";
		if (0 != countDan) {// 含胆
			str = sb.toString() + ";[" + passtype + "];[" + countDan + "]";// 拼接成lotteryNumber
		} else {
			str = sb.toString() + ";[" + passtype + "]";// 拼接成lotteryNumber
		}
		AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		SelectedNumbers selectNumber = new SelectedNumbers();
		selectNumber.setPlayType(playType); // 设置玩法类型
		selectNumber.setLotteryNumber(str); // 设置投注号码
		StringBuffer selectpass = new StringBuffer();

		for (int i = 0; i < selectPasstype.size(); i++) {
			if (0 == i) {
				selectpass.append(selectPasstype.get(i));
			} else {
				selectpass.append(" " + selectPasstype.get(i));
			}
		}
		if (1 == this.passtype) {// 单关
			selectpass.append(passtype.toString());
		}
		// 计算注数
		if (0 == countDan) {// 无胆
			if (7206 != playType && 7208 != playType && 7209 != playType) {// 非混合投注

				setTotalCount(selectpass.toString(), eachSelectDT);
			} else {// 混合投注
				setTotalCountHHTZ(selectpass.toString(), eachSelectDT);
			}
		} else {// 有胆
			setTotalCount(selectpass.toString(), dan_list, eachSelectDT);
		}
		selectNumber.setCount(sumCount);// 设置投注注数
		totalMoney = sumCount * 2 * AppTools.bei;
		selectNumber.setMoney(sumCount * 2);// 设置投注钱数
		AppTools.list_numbers.add(selectNumber);
	}

	/**
	 * 跳到跟单
	 */
	private void join() {
		if (AppTools.qi > 1) {
			dialog.show();
			dialog.setDialogContent("发起合买时不能追号，是否只追一期并继续发起合买？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						AppTools.qi = 1;
						int total = 0; // 总金额
						for (SelectedNumbers num : AppTools.list_numbers) {
							total += num.getMoney();
						}
						total = total * AppTools.bei;
						intent = new Intent(Bet_JCZQ_Activity.this,
								JoinActivity.class);
						intent.putExtra("totalMoney", total + "");
						intent.putExtra("shareMoney", sumCount*2);
						Bet_JCZQ_Activity.this.startActivity(intent);
					}
				}
			});
		} else {
			if(totalMoney < 100) {
				MyToast.getToast(mContext, "投注金额不能低于100元");
			}else{
				int total = 0; // 总金额
				for (SelectedNumbers num : AppTools.list_numbers) {
					total += num.getMoney();
				}
				if (total == 0) {
					MyToast.getToast(getApplicationContext(), "您还没有选择号码");
					return;
				}
				total = total * AppTools.bei;
				intent = new Intent(Bet_JCZQ_Activity.this, JoinActivity.class);
				intent.putExtra("shareMoney", sumCount*2);
				intent.putExtra("totalMoney", total + "");
				Bet_JCZQ_Activity.this.startActivity(intent);
			}
		}
	}

	/**
	 * 手选按钮点击事件
	 */
	private void btn_handClick() {
		AppTools.totalCount = 0;
		if (0 == passtype) { // 多关
			intent = new Intent(Bet_JCZQ_Activity.this,
					Select_JCZQ_Activity.class);
		} else {
			intent = new Intent(Bet_JCZQ_Activity.this,
					Select_JCZQ_DAN_Activity.class);
		}

		intent.putExtra("passtype", passtype);
		int playType = 7201;
		switch (playtype) {
		case 1:// 让球胜平负
			playType = 7201;
			break;
		case 2:// 比分
			playType = 7202;
			break;
		case 3:// 总进球
			playType = 7203;
			break;
		case 4:// 胜平负
			playType = 7207;
			break;
		case 5:// 混合投注
			playType = 7206;
			break;
		case 6:// 半全场
			playType = 7204;
			break;
		case 7:// 胜平负/让球
			playType = 7208;
			break;
		case 8://
			playType = 7209;
			break;
		}
		intent.putExtra("playtype", playType);
		Bet_JCZQ_Activity.this.startActivity(intent);
	}

	/**
	 * 改变文本的值显示出来
	 * 
	 */
	public void changeTextShow() {
		tv_tatol_count.setText(sumCount + "");// 总注数
		String str = et_bei.getText().toString();
		if ("".equals(str)) {
			str = "1";
		}
		AppTools.bei = Integer.parseInt(str);
		totalMoney = sumCount * 2 * AppTools.bei;
		tv_tatol_money.setText(totalMoney + "");// 总钱数
		
		coupon_text.setText("优惠劵（请先确认投注倍数）");
		voucherID = "";
		et_bei.setEnabled(true);
		rlBonus.setEnabled(true);
		bet_btn_deleteall.setEnabled(true);
		if (0 == passtype) {
			layout_jc.setEnabled(true);
			tv_show_passway.setEnabled(true);
		}else{
			layout_jc.setEnabled(false);
			tv_show_passway.setEnabled(false);
		}
	}

	/**
	 * 清空数据
	 */
	public static void clear() {
		AppTools.list_numbers.clear();
		AppTools.totalCount = 0;
		layout_tip_jiangjin.setVisibility(View.GONE);
		// 清空所有数据
		ExpandAdapterJCZQPassMore.clearAllDate();
		ExpandAdapterJCZQPassSingle.clearAllDate();
		selectPasstype.clear();// 清除过关方式
		viewPagerCurrentIndex = 0;// 过关类型 0.多串1 1.多串多
	}

	/**
	 * 返回
	 */
	public void backToPre() {
		if (!Select_JCZQ_Activity.isEmptJCZQ()) {
			dialog.show();
			dialog.setDialogContent("您退出后号码将会清空！");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {

				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {
						ExpandAdapterJCZQPassMore.clearAllDate();
						ExpandAdapterJCZQPassSingle.clearAllDate();
						AppTools.totalCount = 0;
						if (AppTools.list_numbers != null) {
							AppTools.list_numbers.clear();
						}
						if (0 == passtype) { // 多关
							selectPasstype.clear();// 清空过关方式
							initPassType();// 初始化过关方式
						}
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			ExpandAdapterJCZQPassMore.clearAllDate();
			ExpandAdapterJCZQPassSingle.clearAllDate();
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

	@Override
	protected void onResume() {
		super.onResume();
//		init();
//		setListener();
////		et_bei.setText("1");
//		if (1 == passtype) {// 单关
//			initPassType();// 初始化过关方式
//			setSelectNumAndGetCount();// 设置投注格式和注数
//		} else {
//			selectPassType();
//		}
//		changeTextShow();
//		getJiangjin();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * 计算总注数-混合投注
	 */
	public void setTotalCountHHTZ(String type, List<String> list) {
		if (type.contains("AA"))
			this.sumCount += NumberTools.getAll2G1Mixed_hunhe(list);
		if (type.contains("AB"))
			this.sumCount += NumberTools.getAll3G1Mixed_hunhe(list);
		if (type.contains("AC"))
			this.sumCount += NumberTools.getAll3G3Mixed_hunhe(list);
		if (type.contains("AD"))
			this.sumCount += NumberTools.getAll3G4Mixed_hunhe(list);
		if (type.contains("AE"))
			this.sumCount += NumberTools.getAll4G1Mixed_hunhe(list);
		if (type.contains("AF"))
			this.sumCount += NumberTools.getAll4G4Mixed_hunhe(list);
		if (type.contains("AG"))
			this.sumCount += NumberTools.getAll4G5Mixed_hunhe(list);
		if (type.contains("AH"))
			this.sumCount += NumberTools.getAll4G6Mixed_hunhe(list);
		if (type.contains("AI"))
			this.sumCount += NumberTools.getAll4G11Mixed_hunhe(list);
		if (type.contains("AJ"))
			this.sumCount += NumberTools.getAll5G1Mixed_hunhe(list);
		if (type.contains("AK"))
			this.sumCount += NumberTools.getAll5G5Mixed_hunhe(list);
		if (type.contains("AL"))
			this.sumCount += NumberTools.getAll5G6Mixed_hunhe(list);
		if (type.contains("AM"))
			this.sumCount += NumberTools.getAll5G10Mixed_hunhe(list);
		if (type.contains("AN"))
			this.sumCount += NumberTools.getAll5G16Mixed_hunhe(list);
		if (type.contains("AO"))
			this.sumCount += NumberTools.getAll5G20Mixed_hunhe(list);
		if (type.contains("AP"))
			this.sumCount += NumberTools.getAll5G26Mixed_hunhe(list);
		if (type.contains("AQ"))
			this.sumCount += NumberTools.getAll6G1Mixed_hunhe(list);
		if (type.contains("AR"))
			this.sumCount += NumberTools.getAll6G6Mixed_hunhe(list);
		if (type.contains("AS"))
			this.sumCount += NumberTools.getAll6G7Mixed_hunhe(list);
		if (type.contains("AT"))
			this.sumCount += NumberTools.getAll6G15Mixed_hunhe(list);
		if (type.contains("AU"))
			this.sumCount += NumberTools.getAll6G20Mixed_hunhe(list);
		if (type.contains("AV"))
			this.sumCount += NumberTools.getAll6G22Mixed_hunhe(list);
		if (type.contains("AW"))
			this.sumCount += NumberTools.getAll6G35Mixed_hunhe(list);
		if (type.contains("AX"))
			this.sumCount += NumberTools.getAll6G42Mixed_hunhe(list);
		if (type.contains("AY"))
			this.sumCount += NumberTools.getAll6G50Mixed_hunhe(list);
		if (type.contains("AZ"))
			this.sumCount += NumberTools.getAll6G57Mixed_hunhe(list);
		if (type.contains("BA"))
			this.sumCount += NumberTools.getAll7G1Mixed_hunhe(list);
		if (type.contains("BB"))
			this.sumCount += NumberTools.getAll7G7Mixed_hunhe(list);
		if (type.contains("BC"))
			this.sumCount += NumberTools.getAll7G8Mixed_hunhe(list);
		if (type.contains("BD"))
			this.sumCount += NumberTools.getAll7G21Mixed_hunhe(list);
		if (type.contains("BE"))
			this.sumCount += NumberTools.getAll7G35Mixed_hunhe(list);
		if (type.contains("BF"))
			this.sumCount += NumberTools.getAll7G120Mixed_hunhe(list);
		if (type.contains("BG"))
			this.sumCount += NumberTools.getAll8G1Mixed_hunhe(list);
		if (type.contains("BH"))
			this.sumCount += NumberTools.getAll8G8Mixed_hunhe(list);
		if (type.contains("BI"))
			this.sumCount += NumberTools.getAll8G9Mixed_hunhe(list);
		if (type.contains("BJ"))
			this.sumCount += NumberTools.getAll8G28Mixed_hunhe(list);
		if (type.contains("BK"))
			this.sumCount += NumberTools.getAll8G56Mixed_hunhe(list);
		if (type.contains("BL"))
			this.sumCount += NumberTools.getAll8G70Mixed_hunhe(list);
		if (type.contains("BM"))
			this.sumCount += NumberTools.getAll8G247Mixed_hunhe(list);
	}

	/**
	 * 给总注数赋值 有胆的情况下
	 */
	public void setTotalCount(String type, List<String> list_dan,
			List<String> list) {
		if (type.contains("AA"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 2);
		if (type.contains("AB"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 3);
		if (type.contains("AC"))
			this.sumCount += NumberTools.getAll3G3Mixed_dan(list_dan, list);
		if (type.contains("AD"))
			this.sumCount += NumberTools.getAll3G4Mixed_dan(list_dan, list);
		if (type.contains("AE"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 4);
		if (type.contains("AF"))
			this.sumCount += NumberTools.getAll4G4Mixed_dan(list_dan, list);
		if (type.contains("AG"))
			this.sumCount += NumberTools.getAll4G5Mixed_dan(list_dan, list);
		if (type.contains("AH"))
			this.sumCount += NumberTools.getAll4G6_11Mixed_dan(list_dan, list,
					6);
		if (type.contains("AI"))
			this.sumCount += NumberTools.getAll4G6_11Mixed_dan(list_dan, list,
					11);
		if (type.contains("AJ"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 5);
		if (type.contains("AK"))
			this.sumCount += NumberTools.getAll5G5Mixed_dan(list_dan, list);
		if (type.contains("AL"))
			this.sumCount += NumberTools.getAll5G6Mixed_dan(list_dan, list);
		if (type.contains("AM"))
			this.sumCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 10);
		if (type.contains("AN"))
			this.sumCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 16);
		if (type.contains("AO"))
			this.sumCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 20);
		if (type.contains("AP"))
			this.sumCount += NumberTools.getAll5G26Mixed_dan(list_dan, list);
		if (type.contains("AQ"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 6);
		if (type.contains("AR"))
			this.sumCount += NumberTools.getAll6G6Mixed_dan(list_dan, list);
		if (type.contains("AS"))
			this.sumCount += NumberTools.getAll6G7Mixed_dan(list_dan, list);
		if (type.contains("AT"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 15);
		if (type.contains("AU"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 20);
		if (type.contains("AV"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 22);
		if (type.contains("AW"))
			this.sumCount += NumberTools.getAll6G35Mixed_dan(list_dan, list);
		if (type.contains("AX"))
			this.sumCount += NumberTools.getAll6G42Mixed_dan(list_dan, list);
		if (type.contains("AY"))
			this.sumCount += NumberTools.getAll6G15_20_22_50Mixed_dan(list_dan,
					list, 50);
		if (type.contains("AZ"))
			this.sumCount += NumberTools.getAll6G57Mixed_dan(list_dan, list);
		if (type.contains("BA"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 7);
		if (type.contains("BB"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 7);
		if (type.contains("BC"))
			this.sumCount += NumberTools.getAll7G8Mixed_dan(list_dan, list);
		if (type.contains("BD"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 21);
		if (type.contains("BE"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 35);
		if (type.contains("BF"))
			this.sumCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 120);
		if (type.contains("BG"))
			this.sumCount += NumberTools.getAllnG1Mixed_dan(list_dan, list, 8);
		if (type.contains("BH"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 8);
		if (type.contains("BI"))
			this.sumCount += NumberTools.getAll8G9Mixed_dan(list_dan, list);
		if (type.contains("BJ"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 28);
		if (type.contains("BK"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 56);
		if (type.contains("BL"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 70);
		if (type.contains("BM"))
			this.sumCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 247);
	}

	/**
	 * 给总注数赋值 没胆的情况下
	 */
	public void setTotalCount(String type, List<String> list) {
		if (type.contains("A0"))
			this.sumCount += NumberTools.getCountBySinglePass(list);
		if (type.contains("AA"))
			this.sumCount += NumberTools.getAll2G1Mixed(list);
		if (type.contains("AB"))
			this.sumCount += NumberTools.getAll3G1Mixed(list);
		if (type.contains("AC"))
			this.sumCount += NumberTools.getAll3G3Mixed(list);
		if (type.contains("AD"))
			this.sumCount += NumberTools.getAll3G4Mixed(list);
		if (type.contains("AE"))
			this.sumCount += NumberTools.getAll4G1Mixed(list);
		if (type.contains("AF"))
			this.sumCount += NumberTools.getAll4G4Mixed(list);
		if (type.contains("AG"))
			this.sumCount += NumberTools.getAll4G5Mixed(list);
		if (type.contains("AH"))
			this.sumCount += NumberTools.getAll4G6Mixed(list);
		if (type.contains("AI"))
			this.sumCount += NumberTools.getAll4G11Mixed(list);
		if (type.contains("AJ"))
			this.sumCount += NumberTools.getAll5G1Mixed(list);
		if (type.contains("AK"))
			this.sumCount += NumberTools.getAll5G5Mixed(list);
		if (type.contains("AL"))
			this.sumCount += NumberTools.getAll5G6Mixed(list);
		if (type.contains("AM"))
			this.sumCount += NumberTools.getAll5G10Mixed(list);
		if (type.contains("AN"))
			this.sumCount += NumberTools.getAll5G16Mixed(list);
		if (type.contains("AO"))
			this.sumCount += NumberTools.getAll5G20Mixed(list);
		if (type.contains("AP"))
			this.sumCount += NumberTools.getAll5G26Mixed(list);
		if (type.contains("AQ"))
			this.sumCount += NumberTools.getAll6G1Mixed(list);
		if (type.contains("AR"))
			this.sumCount += NumberTools.getAll6G6Mixed(list);
		if (type.contains("AS"))
			this.sumCount += NumberTools.getAll6G7Mixed(list);
		if (type.contains("AT"))
			this.sumCount += NumberTools.getAll6G15Mixed(list);
		if (type.contains("AU"))
			this.sumCount += NumberTools.getAll6G20Mixed(list);
		if (type.contains("AV"))
			this.sumCount += NumberTools.getAll6G22Mixed(list);
		if (type.contains("AW"))
			this.sumCount += NumberTools.getAll6G35Mixed(list);
		if (type.contains("AX"))
			this.sumCount += NumberTools.getAll6G42Mixed(list);
		if (type.contains("AY"))
			this.sumCount += NumberTools.getAll6G50Mixed(list);
		if (type.contains("AZ"))
			this.sumCount += NumberTools.getAll6G57Mixed(list);
		if (type.contains("BA"))
			this.sumCount += NumberTools.getAll7G1Mixed(list);
		if (type.contains("BB"))
			this.sumCount += NumberTools.getAll7G7Mixed(list);
		if (type.contains("BC"))
			this.sumCount += NumberTools.getAll7G8Mixed(list);
		if (type.contains("BD"))
			this.sumCount += NumberTools.getAll7G21Mixed(list);
		if (type.contains("BE"))
			this.sumCount += NumberTools.getAll7G35Mixed(list);
		if (type.contains("BF"))
			this.sumCount += NumberTools.getAll7G120Mixed(list);
		if (type.contains("BG"))
			this.sumCount += NumberTools.getAll8G1Mixed(list);
		if (type.contains("BH"))
			this.sumCount += NumberTools.getAll8G8Mixed(list);
		if (type.contains("BI"))
			this.sumCount += NumberTools.getAll8G9Mixed(list);
		if (type.contains("BJ"))
			this.sumCount += NumberTools.getAll8G28Mixed(list);
		if (type.contains("BK"))
			this.sumCount += NumberTools.getAll8G56Mixed(list);
		if (type.contains("BL"))
			this.sumCount += NumberTools.getAll8G70Mixed(list);
		if (type.contains("BM"))
			this.sumCount += NumberTools.getAll8G247Mixed(list);
	}

	/**
	 * 拆分数据得到赔率数据
	 */
	private void getBonusOptimization() {
		String peilv = "";// 一种结果的赔率
		String showresult = "";
		int a = 0;
		String ret = "";
		if (null == list_JC_Details)
			list_JC_Details = new ArrayList<JC_Details>();
		else
			list_JC_Details.clear();
		if (null != AppTools.list_numbers) {
			a = AppTools.list_numbers.size();
		}

		int b = adapter.bet_List_Matchs.size();
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				String[] aa = AppTools.list_numbers.get(i).getLotteryNumber()
						.split(";")[1].replace("|", "&").split("&");
				for (int k = 0; k < aa.length; k++) {
					String bb = aa[k].replace("[", "").replace("]", "")
							.replace(" ", "");
					if (bb.substring(0, bb.lastIndexOf("(")).equals(
							adapter.bet_List_Matchs.get(j).getMatchId())) {
						String[] jieguo = bb
								.substring(bb.lastIndexOf("("),
										bb.lastIndexOf(")")).replace("(", "")
								.replace(")", "").split(",");
						for (int k2 = 0; k2 < jieguo.length; k2++) {
							String saiguo = jieguo[k2];

							if (AppTools.list_numbers.get(i).getLotteryNumber()
									.split(";")[0].equals("7206")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7207")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7208")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7209")) {
								switch (Integer.parseInt(saiguo)) {
								case 1:
								case 500:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSpfwin();
									showresult = "胜";
									break;
								case 2:
								case 501:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSpfflat();
									showresult = "平";
									break;
								case 3:
								case 502:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSpflose();
									showresult = "负";
									break;
								default:
									break;
								}
							}

							if (AppTools.list_numbers.get(i).getLotteryNumber()
									.split(";")[0].equals("7201")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7206")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7208")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7209")) {
								switch (Integer.parseInt(saiguo)) {
								case 1:
								case 100:
									peilv = adapter.bet_List_Matchs.get(j)
											.getWin();
									showresult = "让胜";
									break;
								case 2:
								case 101:
									peilv = adapter.bet_List_Matchs.get(j)
											.getFlat();
									showresult = "让平";
									break;
								case 3:
								case 102:
									peilv = adapter.bet_List_Matchs.get(j)
											.getLose();
									showresult = "让负";
									break;
								default:
									break;
								}
							}
							if (AppTools.list_numbers.get(i).getLotteryNumber()
									.split(";")[0].equals("7203")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7206")) {
								switch (Integer.parseInt(saiguo)) {
								case 1:
								case 200:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn0();
									showresult = "0";
									break;
								case 2:
								case 201:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn1();
									showresult = "1";
									break;
								case 3:
								case 202:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn2();
									showresult = "2";
									break;
								case 4:
								case 203:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn3();
									showresult = "3";
									break;
								case 5:
								case 204:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn4();
									showresult = "4";
									break;

								case 6:
								case 205:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn5();
									showresult = "5";
									break;
								case 7:
								case 206:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn6();
									showresult = "6";
									break;
								case 8:
								case 207:
									peilv = adapter.bet_List_Matchs.get(j)
											.getIn7();
									showresult = "7+";
									break;
								default:
									break;
								}
							}
							if (AppTools.list_numbers.get(i).getLotteryNumber()
									.split(";")[0].equals("7204")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7206")) {
								switch (Integer.parseInt(saiguo)) {
								case 1:
								case 400:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSs();
									showresult = "胜胜";
									break;
								case 2:
								case 401:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSp();
									showresult = "胜平";
									break;
								case 3:
								case 402:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSf();
									showresult = "胜负";
									break;
								case 4:
								case 403:
									peilv = adapter.bet_List_Matchs.get(j)
											.getPs();
									showresult = "平胜";
									break;
								case 5:
								case 404:
									peilv = adapter.bet_List_Matchs.get(j)
											.getPp();
									showresult = "平平";
									break;
								case 6:
								case 405:
									peilv = adapter.bet_List_Matchs.get(j)
											.getPf();
									showresult = "平负";
									break;
								case 7:
								case 406:
									peilv = adapter.bet_List_Matchs.get(j)
											.getFs();
									showresult = "负胜";
									break;
								case 8:
								case 407:
									peilv = adapter.bet_List_Matchs.get(j)
											.getFp();
									showresult = "负平";
									break;
								case 9:
								case 408:
									peilv = adapter.bet_List_Matchs.get(j)
											.getFf();
									showresult = "负负";
									break;
								default:
									break;
								}
							}
							if (AppTools.list_numbers.get(i).getLotteryNumber()
									.split(";")[0].equals("7202")
									|| AppTools.list_numbers.get(i)
											.getLotteryNumber().split(";")[0]
											.equals("7206")) {
								switch (Integer.parseInt(saiguo)) {
								case 1:
								case 300:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS10();
									showresult = "1:0";
									break;
								case 2:
								case 301:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS20();
									showresult = "2:0";
									break;
								case 3:
								case 302:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS21();
									showresult = "2:1";
									break;
								case 4:
								case 303:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS30();
									showresult = "3:0";
									break;
								case 5:
								case 304:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS31();
									showresult = "3:1";
									break;
								case 6:
								case 305:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS32();
									showresult = "3:2";
									break;
								case 7:
								case 306:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS40();
									showresult = "4:0";
									break;
								case 8:
								case 307:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS41();
									showresult = "4:1";
									break;
								case 9:
								case 308:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS42();
									showresult = "4:2";
									break;
								case 10:
								case 309:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS50();
									showresult = "5:0";
									break;
								case 11:
								case 310:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS51();
									showresult = "5:1";
									break;
								case 12:
								case 311:
									peilv = adapter.bet_List_Matchs.get(j)
											.getS52();
									showresult = "5:2";
									break;
								case 13:
								case 312:
									peilv = adapter.bet_List_Matchs.get(j)
											.getSother();
									showresult = "胜其他";
									break;
								case 14:
								case 313:
									peilv = adapter.bet_List_Matchs.get(j)
											.getP00();
									showresult = "0:0";
									break;
								case 15:
								case 314:
									peilv = adapter.bet_List_Matchs.get(j)
											.getP11();
									showresult = "1:1";
									break;
								case 16:
								case 315:
									peilv = adapter.bet_List_Matchs.get(j)
											.getP22();
									showresult = "2:2";
									break;
								case 17:
								case 316:
									peilv = adapter.bet_List_Matchs.get(j)
											.getP33();
									showresult = "3:3";
									break;
								case 18:
								case 317:
									peilv = adapter.bet_List_Matchs.get(j)
											.getPother();
									showresult = "平其他";
									break;
								case 19:
								case 318:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF01();
									showresult = "0:1";
									break;
								case 20:
								case 319:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF02();
									showresult = "0:2";
									break;
								case 21:
								case 320:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF12();
									showresult = "1:2";
									break;
								case 22:
								case 321:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF03();
									showresult = "0:3";
									break;
								case 23:
								case 322:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF13();
									showresult = "1:3";
									break;
								case 24:
								case 323:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF23();
									showresult = "2:3";
									break;
								case 25:
								case 324:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF04();
									showresult = "0:4";
									break;
								case 26:
								case 325:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF14();
									showresult = "1:4";
									break;
								case 27:
								case 326:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF24();
									showresult = "2:4";
									break;
								case 28:
								case 327:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF05();
									showresult = "0:5";
									break;
								case 29:
								case 328:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF15();
									showresult = "1:5";
									break;
								case 30:
								case 329:
									peilv = adapter.bet_List_Matchs.get(j)
											.getF25();
									showresult = "2:5";
									break;
								case 31:
								case 330:
									peilv = adapter.bet_List_Matchs.get(j)
											.getFother();
									showresult = "负其他";
									break;
								default:
									break;
								}
							}
							JC_Details jc_details = new JC_Details();
							jc_details.setPlaytype_info(AppTools.list_numbers
									.get(i).getLotteryNumber().split(";")[0]);
							jc_details.setMatchNumber(adapter.bet_List_Matchs
									.get(j).getMatchNumber());
							jc_details.setResult(saiguo);
							jc_details.setMatchID(adapter.bet_List_Matchs
									.get(j).getMatchId());
							jc_details.setMainTeam(adapter.bet_List_Matchs.get(
									j).getMainTeam());
							jc_details.setGuestTeam(adapter.bet_List_Matchs
									.get(j).getGuestTeam());
							jc_details.setShowresult(showresult);
							jc_details.setPeilv(Double.parseDouble(peilv
									.equals("") ? "0" : peilv));
							list_JC_Details.add(jc_details);
						}
					}
				}
			}
		}

	}

	/**
	 * 得到每场对阵的最大赔率
	 */
	private void get_max_peilv_list() {
		if ((playtype == 5 && 0 == passtype)
				|| (playtype == 7 && 0 == passtype)
				|| (playtype == 8 && 0 == passtype)) {
			System.out.println("--------混合");
			for (int i = 0; i < Bet_JCZQ_Activity.list_JC_Details.size(); i++) {
				if (max_peilv.containsKey(Bet_JCZQ_Activity.list_JC_Details
						.get(i).getMatchID())) {
					
					JC_Details iJC_Details= Bet_JCZQ_Activity.list_JC_Details.get(i);
					for(int j = 0; j < Bet_JCZQ_Activity.list_JC_Details.size(); j++){
						JC_Details jJC_Details= Bet_JCZQ_Activity.list_JC_Details.get(j);
						if(jJC_Details.getMatchID() == iJC_Details.getMatchID()) {
							if(jJC_Details.getResult().equals("500") && iJC_Details.getResult().equals("100")
									|| jJC_Details.getResult().equals("100") && iJC_Details.getResult().equals("500")) {
								Bet_JCZQ_Activity.list_JC_Details.get(i).setPeilv(Double.parseDouble(iJC_Details.getPeilv())+Double.parseDouble(jJC_Details.getPeilv()));
								Bet_JCZQ_Activity.list_JC_Details.get(j).setPeilv(0.0);
							}else if(jJC_Details.getResult().equals("502") && iJC_Details.getResult().equals("102") 
									|| jJC_Details.getResult().equals("102") && iJC_Details.getResult().equals("502")) {
								Bet_JCZQ_Activity.list_JC_Details.get(i).setPeilv(Double.parseDouble(iJC_Details.getPeilv())+Double.parseDouble(jJC_Details.getPeilv()));
								Bet_JCZQ_Activity.list_JC_Details.get(j).setPeilv(0.0);
							}
						}
					}
					
					if (Double.parseDouble(Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv()) > Double.parseDouble(max_peilv
							.get(Bet_JCZQ_Activity.list_JC_Details.get(i)
									.getMatchID()))) {
						max_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
								.getMatchID(),
								Bet_JCZQ_Activity.list_JC_Details.get(i)
										.getPeilv());
					} else
						continue;
				} else
					max_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
							.getMatchID(), Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv());

			}
		} else {
			for (int i = 0; i < Bet_JCZQ_Activity.list_JC_Details.size(); i++) {
				if (max_peilv.containsKey(Bet_JCZQ_Activity.list_JC_Details
						.get(i).getMatchID())) {
					if (Double.parseDouble(Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv()) > Double.parseDouble(max_peilv
							.get(Bet_JCZQ_Activity.list_JC_Details.get(i)
									.getMatchID()))) {
						max_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
								.getMatchID(),
								Bet_JCZQ_Activity.list_JC_Details.get(i)
										.getPeilv());
					} else
						continue;
				} else
					max_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
							.getMatchID(), Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv());
			}
		}
		if (list_max_jiangjin.size() > 0) {
			list_max_jiangjin.clear();
		}
		Iterator iter = max_peilv.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String val = (String) entry.getValue();
			list_max_jiangjin.add(Double.parseDouble(val));
		}
	}

	/**
	 * 得到每场对阵的最小赔率
	 */
	private void get_min_peilv_list() {
		if ((playtype == 5 && 0 == passtype)
				|| (playtype == 7 && 0 == passtype)
				|| (playtype == 8 && 0 == passtype)) {
			System.out.println("--------混合");
			for (int i = 0; i < list_JC_Details.size(); i++) {
				if (min_peilv.containsKey(list_JC_Details.get(i).getMatchID())) {
					if (Double.parseDouble(list_JC_Details.get(i).getPeilv()) < Double
							.parseDouble(min_peilv.get(list_JC_Details.get(i)
									.getMatchID()))) {
						min_peilv.put(list_JC_Details.get(i).getMatchID(),
								list_JC_Details.get(i).getPeilv());
					} else
						continue;
				} else
					min_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
							.getMatchID(), Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv());
			}
		} else {
			for (int i = 0; i < Bet_JCZQ_Activity.list_JC_Details.size(); i++) {
				if (min_peilv.containsKey(Bet_JCZQ_Activity.list_JC_Details
						.get(i).getMatchID())) {
					if (Double.parseDouble(Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv()) < Double.parseDouble(min_peilv
							.get(Bet_JCZQ_Activity.list_JC_Details.get(i)
									.getMatchID()))) {
						min_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
								.getMatchID(),
								Bet_JCZQ_Activity.list_JC_Details.get(i)
										.getPeilv());
					} else
						continue;
				} else
					min_peilv.put(Bet_JCZQ_Activity.list_JC_Details.get(i)
							.getMatchID(), Bet_JCZQ_Activity.list_JC_Details
							.get(i).getPeilv());
			}
		}

		if (list_min_jiangjin.size() > 0) {
			list_min_jiangjin.clear();
		}
		Iterator iter = min_peilv.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String val = (String) entry.getValue();
			list_min_jiangjin.add(Double.parseDouble(val));
		}

	}
}
