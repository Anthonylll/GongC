package com.gcapp.tc.sd.ui;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MySingleton;
import com.android.volley.toolbox.RequestParams;
import com.gcapp.tc.dataaccess.Information;
import com.gcapp.tc.protocol.RspBodyBaseBean;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.BaseHelper;
import com.gcapp.tc.utils.HttpUtils;
import com.gcapp.tc.utils.NetWork;
import com.gcapp.tc.view.MyToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.gcapp.tc.R;

/**
 * 功能：咨询模块的子项类
 * 
 * @author lenovo
 * 
 */
public class Information_Detail extends Activity implements OnClickListener {
	private String auth;
	private String info;
	private WebView tx_show;
	private TextView informationDetail_Datetime, informationDetail_recordCount,
			informationDetail_title, informationDetail_Current,
			informationDetail_Sum;
	private Button informationDetail_Pervious, informationDetail_Next;
	private ImageButton information_detail_share, btn_back;
	private Context context;
	private MyHandler handler;
	private List<Information> informations = new ArrayList<Information>();
	private Information informations_show = new Information();

	private int newType = 2;
	private long currentNewId;
	private long[] ids;
	private int newIndex = 1;
	private String TAG = "Information_Detail";
	private MySingleton mySingleton;
	private String url;
	private ShareAction mShareAction;
	private UMShareListener mShareListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_informationdetail);
		App.activityS.add(this);
		findView();
		initBundle();
		setListener();
		getInformation();
		getHttpRes(false);
	}

	/**
	 * 初始化自定义控件
	 */
	private void findView() {
		handler = new MyHandler();
//		information_detail_share = (ImageButton) findViewById(R.id.information_detail_share);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		tx_show = (WebView) this.findViewById(R.id.tx_show);
		informationDetail_Current = (TextView) this
				.findViewById(R.id.informationDetail_Current);
		informationDetail_Sum = (TextView) this
				.findViewById(R.id.informationDetail_Sum);
		informationDetail_title = (TextView) this
				.findViewById(R.id.information_detail_title);
		informationDetail_Datetime = (TextView) this
				.findViewById(R.id.informationDetail_Datetime);
//		informationDetail_recordCount = (TextView) this
//				.findViewById(R.id.informationDetail_recordCount);
		informationDetail_Pervious = (Button) this
				.findViewById(R.id.informationDetail_Pervious);
		informationDetail_Next = (Button) this
				.findViewById(R.id.informationDetail_Next);
		context = getApplicationContext();
		mySingleton = MySingleton.getInstance(context);

		mShareListener = new CustomShareListener(this);
		mShareAction = new ShareAction(Information_Detail.this).setDisplayList(
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.YIXIN,
				SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN)
				.setShareboardclickCallback(new ShareBoardlistener() {
					@Override
					public void onclick(SnsPlatform snsPlatform,
							SHARE_MEDIA share_media) {
						UMWeb web = new UMWeb(AppTools.url + "");
						web.setTitle("来自分享面板标题");
						web.setDescription("来自分享面板内容");
						web.setThumb(new UMImage(Information_Detail.this,
								R.drawable.main_logo));
						new ShareAction(Information_Detail.this).withMedia(web)
								.setPlatform(share_media)
								.setCallback(mShareListener).share();
					}
				});
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		informationDetail_Pervious.setOnClickListener(this);
		informationDetail_Next.setOnClickListener(this);
		btn_back.setOnClickListener(this);
//		information_detail_share.setOnClickListener(this);
	}

	/**
	 * 获取当前资讯的分享url
	 */
	private void getHttpRes(boolean force) {
		if (NetWork.isConnect(context)) {
			String opt = "67";
			String info = "{\"infoType\":\"" + newType + "\",\"urlId\":\""
					+ currentNewId + "\"}";
			JsonObjectRequest request = new JsonObjectRequest(AppTools.path,
					RequestParams.convertParams(context, opt, info),
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.i(TAG, "JSONObject response==>" + response);

							url = (String) response.opt("url");
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							RequestParams.convertError(context, error, true);
						}
					});
			request.setTag(TAG);
			request.setCacheTime(JsonRequest.CONFIG_CACHE_SHORT);
			if (force) {// 删除缓存
				mySingleton.dropCache(request.getCacheKey());
			}
			mySingleton.addToRequestQueue(request);

		} else {
			MyToast.getToast(context, "网络连接异常，请检查网络");
		}
	}

	/**
	 * 得到Bundle传过来的值
	 */
	private void initBundle() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("informationList");
		if (bundle != null) {
			newType = bundle.getInt("newType");
			currentNewId = bundle.getLong("currentNewId");
			ids = bundle.getLongArray("ids");
			newIndex = bundle.getInt("newIndex");
		}
	}

	/**
	 * 设置页面内容
	 */
	private void showContent() {
		informationDetail_Current.setText((newIndex + 1) + "");
		informationDetail_Sum.setText(ids.length + "");
		informationDetail_title.setText(informations_show.getTitle());
		informationDetail_Datetime.setText(informations_show.getDateTime());
//		informationDetail_recordCount.setText("来源："
//				+ getResources().getString(R.string.app_logo) + "网");
		if (informations_show.getContent() != null) {
			String Url = informations_show.getContent();
			WebSettings websetting = tx_show.getSettings();
			websetting.setJavaScriptEnabled(true);
			websetting.setSavePassword(false);
			websetting.setSupportZoom(false);
			websetting.setSaveFormData(false);
			tx_show.setWebChromeClient(new WebChromeClient());
			tx_show.loadUrl(Url);
			// 如果页面中链接，如果希望点击链接继续在当前browser中响应，
			// 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
			tx_show.setWebViewClient(new WebViewClient() {
				public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
					view.loadUrl(url);
					return true;
				}
			});
		} else
			tx_show.loadData("咨询已经修改", "text/html", "UTF-8");
	}

	/**
	 * 请求资讯信息
	 */
	private void getInformation() {
		Boolean isrequest = true;
		for (Information information : informations) {
			if (information.getId() == currentNewId) {
				isrequest = false;
				informations_show = information;
				break;
			}
		}
		if (isrequest) {
			if (NetWork.isConnect(getApplicationContext())) {
				String mImei = RspBodyBaseBean.getIMEI(context);
				String mTime = RspBodyBaseBean.getTime();
				String uid = "-1";
				String password = "";
				if (null != AppTools.user) {
					uid = AppTools.user.getUid();
					password = AppTools.user.getUserPass();
				}
				info = RspBodyBaseBean.getInformationDetail(newType,
						currentNewId);
				String mCrc = RspBodyBaseBean.getCrc(mTime, mImei,
						AppTools.key, info, uid);
				auth = RspBodyBaseBean.getAuth(mCrc, mTime, mImei, uid);
				MyAsynTask mMyAsynTask = new MyAsynTask();
				mMyAsynTask.execute();
			} else {
				MyToast.getToast(getApplicationContext(), "网络连接异常，请检查网络");
			}
		} else {
			showContent();
		}

	}

	/**
	 * 上一条或者下一条
	 */
	private void previousOrNext() {
		getInformation();
		getHttpRes(false);
	}

	/**
	 * 公用点击监听处理方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

//		case R.id.information_detail_share:// 分享
//			mShareAction.open();
//			break;

		case R.id.informationDetail_Pervious:
			if (ids == null) {
				MyToast.getToast(getApplicationContext(), "数据异常");
				return;
			}
			if (ids.length <= 0) {
				MyToast.getToast(getApplicationContext(), "数据异常");
				return;
			} else if (ids.length == 1) {
				return;
			} else {
				if (newIndex == 0) {
					return;
				} else {
					newIndex = newIndex - 1;
				}
				currentNewId = ids[newIndex];
			}
			previousOrNext();
			break;

		case R.id.informationDetail_Next:
			if (ids == null) {
				MyToast.getToast(getApplicationContext(), "数据异常");
				return;
			}
			if (ids.length <= 0) {
				MyToast.getToast(getApplicationContext(), "数据异常");
				return;
			} else if (ids.length == 1) {
				return;
			} else {
				if (newIndex == ids.length - 1) {
					return;
				} else {
					newIndex = newIndex + 1;
				}
				currentNewId = ids[newIndex];
			}
			previousOrNext();
			break;

		default:
			break;
		}
	}

	private static class CustomShareListener implements UMShareListener {
		private WeakReference<Information_Detail> mActivity;

		private CustomShareListener(Information_Detail activity) {
			mActivity = new WeakReference(activity);
		}

		@Override
		public void onStart(SHARE_MEDIA platform) {
		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			if (platform.name().equals("WEIXIN_FAVORITE")) {
				Toast.makeText(mActivity.get(), platform + " 收藏成功啦",
						Toast.LENGTH_SHORT).show();
			} else {
				if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
						&& platform != SHARE_MEDIA.EMAIL
						&& platform != SHARE_MEDIA.FLICKR
						&& platform != SHARE_MEDIA.FOURSQUARE
						&& platform != SHARE_MEDIA.TUMBLR
						&& platform != SHARE_MEDIA.POCKET
						&& platform != SHARE_MEDIA.PINTEREST
						&& platform != SHARE_MEDIA.INSTAGRAM
						&& platform != SHARE_MEDIA.GOOGLEPLUS
						&& platform != SHARE_MEDIA.YNOTE
						&& platform != SHARE_MEDIA.EVERNOTE) {
					Toast.makeText(mActivity.get(), platform + " 分享成功啦",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
					&& platform != SHARE_MEDIA.EMAIL
					&& platform != SHARE_MEDIA.FLICKR
					&& platform != SHARE_MEDIA.FOURSQUARE
					&& platform != SHARE_MEDIA.TUMBLR
					&& platform != SHARE_MEDIA.POCKET
					&& platform != SHARE_MEDIA.PINTEREST
					&& platform != SHARE_MEDIA.INSTAGRAM
					&& platform != SHARE_MEDIA.GOOGLEPLUS
					&& platform != SHARE_MEDIA.YNOTE
					&& platform != SHARE_MEDIA.EVERNOTE) {
				Toast.makeText(mActivity.get(), platform + " 分享失败啦",
						Toast.LENGTH_SHORT).show();
				if (t != null) {
					Log.d("throw", "throw:" + t.getMessage());
				}
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(mActivity.get(), platform + " 分享取消了",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 解析json对象
	 * 
	 * @param items
	 * @return
	 */
	private String addInformation(JSONObject items) {
		if (informations_show == null) {
			informations_show = new Information();
		}
		currentNewId = items.optLong("informationId");
		informations_show.setId(currentNewId);
		informations_show.setReadCount(items.optInt("readCount"));
		informations_show.setParentTypeId(newType);
		informations_show.setDateTime(items.optString("dateTime"));
		informations_show.setUrl(items.optString("URL"));
		String title = items.optString("title");
		title = title.replace("&quot;", "\"");
		informations_show.setTitle(title);

		try {
			informations_show.setContent(URLDecoder.decode(
					items.optString("content"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			VolleyLog.e("UnsupportedEncodingException=>%s", e.toString());
			return "2";
		}
		informations.add(informations_show);
		return "1";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 异步任务 用来后台获取数据
	 */
	class MyAsynTask extends AsyncTask<Void, Integer, String> {
		/**
		 * 在后台执行的程序
		 */
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = BaseHelper.showProgress(Information_Detail.this, null,
					"加载中..", false, true);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String mOpt = "45";
			String values[] = { mOpt, auth, info };
			String result = HttpUtils.doPost(AppTools.names, values,
					AppTools.path);
			System.out.println("++++=====result++++" + result);
			if ("-500".equals(result)) {
				return result;
			}
			JSONObject item = null;
			try {
				item = new JSONObject(result);
			} catch (Exception ex) {
				VolleyLog.e("Exception=>%s", ex.toString());
			}
			if (item != null) {
				if ("0".equals(item.optString("error"))) {
					int i = -1;
					for (int j = 0; j < informations.size(); j++) {
						if (informations.get(j).getId() == currentNewId) {
							i = j;
						}
					}
					if (i != -1) {
						informations.remove(i);
					}
					return addInformation(item);
				} else {
					VolleyLog.e("msg=>%s", item.optString("msg"));
				}
			}
			return "2";
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			handler.sendEmptyMessage(1);
			super.onPostExecute(result);
		}
	}

	/**
	 * 处理请求结果，刷新页面
	 * 
	 * @author lenovo
	 * 
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showContent();
				break;
			case 2:
				MyToast.getToast(getApplicationContext(), "暂无内容");
				break;
			case -500:
				MyToast.getToast(Information_Detail.this, "连接超时");
				break;
			}
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&tx_show.canGoBack()){
        	tx_show.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
