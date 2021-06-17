package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.IntegralModel;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2019-7-5 下午3:31:37
 * @version 5.5.0 
 * @Description 
 */
public class BasketIntegralAdapter extends BaseAdapter{

	private int resourceId;
    private Context mContext = null;
    private List<IntegralModel> mlist = null;
	
	public BasketIntegralAdapter(Context context, int textViewResourceId,List<IntegralModel> list) {
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
			 viewHolder.list_win_text = (TextView) convertView.findViewById(R.id.list_win_text);
			 viewHolder.list_defeatCount_text = (TextView) convertView.findViewById(R.id.list_defeatCount_text);
			 viewHolder.list_winstate_text = (TextView) convertView.findViewById(R.id.list_winstate_text);
			 viewHolder.list_probability_text = (TextView) convertView.findViewById(R.id.list_probability_text);
			 convertView.setTag (viewHolder);
		} else {
			 viewHolder = (ViewHolder) convertView.getTag ();
		 }
		if (item != null) {
			viewHolder.list_learue_text.setText(item.getLeagueRanking());
			viewHolder.list_team_text.setText(item.getTeamName());
//			viewHolder.list_end_text.setText(item.getMatchCount());
			viewHolder.list_win_text.setText(item.getWinCount());
//			viewHolder.list_flat_text.setText(item.getFlatCount());
			viewHolder.list_defeatCount_text.setText(item.getDefeatCount());
//			viewHolder.list_enter_text.setText(item.getEnterCount());
//			viewHolder.list_lose_text.setText(item.getLoseCount());
			viewHolder.list_probability_text.setText(item.getWinProbability()+"%");
			if(item.getWinState().equals("0")) {
				viewHolder.list_winstate_text.setText(item.getWinContent()+"连败");
				viewHolder.list_winstate_text.setTextColor(mContext.getResources().getColor(R.color.red));
			}else{
				viewHolder.list_winstate_text.setText(item.getWinContent()+"连胜");
				viewHolder.list_winstate_text.setTextColor(mContext.getResources().getColor(R.color.green));
			}
//			if(!item.getTeamType().equals("") && item.getTeamType() != null){
//				viewHolder.list_item_layout.setBackgroundResource(R.color.live_integral_bg);
//			}else{
//				viewHolder.list_item_layout.setBackgroundResource(R.color.white);
//			}
		}
		return convertView;
	}
	
	class ViewHolder {
        TextView list_learue_text;
        TextView list_team_text;
        TextView list_win_text;
        TextView list_defeatCount_text;
        TextView list_probability_text;
        TextView list_winstate_text;
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
