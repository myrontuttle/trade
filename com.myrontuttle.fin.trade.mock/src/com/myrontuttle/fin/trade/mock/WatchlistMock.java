package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.WatchlistService;

public class WatchlistMock implements WatchlistService {

	private final static String LID = "LotID";
	private final String[] HOLDINGS = new String[]{"AAPL", "MSFT"};
	
	@Override
	public String create(String userId, String name) throws Exception {
		return name;
	}

	@Override
	public boolean delete(String userId, String watchlistId) throws Exception {
		return false;
	}

	@Override
	public boolean rename(String userId, String watchlistId, String newName)
			throws Exception {
		return false;
	}

	@Override
	public String addHolding(String userId, String watchlistId, String symbol)
			throws Exception {
		return LID;
	}

	@Override
	public String[] retrieveHoldings(String userId, String watchlistId)
			throws Exception {
		return HOLDINGS;
	}

	@Override
	public boolean removeHolding(String userId, String watchlistId,
			String symbol) throws Exception {
		return false;
	}

}
