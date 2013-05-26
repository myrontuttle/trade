package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.evolve.ExpressedFitnessEvaluator;

import com.myrontuttle.fin.trade.api.PortfolioService;

/**
 * Setup how strategies are evaluated
 * @author Myron Tuttle
 */
public class TradeCandidateEvaluator implements ExpressedFitnessEvaluator<int[]> {

	private final PortfolioService portfolioService;
	
	TradeCandidateEvaluator(PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}

	/**
	 * Higher values indicate better fitness
	 */
	@Override
	public boolean isNatural() {
		return true;
	}

	@Override
	public double getFitness(ExpressedCandidate<int[]> candidate,
			List<ExpressedCandidate<int[]>> population) {
		
		TradeCandidate tradeCandidate = (TradeCandidate)candidate;
		
		// Fitness is simply realized gain
		double balance = 0;
		try {
			balance = portfolioService.closeAllPositions(tradeCandidate.getPortfolioId());
		} catch (Exception e) {
			System.out.println("Error getting portfolio balance: " + e.getMessage());
			e.printStackTrace();
		}
		return (balance >= tradeCandidate.getStartingCash()) ? 
					balance - tradeCandidate.getStartingCash() : 0;
	}
}
