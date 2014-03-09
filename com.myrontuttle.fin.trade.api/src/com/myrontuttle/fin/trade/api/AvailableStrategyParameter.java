package com.myrontuttle.fin.trade.api;

public class AvailableStrategyParameter {

	private String name;
	private int lower;
	private int upper;
	
	public AvailableStrategyParameter(String name, int lower, int upper) {
		this.name = name;
		this.lower = lower;
		this.upper = upper;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUpper() {
		return upper;
	}
	public void setUpper(int upper) {
		this.upper = upper;
	}
	public int getLower() {
		return lower;
	}
	public void setLower(int lower) {
		this.lower = lower;
	}
}
