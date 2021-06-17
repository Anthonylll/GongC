package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.WinDetail;
import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：开奖详情的子项的适配器
 * 
 * @author lenovo
 * 
 */
public class WinInfoChildAdapter extends BaseAdapter {
	private List<WinDetail> winDetails;
	private Context context;

	public WinInfoChildAdapter(Context context, List<WinDetail> winDetails) {
		this.context = context;
		this.winDetails = winDetails;
	}

	@Override
	public int getCount() {
		return winDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return winDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.win_lottery_info_child_list_item, null);
			holder = new ViewHolder();
			holder.center_tv_level1 = (TextView) convertView
					.findViewById(R.id.center_tv_level1);
			holder.win_one_count = (TextView) convertView
					.findViewById(R.id.win_one_count);
			holder.lottery_one = (TextView) convertView
					.findViewById(R.id.lottery_one);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (winDetails == null || winDetails.isEmpty())
			return null;
		WinDetail detail = winDetails.get(position);
		holder.center_tv_level1
				.setText(TextUtils.isEmpty(detail.getBonusName()) ? "-"
						: detail.getBonusName());
		holder.win_one_count.setText((detail.getWinningCount() == -1) ? "-"
				: (detail.getWinningCount() + ""));
		holder.lottery_one
				.setText(TextUtils.isEmpty(detail.getBonusValue()) ? "0"
						: detail.getBonusValue());
		return convertView;
	}

	private class ViewHolder {
		private TextView center_tv_level1, win_one_count, lottery_one;
	}

}
