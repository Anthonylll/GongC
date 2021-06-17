package com.gcapp.tc.sd.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.R;

/**
 * 功能：竞彩的投注详情adapter
 * 
 * @author lenovo
 * 
 */
public class OrderInfoJcPlayTypeAdapter extends BaseAdapter {
	private Context context;
	private List<String> playtypes;
	private List<String> playResult;
	private List<List<Map<String, Object>>> playitems;

	public OrderInfoJcPlayTypeAdapter(Context context, List<String> playtypes,
			List<String> playResult, List<List<Map<String, Object>>> playitems) {
		this.context = context;
		this.playtypes = playtypes;
		this.playResult = playResult;
		this.playitems = playitems;
	}

	@Override
	public int getCount() {
		return playtypes.size();
	}

	@Override
	public Object getItem(int position) {
		return playtypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.orderinfo_jc_child_item, null);
			holder = new ViewHolder();
			holder.lin_playtpe = (TextView) convertView
					.findViewById(R.id.lin_playtpe);
			holder.orderinfo_jc_child_listview = (MyListView2) convertView
					.findViewById(R.id.orderinfo_jc_child_listview);
			holder.child_result = (TextView) convertView
					.findViewById(R.id.child_result);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.lin_playtpe.setText(playtypes.get(position));
		holder.orderinfo_jc_child_listview.setAdapter(new InnerAdapter(
				playitems.get(position)));
		List<Map<String, Object>> items = playitems.get(position);
		for(int i=0;i<items.size();i++) {
			if(items.get(i).get("select").toString().contains(playResult.get(position))){
				holder.child_result.setText(playResult.get(position));
				holder.child_result.setTextColor(context.getResources().getColor(R.color.main_red_new));
				break;
			}else{
				holder.child_result.setText(playResult.get(position));
				holder.child_result.setTextColor(context.getResources().getColor(R.color.grey));
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView lin_playtpe, child_result;
		MyListView2 orderinfo_jc_child_listview;
	}

	private class InnerAdapter extends BaseAdapter {
		List<Map<String, Object>> maps = null;

		public InnerAdapter(List<Map<String, Object>> maps) {
			this.maps = maps;
		}

		@Override
		public int getCount() {
			return maps.size();
		}

		@Override
		public Object getItem(int position) {
			return maps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			InnerViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.orderinfo_jc_child2_item, null);
				holder = new InnerViewHolder();
				holder.child_invest = (TextView) convertView
						.findViewById(R.id.child_invest);
				convertView.setTag(holder);
			} else {
				holder = (InnerViewHolder) convertView.getTag();
			}
			holder.child_invest.setText(Html.fromHtml(maps.get(position).get(
					"select")
					// + "<br><font color=\"#008000\">"
					+ "<br><font color=\"#666666\">"

					+ maps.get(position).get("odd") + "</font>"));
			return convertView;
		}

		private class InnerViewHolder {
			TextView child_invest;
		}
	}
}
