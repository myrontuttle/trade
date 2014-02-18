package com.myrontuttle.fin.trade.strategies;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.myrontuttle.fin.trade.api.ActionType;
import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.AlertOrder;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AlertTrade;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.AvailableStrategyParameter;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.Trade;
import com.myrontuttle.fin.trade.api.TradeStrategy;

/**
 * Provides a trade manager with a bounded trade policy:
 * When a trade is opened a box is drawn to bound the trade.  Think of a time/price chart.
 * The left side of the box is the time the trade opened
 * The right side of the box is the time the trade will close if nothing else happens
 * The bottom and top of the box is when we close the trade
 * 
 * @author Myron Tuttle
 */
public class BoundedStrategy implements TradeStrategy {
	
	public final static String NAME = "Bounded";
	public final static String DESCRIPTION = "Creates bounds around a trade to exit after a certain time or " +
			"percent gain/loss.";

	public final static String OPEN_ORDER = "openOrderType";
	public final static String TRADE_ALLOC = "tradeAllocation";
	public final static String PERCENT_BELOW = "percentBelow";
	public final static String TIME_LIMIT = "timeLimit";
	public final static String PERCENT_ABOVE = "percentAbove";
	public final static String UPPER = "Upper";
	public final static String LOWER = "Lower";

	public final static int NUM_THREADS = 1;
	public final static TimeUnit TIME_LIMIT_UNIT = TimeUnit.SECONDS;
	public final static int OPEN_ORDER_LOWER = 0;
	public final static int OPEN_ORDER_UPPER = 0;
	public final static int TRADE_ALLOC_LOWER = 0;
	public final static int TRADE_ALLOC_UPPER = 100;
	public final static int PERCENT_BELOW_LOWER = 0;
	public final static int PERCENT_BELOW_UPPER = 100;
	public final static int TIME_LIMIT_LOWER = 60;
	public final static int TIME_LIMIT_UPPER = 60*60*24;
	public final static int PERCENT_ABOVE_LOWER = 0;
	public final static int PERCENT_ABOVE_UPPER = 100;
	
	protected final PortfolioService portfolioService;
	protected final QuoteService quoteService;
	protected final AlertService alertService;
	protected final AlertReceiverService alertReceiver;

	private AvailableStrategyParameter openOrderType = new AvailableStrategyParameter(OPEN_ORDER, 
			OPEN_ORDER_LOWER,
			OPEN_ORDER_UPPER);	
	private AvailableStrategyParameter tradeAllocation = new AvailableStrategyParameter(TRADE_ALLOC, 
			TRADE_ALLOC_LOWER,
			TRADE_ALLOC_UPPER);
	private AvailableStrategyParameter percentBelow = new AvailableStrategyParameter(PERCENT_BELOW,
			PERCENT_BELOW_LOWER,
			PERCENT_BELOW_UPPER);
	private AvailableStrategyParameter timeLimit = new AvailableStrategyParameter(TIME_LIMIT,
			TIME_LIMIT_LOWER,
			TIME_LIMIT_UPPER);
	private AvailableStrategyParameter percentAbove = new AvailableStrategyParameter(PERCENT_ABOVE, 
			PERCENT_ABOVE_LOWER,
			PERCENT_ABOVE_UPPER);
	
	private AvailableStrategyParameter[] availableParameters = new AvailableStrategyParameter[]{
			openOrderType,
			tradeAllocation,
			percentBelow,
			timeLimit,
			percentAbove
	};

	private ScheduledExecutorService ses;
	protected HashMap<String, BoundedTrade> openTrades;
	
	public BoundedStrategy(PortfolioService portfolioService, QuoteService quoteService, 
									AlertService alertService, AlertReceiverService alertReceiver) {
		this.portfolioService = portfolioService;
		this.quoteService = quoteService;
		this.alertService = alertService;
		this.alertReceiver = alertReceiver;

        this.ses = Executors.newScheduledThreadPool(NUM_THREADS);
        this.openTrades = new HashMap<String, BoundedTrade>();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public AvailableStrategyParameter[] availableParameters() {
		return availableParameters;
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.fin.trade.api.TradeStrategy#setLimits(java.util.HashMap)
	 */
	@Override
	public void setParameterLimits(HashMap<String, Integer> limits) {
		if (limits == null || limits.values().isEmpty()) {
			System.out.println("No limits set for Bounded With Adjust Strategy. Using defaults");
			return;
		}
		if (limits.containsKey(OPEN_ORDER + LOWER)) {
			openOrderType.setLower(limits.get(OPEN_ORDER + LOWER));
		}
		if (limits.containsKey(OPEN_ORDER + UPPER)) {
			openOrderType.setUpper(limits.get(OPEN_ORDER + UPPER));
		}
		if (limits.containsKey(TRADE_ALLOC + LOWER)) {
			tradeAllocation.setLower(limits.get(TRADE_ALLOC + LOWER));
		}
		if (limits.containsKey(TRADE_ALLOC + UPPER)) {
			tradeAllocation.setUpper(limits.get(TRADE_ALLOC + UPPER));
		}
		if (limits.containsKey(PERCENT_BELOW + LOWER)) {
			percentBelow.setLower(limits.get(PERCENT_BELOW + LOWER));
		}
		if (limits.containsKey(PERCENT_BELOW + UPPER)) {
			percentBelow.setUpper(limits.get(PERCENT_BELOW + UPPER));
		}
		if (limits.containsKey(TIME_LIMIT + LOWER)) {
			timeLimit.setLower(limits.get(TIME_LIMIT + LOWER));
		}
		if (limits.containsKey(TIME_LIMIT + UPPER)) {
			timeLimit.setUpper(limits.get(TIME_LIMIT + UPPER));
		}
		if (limits.containsKey(PERCENT_ABOVE + LOWER)) {
			percentAbove.setLower(limits.get(PERCENT_ABOVE + LOWER));
		}
		if (limits.containsKey(PERCENT_ABOVE + UPPER)) {
			percentAbove.setUpper(limits.get(PERCENT_ABOVE + UPPER));
		}
	}

	@Override
	public void setOrderTypesAvailable(int OrderTypesAvailable) {
		openOrderType.setUpper(OrderTypesAvailable - 1);
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.fin.trade.api.TradeStrategy#takeAction(com.myrontuttle.fin.trade.api.AlertAction)
	 */
	@Override
	public String takeAction(String userId, AlertAction alertAction) throws Exception {
		String actionType = alertAction.getActionType();
		if (actionType.equals(ActionType.TRADE.toString())) {
			AlertTrade at = (AlertTrade)alertAction;
			return openTrade(userId, at.getTrade(), at.getPortfolioId());
		} else if (actionType.equals(ActionType.ORDER.toString())) {
			AlertOrder ao = (AlertOrder)alertAction;
			return closeTrade(userId, ao.getOrder(), ao.getPortfolioId());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.fin.trade.api.TradeStrategy#describeTrade(java.lang.String, com.myrontuttle.fin.trade.api.Trade)
	 */
	@Override
	public String[] describeTrade(String userId, Trade trade) throws Exception {
		Hashtable<String, Integer> parameters = trade.getParameters();
		if (parameters == null || parameters.size() == 0) {
			return new String[0];
		}
		String[] desc = new String[4];
		desc[0] = "If an alert fires for " + trade.getSymbol() + " then " + portfolioService.
				openOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)] + " it with " +
				parameters.get(TRADE_ALLOC) + "% of portfolio.";
		
		desc[1] = "If the price of " + trade.getSymbol() + " falls below " + parameters.get(PERCENT_BELOW) +
				"% then " + portfolioService.closeOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)] + 
				" it.";
		
		desc[2] = "If the price of " + trade.getSymbol() + " rises above " + parameters.get(PERCENT_ABOVE) +
				"% then " + portfolioService.closeOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)] + 
				" it.";
		
		desc[3] = "If the position in " + trade.getSymbol() + " lasts longer than " + 
				parameters.get(TIME_LIMIT) + " " + TIME_LIMIT_UNIT.toString() + " then " + 
				portfolioService.closeOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)] + 
				" it.";
				
		return desc;
	}

	protected String openTrade(String userId, Trade trade, String portfolioId) throws Exception {
		if (portfolioService.openOrderTypesAvailable(userId).length != 
				portfolioService.closeOrderTypesAvailable(userId).length) {
			throw new Exception("Open and close order types must match.  Trade not made.");
		}
		Hashtable<String, Integer> tradeParams = trade.getParameters();
		
		String openOrderType = portfolioService.
									openOrderTypesAvailable(userId)[tradeParams.get(OPEN_ORDER)];
		String closeOrderType = portfolioService.
									closeOrderTypesAvailable(userId)[tradeParams.get(OPEN_ORDER)];
		
		try {
			double portfolioBalance = portfolioService.getAvailableBalance(userId, portfolioId);
			double maxTradeAmount = portfolioBalance * (tradeParams.get(TRADE_ALLOC) / 100.0);
			double currentPrice = quoteService.getLast(userId, trade.getSymbol());

			if (currentPrice <= maxTradeAmount) {
				String tradeId = UUID.randomUUID().toString();
				
				// Open position
				int quantity = (int)Math.floor(maxTradeAmount / currentPrice);
				Order openOrder = new Order(tradeId, openOrderType, trade.getSymbol(), quantity);
				portfolioService.openPosition(userId, portfolioId, openOrder);

				Order closeOrder = new Order(tradeId, closeOrderType, trade.getSymbol(), quantity);
				
				// stop below
				AlertOrder stopBelow = createStopTrade(userId, trade, currentPrice, closeOrder, tradeId, true);
				
				// time in trade
				ScheduledFuture<?> timeInTrade = createTimeLimit(userId, closeOrder, portfolioId, 
						tradeParams.get(TIME_LIMIT));
				
				// stop above
				AlertOrder stopAbove = createStopTrade(userId, trade, currentPrice, closeOrder, tradeId, false);
				
				openTrades.put(tradeId, new BoundedTrade(trade, portfolioId, openOrder, stopBelow, 
												timeInTrade, stopAbove));
				
				return tradeId;
				
			} else {
				throw new Exception("Not enough allocated to trade " + trade.getSymbol() +
						". Current Price(" + currentPrice + ") > Max Allowed Trade Amount (" + 
						maxTradeAmount + ")");
				
			}			
		} catch (Exception e) {
			throw new Exception("Unable to complete trade. " + e.getMessage());
		}
	}

	protected String closeTrade(String userId, Order order, String portfolioId) throws Exception {
		if (openTrades.containsKey(order.getTradeId())) {
			try {
				portfolioService.closePosition(userId, portfolioId, order);
				openTrades.get(order.getTradeId()).closeTrade(userId, alertService, alertReceiver);
				openTrades.remove(order.getTradeId());
				return order.getTradeId();
			} catch (Exception e) {
				throw new Exception("Unable to close position. " + e.getMessage());
			}
		} else {
			throw new Exception("Trade already closed");
		}
	}
	
	protected AlertOrder createStopTrade(String userId, Trade trade, double currentPrice, 
							Order closeOrder, String portfolioId, boolean priceRiseGood) throws Exception {

		AvailableAlert alertWhen = (priceRiseGood) ? alertService.getPriceBelowAlert(userId) :
										alertService.getPriceAboveAlert(userId);
		String alertParam = (priceRiseGood) ? PERCENT_BELOW : PERCENT_ABOVE;
		
		double loss = currentPrice - (trade.getParameters().get(alertParam) / 100) * currentPrice;
		SelectedAlert stopLossAlert = new SelectedAlert(alertWhen.getId(),
														alertWhen.getCondition(),
														trade.getSymbol(), 
														loss);
		alertService.setupAlerts(userId, stopLossAlert);
		AlertOrder stopLoss = new AlertOrder(stopLossAlert, portfolioId, closeOrder);
		alertReceiver.watchFor(stopLoss);
		
		return stopLoss;
	}
	
	protected ScheduledFuture<?> createTimeLimit(final String userId, final Order closeOrder, 
													final String portfolioId, 
													int time) {
		return ses.schedule(new Runnable () {
			@Override
			public void run() {
				try {
					closeTrade(userId, closeOrder, portfolioId);
				} catch (Exception e) {
					System.out.println("Unable to close trade." + e.getMessage());
				}
			}
		}, time, TIME_LIMIT_UNIT);
	}
}
