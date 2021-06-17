package com.gcapp.tc.sd.ui;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * 功能：充值选项卡界面
 * 
 * @author lenovo
 */
public class SelectRechargeTypeActivity extends Activity implements
		View.OnClickListener {
	private Activity context = SelectRechargeTypeActivity.this;
	public static final String TAG = "SelectRechargeTypeActivity";
	/** 银联支付*/
	private RelativeLayout layout_uppay, layout_unionpay_two;
	/** 支付宝支付*/
	private RelativeLayout layout_alipay;
	/** 支付宝快捷支付*/
	private RelativeLayout layout_zhifubao,layout_alipay2;
	/** 京东支付 */
	private RelativeLayout layout_jd;
	/** QQ支付 */
	private RelativeLayout layout_qq;
	/** 微信支付 */
	private RelativeLayout layout_juhe;
	/** 微信扫码支付 */
	private RelativeLayout layout_weixin_saoma;
	/** 人工 */
	private RelativeLayout layout_artificial,layout_artificial_weixin;
	private ImageButton btn_back;// 返回
	private TextView tv_tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_paytype);
		findView();
		setListener();
		init();
	}

	/**
	 * 初始化UI控件
	 */
	public void findView() {
		layout_uppay = (RelativeLayout) findViewById(R.id.layout_uppay); // 快捷1
		layout_unionpay_two = (RelativeLayout) findViewById(R.id.layout_unionpay_two); // 快捷2
		layout_alipay = (RelativeLayout) findViewById(R.id.layout_alipay);// 支付宝
		layout_zhifubao = (RelativeLayout) findViewById(R.id.layout_zhifubao);// 支付宝快捷
		layout_alipay2 = (RelativeLayout) findViewById(R.id.layout_alipay2);
		layout_jd = (RelativeLayout) findViewById(R.id.layout_jd);// 银联扫码
		layout_qq = (RelativeLayout) findViewById(R.id.layout_qq);// qq扫码
		layout_juhe = (RelativeLayout) findViewById(R.id.layout_juhe);// 微信
		layout_weixin_saoma = (RelativeLayout) findViewById(R.id.layout_weixin_saoma);
		layout_artificial = (RelativeLayout) findViewById(R.id.layout_artificial);
		layout_artificial_weixin = (RelativeLayout) findViewById(R.id.layout_artificial_weixin);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		tv_tip = (TextView) findViewById(R.id.tv_tip);

	}

	/**
	 * 绑定界面监听
	 */
	public void setListener() {
		layout_uppay.setOnClickListener(this);
		layout_unionpay_two.setOnClickListener(this);
		layout_alipay.setOnClickListener(this);
		layout_zhifubao.setOnClickListener(this);
		layout_alipay2.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		layout_jd.setOnClickListener(this);
		layout_qq.setOnClickListener(this);
		layout_juhe.setOnClickListener(this);
		layout_weixin_saoma.setOnClickListener(this);
		layout_artificial.setOnClickListener(this);
		layout_artificial_weixin.setOnClickListener(this);
	}

	/**
	 * 初始化界面数据
	 */
	public void init() {
		getRechargeChannel();
		Spanned spanned = Html.fromHtml(AppTools.changeStringColor("#000000",
				"支付宝支付") + AppTools.changeStringColor("#e3393c", "（推荐）"));
		tv_tip.setText(spanned);
	}

	/**
	 * 获取支付方式
	 */
	private void getRechargeChannel() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...", 0) {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "支付方式请求结果(86)" + object);
				try {
					String error = object.optString("error");
					String info = object.optString("info");
					if (null != error && error.equals("0") && !info.equals("")
							&& info != error) {
						JSONArray jsonArray = new JSONArray(info);
						for (int i = 0; i < jsonArray.length(); i++) {
							String rechargeCode = jsonArray.getJSONObject(i)
									.getString("TypeValue");
							setViewVisibility(rechargeCode);
						}
					} else {
						MyToast.getToast(context, object.optString("msg"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		requestUtil.getRecharge();
	}

	/**
	 * 设置视图的显示
	 */
	private void setViewVisibility(String code) {
		if (code.equals("2001")) {
			layout_alipay.setVisibility(View.VISIBLE);
		} else if (code.equals("2002")) {
			layout_juhe.setVisibility(View.VISIBLE);
		} else if (code.equals("2003")) {
			layout_uppay.setVisibility(View.VISIBLE);
		} else if (code.equals("2004")) {
			layout_unionpay_two.setVisibility(View.VISIBLE);
		} else if (code.equals("2005")) {
			layout_jd.setVisibility(View.VISIBLE);
		} else if (code.equals("2006")) {
			layout_qq.setVisibility(View.VISIBLE);
		}else if (code.equals("2007")) {
			layout_zhifubao.setVisibility(View.VISIBLE);
		}else if (code.equals("2010")) {
			layout_weixin_saoma.setVisibility(View.VISIBLE);
		}else if (code.equals("2011")) {
			layout_artificial.setVisibility(View.VISIBLE);
		}else if (code.equals("2009")) {
			layout_alipay2.setVisibility(View.VISIBLE);
		}else if (code.equals("2013")) {
			layout_artificial_weixin.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 公用点击监听
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回
			finish();
			break;
		case R.id.layout_uppay:// 快捷支付1
			Intent intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "upay");
			intent.putExtra("way", "913");
			context.startActivity(intent);
			break;

		case R.id.layout_alipay:// 支付宝支付
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "zfb");
			intent.putExtra("way", "1001");
			context.startActivity(intent);
			break;
		case R.id.layout_zhifubao:// 支付宝快捷支付
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "zfbkj");
			intent.putExtra("way", "904");
			context.startActivity(intent);
			break;
		case R.id.layout_alipay2:// 支付宝扫码支付
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "zfbh5");
			intent.putExtra("way", "903");
			context.startActivity(intent);
			break;
		case R.id.layout_jd:// 银联扫码
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "jd");
			intent.putExtra("way", "910");
			context.startActivity(intent);
			break;
		case R.id.layout_qq:// QQ扫码支付
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "qq");
			intent.putExtra("way", "908");
			context.startActivity(intent);
			break;
		case R.id.layout_juhe:// 微信支付
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "juhe");
			intent.putExtra("way", "901");
			context.startActivity(intent);
			break;
		case R.id.layout_weixin_saoma:// 微信扫码
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "wxsm");
			intent.putExtra("way", "902");
			context.startActivity(intent);
			break;
		case R.id.layout_unionpay_two: //快捷支付2
			intent = new Intent(context, ALLRechargeActivity.class);
			intent.putExtra("mark", "upay");
			intent.putExtra("way", "907");
			context.startActivity(intent);
			break;
		case R.id.layout_artificial: //人工
			intent = new Intent(context, ArtificialPayActivity.class);
			intent.putExtra("mark", "2011");
			context.startActivity(intent);
			break;
		case R.id.layout_artificial_weixin: //人工微信
			intent = new Intent(context, ArtificialPayActivity.class);
			intent.putExtra("mark", "2013");
			context.startActivity(intent);
			break;
		}
	}
}
