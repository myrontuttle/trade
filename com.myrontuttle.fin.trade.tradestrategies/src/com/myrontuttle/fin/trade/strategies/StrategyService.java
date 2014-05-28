package com.myrontuttle.fin.trade.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableStrategyParameter;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.TradeStrategyService;

public class StrategyService implements TradeStrategyService {

	private final static int NUM_THREADS = 2;
	private final static int INITIAL_DELAY = 10;	// Seconds
	
	public final static String MOMENT_PASSED = "momentPassed";
	
	private static TradeDAO tradeDAO;

	private static PortfolioService portfolioService;
	private static QuoteService quoteService;
	private static AlertService alertService;

	private final ScheduledExecutorService ses;
	private final HashMap<String, ScheduledFuture<?>> eventFutures = new HashMap<String, ScheduledFuture<?>>();
	
	// 1. Add the name of the strategies
	public static String[] availableTradeStrategies = new String[]{
		BoundedStrategy.NAME,
		BoundedWAdjustStrategy.NAME
	};

	public StrategyService() {
        this.ses = Executors.newScheduledThreadPool(NUM_THREADS);
	}
	
	public TradeDAO getTradeDAO() {
		return tradeDAO;
	}

	public void setTradeDAO(TradeDAO tradeDAO) {
		StrategyService.tradeDAO = tradeDAO;
	}
	
	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		StrategyService.portfolioService = portfolioService;
	}
	
	public static QuoteService getQuoteService() {
		return quoteService;
	}

	public void setQuoteService(QuoteService quoteService) {
		StrategyService.quoteService = quoteService;
	}
	
	public static AlertService getAlertService() {
		return alertService;
	}

	public void setAlertService(AlertService alertService) {
		StrategyService.alertService = alertService;
	}
	
	public void init() {
		
		ses.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					List<Event> events = tradeDAO.findEventsWithTrigger(MOMENT_PASSED);
					if (events != null) {
						System.out.println("Starting strategy event tracking");
						for(Event event : events) {
							scheduleEvent(event);
						}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}, INITIAL_DELAY, TimeUnit.SECONDS);
	}

	@Override
	public String[] availableTradeStrategies() {
		return availableTradeStrategies;
	}

	@Override
	public String addTrade(String tradeStrategy, String userId,
			String portfolioId, String symbol) {
		Trade t = new Trade(tradeStrategy, userId, portfolioId, symbol);
		tradeDAO.saveTrade(t);
		return t.getTradeId();
	}

	@Override
	public boolean tradeExists(String tradeId) {
		return tradeDAO.tradeExists(tradeId);
	}

	@Override
	public String[] describeTrade(String tradeId) {
		Trade t = tradeDAO.findTrade(tradeId);
		try {
			if (t.getTradeStrategy().equals(BoundedStrategy.NAME)) {
				return BoundedStrategy.describeTrade(t, portfolioService);
			} else if (t.getTradeStrategy().equals(BoundedWAdjustStrategy.NAME)) {
				return BoundedWAdjustStrategy.describeTrade(t, portfolioService);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void removeTrade(String tradeId) {
		tradeDAO.removeTrade(tradeId);
	}

	@Override
	public void removeAllTrades(String userId) {
		List<Trade> trades = tradeDAO.findTrades(userId);
		for (Trade t : trades) {
			tradeDAO.removeTrade(t.getTradeId());
		}
	}

	@Override
	public AvailableStrategyParameter[] availableTradeParameters(
			String tradeStrategy) {
		if (tradeStrategy.equals(BoundedStrategy.NAME)) {
			return BoundedStrategy.availableParameters();
		} else if (tradeStrategy.equals(BoundedWAdjustStrategy.NAME)) {
			return BoundedWAdjustStrategy.availableParameters();
		}

		return null;
	}

	@Override
	public void setTradeParameter(String tradeId, String name, int value) {
		Trade t = tradeDAO.findTrade(tradeId);
		t.addParameter(name, value);
		tradeDAO.updateTrade(t);
	}

	@Override
	public int getTradeParameter(String tradeId, String name) {
		Trade t = tradeDAO.findTrade(tradeId);
		return t.getParameter(name);
	}

	@Override
	public String tradeActionToStart(String tradeStrategy) {
		if (tradeStrategy.equals(BoundedStrategy.NAME)) {
			return BoundedStrategy.OPEN;
		} else if (tradeStrategy.equals(BoundedWAdjustStrategy.NAME)) {
			return BoundedWAdjustStrategy.OPEN;
		}
		
		return null;
	}

	@Override
	public void setTradeEvent(String tradeId, String event, String actionType, String trigger) {
		Trade t = tradeDAO.findTrade(tradeId);
		Event e = new Event(event, actionType, trigger);
		t.addEvent(e);
		tradeDAO.updateTrade(t);
		
		if (trigger.equals(MOMENT_PASSED)) {
			scheduleEvent(e);
		}
	}
	
	private void scheduleEvent(final Event event) {
		
		int delay = Seconds.secondsBetween(new DateTime(), new DateTime(event.getTrigger())).getSeconds();
		if (delay > 0) {
			eventFutures.put(event.getEvent(), ses.schedule(new Runnable() {
				@Override
				public void run() {
					eventOccurred(event.getEvent());
				}
			}, delay, TimeUnit.SECONDS));
		}
	}

	@Override
	public void removeAllTradeEvents(String tradeId) {
		Trade t = tradeDAO.findTrade(tradeId);
		for(Event event : t.getEvents()) {
			t.removeEvent(event);
			if (event.getTrigger().equals(MOMENT_PASSED) && eventFutures.containsKey(event.getEvent())) {
				eventFutures.get(event.getEvent()).cancel(true);
				eventFutures.remove(event.getEvent());
			}
		}
		tradeDAO.updateTrade(t);
	}

	@Override
	public void eventOccurred(String event) {
		List<Event> events = tradeDAO.findEvents(event);
		for (Event e : events) {
			Trade trade = tradeDAO.findTrade(e.getTradeId());
			String strategy = trade.getTradeStrategy();
			try {
				if (strategy.equals(BoundedStrategy.NAME)) {
					BoundedStrategy.takeAction(e, 
							trade, 
							portfolioService, 
							quoteService, 
							alertService,
							this);
				} else if (strategy.equals(BoundedWAdjustStrategy.NAME)) {
					BoundedWAdjustStrategy.takeAction(e, 
							trade, 
							portfolioService, 
							quoteService, 
							alertService,
							this);
				}
				if (e.getTrigger().equals(MOMENT_PASSED)) {
					eventFutures.get(e.getEvent()).cancel(true);
					eventFutures.remove(e.getEvent());
				}
				trade.removeEvent(e);
				tradeDAO.updateTrade(trade);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}		
	}
	
}
