package com.android.volley.toolbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.gcapp.tc.utils.FileUtils;

import java.io.IOException;

/**
 * 图片缓存实现类，采用LruCache和内存二级缓存方案。 Created by Mike on 2015/5/6.
 */
public class ImageCacheImpl implements ImageLoader.ImageCache {
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * 操作文件相关类对象的引用
	 */
	private FileUtils fileUtils;

	public ImageCacheImpl(Context context) {
		// 获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;
		// 给LruCache分配1/8 4M
		mLruCache = new LruCache<String, Bitmap>(mCacheSize) {

			// 必须重写此方法，来测量Bitmap的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		fileUtils = new FileUtils(context);
	}

	@Override
	public Bitmap getBitmap(String url) {
		return showCacheBitmap(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		try {
			// 保存在SD卡或者手机目录
			fileUtils.savaBitmap(url, bitmap);
		} catch (IOException e) {
			Log.e("IOException", "ImageCacheImpl.putBitmap()=>" + e.toString());
		}
		// 将Bitmap 加入LruCache缓存
		addBitmapToCache(url, bitmap);
	}

	/**
	 * 添加Bitmap到LruCache缓存
	 */
	public void addBitmapToCache(String key, Bitmap bitmap) {
		if (getBitmapFromCache(key) == null && bitmap != null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache缓存中获取一个Bitmap
	 */
	public Bitmap getBitmapFromCache(String key) {
		return mLruCache.get(key);
	}

	/**
	 * 获取Bitmap, LruCache中没有就去手机或者sd卡中获取
	 * 
	 * @param url
	 *            图片url
	 * @return Bitmap
	 */
	public Bitmap showCacheBitmap(String url) {
		if (getBitmapFromCache(url) != null) {
			return getBitmapFromCache(url);
		} else if (fileUtils.isFileExists(url)
				&& fileUtils.getFileSize(url) != 0) {
			// 从SD卡获取手机里面获取Bitmap
			Bitmap bitmap = fileUtils.getBitmap(url);
			// 将Bitmap 加入缓存
			addBitmapToCache(url, bitmap);
			return bitmap;
		}

		return null;
	}

}
