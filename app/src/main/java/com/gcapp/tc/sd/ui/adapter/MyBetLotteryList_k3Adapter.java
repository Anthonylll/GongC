package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.sd.ui.Bet_k3_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：快三的投注页面的listview的适配器
 * 
 * @author echo
 */
public class MyBetLotteryList_k3Adapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private Bet_k3_Activity betActivity;

	/** 构造方法 实现初始化 */
	public MyBetLotteryList_k3Adapter(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		betActivity = (Bet_k3_Activity) context;
	}

	@Override
	public int getCount() {
		return listSchemes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listSchemes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/** 获得视图 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		final int index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.item_bet_lv, null);
			// 得到控件
			holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			holder.tv_show_number = (TextView) view
					.findViewById(R.id.tv_show_number);

			holder.tv_type_count_money = (TextView) view
					.findViewById(R.id.tv_type_count_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		SelectedNumbers num = listSchemes.get(position);
		// 给控件赋值
		Spanned number = Html.fromHtml("<font color='#BE0205'>"
				+ num.getShowLotteryNumber() + "</FONT>");
		holder.tv_show_number.setText(number);
		switch (num.getPlayType()) {
		case 8301:
			holder.tv_type_count_money.setText("和值  " + num.getCount() + "注  "
					+ listSchemes.get(position).getMoney() + "元");
			break;
		case 8302:
			holder.tv_type_count_money.setText("三同号通选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		case 8303:
			holder.tv_type_count_money.setText("三同号单选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		case 8304:
			holder.tv_type_count_money.setText("二同号复选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		case 8305:
			holder.tv_type_count_money.setText("二同号单选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		case 8306:
			holder.tv_type_count_money.setText("三不同号  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		case 8307:
			holder.tv_type_count_money.setText("二不同号  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		case 8308:
			holder.tv_type_count_money.setText("三连号通选  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		case 8309:
			holder.tv_type_count_money.setText("三不同号-胆拖" + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		case 8310:
			holder.tv_type_count_money.setText("二不同号-胆拖  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
			break;
		default:
			break;
		}

		holder.iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppTools.list_numbers.remove(index);
				betActivity.adapter.notifyDataSetChanged();
				betActivity.changeTextShow();
			}
		});

		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		ImageView iv_delete;
		TextView tv_show_number;
		TextView tv_type_count_money;
	}
}
