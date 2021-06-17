package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：北京单场的过关方式的适配器
 * 
 * @author lenovo
 * 
 */
public class SelectPasstype_Gridview_BJDCAdapter extends BaseAdapter {
	private int pIndex;// 标记是多串一还是多串多
	private Context context;

	public SelectPasstype_Gridview_BJDCAdapter(Context context, int pIndex) {
		this.pIndex = pIndex;
		this.context = context;
	}

	@Override
	public int getCount() {
		int size = 0;
		if (pIndex == 0) {
			size = SelectPasstype_List_BJDCAdapter.passOne.size();
		} else {
			size = SelectPasstype_List_BJDCAdapter.passMore.size();
		}
		return size;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		if (pIndex == 0) {// 多串一
			final HolderPassOne holder;
			if (null == contentView) {
				contentView = LayoutInflater.from(context).inflate(
						R.layout.bet_passone_gv_item, null);
				holder = new HolderPassOne();
				holder.iv_passone = (ImageView) contentView
						.findViewById(R.id.iv_passone);
				holder.tv_passone = (TextView) contentView
						.findViewById(R.id.tv_passone);
				holder.layout_passone = (LinearLayout) contentView
						.findViewById(R.id.layout_passone);
				contentView.setTag(holder);
			} else {
				holder = (HolderPassOne) contentView.getTag();
			}
			holder.tv_passone
					.setText(SelectPasstype_List_BJDCAdapter.passType
							.get(SelectPasstype_List_BJDCAdapter.passOne
									.get(position)));
			setBackGround(holder.iv_passone, R.drawable.left_gou_unselected,
					holder.tv_passone, R.drawable.right_unselected);
			if (SelectPasstype_List_BJDCAdapter.selectPassType
					.contains(SelectPasstype_List_BJDCAdapter.passOne
							.get(position))) {
				setBackGround(holder.iv_passone, R.drawable.left_gou_selected,
						holder.tv_passone, R.drawable.right_selected);
			}
			holder.layout_passone
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							boolean mark = false;
							if (SelectPasstype_List_BJDCAdapter.selectPassType
									.contains(SelectPasstype_List_BJDCAdapter.passOne
											.get(position))) {// 如果包含
								SelectPasstype_List_BJDCAdapter.selectPassType
										.remove(SelectPasstype_List_BJDCAdapter.passOne
												.get(position));// 移除
							} else {// 反之添加
								for (String temp : SelectPasstype_List_BJDCAdapter.selectPassType) {// 判断是否已经选了多串多
									if (SelectPasstype_List_BJDCAdapter.passMore
											.contains(temp)) {
										mark = true;
									}
								}
								if (!mark) {// 如果不包含多穿多就添加
									SelectPasstype_List_BJDCAdapter.selectPassType
											.add(SelectPasstype_List_BJDCAdapter.passOne
													.get(position));
								} else {
									MyToast.getToast(context, "只能选择一种过关方式")
											;
								}
							}
							notifyDataSetChanged();
							// 计算注数
							if (null != SelectPasstype_List_BJDCAdapter.listener) {
								SelectPasstype_List_BJDCAdapter.listener
										.getResult(
												1,
												SelectPasstype_List_BJDCAdapter.selectPassType,
												0);
							}
						}
					});
			return contentView;

		} else {// 多串多
			final HolderPassMore holder;
			if (null == contentView) {
				contentView = LayoutInflater.from(context).inflate(
						R.layout.bet_passmore_gv_item, null);
				holder = new HolderPassMore();
				holder.iv_passmore = (ImageView) contentView
						.findViewById(R.id.iv_passmore);
				holder.tv_passmore = (TextView) contentView
						.findViewById(R.id.tv_passmore);
				holder.layout_passmore = (LinearLayout) contentView
						.findViewById(R.id.layout_passmore);
				contentView.setTag(holder);
			} else {
				holder = (HolderPassMore) contentView.getTag();
			}
			holder.tv_passmore
					.setText(SelectPasstype_List_BJDCAdapter.passType
							.get(SelectPasstype_List_BJDCAdapter.passMore
									.get(position)));
			setBackGround(holder.iv_passmore, R.drawable.left_gou_unselected,
					holder.tv_passmore, R.drawable.right_unselected);
			if (SelectPasstype_List_BJDCAdapter.selectPassType
					.contains(SelectPasstype_List_BJDCAdapter.passMore
							.get(position))) {
				setBackGround(holder.iv_passmore, R.drawable.left_gou_selected,
						holder.tv_passmore, R.drawable.right_selected);
			}
			holder.layout_passmore
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (SelectPasstype_List_BJDCAdapter.selectPassType
									.contains(SelectPasstype_List_BJDCAdapter.passMore
											.get(position))) {// 如果包含
								SelectPasstype_List_BJDCAdapter.selectPassType
										.remove(SelectPasstype_List_BJDCAdapter.passMore
												.get(position));// 移除
							} else {// 反之添加
								boolean mark = false;
								for (String temp : SelectPasstype_List_BJDCAdapter.selectPassType) {// 判断是否已经选了多串多
									if (SelectPasstype_List_BJDCAdapter.passOne
											.contains(temp)) {
										mark = true;
									}
								}
								if (!mark) {// 如果不包含多穿多就添加
									SelectPasstype_List_BJDCAdapter.selectPassType
											.add(SelectPasstype_List_BJDCAdapter.passMore
													.get(position));
								} else {
									MyToast.getToast(context, "只能选择一种过关方式")
											;
								}
							}

							notifyDataSetChanged();
							// 计算注数
							if (null != SelectPasstype_List_BJDCAdapter.listener) {
								SelectPasstype_List_BJDCAdapter.listener
										.getResult(
												1,
												SelectPasstype_List_BJDCAdapter.selectPassType,
												1);
							}
						}
					});
			return contentView;
		}
	}

	/**
	 * 设置背景
	 */
	public void setBackGround(ImageView iv_pass, int iv_bg, TextView tv_pass,
			int tv_bg) {
		iv_pass.setBackgroundResource(iv_bg);
		tv_pass.setBackgroundResource(tv_bg);
	}

	static class HolderPassOne {
		private ImageView iv_passone;
		private TextView tv_passone;
		private LinearLayout layout_passone;
	}

	static class HolderPassMore {
		private ImageView iv_passmore;
		private TextView tv_passmore;
		private LinearLayout layout_passmore;

	}
}
