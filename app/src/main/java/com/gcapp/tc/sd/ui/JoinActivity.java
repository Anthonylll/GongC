package com.gcapp.tc.sd.ui;

import java.text.DecimalFormat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.ConfirmDialog;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

import org.json.JSONObject;

/**
 * 功能：合买界面
 * 
 * @author lenovo
 */
public class JoinActivity extends Activity implements OnClickListener {
	
	private Context context = JoinActivity.this;
	private final static String TAG = "JoinActivity";
	private ImageButton btn_back; // 返回
	private ImageView iv_up_down;// 玩法提示图标
	private TextView btn_playtype;// 玩法
	/** 自购金额*/
	private TextView follow_self_money;
	/** 每份金额*/
	private EditText follow_et_money;
	private EditText follow_et_yj;// 中奖佣金
	private Button follow_btn_sub_yj;// 减
	private Button follow_btn_add_yj;// 加
	private Button follow_sub_money;
	private Button follow_add_money;
	private Button ten_add_money;
	private EditText follow_et_name, follow_et_description; // 方案标题// 方案描述
	// 密码设置
	private TextView follow_btn_public, mfollow_btn_toend, follow_btn_afterwin,
			follow_btn_baomi;
	private TextView follow_tv_total_money; // 应付金额
	private TextView hint_text;
	private Button btn_submit;
	private ImageButton btn_help;
	/** 总份数*/
	private long totalCount;
	/** 总金额(自购金额)*/
	private int totalMoney;
	/** 每份金额即起跟金额*/
	private long shareMoney = 2;
	private int minMoney;
	/** 每份金额是否符合要求*/
	private boolean isEligible = true;
	/** 应付金额*/
	private long payMoney = 0;
	/** 自购份数*/
	private long selfCount = 0;
	private int Bonus = 0; // 佣金
	/** 保密设置*/
	private int secrecyLevel = 4;
	/** 订单类型：0普通订单1发单*/
	private int baoCount = 1;
	private boolean flag = false; // 是否追号。
	private long fen; // 至少要买的份数
	private String messgae;
	private RequestUtil requestUtil;
	/** 发起合买的类型。1普通。2.竞足优化合买。3.竞篮优化合买（优化玩法中已隐藏发单）*/
	private int a = 0;
	private DecimalFormat format = new DecimalFormat("#####0.00");
	private ConfirmDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_join);
		App.activityS1.add(this);
		findView();
		setListener();
		init();
		getScaleParams();
	}

	/**
	 * 初始化UI
	 */
	private void findView() {
		follow_self_money = (TextView) this.findViewById(R.id.follow_self_money);
		follow_et_money = (EditText) this.findViewById(R.id.follow_et_money);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		iv_up_down = (ImageView) this.findViewById(R.id.iv_up_down);
		btn_playtype = (TextView) this.findViewById(R.id.btn_playtype);
		follow_et_yj = (EditText) this.findViewById(R.id.follow_et_yj);
		follow_btn_sub_yj = (Button) this.findViewById(R.id.follow_btn_sub_yj);
		follow_btn_add_yj = (Button) this.findViewById(R.id.follow_btn_add_yj);
		follow_sub_money = (Button) this.findViewById(R.id.follow_sub_money);
		follow_add_money = (Button) this.findViewById(R.id.follow_add_money);
		ten_add_money = (Button) this.findViewById(R.id.ten_add_money);
		follow_btn_public = (TextView) this.findViewById(R.id.follow_btn_public);
		follow_btn_baomi = (TextView) this.findViewById(R.id.follow_btn_baomi);
		mfollow_btn_toend = (TextView) this.findViewById(R.id.mfollow_btn_toend);
		follow_btn_afterwin = (TextView) this.findViewById(R.id.follow_btn_afterwin);
		follow_et_name = (EditText) this.findViewById(R.id.follow_et_name);
		follow_et_description = (EditText) this.findViewById(R.id.follow_et_description);
		follow_tv_total_money = (TextView) this.findViewById(R.id.follow_tv_total_money);
		hint_text = (TextView) this.findViewById(R.id.hint_text);
		btn_submit = (Button) this.findViewById(R.id.btn_submit);
		btn_help = (ImageButton) this.findViewById(R.id.btn_help);
		btn_help.setVisibility(View.GONE);
	}

	/**
	 * 初始化属性
	 */
	private void init() {
		Intent intent = getIntent();
		if (null != intent) {
			String t = intent.getStringExtra("totalMoney");
			a = intent.getIntExtra("join_type", 0);
			totalMoney = Integer.parseInt(t);
			if (intent.getStringExtra("flag") != null) {
				this.flag = true;
			}
			shareMoney= intent.getLongExtra("shareMoney", 2);
			minMoney = (int) intent.getLongExtra("shareMoney", 2);
			follow_et_money.setText(shareMoney+"");
		}
		totalCount = totalMoney/shareMoney;
		follow_self_money.setText(totalMoney+"");
		fen = 1;
		if (!"".equals(AppTools.followLeastBuyScale)) {
			fen = (int) Math.ceil(totalCount
					* Double.parseDouble(AppTools.followLeastBuyScale));
		}
		int mLowBounds = 1;
		if (!"".equals(AppTools.followCommissionScale)
				&& AppTools.followCommissionScale.contains(".")) {
			String commission = AppTools.followCommissionScale;
			Double num = Double.parseDouble(commission)*100;
			mLowBounds = (new Double(num)).intValue();
		}
		follow_et_yj.setText(mLowBounds + "");
		selfCount = fen;
		setTextChange();
		iv_up_down.setVisibility(View.GONE);
		btn_playtype.setText("发起订单");
		
		dialog = new ConfirmDialog(this, R.style.dialog);
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		btn_back.setOnClickListener(this);
		follow_btn_public.setOnClickListener(this);
		follow_btn_baomi.setOnClickListener(this);
		mfollow_btn_toend.setOnClickListener(this);
		follow_btn_afterwin.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		follow_sub_money.setOnClickListener(this);
		follow_add_money.setOnClickListener(this);
		ten_add_money.setOnClickListener(this);
		follow_btn_sub_yj.setOnClickListener(this);
		follow_btn_add_yj.setOnClickListener(this);
		follow_et_yj.addTextChangedListener(yj_textWatcher);
		follow_et_money.addTextChangedListener(money_textWatcher);
		follow_et_money.setFocusable(false);
	}

	/**
	 * 加与减的点击监听方法
	 * 
	 * @param et
	 *            ：输入的文本框对象
	 * @param type
	 *            ：类型
	 */
	public void changEditText(EditText et, int type) {
		if (null != et) {
			String content = et.getText().toString();
			int number = Integer.parseInt(content);
			if (1 == type) {
				number -= 1;
			}else if (2 == type) {
				number += 1;
			}else if (3 == type) {
				number -= minMoney;
				while(totalMoney%number != 0 && number > minMoney) {
					number -= minMoney;
				}
				shareMoney = number;
			}else if (4 == type) {
				number += minMoney;
				while(totalMoney%number != 0 && number < totalMoney) {
					number += minMoney;
				}
				shareMoney = number;
			}else if(5 == type) {
				number += minMoney*10;
				if(number%10 != 0) {
					number = number - minMoney;
				}
				while(totalMoney%number != 0 && number < totalMoney) {
					number += minMoney;
				}
				shareMoney = number;
			}
			et.setText(number + "");
		}
	}
	
	/**
	 * 每份金额改变时
	 */
	private TextWatcher money_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			follow_et_money.setHint("默认为"+minMoney);
			int maxMoney = totalMoney;
			if ("".equals(edt.toString())) {
				shareMoney = minMoney;
			} else {
				if (Integer.parseInt(edt.toString().trim()) > maxMoney) {
					follow_et_money.setText(""+maxMoney);
					MyToast.getToast(JoinActivity.this, "起跟金额最高"+maxMoney+"元");
				}else {
					shareMoney = Integer.parseInt(edt.toString().trim());
				}
			}
		}
	};
	
	/**
	 * 重新设置份数
	 */
	private void setCopiesChange() {
		if(totalMoney%shareMoney == 0) {
			isEligible = true;
			fen = (int) Math.ceil(totalMoney/shareMoney
					* Double.parseDouble(AppTools.followLeastBuyScale));
			hint_text.setText("元");
			hint_text.setTextColor(this.getResources().getColor(R.color.grey));
		}else{
			isEligible = false;
			hint_text.setText("金额违规，无法发单");
			hint_text.setTextColor(this.getResources().getColor(R.color.main_red_new));
		}
	}

	/**
	 * 当文本的值改变时
	 */
	private TextWatcher yj_textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			int bounds = 0;
			follow_et_yj.setHint("默认为0");
			if ("".equals(edt.toString())) {// 为空
				bounds = 0;
			} else {
				if (Integer.parseInt(edt.toString().trim()) > 10) {
					bounds = 10;
					follow_et_yj.setText(10 + "");// 最高佣金为10%
					MyToast.getToast(JoinActivity.this, "最高佣金为10%");
				} else if (Integer.parseInt(edt.toString().trim()) < 5) {// 低于最低佣金
					bounds = 5;
					follow_et_yj.setText(5 + "");// 最低佣金
					MyToast.getToast(JoinActivity.this, "最低佣金为5%");
				} else {
					bounds = Integer.parseInt(edt.toString().trim());
				}
			}
			Bonus = bounds;
			setCursorPosition(follow_et_yj);
		}
	};

	/**
	 * 设置文本监听焦点
	 * 
	 * @param et
	 */
	public void setCursorPosition(EditText et) {
		CharSequence text = et.getText();
		if (text != null) {
			Selection.setSelection((Spannable) text, text.length());
		}
	}

	/**
	 * 选出每份 的金额
	 */
	public void setShareMoney(TextView btn) {

	}

	/**
	 * 设置投注保密性
	 */
	public void setPass(TextView btn, int level) {
		secrecyLevel = level;
		follow_btn_public.setBackgroundResource(R.color.white);
		follow_btn_public.setTextColor(getResources().getColor(R.color.grey));
		follow_btn_baomi.setBackgroundResource(R.color.white);
		follow_btn_baomi.setTextColor(getResources().getColor(R.color.grey));
		mfollow_btn_toend.setBackgroundResource(R.color.white);
		mfollow_btn_toend.setTextColor(getResources().getColor(R.color.grey));
		follow_btn_afterwin.setBackgroundResource(R.color.white);
		follow_btn_afterwin.setTextColor(getResources().getColor(R.color.grey));
		btn.setBackgroundResource(R.color.main_red_new);
		btn.setTextColor(Color.WHITE);
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.follow_sub_money:
			String fMoney = follow_et_money.getText().toString().trim();
			if ((Integer.valueOf(fMoney)>minMoney)&& !fMoney.equals("")){
				changEditText(follow_et_money, 3);
				setCopiesChange();
			}else{
				MyToast.getToast(JoinActivity.this, "起跟金额最低"+minMoney+"元");
			}
			break;
		case R.id.follow_add_money:
			if ("".equals(follow_et_money.getText().toString().trim())) {
				follow_et_money.setText(""+minMoney);
			} else {
				changEditText(follow_et_money, 4);
				setCopiesChange();
			}
			break;
		case R.id.ten_add_money:
			if ("".equals(follow_et_money.getText().toString().trim())) {
				follow_et_money.setText(""+minMoney);
			} else {
				changEditText(follow_et_money, 5);
				setCopiesChange();
			}
			break;
		// 佣金比
		case R.id.follow_btn_sub_yj:
			if (!"0".equals(follow_et_yj.getText().toString().trim())
					&& !"".equals(follow_et_yj.getText().toString().trim())) {
				changEditText(follow_et_yj, 1);// 减 为0或""时不能减
				setTextChange();
			}
			break;
		case R.id.follow_btn_add_yj:
			if ("".equals(follow_et_yj.getText().toString().trim())) {
				follow_et_yj.setText("1");
			} else {
				changEditText(follow_et_yj, 2);// 加 为""时点击+将et设置为1
			}
			break;

		// 投注保密设置
		case R.id.follow_btn_public:
			setPass(follow_btn_public, 0);
			break;
		case R.id.mfollow_btn_toend:
			setPass(mfollow_btn_toend, 1);
			break;
		case R.id.follow_btn_afterwin:
			setPass(follow_btn_afterwin, 4);
			break;
		case R.id.follow_btn_baomi:
			setPass(follow_btn_baomi, 2);
			break;
		// 付款
		case R.id.btn_submit:
			if(shareMoney >= 2) {
				if (selfCount < fen) {
					MyToast.getToast(context, "至少购买" + fen + "份");
				} else {
					if(isEligible) {
						dialog.show();
						dialog.setDialogContent("确认付款？");
						dialog.setDialogResultListener(new ConfirmDialog.DialogResultListener() {
							@Override
							public void getResult(int resultCode) {
								if (1 == resultCode) {// 确定
									commit();
								}
							}
						});
					}else{
						MyToast.getToast(context, "金额违规，无法发单");
					}
				}
			}else{
				MyToast.getToast(context, "起跟金额不能低于2元");
			}
			break;
		case R.id.btn_back:
			JoinActivity.this.finish();
			break;
		}
	}

	/**
	 * 购买请求
	 */
	public void commit() {
		String mTitle = follow_et_name.getText().toString().trim();
		String mContent = follow_et_description.getText().toString().trim();
		if(mContent.equals("")) {
			mContent="跟我一起中大奖！！！";
		};

		if (AppTools.user != null) {
			requestUtil = new RequestUtil(context, false, 0, true, "正在支付...") {
				@Override
				public void responseCallback(JSONObject object) {
					if (RequestUtil.DEBUG)
						Log.i(TAG, "合买支付彩结果" + object);
					if (null != object) {
						String error = object.optString("error");
						if ("0".equals(error)) {
							AppTools.user.setBalance(object
									.optDouble("balance"));
							AppTools.user.setFreeze(object.optDouble("freeze"));
							AppTools.schemeId = object.optInt("schemeids");
							String SucJinECost = format.format(object
									.optDouble("currentMoeny")); // 金额消费
							Log.e("金额消费", SucJinECost);
							String SucCaiJinCost = format.format(object
									.optDouble("currentHandsel")); // 彩金消费
							Log.e("彩金消费", SucCaiJinCost);
							String SuccMoney = format.format(object
									.optDouble("handselMoney")); // 剩余彩金
							String voucherMoney = format.format(object
									.optDouble("voucherMoney"));
							Log.e("剩余彩金", SuccMoney);
							for (int i = 0; i < App.activityS1.size(); i++) {
								App.activityS1.get(i).finish();
							}
							if (null != AppTools.list_numbers)
								AppTools.list_numbers.clear();
							AppTools.totalCount = 0;
							clearAllLotterySelectData();
							Intent intent = new Intent(getApplicationContext(),
									PaySuccessActivity.class);

							intent.putExtra("paymoney", payMoney);
							intent.putExtra("currentMoeny", SucJinECost);
							intent.putExtra("currentHandsel", SucCaiJinCost);
							intent.putExtra("handselMoney", SuccMoney);
							intent.putExtra("voucherMoney", voucherMoney);
							intent.putExtra("isJoin", false);
							JoinActivity.this.startActivity(intent);
						} else if ("-500".equals(error)) {
							MyToast.getToast(context, "连接超时，请重试。");
						} else if ("10000".equals(error)) {
							MyToast.getToast(context, "支付失败，请重试。");
							init();
						} else {
							messgae = object.optString("msg");
							if (messgae.contains("info")) {
								MyToast.getToast(context, messgae
										+ "！请检查是否包含非法字符：英文双引号\"\"!");
							} else {
								MyToast.getToast(context, messgae);
							}
							if (RequestUtil.DEBUG)
								Log.e(TAG, "支付出错," + messgae);
						}
					} else {
						MyToast.getToast(context, "支付出错");
						if (RequestUtil.DEBUG)
							Log.e(TAG, "支付出错");
					}
				}

				@Override
				public void responseError(VolleyError error) {
					MyToast.getToast(context, "支付出错");
					if (RequestUtil.DEBUG)
						Log.e(TAG, "支付出错," + error.getMessage());
				}
			};
			if (a == 0) {
				requestUtil.commitFollow(totalMoney, shareMoney,
						baoCount, Bonus, mTitle, mContent, secrecyLevel, flag);
			} else if (a == 1) {
				requestUtil.commitBetting_jc_voptimization(totalMoney, 1,
						Bonus_JCZQ_Activity.list_Show,
						Bonus_JCZQ_Activity.youhua_type, mTitle, mContent,
						baoCount, (int) (totalMoney / shareMoney), (int) selfCount,
						secrecyLevel, Bonus);
			} else if (a == 2) {
				requestUtil.commitBetting_jc_voptimization(totalMoney, 1,
						Bonus_JCLQ_Activity.list_Show,
						Bonus_JCZQ_Activity.youhua_type, mTitle, mContent,
						baoCount, (int) (totalMoney / shareMoney), (int) selfCount,
						secrecyLevel, Bonus);
			}
		} else {// 未登陆
			MyToast.getToast(JoinActivity.this, "请先登陆");
			Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
			intent.putExtra("loginType", "join");
			JoinActivity.this.startActivity(intent);
		}
	}

	/**
	 * 获取合买佣金比列和合买最少购买比列
	 */
	public void getScaleParams() {
		requestUtil = new RequestUtil(context, false, 0) {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG,
							"获取合买佣金比列和合买最少购买比列jsonObject--- "
									+ jsonObject.toString());
				if ("0".equals(jsonObject.optString("error"))) {
					AppTools.followCommissionScale = jsonObject
							.optString("yongjin"); // 合买佣金比列
					AppTools.followLeastBuyScale = jsonObject
							.optString("rengou"); // 合买最少购买比列
					init();
				}
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取合买佣金比列和合买最少购买比列出错--- " + error.getMessage());
				init();
			}
		};
		requestUtil.getBuyParams();
	}

	/**
	 * 更改显示文本 *
	 */
	private void setTextChange() {
		Log.i(TAG, "改变了文本显示---");
		follow_tv_total_money.setText(totalMoney + "");
	}

	/**
	 * 销毁activity
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}

	/**
	 * 清除所有彩种所选数据
	 */
	public static void clearAllLotterySelectData() {
		Select_DLT_Activity.clearHashSet();
		Select_RX9_Activity.clearHashSet();
		Select_SFC_Activity.clearHashSet();
		Select_JCZQ_Activity.clearHashSet();
		Select_SSQ_Activity.clearHashSet();
	}

}
