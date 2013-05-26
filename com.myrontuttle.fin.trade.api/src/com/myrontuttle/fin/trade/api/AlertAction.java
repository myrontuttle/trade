package com.myrontuttle.fin.trade.api;

import java.io.Serializable;

/**
 * Takes a specific action when its alert is triggered
 * @author Myron Tuttle
 */
public abstract class AlertAction implements Serializable  {

	private static final long serialVersionUID = 1L;

	private final SelectedAlert alert;
	private final String portfolioId;

	public AlertAction(SelectedAlert alert, String portfolioId) {
		this.alert = alert;
		this.portfolioId = portfolioId;
	}

	public SelectedAlert getAlert() {
		return alert;
	}

	public String getPortfolioId() {
		return portfolioId;
	}
	
	public abstract String getActionType();

	/**
	 * Checks if the condition contains the condition in the alert
	 * @param condition The condition indicated in the alert
	 * @return True if the condition contains the condition in the alert 
	 */
	public boolean alertMatches(String condition) {
		if (condition.contains(alert.getCondition())) {
			return true;
		} else {
			return false;
		}
	}
}
