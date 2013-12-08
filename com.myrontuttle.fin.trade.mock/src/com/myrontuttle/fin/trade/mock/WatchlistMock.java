package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.WatchlistService;

public class WatchlistMock implements WatchlistService {

	@Override
	public String create(String userId, String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String userId, String watchlistId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rename(String userId, String watchlistId, String newName)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String addHolding(String userId, String watchlistId, String symbol)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] retrieveHoldings(String userId, String watchlistId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeHolding(String userId, String watchlistId,
			String symbol) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
