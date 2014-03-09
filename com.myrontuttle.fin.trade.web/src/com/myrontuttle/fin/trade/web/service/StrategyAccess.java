package com.myrontuttle.fin.trade.web.service;

import com.myrontuttle.fin.trade.api.TradeStrategyService;

/**
 * Service locator for Trade Strategy Service
 */
public class StrategyAccess {
	
	private static TradeStrategyService tradeStrategyService;

	public static TradeStrategyService getTradeStrategyService() {
		return tradeStrategyService;
	}

	public void setTradeStrategyService(TradeStrategyService tradeStrategyService) {
		StrategyAccess.tradeStrategyService = tradeStrategyService;
	}

}
