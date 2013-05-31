package com.myrontuttle.fin.trade.api;

import java.util.HashMap;

/**
 * Receives and processes alerts
 * @author Myron Tuttle
 */
public interface AlertReceiverService {

	/**
	 * Starts the receiver receiving alerts
	 * @param userId
	 * @param connectionDetails Name-value pairs that provide details necessary to start receiving
	 * @return true if the start was successful
	 */
	public boolean startReceiving(String userId, HashMap<String, String> connectionDetails);
	
	/**
	 * Stops the receiver from receiving alerts
	 * @return true if the stop was successful
	 */
	public boolean stopReceiving();
	
	/**
	 * Sets the alerts the receiver should be watching for to make trades or orders
	 * @param alertActions Define the alerts and trades/orders to make when those 
	 * 			alerts are triggered
	 */
	public void watchFor(AlertAction... alertActions);
	
	/**
	 * Removes an alert from the list of alerts being watched for
	 * @param alertAction to be removed
	 */
	public void stopWatchingFor(AlertAction alertAction);
	
	/**
	 * Tries to match a condition received with an existing alert
	 * @param userId
	 * @param condition The triggered condition to match against.
	 * @return The number of times the condition matched an alert
	 */
	public int matchAlert(String userId, String condition);
}
