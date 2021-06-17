package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.fragment.CJLQMatchItemFragment;
import com.gcapp.tc.fragment.DtMatchFragmentJCLQ;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapter_jclq_52;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyJCDialog_jclq;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能：竞彩篮球的过关选号界面
 * 
 * @author lenovo
 */
public class Select_JCLQ_Activity extends FragmentActivity implements
		View.OnClickListener,
		CJLQMatchItemFragment.OnFragmentInteractionListener,
		DtMatchFragmentJCLQ.OnFatherFragmentInteractionListener {
	/* 头部 */
	private ImageButton btn_back; // 返回
	private ImageButton btn_screen; // 筛选
	private ImageView iv_up_down;// 玩法提示图标
	private LinearLayout layout_select_playtype;// 玩法选择
	private TextView btn_playtype;// 玩法
	private Animation animation = null;
	private ImageButton btn_playinfo;// 帮助
	/* 尾部 */
	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 选好了
	private TextView tv_tip_jc;

	public int total = 0;
	private int ways = 0; // 0：过关 ; 1:单关
	private Set<String> set;
	/**
	 * 筛选出的过关赛事
	 */
	private List<String> guoguan_set;
	/**
	 * 筛选出的单关赛事
	 */
	private List<String> danguan_set;
	public static RelativeLayout select_jclq_title;
	/**
	 * 7301胜负 7302让分胜负 7303胜分差 7404 大小分 7306 混合过关
	 */
	private String[] type_name = { "混合过关", "胜负", "让分胜负", "胜分差", "大小分", "" };// “混合过关、胜负、让分胜负、胜负差、大小分（默认为混合过关）”
	private ConfirmDialog dialog;// 提示框
	private int type = 7301;
	private boolean isChecked = false;
	public PopupWindow popWindow;
	private MyJCDialog_jclq myDialog;
	private MyPlayTypeAdapter adapter;
	private DtMatchFragmentJCLQ fragment;
	private android.support.v4.app.FragmentManager fragmentManager;
	// 存储过关筛选出的对阵
	public static List<List<DtMatch_Basketball>> list_Matchs = new ArrayList<List<DtMatch_Basketball>>();
	// 存储单关筛选出的对阵
	public static List<List<DtMatch_Basketball>> list_Matchs_single = new ArrayList<List<DtMatch_Basketball>>();

	private Button btn_playhelp, btn_sort;// 玩法说明，赛事筛选
	private PopupWindow popWindown_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_jclq_activity52);
		App.activityS.add(this);
		App.activityS1.add(this);
		findView();
		setListener();
		init();
	}

	/**
	 * 初始化UI界面控件
	 */
	private void findView() {
		layout_select_playtype = (LinearLayout) this
				.findViewById(R.id.layout_select_playtype);
		btn_screen = (ImageButton) this.findViewById(R.id.btn_screen);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		btn_playtype = (TextView) this.findViewById(R.id.btn_playtype);
		btn_playinfo = (ImageButton) this.findViewById(R.id.btn_help);

		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		tv_tip_jc = (TextView) findViewById(R.id.tv_tip_jc);

		select_jclq_title = (RelativeLayout) findViewById(R.id.select_jclq_title);
		// 把筛选的队存起来
		set = new HashSet<String>();// 多关赛事 存“欧冠”等
		guoguan_set = new ArrayList<String>();
		danguan_set = new ArrayList<String>();

		if (AppTools.DtMatch_Basketball == null
				|| AppTools.DtMatch_Basketball_single == null) {
			MyToast.getToast(this, "竞彩数据为空");
		} else {
			// 取多关
			for (List<DtMatch_Basketball> listMatch : AppTools.DtMatch_Basketball) {

				for (DtMatch_Basketball match : listMatch) {
					if (!set.contains(match.getGame())) {
						set.add(match.getGame());
					}
					if (!guoguan_set.contains(match.getGame())) {
						guoguan_set.add(match.getGame());
					}
				}
			}
			// 取单关
			for (List<DtMatch_Basketball> listMatch : AppTools.DtMatch_Basketball_single) {
				for (DtMatch_Basketball match : listMatch) {
					if (!danguan_set.contains(match.getGame())) {
						danguan_set.add(match.getGame());
					}
				}
			}
		}
		type = getIntent().getIntExtra("playType", 7306);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_playinfo.setOnClickListener(this);
		btn_screen.setOnClickListener(this);
		btn_playtype.setOnClickListener(this);
		layout_select_playtype.setOnClickListener(this);
		btn_playinfo.setOnClickListener(this);
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	/**
	 * 初始化数据和属性
	 */
	private void init() {
		isChecked = getIntent().getBooleanExtra("canChange", true);
		myDialog = new MyJCDialog_jclq(this, set, R.style.dialog_screen);
		dialog = new ConfirmDialog(this, R.style.dialog);
		fragment = DtMatchFragmentJCLQ.newInstance(type, 0);
		fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.add(R.id.cjlq_fragment_container, fragment)
				.commitAllowingStateLoss();
		btn_playtype.setText(type_name[0]);
		changeTextShow(0);
	}

	/**
	 * 重写菜单创建方法
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_select_jclq_activity52, menu);
		return true;
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 筛选 **/
		case R.id.btn_screen:
			screenDMList();
			break;

		case R.id.btn_back:
			onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(10, 10));
			break;

		/** 点击下拉框 **/
		case R.id.layout_select_playtype:
		case R.id.btn_playtype:
			if (isChecked) {
				createPopWindow();
				popWindow.showAsDropDown(select_jclq_title);
			}
			break;

		case R.id.btn_help:
			newPopWindow();
			popWindown_title.showAsDropDown(v);
			backgroundAlpaha(Select_JCLQ_Activity.this, 0.5f);
			break;

		/** 点击选好了 **/
		case R.id.btn_submit:
			submit();
			break;
		/** 点击清空 **/
		case R.id.btn_clearall:
			clearSelect();
			break;
		}
	}

	/**
	 * 玩法说明
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_JCLQ_Activity.this,
				PlayDescription.class);
		Select_JCLQ_Activity.this.startActivity(intent);
	}

	/**
	 * 创建popWindow
	 */

	private void newPopWindow() {
		LayoutInflater inflact = LayoutInflater.from(Select_JCLQ_Activity.this);
		View view = inflact.inflate(R.layout.pop_item_jc, null);
		btn_playhelp = (Button) view.findViewById(R.id.btn_playhelp);
		btn_sort = (Button) view.findViewById(R.id.btn_sort);
		popWindown_title = new PopupWindow(view,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindown_title.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindown_title.setTouchable(true); // 设置PopupWindow可触摸
		popWindown_title.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		// 设置之后点击返回键 popwindow 会消失
		popWindown_title.setBackgroundDrawable(new BitmapDrawable());
		popWindown_title.setFocusable(true);
		popWindown_title
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						backgroundAlpaha(Select_JCLQ_Activity.this, 1.0f);
						// TODO Auto-generated method stub
					}
				});
		// 设置popwindow的消失事件
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindown_title != null
							&& popWindown_title.isShowing()) {
						popWindown_title.dismiss();
						backgroundAlpaha(Select_JCLQ_Activity.this, 1.0f);
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
				popWindown_title.dismiss();
				backgroundAlpaha(Select_JCLQ_Activity.this, 1.0f);
			}
		});

		btn_sort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				screenDMList();
				popWindown_title.dismiss();
				backgroundAlpaha(Select_JCLQ_Activity.this, 1.0f);
			}
		});

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindown_title != null && popWindown_title.isShowing()) {
					popWindown_title.dismiss();
					popWindown_title = null;
				}
				return true;
			}
		});
	}

	public void screenDMList() {
		myDialog.set(ways, ways == 0 ? guoguan_set : danguan_set);
		myDialog.show();
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
	 * 创建玩法的popWindow
	 */
	private void createPopWindow() {
		rote(1);
		LayoutInflater inflact = LayoutInflater.from(this);
		View view = inflact.inflate(R.layout.pop_jclq, null);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		popWindow = new PopupWindow(view, metric.widthPixels,
				metric.heightPixels);
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				rote(2);
			}
		});// 设置popwindow的消失事件
			// 设置之后点击返回键 popwindow 会消失
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		if (adapter == null)
			adapter = new MyPlayTypeAdapter();
		GridView type_name = (GridView) view.findViewById(R.id.gv_type_jclq);
		type_name.setAdapter(adapter);
		type_name.setOnItemClickListener(new MyTypeAdapterItemListener());
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		View.OnKeyListener listener = new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (popWindow != null && popWindow.isShowing()) {
						popWindow.dismiss();
						rote(2);
					}
					break;
				}
				return true;
			}
		};

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popWindow != null && popWindow.isShowing()) {
					popWindow.dismiss();
					rote(2);
					popWindow = null;
				}
				return true;
			}
		});
	}

	/**
	 * 关闭popwindow
	 */
	private void dismissPopWindow() {
		if (null != popWindow && popWindow.isShowing()) {
			popWindow.dismiss();
		}
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
	 * 改变显示值
	 * 
	 * @param adpaterType
	 *            :玩法类型参数
	 */
	public void changeTextShow(int adpaterType) {
		total = 0;
		if (ways == 0) {
			switch (adpaterType) {
			case 1:
				if (null != ExpandAdapter_jclq_52.map_hashMap_sf) {
					for (int i = 0; i < AppTools.DtMatch_Basketball.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_sf.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_sf
									.get(i).size();
					}
				}
				break;

			case 2:
				if (null != ExpandAdapter_jclq_52.map_hashMap_dx) {
					for (int i = 0; i < AppTools.DtMatch_Basketball.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_dx.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_dx
									.get(i).size();
					}
				}
				break;
			case 3:
				if (null != ExpandAdapter_jclq_52.map_hashMap_rfsf) {
					for (int i = 0; i < AppTools.DtMatch_Basketball.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_rfsf
								.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_rfsf
									.get(i).size();
					}
				}
				break;
			case 4:
				if (null != ExpandAdapter_jclq_52.map_hashMap_cbf) {
					for (int i = 0; i < AppTools.DtMatch_Basketball.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_cbf
								.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_cbf.get(
									i).size();
					}
				}
				break;

			case 5:
				if (null != ExpandAdapter_jclq_52.map_hashMap_hhtz) {
					for (int i = 0; i < AppTools.DtMatch_Basketball.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_hhtz
								.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_hhtz
									.get(i).size();
					}
				}
				break;

			default:
				break;
			}
		} else {// 单关
			switch (adpaterType) {
			case 1:
				if (null != ExpandAdapter_jclq_52.map_hashMap_sf) {
					for (int i = 0; i < AppTools.DtMatch_Basketball_single
							.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_sf.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_sf
									.get(i).size();
					}
				}
				break;
			case 2:
				if (null != ExpandAdapter_jclq_52.map_hashMap_dx) {
					for (int i = 0; i < AppTools.DtMatch_Basketball_single
							.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_dx.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_dx
									.get(i).size();
					}
				}
				break;
			case 3:
				if (null != ExpandAdapter_jclq_52.map_hashMap_rfsf) {
					for (int i = 0; i < AppTools.DtMatch_Basketball_single
							.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_rfsf
								.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_rfsf
									.get(i).size();
					}
				}
				break;
			case 4:
				if (null != ExpandAdapter_jclq_52.map_hashMap_cbf) {
					for (int i = 0; i < AppTools.DtMatch_Basketball_single
							.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_cbf
								.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_cbf.get(
									i).size();
					}
				}
				break;
			case 5:
				if (null != ExpandAdapter_jclq_52.map_hashMap_hhtz) {
					for (int i = 0; i < AppTools.DtMatch_Basketball_single
							.size(); i++) {
						if (ExpandAdapter_jclq_52.map_hashMap_hhtz
								.containsKey(i))
							total += ExpandAdapter_jclq_52.map_hashMap_hhtz
									.get(i).size();
					}
				}
				break;
			default:
				break;
			}
		}
		// if (total == 0) {
		// if (ways == 0)
		// tv_tip_jc.setText("请至少选择2场");
		// else
		// tv_tip_jc.setText("请至少选择1场");
		// } else {
		tv_tip_jc.setText("已经选择" + total + "场");
		// }
	}

	/**
	 * 清空所选的 比赛
	 */
	public void clearSelect() {
		fragment.clearSelect();
		total = 0;
		tv_tip_jc.setText("已经选择" + total + "场");
		// if (ways == 0)
		// tv_tip_jc.setText("请至少选择2场");
		// else
		// tv_tip_jc.setText("请至少选择1场");
	}

	/**
	 * 提交投注信息
	 */
	private void submit() {
		if (total < 2 && ways == 0) {
			MyToast.getToast(this, "请至少选泽两场比赛");
		} else if (total == 0 && ways == 1) {
			MyToast.getToast(this, "请至少选泽一场比赛");
		} else {
			Intent intent = new Intent(this, Bet_JCLQ_Activity.class);
			intent.putExtra("type", type);
			intent.putExtra("ways", ways);
			this.startActivity(intent);
		}
	}

	@Override
	public void onFragmentInteraction(int adapterType, String argument) {
		if (argument == null) {
			changeTextShow(adapterType);
		}
	}

	@Override
	public void onFatherFragmentInteraction(int playType, int position) {
		ways = position;
		clearSelect();
		changeTextShow(getAdapterPlayType(playType));
	}

	/**
	 * 将玩法id 转换成adpater对应的type
	 * 
	 * @param type
	 *            :玩法ID
	 * @return
	 */
	private int getAdapterPlayType(int type) {
		int adapter_type = 1;
		switch (type) {
		case 7301: // 胜负
			adapter_type = 1;
			break;
		case 7302: // 让分
			adapter_type = 3;
			break;
		case 7303: // 胜分差
			adapter_type = 4;
			break;
		case 7304: // 大小分
			adapter_type = 2;
			break;
		case 7306: // 混合投注
			adapter_type = 5;
			break;
		}
		return adapter_type;
	}

	/**
	 * 适配器类
	 * 
	 * @author lenovo
	 * 
	 */
	class MyPlayTypeAdapter extends BaseAdapter {
		int index = 0;// 选中下标

		private void setindex(int index) {
			this.index = index;
		}

		@Override
		public int getCount() {
			return type_name.length;
		}

		@Override
		public Object getItem(int position) {
			return type_name[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder;
			// 判断View是否为空
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater
						.from(Select_JCLQ_Activity.this);
				// 得到布局文件
				view = inflater.inflate(R.layout.item_pop_lv_gv, null);
				// 得到控件
				holder.type_name = (TextView) view
						.findViewById(R.id.gv_tv_playType);
				holder.type_name.setBackgroundResource(R.drawable.btn_playtype);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.type_name.setText(type_name[position]);
			if (index == position)
				holder.type_name
						.setBackgroundResource(R.drawable.btn_playtype_select);
			return view;
		}
	}

	/**
	 * 控件类
	 * 
	 * @author lenovo
	 * 
	 */
	class ViewHolder {
		TextView type_name;
	}

	/**
	 * 玩法选项的点击监听
	 * 
	 * @author lenovo
	 * 
	 */
	class MyTypeAdapterItemListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0: // 混合投注
				type = 7306;
				switchPlay(type, 0);
				break;
			case 1: // 胜负
				type = 7301;
				switchPlay(type, 0);
				break;
			case 2: // 让分胜负
				type = 7302;
				switchPlay(type, 0);
				break;
			case 3: // 胜分差
				type = 7303;
				switchPlay(type, 0);
				break;
			case 4: // 大小分
				type = 7304;
				switchPlay(type, 0);
				break;
			case 5:
				dismissPopWindow();
				return;
			default:
				break;
			}
			adapter.setindex(position);
			btn_playtype.setText(type_name[position]);
			dismissPopWindow();
			rote(2);// 关闭popwindow

		}
	}

	/**
	 * 切换玩法fragment
	 * 
	 * @param type
	 *            :玩法ID
	 * @param position
	 *            ：过关方式
	 */
	private void switchPlay(int type, int position) {
		ways = position;
		// TODO 从此篮球不错乱
		Select_JCLQ_Activity.list_Matchs.clear();
		Select_JCLQ_Activity.list_Matchs_single.clear();
		clearSelect();
		fragment = DtMatchFragmentJCLQ.newInstance(type, position);
		fragmentManager.beginTransaction()
				.replace(R.id.cjlq_fragment_container, fragment)
				.commitAllowingStateLoss();
	}

	public List<String> getGuoguan_set() {
		return guoguan_set;
	}

	public void setGuoguan_set(List<String> guoguan_set) {
		this.guoguan_set = guoguan_set;
	}

	public List<String> getDanguan_set() {
		return danguan_set;
	}

	public void setDanguan_set(List<String> danguan_set) {
		this.danguan_set = danguan_set;
	}

	public void dialogCallback(int ways, List<String> listgamea, int dtype) {
		if (listgamea != null) {
			if (ways == 0) {
				guoguan_set.clear();
				for (String game : listgamea) {
					guoguan_set.add(game);
				}
			} else {
				danguan_set.clear();
				for (String game : listgamea) {
					danguan_set.add(game);
				}
			}
		}
		switchPlay(type, ways);
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (total != 0) {
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clearSelect();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});
			} else {
				clearSelect();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 重新进入界面时刷新界面
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent != null) {
			setIntent(intent);
			clearSelect();
			String from = intent.getStringExtra("from");
			if (from != null) {
				if (from.equals("MyCommonLotteryInfo_jc")
						|| from.equals("pay_success")) {
					type = 7306;
					ways = 0;
					btn_playtype.setText(type_name[0]);
					switchPlay(type, ways);
				} else if (from.equals("pay_success_continue")) {
					switch (type) {
					case 7306:
						btn_playtype.setText(type_name[0]);
						break;
					case 7301:
						btn_playtype.setText(type_name[1]);
						break;
					case 7302:
						btn_playtype.setText(type_name[2]);
						break;
					case 7303:
						btn_playtype.setText(type_name[3]);
						break;
					case 7304:
						btn_playtype.setText(type_name[4]);
						break;
					default:
						break;
					}
					ways = 0;
					switchPlay(type, ways);
				} else if (from.equals("Bet_JCLQ_Activity")) {
					type = getIntent().getIntExtra("playType", 7301);
					isChecked = getIntent().getBooleanExtra("canChange", true);
					ways = getIntent().getIntExtra("ways", 0);
					List<String> indexs = getIntent().getStringArrayListExtra(
							"select_index");
					List<String> results = getIntent().getStringArrayListExtra(
							"select");
					update(ways, indexs, results);
					changeTextShow(getAdapterPlayType(type));
				}
			}
		}
		super.onNewIntent(intent);
	}

	private void update(int ways, List<String> indexs, List<String> results) {
		fragment.update(ways, indexs, results);
	}

}
