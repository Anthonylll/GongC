package com.gcapp.tc.sd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;
import org.json.JSONObject;
import java.util.Map;

public class Web_RechargeActivity extends Activity {

	private Activity context = Web_RechargeActivity.this;
	private String TAG = "Web_RechargeActivity";
	private WebView webview;
	String Url;
	String money;
	String uid = "";
	private TextView btn_playtype;
	private ImageButton btn_back;
	private ImageButton btn_help;
	private ImageView iv_up_down;
	private ProgressBar top_progressbar;
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_recharge);
		findView();
		init();
	}

	/** 初始化UI */
	private void findView() {
		webview = (WebView) this.findViewById(R.id.webView1);
		btn_playtype = (TextView) this.findViewById(R.id.btn_playtype);
		btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		btn_help = (ImageButton) this.findViewById(R.id.btn_help);
		iv_up_down = (ImageView) this.findViewById(R.id.iv_up_down);
		top_progressbar = (ProgressBar) this.findViewById(R.id.top_progressbar); 
		
		iv_up_down.setVisibility(View.GONE);
		btn_help.setVisibility(View.GONE);
		btn_playtype.setText("充值");
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		money = getIntent().getBundleExtra("bundle").getString("money");
		String way = getIntent().getBundleExtra("bundle").getString("way");// 区分支付宝和微信
		String card = getIntent().getBundleExtra("bundle").getString("card");
		webview.requestFocusFromTouch();
		webview.requestFocus(View.FOCUS_DOWN);
		webview.setInitialScale(100);
		WebSettings websetting = webview.getSettings();
		websetting.setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new callback(), "Interface");
		if (null != AppTools.user && !"".equals(AppTools.user)) {
			uid = AppTools.user.getUid();
		}
		// H5页面。 参数是 Money：交易的金额和UID：用户Id
		if(way.equals("904")) {
			getPayorder(money);
		}else{
			Url = AppTools.url + "/Home/Room/OnlinePay/Boyapay/Send.aspx?id=" + uid
					+ "&money=" + money + "&way=" + way;
			webview.loadUrl(Url);
		}

//		if(!way.equals("913")){
//			// 如果页面中链接，如果希望点击链接继续在当前browser中响应，
//			webview.setWebViewClient(new WebViewClient() {
//				public boolean shouldOverrideUrlLoading(WebView view, String url) {
//					System.err.println("url===" + url);
//					if (url.startsWith("alipays://") || url.startsWith("weixin://")) {
//						// 调用系统默认浏览器处理url
//						view.stopLoading();
//						view.getContext().startActivity(
//								new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//						return true;
//					}
//					return false;
//				}
//			});
//		}
		
		webview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				top_progressbar.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}
		});
	}

	/**
	 * 获取支付订单
	 */
	private void getPayorder(String money) {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"加载中...", 0) {
			@Override
			public void responseCallback(JSONObject object) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "支付订单信息" + object);
				try {
					String error = object.optString("error");
					String orderinfo = "";
					if(object.has("orderresult")) {
						orderinfo = object.optString("orderresult");
					}
					if (null != error && error.equals("0") && !orderinfo.equals("")) {
						pay(orderinfo);
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
		requestUtil.getPayinfo(money,"904");
	}

	private void pay(final String info) {
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				PayTask alipay = new PayTask(context);
				Map<String,String> result = alipay.payV2(info,true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what){
				case SDK_PAY_FLAG:{
//					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//					Result result = new Result((String) msg.obj);
//					MyToast.getToast(Web_RechargeActivity.this, "充值成功");
				}
				default:
					break;
			}
			return false;
		}
	}) ;

	protected boolean openWithWevView(String url) {// 处理判断url的合法性
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return true;
		}
		return false;
	}

	class callback {
		@JavascriptInterface
		public void toActivity() {
			Web_RechargeActivity.this.startActivity(new Intent(
					Web_RechargeActivity.this, MainActivity.class));
			Web_RechargeActivity.this.finish();
			MyToast.getToast(Web_RechargeActivity.this, "充值成功");
		}
	}
}
