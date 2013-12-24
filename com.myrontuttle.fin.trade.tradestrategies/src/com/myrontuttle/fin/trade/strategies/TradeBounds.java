package com.myrontuttle.fin.trade.strategies;

/**
 * Limits of opening, adjusting and closing a trade
 * @author Myron Tuttle
 */
public class TradeBounds {

	private final String symbol;
	
	// Opening
	private final int openOrderType;
	private final int tradeAllocation;
	
	// Closing
	private final int acceptableLoss;
	private final int timeInTrade; // in seconds
	private final int adjustAt;
	
	/**
	 * @param symbol Symbol to trade on
	 * @param openOrderType What kind of order to open the trade with
	 * @param tradeAllocation How much of the available cash should be allocated to this trade
	 * @param acceptableLoss What is an acceptable loss for this trade
	 * @param timeInTrade How long should the trade stay open
	 * @param adjustAt At what point should the limits above be adjusted
	 */
	public TradeBounds(String symbol, 
				int openOrderType,
				int tradeAllocation, 
				int acceptableLoss, 
				int timeInTrade,
				int adjustAt) {
		this.symbol = symbol;
		this.openOrderType = openOrderType;
		this.tradeAllocation = tradeAllocation;
		this.acceptableLoss = acceptableLoss;
		this.timeInTrade = timeInTrade;
		this.adjustAt = adjustAt;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the orderTypeToOpen
	 */
	public int getOpenOrderType() {
		return openOrderType;
	}

	/**
	 * @return the tradeAllocation
	 */
	public int getTradeAllocation() {
		return tradeAllocation;
	}

	/**
	 * @return the acceptableLoss
	 */
	public int getAcceptableLoss() {
		return acceptableLoss;
	}

	/**
	 * @return the timeInTrade
	 */
	public int getTimeInTrade() {
		return timeInTrade;
	}

	/**
	 * @return the adjustAt
	 */
	public int getAdjustAt() {
		return adjustAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acceptableLoss;
		result = prime * result + adjustAt;
		result = prime * result + openOrderType;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + timeInTrade;
		result = prime * result + tradeAllocation;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeBounds other = (TradeBounds) obj;
		if (acceptableLoss != other.acceptableLoss)
			return false;
		if (adjustAt != other.adjustAt)
			return false;
		if (openOrderType != other.openOrderType)
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (timeInTrade != other.timeInTrade)
			return false;
		if (tradeAllocation != other.tradeAllocation)
			return false;
		return true;
	}
}
