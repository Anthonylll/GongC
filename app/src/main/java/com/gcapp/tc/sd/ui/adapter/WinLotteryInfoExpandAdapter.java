package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.WinLottery;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 功能：开奖详情的ExpandableListAdapter
 * 
 * @author lenovo
 * 
 */
public class WinLotteryInfoExpandAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<WinLottery> list;
	private int childrenCount = 1;

	public WinLotteryInfoExpandAdapter(Context context, List<WinLottery> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view,
			ViewGroup parent) {
		if (list.isEmpty()) {
			return null;
		}
		ViewParentHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewParentHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.win_lotteryinfo_items, null);
			// 得到控件
			holder.linearLayout = (LinearLayout) view
					.findViewById(R.id.ll_ll_bottom);
			holder.lottery_ll_firsShow = (LinearLayout) view
					.findViewById(R.id.lottery_ll_firsShow);
			holder.lottery_ll_firsShow2 = (LinearLayout) view
					.findViewById(R.id.lottery_ll_firsShow2);

			holder.line_k3 = (LinearLayout) view
					.findViewById(R.id.lottery_ll_k3);// 江苏快三的整个布局
			holder.ll_k3common = (LinearLayout) view
					.findViewById(R.id.ll_k3common);// 江苏快三的普通布局
			holder.ll_k3top = (LinearLayout) view.findViewById(R.id.ll_k3top);// 江苏快三的最后一个布局

			holder.k3_number = (TextView) view // 江苏快三的普通布局的开奖号码
					.findViewById(R.id.k3_number);

			holder.text_hezhi = (TextView) view.findViewById(R.id.text_hezhi);
			holder.text_hezhishow = (TextView) view
					.findViewById(R.id.text_hezhishow);

			holder.img1 = (ImageView) view.findViewById(R.id.k3_img1);
			holder.img2 = (ImageView) view.findViewById(R.id.k3_img2);
			holder.img3 = (ImageView) view.findViewById(R.id.k3_img3);

			holder.text_qi = (TextView) view.findViewById(R.id.top_tv_qi);
			holder.text_time = (TextView) view.findViewById(R.id.top_tv_time);
			holder.text_redNum = (TextView) view
					.findViewById(R.id.bottom_tv_redNum);
			holder.text_blueNum = (TextView) view
					.findViewById(R.id.bottom_tv_blueNum);
			holder.win_lottery_expand_arrow = (ImageView) view
					.findViewById(R.id.win_lottery_expand_arrow);
			view.setTag(holder);
		} else {
			holder = (ViewParentHolder) view.getTag();
		}

		WinLottery wLottery = list.get(groupPosition);
		String lotteryId = wLottery.getLotteryId();
		if (lotteryId.equals("83")) {
			holder.linearLayout.setVisibility(View.GONE);
			holder.line_k3.setVisibility(View.VISIBLE);
			if (groupPosition == 0) {
				holder.ll_k3common.setVisibility(View.GONE);
				holder.ll_k3top.setVisibility(View.VISIBLE);
			} else {
				holder.ll_k3common.setVisibility(View.VISIBLE);
				holder.ll_k3top.setVisibility(View.GONE);
			}
		} else {
			holder.linearLayout.setVisibility(View.VISIBLE);
			holder.line_k3.setVisibility(View.GONE);
		}
		holder.win_lottery_expand_arrow
				.setBackgroundResource(isExpanded ? R.drawable.win_lottery_detail_up
						: R.drawable.win_lottery_detail_down);
		holder.text_redNum.setText("");
		holder.text_blueNum.setText("");

		if (groupPosition == 0) {// 第一排字体调大
			holder.text_qi.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
					.getResources().getDimensionPixelSize(R.dimen.max_tx_size));
			holder.text_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
					.getResources().getDimensionPixelSize(R.dimen.m_tx_size));
			holder.text_hezhishow.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					context.getResources().getDimensionPixelSize(
							R.dimen.m_tx_size));
		} else {
			holder.text_qi.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
					.getResources().getDimensionPixelSize(R.dimen.m_tx_size));
			holder.text_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
					.getResources().getDimensionPixelSize(R.dimen.tx_size));
			holder.text_hezhishow.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					context.getResources().getDimensionPixelSize(
							R.dimen.tx_size));
		}
		holder.text_redNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
				.getResources().getDimensionPixelSize(R.dimen.m_tx_size));
		holder.text_blueNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, context
				.getResources().getDimensionPixelSize(R.dimen.m_tx_size));

		if ("28".equals(lotteryId) || "70".equals(lotteryId)
				|| "62".equals(lotteryId) || "66".equals(lotteryId)
				|| "78".equals(lotteryId) || "83".equals(lotteryId)) {
			holder.win_lottery_expand_arrow.setVisibility(View.INVISIBLE);
		}
		// 给控件赋值
		if (wLottery.getName() != null && !"".equals(wLottery.getName())) {
			String str = wLottery.getName();
			String date = str.substring(0, str.length() - 2);
			String qi = str.substring(str.length() - 2, str.length());
			holder.text_qi.setText(Html.fromHtml("第" + date + " "
					+ "<font color='#EB1827'>" + qi + "</font>" + " 期"));
		}

		String weekday = "";
		String date = "";
		if (null != wLottery.getEndTime() && !"".equals(wLottery.getEndTime())) {
			date = wLottery.getEndTime().toString().trim().split(" ")[0];
		}
		try {
			weekday = " (" + LotteryUtils.getWeekOfDate(date) + ")";
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.text_time.setText(wLottery.getEndTime() + weekday);

		if (wLottery.getLotteryId().equals("39")) {
			String[] array = wLottery.getRedNum().replace(",", " ").split(" ");
			List<String> list = new ArrayList<String>();
			Collections.addAll(list, array);
			Collections.sort(list);
			if (groupPosition == 0) {// 设置listview的最新的一个为特殊格式
				holder.lottery_ll_firsShow.removeAllViews();
				holder.lottery_ll_firsShow.setVisibility(View.VISIBLE);
				addNumBall(holder.lottery_ll_firsShow, wLottery.getRedNum()
						.split(" "), null, 0);
				addNumBall(holder.lottery_ll_firsShow, wLottery.getBlueNum()
						.replaceFirst(" ", "").split(" "), null, 1);

			} else {
				holder.text_redNum.setText(list.toString().replace(",", " ")
						.replace("[", " ").replace("]", " "));
				array = wLottery.getBlueNum().replace(",", " ").split(" ");
				list = new ArrayList<String>();
				Collections.addAll(list, array);
				Collections.sort(list);
				holder.text_blueNum.setText(list.toString().replace(",", " ")
						.replace("[", " ").replace("]", " "));
			}
		} else if (wLottery.getLotteryId().equals("83")) {
			String num = wLottery.getRedNum();
			if (groupPosition == 0) {// 设置listview的最新的一个为图片形式
				if (num.substring(0, 1).equals("1")) {
					holder.img1.setBackgroundResource(R.drawable.dice1);
				} else if (num.substring(0, 1).equals("2")) {
					holder.img1.setBackgroundResource(R.drawable.dice2);
				} else if (num.substring(0, 1).equals("3")) {
					holder.img1.setBackgroundResource(R.drawable.dice3);
				} else if (num.substring(0, 1).equals("4")) {
					holder.img1.setBackgroundResource(R.drawable.dice4);
				} else if (num.substring(0, 1).equals("5")) {
					holder.img1.setBackgroundResource(R.drawable.dice5);
				} else if (num.substring(0, 1).equals("6")) {
					holder.img1.setBackgroundResource(R.drawable.dice6);
				}

				if (num.substring(1, 2).equals("1")) {
					holder.img2.setBackgroundResource(R.drawable.dice1);
				} else if (num.substring(1, 2).equals("2")) {
					holder.img2.setBackgroundResource(R.drawable.dice2);
				} else if (num.substring(1, 2).equals("3")) {
					holder.img2.setBackgroundResource(R.drawable.dice3);
				} else if (num.substring(1, 2).equals("4")) {
					holder.img2.setBackgroundResource(R.drawable.dice4);
				} else if (num.substring(1, 2).equals("5")) {
					holder.img2.setBackgroundResource(R.drawable.dice5);
				} else if (num.substring(1, 2).equals("6")) {
					holder.img2.setBackgroundResource(R.drawable.dice6);
				}

				if (num.substring(2, 3).equals("1")) {
					holder.img3.setBackgroundResource(R.drawable.dice1);
				} else if (num.substring(2, 3).equals("2")) {
					holder.img3.setBackgroundResource(R.drawable.dice2);
				} else if (num.substring(2, 3).equals("3")) {
					holder.img3.setBackgroundResource(R.drawable.dice3);
				} else if (num.substring(2, 3).equals("4")) {
					holder.img3.setBackgroundResource(R.drawable.dice4);
				} else if (num.substring(2, 3).equals("5")) {
					holder.img3.setBackgroundResource(R.drawable.dice5);
				} else if (num.substring(2, 3).equals("6")) {
					holder.img3.setBackgroundResource(R.drawable.dice6);
				}
			} else {
				holder.k3_number.setText(wLottery.getRedNum().replace("",
						"     "));
			}
			holder.text_hezhi.setText(""
					+ (Integer.parseInt(wLottery.getRedNum().substring(0, 1))
							+ Integer.parseInt(wLottery.getRedNum().substring(
									1, 2)) + Integer.parseInt(wLottery
							.getRedNum().substring(2, 3))));
		} else if (wLottery.getLotteryId().equals("5")
				|| wLottery.getLotteryId().equals("13")) { // 双色球+七乐彩
			if (groupPosition == 0) {// 设置listview的最新的一个为特殊格式
				holder.lottery_ll_firsShow.removeAllViews();
				holder.lottery_ll_firsShow.setVisibility(View.VISIBLE);
				addNumBall(holder.lottery_ll_firsShow, wLottery.getRedNum()
						.split(" "), null, 0);
				addNumBall(holder.lottery_ll_firsShow, wLottery.getBlueNum()
						.replace(" ", "").split(" "), null, 1);
			} else {
				holder.lottery_ll_firsShow.setVisibility(View.GONE);
				holder.text_redNum.setText(wLottery.getRedNum().replace(",",
						" "));
				holder.text_blueNum.setText(wLottery.getBlueNum());
			}
		} else if (wLottery.getLotteryId().equals("6")
				|| wLottery.getLotteryId().equals("61")
				|| wLottery.getLotteryId().equals("28")
				|| wLottery.getLotteryId().equals("66")
				|| wLottery.getLotteryId().equals("63")
				|| wLottery.getLotteryId().equals("64")
				|| wLottery.getLotteryId().equals("3")) {
			if (groupPosition == 0) {// 设置listview的最新的一个为特殊格式
				holder.lottery_ll_firsShow.removeAllViews();
				holder.lottery_ll_firsShow.setVisibility(View.VISIBLE);
				addNumBall(holder.lottery_ll_firsShow, null,
						wLottery.getRedNum(), 0);
			} else {
				holder.lottery_ll_firsShow.setVisibility(View.GONE);
				holder.text_redNum.setText(wLottery.getRedNum().replace(",",
						" "));
				holder.text_blueNum.setText(wLottery.getBlueNum());
			}
		} else if (wLottery.getLotteryId().equals("62")
				|| wLottery.getLotteryId().equals("70")
				|| wLottery.getLotteryId().equals("78")) {
			if (groupPosition == 0) {// 设置listview的最新的一个为特殊格式
				holder.lottery_ll_firsShow.removeAllViews();
				holder.lottery_ll_firsShow.setVisibility(View.VISIBLE);
				addNumBall(holder.lottery_ll_firsShow, wLottery.getRedNum()
						.split(" "), null, 0);
			} else {
				holder.lottery_ll_firsShow.setVisibility(View.GONE);
				holder.text_redNum.setText(wLottery.getRedNum().replace(",",
						" "));
				holder.text_blueNum.setText(wLottery.getBlueNum());
			}
		} else if (wLottery.getLotteryId().equals("74")
				|| wLottery.getLotteryId().equals("75")) {
			if (groupPosition == 0) {// 设置listview的最新的一个为特殊格式
				holder.lottery_ll_firsShow.removeAllViews();
				holder.lottery_ll_firsShow2.removeAllViews();
				holder.lottery_ll_firsShow.setVisibility(View.VISIBLE);
				holder.lottery_ll_firsShow2.setVisibility(View.VISIBLE);

				addNumBall(holder.lottery_ll_firsShow, null,
						wLottery.getRedNum(), 2);
				addNumBall(holder.lottery_ll_firsShow2, null,
						wLottery.getRedNum(), 3);

			} else {
				holder.lottery_ll_firsShow.setVisibility(View.GONE);
				holder.text_redNum.setText(wLottery.getRedNum().replace(",",
						" "));
				holder.text_blueNum.setText(wLottery.getBlueNum());
			}
		} else {
			holder.text_redNum.setText(wLottery.getRedNum().replace(",", " "));
			holder.text_blueNum.setText(wLottery.getBlueNum());
		}
		return view;
	}

	private void addNumBall(LinearLayout layout, String[] nums, String num,
			int flag) {
		if (nums == null || nums.length == 0) {
			if (TextUtils.isEmpty(num))
				return;

			StringBuilder buffer = new StringBuilder();
			for (int i = 0; i < num.length(); i++) {
				buffer.append(num.substring(i, i + 1));
				if (i != (num.length() - 1)) {
					buffer.append("-");
				}
			}
			nums = buffer.toString().split("-");
		}
		switch (flag) {
		case 0: // 添加红球
			TextView red_ball = null;
			for (int i = 0; i < nums.length; i++) {
				red_ball = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_lottery_ball, null);
				red_ball.setText(nums[i]);
				layout.addView(red_ball);
			}

			break;

		case 1:// 添加蓝球
			TextView blue_ball = null;
			for (int i = 0; i < nums.length; i++) {
				blue_ball = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_lottery_ball, null);
				blue_ball
						.setBackgroundResource(R.drawable.win_lottery_blue_ball_shape);
				blue_ball.setText(nums[i]);
				layout.addView(blue_ball);
			}
			break;
		case 2:// 添加矩形
			TextView rectangle = null;
			for (int i = 0; i < nums.length; i++) {
				rectangle = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_lottery_ball, null);
				rectangle
						.setBackgroundResource(R.drawable.win_lottery_rectangle_shape);
				rectangle.setText(nums[i]);
				layout.addView(rectangle);
			}
			break;

		case 3:// 添加矩形
			TextView rectangle2 = null;
			for (int i = 0; i < nums.length; i++) {
				rectangle2 = (TextView) LayoutInflater.from(context).inflate(
						R.layout.win_rx9_sfc_reg, null);
				rectangle2
						.setBackgroundResource(R.drawable.win_lottery_rectangle);
				rectangle2.setText((i + 1) + "");
				layout.addView(rectangle2);
			}
			break;
		}
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		if (list.isEmpty()) {
			return null;
		}
		ViewChildHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewChildHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.win_lottery_detail_child, null);
			// 得到控件

			holder.center_tv_payCount2 = (TextView) view
					.findViewById(R.id.center_tv_payCount2);
			holder.center_tv_lotteryMoney2 = (TextView) view
					.findViewById(R.id.center_tv_lotteryMoney2);
			holder.win_lottery_info_listview = (MyListView2) view
					.findViewById(R.id.win_lottery_info_listview);
			view.setTag(holder);
		} else {
			holder = (ViewChildHolder) view.getTag();
		}
		WinLottery wLottery = list.get(groupPosition);
		holder.center_tv_payCount2.setText(TextUtils.isEmpty(wLottery
				.getSales()) ? "-" : wLottery.getSales());
		holder.center_tv_lotteryMoney2.setText(TextUtils.isEmpty(wLottery
				.getTotalMoney()) ? "-" : wLottery.getTotalMoney());
		holder.win_lottery_info_listview.setAdapter(new WinInfoChildAdapter(
				context, wLottery.getListWinDetail()));
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private class ViewParentHolder {
		LinearLayout linearLayout, line_k3;// 非快三的开奖号码布局
		LinearLayout lottery_ll_firsShow;// listview的第一排的布局
		LinearLayout lottery_ll_firsShow2;// 胜负彩和任选九listview的第一排的布局
		LinearLayout ll_k3common, ll_k3top;
		TextView k3_number;
		ImageView img1, img2, img3;
		TextView text_hezhi, text_hezhishow;
		TextView text_qi;
		TextView text_time;
		TextView text_redNum;
		TextView text_blueNum;
		ImageView win_lottery_expand_arrow;
	}

	private class ViewChildHolder {
		TextView center_tv_payCount2;
		TextView center_tv_lotteryMoney2;
		MyListView2 win_lottery_info_listview;
	}
}
