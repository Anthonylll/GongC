package com.gcapp.tc.sd.ui.adapter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.SelectedNumbers;
import com.gcapp.tc.dataaccess.TeamArray;
import com.gcapp.tc.sd.ui.Bet_RX9_Activity;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NumberTools;
import com.gcapp.tc.R;

/**
 * 功能：任选九的投注页面adapter
 * 
 * @author lenovo
 * 
 */
public class MyBetLotterListRX9Adapter extends BaseAdapter {
	// 上下文本
	private Context context;
	// 装彩票的集合
	public static List<SelectedNumbers> listSchemes;
	private Bet_RX9_Activity betActivity;
	private List<TeamArray> lisTeamArrays;// 获取的对阵的主客队集合
	private long count = 0; // 投注注数

	public MyBetLotterListRX9Adapter(Context context,
			List<TeamArray> lisTeamArrays, List<SelectedNumbers> listSchemes) {
		this.context = context;
		this.listSchemes = listSchemes;
		this.lisTeamArrays = lisTeamArrays;
		betActivity = (Bet_RX9_Activity) context;
	}

	// 用于刷新数据
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
			// // 得到布局文件
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

		Set<Map.Entry<Integer, String>> entry = RX9_TeamArrayAdapter.btnMap
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

		holder.btn_win.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (RX9_TeamArrayAdapter.btnMap.size() == 9
						&& !RX9_TeamArrayAdapter.btnMap.containsKey(index)) {
					// MyToast.getToast(context, "您已选择9场比赛").show();
					// return;
				}
				// if (vibrator != null)
				// vibrator.vibrate(100);
				String str = "";
				if (RX9_TeamArrayAdapter.btnMap.get(index) != null) {
					str = RX9_TeamArrayAdapter.btnMap.get(index);
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
				if (str.length() == 0) {
					RX9_TeamArrayAdapter.btnMap.remove(index);
				} else {
					RX9_TeamArrayAdapter.btnMap.put(index, str);
				}
				if (RX9_TeamArrayAdapter.btnMap.size() >= 9) {
					getTotalCount(RX9_TeamArrayAdapter.btnMap);
				} else {
					count = 0;
				}
				AppTools.totalCount = count;
				betActivity.setTextShow();
			}
		});

		holder.btn_flat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (RX9_TeamArrayAdapter.btnMap.size() == 9
						&& !RX9_TeamArrayAdapter.btnMap.containsKey(index)) {
					// MyToast.getToast(context, "您已选择9场比赛").show();
					// return;
				}
				String str = "";
				if (RX9_TeamArrayAdapter.btnMap.get(index) != null) {
					str = RX9_TeamArrayAdapter.btnMap.get(index);
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
				if (str.length() == 0) {
					RX9_TeamArrayAdapter.btnMap.remove(index);
				} else {
					RX9_TeamArrayAdapter.btnMap.put(index, str);
				}

				if (RX9_TeamArrayAdapter.btnMap.size() >= 9) {
					getTotalCount(RX9_TeamArrayAdapter.btnMap);
				} else {
					count = 0;
				}
				AppTools.totalCount = count;
				betActivity.setTextShow();
			}
		});

		holder.btn_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (RX9_TeamArrayAdapter.btnMap.size() == 9
						&& !RX9_TeamArrayAdapter.btnMap.containsKey(index)) {
					// MyToast.getToast(context, "您已选择9场比赛").show();
					// return;
				}

				String str = "";
				if (RX9_TeamArrayAdapter.btnMap.get(index) != null) {
					str = RX9_TeamArrayAdapter.btnMap.get(index);
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

				if (str.length() == 0) {
					RX9_TeamArrayAdapter.btnMap.remove(index);
				} else {
					RX9_TeamArrayAdapter.btnMap.put(index, str);
				}
				if (RX9_TeamArrayAdapter.btnMap.size() >= 9) {
					getTotalCount(RX9_TeamArrayAdapter.btnMap);
				} else {
					count = 0;
				}
				AppTools.totalCount = count;
				betActivity.setTextShow();
			}
		});

		return view;
	}

	/** 计算总注数 */
	private void getTotalCount(Map<Integer, String> btnMap) {
		count = NumberTools.getCountForRX9(btnMap);
	}

	/** 静态类 */
	static class ViewHolder {
		TextView mainTeam, guestTeam;
		Button btn_win;
		Button btn_flat;
		Button btn_lose;
	}
}
