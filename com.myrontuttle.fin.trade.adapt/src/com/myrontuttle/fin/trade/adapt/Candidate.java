package com.myrontuttle.fin.trade.adapt;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.tradestrategies.AlertTradeBounds;

/**
 * A candidate which expresses a trading strategy
 * @author Myron Tuttle
 */
public class Candidate implements ExpressedCandidate<int[]> {
	
	private String individualId;
	private int[] genome;
	private String portfolioId;
	private double startingCash;
	
	private String groupId;
	private String alertAddress;
	private SelectedScreenCriteria[] screenCriteria;
	private String[] symbols;
	private AlertTradeBounds[] alerts;
	
	Candidate(){
	}
	
	Candidate(String individualId, String groupId, 
					String alertAddress, int[] genome, 
					SelectedScreenCriteria[] screenCriteria,
					String[] symbols, String portfolioId, 
					AlertTradeBounds[] alerts, double startingCash) {
		this.individualId = individualId;
		this.groupId = groupId;
		this.alertAddress = alertAddress;
		this.genome = genome;
		this.screenCriteria = screenCriteria;
		this.symbols = symbols;
		this.portfolioId = portfolioId;
		this.alerts = alerts;
		this.startingCash = startingCash;
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

	@Override
	public int[] getGenome() {
		return genome;
	}
	public void setGenome(int[] genome) {
		this.genome = genome;
	}

	public String getIndividualId() {
		return individualId;
	}
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}
	
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	public double getStartingCash() {
		return startingCash;
	}
	public void setStartingCash(double startingCash) {
		this.startingCash = startingCash;
	}
/*
	public String getGroupId() {
		return groupId;
	}	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

    public String getAlertAddress() {
		return alertAddress;
	}
	public void setAlertAddress(String alertAddress) {
		this.alertAddress = alertAddress;
	}
	
	public AlertTradeBounds[] getAlerts() {
		return alerts;
	}
	public void setAlerts(AlertTradeBounds[] alerts) {
		this.alerts = alerts;
	}
	
	public SelectedScreenCriteria[] getScreenCriteria() {
		return screenCriteria;
	}
	public void setScreenCriteria(SelectedScreenCriteria[] screenCriteria) {
		this.screenCriteria = screenCriteria;
	}

	public String[] getSymbols() {
		return symbols;
	}
	public void setSymbols(String[] symbols) {
		this.symbols = symbols;
	}


	*/
}
