package com.gcapp.tc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.DtMatch;
import com.gcapp.tc.sd.ui.Bet_JCZQ_Activity;
import com.gcapp.tc.sd.ui.Select_JCZQ_Activity;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapterJCZQPassMore;
import com.gcapp.tc.sd.ui.adapter.ExpandAdapterJCZQPassSingle;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.view.IphoneTreeView;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞彩足球选择投注页面
 */
public class DtMatchFragmentJczq_passMore extends Fragment implements
		OnClickListener {
	private final static String TAG = "DtMatchFragmentJczq_passMore";
	private static Select_JCZQ_Activity activity;
	private static String[] CONTENT;
	// 选过关和单关控件
	// 过关
	private IphoneTreeView lv_passmore;
	private ExpandAdapterJCZQPassMore exAdapter_passmore;
	private List<List<DtMatch>> data_passmore;// 过关
	// 单关
	private IphoneTreeView lv_passsingle;
	private List<List<DtMatch>> data_passsingle;// 单关
	private int playtype;

	public int total_passmore = 0;
	public int total_passsingle = 0;

	private int type = 5;// 玩法类型

	private LinearLayout layout_tip_notjc;// 非竞彩提示
	private LinearLayout layout_tip_jc;// 竞彩提示
	public TextView tv_tip_jc2;// 竞彩提示

	public TextView tv_tip_jc;// 竞彩提示
	/* 尾部 */
	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数
	private int viewPagerCurrentIndex = 0;// viewpager的当前页
	private boolean isEmptPassMore = false;// 过关数据是否是空

	/**
	 * 构造方法
	 * 
	 * @param activity
	 * @param playtype
	 *            玩法id
	 * @param viewPagerCurrentIndex
	 *            页面下标
	 * @param passMoreDMList
	 *            过关对阵
	 * @param passSingleDMList
	 *            单关对阵
	 * @return
	 */
	public static DtMatchFragmentJczq_passMore newInstance(
			Select_JCZQ_Activity activity, int playtype,
			int viewPagerCurrentIndex, List<List<DtMatch>> passMoreDMList,
			List<List<DtMatch>> passSingleDMList) {
		DtMatchFragmentJczq_passMore fragment = new DtMatchFragmentJczq_passMore();
		fragment.viewPagerCurrentIndex = viewPagerCurrentIndex;
		fragment.activity = activity;
		fragment.playtype = playtype;
		fragment.data_passmore = passMoreDMList;// 获取过关数据
		fragment.data_passsingle = passSingleDMList;// 获取单关数据
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View parent = localInflater.inflate(R.layout.framlayout_select_jc,
				container, false);
		init();
		findView(parent);
		return parent;
	}

	/**
	 * 初始化自定义控件
	 * 
	 * @param v
	 */
	private void findView(View v) {
		btn_clearall = (TextView) v.findViewById(R.id.btn_clearall);
		btn_submit = (TextView) v.findViewById(R.id.btn_submit);
		tv_tatol_count = (TextView) v.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) v.findViewById(R.id.tv_tatol_money);
		layout_tip_notjc = (LinearLayout) v.findViewById(R.id.layout_tip_notjc);
		layout_tip_jc = (LinearLayout) v.findViewById(R.id.layout_tip_jc);
		tv_tip_jc = (TextView) v.findViewById(R.id.tv_tip_jc);
		tv_tip_jc2 = (TextView) v.findViewById(R.id.tv_tip_jc2);
		tv_tip_jc2.setText("至少选择2场比赛");
		lv_passmore = (IphoneTreeView) v.findViewById(R.id.expandablelist);
		lv_passmore.setHeaderView(LayoutInflater.from(activity).inflate(
				R.layout.select_jczq_spf_groups, lv_passmore, false));

		testData();
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		// 显示与隐藏
		layout_tip_notjc.setVisibility(View.GONE);
		layout_tip_jc.setVisibility(View.VISIBLE);
		tv_tatol_count.setVisibility(View.GONE);
		tv_tatol_money.setVisibility(View.GONE);
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		if (playtype != 7206) {
			CONTENT = new String[] { "过关", "单关" };
		} else {
			CONTENT = new String[] { "过关" };
		}
		showPlayType();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 改变界面显示值
	 */
	public void changeTextShow() {
		total_passmore = 0;
		total_passsingle = 0;
		String tip = "";
		if (isEmptPassMore) {
			MyToast.getToast(DtMatchFragmentJczq_passMore.activity, "暂无过多关对阵信息")
					;
		}
		if (null != exAdapter_passmore) {
			switch (exAdapter_passmore.getPlayType()) {

			case 5:
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_hhtz) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_hhtz
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_hhtz
									.get(i).size();
					}
				}
				break;

			case 7:// 胜平负/让球
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_hhtz) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_hhtz
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_hhtz
									.get(i).size();
					}
				}
				break;

			case 8:// 2选1
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_hhtz) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_hhtz
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_hhtz
									.get(i).size();
					}
				}
				break;

			case 1:
			case 4:
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_spf) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_spf
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_spf
									.get(i).size();
					}
				}
				break;
			case 2:
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_cbf) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_cbf
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_cbf
									.get(i).size();
					}
				}
				break;
			case 6:
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_bqc) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_bqc
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_bqc
									.get(i).size();
					}
				}
				break;
			case 3:
				if (null != ExpandAdapterJCZQPassMore.map_hashMap_zjq) {
					for (int i = 0; i < AppTools.list_Matchs.size(); i++) {
						if (ExpandAdapterJCZQPassMore.map_hashMap_zjq
								.containsKey(i))
							total_passmore += ExpandAdapterJCZQPassMore.map_hashMap_zjq
									.get(i).size();
					}
				}
				break;
			default:
				break;
			}

			tip = "已选择" + total_passmore + "场";
			// if (total_passmore < 2) {
			// tip = Html.fromHtml("请至少选择"
			// + AppTools.changeStringColor("#e3393c", "2") + "场比赛");
			// } else {
			// tip = Html.fromHtml("已选择"
			// + AppTools
			// .changeStringColor("#e3393c", total_passmore + "")
			// + "场比赛");
			// }
		}
		tv_tip_jc.setText(tip);
	}

	/**
	 * 根据不同玩法显示界面
	 * 
	 */
	private void showPlayType() {
		switch (playtype) {
		case 7201:
			type = 1;
			break;
		case 7202:// 比分
			type = 2;
			Log.i(TAG, "传过来的值");
			for (Integer i : ExpandAdapterJCZQPassMore.map_hashMap_cbf.keySet()) {
				ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(i);
				for (Integer j : ExpandAdapterJCZQPassMore.map_hashMap_cbf.get(
						i).keySet()) {
					ArrayList<String> list = ExpandAdapterJCZQPassMore.map_hashMap_cbf
							.get(i).get(j);
					for (int k = 0; k < list.size(); k++) {
						Log.i(TAG, list.get(k));
					}
				}
			}
			break;
		case 7204:
			type = 6;
			break;
		case 7203:
			type = 3;
			break;
		case 7207:
			type = 4;
			break;
		case 7206:// 混合投注
			type = 5;
			break;

		case 7208:// 胜平负/让球
			type = 7;
			break;

		case 7209:// 2选1
			type = 8;
			break;
		}
	}

	/**
	 * 根据玩法筛选出 队伍
	 * 
	 * @param _list
	 *            ：对阵list
	 * @return
	 */
	private List<List<DtMatch>> setList_Matchs(List<List<DtMatch>> _list) {
		List<List<DtMatch>> list = new ArrayList<List<DtMatch>>();
		if (null != _list) {
			for (List<DtMatch> list_dt : _list) {
				List<DtMatch> list2 = new ArrayList<DtMatch>();
				for (DtMatch dt : list_dt) {
					if (setList_Matchs(dt))
						list2.add(dt);
				}

				list.add(list2);
			}
			for (int i = 0; i < list.size(); i++) {
				if (0 == list.get(i).size()) {
					list.remove(i);
				}
			}
		}
		return list;
	}

	/**
	 * 根据类型筛选出 队伍
	 * 
	 * @param dt
	 *            ：对阵
	 * @return
	 */
	private boolean setList_Matchs(DtMatch dt) {
		switch (this.playtype) {
		case 7201:
			return dt.isSPF();
		case 7202:
			return dt.isCBF();
		case 7204:
			return dt.isBQC();
		case 7203:
			return dt.isZJQ();
		case 7207:
			return dt.isNewSPF();
		case 7206:
			return dt.isHHTZ();
		case 7208:// 胜平负/让球
			return dt.isHHTZ();
		case 7209:// 2选1混投
			return dt.isHHTZ();
		default:
			return false;
		}
	}

	/**
	 * 判断根据玩法筛选出来的对阵是否为空 单关
	 * 
	 * @param list_singlepass_Matchs
	 *            ：对阵list
	 * @return
	 */
	public boolean isEmpt(List<List<DtMatch>> list_singlepass_Matchs) {
		List<List<DtMatch>> lists = setList_Matchs(list_singlepass_Matchs);
		boolean[] flag = new boolean[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			List<DtMatch> matchs = lists.get(i);
			flag[i] = 0 != matchs.size();
		}
		boolean isEmpt = true;
		for (int i = 0; i < flag.length; i++) {
			if (flag[i]) {
				isEmpt = false;
			}
		}
		return isEmpt;
	}

	/**
	 * 得到显示的对阵
	 */
	private void testData() {
		if (null == exAdapter_passmore) {
			exAdapter_passmore = new ExpandAdapterJCZQPassMore(
					DtMatchFragmentJczq_passMore.activity, 1,
					DtMatchFragmentJczq_passMore.this, lv_passmore);
		}

		exAdapter_passmore.setPlayType(type);
		exAdapter_passmore.setList_Matchs(setList_Matchs(data_passmore));// 根据玩法筛选对阵
		if (0 == setList_Matchs(data_passmore).size()) {
			isEmptPassMore = true;// 设置过关数据为空
		}
		lv_passmore.setAdapter(exAdapter_passmore);
		// 展开所有group
		for (int i = 0, count = lv_passmore.getCount(); i < count; i++) {
			lv_passmore.expandGroup(i);
		}
		changeTextShow();
	}

	/** 清空所选的 比赛 */
	public void clearSelect() {
		ExpandAdapterJCZQPassMore.map_hashMap_spf.clear();
		ExpandAdapterJCZQPassMore.map_hashMap_zjq.clear();
		ExpandAdapterJCZQPassMore.map_hashMap_cbf.clear();
		ExpandAdapterJCZQPassMore.map_hashMap_hhtz.clear();
		ExpandAdapterJCZQPassMore.map_hashMap_bqc.clear();
		changeTextShow();
		exAdapter_passmore.notifyDataSetChanged();
	}

	/** 清空 */
	private void clear() {
		clearSelect();
		AppTools.totalCount = 0;
	}

	public void update() {
		if (null != exAdapter_passmore) {
			exAdapter_passmore.notifyDataSetChanged();
		}
	}

	/** 提交号码 */
	private void submitNumber() {
		if (total_passmore < 2) {
			MyToast.getToast(activity, "请至少选泽两场比赛");
			return;
		}
		Intent intent = new Intent(activity, Bet_JCZQ_Activity.class);
		intent.putExtra("playtype", type);
		intent.putExtra("passtype", 0);
		ExpandAdapterJCZQPassSingle.map_hashMap_cbf.clear();
		ExpandAdapterJCZQPassSingle.map_hashMap_spf.clear();
		ExpandAdapterJCZQPassSingle.map_hashMap_zjq.clear();
		ExpandAdapterJCZQPassSingle.map_hashMap_bqc.clear();
		activity.startActivity(intent);
	}

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
		}
	}
}
