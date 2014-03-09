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
}
