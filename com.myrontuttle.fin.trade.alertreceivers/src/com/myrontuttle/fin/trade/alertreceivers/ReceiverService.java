package com.myrontuttle.fin.trade.alertreceivers;

import java.util.HashMap;
import java.util.Map;

import com.myrontuttle.fin.trade.api.AlertReceiver;
import com.myrontuttle.fin.trade.api.AlertReceiverService;

public class ReceiverService implements AlertReceiverService {

	private Map<String, AlertReceiver> receivers;

	// Add the name of any additional strategies here
	public static String[] availableAlertReceivers = new String[]{
		EmailAlertReceiver.NAME
	};
	
	public ReceiverService() {
		receivers = new HashMap<String, AlertReceiver>();
	}
	
	@Override
	public String[] availableReceiverTypes() {
		return availableAlertReceivers;
	}

	@Override
	public AlertReceiver getAlertReceiver(String receiverId, String receiverType) throws Exception {

		if (receivers.containsKey(receiverId)) {
			return receivers.get(receiverId);
		} else {
			if (receiverType.equals(EmailAlertReceiver.NAME)) {
				EmailAlertReceiver ear = new EmailAlertReceiver();
				receivers.put(receiverId, ear);
				return ear;
			} else {
				throw new Exception(receiverType + " is not a valid receiver type");
			}
		}
	}

	@Override
	public void removeAlertReceiver(String receiverId) {
		receivers.remove(receiverId);
	}

}
