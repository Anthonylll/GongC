package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.sd.ui.adapter.CenterLotteryAdapter.ViewHolder2;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：个人中心的追号投注记录的详情adapter
 * 
 * @author lenovo
 * 
 */
public class Follow_lottery_detail_adapter extends BaseAdapter {
	private List<Schemes> myList;
	private int id = -1;
	private Context mContext;
	private int type = 2; // 1显示彩种名字 2显示期号

	public Follow_lottery_detail_adapter(Context context, List<Schemes> aList,
			int type) {
		this.mContext = context;
		setData(aList);
	}

	public void setData(List<Schemes> aList) {
		if (myList == null)
			myList = new ArrayList<Schemes>();
		myList.clear();
		for (Schemes schemes : aList) {
			myList.add(schemes);
		}
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public boolean isEnabled(int position) {
		Schemes scheme = myList.get(position);
		String status = scheme.getSchemeStatus();
		return !"未出票".equals(status);
	}

	@Override
	public Object getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder2 holder;
		if (null == convertView) {
			LayoutInflater inflact = LayoutInflater.from(mContext);
			convertView = inflact.inflate(
					R.layout.follow_lottery_detail_adapter_item, null);
			holder = new ViewHolder2();
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_lotteryName);
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_playType);
			holder.tv_win = (TextView) convertView.findViewById(R.id.tv_isWin);
			holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.img_logo = (TextView) convertView.findViewById(R.id.img_win);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder2) convertView.getTag();
		}

		holder.tv_id.setText(id + "");
		holder.tv_id.setVisibility(View.GONE);
		holder.img_logo.setVisibility(View.GONE);
		holder.tv_win.setTextColor(Color.BLACK);
		Schemes scheme = myList.get(position);

		if (type == 2)
			holder.tv_name.setText("第" + scheme.getIsuseName() + "期");
		else {
			holder.tv_name.setText(scheme.getLotteryName());
		}
		holder.tv_money.setText(scheme.getMoney() + "元");

		if ("False".equals(scheme.getIsPurchasing())) {
			holder.tv_type.setText("合买订单");
		} else if ("True".equals(scheme.getIsPurchasing())) {
			if (scheme.getIsChase() == 0)
				holder.tv_type.setText("普通订单");
			else
				holder.tv_type.setText("追号订单");
		}

		String status = scheme.getSchemeStatus();
		if (status.equals("已中奖")) {
			holder.tv_win.setTextColor(Color.RED);
			holder.img_logo.setVisibility(View.VISIBLE);
			holder.tv_win.setText("中奖" + scheme.getWinMoneyNoWithTax() + "元");
		} else {
			holder.tv_win.setText(status);
		}

		return convertView;
	}

}
