package com.myrontuttle.fin.trade.api;

/**
 * Provides access to a trade strategy
 * @author Myron Tuttle
 */
public interface TradeStrategyService {

	/**
	 * @return The names of the trade strategies that are available from this service
	 */
	public String[] availableTradeStrategies();
	
	/**
	 * @param strategyName provided in availableTradeStrategies()
	 * @return A specific trade strategy
	 */
	public TradeStrategy getTradeStrategy(String strategyName);
}
