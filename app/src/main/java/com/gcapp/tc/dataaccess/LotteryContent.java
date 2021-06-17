package com.gcapp.tc.dataaccess;

import java.io.Serializable;

/**
 * 投注内容实体类
 * 
 * @author lenovo
 * 
 */
public class LotteryContent implements Serializable {
	private String lotteryNumber;// 投注号码
	private String playType;// 玩法ID
	private String sumMoney;// 投注金额
	private String sumNum;// 投注注数

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	public String getSumNum() {
		return sumNum;
	}

	public void setSumNum(String sumNum) {
		this.sumNum = sumNum;
	}

}
