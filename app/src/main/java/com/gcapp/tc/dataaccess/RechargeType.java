package com.gcapp.tc.dataaccess;

import java.util.List;

/**
 * 
 * 充值赠送彩金的状态类型类型
 * 
 * @author zht
 * 
 */

public class RechargeType {

	private String isSuccess; // 返回状态

	private int giveType; // 赠送方式： 0定额，1定比

	private String msg;

	private List<ChargeActivity> activeList; // 活动列表

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getGiveType() {
		return giveType;
	}

	public void setGiveType(int giveType) {
		this.giveType = giveType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<ChargeActivity> getActiveList() {
		return activeList;
	}

	public void setActiveList(List<ChargeActivity> activeList) {
		this.activeList = activeList;
	}

}
