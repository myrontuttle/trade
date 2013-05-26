package com.myrontuttle.fin.trade.api;

/**
 * Used to place an order
 * @author Myron Tuttle
 */
public abstract class Order {
	
	private final String tradeId;
	private final String orderType;
	private final String symbol;
	private final int quantity;
	
	public Order(String tradeId, String orderType, String symbol, int quantity) {
		this.tradeId = tradeId;
		this.orderType = orderType;
		this.symbol = symbol;
		this.quantity = quantity;
	}

	public String getTradeId() {
		return tradeId;
	}

	public String getOrderType() {
		return orderType;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((tradeId == null) ? 0 : tradeId.hashCode());
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
		Order other = (Order) obj;
		if (orderType == null) {
			if (other.orderType != null)
				return false;
		} else if (!orderType.equals(other.orderType))
			return false;
		if (quantity != other.quantity)
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (tradeId == null) {
			if (other.tradeId != null)
				return false;
		} else if (!tradeId.equals(other.tradeId))
			return false;
		return true;
	}

}
