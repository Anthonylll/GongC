package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.DtMatch;
import com.gcapp.tc.fragment.DtMatchFragmentJczq_passMore;
import com.gcapp.tc.sd.ui.Select_JCZQ_Activity;
import com.gcapp.tc.utils.ColorUtil;
import com.gcapp.tc.view.IphoneTreeView;
import com.gcapp.tc.view.SelectJCZQDialog;
import com.gcapp.tc.view.IphoneTreeView.IphoneTreeHeaderAdapter;
import com.gcapp.tc.R;

/**
 * 功能：竞彩足球过关的选号适配器
 * 
 * @author lenovo
 * 
 */
@SuppressLint({ "UseSparseArrays", "ResourceAsColor" })
public class ExpandAdapterJCZQPassMore extends BaseExpandableListAdapter
		implements IphoneTreeHeaderAdapter {
	private final static String TAG = "ExpandAdapterJCZQPassMore";
	public static Context context;
	private DtMatchFragmentJczq_passMore fragment;
	/** 组项 **/
	private List<String> mGroups;
	/** 子项 **/
	public static List<List<DtMatch>> list_Matchs;
	private Select_JCZQ_Activity activity;
	public Map<String, String> hunheMap = new HashMap<String, String>();
	/** 玩法 **/
	private int playType = 5;
	/** 所选的集合 第一个Map装 对阵 第二个Map装 所选的 值 （过关） **/
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap_spf = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap_zjq = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap_cbf = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap_hhtz = new LinkedHashMap <Integer, LinkedHashMap<Integer, ArrayList<String>>>();
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap_bqc = new LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>>();
	private IphoneTreeView ip;
	private HashMap<Integer, Integer> groupStatusMap;

	public ExpandAdapterJCZQPassMore(Context context, int passType,
			DtMatchFragmentJczq_passMore fragment, IphoneTreeView ip) {
		ExpandAdapterJCZQPassMore.context = context;
		activity = (Select_JCZQ_Activity) context;
		this.fragment = fragment;
		this.ip = ip;
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	/** 给数组赋值 */
	public void setGroup() {
		mGroups = new ArrayList<String>();
		mGroups.clear();
		for (int i = 0; i < list_Matchs.size(); i++) {
			if (list_Matchs.get(i).size() == 0) {
				continue;
			}
			mGroups.add(list_Matchs.get(i).get(0).getDay() + " "
					+ list_Matchs.get(i).get(0).getMatchWeek());
		}
	}

	/**
	 * 设置过关对阵信息
	 * 
	 * @param _list_Matchs
	 */
	public void setList_Matchs(List<List<DtMatch>> _list_Matchs) {
		list_Matchs = new ArrayList<List<DtMatch>>();
		for (List<DtMatch> list : _list_Matchs) {
			List<DtMatch> listM = new ArrayList<DtMatch>();
			for (DtMatch dt : list) {
				if (playType == 8) {// 混投2选1
					if (dt.getMainLoseBall() == -1 || dt.getMainLoseBall() == 1) {
						listM.add(dt);
					}
				} else {
					listM.add(dt);
				}
			}
			list_Matchs.add(listM);
		}
		setGroup();
	}

	/** 设置 玩法 **/
	public void setPlayType(int _playType) {
		this.playType = _playType;
	}

	/** 拿到 玩法 **/
	public int getPlayType() {
		return this.playType;
	}

	// 得到子项
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list_Matchs.get(groupPosition).get(childPosition);
	}

	// 得到子项ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 得到子类的数量
	@Override
	public int getChildrenCount(int arg0) {
		return list_Matchs.get(arg0).size();
	}

	// 得到组
	@Override
	public Object getGroup(int position) {
		return mGroups.get(position);
	}

	// 得到组的数量
	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	// 得到组ID
	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// 组 是否被选中
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	// 得到组视图
	@Override
	public View getGroupView(int groupPosition, boolean b, View convertView,
			ViewGroup arg3) {

		if (groupPosition > (list_Matchs.size() - 1))
			return null;
		GroupViewHolder holder;

		if (convertView == null) {
			holder = new GroupViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.select_jczq_spf_groups,
					null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_playCount);

			holder.ll_win_jc_top = (LinearLayout) convertView
					.findViewById(R.id.ll_win_jc_top);
			holder.iv_line = (ImageView) convertView.findViewById(R.id.iv_line);

//			if (groupPosition == 0) {
//				holder.iv_line.setVisibility(View.GONE);
//			} else {
//				holder.iv_line.setVisibility(View.VISIBLE);
//			}
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}

		// 更改背景
		if (b) {
			holder.ll_win_jc_top
					.setBackgroundResource(R.drawable.select_jc_lv_parent_win_up);
		} else {
			holder.ll_win_jc_top
					.setBackgroundResource(R.drawable.select_jc_lv_parent_win_down);
		}

		// if (b) {
		// convertView
		// .setBackgroundResource(R.drawable.select_jc_lv_parent_win_up);
		// } else {
		// convertView
		// .setBackgroundResource(R.drawable.select_jc_lv_parent_win_down);
		// }
		holder.tv_date.setText(mGroups.get(groupPosition));
		holder.tv_count
				.setText(list_Matchs.get(groupPosition).size() + "场比赛可投");
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (groupPosition > (list_Matchs.size() - 1)
				|| childPosition > (list_Matchs.get(groupPosition).size() - 1))
			return null;

		final int groupId = groupPosition;
		final int index = childPosition;
		final ChildViewHolder chiHolder;
		DtMatch dtm_passmore = null;
		dtm_passmore = list_Matchs.get(groupPosition).get(childPosition);
		final DtMatch dtm = dtm_passmore;
		if (convertView == null) {
			chiHolder = new ChildViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.select_jczq_spf_items,
					null);
			chiHolder.ll_spf = (LinearLayout) convertView
					.findViewById(R.id.right_bottom_ll_spf);
			chiHolder.ll_cbf = (LinearLayout) convertView
					.findViewById(R.id.jczq_cbf_ll);
			chiHolder.ll_zjq = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_ll);
			chiHolder.ll_hhtz = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_hhtz);
			/** 胜平负/让球 **/
			chiHolder.ll_spfrqspf = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_spfrq);
			/** 混投2选1 **/
			chiHolder.ll_2x1 = (LinearLayout) convertView
					.findViewById(R.id.ll_2x1);
			chiHolder.btn_main_2x1 = (Button) convertView
					.findViewById(R.id.btn_main_2x1);
			chiHolder.btn_guest_2x1 = (Button) convertView
					.findViewById(R.id.btn_guest_2x1);
			/** 胜负彩 BUTTON **/
			chiHolder.btn_win = (Button) convertView
					.findViewById(R.id.btn_mainWin);
			chiHolder.btn_flat = (Button) convertView
					.findViewById(R.id.btn_float);
			chiHolder.btn_lose = (Button) convertView
					.findViewById(R.id.btn_lose);
			/** 猜比分 BUTTON **/
			chiHolder.btn_open_cbf = (TextView) convertView
					.findViewById(R.id.jczq_cbf_btn_open);
			/** 胜平负/让球 **/
			chiHolder.layout_spfandrq_isspf = (LinearLayout) convertView
					.findViewById(R.id.layout_spfandrq_isspf);
			chiHolder.hhtz_spfandrq_bottom = (LinearLayout) convertView
					.findViewById(R.id.spfrq_bottom);

			chiHolder.tv_tip_spfandrq_isnotrqspf = (TextView) convertView
					.findViewById(R.id.tv_tip_spfrq_isnotrqspf);// 让球胜平负的暂停销售
			chiHolder.tv_tip_spfandrq_isnotspf = (TextView) convertView
					.findViewById(R.id.tv_tip_spfrq_isnotspf);// 胜平负的暂停销售

			chiHolder.tv_spfrq_01 = (TextView) convertView
					.findViewById(R.id.tv_spfrq_1);
			chiHolder.tv_spfrq_02 = (TextView) convertView
					.findViewById(R.id.tv_spfrq_2);
			chiHolder.tv_spfrq_03 = (TextView) convertView
					.findViewById(R.id.tv_spfrq_3);
			chiHolder.tv_spfrq_04 = (TextView) convertView
					.findViewById(R.id.tv_spfrq_4);
			chiHolder.tv_spfrq_05 = (TextView) convertView
					.findViewById(R.id.tv_spfrq_5);
			chiHolder.tv_spfrq_06 = (TextView) convertView
					.findViewById(R.id.tv_spfrq_6);

			chiHolder.ll_btn_spfrq1 = (LinearLayout) convertView
					.findViewById(R.id.jczq_spfrq_1);
			chiHolder.ll_btn_spfrq2 = (LinearLayout) convertView
					.findViewById(R.id.jczq_spfrq_2);
			chiHolder.ll_btn_spfrq3 = (LinearLayout) convertView
					.findViewById(R.id.jczq_spfrq_3);
			chiHolder.ll_btn_spfrq4 = (LinearLayout) convertView
					.findViewById(R.id.jczq_spfrq_4);
			chiHolder.ll_btn_spfrq5 = (LinearLayout) convertView
					.findViewById(R.id.jczq_spfrq_5);
			chiHolder.ll_btn_spfrq6 = (LinearLayout) convertView
					.findViewById(R.id.jczq_spfrq_6);
			chiHolder.tv_spfrq_rq = (TextView) convertView
					.findViewById(R.id.tv_spfrq_rq);

			/** 混合投注 **/
			chiHolder.layout_isspf = (LinearLayout) convertView
					.findViewById(R.id.layout_isspf);
			chiHolder.hhtz_bottom = (LinearLayout) convertView
					.findViewById(R.id.hhtz_bottom);
			chiHolder.tv_tip_isnotrqspf = (TextView) convertView
					.findViewById(R.id.tv_tip_isnotrqspf);
			chiHolder.tv_tip_isnotspf = (TextView) convertView
					.findViewById(R.id.tv_tip_isnotspf);

			chiHolder.tv_hhtz_01 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_1);
			chiHolder.tv_hhtz_02 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_2);
			chiHolder.tv_hhtz_03 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_3);
			chiHolder.tv_hhtz_04 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_4);
			chiHolder.tv_hhtz_05 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_5);
			chiHolder.tv_hhtz_06 = (TextView) convertView
					.findViewById(R.id.tv_hhtz_6);

			chiHolder.tv_result1 = (TextView) convertView
					.findViewById(R.id.tv_result1);
			chiHolder.tv_result2 = (TextView) convertView
					.findViewById(R.id.tv_result2);

			chiHolder.ll_btn_hhtz1 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_1);
			chiHolder.ll_btn_hhtz2 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_2);
			chiHolder.ll_btn_hhtz3 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_3);
			chiHolder.ll_btn_hhtz4 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_4);
			chiHolder.ll_btn_hhtz5 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_5);
			chiHolder.ll_btn_hhtz6 = (LinearLayout) convertView
					.findViewById(R.id.jczq_hhtz_6);

			chiHolder.layout_result = (LinearLayout) convertView
					.findViewById(R.id.layout_result);
			chiHolder.layout_hhtz_rq = (LinearLayout) convertView
					.findViewById(R.id.jczq_zjq_4);
			chiHolder.tv_hhtz_rq = (TextView) convertView
					.findViewById(R.id.tv_hhtz_rq);

			/** 总进球 **/
			findChildView(chiHolder, convertView);

			chiHolder.mItemId = (TextView) convertView
					.findViewById(R.id.spf_tv_id);
			chiHolder.mItemName = (TextView) convertView
					.findViewById(R.id.spf_tv_name);
			chiHolder.mItemTime = (TextView) convertView
					.findViewById(R.id.spf_tv_time);
			chiHolder.mainTeam = (TextView) convertView
					.findViewById(R.id.ll_tv_mainTeam);
			chiHolder.guestTeam = (TextView) convertView
					.findViewById(R.id.ll_tv_cusTeam);
			chiHolder.ball = (TextView) convertView.findViewById(R.id.ll_tv_vs);

			convertView.setTag(chiHolder);
		} else {
			chiHolder = (ChildViewHolder) convertView.getTag();
		}

		chiHolder.ball.setTextColor(ColorUtil.BET_GRAY);
		// 显示不同玩法界面
		switch (playType) {
		case 7:// 胜平负/让球
			chiHolder.ll_2x1.setVisibility(View.GONE);
			chiHolder.ll_spfrqspf.setVisibility(View.VISIBLE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;

		case 8:// 2选1
			chiHolder.ll_2x1.setVisibility(View.VISIBLE);
			chiHolder.ll_spfrqspf.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			if (dtm.getMainLoseBall() == -1) {
				chiHolder.ball.setTextColor(Color.GREEN);
				chiHolder.ball.setText(dtm.getMainLoseBall() + "");
			} else if (dtm.getMainLoseBall() == 1) {
				chiHolder.ball.setTextColor(Color.RED);
				chiHolder.ball.setText("+" + dtm.getMainLoseBall() + "");
			}
			break;

		case 5:// 混合投注
			chiHolder.ll_2x1.setVisibility(View.GONE);
			chiHolder.ll_spfrqspf.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.VISIBLE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;

		case 4:
		case 1:
			chiHolder.ll_2x1.setVisibility(View.GONE);
			chiHolder.ll_spfrqspf.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_spf.setVisibility(View.VISIBLE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			if (playType == 1) {
				if (dtm.getMainLoseBall() == 0) {
					chiHolder.ball.setText("VS");
				} else if (dtm.getMainLoseBall() < 0) {
					chiHolder.ball.setTextColor(Color.GREEN);
					chiHolder.ball.setText(dtm.getMainLoseBall() + "");
				} else if (dtm.getMainLoseBall() > 0) {
					chiHolder.ball.setTextColor(Color.RED);
					chiHolder.ball.setText("+" + dtm.getMainLoseBall() + "");
				}
			}
			if (playType == 4) {
				chiHolder.ball.setText("VS");
			}
			break;
		case 2:// 比分
		case 6:// 半全场
			chiHolder.ll_2x1.setVisibility(View.GONE);
			chiHolder.ll_spfrqspf.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_cbf.setVisibility(View.VISIBLE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;
		case 3:
			chiHolder.ll_2x1.setVisibility(View.GONE);
			chiHolder.ll_spfrqspf.setVisibility(View.GONE);
			chiHolder.ll_hhtz.setVisibility(View.GONE);
			chiHolder.ll_zjq.setVisibility(View.VISIBLE);
			chiHolder.ll_cbf.setVisibility(View.GONE);
			chiHolder.ll_spf.setVisibility(View.GONE);
			chiHolder.ball.setText("VS");
			break;
		}

		if (2 == playType) {
			/** 设置猜比分的 按钮显示内容 **/
			if (null == map_hashMap_cbf.get(groupPosition)) {
				Log.i(TAG, "猜比分是空的");
				set_CBF_BQC_Text(chiHolder, null);
			} else {
				set_CBF_BQC_Text(chiHolder, map_hashMap_cbf.get(groupPosition)
						.get(index));
			}
		}
		if (6 == playType) {
			/** 设置半全场的 按钮显示内容 **/
			if (null == map_hashMap_bqc.get(groupPosition)) {
				set_CBF_BQC_Text(chiHolder, null);
			} else {
				set_CBF_BQC_Text(chiHolder, map_hashMap_bqc.get(groupPosition)
						.get(index));
			}
		}
		if (4 == playType || 1 == playType) {
			/** 改变胜平负Button 背景 **/
			if (null == map_hashMap_spf.get(groupPosition)) {
				change_spf_btn(chiHolder, null);

			} else {
				change_spf_btn(chiHolder, map_hashMap_spf.get(groupPosition)
						.get(index));
			}
		}
		if (5 == playType) {
			/** 改变混合投注背景 **/
			if (null == map_hashMap_hhtz.get(groupPosition)) {
				change_hhtz_btn(chiHolder, null);
			} else {
				change_hhtz_btn(chiHolder, map_hashMap_hhtz.get(groupPosition)
						.get(index));
			}
			if (dtm.isNewSPF()) {// 胜平负
				chiHolder.layout_isspf.setVisibility(View.VISIBLE);
				chiHolder.tv_tip_isnotspf.setVisibility(View.GONE);
			} else {
				chiHolder.layout_isspf.setVisibility(View.GONE);
				chiHolder.tv_tip_isnotspf.setVisibility(View.VISIBLE);
			}
			if (dtm.isSPF()) {// 让球胜平负
				chiHolder.hhtz_bottom.setVisibility(View.VISIBLE);
				chiHolder.tv_tip_isnotrqspf.setVisibility(View.GONE);
			} else {
				chiHolder.hhtz_bottom.setVisibility(View.GONE);
				chiHolder.tv_tip_isnotrqspf.setVisibility(View.VISIBLE);
			}
		}

		if (8 == playType) {
			/** 改变胜平负/让球的投注背景 **/
			if (null == map_hashMap_hhtz.get(groupPosition)) {
				change_2x1_btn(chiHolder, null);
			} else {
				change_2x1_btn(chiHolder, map_hashMap_hhtz.get(groupPosition)
						.get(index));
			}
		}

		if (7 == playType) {
			/** 改变胜平负/让球的投注背景 **/
			if (null == map_hashMap_hhtz.get(groupPosition)) {
				change_spfAndrq_btn(chiHolder, null);
			} else {
				change_spfAndrq_btn(chiHolder,
						map_hashMap_hhtz.get(groupPosition).get(index));
			}
			if (dtm.isNewSPF()) {// 胜平负
				chiHolder.layout_spfandrq_isspf.setVisibility(View.VISIBLE);
				chiHolder.tv_tip_spfandrq_isnotspf.setVisibility(View.GONE);
			} else {
				chiHolder.layout_spfandrq_isspf.setVisibility(View.GONE);
				chiHolder.tv_tip_spfandrq_isnotspf.setVisibility(View.VISIBLE);
			}
			if (dtm.isSPF()) {// 让球胜平负
				chiHolder.hhtz_spfandrq_bottom.setVisibility(View.VISIBLE);
				chiHolder.tv_tip_spfandrq_isnotrqspf.setVisibility(View.GONE);
			} else {
				chiHolder.hhtz_spfandrq_bottom.setVisibility(View.GONE);
				chiHolder.tv_tip_spfandrq_isnotrqspf
						.setVisibility(View.VISIBLE);
			}
		}

		if (3 == playType) {
			/** 改变 总进球 Button 背景 **/
			if (null == map_hashMap_zjq.get(groupPosition)) {
				change_zjq_btn(chiHolder, null);

			} else {
				change_zjq_btn(chiHolder, map_hashMap_zjq.get(groupPosition)
						.get(index));
			}
		}

		if (playType == 8) {// 2选1
			if (dtm.getMainLoseBall() == -1) {
				chiHolder.btn_main_2x1.setText("主胜" + dtm.getSpfwin()); // 胜平负主胜
				chiHolder.btn_guest_2x1.setText("客不败" + dtm.getLose());// 让球胜平负的客胜
			} else if (dtm.getMainLoseBall() == 1) {
				chiHolder.btn_main_2x1.setText("主不败" + dtm.getWin()); // 让球胜平负的主胜
				chiHolder.btn_guest_2x1.setText("客胜" + dtm.getSpflose());// 胜平负客胜
			}
		}
		// 胜平负/让球
		if (playType == 7) {
			chiHolder.tv_spfrq_01.setText("主胜" + dtm.getSpfwin());
			chiHolder.tv_spfrq_02.setText("平" + dtm.getSpfflat());
			chiHolder.tv_spfrq_03.setText("主负" + dtm.getSpflose());
			int color = 1;
			if (dtm.getMainLoseBall() < 0) {
				chiHolder.tv_spfrq_rq.setText(dtm.getMainLoseBall() + "");
				color = 0xff0d9930;
				chiHolder.tv_spfrq_rq.setTextColor(color);
			} else {
				chiHolder.tv_spfrq_rq.setText("+" + dtm.getMainLoseBall());
				color = 0xffe3393c;
				chiHolder.tv_spfrq_rq.setTextColor(color);
			}
			chiHolder.tv_spfrq_04.setText("主胜" + dtm.getWin());
			chiHolder.tv_spfrq_05.setText("平" + dtm.getFlat());
			chiHolder.tv_spfrq_06.setText("主负" + dtm.getLose());
		}

		if (playType == 5) {
			chiHolder.tv_hhtz_01.setText("主胜" + dtm.getSpfwin());
			chiHolder.tv_hhtz_02.setText("平" + dtm.getSpfflat());
			chiHolder.tv_hhtz_03.setText("主负" + dtm.getSpflose());
			int color = 1;
			if (dtm.getMainLoseBall() < 0) {
				chiHolder.tv_hhtz_rq.setText(dtm.getMainLoseBall() + "");
				color = 0xff0d9930;
				// chiHolder.tv_hhtz_rq.setTextColor(color);
			} else {
				chiHolder.tv_hhtz_rq.setText("+" + dtm.getMainLoseBall());
				color = 0xffe3393c;
				// chiHolder.tv_hhtz_rq.setTextColor(color);
			}
			chiHolder.tv_hhtz_04.setText("主胜" + dtm.getWin());
			chiHolder.tv_hhtz_05.setText("平" + dtm.getFlat());
			chiHolder.tv_hhtz_06.setText("主负" + dtm.getLose());
		}
		if (playType == 1) {
			/** 胜平负 显示值绑定 **/
			chiHolder.btn_win.setText("胜" + dtm.getWin());
			chiHolder.btn_flat.setText("平" + dtm.getFlat());
			chiHolder.btn_lose.setText("负" + dtm.getLose());
		} else {
			/** 胜平负 显示值绑定 **/
			chiHolder.btn_win.setText("胜" + dtm.getSpfwin());
			chiHolder.btn_flat.setText("平" + dtm.getSpfflat());
			chiHolder.btn_lose.setText("负" + dtm.getSpflose());
		}

		/** 总进球 显示值绑定 **/
		setChildView(chiHolder, dtm);
		chiHolder.mItemId.setText(dtm.getMatchNumber());
		chiHolder.mainTeam.setText(dtm.getMainTeam());
		chiHolder.guestTeam.setText(dtm.getGuestTeam());
		chiHolder.mItemName.setText(dtm.getGame());
		chiHolder.mItemTime.setText(dtm.getStopSellTime().substring(
				dtm.getStopSellTime().length() - 8, 16));

		/** 胜按钮点击事件 **/
		chiHolder.btn_win.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = "";
				if (1 == playType) {// 让球胜平负
					num = "501";
				} else if (4 == playType) {// 胜平负
					num = "101";
				}
				onClickChange(map_hashMap_spf, chiHolder, groupId, index, num);
			}
		});

		/** 平按钮点击事件 **/
		chiHolder.btn_flat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = "";
				if (1 == playType) {// 让球胜平负
					num = "502";
				} else if (4 == playType) {// 胜平负
					num = "102";
				}
				onClickChange(map_hashMap_spf, chiHolder, groupId, index, num);

			}
		});

		/** 负按钮点击事件 **/
		chiHolder.btn_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = "";
				if (1 == playType) {// 让球胜平负
					num = "503";
				} else if (4 == playType) {// 胜平负
					num = "103";
				}
				onClickChange(map_hashMap_spf, chiHolder, groupId, index, num);
			}
		});

		/** 展开 比分投注区 **/
		chiHolder.btn_open_cbf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> selectResult = null;
				chiHolder.dialog = new SelectJCZQDialog(activity,
						R.style.dialog, dtm, 0);
				android.view.WindowManager.LayoutParams lp = chiHolder.dialog
						.getWindow().getAttributes();
				lp.height = LayoutParams.WRAP_CONTENT;
				chiHolder.dialog.getWindow().setAttributes(lp);
				if (6 == playType) {// 半全场
					if (map_hashMap_bqc.containsKey(groupId)) {
						if (null != map_hashMap_bqc.get(groupId)) {// 如果不为空则获取
							selectResult = map_hashMap_bqc.get(groupId).get(
									index);
						}
					}
					chiHolder.dialog
							.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口
								@Override
								public void getResult(int resultCode,
										ArrayList<String> selectResult) {
									if (1 == resultCode) {// 确定
										LinkedHashMap<Integer, ArrayList<String>> map1 = null;
										map1 = map_hashMap_bqc.get(groupId);
										if (null == map1) {
											map1 = new LinkedHashMap<Integer, ArrayList<String>>();
										}
										map1.put(index, selectResult);
										if (0 == selectResult.size()) {
											map1.remove(index);
										}
										map_hashMap_bqc.put(groupId, map1);
										if (0 == map1.size()) {
											map_hashMap_bqc.remove(groupId);
										}
										set_CBF_BQC_Text(chiHolder,
												selectResult);
										fragment.changeTextShow();
									}
								}
							});
				} else if (2 == playType) {// 比分
					if (map_hashMap_cbf.containsKey(groupId)) {
						if (null != map_hashMap_cbf.get(groupId)) {// 如果不为空则获取
							selectResult = map_hashMap_cbf.get(groupId).get(
									index);
						}
					}
					chiHolder.dialog
							.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口
								@Override
								public void getResult(int resultCode,
										ArrayList<String> selectResult) {
									if (1 == resultCode) {// 确定
										LinkedHashMap<Integer, ArrayList<String>> map1 = null;
										map1 = map_hashMap_cbf.get(groupId);
										if (null == map1) {
											map1 = new LinkedHashMap<Integer, ArrayList<String>>();
										}
										map1.put(index, selectResult);
										if (0 == selectResult.size()) {
											map1.remove(index);
										}
										map_hashMap_cbf.put(groupId, map1);
										if (0 == map1.size()) {
											map_hashMap_cbf.remove(groupId);
										}
										set_CBF_BQC_Text(chiHolder,
												selectResult);
										fragment.changeTextShow();
									}
								}
							});
				}
				chiHolder.dialog.show();
				if (null != selectResult) {
					chiHolder.dialog.setSelect(selectResult);// 将选中的结果传入弹出框
				}
				chiHolder.dialog.setSpfLayoutVisible(View.GONE);// 隐藏胜平负
				chiHolder.dialog.setBifenLayoutVisible(View.GONE);// 隐藏比分
				chiHolder.dialog.setZjqLayoutVisible(View.GONE);// 隐藏总进球
				chiHolder.dialog.setBqcLayoutVisible(View.GONE);// 隐藏半全场
				if (6 == playType) {
					chiHolder.dialog.setBqcLayoutVisible(View.VISIBLE);// 显示半全场
				} else if (2 == playType) {
					chiHolder.dialog.setBifenLayoutVisible(View.VISIBLE);// 显示比分
				}
			}
		});

		/** 总进球 点击 **/
		chiHolder.ll_btn_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "201");
			}
		});
		chiHolder.ll_btn_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "202");
			}
		});
		chiHolder.ll_btn_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "203");
			}
		});
		chiHolder.ll_btn_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "204");
			}
		});
		chiHolder.ll_btn_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "205");
			}
		});
		chiHolder.ll_btn_5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "206");
			}
		});

		chiHolder.ll_btn_6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "207");
			}
		});
		chiHolder.ll_btn_7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickChange(map_hashMap_zjq, chiHolder, groupId, index, "208");
			}
		});

		// 2选1投注玩法點擊事件
		if (dtm.getMainLoseBall() == -1) {
			chiHolder.btn_main_2x1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
							"101");
				}
			});

			chiHolder.btn_guest_2x1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
							"503");
				}
			});

		} else if (dtm.getMainLoseBall() == 1) {
			chiHolder.btn_main_2x1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
							"501");
				}
			});
			chiHolder.btn_guest_2x1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
							"103");
				}
			});
		}

		// 胜平负/让球的投注玩法點擊事件
		chiHolder.ll_btn_spfrq1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"101");
			}
		});
		chiHolder.ll_btn_spfrq2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"102");
			}
		});
		chiHolder.ll_btn_spfrq3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"103");
			}
		});
		chiHolder.ll_btn_spfrq4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"501");
			}
		});
		chiHolder.ll_btn_spfrq5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"502");
			}
		});
		chiHolder.ll_btn_spfrq6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"503");
			}
		});

		// 混合投注玩法點擊事件
		chiHolder.ll_btn_hhtz1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"101");
			}
		});
		chiHolder.ll_btn_hhtz2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"102");
			}
		});
		chiHolder.ll_btn_hhtz3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"103");
			}
		});
		chiHolder.ll_btn_hhtz4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"501");
			}
		});
		chiHolder.ll_btn_hhtz5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"502");
			}
		});
		chiHolder.ll_btn_hhtz6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onClickChange(map_hashMap_hhtz, chiHolder, groupId, index,
						"503");
			}

		});
		chiHolder.layout_result.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ArrayList<String> selectResult = null;
				if (map_hashMap_hhtz.containsKey(groupId)) {
					if (null != map_hashMap_hhtz.get(groupId).get(index)) {// 如果不为空则获取
						selectResult = map_hashMap_hhtz.get(groupId).get(index);
					}
				}
				chiHolder.dialog = new SelectJCZQDialog(context,
						R.style.dialog, dtm, 1);
				chiHolder.dialog
						.setDialogResultListener(new SelectJCZQDialog.DialogResultListener() {// 实现选择结果接口

							@Override
							public void getResult(int resultCode,
									ArrayList<String> selectResult) {
								if (1 == resultCode) {// 确定
									LinkedHashMap<Integer, ArrayList<String>> map1 = null;
									map1 = map_hashMap_hhtz.get(groupId);
									if (null == map1) {
										map1 = new LinkedHashMap<Integer, ArrayList<String>>();
									}
									map1.put(index, selectResult);
									if (0 == selectResult.size()) {
										map1.remove(index);
									}
									map_hashMap_hhtz.put(groupId, map1);
									if (0 == map1.size()) {
										map_hashMap_hhtz.remove(groupId);
									}
									change_hhtz_btn(chiHolder, selectResult);// 刷新布局
									fragment.changeTextShow();
								}
							}
						});
				chiHolder.dialog.show();
				if (null != selectResult) {
					chiHolder.dialog.setSelect(selectResult);// 将选中的结果传入弹出框
				}
				chiHolder.dialog.setSpfLayoutVisible(View.VISIBLE);// 显示胜平负
				chiHolder.dialog.setBifenLayoutVisible(View.VISIBLE);// 显示比分
				chiHolder.dialog.setZjqLayoutVisible(View.VISIBLE);// 显示总进球
				chiHolder.dialog.setBqcLayoutVisible(View.VISIBLE);// 显示半全场

				if (!dtm.isBQC()) {
					chiHolder.dialog.setBqcLayoutGone(View.GONE);
				}
				if (!dtm.isCBF()) {
					chiHolder.dialog.setBifenLayoutGone(View.GONE);
				}
				if (!dtm.isZJQ()) {
					chiHolder.dialog.setZjqLayoutGone(View.GONE);
				}
			}

		});
		return convertView;
	}

	/**
	 * 投注的点击事件
	 * 
	 * @param chiHolder
	 *            控件
	 * @param groupId
	 *            父id
	 * @param index
	 *            子id
	 * @param num
	 */
	public void onClickChange(
			LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<String>>> map_hashMap,
			ChildViewHolder chiHolder, int groupId, int index, String num) {
		ArrayList<String> selectResult = new ArrayList<String>();
		LinkedHashMap<Integer, ArrayList<String>> map = new LinkedHashMap<Integer, ArrayList<String>>();
		if (map_hashMap.containsKey(groupId)) {// 包含map
			map = map_hashMap.get(groupId);// 获取map
		}
		if (map.containsKey(index)) {// 包含list
			selectResult = map.get(index);// 获取list
		}
		if (selectResult.contains(num)) {// 包含结果则移除
			selectResult.remove(num);
		} else {
			selectResult.add(num);// 添加结果
		}
		map.put(index, selectResult);
		if (0 == selectResult.size()) {// 集合为空
			map.remove(index);// 移除
		}
		map_hashMap.put(groupId, map);
		if (0 == map.size()) {
			map_hashMap.remove(groupId);
		}
		if (1 == playType || 4 == playType) {// 胜平负，让球胜平负
			change_spf_btn(chiHolder, selectResult);
		} else if (5 == playType) {// 混合投注
			change_hhtz_btn(chiHolder, selectResult);// 刷新布局
		} else if (3 == playType) {// 总进球
			change_zjq_btn(chiHolder, selectResult);
		} else if (2 == playType || 6 == playType) {// 半全场，比分
			set_CBF_BQC_Text(chiHolder, selectResult);
		} else if (7 == playType) {// 胜平负/让球
			change_spfAndrq_btn(chiHolder, selectResult);// 刷新布局
		} else if (8 == playType) {// 2选1
			change_2x1_btn(chiHolder, selectResult);// 刷新布局
		}
		fragment.changeTextShow();
	}

	/** 总进球 **/
	private void findChildView(ChildViewHolder holder, View v) {
		/** 总进球 **/
		holder.ll_btn_0 = (LinearLayout) v.findViewById(R.id.jczq_zjq_0);
		holder.ll_btn_1 = (LinearLayout) v.findViewById(R.id.jczq_zjq_1);
		holder.ll_btn_2 = (LinearLayout) v.findViewById(R.id.jczq_zjq_2);
		holder.ll_btn_3 = (LinearLayout) v.findViewById(R.id.jczq_zjq_3);
		holder.ll_btn_4 = (LinearLayout) v.findViewById(R.id.jczq_zjq_4);
		holder.ll_btn_5 = (LinearLayout) v.findViewById(R.id.jczq_zjq_5);
		holder.ll_btn_6 = (LinearLayout) v.findViewById(R.id.jczq_zjq_6);
		holder.ll_btn_7 = (LinearLayout) v.findViewById(R.id.jczq_zjq_7);

		holder.tv_0 = (TextView) v.findViewById(R.id.tv_0);
		holder.tv_1 = (TextView) v.findViewById(R.id.tv_1);
		holder.tv_2 = (TextView) v.findViewById(R.id.tv_2);
		holder.tv_3 = (TextView) v.findViewById(R.id.tv_3);
		holder.tv_4 = (TextView) v.findViewById(R.id.tv_4);
		holder.tv_5 = (TextView) v.findViewById(R.id.tv_5);
		holder.tv_6 = (TextView) v.findViewById(R.id.tv_6);
		holder.tv_7 = (TextView) v.findViewById(R.id.tv_7);

		holder.tv_0_1 = (TextView) v.findViewById(R.id.tv_0_1);
		holder.tv_1_1 = (TextView) v.findViewById(R.id.tv_1_1);
		holder.tv_2_1 = (TextView) v.findViewById(R.id.tv_2_1);
		holder.tv_3_1 = (TextView) v.findViewById(R.id.tv_3_1);
		holder.tv_4_1 = (TextView) v.findViewById(R.id.tv_4_1);
		holder.tv_5_1 = (TextView) v.findViewById(R.id.tv_5_1);
		holder.tv_6_1 = (TextView) v.findViewById(R.id.tv_6_1);
		holder.tv_7_1 = (TextView) v.findViewById(R.id.tv_7_1);
	}

	/** 改变 胜平负 ，让球胜平负 背景 **/
	private void change_spf_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.btn_win
				.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
		holder.btn_win.setTextColor(ColorUtil.BET_GRAY);

		holder.btn_flat
				.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
		holder.btn_flat.setTextColor(ColorUtil.BET_GRAY);
		holder.btn_lose
				.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
		holder.btn_lose.setTextColor(ColorUtil.BET_GRAY);
		if (null != selectList && 0 != selectList.size()) {
			if (selectList.contains("101") || selectList.contains("501")) {
				holder.btn_win
						.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				holder.btn_win.setTextColor(Color.WHITE);
			}
			if (selectList.contains("102") || selectList.contains("502")) {
				holder.btn_flat
						.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
				holder.btn_flat.setTextColor(Color.WHITE);
			}
			if (selectList.contains("103") || selectList.contains("503")) {
				holder.btn_lose
						.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				holder.btn_lose.setTextColor(Color.WHITE);
			}
		}
	}

	/** 2选1投注改变 button 背景 **/
	private void change_2x1_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.btn_main_2x1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.btn_main_2x1.setTextColor(ColorUtil.BET_GRAY);

		holder.btn_guest_2x1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.btn_guest_2x1.setTextColor(ColorUtil.BET_GRAY);

		if (null != selectList && 0 != selectList.size()) {// 不为空时

			if (selectList.contains("101") || selectList.contains("501")) {
				holder.btn_main_2x1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.btn_main_2x1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("103") || selectList.contains("503")) {
				holder.btn_guest_2x1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.btn_guest_2x1.setTextColor(Color.WHITE);
			}
		}
	}

	/** 胜平负/让球投注改变 button 背景 **/
	private void change_spfAndrq_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.ll_btn_spfrq1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_spfrq_01.setTextColor(ColorUtil.BET_GRAY);

		holder.ll_btn_spfrq2
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_spfrq_02.setTextColor(ColorUtil.BET_GRAY);

		holder.ll_btn_spfrq3
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_spfrq_03.setTextColor(ColorUtil.BET_GRAY);

		holder.ll_btn_spfrq4
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_spfrq_04.setTextColor(ColorUtil.BET_GRAY);

		holder.ll_btn_spfrq5
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_spfrq_05.setTextColor(ColorUtil.BET_GRAY);

		holder.ll_btn_spfrq6
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_spfrq_06.setTextColor(ColorUtil.BET_GRAY);

		if (null != selectList && 0 != selectList.size()) {// 不为空时
			int size = selectList.size();
			if (selectList.contains("1")) {
				size = selectList.size() - 1;
			}
			if (selectList.contains("101")) {
				holder.ll_btn_spfrq1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_spfrq_01.setTextColor(Color.WHITE);
			}
			if (selectList.contains("102")) {
				holder.ll_btn_spfrq2
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_spfrq_02.setTextColor(Color.WHITE);
			}
			if (selectList.contains("103")) {
				holder.ll_btn_spfrq3
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_spfrq_03.setTextColor(Color.WHITE);
			}
			if (selectList.contains("501")) {
				holder.ll_btn_spfrq4
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_spfrq_04.setTextColor(Color.WHITE);
			}
			if (selectList.contains("502")) {
				holder.ll_btn_spfrq5
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_spfrq_05.setTextColor(Color.WHITE);
			}
			if (selectList.contains("503")) {
				holder.ll_btn_spfrq6
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_spfrq_06.setTextColor(Color.WHITE);
			}
		} else {
		}
	}

	/** 混合投注改变 胜平负 button 背景 **/
	private void change_hhtz_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.ll_btn_hhtz1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_01.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz2
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_02.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz3
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_03.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz4
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_04.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz5
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_05.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_hhtz6
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_hhtz_06.setTextColor(ColorUtil.BET_GRAY);
		if (null != selectList && 0 != selectList.size()) {// 不为空时
			int size = selectList.size();
			if (selectList.contains("1")) {
				size = selectList.size() - 1;
			}
			holder.tv_result1.setText("已选");
			holder.tv_result1.setTextColor(Color.WHITE);
			holder.tv_result2.setText(size + "个");
			holder.tv_result2.setTextColor(Color.WHITE);
			holder.layout_result
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);

			if (0 == size) {
				holder.tv_result1.setText("展开");
				holder.tv_result1.setTextColor(ColorUtil.BET_GRAY);
				holder.tv_result2.setText("全部");
				holder.tv_result2.setTextColor(ColorUtil.BET_GRAY);
				holder.layout_result
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			}
			if (selectList.contains("101")) {
				holder.ll_btn_hhtz1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_01.setTextColor(Color.WHITE);
			}
			if (selectList.contains("102")) {
				holder.ll_btn_hhtz2
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_02.setTextColor(Color.WHITE);
			}
			if (selectList.contains("103")) {
				holder.ll_btn_hhtz3
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_03.setTextColor(Color.WHITE);
			}
			if (selectList.contains("501")) {
				holder.ll_btn_hhtz4
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_04.setTextColor(Color.WHITE);
			}
			if (selectList.contains("502")) {
				holder.ll_btn_hhtz5
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_05.setTextColor(Color.WHITE);
			}
			if (selectList.contains("503")) {
				holder.ll_btn_hhtz6
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_hhtz_06.setTextColor(Color.WHITE);
			}
		} else {
			holder.tv_result1.setText("展开");
			holder.tv_result1.setTextColor(ColorUtil.BET_GRAY);
			holder.tv_result2.setText("全部");
			holder.tv_result2.setTextColor(ColorUtil.BET_GRAY);
			holder.layout_result
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		}
	}

	/** 改变 总进球 背景 **/
	private void change_zjq_btn(ChildViewHolder holder,
			ArrayList<String> selectList) {
		holder.ll_btn_0
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_0.setTextColor(Color.RED);
		holder.tv_0_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_1
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_1.setTextColor(Color.RED);
		holder.tv_1_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_2
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_2.setTextColor(Color.RED);
		holder.tv_2_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_3
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_3.setTextColor(Color.RED);
		holder.tv_3_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_4
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_4.setTextColor(Color.RED);
		holder.tv_4_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_5
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_5.setTextColor(Color.RED);
		holder.tv_5_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_6
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_6.setTextColor(Color.RED);
		holder.tv_6_1.setTextColor(ColorUtil.BET_GRAY);
		holder.ll_btn_7
				.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
		holder.tv_7.setTextColor(Color.RED);
		holder.tv_7_1.setTextColor(ColorUtil.BET_GRAY);
		if (null != selectList) {
			if (selectList.contains("201")) {
				holder.ll_btn_0
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_0.setTextColor(Color.WHITE);
				holder.tv_0_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("202")) {
				holder.ll_btn_1
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_1.setTextColor(Color.WHITE);
				holder.tv_1_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("203")) {
				holder.ll_btn_2
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_2.setTextColor(Color.WHITE);
				holder.tv_2_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("204")) {
				holder.ll_btn_3
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_3.setTextColor(Color.WHITE);
				holder.tv_3_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("205")) {
				holder.ll_btn_4
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_4.setTextColor(Color.WHITE);
				holder.tv_4_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("206")) {
				holder.ll_btn_5
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_5.setTextColor(Color.WHITE);
				holder.tv_5_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("207")) {
				holder.ll_btn_6
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_6.setTextColor(Color.WHITE);
				holder.tv_6_1.setTextColor(Color.WHITE);
			}
			if (selectList.contains("208")) {
				holder.ll_btn_7
						.setBackgroundResource(R.drawable.select_jczq_tv_bolder_red);
				holder.tv_7.setTextColor(Color.WHITE);
				holder.tv_7_1.setTextColor(Color.WHITE);
			}
		}
	}

	/** 绑定总进球 显示信息 **/
	private void setChildView(ChildViewHolder holder, DtMatch dtm) {
		holder.tv_0_1.setText(dtm.getIn0());
		holder.tv_1_1.setText(dtm.getIn1());
		holder.tv_2_1.setText(dtm.getIn2());
		holder.tv_3_1.setText(dtm.getIn3());
		holder.tv_4_1.setText(dtm.getIn4());
		holder.tv_5_1.setText(dtm.getIn5());
		holder.tv_6_1.setText(dtm.getIn6());
		holder.tv_7_1.setText(dtm.getIn7());
	}

	/**
	 * 将id转换成比赛结果
	 * 
	 * @param number
	 * @return
	 */
	public String changeNumToResult(int number) {
		String result = "";
		switch (number) {
		case 401:
			result = "胜胜";
			break;
		case 402:
			result = "胜平";
			break;
		case 403:
			result = "胜负";
			break;
		case 404:
			result = "平胜";
			break;
		case 405:
			result = "平平";
			break;
		case 406:
			result = "平负";
			break;
		case 407:
			result = "负胜";
			break;
		case 408:
			result = "负平";
			break;
		case 409:
			result = "负负";
			break;
		case 301:
			result = "1:0";
			break;
		case 302:
			result = "2:0";
			break;
		case 303:
			result = "2:1";
			break;
		case 304:
			result = "3:0";
			break;
		case 305:
			result = "3:1";
			break;
		case 306:
			result = "3:2";
			break;
		case 307:
			result = "4:0";
			break;
		case 308:
			result = "4:1";
			break;
		case 309:
			result = "4:2";
			break;
		case 310:
			result = "5:0";
			break;
		case 311:
			result = "5:1";
			break;
		case 312:
			result = "5:2";
			break;
		case 313:
			result = "胜其他";
			break;
		case 314:
			result = "0:0";
			break;
		case 315:
			result = "1:1";
			break;
		case 316:
			result = "2:2";
			break;
		case 317:
			result = "3:3";
			break;
		case 318:
			result = "平其他";
			break;
		case 319:
			result = "0:1";
			break;
		case 320:
			result = "0:2";
			break;
		case 321:
			result = "1:2";
			break;
		case 322:
			result = "0:3";
			break;
		case 323:
			result = "1:3";
			break;
		case 324:
			result = "2:3";
			break;
		case 325:
			result = "0:4";
			break;
		case 326:
			result = "1:4";
			break;
		case 327:
			result = "2:4";
			break;
		case 328:
			result = "0:5";
			break;
		case 329:
			result = "1:5";
			break;
		case 330:
			result = "2:5";
			break;
		case 331:
			result = "负其他";
			break;
		}
		return result;

	}

	/** 给猜比分和半全场按钮设置显示值 **/
	private void set_CBF_BQC_Text(ChildViewHolder chiHolder,
			ArrayList<String> selectList) {
		String s = "";
		if (6 == playType) {// 半全场
			if (null != selectList && 0 != selectList.size()) {
				s = changeOddsToResult(selectList);
				chiHolder.btn_open_cbf.setTextColor(Color.WHITE);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_red);
			} else {
				s = "点击展开投注区";
				chiHolder.btn_open_cbf.setTextColor(ColorUtil.BET_GRAY);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_white);
			}
		} else if (2 == playType) {// 猜比分
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
			if (null != selectList && 0 != selectList.size()) {
				s = changeOddsToResult(selectList);
				chiHolder.btn_open_cbf.setTextColor(Color.WHITE);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_red);
			} else {
				s = "请选择比分投注";
				chiHolder.btn_open_cbf.setTextColor(ColorUtil.BET_GRAY);
				chiHolder.btn_open_cbf
						.setBackgroundResource(R.drawable.select_jc_bg_white);
			}
		}
		chiHolder.btn_open_cbf.setText(s);
	}

	/**
	 * * 转换半全场和比分数据
	 * 
	 * @return 转换后的结果
	 * @param resultlist
	 *            选择结果
	 */

	public String changeOddsToResult(ArrayList<String> resultlist) {
		StringBuffer result = new StringBuffer();
		int[] array = new int[resultlist.size()];
		for (int i = 0; i < resultlist.size(); i++) {
			array[i] = Integer.parseInt(resultlist.get(i).trim());
		}
		Arrays.sort(array);
		for (int i = 0; i < array.length; i++) {
			if (!"".equals(changeNumToResult(array[i]))) {
				result.append("|" + changeNumToResult(array[i]));
			}
		}
		return result.substring(1);
	}

	/** 组空间 */
	static class GroupViewHolder {
		TextView tv_date;
		TextView tv_count;
		LinearLayout ll_win_jc_top;
		ImageView iv_line;
	}

	/** 子类控件 */
	static class ChildViewHolder {
		TextView mItemId, mItemName, mItemTime;

		TextView mainTeam, guestTeam, ball;

		Button btn_main_2x1, btn_guest_2x1;// 混投2选1
		Button btn_win;
		Button btn_flat;
		Button btn_lose;
		TextView btn_open_cbf; // 展开猜比分按钮

		LinearLayout layout_isspf;// 胜平负布局
		LinearLayout hhtz_bottom;// 让球胜平负布局

		TextView tv_tip_isnotrqspf;// 让球胜平负暂停销售
		TextView tv_tip_isnotspf;// 胜平负暂停销售

		LinearLayout layout_spfandrq_isspf;// （胜平负/让球）的胜平负布局
		LinearLayout hhtz_spfandrq_bottom;// （胜平负/让球）的 让球胜平负布局

		TextView tv_tip_spfandrq_isnotrqspf;// （胜平负/让球）的让球胜平负暂停销售
		TextView tv_tip_spfandrq_isnotspf;// （胜平负/让球）的 胜平负暂停销售

		LinearLayout ll_spf, ll_cbf, ll_zjq, ll_hhtz, ll_spfrqspf, ll_2x1;

		LinearLayout ll_btn_0, ll_btn_1, ll_btn_2, ll_btn_3, ll_btn_4,
				ll_btn_5, ll_btn_6, ll_btn_7;
		// 混合投注點擊按鈕
		LinearLayout ll_btn_hhtz1, ll_btn_hhtz2, ll_btn_hhtz3, ll_btn_hhtz4,
				ll_btn_hhtz5, ll_btn_hhtz6, layout_result, layout_hhtz_rq;
		TextView tv_hhtz_01, tv_hhtz_02, tv_hhtz_03, tv_hhtz_04, tv_hhtz_05,
				tv_hhtz_06, tv_result1, tv_result2;// 胜平负
		// 胜平负/让球
		LinearLayout ll_btn_spfrq1, ll_btn_spfrq2, ll_btn_spfrq3,
				ll_btn_spfrq4, ll_btn_spfrq5, ll_btn_spfrq6;
		TextView tv_spfrq_01, tv_spfrq_02, tv_spfrq_03, tv_spfrq_04,
				tv_spfrq_05, tv_spfrq_06;

		TextView tv_0, tv_0_1, tv_1, tv_1_1, tv_2, tv_2_1, tv_3, tv_3_1, tv_4,
				tv_4_1, tv_5, tv_5_1, tv_6, tv_6_1, tv_7, tv_7_1, tv_hhtz_rq,
				tv_spfrq_rq;

		SelectJCZQDialog dialog;
	}

	/**
	 * 初始化所有数据
	 */
	public static void clearAllDate() {
		if (null != map_hashMap_spf) {
			map_hashMap_spf.clear();
		}
		if (null != map_hashMap_zjq) {
			map_hashMap_zjq.clear();
		}
		if (null != map_hashMap_cbf) {
			map_hashMap_cbf.clear();
		}
		if (null != map_hashMap_bqc) {
			map_hashMap_bqc.clear();
		}
		if (null != map_hashMap_hhtz) {
			map_hashMap_hhtz.clear();
		}
	}

	/**
	 * 清除已选数据
	 */
	public static void clearSelectMap() {
		if (null != map_hashMap_spf) {
			map_hashMap_spf.clear();
		}
		if (null != map_hashMap_zjq) {
			map_hashMap_zjq.clear();
		}
		if (null != map_hashMap_cbf) {
			map_hashMap_cbf.clear();
		}
		if (null != map_hashMap_bqc) {
			map_hashMap_bqc.clear();
		}
		if (null != map_hashMap_hhtz) {
			map_hashMap_hhtz.clear();
		}
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1 && !ip.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureTreeHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		TextView tv_date = (TextView) header.findViewById(R.id.tv_date);
		TextView tv_count = (TextView) header.findViewById(R.id.tv_playCount);
		header.setBackgroundResource(R.drawable.select_jc_lv_parent_win_up);
		tv_date.setText(mGroups.get(groupPosition));
		tv_count.setText(list_Matchs.get(groupPosition).size() + "场比赛可投");
	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);

	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

}
