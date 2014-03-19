package com.myrontuttle.fin.trade.strategies;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import com.myrontuttle.fin.trade.api.AlertOrder;
import com.myrontuttle.fin.trade.api.AlertReceiver;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AlertTrade;
import com.myrontuttle.fin.trade.api.AlertTradeAdjustment;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.Trade;

public class BoundedWAdjustStrategyTest {

	private PortfolioService portfolioService;
	private QuoteService quoteService;
	private AlertService alertService;
	private AlertReceiver alertReceiver;
	
	private BoundedWAdjustStrategy bwas;
	
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
		alertReceiver = mock(AlertReceiver.class);

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
		
		bwas = new BoundedWAdjustStrategy(portfolioService, quoteService, 
										alertService, alertReceiver);
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
		Hashtable<String, Integer> params = new Hashtable<String, Integer>(5);
		params.put(BoundedWAdjustStrategy.OPEN_ORDER, 0);
		params.put(BoundedWAdjustStrategy.TRADE_ALLOC, 10);
		params.put(BoundedWAdjustStrategy.PERCENT_BELOW, 10);
		params.put(BoundedWAdjustStrategy.TIME_LIMIT, 3600);
		params.put(BoundedWAdjustStrategy.PERCENT_ABOVE, 30);
		
		Trade tradeMsft = new Trade(avgSymbol, params);
		SelectedAlert openAlert = new SelectedAlert(1, "Price went up", avgSymbol, null);
		AlertTrade atb = new AlertTrade(openAlert, userId, richPortfolio, tradeMsft);
				
		// Test
		String openTradeId = bwas.takeAction(atb);
		assertTrue(openTradeId != null);

		SelectedAlert adjustAlert = new SelectedAlert(1, "Price went up again", avgSymbol, null);
		AlertTradeAdjustment ata = new AlertTradeAdjustment(adjustAlert, userId, richPortfolio, openTradeId);
		
		String adjustTradeId = bwas.takeAction(ata);
		assertEquals(openTradeId, adjustTradeId);
		
		SelectedAlert closeAlert = new SelectedAlert(0, "Price went down", avgSymbol, null);
		Order closeOrder = new Order(openTradeId, "sell", avgSymbol, 10);
		AlertOrder ao = new AlertOrder(closeAlert, userId, richPortfolio, closeOrder);
		
		assertEquals(bwas.takeAction(ao), openTradeId);
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
		Hashtable<String, Integer> params = new Hashtable<String, Integer>(5);
		params.put(BoundedWAdjustStrategy.OPEN_ORDER, 0);
		params.put(BoundedWAdjustStrategy.TRADE_ALLOC, 10);
		params.put(BoundedWAdjustStrategy.PERCENT_BELOW, 10);
		params.put(BoundedWAdjustStrategy.TIME_LIMIT, 3600);
		params.put(BoundedWAdjustStrategy.PERCENT_ABOVE, 30);

		Trade tradeBrk = new Trade(expensiveSymbol, params);
		SelectedAlert openAlert = new SelectedAlert(1, "Price went up", avgSymbol, null);
		AlertTrade atb = new AlertTrade(openAlert, userId, poorPortfolio, tradeBrk);

		// Test
		exception.expect(Exception.class);
		String openTradeId = bwas.takeAction(atb);
		assertTrue(openTradeId == null);
	}

}
