package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.R;

public class LuckyNumberAdapter extends BaseAdapter {
	private String[] lotteryNumbers;
	private Context mContext;

	public LuckyNumberAdapter(Context mContext, String[] lotteryNumbers) {
		this.lotteryNumbers = lotteryNumbers;
		this.mContext = mContext;
	}

	/**
	 * 给 幸运号码 数组赋值
	 */
	public void setLotteryNumber(String[] _lotteryNumbers) {
		this.lotteryNumbers = _lotteryNumbers;
	}

	@Override
	public int getCount() {
		return lotteryNumbers.length;
	}

	@Override
	public Object getItem(int position) {
		return lotteryNumbers[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			LayoutInflater inflact = LayoutInflater.from(mContext);
			convertView = inflact.inflate(R.layout.luck_number_item, null);
			holder = new ViewHolder();
			holder.tv_redNum = (TextView) convertView
					.findViewById(R.id.tv_redNum);
			holder.tv_blueNum = (TextView) convertView
					.findViewById(R.id.tv_blueNum);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 赋值
		holder.tv_redNum.setText("");
		holder.tv_blueNum.setText("");

		if (lotteryNumbers[position].split("-").length == 2) {
			holder.tv_redNum.setText(lotteryNumbers[position].split("-")[0]);
			holder.tv_blueNum.setText(" "
					+ lotteryNumbers[position].split("-")[1]);
		} else {
			holder.tv_redNum.setText(lotteryNumbers[position]);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView tv_redNum, tv_blueNum;
	}

}