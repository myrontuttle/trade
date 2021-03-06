package com.myrontuttle.fin.trade.api;

/**
 * The specific parameter that was selected for a strategy * 
 * @author Myron Tuttle
 */
public interface SelectedStrategyParameter {
	
	public long getTradeId();
	
	public String getName();
	
	public int getValue();
}
