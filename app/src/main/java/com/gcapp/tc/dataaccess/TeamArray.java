package com.gcapp.tc.dataaccess;

/**
 * 胜负彩和任选9的实体类
 * 
 * @author lenovo
 * 
 */
public class TeamArray {
	private String isuseName;//期号
	private String endtime;//截止时间
	private String id;// 对阵ID
	private String game;// 对阵名称
	private String matchNumber;// 对阵号码
	private String matchDate;// 日期
	private String mainTeam;// 主队
	private String guestTeam;// 客队
	private String date;// 日期
	private String time;// 时间
	private String winSp;// 胜
	private String floatSp;// 平
	private String loseSp;// 负

	public String getIsuseName() {
		return isuseName;
	}

	public void setIsuseName(String isuseName) {
		this.isuseName = isuseName;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getWinSp() {
		return winSp;
	}

	public void setWinSp(String winSp) {
		this.winSp = winSp;
	}

	public String getFloatSp() {
		return floatSp;
	}

	public void setFloatSp(String floatSp) {
		this.floatSp = floatSp;
	}

	public String getLoseSp() {
		return loseSp;
	}

	public void setLoseSp(String loseSp) {
		this.loseSp = loseSp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getMatchNumber() {
		return matchNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
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
}
