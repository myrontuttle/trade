package com.myrontuttle.fin.trade.adapt.express;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.StrategyDAO;
import com.myrontuttle.fin.trade.adapt.express.BasicExpression;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.AvailableScreenCriteria;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.ScreenerService;
import com.myrontuttle.fin.trade.api.SelectedAlert;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.api.WatchlistService;
import com.myrontuttle.fin.trade.tradestrategies.BasicTradeStrategy;
import com.myrontuttle.fin.trade.tradestrategies.TradeBounds;

public class BasicExpressionTest {

	private final static String CID = "candidateID";
	private final static String WID = "watchlistID";
	private final static String PID = "portfolioID";
	private final static String GID = "groupID";
	private final static String EMAIL = "test@test.com";
	private final double STARTING_CASH = 10000.00;

	private static final int SCREEN_GENES = 3;
	private static final int MAX_SYMBOLS_PER_SCREEN = 5;
	private static final int ALERTS_PER_SYMBOL = 1;
	private static final int GENE_UPPER_VALUE = 100;
	
	private ScreenerService screenerService;
	private WatchlistService watchlistService;
	private AlertService alertService;
	private PortfolioService portfolioService;
	private BasicTradeStrategy basicTradeStrategy;
	private AlertReceiverService alertReceiver;

	private StrategyDAO strategyDAO;
	
	private BasicExpression<int[]> expression;
	
	private Candidate candidateA;
	private Group group1;
	private int[] genomeA = new int[]{1	// Sort by first screener gene
									,55,20,20	// 1st screener gene
									,25,20,20	// 2nd screener gene (inactive)
									,100,75,50	// 3rd screener gene
									,19,50,0,0  // 1st alert gene
									,0,0,0,0	// 2nd alert gene
									,0,0,0,0	// 3rd alert gene
									,0,0,0,0	// 4th alert gene
									,0,0,0,0	// 5th alert gene
									,0,0,0,0	// 6th alert gene
									,0,0,0,0	// 7th alert gene
									,0,0,0,0	// 8th alert gene
									,0,0,0,0	// 9th alert gene
									,0,0,0,0	// 10th alert gene
									//TODO: Add trade genes
									};
	private AvailableScreenCriteria[] availableScreenCriteria = 
				new AvailableScreenCriteria[]{
					new AvailableScreenCriteria("RCCAssetClass", "OR", 
							new String[]{"LIKE[0]=Large Cap",
										 "LIKE[0]=Micro Cap",
										 "LIKE[0]=Mid Cap",
										 "LIKE[0]=Small Cap"}),
					new AvailableScreenCriteria("RCCRegion", "OR", 
							new String[]{"LIKE[0]=",
										 "LIKE[0]=Africa & Mideast",
										 "LIKE[0]=Americas",
										 "LIKE[0]=Asia-Pacific",
										 "LIKE[0]=Europe"})
	};
	private SelectedScreenCriteria[] selectedScreenCriteria =
			new SelectedScreenCriteria[]{
				new SelectedScreenCriteria("RCCAssetClass", "LIKE[0]=Large Cap", "OR")
	};
	private String[] screenSymbols = new String[]{"AAPL", "MSFT"};
	
	private String watchlistName = "Watch1";
	private String portfolioName = "Port1";

	private final int alertId = 1;
	private final String condition = "{symbol}'s price fell below {Price}";
	private final String actualCondition = "AAPL's price fell below 400";
	private final double alertUpper = 500.0;
	private final double alertLower = 300.0;
	private final int alertListLength = 0;
	private AvailableAlert priceBelowAlert = new AvailableAlert(alertId, condition, 
			new String[]{"DOUBLE"}, 
			new String[]{"Price"}, new String[]{"Fund.Price.Low.Lifetime"}, 
			new String[]{"Quote.Value.Last"}, null);
	private AvailableAlert[] availableAlerts = new AvailableAlert[]{
			priceBelowAlert
	};
	private SelectedAlert[] selectedAlerts = new SelectedAlert[]{
			new SelectedAlert(alertId, condition, "AAPL", new double[]{400})
	};
	
	@Before
	public void setUp() throws Exception {

	    // Create mocks
		screenerService = mock(ScreenerService.class);
		watchlistService = mock(WatchlistService.class);
		alertService = mock(AlertService.class);
		portfolioService = mock(PortfolioService.class);
		basicTradeStrategy = mock(BasicTradeStrategy.class);
		alertReceiver = mock(AlertReceiverService.class);
		
		strategyDAO = mock(StrategyDAO.class);
		
		// Describe Mocks
		when(screenerService.getAvailableCriteria(GID)).thenReturn(availableScreenCriteria);
		when(screenerService.screen(GID, 
									selectedScreenCriteria, 
									availableScreenCriteria[0].getName(), 
									MAX_SYMBOLS_PER_SCREEN))
			.thenReturn(screenSymbols);
		
		when(watchlistService.create(CID, watchlistName)).thenReturn(WID);
		when(watchlistService.addHolding(CID, WID, screenSymbols[0]));
		when(portfolioService.create(CID, portfolioName)).thenReturn(PID);
		when(portfolioService.addCashTransaction(CID, PID, STARTING_CASH, 
											true, true)).thenReturn(true);
		
		when(alertService.getAvailableAlerts(CID)).thenReturn(availableAlerts);
		when(alertService.getUpperDouble(CID, alertId, screenSymbols[0], 0)).thenReturn(alertUpper);
		when(alertService.getLowerDouble(CID, alertId, screenSymbols[0], 0)).thenReturn(alertLower);
		when(alertService.getListLength(CID, alertId, 0)).thenReturn(alertListLength);
		when(alertService.parseCondition(priceBelowAlert, screenSymbols[0], new double[50])).
			thenReturn(actualCondition);
		when(alertService.addAlertDestination(GID, EMAIL, "EMAIL")).thenReturn(true);
		when(alertService.setupAlerts(GID, selectedAlerts)).thenReturn(true);
		
		when(portfolioService.openOrderTypesAvailable(CID)).thenReturn(new String[]{"buy"});
		when(basicTradeStrategy.tradeAllocationLower()).thenReturn(BasicTradeStrategy.TRADE_ALLOC_LOWER);
		when(basicTradeStrategy.tradeAllocationUpper()).thenReturn(BasicTradeStrategy.TRADE_ALLOC_UPPER);
		when(basicTradeStrategy.acceptableLossLower()).thenReturn(BasicTradeStrategy.ACCEPT_LOSS_LOWER);
		when(basicTradeStrategy.acceptableLossUpper()).thenReturn(BasicTradeStrategy.ACCEPT_LOSS_UPPER);
		when(basicTradeStrategy.timeInTradeLower()).thenReturn(BasicTradeStrategy.TIME_IN_TRADE_LOWER);
		when(basicTradeStrategy.timeInTradeUpper()).thenReturn(BasicTradeStrategy.TIME_IN_TRADE_UPPER);
		when(basicTradeStrategy.adjustAtLower()).thenReturn(BasicTradeStrategy.ADJUST_AT_LOWER);
		when(basicTradeStrategy.adjustAtUpper()).thenReturn(BasicTradeStrategy.ADJUST_AT_UPPER);
		
		expression = new BasicExpression<int[]>();
		expression.setAlertReceiver(alertReceiver);
		expression.setAlertService(alertService);
		expression.setBasicTradeStrategy(basicTradeStrategy);
		expression.setPortfolioService(portfolioService);
		expression.setScreenerService(screenerService);
		expression.setStrategyDAO(strategyDAO);
	}

	@Test
	public void testExpressScreenerGenes() {
		SelectedScreenCriteria[] screenCriteria = 
				expression.expressScreenerGenes(GID, genomeA, SCREEN_GENES, GENE_UPPER_VALUE);
		
		assertTrue(screenCriteria.length > 0);
		assertEquals("RCCAssetClass",screenCriteria[0].getName());
		assertEquals("LIKE[0]=Large Cap",screenCriteria[0].getValue());
	}
	
	@Test
	public void testExpressAlertGenes() {
		SelectedAlert[] selectedAlerts = expression.expressAlertGenes(GID, genomeA, 
																	screenSymbols, group1);
		
		assertTrue(selectedAlerts.length > 0);
		assertEquals(condition, selectedAlerts[0].getCondition());
		assertEquals(screenSymbols[0], selectedAlerts[0].getSymbol());
		assertTrue(selectedAlerts[0].getParam(0) > alertLower && 
						selectedAlerts[0].getParam(0) > alertUpper);
	}
	
	@Test
	public void testExpressTradeGenes() {
		TradeBounds[] trades = expression.expressTradeGenes(CID, genomeA, screenSymbols, group1);
		fail("Not yet implemented");
	}
	
	@Test
	public void testExpress() {
		candidateA = new Candidate(CID, GID, genomeA, PID, STARTING_CASH);
		
		fail("Not yet implemented");
	}

}
