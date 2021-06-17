package com.gcapp.tc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件操作类
 */
public class FileUtils {
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment
			.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = "/ShoveImage";

	public FileUtils(Context context) {
		mDataRootPath = context.getCacheDir().getPath();
	}

	/**
	 * 获取储存Image的目录
	 * 
	 * @return 有sd卡返回sd卡上的目录，否则就返回到手机内存目录
	 */
	private String getStorageDirectory() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME
				: mDataRootPath + FOLDER_NAME;
	}

	/**
	 * 保存Image的方法
	 * 
	 * @param fileName
	 *            名称
	 * @param bitmap
	 *            图片
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap) throws IOException {
		if (bitmap == null) {
			return;
		}
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		File file = new File(path + File.separator
				+ getFilenameForUrl(fileName));
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 从手机或者sd卡获取Bitmap
	 * 
	 * @param fileName
	 *            文件名
	 * @return Bitmap
	 */
	public Bitmap getBitmap(String fileName) {
		return BitmapFactory.decodeFile(getStorageDirectory() + File.separator
				+ getFilenameForUrl(fileName));
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 *            文件名
	 * @return boolean
	 */
	public boolean isFileExists(String fileName) {
		return new File(getStorageDirectory() + File.separator
				+ getFilenameForUrl(fileName)).exists();
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param fileName
	 *            文件名
	 * @return long
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator
				+ getFilenameForUrl(fileName)).length();
	}

	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}

		dirFile.delete();
	}

	/**
	 * Creates a pseudo-unique filename for the specified cache key.
	 * 
	 * @param key
	 *            The key to generate a file name for.
	 * @return A pseudo-unique filename.
	 */
	private String getFilenameForUrl(String key) {
		if (key.contains("http://") || key.contains("https://")) {
			// 将url转换成唯一普通的字符串
			int firstHalfLength = key.length() / 2;
			String localFilename = String.valueOf(key.substring(0,
					firstHalfLength).hashCode());
			localFilename += String.valueOf(key.substring(firstHalfLength)
					.hashCode());
			return localFilename;
		}
		return key;
	}
}