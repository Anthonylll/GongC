package com.gcapp.tc.dataaccess;

/**
 * 高频彩的开奖信息实体类
 * 
 * @author lenovo
 * 
 */
public class WinInfo {
	private String winNumber;// 开奖号码
	private String winQihao;// 期次
	private String state;// 状态

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getWinQihao() {
		return winQihao;
	}

	public void setWinQihao(String winQihao) {
		this.winQihao = winQihao;
	}

}
