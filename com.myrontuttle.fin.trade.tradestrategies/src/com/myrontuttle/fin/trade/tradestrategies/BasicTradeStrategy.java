package com.myrontuttle.fin.trade.tradestrategies;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.TradeStrategy;

/**
 * Provides a trade manager with a basic trade policy
 * @author Myron Tuttle
 */
public class BasicTradeStrategy implements TradeStrategy {
	
	public final static String STARTING_CASH = "startingCash";
	public final static String TRADE_ALLOC = "tradeAllocation";
	public final static String ACCEPTABLE_LOSS = "acceptableLoss";
	public final static String TIME_IN_TRADE = "timeInTrade";
	public final static String ADJUST_AT = "adjustAt";
	public final static String UPPER = "Upper";
	public final static String LOWER = "Lower";

	public final static int NUM_THREADS = 1;
	public final static TimeUnit TIME_IN_TRADE_UNIT = TimeUnit.SECONDS;
	public final static double DEFAULT_STARTING_CASH = 10000.00;
	public final static int DEFAULT_NUMBER_ORDER_TYPES = 1;
	public final static int TRADE_ALLOC_UPPER = 100;
	public final static int TRADE_ALLOC_LOWER = 1;
	public final static int ACCEPT_LOSS_UPPER = 100;
	public final static int ACCEPT_LOSS_LOWER = 1;
	public final static int TIME_IN_TRADE_UPPER = 60*60*24;
	public final static int TIME_IN_TRADE_LOWER = 60;
	public final static int ADJUST_AT_UPPER = 100;
	public final static int ADJUST_AT_LOWER = 1;
	
	private final PortfolioService portfolioService;
	private final QuoteService quoteService;
	private final AlertService alertService;
	private final AlertReceiverService alertReceiver;
	
	private double startingCash;
	private int tradeAllocationUpper;
	private int tradeAllocationLower;
	private int acceptableLossUpper;
	private int acceptableLossLower;
	private int timeInTradeUpper;
	private int timeInTradeLower;
	private int adjustAtUpper;
	private int adjustAtLower;

	private ScheduledExecutorService ses;
	private HashMap<String, Trade> openTrades;
	
	BasicTradeStrategy(PortfolioService portfolioService, QuoteService quoteService, 
									AlertService alertService, AlertReceiverService alertReceiver) {
		this.portfolioService = portfolioService;
		this.quoteService = quoteService;
		this.alertService = alertService;
		this.alertReceiver = alertReceiver;

        this.ses = Executors.newScheduledThreadPool(NUM_THREADS);
        this.openTrades = new HashMap<String, Trade>();
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.fin.trade.api.TradeStrategy#setLimits(java.util.HashMap)
	 */
	@Override
	public void setLimits(HashMap<String, String> mgmtDetails) {
		try {
			startingCash = Double.parseDouble(mgmtDetails.get(STARTING_CASH));
		} catch (NumberFormatException nfe) {
			startingCash = DEFAULT_STARTING_CASH;
		}
		try {
			tradeAllocationUpper = Integer.parseInt(mgmtDetails.get(TRADE_ALLOC + UPPER));
		} catch (NumberFormatException nfe) {
			tradeAllocationUpper = TRADE_ALLOC_UPPER;
		}
		try {
			tradeAllocationLower = Integer.parseInt(mgmtDetails.get(TRADE_ALLOC + LOWER));
		} catch (NumberFormatException nfe) {
			tradeAllocationLower = TRADE_ALLOC_LOWER;
		}
		try {
			acceptableLossUpper = Integer.parseInt(mgmtDetails.get(ACCEPTABLE_LOSS + UPPER));
		} catch (NumberFormatException nfe) {
			acceptableLossUpper = ACCEPT_LOSS_UPPER;
		}
		try {
			acceptableLossLower = Integer.parseInt(mgmtDetails.get(ACCEPTABLE_LOSS + LOWER));
		} catch (NumberFormatException nfe) {
			acceptableLossLower = ACCEPT_LOSS_LOWER;
		}
		try {
			timeInTradeUpper = Integer.parseInt(mgmtDetails.get(TIME_IN_TRADE + UPPER));
		} catch (NumberFormatException nfe) {
			timeInTradeUpper = TIME_IN_TRADE_UPPER;
		}
		try {
			timeInTradeLower = Integer.parseInt(mgmtDetails.get(TIME_IN_TRADE + LOWER));
		} catch (NumberFormatException nfe) {
			timeInTradeLower = TIME_IN_TRADE_LOWER;
		}
		try {
			adjustAtUpper = Integer.parseInt(mgmtDetails.get(ADJUST_AT + UPPER));
		} catch (NumberFormatException nfe) {
			adjustAtUpper = ADJUST_AT_UPPER;
		}
		try {
			adjustAtLower = Integer.parseInt(mgmtDetails.get(ADJUST_AT + LOWER));
		} catch (NumberFormatException nfe) {
			adjustAtLower = ADJUST_AT_LOWER;
		}
	}

	@Override
	public double getStartingCash() {
		return startingCash;
	}

	public int tradeAllocationUpper() {
		return tradeAllocationUpper;
	}

	public int tradeAllocationLower() {
		return tradeAllocationLower;
	}
	
	public int acceptableLossUpper() {
		return acceptableLossUpper;
	}

	public int acceptableLossLower() {
		return acceptableLossLower;
	}

	public int timeInTradeUpper() {
		return timeInTradeUpper;
	}

	public int timeInTradeLower() {
		return timeInTradeLower;
	}

	public int adjustAtUpper() {
		return adjustAtUpper;
	}

	public int adjustAtLower() {
		return adjustAtLower;
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.fin.trade.api.TradeStrategy#takeAction(com.myrontuttle.fin.trade.api.AlertAction)
	 */
	@Override
	public String takeAction(String userId, AlertAction alertAction) throws Exception {
		String actionType = alertAction.getActionType();
		if (actionType.equals(ActionType.TRADE_BOUNDS.toString())) {
			AlertTradeBounds atb = (AlertTradeBounds)alertAction;
			return openTrade(userId, atb.getTradeBounds(), atb.getPortfolioId());
		} else if (actionType.equals(ActionType.TRADE_ADJUSTMENT.toString())) {
			AlertTradeAdjustment ata = (AlertTradeAdjustment)alertAction;
			return adjustTrade(userId, ata.getTradeId());
		} else if (actionType.equals(ActionType.ORDER.toString())) {
			AlertOrder ao = (AlertOrder)alertAction;
			return closeTrade(userId, ao.getOrder(), ao.getPortfolioId());
		}
		return null;
	}

	private String openTrade(String userId, TradeBounds tradeBounds, String portfolioId) throws Exception {
		if (portfolioService.openOrderTypesAvailable(userId).length != 
				portfolioService.closeOrderTypesAvailable(userId).length) {
			throw new Exception("Open and close order types must match.  Trade not made.");
		}
		String openOrderType = portfolioService.openOrderTypesAvailable(userId)[tradeBounds.getOpenOrderType()];
		String closeOrderType = portfolioService.closeOrderTypesAvailable(userId)[tradeBounds.getOpenOrderType()];
		
		try {
			double portfolioBalance = portfolioService.getAvailableBalance(userId, portfolioId);
			double maxTradeAmount = portfolioBalance * (tradeBounds.getTradeAllocation() / 100.0);
			double currentPrice = quoteService.getLast(userId, tradeBounds.getSymbol());

			if (currentPrice <= maxTradeAmount) {
				String tradeId = UUID.randomUUID().toString();
				
				// Open position
				int quantity = (int)Math.floor(maxTradeAmount / currentPrice);
				Order openOrder = new Order(tradeId, openOrderType, tradeBounds.getSymbol(), quantity);
				portfolioService.openPosition(userId, portfolioId, openOrder);

				Order closeOrder = new Order(tradeId, closeOrderType, tradeBounds.getSymbol(), quantity);
				
				// stop loss
				AlertOrder stopLoss = createStopLoss(userId, tradeBounds, currentPrice, closeOrder, tradeId);
				
				// time in trade
				ScheduledFuture<?> timeInTrade = createTimeInTrade(userId, closeOrder, portfolioId, 
																	tradeBounds.getTimeInTrade());
				
				// adjustment at
				AlertTradeAdjustment adjustment = createAdjustment(userId, tradeBounds, currentPrice, 
															tradeId, portfolioId);
				
				openTrades.put(tradeId, new Trade(tradeBounds, portfolioId, openOrder, stopLoss, 
												timeInTrade, adjustment));
				
				return tradeId;
				
			} else {
				throw new Exception("Not enough allocated to trade " + tradeBounds.getSymbol());
			}			
		} catch (Exception e) {
			throw new Exception("Unable to complete trade. " + e.getMessage());
		}
	}

	private String closeTrade(String userId, Order order, String portfolioId) throws Exception {
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

	private String adjustTrade(String userId, String tradeId) throws Exception {
		if (openTrades.containsKey(tradeId)) {

			Trade trade = openTrades.get(tradeId);
			TradeBounds tradeBounds = trade.getTradeBounds();
			String symbol = tradeBounds.getSymbol();
			int quantity = trade.getOpenOrder().getQuantity();
			String closeOrderType = trade.getStopLoss().getOrder().getOrderType();
			Order closeOrder = new Order(tradeId, closeOrderType, symbol, quantity);

			try {
				double currentPrice = quoteService.getLast(userId, symbol);
				
				// stop loss
				AlertOrder stopLoss = createStopLoss(userId, tradeBounds, currentPrice, 
														closeOrder, tradeId);
				trade.setStopLoss(userId, stopLoss, alertService, alertReceiver);
				
				// time in trade
				ScheduledFuture<?> timeInTrade = createTimeInTrade(userId, closeOrder, trade.getPortfolioId(), 
																	tradeBounds.getTimeInTrade());
				trade.setTimeInTrade(timeInTrade);
				
				// adjustment at
				AlertTradeAdjustment adjustment = createAdjustment(userId, tradeBounds, currentPrice, 
															tradeId, trade.getPortfolioId());
				trade.setAdjustment(userId, adjustment, alertService, alertReceiver);
				
				return tradeId;
			} catch (Exception e) {
				throw new Exception("Unable to adjust trade. " + e.getMessage());
			}
		} else {
			throw new Exception("Trade isn't open. Can't make adjustment");
		}
	}
	
	private AlertOrder createStopLoss(String userId, TradeBounds tradeBounds, double currentPrice, 
							Order closeOrder, String portfolioId) throws Exception {

		AvailableAlert priceBelowAlert = alertService.getPriceBelowAlert(userId);
		double loss = currentPrice - (tradeBounds.getAcceptableLoss() / 100) * currentPrice;
		SelectedAlert stopLossAlert = new SelectedAlert(priceBelowAlert.getId(),
														priceBelowAlert.getCondition(),
														tradeBounds.getSymbol(), 
														loss);
		alertService.setupAlerts(userId, stopLossAlert);
		AlertOrder stopLoss = new AlertOrder(stopLossAlert, portfolioId, closeOrder);
		alertReceiver.watchFor(stopLoss);
		
		return stopLoss;
	}
	
	private ScheduledFuture<?> createTimeInTrade(final String userId, final Order closeOrder, 
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
		}, time, TIME_IN_TRADE_UNIT);
	}

	private AlertTradeAdjustment createAdjustment(String userId, TradeBounds tradeBounds, double currentPrice, 
											String tradeId, String portfolioId) throws Exception {
		AvailableAlert priceAboveAlert = alertService.getPriceAboveAlert(userId);
		double adjustmentPrice = currentPrice + (tradeBounds.getAdjustAt() / 100) * currentPrice;
		SelectedAlert adjustmentAlert = new SelectedAlert(priceAboveAlert.getId(),
											priceAboveAlert.getCondition(),
											tradeBounds.getSymbol(),
											adjustmentPrice);
		alertService.setupAlerts(userId, adjustmentAlert);
		AlertTradeAdjustment adjustment = new AlertTradeAdjustment(adjustmentAlert, portfolioId, tradeId);
		alertReceiver.watchFor(adjustment);
		
		return adjustment;
	}
}
