package com.gcapp.tc.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gcapp.tc.wheel.widget.TabPageIndicator;
import com.gcapp.tc.wheel.widget.TabPageIndicator.OnTabReselectedListener;
import com.gcapp.tc.R;

/**
 * 功能：过关方式弹出框
 * 
 * @author Kinwee 修改日期2014-12-12
 * 
 */
public class SelectPassTypePopupWindow {
	private final static String TAG = "SelectPassTypePopupWindow";
	private PopupWindow popWindow;// 弹出窗口
	private Context context;
	private GridView gvPassOne;// 多串一
	private GridView gvPassMore;// 多串多
	private MyGridViewAdapterPassOne adapterPassOne;
	private MyGridViewAdapterPassMore adapterPassMore;
	private ArrayList<String> passOne = new ArrayList<String>();// 传入进来的多串一数据
	private ArrayList<String> passMore = new ArrayList<String>();// 传入进来的多串多数据
	private ArrayList<String> selectPassType = new ArrayList<String>();// 最终选择的过关方式
	// 选多串一和多串多控件
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private List<View> listViews;
	private MyPagerAdapter adapter;
	private static String[] CONTENT = new String[] { "自由过关", "多串过关" };
	private int viewPagerCurrentIndex = 0;// viewpager的当前页
	private View relyView;// 依赖的view
	public HashMap<String, String> passType = new HashMap<String, String>();
	private Button btn_cancel;
	private Button btn_confirm;
	private int countDan;// 胆的个数
	private int dtCount;// 获取已选择的比赛场次

	public SelectPassTypePopupWindow(Context context, int countDan,
			int dtCount, int viewPagerCurrentIndex, View relyView) {

		this.context = context;
		this.viewPagerCurrentIndex = viewPagerCurrentIndex;
		this.relyView = relyView;
		this.countDan = countDan;
		this.dtCount = dtCount;
		passType = getPASSTYPE_MAP();
		init();
	}

	/**
	 * 设置选择了的过关方式
	 * 
	 * @param selectPassType
	 */
	public void setSelectPassType(ArrayList<String> selectPassType) {
		System.out.println("=popupwindow=======" + selectPassType.size());
		this.selectPassType = selectPassType;

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

	/** 创建popWindow */
	public void createPopWindow() {
		final Context contextThemeWrapper = new ContextThemeWrapper(context,
				R.style.StyledIndicators_red);

		LayoutInflater localInflater = LayoutInflater.from(context)
				.cloneInContext(contextThemeWrapper);
		View parent = localInflater.inflate(R.layout.pop_select_passtype, null);
		btn_cancel = (Button) parent.findViewById(R.id.btn_cancel);
		btn_confirm = (Button) parent.findViewById(R.id.btn_confirm);
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 取消
				if (null != SelectPassTypePopupWindow.this.listener) {
					listener.getResult(0, selectPassType, viewPagerCurrentIndex);
				}
				popWindow.dismiss();
			}
		});
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 确定
				if (null != SelectPassTypePopupWindow.this.listener) {
					listener.getResult(1, selectPassType, viewPagerCurrentIndex);
				}
				popWindow.dismiss();
			}
		});
		viewPager = (ViewPager) parent.findViewById(R.id.pop_vp);
		initViewPager();
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		indicator = (TabPageIndicator) parent.findViewById(R.id.indicator);
		indicator.setViewPager(viewPager, viewPagerCurrentIndex);
		indicator.setOnTabReselectedListener(new OnTabReselectedListener() {
			@Override
			public void onTabReselected(int position, boolean sort_state) {
			}
		});
		indicator
				.setOnMyPagerChangeListener(new TabPageIndicator.OnMyPagerChangeListener() {

					@Override
					public void pagerChanged() {
						viewPagerCurrentIndex = viewPager.getCurrentItem();
						selectPassType.clear();// 清空选择的过关方式
						adapterPassOne.notifyDataSetChanged();
						adapterPassMore.notifyDataSetChanged();
					}
				});
		popWindow = new PopupWindow(parent, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(false); // 设置PopupWindow外部区域是否可触摸
		// popWindow.showAtLocation(relyView, Gravity.CENTER, 0, 0);// 设置显示的位置

		int[] location = new int[2];
		relyView.getLocationOnScreen(location);
		popWindow.showAtLocation(relyView, Gravity.NO_GRAVITY, location[0],
				location[1] - popWindow.getHeight());

	}

	private void initViewPager() {
		listViews = new ArrayList<View>();
		// 过关
		LayoutInflater inflate = LayoutInflater.from(context);
		View view_passone = inflate
				.inflate(R.layout.pop_passtype_vp_item, null);

		view_passone.setTag("passone");
		listViews.add(view_passone);

		View view_passmore = inflate.inflate(R.layout.pop_passtype_vp_item,
				null);

		view_passmore.setTag("passmore");
		listViews.add(view_passmore);
	}

	class MyGridViewAdapterPassOne extends BaseAdapter {
		@Override
		public int getCount() {
			return passOne.size();
		}

		@Override
		public Object getItem(int position) {
			return passOne.get(position);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
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
			holder.tv_passone.setText(passType.get(passOne.get(position)));
			setBackGround(holder.iv_passone, R.drawable.left_gou_unselected,
					holder.tv_passone, R.drawable.right_unselected);
			if (selectPassType.contains(passOne.get(position))) {
				setBackGround(holder.iv_passone, R.drawable.left_gou_selected,
						holder.tv_passone, R.drawable.right_selected);
			}
			holder.layout_passone
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (selectPassType.contains(passOne.get(position))) {// 如果包含
								selectPassType.remove(passOne.get(position));// 移除
							} else {// 反之添加
								selectPassType.add(passOne.get(position));
							}
							notifyDataSetChanged();
						}
					});
			return contentView;
		}
	}

	/**
	 * 设置背景
	 * 
	 * @param iv_pass
	 *            左边的imageview
	 * @param iv_bg
	 *            图片背景
	 * @param tv_pass
	 *            右边的textview
	 * @param tv_bg
	 *            textview背景
	 */
	public void setBackGround(ImageView iv_pass, int iv_bg, TextView tv_pass,
			int tv_bg) {
		iv_pass.setBackgroundResource(iv_bg);
		tv_pass.setBackgroundResource(tv_bg);
	}

	/**
	 * 获取过关方式map
	 * 
	 * @return
	 */
	public HashMap<String, String> getPASSTYPE_MAP() {
		HashMap<String, String> passType = new HashMap<String, String>();
		String str_show = "单关,2串1,3串1,3串3,3串4,4串1,4串4,4串5,4串6,4串11,5串1,5串5,5串6,5串10,5串16,5串20,5串26,6串1,6串6,6串7,6串15,6串20,6串22,6串35,6串42,6串50,6串57,7串1,7串7,7串8,7串21,7串35,7串120,8串1,8串8,8串9,8串28,8串56,8串70,8串247";
		String str_passtype = "A0,AA,AB,AC,AD,AE,AF,AG,AH,AI,AJ,AK,AL,AM,AN,AO,AP,AQ,AR,AS,AT,AU,AV,AW,AX,AY,AZ,BA,BB,BC,BD,BE,BF,BG,BH,BI,BJ,BK,BL,BM";
		String[] show = str_show.split(",");
		String[] passtype = str_passtype.split(",");
		for (int i = 0; i < passtype.length; i++) {
			passType.put(passtype[i], show[i]);
		}
		return passType;
	}

	class MyGridViewAdapterPassMore extends BaseAdapter {
		@Override
		public int getCount() {
			return passMore.size();
		}

		@Override
		public Object getItem(int position) {
			return passMore.get(position);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			final HolderPassMore holder;
			// TODO
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
			holder.tv_passmore.setText(passType.get(passMore.get(position)));
			setBackGround(holder.iv_passmore, R.drawable.left_yuan_unselected,
					holder.tv_passmore, R.drawable.right_unselected);
			if (selectPassType.contains(passMore.get(position))) {
				setBackGround(holder.iv_passmore,
						R.drawable.left_yuan_selected, holder.tv_passmore,
						R.drawable.right_selected);
			}
			holder.layout_passmore
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (selectPassType.contains(passMore.get(position))) {// 如果包含
								selectPassType.remove(passMore.get(position));// 移除
							} else {// 反之添加
								selectPassType.add(passMore.get(position));
							}
							notifyDataSetChanged();
						}
					});
			return contentView;
		}
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

	private DialogResultListener listener;

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

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return listViews.size();
		}

		/**
		 * 从指定的position创建page
		 * 
		 * @param collection
		 *            ViewPager容器
		 * @param position
		 * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			((ViewPager) collection).addView(listViews.get(position), 0);

			View v = listViews.get(position);
			if ("passone".equals((String) v.getTag())) {// 多串一
				gvPassOne = (GridView) v.findViewById(R.id.pop_vp_gv);
				if (null == adapterPassOne) {
					adapterPassOne = new MyGridViewAdapterPassOne();
				}
				gvPassOne.setAdapter(adapterPassOne);
			} else if ("passmore".equals((String) v.getTag())) {// 多串多
				gvPassMore = (GridView) v.findViewById(R.id.pop_vp_gv);
				if (null == adapterPassMore) {
					adapterPassMore = new MyGridViewAdapterPassMore();
				}
				gvPassMore.setAdapter(adapterPassMore);
			}
			return v;
		}

		/**
		 * 55. * <span style='font-family:
		 * "Droid Sans";'>从指定的position销毁page</span> 56. * 57. * 58. *<span
		 * style='font-family: "Droid Sans";'>参数同上</span> 59.
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView(listViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}
	}
}
