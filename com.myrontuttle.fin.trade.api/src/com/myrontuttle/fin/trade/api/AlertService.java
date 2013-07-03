/**
 * 
 */
package com.myrontuttle.fin.trade.api;

/**
 * Service for creating notifications when financial instruments meet 
 * certain triggers.
 * @author Myron Tuttle
 */
public interface AlertService {
	
	/**
	 * Gets the alerts that are available from this service
	 * @param userId
	 * @return Array of alerts
	 */
	public AvailableAlert[] getAvailableAlerts(String userId) throws Exception;
	
	/**
	 * Gets a specific available alert given their id
	 * @return AvailableAlert
	 */
	public AvailableAlert getAlert(String userId, int id) throws Exception;

	/**
	 * Gets the alert that indicates the price has gone below a value
	 * @return Alert for a price moving below a certain value
	 * @throws Exception
	 */
	public AvailableAlert getPriceBelowAlert(String userId) throws Exception;
	
	/**
	 * Gets the alert that indicates the price has gone above a value
	 * @return Alert for a price moving above a certain value alert
	 * @throws Exception
	 */
	public AvailableAlert getPriceAboveAlert(String userId) throws Exception;
	
	/**
	 * Parses a condition from the generic condition that's available to the actual
	 * condition with symbol and values that will be sent when triggered
	 * @param alert The alert we're getting the generic condition from
	 * @param symbol Symbol that we're looking to match on
	 * @param params Values to fill in to the alert
	 * @return parsed condition string
	 */
	public String parseCondition(AvailableAlert alert, String symbol, double... params);

	/**
	 * Returns the potentially calculated lower bound for a condition and criteriaIndex
	 * @param userId
	 * @param id The id of the alert
	 * @param symbol The symbol to get a limit for
	 * @param criteriaIndex The number of the criteria to get the bound for
	 * @return The lower bound
	 */
	public double getLowerDouble(String userId, int id, String symbol, int criteriaIndex);

	/**
	 * Returns the potentially calculated upper bound for a condition and criteriaIndex
	 * @param userId
	 * @param id The id of the alert
	 * @param symbol The symbol to get a limit for
	 * @param criteriaIndex The number of the criteria to get the bound for
	 * @return The upper bound
	 */
	public double getUpperDouble(String userId, int id, String symbol, int criteriaIndex);
	
	/**
	 * Returns the length of a list criteria
	 * @param id The id of the alert
	 * @param criteriaIndex The number of the criteria to get the bound for
	 * @return The upper bound
	 */
	public int getListLength(int id, int criteriaIndex);
	
	/**
	 * Sets up selected alerts to be triggered (potentially)
	 * @param userId
	 * @return Indicates if the alert was set up successfully
	 */
	public boolean setupAlerts(String userId, SelectedAlert... alerts) throws Exception;
	
	/**
	 * Returns the alerts that have been setup
	 * @param userId
	 * @return Array of active alerts
	 * @throws Exception
	 */
	public SelectedAlert[] getActiveAlerts(String userId) throws Exception;
	
	/**
	 * Removes a specific alert
	 * @param userId
	 * @param alert to be removed
	 * @return true if the alert was removed
	 * @throws Exception
	 */
	public boolean removeAlert(String userId, SelectedAlert alert) throws Exception;
	
	/**
	 * Remove all alerts
	 * @param userId
	 * @return Indicates if alerts were removed successfully
	 */
	public boolean removeAllAlerts(String userId) throws Exception;

}
