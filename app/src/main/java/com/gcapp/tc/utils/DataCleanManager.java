package com.gcapp.tc.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class DataCleanManager {

	/**
	 * 清除本应用内部缓存 对应路径 (/data/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */

	public static void cleanInternalCache(Context context) {

		deleteFilesByDirectory(context.getCacheDir());

	}

	/**
	 * 清除本应用数据库 对应路径 (/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */

	public static void cleanDatabases(Context context) {

		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));

	}

	/**
	 * 
	 * 清除sharedPreference (/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	public static void cleanSharedPreference(Context context) {

		deleteFilesByDirectory(new File("/data/data" + context.getPackageName()
				+ "/shared_prefs"));

	}

	/**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 */

	public static void cleanDataBaseByName(Context context, String dbName) {

		context.deleteDatabase(dbName);

	}

	/**
	 * 清除getFilesDir()下的内容; 对应路径 (/data/data/com.xxx.xxx/files)
	 * 
	 * @param context
	 */

	public static void cleanFiles(Context context) {

		deleteFilesByDirectory(context.getFilesDir());

	}

	/**
	 * 清除外部cache下的内容 对应路径 (/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			deleteFilesByDirectory(context.getExternalCacheDir());

		}

	}

	/**
	 * 清除自定义路径下的文件，且只支持目录下的文件删除
	 * 
	 * 
	 * @param fileName
	 */
	public static void cleanCustomCache(String fileName) {

		deleteFilesByDirectory(new File(fileName));

	}

	/**
	 * 清除本应用所有数据
	 * 
	 * @param context
	 * @param filePath
	 */
	public static void cleanApplication(Context context, String... filePath) {

		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanFiles(context);
		cleanSharedPreference(context);

		for (String filepath : filePath) {

			cleanCustomCache(filepath);

		}

	}

	/**
	 * 删除方法，这里只会删除某个文件夹下的文件，如果传入的directory是个文件，则不做处理。 File.isDirectory
	 * 返回true则表示路径是个目录文件夹.
	 * 
	 * @param directory
	 */

	private static void deleteFilesByDirectory(File directory) {

		if (directory != null && directory.exists() && directory.isDirectory()) {

			for (File item : directory.listFiles()) {

				item.delete();

			}

		}

		if (directory != null && directory.exists()) {

			directory.delete();
		}

	}

}
