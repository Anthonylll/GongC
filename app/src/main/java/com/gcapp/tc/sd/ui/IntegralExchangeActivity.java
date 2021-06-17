package com.gcapp.tc.sd.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gcapp.tc.fragment.IntegralFragment;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.Dialog_integration_exchange;
import com.gcapp.tc.view.MyGridView;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：积分中心的积分兑换类
 * 
 * @author lenovo
 * 
 */
public class IntegralExchangeActivity extends FragmentActivity implements
		IntegralFragment.OnFragmentInteractionListener {
	private Context context = IntegralExchangeActivity.this;
	private static final String TAG = "IntegralDetailActivity";
	private FragmentTransaction transaction;

	ImageButton btn_back;

	TextView btn_integral_exchange;// 兑换

	Button btn_goto;// 积分明细

	FrameLayout frame_integral_detail;

	EditText et_integral;

	MyGridView gv_integral;

	private double exchangeIntegral;
	private Intent intent;
	private IntegralFragment integralFragment;
	private MyGridViewAdapter adapter;
	private int[] quickExchange = { 10, 20, 50, 100 };
	private int[] imageResId = { R.drawable.exchange_ten,
			R.drawable.exchange_twenty, R.drawable.exchange_fifty,
			R.drawable.exchange_onehundred };
	private Dialog_integration_exchange dialog;// 提示框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_integral_exchange);
		init();
	}

	/**
	 * 跳转到积分明细
	 */
	protected void onclickDetail() {
		intent = new Intent(this, IntegralDetailActivity.class);
		this.startActivity(intent);
	}

	/**
	 * 跳转到积分明细
	 */
	public void onclickDetail(View view) {
		onclickDetail();
	}

	/**
	 * 兑换积分
	 */
	protected void onclickExchangeIntegral() {
		String text = "";
		if ("".equals(et_integral.getText().toString())) {
			text = "0.0";
		} else {
			if (!et_integral.getText().toString().contains(".")) {
				text = et_integral.getText() + ".0";
			} else {
				text = et_integral.getText().toString();
			}
		}
		exchangeIntegral = Double.parseDouble(text);
		exchangeIntegral();
	}

	/**
	 * 兑换积分
	 */
	public void onclickExchangeIntegral(View view) {
		onclickExchangeIntegral();
	}

	/**
	 * 请求兑换
	 */
	private void exchangeIntegral() {
		if (check()) {
			RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
					"正在兑换...") {
				@Override
				public void responseCallback(JSONObject reponseJson) {
					if (RequestUtil.DEBUG)
						Log.i(TAG, "积分兑换返回结果：" + reponseJson);
					String error = reponseJson.optString("error");
					if (null != error && error.equals("0")) {// 正常返回
						AppTools.currentScoring = reponseJson
								.optInt("currentScoring");
						AppTools.totalScoring = reponseJson
								.optInt("totalScoring");
						AppTools.totalConversionScoring = reponseJson
								.optInt("totalConversionScoring");
						AppTools.scoringExchangerate = reponseJson
								.optInt("scoringExchangerate");
						AppTools.user.setBalance(reponseJson
								.optDouble("balance"));
						// MyToast.getToast(context, "恭喜您，兑换成功！");
						showDialog();// 弹出对话框

						setCursorPosition(et_integral);
						integralFragment.updateAll(
								AppTools.currentScoring + "",
								AppTools.totalScoring + "",
								AppTools.totalConversionScoring + "");
					} else {
						String msg = reponseJson.optString("msg");
						if (RequestUtil.DEBUG) {
							Log.e(TAG, "兑换出错，" + msg);
						}
						MyToast.getToast(context, msg);
					}
				}

				@Override
				public void responseError(VolleyError error) {
					if (RequestUtil.DEBUG) {
						Log.e(TAG, "积分参数请求错误" + error.getClass().getName() + "");
					}
					MyToast.getToast(context, "数据请求错误，请稍后重试...");
				}
			};
			requestUtil.exchangeIntegral(exchangeIntegral);
		}
	}

	/**
	 * 签到成功的赠送积分的窗口提示
	 */
	private void showDialog() {
		dialog.show();

	}

	/**
	 * 判断是否符合兑换条件
	 * 
	 * @return true/false
	 */
	private boolean check() {
		boolean isTrue = true;
		if (0 == AppTools.currentScoring) {
			isTrue = false;
			MyToast.getToast(context, "可兑换积分为0");
		} else {
			if (0 == exchangeIntegral) {
				isTrue = false;
				MyToast.getToast(context, "至少兑换" + AppTools.scoringExchangerate
						+ "分");
				exchangeIntegral = AppTools.scoringExchangerate;
			} else if (exchangeIntegral < AppTools.scoringExchangerate) {
				isTrue = false;
				exchangeIntegral = AppTools.scoringExchangerate;
				MyToast.getToast(context, "可兑换积分必须是" + exchangeIntegral
						+ "的整数倍");
			} else if (exchangeIntegral > AppTools.currentScoring) {
				isTrue = false;
				if (0 == AppTools.scoringExchangerate
						* AppTools.scoringExchangerate)
					exchangeIntegral = 0;
				else
					exchangeIntegral = AppTools.currentScoring
							/ AppTools.scoringExchangerate
							* AppTools.scoringExchangerate;
				MyToast.getToast(context, "超出可兑换积分" + exchangeIntegral);
			}
		}
		et_integral.setText((int) exchangeIntegral + "");
		setCursorPosition(et_integral);
		return isTrue;
	}

	/**
	 * 设置文本框的焦点
	 * 
	 * @param et
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text instanceof Spannable) {
			Selection.setSelection((Spannable) text, text.toString().length());
		}
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		dialog = new Dialog_integration_exchange(this, R.style.dialog);
		initView();
		App.activityS.add(this);
		App.activityS1.add(this);
		adapter = new MyGridViewAdapter();
		gv_integral.setAdapter(adapter);
		transaction = getSupportFragmentManager().beginTransaction();
		integralFragment = IntegralFragment.newInstance(AppTools.currentScoring
				+ "", AppTools.totalScoring + "",
				AppTools.totalConversionScoring + "");
		transaction.add(R.id.frame_integral_detail, integralFragment);
		transaction.commit();
	}

	/**
	 * 初始化自定义控件
	 */
	private void initView() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);

		btn_integral_exchange = (TextView) findViewById(R.id.btn_integral_exchange);// 兑换

		btn_goto = (Button) findViewById(R.id.btn_goto);// 积分明细

		frame_integral_detail = (FrameLayout) findViewById(R.id.frame_integral_detail);

		et_integral = (EditText) findViewById(R.id.et_integral);

		gv_integral = (MyGridView) findViewById(R.id.gv_integral);

	}

	/**
	 * 返回
	 */
	protected void onclickBack() {
		for (Activity activity : App.activityS1) {
			activity.finish();
		}
	}

	/**
	 * 返回按钮
	 */
	public void onclickBack(View view) {
		onclickBack();
	}

	/**
	 * 重写返回键监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onclickBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		onclickDetail();
	}

	/**
	 * 自定义适配器
	 * 
	 * @author lenovo
	 * 
	 */
	private class MyGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return quickExchange.length;
		}

		@Override
		public Object getItem(int position) {
			return quickExchange[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			MyHolder holder;
			if (null == convertView) {
				convertView = getLayoutInflater().inflate(
						R.layout.layout_integral_gv_item, null);
				holder = new MyHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (MyHolder) convertView.getTag();
			holder.iv_integral.setBackgroundResource(imageResId[position]);
			holder.tv_integral.setText("消耗积分：" + quickExchange[position]
					* AppTools.scoringExchangerate + "分");
			holder.btn_integral.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					exchangeIntegral = quickExchange[position]
							* AppTools.scoringExchangerate;
					exchangeIntegral();// 兑换积分
				}
			});
			return convertView;
		}
	}

	/**
	 * 初始化控件的静态类
	 * 
	 * @author lenovo
	 * 
	 */
	static class MyHolder {
		public MyHolder(View view) {
			iv_integral = (ImageView) view.findViewById(R.id.iv_integral);
			tv_integral = (TextView) view.findViewById(R.id.tv_integral);
			btn_integral = (Button) view.findViewById(R.id.btn_integral);
		}

		ImageView iv_integral;
		TextView tv_integral;
		Button btn_integral;
	}
}
