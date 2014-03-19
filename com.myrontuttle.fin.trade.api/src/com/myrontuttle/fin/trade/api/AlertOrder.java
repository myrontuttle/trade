package com.myrontuttle.fin.trade.api;


public class AlertOrder extends AlertAction {

	private static final long serialVersionUID = 1L;

	private final Order order;
	
	public AlertOrder(SelectedAlert alert, String userId, String portfolioId, Order order) {
		super(alert, userId, portfolioId);
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public String getActionType() {
		return ActionType.ORDER.toString();
	}
}
