package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.gcapp.tc.dataaccess.MyMessage;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;

/**
 * 消息的子项fragment
 */
public class AllMessageItemFragment extends Fragment {
	private static final String ARG_PARAM1 = "type";
	private String opt = "16"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	/**
	 * 2是个人信息 1 是系统信息
	 */
	private int type;
	private int pageSize = 20;
	private int pageIndex = 1;
	private List<MyMessage> listMessage = new ArrayList<MyMessage>();
	private OnFragmentInteractionListener mListener;

	private PullToRefreshListView all_message_item_pulllistview;
	private TextView all_message_item_hint;
	private MyAdapter myAdapter;
	public static MyMessage mes = null;
	private int isEnd = 0;
	private Context context;
	private static final String TAG = "AllMessageItemFragment";

	/**
	 * 构造方法
	 * 
	 * @param param1
	 * @return
	 */
	public static AllMessageItemFragment newInstance(int param1) {
		AllMessageItemFragment fragment = new AllMessageItemFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public AllMessageItemFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			type = getArguments().getInt(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();

		View view = inflater.inflate(R.layout.fragment_all_message_item,
				container, false);
		findView(view);
		init();
		setListener();
		return view;
	}

	/**
	 * 初始化自定义控件
	 * 
	 * @param view
	 */
	private void findView(View view) {
		all_message_item_pulllistview = (PullToRefreshListView) view
				.findViewById(R.id.all_message_item_pulllistview);
		all_message_item_hint = (TextView) view
				.findViewById(R.id.all_message_item_hint);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(getActivity());
		myAdapter = new MyAdapter(listMessage);
		all_message_item_pulllistview.setAdapter(myAdapter);
		getMessageInfo();
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		all_message_item_pulllistview
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						all_message_item_pulllistview.getLoadingLayoutProxy(
								true, false).setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
						if (NetWork.isConnect(getActivity())) {
							// if (myAsynTask != null) {
							// myAsynTask.cancel(true);
							// }
							listMessage.clear();
							pageIndex = 1;
							getMessageInfo();
							// myAsynTask = new MyAsynTask();
							// myAsynTask.execute();
						} else {
							MyToast.getToast(getActivity(), "网络连接异常，请检查网络")
									;
							all_message_item_pulllistview.onRefreshComplete();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载
						if (NetWork.isConnect(getActivity())) {
							// if (myAsynTask != null) {
							// myAsynTask.cancel(true);
							// }
							pageIndex++;
							getMessageInfo();
							// myAsynTask = new MyAsynTask();
							// myAsynTask.execute();
						} else {
							MyToast.getToast(getActivity(), "网络连接异常，请检查网络")
									;
							all_message_item_pulllistview.onRefreshComplete();
						}
					}
				});

		all_message_item_pulllistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						mes = myAdapter.listm.get(Math.max(0, position - 1));
						Intent intent = new Intent(getActivity(),
								MessageInfoActivity.class);

						startActivity(intent);
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 请求系统和推送消息
	 */
	public void getMessageInfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在请求...",1) {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "消息请求result====" + object);

				String error = json2Bean(type, object.toString());

				all_message_item_pulllistview.onRefreshComplete();
				dealResult(error);

				if (object.toString().equals("-500")) {
					MyToast.getToast(context, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getMessageInfo(type, pageIndex, pageSize, -1);
	}

	/**
	 * 解析JSON数据
	 * 
	 * @param type
	 *            ：类型
	 * @param result
	 *            ：请求结果
	 * @return
	 */
	private String json2Bean(int type, String result) {
		String error = "-1";
		switch (type) {
		case 1:// 推送消息
			try {
				JSONObject object = new JSONObject(result);
				String smsList = object.getString("info");
				JSONArray array = new JSONArray(smsList);

				isEnd = array.length();
				if (isEnd == 0) {
					error = "-3";
				}
				error = object.getString("error");
				MyMessage msg;
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					msg = new MyMessage();
					msg.setId(i);
					msg.setContent(item.getString("MessageContent"));
					msg.setTitle(item.getString("MessageTitle"));
					msg.setRead(true);
					msg.setRecordCount(0);
					msg.setSerialNumber(item.optString("SerialNumber"));
					msg.setTime(item.optString("DateTime"));

					for (MyMessage m : listMessage) {
						if (m.getId() == msg.getId()) {
							listMessage.remove(m);
							isEnd--;
						}
					}
					listMessage.add(0, msg);
				}
				// if (myAsynTask.isCancelled()) {
				// error = "-2";
				// }
			} catch (Exception e) {
				Log.i("x", "拿历史推送消息错误->" + e.getMessage());
			}
			break;

		case 2: // 系统消息
			try {
				JSONObject object = new JSONObject(result);
				String smsList = object.getString("stationSMSList");
				JSONArray array = new JSONArray(smsList);

				isEnd = array.length();
				if (isEnd == 0) {
					error = "-3";
				}
				error = object.getString("error");
				MyMessage msg;
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					msg = new MyMessage();
					msg.setId(item.optInt("Id"));
					msg.setContent(item.getString("content"));
					msg.setTitle(item.getString("title"));
					msg.setRead(item.optBoolean("isRead"));
					msg.setRecordCount(item.getLong("RecordCount"));
					msg.setSerialNumber(item.getString("SerialNumber"));
					msg.setTime(item.getString("dateTime"));

					for (MyMessage m : listMessage) {
						if (m.getId() == msg.getId()) {
							listMessage.remove(m);
							isEnd--;
						}
					}
					listMessage.add(msg);
				}
				// if (myAsynTask.isCancelled()) {
				// error = "-2";
				// }
			} catch (Exception e) {
				Log.i("x", "拿系统消息错误->" + e.getMessage());
			}
			break;
		}
		return error;
	}

	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	/**
	 * 处理请求结果
	 * 
	 * @param result
	 *            ：返回的参数
	 */
	private void dealResult(String result) {
		if (result == null)
			return;
		switch (Integer.valueOf(result)) {
		case -500:
			setStatus(false, "暂无消息", true);
			break;
		case -1: // 系统错误
			setStatus(false, "网络异常", true);
			break;
		case -2: // 取消
			setStatus(false, "已取消", true);
			break;
		case -3: // content字段为空
			setStatus(false, "暂无消息", true);
			break;
		case AppTools.ERROR_SUCCESS:

			myAdapter.setList(listMessage);
			myAdapter.notifyDataSetChanged();

			setStatus(true, "加载完毕", false);
			if (isEnd < pageSize && pageIndex != 1) {
				setStatus(false, "没有更多数据", true);
			}
			if (listMessage.size() == 0 && pageIndex == 1) {
				setStatus(false, "暂无消息", false);
			}
			break;

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(Uri uri);
	}

	/**
	 * 数据处理适配器
	 */
	class MyAdapter extends BaseAdapter {
		private List<MyMessage> listm;

		public MyAdapter(List<MyMessage> list) {
			setList(list);
		}

		public void clear() {
			this.listm.clear();
		}

		/**
		 * 给数组赋值 *
		 */
		public void setList(List<MyMessage> list) {
			this.listm = new ArrayList<MyMessage>();
			for (int i = 0; i < list.size(); i++) {
				this.listm.add(list.get(i));
			}
		}

		@Override
		public int getCount() {
			return listm.size();
		}

		@Override
		public Object getItem(int position) {
			return listm.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				// 得到布局文件
				convertView = inflater.inflate(R.layout.message_item, null);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (listm.get(position) == null)
				return convertView;

			holder.tv_content.getPaint().setFakeBoldText(false);
			holder.tv_title.getPaint().setFakeBoldText(false);
			holder.tv_time.getPaint().setFakeBoldText(false);

			if (!listm.get(position).isRead()) {
				holder.tv_content.getPaint().setFakeBoldText(true);
				holder.tv_title.getPaint().setFakeBoldText(true);
				holder.tv_time.getPaint().setFakeBoldText(true);
			}

			holder.tv_content.setText(listm.get(position).getContent());
			holder.tv_time.setText(listm.get(position).getTime());
			holder.tv_title.setText(listm.get(position).getTitle());
			return convertView;
		}
	}

	private class ViewHolder {
		TextView tv_content, tv_time, tv_title;
	}

	/**
	 * 改变数据获取之后，listview的状态
	 */

	/**
	 * 处理请求完成的结果
	 * 
	 * @param status
	 *            ：处理加载提示框的显示
	 * @param msg
	 *            ：是否请求完成
	 * @param toast
	 *            ：是否显示toast
	 */
	private void setStatus(boolean status, String msg, boolean toast) {
		if (msg.equals("-1")) {
			if (all_message_item_pulllistview != null
					&& all_message_item_hint != null
			// && myAsynTask != null
			) {
				// myAsynTask.cancel(true);
				all_message_item_pulllistview.onRefreshComplete();
				all_message_item_pulllistview
						.setMode(PullToRefreshBase.Mode.DISABLED);
				all_message_item_hint.setText("正在加载中..");
			}
			return;
		}
		if (status) {
			if (all_message_item_pulllistview != null)
				all_message_item_pulllistview
						.setMode(PullToRefreshBase.Mode.BOTH);

		} else {
			if (all_message_item_pulllistview != null)
				all_message_item_pulllistview
						.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		}
		if (all_message_item_hint != null) {
			all_message_item_hint.setText(msg);
		}
		if (toast) {
			MyToast.getToast(getActivity(), msg);
		}
	}

	public void finish() {
		setStatus(false, "-1", false);
	}
}
