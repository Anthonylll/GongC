package com.gcapp.tc.sd.ui.adapter;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.sd.ui.BaseBetActivity;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：投注基类的适配器
 * 
 * @author lenovo
 * 
 */
public class BaseBetAdapter extends BaseAdapter {
	private List<SelectedNumbers> listSchemes;
	private BaseBetActivity baseBetActivity;

	public BaseBetAdapter(BaseBetActivity baseBetActivity,
			List<SelectedNumbers> params) {
		this.baseBetActivity = baseBetActivity;
		setListSchemes(params);
	}

	public void setListSchemes(List<SelectedNumbers> params) {
		if (params != null) {
			if (listSchemes == null) {
				listSchemes = new ArrayList<SelectedNumbers>();
			} else {
				listSchemes.clear();
			}
			for (SelectedNumbers numbers : params) {
				listSchemes.add(numbers);
			}
		}
	}

	public List<SelectedNumbers> getListSchemes() {
		return listSchemes;
	}

	public void add(SelectedNumbers numbers) {
		listSchemes.add(numbers);
	}

	public void add(int position, SelectedNumbers numbers) {
		listSchemes.add(position, numbers);
	}

	public void remove(int index) {
		listSchemes.remove(index);
	}

	public void clear() {
		listSchemes.clear();
	}

	@Override
	public int getCount() {
		return listSchemes.size();
	}

	@Override
	public Object getItem(int position) {
		return listSchemes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(baseBetActivity).inflate(
					R.layout.item_bet_lv, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (listSchemes.size() == 0) {
			return convertView;
		}
		final int index = position;
		SelectedNumbers num = listSchemes.get(position);
		Spanned number = Html.fromHtml("<font color='#BE0205'>"
				+ num.getLotteryNumber() + "</FONT>");
		holder.tv_show_number.setText(number);
		holder.tv_type_count_money.setText(num.getPlayTypeName() + " "
				+ num.getCount() + "注 " + num.getCount() * 2 + "元");
		holder.iv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				remove(index);
				notifyDataSetChanged();
				baseBetActivity.deletOne(index);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView iv_delete;
		TextView tv_show_number;
		TextView tv_type_count_money;

		public ViewHolder(View view) {
			iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			tv_show_number = (TextView) view.findViewById(R.id.tv_show_number);
			tv_type_count_money = (TextView) view
					.findViewById(R.id.tv_type_count_money);
		}
	}
}
