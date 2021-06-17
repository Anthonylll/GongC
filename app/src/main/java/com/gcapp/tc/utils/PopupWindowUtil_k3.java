package com.gcapp.tc.utils;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.R;

import java.util.List;
import java.util.Map;

/**
 * 快三的玩法弹出框
 * 
 * @author Kinwee 修改日期2014-12-12
 * 
 */
public class PopupWindowUtil_k3 {
	private final static String TAG = "PopupWindowUtil_k3";
	private PopupWindow popWindow;// 弹出窗口
	private Activity activity;// 依赖的activity
	private ListView playType_list;
	private MyGridViewAdapter_k3 gvAdapter_k3;
	private MyListViewAdapter listAdapter;
	private Map<Integer, Map<Integer, String>> data;
	private View relyView;// 依赖的控件
	private int selectParentIndex;// 父id
	private int selectItemIndex;// 子id
	private LayoutInflater inflact;
	private float gv_item_wid;
	private int size;
	private int nunCuloms;
	public static int mark = 1;
	private List<String> listjiangjin;// 快三的玩法选号的adapter数据
	private List<String> listimage;

	public PopupWindowUtil_k3(Activity activity,
			Map<Integer, Map<Integer, String>> data, View relyView,
			List<String> list_jiangjin, List<String> list_image) {
		this.activity = activity;
		this.data = data;
		this.relyView = relyView;
		this.listjiangjin = list_jiangjin;
		this.listimage = list_image;
	}

	/** 创建popWindow */
	public void createPopWindow() {
		inflact = LayoutInflater.from(activity);
		View view = inflact.inflate(R.layout.layout_popwindow, null);
		playType_list = (ListView) view.findViewById(R.id.lv_pop);
		listAdapter = new MyListViewAdapter();
		playType_list.setAdapter(listAdapter);
		playType_list.setSelection(selectParentIndex);
		size = data.get(0).size();
		nunCuloms = size;
		if (size > 3) {
			nunCuloms = 3;
		}
		popWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // 设置PopupWindow可触摸
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOutsideTouchable(true); // 设置PopupWindow外部区域是否可触摸
		popWindow.showAsDropDown(relyView, 0, 0);
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				if (null != listener) {
					listener.getIndex(selectParentIndex, selectItemIndex);
				}
			}
		});
		// 设置popwindow的消失事件
		// 设置之后点击返回键 popwindow 会消失
		// popWindow.setBackgroundDrawable(new BitmapDrawable());
		// popWindow.setFocusable(true);
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		OnKeyListener listener = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					popWindow.dismiss();
					break;
				}
				return true;
			}
		};

		// 监听点击事件，点击其他位置，popupwindow小窗口消失
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popWindow.dismiss();
				return true;
			}
		});
	}

	public void setSelectIndex(int selectParentIndex, int selectItemIndex) {
		this.selectParentIndex = selectParentIndex;
		this.selectItemIndex = selectItemIndex;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.keySet().size();
		}

		@Override
		public Object getItem(int posotion) {
			return data.get(posotion);
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
				contentView = inflact.inflate(R.layout.item_pop_lv, null);
				holder.tv_playType = (TextView) contentView
						.findViewById(R.id.tv_playType);
				holder.gv_playType = (MyGridView) contentView
						.findViewById(R.id.gv_playType);
				contentView.setTag(holder);
			} else {
				holder = (MyListViewHolder) contentView.getTag();
			}
			if (1 == data.keySet().size()) {
				holder.tv_playType.setVisibility(View.GONE);// 设置不可见
			} else if (1 < data.keySet().size()) {
				holder.tv_playType.setVisibility(View.VISIBLE);// 设置可见
				if (0 == position) {
					holder.tv_playType.setText("普通投注");
					mark = 1;
				} else if (1 == position) {
					holder.tv_playType.setText("胆拖投注");
					mark = 2;
				}
			}
			gvAdapter_k3 = new MyGridViewAdapter_k3(position);
			int size = data.get(0).size();
			holder.gv_playType.setNumColumns(3);
			holder.gv_playType.setAdapter(gvAdapter_k3);
			return contentView;
		}
	}

	class MyGridViewAdapter_k3 extends BaseAdapter {
		private int pIndex;
		private int flag = 0;

		public MyGridViewAdapter_k3(int pIndex) {
			this.pIndex = pIndex;
		}

		@Override
		public int getCount() {
			int size = data.get(pIndex).size();
			if (size > 3) {
				if (size % 3 == 1) {
					flag = 1;
					return size + 2;
				} else if (size % 3 == 2) {
					flag = 2;
					return size + 1;
				}
			}
			return size;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int position, View contentView, ViewGroup arg2) {
			MyGridViewHolder_k3 holder;
			if (null == contentView) {
				holder = new MyGridViewHolder_k3();
				contentView = inflact.inflate(R.layout.item_pop_lv_gv_k3, null);
				holder.ll_playType = (LinearLayout) contentView
						.findViewById(R.id.ll_playType);
				holder.gv_tv_playType = (TextView) contentView
						.findViewById(R.id.gv_tv_playType);

				holder.gv_tv_jiangjin = (TextView) contentView
						.findViewById(R.id.gv_tv_jiangjin);

				holder.tv_jiahao = (TextView) contentView
						.findViewById(R.id.tv_jiahao);
				holder.tv_jiahao2 = (TextView) contentView
						.findViewById(R.id.tv_jiahao2);

				holder.gv_ll_image = (LinearLayout) contentView
						.findViewById(R.id.gv_ll_image);
				holder.gv_tv_image1 = (TextView) contentView
						.findViewById(R.id.gv_tv_image1);
				holder.gv_tv_image2 = (TextView) contentView
						.findViewById(R.id.gv_tv_image2);
				holder.gv_tv_image3 = (TextView) contentView
						.findViewById(R.id.gv_tv_image3);
				contentView.setTag(holder);
			} else {
				holder = (MyGridViewHolder_k3) contentView.getTag();
			}

			holder.ll_playType.setBackgroundResource(R.drawable.btn_playtype);
			if (selectItemIndex == position && selectParentIndex == pIndex) {
				holder.ll_playType
						.setBackgroundResource(R.drawable.btn_playtype_select);// 设置选中状态
			}

			holder.gv_tv_playType.setText(data.get(pIndex).get(position));
			if (mark == 2) {
				holder.gv_ll_image.setVisibility(View.GONE);
				holder.gv_tv_jiangjin.setVisibility(View.GONE);
				holder.gv_tv_playType.setVisibility(View.VISIBLE);
			} else {
				if (listjiangjin.size() > 0 && listimage.size() > 0 & mark == 1) {
					holder.gv_tv_playType.setVisibility(View.VISIBLE);
					holder.gv_ll_image.setVisibility(View.VISIBLE);
					holder.gv_tv_jiangjin.setText(listjiangjin.get(position));
					String imagenum = listimage.get(position).trim();
					if (null != imagenum && !imagenum.equals("")) {
						if (imagenum.contains("+")) {
							holder.gv_tv_image1
									.setBackgroundResource(R.drawable.dice1);
							holder.gv_tv_image2
									.setBackgroundResource(R.drawable.dice2);
							holder.gv_tv_image3
									.setBackgroundResource(R.drawable.dice3);
							holder.tv_jiahao.setText("+");
							holder.tv_jiahao2.setText("+");
							holder.tv_jiahao.setVisibility(View.VISIBLE);
							holder.tv_jiahao2.setVisibility(View.VISIBLE);
						} else {
							if (imagenum.substring(0, 1).equals("1")) {
								holder.gv_tv_image1
										.setBackgroundResource(R.drawable.dice1);
							} else if (imagenum.substring(0, 1).equals("2")) {
								holder.gv_tv_image1
										.setBackgroundResource(R.drawable.dice2);
							} else if (imagenum.substring(0, 1).equals("3")) {
								holder.gv_tv_image1
										.setBackgroundResource(R.drawable.dice3);
							} else if (imagenum.substring(0, 1).equals("4")) {
								holder.gv_tv_image1
										.setBackgroundResource(R.drawable.dice4);
							} else if (imagenum.substring(0, 1).equals("5")) {
								holder.gv_tv_image1
										.setBackgroundResource(R.drawable.dice5);
							} else if (imagenum.substring(0, 1).equals("6")) {
								holder.gv_tv_image1
										.setBackgroundResource(R.drawable.dice6);
							}

							if (imagenum.substring(1, 2).equals("1")) {
								holder.gv_tv_image2
										.setBackgroundResource(R.drawable.dice1);
							} else if (imagenum.substring(1, 2).equals("2")) {
								holder.gv_tv_image2
										.setBackgroundResource(R.drawable.dice2);
							} else if (imagenum.substring(1, 2).equals("3")) {
								holder.gv_tv_image2
										.setBackgroundResource(R.drawable.dice3);
							} else if (imagenum.substring(1, 2).equals("4")) {
								holder.gv_tv_image2
										.setBackgroundResource(R.drawable.dice4);
							} else if (imagenum.substring(1, 2).equals("5")) {
								holder.gv_tv_image2
										.setBackgroundResource(R.drawable.dice5);
							} else if (imagenum.substring(1, 2).equals("6")) {
								holder.gv_tv_image2
										.setBackgroundResource(R.drawable.dice6);
							}
							if (imagenum.length() < 3) {
								holder.gv_tv_image3.setVisibility(View.GONE);
							} else {
								holder.gv_tv_image3.setVisibility(View.VISIBLE);

								if (imagenum.substring(2, 3).equals("1")) {
									holder.gv_tv_image3
											.setBackgroundResource(R.drawable.dice1);
								} else if (imagenum.substring(2, 3).equals("2")) {
									holder.gv_tv_image3
											.setBackgroundResource(R.drawable.dice2);
								} else if (imagenum.substring(2, 3).equals("3")) {
									holder.gv_tv_image3
											.setBackgroundResource(R.drawable.dice3);
								} else if (imagenum.substring(2, 3).equals("4")) {
									holder.gv_tv_image3
											.setBackgroundResource(R.drawable.dice4);
								} else if (imagenum.substring(2, 3).equals("5")) {
									holder.gv_tv_image3
											.setBackgroundResource(R.drawable.dice5);
								} else if (imagenum.substring(2, 3).equals("6")) {
									holder.gv_tv_image3
											.setBackgroundResource(R.drawable.dice6);
								}
							}
							holder.tv_jiahao.setVisibility(View.GONE);
							holder.tv_jiahao2.setVisibility(View.GONE);
						}
					} else {

					}
				} else {
					holder.gv_tv_playType.setVisibility(View.GONE);
					holder.gv_ll_image.setVisibility(View.GONE);
				}

			}
			holder.ll_playType.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					switch (flag) {
					case 0:
						selectParentIndex = pIndex;
						selectItemIndex = position;
						break;
					case 1:
					case 2:
						if (position < data.get(pIndex).size()) {
							selectParentIndex = pIndex;
							selectItemIndex = position;
						}
						break;
					}
					popWindow.dismiss();
				}
			});
			return contentView;
		}
	}

	static class MyListViewHolder {
		private TextView tv_playType;
		private MyGridView gv_playType;
	}

	static class MyGridViewHolder_k3 {
		private LinearLayout ll_playType, gv_ll_image;
		private TextView gv_tv_playType, tv_jiahao, tv_jiahao2;
		private TextView gv_tv_jiangjin, gv_tv_image1, gv_tv_image2,
				gv_tv_image3;
	}

	private OnSelectedListener listener;

	public void setOnSelectedListener(OnSelectedListener listener) {
		this.listener = listener;
	}

	public interface OnSelectedListener {
		void getIndex(int parentIndex, int itemIndex);
	}
}
