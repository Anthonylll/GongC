package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import com.gcapp.tc.dataaccess.CouponModel;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-7-20 下午1:51:19
 * @version 1.1.0 
 * @Description 优惠券adapter
 */
public class CouponAdapter  extends BaseAdapter{

	private Context context;
	// 装图片的集合
	private List<CouponModel> couponlist;

	public CouponAdapter(Context context, List<CouponModel> list) {
		this.context = context;
		this.couponlist = list;
	}

	@Override
	public int getCount() {
		return couponlist.size();
	}

	@Override
	public Object getItem(int position) {
		return couponlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.coupon_list_item, null);
			holder.left_layout = (RelativeLayout) view.findViewById(R.id.left_layout);
			holder.coupon_money_limit = (TextView) view.findViewById(R.id.coupon_money_limit);
			holder.coupon_money_full = (TextView) view.findViewById(R.id.coupon_money_full);
			holder.coupon_limit_time = (TextView) view.findViewById(R.id.coupon_limit_time);
			holder.coupon_limit_full = (TextView) view.findViewById(R.id.coupon_limit_full);
			holder.line = (View) view.findViewById(R.id.line);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		CouponModel coupon = couponlist.get(position);
		if (coupon != null) {
			holder.coupon_money_limit.setText(coupon.getLimitMoney()+"¥");
			holder.coupon_money_full.setText("满"+coupon.getFullMoney()+"元可用");
			holder.coupon_limit_time.setText(coupon.getCouponTime());
			holder.coupon_limit_full.setText("满"+coupon.getFullMoney()+"减"+coupon.getLimitMoney());
		}
		if(coupon.getIsUsed().equals("True") || coupon.getIsExpired().equals("False")) {
			holder.left_layout.setBackgroundResource(R.drawable.coupon_item_background2);
		}
		if(couponlist.size() == (position+1)) {
			holder.line.setVisibility(View.VISIBLE);
		}else{
			holder.line.setVisibility(View.GONE);
		}
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		RelativeLayout left_layout;
		TextView coupon_money_limit;
		TextView coupon_money_full;
		TextView coupon_limit_time;
		TextView coupon_limit_full;
		View line;
	}
	
}
