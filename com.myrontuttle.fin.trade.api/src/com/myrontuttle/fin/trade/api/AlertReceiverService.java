package com.myrontuttle.fin.trade.api;

/**
 * Provides an alert receiver
 * @author Myron Tuttle
 */
public interface AlertReceiverService extends Service {

	/**
	 * @return The names of the alert receiver types that are available from this service
	 */
	public String[] availableReceiverTypes();
	
	/**
	 * @param receiverId Id of receiver to get
	 * @param receiverType provided in availableReceiverTypes()
	 * @return A specific alert receiver
	 */
	public AlertReceiver getAlertReceiver(String receiverId, String receiverType) throws Exception;
	
	/**
	 * @param receiverId Id of receiver to remove
	 * @throws Exception
	 */
	public void removeAlertReceiver(String receiverId);
}
