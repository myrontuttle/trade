package com.myrontuttle.fin.trade.api;

/**
 * Provides an alert receiver
 * @author Myron Tuttle
 */
public interface AlertReceiverService extends Service {

	/**
	 * @return The names of the alert receivers that are available from this service
	 */
	public String[] availableAlertReceivers();
	
	/**
	 * @param receiverName provided in availableAlertReceivers()
	 * @return A specific alert receiver
	 */
	public AlertReceiver getAlertReceiver(String receiverName) throws Exception;
}
