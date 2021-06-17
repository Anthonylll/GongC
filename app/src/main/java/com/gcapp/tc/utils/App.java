package com.gcapp.tc.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 用来存放所有Activity
 * 
 * @author SLS003
 */
public class App extends FrontiaApplication {
	public static ArrayList<Activity> activityS = new ArrayList<Activity>();
	public static ArrayList<Activity> activityS1 = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();

//		Config.DEBUG = true;
//		QueuedWork.isUseThreadPool = false;
		UMShareAPI.get(this);

		initImageLoader(getApplicationContext());
		CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());
		// 发送以前没发送的报告(可选)
		crashHandler.sendPreviousReportsToServer();
	}

	// 各个平台的配置，建议放在全局Application或者程序入口
	{
		PlatformConfig.setWeixin("wxdc1e388c3822c80b",
				"3baf1193c85774b3fd9d18447d76cab0");
		PlatformConfig.setQQZone("100424468",
				"c7394704798a158208a74ab60104f0ba");
		// PlatformConfig
		// .setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad",
		// "http://sns.whalecloud.com");
		PlatformConfig
				.setSinaWeibo("3921700954", "9c29cc8bc30f5edc087bde312ef083a1",
						"http://sns.whalecloud.com");

		 PlatformConfig.setAlipay("2015111700822536");
		// 豆瓣RENREN平台目前只能在服务器端配置
		PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
		PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi",
				"MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
		PlatformConfig.setLaiwang("laiwangd497e70d4",
				"d497e70d4c3e4efeab1381476bac4c5e");
		PlatformConfig.setPinterest("1439206");
		PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
		PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
		PlatformConfig.setVKontakte("5764965", "5My6SNliAaLxEm3Lyd9J");
		PlatformConfig.setDropbox("oz8v5apet3arcdy", "h7p2pjbzkkxt02a");
	}

	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 结束所有Activity
	 */
	public static void killAllActivity() {
		for (int i = 0, size = activityS.size(); i < size; i++) {
			if (null != activityS.get(i)) {
				activityS.get(i).finish();
			}
		}
		activityS.clear();
	}
}
