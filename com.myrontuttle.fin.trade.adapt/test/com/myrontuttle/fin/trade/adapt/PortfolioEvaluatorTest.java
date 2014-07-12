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
	
	private final static String CA = "candidateA";
	private final static String CB = "candidateB";
	private final static String CC = "candidateC";
	private final static String PA = "portfolioA";
	private final static String PB = "portfolioB";
	private final static String PC = "portfolioC";
	private final static String G1 = "group1";
	
	private final static String ANALYSIS = "UnrealizedGain";

	private PortfolioService portfolioService;
	private GroupDAO groupDAO;
	
	private PortfolioEvaluator evaluator;
	
	private ExpressedCandidate<int[]> candidateA = new Candidate(CA, 
													G1, 
													new int[]{1,2}, 
													PA);
	private ExpressedCandidate<int[]> candidateB = new Candidate(CB, 
													G1, 
													new int[]{1,2}, 
													PB);
	private ExpressedCandidate<int[]> candidateC = new Candidate(CC, 
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
		
		group1.setEvaluationStrategy(ANALYSIS);
		
	    // Arrange mocks
		portfolioService = mock(PortfolioService.class);
		when(portfolioService.analyze(CA, PA, ANALYSIS)).
				thenReturn(10000.00);
		when(portfolioService.analyze(CB, PB, ANALYSIS)).
				thenReturn(5000.00);
		when(portfolioService.analyze(CC, PC, ANALYSIS)).
				thenReturn(0.00);
		
		groupDAO = mock(GroupDAO.class);
		when(groupDAO.findGroup(G1)).thenReturn(group1);

		evaluator = new PortfolioEvaluator();
		evaluator.setPortfolioService(portfolioService);
		evaluator.setGroupDAO(groupDAO);
	}

	@Test
	public void testGetFitness() {
		
		assertEquals(10000.00, evaluator.getFitness(candidateA, population), .01);
		assertEquals(5000.00, evaluator.getFitness(candidateB, population), .01);
		assertEquals(0.01, evaluator.getFitness(candidateC, population), .001);
	}

}
