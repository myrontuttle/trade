package com.myrontuttle.fin.trade.api;

import java.io.Serializable;
import java.util.Hashtable;

public class Trade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String symbol;
	private final Hashtable<String, Integer> parameters;
	
	public Trade(String symbol, Hashtable<String, Integer> parameters) {
		super();
		this.symbol = symbol;
		this.parameters = parameters;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public Hashtable<String, Integer> getParameters() {
		return parameters;
	}
	
}
