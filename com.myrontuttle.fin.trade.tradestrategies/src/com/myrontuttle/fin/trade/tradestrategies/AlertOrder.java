package com.myrontuttle.fin.trade.tradestrategies;

import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.SelectedAlert;

public class AlertOrder extends AlertAction {

	private static final long serialVersionUID = 1L;

	private final Order order;
	
	public AlertOrder(SelectedAlert alert, String portfolioId, Order order) {
		super(alert, portfolioId);
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
