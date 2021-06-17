package com.gcapp.tc.dataaccess;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * AccountDetails：账户明细实体类
 * 
 * @author lenovo
 * 
 */
public class AccountDetails implements Serializable {

	public String date;// 日期
	private String time;// 时间
	private double money;// 金额
	private String status;// 方案状态
	private String betType;// 投注方式
	private int img_type;// 图片标识

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMoney() {
		DecimalFormat format = new DecimalFormat("#####0.00");
		return format.format(money);
	}

	public void setMoney(double detailMoney) {
		this.money = detailMoney;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public int getImg_type() {
		return img_type;
	}

	public void setImg_type(int img_type) {
		this.img_type = img_type;
	}

}
