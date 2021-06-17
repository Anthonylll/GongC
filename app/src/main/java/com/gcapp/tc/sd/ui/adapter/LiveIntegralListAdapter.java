package com.gcapp.tc.sd.ui.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.IntegralModel;
import com.gcapp.tc.R;

/**
 * @author dm
 * @date 2018-8-8 下午5:07:29
 * @version 5.5.0 
 * @Description 直播积分adapter
 */
public class LiveIntegralListAdapter extends BaseAdapter{

	private int resourceId;
    private Context mContext = null;
    private List<IntegralModel> mlist = null;
	
	public LiveIntegralListAdapter(Context context, int textViewResourceId,List<IntegralModel> list) {
		this.mContext = context;
        this.resourceId = textViewResourceId;
        this.mlist = list;
	}

	@Override
	public View getView(int position,View convertView,ViewGroup parent) {
		IntegralModel item = (IntegralModel) getItem(position);
		ViewHolder viewHolder = null;
		if(convertView == null) {
			 viewHolder = new ViewHolder ();
			 convertView = LayoutInflater.from (mContext).inflate (resourceId, null);
			 viewHolder.list_learue_text = (TextView) convertView.findViewById(R.id.list_learue_text);
			 viewHolder.list_team_text = (TextView) convertView.findViewById(R.id.list_team_text);
			 viewHolder.list_end_text = (TextView) convertView.findViewById(R.id.list_end_text);
			 viewHolder.list_win_text = (TextView) convertView.findViewById(R.id.list_win_text);
			 viewHolder.list_flat_text = (TextView) convertView.findViewById(R.id.list_flat_text);
			 viewHolder.list_defeatCount_text = (TextView) convertView.findViewById(R.id.list_defeatCount_text);
			 viewHolder.list_enter_text = (TextView) convertView.findViewById(R.id.list_enter_text);
			 viewHolder.list_lose_text = (TextView) convertView.findViewById(R.id.list_lose_text);
			 viewHolder.list_entirely_text = (TextView) convertView.findViewById(R.id.list_entirely_text);
			 viewHolder.list_total_text = (TextView) convertView.findViewById(R.id.list_total_text);
			 viewHolder.list_item_layout = (LinearLayout) convertView.findViewById(R.id.list_item_layout);
			 convertView.setTag (viewHolder);
		} else {
			 viewHolder = (ViewHolder) convertView.getTag ();
		 }
		if (item != null) {
			viewHolder.list_learue_text.setText(item.getLeagueRanking());
			viewHolder.list_team_text.setText(item.getTeamName());
			viewHolder.list_end_text.setText(item.getMatchCount());
			viewHolder.list_win_text.setText(item.getWinCount());
			viewHolder.list_flat_text.setText(item.getFlatCount());
			viewHolder.list_defeatCount_text.setText(item.getDefeatCount());
			viewHolder.list_enter_text.setText(item.getEnterCount());
			viewHolder.list_lose_text.setText(item.getLoseCount());
			viewHolder.list_entirely_text.setText(item.getEntirelyCount());
			viewHolder.list_total_text.setText(item.getTotalIntegral());
			if(!item.getTeamType().equals("") && item.getTeamType() != null){
				viewHolder.list_item_layout.setBackgroundResource(R.color.live_integral_bg);
			}else{
				viewHolder.list_item_layout.setBackgroundResource(R.color.white);
			}
		}
		return convertView;
	}
	
	class ViewHolder {
        TextView list_learue_text;
        TextView list_team_text;
        TextView list_end_text;
        TextView list_win_text;
        TextView list_flat_text;
        TextView list_defeatCount_text;
        TextView list_enter_text;
        TextView list_lose_text;
        TextView list_entirely_text;
        TextView list_total_text;
        LinearLayout list_item_layout;
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
