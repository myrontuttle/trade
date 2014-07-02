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
	 * @param userId User to receive alerts
	 * @return Array of alerts
	 */
	public AvailableAlert[] getAvailableAlerts(String userId) throws Exception;
	
	/**
	 * Gets a specific available alert given their id
	 * @param userId User to receive alerts
	 * @param id The id of the alert
	 * @return AvailableAlert
	 */
	public AvailableAlert getAlert(String userId, int id) throws Exception;

	/**
	 * Gets the alert that indicates the price has gone below a value
	 * @param userId User to receive alerts
	 * @return Alert for a price moving below a certain value
	 * @throws Exception
	 */
	public AvailableAlert getPriceBelowAlert(String userId) throws Exception;
	
	/**
	 * Gets the alert that indicates the price has gone above a value
	 * @param userId User to receive alerts
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
	public String parseCondition(AvailableAlert alert, String symbol, 
			double... params) throws Exception;

	/**
	 * Returns the potentially calculated lower bound for a condition and criteriaIndex
	 * @param userId User to receive alerts
	 * @param id The id of the alert
	 * @param symbol The symbol to get a limit for
	 * @param criteriaIndex The number of the criteria to get the bound for
	 * @return The lower bound
	 */
	public double getLowerDouble(String userId, int id, String symbol, 
			int criteriaIndex) throws Exception;

	/**
	 * Returns the potentially calculated upper bound for a condition and criteriaIndex
	 * @param userId User to receive alerts
	 * @param id The id of the alert
	 * @param symbol The symbol to get a limit for
	 * @param criteriaIndex The number of the criteria to get the bound for
	 * @return The upper bound
	 */
	public double getUpperDouble(String userId, int id, String symbol, 
			int criteriaIndex) throws Exception;
	
	/**
	 * Returns the length of a list criteria
	 * @param userId User to receive alerts
	 * @param id The id of the alert
	 * @param criteriaIndex The number of the criteria to get the bound for
	 * @return The upper bound
	 */
	public int getListLength(String userId, int id, int criteriaIndex) throws Exception;
	
	/**
	 * Adds the customer email and delivery format if it isn't already there
	 * @param userId User to receive alerts
	 * @param alertAddress To deliver alerts to
	 * @return boolean True if the action was successful
	 * @throws Exception
	 */
	public boolean addAlertDestination(String userId, String alertAddress, 
										String alertType) throws Exception;

	/**
	 * Sets up selected alerts to be triggered (potentially)
	 * @param userId User to receive alerts
	 * @param SelectedAlert Alerts to setup
	 * @return Array of id's for the setup alerts
	 */
	public String[] setupAlerts(String userId, SelectedAlert... alerts) throws Exception;
	
	/**
	 * Sets up a single alert to potentially be triggered
	 * @param userId User to receive alerts
	 * @param alertId Id of alert
	 * @param condition Condition that triggers alert
	 * @param symbol Symbol that alert triggers for
	 * @param params Parameters that define when the alert is triggered
	 * @return Id of the setup alert
	 */
	public String setupAlert(String userId, int alertId, String condition, String symbol, double... params);
	
	/**
	 * Returns the alerts that have been setup
	 * @param userId User to receive alerts
	 * @return Array of active alerts
	 * @throws Exception
	 */
	public SelectedAlert[] getActiveAlerts(String userId) throws Exception;
	
	/**
	 * Removes a specific alert
	 * @param userId User to remove alerts from
	 * @param alertId Alert to be removed
	 * @return true if the alert was removed
	 * @throws Exception
	 */
	public boolean removeAlert(String userId, String alertId) throws Exception;
	
	/**
	 * Remove all alerts
	 * @param userId User to remove alerts from
	 * @return Indicates if alerts were removed successfully
	 */
	public boolean removeAllAlerts(String userId) throws Exception;

}
