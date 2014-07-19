package com.myrontuttle.fin.trade.mock;

import java.util.ArrayList;

import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.Transaction;

public class PortfolioMock implements PortfolioService {

	private final static long UID = 1334;
	private final static String PID = "PortfolioID";
	private final static String LID = "LotID";
	private final static double STARTING_CASH = 10000.00;
	private final static String BUY = "Buy";
	private final static String SELL = "Sell";
	private final static String SHORT = "ShortSell";
	private final static String COVER = "BuyToCover";
	private final static String REALIZED_GAIN = "RealizedGain";
	private final static double GAIN = 15.23;
	
	private String[] openOrderTypes = new String[]{ BUY, SHORT };
	private String[] closeOrderTypes = new String[]{ SELL, COVER };
	
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>(2);
	
	@Override
	public String create(long userId, String name) throws Exception {
		return name;
	}

	@Override
	public boolean delete(long userId, String portfolioId) throws Exception {
		return false;
	}

	@Override
	public boolean rename(long userId, String portfolioId, String newName)
			throws Exception {
		return false;
	}

	@Override
	public boolean addCashTransaction(long userId, String portfolioId,
			double quantity, boolean credit, boolean open) throws Exception {
		return false;
	}

	@Override
	public double getAvailableBalance(long userId, String portfolioId)
			throws Exception {
		return STARTING_CASH;
	}

	@Override
	public Transaction getTransaction(long userId, String portfolioId, 
			String transactionId) throws Exception {
		return new TransactionMock(UID, PID, "01", "2014-06-25 09:30", BUY, "AAPL", 20, 134.23);
	}
	
	@Override
	public ArrayList<Transaction> getTransactions(long userId, String portfolioId)
			throws Exception {

		transactions.add(new TransactionMock(UID, PID, "01", "2014-06-25 09:30", BUY, "AAPL", 20, 134.23));
		transactions.add(new TransactionMock(UID, PID, "02", "2014-06-25 14:30", SELL, "AAPL", 20, 140.89));
		return transactions;
	}

	@Override
	public String[] openOrderTypesAvailable(long userId) {
		return openOrderTypes;
	}

	@Override
	public boolean priceRiseGood(String orderType) {
		if (orderType.equals(BUY) || orderType.equals(SELL)) {
			return true;
		}
		return false;
	}


	@Override
	public String openPosition(long userId, String portfolioId, String symbol, double quantity,
			String orderType)
			throws Exception {
		return LID;
	}

	@Override
	public String[] closeOrderTypesAvailable(long userId) {
		return closeOrderTypes;
	}

	@Override
	public boolean closePosition(long userId, String portfolioId, String symbol, double quantity,
			String orderType)
			throws Exception {
		return false;
	}

	@Override
	public double closeAllPositions(long userId, String portfolioId)
			throws Exception {
		return STARTING_CASH + (Math.random() - 0.5) * 100;
	}

	@Override
	public String[] availableAnalysis() throws Exception {
		return new String[]{REALIZED_GAIN};
	}

	@Override
	public double analyze(long userId, String portfolioId, String analysisType)
			throws Exception {
		return GAIN;
	}
}
