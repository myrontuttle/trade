package com.myrontuttle.fin.trade.strategies;

import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.SelectedAlert;

public class AlertTradeAdjustment extends AlertAction {

	private static final long serialVersionUID = 1L;
	
	private final String tradeId;

	public AlertTradeAdjustment(SelectedAlert alert, String portfolioId, String tradeId) {
		super(alert, portfolioId);
		this.tradeId = tradeId;
	}

	public String getTradeId() {
		return tradeId;
	}

	@Override
	public String getActionType() {
		return ActionType.TRADE_ADJUSTMENT.toString();
	}

}
