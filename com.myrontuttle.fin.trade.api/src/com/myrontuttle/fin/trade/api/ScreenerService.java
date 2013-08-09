/**
 * 
 */
package com.myrontuttle.fin.trade.api;

/**
 * Service for screening financial instruments with a variety of criteria
 * @author Myron Tuttle
 */
public interface ScreenerService {
	
    /**
     * Shows the criteria available to screen against.
	 * @param userId
     * @return A list of available screener options.
     */
	public AvailableScreenCriteria[] getAvailableCriteria(String userId) throws Exception;
	
	/**
	 * Given a set of criteria to screen for, returns the financial instruments
	 * matching them.
	 * @param userId
	 * @param selectedCriteria An array of the selected criteria used to screen instruments
	 * @param sortBy The position of the selectedCriteria in the list by which to sort results
	 * @param maxSymbols The maximum number of symbols to return
	 * @return A list of financial instruments
	 */
	public String[] screen(String userId, SelectedScreenCriteria[] selectedCriteria, String sortBy, int maxSymbols) 
			throws Exception;
}
