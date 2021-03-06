package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.WinInfo;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.adapter.GridViewSSCAdapter;
import com.gcapp.tc.sd.ui.adapter.WinInfoAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.JiangjinTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.PopupWindowUtil;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.CustomDigitalClock;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.SmanagerView;
import com.gcapp.tc.view.VibratorView;
import com.gcapp.tc.R;

/**
 * ????????? ????????? ???????????????,????????????
 */
public class Select_SSC_Activity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "Select_SSCActivity";
	/* ?????? */
	private RelativeLayout layout_top_select;// ????????????
	private ImageButton btn_back; // ??????
	private LinearLayout layout_select_playtype;// ????????????
	private ImageView iv_up_down;// ??????????????????
	private TextView btn_playtype;// ??????
	private ImageButton btn_help;// ??????
	private ConfirmDialog dialog;// ?????????
	private Animation animation = null;
	/* ?????? */
	private TextView btn_clearall; // ????????????
	private TextView btn_submit; // ?????????
	public TextView tv_tatol_count;// ?????????
	public TextView tv_tatol_money;// ?????????
	/* ???????????? */
	private LinearLayout layout_shake;// ?????????
	private ImageView iv_shake;// ?????????
	private TextView tv_shake;// ?????????

	private CustomDigitalClock custTime;// ??????????????????
	private TextView tv_lotteryStoptime;// ???????????????
	private TextView tv_show1, tv_show2, tv_show3, tv_show4, tv_show5,
			tv_show6;
	private TextView tv_tip;
	private RelativeLayout rl_one, rl_two, rl_three, rl_four, rl_five, rl_six;
	private String selected_redball;// ?????????????????????
	private Bundle bundle;
	public Vibrator vibrator; // ?????????
	private SensorManager mSmanager; // ?????????

	/** ????????? */
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// ??????????????????
	private long vTime = 0; // ???????????????
	private SharedPreferences settings;
	private Editor editor;
	private PopupWindowUtil popUtil;
	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
	private int parentIndex = 0;
	private int itemIndex = 0;
	private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
	private int playType = 2803;
	private MyGridView gv_one, gv_two, gv_three, gv_four, gv_five, gv_six,
			gv_seven;
	private GridViewSSCAdapter mAdapterOne, mAdapterTwo, mAdapterThree,
			mAdapterFour, mAdapterFive, mAdapterSix, mAdapterSeven;
	private int index = 1; // ???????????????
	private ArrayList<String> list, list2, list3, list4, list5;

	private ArrayList<String> list_yilou_daxiao1 = new ArrayList<String>();
	private ArrayList<String> list_yilou_daxiao2 = new ArrayList<String>();

	private ArrayList<String> list_yilou1 = new ArrayList<String>();
	private ArrayList<String> list_yilou2 = new ArrayList<String>();
	private ArrayList<String> list_yilou3 = new ArrayList<String>();
	private ArrayList<String> list_yilou4 = new ArrayList<String>();
	private ArrayList<String> list_yilou5 = new ArrayList<String>();
	String strDXDS = "";
	private String[] numbers = new String[] { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9" };
	private String[] numbersZSHZ = new String[] { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27" };
	private String[] dxds = new String[] { "???", "???", "???", "???" };
	private MyHandler myHandler;
	private MyHandler2 myHandler2;
	private View view;
	private PopupWindow popWindow;
	private Button btn_playHelp, btn_winNumber, btn_forgetNum;// ?????????????????????????????????????????????
	private boolean visibleGone = true;

	// ????????????
	private LinearLayout ll_jiangjin;// ????????????
	private TextView tv_jingjin, tv_profits, tv_winOrLost;
	private long max_jiangjin, min_jiangjin;// ?????????????????????
	private long max_profits, min_profits;// ?????????????????????

	// ???5??????????????????
	private ScrollView sv_show_ball;
	private float moveY = 0;
	private ListView k3_win_listView;// ?????????5??? ???????????????
	private LinearLayout k3_layout_tile;// ?????????5??? ???????????????
	private WinInfoAdapter adapter_win;// ???5????????????????????????
	private List<WinInfo> list_wininfo = new ArrayList<WinInfo>();// ???5??????????????????adapter
	private Context context = Select_SSC_Activity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ???????????????
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_ssc);
		App.activityS.add(this);
		App.activityS1.add(this);
		clear();
		getYilouData();
		findView();
		init();
	}

	/**
	 * ?????????????????????
	 */
	public void getYilouData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"????????????...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "?????????????????????" + isusesInfo);

				try {
					JSONArray array = new JSONArray(
							isusesInfo.getString("miss"));
					JSONObject item = array.getJSONObject(0);
					if (list_yilou5.size() > 0 || list_yilou4.size() > 0
							|| list_yilou3.size() > 0 || list_yilou2.size() > 0
							|| list_yilou1.size() > 0
							|| list_yilou_daxiao1.size() > 0
							|| list_yilou_daxiao2.size() > 0) {
						list_yilou5.clear();
						list_yilou4.clear();
						list_yilou3.clear();
						list_yilou2.clear();
						list_yilou1.clear();
						list_yilou_daxiao1.clear();
						list_yilou_daxiao2.clear();
					}
					String str5 = "";
					String str4 = "";
					String str3 = "";
					String str2 = "";
					String str1 = "";

					String dxds1 = "";// ???????????? ??????????????????
					if (itemIndex == 0 && playType == 2803) {
						str5 = item.optString("5");
					} else if ((itemIndex == 1 && playType == 2803)) {
						str4 = item.optString("4");
						str5 = item.optString("5");
					} else if (playType == 2814) {// ????????????
						str4 = item.optString("1");
					} else if (itemIndex == 3 && playType == 2803) {
						str5 = item.optString("3");
						str4 = item.optString("4");
						str3 = item.optString("5");
					} else if ((itemIndex == 6 && playType == 2803)
							|| playType == 2805) {
						str5 = item.optString("1");
						str4 = item.optString("2");
						str3 = item.optString("3");
						str2 = item.optString("4");
						str1 = item.optString("5");
					} else if (playType == 2806 || playType == 2811
							|| playType == 2812 || playType == 2815) { // ????????????
						str5 = item.optString("1");
					} else if (playType == 2804) { // ????????????
						dxds1 = item.optString("1");
					}
					if (playType == 2804) {// ????????????
						if (null != dxds1 && !"".equals(dxds1))
							for (int i = 0; i < dxds1.split(",").length; i++) {
								if (i < 4) {
									list_yilou_daxiao1.add(dxds1.split(",")[i]);
								} else {
									list_yilou_daxiao2.add(dxds1.split(",")[i]);
								}
							}
					} else {
						if (null != str5 && !"".equals(str5))
							for (int i = 0; i < str5.split(",").length; i++) {
								list_yilou5.add(str5.split(",")[i]);
							}

						if (null != str4 && !"".equals(str4))
							for (int i = 0; i < str4.split(",").length; i++) {
								list_yilou4.add(str4.split(",")[i]);
							}
						if (null != str3 && !"".equals(str3))
							for (int i = 0; i < str3.split(",").length; i++) {
								list_yilou3.add(str3.split(",")[i]);
							}
						if (null != str2 && !"".equals(str2))
							for (int i = 0; i < str2.split(",").length; i++) {
								list_yilou2.add(str2.split(",")[i]);
							}
						if (null != str1 && !"".equals(str1))
							for (int i = 0; i < str1.split(",").length; i++) {
								list_yilou1.add(str1.split(",")[i]);
							}
					}

					if (playType == 2815 || itemIndex == 10) {
						mAdapterOne.setNumbers(numbersZSHZ);
					} else {
						mAdapterOne.setNumbers(numbers);
					}
					gv_one.setAdapter(mAdapterOne);
					gv_two.setAdapter(mAdapterTwo);
					gv_three.setAdapter(mAdapterThree);
					gv_four.setAdapter(mAdapterFour);
					gv_five.setAdapter(mAdapterFive);
					gv_six.setAdapter(mAdapterSix);
					gv_seven.setAdapter(mAdapterSeven);

					mAdapterOne.notifyDataSetChanged();
					mAdapterTwo.notifyDataSetChanged();
					mAdapterThree.notifyDataSetChanged();
					mAdapterFour.notifyDataSetChanged();
					mAdapterFive.notifyDataSetChanged();
					mAdapterSix.notifyDataSetChanged();
					mAdapterSeven.notifyDataSetChanged();
				} catch (Exception ex) {
				}

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(Select_SSC_Activity.this, "????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_SSC_Activity.this, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		requestUtil.commit_yilou(playType);
	}

	/**
	 * ??????????????????
	 */
	public void getWinnumberData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"????????????...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "????????????????????????" + isusesInfo);
				try {
					JSONArray array = new JSONArray(
							isusesInfo.getString("OpenInfo"));
					if (array.length() > 0) {
						if (list_wininfo.size() > 0) {
							list_wininfo.clear();
						}
					}
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						if (null != item) {
							WinInfo Info = new WinInfo();
							Info.setWinQihao(item.optString("Name"));
							Info.setWinNumber(item
									.optString("WinLotteryNumber"));
							Info.setState(item.optString("NumberType"));
							list_wininfo.add(Info);
						}
					}
				} catch (Exception ex) {
					Log.i("login", "???????????????---" + ex.getMessage());
				}
				k3_win_listView.setVisibility(View.VISIBLE);
				k3_layout_tile.setVisibility(View.VISIBLE);
				adapter_win.notifyDataSetChanged();

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(Select_SSC_Activity.this, "????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_SSC_Activity.this, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		requestUtil.commit_winNumber("28");
	}

	/**
	 * ?????????UI???????????????
	 */
	private void findView() {
		AppTools.isCanBet = true;
		bundle = new Bundle();
		myHandler = new MyHandler();
		myHandler2 = new MyHandler2();

		k3_win_listView = (ListView) findViewById(R.id.k3_win_listView);// ?????????5???
																		// ???????????????
		k3_layout_tile = (LinearLayout) findViewById(R.id.k3_layout_tile);// ?????????5???
																			// ???????????????
		adapter_win = new WinInfoAdapter(list_wininfo, Select_SSC_Activity.this);
		k3_win_listView.setAdapter(adapter_win);
		sv_show_ball = (ScrollView) findViewById(R.id.sv_show_ball);

		ll_jiangjin = (LinearLayout) findViewById(R.id.ll_jiangjin);// ????????????
		ll_jiangjin.setVisibility(View.GONE);
		tv_jingjin = (TextView) findViewById(R.id.tv_jingjin);// ??????
		tv_profits = (TextView) findViewById(R.id.tv_profits);// ??????
		tv_winOrLost = (TextView) findViewById(R.id.tv_winOrLost);

		custTime = (CustomDigitalClock) this
				.findViewById(R.id.bet_tv_lotteryEnd);
		tv_lotteryStoptime = (TextView) findViewById(R.id.tv_lotteryStoptime);
		getCustomTime();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (TextView) findViewById(R.id.btn_submit);

		layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
		iv_shake = (ImageView) findViewById(R.id.iv_shake);
		tv_shake = (TextView) findViewById(R.id.tv_shake);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		tv_show1 = (TextView) this.findViewById(R.id.tv_show);
		tv_show2 = (TextView) this.findViewById(R.id.tv_show2);
		tv_show3 = (TextView) this.findViewById(R.id.tv_show3);
		tv_show4 = (TextView) this.findViewById(R.id.tv_show4);
		tv_show5 = (TextView) this.findViewById(R.id.tv_show5);
		tv_show6 = (TextView) this.findViewById(R.id.tv_show6);
		tv_tip = (TextView) this.findViewById(R.id.tv_tip);
		gv_one = (MyGridView) findViewById(R.id.number_sv_center_gv_showOne);
		gv_two = (MyGridView) findViewById(R.id.number_sv_center_gv_showTwo);
		gv_three = (MyGridView) findViewById(R.id.number_sv_center_gv_showThree);
		gv_four = (MyGridView) findViewById(R.id.number_sv_center_gv_showFour);
		gv_five = (MyGridView) findViewById(R.id.number_sv_center_gv_showFive);
		gv_six = (MyGridView) findViewById(R.id.number_sv_dxds);
		gv_seven = (MyGridView) findViewById(R.id.number_sv_dxds2);
		rl_one = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlOne);
		rl_two = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlTwo);
		rl_three = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlThree);
		rl_four = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlFour);
		rl_five = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlFive);
		rl_six = (RelativeLayout) this.findViewById(R.id.rl_dxds);
		mAdapterOne = new GridViewSSCAdapter(Select_SSC_Activity.this, numbers,
				false, false, visibleGone, list_yilou5);
		mAdapterTwo = new GridViewSSCAdapter(Select_SSC_Activity.this, numbers,
				false, false, visibleGone, list_yilou4);
		mAdapterThree = new GridViewSSCAdapter(Select_SSC_Activity.this,
				numbers, false, false, visibleGone, list_yilou3);
		mAdapterFour = new GridViewSSCAdapter(Select_SSC_Activity.this,
				numbers, false, false, visibleGone, list_yilou2);
		mAdapterFive = new GridViewSSCAdapter(Select_SSC_Activity.this,
				numbers, false, false, visibleGone, list_yilou1);

		mAdapterSix = new GridViewSSCAdapter(Select_SSC_Activity.this, dxds,
				false, visibleGone, list_yilou_daxiao1);
		mAdapterSeven = new GridViewSSCAdapter(Select_SSC_Activity.this, dxds,
				false, visibleGone, list_yilou_daxiao2);

		gv_one.setAdapter(mAdapterOne);
		gv_two.setAdapter(mAdapterTwo);
		gv_three.setAdapter(mAdapterThree);
		gv_four.setAdapter(mAdapterFour);
		gv_five.setAdapter(mAdapterFive);
		gv_six.setAdapter(mAdapterSix);
		gv_seven.setAdapter(mAdapterSeven);

		sv_show_ball.setOnTouchListener(new MyOntouchListener());
		gv_one.setOnTouchListener(new MyOntouchListener());
		gv_two.setOnTouchListener(new MyOntouchListener());
		gv_three.setOnTouchListener(new MyOntouchListener());
		gv_four.setOnTouchListener(new MyOntouchListener());
		gv_five.setOnTouchListener(new MyOntouchListener());
		gv_six.setOnTouchListener(new MyOntouchListener());
		gv_seven.setOnTouchListener(new MyOntouchListener());

		gv_one.setOnItemClickListener(new MyItemClickListener_1());
		gv_two.setOnItemClickListener(new MyItemClickListener_2());
		gv_three.setOnItemClickListener(new MyItemClickListener_3());
		gv_four.setOnItemClickListener(new MyItemClickListener_4());
		gv_five.setOnItemClickListener(new MyItemClickListener_5());
		gv_six.setOnItemClickListener(new MyItemClickListener_6());
		gv_seven.setOnItemClickListener(new MyItemClickListener_7());
		// ?????????
		custTime.setClockListener(new CustomDigitalClock.ClockListener() {
			@Override
			public void timeEnd() {
				if (custTime.getType() == 3) {
					custTime.setMTickStop(false);
					custTime.setType(4);
					custTime.setEndTime(AppTools.lottery.getDistanceTime2());
					tv_lotteryStoptime.setText("?????????????????????: ");
				} else {
					RequestUtil requestUtil = new RequestUtil(
							Select_SSC_Activity.this, false, 0) {
						@Override
						public void responseCallback(JSONObject reponseJson) {
							String result = AppTools.getDate(reponseJson);
							if ("0".equals(result)) {
								getCustomTime();
								// MainActivity.update();
							} else if ("-1001".equals(result)) {
								if (RequestUtil.DEBUG) {
									Log.e(TAG, "??????????????????????????????");
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
		// ??????????????????
		// ??????
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	/**
	 * ??????????????????????????????
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
			tv_lotteryStoptime.setText("?????????????????????: ");
		} else if (custTime.getType() == 3) {
			tv_lotteryStoptime.setText("?????????????????? ");
		}
	}

	/**
	 * ??????????????? ??????????????????
	 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
		vibrator = VibratorView.getVibrator(getApplicationContext());
		btn_back.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		iv_up_down.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		btn_help.setOnClickListener(this);
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		layout_shake.setOnClickListener(this);
		iv_shake.setOnClickListener(this);
		tv_shake.setOnClickListener(this);
		settings = getSharedPreferences("app_user", 0);// ??????SharedPreference??????
		editor = settings.edit();// ??????????????????
		Map<Integer, String> playType = new HashMap<Integer, String>();
		// ???????????????????????????
		playType.put(0, "????????????");
		playType.put(1, "????????????");
		playType.put(2, "????????????");
		playType.put(3, "????????????");
		playType.put(4, "????????????");
		playType.put(5, "????????????");
		playType.put(6, "????????????");
		playType.put(7, "????????????");
		playType.put(8, "????????????");
		// playType.put(9, "????????????");
		// playType.put(10, "????????????");
		// playType.put(11, "");
		Set<Integer> set = playType.keySet();
		// int[] playtype_array = { 2803, 2803, 2806, 2803, 2811, 2812, 2803,
		// 2805, 2804, 2814, 2815 };
		int[] playtype_array = { 2803, 2803, 2806, 2803, 2811, 2812, 2803,
				2805, 2804 };
		for (Integer i : set) {
			if (i < 9) {
				playtypeMap.put(playtype_array[i], i);
			}
		}
		data.put(0, playType);
		dialog = new ConfirmDialog(this, R.style.dialog);
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		showGridView();

		if (btn_playtype.getText().toString().contains("????????????")) {
			tv_tip.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
		}
	}

	/**
	 * ???????????????5????????????????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyOntouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			sv_show_ball.getParent().requestDisallowInterceptTouchEvent(true);// ?????????????????????????????????touch??????
			float y = event.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				moveY = y;
			}
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				float mY = event.getY();

				if (mY - moveY > 20) {
					AppTools.flag = 0;
					getWinnumberData();
					// myAsynTask2 = new MyAsynTask2();
					// myAsynTask2.execute();

				} else if (moveY - mY > 20) {
					AppTools.flag = -1;
					k3_win_listView.setVisibility(View.GONE);
					k3_layout_tile.setVisibility(View.GONE);
				}
			}
			if (event.getAction() == MotionEvent.ACTION_SCROLL) {
			}
			return false;
		}

		public boolean onInterceptTouchEvent(MotionEvent ev) {
			if (AppTools.flag == -1) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * ?????????Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_1 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			String str = position + "";
			if (mAdapterOne.getOneSet().contains(str)) {
				mAdapterOne.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {

				if (itemIndex == 0 || itemIndex == 1
						|| itemIndex == 3
						|| itemIndex == 6
						|| itemIndex == 7) {// ????????????????????????????????????
					if (mAdapterOne.getOneSetSize() >= 1) {
						MyToast.getToast(Select_SSC_Activity.this, "???????????????1???");
						return;
					}
				}

				if (9 == itemIndex) {// ?????? ??????
					mAdapterOne.clear();
					mAdapterOne.addOne(str);
				} else {
					mAdapterOne.addOne(str);
				}
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			mAdapterOne.notifyDataSetChanged();
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ???2???Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_2 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			String str = position + "";
			if (mAdapterTwo.getOneSet().contains(str)) {
				mAdapterTwo.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (itemIndex == 0 || itemIndex == 1
						|| itemIndex == 3
						|| itemIndex == 6
						|| itemIndex == 7) {// ????????????????????????????????????
					if (mAdapterTwo.getOneSetSize() >= 1) {
						MyToast.getToast(Select_SSC_Activity.this, "???????????????1???");
						return;
					}
				}

				if (9 == itemIndex) {// ?????? ??????
					mAdapterTwo.clear();
					mAdapterTwo.addOne(str);
				} else {
					mAdapterTwo.addOne(str);
				}
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			mAdapterTwo.notifyDataSetChanged();
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ???3???Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_3 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			String str = position + "";

			if (mAdapterThree.getOneSet().contains(str)) {
				mAdapterThree.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (itemIndex == 0 || itemIndex == 1
						|| itemIndex == 3
						|| itemIndex == 6
						|| itemIndex == 7) {// ????????????????????????????????????
					if (mAdapterThree.getOneSetSize() >= 1) {
						MyToast.getToast(Select_SSC_Activity.this, "???????????????1???");
						return;
					}
				}

				mAdapterThree.addOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ???4???Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_4 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			String str = position + "";
			if (mAdapterFour.getOneSet().contains(str)) {
				mAdapterFour.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (itemIndex == 0 || itemIndex == 1
						|| itemIndex == 3
						|| itemIndex == 6
						|| itemIndex == 7) {// ????????????????????????????????????
					if (mAdapterFour.getOneSetSize() >= 1) {
						MyToast.getToast(Select_SSC_Activity.this, "???????????????1???");
						return;
					}
				}

				mAdapterFour.addOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ???5???Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_5 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			String str = position + "";
			if (mAdapterFive.getOneSet().contains(str)) {
				mAdapterFive.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (itemIndex == 0 || itemIndex == 1
						|| itemIndex == 3
						|| itemIndex == 6
						|| itemIndex == 7) {// ????????????????????????????????????
					if (mAdapterFive.getOneSetSize() >= 1) {
						MyToast.getToast(Select_SSC_Activity.this, "???????????????1???");
						return;
					}
				}
				mAdapterFive.addOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ???6???Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_6 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			if (mAdapterSix.getIndexSet().contains(position)) {
				mAdapterSix.removeIndex(position);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				mAdapterSix.addIndex(position);
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ???7???Gridview???????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_7 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (null != vibrator)
				vibrator.vibrate(100);
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			if (mAdapterSeven.getIndexSet().contains(position)) {
				mAdapterSeven.removeIndex(position);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_SSC_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				mAdapterSeven.addIndex(position);
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ????????????
	 */
	private void setTotalCount() {
		if (itemIndex == 8)
			AppTools.totalCount = mAdapterSix.getIndexSetSize()
					* mAdapterSeven.getIndexSetSize();
		else {
			AppTools.totalCount = NumberTools.getSSC_count(
					mAdapterOne.getOneSet(), mAdapterTwo.getOneSet(),
					mAdapterThree.getOneSet(), mAdapterFour.getOneSet(),
					mAdapterFive.getOneSet(), itemIndex + 1);
		}
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * ?????????????????????
	 */
	private void getJiangjin() {
		if (itemIndex == 8) {
			if (AppTools.totalCount > 0) {
				max_jiangjin = 4;
				min_jiangjin = 4;
			} else {
				max_jiangjin = 0;
				min_jiangjin = 0;
			}
		} else {
			max_jiangjin = JiangjinTools.getSSC_Jiangjin("max",
					mAdapterOne.getOneSet(), mAdapterTwo.getOneSet(),
					mAdapterThree.getOneSet(), mAdapterFour.getOneSet(),
					mAdapterFive.getOneSet(), itemIndex + 1);

			min_jiangjin = JiangjinTools.getSSC_Jiangjin("min",
					mAdapterOne.getOneSet(), mAdapterTwo.getOneSet(),
					mAdapterThree.getOneSet(), mAdapterFour.getOneSet(),
					mAdapterFive.getOneSet(), itemIndex + 1);
		}

		if (max_jiangjin > 0) {
			ll_jiangjin.setVisibility(View.VISIBLE);
			if (max_jiangjin == min_jiangjin) {
				max_profits = max_jiangjin - AppTools.totalCount * 2;
				tv_jingjin.setText(max_jiangjin + "");

				if (max_profits < 1) {
					tv_winOrLost.setText("??????");
					long lost = AppTools.totalCount * 2 - max_jiangjin;
					tv_profits.setText(lost + "");
				} else {
					tv_winOrLost.setText("??????");
					tv_profits.setText(max_profits + "");
				}
			} else {
				min_profits = min_jiangjin - AppTools.totalCount * 2;
				max_profits = max_jiangjin - AppTools.totalCount * 2;
				tv_jingjin.setText(min_jiangjin + "???" + max_jiangjin);

				if (max_profits < 1 && min_profits < 1) {
					tv_winOrLost.setText("??????");
					long lostmin = AppTools.totalCount * 2 - max_jiangjin;
					long lostmax = AppTools.totalCount * 2 - min_jiangjin;
					tv_profits.setText(lostmin + "???" + lostmax);
				} else {
					tv_winOrLost.setText("??????");
					tv_profits.setText(min_profits + "???" + max_profits);
				}
			}
		} else {
			ll_jiangjin.setVisibility(View.GONE);
		}
	}

	/**
	 * ??????????????????
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** ?????? **/
		case R.id.btn_back:
			exit();
			break;
		/** ???????????? **/
		case R.id.btn_submit:
			submitNumber();
			break;
		/** ?????? **/
		case R.id.btn_clearall:
			clear();
			break;
		case R.id.btn_help:
			createPopWindow();
			popWindow.showAsDropDown(v);
			LotteryUtils.backgroundAlpaha(Select_SSC_Activity.this, 0.5f);
			break;
		/** ?????? **/
		case R.id.layout_shake:
		case R.id.iv_shake:
		case R.id.tv_shake:
		case R.id.layout_shake2:
		case R.id.iv_shake2:
			if (null != vibrator)
				vibrator.vibrate(300);
			selectRandom();// ??????
			break;
		/** ????????? **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
		case R.id.iv_up_down:
			popUtil = new PopupWindowUtil(this, data, layout_top_select);
			popUtil.setSelectIndex(parentIndex, itemIndex);
			popUtil.createPopWindow();
			popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
				@Override
				public void getIndex(int parentIndex, int itemIndex) {
					if (itemIndex != Select_SSC_Activity.this.itemIndex) {
						if (11 > itemIndex) {
							Select_SSC_Activity.this.parentIndex = parentIndex;
							Select_SSC_Activity.this.itemIndex = itemIndex;
							k3_win_listView.setVisibility(View.GONE);
							k3_layout_tile.setVisibility(View.GONE);

							changePlayType();
							getYilouData();
							AppTools.totalCount = 0;
							clear();
						}
					}
					rote(2);// ???????????? ??????
				}
			});
			rote(1);// ???????????? ??????
			break;
		}
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 */
	private void createPopWindow() {
		LayoutInflater inflact = LayoutInflater.from(Select_SSC_Activity.this);
		View view = inflact.inflate(R.layout.pop_item, null);
		btn_playHelp = (Button) view.findViewById(R.id.btn_playhelps);
		btn_winNumber = (Button) view.findViewById(R.id.btn_win);
		btn_forgetNum = (Button) view.findViewById(R.id.btn_omissive);// ???????????????

		popWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true); // ??????PopupWindow???????????????
		popWindow.setTouchable(true); // ??????PopupWindow?????????
		popWindow.setOutsideTouchable(true); // ??????PopupWindow???????????????????????????
		// ??????????????????????????? popwindow ?????????
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				LotteryUtils.backgroundAlpaha(Select_SSC_Activity.this, 1.0f);
			}
		});
		// ??????popwindow???????????????
		// ????????????????????????????????????????????????popupwindow????????????????????????????????????????????????
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
						LotteryUtils.backgroundAlpaha(Select_SSC_Activity.this,
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
				LotteryUtils.backgroundAlpaha(Select_SSC_Activity.this, 1.0f);
			}
		});
		btn_winNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				winDetail();
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(Select_SSC_Activity.this, 1.0f);
			}
		});
		btn_forgetNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (visibleGone) {
					visibleGone = false;
					mAdapterOne.setVisibles(visibleGone);
					mAdapterTwo.setVisibles(visibleGone);
					mAdapterThree.setVisibles(visibleGone);
					mAdapterFour.setVisibles(visibleGone);
					mAdapterFive.setVisibles(visibleGone);
					mAdapterSix.setVisibles(visibleGone);
					mAdapterSeven.setVisibles(visibleGone);
					mAdapterOne.notifyDataSetChanged();
					mAdapterTwo.notifyDataSetChanged();
					mAdapterThree.notifyDataSetChanged();
					mAdapterFour.notifyDataSetChanged();
					mAdapterFive.notifyDataSetChanged();
					mAdapterSix.notifyDataSetChanged();
					mAdapterSeven.notifyDataSetChanged();
					return;
				}
				if (!visibleGone) {
					visibleGone = true;
					mAdapterOne.setVisibles(visibleGone);
					mAdapterTwo.setVisibles(visibleGone);
					mAdapterThree.setVisibles(visibleGone);
					mAdapterFour.setVisibles(visibleGone);
					mAdapterFive.setVisibles(visibleGone);
					mAdapterSix.setVisibles(visibleGone);
					mAdapterSeven.setVisibles(visibleGone);
					mAdapterOne.notifyDataSetChanged();
					mAdapterTwo.notifyDataSetChanged();
					mAdapterThree.notifyDataSetChanged();
					mAdapterFour.notifyDataSetChanged();
					mAdapterFive.notifyDataSetChanged();
					mAdapterSix.notifyDataSetChanged();
					mAdapterSeven.notifyDataSetChanged();
					return;
				}
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(Select_SSC_Activity.this, 1.0f);
			}
		});

		// ??????????????????????????????????????????popupwindow???????????????
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
	 * ????????????????????????????????????
	 */
	public void changePlayType() {
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		layout_shake.setVisibility(View.INVISIBLE);
		switch (itemIndex) {
		case 0:// ????????????
			if (itemIndex != 0)
				clear();
			itemIndex = 0;
			showGridView();
			layout_shake.setVisibility(View.VISIBLE);
			break;
		case 1:// ????????????
			if (itemIndex != 1)
				clear();
			itemIndex = 1;
			showGridView();
			layout_shake.setVisibility(View.VISIBLE);
			break;
		case 2:// ????????????
			if (itemIndex != 2)
				clear();
			itemIndex = 2;
			showGridView();
			break;
		case 3:// ????????????
			if (itemIndex != 3)
				clear();
			itemIndex = 3;
			showGridView();
			layout_shake.setVisibility(View.VISIBLE);
			break;
		case 4:// ????????????
			if (itemIndex != 4)
				clear();
			itemIndex = 4;
			showGridView();
			break;
		case 5:// ????????????
			if (itemIndex != 5)
				clear();
			itemIndex = 5;
			showGridView();
			break;
		case 6:// ????????????
			if (itemIndex != 6)
				clear();
			itemIndex = 6;
			showGridView();
			layout_shake.setVisibility(View.VISIBLE);
			break;
		case 7:// ????????????
			if (itemIndex != 7)
				clear();
			itemIndex = 7;
			showGridView();
			layout_shake.setVisibility(View.VISIBLE);
			break;
		case 8:// ????????????
			if (itemIndex != 8)
				clear();
			itemIndex = 8;
			showGridView();
			break;
		case 9:// ????????????
			if (itemIndex != 9)
				clear();
			itemIndex = 9;
			showGridView();
			break;
		case 10:// ????????????
			if (itemIndex != 10)
				clear();
			itemIndex = 10;
			showGridView();
			break;
		}
	}

	/**
	 * ??????
	 * 
	 * @param type
	 *            1.?????? 2.??????
	 */
	public void rote(int type) {
		if (1 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_up);
		} else if (2 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_down);
		}
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		animation.setFillAfter(true);
		if (iv_up_down != null) {
			iv_up_down.startAnimation(animation);
		}
	}

	/**
	 * ???????????????????????????
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (animation != null && iv_up_down != null && animation.hasStarted()) {
			iv_up_down.clearAnimation();
			iv_up_down.startAnimation(animation);
		}
	}

	/**
	 * ??????????????????????????? ????????????????????? ????????????
	 */
	public void getItem() {
		k3_win_listView.setVisibility(View.GONE);
		k3_layout_tile.setVisibility(View.GONE);

		Intent intent = Select_SSC_Activity.this.getIntent();
		/* ???????????? ???????????????????????? */
		if (AppTools.list_numbers != null && AppTools.list_numbers.size() != 0)
			playType = AppTools.list_numbers.get(0).getPlayType();
		Bundle bundle = intent.getBundleExtra("SSCBundle");
		if (null != bundle) {
			itemIndex = bundle.getInt("type") - 1;
			playType = bundle.getInt("playType");

			if (itemIndex == 8) {
				strDXDS = bundle.getString("Bet_dxds");
				if (strDXDS != null && strDXDS.split(",").length == 2) {
					if (strDXDS.split(",")[0].contains("???"))
						mAdapterSix.addIndex(0);
					if (strDXDS.split(",")[0].contains("???"))
						mAdapterSix.addIndex(1);
					if (strDXDS.split(",")[0].contains("???"))
						mAdapterSix.addIndex(2);
					if (strDXDS.split(",")[0].contains("???"))
						mAdapterSix.addIndex(3);

					if (strDXDS.split(",")[1].contains("???"))
						mAdapterSeven.addIndex(0);
					if (strDXDS.split(",")[1].contains("???"))
						mAdapterSeven.addIndex(1);
					if (strDXDS.split(",")[1].contains("???"))
						mAdapterSeven.addIndex(2);
					if (strDXDS.split(",")[1].contains("???"))
						mAdapterSeven.addIndex(3);
				}
			} else {
				list = bundle.getStringArrayList("oneSet");
				mAdapterOne.setOneSet(list);
				list2 = bundle.getStringArrayList("twoSet");
				mAdapterTwo.setOneSet(list2);
				list3 = bundle.getStringArrayList("threeSet");
				mAdapterThree.setOneSet(list3);
				list4 = bundle.getStringArrayList("fourSet");
				mAdapterFour.setOneSet(list4);
				list5 = bundle.getStringArrayList("fiveSet");
				mAdapterFive.setOneSet(list5);
			}
			setTotalCount();
			changePlayType();
			getJiangjin();
			getYilouData();
		}
	}

	/**
	 * ????????????ID????????????????????????
	 */
	private void showGridView() {
		rl_six.setVisibility(View.GONE);

		vibrator = VibratorView.getVibrator(Select_SSC_Activity.this);
		switch (itemIndex) {
		case 0:
			showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE,
					View.GONE);
			btn_playtype.setText("????????????");
			playType = 2803;
			tv_show1.setText("??????");
			tv_tip.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			break;

		case 1:
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,
					View.GONE);
			tv_show2.setText("??????");
			tv_show1.setText("??????");
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("??????????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			playType = 2803;
			break;
		case 2:
			showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE,
					View.GONE);
			tv_show1.setText("??????");
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			playType = 2806;
			break;
		case 3:
			showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,
					View.GONE);
			tv_show1.setText("??????");
			tv_show2.setText("??????");
			tv_show3.setText("??????");
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("??????????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			playType = 2803;
			break;
		case 4:
			showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE,
					View.GONE);
			tv_show1.setText("??????");
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			playType = 2811;
			break;
		case 5:
			showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE,
					View.GONE);
			tv_show1.setText("??????");
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("????????????"
					+ "<font color='#e3393c'>3</font>" + "?????????"));
			playType = 2812;
			break;
		case 6:
			showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE,
					View.VISIBLE, View.VISIBLE);
			tv_show1.setText("??????");
			tv_show2.setText("??????");
			tv_show3.setText("??????");
			tv_show4.setText("??????");
			tv_show5.setText("??????");
			playType = 2803;
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("??????????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			break;
		case 7:
			showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE,
					View.VISIBLE, View.VISIBLE);
			tv_show1.setText("??????");
			tv_show2.setText("??????");
			tv_show3.setText("??????");
			tv_show4.setText("??????");
			tv_show5.setText("??????");
			playType = 2805;
			btn_playtype.setText("????????????");
			tv_tip.setText(Html.fromHtml("??????????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			break;
		case 8:
			showGridView(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE);
			btn_playtype.setText("????????????");
			playType = 2804;
			tv_show6.setText("??????");
			tv_show6.setVisibility(View.GONE);
			rl_six.setVisibility(View.VISIBLE);
			tv_tip.setText("?????????????????????");
			break;

		case 9:
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE,
					View.GONE);
			btn_playtype.setText("????????????");
			playType = 2814;
			tv_show1.setText("??????");
			tv_show2.setText("??????");
			tv_tip.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			break;
		case 10:
			showGridView(View.VISIBLE, View.GONE, View.GONE, View.GONE,
					View.GONE);
			btn_playtype.setText("????????????");
			playType = 2815;
			tv_show1.setText("??????");
			tv_tip.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			break;
		}
	}

	/**
	 * ??????GridView
	 */
	private void showGridView(int one, int two, int three, int four, int five) {
		rl_one.setVisibility(one);
		rl_two.setVisibility(two);
		rl_three.setVisibility(three);
		rl_four.setVisibility(four);
		rl_five.setVisibility(five);
	}

	/**
	 * ????????????
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_SSC_Activity.this,
				PlayDescription.class);
		Select_SSC_Activity.this.startActivity(intent);
	}

	/**
	 * ????????????
	 */
	private void winDetail() {
		Intent intent = new Intent(Select_SSC_Activity.this,
				WinLotteryInfoActivity.class);
		intent.putExtra("lotteryId", AppTools.lottery.getLotteryID());
		startActivity(intent);
	}

	/**
	 * ????????????
	 */
	public void vibrator() {
		if (null != vibrator)
			vibrator.vibrate(300);
	}

	/**
	 * ?????? ????????????
	 */

	public void selectRandom() {
		switch (itemIndex) {
		case 0:
			mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
			vibrator();
			break;
		case 1:
			mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterTwo.setOneSet(NumberTools.getRandomNum(1, 9));
			vibrator();
			break;
		case 3:
			mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterTwo.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterThree.setOneSet(NumberTools.getRandomNum(1, 9));
			vibrator();
			break;
		case 6:
		case 7:// ???????????????????????????
			mAdapterOne.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterTwo.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterThree.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterFour.setOneSet(NumberTools.getRandomNum(1, 9));
			mAdapterFive.setOneSet(NumberTools.getRandomNum(1, 9));
			vibrator();
			break;
		default:
			break;
		}
		update();
		setTotalCount();
		getJiangjin();
	}

	/**
	 * ??????Adapter
	 */
	private void update() {
		if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree)
			return;
		mAdapterOne.notifyDataSetChanged();
		mAdapterTwo.notifyDataSetChanged();
		mAdapterThree.notifyDataSetChanged();
		mAdapterFour.notifyDataSetChanged();
		mAdapterFive.notifyDataSetChanged();
		mAdapterSix.notifyDataSetChanged();
		mAdapterSeven.notifyDataSetChanged();
		Log.i("x", "?????????update");
	}

	/**
	 * ????????????
	 */
	private void submitNumber() {
		if (AppTools.totalCount == 0) {
			MyToast.getToast(Select_SSC_Activity.this, "?????????????????????");
		} else if (AppTools.totalCount > 10000) {
			MyToast.getToast(Select_SSC_Activity.this, "????????????????????????20000");
		} else {
			Intent intent = new Intent(Select_SSC_Activity.this,
					Bet_SSC_Activity.class);
			Bundle bundle = new Bundle();
			bundle.putString("one", AppTools.sortSet(mAdapterOne.getOneSet())
					.toString().replace("[", "").replace("]", ""));
			bundle.putString("two", AppTools.sortSet(mAdapterTwo.getOneSet())
					.toString().replace("[", "").replace("]", ""));
			bundle.putString("three",
					AppTools.sortSet(mAdapterThree.getOneSet()).toString()
							.replace("[", "").replace("]", ""));
			bundle.putString("four", AppTools.sortSet(mAdapterFour.getOneSet())
					.toString().replace("[", "").replace("]", ""));
			bundle.putString("five", AppTools.sortSet(mAdapterFive.getOneSet())
					.toString().replace("[", "").replace("]", ""));

			String d = "";
			for (Integer i : mAdapterSix.getIndexSet()) {
				d += dxds[i];
			}
			String d2 = "";
			for (Integer i : mAdapterSeven.getIndexSet()) {
				d2 += dxds[i];
			}
			d += "," + d2;
			bundle.putString("dxds", d);

			bundle.putInt("type", itemIndex + 1);
			bundle.putInt("playType", playType);
			intent.putExtra("bundle", bundle);
			Select_SSC_Activity.this.startActivity(intent);
		}
	}

	/**
	 * ?????? ??????
	 */
	private void clear() {
		if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree
				|| null == mAdapterFour || null == mAdapterFive
				|| null == mAdapterSix || null == mAdapterSeven) {
			return;
		}
		mAdapterOne.clear();
		mAdapterTwo.clear();
		mAdapterThree.clear();
		mAdapterFour.clear();
		mAdapterFive.clear();
		mAdapterSix.clear();
		mAdapterSeven.clear();

		mAdapterOne.notifyDataSetChanged();
		mAdapterTwo.notifyDataSetChanged();
		mAdapterThree.notifyDataSetChanged();
		mAdapterFour.notifyDataSetChanged();
		mAdapterFive.notifyDataSetChanged();
		mAdapterSix.notifyDataSetChanged();
		mAdapterSeven.notifyDataSetChanged();

		AppTools.totalCount = 0;
		ll_jiangjin.setVisibility(View.GONE);
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * ???????????????????????????????????????
	 */
	public void register() {
		getItem();
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
		vibrator = VibratorView.getVibrator(getApplicationContext());
	}

	public void unregister() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/**
	 * ??????????????? ??? ?????????
	 */
	@Override
	protected void onResume() {
		super.onResume();
		register();
	}

	/**
	 * ??????????????? ??? ?????????
	 */
	@Override
	protected void onStop() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/**
	 * ??????????????? ????????????
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * ???????????? ?????????????????????
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {

		// ??????????????????
		long currentUpdateTime = System.currentTimeMillis();
		if (vTime == 0) {
			vTime = currentUpdateTime;
		}
		// ???????????????????????????
		long timeInterval = currentUpdateTime - btime;
		// ???????????????????????????????????????
		if (timeInterval < 150)
			return;
		// ?????????????????????last??????
		btime = currentUpdateTime;
		// ??????x,y,z??????
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// ??????x,y,z????????????
		float deltaX = x - bx;
		float deltaY = y - by;
		float deltaZ = z - bz;
		// ????????????????????????last??????
		bx = x;
		by = y;
		bz = z;
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		// ?????????????????????????????????
		if (speed >= 500 && currentUpdateTime - vTime > 700) {
			vTime = System.currentTimeMillis();
			selectRandom();
		}
	}

	/**
	 * ?????????????????????
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exit();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ?????????????????????????????????
	 */
	private void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {

			if (mAdapterOne.getOneSetSize() != 0
					|| mAdapterTwo.getOneSetSize() != 0
					|| mAdapterThree.getOneSetSize() != 0
					|| mAdapterFour.getOneSetSize() != 0
					|| mAdapterFive.getOneSetSize() != 0
					|| mAdapterSix.getIndexSetSize() != 0
					|| mAdapterSeven.getIndexSetSize() != 0) {
				dialog.show();
				dialog.setDialogContent("??????????????????????????????,?????????????????????????????????");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// ??????
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
			if (mAdapterOne.getOneSetSize() != 0
					|| mAdapterTwo.getOneSetSize() != 0
					|| mAdapterThree.getOneSetSize() != 0
					|| mAdapterFour.getOneSetSize() != 0
					|| mAdapterFive.getOneSetSize() != 0
					|| mAdapterSix.getIndexSetSize() != 0
					|| mAdapterSeven.getIndexSetSize() != 0) {
				dialog.show();
				dialog.setDialogContent("??????????????????????????????,?????????????????????????????????");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// ??????
							clear();
							Intent intent = new Intent(
									Select_SSC_Activity.this,
									Bet_SSC_Activity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("playType", playType);
							bundle.putInt("btnIndex", itemIndex + 1);
							intent.putExtra("bundle1", bundle);
							Select_SSC_Activity.this.startActivity(intent);
						}
					}
				});
			} else {
				clear();
				Intent intent = new Intent(Select_SSC_Activity.this,
						Bet_SSC_Activity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("playType", playType);
				bundle.putInt("btnIndex", itemIndex + 1);
				intent.putExtra("bundle1", bundle);
				Select_SSC_Activity.this.startActivity(intent);
			}
		}
	}

	/**
	 * ??????Adapter
	 */
	public void updateAdapter() {
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * ?????????????????????????????????
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler2 extends Handler {
		@Override
		public void handleMessage(Message msg) {
			k3_win_listView.setVisibility(View.VISIBLE);
			k3_layout_tile.setVisibility(View.VISIBLE);

			switch (msg.what) {
			case 11:
				adapter_win.notifyDataSetChanged();
				break;

			case -500:
				MyToast.getToast(getApplicationContext(), "??????????????????????????????");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * ??????????????????????????????
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (playType == 2815 || itemIndex == 10) {
					mAdapterOne.setNumbers(numbersZSHZ);
				} else {
					mAdapterOne.setNumbers(numbers);
				}
				gv_one.setAdapter(mAdapterOne);
				gv_two.setAdapter(mAdapterTwo);
				gv_three.setAdapter(mAdapterThree);
				gv_four.setAdapter(mAdapterFour);
				gv_five.setAdapter(mAdapterFive);
				gv_six.setAdapter(mAdapterSix);
				gv_seven.setAdapter(mAdapterSeven);

				mAdapterOne.notifyDataSetChanged();
				mAdapterTwo.notifyDataSetChanged();
				mAdapterThree.notifyDataSetChanged();
				mAdapterFour.notifyDataSetChanged();
				mAdapterFive.notifyDataSetChanged();
				mAdapterSix.notifyDataSetChanged();
				mAdapterSeven.notifyDataSetChanged();
				break;

			case 0:
				index = 1;
				AppTools.isCanBet = true;
				for (Lottery ll : HallFragment.listLottery) {
					if (ll.getLotteryID().equals("62")
							|| ll.getLotteryID().equals("70")) {
						AppTools.lottery = ll;
					}
				}
				break;
			case -500:
				MyToast.getToast(getApplicationContext(), "??????????????????????????????");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * ??????activity
	 */
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
