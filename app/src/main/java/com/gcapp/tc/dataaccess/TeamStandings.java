package com.gcapp.tc.dataaccess;

import java.util.List;

/**
 * @author anthony
 * @date 2018-5-8 下午4:15:14
 * @version 5.6.20 
 * @Description 
 */
public class TeamStandings {
	
	/** 队伍名称*/
	private String teamName;
	/** 比赛场数*/
	private String matchCount;
	/** 胜场数*/
	private String winCount;
	/** 平场数*/
	private String bisectionCount;
	/** 负场数*/
	private String loseCount;
	/** 比赛信息*/
	private List<MatchInformation> matchList;
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(String matchCount) {
		this.matchCount = matchCount;
	}
	public String getWinCount() {
		return winCount;
	}
	public void setWinCount(String winCount) {
		this.winCount = winCount;
	}
	public String getBisectionCount() {
		return bisectionCount;
	}
	public void setBisectionCount(String bisectionCount) {
		this.bisectionCount = bisectionCount;
	}
	public String getLoseCount() {
		return loseCount;
	}
	public void setLoseCount(String loseCount) {
		this.loseCount = loseCount;
	}
	public List<MatchInformation> getMatchList() {
		return matchList;
	}
	public void setMatchList(List<MatchInformation> matchList) {
		this.matchList = matchList;
	}
	
}
