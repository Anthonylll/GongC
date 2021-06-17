package com.gcapp.tc.sd.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.BaseHelper;
import com.gcapp.tc.utils.HttpUtils;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 支付宝充值
 * 
 * @author SLS003
 */
public class RechargeActivity extends Activity implements OnClickListener {
	private static final String TAG = "RechargeActivity";
	private Activity context = RechargeActivity.this;
	private EditText et_money;
	private Button btn_recharge;
	private ImageButton btn_back;
	private TextView tv_name, tv_title;
	private double money;
	private ProgressDialog mProgress = null;
	private MyHandler myHandler;
	private int code;
	private String payUrl, message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recharge);
		findView();
		init();
		setListener();
	}

	private void init() {
		money = 1;
		et_money.setSelection(et_money.getText().length());
	}

	/** 初始化UI */
	private void findView() {
		myHandler = new MyHandler();
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_title.setText("支付宝快捷充值");
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		et_money = (EditText) this.findViewById(R.id.recahge_et_money);
		btn_recharge = (Button) this.findViewById(R.id.recharge_btn_ok);
		tv_name = (TextView) this.findViewById(R.id.recahge_name2);
		tv_name.setText(AppTools.user.getName());
	}

	/** 绑定监听 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_recharge.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_btn_ok:
			if (check()) {
				mProgress = BaseHelper.showProgress(RechargeActivity.this,
						null, "加载中....", true, true);
				MyAsynTask2 task = new MyAsynTask2();
				task.execute();
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 异步任务
	 */
	class MyAsynTask2 extends AsyncTask<Void, Integer, String> {
		@Override
		protected String doInBackground(Void... params) {
			String[] names = { "id", "money", "way" };
			// http://www.skillall.cn/Home/Room/OnlinePay/Swiftpass/Send.aspx?id=1&money=1&way=Android
			String uid = AppTools.user.getUid();
			String values[] = { uid, money + "", "Android" };
			String result = HttpUtils.doPost(names, values, AppTools.zfbpath);
			Log.i("login", "支付result---" + result);

			if ("-500".equals(result) || null == result)
				return "-500";
			try {
				JSONObject Info = new JSONObject(result);
				code = Info.optInt("errorCode");// 编号
				payUrl = Info.optString("value");// 请求地址
				message = Info.optString("message");// 错误消息
				if (code == 0) {
					return "0";
				} else {
					return "-100";
				}
			} catch (Exception ex) {
				Log.i("login", "获取值异常---" + ex.getMessage());
				return "-110";
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			myHandler.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			switch (msg.what) {
			case 0:
				System.out.println("====payUrl===" + payUrl);
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(payUrl));// 加载alipays开头的URL
				startActivity(intent);
				break;
			case -100:
				MyToast.getToast(RechargeActivity.this, message + "");
				break;
			default:
				MyToast.getToast(RechargeActivity.this, message + "");
				break;
			}
			super.handleMessage(msg);
		}
	}

	public boolean check() {
		String text = et_money.getText() + "";
		boolean isPass = true;
		if (!"".equals(text)) {
			text = text + ".0";
			double money_text = Double.parseDouble(text);
			if (money_text < 1) {
				et_money.setText(1 + "");
				money = 1;
				MyToast.getToast(RechargeActivity.this, "至少充值1元");
				isPass = false;
			} else if (money_text > 100000) {// 输入金额大于可用余额
				et_money.setText(100000 + "");
				money = 100000;
				MyToast.getToast(RechargeActivity.this, "单笔充值金额最多100000元");
				isPass = false;
			}
			setCursorPosition(et_money);
			money = money_text;
		} else {
			return false;
		}
		return isPass;
	}

	/** 文本框 监听 */
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			if ("".equals(text)) {
				money = 1;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};

	private void setCursorPosition(EditText et_money) {
		CharSequence text = et_money.getText();
		if (text != null) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/** 关闭进度框 */
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
