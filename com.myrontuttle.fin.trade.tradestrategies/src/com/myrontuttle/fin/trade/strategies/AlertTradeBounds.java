package com.myrontuttle.fin.trade.strategies;

import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.SelectedAlert;

public class AlertTradeBounds extends AlertAction {

	private static final long serialVersionUID = 1L;

	private final TradeBounds tradeBounds;
	
	public AlertTradeBounds(SelectedAlert alert, String portfolioId, TradeBounds tradeBounds) {
		super(alert, portfolioId);
		this.tradeBounds = tradeBounds;
	}

	public TradeBounds getTradeBounds() {
		return tradeBounds;
	}

	@Override
	public String getActionType() {
		return ActionType.TRADE_BOUNDS.toString();
	}
}
