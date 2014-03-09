package com.myrontuttle.fin.trade.api;

import java.util.List;

/**
 * Provides access to a trade strategy
 * @author Myron Tuttle
 */
public interface TradeStrategyService extends Service {

	/**
	 * @return The names of the trade strategies that are available from this service
	 */
	public String[] availableTradeStrategies();
	
	/**
	 * @param services required for the strategy
	 * @param strategyName provided in availableTradeStrategies()
	 * @return A specific trade strategy
	 */
	public TradeStrategy getTradeStrategy(String strategyName, List<Service> services) throws Exception;
}
