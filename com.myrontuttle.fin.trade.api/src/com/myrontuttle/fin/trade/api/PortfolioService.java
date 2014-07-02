package com.myrontuttle.fin.trade.api;

import java.util.ArrayList;

/**
 * Service for creating portfolios of issue holdings 
 * @author Myron Tuttle
 */
public interface PortfolioService {

	/**
	 * Create a new portfolio
	 * @param userId
	 * @param name Readable name of the portfolio
	 * @throws Exception 
	 * @return Id of the created portfolio
	 */
	public String create(String userId, String name) throws Exception;
	
	/**
	 * Delete a portfolio
	 * @param userId
	 * @param portfolioId ID of the portfolio
	 * @return Indicates whether the deletion was successful
	 */
	public boolean delete(String userId, String portfolioId) throws Exception;

	/**
	 * Rename a portfolio
	 * @param userId
	 * @param portfolioId Id of the portfolio
	 * @param newName New name for the portfolio
	 * @return Indicates whether the renaming was successful
	 */
	public boolean rename(String userId, String portfolioId, String newName) throws Exception;

	/**
	 * Add a cash transaction (either credit or debit)
	 * @param userId
	 * @param portfolioId Id of the portfolio we're adding the cash to
	 * @param quantity Amount of cash to add
	 * @param credit Whether this is a credit or debit transaction
	 * @param open Whether this is to open the portfolio
	 * @return Indicates whether the transaction was successful
	 */
	public boolean addCashTransaction(String userId, String portfolioId, double quantity, 
										boolean credit, boolean open) throws Exception;

	/**
	 * Get the current cash available for opening a position
	 * @param userId
	 * @param portfolioId Id of the portfolio we want to know cash balance
	 * @return Available cash
	 */
	public double getAvailableBalance(String userId, String portfolioId) throws Exception;
	
	/**
	 * Provides all of the transactions that have occurred for this user's portfolio
	 * @param userId
	 * @param portfolioId
	 * @return Array of transactions
	 * @throws Exception
	 */
	public ArrayList<Transaction> getTransactions(String userId, String portfolioId) throws Exception;
	
	/**
	 * Provides a single transaction with the provided transactionId
	 * @param userId User this transaction belongs to
	 * @param portfolioId Portfolio which contains this transaction
	 * @param transactionId Id of transaction to retrieve
	 * @return
	 * @throws Exception
	 */
	public Transaction getTransaction(String transactionId) throws Exception;
	
	/**
	 * The order types available to open a position
	 * @param userId
	 * @return An array of order types available from this portfolio for opening a position
	 */
	public String[] openOrderTypesAvailable(String userId) throws Exception ;
	
	/**
	 * Indicates whether an order type would benefit from a price increase
	 * @param orderType From the openOrderTypesAvailable
	 * @return True if rising prices would result in a better outcome for a holding
	 */
	public boolean priceRiseGood(String orderType) throws Exception ;
	
	/**
	 * Open a position with a particular order type
	 * @param userId
	 * @param portfolioId Id of the portfolio where we're buying shares for
	 * @param order The order to open the position with
	 * @param symbol Symbol we're opening a position with
	 * @param quantity Amount of symbol to open position with
	 * @param orderType From openOrderTypesAvailable
	 * @return Transaction/Lot id
	 */
	public String openPosition(String userId, String portfolioId, String symbol, double quantity,
			String orderType) throws Exception;

	/**
	 * The order types available to close a position
	 * @param userId
	 * @return An array of order types available from this portfolio for closing a position
	 */
	public String[] closeOrderTypesAvailable(String userId) throws Exception ;
	
	/**
	 * Close a position with a particular order type
	 * @param userId
	 * @param portfolioId ID of the portfolio where we're selling shares from
	 * @param order The order to close the position with
	 * @param symbol Symbol for which we're closing the position
	 * @param quantity Amount of symbol to close position
	 * @param orderType From closeOrderTypesAvailable
	 * @return Indicates whether the sale was successful
	 */
	public boolean closePosition(String userId, String portfolioId, String symbol, double quantity,
			String orderType) throws Exception;
	
	/**
	 * Close all open positions so the portfolio just has cash
	 * @param userId
	 * @param portfolioId Id of the portfolio to close positions for
	 * @return Available cash after closing all positions
	 */
	public double closeAllPositions(String userId, String portfolioId) throws Exception;
}
