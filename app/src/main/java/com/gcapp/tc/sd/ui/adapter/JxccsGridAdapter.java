package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 江西时时彩grid适配器
 */
public class JxccsGridAdapter extends BaseAdapter {
	private boolean gone;
	private Context context;
	private String[] numbers;
	private int type; // 0为圆形，1为方形
	private HashSet<String> oneSet = new HashSet<String>();
	private List<String> list_yilou = new ArrayList<String>(); // 遗漏值

	public JxccsGridAdapter(Context context, String[] numbers,
			List<String> yilou, int mark, boolean visileOrnot) {
		this.context = context;
		this.numbers = numbers;
		this.list_yilou = yilou;
		type = 0;
		this.gone = visileOrnot;
	}

	public JxccsGridAdapter(Context context, String[] numbers, int type,
			boolean visileOrnot, List<String> yilou) {
		this.context = context;
		this.numbers = numbers;
		this.type = type;
		this.gone = visileOrnot;
		this.list_yilou = yilou;
	}

	public JxccsGridAdapter(Context context, String[] numbers,
			List<String> list, boolean visileOrnot) {
		this.context = context;
		this.numbers = numbers;
		setOneSet(list);
		this.gone = visileOrnot;
	}

	public void setVisibles(boolean visibleGone) {
		gone = visibleGone;
	}

	public void setOneSet(List<String> list) {
		if (null == list)
			return;
		clear();
		for (String s : list) {
			oneSet.add(s);
		}
	}

	public HashSet<String> getOneSet() {
		return oneSet;
	}

	public boolean contains(String num) {
		return oneSet.contains(num);
	}

	public void add(String num) {
		oneSet.add(num);
	}

	public void remove(String num) {
		oneSet.remove(num);
	}

	public void clear() {
		oneSet.clear();
	}

	@Override
	public int getCount() {
		return numbers.length;
	}

	@Override
	public Object getItem(int position) {
		return numbers[position];
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
					R.layout.gridview_items, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textNum.setTextColor(context.getResources().getColor(
				R.color.main_red));
		holder.textNum
				.setBackgroundResource(type == 0 ? R.drawable.icon_ball_red_unselected
						: R.drawable.bet_btn_dan_unselected);

		if (oneSet.contains(numbers[position])) {
			holder.textNum
					.setBackgroundResource(type == 0 ? R.drawable.icon_ball_red_selected
							: R.drawable.bet_btn_dan_selected);
			holder.textNum.setTextColor(Color.WHITE);
		}
		holder.textNum.setText(numbers[position]);

		// 处理遗漏值
		if (gone) {
			holder.tv_yilou.setVisibility(View.VISIBLE);
			if (list_yilou.size() > 0) {
				holder.tv_yilou.setText(list_yilou.get(position));
			} else {
				holder.tv_yilou.setVisibility(View.GONE);
			}
		} else {
			holder.tv_yilou.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView textNum;
		TextView tv_yilou;

		public ViewHolder(View view) {
			textNum = (TextView) view.findViewById(R.id.btn_showNum);
			tv_yilou = (TextView) view.findViewById(R.id.tv_yilou);
		}
	}
}
