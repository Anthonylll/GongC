package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 功能：北单过关方式的适配器
 * 
 * @author lenovo
 * 
 */
public class SelectPasstype_List_BJDCAdapter extends BaseAdapter {
	private Context context;
	public static ArrayList<String> passOne = new ArrayList<String>();// 传入进来的多串一数据
	public static ArrayList<String> passMore = new ArrayList<String>();// 传入进来的多串多数据
	public static ArrayList<String> selectPassType = new ArrayList<String>();// 最终选择的过关方式
	public static HashMap<String, String> passType = new HashMap<String, String>();
	private int countDan;// 胆的个数
	private int dtCount;// 获取已选择的比赛场次
	private int viewPagerCurrentIndex = 0;// viewpager的当前页
	private SelectPasstype_Gridview_BJDCAdapter gvAdapter;
	private int playType = 0;

	public SelectPasstype_List_BJDCAdapter(Context context, int countDan,
			int dtCount, int viewPagerCurrentIndex, int playType) {
		this.viewPagerCurrentIndex = viewPagerCurrentIndex;
		this.context = context;
		this.countDan = countDan;
		this.dtCount = dtCount;
		passType = getPASSTYPE_MAP();
		this.playType = playType;
		init();
	}

	/**
	 * 获取过关方式map
	 * 
	 * @return
	 */
	public HashMap<String, String> getPASSTYPE_MAP() {
		HashMap<String, String> passType = new HashMap<String, String>();
		String str_show = "单关,2串1,2串3,3串1,3串4,3串7,4串1,4串5,4串11,4串15,5串1,5串6,5串16,5串26,5串31,6串1,6串7,6串22,6串42,6串57,6串63,7串1,8串1,9串1,10串1,11串1,12串1,13串1,14串1,15串1";
		String str_passtype = "A0,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,BA,BB,BC";
		String[] show = str_show.split(",");
		String[] passtype = str_passtype.split(",");
		for (int i = 0; i < passtype.length; i++) {
			passType.put(passtype[i], show[i]);
		}
		return passType;
	}

	@Override
	public int getCount() {
		if (passMore.size() > 0) {
			return 2;
		} else
			return 1;
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
	public View getView(int position, View contentView, ViewGroup arg2) {
		MyListViewHolder holder;
		if (null == contentView) {
			holder = new MyListViewHolder();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.item_pop_lv, null);
			holder.tv_playType = (TextView) contentView
					.findViewById(R.id.tv_playType);
			holder.gv_playType = (MyGridView) contentView
					.findViewById(R.id.gv_playType);
			contentView.setTag(holder);
		} else {
			holder = (MyListViewHolder) contentView.getTag();
		}
		if (0 == position) {
			holder.tv_playType.setText("标准过关");
		} else if (1 == position) {
			holder.tv_playType.setText("组合过关");
		}
		gvAdapter = new SelectPasstype_Gridview_BJDCAdapter(context, position);
		holder.gv_playType.setNumColumns(3);
		holder.gv_playType.setAdapter(gvAdapter);
		return contentView;
	}

	public static DialogResultListener listener;

	public void setDialogResultListener(DialogResultListener listener) {
		this.listener = listener;
	}

	public interface DialogResultListener {
		/**
		 * * 获取结果的方法
		 * 
		 * @param resultCode
		 *            0.取消 1.确定
		 * @param selectResult
		 *            选择的结果集合
		 * @param type
		 *            0.多串1 1.多串多
		 */
		void getResult(int resultCode, ArrayList<String> selectResult, int type);
	}

	static class MyListViewHolder {
		private TextView tv_playType;
		private MyGridView gv_playType;
	}

	/**
	 * 设置选择了的过关方式
	 * 
	 * @param selectPassType
	 */
	public void setSelectPassType(ArrayList<String> selectPassType) {
		this.selectPassType = selectPassType;
	}

	public void init() {
		String str_show = "单关,2串1,2串3,3串1,3串4,3串7,4串1,4串5,4串11,4串15,5串1,5串6,5串16,5串26,5串31,6串1,6串7,6串22,6串42,6串57,6串63,7串1,8串1,9串1,10串1,11串1,12串1,13串1,14串1,15串1";
		String str_passtype = "A0,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,BA,BB,BC";
		String[] show = str_show.split(",");
		String[] passtype = str_passtype.split(",");
		// 筛选过关方式
		ArrayList<String> array = new ArrayList<String>();
		for (int i = 0; i < show.length; i++) {
			array.add(show[i]);
		}
		passOne = new ArrayList<String>();
		passMore = new ArrayList<String>();
		int max_ = 0; // 根据玩法不同最大串关数
		switch (playType) {
		case 1:
			max_ = 15;
			break;
		case 2:
		case 3:
		case 5:
			max_ = 6;
			break;
		case 4:
			max_ = 3;
			break;
		default:
			break;
		}
		if (0 == countDan || 1 == countDan) {// 无胆,一个胆
			if (0 == countDan && dtCount != 0) {
				passOne.add("A0");
			}
			for (int i = 1; i < array.size(); i++) {// 遍历array将符合条件的过关方式筛选出来
				for (int j = 2; j <= dtCount; j++) {// 筛选2串到dtCount串的过关方式
					String pass = array.get(i);
					int a = pass.indexOf("串");
					String sb1 = pass.substring(0, a);// 截取第一个字符
					if (j <= max_ && sb1.equals("" + j)) {
						String sb2 = pass.substring(a + 1, pass.length());
						if (sb2.equals("1")) {// 包含1,且长度为3则为多串1
							passOne.add(passtype[i]);// 将相对应的过关方式代号加入集合
						} else {// 多串多
							passMore.add(passtype[i]);// 将相对应的过关方式代号加入集合
						}
					}
				}
			}

		} else {// 两个胆以及以上
			for (int i = 1; i < array.size(); i++) {// 遍历array将符合条件的过关方式筛选出来
				for (int j = countDan + 1; j <= dtCount; j++) {// 筛选countDan+1串到dtCount串的过关方式
					String pass = array.get(i);
					int a = pass.indexOf("串");
					String sb1 = pass.substring(0, a);// 截取第一个字符
					if (j <= max_ && sb1.equals("" + j)) {
						String sb2 = pass.substring(a + 1, pass.length());
						if (sb2.equals("1")) {// 包含1,且长度为3则为多串1
							passOne.add(passtype[i]);// 将相对应的过关方式代号加入集合
						} else {// 多串多
							passMore.add(passtype[i]);// 将相对应的过关方式代号加入集合
						}
					}
				}
			}
		}
	}
}
