package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Jiang_k3_Info;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.adapter.MyGridViewAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.JiangjinTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.PopupWindowUtil_k3;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.CustomDigitalClock;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.SmanagerView;
import com.gcapp.tc.view.VibratorView;
import com.gcapp.tc.R;

/**
 * 功能：江苏快3 选号界面，实现选号
 * 
 * @author lenovo
 * 
 */
public class Select_K3_Activity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "Select_k3Activity";
	private Context context = Select_K3_Activity.this;
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype, ll_winnum;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private ImageView iv_up_down2;// 显示近10期开奖号码
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框
	private Animation animation = null;
	private LinearLayout ll_daxiao;// 大小单双的布局
	private TextView tv_da, tv_xiao, tv_single, tv_shuang;
	private TextView tv_img1, tv_title1, tv_img2, tv_title2;
	private View view;
	private PopupWindow popWindow;
	private Button btn_playHelp, btn_winNumber, btn_forgetNum;// 玩法说明，开奖详情，显示遗漏值
	private boolean visibleGone = true;
	/* 尾部 */
	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数
	private TextView tv_winnumber, tv_winnum1, tv_winnum2, tv_winnum3;// 显示开奖号码
	private LinearLayout layout_shake;// 摇一摇
	private ImageView iv_shake;// 摇一摇
	private LinearLayout layout_shake2;// 摇一摇
	private ImageView iv_shake2;// 摇一摇
	private static List<String> list_hezhi = new ArrayList<String>();
	private String win_number;// 中奖
	private String win_lastQi;
	private TextView tv_top; // 选号上面的提示
	private Bundle bundle;
	public Vibrator vibrator; // 震动器
	private SensorManager mSmanager; // 传感器
	private LinearLayout ll_hezhishow, ll_hezhishow2;
	private ArrayList<String> list_yilou1 = new ArrayList<String>();
	private ArrayList<String> list_yilou2 = new ArrayList<String>();
	private ArrayList<String> list_yilou3 = new ArrayList<String>();// 集成玩法的遗漏值

	/** 传感器 */
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// 这一次的时间
	private long vTime = 0; // 震动的时间

	private SharedPreferences settings;
	private Editor editor;
	private PopupWindowUtil_k3 popUtil;
	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
	private List<String> list_jiangjin = new ArrayList<String>();// 快三的玩法选号的adapter数据
	private List<String> list_image = new ArrayList<String>();
	private int parentIndex = 0;
	private int itemIndex = 0;
	private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> playtypeMap_dan = new HashMap<Integer, Integer>();
	private List<String> list1 = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();
	private List<String> list_daxiao = new ArrayList<String>();
	private List<String> list_daxiao2 = new ArrayList<String>();
	private List<Jiang_k3_Info> list_wininfo = new ArrayList<Jiang_k3_Info>();// 近10期开奖号码的adapter
	private int playID = 8301;
	// 选号布局适配器
	private Adpater adpater, adpater1, adpater2;
	private MyAdapter adpater_daxiao;
	private MywinNumAdapter adapter_win;// 近10期开奖详情适配器
	private MyGridView gridView, gridView_ertonghaodan_1,
			gridView_ertonghaodan_2, gridView_daxiao;
	private TextView tv_title, tv_dan, tv_tuo;// 界面两个gridView 胆拖和二同单选
	private TextView tv_jiezhitime;
	private TextView tv_daxiao;
	// 1 为和值 2 为二同和胆拖 3 为快速选号
	private RelativeLayout relativeLayout1, relativeLayout2;
	private int index = 1; // 倒计时标签
	private CustomDigitalClock custTime;// 显示截止时间
	private Spanned tip = null;
	private String opt, auth, info, time, imei; // 格式化后的参数
	private ListView k3_win_listView;// 显示快3的最近10期 的开奖详情
	private LinearLayout k3_layout_tile;// 显示快3的最近10期 的开奖详情
	private LinearLayout img_line;
	private List<String> daxiaodanshuang = new ArrayList<String>();// 存放大小单双的list
	// 奖金估测
	private LinearLayout ll_jiangjin;// 奖金预测
	private TextView tv_jingjin, tv_profits, tv_winOrLost;
	private long max_jiangjin, min_jiangjin;// 最大与最小奖金
	private long max_profits, min_profits;// 最大与最小利润

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_k3);
		App.activityS.add(this);
		App.activityS1.add(this);
		getYilouData();
		findView();
		init();
	}

	/**
	 * 设置快三的倒计时功能
	 */
	private void getCustomTime() {
		if (AppTools.lottery.getEndtime() != null
				&& AppTools.lottery.getDistanceTime()
						- System.currentTimeMillis() > 0) {
			custTime.setMTickStop(false);
			custTime.setType(3);
			custTime.setEndTime(AppTools.lottery.getDistanceTime());
		} else {
			if (AppTools.lottery.getDistanceTime2()
					- System.currentTimeMillis() > 0) {
				custTime.setMTickStop(false);
				custTime.setType(4);
				custTime.setEndTime(AppTools.lottery.getDistanceTime2());
			}
		}
		if (custTime.getType() == 4) {
			tv_jiezhitime.setText("距离下一期开始: ");
		} else if (custTime.getType() == 3) {
			tv_jiezhitime.setText("距" + AppTools.lottery.getIsuseName().trim()
					+ "期截止:");
		}
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		MyGridViewAdapter.playType = 501;
		bundle = new Bundle();
		img_line = (LinearLayout) findViewById(R.id.img_line);// 线条布局
		ll_jiangjin = (LinearLayout) findViewById(R.id.ll_jiangjin);// 奖金预测
		ll_jiangjin.setVisibility(View.GONE);
		tv_jingjin = (TextView) findViewById(R.id.tv_jingjin);// 奖金
		tv_profits = (TextView) findViewById(R.id.tv_profits);// 盈利
		tv_winOrLost = (TextView) findViewById(R.id.tv_winOrLost);
		ll_daxiao = (LinearLayout) findViewById(R.id.ll_daxiao);// 大小单双布局
		ll_daxiao.setVisibility(View.VISIBLE);// 大小单双布局
		tv_da = (TextView) findViewById(R.id.tv_da);
		tv_xiao = (TextView) findViewById(R.id.tv_xiao);
		tv_single = (TextView) findViewById(R.id.tv_dan);
		tv_shuang = (TextView) findViewById(R.id.tv_shuang);
		ll_hezhishow = (LinearLayout) findViewById(R.id.ll_hezhishow);
		ll_hezhishow2 = (LinearLayout) findViewById(R.id.ll_hezhishow2);
		tv_img1 = (TextView) findViewById(R.id.tv_img1);
		tv_title1 = (TextView) findViewById(R.id.tv_title1);
		tv_img2 = (TextView) findViewById(R.id.tv_img2);
		tv_title2 = (TextView) findViewById(R.id.tv_title2);
		tv_da.setTextColor(getResources().getColor(R.color.black_light));
		tv_xiao.setTextColor(getResources().getColor(R.color.black_light));
		tv_single.setTextColor(getResources().getColor(R.color.black_light));
		tv_shuang.setTextColor(getResources().getColor(R.color.black_light));

		tv_da.setOnClickListener(new hezhi_daxiaoClickListener());
		tv_xiao.setOnClickListener(new hezhi_daxiaoClickListener());
		tv_single.setOnClickListener(new hezhi_daxiaoClickListener());
		tv_shuang.setOnClickListener(new hezhi_daxiaoClickListener());
		ll_winnum = (LinearLayout) findViewById(R.id.ll_winnum);// 开奖号码布局
		custTime = (CustomDigitalClock) this
				.findViewById(R.id.select_ks_endtime);
		tv_jiezhitime = (TextView) this.findViewById(R.id.tv_jiezhitime);
		getCustomTime();
		k3_win_listView = (ListView) findViewById(R.id.k3_win_listView);// 近10期的开奖号码
		k3_layout_tile = (LinearLayout) findViewById(R.id.k3_layout_tile);// 近10期的开奖号码
		adapter_win = new MywinNumAdapter(list_wininfo, Select_K3_Activity.this);
		k3_win_listView.setAdapter(adapter_win);
		tv_daxiao = (TextView) findViewById(R.id.tv_daxiao);// 第二排布局的文字提示
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		iv_up_down2 = (ImageView) findViewById(R.id.iv_up_down2);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
		iv_shake = (ImageView) findViewById(R.id.iv_shake);
		layout_shake2 = (LinearLayout) findViewById(R.id.layout_shake2);
		iv_shake2 = (ImageView) findViewById(R.id.iv_shake2);
		tv_winnumber = (TextView) findViewById(R.id.tv_winnumber);
		tv_winnum1 = (TextView) findViewById(R.id.tv_winnum1);
		tv_winnum2 = (TextView) findViewById(R.id.tv_winnum2);
		tv_winnum3 = (TextView) findViewById(R.id.tv_winnum3);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		tv_top = (TextView) this.findViewById(R.id.textViewtop);

		// 胆拖
		tv_title = (TextView) this.findViewById(R.id.textView2);
		tv_dan = (TextView) this.findViewById(R.id.textView5);
		tv_tuo = (TextView) this.findViewById(R.id.textView6);
		// 注数
		relativeLayout1 = (RelativeLayout) this
				.findViewById(R.id.relativeLayout_hezhi);
		relativeLayout2 = (RelativeLayout) this
				.findViewById(R.id.relativeLayout_ertonghao);
		// 二同号单选和胆拖玩法
		gridView = (MyGridView) this.findViewById(R.id.gridView_hezhi);// 和值布局
		gridView_daxiao = (MyGridView) this.findViewById(R.id.gridView_daxiao);// 大小单双速选
		gridView_daxiao.setVisibility(View.VISIBLE);

		gridView.setNumColumns(4);
		gridView_daxiao.setNumColumns(4);

		adpater = new Adpater(list1, list2, new ArrayList<String>(),
				Select_K3_Activity.this, visibleGone, list_yilou1);
		gridView.setAdapter(adpater);

		adpater_daxiao = new MyAdapter(list_daxiao, list_daxiao2,
				Select_K3_Activity.this, visibleGone, list_yilou3);
		gridView_daxiao.setAdapter(adpater_daxiao);

		// 默认二同单选号
		adpater1 = new Adpater(list1, new ArrayList<String>(),
				new ArrayList<String>(), Select_K3_Activity.this, visibleGone,
				list_yilou1);
		adpater2 = new Adpater(list2, new ArrayList<String>(),
				new ArrayList<String>(), Select_K3_Activity.this, visibleGone,
				list_yilou2);

		gridView_ertonghaodan_1 = (MyGridView) this
				.findViewById(R.id.gridView_ertonghaodan_1);
		gridView_ertonghaodan_2 = (MyGridView) this
				.findViewById(R.id.gridView_ertonghaodan_2);
		gridView_ertonghaodan_1.setAdapter(adpater1);
		gridView_ertonghaodan_2.setAdapter(adpater2);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);

		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(Select_K3_Activity.this);
	}

	/**
	 * 初始化页面显示,得到上期开奖号码和绑定监听
	 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// 注册传感器
		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (NetWork.isConnect(Select_K3_Activity.this)) {
			if (AppTools.lottery != null) {
				if (AppTools.lottery.getLastWinNumber() != null) {
					win_number = AppTools.lottery.getLastWinNumber().trim();
				}
				if (AppTools.lottery.getLastIsuseName() != null) {
					win_lastQi = AppTools.lottery.getLastIsuseName().trim();
				}
			}
			if (null != win_number && null != win_lastQi
					&& !"".equals(win_number)) {
				String winNumber = "";
				if (!"".equals(win_number)) {
					winNumber = win_number.substring(0, 1) + " "
							+ win_number.substring(1, 2) + " "
							+ win_number.substring(2, 3);
				}
				tv_winnumber.setText(win_lastQi + "期开奖：  " + winNumber);

				if (win_number.substring(0, 1).equals("1")) {
					tv_winnum1.setBackgroundResource(R.drawable.dice1);
				} else if (win_number.substring(0, 1).equals("2")) {
					tv_winnum1.setBackgroundResource(R.drawable.dice2);
				} else if (win_number.substring(0, 1).equals("3")) {
					tv_winnum1.setBackgroundResource(R.drawable.dice3);
				} else if (win_number.substring(0, 1).equals("4")) {
					tv_winnum1.setBackgroundResource(R.drawable.dice4);
				} else if (win_number.substring(0, 1).equals("5")) {
					tv_winnum1.setBackgroundResource(R.drawable.dice5);
				} else if (win_number.substring(0, 1).equals("6")) {
					tv_winnum1.setBackgroundResource(R.drawable.dice6);
				}
				if (win_number.substring(1, 2).equals("1")) {
					tv_winnum2.setBackgroundResource(R.drawable.dice1);
				} else if (win_number.substring(1, 2).equals("2")) {
					tv_winnum2.setBackgroundResource(R.drawable.dice2);
				} else if (win_number.substring(1, 2).equals("3")) {
					tv_winnum2.setBackgroundResource(R.drawable.dice3);
				} else if (win_number.substring(1, 2).equals("4")) {
					tv_winnum2.setBackgroundResource(R.drawable.dice4);
				} else if (win_number.substring(1, 2).equals("5")) {
					tv_winnum2.setBackgroundResource(R.drawable.dice5);
				} else if (win_number.substring(1, 2).equals("6")) {
					tv_winnum2.setBackgroundResource(R.drawable.dice6);
				}

				if (win_number.substring(2, 3).equals("1")) {
					tv_winnum3.setBackgroundResource(R.drawable.dice1);
				} else if (win_number.substring(2, 3).equals("2")) {
					tv_winnum3.setBackgroundResource(R.drawable.dice2);
				} else if (win_number.substring(2, 3).equals("3")) {
					tv_winnum3.setBackgroundResource(R.drawable.dice3);
				} else if (win_number.substring(2, 3).equals("4")) {
					tv_winnum3.setBackgroundResource(R.drawable.dice4);
				} else if (win_number.substring(2, 3).equals("5")) {
					tv_winnum3.setBackgroundResource(R.drawable.dice5);
				} else if (win_number.substring(2, 3).equals("6")) {
					tv_winnum3.setBackgroundResource(R.drawable.dice6);
				}
			} else {
				tv_winnum1.setText("");
				tv_winnumber.setText("暂无开奖号码 ");
			}
		} else {
			Toast.makeText(Select_K3_Activity.this, "网络连接异常，获得数据失败！",
					Toast.LENGTH_SHORT);
		}
		// 得到数据
		setData();
		// 设置监听
		setList();
		// 给Adapter赋值
		btn_back.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		iv_up_down.setOnClickListener(this);
		iv_up_down2.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		btn_help.setOnClickListener(this);
		btn_clearall.setOnClickListener(this);
		ll_winnum.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		layout_shake.setOnClickListener(this);
		iv_shake.setOnClickListener(this);
		layout_shake2.setOnClickListener(this);
		iv_shake2.setOnClickListener(this);
		// 倒计时
		custTime.setClockListener(new CustomDigitalClock.ClockListener() {
			@Override
			public void timeEnd() {
				if (custTime.getType() == 3) {
					custTime.setMTickStop(false);
					custTime.setType(4);
					custTime.setEndTime(AppTools.lottery.getDistanceTime2());
				} else {
					RequestUtil requestUtil = new RequestUtil(
							Select_K3_Activity.this, false, 0) {
						@Override
						public void responseCallback(JSONObject reponseJson) {
							String result = AppTools.getDate(reponseJson);
							if ("0".equals(result)) {
								getCustomTime();
							} else if ("-1001".equals(result)) {
								if (RequestUtil.DEBUG) {
									Log.e(TAG, "获取购彩大厅数据出错");
								}
							}
						}

						@Override
						public void responseError(VolleyError error) {
						}
					};
					requestUtil.getLotteryData(AppTools.lottery.getLotteryID());
				}
			}

			@Override
			public void remainFiveMinutes() {
			}
		});
		settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
		editor = settings.edit();// 获取编辑对象
		list_jiangjin.add("奖金9-240元");
		list_jiangjin.add("奖金40-240");
		list_jiangjin.add("奖金15-80元");
		list_jiangjin.add("奖金10-40元");
		list_jiangjin.add("奖金8元");
		list_jiangjin.add("");

		list_image.add("1+2+3");
		list_image.add("111");
		list_image.add("113");
		list_image.add("235");
		list_image.add("35");
		list_image.add("");

		Map<Integer, String> playType = new HashMap<Integer, String>();// 普通玩法
		playType.put(0, "和值");
		playType.put(1, "三同号");
		playType.put(2, "二同号");
		playType.put(3, "三不同号");
		playType.put(4, "二不同号");
		playType.put(5, "");

		Set<Integer> set = playType.keySet();
		int[] playtype_array = { 8301, 8303, 8305, 8306, 8307 };
		int[] playtype_array_dan = { 8309, 8310 };
		for (Integer i : set) {
			if (i < 5) {
				playtypeMap.put(playtype_array[i], i);
			}
		}
		data.put(0, playType);
		dialog = new ConfirmDialog(this, R.style.dialog);
		tip = Html.fromHtml("猜开奖号码相加的和");
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		tv_top.setText(tip);
	}

	/**
	 * 提交遗漏值请求
	 */
	public void getYilouData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "遗漏值请求结果" + isusesInfo);
				try {
					JSONArray array = new JSONArray(
							isusesInfo.getString("miss"));
					JSONObject item = null;
					JSONObject item2 = null;
					if (array.length() > 1) {
						item = array.getJSONObject(0);
						item2 = array.getJSONObject(1);
					} else {
						item = array.getJSONObject(0);
					}

					if (list_yilou2.size() > 0 || list_yilou3.size() > 0
							| list_yilou1.size() > 0) {
						list_yilou2.clear();
						list_yilou1.clear();
						list_yilou3.clear();
					}
					String str3 = "";
					String str2 = "";
					String str1 = "";
					int playmark = playID % 100;
					if (playmark == 1) {
						str1 = item.optString("1");
					} else if (playmark == 3) {// 三同号+三同号通选
						str1 = item.optString("1");
						str3 = item2.optString("1");
					} else if (playmark == 5) { // 二同号
						str1 = item.optString("1");
						str2 = item.optString("2");
						str3 = item2.optString("1");

					} else if (playmark == 6) { // 三不同号+三连号通选
						str1 = item.optString("1");
						str3 = item2.optString("1");

					} else if (playmark == 7) { // 二不同号
						str1 = item.optString("1");
						str2 = item.optString("2");
					}
					if (null != str1 && !"".equals(str1))
						for (int i = 0; i < str1.split(",").length; i++) {
							list_yilou1.add(str1.split(",")[i]);
						}

					if (null != str2 && !"".equals(str2))
						for (int i = 0; i < str2.split(",").length; i++) {
							list_yilou2.add(str2.split(",")[i]);
						}

					if (null != str3 && !"".equals(str3))
						for (int i = 0; i < str3.split(",").length; i++) {
							list_yilou3.add(str3.split(",")[i]);
						}

					adpater = new Adpater(list1, list2,
							new ArrayList<String>(), Select_K3_Activity.this,
							visibleGone, list_yilou1);
					gridView.setAdapter(adpater);

					adpater_daxiao = new MyAdapter(list_daxiao, list_daxiao2,
							Select_K3_Activity.this, visibleGone, list_yilou3);
					gridView_daxiao.setAdapter(adpater_daxiao);
					// 默认二同单选号
					adpater1 = new Adpater(list1, new ArrayList<String>(),
							new ArrayList<String>(), Select_K3_Activity.this,
							visibleGone, list_yilou1);
					adpater2 = new Adpater(list2, new ArrayList<String>(),
							new ArrayList<String>(), Select_K3_Activity.this,
							visibleGone, list_yilou2);
					gridView_ertonghaodan_1.setAdapter(adpater1);
					gridView_ertonghaodan_2.setAdapter(adpater2);
					adpater1.notifyDataSetChanged();
					adpater2.notifyDataSetChanged();
					adpater.notifyDataSetChanged();
					adpater_daxiao.notifyDataSetChanged();

				} catch (Exception ex) {
					Log.i("login", "解析异常---" + ex.getMessage());
				}

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(Select_K3_Activity.this, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_K3_Activity.this, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.commit_yilou(playID);
	}

	/**
	 * 快三的近十期开奖号码详情适配器
	 */
	class MywinNumAdapter extends BaseAdapter {
		Context context;
		List<Jiang_k3_Info> list_windetail;

		public MywinNumAdapter(List<Jiang_k3_Info> list1, Context context) {
			this.context = context;
			this.list_windetail = list1;
		}

		@Override
		public int getCount() {
			return list_windetail.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_windetail.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parentview) {
			ViewHolder_Winnumber holder;
			// 判断View是否为空
			if (view == null) {
				holder = new ViewHolder_Winnumber();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				view = inflater.inflate(R.layout.item_k3_selec_winnumber, null);
				// 得到控件
				holder.qihao = (TextView) view.findViewById(R.id.tv_qi);// 期号
				holder.status = (TextView) view.findViewById(R.id.tv_status);// 状态
				holder.winnumber = (TextView) view
						.findViewById(R.id.tv_winnumber);// 开奖号码
				holder.image1 = (TextView) view.findViewById(R.id.tv_img1);// 开奖1
				holder.image2 = (TextView) view.findViewById(R.id.tv_img2);// 开奖2
				holder.image3 = (TextView) view.findViewById(R.id.tv_img3);// 开奖3
				view.setTag(holder);
			} else {
				holder = (ViewHolder_Winnumber) view.getTag();
			}
			Jiang_k3_Info info = list_windetail.get(position);
			holder.qihao.setText(info.getName() + "期");
			if (null != info.getNumberType().toString()
					&& info.getNumberType().toString().trim().length() == 3) {
				holder.status.setText(" " + info.getNumberType());
			} else {
				holder.status.setText(info.getNumberType());
			}

			String winnumber = info.getWinNumber().toString().trim();
			if (null != winnumber && !"".equals(winnumber)) {
				int one = Integer.parseInt(winnumber.substring(0, 1));
				int two = Integer.parseInt(winnumber.substring(1, 2));
				int three = Integer.parseInt(winnumber.substring(2, 3));
				int hezhi = one + two + three;
				holder.winnumber.setText(hezhi + "");
			} else {
				holder.winnumber.setText("");
			}

			if (winnumber.substring(0, 1).equals("1")) {
				holder.image1.setBackgroundResource(R.drawable.dice1);
			} else if (winnumber.substring(0, 1).equals("2")) {
				holder.image1.setBackgroundResource(R.drawable.dice2);
			} else if (winnumber.substring(0, 1).equals("3")) {
				holder.image1.setBackgroundResource(R.drawable.dice3);
			} else if (winnumber.substring(0, 1).equals("4")) {
				holder.image1.setBackgroundResource(R.drawable.dice4);
			} else if (winnumber.substring(0, 1).equals("5")) {
				holder.image1.setBackgroundResource(R.drawable.dice5);
			} else if (winnumber.substring(0, 1).equals("6")) {
				holder.image1.setBackgroundResource(R.drawable.dice6);
			}

			if (winnumber.substring(1, 2).equals("1")) {
				holder.image2.setBackgroundResource(R.drawable.dice1);
			} else if (winnumber.substring(1, 2).equals("2")) {
				holder.image2.setBackgroundResource(R.drawable.dice2);
			} else if (winnumber.substring(1, 2).equals("3")) {
				holder.image2.setBackgroundResource(R.drawable.dice3);
			} else if (winnumber.substring(1, 2).equals("4")) {
				holder.image2.setBackgroundResource(R.drawable.dice4);
			} else if (winnumber.substring(1, 2).equals("5")) {
				holder.image2.setBackgroundResource(R.drawable.dice5);
			} else if (winnumber.substring(1, 2).equals("6")) {
				holder.image2.setBackgroundResource(R.drawable.dice6);
			}

			if (winnumber.substring(2, 3).equals("1")) {
				holder.image3.setBackgroundResource(R.drawable.dice1);
			} else if (winnumber.substring(2, 3).equals("2")) {
				holder.image3.setBackgroundResource(R.drawable.dice2);
			} else if (winnumber.substring(2, 3).equals("3")) {
				holder.image3.setBackgroundResource(R.drawable.dice3);
			} else if (winnumber.substring(2, 3).equals("4")) {
				holder.image3.setBackgroundResource(R.drawable.dice4);
			} else if (winnumber.substring(2, 3).equals("5")) {
				holder.image3.setBackgroundResource(R.drawable.dice5);
			} else if (winnumber.substring(2, 3).equals("6")) {
				holder.image3.setBackgroundResource(R.drawable.dice6);
			}
			return view;
		}

		class ViewHolder_Winnumber {
			private TextView qihao, status, winnumber;// 期号，形态，和值
			private LinearLayout relativeLayout_poo;
			private TextView image1, image2, image3;
		}
	}

	/**
	 * 提交近十期开奖号码的请求
	 */
	public void getWinnumberData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "开奖号码请求结果： " + isusesInfo);
				try {
					JSONArray array = new JSONArray(
							isusesInfo.getString("OpenInfo"));
					if (list_wininfo.size() > 0) {
						list_wininfo.clear();
					}
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						if (null != item) {
							Jiang_k3_Info k3Info = new Jiang_k3_Info();
							k3Info.setName(item.optString("Name"));
							;
							k3Info.setWinNumber(item
									.optString("WinLotteryNumber"));
							k3Info.setNumberType(item.optString("NumberType"));
							list_wininfo.add(k3Info);
						}
					}
				} catch (Exception ex) {
					Log.i("login", "获取值异常---" + ex.getMessage());
				}
				adapter_win = new MywinNumAdapter(list_wininfo,
						Select_K3_Activity.this);
				k3_win_listView.setAdapter(adapter_win);
				adapter_win.notifyDataSetChanged();

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(Select_K3_Activity.this, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_K3_Activity.this, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.commit_winNumber("83");
	}

	/**
	 * 根据玩法设置页面显示
	 */
	private void setData() {
		list1.clear();
		list2.clear();
		list_daxiao.clear();
		list_daxiao2.clear();
		if (playID == 8301) {
			for (int i = 4; i < 18; i++) {
				list1.add(i + "");
			}
			// for (int i = 3; i < 19; i++) {
			// list1.add(i + "");
			// }
			// list2.add("奖金240元");
			list2.add("奖金80元");
			list2.add("奖金40元");
			list2.add("奖金25元");
			list2.add("奖金16元");
			list2.add("奖金12元");
			list2.add("奖金10元");
			list2.add("奖金9元");
			list2.add("奖金9元");
			list2.add("奖金10元");
			list2.add("奖金12元");
			list2.add("奖金16元");
			list2.add("奖金25元");
			list2.add("奖金40元");
			list2.add("奖金80元");
			// list2.add("奖金240元");

		} else if (playID == 8303) {
			for (int i = 1; i < 7; i++) {
				list1.add(i + "" + i + "" + i + "");
				list2.add("奖金240元");
			}
			list_daxiao.add("三同号通选");
			list_daxiao2.add("任意一个豹子开出即中40元");

		} else if (playID == 8305) {
			for (int i = 1; i < 7; i++) {
				list2.add(i + "");
				list1.add(i + "" + i);
				list_daxiao.add(i + "" + i + "*");
			}

		} else if (playID == 8307 || playID == 8306) {
			for (int i = 1; i < 7; i++) {
				list1.add(i + "");
			}
			list_daxiao.add("三连号通选");
		}

		// 胆拖玩法
		else if (playID == 8309) {
			for (int i = 1; i < 7; i++) {
				list2.add(i + "");
				list1.add(i + "");
			}
		} else if (playID == 8310) {
			for (int i = 1; i < 7; i++) {
				list2.add(i + "");
				list1.add(i + "");
			}
		}
	}

	/**
	 * 绑定页面选号监听
	 */
	private void setList() {
		gridView.setOnItemClickListener(new OnItemClickListenerHezhi());
		gridView_daxiao.setOnItemClickListener(new OnItemClickListenerDaxiao());
		gridView_ertonghaodan_1.setOnItemClickListener(new erbutong1());
		gridView_ertonghaodan_2.setOnItemClickListener(new erbutong2());
	}

	/**
	 * 选号Gridview的监听事件
	 * 
	 * @author lenovo
	 * 
	 */
	class OnItemClickListenerDaxiao implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if (null != vibrator)
				vibrator.vibrate(150);
			LinearLayout rl = (LinearLayout) view.findViewById(R.id.ll_pop_k3);
			TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text3);
			TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text4);
			String index = arg2 + 1 + "".trim();

			if (adpater_daxiao.onSet1.contains(index)) {
				adpater_daxiao.onSet1.remove(index);
				tv1.setTextColor(Color.RED);
				tv2.setTextColor(Color.BLACK);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);

			} else {
				if (playID == 8305) {// 二同号
					if (adpater_daxiao.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "最多只能选1个");
						return;
					}
				}
				adpater_daxiao.onSet1.add(index);
				tv1.setTextColor(Color.WHITE);
				tv2.setTextColor(Color.WHITE);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
			}
			setCount();
			getJiangjin();
		}
	}

	/**
	 * 选号Gridview的监听事件
	 * 
	 * @author lenovo
	 * 
	 */
	class OnItemClickListenerHezhi implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if (null != vibrator)
				vibrator.vibrate(150);
			LinearLayout rl = (LinearLayout) view
					.findViewById(R.id.relativeLayout_pop_k3);
			TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text1);
			TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text2);
			String index;
			if (playID == 8301) {
				index = arg2 + 4 + "";
			} else
				index = arg2 + 1 + "";
			if (adpater.onSet1.contains(index)) {
				adpater.onSet1.remove(index);
				tv1.setTextColor(Color.RED);
				tv2.setTextColor(Color.BLACK);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
			} else {
				if (playID == 8303) {// 三同号
					if (adpater.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "最多只能选1个");
						return;
					}
				}

				if (playID == 8306) {// 三不同号
					if (adpater.onSet1.size() >= 3) {
						MyToast.getToast(Select_K3_Activity.this, "最多只能选3个");
						return;
					}
				}

				if (playID == 8307) {// 二不同号
					if (adpater.onSet1.size() >= 2) {
						MyToast.getToast(Select_K3_Activity.this, "最多只能2个");
						return;
					}
				}

				adpater.onSet1.add(index);
				tv1.setTextColor(Color.WHITE);
				tv2.setTextColor(Color.WHITE);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
			}
			if (playID == 8301) {
				chageDxds();
			}
			setCount();
			getJiangjin();
		}
	}

	/**
	 * 改变和值玩法的大小单双颜色的状态
	 */
	public void chageDxds() {
		daxiaodanshuang.clear();
		tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		tv_single.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		tv_shuang.setBackgroundResource(R.drawable.bet_btn_dan_unselected);

		tv_da.setTextColor(getResources().getColor(R.color.black_light));
		tv_xiao.setTextColor(getResources().getColor(R.color.black_light));
		tv_single.setTextColor(getResources().getColor(R.color.black_light));
		tv_shuang.setTextColor(getResources().getColor(R.color.black_light));
		if (adpater.onSet1.size() == 7) {
			if (adpater.onSet1.contains("4") && adpater.onSet1.contains("5")
					&& adpater.onSet1.contains("6")
					&& adpater.onSet1.contains("7")
					&& adpater.onSet1.contains("8")
					&& adpater.onSet1.contains("9")
					&& adpater.onSet1.contains("10")) {
				tv_xiao.setTextColor(Color.WHITE);
				tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("小");
			} else if (adpater.onSet1.contains("13")
					&& adpater.onSet1.contains("14")
					&& adpater.onSet1.contains("15")
					&& adpater.onSet1.contains("16")
					&& adpater.onSet1.contains("17")
					// && adpater.onSet1.contains("18")
					&& adpater.onSet1.contains("12")
					&& adpater.onSet1.contains("11")) {
				tv_da.setTextColor(Color.WHITE);
				tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("大");
			} else if (adpater.onSet1.contains("5")
					&& adpater.onSet1.contains("7")
					&& adpater.onSet1.contains("9")
					&& adpater.onSet1.contains("11")
					&& adpater.onSet1.contains("13")
					&& adpater.onSet1.contains("15")
					&& adpater.onSet1.contains("17")) {
				tv_single.setTextColor(Color.WHITE);
				tv_single
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("单");
			} else if (adpater.onSet1.contains("4")
					&& adpater.onSet1.contains("6")
					&& adpater.onSet1.contains("8")
					&& adpater.onSet1.contains("10")
					&& adpater.onSet1.contains("12")
					&& adpater.onSet1.contains("14")
					&& adpater.onSet1.contains("16")
			// && adpater.onSet1.contains("18")
			) {
				tv_shuang.setTextColor(Color.WHITE);
				tv_shuang
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("双");
			}
		} else if (adpater.onSet1.size() == 4) {
			if (adpater.onSet1.contains("11") && adpater.onSet1.contains("13")
					&& adpater.onSet1.contains("15")
					&& adpater.onSet1.contains("17")) {
				tv_da.setTextColor(Color.WHITE);
				tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_single.setTextColor(Color.WHITE);
				tv_single
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("大");
				daxiaodanshuang.add("单");
			} else if (adpater.onSet1.contains("4")
					&& adpater.onSet1.contains("6")
					&& adpater.onSet1.contains("8")
					&& adpater.onSet1.contains("10")) {
				tv_xiao.setTextColor(Color.WHITE);
				tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_shuang.setTextColor(Color.WHITE);
				tv_shuang
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("小");
				daxiaodanshuang.add("双");
			}
		} else if (adpater.onSet1.size() == 3) {
			if (adpater.onSet1.contains("12") && adpater.onSet1.contains("14")
					&& adpater.onSet1.contains("16")) {
				tv_da.setTextColor(Color.WHITE);
				tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_shuang.setTextColor(Color.WHITE);
				tv_shuang
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("大");
				daxiaodanshuang.add("双");
			} else if (adpater.onSet1.contains("5")
					&& adpater.onSet1.contains("7")
					&& adpater.onSet1.contains("9")) {
				tv_xiao.setTextColor(Color.WHITE);
				tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_single.setTextColor(Color.WHITE);
				tv_single
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("小");
				daxiaodanshuang.add("单");
			}
		}
	}

	/**
	 * Gridview的子项监听1
	 * 
	 * @author lenovo
	 * 
	 */
	class erbutong1 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if (null != vibrator)
				vibrator.vibrate(150);
			LinearLayout rl = (LinearLayout) view
					.findViewById(R.id.relativeLayout_pop_k3);
			TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text1);
			TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text2);
			String index = arg2 + 1 + "";

			if (adpater1.onSet1.contains(index)) {
				adpater1.onSet1.remove(index);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
				tv1.setTextColor(Color.RED);
				tv2.setTextColor(Color.BLACK);
			} else {

				if (playID == 8305) {// 二同号
					if (adpater1.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "最多只能选1个");
						return;
					}
				}

				if (playID == 8309) {
					if (adpater1.onSet1.size() > 1) {
						Toast.makeText(Select_K3_Activity.this, "最多只能选择2个胆码",
								1500);
						return;
					}
				} else if (playID == 8310) {
					if (adpater1.onSet1.size() > 0) {
						Toast.makeText(Select_K3_Activity.this, "最多只能选择1个胆码",
								1500);
						return;
					}
				}
				if (adpater2.onSet1.contains(index)) {
					adpater2.onSet1.remove(index);
					adpater2.notifyDataSetChanged();
				}
				adpater1.onSet1.add(index);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv1.setTextColor(Color.WHITE);
				tv2.setTextColor(Color.WHITE);
			}
			setCount();
			getJiangjin();
		}

	}

	/**
	 * 和值的大小单双点击监听
	 */
	class hezhi_daxiaoClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			list_hezhi.clear();
			switch (v.getId()) {
			// 大
			case R.id.tv_da:
				if (daxiaodanshuang.size() == 0) {
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("大");
					tv_da.setTextColor(Color.WHITE);
					for (int i = 11; i < 18; i++) {// 大
						list_hezhi.add(i + "");
					}

					System.out.println("======list_hezhi===="
							+ list_hezhi.toString());
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("大")) {
							sListIterator1.remove();
						}
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("小")) {
							sListIterator1.remove();
						}
					}
					for (int i = 11; i < 18; i++) {// 大
						list_hezhi.add(i + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("大");
					tv_da.setTextColor(Color.WHITE);

					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 5; i < 9; i++) {// 大单
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("大");
					tv_da.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 6; i < 9; i++) {// 大双
						list_hezhi.add(2 * i + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("大");
					tv_da.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("大")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// 单
						list_hezhi.add(2 * i + 3 + "");
					}

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("大")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// 双
						list_hezhi.add(2 * i + 2 + "");
					}

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("小")) {
							sListIterator1.remove();
						}
					}
					list_hezhi.clear();
					for (int i = 5; i < 9; i++) {// 大单
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("大");
					tv_da.setTextColor(Color.WHITE);
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("小")) {
							sListIterator1.remove();
						}
					}
					list_hezhi.clear();
					for (int i = 6; i < 9; i++) {// 大双
						list_hezhi.add(2 * i + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("大");
					tv_da.setTextColor(Color.WHITE);
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				}
				adpater.setonSet1(list_hezhi);
				break;

			/**
			 * 点击小
			 */
			case R.id.tv_xiao:
				if (daxiaodanshuang.size() == 0) {
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("小");
					tv_xiao.setTextColor(Color.WHITE);
					for (int i = 4; i < 11; i++) {// 小
						list_hezhi.add(i + "");
					}
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("小")) {
							sListIterator1.remove();
						}
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("大")) {
							sListIterator1.remove();
						}
					}
					for (int i = 4; i < 11; i++) {// 小
						list_hezhi.add(i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("小");
					tv_xiao.setTextColor(Color.WHITE);

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 1; i < 4; i++) {// 小单
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("小");
					tv_xiao.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 2; i < 6; i++) {// 小双
						list_hezhi.add(2 * i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("小");
					tv_xiao.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("大")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 4; i++) {// 小单
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("小");
					tv_xiao.setTextColor(Color.WHITE);

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("大")) {
							sListIterator1.remove();
						}
					}
					for (int i = 2; i < 6; i++) {// 小双
						list_hezhi.add(2 * i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("小");
					tv_xiao.setTextColor(Color.WHITE);

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("小")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// 单
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("小")) {
							sListIterator1.remove();
						}
					}
					for (int i = 2; i < 9; i++) {// 双
						list_hezhi.add(2 * i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				}
				adpater.setonSet1(list_hezhi);
				break;
			// 点击单
			case R.id.tv_dan:
				if (daxiaodanshuang.size() == 0) {
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("单");
					tv_single.setTextColor(Color.WHITE);
					for (int i = 1; i < 8; i++) {// 单
						list_hezhi.add(2 * i + 3 + "");
					}

				} else if (daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("单")) {
							sListIterator1.remove();
						}
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("双")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// 单
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("单");
					tv_single.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 5; i < 9; i++) {// 大单
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("单");
					tv_single.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.size() == 1) {

					for (int i = 1; i < 4; i++) {// 小单
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("单");
					tv_single.setTextColor(Color.WHITE);
					// 需要修改
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("单")) {
							sListIterator1.remove();
						}
					}
					for (int i = 11; i < 18; i++) {// 大
						list_hezhi.add(i + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));

				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("双")) {
							sListIterator1.remove();
						}
					}
					for (int i = 5; i < 9; i++) {// 大单
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("单");
					tv_single.setTextColor(Color.WHITE);

				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("单")) {
							sListIterator1.remove();
						}
					}
					for (int i = 4; i < 11; i++) {// 小
						list_hezhi.add(i + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("双")) {
							sListIterator1.remove();
						}
					}

					for (int i = 1; i < 4; i++) {// 小单
						list_hezhi.add(2 * i + 3 + "");
					}

					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("单");
					tv_single.setTextColor(Color.WHITE);
				}
				adpater.setonSet1(list_hezhi);
				break;
			// 点击双
			case R.id.tv_shuang:
				if (daxiaodanshuang.size() == 0) {
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("双");
					tv_shuang.setTextColor(Color.WHITE);

					for (int i = 2; i < 9; i++) {// 双
						list_hezhi.add(2 * i + "");
					}
				} else if (daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("双")) {
							sListIterator1.remove();
						}
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("单")) {
							sListIterator1.remove();
						}
					}
					for (int i = 2; i < 9; i++) {// 双
						list_hezhi.add(2 * i + "");
					}

					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("双");
					tv_shuang.setTextColor(Color.WHITE);

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));

				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 6; i < 9; i++) { // 大双
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("双");
					tv_shuang.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 2; i < 6; i++) {// 小双
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("双");
					tv_shuang.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("单")) {
							sListIterator1.remove();
						}
					}
					for (int i = 6; i < 9; i++) { // 大双
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("双");
					tv_shuang.setTextColor(Color.WHITE);

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("大")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("双")) {
							sListIterator1.remove();
						}
					}
					for (int i = 11; i < 18; i++) {// 大
						list_hezhi.add(i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("单")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("单")) {
							sListIterator1.remove();
						}
					}
					list_hezhi.clear();
					for (int i = 2; i < 6; i++) {// 小双
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("双");
					tv_shuang.setTextColor(Color.WHITE);

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("小")
						&& daxiaodanshuang.contains("双")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("双")) {
							sListIterator1.remove();
						}
					}
					for (int i = 4; i < 11; i++) {// 小
						list_hezhi.add(i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));
				}
				adpater.setonSet1(list_hezhi);
				break;

			default:
				break;
			}
			update();
			setCount();
			getJiangjin();
		}
	}

	/**
	 * Gridview的子项点击监听
	 * 
	 * @author lenovo
	 * 
	 */
	class erbutong2 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			if (null != vibrator)
				vibrator.vibrate(150);
			LinearLayout rl = (LinearLayout) view
					.findViewById(R.id.relativeLayout_pop_k3);
			TextView tv1 = (TextView) view.findViewById(R.id.pop_k3_text1);
			TextView tv2 = (TextView) view.findViewById(R.id.pop_k3_text2);
			String index = arg2 + 1 + "";
			if (adpater2.onSet1.contains(index)) {
				adpater2.onSet1.remove(index);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
				tv1.setTextColor(getResources().getColor(R.color.black_light));
				tv2.setTextColor(Color.BLACK);
			} else {
				if (playID == 8305) {// 二同号
					if (adpater2.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "最多只能选1个");
						return;
					}
				}

				if (adpater1.onSet1.contains(index)) {
					adpater1.onSet1.remove(index);
					adpater1.notifyDataSetChanged();
				}
				adpater2.onSet1.add(index);
				rl.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv1.setTextColor(Color.WHITE);
				tv2.setTextColor(Color.WHITE);
			}
			setCount();
			getJiangjin();
		}

	}

	/**
	 * 计算注数的方法
	 */
	private void setCount() {
		if (playID == 8305 || 8309 == playID || 8310 == playID)
			AppTools.totalCount = NumberTools.getCountFor_k3(playID,
					adpater1.onSet1, adpater2.onSet1, adpater_daxiao.onSet1);
		else {
			AppTools.totalCount = NumberTools.getCountFor_k3(playID,
					adpater.onSet1, new ArrayList<String>(),
					adpater_daxiao.onSet1);
		}
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 计算预测奖金的方法
	 */
	private void getJiangjin() {
		if (playID == 8305 || 8309 == playID || 8310 == playID) {
			max_jiangjin = JiangjinTools.getJiangjinFor_k3("max", playID,
					adpater1.onSet1, adpater2.onSet1, adpater_daxiao.onSet1);
			min_jiangjin = JiangjinTools.getJiangjinFor_k3("min", playID,
					adpater1.onSet1, adpater2.onSet1, adpater_daxiao.onSet1);
		} else {
			max_jiangjin = JiangjinTools.getJiangjinFor_k3("max", playID,
					adpater.onSet1, new ArrayList<String>(),
					adpater_daxiao.onSet1);

			min_jiangjin = JiangjinTools.getJiangjinFor_k3("min", playID,
					adpater.onSet1, new ArrayList<String>(),
					adpater_daxiao.onSet1);
		}

		if (max_jiangjin > 0) {
			ll_jiangjin.setVisibility(View.VISIBLE);
			if (max_jiangjin == min_jiangjin) {
				max_profits = max_jiangjin - AppTools.totalCount * 2;
				tv_jingjin.setText(max_jiangjin + "");

				if (max_profits < 1) {
					tv_winOrLost.setText("亏损");
					long lost = AppTools.totalCount * 2 - max_jiangjin;
					tv_profits.setText(lost + "");
				} else {
					tv_winOrLost.setText("盈利");
					tv_profits.setText(max_profits + "");
				}
			} else {
				min_profits = min_jiangjin - AppTools.totalCount * 2;
				max_profits = max_jiangjin - AppTools.totalCount * 2;
				tv_jingjin.setText(min_jiangjin + "至" + max_jiangjin);

				if (max_profits < 1 && min_profits < 1) {
					tv_winOrLost.setText("亏损");
					long lostmin = AppTools.totalCount * 2 - max_jiangjin;
					long lostmax = AppTools.totalCount * 2 - min_jiangjin;
					tv_profits.setText(lostmin + "至" + lostmax);
				} else {
					tv_winOrLost.setText("盈利");
					tv_profits.setText(min_profits + "至" + max_profits);
				}
			}
		} else {
			ll_jiangjin.setVisibility(View.GONE);
		}
	}

	/**
	 * 开奖详情
	 */
	private void winDetail() {
		Intent intent = new Intent(Select_K3_Activity.this,
				WinLotteryInfoActivity.class);
		intent.putExtra("lotteryId", AppTools.lottery.getLotteryID());
		startActivity(intent);
	}

	/**
	 * 创建popWindow
	 */
	private void createPopWindow() {
		LayoutInflater inflact = LayoutInflater.from(Select_K3_Activity.this);
		View view = inflact.inflate(R.layout.pop_item, null);
		btn_playHelp = (Button) view.findViewById(R.id.btn_playhelps);
		btn_winNumber = (Button) view.findViewById(R.id.btn_win);
		btn_forgetNum = (Button) view.findViewById(R.id.btn_omissive);// 显示遗漏值

		popWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		// 设置之后点击返回键 popwindow 会消失
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 1.0f);
			}
		});
		// 设置popwindow的消失事件
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
						LotteryUtils.backgroundAlpaha(Select_K3_Activity.this,
								1.0f);
					}
					break;
				}
				return true;
			}
		};

		btn_playHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playExplain();
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 1.0f);
			}
		});
		btn_winNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				winDetail();
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 1.0f);
			}
		});
		btn_forgetNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (visibleGone) {
					visibleGone = false;
					adpater.setVisibles(visibleGone);
					adpater1.setVisibles(visibleGone);
					adpater2.setVisibles(visibleGone);
					adpater_daxiao.setVisibles(visibleGone);
					adpater.notifyDataSetChanged();
					adpater1.notifyDataSetChanged();
					adpater2.notifyDataSetChanged();
					adpater_daxiao.notifyDataSetChanged();
					return;
				}
				if (!visibleGone) {
					visibleGone = true;
					adpater.setVisibles(visibleGone);
					adpater1.setVisibles(visibleGone);
					adpater2.setVisibles(visibleGone);
					adpater_daxiao.setVisibles(visibleGone);
					adpater.notifyDataSetChanged();
					adpater1.notifyDataSetChanged();
					adpater2.notifyDataSetChanged();
					adpater_daxiao.notifyDataSetChanged();
					return;
				}
				LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 1.0f);
				popWindow.dismiss();
			}
		});

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					popWindow = null;
				}
				return true;
			}
		});
	}

	/**
	 * 公共点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 返回 **/
		case R.id.btn_back:
			exit();
			break;
		/** 提交号码 **/
		case R.id.btn_submit:
			submitNumber();
			break;
		/** 清空 **/
		case R.id.btn_clearall:
			clear();
			break;
		/** 玩法说明 **/
		case R.id.btn_help:
			createPopWindow();
			popWindow.showAsDropDown(v);
			LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 0.5f);
			break;
		/** 机选 **/
		case R.id.layout_shake:
		case R.id.iv_shake:
		case R.id.layout_shake2:
		case R.id.iv_shake2:
			if (null != vibrator)
				vibrator.vibrate(300);
			selectRandom();// 机选
			break;
		case R.id.iv_up_down2:// 是否显示近10期开奖号码
		case R.id.ll_winnum:
			getWinnumberData();

			if (k3_win_listView.getVisibility() == View.GONE) {
				rote(3);// 旋转动画 向上
				k3_win_listView.setVisibility(View.VISIBLE);
				k3_layout_tile.setVisibility(View.VISIBLE);
				img_line.setVisibility(View.VISIBLE);
			} else if (k3_win_listView.getVisibility() == View.VISIBLE) {
				rote(4);// 旋转动画 向下
				k3_win_listView.setVisibility(View.GONE);
				k3_layout_tile.setVisibility(View.GONE);
				img_line.setVisibility(View.GONE);
			}
			break;
		/** 选玩法 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
		case R.id.iv_up_down:
			popUtil = new PopupWindowUtil_k3(this, data, layout_top_select,
					list_jiangjin, list_image);
			popUtil.setSelectIndex(parentIndex, itemIndex);
			popUtil.createPopWindow();
			popUtil.setOnSelectedListener(new PopupWindowUtil_k3.OnSelectedListener() {
				@Override
				public void getIndex(int parentIndex, int itemIndex) {
					if ((parentIndex == 0 && itemIndex <= 4)
							|| (parentIndex == 1 && itemIndex <= 1)) {
						if (Select_K3_Activity.this.parentIndex != parentIndex
								|| (Select_K3_Activity.this.parentIndex == parentIndex && Select_K3_Activity.this.itemIndex != itemIndex)) {
							Select_K3_Activity.this.parentIndex = parentIndex;
							Select_K3_Activity.this.itemIndex = itemIndex;
							clear();
							changePlayType();
							getYilouData();
						}
					}
					rote(2);// 旋转动画 向下
				}
			});
			rote(1);// 旋转动画 向上
			break;
		}
	}

	/**
	 * 刷新界面
	 */
	private void update() {
		if (adpater.onSet1 != null && adpater1.onSet1 != null
				&& adpater2.onSet1 != null && adpater_daxiao.onSet1 != null) {
			adpater.notifyDataSetChanged();
			adpater1.notifyDataSetChanged();
			adpater2.notifyDataSetChanged();
			adpater_daxiao.notifyDataSetChanged();
		}
	}

	/**
	 * 选择不同玩法的界面处理
	 */
	public void changePlayType() {
		String mark = "";
		if (0 == parentIndex) {// 普通
			mark = "普通-";
		} else if (1 == parentIndex) {// 胆拖
			mark = "胆拖-";
		}

		setShakeBtnVisible(View.VISIBLE);
		btn_playtype.setText(mark + data.get(parentIndex).get(itemIndex));
		relativeLayout1.setVisibility(View.GONE);
		relativeLayout2.setVisibility(View.GONE);
		ll_hezhishow.setVisibility(View.GONE);
		ll_hezhishow2.setVisibility(View.GONE);
		tv_top.setVisibility(View.VISIBLE);
		switch (parentIndex) {
		case 0:
			layout_shake.setVisibility(View.VISIBLE);
			layout_shake2.setVisibility(View.VISIBLE);
			switch (itemIndex) {
			case 0:// 和 值
				ll_daxiao.setVisibility(View.VISIBLE);// 大小单双布局
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.VISIBLE);
				playID = 8301;
				setData();// 拿取數據
				tip = Html.fromHtml("猜开奖号码相加的和");
				tv_top.setText(tip);
				gridView.setNumColumns(4);
				gridView_daxiao.setNumColumns(4);
				break;
			case 1:// 三同号
				ll_daxiao.setVisibility(View.GONE);// 下面玩法布局
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.VISIBLE);
				tv_daxiao.setVisibility(View.GONE);
				playID = 8303;
				setData();// 拿取數據
				tip = Html.fromHtml("猜豹子号(3个号相同)");
				tv_top.setText(tip);
				gridView.setNumColumns(3);
				gridView_daxiao.setNumColumns(1);
				break;
			case 2:// 二同号
				playID = 8305;
				setData();// 拿取數據
				ll_hezhishow2.setVisibility(View.VISIBLE);
				tv_img2.setText("复选");
				tv_title2.setText("猜开奖中2个指定相同号码，奖金15元");
				ll_daxiao.setVisibility(View.GONE);// 下面玩法布局
				relativeLayout2.setVisibility(View.VISIBLE);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);
				tv_dan.setVisibility(View.VISIBLE);
				tv_tuo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.VISIBLE);
				tv_daxiao.setVisibility(View.GONE);
				tip = Html.fromHtml("猜对子号(有2个号相同 )");
				tv_title.setText(tip);
				tv_dan.setText("同号");
				tv_tuo.setText("不同号");
				gridView_daxiao.setNumColumns(3);
				break;

			case 3:// 三不同号
				ll_hezhishow.setVisibility(View.VISIBLE);
				tv_img1.setVisibility(View.VISIBLE);
				tv_title1.setText("选三个不同号码,与开奖相同即中40元");
				ll_hezhishow2.setVisibility(View.VISIBLE);
				tv_img2.setText("三连号");
				tv_title2.setText("123,234,345,456任意开出即中40元");
				tv_daxiao.setVisibility(View.GONE);
				ll_daxiao.setVisibility(View.GONE);// 大小单双布局
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.VISIBLE);
				tv_top.setVisibility(View.GONE);
				playID = 8306;
				setData();// 拿取數據
				gridView.setNumColumns(6);
				gridView_daxiao.setNumColumns(1);
				break;

			case 4:// 二不同号
				ll_hezhishow.setVisibility(View.VISIBLE);
				tv_img1.setVisibility(View.GONE);
				tv_title1.setText("选2个不同号码，猜中开奖的任意2位即中8元");
				ll_daxiao.setVisibility(View.GONE);// 大小单双布局
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.GONE);
				playID = 8307;
				tv_top.setVisibility(View.GONE);
				setData();// 拿取數據
				gridView.setNumColumns(3);
				break;
			}

			break;
		case 1:// 胆拖玩法
			layout_shake2.setVisibility(View.INVISIBLE);
			switch (itemIndex) {
			case 0:
				playID = 8309;
				setData();// 拿取數據
				ll_daxiao.setVisibility(View.GONE);// 大小单双布局
				relativeLayout2.setVisibility(View.VISIBLE);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);

				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.GONE);

				tv_dan.setVisibility(View.VISIBLE);
				tv_tuo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.VISIBLE);

				tip = Html.fromHtml("单注奖金固定为"
						+ AppTools.changeStringColor("#e3393c", "80") + "元");
				tv_title.setText(tip);
				tv_dan.setText("胆码");
				tv_tuo.setText("拖码");
				break;
			case 1:
				playID = 8310;
				setData();// 拿取數據
				ll_daxiao.setVisibility(View.GONE);// 大小单双布局
				relativeLayout2.setVisibility(View.VISIBLE);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);
				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.GONE);
				tv_dan.setVisibility(View.VISIBLE);
				tv_tuo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.VISIBLE);
				tip = Html.fromHtml("单注奖金固定为"
						+ AppTools.changeStringColor("#e3393c", "80") + "元");
				tv_title.setText(tip);
				tv_dan.setText("胆码");
				tv_tuo.setText("拖码");
				break;
			default:
				break;
			}
			break;
		}
	}

	/**
	 * 箭头的动画旋转
	 * 
	 * @param type
	 *            :1.向上 2.向下
	 */
	public void rote(int type) {
		if (1 == type || 3 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_up);
		} else if (2 == type || 4 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_down);
		}
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setFillAfter(true);
		if (type < 3) {
			if (iv_up_down != null) {
				iv_up_down.startAnimation(animation);
			}
		} else {
			if (iv_up_down2 != null) {
				iv_up_down2.startAnimation(animation);
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (animation != null && iv_up_down != null && animation.hasStarted()) {
			iv_up_down.clearAnimation();
			iv_up_down.startAnimation(animation);
		}
	}

	/**
	 * 设置机选按钮是否可见
	 * 
	 * @param v
	 *            :是否可见的参数
	 */
	private void setShakeBtnVisible(int v) {
		layout_shake.setVisibility(v);
		layout_shake2.setVisibility(v);
	}

	/**
	 * 从投注页面跳转过来 将投注页面的值 显示出来
	 */
	public void getItem() {
		Intent intent = Select_K3_Activity.this.getIntent();
		bundle = intent.getBundleExtra("k_3Bundle");
		if (null != bundle) {
			playID = bundle.getInt("type");
			if (null != playtypeMap.get(playID)) {
				itemIndex = playtypeMap.get(playID);
				parentIndex = 0;
			}
			if (null != playtypeMap_dan.get(playID)) {
				itemIndex = playtypeMap_dan.get(playID);
				parentIndex = 1;
			}
			changePlayType();

			if (bundle.getInt("shouji") != 0) {
				if (null != bundle.getStringArrayList("threeSet")
						&& bundle.getStringArrayList("threeSet").size() > 0) {
					ArrayList<String> list3 = bundle
							.getStringArrayList("threeSet");
					adpater_daxiao.setonSet1(list3);
					System.out.println("select_+"
							+ adpater_daxiao.onSet1.toString());

				} else {
					if (playID != 8305 && playID != 8309 && playID != 8310) {
						ArrayList<String> list = bundle
								.getStringArrayList("oneSet");
						adpater.setonSet1(list);
						System.out.println("select_+"
								+ adpater.onSet1.toString());
					} else {
						ArrayList<String> list2 = bundle
								.getStringArrayList("oneSet");
						System.out.println("slele+list2" + list2);
						adpater1.setonSet1(list2);
						ArrayList<String> list3 = bundle
								.getStringArrayList("twoSet");
						System.out.println("slele+list3" + list3);
						adpater2.setonSet1(list3);
					}
				}
				if (playID == 8301) {
					chageDxds();
				}
				adpater.notifyDataSetChanged();
				adpater_daxiao.notifyDataSetChanged();
				adpater1.notifyDataSetChanged();
				adpater2.notifyDataSetChanged();
				setCount();
				getJiangjin();
			}
		}
	}

	/**
	 * 玩法说明
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_K3_Activity.this,
				PlayDescription.class);
		Select_K3_Activity.this.startActivity(intent);
	}

	/**
	 * 机选 按钮点击
	 */
	public void selectRandom() {
		List<String> list = new ArrayList<String>();
		if (null != vibrator)
			vibrator.vibrate(150);
		switch (playID) {
		case 8301: // 和值
			clearDaxiao();
			list.add((int) ((Math.random() * 14) + 4) + "");
			adpater.setonSet1(list);
			break;
		case 8302: // 三同通选
		case 8308: // 三连号通选
			list.add("1");
			adpater.setonSet1(list);
			break;
		case 8303: // 三同单选
		case 8304: // 二同复选
			adpater.setonSet1(NumberTools.getRandomNum6(1, 6));
			break;
		case 8307:// 二不同号
			adpater.setonSet1(NumberTools.getRandomNum6(2, 6));
			break;
		case 8306:// 三不同号
			adpater.setonSet1(NumberTools.getRandomNum6(3, 6));
			break;
		case 8305:// 二同单选
			list = NumberTools.getRandomNum6(1, 6);
			adpater1.setonSet1(list);
			while (true) {
				list = NumberTools.getRandomNum6(1, 6);
				if (!adpater1.onSet1.get(0).equals(list.get(0))) {
					adpater2.setonSet1(list);
					break;
				}
			}
			break;
		}
		update();
		setCount();
		getJiangjin();
	}

	/**
	 * 提交号码
	 */
	private void submitNumber() {
		if (AppTools.totalCount < 1) {
			MyToast.getToast(Select_K3_Activity.this, "请至少选择一注");
			return;
		} else {
			if (playID == 8305) {// 二同号
				if (adpater1.onSet1.size() == 0 && adpater2.onSet1.size() == 0) {
					// 不处理
				} else {
					if (adpater1.onSet1.size() == 0) {
						MyToast.getToast(Select_K3_Activity.this,
								"二同号单选至少需要选择一个相同号码");
						return;
					} else if (adpater2.onSet1.size() == 0) {
						MyToast.getToast(Select_K3_Activity.this,
								"二同号单选至少需要选择一个不同号码");
						return;
					}
				}
			}
			if (playID == 8306) { // 三不同号
				if (adpater.onSet1.size() < 3 && adpater.onSet1.size() != 0) {
					MyToast.getToast(Select_K3_Activity.this, "三不同号至少需要选择3个号码");
					return;
				}
			}
		}

		Intent intent1 = new Intent(Select_K3_Activity.this,
				Bet_k3_Activity.class);
		Bundle bundle = new Bundle();
		if (playID != 8305 && playID != 8309 && playID != 8310) {

			if (playID == 8301) {
				List<Integer> list1 = new ArrayList<Integer>();

				for (int i = 0; i < adpater.onSet1.size(); i++) {
					list1.add(Integer.parseInt(adpater.onSet1.get(i)));
				}

				Collections.sort(list1);
				bundle.putString("one", list1.toString().replace("[", "")
						.replace("]", ""));

			} else {
				Collections.sort(adpater.onSet1);
				Collections.sort(adpater_daxiao.onSet1);
				bundle.putString(
						"one",
						adpater.onSet1.toString().replace("[", "")
								.replace("]", ""));
				bundle.putString("three", adpater_daxiao.onSet1.toString()
						.replace("[", "").replace("]", ""));
			}

		} else {
			Collections.sort(adpater1.onSet1);
			Collections.sort(adpater2.onSet1);
			Collections.sort(adpater_daxiao.onSet1);
			bundle.putString("one", adpater1.onSet1.toString().replace("[", "")
					.replace("]", ""));
			bundle.putString("two", adpater2.onSet1.toString().replace("[", "")
					.replace("]", ""));
			bundle.putString(
					"three",
					adpater_daxiao.onSet1.toString().replace("[", "")
							.replace("]", ""));
		}
		bundle.putInt("playType", playID);
		intent1.putExtra("bundle", bundle);
		Select_K3_Activity.this.startActivity(intent1);
	}

	/**
	 * 清空选中状态
	 * 
	 */
	private void clear() {
		if (null != adpater.onSet1) {
			adpater.onSet1.clear();
			adpater.notifyDataSetChanged();
		}
		if (null != adpater1.onSet1) {
			adpater1.onSet1.clear();
			adpater1.notifyDataSetChanged();
		}
		if (null != adpater2.onSet1) {
			adpater2.onSet1.clear();
			adpater2.notifyDataSetChanged();
		}
		if (null != adpater_daxiao.onSet1) {
			adpater_daxiao.onSet1.clear();
			adpater_daxiao.notifyDataSetChanged();
		}
		clearDaxiao();
		AppTools.totalCount = 0;
		ll_jiangjin.setVisibility(View.GONE);
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 清空和值下面的大小单双的选中状态
	 */
	public void clearDaxiao() {
		daxiaodanshuang.clear();
		tv_da.setTextColor(getResources().getColor(R.color.black_light));
		tv_xiao.setTextColor(getResources().getColor(R.color.black_light));
		tv_single.setTextColor(getResources().getColor(R.color.black_light));
		tv_shuang.setTextColor(getResources().getColor(R.color.black_light));
		tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		tv_single.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
		tv_shuang.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
	}

	/**
	 * 返回该页面时处理页面显示
	 */
	public void register() {
		clearDaxiao();
		getItem();
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// 注册传感器
		vibrator = VibratorView.getVibrator(getApplicationContext());
	}

	/**
	 * 清空页面信息
	 */
	public void unregister() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/**
	 * 注册传感器 和 振动器
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		register();
	}

	/**
	 * 销毁传感器 和 振动器
	 */
	@Override
	protected void onStop() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/**
	 * 精确传感器 状态改变
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * 当传感器 状态改变的时候
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// 现在检测时间
		long currentUpdateTime = System.currentTimeMillis();
		if (vTime == 0) {
			vTime = currentUpdateTime;
		}
		// 两次检测的时间间隔
		long timeInterval = currentUpdateTime - btime;
		// 判断是否达到了检测时间间隔
		if (timeInterval < 150)
			return;
		// 现在的时间变成last时间
		btime = currentUpdateTime;
		// 获得x,y,z坐标
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// 获得x,y,z的变化值
		float deltaX = x - bx;
		float deltaY = y - by;
		float deltaZ = z - bz;
		// 将现在的坐标变成last坐标
		bx = x;
		by = y;
		bz = z;
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		// 达到速度阀值，发出提示
		if (speed >= 500 && currentUpdateTime - vTime > 700) {
			vTime = System.currentTimeMillis();
			selectRandom();
		}
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exit();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出当前页面
	 */
	private void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {
			if (0 != adpater.onSet1.size() || 0 != adpater1.onSet1.size()
					|| 0 != adpater2.onSet1.size()) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clear();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				clear();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else if (AppTools.list_numbers != null
				&& AppTools.list_numbers.size() != 0) {
			if (0 != adpater.onSet1.size() || 0 != adpater1.onSet1.size()
					|| 0 != adpater2.onSet1.size()) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clear();
							Intent intent = new Intent(Select_K3_Activity.this,
									Bet_k3_Activity.class);
							Select_K3_Activity.this.startActivity(intent);
						}
					}
				});
			} else {
				clear();
				Intent intent = new Intent(Select_K3_Activity.this,
						Bet_k3_Activity.class);
				Select_K3_Activity.this.startActivity(intent);
			}
		}
	}

	/**
	 * 刷新页面的注数和金额
	 */
	public void updateAdapter() {
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 11:
				adapter_win = new MywinNumAdapter(list_wininfo,
						Select_K3_Activity.this);
				k3_win_listView.setAdapter(adapter_win);
				adapter_win.notifyDataSetChanged();
				break;
			case 1:
				adpater = new Adpater(list1, list2, new ArrayList<String>(),
						Select_K3_Activity.this, visibleGone, list_yilou1);
				gridView.setAdapter(adpater);

				adpater_daxiao = new MyAdapter(list_daxiao, list_daxiao2,
						Select_K3_Activity.this, visibleGone, list_yilou3);
				gridView_daxiao.setAdapter(adpater_daxiao);
				// 默认二同单选号
				adpater1 = new Adpater(list1, new ArrayList<String>(),
						new ArrayList<String>(), Select_K3_Activity.this,
						visibleGone, list_yilou1);
				adpater2 = new Adpater(list2, new ArrayList<String>(),
						new ArrayList<String>(), Select_K3_Activity.this,
						visibleGone, list_yilou2);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);
				adpater1.notifyDataSetChanged();
				adpater2.notifyDataSetChanged();
				adpater.notifyDataSetChanged();
				adpater_daxiao.notifyDataSetChanged();
				break;
			case 0:
				index = 1;
				AppTools.isCanBet = true;
				for (Lottery ll : HallFragment.listLottery) {
					if (ll.getLotteryID().equals("83")) {
						AppTools.lottery = ll;
					}
				}
				break;

			case -500:
				MyToast.getToast(getApplicationContext(), "连接超时，请检查网络");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 处理页面数据的适配器
	 * 
	 * @author lenovo
	 * 
	 */
	class MyAdapter extends BaseAdapter {
		Context context;
		List<String> list_daxiao;
		List<String> list_daxiao2;
		boolean isforgotball;
		List<String> listyilou;
		private List<String> onSet1 = new ArrayList<String>();

		public MyAdapter(List<String> listdaxiao, List<String> listdaxiao2,
				Context context, boolean isforget, List<String> listyiou) {
			this.context = context;
			this.list_daxiao = listdaxiao;
			this.list_daxiao2 = listdaxiao2;
			this.isforgotball = isforget;
			this.listyilou = listyiou;
		}

		public void setonSet1(List<String> list) {
			this.onSet1 = list;
		}

		@Override
		public int getCount() {
			if (playID == 8305) {
				return listyilou.size();
			} else
				return list_daxiao.size();
		}

		public void setVisibles(boolean visibleGone) {
			isforgotball = visibleGone;
		}

		@Override
		public Object getItem(int arg0) {
			return list_daxiao.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				view = inflater.inflate(R.layout.k3_daxiao, null);

				holder.ll_pop_k3 = (LinearLayout) view
						.findViewById(R.id.ll_pop_k3);
				holder.ll_pop_k3 = (LinearLayout) view
						.findViewById(R.id.ll_pop_k3);
				holder.daxiao = (TextView) view.findViewById(R.id.pop_k3_text3);
				holder.daxiao2 = (TextView) view
						.findViewById(R.id.pop_k3_text4);
				holder.tv_k3_yilou = (TextView) view
						.findViewById(R.id.tv_k3_yilou);// 遗漏值
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (isforgotball) {
				holder.tv_k3_yilou.setVisibility(View.VISIBLE);
			} else {
				holder.tv_k3_yilou.setVisibility(View.GONE);
			}
			holder.ll_pop_k3
					.setBackgroundResource(R.drawable.bet_btn_dan_unselected);

			if (null != listyilou && listyilou.size() > 0)
				holder.tv_k3_yilou.setText(listyilou.get(position));
			if (position == 0) {
				if (null != listyilou && listyilou.size() > 0)
					holder.tv_k3_yilou.setText("遗漏：" + listyilou.get(position));
			}
			holder.daxiao.setText(list_daxiao.get(position));
			holder.daxiao.setTextColor(getResources().getColor(
					R.color.main_red_new));
			holder.daxiao2.setTextColor(Color.BLACK);
			if (list_daxiao2.size() != 0) {
				holder.daxiao2.setText(list_daxiao2.get(position));
				holder.daxiao2.setVisibility(View.VISIBLE);
			} else {
				holder.daxiao2.setVisibility(View.GONE);
			}
			if (onSet1.contains(position + 1 + "")) {
				holder.ll_pop_k3
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				holder.daxiao.setTextColor(Color.WHITE);
				holder.daxiao2.setTextColor(Color.WHITE);
			}
			return view;
		}

		class ViewHolder {
			private TextView daxiao, daxiao2;
			private LinearLayout ll_pop_k3;
			private TextView tv_k3_yilou;
		}

	}

	/**
	 * 控制遗漏值显示的适配器
	 * 
	 * @author lenovo
	 * 
	 */
	class Adpater extends BaseAdapter {
		Context context;
		List<String> list1;
		List<String> list2;
		boolean isforgetball;// 遗漏值是否显示
		List<String> list_yilou;

		private List<String> onSet1 = new ArrayList<String>();

		public Adpater(List<String> list1, List<String> list2,
				List<String> listdaxiao, Context context, boolean isforgot,
				List<String> listyiou) {
			this.context = context;
			this.list1 = list1;
			this.list2 = list2;
			this.isforgetball = isforgot;
			this.list_yilou = listyiou;
		}

		public void setonSet1(List<String> list) {
			this.onSet1 = list;
		}

		public void setVisibles(boolean visibleGone) {
			isforgetball = visibleGone;
		}

		@Override
		public int getCount() {
			if (playID == 8301) {
				if (list_yilou.size() > 0) {
					return list_yilou.size() - 2;
				} else
					return 14;

			} else
				return list1.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list1.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			// 判断View是否为空
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				view = inflater.inflate(R.layout.item_k3, null);
				// 得到控件
				holder.relativeLayout_poo = (LinearLayout) view
						.findViewById(R.id.relativeLayout_pop_k3);
				holder.tv_k3_yilou = (TextView) view
						.findViewById(R.id.tv_k3_yilou2);
				holder.hezhi = (TextView) view.findViewById(R.id.pop_k3_text1);
				holder.jiangjin = (TextView) view
						.findViewById(R.id.pop_k3_text2);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			if (isforgetball) {
				holder.tv_k3_yilou.setVisibility(View.VISIBLE);
			} else {
				holder.tv_k3_yilou.setVisibility(View.GONE);
			}
			if (null != list_yilou && list_yilou.size() > 0) {
				if (playID == 8301) {
					holder.tv_k3_yilou.setText(list_yilou.get(position + 1));
				} else {
					holder.tv_k3_yilou.setText(list_yilou.get(position));
				}
			}

			if (playID != 8305) {
				if (position == 0) {
					if (playID == 8301) {
						if (null != list_yilou && list_yilou.size() > 0)
							holder.tv_k3_yilou.setText("遗漏："
									+ list_yilou.get(position + 1));
					} else {
						if (null != list_yilou && list_yilou.size() > 0)
							holder.tv_k3_yilou.setText("遗漏："
									+ list_yilou.get(position));
					}

				}
			}
			holder.hezhi.setTextColor(Color.RED);
			holder.jiangjin.setTextColor(Color.BLACK);
			holder.hezhi.setText(list1.get(position));

			if (list2.size() != 0) {
				holder.jiangjin.setText(list2.get(position));
				holder.jiangjin.setVisibility(View.VISIBLE);
			} else {
				holder.jiangjin.setVisibility(View.GONE);
			}
			if (8302 == playID || 8308 == playID) {
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				holder.relativeLayout_poo.setLayoutParams(param);
			} else {
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				holder.relativeLayout_poo.setLayoutParams(param);
			}
			holder.relativeLayout_poo
					.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
			if (playID == 8301) {
				if (adpater.onSet1.contains(position + 4 + "")) {
					holder.relativeLayout_poo
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					holder.hezhi.setTextColor(Color.WHITE);
					holder.jiangjin.setTextColor(Color.WHITE);
				}
			} else {
				if (onSet1.contains(position + 1 + "")) {
					holder.relativeLayout_poo
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					holder.hezhi.setTextColor(Color.WHITE);
					holder.jiangjin.setTextColor(Color.WHITE);
				}
			}
			return view;
		}

		class ViewHolder {
			private TextView hezhi, jiangjin, tv_k3_yilou;
			private LinearLayout relativeLayout_poo;
		}
	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}
}
