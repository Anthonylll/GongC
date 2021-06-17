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
import com.gcapp.tc.sd.ui.Bet_11x5_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：11选5的投注页面的listview的适配器
 * 
 * @author echo
 */
public class MyBetLotteryList11X5Adapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private Bet_11x5_Activity betActivity;

	/** 构造方法 实现初始化 */
	public MyBetLotteryList11X5Adapter(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		betActivity = (Bet_11x5_Activity) context;
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
		int i = num.getPlayType() - Integer.parseInt(num.getLotteryId()) * 100;
		if (i == 1)
			holder.tv_type_count_money.setText("普通--前一  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 2)
			holder.tv_type_count_money.setText("普通--任选二  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 3)
			holder.tv_type_count_money.setText("普通--任选三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 4)
			holder.tv_type_count_money.setText("普通--任选四  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 5)
			holder.tv_type_count_money.setText("普通--任选五  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 6)
			holder.tv_type_count_money.setText("普通--任选六  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 7)
			holder.tv_type_count_money.setText("普通--任选七  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 8)
			holder.tv_type_count_money.setText("普通--任选八  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 9)
			holder.tv_type_count_money.setText("普通--直选二  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 10)
			holder.tv_type_count_money.setText("普通--直选三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 11)
			holder.tv_type_count_money.setText("普通--组选二  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 12)
			holder.tv_type_count_money.setText("普通--组选三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 13)
			holder.tv_type_count_money.setText("胆拖--组选二  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 14)
			holder.tv_type_count_money.setText("胆拖--组选三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 15)
			holder.tv_type_count_money.setText("胆拖--任选二  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 16)
			holder.tv_type_count_money.setText("胆拖--任选三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 17)
			holder.tv_type_count_money.setText("胆拖--任选四  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 18)
			holder.tv_type_count_money.setText("胆拖--任选五  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 19)
			holder.tv_type_count_money.setText("胆拖--任选六  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 20)
			holder.tv_type_count_money.setText("胆拖--任选七  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 21)
			holder.tv_type_count_money.setText("胆拖--任选八  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 22)
			holder.tv_type_count_money.setText("普通--乐选二  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 23)
			holder.tv_type_count_money.setText("普通--乐选三  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 24)
			holder.tv_type_count_money.setText("普通--乐选四  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (i == 25)
			holder.tv_type_count_money.setText("普通--乐选五  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
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
