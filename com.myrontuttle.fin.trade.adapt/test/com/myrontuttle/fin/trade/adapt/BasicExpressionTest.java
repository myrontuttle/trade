package com.myrontuttle.fin.trade.adapt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableScreenCriteria;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.ScreenerService;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.api.WatchlistService;
import com.myrontuttle.fin.trade.tradestrategies.BasicTradeStrategy;

public class BasicExpressionTest {

	private final static String CA = "candidateA";
	private final static String PA = "portfolioA";
	private final static String G1 = "group1";
	private final static String E1 = "test@test.com";
	private final double STARTING_CASH = 10000.00;
	
	private ScreenerService screenerService;
	private WatchlistService watchlistService;
	private AlertService alertService;
	private PortfolioService portfolioService;
	private BasicTradeStrategy basicTradeStrategy;
	private AlertReceiverService alertReceiver;

	private EntityManager em;
	
	private BasicExpression<int[]> expression;

	private Candidate candidateA;
	private int[] genomeA = new int[]{1	// Sort by first screener gene
									,55,20,20	// 1st screener gene
									,25,20,20	// 2nd screener gene (inactive)
									,100,75,50	// 3rd screener gene
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
	private String[] screenSymbols;
	
	@Before
	public void setUp() throws Exception {

	    // Arrange mocks
		screenerService = mock(ScreenerService.class);
		watchlistService = mock(WatchlistService.class);
		alertService = mock(AlertService.class);
		portfolioService = mock(PortfolioService.class);
		basicTradeStrategy = mock(BasicTradeStrategy.class);
		alertReceiver = mock(AlertReceiverService.class);
		
		em = mock(EntityManager.class);
		
		when(screenerService.getAvailableCriteria(G1)).thenReturn(availableScreenCriteria);
		when(screenerService.screen(G1, 
									expression.expressScreenerGenes(G1, genomeA, 1), 
									availableScreenCriteria[0].getName(), 
									BasicExpression.MAX_SYMBOLS_PER_SCREEN))
			.thenReturn(screenSymbols);
		
		when(portfolioService.closeAllPositions(CA, PA)).
				thenReturn(20000.00);
		
		expression = new BasicExpression<int[]>(screenerService, 
							watchlistService,
							alertService, 
							portfolioService,
							basicTradeStrategy,
							alertReceiver,
							em);
		when(expression.newCandidateRecord(genomeA, G1)).thenReturn(candidateA);
		when(expression.findAlertAddress(G1)).thenReturn(E1);
	}

	@Test
	public void testExpressScreenerGenes() {
		SelectedScreenCriteria[] screenCriteria = 
				expression.expressScreenerGenes(G1, genomeA, 1);
		
		assertTrue(screenCriteria.length > 0);
		assertEquals("RCCAssetClass",screenCriteria[0].getName());
		assertEquals("LIKE[0]=Large Cap",screenCriteria[0].getValue());
	}
	
	@Test
	public void testExpress() {
		candidateA = new Candidate(CA, G1, genomeA, PA, STARTING_CASH);
		
		fail("Not yet implemented");
	}

}
