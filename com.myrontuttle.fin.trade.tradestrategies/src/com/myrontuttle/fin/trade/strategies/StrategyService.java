package com.myrontuttle.fin.trade.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myrontuttle.fin.trade.api.Service;
import com.myrontuttle.fin.trade.api.TradeStrategy;
import com.myrontuttle.fin.trade.api.TradeStrategyService;

public class StrategyService implements TradeStrategyService {
	
	private Map<String, Class<? extends TradeStrategy>> strategies;
	
	// 1. Add the name of the strategies
	public static String[] availableTradeStrategies = new String[]{
		BoundedStrategy.NAME,
		BoundedWAdjustStrategy.NAME
	};

	public StrategyService() {
		strategies = new HashMap<String, Class<? extends TradeStrategy>>();

		// 2. Define strategies
		strategies.put(BoundedStrategy.NAME, BoundedStrategy.class);
		strategies.put(BoundedWAdjustStrategy.NAME, BoundedWAdjustStrategy.class);
	}

	@Override
	public String[] availableTradeStrategies() {
		return availableTradeStrategies;
	}

	@Override
	public TradeStrategy getTradeStrategy(String strategyName,
			List<Service> services) throws Exception {
		
		if (strategies.containsKey(strategyName)) {
			TradeStrategy ts = strategies.get(strategyName).newInstance();
			ts.setup(services);
			return ts;
		} else {
			throw new Exception("No existing strategy with name: " + strategyName);
		}
	}

}
