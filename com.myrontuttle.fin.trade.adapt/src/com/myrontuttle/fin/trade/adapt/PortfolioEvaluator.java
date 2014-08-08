package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;
import com.myrontuttle.sci.evolve.api.ExpressedFitnessEvaluator;

public class PortfolioEvaluator implements ExpressedFitnessEvaluator<int[]> {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioEvaluator.class);

	private static PortfolioService portfolioService = null;
	private static AdaptDAO adaptDAO;

	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		PortfolioEvaluator.portfolioService = portfolioService;
	}

	public static AdaptDAO getAdaptDAO() {
		return adaptDAO;
	}

	public void setAdaptDAO(AdaptDAO adaptDAO) {
		PortfolioEvaluator.adaptDAO = adaptDAO;
	}
	
	@Override
	public double getFitness(ExpressedCandidate<int[]> candidate,
			List<ExpressedCandidate<int[]>> population) {

		double fitness = 0.00;
		Candidate tradeCandidate = (Candidate)candidate;

		if (tradeCandidate.getPortfolioId() == null || tradeCandidate.getPortfolioId() == "") {
			return fitness;
		} else {
			// Give a small boost for at least having a portfolio when starting out
			fitness = 0.01;
		}

		Group group = adaptDAO.findGroup(tradeCandidate.getGroupId());
		String evalStrategy = group.getString("Eval.Strategy");
		try {
			if (evalStrategy.equals("Realized Gain")) {
				portfolioService.closeAllPositions(
					tradeCandidate.getCandidateId(), tradeCandidate.getPortfolioId());
			}
			double analysis = portfolioService.analyze(
					tradeCandidate.getCandidateId(), 
					tradeCandidate.getPortfolioId(), evalStrategy);
			if (analysis > fitness) {
				fitness = analysis;
			}
		} catch (Exception e) {
			logger.warn("Error analyzing portfolio for candidate: {}. Group: {}.",
					tradeCandidate.getCandidateId(), tradeCandidate.getGroupId(), e);
			return fitness;
		}
		
		return fitness;
	}

	@Override
	public boolean isNatural() {
		return true;
	}

}
