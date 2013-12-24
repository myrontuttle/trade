package com.myrontuttle.fin.trade.strategies;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.Order;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.QuoteService;
import com.myrontuttle.fin.trade.api.SelectedAlert;

public class BasicTradeStrategyTest {

	private PortfolioService portfolioService;
	private QuoteService quoteService;
	private AlertService alertService;
	private AlertReceiverService alertReceiverService;
	
	private BasicTradeStrategy bts;
	
	private final String userId = "testuser";
	private final String richPortfolio = "rich";
	private final String poorPortfolio = "poor";
	private final String avgSymbol = "MSFT.O";
	private final String expensiveSymbol = "BRK.A";

	private final double richBalance = 5000000.00;
	private final double poorBalance = 10.00;
	private final double avgPrice = 33.03;
	private final double expensivePrice = 167380.00;
	
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
		when(portfolioService.closePosition(userId, richPortfolio, any(Order.class))).thenReturn(true);
		
		bts = new BasicTradeStrategy(portfolioService, quoteService, 
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
		TradeBounds tradeMsft = new TradeBounds(avgSymbol, 0, 10, 10, 3600, 30);
		SelectedAlert openAlert = new SelectedAlert(1, "Price went up", avgSymbol, null);
		AlertTradeBounds atb = new AlertTradeBounds(openAlert, richPortfolio, tradeMsft);
				
		// Test
		String openTradeId = bts.takeAction(userId, atb);
		assertTrue(openTradeId != null);

		SelectedAlert adjustAlert = new SelectedAlert(1, "Price went up again", avgSymbol, null);
		AlertTradeAdjustment ata = new AlertTradeAdjustment(adjustAlert, richPortfolio, openTradeId);
		
		String adjustTradeId = bts.takeAction(userId, ata);
		assertEquals(openTradeId, adjustTradeId);
		
		SelectedAlert closeAlert = new SelectedAlert(0, "Price went down", avgSymbol, null);
		Order closeOrder = new Order(openTradeId, "sell", avgSymbol, 10);
		AlertOrder ao = new AlertOrder(closeAlert, richPortfolio, closeOrder);
		
		assertEquals(bts.takeAction(userId, ao), openTradeId);
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
		TradeBounds tradeBrk = new TradeBounds(expensiveSymbol, 0, 10, 10, 3600, 30);
		SelectedAlert openAlert = new SelectedAlert(1, "Price went up", avgSymbol, null);
		AlertTradeBounds atb = new AlertTradeBounds(openAlert, poorPortfolio, tradeBrk);

		// Test
		exception.expect(Exception.class);
		String openTradeId = bts.takeAction(userId, atb);
		assertTrue(openTradeId == null);
	}

}
