package com.android.volley.toolbox;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * 单例模式，构造全局唯一的RequestQueue实例和ImageLoader实例
 */
public class MySingleton {

	private static MySingleton mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

	private MySingleton(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();
		mImageLoader = new ImageLoader(mRequestQueue, new ImageCacheImpl(
				context));
	}

	// public static synchronized MySingleton getInstance(Context context) {
	// if (mInstance == null) {
	// mInstance = new MySingleton(context);
	// }
	// return mInstance;
	// }
	// 双重检查锁定
	public static MySingleton getInstance(Context context) {
		if (mInstance == null) {
			synchronized (MySingleton.class) {
				if (mInstance == null) {
					mInstance = new MySingleton(context);
				}
			}
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			mRequestQueue = Volley
					.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	/**
	 * 将request添加到请求队列
	 * 
	 * @param req
	 *            具体请求实现类
	 * @param <T>
	 *            请求实现类的数据返回类型
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * 停止整个请求队列
	 */
	public void stopRequestQueue() {
		getRequestQueue().stop();
	}

	/**
	 * 取消对应的tag值的请求
	 * 
	 * @param tag
	 *            标签
	 */
	public void cancelAll(final Object tag) {
		getRequestQueue().cancelAll(tag);
	}

	/**
	 * 去掉对应的request对应的缓存
	 * 
	 * @param cachekey
	 *            request对应的cacheKey
	 */
	public void dropCache(String cachekey) {
		getRequestQueue().getCache().remove(cachekey);
	}

}
