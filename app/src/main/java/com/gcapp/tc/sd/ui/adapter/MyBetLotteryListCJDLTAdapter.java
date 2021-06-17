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
import com.gcapp.tc.sd.ui.Bet_DLT_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.R;

import java.util.List;

/**
 * 功能：大乐透的投注页面的listview的适配器
 * 
 * @author echo
 */
public class MyBetLotteryListCJDLTAdapter extends BaseAdapter {

	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<SelectedNumbers> listSchemes;
	private Bet_DLT_Activity betActivity;

	/** 构造方法 实现初始化 */
	public MyBetLotteryListCJDLTAdapter(Context context,
			List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		betActivity = (Bet_DLT_Activity) context;
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

		String red = "";
		String blue = "";
		if (null == num.getRedTuoNum()) {
			for (String str : num.getRedNumbers()) {
				red += str + " ";
			}
		} else if (num.getRedTuoNum().size() != 0) {
			for (int i = 0; i < num.getRedNumbers().size(); i++) {
				if (i == 0)
					red = "(";
				red = red + " " + num.getRedNumbers().toArray()[i];
				if (i == num.getRedNumbers().size() - 1) {
					red += ")";
				}
			}
			for (String str : num.getRedTuoNum()) {
				red += str + " ";
			}
		}
		if (null == num.getBlueTuoNum()) {
			for (String str : num.getBlueNumbers()) {
				blue += str + " ";
			}
		} else if (num.getBlueTuoNum().size() != 0) {
			blue = "(" + num.getBlueNumbers().toArray()[0] + ")";
			for (String str : num.getBlueTuoNum()) {
				blue += str + " ";
			}
		}
		Spanned number = Html
				.fromHtml("<font color='#BE0205'>" + red + "</FONT>"
						+ "<font color='#4060ff'>" + " " + blue + "</FONT>");

		holder.tv_show_number.setText(number);

		if (3901 == num.getPlayType())
			holder.tv_type_count_money.setText("普通投注  " + num.getCount()
					+ "注  " + listSchemes.get(position).getMoney() + "元");
		else if (3903 == num.getPlayType())
			holder.tv_type_count_money.setText("前区胆拖" + num.getCount() + "注  "
					+ listSchemes.get(position).getMoney() + "元");
		else if (3906 == num.getPlayType())
			holder.tv_type_count_money.setText("后区胆拖" + num.getCount() + "注  "
					+ listSchemes.get(position).getMoney() + "元");
		else if (3907 == num.getPlayType())
			holder.tv_type_count_money.setText("双区胆拖" + num.getCount() + "注  "
					+ listSchemes.get(position).getMoney() + "元");
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
