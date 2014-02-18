package com.myrontuttle.fin.trade.api;


public class AlertTrade extends AlertAction {

	private static final long serialVersionUID = 1L;

	private final Trade trade;
	
	public AlertTrade(SelectedAlert alert, String portfolioId, Trade trade) {
		super(alert, portfolioId);
		this.trade = trade;
	}

	public Trade getTrade() {
		return trade;
	}

	@Override
	public String getActionType() {
		return ActionType.TRADE.toString();
	}
}
