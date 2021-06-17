package com.gcapp.tc.utils;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageManager {
	/**
	 * 获取图片加载
	 * 
	 * @return
	 */
	public static ImageLoader getInstance() {
		return ImageLoader.getInstance();
	}

	/**
	 * 新闻图片缓存设置
	 */

	private static DisplayImageOptions newsHeadOptions;

	public static DisplayImageOptions getNewsHeadOptions() {
		if (newsHeadOptions == null) {
			newsHeadOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true) // 缓存内存
					.cacheOnDisc(true)// 缓存文件
					.build();
		}
		return newsHeadOptions;
	}

	private static DisplayImageOptions viewsHeadOptions;

	public static DisplayImageOptions getViewsHeadOptions() {
		if (viewsHeadOptions == null) {
			viewsHeadOptions = new DisplayImageOptions.Builder()
					// .showImageOnLoading(R.drawable.gallery_default)
					// .showImageForEmptyUri(R.drawable.gallery_default)
					// .showImageOnFail(R.drawable.gallery_default)
					.resetViewBeforeLoading(false).cacheOnDisc(true)
					.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(100)).build();
		}
		return viewsHeadOptions;
	}

}