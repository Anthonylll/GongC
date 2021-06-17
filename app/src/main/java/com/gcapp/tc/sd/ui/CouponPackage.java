package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.CouponModel;
import com.gcapp.tc.sd.ui.adapter.CouponAdapter;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author anthony
 * @date 2018-7-20 上午11:25:59
 * @version 1.1.0 
 * @Description 我的优惠券
 */
public class CouponPackage {
	
	private View view;
	private Context mContext;
	private String mflag;
	private ListView coupon_list;
	private TextView nodata_text;
	private CouponAdapter couponAdapter;
	private List<CouponModel> couponList;
	private CouponModel couponModel;
	private String couponMoney ;
	private Activity mActivity;

	public CouponPackage(Context context,Activity activity, View v,String flag,String money) {
		this.view = v;
		this.mContext = context;
		this.mflag = flag;
		this.couponMoney = money;
		this.mActivity = activity;
	}
	
	/**
	 * 初始化控件和监听事件
	 */
	public void init() {
		initView();
		initData();
	}

	private void initView() {
		coupon_list = (ListView)view.findViewById(R.id.coupon_list);
		nodata_text = (TextView)view.findViewById(R.id.nodata_text);
		coupon_list.setOnItemClickListener(new MyItemsClickListener());
	}

	private void initData() {
		couponList = new ArrayList<CouponModel>();
		getCouponData();
	}
	
	/**
	 * 获取用户优惠券
	 */
	private void getCouponData() {
		RequestUtil requestUtil = new RequestUtil(mContext, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject item) {
				try {
					if (item.getString("error").equals("0")) {
						JSONArray jsonArray = item.getJSONArray("info");
						for (int i = 0; i < jsonArray.length(); i++) {
							couponModel = new CouponModel();
							JSONObject jsonObtect = jsonArray.getJSONObject(i);
							String expired = jsonObtect.getString("isContent");
							String unused = jsonObtect.getString("IsUse");
							if(mflag.equals("1")&& expired.equals("False")){
								dealJson(couponModel,jsonObtect);
							}else if(mflag.equals("0")&& expired.equals("True")&& unused.equals("False")) {
								dealJson(couponModel,jsonObtect);
							}
						}
					}
					initAdapter();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(mContext, "优惠券数据异常");
			}
		};
		requestUtil.getCouponDatas(couponMoney);
	}
	
	/**
	 * 处理数据
	 */
	private void dealJson(CouponModel couponModel,JSONObject jsonObtect) {
		try {
			couponModel.setLimitMoney((int)Float.parseFloat(jsonObtect.getString("Money"))+"");
			couponModel.setCouponTime(jsonObtect.getString("StartDate")+"-"+jsonObtect.getString("EndDate"));
			couponModel.setFullMoney((int)Float.parseFloat(jsonObtect.getString("MinMoney"))+"");
			couponModel.setIsExpired(jsonObtect.getString("isContent"));
			couponModel.setIsUsed(jsonObtect.getString("IsUse"));
			couponModel.setCouponId(jsonObtect.getString("VoucherID"));
			couponList.add(couponModel);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化adapter
	 */
	private void initAdapter() {
		couponAdapter = new CouponAdapter(mContext, couponList);
		coupon_list.setAdapter(couponAdapter);
		if(couponList.size() == 0) {
			nodata_text.setVisibility(View.VISIBLE);
			if(mflag.equals("1")) {
				nodata_text.setText("无过期优惠券");
			}
		}else{
			nodata_text.setVisibility(View.GONE);
		}
	}

	/**
	 * 子项点击事件
	 */
	private class MyItemsClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			String money = couponList.get(position).getLimitMoney();
			String couponId = couponList.get(position).getCouponId();
			String time = couponList.get(position).getIsExpired();
			if(time.equals("False")){
				MyToast.getToast(mContext, "优惠券已过期!");
			}else if(couponMoney.equals("-1") || couponMoney.equals("0")) {
				
			}else{
				Intent intent = new Intent();
	            intent.putExtra("money", money);
	            intent.putExtra("voucherID", couponId);
	            mActivity.setResult(1, intent);
	            mActivity.finish();
			}
		}
	}
	
}
