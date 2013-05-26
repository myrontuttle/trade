package com.myrontuttle.fin.trade.api;

import java.util.HashMap;

/**
 * Determines the limits for entering, adjusting, and exiting a trade
 * @author Myron Tuttle
 */
public interface TradeStrategy {

	/**
	 * Sets the limits for the strategy
	 * @param limits Name-value pairs that provide upper and lower limits
	 */
	public void setLimits(HashMap<String, String> limits);

	/**
	 * Returns the amount of cash that accounts start with
	 */
	public double getStartingCash();
	
	/**
	 * Opens, adjusts, or closes a trade based on the action
	 * @param alertAction The action to take
	 * @return tradeId if trade was successful, otherwise null
	 * @throws Exception
	 */
	public String takeAction(AlertAction alertAction) throws Exception;
}
