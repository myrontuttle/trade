package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;

public class PortfolioMock implements PortfolioService {

	@Override
	public String create(String userId, String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String userId, String portfolioId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rename(String userId, String portfolioId, String newName)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addCashTransaction(String userId, String portfolioId,
			double quantity, boolean credit, boolean open) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getAvailableBalance(String userId, String portfolioId)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] openOrderTypesAvailable(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String openPosition(String userId, String portfolioId, Order order)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] closeOrderTypesAvailable(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean closePosition(String userId, String portfolioId, Order order)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double closeAllPositions(String userId, String portfolioId)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
