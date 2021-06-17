package com.gcapp.tc.sd.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.BaseHelper;
import com.gcapp.tc.utils.RequestUtil;
import com.gcapp.tc.view.MyToast;
import com.gcapp.tc.R;

/**
 * AboutAppActivity是个人中心的关于
 * 
 * @author lenovo
 */
public class AboutAppActivity extends Activity implements OnClickListener{
	
	private static final String TAG = "AboutAppActivity";
	private Context context = AboutAppActivity.this;
	// 图片
//	ImageView iv_about;
	// 返回键
	ImageButton btn_back;
	// 版本信息
	TextView tv_about_version;
	// 公司名称
	TextView tv_about_company,customer_service_btn;
	// 站点名称/电话
	TextView tv_about_phone_name;
	// 公司网站
	private String imgUrl;// 图片路径
//	private String URL;// 站点URL
	private String appVersion;// APP版本
	private String companyName;// 公司名称
	private String servicePhone;// 客服电话
	/** 客服QQ*/
	private String qqNumber;
//	private String siteName;// 站点名
	private TextView tv_about_hotline, tv_call;
	private MyHandler2 myHandler2;
	private Bitmap bitmap = null;
	private ProgressDialog mProgress = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_aboutapp);
		getSiteNameAndPhone();
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		initView();
		appVersion = AppTools.version;
		if (!"".equals(servicePhone)) {
//			tv_about_hotline.setText("客服热线：" + servicePhone);
		}
		if (appVersion != null && !"".equals(appVersion)) {
			tv_about_version.setText("当前版本：" + appVersion);
		}
		
		if (!"".equals(companyName)) {
			tv_about_company.setText("版权所有：" + companyName);
		}
		if (imgUrl != null && !"".equals(imgUrl)) {
			mProgress = BaseHelper.showProgress(AboutAppActivity.this,
					null, "加载中...", false, true);
			MyAsynTask2 myAsynTask2 = new MyAsynTask2();
			myAsynTask2.execute();
		}

//		tv_call.setOnClickListener(this);
		customer_service_btn.setOnClickListener(this);
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
	
	/**
	 * 控件初始化
	 */
	private void initView() {
		myHandler2 = new MyHandler2();
		// 图片
//		iv_about = (ImageView) findViewById(R.id.iv_about);
		// 热线
		tv_about_hotline = (TextView) findViewById(R.id.tv_about_hotline);
		tv_call = (TextView) findViewById(R.id.tv_call);
		tv_about_hotline.setVisibility(View.GONE);
		tv_call.setVisibility(View.GONE);
		// 返回键
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		// 版本信息
		tv_about_version = (TextView) findViewById(R.id.tv_about_version);
		// 公司名称
		tv_about_company = (TextView) findViewById(R.id.tv_about_company);
		// 站点名称/电话
		tv_about_phone_name = (TextView) findViewById(R.id.tv_about_phone_name);
		// 公司网站
		customer_service_btn = (TextView) findViewById(R.id.customer_service_btn);
	}

	/*** 异步任务 用来后台获取数据 */
	class MyAsynTask2 extends AsyncTask<Void, Integer, String> {
		/** 在后台执行的程序 */
		@Override
		protected String doInBackground(Void... params) {
			URL myFileUrl = null;
			try {
				myFileUrl = new URL(imgUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				if (null == bitmap) {
					return "-1";
				}
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "11";
		}

		@Override
		protected void onPostExecute(String result) {
			myHandler2.sendEmptyMessage(Integer.parseInt(result));
			super.onPostExecute(result);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler2 extends Handler {
		@Override
		public void handleMessage(Message msg) {
			closeProgress();
			
			switch (msg.what) {
			case 11:// 成功
//				iv_about.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	/**
	 * 获取站点名称和客服电话
	 */
	public void getSiteNameAndPhone() {
		RequestUtil requestUtil = new RequestUtil(AboutAppActivity.this, true,
				Request.CONFIG_CACHE_MEDIUM, true, "正在加载信息...") {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG,
							"获取站点名称和客服电话jsonObject--- " + jsonObject.toString());
				String name = jsonObject.optString("SiteName");
				String phone = jsonObject.optString("Phone");
				String company = jsonObject.optString("CompanyName");
				String qq = jsonObject.optString("QQ");
				String url = jsonObject.optString("SiteURL");
				String img = jsonObject.optString("SiteImg");
				if (!"".equals(name)) {
					AppTools.APP_NAME = name;
//					siteName = name;
				}
				if (!"".equals(phone)) {
					AppTools.SERVICE_PHONE = phone;
					servicePhone = phone;
				}
				if (!"".equals(company)) {
					companyName = company;
				}
				if (!"".equals(img)) {
					imgUrl = img;
				}
				if (!"".equals(qq)) {
					qqNumber = qq;
				}
				init();
			}

			@Override
			public void responseError(VolleyError error) {
				if (RequestUtil.DEBUG)
					Log.e(TAG, "获取站点名称和客服电话出错--- " + error.getMessage());
				MyToast.getToast(context, "服务器错误，请重试。");
				init();
				AboutAppActivity.this.finish();
			}
		};
		requestUtil.getSiteNameAndPhone();
	}

	/**
	 * 返回键监听
	 * 
	 * @param view
	 */
	public void back(View view) {
		this.finish();
	}

	/**
	 * 网站色红孩子样式和点击链接监听事件
	 * 
	 * @author lenovo
	 * 
	 */
//	private class MyURLSpan extends ClickableSpan {
//
//		private String mUrl;
//
//		MyURLSpan(String url) {
//			mUrl = url;
//		}
//
//		@Override
//		public void onClick(View widget) {
//			Intent intent = new Intent();
//			intent.setAction("android.intent.action.VIEW");
//			Uri content_url = Uri.parse(URL);
//			intent.setData(content_url);
//			startActivity(intent);
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_call:
			if (null != servicePhone && !"".equals(servicePhone)) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + servicePhone));
				startActivity(intent);
			} else {
				MyToast.getToast(context, "暂无客服热线!");
			}
			break;
		case R.id.customer_service_btn:
			if(isQQClientAvailable(context)){
                final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+qqNumber+"&version=1";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
            }else{
                Toast.makeText(context,"请安装QQ客户端",Toast.LENGTH_SHORT).show();
            }
			break;
		default:
			break;
		}
	}
	
	/**
	 * 是否安装了QQ客户端
	 * @param context
	 * @return
	 */
	private boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
