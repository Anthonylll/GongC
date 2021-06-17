package com.gcapp.tc.dataaccess;

/**
 * @author anthony
 * @date 2018-4-2 上午11:16:54
 * @version 5.5.4 
 * @Description 比分直播的实体类
 */
public class LiveMatch {

	private int matchId;
	/**开赛时间*/
	private String matchTime;
	/**比赛已进行的时间*/
	private String matchDate;
	private String matchNumber;
	/** 联赛名*/
	private String matchOrganization;
	private String hostGrade;
	private String guestGrade;
	/** 上半场主队得分*/
	private String hostFirstGrade;
	/** 上半场客队得分*/
	private String guestFirstGrade;
	private String hostTeam;
	private String guestTeam;
	private String matchState;
	private String hostTeamImg;
	private String guestTeamImg;
	private String hostRanking;
	private String guestRanking;
	/** 篮球球队地区*/
	private String hostArea;
	private String guestArea;
	
	public String getHostArea() {
		return hostArea;
	}
	public void setHostArea(String hostArea) {
		this.hostArea = hostArea;
	}
	public String getGuestArea() {
		return guestArea;
	}
	public void setGuestArea(String guestArea) {
		this.guestArea = guestArea;
	}
	public String getHostRanking() {
		return hostRanking;
	}
	public void setHostRanking(String hostRanking) {
		this.hostRanking = hostRanking;
	}
	public String getGuestRanking() {
		return guestRanking;
	}
	public void setGuestRanking(String guestRanking) {
		this.guestRanking = guestRanking;
	}
	public String getHostTeamImg() {
		return hostTeamImg;
	}
	public void setHostTeamImg(String hostTeamImg) {
		this.hostTeamImg = hostTeamImg;
	}
	public String getGuestTeamImg() {
		return guestTeamImg;
	}
	public void setGuestTeamImg(String guestTeamImg) {
		this.guestTeamImg = guestTeamImg;
	}
	public String getHostFirstGrade() {
		return hostFirstGrade;
	}
	public void setHostFirstGrade(String hostFirstGrade) {
		this.hostFirstGrade = hostFirstGrade;
	}
	public String getGuestFirstGrade() {
		return guestFirstGrade;
	}
	public void setGuestFirstGrade(String guestFirstGrade) {
		this.guestFirstGrade = guestFirstGrade;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}
	public String getMatchNumber() {
		return matchNumber;
	}
	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}
	public String getMatchOrganization() {
		return matchOrganization;
	}
	public void setMatchOrganization(String matchOrganization) {
		this.matchOrganization = matchOrganization;
	}
	public String getHostGrade() {
		return hostGrade;
	}
	public void setHostGrade(String hostGrade) {
		this.hostGrade = hostGrade;
	}
	public String getGuestGrade() {
		return guestGrade;
	}
	public void setGuestGrade(String guestGrade) {
		this.guestGrade = guestGrade;
	}
	public String getHostTeam() {
		return hostTeam;
	}
	public void setHostTeam(String hostTeam) {
		this.hostTeam = hostTeam;
	}
	public String getGuestTeam() {
		return guestTeam;
	}
	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}
	public String getMatchState() {
		return matchState;
	}
	public void setMatchState(String matchState) {
		this.matchState = matchState;
	}
	
	
	
}
