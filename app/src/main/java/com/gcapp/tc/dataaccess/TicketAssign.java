package com.gcapp.tc.dataaccess;

/**
 * 奖金优化，票数分布
 * 
 * @author H
 */
public class TicketAssign implements Comparable<TicketAssign> {
	public int size;
	public float weight;
	public float value;
	private int id;

	@Override
	public int compareTo(TicketAssign o) {
		return (int) (value - o.value);
	}

	public void setValue() {
		value = weight * size;
	}

	public void clear() {
		value = 0;
		size = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
}
