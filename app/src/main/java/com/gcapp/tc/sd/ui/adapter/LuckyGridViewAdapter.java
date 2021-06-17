package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gcapp.tc.R;

/**
 * 功能：幸运选号的星座选择adapter
 * 
 * @author lenovo
 * 
 */
public class LuckyGridViewAdapter extends BaseAdapter {
	private static final String TAG = "LuckyGridViewAdapter";
	private Context context;
	private int[] resId;
	private int[] resIdSelected;
	private int selectIndex;
	private boolean isEnableed = true;

	public LuckyGridViewAdapter(Context context, int[] resId,
			int[] resIdSelected) {
		this.resId = resId;
		this.context = context;
		this.resIdSelected = resIdSelected;
	}

	public void setItemEnableed(boolean isEnableed) {
		this.isEnableed = isEnableed;
	}

	public void setSelectedIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

	@Override
	public int getCount() {
		return resId.length;
	}

	@Override
	public Object getItem(int position) {
		return resId[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyHolder myHolder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_lucky_gv_item, null);
			myHolder = new MyHolder(convertView);
			convertView.setTag(myHolder);
		} else
			myHolder = (MyHolder) convertView.getTag();
		myHolder.iv_lucky.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEnableed) {
					selectIndex = position;
					notifyDataSetChanged();
					if (listener != null)
						listener.isChanged(true);
				}
			}
		});
		if (position == selectIndex)
			myHolder.iv_lucky.setBackgroundResource(resIdSelected[position]);
		else
			myHolder.iv_lucky.setBackgroundResource(resId[position]);
		return convertView;
	}

	static class MyHolder {
		public MyHolder(View view) {
			iv_lucky = (ImageView) view.findViewById(R.id.iv_lucky);
		}

		ImageView iv_lucky;
	}

	public void setOnSelectedChangedListener(OnSelectedChangedListener listener) {
		this.listener = listener;
	}

	public OnSelectedChangedListener listener;

	public interface OnSelectedChangedListener {
		void isChanged(boolean ischanged);
	}
}
