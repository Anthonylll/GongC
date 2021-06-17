package com.gcapp.tc.dataaccess;

import java.io.Serializable;

/**
 * 首页图片轮播的图片类
 * 
 * @author lenovo
 * 
 */
public class ImageViews implements Serializable {
	private String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
