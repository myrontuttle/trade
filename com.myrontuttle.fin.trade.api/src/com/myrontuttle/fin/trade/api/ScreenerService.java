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
     * @return A list of available screener options.
     */
	public AvailableScreenCriteria[] getAvailableCriteria() throws Exception;
	
	/**
	 * Given a set of criteria to screen for, returns the financial instruments
	 * matching them.
	 * @param selectedCriteria An array of the selected criteria used to screen instruments
	 * @param sortBy The position of the selectedCriteria in the list by which to sort results
	 * @param maxSymbols The maximum number of symbols to return
	 * @return A list of financial instruments
	 */
	public String[] screen(SelectedScreenCriteria[] selectedCriteria, int sortBy, int maxSymbols) 
			throws Exception;
}
