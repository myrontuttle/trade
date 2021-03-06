package com.myrontuttle.fin.trade.strategies;

import java.util.HashMap;

import com.myrontuttle.fin.trade.api.AvailableStrategyParameter;

/**
 * Determines the behavior for entering, adjusting, and exiting a trade
 * @author Myron Tuttle
 */
public interface TradeStrategy {

	/**
	 * @return The name of this strategy
	 */
	public String getName();
	
	/**
	 * @return A description of this strategy
	 */
	public String getDescription();
	
	/**
	 * @return The list of parameters needed for this strategy
	 */
	public AvailableStrategyParameter[] availableParameters();
	
	/**
	 * Sets the limits for the strategy parameters
	 * @param limits Name-value pairs that provide upper and lower limits
	 */
	public void setParameterLimits(HashMap<String, Integer> limits);
	
	/**
	 * Sets the number of order types that are available to the strategy
	 * @param OrderTypesAvailable
	 */
	public void setOrderTypesAvailable(int OrderTypesAvailable);
	
	/**
	 * Opens, adjusts, or closes a trade based on the action
	 * @param alertAction The action to take
	 * @return tradeId if trade was successful, otherwise null
	 * @throws Exception
	 */
	public String takeAction(String actionType, Trade trade) throws Exception;

	/**
	 * Describes the actions to be taken based on a user and trade
	 * @param userId
	 * @param trade
	 * @return
	 * @throws Exception
	 */
	public String[] describeTrade(String userId, Trade trade) throws Exception;
}
