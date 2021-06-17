package com.gcapp.tc.sd.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.R;

import java.util.List;
import java.util.Map;

/** 历史中奖号码Adapter */
public class WinLotteryAdapter extends BaseAdapter {
	private Context context;
	// 装图片的集合
	private List<Map<String, String>> list;
	private float scale = 1f;

	public WinLotteryAdapter(Context context, List<Map<String, String>> list,
			Activity parent) {
		scale = AppTools.getDpi(parent);
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.win_lottery_items, null);
			// 得到控件
			holder.image = (ImageView) view.findViewById(R.id.win_items_image);
			holder.lotteryName = (TextView) view
					.findViewById(R.id.win_tv_lotteryName);
			holder.lotteryQI = (TextView) view
					.findViewById(R.id.win_tv_lotteryQi);
			holder.linearLayout1 = (LinearLayout) view
					.findViewById(R.id.lottery_ll_t);
			holder.lottery_ll_k3 = (LinearLayout) view
					.findViewById(R.id.lottery_ll_k3);
			holder.img1 = (ImageView) view.findViewById(R.id.k3_img1);

			holder.text_hezhi = (TextView) view.findViewById(R.id.text_hezhi);
			holder.img2 = (ImageView) view.findViewById(R.id.k3_img2);
			holder.img3 = (ImageView) view.findViewById(R.id.k3_img3);

			holder.ly_lottery_windetail = (LinearLayout) view
					.findViewById(R.id.ly_lottery_windetail);
			holder.win_tv_lottery_date = (TextView) view
					.findViewById(R.id.win_tv_lottery_date);
			holder.win_lottery_tv_hint = (TextView) view
					.findViewById(R.id.win_lottery_tv_hint);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (list.isEmpty()) {
			return view;
		}
		Map<String, String> item = list.get(position);
		if (Integer.parseInt(item.get("lotteryId")) == 83) {// 江苏快三

			String winnum = null;
			if (null != item.get("winLotteryNumber")
					&& !"".equals(item.get("winLotteryNumber"))) {
				holder.linearLayout1.setVisibility(View.GONE);
				holder.lottery_ll_k3.setVisibility(View.VISIBLE);
				holder.text_hezhi.setVisibility(View.VISIBLE);
			} else {
				holder.linearLayout1.setVisibility(View.GONE);
				holder.lottery_ll_k3.setVisibility(View.GONE);
				holder.text_hezhi.setVisibility(View.GONE);
			}

		} else {
			holder.linearLayout1.setVisibility(View.VISIBLE);
			holder.lottery_ll_k3.setVisibility(View.GONE);
			holder.text_hezhi.setVisibility(View.GONE);
		}
		// 给控件赋值
		String result = "", mainTeam = "", guestTeam = "";
		if (Integer.parseInt(item.get("lotteryId")) == 72) {
			holder.image.setVisibility(View.GONE);
			mainTeam = item.get("mainTeam");
			result = item.get("result");
			guestTeam = item.get("guestTeam");
			if (!item.get("isOpen").equals("0")) {
				holder.ly_lottery_windetail
						.setBackgroundResource(R.drawable.win_football);

			} else {
				holder.ly_lottery_windetail.setVisibility(View.GONE);
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
		} else if (Integer.parseInt(item.get("lotteryId")) == 73) {
			mainTeam = item.get("mainTeam");
			result = item.get("result");
			guestTeam = item.get("guestTeam");
			holder.image.setVisibility(View.GONE);
			if (!item.get("isOpen").equals("0")) {
				holder.ly_lottery_windetail
						.setBackgroundResource(R.drawable.win_basketball);
			} else {
				holder.ly_lottery_windetail.setVisibility(View.GONE);
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
		} else if (Integer.parseInt(item.get("lotteryId")) == 45) {
			mainTeam = item.get("mainTeam");
			result = item.get("result");
			guestTeam = item.get("guestTeam");
			holder.image.setVisibility(View.GONE);
			if (!item.get("isOpen").equals("0")) {
				holder.ly_lottery_windetail
						.setBackgroundResource(R.drawable.win_bjdc);
			} else {
				holder.ly_lottery_windetail.setVisibility(View.GONE);
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
		} else {
			holder.ly_lottery_windetail.setBackgroundResource(0);
			holder.image.setVisibility(View.GONE);
			holder.image.setBackgroundResource(AppTools.allLotteryLogo.get(item
					.get("lotteryId")));
		}
		holder.lotteryName.setText(LotteryUtils.getTitleText(item
				.get("lotteryId")));
		String temp = "第";
		String temp2 = "期";
		String weekday = "";
		if (Integer.parseInt(item.get("lotteryId")) == 72
				|| Integer.parseInt(item.get("lotteryId")) == 73
				|| Integer.parseInt(item.get("lotteryId")) == 45) {
			temp = "比赛日：";
			temp2 = "";
			String date = item.get("name");
			if (date.contains("/")) {
				date = date.replace("/", "-");
			}
			try {
				if (null != date && !"".equals(date) && date.contains("-")) {
					weekday = LotteryUtils.getWeekOfDate(date);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!"".equals(weekday)) {
				holder.lotteryQI.setText(item.get("name").equals("") ? " "
						: temp + item.get("name") + " (" + weekday + ")");
			} else {
				holder.lotteryQI.setText(item.get("name").equals("") ? " "
						: temp + item.get("name"));
			}

		} else {
			String date = item.get("EndTime");
			try {
				if (null != date && !"".equals(date) && date.contains("-")) {
					weekday = " (" + LotteryUtils.getWeekOfDate(date) + ")";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.lotteryQI.setText(item.get("name").equals("") ? " " : temp
					+ item.get("name") + temp2 + weekday);
		}
		holder.linearLayout1.removeAllViews();
		holder.win_lottery_tv_hint.setText("");
		holder.win_tv_lottery_date.setVisibility(View.GONE);
		holder.win_lottery_tv_hint.setVisibility(View.GONE);

		String num = null;
		if (item.get("winLotteryNumber") != null
				&& !item.get("winLotteryNumber").equals("")) {
			num = item.get("winLotteryNumber").replaceAll("\\s?[\\+]\\s?", "-");
		}
		switch (Integer.parseInt(item.get("lotteryId"))) {
		case 72:
		case 45:
			TextView team = null;
			team = (TextView) LayoutInflater.from(context).inflate(
					R.layout.win_lottery_jingcai, null);
			team.setText("              " + mainTeam + "   " + result + "   "
					+ guestTeam);
			holder.linearLayout1.addView(team);
			break;
		case 73:
			TextView team2 = null;
			team2 = (TextView) LayoutInflater.from(context).inflate(
					R.layout.win_lottery_jingcai, null);
			team2.setText("              " + guestTeam + "   " + result + "   "
					+ mainTeam);
			holder.linearLayout1.addView(team2);
			break;
		// 双色球
		case 5:
		case 39:
		case 13:
			if (num != null && num.contains("-")) {

				addNumBall(holder.linearLayout1, num.split("-")[0].split(" "),
						null, 0);
				addNumBall(holder.linearLayout1, num.split("-")[1].split(" "),
						null, 1);
			} else {
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
			break;
		case 62:// 山东11选5
		case 78:// 广东11选5
		case 70:// 江西11选5
			if (num != null && !num.equals("")) {
				addNumBall(holder.linearLayout1, num.split(" "), null, 0);
			} else {
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
			break;
		case 3: // 七星彩
		case 6: // 3D
		case 28: // 重庆时时彩
		case 61: // 江西时时彩
		case 63: // 排列3
		case 64: // 排列5
		case 66:
			if (num != null) {
				addNumBall(holder.linearLayout1, null, num, 0);
			} else {
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
			break;
		case 83: // 江苏快3
			if (num != null) {
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

				holder.text_hezhi
						.setText("和值："
								+ (Integer.parseInt(num.substring(0, 1))
										+ Integer.parseInt(num.substring(1, 2)) + Integer
											.parseInt(num.substring(2, 3))));

			} else {
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
			break;
		case 74:
		case 75:
			if (num != null) {
				addNumBall(holder.linearLayout1, null, num, 2);
			} else {
				holder.win_lottery_tv_hint.setVisibility(View.VISIBLE);
				holder.win_lottery_tv_hint.setText("暂无开奖信息");
			}
			break;
		default:
			break;
		}
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		ImageView image;
		TextView lotteryName, lotteryQI, tv_1, tv_2, win_tv_lottery_date,
				win_lottery_tv_hint;
		GridView gv_num;
		LinearLayout linearLayout1;
		LinearLayout ly_lottery_windetail;
		LinearLayout lottery_ll_k3;// 快三布局
		ImageView img1, img2, img3;
		TextView text_hezhi;
	}

	/**
	 * 添加数字求
	 * 
	 * @param layout
	 * @param nums
	 * @param num
	 * @param flag
	 *            0为红球，1为蓝球，2为矩形
	 */
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
		}
	}
}
