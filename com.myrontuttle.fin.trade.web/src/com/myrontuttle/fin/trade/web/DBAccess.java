package com.myrontuttle.fin.trade.web;

import com.myrontuttle.fin.trade.adapt.StrategyDAO;

/**
 * service locator class for data access object
 */
public class DBAccess {

	private static StrategyDAO strategyDAO;
	
	public static StrategyDAO getDAO() {
		return strategyDAO;
	}

    public void setStrategyDAO(StrategyDAO sdao) {
    	DBAccess.strategyDAO = sdao;
    }
}
