package com.myrontuttle.fin.trade.adapt.eval;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.eval.BasicEvaluator;
import com.myrontuttle.fin.trade.api.PortfolioService;

public class BasicEvaluatorTest {
	
	private final static String CA = "candidateA";
	private final static String CB = "candidateB";
	private final static String CC = "candidateC";
	private final static String PA = "portfolioA";
	private final static String PB = "portfolioB";
	private final static String PC = "portfolioC";

	private PortfolioService portfolioService;
	
	private BasicEvaluator evaluator;
	
	private double startingCash = 10000.00;
	private ExpressedCandidate<int[]> candidateA = new Candidate(CA, 
													"group1", 
													new int[]{1,2}, 
													PA, 
													startingCash);
	private ExpressedCandidate<int[]> candidateB = new Candidate(CB, 
													"group1", 
													new int[]{1,2}, 
													PB, 
													startingCash);
	private ExpressedCandidate<int[]> candidateC = new Candidate(CC, 
													"group1", 
													new int[]{1,2}, 
													PC, 
													startingCash);
	
	private ArrayList<ExpressedCandidate<int[]>> group1 = 
				new ArrayList<ExpressedCandidate<int[]>>(3);
	
	@Before
	public void setUp() throws Exception {

		group1.add(candidateA);
		group1.add(candidateB);
		group1.add(candidateC);
		
	    // Arrange mocks
		portfolioService = mock(PortfolioService.class);
		when(portfolioService.closeAllPositions(CA, PA)).
				thenReturn(20000.00);
		when(portfolioService.closeAllPositions(CB, PB)).
				thenReturn(15000.00);
		when(portfolioService.closeAllPositions(CC, PC)).
				thenReturn(5000.00);

		evaluator = new BasicEvaluator();
		evaluator.setPortfolioService(portfolioService);
	}

	@Test
	public void testGetFitness() {
		
		assertEquals(10000.00, evaluator.getFitness(candidateA, group1), .01);
		assertEquals(5000.00, evaluator.getFitness(candidateB, group1), .01);
		assertEquals(0, evaluator.getFitness(candidateC, group1), .01);
	}

}
