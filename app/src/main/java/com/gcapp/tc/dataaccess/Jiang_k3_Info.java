package com.gcapp.tc.dataaccess;

/**
 * 快三的近10期开奖信息实体类
 * 
 * @author lenovo
 * 
 */
public class Jiang_k3_Info {
	private String name;// 期次
	private String winNumber;// 开奖号码
	private String NumberType;// 形态

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getNumberType() {
		return NumberType;
	}

	public void setNumberType(String numberType) {
		NumberType = numberType;
	}

}
