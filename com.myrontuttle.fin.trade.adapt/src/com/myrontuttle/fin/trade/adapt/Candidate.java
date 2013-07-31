package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import javax.persistence.*;

import com.myrontuttle.evolve.ExpressedCandidate;
/*
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.tradestrategies.AlertTradeBounds;
*/
/**
 * A candidate which expresses a trading strategy
 * @author Myron Tuttle
 */
@Entity(name = "Candidates")
public class Candidate implements ExpressedCandidate<int[]> {

	@Id
	@Column(name = "CandidateId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String candidateId;

	@Column(name = "GroupId")
	private String groupId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "GroupId", referencedColumnName = "GroupId")
	private Group group;
	
	@ElementCollection
	@Column(name = "GenomeList")
	private List<Integer> genomeList;

	@Column(name = "PortfolioId")
	private String portfolioId;

	@Column(name = "StartingCash")
	private double startingCash;
	
	/*
	private SelectedScreenCriteria[] screenCriteria;
	private String[] symbols;
	private AlertTradeBounds[] alerts;
	*/
	
	Candidate(){ }
	
	Candidate(String candidateId, String groupId, List<Integer> genomeList, 
			String portfolioId, double startingCash/*,
			SelectedScreenCriteria[] screenCriteria,
			String[] symbols, AlertTradeBounds[] alerts*/) {
		this.candidateId = candidateId;
		this.groupId = groupId;
		this.genomeList = genomeList;
		this.portfolioId = portfolioId;
		this.startingCash = startingCash;
		/*
		this.screenCriteria = screenCriteria;
		this.symbols = symbols;
		this.alerts = alerts;
		*/
	}

	public String getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}
	
	public String getGroupId() {
		return groupId;
	}	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<Integer> getGenomeList() {
		return genomeList;
	}
	public void setGenomeList(List<Integer> genomeList) {
		this.genomeList = genomeList;
	}

	@Override
	public int[] getGenome() {
		return listToArray(genomeList);
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

/*
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
