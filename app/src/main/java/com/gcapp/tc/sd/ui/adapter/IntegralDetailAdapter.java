package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.IntegralInfo;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：积分明细adapter
 * 
 * @author lenovo
 * 
 */
public class IntegralDetailAdapter extends BaseAdapter {
	private Context context;
	private List<IntegralInfo> listData;

	public IntegralDetailAdapter(Context context, List<IntegralInfo> listData) {
		this.context = context;
		setAll(listData);
	}

	public void setAll(List<IntegralInfo> Data) {
		if (listData == null) {
			listData = new ArrayList<IntegralInfo>();
		}
		listData.clear();
		for (IntegralInfo info : Data) {
			listData.add(info);
		}
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.integral_item, null);

			// 得到控件
			holder.time = (TextView) view.findViewById(R.id.time);
			holder.once = (TextView) view.findViewById(R.id.once);
			holder.way = (TextView) view.findViewById(R.id.way);// 方式
			holder.total = (TextView) view.findViewById(R.id.total);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (listData.isEmpty()) {
			return view;
		}
		// 给控件赋值
		IntegralInfo Info = listData.get(position);
		// 赋值
		holder.time.setText(Info.getTime() + "");
		Spanned html;
		String once = Info.getOnece();
		if (once.startsWith("+")) {
			html = Html.fromHtml("<font color=\"#227be3\">" + once + "</font>");
			holder.once.setText(html);
		} else {
			holder.once.setText(once);
		}
		holder.way.setText(typeToWays(Info.getWay()));
		holder.total.setText(Info.getTotal());
		return view;
	}

	static class ViewHolder {
		TextView time;
		TextView once;
		TextView way;
		TextView total;
	}

	private String typeToWays(String type) {
		String typeName;
		switch (Integer.valueOf(type)) {
		case 1:
			typeName = "购彩赠送积分";
			break;
		case 2:
			typeName = "下级购彩赠送积分";
			break;
		case 3:
			typeName = "系统赠送积分";
			break;
		case 4:
			typeName = "手工赠加积分";
			break;
		case 5:
			typeName = "中奖送积分";
			break;
		case 101:
			typeName = "兑换积分";
			break;
		case 201:
			typeName = "惩罚扣积分";
			break;
		case 301:
			typeName = "签到赠送积分";
			break;
		default:
			typeName = "赠送积分";
			break;
		}
		return typeName;
	}
}
