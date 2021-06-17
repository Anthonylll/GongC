package com.gcapp.tc.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 彩种的实体类
 * 
 * @author lenovo
 * 
 */
public class Lottery implements Serializable {
	private String lotteryID; // 彩种id
	private String lotteryName; // 彩种名称
	private String lotteryDescription; // 描述
	private String lotteryAgainst; // 竞彩最新对阵
	private String addaward;
	private String addawardsingle;
	private String isuseId; // 该彩种当前期号id
	private String isuseName; // 奖期名称
	// 新增字段
	private String isSale; // 是否开售
	private String starttime; // 奖期开始时间
	private String endtime; // 奖期结束时间
	private String lastIsuseName; // 最后已开奖的期名
	private String lastWinNumber; // 最后已开奖的号码
	private String originalTime; //
	private long distanceTime2;
	private String explanation; // 该彩奖池
	private List<String> dtCanChaseIsuses; // 该彩种可追奖期数
	private long distanceTime; // 倒计时的时间
	private List<String> preSaleInfo; // 预售期期号信息
	private List<List<DtMatchBJDC>> list_Matchs_bjdc; // 比赛对阵信息
	private List<List<DtMatchBJDC>> list_singlepass_Matchs_bjdc; // 单关比赛对阵信息
	private int type;

	// 篮球单关比赛对阵信息
	public void setList_Matchs_bjdc(List<List<DtMatchBJDC>> list_Matchs_bjdc) {
		this.list_Matchs_bjdc = list_Matchs_bjdc;
	}

	private String dtmatch; // 第一场比赛

	public String getIsSale() {
		return isSale;
	}

	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}

	private int isChase;// 是否是追号 0.普通 1.追号
	private int chaseTaskID;// 当前投注追号任务号

	public int getIsChase() {
		return isChase;
	}

	public List<List<DtMatchBJDC>> getList_Matchs_bjdc() {
		return list_Matchs_bjdc;
	}

	public List<List<DtMatchBJDC>> getList_singlepass_Matchs_bjdc() {
		return list_singlepass_Matchs_bjdc;
	}

	public void setIsChase(int isChase) {
		this.isChase = isChase;
	}

	public int getChaseTaskID() {
		return chaseTaskID;
	}

	public void setChaseTaskID(int chaseTaskID) {
		this.chaseTaskID = chaseTaskID;
	}

	public String getLotteryDescription() {
		return lotteryDescription;
	}

	public void setLotteryDescription(String lotteryDescription) {
		this.lotteryDescription = lotteryDescription;
	}

	public String getLotteryAgainst() {
		return lotteryAgainst;
	}

	public void setLotteryAgainst(String lotteryAgainst) {
		this.lotteryAgainst = lotteryAgainst;
	}

	public String getDtmatch() {
		return dtmatch;
	}

	public void setDtmatch(String dtmatch) {
		this.dtmatch = dtmatch;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public long getDistanceTime() {
		return distanceTime;
	}

	public void setDistanceTime(long distanceTime) {
		this.distanceTime = distanceTime;
	}

	public String getLotteryID() {
		return lotteryID;
	}

	public void setLotteryID(String lotteryID) {
		this.lotteryID = lotteryID;
	}

	public String getIsuseId() {
		return isuseId;
	}

	public void setIsuseId(String isuseId) {
		this.isuseId = isuseId;
	}

	public String getIsuseName() {
		return isuseName;
	}

	public void setIsuseName(String isuseName) {
		this.isuseName = isuseName;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getLastIsuseName() {
		return lastIsuseName;
	}

	public void setLastIsuseName(String lastIsuseName) {
		this.lastIsuseName = lastIsuseName;
	}

	public String getLastWinNumber() {
		return lastWinNumber;
	}

	public void setLastWinNumber(String lastWinNumber) {
		this.lastWinNumber = lastWinNumber;
	}

	public List<String> getDtCanChaseIsuses() {
		return dtCanChaseIsuses;
	}

	public void setDtCanChaseIsuses(List<String> dtCanChaseIsuses) {
		this.dtCanChaseIsuses = new ArrayList<String>();
		for (String str : dtCanChaseIsuses) {
			this.dtCanChaseIsuses.add(str);
		}
	}

	public List<String> getPreSaleInfo() {
		return preSaleInfo;
	}

	public void setPreSaleInfo(List<String> preSaleInfo) {
		this.preSaleInfo = new ArrayList<String>();
		for (String str : preSaleInfo) {
			this.preSaleInfo.add(str);
		}
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOriginalTime() {
		return originalTime;
	}

	public void setOriginalTime(String originalTime) {
		this.originalTime = originalTime;
	}

	public long getDistanceTime2() {
		return distanceTime2;
	}

	public void setDistanceTime2(long distanceTime2) {
		this.distanceTime2 = distanceTime2;
	}

	public String getAddaward() {
		return addaward;
	}

	public void setAddaward(String addaward) {
		this.addaward = addaward;
	}

	public String getAddawardsingle() {
		return addawardsingle;
	}

	public void setAddawardsingle(String addawardsingle) {
		this.addawardsingle = addawardsingle;
	}
}
