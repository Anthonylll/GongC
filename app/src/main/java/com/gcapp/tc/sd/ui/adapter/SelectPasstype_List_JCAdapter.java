package com.gcapp.tc.sd.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.R;

public class SelectPasstype_List_JCAdapter extends BaseAdapter {
	private Context context;
	private int countDan;// 胆的个数
	private int viewPagerCurrentIndex = 0;// viewpager的当前页
	private int dtCount;// 获取已选择的比赛场次
	private final static String TAG = "SelectPasstype_JCAdapter";
	public static ArrayList<String> passOne = new ArrayList<String>();// 传入进来的多串一数据
	public static ArrayList<String> passMore = new ArrayList<String>();// 传入进来的多串多数据
	public static LinkedHashMap<String, String> passType = new LinkedHashMap<String, String>();
	public static ArrayList<String> selectPassType = new ArrayList<String>();// 最终选择的过关方式

	private SelectPasstype_Gridview_JCAdapter gvAdapter;

	public SelectPasstype_List_JCAdapter(Context context, int countDan,
			int dtCount, int viewPagerCurrentIndex) {
		this.viewPagerCurrentIndex = viewPagerCurrentIndex;
		this.context = context;
		this.countDan = countDan;
		this.dtCount = dtCount;
		passType = getPASSTYPE_MAP();
		init();
	}

	/**
	 * 获取过关方式map
	 * 
	 * @return
	 */
	public LinkedHashMap<String, String> getPASSTYPE_MAP() {
		LinkedHashMap<String, String> passType = new LinkedHashMap<String, String>();
		String str_show = "单关,2串1,3串1,3串3,3串4,4串1,4串4,4串5,4串6,4串11,5串1,5串5,5串6,5串10,5串16,5串20,5串26,6串1,6串6,6串7,6串15,6串20,6串22,6串35,6串42,6串50,6串57,7串1,7串7,7串8,7串21,7串35,7串120,8串1,8串8,8串9,8串28,8串56,8串70,8串247";
		String str_passtype = "A0,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,BA,BB,BC,BD,BE,BF,BG,BH,BI,BJ,BK,BL,BM";
		String[] show = str_show.split(",");
		String[] passtype = str_passtype.split(",");
		for (int i = 0; i < passtype.length; i++) {
			passType.put(passtype[i], show[i]);
		}
		return passType;
	}

	public void init() {
		String str_show = "2串1,3串1,3串3,3串4,4串1,4串4,4串5,4串6,4串11,5串1,5串5,5串6,5串10,5串16,5串20,5串26,6串1,6串6,6串7,6串15,6串20,6串22,6串35,6串42,6串50,6串57,7串1,7串7,7串8,7串21,7串35,7串120,8串1,8串8,8串9,8串28,8串56,8串70,8串247";
		String[] show = str_show.split(",");
		String str_passtype = "AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,BA,BB,BC,BD,BE,BF,BG,BH,BI,BJ,BK,BL,BM";
		String[] passtype = str_passtype.split(",");
		// 筛选过关方式
		ArrayList<String> array = new ArrayList<String>();
		Collections.addAll(array, show);
		passOne = new ArrayList<String>();
		passMore = new ArrayList<String>();
		Log.i(TAG, "胆的个数-----" + countDan);
		if (0 == countDan || 1 == countDan) {// 无胆,一个胆
			for (int i = 0; i < array.size(); i++) {// 遍历array将符合条件的过关方式筛选出来
				for (int j = 2; j <= dtCount; j++) {// 筛选2串到dtCount串的过关方式
					String pass = array.get(i);
					String sb1 = pass.substring(0, 1);// 截取第一个字符
					if (sb1.equals("" + j)) {
						String sb2 = pass.substring(2, 3);
						if (3 == pass.length() && sb2.equals("1")) {// 包含1,且长度为3则为多串1
							passOne.add(passtype[i]);// 将相对应的过关方式代号加入集合
						} else {// 多串多
							passMore.add(passtype[i]);// 将相对应的过关方式代号加入集合
						}
					}
				}
			}
		} else {// 两个胆以及以上
			for (int i = 0; i < array.size(); i++) {// 遍历array将符合条件的过关方式筛选出来
				for (int j = countDan + 1; j <= dtCount; j++) {// 筛选countDan+1串到dtCount串的过关方式
					String pass = array.get(i);
					String sb1 = pass.substring(0, 1);// 截取第一个字符
					if (sb1.equals("" + j)) {
						String sb2 = pass.substring(2, 3);
						if (3 == pass.length() && sb2.equals("1")) {// 包含1,且长度为3则为多串1
							passOne.add(passtype[i]);// 将相对应的过关方式代号加入集合
						} else {// 多串多
							passMore.add(passtype[i]);// 将相对应的过关方式代号加入集合
						}
					}
				}
			}
		}
	}

	/**
	 * 设置选择了的过关方式
	 * 
	 * @param selectPassType
	 */
	public void setSelectPassType(ArrayList<String> selectPassType) {
		this.selectPassType = selectPassType;
	}

	@Override
	public int getCount() {
		if (passMore.size() > 0) {
			// return 2;
			return 1;
		} else
			return 1;
	}

	@Override
	public Object getItem(int posotion) {
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
		gvAdapter = new SelectPasstype_Gridview_JCAdapter(context, position);
		holder.gv_playType.setNumColumns(3);
		holder.gv_playType.setAdapter(gvAdapter);
		return contentView;
	}

	static class MyListViewHolder {
		private TextView tv_playType;
		private MyGridView gv_playType;
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
}
