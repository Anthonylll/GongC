package com.gcapp.tc.dataaccess;

/**
 * 显示投注详情的竞彩奖金优化实体类
 * 
 * @author yxl
 * 
 */
public class ShowDtMatchOptimize {

	private String preBetType;// 优化方案
	private String ggWay;// 玩法
	private String investNum;// 注数
	private String buyContent;// 购买赛事详情
	private String result;// 赛果
	private String winMoney;// 奖金
	private String markRed;// 是否猜中 "0,0" 0表示未猜中，1表示猜中

	public String getPreBetType() {
		return preBetType;
	}

	public void setPreBetType(String preBetType) {
		this.preBetType = preBetType;
	}

	public String getGgWay() {
		return ggWay;
	}

	public void setGgWay(String ggWay) {
		this.ggWay = ggWay;
	}

	public String getInvestNum() {
		return investNum;
	}

	public void setInvestNum(String investNum) {
		this.investNum = investNum;
	}

	public String getBuyContent() {
		return buyContent;
	}

	public void setBuyContent(String buyContent) {
		this.buyContent = buyContent;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(String winMoney) {
		this.winMoney = winMoney;
	}

	public String getMarkRed() {
		return markRed;
	}

	public void setMarkRed(String markRed) {
		this.markRed = markRed;
	}

	@Override
	public String toString() {
		return "ShowDtMatchOptimize [preBetType=" + preBetType + ", ggWay="
				+ ggWay + ", investNum=" + investNum + ", buyContent="
				+ buyContent + ", result=" + result + ", winMoney=" + winMoney
				+ ", markRed=" + markRed + "]";
	}

}
