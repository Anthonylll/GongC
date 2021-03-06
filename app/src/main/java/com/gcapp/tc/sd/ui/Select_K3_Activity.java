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
 * ??????????????????3 ???????????????????????????
 * 
 * @author lenovo
 * 
 */
public class Select_K3_Activity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "Select_k3Activity";
	private Context context = Select_K3_Activity.this;
	/* ?????? */
	private RelativeLayout layout_top_select;// ????????????
	private ImageButton btn_back; // ??????
	private LinearLayout layout_select_playtype, ll_winnum;// ????????????
	private ImageView iv_up_down;// ??????????????????
	private ImageView iv_up_down2;// ?????????10???????????????
	private TextView btn_playtype;// ??????
	private ImageButton btn_help;// ??????
	private ConfirmDialog dialog;// ?????????
	private Animation animation = null;
	private LinearLayout ll_daxiao;// ?????????????????????
	private TextView tv_da, tv_xiao, tv_single, tv_shuang;
	private TextView tv_img1, tv_title1, tv_img2, tv_title2;
	private View view;
	private PopupWindow popWindow;
	private Button btn_playHelp, btn_winNumber, btn_forgetNum;// ?????????????????????????????????????????????
	private boolean visibleGone = true;
	/* ?????? */
	private TextView btn_clearall; // ????????????
	private TextView btn_submit; // ?????????
	public TextView tv_tatol_count;// ?????????
	public TextView tv_tatol_money;// ?????????
	private TextView tv_winnumber, tv_winnum1, tv_winnum2, tv_winnum3;// ??????????????????
	private LinearLayout layout_shake;// ?????????
	private ImageView iv_shake;// ?????????
	private LinearLayout layout_shake2;// ?????????
	private ImageView iv_shake2;// ?????????
	private static List<String> list_hezhi = new ArrayList<String>();
	private String win_number;// ??????
	private String win_lastQi;
	private TextView tv_top; // ?????????????????????
	private Bundle bundle;
	public Vibrator vibrator; // ?????????
	private SensorManager mSmanager; // ?????????
	private LinearLayout ll_hezhishow, ll_hezhishow2;
	private ArrayList<String> list_yilou1 = new ArrayList<String>();
	private ArrayList<String> list_yilou2 = new ArrayList<String>();
	private ArrayList<String> list_yilou3 = new ArrayList<String>();// ????????????????????????

	/** ????????? */
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// ??????????????????
	private long vTime = 0; // ???????????????

	private SharedPreferences settings;
	private Editor editor;
	private PopupWindowUtil_k3 popUtil;
	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
	private List<String> list_jiangjin = new ArrayList<String>();// ????????????????????????adapter??????
	private List<String> list_image = new ArrayList<String>();
	private int parentIndex = 0;
	private int itemIndex = 0;
	private Map<Integer, Integer> playtypeMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> playtypeMap_dan = new HashMap<Integer, Integer>();
	private List<String> list1 = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();
	private List<String> list_daxiao = new ArrayList<String>();
	private List<String> list_daxiao2 = new ArrayList<String>();
	private List<Jiang_k3_Info> list_wininfo = new ArrayList<Jiang_k3_Info>();// ???10??????????????????adapter
	private int playID = 8301;
	// ?????????????????????
	private Adpater adpater, adpater1, adpater2;
	private MyAdapter adpater_daxiao;
	private MywinNumAdapter adapter_win;// ???10????????????????????????
	private MyGridView gridView, gridView_ertonghaodan_1,
			gridView_ertonghaodan_2, gridView_daxiao;
	private TextView tv_title, tv_dan, tv_tuo;// ????????????gridView ?????????????????????
	private TextView tv_jiezhitime;
	private TextView tv_daxiao;
	// 1 ????????? 2 ?????????????????? 3 ???????????????
	private RelativeLayout relativeLayout1, relativeLayout2;
	private int index = 1; // ???????????????
	private CustomDigitalClock custTime;// ??????????????????
	private Spanned tip = null;
	private String opt, auth, info, time, imei; // ?????????????????????
	private ListView k3_win_listView;// ?????????3?????????10??? ???????????????
	private LinearLayout k3_layout_tile;// ?????????3?????????10??? ???????????????
	private LinearLayout img_line;
	private List<String> daxiaodanshuang = new ArrayList<String>();// ?????????????????????list
	// ????????????
	private LinearLayout ll_jiangjin;// ????????????
	private TextView tv_jingjin, tv_profits, tv_winOrLost;
	private long max_jiangjin, min_jiangjin;// ?????????????????????
	private long max_profits, min_profits;// ?????????????????????

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ???????????????
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_k3);
		App.activityS.add(this);
		App.activityS1.add(this);
		getYilouData();
		findView();
		init();
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
			tv_jiezhitime.setText("?????????????????????: ");
		} else if (custTime.getType() == 3) {
			tv_jiezhitime.setText("???" + AppTools.lottery.getIsuseName().trim()
					+ "?????????:");
		}
	}

	/**
	 * ?????????UI
	 */
	private void findView() {
		MyGridViewAdapter.playType = 501;
		bundle = new Bundle();
		img_line = (LinearLayout) findViewById(R.id.img_line);// ????????????
		ll_jiangjin = (LinearLayout) findViewById(R.id.ll_jiangjin);// ????????????
		ll_jiangjin.setVisibility(View.GONE);
		tv_jingjin = (TextView) findViewById(R.id.tv_jingjin);// ??????
		tv_profits = (TextView) findViewById(R.id.tv_profits);// ??????
		tv_winOrLost = (TextView) findViewById(R.id.tv_winOrLost);
		ll_daxiao = (LinearLayout) findViewById(R.id.ll_daxiao);// ??????????????????
		ll_daxiao.setVisibility(View.VISIBLE);// ??????????????????
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
		ll_winnum = (LinearLayout) findViewById(R.id.ll_winnum);// ??????????????????
		custTime = (CustomDigitalClock) this
				.findViewById(R.id.select_ks_endtime);
		tv_jiezhitime = (TextView) this.findViewById(R.id.tv_jiezhitime);
		getCustomTime();
		k3_win_listView = (ListView) findViewById(R.id.k3_win_listView);// ???10??????????????????
		k3_layout_tile = (LinearLayout) findViewById(R.id.k3_layout_tile);// ???10??????????????????
		adapter_win = new MywinNumAdapter(list_wininfo, Select_K3_Activity.this);
		k3_win_listView.setAdapter(adapter_win);
		tv_daxiao = (TextView) findViewById(R.id.tv_daxiao);// ??????????????????????????????
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

		// ??????
		tv_title = (TextView) this.findViewById(R.id.textView2);
		tv_dan = (TextView) this.findViewById(R.id.textView5);
		tv_tuo = (TextView) this.findViewById(R.id.textView6);
		// ??????
		relativeLayout1 = (RelativeLayout) this
				.findViewById(R.id.relativeLayout_hezhi);
		relativeLayout2 = (RelativeLayout) this
				.findViewById(R.id.relativeLayout_ertonghao);
		// ??????????????????????????????
		gridView = (MyGridView) this.findViewById(R.id.gridView_hezhi);// ????????????
		gridView_daxiao = (MyGridView) this.findViewById(R.id.gridView_daxiao);// ??????????????????
		gridView_daxiao.setVisibility(View.VISIBLE);

		gridView.setNumColumns(4);
		gridView_daxiao.setNumColumns(4);

		adpater = new Adpater(list1, list2, new ArrayList<String>(),
				Select_K3_Activity.this, visibleGone, list_yilou1);
		gridView.setAdapter(adpater);

		adpater_daxiao = new MyAdapter(list_daxiao, list_daxiao2,
				Select_K3_Activity.this, visibleGone, list_yilou3);
		gridView_daxiao.setAdapter(adpater_daxiao);

		// ?????????????????????
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
	 * ?????????????????????,???????????????????????????????????????
	 */
	private void init() {
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
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
				tv_winnumber.setText(win_lastQi + "????????????  " + winNumber);

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
				tv_winnumber.setText("?????????????????? ");
			}
		} else {
			Toast.makeText(Select_K3_Activity.this, "??????????????????????????????????????????",
					Toast.LENGTH_SHORT);
		}
		// ????????????
		setData();
		// ????????????
		setList();
		// ???Adapter??????
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
		// ?????????
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
		settings = getSharedPreferences("app_user", 0);// ??????SharedPreference??????
		editor = settings.edit();// ??????????????????
		list_jiangjin.add("??????9-240???");
		list_jiangjin.add("??????40-240");
		list_jiangjin.add("??????15-80???");
		list_jiangjin.add("??????10-40???");
		list_jiangjin.add("??????8???");
		list_jiangjin.add("");

		list_image.add("1+2+3");
		list_image.add("111");
		list_image.add("113");
		list_image.add("235");
		list_image.add("35");
		list_image.add("");

		Map<Integer, String> playType = new HashMap<Integer, String>();// ????????????
		playType.put(0, "??????");
		playType.put(1, "?????????");
		playType.put(2, "?????????");
		playType.put(3, "????????????");
		playType.put(4, "????????????");
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
		tip = Html.fromHtml("???????????????????????????");
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		tv_top.setText(tip);
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
					} else if (playmark == 3) {// ?????????+???????????????
						str1 = item.optString("1");
						str3 = item2.optString("1");
					} else if (playmark == 5) { // ?????????
						str1 = item.optString("1");
						str2 = item.optString("2");
						str3 = item2.optString("1");

					} else if (playmark == 6) { // ????????????+???????????????
						str1 = item.optString("1");
						str3 = item2.optString("1");

					} else if (playmark == 7) { // ????????????
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
					// ?????????????????????
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
					Log.i("login", "????????????---" + ex.getMessage());
				}

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(Select_K3_Activity.this, "????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_K3_Activity.this, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		requestUtil.commit_yilou(playID);
	}

	/**
	 * ?????????????????????????????????????????????
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
			// ??????View????????????
			if (view == null) {
				holder = new ViewHolder_Winnumber();
				LayoutInflater inflater = LayoutInflater.from(context);
				// ??????????????????
				view = inflater.inflate(R.layout.item_k3_selec_winnumber, null);
				// ????????????
				holder.qihao = (TextView) view.findViewById(R.id.tv_qi);// ??????
				holder.status = (TextView) view.findViewById(R.id.tv_status);// ??????
				holder.winnumber = (TextView) view
						.findViewById(R.id.tv_winnumber);// ????????????
				holder.image1 = (TextView) view.findViewById(R.id.tv_img1);// ??????1
				holder.image2 = (TextView) view.findViewById(R.id.tv_img2);// ??????2
				holder.image3 = (TextView) view.findViewById(R.id.tv_img3);// ??????3
				view.setTag(holder);
			} else {
				holder = (ViewHolder_Winnumber) view.getTag();
			}
			Jiang_k3_Info info = list_windetail.get(position);
			holder.qihao.setText(info.getName() + "???");
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
			private TextView qihao, status, winnumber;// ????????????????????????
			private LinearLayout relativeLayout_poo;
			private TextView image1, image2, image3;
		}
	}

	/**
	 * ????????????????????????????????????
	 */
	public void getWinnumberData() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"????????????...", 0) {
			@Override
			public void responseCallback(JSONObject isusesInfo) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "??????????????????????????? " + isusesInfo);
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
					Log.i("login", "???????????????---" + ex.getMessage());
				}
				adapter_win = new MywinNumAdapter(list_wininfo,
						Select_K3_Activity.this);
				k3_win_listView.setAdapter(adapter_win);
				adapter_win.notifyDataSetChanged();

				if (isusesInfo.toString().equals("-500")) {
					MyToast.getToast(Select_K3_Activity.this, "????????????");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(Select_K3_Activity.this, "?????????????????????????????????..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "????????????" + error.getMessage());
			}
		};
		requestUtil.commit_winNumber("83");
	}

	/**
	 * ??????????????????????????????
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
			// list2.add("??????240???");
			list2.add("??????80???");
			list2.add("??????40???");
			list2.add("??????25???");
			list2.add("??????16???");
			list2.add("??????12???");
			list2.add("??????10???");
			list2.add("??????9???");
			list2.add("??????9???");
			list2.add("??????10???");
			list2.add("??????12???");
			list2.add("??????16???");
			list2.add("??????25???");
			list2.add("??????40???");
			list2.add("??????80???");
			// list2.add("??????240???");

		} else if (playID == 8303) {
			for (int i = 1; i < 7; i++) {
				list1.add(i + "" + i + "" + i + "");
				list2.add("??????240???");
			}
			list_daxiao.add("???????????????");
			list_daxiao2.add("??????????????????????????????40???");

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
			list_daxiao.add("???????????????");
		}

		// ????????????
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
	 * ????????????????????????
	 */
	private void setList() {
		gridView.setOnItemClickListener(new OnItemClickListenerHezhi());
		gridView_daxiao.setOnItemClickListener(new OnItemClickListenerDaxiao());
		gridView_ertonghaodan_1.setOnItemClickListener(new erbutong1());
		gridView_ertonghaodan_2.setOnItemClickListener(new erbutong2());
	}

	/**
	 * ??????Gridview???????????????
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
				if (playID == 8305) {// ?????????
					if (adpater_daxiao.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "???????????????1???");
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
	 * ??????Gridview???????????????
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
				if (playID == 8303) {// ?????????
					if (adpater.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "???????????????1???");
						return;
					}
				}

				if (playID == 8306) {// ????????????
					if (adpater.onSet1.size() >= 3) {
						MyToast.getToast(Select_K3_Activity.this, "???????????????3???");
						return;
					}
				}

				if (playID == 8307) {// ????????????
					if (adpater.onSet1.size() >= 2) {
						MyToast.getToast(Select_K3_Activity.this, "????????????2???");
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
	 * ????????????????????????????????????????????????
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
				daxiaodanshuang.add("???");
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
				daxiaodanshuang.add("???");
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
				daxiaodanshuang.add("???");
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
				daxiaodanshuang.add("???");
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
				daxiaodanshuang.add("???");
				daxiaodanshuang.add("???");
			} else if (adpater.onSet1.contains("4")
					&& adpater.onSet1.contains("6")
					&& adpater.onSet1.contains("8")
					&& adpater.onSet1.contains("10")) {
				tv_xiao.setTextColor(Color.WHITE);
				tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_shuang.setTextColor(Color.WHITE);
				tv_shuang
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("???");
				daxiaodanshuang.add("???");
			}
		} else if (adpater.onSet1.size() == 3) {
			if (adpater.onSet1.contains("12") && adpater.onSet1.contains("14")
					&& adpater.onSet1.contains("16")) {
				tv_da.setTextColor(Color.WHITE);
				tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_shuang.setTextColor(Color.WHITE);
				tv_shuang
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("???");
				daxiaodanshuang.add("???");
			} else if (adpater.onSet1.contains("5")
					&& adpater.onSet1.contains("7")
					&& adpater.onSet1.contains("9")) {
				tv_xiao.setTextColor(Color.WHITE);
				tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				tv_single.setTextColor(Color.WHITE);
				tv_single
						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
				daxiaodanshuang.add("???");
				daxiaodanshuang.add("???");
			}
		}
	}

	/**
	 * Gridview???????????????1
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

				if (playID == 8305) {// ?????????
					if (adpater1.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "???????????????1???");
						return;
					}
				}

				if (playID == 8309) {
					if (adpater1.onSet1.size() > 1) {
						Toast.makeText(Select_K3_Activity.this, "??????????????????2?????????",
								1500);
						return;
					}
				} else if (playID == 8310) {
					if (adpater1.onSet1.size() > 0) {
						Toast.makeText(Select_K3_Activity.this, "??????????????????1?????????",
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
	 * ?????????????????????????????????
	 */
	class hezhi_daxiaoClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			list_hezhi.clear();
			switch (v.getId()) {
			// ???
			case R.id.tv_da:
				if (daxiaodanshuang.size() == 0) {
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_da.setTextColor(Color.WHITE);
					for (int i = 11; i < 18; i++) {// ???
						list_hezhi.add(i + "");
					}

					System.out.println("======list_hezhi===="
							+ list_hezhi.toString());
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 11; i < 18; i++) {// ???
						list_hezhi.add(i + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_da.setTextColor(Color.WHITE);

					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 5; i < 9; i++) {// ??????
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_da.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 6; i < 9; i++) {// ??????
						list_hezhi.add(2 * i + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_da.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// ???
						list_hezhi.add(2 * i + 3 + "");
					}

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// ???
						list_hezhi.add(2 * i + 2 + "");
					}

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					list_hezhi.clear();
					for (int i = 5; i < 9; i++) {// ??????
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_da.setTextColor(Color.WHITE);
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					list_hezhi.clear();
					for (int i = 6; i < 9; i++) {// ??????
						list_hezhi.add(2 * i + "");
					}
					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_da.setTextColor(Color.WHITE);
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				}
				adpater.setonSet1(list_hezhi);
				break;

			/**
			 * ?????????
			 */
			case R.id.tv_xiao:
				if (daxiaodanshuang.size() == 0) {
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_xiao.setTextColor(Color.WHITE);
					for (int i = 4; i < 11; i++) {// ???
						list_hezhi.add(i + "");
					}
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 4; i < 11; i++) {// ???
						list_hezhi.add(i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_xiao.setTextColor(Color.WHITE);

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 1; i < 4; i++) {// ??????
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_xiao.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 2; i < 6; i++) {// ??????
						list_hezhi.add(2 * i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_xiao.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 4; i++) {// ??????
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_xiao.setTextColor(Color.WHITE);

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 2; i < 6; i++) {// ??????
						list_hezhi.add(2 * i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_xiao.setTextColor(Color.WHITE);

					tv_da.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_da.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// ???
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 2; i < 9; i++) {// ???
						list_hezhi.add(2 * i + "");
					}
					tv_xiao.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_xiao.setTextColor(getResources().getColor(
							R.color.black_light));
				}
				adpater.setonSet1(list_hezhi);
				break;
			// ?????????
			case R.id.tv_dan:
				if (daxiaodanshuang.size() == 0) {
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_single.setTextColor(Color.WHITE);
					for (int i = 1; i < 8; i++) {// ???
						list_hezhi.add(2 * i + 3 + "");
					}

				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 1; i < 8; i++) {// ???
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_single.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 5; i < 9; i++) {// ??????
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_single.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {

					for (int i = 1; i < 4; i++) {// ??????
						list_hezhi.add(2 * i + 3 + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_single.setTextColor(Color.WHITE);
					// ????????????
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 11; i < 18; i++) {// ???
						list_hezhi.add(i + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));

				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 5; i < 9; i++) {// ??????
						list_hezhi.add(2 * i + 1 + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_single.setTextColor(Color.WHITE);

				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 4; i < 11; i++) {// ???
						list_hezhi.add(i + "");
					}
					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}

					for (int i = 1; i < 4; i++) {// ??????
						list_hezhi.add(2 * i + 3 + "");
					}

					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_single.setTextColor(Color.WHITE);
				}
				adpater.setonSet1(list_hezhi);
				break;
			// ?????????
			case R.id.tv_shuang:
				if (daxiaodanshuang.size() == 0) {
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_shuang.setTextColor(Color.WHITE);

					for (int i = 2; i < 9; i++) {// ???
						list_hezhi.add(2 * i + "");
					}
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 2; i < 9; i++) {// ???
						list_hezhi.add(2 * i + "");
					}

					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_shuang.setTextColor(Color.WHITE);

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));

				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 6; i < 9; i++) { // ??????
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_shuang.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 1) {
					for (int i = 2; i < 6; i++) {// ??????
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_shuang.setTextColor(Color.WHITE);
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 6; i < 9; i++) { // ??????
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_shuang.setTextColor(Color.WHITE);

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 11; i < 18; i++) {// ???
						list_hezhi.add(i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_shuang.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					list_hezhi.clear();
					for (int i = 2; i < 6; i++) {// ??????
						list_hezhi.add(2 * i + "");
					}
					tv_shuang
							.setBackgroundResource(R.drawable.bet_btn_dan_selected);
					daxiaodanshuang.add("???");
					tv_shuang.setTextColor(Color.WHITE);

					tv_single
							.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
					tv_single.setTextColor(getResources().getColor(
							R.color.black_light));
				} else if (daxiaodanshuang.contains("???")
						&& daxiaodanshuang.contains("???")
						&& daxiaodanshuang.size() == 2) {
					Iterator<String> sListIterator1 = daxiaodanshuang
							.iterator();
					while (sListIterator1.hasNext()) {
						String e = sListIterator1.next();
						if (e.equals("???")) {
							sListIterator1.remove();
						}
					}
					for (int i = 4; i < 11; i++) {// ???
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
	 * Gridview?????????????????????
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
				if (playID == 8305) {// ?????????
					if (adpater2.onSet1.size() >= 1) {
						MyToast.getToast(Select_K3_Activity.this, "???????????????1???");
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
	 * ?????????????????????
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
	 * ???????????????????????????
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
	 * ????????????
	 */
	private void winDetail() {
		Intent intent = new Intent(Select_K3_Activity.this,
				WinLotteryInfoActivity.class);
		intent.putExtra("lotteryId", AppTools.lottery.getLotteryID());
		startActivity(intent);
	}

	/**
	 * ??????popWindow
	 */
	private void createPopWindow() {
		LayoutInflater inflact = LayoutInflater.from(Select_K3_Activity.this);
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
				LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 1.0f);
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
		/** ???????????? **/
		case R.id.btn_help:
			createPopWindow();
			popWindow.showAsDropDown(v);
			LotteryUtils.backgroundAlpaha(Select_K3_Activity.this, 0.5f);
			break;
		/** ?????? **/
		case R.id.layout_shake:
		case R.id.iv_shake:
		case R.id.layout_shake2:
		case R.id.iv_shake2:
			if (null != vibrator)
				vibrator.vibrate(300);
			selectRandom();// ??????
			break;
		case R.id.iv_up_down2:// ???????????????10???????????????
		case R.id.ll_winnum:
			getWinnumberData();

			if (k3_win_listView.getVisibility() == View.GONE) {
				rote(3);// ???????????? ??????
				k3_win_listView.setVisibility(View.VISIBLE);
				k3_layout_tile.setVisibility(View.VISIBLE);
				img_line.setVisibility(View.VISIBLE);
			} else if (k3_win_listView.getVisibility() == View.VISIBLE) {
				rote(4);// ???????????? ??????
				k3_win_listView.setVisibility(View.GONE);
				k3_layout_tile.setVisibility(View.GONE);
				img_line.setVisibility(View.GONE);
			}
			break;
		/** ????????? **/
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
					rote(2);// ???????????? ??????
				}
			});
			rote(1);// ???????????? ??????
			break;
		}
	}

	/**
	 * ????????????
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
	 * ?????????????????????????????????
	 */
	public void changePlayType() {
		String mark = "";
		if (0 == parentIndex) {// ??????
			mark = "??????-";
		} else if (1 == parentIndex) {// ??????
			mark = "??????-";
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
			case 0:// ??? ???
				ll_daxiao.setVisibility(View.VISIBLE);// ??????????????????
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.VISIBLE);
				playID = 8301;
				setData();// ????????????
				tip = Html.fromHtml("???????????????????????????");
				tv_top.setText(tip);
				gridView.setNumColumns(4);
				gridView_daxiao.setNumColumns(4);
				break;
			case 1:// ?????????
				ll_daxiao.setVisibility(View.GONE);// ??????????????????
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.VISIBLE);
				tv_daxiao.setVisibility(View.GONE);
				playID = 8303;
				setData();// ????????????
				tip = Html.fromHtml("????????????(3????????????)");
				tv_top.setText(tip);
				gridView.setNumColumns(3);
				gridView_daxiao.setNumColumns(1);
				break;
			case 2:// ?????????
				playID = 8305;
				setData();// ????????????
				ll_hezhishow2.setVisibility(View.VISIBLE);
				tv_img2.setText("??????");
				tv_title2.setText("????????????2??????????????????????????????15???");
				ll_daxiao.setVisibility(View.GONE);// ??????????????????
				relativeLayout2.setVisibility(View.VISIBLE);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);
				tv_dan.setVisibility(View.VISIBLE);
				tv_tuo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.VISIBLE);
				tv_daxiao.setVisibility(View.GONE);
				tip = Html.fromHtml("????????????(???2???????????? )");
				tv_title.setText(tip);
				tv_dan.setText("??????");
				tv_tuo.setText("?????????");
				gridView_daxiao.setNumColumns(3);
				break;

			case 3:// ????????????
				ll_hezhishow.setVisibility(View.VISIBLE);
				tv_img1.setVisibility(View.VISIBLE);
				tv_title1.setText("?????????????????????,?????????????????????40???");
				ll_hezhishow2.setVisibility(View.VISIBLE);
				tv_img2.setText("?????????");
				tv_title2.setText("123,234,345,456??????????????????40???");
				tv_daxiao.setVisibility(View.GONE);
				ll_daxiao.setVisibility(View.GONE);// ??????????????????
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.VISIBLE);
				tv_top.setVisibility(View.GONE);
				playID = 8306;
				setData();// ????????????
				gridView.setNumColumns(6);
				gridView_daxiao.setNumColumns(1);
				break;

			case 4:// ????????????
				ll_hezhishow.setVisibility(View.VISIBLE);
				tv_img1.setVisibility(View.GONE);
				tv_title1.setText("???2???????????????????????????????????????2?????????8???");
				ll_daxiao.setVisibility(View.GONE);// ??????????????????
				relativeLayout1.setVisibility(View.VISIBLE);
				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.GONE);
				playID = 8307;
				tv_top.setVisibility(View.GONE);
				setData();// ????????????
				gridView.setNumColumns(3);
				break;
			}

			break;
		case 1:// ????????????
			layout_shake2.setVisibility(View.INVISIBLE);
			switch (itemIndex) {
			case 0:
				playID = 8309;
				setData();// ????????????
				ll_daxiao.setVisibility(View.GONE);// ??????????????????
				relativeLayout2.setVisibility(View.VISIBLE);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);

				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.GONE);

				tv_dan.setVisibility(View.VISIBLE);
				tv_tuo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.VISIBLE);

				tip = Html.fromHtml("?????????????????????"
						+ AppTools.changeStringColor("#e3393c", "80") + "???");
				tv_title.setText(tip);
				tv_dan.setText("??????");
				tv_tuo.setText("??????");
				break;
			case 1:
				playID = 8310;
				setData();// ????????????
				ll_daxiao.setVisibility(View.GONE);// ??????????????????
				relativeLayout2.setVisibility(View.VISIBLE);
				gridView_ertonghaodan_1.setAdapter(adpater1);
				gridView_ertonghaodan_2.setAdapter(adpater2);
				gridView_daxiao.setVisibility(View.GONE);
				tv_daxiao.setVisibility(View.GONE);
				tv_dan.setVisibility(View.VISIBLE);
				tv_tuo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.VISIBLE);
				tip = Html.fromHtml("?????????????????????"
						+ AppTools.changeStringColor("#e3393c", "80") + "???");
				tv_title.setText(tip);
				tv_dan.setText("??????");
				tv_tuo.setText("??????");
				break;
			default:
				break;
			}
			break;
		}
	}

	/**
	 * ?????????????????????
	 * 
	 * @param type
	 *            :1.?????? 2.??????
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
	 * ??????????????????????????????
	 * 
	 * @param v
	 *            :?????????????????????
	 */
	private void setShakeBtnVisible(int v) {
		layout_shake.setVisibility(v);
		layout_shake2.setVisibility(v);
	}

	/**
	 * ??????????????????????????? ????????????????????? ????????????
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
	 * ????????????
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_K3_Activity.this,
				PlayDescription.class);
		Select_K3_Activity.this.startActivity(intent);
	}

	/**
	 * ?????? ????????????
	 */
	public void selectRandom() {
		List<String> list = new ArrayList<String>();
		if (null != vibrator)
			vibrator.vibrate(150);
		switch (playID) {
		case 8301: // ??????
			clearDaxiao();
			list.add((int) ((Math.random() * 14) + 4) + "");
			adpater.setonSet1(list);
			break;
		case 8302: // ????????????
		case 8308: // ???????????????
			list.add("1");
			adpater.setonSet1(list);
			break;
		case 8303: // ????????????
		case 8304: // ????????????
			adpater.setonSet1(NumberTools.getRandomNum6(1, 6));
			break;
		case 8307:// ????????????
			adpater.setonSet1(NumberTools.getRandomNum6(2, 6));
			break;
		case 8306:// ????????????
			adpater.setonSet1(NumberTools.getRandomNum6(3, 6));
			break;
		case 8305:// ????????????
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
	 * ????????????
	 */
	private void submitNumber() {
		if (AppTools.totalCount < 1) {
			MyToast.getToast(Select_K3_Activity.this, "?????????????????????");
			return;
		} else {
			if (playID == 8305) {// ?????????
				if (adpater1.onSet1.size() == 0 && adpater2.onSet1.size() == 0) {
					// ?????????
				} else {
					if (adpater1.onSet1.size() == 0) {
						MyToast.getToast(Select_K3_Activity.this,
								"???????????????????????????????????????????????????");
						return;
					} else if (adpater2.onSet1.size() == 0) {
						MyToast.getToast(Select_K3_Activity.this,
								"???????????????????????????????????????????????????");
						return;
					}
				}
			}
			if (playID == 8306) { // ????????????
				if (adpater.onSet1.size() < 3 && adpater.onSet1.size() != 0) {
					MyToast.getToast(Select_K3_Activity.this, "??????????????????????????????3?????????");
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
	 * ??????????????????
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
	 * ????????????????????????????????????????????????
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
	 * ????????????????????????????????????
	 */
	public void register() {
		clearDaxiao();
		getItem();
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// ???????????????
		vibrator = VibratorView.getVibrator(getApplicationContext());
	}

	/**
	 * ??????????????????
	 */
	public void unregister() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/**
	 * ??????????????? ??? ?????????
	 * 
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
	 * ??????????????????
	 */
	private void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {
			if (0 != adpater.onSet1.size() || 0 != adpater1.onSet1.size()
					|| 0 != adpater2.onSet1.size()) {
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
			if (0 != adpater.onSet1.size() || 0 != adpater1.onSet1.size()
					|| 0 != adpater2.onSet1.size()) {
				dialog.show();
				dialog.setDialogContent("??????????????????????????????,?????????????????????????????????");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// ??????
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
	 * ??????????????????????????????
	 */
	public void updateAdapter() {
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * ?????????????????????
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
				// ?????????????????????
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
				MyToast.getToast(getApplicationContext(), "??????????????????????????????");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * ??????????????????????????????
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
				// ??????????????????
				view = inflater.inflate(R.layout.k3_daxiao, null);

				holder.ll_pop_k3 = (LinearLayout) view
						.findViewById(R.id.ll_pop_k3);
				holder.ll_pop_k3 = (LinearLayout) view
						.findViewById(R.id.ll_pop_k3);
				holder.daxiao = (TextView) view.findViewById(R.id.pop_k3_text3);
				holder.daxiao2 = (TextView) view
						.findViewById(R.id.pop_k3_text4);
				holder.tv_k3_yilou = (TextView) view
						.findViewById(R.id.tv_k3_yilou);// ?????????
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
					holder.tv_k3_yilou.setText("?????????" + listyilou.get(position));
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
	 * ?????????????????????????????????
	 * 
	 * @author lenovo
	 * 
	 */
	class Adpater extends BaseAdapter {
		Context context;
		List<String> list1;
		List<String> list2;
		boolean isforgetball;// ?????????????????????
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
			// ??????View????????????
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// ??????????????????
				view = inflater.inflate(R.layout.item_k3, null);
				// ????????????
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
							holder.tv_k3_yilou.setText("?????????"
									+ list_yilou.get(position + 1));
					} else {
						if (null != list_yilou && list_yilou.size() > 0)
							holder.tv_k3_yilou.setText("?????????"
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
