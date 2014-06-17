package com.myrontuttle.fin.trade.adapt.eval;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.GroupDAO;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;
import com.myrontuttle.sci.evolve.api.ExpressedFitnessEvaluator;

/**
 * Setup how strategies are evaluated
 * @author Myron Tuttle
 */
public class RealizedGainEvaluator implements ExpressedFitnessEvaluator<int[]> {

	private static final Logger logger = LoggerFactory.getLogger(RealizedGainEvaluator.class);

	private static PortfolioService portfolioService = null;
	private static GroupDAO groupDAO;

	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		RealizedGainEvaluator.portfolioService = portfolioService;
	}

	public static GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		RealizedGainEvaluator.groupDAO = groupDAO;
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
			logger.warn("Error getting portfolio balance for candidate: {}. Group: {}.",
					tradeCandidate.getCandidateId(), tradeCandidate.getGroupId(), e);
			return 0;
		}

		if (balance == startingCash) {
			// Give a small boost for at least having a portfolio when starting out
			return 0.01;
		} else if (balance < startingCash) {
			return 0;
		} else {
			return balance - startingCash;
		}
	}
}
