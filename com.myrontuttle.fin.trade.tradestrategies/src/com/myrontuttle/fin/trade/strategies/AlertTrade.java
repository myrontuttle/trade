package com.myrontuttle.fin.trade.strategies;

import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.Trade;

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
