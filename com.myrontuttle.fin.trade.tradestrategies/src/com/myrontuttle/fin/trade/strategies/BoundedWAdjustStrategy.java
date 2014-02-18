package com.myrontuttle.fin.trade.strategies;

import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import com.myrontuttle.fin.trade.api.ActionType;
import com.myrontuttle.fin.trade.api.AlertAction;
import com.myrontuttle.fin.trade.api.AlertOrder;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AlertTrade;
import com.myrontuttle.fin.trade.api.AlertTradeAdjustment;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.Trade;
import com.myrontuttle.fin.trade.api.TradeStrategy;

/**
 * Provides a trade manager with a basic trade policy:
 * When a trade is opened a box is drawn to bound the trade.  Think of a time/price chart.
 * The left side of the box is the time the trade opened
 * The bottom (top when shorting) of the box is the acceptable loss
 * The right side of the box is the time the trade will close if nothing else happens
 * The top (bottom when shorting) of the box is the point when the box is adjusted like a trailing stop loss
 * 
 * @author Myron Tuttle
 */
public class BoundedWAdjustStrategy extends BoundedStrategy implements TradeStrategy {
	
	public final static String NAME = "Bounded With Adjustment";
	public final static String DESCRIPTION = "Creates bounds around a trade to exit after a certain time or " +
			"percent loss. Adjusts the bounds if a gain is reached.";

	public BoundedWAdjustStrategy(PortfolioService portfolioService, QuoteService quoteService, 
									AlertService alertService, AlertReceiverService alertReceiver) {
		super(portfolioService, quoteService, alertService, alertReceiver);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
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
		} else if (actionType.equals(ActionType.TRADE_ADJUSTMENT.toString())) {
			AlertTradeAdjustment ata = (AlertTradeAdjustment)alertAction;
			return adjustTrade(userId, ata.getTradeId());
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

		String openOrderType = portfolioService.
									openOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)];
		String closeOrderType = portfolioService.
									closeOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)];
		
		String[] desc = new String[4];
		desc[0] = "If an alert fires for " + trade.getSymbol() + " then " + portfolioService.
				openOrderTypesAvailable(userId)[parameters.get(OPEN_ORDER)] + " it with " +
				parameters.get(TRADE_ALLOC) + "% of portfolio.";

		if (portfolioService.priceRiseGood(openOrderType)) {

			desc[1] = "If the price of " + trade.getSymbol() + " falls below " + parameters.get(PERCENT_BELOW) +
					"% then " + closeOrderType + " it.";

			desc[2] = "If the price of " + trade.getSymbol() + " rises above " + parameters.get(PERCENT_ABOVE) +
					"% then adjust these instructions.";
		} else {

			desc[1] = "If the price of " + trade.getSymbol() + " rises above " + parameters.get(PERCENT_ABOVE) +
					"% then " + closeOrderType + " it.";

			desc[2] = "If the price of " + trade.getSymbol() + " falls below " + parameters.get(PERCENT_BELOW) +
					"% then adjust these instructions.";
		}
		
		desc[3] = "If the position in " + trade.getSymbol() + " lasts longer than " + 
				parameters.get(TIME_LIMIT) + " " + TIME_LIMIT_UNIT.toString() + " then " + 
				closeOrderType + " it.";
				
		return desc;
	}

	@Override
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
		
		boolean priceRiseGood = portfolioService.priceRiseGood(openOrderType);
		
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
				
				// stop loss
				AlertOrder stopLoss = createStopTrade(userId, trade, currentPrice, closeOrder, tradeId, priceRiseGood);
				
				// time in trade
				ScheduledFuture<?> timeInTrade = createTimeLimit(userId, closeOrder, portfolioId, 
						tradeParams.get(TIME_LIMIT));
				
				// adjustment at
				AlertTradeAdjustment adjustment = createAdjustment(userId, trade, currentPrice, 
															tradeId, portfolioId, priceRiseGood);
				
				openTrades.put(tradeId, new BoundedTrade(trade, portfolioId, openOrder, stopLoss, 
												timeInTrade, adjustment));
				
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

	private String adjustTrade(String userId, String tradeId) throws Exception {
		if (openTrades.containsKey(tradeId)) {

			BoundedTrade boundedTrade = openTrades.get(tradeId);
			Trade trade = boundedTrade.getTrade();
			String symbol = trade.getSymbol();
			int quantity = boundedTrade.getOpenOrder().getQuantity();
			
			Hashtable<String, Integer> tradeParams = trade.getParameters();
			String closeOrderType = portfolioService.
					closeOrderTypesAvailable(userId)[tradeParams.get(OPEN_ORDER)];
			boolean priceRiseGood = portfolioService.priceRiseGood(closeOrderType);
			
			Order closeOrder = new Order(tradeId, closeOrderType, symbol, quantity);

			try {
				double currentPrice = quoteService.getLast(userId, symbol);
				
				// stop loss
				AlertOrder stopLoss = createStopTrade(userId, trade, currentPrice, 
														closeOrder, tradeId, priceRiseGood);
				boundedTrade.setOutOfTheMoney(userId, stopLoss, alertService, alertReceiver);
				
				// time in trade
				ScheduledFuture<?> timeLimit = createTimeLimit(userId, closeOrder, boundedTrade.getPortfolioId(), 
																trade.getParameters().get(TIME_LIMIT));
				boundedTrade.setTimeLimit(timeLimit);
				
				// adjustment at
				AlertTradeAdjustment adjustment = createAdjustment(userId, trade, currentPrice, 
															tradeId, boundedTrade.getPortfolioId(), priceRiseGood);
				boundedTrade.setInTheMoney(userId, adjustment, alertService, alertReceiver);
				
				return tradeId;
			} catch (Exception e) {
				throw new Exception("Unable to adjust trade. " + e.getMessage());
			}
		} else {
			throw new Exception("Trade isn't open. Can't make adjustment");
		}
	}

	private AlertTradeAdjustment createAdjustment(String userId, Trade trade, double currentPrice, 
											String tradeId, String portfolioId, boolean priceRiseGood) 
													throws Exception {

		AvailableAlert alertWhen = (priceRiseGood) ? alertService.getPriceAboveAlert(userId) :
										alertService.getPriceBelowAlert(userId);
		String alertType = (priceRiseGood) ? PERCENT_BELOW : PERCENT_ABOVE;
		
		double adjustmentPrice = currentPrice + 
					(trade.getParameters().get(alertType) / 100) * currentPrice;
		SelectedAlert adjustmentAlert = new SelectedAlert(alertWhen.getId(),
											alertWhen.getCondition(),
											trade.getSymbol(),
											adjustmentPrice);
		alertService.setupAlerts(userId, adjustmentAlert);
		AlertTradeAdjustment adjustment = new AlertTradeAdjustment(adjustmentAlert, portfolioId, tradeId);
		alertReceiver.watchFor(adjustment);
		
		return adjustment;
	}
}
