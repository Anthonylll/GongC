package com.gcapp.tc.dataaccess;

import java.io.Serializable;

/** 开奖号码实体类 */
public class WinDetail implements Serializable {

	private String bonusName;
	private String bonusValue;
	private int winningCount;

	public String getBonusName() {
		return bonusName;
	}

	public void setBonusName(String bonusName) {
		this.bonusName = bonusName;
	}

	public String getBonusValue() {
		return bonusValue;
	}

	public void setBonusValue(String bonusValue) {
		this.bonusValue = bonusValue;
	}

	public int getWinningCount() {
		return winningCount;
	}

	public void setWinningCount(int winningCount) {
		this.winningCount = winningCount;
	}

}
