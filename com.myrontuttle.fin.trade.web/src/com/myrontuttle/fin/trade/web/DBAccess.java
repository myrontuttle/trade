package com.myrontuttle.fin.trade.web;

import com.myrontuttle.fin.trade.adapt.StrategyDAO;

/**
 * service locator class for data access object
 */
public class DBAccess {

	private static StrategyDAO strategyDAO;

    public void setStrategyDAO(StrategyDAO sdao) {
    	System.out.println("Setting StrategyDAO for DBAccess");
    	DBAccess.strategyDAO = sdao;
    }
	
	public static StrategyDAO getDAO() {
		return strategyDAO;
	}
}
