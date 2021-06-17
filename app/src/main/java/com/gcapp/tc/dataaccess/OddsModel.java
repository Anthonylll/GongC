package com.gcapp.tc.dataaccess;
/**
 * @author anthony
 * @date 2018-5-23 上午10:41:47
 * @version 5.5.0 
 * @Description 
 */
public class OddsModel {
	
	private String oddsCompany;
	private String oddsInitialWin;
	private String oddsInitialFlat;
	private String oddsInitialLose;
	private String oddsInstantWin;
	private String oddsInstantFlat;
	private String oddsInstantLose;
	/** 主赔变化*/
	private String hChange;
	/** 客赔变化*/
	private String gChange;
	/** 平变化*/
	private String fChange;
	
	public String getfChange() {
		return fChange;
	}
	public void setfChange(String fChange) {
		this.fChange = fChange;
	}
	public String gethChange() {
		return hChange;
	}
	public void sethChange(String hChange) {
		this.hChange = hChange;
	}
	public String getgChange() {
		return gChange;
	}
	public void setgChange(String gChange) {
		this.gChange = gChange;
	}
	public String getOddsCompany() {
		return oddsCompany;
	}
	public void setOddsCompany(String oddsCompany) {
		this.oddsCompany = oddsCompany;
	}
	public String getOddsInitialWin() {
		return oddsInitialWin;
	}
	public void setOddsInitialWin(String oddsInitialWin) {
		this.oddsInitialWin = oddsInitialWin;
	}
	public String getOddsInitialFlat() {
		return oddsInitialFlat;
	}
	public void setOddsInitialFlat(String oddsInitialFlat) {
		this.oddsInitialFlat = oddsInitialFlat;
	}
	public String getOddsInitialLose() {
		return oddsInitialLose;
	}
	public void setOddsInitialLose(String oddsInitialLose) {
		this.oddsInitialLose = oddsInitialLose;
	}
	public String getOddsInstantWin() {
		return oddsInstantWin;
	}
	public void setOddsInstantWin(String oddsInstantWin) {
		this.oddsInstantWin = oddsInstantWin;
	}
	public String getOddsInstantFlat() {
		return oddsInstantFlat;
	}
	public void setOddsInstantFlat(String oddsInstantFlat) {
		this.oddsInstantFlat = oddsInstantFlat;
	}
	public String getOddsInstantLose() {
		return oddsInstantLose;
	}
	public void setOddsInstantLose(String oddsInstantLose) {
		this.oddsInstantLose = oddsInstantLose;
	}
}
