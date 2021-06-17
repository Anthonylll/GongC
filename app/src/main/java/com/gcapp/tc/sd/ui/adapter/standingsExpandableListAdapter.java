package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.TeamStandings;
import com.gcapp.tc.utils.ListViewUtils;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-5-8 上午9:57:11
 * @version 5.5.0 
 * @Description 
 */
public class standingsExpandableListAdapter extends BaseExpandableListAdapter{

	private List<String> grouplist;
	private List<List<TeamStandings>> childlist;
	private Context mContext;
	private MatchInformationAdapter informationAdapter;

	public standingsExpandableListAdapter(Context context, List<String> grouplist,
			List<List<TeamStandings>> childlist) {
		super();
		this.mContext = context;
		this.grouplist = grouplist;
		this.childlist = childlist;
	}

	@Override
	public int getGroupCount() {
		return grouplist.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childlist.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return grouplist.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childlist.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.activity_live_group_item, parent, false);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.group_text);
			groupViewHolder.group_image = (ImageView) convertView
					.findViewById(R.id.group_image);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		groupViewHolder.tvTitle.setText(grouplist.get(groupPosition));

		if (childlist.get(groupPosition).size() == 0) {
			groupViewHolder.group_image.setVisibility(View.GONE);
		} else {
			groupViewHolder.group_image.setVisibility(View.VISIBLE);
			if (isExpanded) {
				groupViewHolder.group_image
						.setBackgroundResource(R.drawable.win_lottery_detail_up);
			} else {
				groupViewHolder.group_image
						.setBackgroundResource(R.drawable.win_lottery_detail_down);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.live_standings_child_item, parent, false);
			childViewHolder = new ChildViewHolder();
			childViewHolder.match_count = (TextView) convertView
					.findViewById(R.id.match_count);
			childViewHolder.win_count= (TextView) convertView.findViewById(R.id.win_count);
			childViewHolder.bisection_count= (TextView) convertView.findViewById(R.id.bisection_count);
			childViewHolder.lose_count= (TextView) convertView.findViewById(R.id.lose_count);
			childViewHolder.standings_hint_text= (TextView) convertView.findViewById(R.id.standings_hint_text);
			childViewHolder.result_date= (TextView) convertView.findViewById(R.id.result_date);
			childViewHolder.record_match_list= (MyListView2) convertView.findViewById(R.id.record_match_list);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		TeamStandings teamStandings = childlist.get(groupPosition).get(childPosition);
		childViewHolder.win_count.setText(teamStandings.getWinCount());
		childViewHolder.bisection_count.setText(teamStandings.getBisectionCount());
		childViewHolder.lose_count.setText(teamStandings.getLoseCount());
		String win = teamStandings.getWinCount();
		if(!win.equals("") && win != null) {
			childViewHolder.result_date.setText("胜负");
			childViewHolder.match_count.setText("·近"+teamStandings.getMatchCount()+"场,"+teamStandings.getTeamName());
		}else{
			childViewHolder.result_date.setText("相隔");
			childViewHolder.match_count.setText(teamStandings.getTeamName());
		}
		if(teamStandings.getMatchList().size() == 0 || teamStandings.getMatchList() == null) {
			childViewHolder.standings_hint_text.setVisibility(View.VISIBLE);
		}else{
			childViewHolder.standings_hint_text.setVisibility(View.GONE);
		}
		informationAdapter = new MatchInformationAdapter(mContext,R.layout.match_information_list,teamStandings.getMatchList());
		childViewHolder.record_match_list.setAdapter(informationAdapter);
		ListViewUtils.setListViewHeightBasedOnChildren(childViewHolder.record_match_list);
		return convertView;
	}

	static class GroupViewHolder {
		TextView tvTitle;
		ImageView group_image;
	}

	static class ChildViewHolder {
		TextView match_count;
		TextView win_count;
		TextView bisection_count;
		TextView lose_count;
		TextView standings_hint_text;
		TextView result_date;
		MyListView2 record_match_list;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
