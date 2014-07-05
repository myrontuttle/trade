package com.myrontuttle.fin.trade.api;

public interface Transaction {
	
	public String getUserId();
	
	public String getPortfolioId();
	
	public String getTransactionId();

	public String getDateTime();

	public String getOrderType();
	
	public String getSymbol();
	
	public double getQuantity();
	
	public double getValue();
}
