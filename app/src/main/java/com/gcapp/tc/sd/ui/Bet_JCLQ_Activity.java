package com.gcapp.tc.sd.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.dataaccess.JC_Details;
import com.gcapp.tc.dataaccess.Show_JC_Details;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapter_jclq_52;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapter_jclq_52_danguan;
import com.gcapp.tc.sd.ui.adapter.MyBetLotteryJCLQAdapter;
import com.gcapp.tc.sd.ui.adapter.SelectPasstype_List_JCAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.JiangjinTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * Bet_JCLQ_Activity功能：竞彩篮球投注页面
 */
@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
public class Bet_JCLQ_Activity extends Activity implements OnClickListener {
	
	private Context context = Bet_JCLQ_Activity.this;
	private static final String TAG = "Bet_JCLQ_Activity";
	private LinearLayout ll_pass2;
	/** 新添加的*/
	private ListView passType_list;
	private SelectPasstype_List_JCAdapter listAdapter;
	private RelativeLayout layout_cbs, layout_notjc;
	private ImageView line_shade2;
	private LinearLayout layout_jc;// 竞彩布局
	private ImageView bet_btn_deleteall;
	private LinearLayout btn_continue_select;
	private ImageButton btn_back;
	private TextView btn_clear, btn_join, btn_pay;
	private TextView bet_tv_guize; // 委托投注规则
	// 发起合买
	private EditText et_bei;
	private TextView tv_count, tv_money;
	private TextView tv_show_passway;
	private TextView tv_show_passway2, tv_show_passway3;// 过关
	private Intent intent;

	private MyListView2 listView;
	private MyBetLotteryJCLQAdapter betAdapter;
	// /** 所选的集合 */
	public LinkedHashMap<Integer, LinkedHashMap<Integer, String>> select_hashMap = new LinkedHashMap<Integer, LinkedHashMap<Integer, String>>();
	/** 装 组下标 — 子类下标 的集合*/
	private ArrayList<String> listStr;

	private List<String> listResult = new ArrayList<String>();
	private List<String> listResult_dan = new ArrayList<String>();
	private Set<Integer> checkIndex;
	private int radio_index = -1;
	/** 选择了几场比赛 **/
	private long totalCount = 0;
	/** 玩法Id*/
	private int type2;
	/** 0：过关 ; 1:单关*/
	private int ways;
	public TextView tv_jiangjin;// 总奖金
	private static LinearLayout layout_tip_jiangjin;
	private String passType = "";
	private int viewPagerCurrentIndex = 0;
	private ArrayList<String> playtype_list;
	private ConfirmDialog dialog;// 提示框
	private ConfirmDialog orderdialog;
	private DecimalFormat format = new DecimalFormat("#####0.00");
	public static ArrayList<JC_Details> list_JC_Details;
	private List<Show_JC_Details> list_Show = new ArrayList<Show_JC_Details>();
	private LinkedHashMap<String, String> max_peilv_youhua = new LinkedHashMap<String, String>();
	private RelativeLayout rlBonus;
	private int mCount = 0;

	private LinkedHashMap<String, String> max_peilv = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> min_peilv = new LinkedHashMap<String, String>();
	private List<Double> list_max_jiangjin = new ArrayList<Double>();
	private List<Double> list_min_jiangjin = new ArrayList<Double>();
	private double max_jiangjin, min_jiangjin;
	/** 最大投注倍数*/
	private int maxMultiple = 9999;
	/** intent回传值*/
	private int Result_OK = 1;
	/** 优惠券Id*/
	private String voucherID = "";
	private RelativeLayout coupon_layout;
	private TextView coupon_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bet_jczq);
		App.activityS.add(this);
		App.activityS1.add(this);

		rlBonus = (RelativeLayout) this.findViewById(R.id.rl_bonus);
		rlBonus.setVisibility(View.VISIBLE);
		findViewById(R.id.tv_bonus).setVisibility(View.GONE);
		TextView title = (TextView) findViewById(R.id.btn_playtype);
		title.setText("竞彩篮球投注");
		findViewById(R.id.iv_up_down).setVisibility(View.GONE);
		findViewById(R.id.btn_help).setVisibility(View.GONE);
		init();
		findView();
		setListener();
		setCountText();
		
		if (ways == 0) {
			selectType();
		}
	}

	/**
	 * 初始化属性
	 */
	private void init() {
		type2 = getIntent().getIntExtra("type", 7301);
		ways = getIntent().getIntExtra("ways", 0);
		if (ways == 1) {
			passType = "A0";
		}
		dialog = new ConfirmDialog(this, R.style.dialog);
		orderdialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		passType_list = (ListView) findViewById(R.id.select_middle);
		tv_jiangjin = (TextView) this.findViewById(R.id.tv_jiangjin); // 奖金
		layout_tip_jiangjin = (LinearLayout) findViewById(R.id.layout_tip_jiangjin); // 奖金
		ll_pass2 = (LinearLayout) findViewById(R.id.ll_pass2);
		bet_tv_guize = (TextView) this.findViewById(R.id.bet_tv_guize);
		layout_cbs = (RelativeLayout) this.findViewById(R.id.layout_cbs);
		line_shade2 = (ImageView) this.findViewById(R.id.line_shade2);
		layout_notjc = (RelativeLayout) this.findViewById(R.id.layout_notjc);
		layout_jc = (LinearLayout) this.findViewById(R.id.layout_jc);
		bet_btn_deleteall = (ImageView) this
				.findViewById(R.id.bet_btn_deleteall);
		btn_continue_select = (LinearLayout) this
				.findViewById(R.id.btn_continue_select);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		coupon_layout = (RelativeLayout) this.findViewById(R.id.coupon_layout);
		coupon_text = (TextView) this.findViewById(R.id.coupon_text);

		btn_clear = (TextView) this.findViewById(R.id.btn_clearall);
		btn_pay = (TextView) findViewById(R.id.btn_submit);
		btn_join = (TextView) this.findViewById(R.id.btn_follow);
		tv_show_passway = (TextView) this.findViewById(R.id.tv_show_passway);
		tv_show_passway2 = (TextView) this.findViewById(R.id.tv_show_passway2);
		tv_show_passway3 = (TextView) this.findViewById(R.id.tv_show_passway3);
		tv_show_passway.setVisibility(View.VISIBLE);
		tv_show_passway2.setVisibility(View.VISIBLE);
		tv_show_passway3.setVisibility(View.GONE);

		et_bei = (EditText) this.findViewById(R.id.et_bei);
		tv_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		listView = (MyListView2) this.findViewById(R.id.bet_lv_scheme);
		setListStr();

		betAdapter = new MyBetLotteryJCLQAdapter(Bet_JCLQ_Activity.this,
				listStr, type2, ways);

		listAdapter = new SelectPasstype_List_JCAdapter(Bet_JCLQ_Activity.this,
				betAdapter.list_dan.size(), mCount, viewPagerCurrentIndex);
		passType_list.setAdapter(listAdapter);

		setApp();
		// 隐藏与显示
		btn_clear.setVisibility(View.GONE);
		layout_notjc.setVisibility(View.GONE);
		layout_cbs.setVisibility(View.GONE);
		line_shade2.setVisibility(View.GONE);
		layout_jc.setVisibility(View.VISIBLE);
		if(AppTools.user != null) {
			if(AppTools.user.getIsgreatMan().equals("True")) {
				btn_join.setVisibility(View.VISIBLE);
				maxMultiple = 99999;
			}
		}

		Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
				"过关方式"));
		if (ways == 1) {
			tv_show_passway.setText(Html.fromHtml(AppTools.changeStringColor(
					"#808080", "单关")));// 单关

			tv_show_passway2.setVisibility(View.GONE);
			ll_pass2.setVisibility(View.GONE);
		} else {
			tv_show_passway.setText(text); // 过关
			ll_pass2.setVisibility(View.VISIBLE);
		}
		btn_pay.setText("付款");
	}

	/**
	 * 给选的值赋值 *
	 */
	public void setListStr(LinkedHashMap<Integer, LinkedHashMap<Integer, String>> hashMap) {
		listStr = new ArrayList<String>();
		Set set = hashMap.entrySet();
		for (Object aSet : set) {
			LinkedHashMap<Integer, String> mm = new LinkedHashMap<Integer, String>();
			String str = "";
			LinkedHashMap.Entry mapentry = (LinkedHashMap.Entry) aSet;
			str = (Integer) mapentry.getKey() + "";
			Set set2 = (hashMap.get(mapentry.getKey())).entrySet();
			for (Object aSet2 : set2) {
				String str2 = "";
				LinkedHashMap.Entry map = (LinkedHashMap.Entry) aSet2;

				mm.put(Integer.parseInt(map.getKey().toString()), map
						.getValue().toString());

				str2 = str + "-" + map.getKey();
				listStr.add(str2);
			}
			select_hashMap.put(Integer.parseInt(mapentry.getKey().toString()),
					mm);
		}
	}

	/**
	 * 根据玩法来给集合赋值
	 */
	public void setListStr() {
		if (ways == 0) {
			switch (type2) {
			case 7301:
				setListStr(ExpandAdapter_jclq_52.map_hashMap_sf);
				break;
			case 7304:
				setListStr(ExpandAdapter_jclq_52.map_hashMap_dx);
				break;
			case 7302:
				setListStr(ExpandAdapter_jclq_52.map_hashMap_rfsf);
				break;
			case 7303:
				setListStr(ExpandAdapter_jclq_52.map_hashMap_cbf);
				break;
			case 7306:
				setListStr(ExpandAdapter_jclq_52.map_hashMap_hhtz);
				break;
			}
		} else {
			switch (type2) {
			case 7301:
				setListStr(ExpandAdapter_jclq_52_danguan.map_hashMap_sf);
				break;
			case 7304:
				setListStr(ExpandAdapter_jclq_52_danguan.map_hashMap_dx);
				break;
			case 7302:
				setListStr(ExpandAdapter_jclq_52_danguan.map_hashMap_rfsf);
				break;
			case 7303:
				setListStr(ExpandAdapter_jclq_52_danguan.map_hashMap_cbf);
				break;
			case 7306:
				setListStr(ExpandAdapter_jclq_52_danguan.map_hashMap_hhtz);
				break;
			}
		}

	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		listView.setAdapter(betAdapter);
		bet_btn_deleteall.setOnClickListener(this);
		btn_continue_select.setOnClickListener(this);
//		rlBonus.setOnClickListener(this);
		bet_tv_guize.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		tv_show_passway.setOnClickListener(this);
		btn_pay.setOnClickListener(this);
		btn_join.setOnClickListener(this);
		layout_jc.setOnClickListener(this);
		coupon_layout.setOnClickListener(this);
		et_bei.addTextChangedListener(bei_textWatcher);
	}

	/**
	 * 当文本的值改变时
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
					et_bei.setText(""+maxMultiple);
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最大倍数为"+maxMultiple);
				}
				if (Integer.parseInt(edt.toString().trim()) == 0) {
					et_bei.setText("1");
					et_bei.setSelection(et_bei.getText().length());
					MyToast.getToast(getApplicationContext(), "最小为1倍");
				}
				if (edt.toString().substring(0, 1).equals("0")) {
					et_bei.setText(edt.toString().substring(1,
							edt.toString().length()));
					et_bei.setSelection(0);
				}
			}
			setCountText();
			getJiangjin();
		}
	};

	/**
	 * 刷新页面数据
	 */
	public void updateAdapter() {
		betAdapter.notifyDataSetChanged();
		setTotalCount();
		setCountText();
	}

	/**
	 * 刷新适配器
	 */
	public void updateAdapter(String from) {
		if (from.equals("Bet_sfc_hhtz_jclq_Dialog")) {
			clean_passType();
			setListStr();
			betAdapter.setListDtmatch(listStr);
			betAdapter.notifyDataSetChanged();
			setApp();
			updateAdapter();
		}
	}

	/**
	 * 刷新适配器
	 */
	public void updateAdapter(int mark) {
		if (mark == 1) {
			betAdapter.notifyDataSetChanged();
			this.setListResult();// 应该注释
			setTotalCount();
			setCountText();
		}
	}

	/**
	 * 清空 *
	 */
	public void doClear() {
		dialog.show();
		dialog.setDialogContent("您确认清空所有已选号码？");
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) {// 确定
					for (String str : listStr) {
						Integer s = Integer.parseInt(str.split("-")[0]);
						// 清空
						select_hashMap.get(s).clear();
					}
					betAdapter.clear();
					Bet_JCLQ_Activity.this.passType = "";
					if (null != checkIndex)
						Bet_JCLQ_Activity.this.checkIndex.clear();
					Bet_JCLQ_Activity.this.radio_index = -1;
					clean_passType();
					updateAdapter(1);
				}
			}
		});
	}

	/**
	 * 增加一注 *
	 */
	public void doAdd() {
		this.passType = "";
		if (null != checkIndex)
			this.checkIndex.clear();
		this.radio_index = -1;
		setListResult();
		betAdapter.list_dan.clear();
		if (ways == 0) {
			intent = new Intent(Bet_JCLQ_Activity.this,
					Select_JCLQ_Activity.class);
		} else {
			intent = new Intent(Bet_JCLQ_Activity.this,
					Select_JCLQ_DAN_Activity.class);
		}
		intent.putExtra("from", "Bet_JCLQ_Activity");
		intent.putExtra("playType", type2);
		intent.putExtra("canChange", true);
		intent.putExtra("ways", ways);
		intent.putStringArrayListExtra("select_index", listStr);
		intent.putStringArrayListExtra("select", toSelectExtra());
		Bet_JCLQ_Activity.this.startActivity(intent);
	}

	/**
	 * 得到投注内容
	 * 
	 * @return
	 */
	private ArrayList<String> toSelectExtra() {
		ArrayList<String> temp_select = new ArrayList<String>();
		for (int i = 0; i < listStr.size(); i++) {
			String str = listStr.get(i);
			Integer s = Integer.parseInt(str.split("-")[0]);
			Integer s2 = Integer.parseInt(str.split("-")[1]);
			// 拿到投注内容
			String re = select_hashMap.get(s).get(s2);
			temp_select.add(re);
		}
		return temp_select;
	}

	/**
	 * 给所选集合赋值 *
	 */
	public void setListResult() {
		LinkedHashMap<Integer, LinkedHashMap<Integer, String>> hashMap = null;
		if (ways == 0) {
			switch (type2) {
			case 7301:
				hashMap = ExpandAdapter_jclq_52.map_hashMap_sf;
				break;
			case 7304:
				hashMap = ExpandAdapter_jclq_52.map_hashMap_dx;
				break;
			case 7302:
				hashMap = ExpandAdapter_jclq_52.map_hashMap_rfsf;
				break;
			case 7303:
				hashMap = ExpandAdapter_jclq_52.map_hashMap_cbf;
				break;
			case 7306:
				hashMap = ExpandAdapter_jclq_52.map_hashMap_hhtz;
				break;
			}
		} else {
			switch (type2) {
			case 7301:
				hashMap = ExpandAdapter_jclq_52_danguan.map_hashMap_sf;
				break;
			case 7304:
				hashMap = ExpandAdapter_jclq_52_danguan.map_hashMap_dx;
				break;
			case 7302:
				hashMap = ExpandAdapter_jclq_52_danguan.map_hashMap_rfsf;
				break;
			case 7303:
				hashMap = ExpandAdapter_jclq_52_danguan.map_hashMap_cbf;
				break;
			case 7306:
				hashMap = ExpandAdapter_jclq_52_danguan.map_hashMap_hhtz;
				break;
			}
		}

		listStr.clear();
		Set set = select_hashMap.entrySet();
		for (Object aSet : set) {
			LinkedHashMap<Integer, String> mm = new LinkedHashMap<Integer, String>();
			String str = "";
			LinkedHashMap.Entry mapentry = (LinkedHashMap.Entry) aSet;
			str = mapentry.getKey() + "";
			Set set2 = (select_hashMap.get(mapentry.getKey())).entrySet();
			for (Object aSet2 : set2) {
				String str2 = "";
				LinkedHashMap.Entry map = (LinkedHashMap.Entry) aSet2;

				mm.put(Integer.parseInt(map.getKey().toString()), map
						.getValue().toString());

				str2 = str + "-" + map.getKey();
				listStr.add(str2);
			}
			if (hashMap != null) {
				hashMap.put(Integer.parseInt(mapentry.getKey().toString()), mm);
			}
		}
	}

	/**
	 * 跳到合买
	 */
	private void doJoin() {
		if (passType.length() == 0) {
			if (ways == 0) {
				selectType();
			}
			return;
		}
		if (totalCount == 0) {
			MyToast.getToast(getApplicationContext(), "您还没有选择对阵");
			return;
		}
		setApp();
		intent = new Intent(Bet_JCLQ_Activity.this, JoinActivity.class);
		intent.putExtra("totalMoney", (totalCount * 2 * AppTools.bei) + "");
		intent.putExtra("shareMoney", this.getTotalCount() * 2);
		Bet_JCLQ_Activity.this.startActivity(intent);
	}

	/**
	 * 点击事件 *
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 清空 **/
		case R.id.bet_btn_deleteall:
			doClear();
			break;
		/** 退出 **/
		case R.id.btn_back:
			esc();
			break;
		/** 付款 **/
		case R.id.btn_submit:
			if(AppTools.user != null){
				if(this.getTotalCount() * AppTools.bei * 2 < 10) {
					MyToast.getToast(context, "投注金额不能低于10元");
				}else{
					paymentMethod();
				}
//				if(AppTools.user.getIsgreatMan().equals("True")&&this.getTotalCount() * AppTools.bei * 2 >= 100){
//					dialog.show();
//					dialog.setDialogContent("是否发单？");
//					dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
//						@Override
//						public void getResult(int resultCode) {
//							if (1 == resultCode) {// 确定
//								doJoin();
//							}else{
//								paymentMethod();
//							}
//						}
//					});
//				}else{
//					paymentMethod();
//				}
			}else{
				MyToast.getToast(Bet_JCLQ_Activity.this, "请先登陆");
				intent = new Intent(Bet_JCLQ_Activity.this, LoginActivity.class);
				intent.putExtra("loginType", "bet");
				Bet_JCLQ_Activity.this.startActivity(intent);
			}
			
			break;
		case R.id.tv_bonus:
		case R.id.rl_bonus:
			if (ways == 1 && "0".equals(tv_count.getText().toString().trim())) {
				MyToast.getToast(context, "未选择比赛");
			} else if (passType.length() == 0) {
				if (ways == 0) {
					selectType();
				}
				// MyToast.getToast(context, "未选择过关方式");
			} else if (listResult_dan.size() != 0) {
				MyToast.getToast(context, "不支持胆拖玩法");
			} else if (viewPagerCurrentIndex == 1) {
				MyToast.getToast(context, "该串关不支持奖金优化");
//			} else if (7306 == type2) {
//				MyToast.getToast(context, "混投不支持奖金优化");
			} else {
				getBonusOptimization();
				setDate();
				get_max_peilv_list_youhua();
				intent = new Intent(Bet_JCLQ_Activity.this,
						Bonus_JCLQ_Activity.class);
				intent.putExtra("multipleFreePass",
						passType.contains(",") ? true : false);
				intent.putExtra("totalMoney",
						Long.parseLong(tv_money.getText().toString().trim()));
				Bet_JCLQ_Activity.this.startActivity(intent);
			}
			break;

		/** 发单 **/
		case R.id.btn_follow:
			if( totalCount * 2 *AppTools.bei < 100){
				MyToast.getToast(context, "投注金额不能低于100元");
			}else{
				doJoin();
			}
			break;
		/** 增加选号 **/
		case R.id.btn_continue_select:
			doAdd();
			break;

		// 选择过关方式
		case R.id.tv_show_passway:
		case R.id.layout_jc:
			if (ways == 0) {
				selectType();
			}
			break;

		case R.id.tv_ckName2:
			Intent intent = new Intent(Bet_JCLQ_Activity.this,
					PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_JCLQ_Activity.this.startActivity(intent);
			break;
		case R.id.bet_tv_guize:
			intent = new Intent(Bet_JCLQ_Activity.this, PlayDescription.class);
			intent.putExtra("type", 0);
			Bet_JCLQ_Activity.this.startActivity(intent);
			break;
		case R.id.coupon_layout:
			if(AppTools.user != null && totalCount * 2 * AppTools.bei>0){
				Intent intentCoupon = new Intent(Bet_JCLQ_Activity.this,
						CouponActivity.class);
				intentCoupon.putExtra("money",""+totalCount * 2 * AppTools.bei );
				startActivityForResult(intentCoupon,Result_OK);
			}else if(totalCount * 2 * AppTools.bei <= 0) {
				MyToast.getToast(context, "请选择过关方式!");
			}else{
				Intent intentLogin = new Intent(Bet_JCLQ_Activity.this,LoginActivity.class);
				startActivity(intentLogin);
			}
			break;
		default:
			break;
		}
	}
	
	private void paymentMethod() {
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Result_OK){
			String couponMoney = data.getStringExtra("money");
			coupon_text.setText("优惠"+couponMoney+"元");
			tv_money.setText(this.getTotalCount() * AppTools.bei * 2 -Integer.parseInt(couponMoney)+ "");
			voucherID = data.getStringExtra("voucherID");
			et_bei.setEnabled(false);
//			rlBonus.setEnabled(false);
			tv_show_passway.setEnabled(false);
			layout_jc.setEnabled(false);
			bet_btn_deleteall.setEnabled(false);
		}
	}

	/**
	 * 提交购买请求
	 */
	public void commit() {
		if (passType.length() == 0) {
			if (ways == 0) {
				selectType();
			}
			// MyToast.getToast(Bet_JCLQ_Activity.this, "请先选择过关方式");
			return;

		}
		if (totalCount == 0 || tv_count.getText().equals("0")) {
			MyToast.getToast(Bet_JCLQ_Activity.this, "请先至少选择一注");
			return;
		}
		if (AppTools.user != null) {
			btn_pay.setEnabled(false);// 控制按钮重复点击

			setApp();
			RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
					"正在支付...") {
				@Override
				public void responseCallback(JSONObject object) {
					// setEnabled(true);
					btn_pay.setEnabled(true);// 控制按钮重复点击
					if (RequestUtil.DEBUG)
						Log.i(TAG, "竞彩篮球投注支付结果" + object);
					String error = object.optString("error");
					String resultMsg = object.optString("msg");
					if ("0".equals(error)) {
						AppTools.user.setBalance(object.optDouble("balance"));
						AppTools.user.setFreeze(object.optDouble("freeze"));
						AppTools.schemeId = object.optInt("schemeids");
						AppTools.lottery.setChaseTaskID(object
								.optInt("chasetaskids"));

						String SucJinECost = format.format(object
								.optDouble("currentMoeny"));
						String SucCaiJinCost = format.format(object
								.optDouble("currentHandsel"));
						String SuccMoney = format.format(object
								.optDouble("handselMoney"));
						String voucherMoney = format.format(object
								.optDouble("voucherMoney"));
						AppTools.totalCount = 0;
						ExpandAdapter_jclq_52.map_hashMap_dx.clear();
						ExpandAdapter_jclq_52.map_hashMap_sf.clear();
						ExpandAdapter_jclq_52.map_hashMap_rfsf.clear();
						ExpandAdapter_jclq_52.map_hashMap_cbf.clear();
						ExpandAdapter_jclq_52.map_hashMap_hhtz.clear();

						ExpandAdapter_jclq_52_danguan.map_hashMap_dx.clear();
						ExpandAdapter_jclq_52_danguan.map_hashMap_sf.clear();
						ExpandAdapter_jclq_52_danguan.map_hashMap_rfsf.clear();
						ExpandAdapter_jclq_52_danguan.map_hashMap_cbf.clear();
						ExpandAdapter_jclq_52_danguan.map_hashMap_hhtz.clear();
						AppTools.totalCount = 0;
						// 结束所有的跳到主页面
						Intent intent = new Intent(getApplicationContext(),
								PaySuccessActivity.class);
						viewPagerCurrentIndex = 0;
						passType = "";
						intent.putExtra("paymoney",
								(totalCount * 2 * AppTools.bei));

						intent.putExtra("currentMoeny", SucJinECost);
						intent.putExtra("currentHandsel", SucCaiJinCost);
						intent.putExtra("handselMoney", SuccMoney);
						intent.putExtra("voucherMoney", voucherMoney);
						intent.putExtra("passtype_lanqiu", ways);

						Bet_JCLQ_Activity.this.startActivity(intent);
					} else if (error.equals(AppTools.ERROR_MONEY + "")) {
						LotteryUtils.showMoneyLess(Bet_JCLQ_Activity.this,
								(totalCount * 2));
					} else if (error.equals("-500")) {
						MyToast.getToast(Bet_JCLQ_Activity.this, "连接超时");
					} else {
						MyToast.getToast(Bet_JCLQ_Activity.this, resultMsg);
					}
				}

				@Override
				public void responseError(VolleyError error) {
					btn_pay.setEnabled(true);// 控制按钮重复点击
					MyToast.getToast(Bet_JCLQ_Activity.this, "抱歉，支付出现未知错误..");
					if (RequestUtil.DEBUG)
						Log.e(TAG, "投注支付报错" + error.getMessage());
				}
			};
			requestUtil.commitBetting(totalCount, 0, 1,voucherID);
		} else {
			MyToast.getToast(Bet_JCLQ_Activity.this, "请先登陆");
			intent = new Intent(Bet_JCLQ_Activity.this, LoginActivity.class);
			intent.putExtra("loginType", "bet");
			Bet_JCLQ_Activity.this.startActivity(intent);
		}
	}

	/**
	 * 清空过关方式
	 */
	public void clean_passType() {
		layout_tip_jiangjin.setVisibility(View.GONE);
		if (ways == 1)
			return;
		viewPagerCurrentIndex = 0;
		passType = "";
		Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
				"过关方式"));
		tv_show_passway.setText(text);// 过关
	}

	/**
	 * 计算预测奖金
	 */
	private void getJiangjin() {
		if (min_peilv.size() > 0) {
			min_peilv.clear();
		}
		if (max_peilv.size() > 0) {
			max_peilv.clear();
		}
		getBonusOptimization();
		get_max_peilv_list();
		get_min_peilv_list();
		String type = setText(passType);

		max_jiangjin = JiangjinTools.getjczq_jiangjin("max", type,
				list_max_jiangjin) * (AppTools.bei);
		min_jiangjin = JiangjinTools.getjczq_jiangjin("min", type,
				list_min_jiangjin) * (AppTools.bei);
		DecimalFormat df = new DecimalFormat("########0.00");
		String result = df.format(max_jiangjin);
		max_jiangjin = Double.parseDouble(result);

		String result2 = df.format(min_jiangjin);
		min_jiangjin = Double.parseDouble(result2);

		// max_jiangjin = JiangjinTools.getjczq_jiangjin("max", type,
		// list_max_jiangjin);
		// min_jiangjin = JiangjinTools.getjczq_jiangjin("min", type,
		// list_min_jiangjin);

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
	 * 得到玩法窗口是否显示
	 * 
	 * @return
	 */
	public boolean getPopIsShow() {
		if (passType_list.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 选择过关方式
	 */
	public void selectType() {
		mCount = betAdapter.getIndexsize();
		if (type2 == 7303) {
			if (mCount > 4) {
				mCount = 4;
			}
		}
		for (int i = 0; i < listResult.size(); i++) {
			if (7306 == type2) {
				String[] arry = listResult.get(i).split(",");
				for (int j = 0; j < arry.length; j++) {
					if (arry[j].length() < 3) {// 判断长度是否小于3,则为胜分差
						if (mCount > 4) {
							mCount = 4;
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
					Bet_JCLQ_Activity.this, betAdapter.list_dan.size(), mCount,
					viewPagerCurrentIndex);
			passType_list.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
		} else {
			passType_list.setVisibility(View.GONE);
			tv_show_passway.setVisibility(View.VISIBLE);
			if (tv_show_passway.getText().toString().trim().equals("过关方式")) {
				tv_show_passway2.setVisibility(View.VISIBLE);
				ll_pass2.setVisibility(View.VISIBLE);
			} else {
				tv_show_passway2.setVisibility(View.GONE);
				ll_pass2.setVisibility(View.GONE);
			}

			tv_show_passway3.setVisibility(View.GONE);
		}

		if (0 != passType.length()) {// 不为空
			playtype_list = new ArrayList<String>();
			for (int i = 0; i < passType.split(",").length; i++) {
				playtype_list.add(passType.split(",")[i]);
			}
			listAdapter.setSelectPassType(playtype_list);
		} else {
			listAdapter.setSelectPassType(new ArrayList<String>());
		}
		listAdapter
				.setDialogResultListener(new SelectPasstype_List_JCAdapter.DialogResultListener() {
					@Override
					public void getResult(int resultCode,
							ArrayList<String> selectResult, int type) {
						if (1 == resultCode) {// 确定
							viewPagerCurrentIndex = type;
							passType = "";
							for (int i = 0; i < selectResult.size(); i++) {
								if (i == 0)
									passType += selectResult.get(i);
								else
									passType += "," + selectResult.get(i);
							}
							setPassText(setText(passType));
							getJiangjin();
						}
					}
				});
		updateAdapter();
	}

	public void setPassType(String s) {
		this.passType = s;
	}

	/**
	 * 计算总注数 *
	 */
	public void setTotalCount() {
		this.totalCount = 0;
		String type = passType;
		if (type.length() == 0) {
			return;
		}
		setList();
		List<String> list = new ArrayList<String>();
		List<String> list_dan = new ArrayList<String>();
		if (ways == 1) { // 单关注数
			for (int i = 0; i < listResult.size(); i++) {
				String str = "";
				for (int j = 0; j < listResult.get(i).split(",").length; j++) {
					str += "1";
				}
				list.add(str);
			}
			this.totalCount = NumberTools.getCountBySinglePass(list);
			return;
		}

		for (int i = 0; i < listResult.size(); i++) {
			if (7306 != type2) {// 非混合投注
				String str = "";
				for (int j = 0; j < listResult.get(i).split(",").length; j++) {
					str += "1";
				}
				list.add(str);
			} else {// 混合投注
				StringBuilder sb_sf = new StringBuilder();
				StringBuilder sb_rfsf = new StringBuilder();
				StringBuilder sb_dxf = new StringBuilder();
				StringBuilder sb_sfc = new StringBuilder();
				String[] arry = listResult.get(i).split(",");
				for (int j = 0; j < arry.length; j++) {
					if (arry[j].length() < 3) {// 判断长度是否小于3,则为胜分差
						sb_sfc.append("4,");
					} else {
						String first = arry[j].charAt(0) + "";
						if ("1".equals(first)) {// 胜负
							sb_sf.append("1,");
						} else if ("2".equals(first)) {// 让分胜负
							sb_rfsf.append("2,");
						} else if ("3".equals(first)) {// 大小分
							sb_dxf.append("3,");
						}
					}
				}
				StringBuilder sb_hhtz = new StringBuilder();
				if (0 != sb_sf.length()) {// 选了胜负
					String str_sf = "";
					if (0 == sb_rfsf.length() && 0 == sb_dxf.length()
							&& 0 == sb_sfc.length()) {// 没有让分胜负，大小分，胜分差
						str_sf = sb_sf.substring(0, sb_sf.length() - 1);
					} else {
						str_sf = sb_sf.substring(0, sb_sf.length() - 1) + "|";
					}
					sb_hhtz.append(str_sf);
				}
				if (0 != sb_rfsf.length()) {// 选了让分胜负
					String str_rfsf = "";
					if (0 == sb_dxf.length() && 0 == sb_sfc.length()) {// 没有大小分，胜分差
						str_rfsf = sb_rfsf.substring(0, sb_rfsf.length() - 1);
					} else {
						str_rfsf = sb_rfsf.substring(0, sb_rfsf.length() - 1)
								+ "|";
					}
					sb_hhtz.append(str_rfsf);
				}
				if (0 != sb_dxf.length()) {// 选了大小分
					String str_dxf = "";
					if (0 == sb_sfc.length()) {// 没有胜分差
						str_dxf = sb_dxf.substring(0, sb_dxf.length() - 1);
					} else {
						str_dxf = sb_dxf.substring(0, sb_dxf.length() - 1)
								+ "|";
					}
					sb_hhtz.append(str_dxf);
				}
				if (0 != sb_sfc.length()) {// 选了胜分差
					String str_sfc = "";
					str_sfc = sb_sfc.substring(0, sb_sfc.length() - 1);
					sb_hhtz.append(str_sfc);
				}
				list.add(sb_hhtz.toString());
			}
		}
		if (betAdapter.list_dan.size() == 0) {
			if (7306 == type2) {// 混合投注
				setTotalCountHHTZ(type, list);
			} else {
				setTotalCount(type, list);
			}
		} else {
			for (int i = 0; i < listResult_dan.size(); i++) {
				String str = "";
				for (int j = 0; j < listResult_dan.get(i).split(",").length; j++) {
					str += "1";
				}
				list_dan.add(str);
			}
			setTotalCount(type, list_dan, list);
		}
	}

	/**
	 * 计算总注数-混合投注 *
	 */
	public void setTotalCountHHTZ(String type, List<String> list) {
		if (type.contains("AA"))
			this.totalCount += NumberTools.getAll2G1Mixed_hunhe(list);
		if (type.contains("AB"))
			this.totalCount += NumberTools.getAll3G1Mixed_hunhe(list);
		if (type.contains("AC"))
			this.totalCount += NumberTools.getAll3G3Mixed_hunhe(list);
		if (type.contains("AD"))
			this.totalCount += NumberTools.getAll3G4Mixed_hunhe(list);
		if (type.contains("AE"))
			this.totalCount += NumberTools.getAll4G1Mixed_hunhe(list);
		if (type.contains("AF"))
			this.totalCount += NumberTools.getAll4G4Mixed_hunhe(list);
		if (type.contains("AG"))
			this.totalCount += NumberTools.getAll4G5Mixed_hunhe(list);
		if (type.contains("AH"))
			this.totalCount += NumberTools.getAll4G6Mixed_hunhe(list);
		if (type.contains("AI"))
			this.totalCount += NumberTools.getAll4G11Mixed_hunhe(list);
		if (type.contains("AJ"))
			this.totalCount += NumberTools.getAll5G1Mixed_hunhe(list);
		if (type.contains("AK"))
			this.totalCount += NumberTools.getAll5G5Mixed_hunhe(list);
		if (type.contains("AL"))
			this.totalCount += NumberTools.getAll5G6Mixed_hunhe(list);
		if (type.contains("AM"))
			this.totalCount += NumberTools.getAll5G10Mixed_hunhe(list);
		if (type.contains("AN"))
			this.totalCount += NumberTools.getAll5G16Mixed_hunhe(list);
		if (type.contains("AO"))
			this.totalCount += NumberTools.getAll5G20Mixed_hunhe(list);
		if (type.contains("AP"))
			this.totalCount += NumberTools.getAll5G26Mixed_hunhe(list);
		if (type.contains("AQ"))
			this.totalCount += NumberTools.getAll6G1Mixed_hunhe(list);
		if (type.contains("AR"))
			this.totalCount += NumberTools.getAll6G6Mixed_hunhe(list);
		if (type.contains("AS"))
			this.totalCount += NumberTools.getAll6G7Mixed_hunhe(list);
		if (type.contains("AT"))
			this.totalCount += NumberTools.getAll6G15Mixed_hunhe(list);
		if (type.contains("AU"))
			this.totalCount += NumberTools.getAll6G20Mixed_hunhe(list);
		if (type.contains("AV"))
			this.totalCount += NumberTools.getAll6G22Mixed_hunhe(list);
		if (type.contains("AW"))
			this.totalCount += NumberTools.getAll6G35Mixed_hunhe(list);
		if (type.contains("AX"))
			this.totalCount += NumberTools.getAll6G42Mixed_hunhe(list);
		if (type.contains("AY"))
			this.totalCount += NumberTools.getAll6G50Mixed_hunhe(list);
		if (type.contains("AZ"))
			this.totalCount += NumberTools.getAll6G57Mixed_hunhe(list);
		if (type.contains("BA"))
			this.totalCount += NumberTools.getAll7G1Mixed_hunhe(list);
		if (type.contains("BB"))
			this.totalCount += NumberTools.getAll7G7Mixed_hunhe(list);
		if (type.contains("BC"))
			this.totalCount += NumberTools.getAll7G8Mixed_hunhe(list);
		if (type.contains("BD"))
			this.totalCount += NumberTools.getAll7G21Mixed_hunhe(list);
		if (type.contains("BE"))
			this.totalCount += NumberTools.getAll7G35Mixed_hunhe(list);
		if (type.contains("BF"))
			this.totalCount += NumberTools.getAll7G120Mixed_hunhe(list);
		if (type.contains("BG"))
			this.totalCount += NumberTools.getAll8G1Mixed_hunhe(list);
		if (type.contains("BH"))
			this.totalCount += NumberTools.getAll8G8Mixed_hunhe(list);
		if (type.contains("BI"))
			this.totalCount += NumberTools.getAll8G9Mixed_hunhe(list);
		if (type.contains("BJ"))
			this.totalCount += NumberTools.getAll8G28Mixed_hunhe(list);
		if (type.contains("BK"))
			this.totalCount += NumberTools.getAll8G56Mixed_hunhe(list);
		if (type.contains("BL"))
			this.totalCount += NumberTools.getAll8G70Mixed_hunhe(list);
		if (type.contains("BM"))
			this.totalCount += NumberTools.getAll8G247Mixed_hunhe(list);
	}

	/**
	 * 给总注数赋值 没胆的情况下 *
	 */
	public void setTotalCount(String type, List<String> list_dan,
			List<String> list) {
		if (type.contains("AA"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 2);
		if (type.contains("AB"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 3);
		if (type.contains("AC"))
			this.totalCount += NumberTools.getAll3G3Mixed_dan(list_dan, list);
		if (type.contains("AD"))
			this.totalCount += NumberTools.getAll3G4Mixed_dan(list_dan, list);
		if (type.contains("AE"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 4);
		if (type.contains("AF"))
			this.totalCount += NumberTools.getAll4G4Mixed_dan(list_dan, list);
		if (type.contains("AG"))
			this.totalCount += NumberTools.getAll4G5Mixed_dan(list_dan, list);
		if (type.contains("AH"))
			this.totalCount += NumberTools.getAll4G6_11Mixed_dan(list_dan,
					list, 6);
		if (type.contains("AI"))
			this.totalCount += NumberTools.getAll4G6_11Mixed_dan(list_dan,
					list, 11);
		if (type.contains("AJ"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 5);
		if (type.contains("AK"))
			this.totalCount += NumberTools.getAll5G5Mixed_dan(list_dan, list);
		if (type.contains("AL"))
			this.totalCount += NumberTools.getAll5G6Mixed_dan(list_dan, list);
		if (type.contains("AM"))
			this.totalCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 10);
		if (type.contains("AN"))
			this.totalCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 16);
		if (type.contains("AO"))
			this.totalCount += NumberTools.getAll5G10_16_20Mixed_dan(list_dan,
					list, 20);
		if (type.contains("AP"))
			this.totalCount += NumberTools.getAll5G26Mixed_dan(list_dan, list);
		if (type.contains("AQ"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 6);
		if (type.contains("AR"))
			this.totalCount += NumberTools.getAll6G6Mixed_dan(list_dan, list);
		if (type.contains("AS"))
			this.totalCount += NumberTools.getAll6G7Mixed_dan(list_dan, list);
		if (type.contains("AT"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 15);
		if (type.contains("AU"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 20);
		if (type.contains("AV"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 22);
		if (type.contains("AW"))
			this.totalCount += NumberTools.getAll6G35Mixed_dan(list_dan, list);
		if (type.contains("AX"))
			this.totalCount += NumberTools.getAll6G42Mixed_dan(list_dan, list);
		if (type.contains("AY"))
			this.totalCount += NumberTools.getAll6G15_20_22_50Mixed_dan(
					list_dan, list, 50);
		if (type.contains("AZ"))
			this.totalCount += NumberTools.getAll6G57Mixed_dan(list_dan, list);

		if (type.contains("BA"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 7);

		if (type.contains("BB"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 7);

		if (type.contains("BC"))
			this.totalCount += NumberTools.getAll7G8Mixed_dan(list_dan, list);

		if (type.contains("BD"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 21);
		if (type.contains("BE"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 35);

		if (type.contains("BF"))
			this.totalCount += NumberTools.getAll7G7_21_35Mixed_dan(list_dan,
					list, 120);
		if (type.contains("BG"))
			this.totalCount += NumberTools
					.getAllnG1Mixed_dan(list_dan, list, 8);
		if (type.contains("BH"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 8);
		if (type.contains("BI"))
			this.totalCount += NumberTools.getAll8G9Mixed_dan(list_dan, list);
		if (type.contains("BJ"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 28);
		if (type.contains("BK"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 56);
		if (type.contains("BL"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 70);
		if (type.contains("BM"))
			this.totalCount += NumberTools.getAll8G8_28_56_70_247Mixed_dan(
					list_dan, list, 247);
	}

	/**
	 * 给总注数赋值 没胆的情况下 *
	 */
	public void setTotalCount(String type, List<String> list) {
		System.out.println(type);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		if (type.contains("AA"))
			this.totalCount += NumberTools.getAll2G1Mixed(list);
		if (type.contains("AB"))
			this.totalCount += NumberTools.getAll3G1Mixed(list);
		if (type.contains("AC"))
			this.totalCount += NumberTools.getAll3G3Mixed(list);
		if (type.contains("AD"))
			this.totalCount += NumberTools.getAll3G4Mixed(list);
		if (type.contains("AE"))
			this.totalCount += NumberTools.getAll4G1Mixed(list);
		if (type.contains("AF"))
			this.totalCount += NumberTools.getAll4G4Mixed(list);
		if (type.contains("AG"))
			this.totalCount += NumberTools.getAll4G5Mixed(list);
		if (type.contains("AH"))
			this.totalCount += NumberTools.getAll4G6Mixed(list);
		if (type.contains("AI"))
			this.totalCount += NumberTools.getAll4G11Mixed(list);
		if (type.contains("AJ"))
			this.totalCount += NumberTools.getAll5G1Mixed(list);
		if (type.contains("AK"))
			this.totalCount += NumberTools.getAll5G5Mixed(list);
		if (type.contains("AL"))
			this.totalCount += NumberTools.getAll5G6Mixed(list);
		if (type.contains("AM"))
			this.totalCount += NumberTools.getAll5G10Mixed(list);
		if (type.contains("AN"))
			this.totalCount += NumberTools.getAll5G16Mixed(list);
		if (type.contains("AO"))
			this.totalCount += NumberTools.getAll5G20Mixed(list);
		if (type.contains("AP"))
			this.totalCount += NumberTools.getAll5G26Mixed(list);
		if (type.contains("AQ"))
			this.totalCount += NumberTools.getAll6G1Mixed(list);
		if (type.contains("AR"))
			this.totalCount += NumberTools.getAll6G6Mixed(list);
		if (type.contains("AS"))
			this.totalCount += NumberTools.getAll6G7Mixed(list);
		if (type.contains("AT"))
			this.totalCount += NumberTools.getAll6G15Mixed(list);
		if (type.contains("AU"))
			this.totalCount += NumberTools.getAll6G20Mixed(list);
		if (type.contains("AV"))
			this.totalCount += NumberTools.getAll6G22Mixed(list);
		if (type.contains("AW"))
			this.totalCount += NumberTools.getAll6G35Mixed(list);
		if (type.contains("AX"))
			this.totalCount += NumberTools.getAll6G42Mixed(list);
		if (type.contains("AY"))
			this.totalCount += NumberTools.getAll6G50Mixed(list);
		if (type.contains("AZ"))
			this.totalCount += NumberTools.getAll6G57Mixed(list);
		if (type.contains("BA"))
			this.totalCount += NumberTools.getAll7G1Mixed(list);
		if (type.contains("BB"))
			this.totalCount += NumberTools.getAll7G7Mixed(list);
		if (type.contains("BC"))
			this.totalCount += NumberTools.getAll7G8Mixed(list);
		if (type.contains("BD"))
			this.totalCount += NumberTools.getAll7G21Mixed(list);
		if (type.contains("BE"))
			this.totalCount += NumberTools.getAll7G35Mixed(list);
		if (type.contains("BF"))
			this.totalCount += NumberTools.getAll7G120Mixed(list);
		if (type.contains("BG"))
			this.totalCount += NumberTools.getAll8G1Mixed(list);
		if (type.contains("BH"))
			this.totalCount += NumberTools.getAll8G8Mixed(list);
		if (type.contains("BI"))
			this.totalCount += NumberTools.getAll8G9Mixed(list);
		if (type.contains("BJ"))
			this.totalCount += NumberTools.getAll8G28Mixed(list);
		if (type.contains("BK"))
			this.totalCount += NumberTools.getAll8G56Mixed(list);
		if (type.contains("BL"))
			this.totalCount += NumberTools.getAll8G70Mixed(list);
		if (type.contains("BM"))
			this.totalCount += NumberTools.getAll8G247Mixed(list);
	}

	/**
	 * 得到总注数 *
	 */
	public long getTotalCount() {
		setTotalCount();
		return this.totalCount;
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			esc();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 点击确认退出程序
	 */
	class positiveClick implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			ExpandAdapter_jclq_52.map_hashMap_sf.clear();
			ExpandAdapter_jclq_52.map_hashMap_dx.clear();
			ExpandAdapter_jclq_52_danguan.map_hashMap_sf.clear();
			ExpandAdapter_jclq_52_danguan.map_hashMap_dx.clear();
			Bet_JCLQ_Activity.this.finish();
		}
	}

	/**
	 * 退出界面
	 */
	private void esc() {
		dialog.show();
		dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
		dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
			@Override
			public void getResult(int resultCode) {
				if (1 == resultCode) {// 确定
					ExpandAdapter_jclq_52.map_hashMap_sf.clear();
					ExpandAdapter_jclq_52.map_hashMap_dx.clear();
					ExpandAdapter_jclq_52_danguan.map_hashMap_sf.clear();
					ExpandAdapter_jclq_52_danguan.map_hashMap_dx.clear();
					for (int i = 0; i < App.activityS1.size(); i++) {
						App.activityS1.get(i).finish();
					}
				}
			}
		});
	}

	/**
	 * 清空过关方式
	 */
	public void clearTypedialog() {
		if (tv_show_passway.getText().toString().contains(",")
				|| tv_show_passway.getText().toString().length() == 3) {
			String[] array = tv_show_passway.getText().toString().split(",");
			int a = betAdapter.list_dan.size();
			for (int i = 0; i < array.length; i++) {
				int b = Integer.parseInt(array[i].substring(0, 1));
				if (b <= a) {
					String[] ar = new String[array.length - 1];
					for (int j = 0; j < array.length - 1; j++) {
						ar[j] = array[j + 1];
					}
					String str = "";
					for (int j = 0; j < ar.length; j++) {
						if (j == ar.length - 1) {
							str += (String) ar[j];
						} else
							str += (String) ar[j] + ",";
					}
					setPassText(str);
				}
			}
		}

		checkIndex = null;
		passType = "";

	}

	public int getType() {
		return this.type2;
	}

	/**
	 * 给过关方式按钮赋值 *
	 */
	public void setPassText(String str) {
		if (str.length() == 0) {
			Spanned text = Html.fromHtml(AppTools.changeStringColor("#808080",
					"过关方式"));
			this.tv_show_passway.setText(text);// 过关
			ll_pass2.setVisibility(View.VISIBLE);
			// 设置背景
		} else {
			Spanned text = Html.fromHtml(AppTools.changeStringColor("#e3393c",
					str));
			this.tv_show_passway.setText(text);// 过关
			ll_pass2.setVisibility(View.GONE);
			tv_show_passway2.setVisibility(View.GONE);
		}
		setCountText();
	}

	/**
	 * 设置显示 *
	 */
	public void setCountText() {
		if (et_bei.getText().toString().trim().length() == 0)
			AppTools.bei = 1;
		else
			AppTools.bei = Integer.parseInt(et_bei.getText().toString().trim());

		tv_count.setText(this.getTotalCount() + "");
		tv_money.setText(this.getTotalCount() * AppTools.bei * 2 + ""); // 倍数修改
		if (ways == 1) {
			getJiangjin();
		}
		coupon_text.setText("优惠劵（请先确认投注倍数）");
		voucherID = "";
		et_bei.setEnabled(true);
//		rlBonus.setEnabled(true);
		tv_show_passway.setEnabled(true);
		layout_jc.setEnabled(true);
		bet_btn_deleteall.setEnabled(true);
	}

	/**
	 * 格式化投注信息 *
	 */
	private void setList() {
		listResult.clear();
		listResult_dan.clear();
		/** 没有格式化的投注内容 **/
		String betNum = "";
		/** 格式化后的投注内容 **/
		String betNum2 = "";

		for (int j = 0; j < listStr.size(); j++) {
			String str = listStr.get(j);

			/** 得到 HashMap 的键 **/
			Integer s = Integer.parseInt(str.split("-")[0]);
			Integer s2 = Integer.parseInt(str.split("-")[1]);
			// 拿到投注内容
			betNum = select_hashMap.get(s).get(s2);
			if (betNum.length() == 0)
				continue;
			if (betNum.contains(",")) {
				betNum2 = betNum;
			} else {
				betNum2 = "";
				if (type2 != 7306) {
					if (type2 == 7303) {// 胜分差
						if (betNum.length() > 1) {
							betNum2 = betNum;
						} else {
							for (int i = 0; i < betNum.length() - 1; i++) {
								String g = betNum.substring(i, i + 1);
								betNum2 += g + ",";
							}
							if (betNum.length() != 0)
								betNum2 += betNum
										.substring(betNum.length() - 1);
						}
					} else {
						for (int i = 0; i < betNum.length() - 1; i++) {
							String g = betNum.substring(i, i + 1);
							betNum2 += g + ",";
						}
						if (betNum.length() != 0)
							betNum2 += betNum.substring(betNum.length() - 1);
					}

				} else {
					betNum2 = betNum;
				}
			}

			boolean isDan = false;
			for (String index : betAdapter.list_dan) {
				if (Integer.parseInt(index) == j) {
					listResult_dan.add(betNum2);
					isDan = true;
				}
			}
			if (!isDan)
				listResult.add(betNum2);
		}
	}

	/**
	 * 封装投注格式
	 */
	private void setApp() {
		setList();
		if (listResult.size() == 0)
			return;
		/** 装所有对阵信息的集合 **/
		List<DtMatch_Basketball> list = betAdapter.getListDtMatch();
		/** 拼凑字符串 **/
		String st = type2 + ";[";
		for (int i = 0; i < list.size(); i++) {
			System.out.println("list===" + list.get(i).getMatchId());
		}
		for (int i = 0; i < betAdapter.list_dan.size(); i++) {
			if (type2 != 7306) {
				st += list.get(Integer.parseInt(betAdapter.list_dan.get(i)))
						.getMatchId() + "(" + listResult_dan.get(i) + ")";
			} else {
				String[] array = listResult_dan.get(i).split(",");
				for (int k = 0; k < array.length; k++) {
				}
				for (int k = 0; k < array.length; k++) {
					if (Integer.parseInt(array[k]) < 15) {
						array[k] = chang_sfc_hhtz(array[k]);
					}
				}
				for (int k = 0; k < array.length; k++) {
				}
				String aaa = "";
				for (int k2 = 0; k2 < array.length; k2++) {
					if (k2 == 0)
						aaa = array[k2];
					else
						aaa += "," + array[k2];
				}
				st += list.get(Integer.parseInt(betAdapter.list_dan.get(i)))
						.getMatchId() + "(" + aaa + ")";
			}
			if (listResult_dan.size() - 1 == i) {
				st += "]";
			} else
				st += "|";
			if (betAdapter.list_dan.size() - i == 1)
				st += "[";
		}
		List<DtMatch_Basketball> lista = new ArrayList<DtMatch_Basketball>();
		for (int i = 0; i < betAdapter.list_dan.size(); i++) {
			DtMatch_Basketball mm = list.get(Integer
					.parseInt(betAdapter.list_dan.get(i)));
			lista.add(mm);
		}
		for (int i = 0; i < lista.size(); i++) {
			list.remove(lista.get(i));
		}
		for (int i = 0; i < list.size(); i++) {
			if (type2 != 7306)
				st += list.get(i).getMatchId() + "(" + listResult.get(i) + ")";
			else {
				String[] array = listResult.get(i).split(",");
				for (int k = 0; k < array.length; k++) {
				}
				for (int k = 0; k < array.length; k++) {
					if (Integer.parseInt(array[k]) < 15) {
						array[k] = chang_sfc_hhtz(array[k]);
					}
				}
				for (int k = 0; k < array.length; k++) {
				}
				String aaa = "";
				for (int k2 = 0; k2 < array.length; k2++) {
					if (k2 == 0)
						aaa = array[k2];
					else
						aaa += "," + array[k2];
				}
				st += list.get(i).getMatchId() + "(" + aaa + ")";
			}
			if (list.size() - 1 == i) {
				st += "]";
			} else
				st += "|";
		}
		// 拿到过关方式类型 passType = [AA, AB]
		String str_type[] = passType.split(",");
		// 拼凑 过关方式和倍数
		System.out.println("STR_TYPE_SIZE()====" + str_type.length);
		String a = "";
		for (int k = 0; k < str_type.length; k++) {
			if (k == str_type.length - 1) {
				a += str_type[k] + AppTools.bei;
			} else
				a += str_type[k] + AppTools.bei + ",";
		}
		st += ";[" + a + "]";

		if (listResult_dan.size() != 0)
			st += ";[" + listResult_dan.size() + "]";

		AppTools.ball = st;
		AppTools.lottery.setType(type2);
	}

	/**
	 * 转换投注结果格式
	 * 
	 * @param sfc_result
	 *            ：投注结果
	 * @return
	 */
	private String chang_sfc_hhtz(String sfc_result) {
		switch (Integer.parseInt(sfc_result)) {
		case 1:
			return "400";
		case 2:
			return "406";
		case 3:
			return "401";
		case 4:
			return "407";
		case 5:
			return "402";
		case 6:
			return "408";
		case 7:
			return "403";
		case 8:
			return "409";
		case 9:
			return "404";
		case 10:
			return "410";
		case 11:
			return "405";
		case 12:
			return "411";
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if (ways == 0) {
//			selectType();
//		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		this.setListStr();
		betAdapter.setListDtmatch(listStr);
		betAdapter.notifyDataSetChanged();
		super.onNewIntent(intent);
	}

	/**
	 * 拿到过关方式的显示值
	 */
	private String setText(String Type) {
		String showType = "";
		String passType = Type.replace("[", "").replace("]", "")
				.replace(" ", "");

		if (passType.length() == 0) {
			return showType;
		}
		if (passType.contains("A0"))
			showType += ",单关";
		if (passType.contains("AA"))
			showType += ",2串1";
		if (passType.contains("AB"))
			showType += ",3串1";
		if (passType.contains("AC"))
			showType += ",3串3";
		if (passType.contains("AD"))
			showType += ",3串4";
		if (passType.contains("AE"))
			showType += ",4串1";
		if (passType.contains("AF"))
			showType += ",4串4";
		if (passType.contains("AG"))
			showType += ",4串5";
		if (passType.contains("AH"))
			showType += ",4串6";
		if (passType.contains("AI"))
			showType += ",4串11";
		if (passType.contains("AJ"))
			showType += ",5串1";
		if (passType.contains("AK"))
			showType += ",5串5";
		if (passType.contains("AL"))
			showType += ",5串6";
		if (passType.contains("AM"))
			showType += ",5串10";
		if (passType.contains("AN"))
			showType += ",5串16";
		if (passType.contains("AO"))
			showType += ",5串20";
		if (passType.contains("AP"))
			showType += ",5串26";
		if (passType.contains("AQ"))
			showType += ",6串1";
		if (passType.contains("AR"))
			showType += ",6串6";
		if (passType.contains("AS"))
			showType += ",6串7";
		if (passType.contains("AT"))
			showType += ",6串15";
		if (passType.contains("AU"))
			showType += ",6串20";
		if (passType.contains("AV"))
			showType += ",6串22";
		if (passType.contains("AW"))
			showType += ",6串35";
		if (passType.contains("AX"))
			showType += ",6串42";
		if (passType.contains("AY"))
			showType += ",6串50";
		if (passType.contains("AZ"))
			showType += ",6串57";
		if (passType.contains("BA"))
			showType += ",7串1";
		if (passType.contains("BB"))
			showType += ",7串7";
		if (passType.contains("BC"))
			showType += ",7串8";
		if (passType.contains("BD"))
			showType += ",7串21";
		if (passType.contains("BE"))
			showType += ",7串35";
		if (passType.contains("BF"))
			showType += ",7串120";
		if (passType.contains("BG"))
			showType += ",8串1";
		if (passType.contains("BH"))
			showType += ",8串8";
		if (passType.contains("BI"))
			showType += ",8串9";
		if (passType.contains("BJ"))
			showType += ",8串28";
		if (passType.contains("BK"))
			showType += ",8串56";
		if (passType.contains("BL"))
			showType += ",8串70";
		if (passType.contains("BM"))
			showType += ",8串247";

		return showType.substring(1);
	}

	/**
	 * 拆分数据得到赔率数据
	 */
	private void getBonusOptimization() {
		String peilv = "";// 一种结果的赔率
		String showresult = "";
		String ret = "";
		if (null == list_JC_Details)
			list_JC_Details = new ArrayList<JC_Details>();
		else
			list_JC_Details.clear();
		int b = betAdapter.listDtmatch.size();
		for (int j = 0; j < b; j++) {
			String[] aa = AppTools.ball.split(";")[1].replace("|", "&").split(
					"&");
			for (int k = 0; k < aa.length; k++) {
				String bb = aa[k].replace("[", "").replace("]", "")
						.replace(" ", "");
				if (bb.substring(0, bb.lastIndexOf("(")).equals(
						betAdapter.listDtmatch.get(j).getMatchId())) {
					String[] jieguo = bb
							.substring(bb.lastIndexOf("("), bb.lastIndexOf(")"))
							.replace("(", "").replace(")", "").split(",");
					for (int k2 = 0; k2 < jieguo.length; k2++) {
						String saiguo = jieguo[k2];
						// 7201 让球胜负 胜1平2负3
						// 7207 胜负 胜1平2负3
						// 7203 总进球 0球1 1球2；
						// 7201 让球胜负 胜1平2负3
						if (AppTools.ball.split(";")[0].equals("7301")
								|| AppTools.ball.split(";")[0].equals("7306")) {
							switch (Integer.parseInt(saiguo)) {
							case 2:
							case 101:
								peilv = betAdapter.listDtmatch.get(j)
										.getMainWin();
								showresult = "胜";
								break;
							case 1:
							case 100:
								peilv = betAdapter.listDtmatch.get(j)
										.getMainLose();
								showresult = "负";
								break;
							}
						}
						if (AppTools.ball.split(";")[0].equals("7302")
								|| AppTools.ball.split(";")[0].equals("7306")) {
							switch (Integer.parseInt(saiguo)) {
							case 2:
							case 201:
								peilv = betAdapter.listDtmatch.get(j)
										.getLetMainWin();
								showresult = "让分胜";
								break;
							case 1:
							case 200:

								peilv = betAdapter.listDtmatch.get(j)
										.getLetMainLose();
								showresult = "让分负";
								break;
							}
						}
						if (AppTools.ball.split(";")[0].equals("7304")
								|| AppTools.ball.split(";")[0].equals("7306")) {
							switch (Integer.parseInt(saiguo)) {
							case 1:
							case 300:
								peilv = betAdapter.listDtmatch.get(j).getBig();
								showresult = "大分";
								break;
							case 2:
							case 301:
								peilv = betAdapter.listDtmatch.get(j)
										.getSmall();
								showresult = "小分";
								break;
							}
						}
						if (AppTools.ball.split(";")[0].equals("7303")
								|| AppTools.ball.split(";")[0].equals("7306")) {
							switch (Integer.parseInt(saiguo)) {
							case 2:
							case 406:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferMain1_5();
								showresult = "主胜1-5";
								break;
							case 1:
							case 400:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferGuest1_5();
								showresult = "主负1-5";
								break;
							case 4:
							case 407:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferMain6_10();
								showresult = "主胜6-10";
								break;
							case 3:
							case 401://
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferGuest6_10();
								showresult = "主负6-10";
								break;
							case 6:
							case 408:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferMain11_15();
								showresult = "主胜11-15";
								break;
							case 5:
							case 402:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferGuest11_15();
								showresult = "主负11-15";
								break;
							case 8:
							case 409:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferMain16_20();
								showresult = "主胜16-20";
								break;
							case 7:
							case 403:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferGuest16_20();
								showresult = "主负16-20";
								break;
							case 10:
							case 410:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferMain21_25();
								showresult = "主胜21-25";
								break;
							case 9:
							case 404:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferGuest21_25();
								showresult = "主负21-25";
								break;
							case 12:
							case 411:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferMain26();
								showresult = "主胜26+";
								break;
							case 11:
							case 405:
								peilv = betAdapter.listDtmatch.get(j)
										.getDifferGuest26();
								showresult = "主负26+";
								break;
							}
						}

						JC_Details jc_details = new JC_Details();
						jc_details
								.setPlaytype_info(AppTools.ball.split(";")[0]);
						jc_details.setMatchNumber(betAdapter.listDtmatch.get(j)
								.getMatchNumber());
						jc_details.setResult(saiguo);
						jc_details.setMatchID(betAdapter.listDtmatch.get(j)
								.getMatchId());
						jc_details.setMainTeam(betAdapter.listDtmatch.get(j)
								.getMainTeam());
						jc_details.setGuestTeam(betAdapter.listDtmatch.get(j)
								.getGuestTeam());
						jc_details.setShowresult(showresult);
						jc_details
								.setPeilv(Double.parseDouble(peilv.equals("") ? "0"
										: peilv));
						list_JC_Details.add(jc_details);
					}
				}
			}
		}
	}

	// 单关
	private void add_dg() {
		for (int j = 0; j < list_JC_Details.size(); j++) {
			Show_JC_Details show_details = new Show_JC_Details();
			show_details.setPass("单关");
			show_details.setList_result(addlist(new int[] { j }));
			show_details.setList_JC_Details(add_jc_de(new int[] { j }));
			show_details.setSum_peilv(getpeilv(new int[] { j }));
			list_Show.add(show_details);
		}
	}

	private List<JC_Details> add_jc_de(int[] array) {
		List<JC_Details> list = new ArrayList<JC_Details>();
		for (int i = 0; i < array.length; i++) {
			list.add(list_JC_Details.get(array[i]));
		}
		return list;
	}

	private void add2x1() {
		for (int j = 0; j < list_JC_Details.size() - 1; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size(); j2++) {
				if (!getboolean(new int[] { j, j2 })) {
					continue;
				}
				System.out.println("===");
				Show_JC_Details show_details = new Show_JC_Details();
				show_details.setPass("2串1");
				show_details.setList_result(addlist(new int[] { j, j2 }));
				show_details.setSum_peilv(getpeilv(new int[] { j, j2 }));
				show_details.setList_JC_Details(add_jc_de(new int[] { j, j2 }));
				list_Show.add(show_details);
			}

		}
	}

	private void add3x1() {
		for (int j = 0; j < list_JC_Details.size() - 2; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size() - 1; j2++) {
				for (int j3 = j2 + 1; j3 < list_JC_Details.size(); j3++) {
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

	private void add4x1() {
		for (int j = 0; j < list_JC_Details.size() - 3; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size() - 2; j2++) {
				for (int j3 = j2 + 1; j3 < list_JC_Details.size() - 1; j3++) {
					for (int j4 = j3 + 1; j4 < list_JC_Details.size(); j4++) {
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

	private void add5x1() {
		int l = 0;
		for (int j = 0; j < list_JC_Details.size() - 4; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size() - 3; j2++) {
				for (int j3 = j2 + 1; j3 < list_JC_Details.size() - 2; j3++) {
					for (int j4 = j3 + 1; j4 < list_JC_Details.size() - 1; j4++) {
						for (int j5 = j4 + 1; j5 < list_JC_Details.size(); j5++) {
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

	private void add6x1() {
		for (int j = 0; j < list_JC_Details.size() - 5; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size() - 4; j2++) {
				for (int j3 = j2 + 1; j3 < list_JC_Details.size() - 3; j3++) {
					for (int j4 = j3 + 1; j4 < list_JC_Details.size() - 2; j4++) {
						for (int j5 = j4 + 1; j5 < list_JC_Details.size() - 1; j5++) {
							for (int j6 = j5 + 1; j6 < list_JC_Details.size(); j6++) {
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

	private void add7x1() {
		for (int j = 0; j < list_JC_Details.size() - 6; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size() - 5; j2++) {
				for (int j3 = j2 + 1; j3 < list_JC_Details.size() - 4; j3++) {
					for (int j4 = j3 + 1; j4 < list_JC_Details.size() - 3; j4++) {
						for (int j5 = j4 + 1; j5 < list_JC_Details.size() - 2; j5++) {
							for (int j6 = j5 + 1; j6 < list_JC_Details.size() - 1; j6++) {
								for (int j7 = j6 + 1; j7 < list_JC_Details
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

	private void add8x1() {
		for (int j = 0; j < list_JC_Details.size() - 7; j++) {
			for (int j2 = j + 1; j2 < list_JC_Details.size() - 6; j2++) {
				for (int j3 = j2 + 1; j3 < list_JC_Details.size() - 5; j3++) {
					for (int j4 = j3 + 1; j4 < list_JC_Details.size() - 4; j4++) {
						for (int j5 = j4 + 1; j5 < list_JC_Details.size() - 3; j5++) {
							for (int j6 = j5 + 1; j6 < list_JC_Details.size() - 2; j6++) {
								for (int j7 = j6 + 1; j7 < list_JC_Details
										.size() - 1; j7++) {
									for (int j8 = j7 + 1; j8 < list_JC_Details
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
	 * 判断是否继续
	 * 
	 * @param pass
	 * @return
	 */
	public boolean getboolean(int[] pass) {
		boolean flag = true;
		for (int i = 0; i < pass.length - 1; i++) {
			for (int j = i + 1; j < pass.length; j++) {
				if (list_JC_Details.get(pass[i]).getMatchID()
						.equals(list_JC_Details.get(pass[j]).getMatchID())) {
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * 计算赔率
	 * 
	 * @param index
	 *            ：下标集合
	 * @return
	 */
	private double getpeilv(int[] index) {
		double pei = 1;
		for (int i = 0; i < index.length; i++) {
			pei *= Double.parseDouble(list_JC_Details.get(index[i]).getPeilv());
		}
		return pei;
	}

	/**
	 * 添加对阵数据
	 * 
	 * @param index
	 * @return
	 */
	private List<String> addlist(int[] index) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < index.length; i++) {
			list.add(list_JC_Details.get(index[i]).getMatchNumber() + ","
					+ list_JC_Details.get(index[i]).getMainTeam() + ","
					+ list_JC_Details.get(index[i]).getGuestTeam() + ","
					+ list_JC_Details.get(index[i]).getShowresult() + ","
					+ list_JC_Details.get(index[i]).getPeilv() + ","
					+ list_JC_Details.get(index[i]).getMatchID());
		}
		return list;
	}

	/**
	 * 根据过关参数整理数据
	 */
	private void setDate() {
		setApp();
		if (AppTools.ball != null && "" != AppTools.ball) {
			list_Show.clear();
			if (AppTools.ball.split(";")[2].contains("A0")) {
				add_dg();
			}
			if (AppTools.ball.split(";")[2].contains("AA")) {
				add2x1();
			}
			if (AppTools.ball.split(";")[2].contains("AB")) {
				add3x1();
			}
			if (AppTools.ball.split(";")[2].contains("AE")) {
				add4x1();
			}
			if (AppTools.ball.split(";")[2].contains("AJ")) {
				add5x1();
			}
			if (AppTools.ball.split(";")[2].contains("AQ")) {
				add6x1();
			}
			if (AppTools.ball.split(";")[2].contains("BA")) {
				add7x1();
			}
			if (AppTools.ball.split(";")[2].contains("BG")) {
				add8x1();
			}
		}
	}

	/**
	 * 得到每场比赛的最大赔率
	 */
	private void get_max_peilv_list() {
		max_peilv.clear();
		if (type2 == 7306) {
			for (int i = 0; i < list_JC_Details.size(); i++) {
				if (max_peilv.containsKey(list_JC_Details.get(i).getMatchID())) {
					if (Double.parseDouble(list_JC_Details.get(i).getPeilv()) > Double
							.parseDouble(max_peilv.get(list_JC_Details.get(i)
									.getMatchID()))) {
						max_peilv.put(list_JC_Details.get(i).getMatchID(),
								list_JC_Details.get(i).getPeilv());
					} else
						continue;
				} else
					max_peilv.put(list_JC_Details.get(i).getMatchID(),
							list_JC_Details.get(i).getPeilv());
			}
		} else {
			for (int i = 0; i < list_JC_Details.size(); i++) {
				if (max_peilv.containsKey(list_JC_Details.get(i).getMatchID())) {
					if (Double.parseDouble(list_JC_Details.get(i).getPeilv()) > Double
							.parseDouble(max_peilv.get(list_JC_Details.get(i)
									.getMatchID()))) {
						max_peilv.put(list_JC_Details.get(i).getMatchID(),
								list_JC_Details.get(i).getPeilv());
					} else
						continue;
				} else
					max_peilv.put(list_JC_Details.get(i).getMatchID(),
							list_JC_Details.get(i).getPeilv());
			}
		}

		if (list_max_jiangjin.size() > 0) {
			list_max_jiangjin.clear();
		}
		Iterator iter = max_peilv.entrySet().iterator();
		while (iter.hasNext()) {
			LinkedHashMap.Entry entry = (LinkedHashMap.Entry) iter.next();
			String val = (String) entry.getValue();
			System.out.println("===val==" + val);
			list_max_jiangjin.add(Double.parseDouble(val));
		}
	}

	/**
	 * 得到每场比赛的最新赔率
	 */
	private void get_min_peilv_list() {
		min_peilv.clear();
		if (type2 == 7306) {
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
					min_peilv.put(list_JC_Details.get(i).getMatchID(),
							list_JC_Details.get(i).getPeilv());
			}
		} else {
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
					min_peilv.put(list_JC_Details.get(i).getMatchID(),
							list_JC_Details.get(i).getPeilv());
			}
		}
		if (list_min_jiangjin.size() > 0) {
			list_min_jiangjin.clear();
		}
		Iterator iter = min_peilv.entrySet().iterator();
		while (iter.hasNext()) {
			LinkedHashMap.Entry entry = (LinkedHashMap.Entry) iter.next();
			String val = (String) entry.getValue();
			list_min_jiangjin.add(Double.parseDouble(val));
		}
	}

	/**
	 * 得到奖金优化的最大赔率集合
	 */
	private void get_max_peilv_list_youhua() {
		max_peilv_youhua.clear();
		if (type2 == 7306) {
			for (int i = 0; i < list_JC_Details.size(); i++) {
				if (max_peilv_youhua.containsKey(list_JC_Details.get(i)
						.getMatchID())) {
					if (Double.parseDouble(list_JC_Details.get(i).getPeilv()) > Double
							.parseDouble(max_peilv_youhua.get(list_JC_Details
									.get(i).getMatchID()))) {
						max_peilv_youhua.put(list_JC_Details.get(i)
								.getMatchID(), list_JC_Details.get(i)
								.getPeilv());
					} else
						continue;
				} else
					max_peilv_youhua.put(list_JC_Details.get(i).getMatchID()
							+ list_JC_Details.get(i).getResult()
									.substring(0, 1), list_JC_Details.get(i)
							.getPeilv());
			}
		} else {
			for (int i = 0; i < list_JC_Details.size(); i++) {
				if (max_peilv_youhua.containsKey(list_JC_Details.get(i)
						.getMatchID())) {
					if (Double.parseDouble(list_JC_Details.get(i).getPeilv()) > Double
							.parseDouble(max_peilv_youhua.get(list_JC_Details
									.get(i).getMatchID()))) {
						max_peilv_youhua.put(list_JC_Details.get(i)
								.getMatchID(), list_JC_Details.get(i)
								.getPeilv());
					} else
						continue;
				} else
					max_peilv_youhua.put(list_JC_Details.get(i).getMatchID(),
							list_JC_Details.get(i).getPeilv());
			}
		}
	}
}
