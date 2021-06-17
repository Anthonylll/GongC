package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.LotteryContent;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：合买大厅的详情页面的选号详情adapter
 * 
 * @author echo
 * 
 */
public class MyFollowNumberAdapter extends BaseAdapter {

	private Context mContext;
	private List<LotteryContent> contents;
	private String zhushu;// 方案注数
	private int beishu;// 倍数
	private String fangshi;// 方式

	public MyFollowNumberAdapter(Context context,
			List<LotteryContent> contents, int beishu) {
		this.mContext = context;
		this.contents = contents;
		this.beishu = beishu;
	}

	@Override
	public int getCount() {
		return contents.size();
	}

	@Override
	public Object getItem(int arg0) {
		return contents.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 得到布局文件
			view = inflater.inflate(R.layout.follow_number_item, null);
			// 得到控件
			holder.tv_item_fangshi = (TextView) view
					.findViewById(R.id.tv_item_fangshi);
			holder.tv_item_touzhu = (TextView) view
					.findViewById(R.id.tv_item_touzhu);
			holder.tv_item_beishu = (TextView) view
					.findViewById(R.id.tv_item_beishu);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		LotteryContent content = contents.get(position);
		String[] str = transNum(content.getLotteryNumber());
		Spanned number;
		if (str.length == 2) {
			// 给控件赋值
			number = Html.fromHtml("<font color='#EB1827'>" + str[0]
					+ "</FONT>" + "<font color='#4060ff'>" + " " + str[1]
					+ "</FONT>");
		} else {
			number = Html.fromHtml("<font color='#EB1827'>" + str[0]
					+ "</FONT>");
		}
		holder.tv_item_fangshi.setText(LotteryUtils.transPlayName(
				content.getPlayType(), content.getLotteryNumber()));
		holder.tv_item_touzhu.setText(number);
		holder.tv_item_beishu.setText(beishu + "");
		return view;
	}

	private String[] transNum(String num) {
		return num.split("\\s?[+]\\s?");
	}

	static class ViewHolder {
		TextView tv_item_fangshi;
		TextView tv_item_touzhu;
		TextView tv_item_beishu;
	}

}
