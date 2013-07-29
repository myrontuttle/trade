package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.tradestrategies.AlertTradeBounds;

/**
 * A candidate which expresses a trading strategy
 * @author Myron Tuttle
 */
public class Candidate implements ExpressedCandidate<int[]> {
	
	private List<Integer> genomeList;
	private int[] genome;

	private String individualId;
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
					String alertAddress, List<Integer> genomeList, 
					SelectedScreenCriteria[] screenCriteria,
					String[] symbols, String portfolioId, 
					AlertTradeBounds[] alerts, double startingCash) {
		this.individualId = individualId;
		this.groupId = groupId;
		this.alertAddress = alertAddress;
		this.genomeList = genomeList;
		this.genome = listToArray(genomeList);
		this.screenCriteria = screenCriteria;
		this.symbols = symbols;
		this.portfolioId = portfolioId;
		this.alerts = alerts;
		this.startingCash = startingCash;
	}
	
	private int[] listToArray(List<Integer> list) {
		int[] array = new int[list.size()];
		for (int i=0; i<array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
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
		if (this.genomeList.size() > expressedCandidate.getGenome().length) {
			return 1;
		} else if (this.genomeList.size() < expressedCandidate.getGenome().length) {
			return -1;
		} else {
			for (int i=0; i<this.genomeList.size(); i++) {
				if (this.genomeList.get(i) > expressedCandidate.getGenome()[i]) {
					return 1;
				} else if (this.genomeList.get(i) > expressedCandidate.getGenome()[i]) {
					return -1;
				}
			}
			return 0;
		}
	}

	/**
	 * @return the genomeList
	 */
	public List<Integer> getGenomeList() {
		return genomeList;
	}

	/**
	 * @param genomeList the genomeList to set
	 */
	public void setGenomeList(List<Integer> genomeList) {
		this.genomeList = genomeList;
	}

	@Override
	public int[] getGenome() {
		return genome;
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
