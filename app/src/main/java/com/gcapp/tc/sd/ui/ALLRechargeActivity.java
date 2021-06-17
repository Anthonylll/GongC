package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：聚合充值
 * 
 * @author lenovo
 * 
 */
public class ALLRechargeActivity extends Activity implements OnClickListener {
	private static final String TAG = "ALLRechargeActivity";
	private Activity context = ALLRechargeActivity.this;
	private EditText et_money;
	private Button btn_recharge;
	private ImageButton btn_back;
//	private TextView tv_name;
	private double money;
	private TextView tv_title;
	private String Mark,Way;
	private String cardNumber;
	private TextView tv_card;
	/** 银行卡号*/
	private EditText card_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recharge);
		// // 检测安全支付服务是否被安装
		findView();
		init();
		setListener();
	}
	
	private void init() {
		money = 1;
		et_money.setSelection(et_money.getText().length());
		 Mark = getIntent().getStringExtra("mark");
		 Way = getIntent().getStringExtra("way");
		 
		if ("wx".equals(Mark)) {
			tv_title.setText("微信扫码支付");
		}else if("wxh5".equals(Mark)){
			tv_title.setText("微信支付");
		}else if ("upay".equals(Mark)) {
			tv_title.setText("银联支付");
		}else if ("zfb".equals(Mark)) {
			tv_title.setText("支付宝支付");
		}else if ("zfbkj".equals(Mark)) {
			tv_title.setText("支付宝快捷支付");
		}else if ("jd".equals(Mark)) {
			tv_title.setText("银联扫码支付");
		}else if ("qq".equals(Mark)) {
			tv_title.setText("QQ扫码支付");
		}else if ("juhe".equals(Mark)) {
			tv_title.setText("微信支付");
		}else if ("wxsm".equals(Mark)) {
			tv_title.setText("微信扫码支付");
		}else {
			tv_title.setText("支付宝支付H5");
		}
		
		if(Way.equals("913")){
			tv_card.setVisibility(View.VISIBLE);
			card_number.setVisibility(View.VISIBLE);
			String card = AppTools.user.getBankCard();
			if(card != null && !card.equals("")) {
				card_number.setText(card);
			}
		}else{
			tv_card.setVisibility(View.GONE);
			card_number.setVisibility(View.GONE);
		}
		
	}

	/** 初始化UI */
	private void findView() {
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		et_money = (EditText) this.findViewById(R.id.recahge_et_money);
		btn_recharge = (Button) this.findViewById(R.id.recharge_btn_ok);
//		tv_name = (TextView) this.findViewById(R.id.recahge_name2);
		tv_card = (TextView) this.findViewById(R.id.tv_card);
		card_number = (EditText) this.findViewById(R.id.card_number);
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
				if (et_money.getText().length() == 0)
					money = 1.00;
				else
					money = Double.parseDouble(et_money.getText().toString());
				Intent intent = new Intent(ALLRechargeActivity.this,
						Web_RechargeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("money", money + "");
				bundle.putString("card", cardNumber + "");
				bundle.putString("mark", Mark);
				bundle.putString("way", Way);
				intent.putExtra("bundle", bundle);
				ALLRechargeActivity.this.startActivity(intent);
			}
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	public boolean check() {
		String text = et_money.getText() + "";
		cardNumber = card_number.getText().toString();
		if(Way.equals("913") && cardNumber.equals("") || cardNumber == null){
			MyToast.getToast(ALLRechargeActivity.this, "银行卡号不能为空");
			return false;
		}
		boolean isPass = true;
		if (!"".equals(text)) {
			text = text + ".0";
			double money_text = Double.parseDouble(text);
			if (money_text < 1) {
				et_money.setText(1 + "");
				money = 1;
				MyToast.getToast(ALLRechargeActivity.this, "至少充值1元");
				isPass = false;
			} else if (money_text > 10000) {// 输入金额大于可用余额
				et_money.setText(10000 + "");
				money = 10000;
				MyToast.getToast(ALLRechargeActivity.this, "单笔充值金额最多10000元");
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

}
