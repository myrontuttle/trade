package com.myrontuttle.fin.trade.api;

/**
 * Indicates that the boundaries of a trade are changing
 * @author Myron Tuttle
 */
public abstract class TradeAdjustment {

	private final String tradeId;
	
	public TradeAdjustment(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeId() {
		return tradeId;
	}
}
