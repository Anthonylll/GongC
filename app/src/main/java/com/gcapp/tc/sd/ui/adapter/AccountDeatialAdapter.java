package com.gcapp.tc.sd.ui.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.AccountDetails;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.R;

/**
 * 功能：账户明细的适配器
 * 
 * @author lenovo
 * 
 */
public class AccountDeatialAdapter extends BaseAdapter {
	private Context context;
	private List<AccountDetails> list;
	private ViewHolder viewHolder;
	private int mPosition;
	private int childPosition;
	private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	private int typemark;
	private OnItemClickListener itemClickListener;

	public AccountDeatialAdapter(Context context,
			List<AccountDetails> detailsList, int mark) {
		super();
		this.context = context;
		list = detailsList;
		typemark = mark;
	}

	public AccountDeatialAdapter(Context context,
			List<AccountDetails> detailsList, OnItemClickListener listener) {
		super();
		this.context = context;
		list = detailsList;
		this.itemClickListener = listener;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {

		if (converView == null) {
			converView = LayoutInflater.from(context).inflate(
					R.layout.fundsfragment_item, parent, false);
			mPosition = position;
			viewHolder = new ViewHolder();
			viewHolder.lvContent = (MyListView2) converView
					.findViewById(R.id.funfrag_lv2);
			converView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) converView.getTag();
		}
		// 定义final常量，防止布局在重用时导致position错位
		// final修饰变量时对象引用不变
		// final AccountDetails ad = list.get(position); // getview
		// 控件赋值
		MyListViewAdapter listViewAdapter = new MyListViewAdapter(list);
		listViewAdapter.notifyDataSetChanged();
		viewHolder.lvContent.setAdapter(listViewAdapter);

		return converView;
	}

	class ViewHolder {
		public MyListView2 lvContent;
	}

	class MyListViewAdapter extends BaseAdapter {
		private List<AccountDetails> schemesList;
		private ViewHolder2 viewHolder2;

		public MyListViewAdapter(List<AccountDetails> schemesList) {
			super();
			this.schemesList = schemesList;
		}

		@Override
		public int getCount() {
			return schemesList.size();
		}

		@Override
		public Object getItem(int position) {
			return schemesList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View converView, ViewGroup parent) {

			if (converView == null) {
				converView = LayoutInflater.from(context).inflate(
						R.layout.funsfragment_item_item2, parent, false);
				viewHolder2 = new ViewHolder2();
				childPosition = position;
				viewHolder2.tv_date = (TextView) converView
						.findViewById(R.id.tv_account_date);
				viewHolder2.tv_time = (TextView) converView
						.findViewById(R.id.tv_account_time);
				viewHolder2.tv_img = (TextView) converView
						.findViewById(R.id.tv_account_img);
				viewHolder2.tv_money = (TextView) converView
						.findViewById(R.id.tv_account_money);
				viewHolder2.tv_betType = (TextView) converView
						.findViewById(R.id.tv_account_type);
				viewHolder2.tv_status = (TextView) converView
						.findViewById(R.id.tv_account_state);
				converView.setTag(viewHolder2);
			} else {
				viewHolder2 = (ViewHolder2) converView.getTag();
			}
			AccountDetails sch = schemesList.get(position);
			String date = sch.getDate();
			String result_date = "";
			if (null != date && !"".equals(date)) {
				result_date = date.split("-")[1] + "-" + date.split("-")[2];
			}

			String time = sch.getTime();
			String result_time = "";
			if (null != time && !"".equals(time)) {
				result_time = time.split(":")[0] + ": " + time.split(":")[1]
						+ ": " + time.split(":")[2];
			}
			viewHolder2.tv_date.setText(result_date);
			viewHolder2.tv_time.setText(result_time);
			viewHolder2.tv_betType.setText("类型：" + sch.getBetType());
			if (typemark == 4 || typemark == 1) {
				viewHolder2.tv_status.setText("状态：" + sch.getStatus());
			} else {
				viewHolder2.tv_status.setText("");
			}

			if (typemark == -1) {
				if (sch.getImg_type() < 100) {
					viewHolder2.tv_money.setText("+" + sch.getMoney());
					viewHolder2.tv_img
							.setBackgroundResource(R.drawable.account_drawmoney);
				} else {
					viewHolder2.tv_money.setText("-" + sch.getMoney());
					viewHolder2.tv_img
							.setBackgroundResource(R.drawable.account_drawmoney2);
				}
			} else if (typemark == 1) {
				viewHolder2.tv_money.setText("-" + sch.getMoney());
				viewHolder2.tv_img
						.setBackgroundResource(R.drawable.account_bet);
			} else if (typemark == 2) {
				viewHolder2.tv_money.setText("+" + sch.getMoney());
				viewHolder2.tv_img
						.setBackgroundResource(R.drawable.account_alipay);
			} else if (typemark == 3) {
				viewHolder2.tv_money.setText("+" + sch.getMoney());
				viewHolder2.tv_img
						.setBackgroundResource(R.drawable.account_win);
			} else {
				viewHolder2.tv_money.setText("-" + sch.getMoney());
				viewHolder2.tv_img
						.setBackgroundResource(R.drawable.account_drawmoney2);
			}
			return converView;
		}

		class ViewHolder2 {
			public TextView tv_date;
			public TextView tv_time;
			public TextView tv_img;
			public TextView tv_money;
			public TextView tv_betType;
			public TextView tv_status;
		}
	}
}
