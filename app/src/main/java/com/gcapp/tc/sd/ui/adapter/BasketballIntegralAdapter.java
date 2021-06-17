package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.TotalIntegralModel;
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
 * @author dm
 * @date 2019-7-5 下午3:28:20
 * @version 5.5.0 
 * @Description 
 */
public class BasketballIntegralAdapter extends BaseExpandableListAdapter{

	private List<String> grouplist;
	private List<List<TotalIntegralModel>> childlist;
	private Context mContext;
	private BasketIntegralAdapter integralAdapter;

	public BasketballIntegralAdapter(Context context, List<String> grouplist,
			List<List<TotalIntegralModel>> childlist) {
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
					R.layout.live_integral_basketball_child, parent, false);
			childViewHolder = new ChildViewHolder();
			childViewHolder.team_area= (TextView) convertView.findViewById(R.id.team_area);
			childViewHolder.record_integral_list= (MyListView2) convertView.findViewById(R.id.record_integral_list);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		TotalIntegralModel tIntegralModel = childlist.get(groupPosition).get(childPosition);
		childViewHolder.team_area.setText("· "+tIntegralModel.getTeamArea());
		integralAdapter = new BasketIntegralAdapter(mContext,R.layout.live_integral_basket_list,tIntegralModel.getIntegralList());
		childViewHolder.record_integral_list.setAdapter(integralAdapter);
		ListViewUtils.setListViewHeightBasedOnChildren(childViewHolder.record_integral_list);
		return convertView;
	}

	static class GroupViewHolder {
		TextView tvTitle;
		ImageView group_image;
	}

	static class ChildViewHolder {
		TextView team_area;
		MyListView2 record_integral_list;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
