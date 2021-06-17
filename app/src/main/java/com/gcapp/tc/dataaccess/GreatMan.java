package com.gcapp.tc.dataaccess;

/**
 * 大神实体类
 */
public class GreatMan {
	
	private String img;
	private String name;
	/**大神id*/
	private String manID;
	private String orderCount;
	
	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getManID() {
		return manID;
	}
	
	public void setManID(String manID) {
		this.manID = manID;
	}
	
}
