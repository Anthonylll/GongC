package com.gcapp.tc.dataaccess;

import java.text.DecimalFormat;

/**
 * 功能：用户实体类 版本
 */
public class Users {

	private String uid; // 用户ID

	private String name; // 用户名

	private String qqNumber;
	private String image_url;// 上传头像的图片路径

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	private String userPass; // 用户密码

	private String realityName; // 真是姓名

	private String idcardnumber; // 身份证号码

	private String email; // 用户邮箱

	private String mobile; // 手机号码

	private double balance; // 可用余额
	/** 用户最低提现金额*/
	private double minWithdraw;

	private double freeze; // 冻结金额

	private int scoring; // 可用积分

	private int msgCountAll; // 消息总条数

	private int msgCount; // 未读消息条数

	private String bankName; // 银行信息

	private String fullName; // 支行

	private String location; // 开户地点

	private String provinceName; // 省

	private String cityName;// 城市

	private String bangNum; // 银行卡号

	private String zfbNum; // 支付宝
	
	private String bankCard;//充值银行卡号

	private String bankUserName; // 持卡人

	private String securityQuestionId;// 问题

	private String securityQuestionAnswer;// 答案
	/**累积中奖*/
	private double totalWinMoney;
	/**是否是大神*/
	private String isgreatMan;
	/**可提现金额*/
	private double extractMoney;

	public double getMinWithdraw() {
		return minWithdraw;
	}

	public void setMinWithdraw(double minWithdraw) {
		this.minWithdraw = minWithdraw;
	}

	public String getSecurityQuestionAnswer() {
		return securityQuestionAnswer;
	}

	public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
		this.securityQuestionAnswer = securityQuestionAnswer;
	}

	private double handselAmount; // 彩金金额

	public String getHandselAmount() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(handselAmount);
	}

	public void setHandselAmount(double handselAmount) {
		this.handselAmount = handselAmount;
	}

	public String getSecurityQuestionId() {
		return securityQuestionId;
	}

	public void setSecurityQuestionId(String securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealityName() {
		return realityName;
	}

	public void setRealityName(String realityName) {
		this.realityName = realityName;
	}

	public String getIdcardnumber() {
		return idcardnumber;
	}

	public void setIdcardnumber(String idcardnumber) {
		this.idcardnumber = idcardnumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBalance() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(balance);
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getFreeze() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(freeze);
	}

	public void setFreeze(double freeze) {
		this.freeze = freeze;
	}

	public int getScoring() {
		return scoring;
	}

	public void setScoring(int scoring) {
		this.scoring = scoring;
	}

	public int getMsgCountAll() {
		return msgCountAll;
	}

	public void setMsgCountAll(int msgCountAll) {
		this.msgCountAll = msgCountAll;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBangNum() {
		return bangNum;
	}

	public void setBangNum(String bangNum) {
		this.bangNum = bangNum;
	}

	public String getZfbNum() {
		return zfbNum;
	}

	public void setZfbNum(String zfbNum) {
		this.zfbNum = zfbNum;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBankUserName() {
		return bankUserName;
	}

	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTotalWinMoney() {
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(totalWinMoney);
	}

	public void setTotalWinMoney(double totalWinMoney) {
		this.totalWinMoney = totalWinMoney;
	}
	
	public String getIsgreatMan() {
		return isgreatMan;
	}

	public void setIsgreatMan(String isgreatMan) {
		this.isgreatMan = isgreatMan;
	}

	public double getExtractMoney() {
		return extractMoney;
	}

	public void setExtractMoney(double extractMoney) {
		this.extractMoney = extractMoney;
	}
	
}
