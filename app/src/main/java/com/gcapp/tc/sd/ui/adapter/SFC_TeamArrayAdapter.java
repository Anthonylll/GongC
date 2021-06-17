package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.TeamArray;
import com.gcapp.tc.sd.ui.Select_SFC_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：胜负彩的选号界面适配器
 * 
 * @author lenovo
 * 
 */
public class SFC_TeamArrayAdapter extends BaseAdapter {
	// 上下文本
	private Context context;

	private List<TeamArray> list_TeamArray;
	private Select_SFC_Activity betActivity;
	private Vibrator vibrator; // 震动器
	private long count = 0; // 投注注数

	/** 存储所选 号码 */
	public static Map<Integer, String> btnMap = new HashMap<Integer, String>();

	public SFC_TeamArrayAdapter(Context context, List<TeamArray> TeamArray,
			Vibrator vibrator) {
		this.context = context;
		this.list_TeamArray = TeamArray;
		this.vibrator = vibrator;
		betActivity = (Select_SFC_Activity) context;
	}

	@Override
	public int getCount() {
		return list_TeamArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list_TeamArray.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final int index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.select_lv_item, null);
			// 得到控件
			holder.select_lv_item_tv_mainteam = (TextView) view
					.findViewById(R.id.select_lv_item_tv_mainteam);
			holder.select_lv_item_tv_guessteam = (TextView) view
					.findViewById(R.id.select_lv_item_tv_guessteam);
			holder.select_lv_item_tv_num = (TextView) view
					.findViewById(R.id.select_lv_item_tv_num);
			holder.select_lv_item_tv_matchname = (TextView) view
					.findViewById(R.id.select_lv_item_tv_matchname);
			holder.select_lv_item_tv_time = (TextView) view
					.findViewById(R.id.select_lv_item_tv_time);
			holder.select_lv_item_btn_left = (Button) view
					.findViewById(R.id.select_lv_item_btn_left);
			holder.select_lv_item_btn_center = (Button) view
					.findViewById(R.id.select_lv_item_btn_center);
			holder.select_lv_item_btn_right = (Button) view
					.findViewById(R.id.select_lv_item_btn_right);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		TeamArray teamArray = list_TeamArray.get(position);
		holder.select_lv_item_tv_mainteam.setText(teamArray.getMainTeam());
		// 处理界面胜平负显示
		holder.select_lv_item_btn_left.setText("胜" + teamArray.getWinSp());
		holder.select_lv_item_btn_center.setText("平" + teamArray.getFloatSp());
		holder.select_lv_item_btn_right.setText("负" + teamArray.getLoseSp());

		holder.select_lv_item_tv_num.setText(teamArray.getId());
		holder.select_lv_item_tv_matchname.setText(teamArray.getTime().split(
				" ")[0]
				+ teamArray.getGame());
		holder.select_lv_item_tv_time.setText(teamArray.getTime().split(" ")[1]
				+ teamArray.getMatchDate().trim());
		holder.select_lv_item_tv_guessteam.setText(teamArray.getGuestTeam());
		holder.select_lv_item_btn_left
				.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
		holder.select_lv_item_btn_center
				.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
		holder.select_lv_item_btn_right
				.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
		holder.select_lv_item_btn_left.setTextColor(Color.GRAY);
		holder.select_lv_item_btn_center.setTextColor(Color.GRAY);
		holder.select_lv_item_btn_right.setTextColor(Color.GRAY);
		if (btnMap.get(index) != null) {
			String Str = btnMap.get(index);
			if (Str.contains("1")) {
				holder.select_lv_item_btn_center
						.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
				holder.select_lv_item_btn_center.setTextColor(Color.WHITE);
			}
			if (Str.contains("0")) {
				holder.select_lv_item_btn_right
						.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
				holder.select_lv_item_btn_right.setTextColor(Color.WHITE);
			}
			if (Str.contains("3")) {
				holder.select_lv_item_btn_left
						.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
				holder.select_lv_item_btn_left.setTextColor(Color.WHITE);
			}
		}

		holder.select_lv_item_btn_right
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (vibrator != null)
							vibrator.vibrate(100);
						String str = "";
						if (btnMap.get(index) != null) {
							str = btnMap.get(index);
						}

						if (str.contains("0")) {
							str = str.replace("0", "");
							holder.select_lv_item_btn_right
									.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
							holder.select_lv_item_btn_right
									.setTextColor(Color.GRAY);
						} else {
							str += "0";
							holder.select_lv_item_btn_right
									.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
							holder.select_lv_item_btn_right
									.setTextColor(Color.WHITE);
						}

						btnMap.put(index, str);
						getTotalCount(btnMap);
						if (count > 10000) {
							MyToast.getToast(context, "投注金额不能超过20000");
							str = str.replace("0", "");
							holder.select_lv_item_btn_right
									.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
							holder.select_lv_item_btn_right
									.setTextColor(Color.GRAY);
							btnMap.put(index, str);
						} else {
							AppTools.totalCount = count;
						}
						betActivity.setTextShow();
					}
				});

		holder.select_lv_item_btn_center
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (vibrator != null)
							vibrator.vibrate(100);
						String str = "";
						if (btnMap.get(index) != null) {
							str = btnMap.get(index);
						}

						if (str.contains("1")) {
							str = str.replace("1", "");
							holder.select_lv_item_btn_center
									.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
							holder.select_lv_item_btn_center
									.setTextColor(Color.GRAY);
						} else {
							str += "1";
							holder.select_lv_item_btn_center
									.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
							holder.select_lv_item_btn_center
									.setTextColor(Color.WHITE);
						}

						btnMap.put(index, str);
						getTotalCount(btnMap);
						if (count > 10000) {
							MyToast.getToast(context, "投注金额不能超过20000");
							str = str.replace("1", "");
							holder.select_lv_item_btn_center
									.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
							btnMap.put(index, str);
						} else {
							AppTools.totalCount = count;
						}
						betActivity.setTextShow();
					}
				});

		holder.select_lv_item_btn_left
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (vibrator != null)
							vibrator.vibrate(100);
						String str = "";
						if (btnMap.get(index) != null) {
							str = btnMap.get(index);
						}

						if (str.contains("3")) {
							str = str.replace("3", "");
							holder.select_lv_item_btn_left
									.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
							holder.select_lv_item_btn_left
									.setTextColor(Color.GRAY);
						} else {
							str += "3";
							holder.select_lv_item_btn_left
									.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
							holder.select_lv_item_btn_left
									.setTextColor(Color.WHITE);
						}

						btnMap.put(index, str);
						getTotalCount(btnMap);
						if (count > 10000) {
							MyToast.getToast(context, "投注金额不能超过20000");
							str = str.replace("3", "");
							holder.select_lv_item_btn_left
									.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
							holder.select_lv_item_btn_left
									.setTextColor(Color.GRAY);
							btnMap.put(index, str);
						} else {
							AppTools.totalCount = count;
						}
						betActivity.setTextShow();
					}
				});
		return view;
	}

	/** 计算总注数 */
	public void getTotalCount(Map<Integer, String> btnMap) {
		count = NumberTools.getCountForSFC(btnMap);
	}

	/** 静态类 */
	static class ViewHolder {
		TextView select_lv_item_tv_mainteam, select_lv_item_tv_guessteam,
				select_lv_item_tv_num, select_lv_item_tv_matchname,
				select_lv_item_tv_time;
		Button select_lv_item_btn_left, select_lv_item_btn_center,
				select_lv_item_btn_right;
	}

}
