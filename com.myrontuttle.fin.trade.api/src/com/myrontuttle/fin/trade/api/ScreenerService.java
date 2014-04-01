/**
 * 
 */
package com.myrontuttle.fin.trade.api;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Service for screening financial instruments with a variety of criteria
 * @author Myron Tuttle
 */
public interface ScreenerService extends Service {
	
    /**
     * Shows the criteria available to screen against.
	 * @param userId
     * @return A list of available screener options.
     */
	public AvailableScreenCriteria[] getAvailableCriteria(String userId) throws Exception;
	
	/**
	 * Gets the list of criteria names which are used for screening
	 * @return List of criteria names
	 * @throws Exception
	 */
	public HashSet<String> getCriteriaUsed() throws Exception;
	
	/**
	 * Sets which of the available criteria will be used for screening
	 * @param criteriaUsed Names of the available criteria to be used for screening
	 * @throws Exception
	 */
	public void setCriteriaUsed(HashSet<String> criteriaUsed) throws Exception;
	
	/**
	 * Provides list of screen criteria which will always be included in screens
	 * @return Array of SelectedScreenCriteria
	 * @throws Exception
	 */
	public SelectedScreenCriteria[] getFixedCriteria() throws Exception;
	
	/**
	 * Sets screen criteria which will always be included in screens
	 * @param fixedCriteria
	 * @throws Exception
	 */
	public void setFixedCriteria(SelectedScreenCriteria[] fixedCriteria) throws Exception;
	
	/**
	 * Initializes the Screener service
	 * @param settings
	 * @throws Exception
	 */
	public void initialize(HashMap<String, String> settings) throws Exception;
	
	/**
	 * Returns settings that have been used to initialize the screener
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> getSettings() throws Exception;
	
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
