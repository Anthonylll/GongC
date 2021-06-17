package com.gcapp.tc.dataaccess;

/**
 * 积分中心实体类
 * 
 * @author lenovo
 * 
 */
public class IntegralInfo {
	private String id;// 方案ID
	private String time;// 时间
	private String onece;// 积分额
	private String way;// 赠送方式
	private String total;// 总积分

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOnece() {
		return onece;
	}

	public void setOnece(String onece) {
		this.onece = onece;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
