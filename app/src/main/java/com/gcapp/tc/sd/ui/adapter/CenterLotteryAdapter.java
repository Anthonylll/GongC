package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 功能：个人中心的彩票投注记录adapter
 * 
 * @author lenovo
 * 
 */
public class CenterLotteryAdapter extends BaseAdapter {
	private Context mContext;
	private HashMap<String, List<String>> mMap;
	private List<String> dath;
	private List<List<Schemes>> mList;
	private OnItemClickListener listener;
	private int type = 1; // 1显示彩种名字 2显示期号

	public CenterLotteryAdapter(Context context, List<String> list,
			List<List<Schemes>> tlist, OnItemClickListener listener, int type) {
		this.mContext = context;
		this.listener = listener;
		this.type = type;
		setDate(list, tlist);
	}

	public CenterLotteryAdapter(Context context, List<String> list,
			List<List<Schemes>> tlist, OnItemClickListener listener) {
		this.mContext = context;
		this.listener = listener;
		setDate(list, tlist);
	}

	public void setDate(List<String> list, List<List<Schemes>> tlist) {
		if (null == dath)
			dath = new ArrayList<String>();
		if (null == mList)
			mList = new ArrayList<List<Schemes>>();
		dath.clear();
		mList.clear();
		for (String str : list) {
			dath.add(str);
		}
		for (List<Schemes> li : tlist) {
			List<Schemes> slist = new ArrayList<Schemes>();
			for (Schemes str : li) {
				slist.add(str);
			}
			mList.add(slist);
		}
	}

	/** 给集合赋值 */
	public void setHashMap(HashMap<String, List<String>> set) {
		for (Entry<String, List<String>> entry : set.entrySet()) {
			List<String> l = new ArrayList<String>();
			for (String s : entry.getValue()) {
				l.add(s);
			}
			this.mMap.put(entry.getKey(), l);
		}
	}

	@Override
	public int getCount() {
		return dath.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dath.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (null == view) {
			LayoutInflater inflact = LayoutInflater.from(mContext);
			view = inflact.inflate(R.layout.center_lottery_item, null);
			holder = new ViewHolder();
			holder.tv_month = (TextView) view.findViewById(R.id.tv_month);
			holder.tv_day = (TextView) view.findViewById(R.id.tv_day);
			holder.listView = (MyListView2) view
					.findViewById(R.id.item_listView);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
//		if(mList.size() != 0) {
			holder.listView.setAdapter(new MyListViewAdapter(mList.get(position),
					position));
			holder.listView.setOnItemClickListener(listener);
			holder.tv_month.setText(dath.get(position).split("-")[1] + "月");
			holder.tv_day.setText(dath.get(position).split("-")[2] + "日");
//		}
		return view;
	}

	static class ViewHolder {
		TextView tv_month, tv_day;
		MyListView2 listView;
	}

	class MyListViewAdapter extends BaseAdapter {
		private List<Schemes> aList;
		private int id;

		public MyListViewAdapter(List<Schemes> aList, int id) {
			this.aList = aList;
			this.id = id;
		}

		@Override
		public int getCount() {
			return aList.size();
		}

		@Override
		public Object getItem(int position) {
			return aList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder2 holder;
			if (null == convertView) {
				LayoutInflater inflact = LayoutInflater.from(mContext);
				convertView = inflact.inflate(
						R.layout.center_lottery_item_item, null);
				holder = new ViewHolder2();
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_lotteryName);
				holder.iconOptimize = convertView.findViewById(R.id.optimize);
				holder.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				holder.tv_type = (TextView) convertView
						.findViewById(R.id.tv_playType);
				holder.tv_win = (TextView) convertView
						.findViewById(R.id.tv_isWin);
				holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
				holder.img_logo = (TextView) convertView
						.findViewById(R.id.img_win);
				holder.tvCaiJin = (TextView) convertView
						.findViewById(R.id.tv_CaiJinCost);
				holder.tvJinE = (TextView) convertView
						.findViewById(R.id.tv_JinECost);
				holder.img_right_arrow = (ImageView) convertView
						.findViewById(R.id.img_right_arrow);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder2) convertView.getTag();
			}

			holder.tv_id.setText(id + "");
			holder.tv_id.setVisibility(View.GONE);
			holder.img_logo.setVisibility(View.GONE);

			Schemes scheme = aList.get(position);
			if (type == 2)
				holder.tv_name.setText("第" + scheme.getIsuseName() + "期");
			else {
				holder.tv_name.setText(LotteryUtils.getTitleText(scheme
						.getLotteryID()));
			}
			holder.iconOptimize
					.setVisibility(scheme.getIsPreBet() ? View.VISIBLE
							: View.GONE);
			holder.tvCaiJin.setText("彩金消费" + scheme.getHandselMoney() + "元");
			if(scheme.getCouponMoney() != 0) {
				holder.tvJinE.setText("金额消费" + scheme.getDetailMoney() + "元 | 优惠"+scheme.getCouponMoney()+"元");
			}else{
				holder.tvJinE.setText("金额消费" + scheme.getDetailMoney() + "元");
			}

			holder.tv_money.setText(scheme.getMoney() + "元");
			holder.tv_win.setTextColor(Color.BLACK);

			String status = scheme.getSchemeStatus();
			if (status.equals("已中奖") || scheme.getWinMoneyNoWithTax() > 0) {
				holder.tv_win.setTextColor(Color.RED);
				holder.img_logo.setVisibility(View.VISIBLE);
				holder.img_right_arrow.setVisibility(View.GONE);
				if(scheme.getIsPurchasing().equals("True") && scheme.getIsChase() != 0) {
					holder.tv_win.setText("中奖" + scheme.getWinMoneyNoWithTax()+ "元");
				}else{
					//追号中奖显示
					holder.tv_win.setText("中奖" + scheme.getShareWinMoney()+ "元");
				}
			} else {
				holder.tv_win.setText(status);
				holder.img_right_arrow.setVisibility(View.VISIBLE);
			}

			/** 订单类型 */
			if ("False".equals(scheme.getIsPurchasing())) {
				if(scheme.getInitiateName().equals(AppTools.user.getName())) {
					holder.tv_type.setText("发起订单");
					holder.tv_type.setTextColor(0xfff3b414);
				}else{
					holder.tv_type.setText("跟买订单");
					holder.tv_type.setTextColor(0xff666666);
				}
			} else if ("True".equals(scheme.getIsPurchasing())) {
				/** 普通订单状态 */
				if (scheme.getIsChase() == 0) {
					holder.tv_type.setText("普通订单");
					holder.tv_type.setTextColor(0xff666666);
				} else {
					/** 追号订单状态 */
					holder.tv_type.setText("追号订单");
					holder.tv_type.setTextColor(0xff666666);
				}
			}
			return convertView;
		}
	}

	static class ViewHolder2 {
		TextView tv_name, tv_money, tv_type, tv_win, tv_id, tvJinE, tvCaiJin;
		TextView img_logo;
		View iconOptimize;
		ImageView img_right_arrow;
	}

}
