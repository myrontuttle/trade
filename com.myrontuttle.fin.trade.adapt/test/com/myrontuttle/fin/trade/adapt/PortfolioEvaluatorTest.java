package com.myrontuttle.fin.trade.adapt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.PortfolioEvaluator;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;

public class PortfolioEvaluatorTest {
	
	private final static long C1 = 1;
	private final static long C2 = 2;
	private final static long C3 = 3;
	private final static String PA = "portfolioA";
	private final static String PB = "portfolioB";
	private final static String PC = "portfolioC";
	private final static long G1 = 100;
	
	private final static String ANALYSIS = "UnrealizedGain";

	private PortfolioService portfolioService;
	private AdaptDAO adaptDAO;
	
	private PortfolioEvaluator evaluator;
	
	private ExpressedCandidate<int[]> candidateA = new Candidate(C1, 
													G1, 
													new int[]{1,2}, 
													PA);
	private ExpressedCandidate<int[]> candidateB = new Candidate(C2, 
													G1, 
													new int[]{1,2}, 
													PB);
	private ExpressedCandidate<int[]> candidateC = new Candidate(C3, 
													G1, 
													new int[]{1,2}, 
													PC);
	
	private ArrayList<ExpressedCandidate<int[]>> population = 
				new ArrayList<ExpressedCandidate<int[]>>(3);
	
	private Group group1 = new Group();
	
	@Before
	public void setUp() throws Exception {

		population.add(candidateA);
		population.add(candidateB);
		population.add(candidateC);
		
		group1.setString("Eval.Strategy", ANALYSIS);
		
	    // Arrange mocks
		portfolioService = mock(PortfolioService.class);
		when(portfolioService.analyze(C1, PA, ANALYSIS)).
				thenReturn(10000.00);
		when(portfolioService.analyze(C2, PB, ANALYSIS)).
				thenReturn(5000.00);
		when(portfolioService.analyze(C3, PC, ANALYSIS)).
				thenReturn(0.00);
		
		adaptDAO = mock(AdaptDAO.class);
		when(adaptDAO.findGroup(G1)).thenReturn(group1);

		evaluator = new PortfolioEvaluator();
		evaluator.setPortfolioService(portfolioService);
		evaluator.setAdaptDAO(adaptDAO);
	}

	@Test
	public void testGetFitness() {
		
		assertEquals(10000.00, evaluator.getFitness(candidateA, population), .01);
		assertEquals(5000.00, evaluator.getFitness(candidateB, population), .01);
		assertEquals(0.01, evaluator.getFitness(candidateC, population), .001);
	}

}
