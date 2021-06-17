package com.gcapp.tc.sd.ui.adapter;

/**
 * 功能：竞彩的开奖详情详情adapter
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.LotteryDtMatch;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.view.IphoneTreeView;
import com.gcapp.tc.view.IphoneTreeView.IphoneTreeHeaderAdapter;
import com.gcapp.tc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class ExpandAdapter_lottery extends BaseExpandableListAdapter implements
		IphoneTreeHeaderAdapter {
	public Context context;
	/** 组项 **/
	private List<String> mGroups;
	/** 子项 **/
	private List<List<LotteryDtMatch>> list_Matchs;
	private String weekday = "";
	/** 玩法 **/
	private int lotteryId = 1;
	private IphoneTreeView ip;
	private HashMap<Integer, Integer> groupStatusMap;

	private String date = null;

	public ExpandAdapter_lottery(Context context,
			List<List<LotteryDtMatch>> listMatch, int _lotteryId,
			IphoneTreeView ip, String date) {
		this.context = context;
		this.date = date;
		setList_Matchs(listMatch);
		this.lotteryId = _lotteryId;
		this.ip = ip;
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	/** 给数组赋值 */
	private void setGroup() {
		mGroups = new ArrayList<String>();
		mGroups.clear();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < list_Matchs.size(); i++) {

			if (date != null) {
				Date nowDate = null;
				try {
					nowDate = format.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date nowDate2 = new Date(nowDate.getTime() - i * 24 * 60 * 60
						* 1000);
				String dateStr = format.format(nowDate2);
				mGroups.add(dateStr + "#" + list_Matchs.get(i).size());
			} else {
				if (list_Matchs.get(i).size() == 0) {
					continue;
				}
				mGroups.add(list_Matchs.get(i).get(0).getMatchDate() + "#"
						+ list_Matchs.get(i).get(0).getWeekDay());
			}
		}
	}

	/** 设置子项集合的值 */
	private void setList_Matchs(List<List<LotteryDtMatch>> _list_Matchs) {
		list_Matchs = new ArrayList<List<LotteryDtMatch>>();
		for (List<LotteryDtMatch> list : _list_Matchs) {
			List<LotteryDtMatch> listM = new ArrayList<LotteryDtMatch>();
			for (LotteryDtMatch dt : list) {
				listM.add(dt);
			}
			list_Matchs.add(listM);
		}
		setGroup();
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

	// 场比赛已开奖
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
			convertView = mInflater.inflate(
					R.layout.win_lottery_jc_detail_header, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_weekday = (TextView) convertView
					.findViewById(R.id.tv_weekday);

			holder.ll_win_jc_top = (LinearLayout) convertView
					.findViewById(R.id.ll_win_jc_top);

			holder.iv_line = (ImageView) convertView.findViewById(R.id.iv_line);

			if (groupPosition == 0) {
				holder.iv_line.setVisibility(View.GONE);
			} else {
				holder.iv_line.setVisibility(View.VISIBLE);
			}
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

		try {
			weekday = LotteryUtils.getWeekOfDate(mGroups.get(groupPosition)
					.split("#")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.tv_date.setText(mGroups.get(groupPosition).split("#")[0] + " ("
				+ weekday + ")");
		if (Integer.parseInt(mGroups.get(groupPosition).split("#")[1].trim()) != 0
				&& (mGroups.get(groupPosition).split("#")[1].trim()).length() != 0) {
			holder.tv_weekday.setText(mGroups.get(groupPosition).split("#")[1]
					.trim() + "场比赛已开奖");
		} else {
			holder.tv_weekday.setText("暂无比赛开奖信息");
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (groupPosition > (list_Matchs.size() - 1)
				|| childPosition > (list_Matchs.get(groupPosition).size() - 1))
			return null;

		final ChildViewHolder chiHolder;
		if (convertView == null) {
			chiHolder = new ChildViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.lottery_jc_win_item, null);
			chiHolder.tv_game = (TextView) convertView
					.findViewById(R.id.tv_game);
			chiHolder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			chiHolder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);

			chiHolder.tv_scroll = (TextView) convertView
					.findViewById(R.id.tv_scorll);
			chiHolder.tv_mainTeam = (TextView) convertView
					.findViewById(R.id.tv_mainTeam);
			chiHolder.tv_gusTeam = (TextView) convertView
					.findViewById(R.id.tv_gusTeam);
			chiHolder.tv_loseBall = (TextView) convertView
					.findViewById(R.id.tv_loseBall);
			chiHolder.tv_result = (TextView) convertView
					.findViewById(R.id.tv_result);
			chiHolder.ll_right2 = (LinearLayout) convertView
					.findViewById(R.id.ll_right2);
			// 新加的布局
			chiHolder.tv_result1 = (TextView) convertView
					.findViewById(R.id.tv_result1);
			chiHolder.tv_result2 = (TextView) convertView
					.findViewById(R.id.tv_result2);
			chiHolder.tv_result3 = (TextView) convertView
					.findViewById(R.id.tv_result3);
			chiHolder.tv_result4 = (TextView) convertView
					.findViewById(R.id.tv_result4);
			chiHolder.tv_result5 = (TextView) convertView
					.findViewById(R.id.tv_result5);
			chiHolder.tv_resultInfo1 = (TextView) convertView
					.findViewById(R.id.tv_resultInfo1);
			chiHolder.tv_resultInfo2 = (TextView) convertView
					.findViewById(R.id.tv_resultInfo2);
			chiHolder.tv_resultInfo3 = (TextView) convertView
					.findViewById(R.id.tv_resultInfo3);
			chiHolder.tv_resultInfo4 = (TextView) convertView
					.findViewById(R.id.tv_resultInfo4);
			chiHolder.tv_resultInfo5 = (TextView) convertView
					.findViewById(R.id.tv_resultInfo5);
			chiHolder.ll_result5 = (LinearLayout) convertView
					.findViewById(R.id.ll_result5);

			chiHolder.Img_line = (ImageView) convertView
					.findViewById(R.id.Img_line);

			convertView.setTag(chiHolder);
		} else {
			chiHolder = (ChildViewHolder) convertView.getTag();
		}
		if (lotteryId == 73) {
			chiHolder.ll_result5.setVisibility(View.GONE);
			chiHolder.Img_line.setVisibility(View.GONE);
		} else {
			chiHolder.ll_result5.setVisibility(View.VISIBLE);
			chiHolder.Img_line.setVisibility(View.VISIBLE);
		}
		LotteryDtMatch dtm = list_Matchs.get(groupPosition).get(childPosition);
		chiHolder.tv_game.setText(dtm.getGame());
		if (lotteryId == 73) {
			chiHolder.tv_mainTeam.setText(dtm.getGuestTeam() + "(客)");
			chiHolder.tv_gusTeam.setText(dtm.getMainTeam() + "(主)");
		} else {
			chiHolder.tv_mainTeam.setText(dtm.getMainTeam() + "(主)");
			chiHolder.tv_gusTeam.setText(dtm.getGuestTeam() + "(客)");
		}

		chiHolder.tv_date.setText(dtm.getMatchNumber());
		chiHolder.tv_time.setText(dtm.getStopSellTime().substring(10, 16));
		int color_bg = 0;
		int color = 0;

		if ("胜".equals(dtm.getSpfResult()) || "主胜".equals(dtm.getSfResult())) {
			color_bg = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score3_bg);
			color = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score3_text);
		}
		if ("平".equals(dtm.getSpfResult())) {
			color_bg = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score1_bg);
			color = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score1_text);
		}
		if ("负".equals(dtm.getSpfResult()) || "主负".equals(dtm.getSfResult())) {
			color_bg = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score0_bg);
			color = context.getResources().getColor(
					R.color.win_lottery_jc_detail_score0_text);
		}
		// 显示不同玩法界面
		switch (lotteryId) {
		case 72:
			chiHolder.tv_result1.setText(dtm.getSpfResult() + "");
			chiHolder.tv_resultInfo1.setText(dtm.getSpfSp() + "");
			if (dtm.getLoseBall() > 0) {
				chiHolder.tv_result2.setText("(+" + dtm.getLoseBall() + ")"
						+ dtm.getRqspfResult() + "");
			} else {
				chiHolder.tv_result2.setText("(" + dtm.getLoseBall() + ")"
						+ dtm.getRqspfResult() + "");
			}
			chiHolder.tv_resultInfo2.setText(dtm.getRqspfSp() + "");
			chiHolder.tv_result3.setText(dtm.getBfResult() + "");
			chiHolder.tv_resultInfo3.setText(dtm.getBfSp() + "");
			chiHolder.tv_result4.setText(dtm.getZjqResult() + "球");
			chiHolder.tv_resultInfo4.setText(dtm.getZjqSp() + "");
			chiHolder.tv_result5.setText(dtm.getBqcResult() + "");
			chiHolder.tv_resultInfo5.setText(dtm.getBqcSp() + "");
			chiHolder.tv_scroll.setText(dtm.getAllResult());
			chiHolder.tv_result.setText("半" + dtm.getHalfResult());

			break;
		case 73:
			chiHolder.tv_result1.setText(dtm.getDxResult() + "分");
			chiHolder.tv_resultInfo1.setText(dtm.getDxfSp() + "");
			chiHolder.tv_result2.setText(dtm.getRfsfResult() + "");
			chiHolder.tv_resultInfo2.setText(dtm.getRfsfSp() + "");
			chiHolder.tv_result3.setText(dtm.getSfcResult() + "");
			chiHolder.tv_resultInfo3.setText(dtm.getSfcSp() + "");
			chiHolder.tv_result4.setText(dtm.getSfResult() + "");
			chiHolder.tv_resultInfo4.setText(dtm.getSfSp() + "");

			chiHolder.tv_scroll.setText(dtm.getResult());
			int score = 0;
			if(!dtm.getLoseScore().equals("")) {
				score = (int)Double.parseDouble(dtm.getLoseScore());
			}
			if (score > 0) {
				chiHolder.tv_result.setText("让分" + dtm.getLoseScore());
			} else {
				chiHolder.tv_result.setText("让分" + dtm.getLoseScore());
			}
			break;
		case 45:
			chiHolder.tv_result1.setText(dtm.getSxdsResult() + "");
			chiHolder.tv_resultInfo1.setText(dtm.getSxdsSp() + "");
			chiHolder.tv_result2.setText(dtm.getBqcResult() + "");
			chiHolder.tv_resultInfo2.setText(dtm.getBqcSp() + "");
			chiHolder.tv_result3.setText(dtm.getCbfResult() + "");
			chiHolder.tv_resultInfo3.setText(dtm.getBfSp() + "");
			chiHolder.tv_result4.setText(dtm.getZjqResult() + "球");
			chiHolder.tv_resultInfo4.setText(dtm.getZjqSp() + "");

			if (dtm.getLoseBall() > 0) {
				chiHolder.tv_result5.setText("(+" + dtm.getLoseScore() + ")"
						+ dtm.getSpfResult() + "");
			} else {
				chiHolder.tv_result5.setText("(" + dtm.getLoseScore() + ")"
						+ dtm.getSpfResult() + "");
			}
			chiHolder.tv_resultInfo5.setText(dtm.getRqspfSp() + "");
			chiHolder.tv_scroll.setText(dtm.getAllResult());
			chiHolder.tv_result.setText("半" + dtm.getHalfResult());
			break;
		}
		return convertView;
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

		TextView tv_weekday = (TextView) header.findViewById(R.id.tv_weekday);
		try {
			weekday = LotteryUtils.getWeekOfDate(mGroups.get(groupPosition)
					.split("#")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tv_date.setText(mGroups.get(groupPosition).split("#")[0] + " ("
				+ weekday + ")");

		if (Integer.parseInt(mGroups.get(groupPosition).split("#")[1].trim()) != 0
				&& (mGroups.get(groupPosition).split("#")[1].trim()).length() != 0) {
			tv_weekday.setText(mGroups.get(groupPosition).split("#")[1].trim()
					+ "场比赛已开奖");
		} else {
			tv_weekday.setText("暂无比赛开奖信息");
		}
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

	/** 组控件 */
	static class GroupViewHolder {
		TextView tv_date, tv_weekday;
		TextView tv_count;
		ImageView iv_line;
		LinearLayout ll_win_jc_top;
	}

	/** 子类控件 */
	static class ChildViewHolder {
		TextView tv_game, tv_id, tv_time, tv_date, tv_mainTeam, tv_gusTeam,
				tv_loseBall, tv_result, tv_scroll;
		// 新加的一排
		TextView tv_result1, tv_result2, tv_result3, tv_result4, tv_result5;
		TextView tv_resultInfo1, tv_resultInfo2, tv_resultInfo3,
				tv_resultInfo4, tv_resultInfo5;
		LinearLayout ll_right2, ll_result5;
		ImageView Img_line;
	}
}
