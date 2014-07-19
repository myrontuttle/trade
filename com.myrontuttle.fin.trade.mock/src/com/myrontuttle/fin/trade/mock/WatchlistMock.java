package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.WatchlistService;

public class WatchlistMock implements WatchlistService {

	private final static String LID = "LotID";
	private final String[] HOLDINGS = new String[]{"AAPL", "MSFT"};
	
	@Override
	public String create(long userId, String name) throws Exception {
		return name;
	}

	@Override
	public boolean delete(long userId, String watchlistId) throws Exception {
		return false;
	}

	@Override
	public boolean rename(long userId, String watchlistId, String newName)
			throws Exception {
		return false;
	}

	@Override
	public String addHolding(long userId, String watchlistId, String symbol)
			throws Exception {
		return LID;
	}

	@Override
	public String[] retrieveHoldings(long userId, String watchlistId)
			throws Exception {
		return HOLDINGS;
	}

	@Override
	public boolean removeHolding(long userId, String watchlistId,
			String symbol) throws Exception {
		return false;
	}

}
