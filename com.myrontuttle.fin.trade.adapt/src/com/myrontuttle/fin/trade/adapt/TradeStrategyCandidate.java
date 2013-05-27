package com.myrontuttle.fin.trade.adapt;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.tradestrategies.AlertTradeBounds;

/**
 * A candidate which expresses a trading strategy
 * @author Myron Tuttle
 */
public class TradeStrategyCandidate implements ExpressedCandidate<int[]> {
	
	private final int[] genome;
	private final SelectedScreenCriteria[] screenCriteria;
	private final String[] symbols;
	private final String portfolioId;
	private final AlertTradeBounds[] alerts;
	private final double startingCash;
	
	TradeStrategyCandidate(int[] genome, 
					SelectedScreenCriteria[] screenCriteria,
					String[] symbols, String portfolioId, 
					AlertTradeBounds[] alerts, double startingCash) {
		this.genome = genome;
		this.screenCriteria = screenCriteria;
		this.symbols = symbols;
		this.portfolioId = portfolioId;
		this.alerts = alerts;
		this.startingCash = startingCash;
	}

	@Override
	public int[] getGenome() {
		return genome;
	}
	
	public SelectedScreenCriteria[] getScreenCriteria() {
		return screenCriteria;
	}

	public String[] getSymbols() {
		return symbols;
	}

	public String getPortfolioId() {
		return portfolioId;
	}
	
	public AlertTradeBounds[] getAlerts() {
		return alerts;
	}

	public double getStartingCash() {
		return startingCash;
	}

    /**
     * Compares this candidate's genome with that of the specified
     * expressed candidate.
     * @param expressedCandidate The candidate to compare genome with.
     * @return -1, 0 or 1 if this candidate's genome is less than, equal to,
     * or greater than that of the specified candidate. 
     */
	@Override
	public int compareTo(ExpressedCandidate<int[]> expressedCandidate) {
		if (this.genome.length > expressedCandidate.getGenome().length) {
			return 1;
		} else if (this.genome.length < expressedCandidate.getGenome().length) {
			return -1;
		} else {
			for (int i=0; i<this.genome.length; i++) {
				if (this.genome[i] > expressedCandidate.getGenome()[i]) {
					return 1;
				} else if (this.genome[i] > expressedCandidate.getGenome()[i]) {
					return -1;
				}
			}
			return 0;
		}
	}
}
