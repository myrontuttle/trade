package com.myrontuttle.fin.trade.api;

import java.util.List;
import java.util.Map;

/**
 * Service that imports alerts as they're fired
 * @author Myron Tuttle
 */
public interface AlertReceiverService {

	/**
	 * @return The names of the alert receiver types that are available from this service
	 */
	public String[] availableReceiverTypes();

	/**
	 * Indicates what parameters are available for a type of receiver
	 * @param receiverType One of the types returned from {@link #availableReceiverTypes() availableReceiverTypes}
	 * @return Map from parameter names to their descriptions
	 */
	public Map<String, String> getAvailableParameters(String receiverType);
	
	/**
	 * Adds a new receiver for a user to receive alerts from
	 * @param userId Identifies user that receives the alerts
	 * @param receiverType One of the types returned from {@link #availableReceiverTypes() availableReceiverTypes}
	 * @return String Id of the receiver added
	 */
	public String addReceiver(String userId, String receiverType);

	/**
	 * Lists the receivers assigned to this user
	 * @param userId Identifies user that receives the alerts
	 */
	public List<String> getReceivers(String userId);
	
	/**
	 * Removes an alert receiver and all its associated parameters for a user
	 * @param userId Identifies user that receives the alerts
	 * @param receiverId Id of the receiver to remove
	 */
	public void removeReceiver(String receiverId);
	
	/**
	 * Sets parameters required for a receiver to connect to an alert source
	 * @param receiverId Id of the receiver to set the parameter for
	 * @param name Parameter name
	 * @param value Parameter value
	 */
	public void setReceiverParameter(String receiverId, String name, String value);
	
	/**
	 * Lists the parameters that are set for a specific receiver
	 * @param receiverId Id of receiver to get parameters for
	 * @return Map of parameter names to values
	 */
	public Map<String, String> getReceiverParameters(String receiverId);
	
	/**
	 * Indicates whether all of the required parameters for an alert receiver are set
	 * @param receiverId Allows multiple receivers with different id's
	 * @return True if all the parameters for this user and receiver are set
	 */
	public boolean parametersAreSet(String receiverId);
	
	/**
	 * Starts receiving alerts for a user from a specific receiver
	 * @param receiverId Id of the receiver to start
	 */
	public void startReceiving(String receiverId);

	/**
	 * Starts receiving alerts for a user from all associated receivers
	 * @param userId Id of the receiver to start
	 */
	public void startReceivingAll(String userId);
	
	/**
	 * Stop receiving alerts for a user from a specific receiver
	 * @param receiverId Id of receiver to stop
	 */
	public void stopReceiving(String receiverId);

	/**
	 * Stops receiving alerts for a user from all associated receivers
	 * @param userId Id of the receiver to start
	 */
	public void stopReceivingAll(String userId);
}
