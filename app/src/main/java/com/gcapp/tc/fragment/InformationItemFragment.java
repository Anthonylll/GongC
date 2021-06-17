package com.gcapp.tc.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.gcapp.tc.dataaccess.Information;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.sd.ui.Information_Detail;
import com.gcapp.tc.sd.ui.adapter.InformationAdapter;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.LotteryUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.view.MyToast;

/**
 * 每条咨询消息的fragment
 * 
 * @author lenovo
 * 
 */
public class InformationItemFragment extends Fragment {
	private String TAG = "InformationItemFragment";
	private Activity activity;
	private PullToRefreshListView information_item_listView;
	private TextView information_item_hint;
	private List<Information> informations = new ArrayList<Information>();
	private List<Information> informations_temp = new ArrayList<Information>();
	private InformationAdapter adapter;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int newType = 2;
	private int isEnd = pageSize;
//	private int postion;
	private MySingleton mySingleton;

	/**
	 * 构造方法
	 * 
	 * @param postion
	 * @param newType
	 * @return
	 */
	public static InformationItemFragment newInstance(int postion, int newType) {
		InformationItemFragment fragment = new InformationItemFragment();
		fragment.newType = newType;
//		fragment.postion = postion;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.information_item_fragment,
				container, false);
		activity = getActivity();
		init(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	@SuppressWarnings("unchecked")
	private void init(View view) {
		mySingleton = MySingleton.getInstance(activity);
		information_item_listView = (PullToRefreshListView) view
				.findViewById(R.id.information_item_listView);
		information_item_hint = (TextView) view
				.findViewById(R.id.information_item_hint);

		adapter = new InformationAdapter(activity, informations);// 得到资讯
		information_item_listView.setAdapter(adapter);
		if (informations.isEmpty()) {
			getHttpRes(false);
		}
		information_item_listView
				.setOnItemClickListener(new MyItemsClickListener());
		information_item_listView
				.setOnRefreshListener(new OnRefreshListener2() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						information_item_listView.getLoadingLayoutProxy(true,
								false).setLastUpdatedLabel(
								"最近更新: "
										+ LotteryUtils
												.Long2DateStr_detail(System
														.currentTimeMillis()));
						informations.clear();
						pageIndex = 1;
						getHttpRes(true);
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						pageIndex++;
						getHttpRes(true);
					}
				});
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mySingleton != null) {
			mySingleton.cancelAll(TAG);
		}
	}

	/**
	 * 子项点击事件
	 * 
	 * @author lenovo
	 * 
	 */
	private class MyItemsClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			position = position - 1;

			int count = informations.size();
			if (count > 0) {
				long[] ids = new long[informations.size()];
				long currentNewId;
				currentNewId = informations.get(position).getId();
				for (int i = 0; i < informations.size(); i++) {
					ids[i] = informations.get(i).getId();
				}
				Intent intent = new Intent(activity, Information_Detail.class);
				Bundle bundle = new Bundle();
				bundle.putLongArray("ids", ids);
				bundle.putInt("newIndex", position);
				bundle.putLong("currentNewId", currentNewId);
				bundle.putInt("newType", newType);
				intent.putExtra("informationList", bundle);
				startActivity(intent);
			}
		}
	}

	/**
	 * 获取网络的数据
	 * 
	 * @param force
	 *            是否删除之前的缓存
	 */
	private void getHttpRes(boolean force) {
		if (NetWork.isConnect(activity)) {
			String opt = "45";
			String info = RspBodyBaseBean.getLotteryInformation(pageIndex,
					pageSize, newType);
			Log.i(TAG, "咨讯info ==>" + info);
			JsonObjectRequest request = new JsonObjectRequest(AppTools.path,
					RequestParams.convertParams(activity, opt, info),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.i(TAG, "咨讯result :JSONObject response==>"
									+ response);
							information_item_listView.onRefreshComplete();
							information_item_hint.setVisibility(View.GONE);
							dealResult(parserJSONObject(response));
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							information_item_listView.onRefreshComplete();
							information_item_hint.setVisibility(View.GONE);
							RequestParams.convertError(activity, error, true);
						}
					});

			request.setTag(TAG);
			request.setCacheTime(JsonRequest.CONFIG_CACHE_SHORT);
			if (force) {// 删除缓存
				mySingleton.dropCache(request.getCacheKey());
			}
			mySingleton.addToRequestQueue(request);

		} else {
			information_item_listView.onRefreshComplete();
			MyToast.getToast(activity, "网络连接异常，请检查网络");
			information_item_hint.setVisibility(View.GONE);
			information_item_listView.onRefreshComplete();
		}
	}

	/**
	 * 解析网络获取的json
	 * 
	 * @param result
	 * @return
	 */
	private int parserJSONObject(JSONObject result) {
		if ("0".equals(result.optString("error"))) {
			String array = result.optString("dtInformationTitles");
			JSONArray array2 = null;
			try {
				array2 = new JSONArray(array);
			} catch (JSONException e) {
				VolleyLog.e("JSONException ==> %s", e.toString());
			}
			isEnd = array2 != null ? array2.length() : 0;
			if (isEnd == 0) {
				return 1;
			}
			informations_temp.clear();
			for (int i = 0; i < array2.length(); i++) {
				JSONObject items = null;
				try {
					items = array2.getJSONObject(i);
				} catch (JSONException e) {
					VolleyLog.e("JSONException ==> %s", e.toString());
				}
				if (null != items) {
					addInformation(items);
				}
			}
			return 0;
		} else {
			return Integer.valueOf(result.optString("error"));
		}
	}

	/**
	 * 整理信息集合
	 * 
	 * @param items
	 */
	private void addInformation(JSONObject items) {
		Information information = new Information();
		String titleString = items.optString("title");
		String color = items.optString("color");
		String newTypeNameString = getNewTypeStr(titleString);
		information.setId(items.optLong("id"));
		information.setNewType(newTypeNameString);
		information.setParentTypeId(newType);
		information.setDateTime(items.optString("dateTime"));
		information.setDescribe(items.optString("Abstractt"));
		String  imgUrl=items.optString("PicUrlForApp");
		
		if(imgUrl.startsWith("/")){
			information.setImg_url(AppTools.url + items.optString("PicUrlForApp"));
		}else{
			information.setImg_url(AppTools.url +"/"+ items.optString("PicUrlForApp"));
		}
		information.setColor(color);

		if (!newTypeNameString.equals("")) {
			information.setTitle(titleString.replace(newTypeNameString, ""));
		} else {
			information.setTitle(titleString);
		}
		String title = information.getTitle();
		title = title.replace("&quot;", "\"");
		information.setTitle(title);

		information.setSerialNumber(items.optInt("SerialNumber"));
		information.setRecordCount(items.optString("RecordCount"));
		informations_temp.add(information);
	}

	private String getNewTypeStr(String str) {
		String resultString = "";
		Pattern p = Pattern.compile(".*?\\[(.*?)\\].*?");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			resultString = "[" + m.group(1) + "]"; // 匹配第1项
		}
		return resultString;
	}

	/**
	 * 处理请求结果
	 * 
	 * @param result
	 */
	private void dealResult(int result) {
		switch (result) {
		case 0:
			if (pageIndex == 1) {
				informations.clear();
				setStatus(true, "加载完毕", false);
			}
			for (Information information : informations_temp) {
				informations.add(information);
			}
			adapter.notifyDataSetChanged();

			if (isEnd < pageSize && pageIndex != 1) {
				setStatus(false, "没有更多数据", true);
			}
			if (informations.size() == 0 && pageIndex == 1) {
				setStatus(false, "暂无资讯", false);
			}
			break;
		case 1:
			setStatus(false, "没有更多数据", true);
			break;
		case -1:
			setStatus(false, "数据加载出错，请重试", false);
			break;
		case -500:
			setStatus(false, "连接超时，请重试", false);
		case -4504:
			setStatus(false, "暂无资讯", false);
			break;
		}
	}



	/**
	 * 处理请求结果的提示信息
	 * 
	 * @param status
	 *            ：状态码
	 * @param msg
	 *            ：信息
	 * @param toast
	 *            ：是否弹出toast
	 */
	private void setStatus(boolean status, String msg, boolean toast) {
		if (msg.equals("-1")) {
			if (information_item_listView != null
					&& information_item_hint != null) {
				information_item_listView.onRefreshComplete();
				information_item_listView.setMode(Mode.DISABLED);
				// information_item_hint.setText("正在加载中..");
			}
			return;
		}
		if (status) {
			if (information_item_listView != null)
				information_item_listView.setMode(Mode.BOTH);

		} else {
			if (information_item_listView != null)
				information_item_listView.setMode(Mode.PULL_FROM_START);
		}
		if (information_item_hint != null) {
			information_item_hint.setText(msg);
		}
		if (toast) {
			MyToast.getToast(activity, msg);
		}
	}

	public void finish() {
		setStatus(false, "-1", false);
	}
}
