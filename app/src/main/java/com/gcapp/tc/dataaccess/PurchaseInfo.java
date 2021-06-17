package com.gcapp.tc.dataaccess;

/**
 * 认购列表数据
 * 
 * @author lenovo
 */
public class PurchaseInfo {
	private String pur_username;// 认购人
	private int fensum;// 份数
	private double money;// 金额
	private String time;// 时间
	private String detailTime;// 具体时间

	public String getDetailTime() {
		return detailTime;
	}

	public void setDetailTime(String detailTime) {
		this.detailTime = detailTime;
	}

	public String getPur_username() {
		return pur_username;
	}

	public void setPur_username(String pur_username) {
		this.pur_username = pur_username;
	}

	public int getFensum() {
		return fensum;
	}

	public void setFensum(int fensum) {
		this.fensum = fensum;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
