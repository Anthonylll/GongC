package com.gcapp.tc.dataaccess;

import java.io.Serializable;
import java.util.List;

/**
 * 竞彩奖金优化实体类
 * 
 * @author lenovo
 * 
 */
public class Show_JC_Details implements Comparable, Serializable {
	private String pass;// 过关方式
	private List<String> list_result;// 选号集合
	private int bei;// 投注倍数
	private String Winmony;
	private List<JC_Details> list_JC_Details;// 对阵集合
	private double sum_peilv;// 赔率

	public double getSum_peilv() {
		return sum_peilv;
	}

	public void setSum_peilv(double sum_peilv) {
		this.sum_peilv = sum_peilv;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public List<String> getList_result() {
		return list_result;
	}

	public void setList_result(List<String> list_result) {
		this.list_result = list_result;
	}

	public int getBei() {
		return bei;
	}

	public void setBei(int bei) {
		this.bei = bei;
	}

	public String getWinmony() {
		return Winmony;
	}

	public void setWinmony(String winmony) {
		Winmony = winmony;
	}

	public List<JC_Details> getList_JC_Details() {
		return list_JC_Details;
	}

	public void setList_JC_Details(List<JC_Details> list_JC_Details) {
		this.list_JC_Details = list_JC_Details;
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

}
