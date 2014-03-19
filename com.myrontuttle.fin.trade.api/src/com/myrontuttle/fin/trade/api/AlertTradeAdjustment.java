package com.myrontuttle.fin.trade.api;


public class AlertTradeAdjustment extends AlertAction {

	private static final long serialVersionUID = 1L;
	
	private final String tradeId;

	public AlertTradeAdjustment(SelectedAlert alert, String userId, String portfolioId, String tradeId) {
		super(alert, userId, portfolioId);
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
