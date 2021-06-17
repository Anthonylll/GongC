package com.gcapp.tc.sd.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.PurchaseInfo;
import com.gcapp.tc.dataaccess.Schemes;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：发单方案详情的跟单列表类
 * 
 * @author lenovo
 * 
 */
public class FollowPurchase_Activity extends Activity implements
		OnClickListener {
	// 跟单列表activity
	private static final String TAG = "FollowPurchase_Activity";
	private Context context = FollowPurchase_Activity.this;
	private ListView bet_lv_purchase;
	private List<PurchaseInfo> purchaselist = new ArrayList<PurchaseInfo>();
	private MyAdapter adapter;

	private ProgressDialog dialog;
	private LinearLayout btn_back;// 返回
	private TextView tv_back;// 返回
	private TextView ten_hint_text;
	private ImageButton ib_back;// 返回

	private Bundle bundle;
	private Schemes scheme;
	private String auth, info, time, imei, crc; // 格式化后的参数
	private String opt = "80"; // 格式化后的 opt
	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_followpurchase);
		App.activityS.add(this);
		init();
		findView();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		bundle = getIntent().getBundleExtra("bundle");
		scheme = (Schemes) bundle.getSerializable("schem");
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(FollowPurchase_Activity.this);
		myHandler = new MyHandler();
		getSubscribeInfo();
	}

	/**
	 * 初始化UI控件
	 * 
	 */
	private void findView() {
		bet_lv_purchase = (ListView) this.findViewById(R.id.bet_lv_purchase);
		adapter = new MyAdapter(context, purchaselist);
		bet_lv_purchase.setAdapter(adapter);
		btn_back = (LinearLayout) this.findViewById(R.id.btn_back);
		tv_back = (TextView) this.findViewById(R.id.tv_back);
		ten_hint_text = (TextView) this.findViewById(R.id.ten_hint_text);
		ib_back = (ImageButton) this.findViewById(R.id.ib_back);
		btn_back.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		tv_back.setOnClickListener(this);
	}

	/**
	 * 请求跟单列表数据
	 */
	public void getSubscribeInfo() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...") {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "跟单列表请求结果===》" + object);
				if (purchaselist.size() > 0) {
					purchaselist.clear();
				}
				PurchaseInfo info;
				int length = 1;
				try {
					if ("0".equals(object.getString("error"))) {
						JSONArray array = new JSONArray(
								object.getString("BuyList"));
						if ((AppTools.user == null && array.length() > 10)
								|| (array.length() > 10 && !scheme
										.getInitiateUserID().equals(
												AppTools.user.getUid()))) {
							length = 10;
							myHandler.sendEmptyMessage(2);
						} else {
							length = array.length();
							myHandler.sendEmptyMessage(3);
						}
						for (int i = 0; i < length; i++) {
							JSONObject item = array.getJSONObject(i);
							info = new PurchaseInfo();
							info.setPur_username(item.optString("Name"));
							info.setFensum(item.optInt("Share"));
							info.setMoney(item.optDouble("DetailMoney"));
							String time = item.optString("DateTime");
							info.setTime(time);
							if (null != time && !"".equals(time)
									&& time.contains(" ")) {
								info.setTime(time.split(" ")[0]);
								info.setDetailTime(time.split(" ")[1]);
							}
							purchaselist.add(info);
						}
						adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					e.printStackTrace();
					MyToast.getToast(FollowPurchase_Activity.this, "数据解析异常");
				}
				if (object.toString().equals("-500")) {
					MyToast.getToast(FollowPurchase_Activity.this, "连接超时");
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(FollowPurchase_Activity.this, "抱歉，请求出现未知错误..");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "请求报错" + error.getMessage());
			}
		};
		requestUtil.getSubscribeInfo(scheme.getId() + "");
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (null != dialog) {
				dialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			case -1:
				MyToast.getToast(FollowPurchase_Activity.this, "没有数据");
				break;
			case 2:
				ten_hint_text.setVisibility(View.VISIBLE);
				break;
			case 3:
				ten_hint_text.setVisibility(View.GONE);
				break;
			case -110:
				MyToast.getToast(FollowPurchase_Activity.this, "数据解析异常");
				break;
			case -500:
				MyToast.getToast(FollowPurchase_Activity.this, "连接超时");
				break;
			default:
				MyToast.getToast(FollowPurchase_Activity.this, "网络异常");
				break;
			}
		}
	}

	/**
	 * 自定义列表适配器
	 * 
	 * @author lenovo
	 * 
	 */
	class MyAdapter extends BaseAdapter {
		private Context context;
		private List<PurchaseInfo> list;

		public MyAdapter(Context cont, List<PurchaseInfo> listdata) {
			this.context = cont;
			this.list = listdata;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int id) {
			return list.get(id);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				// 得到布局文件
				view = inflater.inflate(R.layout.item_purchase, null);
				holder.tv_username = (TextView) view
						.findViewById(R.id.tv_username);
				holder.tv_fensum = (TextView) view.findViewById(R.id.tv_fensum);
				holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
				holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (list.size() > position) {
				PurchaseInfo info = list.get(position);
				if (info.getPur_username().length() > 10) {
					String userName = (info.getPur_username()).substring(0, 7);
					holder.tv_username.setText(userName + "***");
				} else {
					holder.tv_username.setText(info.getPur_username());
				}
				holder.tv_fensum.setText(info.getFensum() + "");
				DecimalFormat df = new DecimalFormat("#####0.0");
				holder.tv_money.setText(df.format(info.getMoney()) + "");
				holder.tv_time.setText(info.getTime());
			}
			return view;
		}

		class ViewHolder {
			private TextView tv_username;
			private TextView tv_fensum;
			private TextView tv_money;
			private TextView tv_time;
		}
	}

	/**
	 * 公用的点击监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:// 返回
		case R.id.tv_back:
		case R.id.ib_back:
			FollowPurchase_Activity.this.finish();
			break;

		default:
			break;
		}
	}

}
