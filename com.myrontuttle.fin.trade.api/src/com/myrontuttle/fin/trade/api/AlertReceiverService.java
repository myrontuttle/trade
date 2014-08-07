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
	 * Indicates the options that are available for a specific alert receiver parameter
	 * @param receiverType One of the types returned from {@link #availableReceiverTypes() availableReceiverTypes}
	 * @param parameter Parameter name from {@link #getAvailableParameters() getAvailableParameters}
	 * @return Array of parameter options
	 */
	public String[] getAvailableParameterOptions(String receiverType, String parameter);
	
	/**
	 * Adds a new receiver for a user to receive alerts from
	 * @param userId Identifies user that receives the alerts
	 * @param receiverType One of the types returned from {@link #availableReceiverTypes() availableReceiverTypes}
	 * @return String Id of the receiver added
	 */
	public long addReceiver(long userId, String receiverType);

	/**
	 * Lists the receivers assigned to this user
	 * @param userId Identifies user that receives the alerts
	 */
	public List<Long> getReceivers(long userId);
	
	/**
	 * Removes an alert receiver and all its associated parameters for a user
	 * @param userId Identifies user that receives the alerts
	 * @param receiverId Id of the receiver to remove
	 */
	public void removeReceiver(long receiverId);
	
	/**
	 * Sets parameters required for a receiver to connect to an alert source
	 * @param receiverId Id of the receiver to set the parameter for
	 * @param name Parameter name
	 * @param value Parameter value
	 */
	public void setReceiverParameter(long receiverId, String name, String value);
	
	/**
	 * Lists the parameters that are set for a specific receiver
	 * @param receiverId Id of receiver to get parameters for
	 * @return Map of parameter names to values
	 */
	public Map<String, String> getReceiverParameters(long receiverId);
	
	/**
	 * Indicates whether all of the required parameters for an alert receiver are set
	 * @param receiverId Allows multiple receivers with different id's
	 * @return True if all the parameters for this user and receiver are set
	 */
	public boolean parametersAreSet(long receiverId);
	
	/**
	 * Toggles whether this receiver should be actively receiving
	 * @param receiverId Receiver to set
	 * @param isActive
	 */
	public void setReceiverActive(long receiverId, boolean isActive);
	
	/**
	 * Starts receiving alerts for a user from a specific receiver
	 * @param receiverId Id of the receiver to start
	 */
	public void startReceiving(long receiverId);

	/**
	 * Starts receiving alerts for a user from all associated receivers
	 * @param userId Id of the receiver to start
	 */
	public void startReceivingAll(long userId);
	
	/**
	 * Stop receiving alerts for a user from a specific receiver
	 * @param receiverId Id of receiver to stop
	 */
	public void stopReceiving(long receiverId);

	/**
	 * Stops receiving alerts for a user from all associated receivers
	 * @param userId Id of the receiver to start
	 */
	public void stopReceivingAll(long userId);
}
