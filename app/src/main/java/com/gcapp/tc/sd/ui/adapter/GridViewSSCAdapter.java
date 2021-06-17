package com.gcapp.tc.sd.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 功能：时时彩的选球adapter
 * 
 * @author lenovo
 * 
 */
public class GridViewSSCAdapter extends BaseAdapter {
	// 上下文本
	private Context context;
	// 装彩票的集合
	private String[] Numbers;
	// 存放红球的 集合
	private HashSet<String> oneSet = new HashSet<String>();
	private List<String> yilou = new ArrayList<String>();
	private HashSet<Integer> indexSet = new HashSet<Integer>();
	private boolean type; // 前面是否加 0
	private boolean type2; // 数字从0 开始 还是从 1开始 true 为 1
	private boolean gone;

	/** 构造方法 实现初始化 */
	public GridViewSSCAdapter(Context context, String[] numbers, boolean _type,
			boolean gone, List<String> yiloulist) {
		this.context = context;
		this.Numbers = numbers;
		this.type = _type;
		this.type2 = true;
		this.gone = gone;
		this.yilou = yiloulist;

	}

	/** 构造方法 实现初始化 */
	public GridViewSSCAdapter(Context context, String[] numbers, boolean _type,
			boolean type2, boolean gone, List<String> yiloulist) {
		this.context = context;
		this.Numbers = numbers;
		this.type = _type;
		this.type2 = type2;
		this.gone = gone;
		this.yilou = yiloulist;

	}

	public void setNumbers(String[] numbers) {
		this.Numbers = numbers;
	}

	

	public HashSet<Integer> getIndexSet() {
		return indexSet;
	}

	public int getIndexSetSize() {
		if (null == indexSet)
			return 0;
		return indexSet.size();
	}
	
	public HashSet<String> getOneSet() {
		return oneSet;
	}

	public int getOneSetSize() {
		if (null == oneSet)
			return 0;
		return oneSet.size();
	}

	public void addOne(String num) {
		oneSet.add(num);
	}
	public void removeOne(String num) {
		oneSet.remove(num);
	}
	
	public void addIndex(int num) {
		indexSet.add(num);
	}

	public void removeIndex(int i) {
		indexSet.remove(i);
	}

	public void setVisibles(boolean visibleGone) {
		gone = visibleGone;
	}

	public void setOneSet(List<String> list) {
		if (null == list)
			return;
		oneSet.clear();
		for (String s : list) {
			oneSet.add(s);
			Log.i("x", "增加的值" + s);
		}
	}

	public void setIndexSet(ArrayList<String> list) {
		if (null == list)
			return;
		indexSet.clear();
		for (String s : list) {
			indexSet.add(Integer.parseInt(s));
		}
	}

	public void clear() {
		if (null != oneSet) {
			oneSet.clear();
		}
		if (null != indexSet) {
			indexSet.clear();
		}
	}

	@Override
	public int getCount() {
		return Numbers.length;
	}

	@Override
	public Object getItem(int arg0) {
		return Numbers[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		// 判断View是否为空
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			// 得到布局文件
			view = inflater.inflate(R.layout.gridview_items, null);

			// 得到控件
			holder.btn = (TextView) view.findViewById(R.id.btn_showNum);
			holder.tv_yilou = (TextView) view.findViewById(R.id.tv_yilou);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (gone) {
			holder.tv_yilou.setVisibility(View.VISIBLE);
			if (yilou.size() > 0 && yilou.size() > position) {
				holder.tv_yilou.setText(yilou.get(position));
			} else {
				holder.tv_yilou.setVisibility(View.GONE);
			}
		} else {
			holder.tv_yilou.setVisibility(View.GONE);
		}

		if (type) {
			// 给控件赋值
			if (Numbers[position].length() < 2) {
				holder.btn.setText("0" + Numbers[position]);
			} else {
				holder.btn.setText(Numbers[position]);
			}
		} else {
			holder.btn.setText(Numbers[position]);
		}

		// 设置字体显示颜色 和背景图片
		holder.btn.setTextColor(context.getResources().getColor(
				R.color.main_red));
		holder.btn.setBackgroundResource(R.drawable.icon_ball_red_unselected);

		String str = "";
		if (type) {
			str = (position + 1) + "";
			if (position < 9) {
				str = "0" + (position + 1);
			}
		} else {
			if (type2)
				str = (position + 1) + "";
			else
				str = position + "";
		}

		// 看是否选中
		if (oneSet.contains(str)) {
			holder.btn.setTextColor(Color.WHITE);
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
		}

		if (indexSet.contains(position)) {
			holder.btn.setTextColor(Color.WHITE);
			holder.btn.setBackgroundResource(R.drawable.icon_ball_red_selected);
		}
		return view;
	}

	/** 静态类 */
	static class ViewHolder {
		TextView btn;
		TextView tv_yilou;
	}
}
