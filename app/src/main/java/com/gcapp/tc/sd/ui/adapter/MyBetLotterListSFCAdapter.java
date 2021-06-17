package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.dataaccess.TeamArray;
import com.gcapp.tc.sd.ui.Bet_SFC_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 功能：胜负彩的投注页面adapter
 * 
 * @author lenovo
 * 
 */
public class MyBetLotterListSFCAdapter extends BaseAdapter {
	// 上下文本
	private Context context;
	// 装彩票的集合
	private Vibrator vibrator; // 震动器
	private long count = 0; // 投注注数
	private Bet_SFC_Activity betActivity;
	private List<TeamArray> lisTeamArrays;

	public MyBetLotterListSFCAdapter(Context context,
			List<TeamArray> lisTeamArrays, List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.lisTeamArrays = lisTeamArrays;
		betActivity = (Bet_SFC_Activity) context;
	}

	public void setNumberList(List<TeamArray> numbers) {
		this.lisTeamArrays = numbers;
	}

	@Override
	public int getCount() {
		return lisTeamArrays.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lisTeamArrays.get(arg0);
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
			view = inflater.inflate(R.layout.bet_rxnine_items, null);
			holder.guestTeam = (TextView) view.findViewById(R.id.ll_tv_cusTeam);
			holder.mainTeam = (TextView) view.findViewById(R.id.ll_tv_mainTeam);

			holder.btn_win = (Button) view.findViewById(R.id.btn_mainWin3);
			holder.btn_flat = (Button) view.findViewById(R.id.btn_float1);
			holder.btn_lose = (Button) view.findViewById(R.id.btn_lose0);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		TeamArray teamArray = lisTeamArrays.get(position);
		holder.mainTeam.setText(teamArray.getMainTeam());
		holder.guestTeam.setText(teamArray.getGuestTeam());

		// 处理界面胜平负显示
		holder.btn_win.setText("胜" + teamArray.getWinSp());
		holder.btn_flat.setText("平" + teamArray.getFloatSp());
		holder.btn_lose.setText("负" + teamArray.getLoseSp());

		Set<Map.Entry<Integer, String>> entry = SFC_TeamArrayAdapter.btnMap
				.entrySet();
		for (Map.Entry<Integer, String> e : entry) {
			String Str = "";
			Str = e.getValue();
			if (e.getKey() == index) {
				if (Str.contains("1")) {
					holder.btn_flat
							.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
					holder.btn_flat.setTextColor(Color.WHITE);
				} else {
					holder.btn_flat
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
					holder.btn_flat.setTextColor(Color.GRAY);
				}

				if (Str.contains("0")) {
					holder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
					holder.btn_lose.setTextColor(Color.WHITE);
				} else {
					holder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
					holder.btn_lose.setTextColor(Color.GRAY);
				}

				if (Str.contains("3")) {
					holder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
					holder.btn_win.setTextColor(Color.WHITE);
				} else {
					holder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
					holder.btn_win.setTextColor(Color.GRAY);
				}
			}
		}

		// 点击事件的监听
		holder.btn_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// if (vibrator != null)
				// vibrator.vibrate(100);
				String str = "";
				if (SFC_TeamArrayAdapter.btnMap.get(index) != null) {
					str = SFC_TeamArrayAdapter.btnMap.get(index);
				}

				if (str.contains("0")) {
					str = str.replace("0", "");
					holder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
					holder.btn_lose.setTextColor(Color.GRAY);
				} else {
					str += "0";
					holder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
					holder.btn_lose.setTextColor(Color.WHITE);
				}

				SFC_TeamArrayAdapter.btnMap.put(index, str);
				getTotalCount(SFC_TeamArrayAdapter.btnMap);
				if (count > 10000) {
					MyToast.getToast(context, "投注金额不能超过20000");
					str = str.replace("0", "");
					holder.btn_lose
							.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
					holder.btn_lose.setTextColor(Color.GRAY);
					SFC_TeamArrayAdapter.btnMap.put(index, str);
				} else {
					AppTools.totalCount = count;
				}
				betActivity.setTextShow();
			}
		});
		holder.btn_flat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vibrator != null)
					vibrator.vibrate(100);
				String str = "";
				if (SFC_TeamArrayAdapter.btnMap.get(index) != null) {
					str = SFC_TeamArrayAdapter.btnMap.get(index);
				}
				if (str.contains("1")) {
					str = str.replace("1", "");
					holder.btn_flat
							.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
					holder.btn_flat.setTextColor(Color.GRAY);
				} else {
					str += "1";
					holder.btn_flat
							.setBackgroundResource(R.drawable.select_sfc_lv_item_center_selected);
					holder.btn_flat.setTextColor(Color.WHITE);
				}
				SFC_TeamArrayAdapter.btnMap.put(index, str);
				getTotalCount(SFC_TeamArrayAdapter.btnMap);
				if (count > 10000) {
					MyToast.getToast(context, "投注金额不能超过20000");
					str = str.replace("1", "");
					holder.btn_flat
							.setBackgroundResource(R.drawable.select_sfc_lv_item_center);
					SFC_TeamArrayAdapter.btnMap.put(index, str);
				} else {
					AppTools.totalCount = count;
				}
				betActivity.setTextShow();
			}
		});

		holder.btn_win.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vibrator != null)
					vibrator.vibrate(100);
				String str = "";
				if (SFC_TeamArrayAdapter.btnMap.get(index) != null) {
					str = SFC_TeamArrayAdapter.btnMap.get(index);
				}

				if (str.contains("3")) {
					str = str.replace("3", "");
					holder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
					holder.btn_win.setTextColor(Color.GRAY);
				} else {
					str += "3";
					holder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
					holder.btn_win.setTextColor(Color.WHITE);
				}

				SFC_TeamArrayAdapter.btnMap.put(index, str);
				getTotalCount(SFC_TeamArrayAdapter.btnMap);
				if (count > 10000) {
					MyToast.getToast(context, "投注金额不能超过20000");
					str = str.replace("3", "");
					holder.btn_win
							.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
					holder.btn_win.setTextColor(Color.GRAY);
					SFC_TeamArrayAdapter.btnMap.put(index, str);
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
		TextView mainTeam, guestTeam;
		Button btn_win;
		Button btn_flat;
		Button btn_lose;
	}

	/**
	 * 给map排序
	 * 
	 * @param map
	 */
	private static List<Entry<Integer, String>> sort(Map<Integer, String> map) {
		List<Map.Entry<Integer, String>> mEntryList = new ArrayList<Map.Entry<Integer, String>>(
				map.entrySet());
		Collections.sort(mEntryList,
				new Comparator<Map.Entry<Integer, String>>() {
					@Override
					public int compare(
							Map.Entry<Integer, String> firstMapEntry,
							Map.Entry<Integer, String> secondMapEntry) {
						return firstMapEntry.getKey().compareTo(
								secondMapEntry.getKey());
					}
				});
		return mEntryList;
	}

}
