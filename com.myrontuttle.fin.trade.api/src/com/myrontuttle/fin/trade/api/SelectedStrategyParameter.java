package com.myrontuttle.fin.trade.api;

import java.io.Serializable;

/**
 * The specific parameter that was selected for a strategy * 
 * @author Myron Tuttle
 */
public class SelectedStrategyParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tradeId;
	private String name;
	private int value;
	
	public SelectedStrategyParameter(String tradeId, String name, int value) {
		super();
		this.tradeId = tradeId;
		this.name = name;
		this.value = value;
	}
	
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
