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
	 * Creates a trade to be potentially opened
	 * @param tradeStrategy One of the availableTradeStrategies
	 * @param userId The user the trade will be for
	 * @param portfolioId The account the trade will affect
	 * @param alertUserId The id to create alerts for
	 * @param symbol The symbol to be traded
	 * @return The id of this trade
	 */
	public String addTrade(String tradeStrategy, String userId, String portfolioId, 
			String alertUserId, String symbol);
	
	/**
	 * Indicates whether a trade with this Id exists
	 * @param tradeId
	 * @return True if this is a trade in the system
	 */
	public boolean tradeExists(String tradeId);
	
	/**
	 * Describes the trade with instructions for a trader
	 * @param tradeId The trade to describe
	 * @return An array of instructions describing the trade
	 */
	public String[] describeTrade(String tradeId);
	
	/**
	 * Removes a trade from the system (including all parameters associated with trade)
	 * @param tradeId ID of trade to remove
	 */
	public void removeTrade(String tradeId);
	
	/**
	 * Removes all trades associated with a user
	 * @param userId ID of user to remove trades for
	 */
	public void removeAllTrades(String userId);
	
	/**
	 * Lists the available trade parameters given a trade strategy
	 * @param tradeStrategy One of availableTradeStrategies()
	 * @return Array of available strategy parameters
	 */
	public AvailableStrategyParameter[] availableTradeParameters(String tradeStrategy);
	
	/**
	 * Adds definition to a trade.  If a parameter with the same name already exists for the
	 * tradeId, it is updated with the supplied value.
	 * @param tradeId Id of the trade this parameter is for
	 * @param key The name of this parameter
	 * @param value The value of this parameter
	 */
	public void setTradeParameter(String tradeId, String name, int value);
	
	/**
	 * Gets the value of a trade parameter
	 * @param tradeId Id of the trade whose parameter we're checking
	 * @param name The name of the parameter we're checking
	 * @return The value of the trade parameter.  Returns null if the trade parameter isn't set
	 */
	public int getTradeParameter(String tradeId, String name);
	
	/**
	 * The trade action this strategy uses to start a trade
	 * @param tradeStrategy One of availableTradeStrategies()
	 * @return TradeAction to use when starting trade events
	 */
	public String tradeActionToStart(String tradeStrategy);
	
	/**
	 * Indicates to the service what type of action to take on a trade when a condition occurs
	 * @param tradeId The trade to act on
	 * @param event The event that indicates that the action should be taken
	 * @param actionType The type of action to take
	 * @param trigger The id of the alert that triggers this event (or DateTime when we trigger)
	 */
	public void setTradeEvent(String tradeId, String event, String actionType, String trigger);
	
	/**
	 * Removes all trade events for a specific trade
	 * @param tradeId The id of the trade to remove events from
	 */
	public void removeAllTradeEvents(String tradeId);
	
	/**
	 * Handle to notify the Trade Strategy that an event (i.e. alert triggered) has occurred
	 * @param event The condition of the alert that happened
	 */
	public void eventOccurred(String event);
	
}
