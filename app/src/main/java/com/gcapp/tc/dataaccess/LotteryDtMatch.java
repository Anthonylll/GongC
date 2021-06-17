package com.gcapp.tc.dataaccess;

/**
 * 竞彩的投注信息实体类
 * 
 * @author lenovo
 * 
 */
public class LotteryDtMatch {
	private String id;// 方案ID
	private String matchNumber;// 对阵编号
	private String weekDay;// 星期
	private String game;
	private String mainTeam;// 主队
	private String guestTeam;// 客队
	private String stopSellTime;// 停售时间
	private String matchDate;// 对阵日期
	private int loseBall;// 让球数
	private String allResult;// 全场比赛赛果
	private String halfResult;// 半场比赛赛果

	public String getAllResult() {
		return allResult;
	}

	public void setAllResult(String allResult) {
		this.allResult = allResult;
	}

	public String getHalfResult() {
		return halfResult;
	}

	public void setHalfResult(String halfResult) {
		this.halfResult = halfResult;
	}

	private String spfResult;
	private String rqspfResult;
	private String zjqResult;
	private String cbfResult;
	private String bfResult;
	private String sfcResult;
	private String sfResult;
	private String rfsfResult;
	private String dxResult;
	private String result;
	private String loseScore;
	private String bqcResult;
	private String sxdsResult;

	// 赔率字段
	private String bqcSp;
	private String zjqSp;
	private String sxdsSp;
	private String rqspfSp;
	private String bfSp;
	private String spfSp;
	private String rfsfSp;
	private String sfcSp;
	private String dxfSp;
	private String sfSp;

	public String getSfcResult() {
		return sfcResult;
	}

	public String getRqspfResult() {
		return rqspfResult;
	}

	public void setRqspfResult(String rqspfResult) {
		this.rqspfResult = rqspfResult;
	}

	public void setSfcResult(String sfcResult) {
		this.sfcResult = sfcResult;
	}

	public String getSfcSp() {
		return sfcSp;
	}

	public void setSfcSp(String sfcSp) {
		this.sfcSp = sfcSp;
	}

	public String getBfResult() {
		return bfResult;
	}

	public void setBfResult(String bfResult) {
		this.bfResult = bfResult;
	}

	public String getRfsfResult() {
		return rfsfResult;
	}

	public void setRfsfResult(String rfsfResult) {
		this.rfsfResult = rfsfResult;
	}

	public String getRfsfSp() {
		return rfsfSp;
	}

	public void setRfsfSp(String rfsfSp) {
		this.rfsfSp = rfsfSp;
	}

	public String getSxdsResult() {
		return sxdsResult;
	}

	public void setSxdsResult(String sxdsResult) {
		this.sxdsResult = sxdsResult;
	}

	public String getSfSp() {
		return sfSp;
	}

	public void setSfSp(String sfSp) {
		this.sfSp = sfSp;
	}

	public String getDxfSp() {
		return dxfSp;
	}

	public void setDxfSp(String dxfSp) {
		this.dxfSp = dxfSp;
	}

	public String getSpfSp() {
		return spfSp;
	}

	public void setSpfSp(String spfSp) {
		this.spfSp = spfSp;
	}

	public String getBqcSp() {
		return bqcSp;
	}

	public void setBqcSp(String bqcSp) {
		this.bqcSp = bqcSp;
	}

	public String getZjqSp() {
		return zjqSp;
	}

	public void setZjqSp(String zjqSp) {
		this.zjqSp = zjqSp;
	}

	public String getSxdsSp() {
		return sxdsSp;
	}

	public void setSxdsSp(String sxdsSp) {
		this.sxdsSp = sxdsSp;
	}

	public String getRqspfSp() {
		return rqspfSp;
	}

	public void setRqspfSp(String rqspfSp) {
		this.rqspfSp = rqspfSp;
	}

	public String getBfSp() {
		return bfSp;
	}

	public void setBfSp(String bfSp) {
		this.bfSp = bfSp;
	}

	public String getBqcResult() {
		return bqcResult;
	}

	public void setBqcResult(String bqcResult) {
		this.bqcResult = bqcResult;
	}

	public String getLoseScore() {
		return loseScore;
	}

	public void setLoseScore(String loseScore) {
		this.loseScore = loseScore;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getMainTeam() {
		return mainTeam;
	}

	public void setMainTeam(String mainTeam) {
		this.mainTeam = mainTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public String getStopSellTime() {
		return stopSellTime;
	}

	public void setStopSellTime(String stopSellTime) {
		this.stopSellTime = stopSellTime;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String marchDate) {
		this.matchDate = marchDate;
	}

	public String getSpfResult() {
		return spfResult;
	}

	public void setSpfResult(String spfResult) {
		this.spfResult = spfResult;
	}

	public String getZjqResult() {
		return zjqResult;
	}

	public void setZjqResult(String zjqResult) {
		this.zjqResult = zjqResult;
	}

	public String getCbfResult() {
		return cbfResult;
	}

	public void setCbfResult(String cbfResult) {
		this.cbfResult = cbfResult;
	}

	public String getSfResult() {
		return sfResult;
	}

	public void setSfResult(String sfResult) {
		this.sfResult = sfResult;
	}

	public String getDxResult() {
		return dxResult;
	}

	public void setDxResult(String dxResult) {
		this.dxResult = dxResult;
	}

	public int getLoseBall() {
		return loseBall;
	}

	public void setLoseBall(int loseBall) {
		this.loseBall = loseBall;
	}

}
