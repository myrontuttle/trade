package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.Transaction;

public class TransactionMock implements Transaction {
	
	private String transactionId;
	private String dateTime;
	private String orderType;
	private String symbol;
	private double quantity;
	private double value;
	
	public TransactionMock(String transactionId, String dateTime, String orderType,
			String symbol, double quantity, double value) {
		this.transactionId = transactionId;
		this.dateTime = dateTime;
		this.orderType = orderType;
		this.symbol = symbol;
		this.quantity = quantity;
		this.value = value;
	}

	@Override
	public String getTransactionId() {
		return transactionId;
	}

	@Override
	public String getDateTime() {
		return dateTime;
	}

	@Override
	public String getOrderType() {
		return orderType;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}

	@Override
	public double getQuantity() {
		return quantity;
	}

	@Override
	public double getValue() {
		return value;
	}

}
