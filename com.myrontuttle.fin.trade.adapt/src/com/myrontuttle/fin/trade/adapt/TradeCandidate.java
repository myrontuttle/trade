package com.myrontuttle.fin.trade.adapt;

import java.util.Arrays;

import com.myrontuttle.evolve.ExpressedCandidate;

/**
 * A candidate which expresses a trading strategy
 * @author Myron Tuttle
 */
public class TradeCandidate implements ExpressedCandidate<int[]> {
	
	private final static String GENOME_MARKER = ":G=";
	private final static String PORTFOLIO_MARKER = ":P=";
	private final static String CASH_MARKER = ":C=";
	
	private final int[] genome;
	private final String portfolioId;
	private final double startingCash;
	
	TradeCandidate(int[] genome, String portfolioId, double startingCash) {
		this.genome = genome;
		this.portfolioId = portfolioId;
		this.startingCash = startingCash;
	}

	@Override
	public int[] getGenome() {
		return genome;
	}
	
	public String getPortfolioId() {
		return portfolioId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return GENOME_MARKER + Arrays.toString(genome)
				+ PORTFOLIO_MARKER + portfolioId + CASH_MARKER
				+ startingCash;
	}
	
	public static TradeCandidate fromString(String rep) {
		String g = rep.substring(rep.indexOf(GENOME_MARKER) + GENOME_MARKER.length(), 
									rep.indexOf(PORTFOLIO_MARKER));
		String[] gStrings = g.replace("[", "").replace("]", "").split(", ");
		int[] genome = new int[gStrings.length];
		for (int i=0; i<gStrings.length; i++) {
			genome[i] = Integer.parseInt(gStrings[i]);
		}

		String portfolioId = rep.substring(rep.indexOf(PORTFOLIO_MARKER) + PORTFOLIO_MARKER.length(), 
											rep.indexOf(CASH_MARKER));
		
		String c = rep.substring(rep.indexOf(CASH_MARKER) + CASH_MARKER.length());
		double startingCash = Double.parseDouble(c);
		
		return new TradeCandidate(genome, portfolioId, startingCash);
	}

}
