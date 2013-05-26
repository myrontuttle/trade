package com.myrontuttle.fin.trade.api;

/**
 * Provides price quotes for symbols
 * @author Myron Tuttle
 */
public interface QuoteService {

	/**
	 * Provides the last price of a symbol
	 * @param symbol
	 * @return Last price of the symbol
	 */
	public double getLast(String symbol) throws Exception;
}
