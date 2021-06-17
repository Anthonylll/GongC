package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.dataaccess.DtMatch_Basketball;
import com.gcapp.tc.sd.ui.Bet_JCLQ_Activity;
import com.gcapp.tc.sd.ui.Select_JCLQ_Activity;
import com.gcapp.tc.sd.ui.Select_JCLQ_DAN_Activity;
import com.gcapp.tc.view.Bet_sfc_hhtz_jclq_Dialog;
import com.gcapp.tc.R;

/**
 * 功能：显示竞彩篮球 的投注 ListView的Adapter 版本
 * 
 * @author lenovo
 */
@SuppressLint("UseSparseArrays")
public class MyBetLotteryJCLQAdapter extends BaseAdapter {

	// 上下文本
	private Context context;
	private Bet_JCLQ_Activity betActivity;
	public List<String> list_dan = new ArrayList<String>();
	public List<String> list_dan_new = new ArrayList<String>();
	public List<DtMatch_Basketball> listDtmatch;
	private List<DtMatch_Basketball> listDtmatch2 = new ArrayList<DtMatch_Basketball>();
	/**
	 * 装 组下标 — 子类下标 的集合 *
	 */
	private List<String> listStr;
	private int type;
	private int ways;
	private LinkedHashMap<Integer, LinkedHashMap<Integer, String>> map = null;
	private Set<String> setIndex;

	/**
	 * 构造方法 实现初始化
	 */
	public MyBetLotteryJCLQAdapter(Context context, List<String> lists,
			int _type, int ways) {
		this.context = context;
		betActivity = (Bet_JCLQ_Activity) context;
		this.type = _type;
		this.ways = ways;
		list_dan = new ArrayList<String>();
		list_dan_new = new ArrayList<String>();
		setListDtmatch(lists);
	}

	public List<String> setListDan(List<String> list_dan) {
		list_dan_new = new ArrayList<String>();
		for (int i = 0; i < list_dan.size(); i++) {
			if (i == Integer.parseInt(list_dan.get(i))) {
				list_dan_new.add(list_dan.get(i));
			} else {
				list_dan_new.add(i + "");
			}
		}
		return list_dan_new;
	}

	public int getIndexsize() {
		return setIndex.size();
	}

	public void setSetIndex() {
		setIndex = new HashSet<String>();
		for (int i = 0; i < listDtmatch.size(); i++) {
			setIndex.add("" + i);
		}
	}

	public int getType() {
		return this.type;
	}

	public void clear() {
		if (null != listDtmatch)
			listDtmatch.clear();
		if (null != listDtmatch2)
			listDtmatch2.clear();
		if (null != listStr)
			listStr.clear();
		if (null != list_dan)
			list_dan.clear();
		list_dan_new.clear();
		setIndex.clear();
	}

	/**
	 * 给集合赋值
	 */
	public void setListDtmatch(List<String> listS) {
		setListStr(listS);

		listDtmatch = new ArrayList<DtMatch_Basketball>();
		if (ways == 0) {
			for (int i = 0; i < listStr.size(); i++) {
				int x = Integer.parseInt(listStr.get(i).split("-")[0]);
				int y = Integer.parseInt(listStr.get(i).split("-")[1]);
				this.listDtmatch.add(Select_JCLQ_Activity.list_Matchs.get(x).get(y));
			}
			
//			for (int i = 0; i < Select_JCLQ_Activity.list_Matchs.size(); i++) {
//
//				for (int j = 0; j < listStr.size(); j++) {
//
//					if (i == Integer.parseInt(listStr.get(j).split("-")[0])) {
//
//						for (int k = 0; k < Select_JCLQ_Activity.list_Matchs
//								.get(i).size(); k++) {
//							if (k == Integer
//									.parseInt(listStr.get(j).split("-")[1])) {
//								this.listDtmatch
//										.add(Select_JCLQ_Activity.list_Matchs
//												.get(i).get(k));
//							}
//						}
//					}
//				}
//			}
		} else {
			for (int i = 0; i < listStr.size(); i++) {
				int x = Integer.parseInt(listStr.get(i).split("-")[0]);
				int y = Integer.parseInt(listStr.get(i).split("-")[1]);
				this.listDtmatch.add(Select_JCLQ_DAN_Activity.list_Matchs_single.get(x).get(y));
			}
			
//			for (int i = 0; i < Select_JCLQ_DAN_Activity.list_Matchs_single
//					.size(); i++) {
//				for (int j = 0; j < listStr.size(); j++) {
//
//					if (i == Integer.parseInt(listStr.get(j).split("-")[0])) {
//
//						for (int k = 0; k < Select_JCLQ_DAN_Activity.list_Matchs_single
//								.get(i).size(); k++) {
//							if (k == Integer
//									.parseInt(listStr.get(j).split("-")[1])) {
//								this.listDtmatch
//										.add(Select_JCLQ_DAN_Activity.list_Matchs_single
//												.get(i).get(k));
//							}
//						}
//					}
//				}
//			}
		}
		listDtmatch2 = listDtmatch;
		setSetIndex();
	}

	/**
	 * 拿到所选对阵的集合 *
	 */
	public List<DtMatch_Basketball> getListDtMatch() {
		ArrayList<DtMatch_Basketball> list = new ArrayList<DtMatch_Basketball>();
		for (int i = 0; i < listDtmatch2.size(); i++) {
			if (setIndex.contains(i + "")) {
				list.add(listDtmatch2.get(i));
			}
		}
		return list;
	}

	/**
	 * 所选对阵的 键—键 *
	 */
	public void setListStr(List<String> lists) {
		this.listStr = new ArrayList<String>();
		if (lists != null) {
			for (String str : lists) {
				this.listStr.add(str);
			}
		}

	}

	@Override
	public int getCount() {
		return listDtmatch.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listDtmatch.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final DtMatch_Basketball dtm = listDtmatch.get(position);
		String[] str = listStr.get(position).split("-");
		final ViewHolder holder;
		final int dan_index = position;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(betActivity);
			// 得到布局文件
			view = inflater.inflate(R.layout.bet_jclq_sfdx_item, null);
			// 得到控件
			holder.ll_sf_dx_rfsf = (LinearLayout) view
					.findViewById(R.id.ll_sf_dx_rfsf);

			holder.ll_sf_dxf = (LinearLayout) view.findViewById(R.id.ll_sf_dxf);

			holder.ll_sfc = (LinearLayout) view.findViewById(R.id.ll_sfc);

			holder.btn_win = (Button) view.findViewById(R.id.btn_lose);

			holder.btn_clean = (ImageButton) view.findViewById(R.id.btn_clean);

			holder.btn_lose = (Button) view.findViewById(R.id.btn_mainWin);

			holder.btn_big = (Button) view.findViewById(R.id.btn_big);

			holder.btn_small = (Button) view.findViewById(R.id.btn_small);

			holder.btn_dan = (Button) view.findViewById(R.id.btn_dan);
			holder.tv_mainTeam = (TextView) view
					.findViewById(R.id.ll_tv_cusTeam);
			holder.tv_cusTeam = (TextView) view
					.findViewById(R.id.ll_tv_mainTeam);
			holder.ll_tv_matchNum = (TextView) view
					.findViewById(R.id.ll_tv_matchNum);
			holder.tv_loseball = (TextView) view.findViewById(R.id.ll_tv_vs);
			holder.tv_sfc_show = (TextView) view.findViewById(R.id.tv_sfc_show);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.btn_dan.setVisibility(View.GONE);
//		holder.btn_dan.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
//		holder.btn_dan.setTextColor(Color.RED);
		holder.tv_loseball.setTextColor(Color.RED);
//		if (type == 7306)
//			holder.btn_dan.setVisibility(View.GONE);
//		else
//			holder.btn_dan.setVisibility(View.VISIBLE);
		// 判断胆 是否显示
//		if (setIndex.size() < 3) {
//			holder.btn_dan.setVisibility(View.INVISIBLE);
//			list_dan.remove(dan_index + "");
//			holder.btn_dan
//					.setBackgroundResource(R.drawable.bet_btn_dan_unselected);
//			holder.btn_dan.setTextColor(Color.RED);
//		} else {
//			if (!setIndex.contains(position + "")) {
//				holder.btn_dan.setEnabled(false);
//				list_dan.remove(dan_index + "");
//			} else
//				holder.btn_dan.setEnabled(true);
//		}
//		for (int i = 0; i < list_dan.size(); i++) {
//			if (list_dan.get(i).equals(dan_index + "")) {
//				holder.btn_dan
//						.setBackgroundResource(R.drawable.bet_btn_dan_selected);
//				holder.btn_dan.setTextColor(Color.WHITE);
//			}
//		}
		setListDan(list_dan);
//		if (ways == 1)// 单关不显示胆
//			holder.btn_dan.setVisibility(View.GONE);

		// 给控件赋值
		holder.ll_tv_matchNum.setText(dtm.getMatchNumber());
		holder.tv_mainTeam.setText(dtm.getMainTeam());
		holder.tv_cusTeam.setText(dtm.getGuestTeam());
		if (type == 7301 || type == 7302) {
			holder.ll_sf_dxf.setVisibility(View.GONE);
			holder.ll_sfc.setVisibility(View.GONE);
			holder.ll_sf_dx_rfsf.setVisibility(View.VISIBLE);
			holder.tv_loseball.setText("VS");
			if (type == 7302) {
				holder.btn_win.setText("主胜" + dtm.getLetMainWin());
				holder.btn_lose.setText("主负" + dtm.getLetMainLose());

				if (Double.parseDouble(dtm.getLetScore().equals("") ? "0" : dtm
						.getLetScore()) > 0) {
					holder.tv_loseball.setTextColor(context.getResources()
							.getColor(R.color.main_red));
					holder.tv_loseball.setText("+" + dtm.getLetScore());
				} else {
					holder.tv_loseball.setTextColor(context.getResources()
							.getColor(R.color.select_jczq_tvcolor_green));
					holder.tv_loseball.setText(dtm.getLetScore());
				}
			} else {
				holder.btn_win.setText("主胜" + dtm.getMainWin());
				holder.btn_lose.setText("主负" + dtm.getMainLose());
			}
		} else if (type == 7304) {
			holder.ll_sfc.setVisibility(View.GONE);
			holder.ll_sf_dx_rfsf.setVisibility(View.GONE);
			holder.ll_sf_dxf.setVisibility(View.VISIBLE);
			holder.tv_loseball.setText(dtm.getBigSmallScore());
			holder.btn_big.setText("大分" + dtm.getBig());
			holder.btn_small.setText("小分" + dtm.getSmall());
		} else if (type == 7303) {
			holder.ll_sf_dxf.setVisibility(View.GONE);
			holder.ll_sf_dx_rfsf.setVisibility(View.GONE);
			holder.ll_sfc.setVisibility(View.VISIBLE);
			holder.tv_loseball.setText("VS");
		}
		holder.groupId = Integer.parseInt(str[0]);
		holder.itemId = Integer.parseInt(str[1]);

		// 将所有按钮设置未选中
		holder.btn_win
				.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
		holder.btn_lose
				.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
		holder.btn_win.setTextColor(betActivity.getResources().getColor(
				R.color.select_pop_lv_tv));
		holder.btn_lose.setTextColor(betActivity.getResources().getColor(
				R.color.select_pop_lv_tv));
		holder.btn_big
				.setBackgroundResource(R.drawable.select_sfc_lv_item_left);
		holder.btn_small
				.setBackgroundResource(R.drawable.select_sfc_lv_item_right);
		holder.btn_big.setTextColor(betActivity.getResources().getColor(
				R.color.select_pop_lv_tv));
		holder.btn_small.setTextColor(betActivity.getResources().getColor(
				R.color.select_pop_lv_tv));

		map = betActivity.select_hashMap;
		// 将选中的按钮 改变背景
		if (type == 7301 || type == 7302) {
			if (map.containsKey(holder.groupId)) {
				LinkedHashMap<Integer, String> hashMap = map.get(holder.groupId);
				if (hashMap.containsKey(holder.itemId)) {
					Log.i("x", "ItemId 包含----" + holder.itemId + "----包含子集内容"
							+ hashMap.get(holder.itemId));
					if (hashMap.get(holder.itemId).contains("2")) {
						holder.btn_win
								.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
						holder.btn_win.setTextColor(Color.WHITE);
					}
					if (hashMap.get(holder.itemId).contains("1")) {
						holder.btn_lose
								.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
						holder.btn_lose.setTextColor(Color.WHITE);
					}
				}
			}

		} else if (type == 7304) {
			if (map.containsKey(holder.groupId)) {
				LinkedHashMap<Integer, String> hashMap = map.get(holder.groupId);
				if (hashMap.containsKey(holder.itemId)) {
					Log.i("x", "ItemId 包含----" + holder.itemId + "----包含子集内容"
							+ hashMap.get(holder.itemId));
					if (hashMap.get(holder.itemId).contains("1")) {
						holder.btn_big
								.setBackgroundResource(R.drawable.select_sfc_lv_item_left_selected);
						holder.btn_big.setTextColor(Color.WHITE);
					}
					if (hashMap.get(holder.itemId).contains("2")) {
						holder.btn_small
								.setBackgroundResource(R.drawable.select_sfc_lv_item_right_selected);
						holder.btn_small.setTextColor(Color.WHITE);
					}
				}
			}
		} else if (type == 7303 || type == 7306) {
			if (map.containsKey(holder.groupId)) {
				String show = "";
				LinkedHashMap<Integer, String> hashMap = map.get(holder.groupId);
				if (hashMap.containsKey(holder.itemId)) {
					if (map.get(holder.groupId).get(holder.itemId)
							.contains(",")) {
						for (int i = 0; i < map.get(holder.groupId)
								.get(holder.itemId).split(",").length; i++) {
							if (i == 0)
								show += get_show_sfc(map.get(holder.groupId)
										.get(holder.itemId).split(",")[i]);
							else
								show += ","
										+ get_show_sfc(map.get(holder.groupId)
												.get(holder.itemId).split(",")[i]);
						}

					} else {
						show = get_show_sfc(map.get(holder.groupId).get(
								holder.itemId));
					}
				}
				holder.tv_sfc_show.setText(show);
				holder.tv_sfc_show
						.setBackgroundResource(R.drawable.select_jc_bg_red);
				holder.tv_sfc_show.setTextColor(Color.WHITE);
			}
		}
		/** 点击胜 **/
		holder.btn_win.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPassType();
				LinkedHashMap<Integer, String> hash = new LinkedHashMap<Integer, String>();
				if (map.containsKey(holder.groupId))
					hash = map.get(holder.groupId);
				if (hash.containsKey(holder.itemId)) {
					if (hash.get(holder.itemId).contains("2")) {
						hash.put(holder.itemId, hash.get(holder.itemId)
								.replace("2", ""));
					} else {
						hash.put(holder.itemId, hash.get(holder.itemId) + "2");
					}
				} else {
					hash.put(holder.itemId, "2");
					setIndex.add(position + "");
				}

				setIndex.add(position + "");

				if (hash.get(holder.itemId).length() == 0) {
					hash.remove(holder.itemId);
					setIndex.remove(position + "");
				}
				betActivity.clean_passType();

				betActivity.select_hashMap.put(holder.groupId, hash);
				betActivity.updateAdapter(1);
				notifyDataSetChanged();
				setdan(holder.groupId, holder.btn_dan); // 去判断是否选择
			}

		});

		/** 点击负 **/
		holder.btn_lose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPassType();
				LinkedHashMap<Integer, String> hash = new LinkedHashMap<Integer, String>();
				if (map.containsKey(holder.groupId)) {
					hash = map.get(holder.groupId);
					if (hash.containsKey(holder.itemId)) {
						if (hash.get(holder.itemId).contains("1")) {
							hash.put(holder.itemId, hash.get(holder.itemId)
									.replace("1", ""));
						} else {
							Log.i("x", "增加--1-----" + hash.get(holder.itemId));
							hash.put(holder.itemId, hash.get(holder.itemId)
									+ "1");
						}
					} else {
						hash.put(holder.itemId, "1");
						setIndex.add(position + "");

					}
					if (hash.get(holder.itemId).length() == 0) {
						hash.remove(holder.itemId);
						setIndex.remove(position + "");
					}
				} else {
					setIndex.add(position + "");
				}

				if (hash.containsKey(holder.itemId)) {
					System.out.println("=======点击的方法=true====");
				} else {
					System.out.println("=======点击的方法=false====");
				}
				betActivity.clean_passType();
				betActivity.select_hashMap.put(holder.groupId, hash);
				betActivity.updateAdapter(1);
				notifyDataSetChanged();
				setdan(holder.groupId, holder.btn_dan); // 去判断是否选择
			}
		});

		/** 点击胜 **/
		holder.btn_big.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPassType();
				Log.i("x", "点击胜" + holder.groupId + "----" + holder.itemId);
				LinkedHashMap<Integer, String> hash = new LinkedHashMap<Integer, String>();
				if (map.containsKey(holder.groupId))
					hash = map.get(holder.groupId);
				if (hash.containsKey(holder.itemId)) {
					if (hash.get(holder.itemId).contains("1")) {
						hash.put(holder.itemId, hash.get(holder.itemId)
								.replace("1", ""));
					} else {
						hash.put(holder.itemId, hash.get(holder.itemId) + "1");
					}
				} else {
					hash.put(holder.itemId, "1");
					setIndex.add(position + "");
				}

				setIndex.add(position + "");

				if (hash.get(holder.itemId).length() == 0) {
					hash.remove(holder.itemId);
					setIndex.remove(position + "");
				}

				betActivity.clean_passType();
				betActivity.select_hashMap.put(holder.groupId, hash);
				betActivity.updateAdapter(1);
				setdan(holder.groupId, holder.btn_dan); // 去判断是否选择
			}

		});

		/** 点击负 **/
		holder.btn_small.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPassType();
				LinkedHashMap<Integer, String> hash = new LinkedHashMap<Integer, String>();
				if (map.containsKey(holder.groupId)) {
					hash = map.get(holder.groupId);
					if (hash.containsKey(holder.itemId)) {
						if (hash.get(holder.itemId).contains("2")) {
							hash.put(holder.itemId, hash.get(holder.itemId)
									.replace("2", ""));
						} else {
							hash.put(holder.itemId, hash.get(holder.itemId)
									+ "2");
						}
					} else {
						hash.put(holder.itemId, "2");
						setIndex.add(position + "");
					}
					if (hash.get(holder.itemId).length() == 0) {
						hash.remove(holder.itemId);
						setIndex.remove(position + "");
					}
				} else {
					setIndex.add(position + "");
				}
				betActivity.clean_passType();
				betActivity.select_hashMap.put(holder.groupId, hash);
				betActivity.updateAdapter(1);
				setdan(holder.groupId, holder.btn_dan); // 去判断是否选择
			}
		});

		/** 设置胆 **/
//		holder.btn_dan.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				initPassType();
//				if (list_dan.contains(dan_index + "")) {
//					list_dan.remove(dan_index + "");
//					holder.btn_dan.setBackgroundResource(R.drawable.jc_btn);
//					holder.btn_dan.setTextColor(Color.BLACK);
//				} else {
//					if (listDtmatch.size() > 7) {
//						if (list_dan.size() < 7) {
//							Log.i("x", "点击增加了---胆");
//							list_dan.add(dan_index + "");
//							holder.btn_dan
//									.setBackgroundResource(R.drawable.jc_btn_select);
//							holder.btn_dan.setTextColor(Color.WHITE);
//						}
//					} else {
//						if (list_dan.size() < listDtmatch.size() - 1) {
//							Log.i("x", "点击增加了---胆");
//							list_dan.add(dan_index + "");
//							holder.btn_dan
//									.setBackgroundResource(R.drawable.jc_btn_select);
//							holder.btn_dan.setTextColor(Color.WHITE);
//						}
//					}
//				}
//
//				setListDan(list_dan);
//				betActivity.clean_passType();
//				betActivity.clearTypedialog();
//				betActivity.updateAdapter(1);
//				betActivity.setTotalCount();
//				betActivity.setCountText();
//			}
//		});

		holder.tv_sfc_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPassType();

				Bet_sfc_hhtz_jclq_Dialog bet_sfc_hhtz_jclq_dialog = new Bet_sfc_hhtz_jclq_Dialog(
						context, R.style.dialog, type == 7306 ? 1 : 0,
						holder.groupId, holder.itemId, dtm, map,ways);
				bet_sfc_hhtz_jclq_dialog.show();
			}
		});

		holder.btn_clean.setOnClickListener(new OnClickListener() {// 删除
					@Override
					public void onClick(View v) {
						initPassType();
						betActivity.select_hashMap.get(holder.groupId).remove(
								holder.itemId);
						setIndex.remove(position + "");
						setIndex = set(setIndex);
						listDtmatch.remove(position);
						listStr.remove(position);
						list_dan.clear();
						list_dan_new.clear();
						betActivity.clean_passType();
						betActivity.updateAdapter(1);
					}
				});

		return view;
	}

	// 为了控制玩法弹出框
	public void initPassType() {
		if (ways == 0) {
			if (betActivity.getPopIsShow()) {
				betActivity.selectType();
			}
		}
	}

	// 删除一条对阵的时候更新setindex
	private Set<String> set(Set<String> index) {
		Set<String> ll = new HashSet<String>();
		for (int i = 0; i < index.size(); i++) {
			ll.add(i + "");
		}
		return ll;
	}

	void setdan(int position, View view) { // 父件下标 / 子件下标
		for (int key : map.keySet()) {
			System.out.println("key= " + key + " ///value= " + map.get(key));
		}
		if (null != map.get(position).get(1)
				&& map.get(position).get(1).equals("")
				&& null != map.get(position).get(2)
				&& map.get(position).get(2).equals("")) {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 静态类
	 */
	static class ViewHolder {
		Button btn_win, btn_lose, btn_dan;
		Button btn_big, btn_small;
		ImageButton btn_clean;
		TextView tv_mainTeam, tv_cusTeam, tv_loseball,ll_tv_matchNum;
		TextView tv_sfc_show;
		LinearLayout ll_sf_dx_rfsf, ll_sfc, ll_sf_dxf;
		int groupId, itemId;
	}

	public String get_show_sfc(String key) {
		int a = Integer.parseInt(key);
		switch (a - 1) {
		case 0:
			return "主负1-5";
		case 1:
			return "主胜1-5";
		case 2:
			return "主负6-10";
		case 3:
			return "主胜6-10";
		case 4:
			return "主负11-15";
		case 5:
			return "主胜11-15";
		case 6:
			return "主负16-20";
		case 7:
			return "主胜16-20";
		case 8:
			return "主负21-25";
		case 9:
			return "主胜21-25";
		case 10:
			return "主负26+";
		case 11:
			return "主胜26+";
		case 200:
			return "让主胜";
		case 199:
			return "让主负";
		case 100:
			return "主胜";
		case 99:
			return "主负";
		case 299:
			return "大分";
		case 300:
			return "小分";
		default:
			break;
		}
		return null;
	}

}
