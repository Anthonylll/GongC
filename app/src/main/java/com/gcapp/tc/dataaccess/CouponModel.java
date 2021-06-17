package com.gcapp.tc.dataaccess;
/**
 * @author anthony
 * @date 2018-7-20 下午1:56:39
 * @version 1.1.0 
 * @Description 优惠券实体类
 */
public class CouponModel {

	private String limitMoney;
	private String fullMoney;
	private String couponTime;
	private String isExpired;
	private String isUsed;
	/** 优惠券Id*/
	private String couponId;
	
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getIsExpired() {
		return isExpired;
	}
	public void setIsExpired(String isExpired) {
		this.isExpired = isExpired;
	}
	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	public String getLimitMoney() {
		return limitMoney;
	}
	public void setLimitMoney(String limitMoney) {
		this.limitMoney = limitMoney;
	}
	public String getFullMoney() {
		return fullMoney;
	}
	public void setFullMoney(String fullMoney) {
		this.fullMoney = fullMoney;
	}
	public String getCouponTime() {
		return couponTime;
	}
	public void setCouponTime(String couponTime) {
		this.couponTime = couponTime;
	}
	
	
}
