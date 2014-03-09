package com.myrontuttle.fin.trade.strategies;

import java.util.concurrent.ScheduledFuture;

import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.AlertOrder;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.Trade;

/**
 * Defines when and how to start, adjust and stop trading a symbol on a portfolio
 * @author Myron Tuttle
 */
public class BoundedTrade {

	private final Trade trade;
	private final String portfolioId;
	
	private final Order openOrder;
	private AlertAction outOfTheMoney;
	private ScheduledFuture<?> timeLimit;
	private AlertAction inTheMoney;
	
	public BoundedTrade(Trade trade, String portfolioId, 
				Order openOrder, AlertAction outOfTheMoney, 
			ScheduledFuture<?> timeLimit, AlertAction inTheMoney) {
		this.trade = trade;
		this.portfolioId = portfolioId;
		this.openOrder = openOrder;
		this.outOfTheMoney = outOfTheMoney;
		this.timeLimit = timeLimit;
		this.inTheMoney = inTheMoney;
	}
	
	public void closeTrade(String userId, AlertService alertService, AlertReceiverService alertReceiver) throws Exception {
		alertService.removeAlert(userId, outOfTheMoney.getAlert());
		alertReceiver.stopWatchingFor(outOfTheMoney);
		timeLimit.cancel(false);
		alertService.removeAlert(userId, inTheMoney.getAlert());
		alertReceiver.stopWatchingFor(inTheMoney);
	}

	public Trade getTrade() {
		return trade;
	}

	public String getPortfolioId() {
		return portfolioId;
	}

	public Order getOpenOrder() {
		return openOrder;
	}
	
	public AlertAction getOutOfTheMoney() {
		return outOfTheMoney;
	}

	public void setOutOfTheMoney(String userId, AlertOrder priceReached, AlertService alertService, 
								AlertReceiverService alertReceiver) throws Exception {
		alertService.setupAlerts(userId, priceReached.getAlert());
		alertService.removeAlert(userId, this.outOfTheMoney.getAlert());
		
		alertReceiver.watchFor(priceReached);
		alertReceiver.stopWatchingFor(this.outOfTheMoney);
		
		this.outOfTheMoney = priceReached;
	}

	public ScheduledFuture<?> getTimeInTrade() {
		return timeLimit;
	}

	public void setTimeLimit(ScheduledFuture<?> timeLimit) {
		this.timeLimit.cancel(false);
		this.timeLimit = timeLimit;
	}

	public AlertAction getInTheMoney() {
		return inTheMoney;
	}

	public void setInTheMoney(String userId, AlertAction priceReached, AlertService alertService, 
			AlertReceiverService alertReceiver) throws Exception {
		alertService.setupAlerts(userId, priceReached.getAlert());
		alertService.removeAlert(userId, this.inTheMoney.getAlert());
		
		alertReceiver.watchFor(priceReached);
		alertReceiver.stopWatchingFor(this.inTheMoney);
		
		this.inTheMoney = priceReached;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((openOrder == null) ? 0 : openOrder.hashCode());
		result = prime * result
				+ ((portfolioId == null) ? 0 : portfolioId.hashCode());
		result = prime * result
				+ ((trade == null) ? 0 : trade.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoundedTrade other = (BoundedTrade) obj;
		if (openOrder == null) {
			if (other.openOrder != null)
				return false;
		} else if (!openOrder.equals(other.openOrder))
			return false;
		if (portfolioId == null) {
			if (other.portfolioId != null)
				return false;
		} else if (!portfolioId.equals(other.portfolioId))
			return false;
		if (trade == null) {
			if (other.trade != null)
				return false;
		} else if (!trade.equals(other.trade))
			return false;
		return true;
	}
}
