package com.myrontuttle.fin.trade.strategies;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.TradeStrategy;
import com.myrontuttle.fin.trade.api.TradeStrategyService;

public class StrategyService implements TradeStrategyService {
	
	// 1. Define strategies
	private BoundedStrategy boundedStrategy;
	private BoundedWAdjustStrategy boundedWAdjustStrategy;
	
	// 2. Add the name of the strategies
	public static String[] availableTradeStrategies = new String[]{
		BoundedStrategy.NAME,
		BoundedWAdjustStrategy.NAME
	};

	// 3. Add required arguments/services to the constructor and construct the strategies
	// 4. Make sure to add any service references/arguments to the blueprint config
	public StrategyService(PortfolioService portfolioService, QuoteService quoteService, 
									AlertService alertService, AlertReceiverService alertReceiver) {
		
		this.boundedStrategy = new BoundedStrategy(portfolioService, quoteService, alertService, alertReceiver);
		this.boundedWAdjustStrategy = new BoundedWAdjustStrategy(portfolioService, quoteService, alertService, 
																alertReceiver);
	}

	@Override
	public String[] availableTradeStrategies() {
		return availableTradeStrategies;
	}

	// 5. Return the appropriate strategy based on the name given
	@Override
	public TradeStrategy getTradeStrategy(String strategyName) {
		if (strategyName.equals(BoundedStrategy.NAME)) {
			return boundedStrategy;
		}
		if (strategyName.equals(BoundedWAdjustStrategy.NAME)) {
			return boundedWAdjustStrategy;
		}
		return null;
	}

}
