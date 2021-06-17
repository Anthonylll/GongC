package com.gcapp.tc.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gcapp.tc.sd.ui.Select_JCLQ_DAN_Activity;
import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 功能：竞彩 筛选框
 */
public class MyJCDialog_jclq_danguan extends Dialog implements OnClickListener {

	private Context context;
	private Select_JCLQ_DAN_Activity activity;
	/** 所选集合下标 **/
	private Set<Integer> setCheck = new HashSet<Integer>();
	/** 让球类型 **/
	private int type = 100, oldType = 100;
	private Button btn_cancel, btn_confirm, btn_select_anti, btn_select_all;
	private GridView gv;
	/** 存储所有赛事的集合 game名称 **/
	private List<String> list_all = new ArrayList<String>();
	/** 存储所选赛事的集合 game名称 **/
	private List<String> listGame = new ArrayList<String>();
	private GridAdapter gAdapter;
	private int ways = 1; // 过关或者单关

	public MyJCDialog_jclq_danguan(Context _context, Set<String> set,
			boolean cancelable, OnCancelListener cancelListener) {
		super(_context, cancelable, cancelListener);
		init(_context, set);
	}

	public MyJCDialog_jclq_danguan(Context _context, Set<String> set, int theme) {
		super(_context, theme);
		init(_context, set);
	}

	public MyJCDialog_jclq_danguan(Context _context, Set<String> set) {
		super(_context);
		init(_context, set);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop_screen_dtmatch);
		findView();
		setListener();
		selectAll();
	}

	/** 初始化 */
	private void init(Context _context, Set<String> set) {

		this.context = _context;
		activity = (Select_JCLQ_DAN_Activity) context;
		for (String aSet : set) {
			this.list_all.add(aSet);
		}
	}

	/** 初始化UI */
	private void findView() {
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_select_anti = (Button) this.findViewById(R.id.btn_select_anti);
		btn_select_all = (Button) this.findViewById(R.id.btn_select_all);
		gv = (MyGridView) this.findViewById(R.id.pop_screen_gv);
		gAdapter = new GridAdapter(context);
		gv.setAdapter(gAdapter);
	}

	/** 绑定监听 */
	private void setListener() {
		btn_cancel.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		btn_select_anti.setOnClickListener(this);
		btn_select_all.setOnClickListener(this);
	}

	public void set(int ways, List<String> listgamea) {
		this.ways = ways;
		setCheck.clear();
		if (listgamea.size() == 0) {
			for (int i = 0; i < list_all.size(); i++) {
				setCheck.add(i);
			}
		}
		for (int i = 0; i < listgamea.size(); i++) {
			for (int j = 0; j < list_all.size(); j++) {
				if (listgamea.get(i).equals(list_all.get(j)))
					setCheck.add(j);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			if (setCheck.size() == 0) {
				MyToast.getToast(context, "请至少选择一个赛事");
				return;
			}
			listGame.clear();
			for (Integer i : setCheck) {
				listGame.add(list_all.get(i));
			}
			activity.dialogCallback(ways, listGame, 100);
			oldType = type;
			this.dismiss();
			break;
		case R.id.btn_cancel:
			this.dismiss();
			type = oldType;
			gAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_select_all:
			selectAll();
			break;
		case R.id.btn_select_anti:
			invertSelect();
			break;
		}
	}

	/** 全选 */
	private void selectAll() {
		for (int i = 0; i < list_all.size(); i++) {
			setCheck.add(i);
		}
		gAdapter.notifyDataSetChanged();
	}

	/** 反选 */
	private void invertSelect() {
		for (int i = 0; i < list_all.size(); i++) {
			if (setCheck.contains(i)) {
				setCheck.remove(i);
			} else {
				setCheck.add(i);
			}
		}
		gAdapter.notifyDataSetChanged();
	}

	/** 适配器 */
	class GridAdapter extends BaseAdapter {
		private Context mContext;

		/** 所选集合下标 **/

		private GridAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return list_all.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_all.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			final ViewHolder holder;
			if (null == contentView) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				contentView = inflater.inflate(R.layout.item_pop_screen_gv,
						null);
				holder.tv_dm_name = (TextView) contentView
						.findViewById(R.id.tv_dm_name);
				contentView.setTag(holder);
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			holder.tv_dm_name.setText(list_all.get(position));
			holder.tv_dm_name
					.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
			for (Integer aSetCheck : setCheck) {
				if (aSetCheck == position) {
					holder.tv_dm_name
							.setBackgroundResource(R.drawable.btn_playtype_select);// 设置背景
				}
			}
			holder.tv_dm_name.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Iterator<Integer> it2 = setCheck.iterator();
					int a = 0;
					while (it2.hasNext()) {
						if (it2.next() == position) {
							setCheck.remove(position);// 设置背景
							holder.tv_dm_name
									.setBackgroundResource(R.drawable.select_jczq_tv_bolder_white);
							a += 1;
							return;
						}
					}
					if (a == 0) {
						setCheck.add(position);
						holder.tv_dm_name
								.setBackgroundResource(R.drawable.btn_playtype_select);
					}
					notifyDataSetChanged();
				}
			});
			return contentView;
		}
	}

	class ViewHolder {
		private TextView tv_dm_name;
	}
}
