package com.gcapp.tc.dataaccess;

/**
 * 充值活动明细
 * 
 * @author zht
 */

public class ChargeActivity {

	private int id; // id标号

	private double conditionLowest; // 最低额度

	private double conditionHighest; // 最高额度

	private double numerical; // 比例或定额

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getConditionLowest() {
		return conditionLowest;
	}

	public void setConditionLowest(double conditionLowest) {
		this.conditionLowest = conditionLowest;
	}

	public double getConditionHighest() {
		return conditionHighest;
	}

	public void setConditionHighest(double conditionHighest) {
		this.conditionHighest = conditionHighest;
	}

	public double getNumerical() {
		return numerical;
	}

	public void setNumerical(double numerical) {
		this.numerical = numerical;
	}

}
