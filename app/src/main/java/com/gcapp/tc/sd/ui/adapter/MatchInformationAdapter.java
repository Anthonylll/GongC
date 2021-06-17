package com.gcapp.tc.sd.ui.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.MatchInformation;
import com.gcapp.tc.R;

/**
 * @author anthony
 * @date 2018-5-9 上午11:41:26
 * @version 5.5.0 
 * @Description 战绩：比赛listadapter
 */
public class MatchInformationAdapter extends BaseAdapter{
	
	private int resourceId;
    private Context mContext = null;
    private List<MatchInformation> mlist = null;
	
	public MatchInformationAdapter(Context context, int textViewResourceId,List<MatchInformation> list) {
		this.mContext = context;
        this.resourceId = textViewResourceId;
        this.mlist = list;
	}

	@Override
	public View getView(int position,View convertView,ViewGroup parent) {
		MatchInformation item = (MatchInformation) getItem(position);
		ViewHolder viewHolder = null;
		if(convertView == null) {
			 viewHolder = new ViewHolder ();
			 convertView = LayoutInflater.from (mContext).inflate (resourceId, null);
			 viewHolder.standings_information_time = (TextView) convertView.findViewById(R.id.standings_information_time);
			 viewHolder.standings_information_org = (TextView) convertView.findViewById(R.id.standings_information_org);
			 viewHolder.standings_team_score = (TextView) convertView.findViewById(R.id.standings_team_score);
			 viewHolder.standings_host_team = (TextView) convertView.findViewById(R.id.standings_host_team);
			 viewHolder.standings_guest_team = (TextView) convertView.findViewById(R.id.standings_guest_team);
			 viewHolder.standings_information_result = (TextView) convertView.findViewById(R.id.standings_information_result);
			 convertView.setTag (viewHolder);
		} else {
			 viewHolder = (ViewHolder) convertView.getTag ();
		 }
		if (item != null) {
			viewHolder.standings_information_time.setText(item.getMatchTime());
			viewHolder.standings_information_org.setText(item.getMatchOrganization());
			viewHolder.standings_team_score.setText(item.getMatchScore());
			viewHolder.standings_host_team.setText(item.getHostTeam());
			viewHolder.standings_guest_team.setText(item.getGuestTeam());
			viewHolder.standings_information_result.setText(item.getMatchResult());
			setTextColor(viewHolder.standings_information_result,item.getMatchResult());
			setTextColor(viewHolder.standings_team_score,item.getMatchResult());
		}
		return convertView;
	}
	
	class ViewHolder {
        TextView standings_information_time;
        TextView standings_information_org;
        TextView standings_team_score;
        TextView standings_information_result;
        TextView standings_host_team;
        TextView standings_guest_team;
	}
	
	private void setTextColor(TextView view,String result) {
		if(result.equals("胜")) {
			view.setTextColor(mContext.getResources().getColor(R.color.red));
		}else if(result.equals("平")) {
			view.setTextColor(mContext.getResources().getColor(R.color.blue));
		}else if(result.equals("负")){
			view.setTextColor(mContext.getResources().getColor(R.color.green));
		}else{
			view.setTextColor(mContext.getResources().getColor(R.color.black));
		}
	}
	
	@Override
	public int getCount() {
		return (mlist == null ? 0 : mlist.size ());
	}

	@Override
	public Object getItem(int position) {
		return (mlist == null ? null : mlist.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
