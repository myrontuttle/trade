package com.myrontuttle.fin.trade.alertreceivers;

import java.util.HashMap;
import java.util.Map;

import com.myrontuttle.fin.trade.api.AlertReceiver;
import com.myrontuttle.fin.trade.api.AlertReceiverService;

public class ReceiverService implements AlertReceiverService {

	private Map<String, Class<? extends AlertReceiver>> receivers;

	// 1. Add the name of the strategies
	public static String[] availableAlertReceivers = new String[]{
		EmailAlertReceiver.NAME
	};
	
	public ReceiverService() {
		receivers = new HashMap<String, Class<? extends AlertReceiver>>();

		// 2. Define strategies
		receivers.put(EmailAlertReceiver.NAME, EmailAlertReceiver.class);
	}
	
	@Override
	public String[] availableAlertReceivers() {
		return availableAlertReceivers;
	}

	@Override
	public AlertReceiver getAlertReceiver(String receiverName) throws Exception {

		if (receivers.containsKey(receiverName)) {
			AlertReceiver ar = receivers.get(receiverName).newInstance();
			return ar;
		} else {
			throw new Exception("No existing receiver with name: " + receiverName);
		}
	}

}
