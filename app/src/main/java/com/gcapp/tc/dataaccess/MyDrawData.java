package com.gcapp.tc.dataaccess;

import java.text.DecimalFormat;

/**
 * 提款实体类
 * 
 * @author zht
 * 
 */
public class MyDrawData {
	private int handselDrawing;

	private double userAllowHandsel; // 用户可提彩金

	private int error;

	private String cityName;// 开户城市

	private String bankTypeName;// 银行卡名称

	private String provinceName;// 开户省份

	private String securityquestion;// 安全问题

	private String idCardnumber;// 身份证号码

	private String msg;

	private double balance;// 可用金额

	private String name;// 用户名

	private String alipayAccount;// 支付宝

	private String serverTime;// 服务器时间

	private String isBinded;// 是否绑定

	private String realityName;// 真实姓名

	private String qqnumber;// QQ

	private String bankUserName;

	private String bankCardNumber;// 银行卡号码

	private String branchBankName;// 银行卡名称

	private String mobile;// 手机号码

	public int getHandselDrawing() {
		return handselDrawing;
	}

	public void setHandselDrawing(int handselDrawing) {
		this.handselDrawing = handselDrawing;
	}

	public String getUserAllowHandsel() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(userAllowHandsel);
	}

	public void setUserAllowHandsel(double userAllowHandsel) {
		this.userAllowHandsel = userAllowHandsel;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getBankTypeName() {
		return bankTypeName;
	}

	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getSecurityquestion() {
		return securityquestion;
	}

	public void setSecurityquestion(String securityquestion) {
		this.securityquestion = securityquestion;
	}

	public String getIdCardnumber() {
		return idCardnumber;
	}

	public void setIdCardnumber(String idCardnumber) {
		this.idCardnumber = idCardnumber;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBalance() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(balance);
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getIsBinded() {
		return isBinded;
	}

	public void setIsBinded(String isBinded) {
		this.isBinded = isBinded;
	}

	public String getRealityName() {
		return realityName;
	}

	public void setRealityName(String realityName) {
		this.realityName = realityName;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getQqnumber() {
		return qqnumber;
	}

	public void setQqnumber(String qqnumber) {
		this.qqnumber = qqnumber;
	}

	public String getBankUserName() {
		return bankUserName;
	}

	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}

	public String getBankCardNumber() {
		return bankCardNumber;
	}

	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
