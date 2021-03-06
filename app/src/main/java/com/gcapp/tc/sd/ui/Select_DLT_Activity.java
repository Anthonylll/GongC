package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcapp.tc.sd.ui.adapter.GridViewCJDLTAdapter;
import com.gcapp.tc.sd.ui.adapter.MyGridViewAdapter;
import com.gcapp.tc.sd.ui.adapter.RandomGridViewAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.utils.PopupWindowUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.view.SmanagerView;
import com.gcapp.tc.view.VibratorView;
import com.gcapp.tc.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 功能：大乐透的选号界面
 * 
 * @author lenovo
 * 
 */
public class Select_DLT_Activity extends Activity implements OnClickListener,
		SensorEventListener {
	private final static String TAG = "Buy_DLT_Activit";
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助

	private Animation animation = null;

	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数

	/* 中间部分 */
	private ScrollView sv_show_ball;
	private TextView tv_lotteryname;// 彩种名
	// 中奖的红色蓝色号码
	private TextView tv_selected_redball;
	private LinearLayout layout_shake;// 摇一摇
	private ImageView iv_shake;// 摇一摇
	private TextView tv_shake;// 摇一摇

	private ScrollView scrollView;
	private boolean spinnerIsSelect = false; // 下拉框是否被点击
	private Bundle bundle;

	public Vibrator vibrator; // 震动器
	private SensorManager mSmanager; // 传感器

	private static String lottery_redNum; // 中奖的红色号码
	private static String lottery_blueNum; // 中奖的蓝色号码

	private GridView gridView_red, gridView_blue, gridView_tuo_red,
			gridView_tuo_blue; // 红球区

	private GridViewCJDLTAdapter redAdapter, blueAdapter, redTuoAdapter,
			blueTuoAdapter; // 红球Adapter

	private TextView tv_red, tv_red_show, tv_blue, tv_blue_show; // 红球提示

	private TextView tv_red2, tv_blue2;
	private LinearLayout layout_redTuo, layout_blueTuo;
	private Integer[] reds = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
			32, 33, 34, 35 };
	private Integer[] blues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	/** 机选个数 **/
	private Integer[] redRandom = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	private Integer[] blueRandom = { 2, 3, 4, 5, 6 };
	private MyGridView randomGridView; // 机选随机红(蓝)球个数的GridView
	private RandomGridViewAdapter randomAdapter; // 机选随机红(蓝)球个数的GridView的Adapter
	public int randomRedNum = 0;
	public int randomBlueNum = 0;
	private int type = 1;
	private Map<Integer, Integer> playtypeMap;

	/** 传感器 */
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// 这一次的时间
	private long vTime = 0; // 震动的时间
	private SharedPreferences settings;
	private Editor editor;
	private ConfirmDialog dialog;// 提示框
	private PopupWindowUtil popUtil;
	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
	private int parentIndex;
	private int itemIndex;
	private LinearLayout lottery_ll_win;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_buy_cjdlt);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		init();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		GridViewCJDLTAdapter.playType = 3901;
		lottery_ll_win = (LinearLayout) findViewById(R.id.lottery_ll_win);
		tv_red = (TextView) this.findViewById(R.id.number_sv_center_tv_redShow);
		tv_red_show = (TextView) this
				.findViewById(R.id.number_sv_center_tv_redShow2);
		tv_red2 = (TextView) this.findViewById(R.id.number_dan_show2);// 至少选出2个
		tv_blue2 = (TextView) this.findViewById(R.id.number_danblue_show2);// 至少选出2个

		tv_red2.setText(Html.fromHtml("至少选出" + "<font color='#e3393c'>2</font>"
				+ "个"));
		tv_blue2.setText(Html.fromHtml("至少选出"
				+ "<font color='#105cde'>2</font>" + "个"));

		layout_redTuo = (LinearLayout) this.findViewById(R.id.all_red_dan_qu);
		layout_blueTuo = (LinearLayout) this.findViewById(R.id.all_blue_dan_qu);
		tv_blue = (TextView) this
				.findViewById(R.id.number_sv_center_tv_blueShow);
		tv_blue_show = (TextView) this
				.findViewById(R.id.number_sv_center_tv_blueShow2);
		tv_selected_redball = (TextView) this
				.findViewById(R.id.tv_selected_redball);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		gridView_red = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showRed);
		gridView_blue = (GridView) this
				.findViewById(R.id.number_sv_center_gv_showBlue);
		gridView_tuo_red = (GridView) this
				.findViewById(R.id.number_sv_center_showRed_dan);
		gridView_tuo_blue = (GridView) this
				.findViewById(R.id.number_sv_center_showblue_dan);
		btn_playtype = (TextView) this.findViewById(R.id.btn_playtype);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		tv_lotteryname = (TextView) findViewById(R.id.tv_lotteryname);
		layout_shake = (LinearLayout) findViewById(R.id.layout_shake);
		iv_shake = (ImageView) findViewById(R.id.iv_shake);
		tv_shake = (TextView) findViewById(R.id.tv_shake);
		sv_show_ball = (ScrollView) findViewById(R.id.sv_show_ball);

		if (btn_playtype.getText().toString().contains("普通投注")) {
			tv_red_show.setText(Html.fromHtml("请至少选出"
					+ "<font color='#e3393c'>5</font>" + "个前区号码"));
			tv_blue_show.setText(Html.fromHtml("请至少选出"
					+ "<font color='#105cde'>2</font>" + "个后区号码"));
		}
	}

	/**
	 * 初始化属性 ,得到上期开奖号码
	 * 
	 */
	private void init() {
		lottery_ll_win.removeAllViews();

		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);

		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (NetWork.isConnect(Select_DLT_Activity.this)) {

			if (AppTools.lottery.getLastWinNumber() != null) {
				String num = AppTools.lottery.getLastWinNumber();
				String num2 = num.replaceAll("\\s?[\\+]\\s?", "-");
				if (num2 != null && num2.contains("-")) {

					LotteryUtils.addNumBall(lottery_ll_win,
							num2.split("-")[0].split(" "), null, 0,
							Select_DLT_Activity.this);
					LotteryUtils.addNumBall(lottery_ll_win,
							num2.split("-")[1].split(" "), null, 1,
							Select_DLT_Activity.this);
				} else {
					LotteryUtils.addNumTextView(lottery_ll_win,
							Select_DLT_Activity.this);
				}
				String str[] = num.split("-");
				if (str.length == 2) {
					lottery_redNum = str[0];
					lottery_blueNum = str[1];
				} else {
					lottery_redNum = num;
				}
			} else {
				LotteryUtils.addNumTextView(lottery_ll_win,
						Select_DLT_Activity.this);
			}

			if (null != AppTools.lottery) {
				if (null != AppTools.lottery.getStarttime()
						&& !"".equals(AppTools.lottery.getStarttime())) {
					tv_selected_redball
							.setText(AppTools.lottery.getStarttime());
				} else {
					tv_selected_redball.setText("");
				}
			} else {
				tv_selected_redball.setText("");
			}

		} else {
			LotteryUtils.addNumTextView(lottery_ll_win,
					Select_DLT_Activity.this);

			Toast.makeText(Select_DLT_Activity.this, "网络连接异常，获得数据失败！",
					Toast.LENGTH_SHORT).show();
		}

		// 给Adapter赋值
		redAdapter = new GridViewCJDLTAdapter(Select_DLT_Activity.this, reds,
				Color.RED, 1);
		blueAdapter = new GridViewCJDLTAdapter(Select_DLT_Activity.this, blues,
				Color.BLUE, 3);
		redTuoAdapter = new GridViewCJDLTAdapter(Select_DLT_Activity.this,
				reds, Color.RED, 2);
		blueTuoAdapter = new GridViewCJDLTAdapter(Select_DLT_Activity.this,
				blues, Color.BLUE, 4);
		settings = getSharedPreferences("app_user", 0);// 获取SharedPreference对象
		editor = settings.edit();// 获取编辑对象
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
		tv_lotteryname.setText("大乐透");
		Map<Integer, String> playType = new HashMap<Integer, String>();
		playType.put(0, "普通投注");
//		playType.put(1, "前区胆拖");
//		playType.put(2, "后区胆拖");
		playType.put(1, "双区胆拖");
//		playType.put(4, "");
//		playType.put(5, "");
		Set<Integer> set = playType.keySet();
//		int[] playtype_array = { 3901, 3903, 3906, 3907 };
		int[] playtype_array = { 3901, 3907 };
		playtypeMap = new HashMap<Integer, Integer>();
		for (Integer i : set) {
			if (i < 2) {
				playtypeMap.put(playtype_array[i], i);
			}
		}
		data.put(0, playType);
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 旋转
	 * 
	 * @param type
	 *            1.向上 2.向下
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
	 * 注册传感器，得到选号数据
	 */
	public void register() {
		getItem();
		tv_tatol_count.setText(+AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
		if (MyGridViewAdapter.playType == 501) {
			SmanagerView.registerSensorManager(mSmanager,
					getApplicationContext(), this);// 注册传感器
			vibrator = VibratorView.getVibrator(getApplicationContext());
		}
	}

	/**
	 * 清空所有数据
	 */
	public void unregister() {
		clear();
		vibrator = null;
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		// 绑定Adapter
		gridView_tuo_red.setAdapter(redTuoAdapter);
		gridView_red.setAdapter(redAdapter);
		gridView_blue.setAdapter(blueAdapter);
		gridView_tuo_blue.setAdapter(blueTuoAdapter);
		// 给按钮添加点击监听
		btn_playtype.setOnClickListener(this);
	}

	/**
	 * 从投注页面跳转过来 将投注页面的值 显示出来
	 */
	private void getItem() {
		Intent intent = Select_DLT_Activity.this.getIntent();
		Bundle bundle = null;
		if (intent != null) {
			bundle = intent.getBundleExtra("bundle");
		}
		sv_show_ball.scrollTo(0, 0);
		if (null != bundle) {
			clearHashSet();
			GridViewCJDLTAdapter.playType = bundle.getInt("type");
			int type = GridViewCJDLTAdapter.playType;
			itemIndex = playtypeMap.get(type);
			if (3901 == GridViewCJDLTAdapter.playType) {
				setRandomBtnVisible(View.VISIBLE);
			} else
				setRandomBtnVisible(View.GONE);
			if (null != bundle.getStringArrayList("red")) {
				for (String str : bundle.getStringArrayList("red")) {
					GridViewCJDLTAdapter.redSet.add(str);
				}
			}
			if (null != bundle.getStringArrayList("blue")) {
				for (String str : bundle.getStringArrayList("blue")) {
					GridViewCJDLTAdapter.blueSet.add(str);
				}
			}

			if (GridViewCJDLTAdapter.playType == 3901) {
				setGridViewNotVisible();
			} else {
				if (null != bundle.getStringArrayList("redTuo")) {
					for (String str : bundle.getStringArrayList("redTuo")) {
						System.out.println("str" + str);
						GridViewCJDLTAdapter.redTuoSet.add(str);
					}
				}
				if (null != bundle.getStringArrayList("blueTuo")) {
					for (String str : bundle.getStringArrayList("blueTuo")) {
						GridViewCJDLTAdapter.blueTuoSet.add(str);
					}
				}
				setGridViewVisible();
			}
			updateAdapter();
		}
	}

	/**
	 * 公共点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			playExplain();
			break;
		case R.id.layout_shake:
		case R.id.iv_shake:
		case R.id.tv_shake:
			if (null != vibrator)
				vibrator.vibrate(300);
			selectRandom();// 机选
			break;
		/** 选玩法 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
		case R.id.iv_up_down:
			popUtil = new PopupWindowUtil(this, data, layout_top_select);
			popUtil.setSelectIndex(parentIndex, itemIndex);
			popUtil.createPopWindow();
			popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
				@Override
				public void getIndex(int parentIndex, int itemIndex) {
					if (itemIndex < 4) {
						if (Select_DLT_Activity.this.itemIndex != itemIndex) {
							Select_DLT_Activity.this.parentIndex = parentIndex;
							Select_DLT_Activity.this.itemIndex = itemIndex;
							changePlayType();
						}
					}
					rote(2);// 旋转动画 向下
				}
			});
			rote(1);// 旋转动画 向上
			break;
		case R.id.btn_back:
			exit();
			break;
		}
	}

	/**
	 * 处理玩法改变的方法
	 */
	public void changePlayType() {
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		switch (itemIndex) {
		/** 普通玩法 **/
		case 0:
			btn_playtype.setText("普通投注");
			GridViewCJDLTAdapter.playType = 3901;
			setGridViewNotVisible();
			setRandomBtnVisible(View.VISIBLE);
			break;
//		/** 胆拖玩法 **/
//		case 1:
//			btn_playtype.setText("前区胆拖");
//			GridViewCJDLTAdapter.playType = 3903;
//			setGridViewVisible();
//			setRandomBtnVisible(View.GONE);
//			break;
//		/** 普通玩法 **/
//		case 2:
//			btn_playtype.setText("后区胆拖");
//			GridViewCJDLTAdapter.playType = 3906;
//			setGridViewVisible();
//			setRandomBtnVisible(View.GONE);
//			break;
		/** 胆拖玩法 **/
		case 1:
			btn_playtype.setText("双区胆拖");
			GridViewCJDLTAdapter.playType = 3907;
			setGridViewVisible();
			setRandomBtnVisible(View.GONE);
			break;
		}
		AppTools.totalCount = 0;
		clearHashSet();
		updateAdapter();
		sv_show_ball.scrollTo(0, 0);
	}

	/**
	 * 设置机选按钮是否可见
	 */
	private void setRandomBtnVisible(int v) {
		layout_shake.setVisibility(v);
	}

	/**
	 * 提交号码
	 */
	private void submitNumber() {
		if (GridViewCJDLTAdapter.playType == 3901) {
			if (AppTools.totalCount == 0) {
				if (GridViewCJDLTAdapter.redSet.size() == 0
						&& GridViewCJDLTAdapter.blueSet.size() == 0
						&& GridViewCJDLTAdapter.redTuoSet.size() == 0) {
					// 得到红球的随机数
					GridViewCJDLTAdapter.redSet = NumberTools.getRandomNum2(5,
							35);
					// 得到蓝球的随机数
					GridViewCJDLTAdapter.blueSet = NumberTools.getRandomNum2(2,
							12);
					// 刷新Adapter
					redAdapter.setNumByRandom();
				} else {
					Toast.makeText(Select_DLT_Activity.this, "请至少选择一注",
							Toast.LENGTH_SHORT).show();
				}
				return;
			}
		} else if (GridViewCJDLTAdapter.playType == 3903) {
			if (AppTools.totalCount < 2) {
				Toast.makeText(Select_DLT_Activity.this, "胆拖请至少选择二注",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (GridViewCJDLTAdapter.redTuoSet.size() < 2) {
				Toast.makeText(Select_DLT_Activity.this, "胆区红球至少选2个",
						Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (GridViewCJDLTAdapter.playType == 3906) {
			if (AppTools.totalCount < 2) {
				Toast.makeText(Select_DLT_Activity.this, "胆拖请至少选择二注",
						Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (GridViewCJDLTAdapter.playType == 3907) {
			if (GridViewCJDLTAdapter.redSet.size() < 1) {
				Toast.makeText(Select_DLT_Activity.this, "胆区红球至少选1个",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (GridViewCJDLTAdapter.redSet.size()
					+ GridViewCJDLTAdapter.redTuoSet.size() <= 5) {
				Toast.makeText(Select_DLT_Activity.this, "红球请至少选择6个",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (GridViewCJDLTAdapter.blueTuoSet.size() < 2) {
				Toast.makeText(Select_DLT_Activity.this, "篮拖请至少选2个",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (AppTools.totalCount < 2) {
				Toast.makeText(Select_DLT_Activity.this, "胆拖请至少选择二注",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Intent intent = new Intent(Select_DLT_Activity.this,
				Bet_DLT_Activity.class);
		intent.putExtra("lotteryBundle", bundle);
		Select_DLT_Activity.this.startActivity(intent);
		vTime = 0;
	}

	/**
	 * 机选号码
	 */
	public void selectRandom() {
		// 得到红球的随机数
		GridViewCJDLTAdapter.redSet = NumberTools.getRandomNum2(5, 35);
		// 得到蓝球的随机数
		GridViewCJDLTAdapter.blueSet = NumberTools.getRandomNum2(2, 12);
		// 刷新Adapter
		redAdapter.setNumByRandom();
	}

	/**
	 * 跳转到玩法说明
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_DLT_Activity.this,
				PlayDescription.class);
		Select_DLT_Activity.this.startActivity(intent);
	}

	/**
	 * 注册传感器 和 振动器，刷新页面
	 */
	@Override
	protected void onResume() {
		super.onResume();
		getItem();
		vibrator = VibratorView.getVibrator(getApplicationContext());
		if (GridViewCJDLTAdapter.playType != 3903)
			// 注册传感器
			SmanagerView.registerSensorManager(mSmanager,
					getApplicationContext(), this);
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
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
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出当前页面，清空选号
	 */
	public void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {
			if (GridViewCJDLTAdapter.redSet.size() != 0
					|| GridViewCJDLTAdapter.redTuoSet.size() != 0
					|| GridViewCJDLTAdapter.blueSet.size() != 0
					|| GridViewCJDLTAdapter.blueTuoSet.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clearHashSet();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				clearHashSet();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else if (AppTools.list_numbers != null
				&& AppTools.list_numbers.size() != 0) {
			if (GridViewCJDLTAdapter.redSet.size() != 0
					|| GridViewCJDLTAdapter.redTuoSet.size() != 0
					|| GridViewCJDLTAdapter.blueSet.size() != 0
					|| GridViewCJDLTAdapter.blueTuoSet.size() != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clearHashSet();
							Intent intent = new Intent(
									Select_DLT_Activity.this,
									Bet_DLT_Activity.class);
							Select_DLT_Activity.this.startActivity(intent);
							Select_DLT_Activity.this.finish();
						}
					}
				});
			} else {
				clearHashSet();
				Intent intent = new Intent(Select_DLT_Activity.this,
						Bet_DLT_Activity.class);
				Select_DLT_Activity.this.startActivity(intent);
				Select_DLT_Activity.this.finish();
			}
		}
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
		if (speed >= 900 && currentUpdateTime - vTime > 700) {
			vTime = System.currentTimeMillis();
			if (null != vibrator)
				vibrator.vibrate(300);
			// 得到红球的随机数
			GridViewCJDLTAdapter.redSet = NumberTools.getRandomNum2(5, 35);
			// 得到蓝球的随机数
			GridViewCJDLTAdapter.blueSet = NumberTools.getRandomNum2(2, 12);
			// 随机选
			redAdapter.setNumByRandom();
		}
	}

	/**
	 * 设置胆拖 区可见
	 */
	private void setGridViewVisible() {
		if (GridViewCJDLTAdapter.playType == 3903) {
			btn_playtype.setText("前区胆拖");
			tv_red.setText(R.string.num_cjdlt_red2);

			tv_red_show.setText(Html.fromHtml("至少选出"
					+ "<font color='#e3393c'>1</font>" + "个,最多"
					+ "<font color='#e3393c'>4</font>" + "个"));

			tv_blue.setText(R.string.num_cjdlt_blue);

			tv_blue_show.setText(Html.fromHtml("请至少选出"
					+ "<font color='#105cde'>2</font>" + "个后区号码"));
			// 设置可见
			layout_redTuo.setVisibility(View.VISIBLE);
			layout_blueTuo.setVisibility(View.GONE);
		}
		// 后区胆拖
		else if (3906 == GridViewCJDLTAdapter.playType) {
			btn_playtype.setText("后区胆拖");
			tv_red.setText(R.string.num_cjdlt_red);
			tv_red_show.setText(Html.fromHtml("请选出"
					+ "<font color='#e3393c'>5</font>" + "个前区号码"));

			layout_redTuo.setVisibility(View.GONE);
			layout_blueTuo.setVisibility(View.VISIBLE);
			tv_blue.setText(R.string.num_cjdlt_blue2);

			tv_blue_show.setText(Html.fromHtml("只能选择"
					+ "<font color='#105cde'>2</font>" + "个"));
		}
		// 双区胆拖
		else if (3907 == GridViewCJDLTAdapter.playType) {
			btn_playtype.setText("双区胆拖");
			tv_red.setText(R.string.num_cjdlt_red2);

			tv_red_show.setText(Html.fromHtml("至少选出"
					+ "<font color='#e3393c'>1</font>" + "个,最多"
					+ "<font color='#e3393c'>4</font>" + "个"));

			tv_blue.setText(R.string.num_cjdlt_blue2);
			tv_blue_show.setText(Html.fromHtml("只能选择"
					+ "<font color='#105cde'>2</font>" + "个"));

			layout_redTuo.setVisibility(View.VISIBLE);
			layout_blueTuo.setVisibility(View.VISIBLE);
		}
		// 注销传感器
		SmanagerView.unregisterSmanager(mSmanager, this);
		// 刷新Adapter
		updateAdapter();
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 设置胆拖 区不可见
	 */
	private void setGridViewNotVisible() {
		btn_playtype.setText("普通投注");
		tv_red.setText(R.string.num_cjdlt_red);
		tv_red_show.setText(Html.fromHtml("请至少选出"
				+ "<font color='#e3393c'>5</font>" + "个前区号码"));

		tv_blue.setText(R.string.num_cjdlt_blue);
		tv_blue_show.setText(Html.fromHtml("请至少选出"
				+ "<font color='#105cde'>2</font>" + "个后区号码"));
		// 注册传感器
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);

		// 设置可见
		layout_redTuo.setVisibility(View.GONE);
		layout_blueTuo.setVisibility(View.GONE);
		// 刷新Adapter
		updateAdapter();
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 清空选号和页面数据
	 */
	private void clear() {
		clearHashSet();
		updateAdapter();
		AppTools.totalCount = 0;
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 刷新Adapter
	 */
	public void updateAdapter() {
		redAdapter.notifyDataSetChanged();
		redTuoAdapter.notifyDataSetChanged();
		blueAdapter.notifyDataSetChanged();
		blueTuoAdapter.notifyDataSetChanged();
	}

	/**
	 * 清空选中情况
	 */
	public static void clearHashSet() {
		if (null != GridViewCJDLTAdapter.redSet) {
			GridViewCJDLTAdapter.redSet.clear();
		}
		if (null != GridViewCJDLTAdapter.redTuoSet) {
			GridViewCJDLTAdapter.redTuoSet.clear();
		}
		if (null != GridViewCJDLTAdapter.blueSet) {
			GridViewCJDLTAdapter.blueSet.clear();
		}
		if (null != GridViewCJDLTAdapter.blueTuoSet) {
			GridViewCJDLTAdapter.blueTuoSet.clear();
		}

	}

	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	/**
	 * 机选 按钮点击
	 * 
	 * @param redNum
	 *            ：红球数
	 * @param blueNum
	 *            ：蓝球数
	 */
	public void selectRandom(int redNum, int blueNum) {

		if (redNum == 0) {
			// 得到蓝球的随机数
			GridViewCJDLTAdapter.blueSet = NumberTools.getRandomNum2(blueNum,
					12);
		}
		if (blueNum == 0) {
			// 得到红球的随机数
			GridViewCJDLTAdapter.redSet = NumberTools.getRandomNum2(redNum, 35);
		}
		// 刷新Adapter
		redAdapter.setNumByRandom();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getBundleExtra("bundle") != null) {
			// 投注单页面跳转过来的
			setIntent(intent);
		} else {
			// 投注成功，继续投注跳转过来
			clear();
		}
		super.onNewIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setIntent(null);
	}
}
