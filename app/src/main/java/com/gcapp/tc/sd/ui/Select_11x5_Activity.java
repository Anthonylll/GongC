package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.WinInfo;
import com.gcapp.tc.sd.ui.adapter.GridView11X5Adapter;
import com.gcapp.tc.sd.ui.adapter.WinInfoAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.JiangjinTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
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
 * ????????? 11???5??????????????????????????????
 */
public class Select_11x5_Activity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "Select_11X5Activity";
	private Context context = Select_11x5_Activity.this;
	/* ?????? */
	private RelativeLayout layout_top_select;// ????????????
	private ImageButton btn_back; // ??????
	private LinearLayout layout_select_playtype;// ????????????
	private ImageView iv_up_down;// ??????????????????
	private TextView btn_playtype;// ??????
	private ImageButton btn_help;// ??????
	private ConfirmDialog dialog;// ?????????
	private Animation animation = null;
	private boolean visibleGone = true;
	private View view;
	private PopupWindow pop;
	private Button btn_playHelp, btn_winNumber, btn_forgetNum;// ?????????????????????????????????????????????

	/* ?????? */
	private TextView btn_clearall; // ????????????
	private TextView btn_submit; // ?????????
	public TextView tv_tatol_count;// ?????????
	public TextView tv_tatol_money;// ?????????
	private RelativeLayout rl_one, rl_two, rl_three;
	private MyGridView gv_one, gv_two, gv_three;
	private GridView11X5Adapter mAdapterOne, mAdapterTwo, mAdapterThree;
	private TextView tv_show1, tv_show2, tv_show3;
	private String[] numbers = new String[] { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11" };
	/* ???????????? */
	private int playType = 6202;
	private int play, play2;
	private LinearLayout layout_shake;// ?????????
	private ImageView iv_shake;// ?????????
	private TextView tv_shake;// ?????????
	private PopupWindow popWindow;

	private ArrayList<String> list_yilou1 = new ArrayList<String>();
	private ArrayList<String> list_yilou2 = new ArrayList<String>();
	private ArrayList<String> list_yilou3 = new ArrayList<String>();

	private Bundle bundle;
	public Vibrator vibrator; // ?????????
	private SensorManager mSmanager; // ?????????
	private int type = 1;
	private CustomDigitalClock custTime;// ??????????????????
	private TextView tv_lotteryStoptime;// ???????????????
	private int index = 1;

	/**
	 * ?????????
	 */
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

	private Map<Integer, Integer> playtypeMap;
	private Map<Integer, Integer> playtypeMapDan;

	private String[] normal_play = { "?????????", "?????????", "?????????", "?????????", "?????????", "?????????",
			"?????????", "??????", "????????????", "????????????", "????????????", "????????????" };
	private int[] ids_play = { 2, 3, 4, 5, 6, 7, 8, 1, 9, 11, 10, 12 };

	private String[] normal_play_new = { "?????????", "?????????", "?????????", "?????????", "?????????",
			"?????????", "?????????", "??????", "????????????", "????????????", "????????????", "????????????", "?????????", "?????????",
			"?????????", "?????????" };

	private int[] ids_play_new = { 2, 3, 4, 5, 6, 7, 8, 1, 9, 11, 10, 12, 22,
			23, 24, 25 };

	private String[] dan_play = { "?????????", "?????????", "?????????", "?????????", "?????????", "?????????","??????", "??????", "" };
	private int[] ids_play_dan = { 15, 16, 17, 18, 19, 20, 13, 14 };

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
	private TextView tv_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ???????????????
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_11x5);
		App.activityS.add(this);
		App.activityS1.add(this);

		if ("70".equals(AppTools.lottery.getLotteryID())) {
			playType = 7002;
		} else if ("62".equals(AppTools.lottery.getLotteryID())) {
			playType = 6202;
		} else if ("78".equals(AppTools.lottery.getLotteryID())) {
			playType = 7802;
		}
		getYilouData();
		findView();
		init();
		setListener();
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
	 * ?????????UI??????
	 */
	private void findView() {
		bundle = new Bundle();
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_status.setVisibility(View.INVISIBLE);
		k3_win_listView = (ListView) findViewById(R.id.k3_win_listView);// ?????????5??????????????????
		k3_layout_tile = (LinearLayout) findViewById(R.id.k3_layout_tile);// ?????????5??????????????????
		adapter_win = new WinInfoAdapter(list_wininfo,
				Select_11x5_Activity.this);
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

		// ?????????
		rl_one = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlOne);
		rl_two = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlTwo);
		rl_three = (RelativeLayout) this
				.findViewById(R.id.number_sv_center_rlThree);
		// ???????????????
		tv_show1 = (TextView) this.findViewById(R.id.tv_show);
		tv_show2 = (TextView) this.findViewById(R.id.tv_show2);
		tv_show3 = (TextView) this.findViewById(R.id.tv_show3);
		// ??????gridview
		gv_one = (MyGridView) this
				.findViewById(R.id.number_sv_center_gv_showOne);
		gv_two = (MyGridView) this
				.findViewById(R.id.number_sv_center_gv_showTwo);
		gv_three = (MyGridView) this
				.findViewById(R.id.number_sv_center_gv_showThree);
		// ??????adapter
		mAdapterOne = new GridView11X5Adapter(context, numbers, true,
				visibleGone, list_yilou1);
		mAdapterTwo = new GridView11X5Adapter(context, numbers, true,
				visibleGone, list_yilou2);
		mAdapterThree = new GridView11X5Adapter(context, numbers, true,
				visibleGone, list_yilou3);
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

				if (null != isusesInfo) {
					JSONArray array;
					JSONObject item;
					try {
						array = new JSONArray(isusesInfo.getString("miss"));
						item = array.getJSONObject(0);
						if (list_yilou3.size() > 0 || list_yilou2.size() > 0
								|| list_yilou1.size() > 0) {
							list_yilou3.clear();
							list_yilou2.clear();
							list_yilou1.clear();
						}
						String str3 = "";
						String str2 = "";
						String str1 = "";
						int playmark = playType % 100;
						if (playmark == 2 || playmark == 3 || playmark == 4
								|| playmark == 5 || playmark == 6
								|| playmark == 7 || playmark == 8
								|| playmark == 1 || playmark == 11
								|| playmark == 24 || playmark == 25
								|| playmark == 12) {
							str1 = item.optString("1");

						} else if (playmark == 9 || playmark == 22) {// ????????????
							str1 = item.optString("1");
							str2 = item.optString("2");

						} else if (playmark == 10 || playmark == 23) { // ????????????
							str1 = item.optString("1");
							str2 = item.optString("2");
							str3 = item.optString("3");
						} else if (playmark > 12 && playmark < 22) { // ????????????
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
						mAdapterOne.notifyDataSetChanged();
						mAdapterTwo.notifyDataSetChanged();
						mAdapterThree.notifyDataSetChanged();
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if (isusesInfo.toString().equals("-500")) {
						MyToast.getToast(Select_11x5_Activity.this, "????????????");
					}
				} else {
					MyToast.getToast(context, "?????????????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_11x5_Activity.this, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		int playmark = playType % 100;
		int id = playType / 100;
		int playType_temp = playType;
		if (playmark == 22) {
			playType_temp = id * 100 + 9;
		} else if (playmark == 23) {
			playType_temp = id * 100 + 10;
		} else if (playmark == 24) {
			playType_temp = id * 100 + 4;
		} else if (playmark == 25) {
			playType_temp = id * 100 + 5;
		}
		requestUtil.commit_yilou(playType_temp);
	}

	/**
	 * ?????????????????????
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAdapterOne.notifyDataSetChanged();
				mAdapterTwo.notifyDataSetChanged();
				mAdapterThree.notifyDataSetChanged();
				break;
			case -500:
				MyToast.getToast(getApplicationContext(), "??????????????????????????????");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * ??????????????? ??????????????????
	 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (NetWork.isConnect(context)) {
		} else {
			Toast.makeText(context, "??????????????????????????????????????????", Toast.LENGTH_SHORT);
		}
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
		if ("70".equals(AppTools.lottery.getLotteryID())) {
			playType = 7002;
		} else if ("62".equals(AppTools.lottery.getLotteryID())) {
			playType = 6202;
		} else if ("78".equals(AppTools.lottery.getLotteryID())) {
			playType = 7802;
		}
		play2 = Integer.parseInt(AppTools.lottery.getLotteryID()) * 100;
		play = playType - Integer.parseInt(AppTools.lottery.getLotteryID())
				* 100;/**/
		initData();
		String tip = "";
		if (0 == parentIndex) {// ??????
			tip = "??????-";
		} else if (1 == parentIndex) {// ??????
			tip = "??????-";
		}
		btn_playtype.setText(tip + data.get(parentIndex).get(itemIndex));
		changePlayType();
		dialog = new ConfirmDialog(this, R.style.dialog);
		if (play == 2) {
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
		}
	}

	/**
	 * ?????????????????????
	 */
	private void initData() {
		Map<Integer, String> playType = new HashMap<Integer, String>();
		if ("70".equals(AppTools.lottery.getLotteryID())) {
			for (int i = 0; i < normal_play_new.length; i++) {
				playType.put(i, normal_play_new[i]);
			}
		} else {
			for (int i = 0; i < normal_play.length; i++) {
				playType.put(i, normal_play[i]);
			}
		}
		Set<Integer> set = playType.keySet();
		playtypeMap = new HashMap<Integer, Integer>();
		if ("70".equals(AppTools.lottery.getLotteryID())) {
			for (Integer i : set) {
				playtypeMap.put(
						ids_play_new[i]
								+ Integer.valueOf(AppTools.lottery
										.getLotteryID()) * 100, i);
			}
		} else {
			for (Integer i : set) {
				playtypeMap.put(
						ids_play[i]
								+ Integer.valueOf(AppTools.lottery
										.getLotteryID()) * 100, i);
			}
		}
		data.put(0, playType);
		Map<Integer, String> playType_dan = new HashMap<Integer, String>();
		for (int i = 0; i < dan_play.length; i++) {
			playType_dan.put(i, dan_play[i]);
		}
		Set<Integer> setDan = playType_dan.keySet();
		playtypeMapDan = new HashMap<Integer, Integer>();
		for (Integer i : setDan) {
			if (i <= 7) {
				playtypeMapDan.put(
						ids_play_dan[i]
								+ Integer.valueOf(AppTools.lottery
										.getLotteryID()) * 100, i);
			}
		}
		data.put(1, playType_dan);
	}

	/**
	 * ????????????????????????
	 */
	public void getWinnumberData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"????????????...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "11???5???????????????????????????==???" + isusesInfo);
				try {
					// JSONArray array = new JSONArray(
					// isusesInfo.getString("OpenInfo"));
					JSONArray array = isusesInfo.getJSONArray("OpenInfo");

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
					MyToast.getToast(context, "????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		requestUtil.commit_winNumber((playType / 100) + "");
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
	 * ????????????
	 */
	private void setListener() {
		// ??????Adapter
		gv_one.setAdapter(mAdapterOne);
		gv_two.setAdapter(mAdapterTwo);
		gv_three.setAdapter(mAdapterThree);

		sv_show_ball.setOnTouchListener(new MyOntouchListener());
		gv_one.setOnTouchListener(new MyOntouchListener());
		gv_two.setOnTouchListener(new MyOntouchListener());
		gv_three.setOnTouchListener(new MyOntouchListener());

		gv_one.setOnItemClickListener(new MyItemClickListener_1());
		gv_two.setOnItemClickListener(new MyItemClickListener_2());
		gv_three.setOnItemClickListener(new MyItemClickListener_3());
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
					RequestUtil requestUtil = new RequestUtil(context, false, 0) {
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
	}

	/**
	 * ???????????????????????????5??????????????????
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
	 * GridView?????????????????????1
	 * 
	 * @author lenovo
	 * 
	 */
	class MyItemClickListener_1 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			if (null != vibrator)
				vibrator.vibrate(150);
			String str = (position + 1) + "";
			if (position < 9) {
				str = "0" + (position + 1);
			}
			if (mAdapterOne.getOneSet().contains(str)) {
				mAdapterOne.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_11x5_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (play > 12 && play < 15) {
					if (play - mAdapterOne.getOneSetSize() <= 12) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????"
								+ (play - 12) + "???");
						return;
					}
				}
				if (play > 14) {
					if (play - mAdapterOne.getOneSetSize() <= 14) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????"
								+ (play - 14) + "???");
						return;
					}
				}

				if (play == 8) {// ?????????????????????????????????????????????????????????????????????
					if (mAdapterOne.getOneSetSize() >= 8) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????1???");
						return;
					}
				}

				if (play == 22 || play == 23 || play == 9 || play == 10) {
					if (mAdapterOne.getOneSetSize() >= 1) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????1???");
						return;
					}
				}

				if (play > 23) {
					if (play - mAdapterOne.getOneSetSize() <= 20) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????"
								+ (play - 20) + "???");
						return;
					}
				}

				if (play > 12) {
					mAdapterTwo.removeOne(str);
					mAdapterTwo.notifyDataSetChanged();
					mAdapterOne.addOne(str);
					tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
					tv.setTextColor(Color.WHITE);
				}
				if (play == 10 || play == 9 || play == 22 || play == 23) {
					addMultx(str, mAdapterOne, mAdapterTwo, mAdapterThree);
				} else {
					mAdapterOne.addOne(str);
					tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
					tv.setTextColor(Color.WHITE);
				}
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * GridView?????????????????????2
	 * 
	 * @author lenovo
	 * 
	 */

	class MyItemClickListener_2 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			if (null != vibrator)
				vibrator.vibrate(150);
			String str = (position + 1) + "";
			if (position < 9) {
				str = "0" + (position + 1);
			}
			if (mAdapterTwo.getOneSet().contains(str)) {
				mAdapterTwo.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_11x5_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (play == 22 || play == 23 || play == 9 || play == 10) {
					if (mAdapterTwo.getOneSetSize() >= 1) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????1???");
						return;
					}
				}
				if (play > 12) {
					mAdapterOne.removeOne(str);
					mAdapterOne.notifyDataSetChanged();
					mAdapterTwo.addOne(str);
					tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
					tv.setTextColor(Color.WHITE);
				}
				if (play == 10 || play == 9 || play == 22 || play == 23) {
					addMultx(str, mAdapterTwo, mAdapterOne, mAdapterThree);
				} else {
					mAdapterTwo.addOne(str);
					tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
					tv.setTextColor(Color.WHITE);
				}
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * GridView?????????????????????3
	 * 
	 * @author lenovo
	 * 
	 */

	class MyItemClickListener_3 implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView tv = (TextView) view.findViewById(R.id.btn_showNum);
			if (null != vibrator)
				vibrator.vibrate(150);
			String str = (position + 1) + "";
			if (position < 9) {
				str = "0" + (position + 1);
			}
			if (mAdapterThree.getOneSet().contains(str)) {
				mAdapterThree.removeOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_unselected);
				tv.setTextColor(Select_11x5_Activity.this.getResources()
						.getColor(R.color.red));
			} else {
				if (play == 9 || play == 10) {
					if (mAdapterThree.getOneSetSize() >= 1) {
						MyToast.getToast(Select_11x5_Activity.this, "???????????????1???");
						return;
					}
				}
			}
			if (play == 10 || play == 9) {
				addMultx(str, mAdapterThree, mAdapterOne, mAdapterTwo);
			} else {
				mAdapterThree.addOne(str);
				tv.setBackgroundResource(R.drawable.icon_ball_red_selected);
				tv.setTextColor(Color.WHITE);
			}
			setTotalCount();
			getJiangjin();
		}
	}

	/**
	 * ??????????????????
	 */
	private void addMultx(String number, GridView11X5Adapter... x5Adapters) {
		for (int i = 0; i < x5Adapters.length; i++) {
			if (i == 0) {
				x5Adapters[i].addOne(number);
				x5Adapters[i].notifyDataSetChanged();
			} else {
				x5Adapters[i].removeOne(number);
				x5Adapters[i].notifyDataSetChanged();
			}
		}
	}

	/**
	 * ????????????
	 */
	private void setTotalCount() {

		switch (play) {
		case 1:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 1);
			break;
		case 2:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 2);
			break;
		// ????????????
		case 22:
		case 23:
		case 24:
		case 25:
			AppTools.totalCount = NumberTools.get11X5Count_lexuan(
					mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(),
					mAdapterThree.getOneSetSize(), play);
			break;
		case 3:
		case 12:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 3);
			break;
		case 4:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 4);
			break;
		case 5:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 5);
			break;
		case 6:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 6);
			break;
		case 7:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 7);
			break;
		case 8:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 8);
			break;
		case 9:
			AppTools.totalCount = NumberTools.get11X5zuer(
					mAdapterOne.getOneSet(), mAdapterTwo.getOneSet());
			break;
		case 10:
			AppTools.totalCount = NumberTools.get11X5zusan(
					mAdapterOne.getOneSet(), mAdapterTwo.getOneSet(),
					mAdapterThree.getOneSet());
			break;
		case 13:
		case 15:
			if (mAdapterOne.getOneSetSize() == 1)
				AppTools.totalCount = mAdapterTwo.getOneSetSize();
			else
				AppTools.totalCount = 0;
			break;
		case 11:
			AppTools.totalCount = NumberTools.get11X5Count(
					mAdapterOne.getOneSetSize(), 2);
			break;
		// ??????????????????
		case 14:
		case 16:
			AppTools.totalCount = NumberTools
					.get11X5Count_dan(mAdapterOne.getOneSetSize(),
							mAdapterTwo.getOneSetSize(), 3);
			break;
		case 17:
			AppTools.totalCount = NumberTools
					.get11X5Count_dan(mAdapterOne.getOneSetSize(),
							mAdapterTwo.getOneSetSize(), 4);
			break;
		case 18:
			AppTools.totalCount = NumberTools
					.get11X5Count_dan(mAdapterOne.getOneSetSize(),
							mAdapterTwo.getOneSetSize(), 5);
			break;
		case 19:
			AppTools.totalCount = NumberTools
					.get11X5Count_dan(mAdapterOne.getOneSetSize(),
							mAdapterTwo.getOneSetSize(), 6);
			break;
		case 20:
			AppTools.totalCount = NumberTools
					.get11X5Count_dan(mAdapterOne.getOneSetSize(),
							mAdapterTwo.getOneSetSize(), 7);
			break;
		case 21:
			AppTools.totalCount = NumberTools
					.get11X5Count_dan(mAdapterOne.getOneSetSize(),
							mAdapterTwo.getOneSetSize(), 8);
			break;
		}

		if (play > 12) {
			if (AppTools.totalCount == 1) {
				AppTools.totalCount = 0;
			}
		}

		boolean flag = false;
		for (int i = 0; i < ids_play_dan.length; i++) {
			if (play == ids_play_dan[i]) {
				flag = true;
			}
		}
		if (flag && AppTools.totalCount == 1) {
			tv_tatol_count.setText(0 + "");
			tv_tatol_money.setText(0 + "");
		} else {
			tv_tatol_count.setText(+AppTools.totalCount + "");
			tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		}
	}

	/**
	 * ??????????????????
	 */
	private void getJiangjin() {

		switch (play) {
		// ????????????
		case 22:
		case 23:
		case 24:
		case 25:
			max_jiangjin = JiangjinTools.get11X5_Jiangjin("max", 0, 0, play);
			min_jiangjin = JiangjinTools.get11X5_Jiangjin("min", 0, 0, play);
			break;

		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			max_jiangjin = JiangjinTools.get11X5_Jiangjin("max",
					mAdapterOne.getOneSetSize(), 0, play);
			min_jiangjin = JiangjinTools.get11X5_Jiangjin("min",
					mAdapterOne.getOneSetSize(), 0, play);
			break;

		case 9:// ??????????????????????????????
		case 10:
		case 11:
		case 12:
			max_jiangjin = JiangjinTools.get11X5_Jiangjin("max", 0, 0, play);
			min_jiangjin = JiangjinTools.get11X5_Jiangjin("min", 0, 0, play);
			break;

		case 15:// ????????????
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 13:// ????????????
		case 14:
			max_jiangjin = JiangjinTools.get11X5_Jiangjin("max",
					mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(),
					play);
			min_jiangjin = JiangjinTools.get11X5_Jiangjin("min",
					mAdapterOne.getOneSetSize(), mAdapterTwo.getOneSetSize(),
					play);
			break;
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
			LotteryUtils.backgroundAlpaha(Select_11x5_Activity.this, 0.5f);
			break;

		/** ?????? **/
		case R.id.layout_shake:
		case R.id.iv_shake:
		case R.id.tv_shake:
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
					if (parentIndex == 0
							|| (parentIndex == 1 && itemIndex <= 8)) {
						if (Select_11x5_Activity.this.parentIndex != parentIndex
								|| (Select_11x5_Activity.this.parentIndex == parentIndex && Select_11x5_Activity.this.itemIndex != itemIndex)) {
							Select_11x5_Activity.this.parentIndex = parentIndex;
							Select_11x5_Activity.this.itemIndex = itemIndex;
							k3_win_listView.setVisibility(View.GONE);
							k3_layout_tile.setVisibility(View.GONE);
							changePlayType();
							clear();
							getYilouData();
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
	 * ????????????????????????????????????????????????PopWindow
	 */
	private void createPopWindow() {
		LayoutInflater inflact = LayoutInflater.from(Select_11x5_Activity.this);
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
				LotteryUtils.backgroundAlpaha(Select_11x5_Activity.this, 1.0f);
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
						LotteryUtils.backgroundAlpaha(
								Select_11x5_Activity.this, 1.0f);
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
				LotteryUtils.backgroundAlpaha(Select_11x5_Activity.this, 1.0f);
			}
		});
		btn_winNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				winDetail();
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(Select_11x5_Activity.this, 1.0f);
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

					mAdapterOne.notifyDataSetChanged();
					mAdapterTwo.notifyDataSetChanged();
					mAdapterThree.notifyDataSetChanged();
					return;

				}
				if (!visibleGone) {
					visibleGone = true;
					mAdapterOne.setVisibles(visibleGone);
					mAdapterTwo.setVisibles(visibleGone);
					mAdapterThree.setVisibles(visibleGone);
					mAdapterOne.notifyDataSetChanged();
					mAdapterTwo.notifyDataSetChanged();
					mAdapterThree.notifyDataSetChanged();
					return;
				}
				popWindow.dismiss();
				LotteryUtils.backgroundAlpaha(Select_11x5_Activity.this, 1.0f);
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
	 * ???????????????????????????
	 */
	public void changePlayType() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
		vibrator = VibratorView.getVibrator(getApplicationContext());
		String tip = "";
		if (0 == parentIndex) {// ??????
			tip = "??????-";
		} else if (1 == parentIndex) {// ??????
			tip = "??????-";
		}
		btn_playtype.setText(tip + data.get(parentIndex).get(itemIndex));
		switch (parentIndex) {
		case 0:// ????????????
			switch (itemIndex) {
			case 0:// ????????????
				if (play2 + 2 != playType)
					clear();
				playType = play2 + 2;
				showGridView();
				break;
			case 1:// ????????????
				if (play2 + 3 != playType)
					clear();
				playType = play2 + 3;
				showGridView();
				break;
			case 2:// ????????????
				if (play2 + 4 != playType)
					clear();
				playType = play2 + 4;
				showGridView();
				break;
			case 3:// ????????????
				if (play2 + 5 != playType)
					clear();
				playType = play2 + 5;
				showGridView();
				break;
			case 4:// ????????????
				if (play2 + 6 != playType)
					clear();
				playType = play2 + 6;
				showGridView();
				break;
			case 5:// ????????????
				if (play2 + 7 != playType)
					clear();
				playType = play2 + 7;
				showGridView();
				break;
			case 6:// ????????????
				if (play2 + 8 != playType)
					clear();
				playType = play2 + 8;
				showGridView();
				break;
			case 7:// ????????????
				if (play2 + 1 != playType)
					clear();
				playType = play2 + 1;
				showGridView();
				break;
			case 8:// ????????????
				if (play2 + 9 != playType)
					clear();
				playType = play2 + 9;
				showGridView();
				break;
			case 9:// ????????????
				if (play2 + 11 != playType)
					clear();
				playType = play2 + 11;
				showGridView();
				break;
			case 10:// ????????????
				if (play2 + 10 != playType)
					clear();
				playType = play2 + 10;
				showGridView();
				break;
			case 11:// ????????????
				if (play2 + 12 != playType)
					clear();
				playType = play2 + 12;
				showGridView();
				break;

			case 12:// ?????????
				if (play2 + 22 != playType)
					clear();
				playType = play2 + 22;
				showGridView();
				break;

			case 13:// ?????????
				if (play2 + 23 != playType)
					clear();
				playType = play2 + 23;
				showGridView();
				break;

			case 14:// ?????????
				if (play2 + 24 != playType)
					clear();
				playType = play2 + 24;
				showGridView();
				break;

			case 15:// ?????????
				if (play2 + 25 != playType)
					clear();
				playType = play2 + 25;
				showGridView();
				break;
			}
			break;
		case 1:// ????????????
			switch (itemIndex) {
			case 0:// ????????????
				if (play2 + 15 != playType)
					clear();
				playType = play2 + 15;
				showGridView();
				break;
			case 1:// ????????????
				if (play2 + 16 != playType)
					clear();
				playType = play2 + 16;
				showGridView();
				break;
			case 2:// ????????????
				if (play2 + 17 != playType)
					clear();
				playType = play2 + 17;
				showGridView();
				break;
			case 3:// ????????????
				if (play2 + 18 != playType)
					clear();
				playType = play2 + 18;
				showGridView();
				break;

			case 4:// ????????????
				if (play2 + 19 != playType)
					clear();
				playType = play2 + 19;
				showGridView();
				break;
			case 5:// ????????????
				if (play2 + 20 != playType)
					clear();
				playType = play2 + 20;
				showGridView();
				break;
//			case 6:// ????????????
//				if (play2 + 21 != playType)
//					clear();
//				playType = play2 + 21;
//				showGridView();
//				break;
			case 6:// ????????????
				if (play2 + 13 != playType)
					clear();
				playType = play2 + 13;
				showGridView();
				break;
			case 7:// ????????????
				if (play2 + 14 != playType)
					clear();
				playType = play2 + 14;
				showGridView();
				break;
			}
			break;
		}
		updateAdapter();
		sv_show_ball.scrollTo(0, 0);
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

		Intent intent = Select_11x5_Activity.this.getIntent();
		Bundle bundle = intent.getBundleExtra("11X5Bundle");
		if (null != bundle) {
			playType = bundle.getInt("playtype");
			if (null != playtypeMap.get(playType)) {
				itemIndex = playtypeMap.get(playType);
				parentIndex = 0;
			}
			if (null != playtypeMapDan.get(playType)) {
				itemIndex = playtypeMapDan.get(playType);
				parentIndex = 1;
			}
			changePlayType();
			ArrayList<String> list = bundle.getStringArrayList("oneSet");
			mAdapterOne.setOneSet(list);

			ArrayList<String> list2 = bundle.getStringArrayList("twoSet");
			mAdapterTwo.setOneSet(list2);

			ArrayList<String> list3 = bundle.getStringArrayList("threeSet");
			mAdapterThree.setOneSet(list3);
			mAdapterOne.notifyDataSetChanged();
			mAdapterTwo.notifyDataSetChanged();
			mAdapterThree.notifyDataSetChanged();
		}
	}

	/**
	 * ????????????????????????
	 */
	private void playExplain() {
		Intent intent = new Intent(context, PlayDescription.class);
		startActivity(intent);
	}

	/**
	 * ?????????????????????
	 */
	private void winDetail() {
		Intent intent = new Intent(context, WinLotteryInfoActivity.class);
		intent.putExtra("lotteryId", AppTools.lottery.getLotteryID());
		startActivity(intent);
	}

	/**
	 * ?????? ????????????
	 */
	public void selectRandom() {
		List<String> list = new ArrayList<String>();
		System.out.println("play == " + play);
		switch (play) {

		case 24:
			mAdapterOne.setOneSet(NumberTools.getRandomNum5(4, 11));
			System.out.println("mAdapterOne.getOneSet();"
					+ mAdapterOne.getOneSet());
			break;
		case 25:
			mAdapterOne.setOneSet(NumberTools.getRandomNum5(5, 11));
			System.out.println("mAdapterOne.getOneSet();"
					+ mAdapterOne.getOneSet());
			break;

		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			mAdapterOne.setOneSet(NumberTools.getRandomNum5(play, 11));
			System.out.println("mAdapterOne.getOneSet();"
					+ mAdapterOne.getOneSet());
			break;
		case 9:
		case 22:
			list = NumberTools.getRandomNum5(2, 11);
			mAdapterOne.clear();
			mAdapterOne.addOne(list.get(0));
			list.remove(0);
			mAdapterTwo.setOneSet(list);
			break;
		case 10:
		case 23:
			list = NumberTools.getRandomNum5(3, 11);
			mAdapterOne.clear();
			mAdapterOne.addOne(list.get(0));
			mAdapterTwo.clear();
			mAdapterTwo.addOne(list.get(1));
			mAdapterThree.clear();
			mAdapterThree.addOne(list.get(2));
			break;
		case 11:
			mAdapterOne.setOneSet(NumberTools.getRandomNum5(2, 11));
			break;
		case 12:
			mAdapterOne.setOneSet(NumberTools.getRandomNum5(3, 11));
			break;
		}
		update();
		setTotalCount();
		getJiangjin();
	}

	/**
	 * ??????????????????
	 */
	private void update() {
		if (null == mAdapterOne || null == mAdapterTwo || null == mAdapterThree)
			return;
		mAdapterOne.notifyDataSetChanged();
		mAdapterTwo.notifyDataSetChanged();
		mAdapterThree.notifyDataSetChanged();
	}

	/**
	 * ????????????
	 */
	private void submitNumber() {
		if (AppTools.totalCount == 0) {
			MyToast.getToast(Select_11x5_Activity.this, "?????????????????????");
		} else {
			boolean flag = false;
			for (int i = 0; i < ids_play_dan.length; i++) {
				if (play == ids_play_dan[i]) {
					flag = true;
				}
			}
			if (flag && AppTools.totalCount == 1) {
				return;
			}
			Intent intent = new Intent(Select_11x5_Activity.this,
					Bet_11x5_Activity.class);
			Bundle bundle = new Bundle();
			bundle.putString("one", AppTools.sortSet(mAdapterOne.getOneSet())
					.toString().replace("[", "").replace("]", ""));
			bundle.putString("two", AppTools.sortSet(mAdapterTwo.getOneSet())
					.toString().replace("[", "").replace("]", ""));
			bundle.putString("three",
					AppTools.sortSet(mAdapterThree.getOneSet()).toString()
							.replace("[", "").replace("]", ""));
			bundle.putInt("playType", playType);
			intent.putExtra("bundle", bundle);
			Select_11x5_Activity.this.startActivity(intent);
		}
	}

	/**
	 * ????????????
	 */
	private void clear() {
		updateAdapter();
		AppTools.totalCount = 0;
		ll_jiangjin.setVisibility(View.GONE);
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * ????????????????????????????????????
	 */
	public void register() {
		getItem();
		setTotalCount();
		getJiangjin();
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
		vibrator = VibratorView.getVibrator(getApplicationContext());
	}

	public void unregister() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
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
		if (speed >= 900 && currentUpdateTime - vTime > 700) {
			vTime = System.currentTimeMillis();
			if (play < 11) {
				if (null != vibrator)
					vibrator.vibrate(300);
			}
			clear();
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

	private void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {
			if (mAdapterOne.getOneSetSize() != 0
					|| mAdapterTwo.getOneSetSize() != 0
					|| mAdapterThree.getOneSetSize() != 0) {
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
					|| mAdapterThree.getOneSetSize() != 0) {
				dialog.show();
				dialog.setDialogContent("??????????????????????????????,?????????????????????????????????");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// ??????
							Select_11x5_Activity.this.startActivity(new Intent(
									Select_11x5_Activity.this,
									Bet_11x5_Activity.class));
						}
					}
				});
			} else {
				Select_11x5_Activity.this.startActivity(new Intent(
						Select_11x5_Activity.this, Bet_11x5_Activity.class));
			}
		}
	}

	/**
	 * ??????Adapter
	 */
	public void updateAdapter() {
		if (null != mAdapterOne) {
			mAdapterOne.clear();
			mAdapterOne.notifyDataSetChanged();
		}
		if (null != mAdapterTwo) {
			mAdapterTwo.clear();
			mAdapterTwo.notifyDataSetChanged();
		}
		if (null != mAdapterThree) {
			mAdapterThree.clear();
			mAdapterThree.notifyDataSetChanged();
		}
	}

	/**
	 * ??????GridView
	 */
	private void showGridView(int one, int two, int three) {
		updateAdapter();
		rl_one.setVisibility(one);
		rl_two.setVisibility(two);
		rl_three.setVisibility(three);
	}

	/**
	 * ??????GridView
	 */
	private void showGridView() {
		play = playType - Integer.parseInt(AppTools.lottery.getLotteryID())
				* 100;/**/
		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (play >= 1 && play <= 12) {
			SmanagerView.registerSensorManager(mSmanager,
					getApplicationContext(), this);// ???????????????
			vibrator = VibratorView.getVibrator(getApplicationContext());
			layout_shake.setVisibility(View.VISIBLE);
		}
		if (play > 12 && play <= 21) {
			vibrator = null;
			SmanagerView.unregisterSmanager(mSmanager, this);
			layout_shake.setVisibility(View.INVISIBLE);
		}
		switch (play) {
		case 1:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 2:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 3:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>3</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 4:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>4</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 5:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>5</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 6:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>6</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 7:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>7</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 8:// ??????-??????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>8</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 9:// ??????-?????????
			tv_show1.setText(Html.fromHtml("??????????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;
		case 10:// ??????-?????????
			tv_show1.setText(Html.fromHtml("??????????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE);
			break;
		case 11:// ??????-?????????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 12:// ??????-?????????
			tv_show1.setText(Html.fromHtml("???????????????"
					+ "<font color='#e3393c'>3</font>" + "?????????"));
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
		case 13:// ??????-????????????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));

			tv_show2.setVisibility(View.VISIBLE);
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));

			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;
		case 14:// ??????-????????????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));

			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;
		case 15:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ?????????"
					+ "<font color='#e3393c'>1</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 16:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 17:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>3</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 18:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>4</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 19:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>5</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 20:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>6</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;
		case 21:// ??????-??????
			tv_show1.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>1</font>" + "?????????,??????"
					+ "<font color='#e3393c'>7</font>" + "?????????"));
			tv_show2.setText(Html.fromHtml("?????? ???????????????"
					+ "<font color='#e3393c'>2</font>" + "?????????"));
			tv_show2.setVisibility(View.VISIBLE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 22:// ??????-?????????
			tv_show1.setText("????????????????????????");
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.VISIBLE, View.GONE);
			break;

		case 23:// ??????-?????????
			tv_show1.setText("????????????????????????");
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.VISIBLE, View.VISIBLE);
			break;

		case 24:// ??????-?????????
			tv_show1.setText("?????????????????????");
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;

		case 25:// ??????-?????????
			tv_show1.setText("?????????????????????");
			tv_show2.setVisibility(View.GONE);
			tv_show3.setVisibility(View.GONE);
			showGridView(View.VISIBLE, View.GONE, View.GONE);
			break;
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
