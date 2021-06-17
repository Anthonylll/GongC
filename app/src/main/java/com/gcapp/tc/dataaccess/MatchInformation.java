package com.gcapp.tc.dataaccess;
/**
 * @author anthony
 * @date 2018-5-8 下午4:41:16
 * @version 5.5.0 
 * @Description 战绩：比赛实体类
 */
public class MatchInformation {

	/** 主队vs客队*/
	private String matchScore;
	/** 主队vs客队*/
	private String hostTeam;
	/** 主队vs客队*/
	private String guestTeam;
	/** 比赛时间*/
	private String matchTime;
	/** 联赛名称*/
	private String matchOrganization;
	/** 赛果*/
	private String matchResult;
	/** 未来赛事中距离天数*/
	private String needDay;
	
	public String getNeedDay() {
		return needDay;
	}
	public void setNeedDay(String needDay) {
		this.needDay = needDay;
	}
	public String getMatchScore() {
		return matchScore;
	}
	public void setMatchScore(String matchScore) {
		this.matchScore = matchScore;
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
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getMatchOrganization() {
		return matchOrganization;
	}
	public void setMatchOrganization(String matchOrganization) {
		this.matchOrganization = matchOrganization;
	}
	public String getMatchResult() {
		return matchResult;
	}
	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}
	
}
