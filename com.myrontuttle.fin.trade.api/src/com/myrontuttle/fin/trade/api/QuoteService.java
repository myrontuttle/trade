package com.myrontuttle.fin.trade.api;

/**
 * Provides price quotes for symbols
 * @author Myron Tuttle
 */
public interface QuoteService {

	/**
	 * Provides the last price of a symbol
	 * @param userId ID of user to get price for
	 * @param symbol Of security to get price for
	 * @return Last price of the symbol
	 */
	public double getLast(long userId, String symbol) throws Exception;
}
