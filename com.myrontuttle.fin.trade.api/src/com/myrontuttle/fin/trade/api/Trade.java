package com.myrontuttle.fin.trade.api;

public class Trade {

	private final String symbol;
	private final TradeParameter[] parameters;
	
	public Trade(String symbol, TradeParameter[] parameters) {
		super();
		this.symbol = symbol;
		this.parameters = parameters;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public TradeParameter[] getParameters() {
		return parameters;
	}
	
}
