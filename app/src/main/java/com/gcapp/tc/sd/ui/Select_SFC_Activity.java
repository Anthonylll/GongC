package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gcapp.tc.dataaccess.TeamArray;
import com.gcapp.tc.sd.ui.adapter.SFC_TeamArrayAdapter;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.view.VibratorView;
import com.gcapp.tc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：胜负彩的选号界面，实现选号功能
 * 
 * @author lenovo
 * 
 */
public class Select_SFC_Activity extends Activity implements OnClickListener {
	private final static String TAG = "Select_SFC_Activity";
	/* 头部 */
	private RelativeLayout layout_top_select;// 顶部布局
	private ImageButton btn_back; // 返回
	private LinearLayout layout_select_playtype;// 玩法选择
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	private ImageButton btn_help;// 帮助
	private ConfirmDialog dialog;// 提示框
	/* 尾部 */
	private TextView btn_clearall; // 清除全部
	private TextView btn_submit; // 选好了
	public TextView tv_tatol_count;// 总注数
	public TextView tv_tatol_money;// 总钱数
	private Bundle bundle;
	private int type = 1;
	private ListView listView;
	private SFC_TeamArrayAdapter Adapter;
	public static List<TeamArray> lisTeamArrays = new ArrayList<TeamArray>();
	private Context context;
	// /** 要更改的 **/
	private String opt = "10"; // 格式化后的 opt
	private String auth, info, time, imei, crc; // 格式化后的参数
	private Intent intent;
	private RequestUtil requestUtil;
	private TextView tv_result_tip;
	/** 期号*/
	private TextView num_text;
	/** 截止时间*/
	private TextView time_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_buy_rx9);
		App.activityS.add(this);
		App.activityS1.add(this);
		context = Select_SFC_Activity.this;
		findView();
		clearHashSet();
		init();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
		bundle = new Bundle();
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_playtype = (TextView) findViewById(R.id.btn_playtype);
		btn_help = (ImageButton) findViewById(R.id.btn_help);
		iv_up_down = (ImageView) findViewById(R.id.iv_up_down);
		layout_select_playtype = (LinearLayout) findViewById(R.id.layout_select_playtype);
		btn_clearall = (TextView) findViewById(R.id.btn_clearall);
		btn_submit = (TextView) findViewById(R.id.btn_submit);
		tv_result_tip = (TextView) this.findViewById(R.id.tv_result_tip);
		tv_tatol_count = (TextView) this.findViewById(R.id.tv_tatol_count);
		tv_tatol_money = (TextView) this.findViewById(R.id.tv_tatol_money);
		num_text = (TextView)findViewById(R.id.num_text);
		time_text = (TextView)findViewById(R.id.time_text);
		layout_top_select = (RelativeLayout) findViewById(R.id.layout_top_select);
		listView = (ListView) findViewById(R.id.bet_lv_nums);
		Adapter = new SFC_TeamArrayAdapter(Select_SFC_Activity.this,
				lisTeamArrays,
				VibratorView.getVibrator(getApplicationContext()));
		// /** 要更改的 新加的加载图片 **/
		tv_result_tip.setText("正在加载中...");
		// 隐藏和显示
		iv_up_down.setVisibility(View.GONE);
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		btn_playtype.setText("胜负彩");
		if (NetWork.isConnect(context)) {
			getTeamData();
		} else {
			Toast.makeText(context, "网络连接异常，请检查网络", Toast.LENGTH_SHORT);
		}
		if(lisTeamArrays.size() != 0) {
			num_text.setText(lisTeamArrays.get(0).getIsuseName());
			time_text.setText(lisTeamArrays.get(0).getEndtime());
		}
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_help.setOnClickListener(this);
		btn_clearall.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		listView.setAdapter(Adapter);
	}

	/**
	 * 公共点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 返回 **/
		case R.id.btn_back:
			exit();
			break;
		/** 提交号码 **/
		case R.id.btn_submit:
			submitNumber();
			break;
		/** 清空 **/
		case R.id.btn_clearall:
			clear();
			break;
		/** 玩法说明 **/
		case R.id.btn_help:
			playExplain();
			break;
		}
	}

	/**
	 * 从投注页面跳转过来 将投注页面的值 显示出来
	 */
	public void getItem() {
		Intent intent = Select_SFC_Activity.this.getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		if (null != bundle) {
			List<String> list_key = new ArrayList<String>();
			List<String> list_value = new ArrayList<String>();
			list_key = bundle.getStringArrayList("Key");
			list_value = bundle.getStringArrayList("Value");
			for (int i = 0; list_key.size() > i; i++) {
				SFC_TeamArrayAdapter.btnMap.put(
						Integer.parseInt(list_key.get(i)), list_value.get(i));
			}
		}
		updateAdapter();
	}

	/**
	 * 玩法说明
	 */
	private void playExplain() {
		Intent intent = new Intent(Select_SFC_Activity.this,
				PlayDescription.class);
		Select_SFC_Activity.this.startActivity(intent);
	}

	/**
	 * 提交号码
	 */
	private void submitNumber() {
		if (SFC_TeamArrayAdapter.btnMap.size() < 14
				|| Integer.parseInt(tv_tatol_count.getText().toString()) < 1) {
			Toast.makeText(Select_SFC_Activity.this, "请至少选择1注",
					Toast.LENGTH_SHORT);
			return;
		}
		Intent intent = new Intent(Select_SFC_Activity.this,
				Bet_SFC_Activity.class);
		intent.putExtra("lotteryBundle", bundle);
		Select_SFC_Activity.this.startActivity(intent);
	}

	/**
	 * 清空
	 */
	private void clear() {
		clearHashSet();
		updateAdapter();
		AppTools.totalCount = 0;
	}

	/**
	 * 重写返回键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exit();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出当前页面，清空选号
	 */
	private void exit() {
		if (AppTools.list_numbers == null
				|| (AppTools.list_numbers != null && AppTools.list_numbers
						.size() == 0)) {
			if (SFC_TeamArrayAdapter.btnMap.size() != 0)

			{
				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clearHashSet();
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
						}
					}
				});

			} else {
				clearHashSet();
				for (int i = 0; i < App.activityS1.size(); i++) {
					App.activityS1.get(i).finish();
				}
			}
		} else {
			if (SFC_TeamArrayAdapter.btnMap.size() != 0) {

				dialog.show();
				dialog.setDialogContent("您确认退出选号页面吗,您的号码将不会被保存？");
				dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
					@Override
					public void getResult(int resultCode) {
						if (1 == resultCode) {// 确定
							clearHashSet();
							Intent intent = new Intent(
									Select_SFC_Activity.this,
									Bet_RX9_Activity.class);
							Select_SFC_Activity.this.startActivity(intent);
						}
					}
				});
			} else {
				clearHashSet();
				Intent intent = new Intent(Select_SFC_Activity.this,
						Bet_RX9_Activity.class);
				Select_SFC_Activity.this.startActivity(intent);
			}
		}
	}

	/**
	 * 刷新Adapter
	 */
	public void updateAdapter() {
		if (null == Adapter || null == tv_tatol_count || null == tv_tatol_money)
			return;
		Adapter.notifyDataSetChanged();
		setTextShow();
	}

	/**
	 * 得到注数和金额
	 */
	public void setTextShow() {
		tv_tatol_count.setText(AppTools.totalCount + "");
		tv_tatol_money.setText((AppTools.totalCount * 2) + "");
	}

	/**
	 * 清空选中情况
	 */
	public static void clearHashSet() {
		if (null != SFC_TeamArrayAdapter.btnMap) {
			SFC_TeamArrayAdapter.btnMap.clear();
		}
	}

	/**
	 * 销毁activity
	 */
	@Override
	protected void onDestroy() {
		App.activityS.remove(this);
		super.onDestroy();
	}

	/**
	 * 得到胜负彩的对阵信息
	 */
	private void getTeamData() {
		if (lisTeamArrays == null)
			lisTeamArrays = new ArrayList<TeamArray>();

		if (lisTeamArrays.size() == 0) {
			requestUtil = new RequestUtil(context, true,
					Request.CONFIG_CACHE_MEDIUM) {
				@Override
				public void responseCallback(JSONObject isusesInfo) {
					if (RequestUtil.DEBUG)
						Log.i(TAG, "胜负彩对阵结果" + isusesInfo);
					if (null != isusesInfo) {
						try {
							AppTools.serverTime = isusesInfo
									.optString("serverTime");
							JSONArray array = new JSONArray(
									isusesInfo.getString("isusesInfo"));
							for (int i = 0; i < 1; i++) {
								JSONObject object = array.getJSONObject(i);
								if (null != object) {
									num_text.setText(object.getString("isuseName")+"期");
									time_text.setText(object.getString("endtime")+"截止");
									JSONArray arrayAgainst = new JSONArray(
											object.getString("dtMatch"));
									TeamArray team = null;
									for (int j = 0; j < arrayAgainst.length(); j++) {
										JSONObject objectAgainst = arrayAgainst
												.getJSONObject(j);
										team = new TeamArray();
										team.setId(objectAgainst
												.optString("matchnumber"));
										team.setGame(objectAgainst
												.optString("game"));
										team.setTime(objectAgainst
												.optString("datetime"));
										team.setWinSp(objectAgainst
												.optString("s"));
										team.setFloatSp(objectAgainst
												.optString("p"));
										team.setLoseSp(objectAgainst
												.optString("f"));
										team.setGuestTeam(objectAgainst
												.optString("questTeam"));
										team.setMainTeam(objectAgainst
												.optString("hostTeam"));
										team.setMatchDate(objectAgainst
												.optString("startdatetime"));
										team.setIsuseName(object.getString("isuseName")+"期");
										team.setEndtime(object.getString("endtime")+"截止");
										lisTeamArrays.add(team);
									}
								}
							}
							Adapter = new SFC_TeamArrayAdapter(
									Select_SFC_Activity.this,
									lisTeamArrays,
									VibratorView
											.getVibrator(getApplicationContext()));
							listView.setAdapter(Adapter);
						} catch (JSONException e) {
							e.printStackTrace();
							MyToast.getToast(context, "系统异常.请稍后再试。");
							// 结束所有的跳到主页面
							List<Activity> as = App.activityS;
							for (int i = 0; i < as.size(); i++) {
								as.get(i).finish();
							}
							intent = new Intent(Select_SFC_Activity.this,
									MainActivity.class);
							Select_SFC_Activity.this.startActivity(intent);
							if (RequestUtil.DEBUG)
								Log.e(TAG, "获取对阵数据出错：" + e.getMessage());
						}
					} else {
						MyToast.getToast(context, "暂无对阵信息");
					}
				}

				@Override
				public void responseError(VolleyError error) {
					MyToast.getToast(context, "系统异常.请稍后再试。");
					// 结束所有的跳到主页面
					List<Activity> as = App.activityS;
					for (int i = 0; i < as.size(); i++) {
						as.get(i).finish();
					}
					intent = new Intent(Select_SFC_Activity.this,
							MainActivity.class);
					Select_SFC_Activity.this.startActivity(intent);
					if (RequestUtil.DEBUG)
						Log.e(TAG, "获取对阵数据出错：" + error.getMessage());
				}
			};
			requestUtil.getRx9AndSfcData("74");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (null != requestUtil) {
			Request request = requestUtil.getRequest();
			if (null != request)
				request.cancel();
		}
	}

	/**
	 * 重新进入界面刷新数据
	 */
	@Override
	protected void onResume() {
		super.onResume();
		listView.scrollTo(0, 0);
		getItem();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		super.onNewIntent(intent);
	}
}
