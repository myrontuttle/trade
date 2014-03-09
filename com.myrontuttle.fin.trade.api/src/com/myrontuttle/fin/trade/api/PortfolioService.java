package com.myrontuttle.fin.trade.api;

/**
 * Service for creating portfolios of issue holdings 
 * @author Myron Tuttle
 */
public interface PortfolioService extends Service {

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
	 * @return Transaction/Lot id
	 */
	public String openPosition(String userId, String portfolioId, Order order) throws Exception;

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
	 * @return Indicates whether the sale was successful
	 */
	public boolean closePosition(String userId, String portfolioId, Order order) throws Exception;
	
	/**
	 * Close all open positions so the portfolio just has cash
	 * @param userId
	 * @param portfolioId Id of the portfolio to close positions for
	 * @return Available cash after closing all positions
	 */
	public double closeAllPositions(String userId, String portfolioId) throws Exception;
}
