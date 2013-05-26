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
	 * Provide an upper bound to values used for trade allocation
	 */
	public int tradeAllocationUpper();

	/**
	 * Provide a lower bound to values used for trade allocation
	 */
	public int tradeAllocationLower();

	/**
	 * Provide an upper bound to values used for acceptable loss
	 */
	public int acceptableLossUpper();

	/**
	 * Provide a lower bound to value used for acceptable loss
	 */
	public int acceptableLossLower();

	/**
	 * Provide an upper bound to values used for time in trade
	 */
	public int timeInTradeUpper();

	/**
	 * Provide a lower bound to values used for time in trade
	 */
	public int timeInTradeLower();

	/**
	 * Provide an upper bound to values used for adjust at
	 */
	public int adjustAtUpper();

	/**
	 * Provide a lower bound to value used for adjust at
	 */
	public int adjustAtLower();
	
	/**
	 * Opens, adjusts, or closes a trade based on the action
	 * @param alertAction The action to take
	 * @return tradeId if trade was successful, otherwise null
	 * @throws Exception
	 */
	public String takeAction(AlertAction alertAction) throws Exception;
	
	/**
	 * Opens a trade
	 * @param tradeBounds to set for the trade
	 * @return tradeId if trade was successful, otherwise null
	 */
	public String openTrade(TradeBounds tradeBounds, String portfolioId) throws Exception;
	
	/**
	 * Adjusts an open trade
	 * @param tradeAdjustment The adjustment to make
	 * @return tradeId if trade was successful, otherwise null
	 */
	public String adjustTrade(TradeAdjustment tradeAdjustment) throws Exception;
	
	/**
	 * Places an order
	 * @param order to make
	 * @param portfolioId Id of portfolio to place order on
	 * @return true if the order was successfully placed
	 */
	public boolean closeTrade(Order order, String portfolioId) throws Exception;
}
