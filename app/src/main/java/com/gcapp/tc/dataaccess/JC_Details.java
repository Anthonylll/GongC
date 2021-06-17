package com.gcapp.tc.dataaccess;

import java.io.Serializable;

/**
 * 奖金优化实体类
 * 
 * @author lenovo
 * 
 */
public class JC_Details implements Serializable {
	private String MatchNumber;// 周一001
	private String matchID;// 对阵id
	private String mainTeam;// 主队
	private String guestTeam;// 客队
	private String playtype_info;// 玩法id
	private Double peilv;// 一种结果的赔率
	private String showresult;
	private String result; // 1 2 3 , 500 501

	public String getMatchNumber() {
		return MatchNumber;
	}

	public void setMatchNumber(String matchNumber) {
		MatchNumber = matchNumber;
	}

	public String getMatchID() {
		return matchID;
	}

	public void setMatchID(String matchID) {
		this.matchID = matchID;
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

	public String getPlaytype_info() {
		return playtype_info;
	}

	public void setPlaytype_info(String playtype_info) {
		this.playtype_info = playtype_info;
	}

	public String getPeilv() {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		return df.format(peilv);
	}

	public void setPeilv(Double peilv) {
		this.peilv = peilv;
	}

	public String getShowresult() {
		return showresult;
	}

	public void setShowresult(String showresult) {
		this.showresult = showresult;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
