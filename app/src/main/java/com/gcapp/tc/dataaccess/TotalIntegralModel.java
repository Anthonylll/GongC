package com.gcapp.tc.dataaccess;

import java.util.List;

/**
 * @author dm
 * @date 2019-7-5 下午3:22:48
 * @version 5.5.0 
 * @Description 
 */
public class TotalIntegralModel {

	/** 联赛名称*/
	private String leagueName;
	/** 队伍地区*/
	private String teamArea;
	/** 比赛信息*/
	private List<IntegralModel> integralList;
	
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public String getTeamArea() {
		return teamArea;
	}
	public void setteamArea(String teamArea) {
		this.teamArea = teamArea;
	}
	public List<IntegralModel> getIntegralList() {
		return integralList;
	}
	public void setIntegralList(List<IntegralModel> integralList) {
		this.integralList = integralList;
	}
	
}
