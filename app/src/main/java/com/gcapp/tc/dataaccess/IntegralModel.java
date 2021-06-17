package com.gcapp.tc.dataaccess;
/**
 * @author anthony
 * @date 2018-8-8 下午3:31:42
 * @version 5.5.0 
 * @Description 积分实体类
 */
public class IntegralModel {

	private String leagueName;
	private String leagueRanking;
	private String teamName;
	private String matchCount;
	private String winCount;
	private String flatCount;
	private String defeatCount;
	private String enterCount;
	private String loseCount;
	private String entirelyCount;
	private String totalIntegral;
	/** 是否当前队伍*/
	private String teamType;
	/** 胜率*/
	private String winProbability;
	/** 连胜or连败*/
	private String winState;
	/** 几连胜*/
	private String winContent;
	
	public String getTeamType() {
		return teamType;
	}
	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public String getLeagueRanking() {
		return leagueRanking;
	}
	public void setLeagueRanking(String leagueRanking) {
		this.leagueRanking = leagueRanking;
	}
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
	public String getFlatCount() {
		return flatCount;
	}
	public void setFlatCount(String flatCount) {
		this.flatCount = flatCount;
	}
	public String getDefeatCount() {
		return defeatCount;
	}
	public void setDefeatCount(String defeatCount) {
		this.defeatCount = defeatCount;
	}
	public String getEnterCount() {
		return enterCount;
	}
	public void setEnterCount(String enterCount) {
		this.enterCount = enterCount;
	}
	public String getLoseCount() {
		return loseCount;
	}
	public void setLoseCount(String loseCount) {
		this.loseCount = loseCount;
	}
	public String getEntirelyCount() {
		return entirelyCount;
	}
	public void setEntirelyCount(String entirelyCount) {
		this.entirelyCount = entirelyCount;
	}
	public String getTotalIntegral() {
		return totalIntegral;
	}
	public void setTotalIntegral(String totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
	public String getWinProbability() {
		return winProbability;
	}
	public void setWinProbability(String winProbability) {
		this.winProbability = winProbability;
	}
	public String getWinState() {
		return winState;
	}
	public void setWinState(String winState) {
		this.winState = winState;
	}
	public String getWinContent() {
		return winContent;
	}
	public void setWinContent(String winContent) {
		this.winContent = winContent;
	}
	
}
