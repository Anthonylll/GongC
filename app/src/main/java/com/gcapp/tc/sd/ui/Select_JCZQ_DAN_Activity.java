package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.DtMatch;
import com.gcapp.tc.fragment.DtMatchFragmentJczq_passSingle;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapterJCZQPassMore;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapterJCZQPassSingle;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.PopupWindowUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.ScreenDtMatchPopupWindow;
import com.gcapp.tc.R;

/**
 * 功能：竞彩足球的单关选号界面
 * 
 * @author lenovo
 * 
 */
public class Select_JCZQ_DAN_Activity extends FragmentActivity implements
		OnClickListener {
	private final static String TAG = "Select_JCZQDAN_Activity";
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private RelativeLayout layout_main;// 主布局
	private ImageButton btn_back; // 返回
	private ImageButton btn_screen; // 筛选
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框
	private Animation animation = null;

	private Bundle bundle;
	private int playtype = 7207;// 玩法
	private int passtype = 1;// 过关方式 0.过多关 1.过单关
	// 选玩法控件
	private PopupWindowUtil popUtil;
	private Map<Integer, Map<Integer, String>> data = new HashMap<Integer, Map<Integer, String>>();
	private int parentIndex;
	private int itemIndex;
	public DtMatchFragmentJczq_passSingle fragment;
	private FragmentManager fragmentManager;
	private static List<List<DtMatch>> passMoreDMList = new ArrayList<List<DtMatch>>();
	private static List<List<DtMatch>> passSingleDMList = new ArrayList<List<DtMatch>>();
	private ScreenDtMatchPopupWindow screenPop;
	private ArrayList<String> passSingleDMName = new ArrayList<String>();

	private Button btn_playhelp, btn_sort;// 玩法说明，赛事筛选
	private PopupWindow popWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_jczq);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		init();
		setListener();
		fragmentManager = getSupportFragmentManager();
		fragment = DtMatchFragmentJczq_passSingle.newInstance(this, playtype,
				passtype, passMoreDMList, passSingleDMList);
		fragmentManager.beginTransaction().add(R.id.fl_jczq, fragment).commit();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		bundle = new Bundle();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_screen = (ImageButton) findViewById(R.id.btn_screen);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		layout_main = (RelativeLayout) findViewById(R.id.layout_main);
	}

	/**
	 * 返回该页面时刷新数据
	 */
	@Override
	protected void onResume() {
		// TODO 实现跳转过来屏幕为空功能
		boolean isEmpty = getIntent().getBooleanExtra("isEmpty", false);
		if (isEmpty) {
			changeFragment();
		}
		fragment.update();
		super.onResume();
	}

	/**
	 * 初始化数据和玩法属性
	 */
	private void init() {
		if (isEmptJCZQ()) {
			passMoreDMList = AppTools.list_Matchs;
			passSingleDMList = AppTools.list_singlepass_Matchs;
		}
		playtype = getIntent().getIntExtra("playtype", 7207);
		passtype = getIntent().getIntExtra("passtype", 0);

		for (Integer i : ExpandAdapterJCZQPassMore.map_hashMap_cbf.keySet()) {
			ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(i);
			for (Integer j : ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(i)
					.keySet()) {
				ArrayList<String> list = ExpandAdapterJCZQPassMore.map_hashMap_cbf
						.get(i).get(j);
				for (int k = 0; k < list.size(); k++) {
					Log.i(TAG, list.get(k));
				}
			}
		}
		if (!isEmptJCZQ()) {
			iv_up_down.setEnabled(false);
			btn_playtype.setEnabled(false);
			layout_select_playtype.setEnabled(false);
		}
		Map<Integer, String> playType = new HashMap<Integer, String>();
		// playType.put(0, "混合投注");
		playType.put(0, "胜平负");
		playType.put(1, "让球胜平负");
		playType.put(2, "总进球");
		playType.put(3, "比分");
		playType.put(4, "半全场");
		data.put(0, playType);
		int index = 0;
		switch (playtype) {
		case 7207:// 胜平负
			index = 0;
			break;
		case 7201:// 让球胜平负
			index = 1;
			break;
		case 7203:// 总进球
			index = 2;
			break;
		case 7202:// 比分
			index = 3;
			break;
		case 7204:// 半全场
			index = 4;
			break;
		}
		btn_playtype.setText(playType.get(index));
		dialog = new ConfirmDialog(this, R.style.dialog);
		// btn_screen.setVisibility(View.VISIBLE);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		// 绑定Adapter
		btn_back.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		iv_up_down.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		btn_screen.setOnClickListener(this);
		btn_help.setOnClickListener(this);
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

		/** 玩法说明 **/
		case R.id.btn_help:
			createPopWindow();
			popWindow.showAsDropDown(v);
			backgroundAlpaha(Select_JCZQ_DAN_Activity.this, 0.5f);
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
					if (Select_JCZQ_DAN_Activity.this.itemIndex != itemIndex) {
						Select_JCZQ_DAN_Activity.this.parentIndex = parentIndex;
						Select_JCZQ_DAN_Activity.this.itemIndex = itemIndex;
						changePlayType();
					}
					rote(2);// 旋转动画 向下
				}
			});
			rote(1);// 旋转动画 向上
			break;
		}
	}

	/**
	 * 创建popWindow
	 */

	private void createPopWindow() {
		LayoutInflater inflact = LayoutInflater
				.from(Select_JCZQ_DAN_Activity.this);
		View view = inflact.inflate(R.layout.pop_item_jc, null);
		btn_playhelp = (Button) view.findViewById(R.id.btn_playhelp);
		btn_sort = (Button) view.findViewById(R.id.btn_sort);

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
				backgroundAlpaha(Select_JCZQ_DAN_Activity.this, 1.0f);

				// TODO Auto-generated method stub
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
						backgroundAlpaha(Select_JCZQ_DAN_Activity.this, 1.0f);
					}
					break;
				}
				return true;
			}
		};

		btn_playhelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playExplain();
				popWindow.dismiss();
				backgroundAlpaha(Select_JCZQ_DAN_Activity.this, 1.0f);
			}
		});
		btn_sort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				screenDMList();
				popWindow.dismiss();
				backgroundAlpaha(Select_JCZQ_DAN_Activity.this, 1.0f);
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
	 * 设置添加屏幕的背景透明度
	 * **/
	public void backgroundAlpaha(Activity context, float bgAlpha) {
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		context.getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		context.getWindow().setAttributes(lp);
	}

	/**
	 * 筛选对阵
	 */
	public void screenDMList() {
		screenPop = new ScreenDtMatchPopupWindow(this,
				AppTools.list_singlepass_Matchs, layout_main);
		if (0 != passSingleDMName.size()) {// 不为空
			screenPop.setSelectDMName(passSingleDMName);
		}
		screenPop.createPopWindow();
		screenPop
				.setDialogResultListener(new ScreenDtMatchPopupWindow.DialogResultListener() {
					@Override
					public void getResult(int resultCode,
							List<List<DtMatch>> screenMatch_list,
							ArrayList<String> select_name_list) {
						if (1 == resultCode) {// 确定
							passSingleDMName = select_name_list;
							passSingleDMList = screenMatch_list;
							passtype = 1;
							changeFragment();
						}
					}
				});
	}

	/**
	 * 点击不同玩法选择更新界面数据
	 */
	public void changePlayType() {
		btn_playtype.setText(data.get(parentIndex).get(itemIndex));
		switch (itemIndex) {

		case 0:// 胜平负
			playtype = 7207;
			break;
		case 1:// 让球胜平负
			playtype = 7201;
			break;
		case 2:// 总进球
			playtype = 7203;
			break;
		case 3:// 比分
			playtype = 7202;
			break;
		case 4:// 半全场
			playtype = 7204;
			break;
		}
		changeFragment();
	}

	/**
	 * 清空界面的选中状态
	 */
	public void changeFragment() {
		// 清空过关和单关的所有数据
		ExpandAdapterJCZQPassMore.clearSelectMap();
		ExpandAdapterJCZQPassSingle.clearSelectMap();
		fragment = DtMatchFragmentJczq_passSingle.newInstance(this, playtype,
				passtype, passMoreDMList, passSingleDMList);
		fragmentManager.beginTransaction().replace(R.id.fl_jczq, fragment)
				.commit();
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (animation != null && iv_up_down != null && animation.hasStarted()) {
			iv_up_down.clearAnimation();
			iv_up_down.startAnimation(animation);
		}
	}

	/**
	 * 玩法说明
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_JCZQ_DAN_Activity.this,
				PlayDescription.class);
		Select_JCZQ_DAN_Activity.this.startActivity(intent);
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
	 * 退出当前界面
	 */
	private void exit() {
		if (!isEmptJCZQ()) {
			dialog.show();
			dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
			dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
				@Override
				public void getResult(int resultCode) {
					if (1 == resultCode) {// 确定
						ExpandAdapterJCZQPassMore.clearAllDate();
						ExpandAdapterJCZQPassSingle.clearAllDate();
						Select_JCZQ_DAN_Activity.this.finish();
						for (int i = 0; i < App.activityS1.size(); i++) {
							App.activityS1.get(i).finish();
						}
					}
				}
			});
		} else {
			ExpandAdapterJCZQPassMore.clearAllDate();
			ExpandAdapterJCZQPassSingle.clearAllDate();
			Select_JCZQ_DAN_Activity.this.finish();
			for (int i = 0; i < App.activityS1.size(); i++) {
				App.activityS1.get(i).finish();
			}
		}
	}

	/**
	 * 清空选中情况
	 */
	public static void clearHashSet() {
		ExpandAdapterJCZQPassMore.clearAllDate();
		ExpandAdapterJCZQPassSingle.clearAllDate();
	}

	/**
	 * 销毁activity
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * 判断竞彩足球的所有map是否为空
	 * 
	 * @return
	 */
	public static boolean isEmptJCZQ() {
		if (0 == ExpandAdapterJCZQPassMore.map_hashMap_spf.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_bqc.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_cbf.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_hhtz.size()
				&& 0 == ExpandAdapterJCZQPassMore.map_hashMap_zjq.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_spf.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_bqc.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_cbf.size()
				&& 0 == ExpandAdapterJCZQPassSingle.map_hashMap_zjq.size()) {
			return true;
		} else {
			return false;
		}
	}
}
