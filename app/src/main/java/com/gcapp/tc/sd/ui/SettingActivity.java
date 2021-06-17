package com.gcapp.tc.sd.ui;

import java.lang.ref.WeakReference;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.gcapp.tc.utils.App;
import com.gcapp.tc.utils.AppTools;
import com.gcapp.tc.utils.MyPushTask;
import com.gcapp.tc.utils.RequestUtil;
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
 * 功能：个人中心的设置界面
 * 
 * @author lenovo
 */
public class SettingActivity extends Activity implements OnClickListener {
	private static final String TAG = "SettingActivity";
	private Context context = SettingActivity.this;
	private RelativeLayout rl_suggest, rl_share, setting_ll_update,
			setting_ll_resetpass, setting_ll_push, setting_ll_about,setting_ll_resetbank;
	private ImageButton btn_random, btn_vibrator, btn_back;
	private Button setting_exit;
	private TextView settring_tv_updateNum;
//	private MyHandler myHandler;
	private SharedPreferences settings;
	private Editor editor;
	private ShareAction mShareAction;
	private UMShareListener mShareListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_appsetting);
		findView();
		initBtn();
		setListener();
	}

	/**
	 * 初始化UI控件
	 */
	private void findView() {
//		myHandler = new MyHandler();
		rl_suggest = (RelativeLayout) this
				.findViewById(R.id.setting_ll_suggest);
		rl_share = (RelativeLayout) this.findViewById(R.id.setting_ll_share);
		setting_ll_update = (RelativeLayout) this
				.findViewById(R.id.setting_ll_update);
		setting_ll_resetpass = (RelativeLayout) this
				.findViewById(R.id.setting_ll_resetpass);
		setting_ll_resetbank = (RelativeLayout) this
				.findViewById(R.id.setting_ll_resetbank);
		setting_ll_push = (RelativeLayout) this
				.findViewById(R.id.setting_ll_push);
		setting_ll_about = (RelativeLayout) this
				.findViewById(R.id.setting_ll_about);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		settring_tv_updateNum = (TextView) this
				.findViewById(R.id.settring_tv_updateNum);
		btn_random = (ImageButton) this.findViewById(R.id.setting_btn_random);
		btn_vibrator = (ImageButton) this
				.findViewById(R.id.setting_btn_vibrator);
		setting_exit = (Button) findViewById(R.id.setting_exit);
		settings = getSharedPreferences("app_user", 0);
		editor = settings.edit();
		mShareListener = new CustomShareListener(this);
		mShareAction = new ShareAction(SettingActivity.this).setDisplayList(
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
						web.setThumb(new UMImage(SettingActivity.this,
								R.drawable.main_logo));
						new ShareAction(SettingActivity.this).withMedia(web)
								.setPlatform(share_media)
								.setCallback(mShareListener).share();
					}
				});
	}

	/**
	 * 控制选号机选和震动
	 */
	private void initBtn() {
		if (settings.contains("isSensor")) {
			AppTools.isSensor = settings.getBoolean("isSensor", true);
		}
		if (settings.contains("isVibrator")) {
			AppTools.isVibrator = settings.getBoolean("isVibrator", true);
		}
		if (!AppTools.isSensor)
			btn_random.setSelected(false);
		else
			btn_random.setSelected(true);

		if (!AppTools.isVibrator)
			btn_vibrator.setSelected(false);
		else
			btn_vibrator.setSelected(true);
		settring_tv_updateNum.setText("当前版本" + AppTools.version);
	}

	/**
	 * 绑定监听
	 */
	private void setListener() {
		rl_suggest.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		setting_ll_update.setOnClickListener(this);
		setting_ll_resetpass.setOnClickListener(this);
		setting_ll_resetbank.setOnClickListener(this);
		setting_ll_push.setOnClickListener(this);
		setting_ll_about.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_random.setOnClickListener(this);
		btn_vibrator.setOnClickListener(this);
		setting_exit.setOnClickListener(this);
	}

	/**
	 * 公用监听方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_ll_update:// 检测版本更新
			Version_Update();
			break;

		case R.id.setting_ll_suggest:
			Intent intent = new Intent(SettingActivity.this,
					SuggestActivity.class);
			SettingActivity.this.startActivity(intent);
			break;

		case R.id.setting_ll_share: // 分享
			mShareAction.open();
			break;

		case R.id.setting_ll_push:// 推送管理
			Intent intent_push = new Intent(context, PushManageActivity.class);
			context.startActivity(intent_push);
			break;

		case R.id.setting_ll_resetpass:// 修改密码
			Intent intent_repass = new Intent(context,
					UpdatePasswordActivity.class);
			context.startActivity(intent_repass);
			break;
		case R.id.setting_ll_resetbank:
			Intent intent_rebank = new Intent(context,
					CheckPhoneNumber.class);
			intent_rebank.putExtra("type", "bank");
			context.startActivity(intent_rebank);
			break;

		/** 全程关闭打开摇一摇 **/
		case R.id.setting_btn_random:
			AppTools.isSensor = !AppTools.isSensor;
			editor.putBoolean("isSensor", AppTools.isSensor);
			editor.commit();
			setBtnText(btn_random, AppTools.isSensor);
			break;

		/** 全程关闭打开震动 **/
		case R.id.setting_btn_vibrator:
			AppTools.isVibrator = !AppTools.isVibrator;
			editor.putBoolean("isVibrator", AppTools.isVibrator);
			editor.commit();
			setBtnText(btn_vibrator, AppTools.isVibrator);
			break;

		case R.id.setting_exit:
			exitLogin();
			break;

		case R.id.setting_ll_about:// 关于
			aboutApp();
			break;

		case R.id.btn_back:
			finish();
			break;
		}
	}

	private static class CustomShareListener implements UMShareListener {
		private WeakReference<SettingActivity> mActivity;

		private CustomShareListener(SettingActivity activity) {
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
					// Log.d("throw", "throw:" + t.getMessage());
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
	 * 跳转到关于
	 */
	private void aboutApp() {
		Intent intent = new Intent(context, AboutAppActivity.class);
		this.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 设置图片按钮是否被选定
	 * 
	 * @param btn
	 *            ：图片按钮对象
	 * @param isOpen
	 *            ：是否被选中
	 */
	private void setBtnText(ImageButton btn, boolean isOpen) {
		if (isOpen)
			btn.setSelected(true);
		else
			btn.setSelected(false);
	}

	/**
	 * 检测版本更新
	 */
	public void Version_Update() {
		RequestUtil requestUtil = new RequestUtil(context, false, 0, true,
				"正在检测版本...") {
			@Override
			public void responseCallback(JSONObject jsonObject) {
				if (RequestUtil.DEBUG)
					Log.i(TAG, "版本更新的result：" + jsonObject);

				AppTools.appobject.setUpgrade(jsonObject.optString("upgrade"));
				// AppTools.appobject.setApkName(jsonObject.optString("apkName"));
				AppTools.appobject.setVersionCode(jsonObject
						.optString("currentappversion"));
				AppTools.appobject.setDownLoadUrl(jsonObject
						.optString("downLoadUrl"));
				if (AppTools.appobject.getUpgrade().equals("True")) {
					// return "SU";
					// 这里来检测版本是否需要更新
					UpdateManager mUpdateManager = new UpdateManager(context,
							AppTools.appobject.getDownLoadUrl());
					mUpdateManager.checkUpdateInfo();

				} else {
					MyToast.getToast(context, "当前版本为最新版本");
					// return "False";
				}

				if (jsonObject.toString().equals("-500")) {
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
		requestUtil.getVersion(AppTools.getVerName(getApplicationContext()));
	}

	/**
	 * 处理页面显示的
	 */
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 更新
					// 这里来检测版本是否需要更新
				UpdateManager mUpdateManager = new UpdateManager(context,
						AppTools.appobject.getDownLoadUrl());
				mUpdateManager.checkUpdateInfo();
				break;
			case -1:// 当前是最新版本
				MyToast.getToast(context, "当前版本为最新版本");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	/**
	 * 重新进入界面刷新数据
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 退出登陆
	 */
	private void exitLogin() {
		AppTools.user = null;
		AppTools.username = "";
		AppTools.userpass = "";
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		settings = context.getSharedPreferences("app_user", 0);
		Editor editor = settings.edit();
		editor.putBoolean("isLogin", false);
		editor.putString("uid", "-1");
		editor.commit();
		AppTools.Status = "0";
		MyPushTask.newInstances(context).commit();
		App.killAllActivity();
		finish();
	}

}
