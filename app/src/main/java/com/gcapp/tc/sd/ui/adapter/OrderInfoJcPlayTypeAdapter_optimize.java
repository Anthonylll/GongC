package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：竞彩的奖金优化的投注详情adapter
 * 
 * @author lenovo
 * 
 */
public class OrderInfoJcPlayTypeAdapter_optimize extends BaseAdapter {
	private Context context;
	private List<String> buyContent;
	private List<String> result;
	private List<Integer> markRed;

	public OrderInfoJcPlayTypeAdapter_optimize(Context context,
			List<String> buyContent, List<String> result, List<Integer> markRed) {
		this.context = context;
		this.buyContent = buyContent;
		this.result = result;
		this.markRed = markRed;
	}

	@Override
	public int getCount() {
		return buyContent.size();
	}

	@Override
	public Object getItem(int position) {
		return buyContent.get(position);
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
					R.layout.orderinfo_jc_child_item_optimize, null);
			holder = new ViewHolder();
			holder.child_touzhu_optimize = (TextView) convertView
					.findViewById(R.id.child_touzhu_optimize);
			holder.child_result_optimize = (TextView) convertView
					.findViewById(R.id.child_result_optimize);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String buyContentString = "";
		if (markRed.get(position) == 1) {
			buyContentString = "<font color=\"#EB1827\">"
					+ buyContent.get(position) + "</font>";
		} else {
			buyContentString = buyContent.get(position);
		}
		holder.child_touzhu_optimize.setText(Html.fromHtml(buyContentString));

		String resultsString = "";
		if (markRed.get(position) == 1) {
			resultsString = "<font color=\"#EB1827\">" + result.get(position)
					+ "</font>";
		} else {
			resultsString = result.get(position);
		}
		holder.child_result_optimize.setText(Html.fromHtml(resultsString));
		return convertView;
	}

	private class ViewHolder {
		TextView child_touzhu_optimize, child_result_optimize;
	}

}
