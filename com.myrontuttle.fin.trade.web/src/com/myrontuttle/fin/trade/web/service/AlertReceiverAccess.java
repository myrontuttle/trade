package com.myrontuttle.fin.trade.web.service;

import com.myrontuttle.fin.trade.api.AlertReceiverService;

/**
 * Service locator for Alert Receiver Service
 */
public class AlertReceiverAccess {
	
	private static AlertReceiverService alertReceiverService;

	public static AlertReceiverService getAlertReceiverService() {
		return alertReceiverService;
	}

	public void setAlertReceiverService(AlertReceiverService alertReceiverService) {
		AlertReceiverAccess.alertReceiverService = alertReceiverService;
	}

}
