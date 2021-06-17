package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.WinInfo;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 功能：近5期的开奖号码的适配器
 * 
 * @author lenovo
 * 
 */
public class WinInfoAdapter extends BaseAdapter {
	Context context;
	List<WinInfo> list_windetail;

	public WinInfoAdapter(List<WinInfo> list1, Context context) {
		this.context = context;
		this.list_windetail = list1;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int id) {
		return list_windetail.get(id);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parentview) {
		ViewHolder_Winnumber holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder_Winnumber();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.item_wininfo, null);
			// 得到控件
			holder.qihao = (TextView) view.findViewById(R.id.tv_qi);// 期号
			holder.status = (TextView) view.findViewById(R.id.tv_status);// 状态
			holder.winnumber = (TextView) view.findViewById(R.id.tv_winnumber);// 开奖号码
			view.setTag(holder);

		} else {
			holder = (ViewHolder_Winnumber) view.getTag();
		}
		if (list_windetail.size() > 0) {
			if (list_windetail.size() > position) {
				WinInfo info = list_windetail.get(position);
				holder.qihao.setText(info.getWinQihao() + "期");
				holder.status.setText(info.getState());
				holder.winnumber.setText(info.getWinNumber());
			}
		}
		return view;
	}

	class ViewHolder_Winnumber {
		private TextView qihao, status, winnumber;// 期号，形态
	}

}
