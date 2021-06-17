package com.gcapp.tc.sd.ui.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.Lottery;
import com.gcapp.tc.sd.ui.MainActivity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.CustomDigitalClock;
import com.gcapp.tc.R;

/**
 * 功能：购彩大厅的子项adapter
 * 
 * @author lenovo
 */
public class GridView_HallLotteryAdapter extends BaseAdapter {
	private int holdPosition;
	private boolean isChanged = false;
	private boolean ShowItem = false;
	private final static String TAG = "GridView_HallLotteryAdapter";
	// 上下文本
	private Context context;
	// 装彩票的集合
	private List<Lottery> listLotterys;
	private MainActivity activity;
//	private int mHidePosition = -1;
	private String lotteryIds_reg;
	private boolean falg_ssc, falg_11x5;

	public GridView_HallLotteryAdapter(Context context,
			List<Lottery> listLotterys) {
		this.context = context;
		activity = (MainActivity) context;
		this.listLotterys = listLotterys;
	}

	@Override
	public int getCount() {
		if (0 == listLotterys.size() % 2) {
			return listLotterys.size();
		} else {
			return listLotterys.size() + 1;
		}
	}

	public void updateUI(String mark, boolean flags1, boolean flags2) {
		this.lotteryIds_reg = mark;
		this.falg_ssc = flags1;
		this.falg_11x5 = flags2;
	}

	@Override
	public Lottery getItem(int position) {
		return listLotterys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void showDropItem(boolean showItem) {
		this.ShowItem = showItem;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.gridview_item_hall, null);
			// 得到控件
			holder.gv_rl_hall = (RelativeLayout) view
					.findViewById(R.id.gv_rl_hall);
			holder.hall_iv_reg = (ImageView) view
					.findViewById(R.id.hall_iv_reg);// 是否显示三角形
			holder.iv_lottery = (ImageView) view.findViewById(R.id.iv_lottery);
			holder.iv_lottery_kj = (TextView) view
					.findViewById(R.id.iv_lottery_kj);
			holder.tv_hall_lotteryname = (TextView) view
					.findViewById(R.id.tv_hall_lotteryname);
			holder.tv_hall_describ = (TextView) view
					.findViewById(R.id.tv_hall_describ);
			holder.cdc_hall_time = (CustomDigitalClock) view
					.findViewById(R.id.cdc_hall_time);
//			holder.tv_hall_dm = (TextView) view.findViewById(R.id.tv_hall_dm);
			holder.iv_hall_jiajiang = (ImageView) view
					.findViewById(R.id.iv_winornot);
			holder.tvStopSale = (TextView) view.findViewById(R.id.tv_stopSale);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.iv_lottery_kj.setVisibility(View.GONE);
		holder.iv_hall_jiajiang.setVisibility(View.GONE);
		holder.iv_lottery.setVisibility(View.VISIBLE);
		holder.tvStopSale.setVisibility(View.GONE);
		final Lottery lottery = listLotterys.get(position != listLotterys
				.size() ? position : 0);

		if (0 == listLotterys.size() % 2) { // 判断当彩种为双数
			setParams(lottery, holder,position);
		} else {
			if (listLotterys.size() != position) { // 为单数时最后一个设置要区分
				setParams(lottery, holder,position);
			} else {
				holder.cdc_hall_time.setVisibility(View.GONE);
				holder.tv_hall_lotteryname.setText("");
				holder.iv_lottery
						.setBackgroundResource(R.drawable.selector_hall_gvitem);
				// 玩法数量为单数时的gridview
//				holder.tv_hall_dm.setVisibility(View.GONE);
				holder.iv_lottery.setVisibility(View.GONE);
			}
		}

		if (isChanged) {
			if (position == holdPosition) {
				if (!ShowItem) {
					view.setVisibility(View.INVISIBLE);
				}
			}
		}
		return view;
	}

	private void setParams(final Lottery lottery, final ViewHolder holder,int position) {
		if (null != lottery && "62".equals(lottery.getLotteryID())
		// || (null != lottery && "78".equals(lottery.getLotteryID()))
				|| (null != lottery && "70".equals(lottery.getLotteryID()))
				// || (null != lottery && "28".equals(lottery.getLotteryID()))
				|| (null != lottery && "66".equals(lottery.getLotteryID()))) {
			holder.gv_rl_hall
					.setBackgroundResource(R.drawable.selector_hall_gvitem_pink);
		} else {
			holder.gv_rl_hall
					.setBackgroundResource(R.drawable.selector_hall_gvitem);
		}
		if ((null != lotteryIds_reg && !"".equals(lotteryIds_reg)
				&& ("111").equals(lottery.getLotteryID()) && !falg_11x5)
				|| (null != lotteryIds_reg && !"".equals(lotteryIds_reg)
						&& ("222").equals(lottery.getLotteryID()) && !falg_ssc)
		) {
			holder.hall_iv_reg.setVisibility(View.VISIBLE);
		} else {
			holder.hall_iv_reg.setVisibility(View.GONE);
		}
		// 给控件赋值
		holder.cdc_hall_time.setVisibility(View.GONE);
		holder.tv_hall_lotteryname.setText(lottery.getLotteryName());
		if (null != lottery && null != lottery.getLotteryID()
				&& !"".equals(lottery.getLotteryID())) {

			if (lottery.getLotteryName().contains("单关")) {
				if ("72".equals(lottery.getLotteryID())) {
					holder.iv_lottery
							.setImageResource(R.drawable.log_lottery_jczq_dan);
				} else {
					if ("73".equals(lottery.getLotteryID())) {
						holder.iv_lottery
								.setImageResource(R.drawable.log_lottery_jclq_dan);
					}
				}
			} else {
				holder.iv_lottery.setImageResource(AppTools.allLotteryLogo
						.get(lottery.getLotteryID()));
			}
			// 是否显示今日开奖
			if (null != AppTools.winToday && !"".equals(AppTools.winToday)) {
				String[] str = AppTools.winToday.split(",");
				for (int i = 0; i < str.length; i++) {
					if (null != str[i] && !"".equals(str[i])) {
						if (lottery.getLotteryID().equals((str[i]))) {
							holder.iv_lottery_kj.setVisibility(View.VISIBLE);
						}
					}
				}
			}

			if (lottery.getEndtime() == null)
				holder.cdc_hall_time.setVisibility(View.GONE);

			if (null != lottery.getLotteryDescription()
					&& !"".equals(lottery.getLotteryDescription())) {
				holder.tv_hall_describ.setVisibility(View.VISIBLE);
				holder.tv_hall_describ.setText(lottery.getLotteryDescription());
			} else {
				holder.tv_hall_describ.setVisibility(View.GONE); // 彩种描述隐藏掉
			}

			// 判断是否有期号 或 是足球是由有对阵
			if ("72".equals(lottery.getLotteryID())
					|| "73".equals(lottery.getLotteryID())
					|| "45".equals(lottery.getLotteryID())) {
				if ("true".equals(lottery.getIsSale())) {
					holder.cdc_hall_time.setVisibility(View.GONE);
					holder.tvStopSale.setVisibility(View.GONE); // 让显示停止销售的布局消失

//					holder.tv_hall_dm.setVisibility(View.VISIBLE);
//					holder.tv_hall_dm.setText(lottery.getLotteryAgainst());
				} else if ("false".equals(lottery.getIsSale())) {
					holder.cdc_hall_time.setVisibility(View.GONE);
					holder.tvStopSale.setVisibility(View.VISIBLE);
//					holder.tv_hall_dm.setVisibility(View.GONE);
				}

			} else {
				if ("true".equals(lottery.getIsSale())) {
					if ("0".equals(lottery.getIsuseId())) {
						holder.cdc_hall_time.setVisibility(View.GONE);
						holder.tv_hall_describ.setVisibility(View.VISIBLE);
					} else {
						holder.tv_hall_describ.setVisibility(View.GONE);
						holder.cdc_hall_time.setVisibility(View.VISIBLE);
					}
					holder.tvStopSale.setVisibility(View.GONE); // 让显示停止销售的布局消失

				} else if ("false".equals(lottery.getIsSale())) {
					holder.cdc_hall_time.setVisibility(View.GONE);
					holder.tvStopSale.setVisibility(View.VISIBLE);
				}
				if (lottery.getEndtime() != null
						&& lottery.getDistanceTime()
								- System.currentTimeMillis() > 0) {
					holder.cdc_hall_time.setMTickStop(false);
					holder.cdc_hall_time.setType(1);
					holder.cdc_hall_time.setEndTime(lottery.getDistanceTime());
				} else {
					if (lottery.getDistanceTime2() - System.currentTimeMillis() > 0) {
						holder.cdc_hall_time.setMTickStop(false);
						holder.cdc_hall_time.setType(2);
						holder.cdc_hall_time.setEndTime(lottery
								.getDistanceTime2());
					}
				}
//				holder.tv_hall_dm.setVisibility(View.GONE);
			}
			// 倒计时
			holder.cdc_hall_time
					.setClockListener(new CustomDigitalClock.ClockListener() {
						@Override
						public void timeEnd() {
							if (holder.cdc_hall_time.getType() == 1) {
								holder.cdc_hall_time.setMTickStop(false);
								holder.cdc_hall_time.setType(2);
								holder.cdc_hall_time.setEndTime(lottery
										.getDistanceTime2());
							} else {
								if ("72".equals(lottery.getLotteryID())
										|| "73".equals(lottery.getLotteryID())
										|| "45".equals(lottery.getLotteryID())) {
									return;
								}
								RequestUtil requestUtil = new RequestUtil(
										context, false, 0) {
									@Override
									public void responseCallback(
											JSONObject reponseJson) {
										String result = AppTools
												.getDate(reponseJson);
										if ("0".equals(result)) {
											activity.update();
										} else if ("-1001".equals(result)) {
											if (RequestUtil.DEBUG) {
												Log.e(TAG, "获取购彩大厅数据出错");
											}
										}
									}

									@Override
									public void responseError(VolleyError error) {
									}
								};
								requestUtil.getLotteryData(lottery
										.getLotteryID());
							}
						}

						@Override
						public void remainFiveMinutes() {
						}
					});
		} else {
			holder.gv_rl_hall
					.setBackgroundResource(R.drawable.selector_hall_gvitem_pink);
		}

		if(lottery.getAddawardsingle() != null&&(position ==1 || position ==3)){
			if(lottery.getAddawardsingle().equalsIgnoreCase("ture")) {
				holder.iv_hall_jiajiang.setVisibility(View.VISIBLE);
			}else{
				holder.iv_hall_jiajiang.setVisibility(View.GONE);
			}
		}else if (lottery.getAddaward() != null
				&& lottery.getAddaward().equalsIgnoreCase("true")) {
			holder.iv_hall_jiajiang.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 静态类
	 */
	static class ViewHolder {
		RelativeLayout gv_rl_hall;
		ImageView iv_lottery; // 彩种图片
		TextView tv_hall_lotteryname; // 彩种名
		TextView tv_hall_describ;// 彩种描述
		CustomDigitalClock cdc_hall_time; // 截止时间
//		TextView tv_hall_dm; // 最近比赛
		TextView iv_lottery_kj;
		ImageView iv_hall_jiajiang; // 加奖
		TextView tvStopSale;
		ImageView hall_iv_reg;
	}

}
