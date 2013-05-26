package com.myrontuttle.fin.trade.tradestrategies;

import java.util.concurrent.ScheduledFuture;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.Order;

/**
 * Defines when and how to start, adjust and stop trading a symbol on a portfolio
 * @author Myron Tuttle
 */
public class Trade {

	private final TradeBounds tradeBounds;
	private final String portfolioId;
	
	private final Order openOrder;
	private AlertOrder stopLoss;
	private ScheduledFuture<?> timeInTrade;
	private AlertTradeAdjustment adjustment;
	
	public Trade(TradeBounds tradeBounds, String portfolioId, 
				Order openOrder, AlertOrder stopLoss, 
			ScheduledFuture<?> timeInTrade, AlertTradeAdjustment adjustment) {
		this.tradeBounds = tradeBounds;
		this.portfolioId = portfolioId;
		this.openOrder = openOrder;
		this.stopLoss = stopLoss;
		this.timeInTrade = timeInTrade;
		this.adjustment = adjustment;
	}
	
	public void closeTrade(AlertService alertService, AlertReceiverService alertReceiver) throws Exception {
		alertService.removeAlert(stopLoss.getAlert());
		alertReceiver.stopWatchingFor(stopLoss);
		timeInTrade.cancel(false);
		alertService.removeAlert(adjustment.getAlert());
		alertReceiver.stopWatchingFor(adjustment);
	}

	public TradeBounds getTradeBounds() {
		return tradeBounds;
	}

	public String getPortfolioId() {
		return portfolioId;
	}

	public Order getOpenOrder() {
		return openOrder;
	}
	
	public AlertOrder getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(AlertOrder stopLoss, AlertService alertService, 
								AlertReceiverService alertReceiver) throws Exception {
		alertService.setupAlerts(stopLoss.getAlert());
		alertService.removeAlert(this.stopLoss.getAlert());
		
		alertReceiver.watchFor(stopLoss);
		alertReceiver.stopWatchingFor(this.stopLoss);
		
		this.stopLoss = stopLoss;
	}

	public ScheduledFuture<?> getTimeInTrade() {
		return timeInTrade;
	}

	public void setTimeInTrade(ScheduledFuture<?> timeInTrade) {
		this.timeInTrade.cancel(false);
		this.timeInTrade = timeInTrade;
	}

	public AlertTradeAdjustment getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(AlertTradeAdjustment adjustment, AlertService alertService, 
			AlertReceiverService alertReceiver) throws Exception {
		alertService.setupAlerts(adjustment.getAlert());
		alertService.removeAlert(this.adjustment.getAlert());
		
		alertReceiver.watchFor(adjustment);
		alertReceiver.stopWatchingFor(this.adjustment);
		
		this.adjustment = adjustment;
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
				+ ((tradeBounds == null) ? 0 : tradeBounds.hashCode());
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
		Trade other = (Trade) obj;
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
		if (tradeBounds == null) {
			if (other.tradeBounds != null)
				return false;
		} else if (!tradeBounds.equals(other.tradeBounds))
			return false;
		return true;
	}
}
