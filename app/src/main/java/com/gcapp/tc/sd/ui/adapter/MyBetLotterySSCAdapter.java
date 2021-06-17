package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

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
import com.gcapp.tc.sd.ui.Bet_SSC_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.R;

/**
 * 功能：时时彩的投注页面的listview的适配器
 * 
 * @author echo
 */
public class MyBetLotterySSCAdapter extends BaseAdapter {
	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private Bet_SSC_Activity betActivity;
	private String lotteryId;

	/** 构造方法 实现初始化 */
	public MyBetLotterySSCAdapter(Context context,
			List<SelectedNumbers> listSchemes, String LotteryId) {
		this.context = context;
		this.lotteryId = LotteryId;
		this.listSchemes = listSchemes;
			betActivity = (Bet_SSC_Activity) context;
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
		Spanned number = Html.fromHtml("<font color='#BE0205'>"
				+ num.getShowLotteryNumber() + "</FONT>");
		holder.tv_show_number.setText(number);

		if (lotteryId.equals("28")) {
			if (num.getType() == 1)
				holder.tv_type_count_money.setText("一星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 2)
				holder.tv_type_count_money.setText("二星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 3)
				holder.tv_type_count_money.setText("二星组选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 4)
				holder.tv_type_count_money.setText("三星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 5)
				holder.tv_type_count_money.setText("三星组三  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 6)
				holder.tv_type_count_money.setText("三星组六  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 7)
				holder.tv_type_count_money.setText("五星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 8)
				holder.tv_type_count_money.setText("五星通选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 9)
				holder.tv_type_count_money.setText("大小单双  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 10)
				holder.tv_type_count_money.setText("组三包胆  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 11)
				holder.tv_type_count_money.setText("组三和值  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
		} else {// 新疆时时彩
			if (num.getType() == 1)
				holder.tv_type_count_money.setText("一星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");

			else if (num.getType() == 2)
				holder.tv_type_count_money.setText("二星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 3)
				holder.tv_type_count_money.setText("二星组选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");

			else if (num.getType() == 4)
				holder.tv_type_count_money.setText("二星任选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");

			else if (num.getType() == 5)
				holder.tv_type_count_money.setText("三星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 6)
				holder.tv_type_count_money.setText("三星组三  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 7)
				holder.tv_type_count_money.setText("三星组六  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 8)
				holder.tv_type_count_money.setText("三星任选 " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 9)
				holder.tv_type_count_money.setText("四星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 10)
				holder.tv_type_count_money.setText("五星直选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 11)
				holder.tv_type_count_money.setText("五星通选  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
			else if (num.getType() == 12)
				holder.tv_type_count_money.setText("大小单双  " + num.getCount()
						+ "注  " + listSchemes.get(position).getMoney() + "元");
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
