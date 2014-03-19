package com.myrontuttle.fin.trade.adapt.express;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.GroupDAO;
import com.myrontuttle.fin.trade.adapt.express.BasicExpression;
import com.myrontuttle.fin.trade.api.*;

public class BasicExpressionTest {

	private final static String CID = "candidateID";
	private final static String WID = "watchlistID";
	private final static String PID = "portfolioID";
	private final static String GID = "groupID";
	private final static String LID = "LotID";
	private final static String EMAIL = "test@test.com";
	private final double STARTING_CASH = 10000.00;
	private final static String BUY = "Buy";
	private final static String SELL = "Sell";
	private final static String SHORT = "ShortSell";
	private final static String COVER = "BuyToCover";
	private final static String BOUNDED_STRAT = "Bounded Strategy";

	private static final int SCREEN_GENES = 3;
	private static final int MAX_SYMBOLS_PER_SCREEN = 5;
	private static final int ALERTS_PER_SYMBOL = 1;
	private static final int GENE_UPPER_VALUE = 100;
	
	private ScreenerService screenerService;
	private WatchlistService watchlistService;
	private AlertService alertService;
	private PortfolioService portfolioService;
	private QuoteService quoteService;
	private TradeStrategyService strategyService;
	private AlertReceiver alertReceiver;
	private TradeStrategy tradeStrategy;
	private GroupDAO groupDAO;
	
	private BasicExpression<int[]> expression;
	
	private Candidate candidateA;
	private Group group1;
	private int[] genomeA = new int[]{1	// Sort by first screener gene
									,55,20,20		// 1st screener gene
									,25,20,20		// 2nd screener gene (inactive)
									,100,75,50		// 3rd screener gene
									,19,50,0,0  	// 1st alert gene
									,12,25,0,0		// 2nd alert gene
									,1,2,3,4		// 3rd alert gene
									,8,7,6,5		// 4th alert gene
									,11,21,22,23	// 5th alert gene
									,19,37,24,0,88  	// 1st trade gene
									,55,25,66,100,75	// 2nd trade gene
									,1,2,3,4,5			// 3rd trade gene
									,8,7,6,5,6			// 4th trade gene
									,11,21,22,23,5		// 5th trade gene
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
				new SelectedScreenCriteria("RCCAssetClass", "LIKE[0]=Large Cap", "OR"),
				new SelectedScreenCriteria("RCCRegion", "LIKE[0]=Americas", "OR")
	};
	private String[] screenSymbols = new String[]{"AAPL", "MSFT", "CSCO", "AA", "T"};
	
	private String watchlistName = BasicExpression.WATCH_NAME_PREFIX + BasicExpression.GROUP + GID + 
			BasicExpression.CANDIDATE + CID;
	private String portfolioName = BasicExpression.PORT_NAME_PREFIX + BasicExpression.GROUP + GID + 
			BasicExpression.CANDIDATE + CID;

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
			new SelectedAlert(alertId, condition, screenSymbols[0], new double[]{300}),
			new SelectedAlert(alertId, condition, screenSymbols[1], new double[]{200}),
			new SelectedAlert(alertId, condition, screenSymbols[2], new double[]{100}),
			new SelectedAlert(alertId, condition, screenSymbols[3], new double[]{50}),
			new SelectedAlert(alertId, condition, screenSymbols[4], new double[]{25}),
	};
	
	private String[] openOrderTypes = new String[]{ BUY, SHORT };
	
	private AvailableStrategyParameter[] availableParameters = new AvailableStrategyParameter[]{
			new AvailableStrategyParameter("openOrderType", 0, 0),
			new AvailableStrategyParameter("tradeAllocation", 0, 100),
			new AvailableStrategyParameter("percentBelow", 0, 100),
			new AvailableStrategyParameter("timeLimit", 60, 60*60*24),
			new AvailableStrategyParameter("percentAbove", 0, 100)
	};

	Hashtable<String, Integer> params1 = new Hashtable<String, Integer>(5);

	Hashtable<String, Integer> params2 = new Hashtable<String, Integer>(5);

	private Trade[] trades = new Trade[]{
			new Trade(screenSymbols[0], params1),
			new Trade(screenSymbols[1], params2)
	};
	private AlertTrade[] alertTrade = new AlertTrade[] {
			new AlertTrade(selectedAlerts[0], CID, PID, trades[0])
	};
	
	@Before
	public void setUp() throws Exception {

		// Set variables
		candidateA = new Candidate();
		candidateA.setCandidateId(CID);
		candidateA.setGenomeString(Candidate.generateGenomeString(genomeA));
		candidateA.setGroupId(GID);
		candidateA.setPortfolioId(PID);
		candidateA.setWatchlistId(WID);
		Collection<Candidate> candidates = new ArrayList<Candidate>();
		candidates.add(candidateA);
		
		group1 = new Group();
		group1.setGroupId(GID);
		group1.setNumberOfScreens(SCREEN_GENES);
		group1.setMaxSymbolsPerScreen(MAX_SYMBOLS_PER_SCREEN);
		group1.setAlertsPerSymbol(ALERTS_PER_SYMBOL);
		group1.setGeneUpperValue(100);
		group1.setActive(true);
		group1.setStartingCash(STARTING_CASH);
		group1.setAlertAddress(EMAIL);
		group1.setCandidates(candidates);
		group1.setExpressionStrategy("BasicExpression");
		group1.setTradeStrategy(BOUNDED_STRAT);

		params1.put("openOrderType", 0);
		params1.put("tradeAllocation", 37);
		params1.put("percentBelow", 24);
		params1.put("timeLimit", 60);
		params1.put("percentAbove", 88);
		
		params2.put("openOrderType", 1);
		params2.put("tradeAllocation", 25);
		params2.put("percentBelow", 66);
		params2.put("timeLimit", 86400);
		params2.put("percentAbove", 75);
		
	    // Create mocks
		screenerService = mock(ScreenerService.class);
		watchlistService = mock(WatchlistService.class);
		alertService = mock(AlertService.class);
		portfolioService = mock(PortfolioService.class);
		quoteService = mock(QuoteService.class);
		strategyService = mock(TradeStrategyService.class);
		tradeStrategy = mock(TradeStrategy.class);
		alertReceiver = mock(AlertReceiver.class);
		groupDAO = mock(GroupDAO.class);
		
		// Describe Mocks
		when(screenerService.getAvailableCriteria(GID)).thenReturn(availableScreenCriteria);
		when(screenerService.screen(GID, 
									selectedScreenCriteria, 
									selectedScreenCriteria[0].getName(), 
									MAX_SYMBOLS_PER_SCREEN))
			.thenReturn(screenSymbols);
		
		when(watchlistService.create(CID, watchlistName)).thenReturn(WID);
		when(watchlistService.addHolding(CID, WID, screenSymbols[0])).thenReturn(LID);
		when(watchlistService.addHolding(CID, WID, screenSymbols[1])).thenReturn(LID);
		
		when(portfolioService.create(CID, portfolioName)).thenReturn(PID);
		when(portfolioService.addCashTransaction(CID, PID, STARTING_CASH, 
											true, true)).thenReturn(true);
		when(portfolioService.openOrderTypesAvailable(CID)).thenReturn(openOrderTypes);
		
		when(alertService.getAvailableAlerts(GID)).thenReturn(availableAlerts);
		when(alertService.getUpperDouble(GID, alertId, screenSymbols[0], 0)).thenReturn(alertUpper);
		when(alertService.getLowerDouble(GID, alertId, screenSymbols[0], 0)).thenReturn(alertLower);
		when(alertService.getListLength(GID, alertId, 0)).thenReturn(alertListLength);
		when(alertService.parseCondition(priceBelowAlert, screenSymbols[0], new double[]{400.0})).
			thenReturn(actualCondition);
		when(alertService.addAlertDestination(GID, EMAIL, "EMAIL")).thenReturn(true);
		when(alertService.setupAlerts(GID, selectedAlerts)).thenReturn(selectedAlerts);
		
		when(strategyService.getTradeStrategy(anyString(), anyListOf(Service.class))).thenReturn(tradeStrategy);
		when(tradeStrategy.availableParameters()).thenReturn(availableParameters);
		
		when(groupDAO.findGroup(GID)).thenReturn(group1);
		doAnswer(new Answer<Candidate>() {
		      public Candidate answer(InvocationOnMock invocation) {
		          Candidate candidate = (Candidate)invocation.getArguments()[0];
		          candidate.setCandidateId(CID);
		          candidate.setGroupId(GID);
		          return candidate;
		      }}).when(groupDAO).addCandidate(any(Candidate.class), eq(GID));
		
		//when(alertReceiver.watchFor(alertTradeBounds));
		
		// Assign services
		expression = new BasicExpression<int[]>();
		expression.setAlertReceiver(alertReceiver);
		expression.setAlertService(alertService);
		expression.setTradeStrategyService(strategyService);
		expression.setPortfolioService(portfolioService);
		expression.setWatchlistService(watchlistService);
		expression.setScreenerService(screenerService);
		expression.setGroupDAO(groupDAO);
	}

	@Test
	public void testExpressScreenerGenes() throws Exception {
		SelectedScreenCriteria[] screenCriteria = 
				expression.expressScreenerGenes(candidateA, group1);
		
		assertTrue(screenCriteria.length > 0);
		assertEquals("RCCAssetClass",screenCriteria[0].getName());
		assertEquals("LIKE[0]=Large Cap",screenCriteria[0].getValue());
	}
	
	@Test
	public void testGetScreenSymbols() throws Exception {
		String[] symbols = expression.getScreenSymbols(candidateA, group1, selectedScreenCriteria);
		for (int i=0; i<symbols.length; i++) {
			assertEquals(symbols[i], screenSymbols[i]);
		}
	}
	
	@Test
	public void testSetupWatchlist() throws Exception {
		assertEquals(WID, expression.setupWatchlist(candidateA, group1, screenSymbols));
	}
	
	@Test
	public void testSetupPortfolio() throws Exception {
		assertEquals(PID, expression.setupPortfolio(candidateA, group1));
	}
	
	@Test
	public void testExpressAlertGenes() throws Exception {
		SelectedAlert[] selectedAlerts = expression.expressAlertGenes(candidateA, group1,
																	screenSymbols);
		
		assertTrue(selectedAlerts.length > 0);
		assertEquals(actualCondition, selectedAlerts[0].getCondition());
		assertEquals(screenSymbols[0], selectedAlerts[0].getSymbol());
		assertTrue(selectedAlerts[0].getParam(0) > alertLower && 
						selectedAlerts[0].getParam(0) < alertUpper);
	}
	
	@Test
	public void testSetupAlerts() throws Exception {
		expression.setupAlerts(group1, selectedAlerts);
	}
	
	@Test
	public void testExpressTradeGenes() throws Exception {
		Trade[] trades = expression.expressTradeGenes(candidateA, group1, screenSymbols);
		for (int i=0; i<trades.length; i++) {
			assertTrue(trades[i].equals(trades[i]));
		}
	}
	
	@Test
	public void testSetupAlertReceiver() throws Exception {
		expression.setupAlertReceiver(selectedAlerts, candidateA, PID, trades, group1);
	}
	
	@Test
	public void testExpress() {
		Candidate candidateB = expression.express(genomeA, GID);
		assertTrue(candidateA.equals(candidateB));
	}

}
