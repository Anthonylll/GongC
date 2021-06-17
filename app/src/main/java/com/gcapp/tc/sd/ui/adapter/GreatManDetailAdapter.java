package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.sd.ui.FollowInfoActivity;
import com.gcapp.tc.sd.ui.FollowInfo_jc_Activity;
import com.gcapp.tc.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 大神详情页面adapter
 */
public class GreatManDetailAdapter extends BaseAdapter{

	private Context mContext;
	private List<Schemes> schemeslist = new ArrayList<Schemes>();
	private ViewHolder viewHolder = null;
	
	public GreatManDetailAdapter(Context context, List<Schemes> list) {
		super();
		this.mContext = context;
		this.schemeslist = list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.man_programme_item,null);
			viewHolder.time = (TextView) convertView.findViewById(R.id.programme_initiate_time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.man_programme_content);
			viewHolder.lottery_name = (TextView) convertView.findViewById(R.id.man_lottery_name);
			viewHolder.total_money = (TextView) convertView.findViewById(R.id.man_total_money);
			viewHolder.single_money = (TextView) convertView.findViewById(R.id.man_single_money);
			viewHolder.win_money = (TextView) convertView.findViewById(R.id.win_money);
			viewHolder.follow_btn = (Button) convertView.findViewById(R.id.man_follow_btn);
			viewHolder.programme_layout = (RelativeLayout) convertView.findViewById(R.id.man_programme_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Schemes schemes = schemeslist.get(position);
		viewHolder.time.setText(schemes.getDateTime().substring(0,10));
		viewHolder.content.setText(schemes.getDescription());
		viewHolder.lottery_name.setText(schemes.getLotteryName());
		viewHolder.total_money.setText(String.valueOf(schemes.getMyBuyMoney()));
		viewHolder.single_money.setText(String.valueOf(schemes.getShareMoney()));
		String state = schemes.getSchemeStatus();
		if(state.equals("招募中")) {
			viewHolder.follow_btn.setVisibility(convertView.VISIBLE);
			viewHolder.win_money.setVisibility(convertView.GONE);
			
			viewHolder.follow_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					programmeDetail(position);
				}
			});
		}else if(state.equals("已出票")) {
			viewHolder.follow_btn.setVisibility(convertView.GONE);
			viewHolder.win_money.setVisibility(convertView.VISIBLE);
			viewHolder.win_money.setText("已出票");
			viewHolder.win_money.setTextColor(Color.parseColor("#FF0000"));
		}else if(state.equals("未出票")) {
			viewHolder.follow_btn.setVisibility(convertView.GONE);
			viewHolder.win_money.setVisibility(convertView.VISIBLE);
			viewHolder.win_money.setText("未出票");
			viewHolder.win_money.setTextColor(Color.parseColor("#FF0000"));
		}else{
			viewHolder.follow_btn.setVisibility(convertView.GONE);
			viewHolder.win_money.setVisibility(convertView.VISIBLE);
			viewHolder.win_money.setText(String.valueOf(schemes.getShareWinMoney())+"元");
			if((int)schemes.getWinMoneyNoWithTax() > 0) {
				viewHolder.win_money.setTextColor(Color.parseColor("#FF0000"));
			}else{
				viewHolder.win_money.setTextColor(Color.parseColor("#808080"));
			}
		}
		return convertView;
	}
	
	class ViewHolder {
		public TextView time;
		public TextView content;
		public TextView lottery_name;
		public TextView total_money;
		public TextView single_money;
		public TextView win_money;
		public Button follow_btn;
		public RelativeLayout programme_layout;
	}
	
	@Override
	public int getCount() {
		return (schemeslist != null) ? schemeslist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return schemeslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 跳转至跟单详情页
	 */
	private void programmeDetail(int position) {
		Intent intents;
		if (schemeslist.size() != 0) {
			Schemes schemes = schemeslist.get(position);
			if ("73".equals(schemes.getLotteryID())
					|| "72".equals(schemes.getLotteryID())
					|| "45".equals(schemes.getLotteryID())) {
				intents = new Intent(mContext, FollowInfo_jc_Activity.class);
			} else {
				intents = new Intent(mContext, FollowInfoActivity.class);
			}
			Bundle bundle = new Bundle();
			bundle.putSerializable("schem", schemes);
			schemes.getBuyContent();
			intents.putExtra("bundle", bundle);
			mContext.startActivity(intents);
		}
	}

}
