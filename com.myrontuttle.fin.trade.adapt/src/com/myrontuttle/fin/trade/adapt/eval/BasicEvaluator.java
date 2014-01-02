package com.myrontuttle.fin.trade.adapt.eval;

import java.util.List;


import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.GroupDAO;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.sci.evolve.ExpressedCandidate;
import com.myrontuttle.sci.evolve.ExpressedFitnessEvaluator;

/**
 * Setup how strategies are evaluated
 * @author Myron Tuttle
 */
public class BasicEvaluator implements ExpressedFitnessEvaluator<int[]> {

	private static PortfolioService portfolioService = null;
	private static GroupDAO groupDAO;

	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		BasicEvaluator.portfolioService = portfolioService;
	}

	public static GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		BasicEvaluator.groupDAO = groupDAO;
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
		
		Candidate tradeCandidate = (Candidate)candidate;

		if (tradeCandidate.getPortfolioId() == null || tradeCandidate.getPortfolioId() == "") {
			return 0;
		}
		
		// Fitness is simply realized gain
		double balance = 0;
		double startingCash = 0;
		try {
			startingCash = groupDAO.findGroup(tradeCandidate.getGroupId()).getStartingCash();
			balance = portfolioService.closeAllPositions(tradeCandidate.getCandidateId(),
														tradeCandidate.getPortfolioId());
		} catch (Exception e) {
			System.out.println("Error getting portfolio balance for candidate: " + 
					tradeCandidate.getCandidateId() + ". Group: " + tradeCandidate.getGroupId());
			e.printStackTrace();
			return 0;
		}
		return (balance >= startingCash) ? 
					balance - startingCash : 0;
	}
}
