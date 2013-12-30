package com.myrontuttle.fin.trade.strategies;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.Trade;
import com.myrontuttle.fin.trade.api.TradeParameter;

public class BoundedStrategyTest {

	private PortfolioService portfolioService;
	private QuoteService quoteService;
	private AlertService alertService;
	private AlertReceiverService alertReceiverService;
	
	private BoundedStrategy bs;
	
	private final String userId = "testuser";
	private final String richPortfolio = "rich";
	private final String poorPortfolio = "poor";
	private final String avgSymbol = "MSFT.O";
	private final String expensiveSymbol = "BRK.A";

	private final double richBalance = 5000000.00;
	private final double poorBalance = 10.00;
	private final double avgPrice = 33.03;
	private final double expensivePrice = 167380.00;

	private final int belowId = 1;
	private final String belowCondition = "{symbol}'s price fell below {Price}";
	private AvailableAlert priceBelowAlert = new AvailableAlert(belowId, belowCondition, 
			new String[]{"DOUBLE"}, 
			new String[]{"Price"}, new String[]{"Fund.Price.Low.Lifetime"}, 
			new String[]{"Quote.Value.Last"}, null);

	private final int aboveId = 2;
	private final String aboveCondition = "{symbol}'s price rose above {Price}";
	private AvailableAlert priceAboveAlert = new AvailableAlert(aboveId, aboveCondition, 
			new String[]{"DOUBLE"}, 
			new String[]{"Price"}, new String[]{"Fund.Price.High.Lifetime"}, 
			new String[]{"Quote.Value.Last"}, null);
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		portfolioService = mock(PortfolioService.class);
		quoteService = mock(QuoteService.class);
		alertService = mock(AlertService.class);
		alertReceiverService = mock(AlertReceiverService.class);

	    // Arrange mocks
		when(portfolioService.openOrderTypesAvailable(userId)).thenReturn(new String[]{"buy", "short sell"});
		when(portfolioService.closeOrderTypesAvailable(userId)).thenReturn(new String[]{"sell", "buy to cover"});
		when(portfolioService.getAvailableBalance(userId, richPortfolio)).thenReturn(richBalance);
		when(quoteService.getLast(userId, avgSymbol)).thenReturn(avgPrice);
		when(portfolioService.getAvailableBalance(userId, poorPortfolio)).thenReturn(poorBalance);
		when(quoteService.getLast(userId, expensiveSymbol)).thenReturn(expensivePrice);
		when(portfolioService.closePosition(eq(userId), eq(richPortfolio), any(Order.class))).thenReturn(true);
		
		when(alertService.getPriceBelowAlert(userId)).thenReturn(priceBelowAlert);
		when(alertService.getPriceAboveAlert(userId)).thenReturn(priceAboveAlert);
		
		bs = new BoundedStrategy(portfolioService, quoteService, 
										alertService, alertReceiverService);
	}

	@Test
	public void testHappyTrade() throws Exception {
		
		/* Initialize
		 * OpenOrderType = 0 = "buy"/"sell"
		 * TradeAllocation = 10% of current balance
		 * AcceptableLoss = 10% below current price
		 * TimeInTrade = 60*60 = 3600 seconds = 1 hour
		 * AdjustAt = 30% of current symbol price
		 */
		TradeParameter[] params = new TradeParameter[]{
				new TradeParameter(BoundedWAdjustStrategy.OPEN_ORDER, 0),
				new TradeParameter(BoundedWAdjustStrategy.TRADE_ALLOC, 10),
				new TradeParameter(BoundedWAdjustStrategy.PERCENT_BELOW, 10),
				new TradeParameter(BoundedWAdjustStrategy.TIME_LIMIT, 3600),
				new TradeParameter(BoundedWAdjustStrategy.PERCENT_ABOVE, 30)
		};
		Trade tradeMsft = new Trade(avgSymbol, params);
		SelectedAlert openAlert = new SelectedAlert(1, "Price went up", avgSymbol, null);
		AlertTrade at = new AlertTrade(openAlert, richPortfolio, tradeMsft);
				
		// Test
		String openTradeId = bs.takeAction(userId, at);
		assertTrue(openTradeId != null);
		
		SelectedAlert closeAlert = new SelectedAlert(0, "Price went down", avgSymbol, null);
		Order closeOrder = new Order(openTradeId, "sell", avgSymbol, 10);
		AlertOrder ao = new AlertOrder(closeAlert, richPortfolio, closeOrder);
		
		assertEquals(bs.takeAction(userId, ao), openTradeId);
	}

	@Test
	public void testSadTrade() throws Exception {

		/* Initialize
		 * OpenOrderType = 0 = "buy"/"sell"
		 * TradeAllocation = 10% of current balance
		 * AcceptableLoss = 10% below current price
		 * TimeInTrade = 60*60 = 3600 seconds = 1 hour
		 * AdjustAt = 30% of current symbol price
		 */
		TradeParameter[] params = new TradeParameter[]{
				new TradeParameter(BoundedWAdjustStrategy.OPEN_ORDER, 0),
				new TradeParameter(BoundedWAdjustStrategy.TRADE_ALLOC, 10),
				new TradeParameter(BoundedWAdjustStrategy.PERCENT_BELOW, 10),
				new TradeParameter(BoundedWAdjustStrategy.TIME_LIMIT, 3600),
				new TradeParameter(BoundedWAdjustStrategy.PERCENT_ABOVE, 30)
		};
		Trade tradeBrk = new Trade(expensiveSymbol, params);
		SelectedAlert openAlert = new SelectedAlert(1, "Price went up", avgSymbol, null);
		AlertTrade atb = new AlertTrade(openAlert, poorPortfolio, tradeBrk);

		// Test
		exception.expect(Exception.class);
		String openTradeId = bs.takeAction(userId, atb);
		assertTrue(openTradeId == null);
	}

}
