package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.fragment.HallFragment;
import com.gcapp.tc.sd.ui.adapter.ListScrollAdapter;
import com.gcapp.tc.sd.ui.adapter.LuckyGridViewAdapter;
import com.gcapp.tc.sd.ui.adapter.LuckyNumberAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.view.MyDateTimeDialog;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyReScrollDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.wheel.widget.NumericWheelAdapter;
import com.gcapp.tc.R;

/**
 * 功能：幸运选号类
 * 
 * @author lenovo
 * 
 */
public class LuckyCenterActivity extends Activity {
	private Context context = LuckyCenterActivity.this;

	ImageButton btn_back;// 返回键

	LinearLayout layout_btn_select_lottery;// 选择彩种

	TextView tv_lottery_name;// 彩种名称

	LinearLayout layout_btn_select_playtype;// 选择玩法

	TextView tv_playtype_name;// 玩法名称

	Button btn_select_number;// 立即选号

	ImageView iv_betting;// 立即投注

	LinearLayout layout_name;// 姓名

	EditText et_name;// 姓名输入框

	LinearLayout layout_lovers;// 情侣

	EditText et_lover1;// 情侣输入框1

	EditText et_lover2;// 情侣输入框2

	RelativeLayout layout_year;//

	RelativeLayout layout_month;//

	RelativeLayout layout_day;//

	LinearLayout layout_birth;// 出生日期

	TextView tv_year;// 年

	TextView tv_month;// 月

	TextView tv_day;// 日

	ImageView iv_light;// 光

	MyGridView gv_constellation;// 星座/属相

	MyListView2 lv_number;// 选号列表

	private int num = 0;
	private String lotteryId;// 彩种id
	private String[] luckNumbers;// 幸运号码
	private int redNum, blueNum, redMax, blueMax;
	private boolean isZero, canZero, canSelect = true;
	private Class _betClass;// 投注页面
	/**
	 * 弹出日期框 *
	 */
	private MyDateTimeDialog dateDialog;
	private MyReScrollDialog selectDialogLottry;
	private MyReScrollDialog selectDialogPlaytype;
	private String content_lottery, content_playtype;
	private int selectType; // 0为彩种，1为玩法
	private final String[] lotteryItems = { "广东11选5", "大乐透", "重庆时时彩", "双色球", "江苏快3" };
	private final String[] playtypeItems = { "幸运星座", "幸运属相", "姓名", "情侣", "出生日期" };
	private int[] xingzuoRes = { R.drawable.baiyang, R.drawable.jinniu_bai,
			R.drawable.shuangzi_bai, R.drawable.juxie_bai,
			R.drawable.shizi_bai, R.drawable.chunv_bai,
			R.drawable.tianchen_bai, R.drawable.tianxie_bai,
			R.drawable.sheshou_bai, R.drawable.moxie_bai,
			R.drawable.shuiping_bai, R.drawable.shuangyu_bai };
	private int[] xingzuoResSelected = { R.drawable.baiyang_hong,
			R.drawable.jinniu_hong, R.drawable.shuangzi_hong,
			R.drawable.juxie_hong, R.drawable.shizi_hong,
			R.drawable.chunv_hong, R.drawable.tianchen_hong,
			R.drawable.tianxie_hong, R.drawable.sheshou_hong,
			R.drawable.moxie_hong, R.drawable.shuiping_hong,
			R.drawable.shuangyu_hong };
	private int[] shuxiangRes = { R.drawable.shu_bai, R.drawable.niu_bai,
			R.drawable.hu_bai, R.drawable.tu_bai, R.drawable.long_bai,
			R.drawable.she_bai, R.drawable.ma_bai, R.drawable.yang_bai,
			R.drawable.hou_bai, R.drawable.ji_bai, R.drawable.gou_bai,
			R.drawable.zhu_bai };
	private int[] shuxiangResSelected = { R.drawable.shu_hong,
			R.drawable.niu_hong, R.drawable.hu_hong, R.drawable.tu_hong,
			R.drawable.long_hong, R.drawable.she_hong, R.drawable.ma_hong,
			R.drawable.yang_hong, R.drawable.hou_hong, R.drawable.ji_hong,
			R.drawable.gou_hong, R.drawable.zhu_hong };
	private LuckyGridViewAdapter adapter;
	private int year, month, days;
	private int playtypeIndex;
	private int indexLottery;
	private LuckyNumberAdapter selectNumAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lucky_center);
		init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		// 初始化控件
		initView();
		// 得到Calendar类的实例。
		Calendar now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		days = now.get(Calendar.DATE);
		NumericWheelAdapter mYearAdapter = new NumericWheelAdapter(1880, 2050);
		NumericWheelAdapter mMonthAdapter = new NumericWheelAdapter(1, 12);
		NumericWheelAdapter mDayAdapter = new NumericWheelAdapter(1,
				AppTools.getLastDay(year, month));

		dateDialog = new MyDateTimeDialog(context, R.style.dialog,
				mYearAdapter, mMonthAdapter, mDayAdapter, new MyClickListener());
		dateDialog.initDay(year, month, days);
		ListScrollAdapter mLotteryAdapter = new ListScrollAdapter(1,
				lotteryItems.length, lotteryItems);
		ListScrollAdapter mPlaytypeAdapter = new ListScrollAdapter(1,
				playtypeItems.length, playtypeItems);
		selectDialogLottry = new MyReScrollDialog(context, R.style.dialog,
				mLotteryAdapter, new MyClickListener1(), "请选择彩种");
		content_lottery = lotteryItems[0];
		tv_lottery_name.setText(content_lottery);
		selectDialogLottry.initShowContent(content_lottery);
		selectDialogPlaytype = new MyReScrollDialog(context, R.style.dialog,
				mPlaytypeAdapter, new MyClickListener1(), "请选择玩法");
		content_playtype = playtypeItems[0];
		selectPlayType(0);
		setRandom(0);
		adapter = new LuckyGridViewAdapter(context, xingzuoRes,
				xingzuoResSelected);
		gv_constellation.setAdapter(adapter);
		adapter.setOnSelectedChangedListener(new LuckyGridViewAdapter.OnSelectedChangedListener() {
			@Override
			public void isChanged(boolean ischanged) {
				if (ischanged)
					clearSelectNum();
			}
		});
		tv_playtype_name.setText(content_playtype);
		selectDialogPlaytype.initShowContent(content_playtype);
		et_name.addTextChangedListener(new MyTextWatch());
		et_lover1.addTextChangedListener(new MyTextWatch());
		et_lover2.addTextChangedListener(new MyTextWatch());
	}

	/**
	 * 初始化自定义控件
	 */
	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);// 返回键

		layout_btn_select_lottery = (LinearLayout) findViewById(R.id.layout_btn_select_lottery);// 选择彩种

		tv_lottery_name = (TextView) findViewById(R.id.tv_lottery_name);// 彩种名称

		layout_btn_select_playtype = (LinearLayout) findViewById(R.id.layout_btn_select_playtype);// 选择玩法

		tv_playtype_name = (TextView) findViewById(R.id.tv_playtype_name);// 玩法名称

		btn_select_number = (Button) findViewById(R.id.btn_select_number);// 立即选号

		iv_betting = (ImageView) findViewById(R.id.iv_betting);// 立即投注

		layout_name = (LinearLayout) findViewById(R.id.layout_name);// 姓名

		et_name = (EditText) findViewById(R.id.et_name);// 姓名输入框

		layout_lovers = (LinearLayout) findViewById(R.id.layout_lovers);// 情侣

		et_lover1 = (EditText) findViewById(R.id.et_lover1);// 情侣输入框1

		et_lover2 = (EditText) findViewById(R.id.et_lover2);// 情侣输入框2

		layout_year = (RelativeLayout) findViewById(R.id.layout_year);//

		layout_month = (RelativeLayout) findViewById(R.id.layout_month);//

		layout_day = (RelativeLayout) findViewById(R.id.layout_day);//

		layout_birth = (LinearLayout) findViewById(R.id.layout_birth);// 出生日期

		tv_year = (TextView) findViewById(R.id.tv_year);// 年

		tv_month = (TextView) findViewById(R.id.tv_month);// 月

		tv_day = (TextView) findViewById(R.id.tv_day);// 日

		iv_light = (ImageView) findViewById(R.id.iv_light);// 光

		gv_constellation = (MyGridView) findViewById(R.id.gv_constellation);// 星座/属相

		lv_number = (MyListView2) findViewById(R.id.lv_number);// 选号列表
	}

	/**
	 * 文本改变监听
	 * 
	 * @author lenovo
	 * 
	 */
	class MyTextWatch implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			clearSelectNum();
		}
	}

	/**
	 * 改 不能使用ButterKnife，方法无效，在xml中设置onClick方法
	 */
	public void back(View view) {
		this.finish();
	}

	/**
	 * 选择彩种
	 */

	public void selectLottery(View view) {
		selectType = 0;
		selectDialogLottry.show()	;
	}

	/**
	 * 选择玩法
	 */

	public void selectPlaytype(View view) {
		selectType = 1;
		selectDialogPlaytype.show()	;
	}

	/**
	 * 选择日期 年
	 */
	public void selectY(View view) {
		dateDialog.show()	;
	}

	/**
	 * 选择日期 月
	 */
	public void selectM(View view) {
		dateDialog.show()	;
	}

	/**
	 * 选择日期 日
	 */
	public void selectD(View view) {
		dateDialog.show()	;
	}

	/**
	 * 选择玩法
	 * 
	 * @param index
	 *            ：类型参数
	 */
	private void selectPlayType(int index) {
		clearSelectNum();
		setShowView(index);// 显示布局
		switch (index) {
		case 0:
		case 1:
			int[] ids;
			int[] idsSelected;
			if (0 == index) {
				ids = xingzuoRes;
				idsSelected = xingzuoResSelected;
			} else {
				ids = shuxiangRes;
				idsSelected = shuxiangResSelected;
			}
			adapter = new LuckyGridViewAdapter(context, ids, idsSelected);
			adapter.setOnSelectedChangedListener(new LuckyGridViewAdapter.OnSelectedChangedListener() {
				@Override
				public void isChanged(boolean ischanged) {
					if (ischanged)
						clearSelectNum();
				}
			});
			gv_constellation.setAdapter(adapter);
			break;
		}
	}

	/**
	 * 根据选项刷新界面
	 * 
	 * @param index
	 *            ：参数
	 */
	private void setShowView(int index) {
		gv_constellation.setVisibility(View.GONE);
		layout_name.setVisibility(View.GONE);
		layout_lovers.setVisibility(View.GONE);
		layout_birth.setVisibility(View.GONE);
		switch (index) {
		case 0:
		case 1:
			gv_constellation.setVisibility(View.VISIBLE);
			break;
		case 2:
			layout_name.setVisibility(View.VISIBLE);
			break;
		case 3:
			layout_lovers.setVisibility(View.VISIBLE);
			break;
		case 4:
			layout_birth.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 清空所选号码
	 */
	public void clearSelectNum() {
		if (null != luckNumbers && luckNumbers.length != 0) {
			luckNumbers = new String[] {};
			selectNumAdapter.setLotteryNumber(luckNumbers);
			selectNumAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 根据下标得到机选号码
	 * 
	 * @param positon
	 *            ：下标
	 */
	private void setRandom(int positon) {
		clearSelectNum();
		switch (positon) {
		/** 快赢481 **/
		case 3:
			lotteryId = "5";
			_betClass = Bet_SSQ_Activity.class;
			setRandom(6, 1, 33, 16, true, false);
			break;
		/** 大乐透 */
		case 1:
			lotteryId = "39";
			_betClass = Bet_DLT_Activity.class;
			setRandom(5, 2, 35, 12, true, false);
			break;
		/** 时时彩 */
		case 2:
			lotteryId = "28";
			_betClass = Bet_SSC_Activity.class;
			setRandom(5, 0, 9, 0, false, true);
			break;
		/* 广东11选5 */
		case 0:
			lotteryId = "78";
			_betClass = Bet_11x5_Activity.class;
			setRandom(5, 0, 11, 0, true, false);
			break;
		/** 快3 **/
		case 4:
			lotteryId = "83";
			_betClass = Bet_k3_Activity.class;
			setRandom(3, 0, 6, 0, false, false);
			break;

		default:
			break;
		}
		/** 拿到彩种信息 */
		for (Lottery lottery : HallFragment.listLottery) {
			if (lottery.getLotteryID().equals(lotteryId)) {
				AppTools.lottery = lottery;
			}
		}
		canSelect = AppTools.lottery.getDistanceTime()
				- System.currentTimeMillis() > 0;
	}

	/**
	 * 机选参数
	 * 
	 * @param _redNume
	 *            ：红球个数
	 * @param _blueNum
	 *            ：蓝球个数
	 * @param _redMax
	 *            ：最大红球值
	 * @param _blueMax
	 *            ：最大蓝球值
	 * @param _isZero
	 *            ：是否加0
	 * @param _canZero
	 *            ：是否包含0
	 */
	private void setRandom(int _redNume, int _blueNum, int _redMax,
			int _blueMax, boolean _isZero, boolean _canZero) {
		this.redNum = _redNume;
		this.blueNum = _blueNum;
		this.redMax = _redMax;
		this.blueMax = _blueMax;
		this.isZero = _isZero;
		this.canZero = _canZero;
	}

	/**
	 * 立即选号
	 */
	public void selectNumber(View view) {
		if (canSelect) {
			if (check()) {
				startAni();
			}
		} else
			MyToast.getToast(context, "该彩种奖期已结束，请选择其他彩种");
	}

	/**
	 * 验证输入值是否正确
	 * 
	 * @return
	 */
	private boolean check() {
		boolean isOK = true;
		switch (playtypeIndex) {
		case 0:// 星座
			break;
		case 1:// 属相
			break;
		case 2:// 姓名
			String name = et_name.getText().toString();
			if ("".equals(name)) {
				isOK = false;
				MyToast.getToast(context, "请输入姓名");
			}
			break;
		case 3:// 情侣
			String lover1 = et_lover1.getText().toString();
			String lover2 = et_lover2.getText().toString();
			if ("".equals(lover1) || "".equals(lover2)) {
				isOK = false;
				MyToast.getToast(context, "请输入情侣姓名");
			}
			break;
		case 4:// 生日
			String year = tv_year.getText().toString();
			String month = tv_month.getText().toString();
			String day = tv_day.getText().toString();
			if ("".equals(year) || "".equals(month) || "".equals(day)) {
				isOK = false;
				MyToast.getToast(context, "请选择出生日期");
			}
			break;
		}
		return isOK;
	}

	/**
	 * 立即投注
	 */

	public void goBetting(View view) {
		if (null != luckNumbers && luckNumbers.length != 0) {
			Intent intent = new Intent(context, _betClass);
			context.startActivity(intent);
		} else {
			MyToast.getToast(context, "请选择幸运号码");
		}
	}

	/**
	 * 将投注号码格式化
	 * 
	 * @param numbers
	 *            ：投注号码
	 */
	private void setList_numbers(String[] numbers) {
		if (null == AppTools.list_numbers)
			AppTools.list_numbers = new ArrayList<SelectedNumbers>();
		AppTools.totalCount = num;

		switch (Integer.parseInt(lotteryId)) {
		case 5:
		case 39:
			SelectedNumbers sNumbers;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers = new SelectedNumbers();
				sNumbers.setCount(1);
				sNumbers.setLotteryId(lotteryId);
				sNumbers.setMoney(sNumbers.getCount() * 2);

				String s = numbers[i].split("-")[0].replace("  ", ",").replace(
						" ", "");
				String s2[] = s.split(",");
				List<String> list = new ArrayList<String>();
				Collections.addAll(list, s2);
				sNumbers.setRedNumbers(list);
				list.clear();
				String ss = numbers[i].split("-")[1].replace("  ", ",")
						.replace(" ", "");
				String ss2[] = ss.split(",");
				Collections.addAll(list, ss2);
				sNumbers.setBlueNumbers(list);
				sNumbers.setPlayType(Integer.parseInt(lotteryId + "01"));
				sNumbers.setLotteryNumber(numbers[i].trim().replace("  ", " ")
						.replace("- ", "-"));
				AppTools.list_numbers.add(sNumbers);
			}
			break;
		case 63:
		case 64:
		case 6:
		case 3:
		case 69:
		case 83:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers2;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers2 = new SelectedNumbers();
				sNumbers2.setCount(1);
				sNumbers2.setLotteryId(lotteryId);
				sNumbers2.setMoney(sNumbers2.getCount() * 2);
				sNumbers2.setPlayType(Integer.parseInt(lotteryId + "01"));
				if (lotteryId.equals("83")) {
					sNumbers2.setPlayType(Integer.parseInt(lotteryId + "06"));
				}
				sNumbers2.setShowLotteryNumber(numbers[i].replace("  ", " ")
						.trim());
				sNumbers2.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(sNumbers2.getPlayType(),
								sNumbers2.getShowLotteryNumber()));
				if (lotteryId.equals("6")) {
					sNumbers2.setLotteryNumber(numbers[i].replace("  ", ",")
							.trim());
				}
				AppTools.list_numbers.add(sNumbers2);
			}
			break;
		case 62:
		case 70:
		case 78:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers4 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers4 = new SelectedNumbers();
				sNumbers4.setCount(1);
				sNumbers4.setLotteryId(lotteryId);
				sNumbers4.setMoney(sNumbers4.getCount() * 2);

				sNumbers4.setPlayType(Integer.parseInt(lotteryId + "05"));

				sNumbers4.setShowLotteryNumber(numbers[i].replace("  ", " ")
						.trim());

				sNumbers4.setNumber(numbers[i].replace("  ", " ").trim());

				sNumbers4.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(sNumbers4.getPlayType(),
								sNumbers4.getShowLotteryNumber()));

				AppTools.list_numbers.add(sNumbers4);
			}
			break;
		case 28:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers5 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers5 = new SelectedNumbers();
				sNumbers5.setCount(1);
				sNumbers5.setLotteryId(lotteryId);
				sNumbers5.setMoney(sNumbers5.getCount() * 2);
				sNumbers5.setType(7);
				sNumbers5.setPlayType(Integer.parseInt(lotteryId + "03"));

				sNumbers5.setShowLotteryNumber(numbers[i].replace("  ", "")
						.trim());

				sNumbers5.setNumber(numbers[i].replace("  ", "-").trim());

				sNumbers5.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(sNumbers5.getPlayType(),
								sNumbers5.getShowLotteryNumber()));

				AppTools.list_numbers.add(sNumbers5);
			}
			break;

		case 66:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumber = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumber = new SelectedNumbers();
				sNumber.setCount(1);
				sNumber.setLotteryId(lotteryId);
				sNumber.setMoney(sNumber.getCount() * 2);
				sNumber.setType(7);
				sNumber.setPlayType(Integer.parseInt(lotteryId + "03"));

				sNumber.setShowLotteryNumber(numbers[i].replace("  ", "")
						.trim());

				sNumber.setNumber(numbers[i].replace("  ", "-").trim());

				sNumber.setLotteryNumber(NumberTools
						.lotteryNumberFormatConvert(sNumber.getPlayType(),
								sNumber.getShowLotteryNumber()));
				AppTools.list_numbers.add(sNumber);
			}
			break;

		case 61:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbersjxssc = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbersjxssc = new SelectedNumbers();
				sNumbersjxssc.setCount(1);
				sNumbersjxssc.setLotteryId(lotteryId);
				sNumbersjxssc.setMoney(sNumbersjxssc.getCount() * 2);
				sNumbersjxssc.setPlayType(Integer.parseInt(lotteryId + "05"));
				String[] selectNum = numbers[i].replace(" ", "").trim()
						.split("");
				StringBuffer sb = new StringBuffer();
				for (int j = 1; j < selectNum.length; j++) {
					sb.append("(" + selectNum[j] + ")");
				}
				sNumbersjxssc.setLotteryNumber(sb.toString().trim());
				sNumbersjxssc.setPlayTypeName("五星直选");
				AppTools.list_numbers.add(sNumbersjxssc);
			}
			break;

		case 13:
			AppTools.list_numbers.clear();
			SelectedNumbers sNumbers3 = null;
			for (int i = 0; i < numbers.length; i++) {
				sNumbers3 = new SelectedNumbers();
				sNumbers3.setCount(1);
				sNumbers3.setLotteryId(lotteryId);
				sNumbers3.setMoney(sNumbers3.getCount() * 2);

				sNumbers3.setPlayType(Integer.parseInt(lotteryId + "01"));

				sNumbers3
						.setLotteryNumber(numbers[i].replace("  ", " ").trim());

				String[] s = sNumbers3.getLotteryNumber().split(" ");
				List<String> redList = new ArrayList<String>();
				Collections.addAll(redList, s);
				sNumbers3.setRedNumbers(redList);
				AppTools.list_numbers.add(sNumbers3);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 监听方法
	 * 
	 * @author lenovo
	 * 
	 */
	class MyClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.funds_btn_ok:
				if ("".equals(tv_year.getText().toString())
						|| "".equals(tv_month.getText().toString())
						|| "".equals(tv_day.getText().toString())) {
					setDays(dateDialog.y, dateDialog.m, dateDialog.d);
				}
				if (LuckyCenterActivity.this.year != dateDialog.y
						|| LuckyCenterActivity.this.month != dateDialog.m
						|| LuckyCenterActivity.this.days != dateDialog.d) {
					setDays(dateDialog.y, dateDialog.m, dateDialog.d);
					clearSelectNum();
				}
				dateDialog.dismiss();
				break;
			case R.id.funds_btn_no:
				dateDialog.dismiss();
				break;
			}
			setDays();
			dateDialog.setCheckItem();

		}
	}

	/**
	 * 确认选号的动画效果
	 */
	public void startAni() {
		float xLength = 160;
		xLength = AppTools.getDpi(this) * xLength;
		Animation translateIn = new TranslateAnimation(0, xLength, 0, 0);
		translateIn.setDuration(1000);
		translateIn.setFillAfter(true);
		iv_light.startAnimation(translateIn);
		translateIn.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				clearSelectNum();
				setEnabled(false);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setEnabled(true);
				iv_light.clearAnimation();
				if (null != AppTools.list_numbers)
					AppTools.list_numbers.clear();
				num = 5;
				luckNumbers = new String[num];
				luckNumbers = NumberTools.getRandom(num, redNum, blueNum,
						redMax, blueMax, isZero, canZero);
				setList_numbers(luckNumbers);
				selectNumAdapter = new LuckyNumberAdapter(context, luckNumbers);
				lv_number.setAdapter(selectNumAdapter);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	/**
	 * 设置不可用
	 * 
	 * @param enabled
	 *            ：设置可用不可用参数
	 */
	public void setEnabled(boolean enabled) {
		btn_back.setEnabled(enabled);
		layout_btn_select_lottery.setEnabled(enabled);
		layout_btn_select_playtype.setEnabled(enabled);
		gv_constellation.setEnabled(enabled);
		adapter.setItemEnableed(enabled);
		layout_name.setEnabled(enabled);
		layout_lovers.setEnabled(enabled);
		layout_birth.setEnabled(enabled);
		btn_select_number.setEnabled(enabled);
		iv_betting.setEnabled(enabled);
		et_name.setEnabled(enabled);
		et_lover1.setEnabled(enabled);
		et_lover2.setEnabled(enabled);
		layout_year.setEnabled(enabled);
		layout_month.setEnabled(enabled);
		layout_day.setEnabled(enabled);
	}

	/**
	 * 彩种和玩法dialog 点击监听
	 */
	class MyClickListener1 implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.funds_btn_ok1: // 确定
				if (0 == selectType) { // 彩种
					selectDialogLottry.dismiss();
					content_lottery = selectDialogLottry.showContent;
					tv_lottery_name.setText(content_lottery);
					for (int i = 0; i < lotteryItems.length; i++) {
						if (lotteryItems[i].equals(content_lottery)) {
							if (indexLottery != i) {
								setRandom(i);
								indexLottery = i;
							}
						}
					}
				} else if (1 == selectType) { // 玩法
					selectDialogPlaytype.dismiss();
					content_playtype = selectDialogPlaytype.showContent;
					tv_playtype_name.setText(content_playtype);
					for (int i = 0; i < playtypeItems.length; i++) {
						if (playtypeItems[i].equals(content_playtype)) {
							if (playtypeIndex != i) {
								selectPlayType(i);
								playtypeIndex = i;
							}
						}
					}
				}
				break;
			case R.id.funds_btn_no1: // 取消
				if (0 == selectType) {// 彩种
					if (null != selectDialogLottry
							&& selectDialogLottry.isShowing())
						selectDialogLottry.dismiss();
				} else if (1 == selectType) {// 玩法
					if (null != selectDialogPlaytype
							&& selectDialogPlaytype.isShowing())
						selectDialogPlaytype.dismiss();
				}
				break;
			}
			if (0 == selectType) {
				if (selectDialogLottry != null) {
					selectDialogLottry.initShowContent(content_lottery);
					selectDialogLottry.setCheckItem();
				}
			} else if (1 == selectType) {
				if (selectDialogPlaytype != null) {
					selectDialogPlaytype.initShowContent(content_playtype);
					selectDialogPlaytype.setCheckItem();
				}
			}
		}
	}

	/**
	 * 设置dialog的日期
	 */
	public void setDays() {
		dateDialog.initDay(year, month, days);
	}

	/**
	 * 设置日期
	 * 
	 * @param y
	 *            ：年
	 * @param m
	 *            ：月
	 * @param d
	 *            ：日
	 */
	public void setDays(int y, int m, int d) {
		// 得到Calendar类的实例。
		Calendar now = Calendar.getInstance();
		int nowyear = now.get(Calendar.YEAR);
		int nowmonth = now.get(Calendar.MONTH) + 1;
		int nowdays = now.get(Calendar.DATE);
		boolean istrue = dateDialog.compareData(nowyear, nowmonth, nowdays, y,
				m, d);
		if (istrue) {
			this.year = y;
			this.month = m;
			this.days = d;
			tv_year.setText(year + "");
			tv_month.setText(month + "");
			tv_day.setText(days + "");
		} else {
			tv_year.setText("");
			tv_month.setText("");
			tv_day.setText("");
			MyToast.getToast(getApplicationContext(), "您选择的出生日期超出当前时间，请重新选择")	;
				
		}
	}

	/**
	 * 销毁activity
	 */
	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

}
