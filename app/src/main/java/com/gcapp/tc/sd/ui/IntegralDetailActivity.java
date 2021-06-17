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
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.dataaccess.IntegralInfo;
import com.gcapp.tc.fragment.IntegralFragment;
import com.gcapp.tc.sd.ui.adapter.IntegralDetailAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.view.MyListView2;
import com.gcapp.tc.view.MyScrollView;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：积分明细模块
 * 
 * @author lenovo
 */
public class IntegralDetailActivity extends FragmentActivity implements
		IntegralFragment.OnFragmentInteractionListener {
	MyScrollView scrollView;
	ImageButton btn_back;
	Button btn_setting;
	MyListView2 listView2;
	IntegralFragment fragment;
	private static final String TAG = "IntegralDetailActivity.class";
	private Context context = IntegralDetailActivity.this;
	private LinearLayout ll;
	private ProgressBar pb;
	private IntegralDetailAdapter adapter;
	private List<IntegralInfo> list;
	private int pageIndex = 0;
	private MySingleton mySingleton;
	private List<IntegralInfo> list_open_temp;
	private int total;
	private boolean canRefresh = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_detail);
		init();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		initView();
		mySingleton = MySingleton.getInstance(context);
		ll = new LinearLayout(this);
		ll.setBackgroundColor(getResources().getColor(R.color.my_center_bg));
		ll.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT));
		pb = new ProgressBar(this);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		listView2.addFooterView(ll);
		scrollView.setOnScrollListener(new ScrollListener());
		list = new ArrayList<IntegralInfo>();
		adapter = new IntegralDetailAdapter(this, list);
		listView2.setAdapter(adapter);
		list_open_temp = new ArrayList<IntegralInfo>();
		total = AppTools.totalScoring;
		fragment = IntegralFragment.newInstance(AppTools.currentScoring + "",
				total + "", AppTools.totalConversionScoring + "");
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_oval, fragment).commit();
		getHttpRes(true);
		App.activityS.add(this);
		App.activityS1.add(this);
	}

	/**
	 * 初始化自定义控件
	 */
	private void initView() {
		scrollView = (MyScrollView) findViewById(R.id.integral_scrollview);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		listView2 = (MyListView2) findViewById(R.id.integral_detail_listview);
	}

	/**
	 * 返回键监听
	 */
	public void back() {
		for (Activity activity : App.activityS1) {
			activity.finish();
		}
	}

	/**
	 * 返回键监听
	 */
	public void back(View view) {
		back();
	}

	/**
	 * 重写返回键监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 跳转到积分兑换
	 */
	public void toAnother() {
		Intent intent = new Intent(IntegralDetailActivity.this,
				IntegralExchangeActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到积分兑换
	 */
	public void toAnother(View view) {
		toAnother();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 滚动到顶部
		if (listView2 != null && scrollView != null) {
			listView2.setFocusable(false);
			scrollView.smoothScrollTo(0, 0);
		}
	}

	/**
	 * 获取网络积分明细的数据
	 */
	private void getHttpRes(boolean force) {
		if (NetWork.isConnect(context)) {
			String opt = "66";
			String info = "{\"pageIndex\":\"" + pageIndex + "\"}";
			JsonObjectRequest request = new JsonObjectRequest(AppTools.path,
					RequestParams.convertParams(context, opt, info),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							switch (parseJsonObject(response)) {
							case 0:
								adapter.setAll(list_open_temp);
								adapter.notifyDataSetChanged();
								break;
							case 1:
								adapter.setAll(list_open_temp);
								adapter.notifyDataSetChanged();
								MyToast.getToast(context, "数据加载完成!");
							case -1:
							case -2:
								canRefresh = false;
								listView2.removeFooterView(ll);
								break;
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							canRefresh = false;
							listView2.removeFooterView(ll);
							RequestParams.convertError(context, error, true);
						}
					});
			request.setTag(TAG);
			request.setCacheTime(JsonRequest.CONFIG_CACHE_SHORT);
			if (force) {// 删除缓存
				mySingleton.dropCache(request.getCacheKey());
			}
			mySingleton.addToRequestQueue(request);

		} else {
			MyToast.getToast(context, "网络连接异常，请检查网络");
		}
	}

	/**
	 * 解析积分明细的JSON数据
	 * 
	 * @param result
	 * @return
	 */
	private int parseJsonObject(JSONObject result) {
		try {
			if (result != null) {
				String error = result.getString("error");
				if (error.equals("0")) {
					JSONArray scoringDetails = new JSONArray(
							result.getString("scoringDetails"));
					String pageCount = result.getString("pageCount");
					IntegralInfo info;
					for (int i = 0; i < scoringDetails.length(); i++) {
						info = new IntegralInfo();
						String time = scoringDetails.getJSONObject(i)
								.getString("dateTime");
						String onece = scoringDetails.getJSONObject(i)
								.getString("scoring");
						String ways = scoringDetails.getJSONObject(i)
								.getString("operatorType");
						info.setTime(time);
						info.setWay(ways);// 赠送积分的方式
						if (ways.equals("201") || ways.equals("101")) {
							onece = "-" + onece;
						} else {
							onece = "+" + onece;
						}
						info.setOnece(onece);
						info.setTotal(total + "");
						if (onece.startsWith("+")) {
							total = total - Integer.valueOf(onece.substring(1));
						} else if (onece.startsWith("-")) {
							total = total + Integer.valueOf(onece.substring(1));
						}
						list_open_temp.add(info);
					}
					return scoringDetails.length() < 10 ? 1 : 0;
				} else {
					return -2;
				}
			}
		} catch (Exception e) {
			VolleyLog.e(">Exception==>%s", e.toString());
		}
		return -1;
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		toAnother();
	}

	/**
	 * 滑动监听，加载数据
	 * 
	 * @author lenovo
	 * 
	 */
	private class ScrollListener implements MyScrollView.OnMyScrollListener {

		@Override
		public void onBottom() {
			if (canRefresh) {
				pageIndex++;
				getHttpRes(true);
			}
		}

		@Override
		public void onTop() {
		}

		@Override
		public void onScroll() {
		}
	}

	/**
	 * 重写菜单布局
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_integral_detail, menu);
		return true;
	}

	/**
	 * 重写菜单选项监听
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
