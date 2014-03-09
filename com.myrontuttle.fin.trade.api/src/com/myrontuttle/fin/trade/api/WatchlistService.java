package com.myrontuttle.fin.trade.api;

/**
 * Service for creating lists of issues to watch
 * @author Myron Tuttle
 */
public interface WatchlistService extends Service {

	/**
	 * Create a new watchlist
	 * @param userId
	 * @param name Readable name of the watchlist
	 * @returns String Id of the created watchlist
	 */
	public String create(String userId, String name) throws Exception;

	/**
	 * Delete a watchlist
	 * @param userId
	 * @param watchlistId Id of the watchlist
	 * @returns boolean Indicates whether the removal was successful
	 */
	public boolean delete(String userId, String watchlistId) throws Exception;

	/**
	 * Rename a watchlist
	 * @param userId
	 * @param watchlistId Id of the watchlist
	 * @param newName New name for the watchlist
	 * @returns boolean Indicates whether the renaming was successful
	 */
	public boolean rename(String userId, String watchlistId, String newName) throws Exception;
	
	/**
	 * Add a holding to the watchlist
	 * @param userId
	 * @param watchlistId Id of the watchlist we're adding to
	 * @param symbol Symbol of the issue we're adding
	 * @returns String Transaction/Lot id
	 */
	public String addHolding(String userId, String watchlistId, String symbol) throws Exception;

	/**
	 * Retrieves a list of holdings in a watchlist
	 * @param userId
	 * @param watchlistId Id of the watchlist we're adding to
	 * @returns String[] List of holdings
	 */
	public String[] retrieveHoldings(String userId, String watchlistId) throws Exception;
	
	/**
	 * Remove a holding from the watchlist
	 * @param userId
	 * @param watchlistId Id of the watchlist we're removing from
	 * @param symbol Symbol of the issue we're removing
	 * @returns boolean Indicates whether the removal was successful
	 */
	public boolean removeHolding(String userId, String watchlistId, String symbol) throws Exception;
	
}
