package com.gcapp.tc.sd.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.dataaccess.MyDrawData;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyBankSpinner;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：提款功能
 * 
 * @author echo
 * 
 */
public class WithdrawalActivity extends Activity implements OnClickListener {
	private Context context = WithdrawalActivity.this;
	private static final String TAG = "WithdrawalActivity";
	private TextView tv_bankName, tv_cardNum, tv_name,tv_tip_info,withdrawal_tv_zfb2;
	private EditText et_money;
	private Button btn_ok;
	//btn_question
	private ImageButton btn_back;
	private String opt, auth, info, time, imei, crc; // 格式化后的参数
	private MyBankSpinner spinner_moneyType;
	public int question_index = 0;
	public int money_index = 0;
	private String money;
	private List<Map<String, String>> moneyType = new ArrayList<Map<String, String>>();
	private List<String> mData = new ArrayList<String>();
	private MyHander mHander = new MyHander();
	private Dialog dialog;
	private String DrawType;// 提款类型
	// 新增控件
	private TextView tvBalance;
	private TextView tvAllowCaiJin;
	public EditText edMoneyType;
	private ImageButton btnMoneyType;
	// private TextView tvLastMoney;// 动态变动
	private TextView tvBankPlace; // 开发地
	private TextView tvBank; // 开发行
	private TextView withdraw_hint_msg;
	private MyDrawData myDrawData;
	/** 是否可提现 */
	private String isWithdraw = "False";
	/** 提现时间段 */
	private String withdrawTime = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_withdrawal);
		getTithdrawData();
		findView();
		getInitDraw();
		getSiteNameAndPhone();
		inData();
	}

	/**
	 * 初始化数据
	 */
	private void inData() {
		mData.add("金额");
		mData.add("彩金");
		for (int i = 0; i < mData.size(); i++) {
			HashMap<String, String> maps = new HashMap<String, String>();
			maps.put("name", mData.get(i));
			moneyType.add(maps);
		}
	}

	/**
	 * 初始化提款申请参数
	 */
	private void init() {
		opt = "37";
		info = RspBodyBaseBean.changeWithdrawal_info(et_money.getText()
				.toString().trim(), DrawType);
		String key = AppTools.key;
		crc = RspBodyBaseBean.getCrc(time, imei, key, info,
				AppTools.user.getUid());
		auth = RspBodyBaseBean.getAuth(crc, time, imei, AppTools.user.getUid());
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		time = RspBodyBaseBean.getTime();
		imei = RspBodyBaseBean.getIMEI(this);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		tv_bankName = (TextView) this
				.findViewById(R.id.withdrawal_tv_bankName2);
		withdrawal_tv_zfb2 = (TextView) this
				.findViewById(R.id.withdrawal_tv_zfb2);
//		tv_location = (TextView) this
//				.findViewById(R.id.withdrawal_tv_location2);
//		tv_zhiName = (TextView) this.findViewById(R.id.withdrawal_tv_fullName2);
		tv_cardNum = (TextView) this.findViewById(R.id.withdrawal_tv_bankNum2);
		tv_name = (TextView) this.findViewById(R.id.withdrawal_tv_name2);
		tv_tip_info = (TextView) this.findViewById(R.id.tv_tip_info);

		et_money = (EditText) this.findViewById(R.id.withdrawal_et_money);
		btn_ok = (Button) this.findViewById(R.id.withdrawal_btn_ok);
		// 新增控件
		tvBalance = (TextView) findViewById(R.id.withdrawal_tv_amount2);
		tvAllowCaiJin = (TextView) findViewById(R.id.withdrawal_tv_CaiJin2);
		tvBankPlace = (TextView) findViewById(R.id.withdrawal_tv_bankPlace2);
		tvBank = (TextView) findViewById(R.id.withdrawal_tv_bank2);
		withdraw_hint_msg = (TextView) findViewById(R.id.withdraw_hint_msg);
		setCursorPosition(et_money);
		et_money.setHint("可提款金额"+AppTools.user.getExtractMoney());
		tv_tip_info.setText("【" + getResources().getString(R.string.app_logo)
				+ "】");

		edMoneyType = (EditText) findViewById(R.id.ed_moneyType);
		btnMoneyType = (ImageButton) findViewById(R.id.withdraw_btn_soft2);

		withdraw_hint_msg.setText(getResources().getString(
				R.string.withdrawals_text_three)
				+ withdrawTime + ";");
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		et_money.addTextChangedListener(watcher);
		edMoneyType.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {

				} else {
					mHander.sendEmptyMessage(12);
				}
			}
		});

		btnMoneyType.setOnClickListener(this);
	}

	/**
	 * 获取站点名称和客服电话
	 */
	public void getSiteNameAndPhone() {
		RequestUtil requestUtil = new RequestUtil(WithdrawalActivity.this,
				false, 0, false, "正在加载用户信息...") {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.e(TAG,
							"获取站点名称和客服电话jsonObject--- " + jsonObject.toString());
				if (null != jsonObject) {
					String name = jsonObject.optString("SiteName");
					String phone = jsonObject.optString("Phone");
					if (!"".equals(name)) {
						AppTools.APP_NAME = name;
					}
					if (!"".equals(phone)) {
						AppTools.SERVICE_PHONE = phone;
					}
				} else {
					if (RequestUtil.DEBUG)
						Log.e(TAG, "获取站点名称和客服电话---返回结果为空");
				}
				findView();
				setListener();
				dialog = RequestUtil.createLoadingDialog(context, "正在提款...",
						true);
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取站点名称和客服电话出错--- " + error.getMessage());
				findView();
				setListener();
				dialog = RequestUtil.createLoadingDialog(context, "正在提款...",
						true);
			}
		};
		requestUtil.getSiteNameAndPhone();
	}

	/**
	 * 得到提款需要的本人信息
	 */
	private void getInitDraw() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseError(VolleyError error) {
				RequestParams.convertError(context, error, true);
			}

			@Override
			public void responseCallback(JSONObject reponseJson) {
				Log.e("MyJson41", reponseJson.toString());
				String MyJson = reponseJson.toString();
				Log.i(TAG, "=====MyJson" + MyJson);
				if (null != reponseJson) {
					myDrawData = JSON.parseObject(MyJson, MyDrawData.class);
					int error = myDrawData.getError();
					if (error == 0) {
						mHander.sendEmptyMessage(11);
					} else {
						Toast.makeText(context, "数据初始化失败", Toast.LENGTH_SHORT);
					}
				}
			}
		};
		requestUtil.getInitParams41();
	}

	/**
	 * 获取可提现时间
	 */
	private void getTithdrawData() {

		RequestUtil requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseCallback(JSONObject item) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取可提现时间信息" + item.toString());
				if (null != item) {
					if ("0".equals(item.optString("error"))) {
						isWithdraw = item.optString("isRight");
						withdrawTime = item.optString("rightdate");
					} else {
						isWithdraw = "True";
						withdrawTime = "全天";
					}
				}
			}

			@Override
			public void responseError(VolleyError error) {
				MyToast.getToast(context, "抱歉，请求提现时间出现未知错误!");
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取可提现时间信息异常---" + error.getMessage());
				withdrawTime = "数据异常,请退出该页面后重试!";
			}
		};
		requestUtil.test_interface();
	}

	/**
	 * 点击监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.withdrawal_btn_ok:
			if (isWithdraw.equals("True")) {
				if (outOfRange())
					return;
				money = et_money.getText().toString().trim();

				if ("".equals(edMoneyType.getText().toString().trim())) {
					Toast.makeText(context, "提款类型不能为空", Toast.LENGTH_SHORT);
					return;
				}
				int  withdrawMoney = (int)AppTools.user.getMinWithdraw();
				if (money.length() == 0) {
					MyToast.getToast(WithdrawalActivity.this, "提款金额不能为空");
					return;
				} else if (Integer.parseInt(money) < withdrawMoney) {
					MyToast.getToast(WithdrawalActivity.this, "提款金额必须大于"+withdrawMoney+"元");
					return;
				}else if (Integer.parseInt(money) > 50000) {
					MyToast.getToast(WithdrawalActivity.this, "单笔提款金额不能大于50000元");
					return;
				} else if (Double.parseDouble(money) > AppTools.user
						.getExtractMoney()) {
					MyToast.getToast(WithdrawalActivity.this, "当前可提现金额"
							+ AppTools.user.getExtractMoney() + "元");
					return;
				} else {
					btn_ok.setEnabled(false);
					commit_withdrawMoney();
				}
			} else {
				MyToast.getToast(WithdrawalActivity.this, "可提现时间段为："
						+ withdrawTime);
			}
			break;
		case R.id.withdraw_btn_soft2:
			spinner_moneyType = new MyBankSpinner(context, moneyType,
					money_index, AppTools.MONEY_TYPE, R.style.dialog);
			spinner_moneyType.show();
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	/**
	 * 文本框 监听
	 */
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			int eMoney = 0;
			if (text != null && !text.equals("")) {
				eMoney = Integer.parseInt(text);
			} else {
				eMoney = 0;
			}
			int money_text = 0;
			int withdrawMoney = (int)AppTools.user.getMinWithdraw();
			if (null != text && !"".equals(text) && eMoney > withdrawMoney) {
				money_text = Integer.parseInt(text);
			} else {
//				MyToast.getToast(WithdrawalActivity.this, "提现金额至少是20元");
			}

			double money_max = Double.parseDouble(AppTools.user.getBalance());
			if (money_text > money_max) {// 输入金额大于可用余额
				et_money.setText((int) money_max + "");
				money = (int) money_max + "";
				MyToast.getToast(WithdrawalActivity.this, "输入金额不可大于可用余额");
				return;
			}
			setCursorPosition(et_money);
			money = s.toString().trim();
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

	/**
	 * 设置文本焦点
	 * 
	 * @param et
	 *            ：文本对象
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text != null) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	String msgs;

	/**
	 * 提交提款请求
	 */
	public void commit_withdrawMoney() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在提交...") {
			@Override
			public void responseCallback(JSONObject object) {
				String error = "-1";
				if (RequestUtil.DEBUG)
					Log.i(TAG, "开奖号码请求结果" + object);
				try {
					error = object.getString("error");
					if ("0".equals(error)) {
						String userInfo = object.getString("dtUserInfo");
						JSONArray array = new JSONArray(userInfo);
						JSONObject item = array.getJSONObject(0);
						AppTools.user.setBalance(item.getLong("balance"));
						AppTools.user.setFreeze(item.getDouble("freeze"));
						Log.i("x", "提款成功");
					} else {
						msgs = object.getString("msg");
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("x", "提款失败" + e.getMessage());
				}

				btn_ok.setEnabled(true);
				mHander.sendEmptyMessage(Integer.parseInt(error));

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
		//qId qAnswer
		requestUtil.withDrawMoney(et_money.getText().toString().trim(), DrawType);
	}

	/**
	 * 处理提款请求结果
	 * 
	 * @author lenovo
	 * 
	 */
	@SuppressLint("HandlerLeak")
	class MyHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MyToast.getToast(WithdrawalActivity.this, "提款申请成功，请等待处理。");
				WithdrawalActivity.this.finish();
				break;
			case -178:
				MyToast.getToast(WithdrawalActivity.this, msgs);
				break;
			case -500:
				MyToast.getToast(WithdrawalActivity.this, msgs);
				break;
			case -176:
				MyToast.getToast(WithdrawalActivity.this, msgs);
				break;

			// 有问题，在处理
			case 12:
				String name = edMoneyType.getText().toString().trim();
				if (name.equals("金额")) {
					DrawType = "0";
					et_money.setHint("可提款金额" + AppTools.user.getExtractMoney()
							+ "元");
				} else if (name.equals("彩金")) {
					DrawType = "1";
					et_money.setHint("可提款彩金" + myDrawData.getUserAllowHandsel()
							+ "元");
				}
				break;

			case 11:
				tvBalance.setText(myDrawData.getBalance() + "元");
				tvAllowCaiJin.setText(myDrawData.getUserAllowHandsel() + "元");
				tv_name.setText(myDrawData.getRealityName());
				tv_bankName.setText(myDrawData.getBankTypeName());
				tv_cardNum.setText(myDrawData.getBankCardNumber());
				tvBankPlace.setText(myDrawData.getProvinceName()
						+ myDrawData.getCityName());
				withdrawal_tv_zfb2.setText(myDrawData.getAlipayAccount());
				tvBank.setText(myDrawData.getBranchBankName());
				et_money.setHint("可提款金额" + AppTools.user.getExtractMoney()
						+ "元");
				if (myDrawData.getHandselDrawing() != 1) {
					btnMoneyType.setEnabled(false);
				}
				edMoneyType.setText("金额");
				String name2 = edMoneyType.getText().toString().trim();
				if (name2.equals("金额")) {
//					et_money.setHint("可提款金额:");
					DrawType = "0";
				} else if (name2.equals("彩金")) {
					et_money.setHint("提款彩金");
					DrawType = "1";
				}
				break;
			case -180:
				MyToast.getToast(WithdrawalActivity.this, msgs);
				break;
			case -181:
				MyToast.getToast(WithdrawalActivity.this, msgs);
				break;
			default:
				MyToast.getToast(WithdrawalActivity.this, "操作失败");
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 验证提款金额
	 * 
	 * @return
	 */
	private boolean outOfRange() {
		String money = et_money.getText().toString().trim();
		if (money == null) {
			MyToast.getToast(WithdrawalActivity.this, "请输入提款金额");
			setCursorPosition(et_money);
			return true;
		}
		if ("".equals(money)) {
			MyToast.getToast(WithdrawalActivity.this, "请输入提款金额");
			setCursorPosition(et_money);
			return true;
		}
		if (null != money && !"".equals(money)) {
			money = money + ".0";
			double money_text = Double.parseDouble(money);
			double money_max = Double.parseDouble(AppTools.user.getBalance());
			if (money_text > money_max) {// 输入金额大于可用余额
				et_money.setText((int) money_max + "");
				MyToast.getToast(WithdrawalActivity.this, "可提现金额不足");
				setCursorPosition(et_money);
				return true;
			}
		}
		return false;
	}
}
