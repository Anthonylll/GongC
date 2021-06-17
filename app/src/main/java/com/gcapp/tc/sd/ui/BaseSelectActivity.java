package com.gcapp.tc.sd.ui;

import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.PopupWindowUtil;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.CustomDigitalClock;
import com.gcapp.tc.view.SmanagerView;
import com.gcapp.tc.R;

/**
 * 选号的父类activity，封装头部，底部
 * 
 * @author gaoneng
 * @since 2015/4/8
 */
public abstract class BaseSelectActivity extends FragmentActivity implements
		SensorEventListener {
	/* 头部 */
	RelativeLayout layout_top_select;// 顶部布局
	ImageButton btn_back; // 返回
	LinearLayout layout_select_playtype;// 玩法选择
	ImageView iv_up_down;// 玩法提示图标
	TextView btn_playtype;// 玩法
	ImageButton btn_help;// 帮助

	/* 尾部 */
	TextView btn_clearall; // 清除全部
	Button btn_submit; // 选好了
	TextView tv_tatol_count;// 总注数
	TextView tv_tatol_money;// 总钱数

	/* 显示内容 */
	FrameLayout mFrameLayout;

	protected int mParentIndex = 0;
	protected int mItemIndex = 0;
	private CustomDigitalClock custTime;// 显示截止时间
	private TextView tv_lotteryStoptime;// 倒计时提示
	public Vibrator vibrator; // 震动器
	private SensorManager mSmanager; // 传感器
	float bx = 0;
	float by = 0;
	float bz = 0;
	long btime = 0;// 这一次的时间
	private long vTime = 0; // 震动的时间
	private int position = -1;
	private ConfirmDialog dialog;// 提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_select);
		initAll();
	}

	/**
	 * 初始化所有数据
	 */
	private void initAll() {
		initView();
		App.activityS.add(this);
		App.activityS1.add(this);
		mSmanager = (SensorManager) getSystemService(SENSOR_SERVICE);
		initFrameLayout(mFrameLayout, R.id.container);
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 初始化自定义控件
	 */
	private void initView() {
		/* 头部 */
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);// 顶部布局

		btn_back = (ImageButton) findViewById(R.id.btn_back); // 返回

		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);// 玩法选择

		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);// 玩法提示图标

		btn_playtype = (TextView) findViewById(R.id.btn_playtype);// 玩法

		btn_help = (ImageButton) findViewById(R.id.btn_help);// 帮助

		/* 尾部 */
		btn_clearall = (TextView) findViewById(R.id.btn_clearall); // 清除全部

		btn_submit = (Button) findViewById(R.id.btn_submit); // 选好了

		tv_tatol_count = (TextView) findViewById(R.id.tv_tatol_count);// 总注数

		tv_tatol_money = (TextView) findViewById(R.id.tv_tatol_money);// 总钱数

		/* 显示内容 */
		mFrameLayout = (FrameLayout) findViewById(R.id.container);

	}

	/**
	 * 重写菜单布局
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_base_select, menu);
		return true;
	}

	/**
	 * 重写菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	/**
	 * 初始化mFrameLayout
	 */
	protected abstract void initFrameLayout(FrameLayout container,
			int containerID);

	/**
	 * 初始化玩法选择框
	 */
	protected abstract Map<Integer, Map<Integer, String>> convertPopData();

	/**
	 * 玩法切换
	 */
	protected abstract void changePlayType(int parent, int child);

	/**
	 * 触发摇一摇
	 */
	protected abstract void sensorChanged();

	/**
	 * 是否已选号码
	 */
	protected abstract boolean hasSelected();

	/**
	 * 当前activity没有注销时，返回此activity调用
	 */
	protected abstract void backNewIntent(Intent intent);

	/**
	 * 帮助按钮点击事件
	 */
	public abstract void btn_help(View view);

	/**
	 * 清空按钮点击事件
	 */
	public abstract void btn_clearall(View view);

	/**
	 * 提交按钮点击事件
	 */
	public void btn_submit(View view) {
		if (position != -1 && AppTools.list_numbers != null) {
			AppTools.list_numbers.remove(position);
		}
		submit();
	}

	protected abstract void submit();

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void btn_back(View view) {
		exit();
	}

	/**
	 * 玩法选择
	 * 
	 * @param view
	 */
	public void btn_playtype(View view) {
		swichPlayType();
	}

	/**
	 * 旋转
	 * 
	 * @param type
	 *            1.向上 2.向下
	 */
	private void rote(int type) {
		Animation animation;
		if (1 == type) {
			animation = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rote_playtype_up);
		} else {
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
	 * 切换玩法弹出Popwindow
	 */
	private void swichPlayType() {
		final Map<Integer, Map<Integer, String>> mapMap = convertPopData();
		if (mapMap == null) {
			return;
		}
		PopupWindowUtil popUtil = new PopupWindowUtil(this, mapMap,
				layout_top_select);
		popUtil.setSelectIndex(mParentIndex, mItemIndex);
		popUtil.createPopWindow();
		popUtil.setOnSelectedListener(new PopupWindowUtil.OnSelectedListener() {
			@Override
			public void getIndex(int parentIndex, int itemIndex) {
				if (itemIndex != mItemIndex) {
					mParentIndex = parentIndex;
					mItemIndex = itemIndex;
					setTypeName(mapMap.get(mParentIndex).get(mItemIndex));
					setCountAndMoney(0);
					changePlayType(mParentIndex, mItemIndex);
				}
				rote(2);// 旋转动画 向下
			}
		});
		rote(1);// 旋转动画 向上
	}

	/**
	 * 注册传感器 和 振动器
	 */
	@Override
	protected void onResume() {
		super.onResume();
		SmanagerView.registerSensorManager(mSmanager, getApplicationContext(),
				this);// 注册传感器
	}

	/**
	 * 销毁传感器 和 振动器
	 */
	@Override
	protected void onStop() {
		SmanagerView.unregisterSmanager(mSmanager, this);
		super.onStop();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		backNewIntent(intent);
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
			sensorChanged();
		}
	}

	/**
	 * 退出选号页面
	 */
	private void exit() {
		if (hasSelected()) {
			dialog.show();
			dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						if ((AppTools.list_numbers == null || AppTools.list_numbers
								.size() == 0) && AppTools.totalCount == 0) {
							Intent intent = new Intent(BaseSelectActivity.this,
									MainActivity.class);
							BaseSelectActivity.this.startActivity(intent);
						} else {
							finish();
						}
					}
				}
			});
		} else {
			if ((AppTools.list_numbers == null || AppTools.list_numbers.size() == 0)
					&& AppTools.totalCount == 0) {
				Intent intent = new Intent(BaseSelectActivity.this,
						MainActivity.class);
				BaseSelectActivity.this.startActivity(intent);
			} else {
				finish();
			}
		}
	}

	/**
	 * 设置注数和金额
	 * 
	 * @param count
	 *            注数
	 */
	protected void setCountAndMoney(long count) {
		tv_tatol_count.setText(count + "");
		tv_tatol_money.setText(count * 2 + "");
	}

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
			tv_lotteryStoptime.setText("距离下一期开始: ");
		} else if (custTime.getType() == 3) {
			tv_lotteryStoptime.setText("距本期截止： ");
		}
	}

	/**
	 * 设置彩种名称和开奖号码
	 */
	protected void setNameAndNum() {
		custTime = (CustomDigitalClock) this
				.findViewById(R.id.bet_tv_lotteryEnd);
		tv_lotteryStoptime = (TextView) findViewById(R.id.tv_lotteryStoptime);
		custTime.setEndTime(AppTools.lottery.getDistanceTime());
		custTime.setType(3);
		custTime.setMTickStop(false);
		getCustomTime();
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
							BaseSelectActivity.this, false, 0) {
						@Override
						public void responseCallback(JSONObject reponseJson) {
							String result = AppTools.getDate(reponseJson);
							if ("0".equals(result)) {
								getCustomTime();
							} else if ("-1001".equals(result)) {
								if (RequestUtil.DEBUG) {
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
	 * 是否隐藏箭头
	 */
	protected void setArrowVisible(boolean isGone) {
		iv_up_down.setVisibility(isGone ? View.GONE : View.VISIBLE);
	}

	/**
	 * 设置玩法名称
	 */
	protected void setTypeName(String typename) {
		btn_playtype.setText(typename);
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	/* 提供震动 */
	public void vibrator() {
		if (null != vibrator)
			vibrator.vibrate(300);
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
