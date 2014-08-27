package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import com.myrontuttle.sci.evolve.api.ExpressedCandidate;

/**
 * A candidate which expresses a trading strategy
 * @author Myron Tuttle
 */
@Entity(name = "CANDIDATES")
public class Candidate implements ExpressedCandidate<int[]>, Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String GENE_SEPARATOR = ",";

	@Id
	@Column(name = "CANDIDATE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long candidateId;

	@Column(name = "GROUP_ID")
	private long groupId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
	private Group group;
	
	@Transient
	private int[] genome;
	
	@Column(name = "GENOME_STRING")
	@Lob
	private String genomeString;

	@Column(name = "WATCHLIST_ID")
	private String watchlistId;

	@Column(name = "PORTFOLIO_ID")
	private String portfolioId;
	
	@Column(name = "BORN_IN_GEN")
	private int bornInGen;
	
	@Column(name = "LAST_EXRESSED_GEN")
	private int lastExpressedGen;
	
	@Column(name = "BEST_IN_GROUP")
	private boolean bestInGroup;
	
	@OneToMany(mappedBy = "candidate", targetEntity = SavedScreen.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<SavedScreen> savedScreens;
	
	@ElementCollection
	@CollectionTable(
			name="SYMBOLS",
			joinColumns=@JoinColumn(name="CANDIDATE_ID")
	)
	@Column(name="SYMBOL")
	private List<String> symbols;
	
	@OneToMany(mappedBy = "candidate", targetEntity = SavedAlert.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<SavedAlert> savedAlerts;

	@OneToMany(mappedBy = "candidate", targetEntity = TradeParameter.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<TradeParameter> tradeParameters;
	
	public Candidate(){ }
	
	public Candidate(long candidateId, long groupId, int[] genome, 
			String portfolioId) {
		this.candidateId = candidateId;
		this.groupId = groupId;
		this.genome = genome;
		this.genomeString = Arrays.toString(genome);
		this.portfolioId = portfolioId;
	}

	public long getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}
	
	public long getGroupId() {
		return groupId;
	}	
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getGenomeString() {
		return genomeString;
	}
	public void setGenomeString(String genomeString) {
		this.genomeString = genomeString;
		if (genome == null) {
			setGenome(parseGenomeString(genomeString));
		}
	}

	@Override
	public int[] getGenome() {
		if (genome == null) {
			setGenome(parseGenomeString(genomeString));
		}
		return genome;
	}
	
	public void setGenome(int[] genome) {
		this.genome = genome;
		if (genomeString == null || genomeString.isEmpty()) {
			setGenomeString(generateGenomeString(genome));
		}
	}
	
	public String getWatchlistId() {
		return watchlistId;
	}

	public void setWatchlistId(String watchlistId) {
		this.watchlistId = watchlistId;
	}

	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	public int getBornInGen() {
		return bornInGen;
	}

	public void setBornInGen(int bornInGen) {
		this.bornInGen = bornInGen;
	}

	public int getLastExpressedGen() {
		return lastExpressedGen;
	}

	public void setLastExpressedGen(int lastExpressedGen) {
		this.lastExpressedGen = lastExpressedGen;
	}

	public boolean isBestInGroup() {
		return bestInGroup;
	}

	public void setBestInGroup(boolean bestInGroup) {
		this.bestInGroup = bestInGroup;
	}

	public Collection<SavedScreen> getSavedScreens() {
		return savedScreens;
	}

	public void setSavedScreens(Collection<SavedScreen> savedScreens) {
		this.savedScreens = savedScreens;
	}

	public List<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<String> symbols) {
		this.symbols = symbols;
	}

	public Collection<SavedAlert> getSavedAlerts() {
		return savedAlerts;
	}

	public void setSavedAlerts(Collection<SavedAlert> savedAlerts) {
		this.savedAlerts = savedAlerts;
	}

	public Collection<TradeParameter> getTradeParameters() {
		return tradeParameters;
	}

	public void setTradeParameters(Collection<TradeParameter> tradeParameters) {
		this.tradeParameters = tradeParameters;
	}

	public void addScreen(SavedScreen s) {
		this.savedScreens.add(s);
		if (s.getCandidate() != this) {
			s.setCandidate(this);
		}
	}
	
	public void removeScreen(SavedScreen s) {
		if (savedScreens.contains(s)) {
			savedScreens.remove(s);
			s.setCandidate(null);
		}
	}

	public void addSymbol(String s) {
		this.symbols.add(s);
	}
	
	public void removeSymbol(String s) {
		if (symbols.contains(s)) {
			symbols.remove(s);
		}
	}
	
	public void addAlert(SavedAlert a) {
		this.savedAlerts.add(a);
		if (a.getCandidate() != this) {
			a.setCandidate(this);
		}
	}
	
	public void removeAlert(SavedAlert a) {
		if (savedAlerts.contains(a)) {
			savedAlerts.remove(a);
			a.setCandidate(null);
		}
	}
	
	public void addTradeParameter(TradeParameter t) {
		this.tradeParameters.add(t);
		if (t.getCandidate() != this) {
			t.setCandidate(this);
		}
	}
	
	public void removeTradeParameter(TradeParameter t) {
		if (tradeParameters.contains(t)) {
			tradeParameters.remove(t);
			t.setCandidate(null);
		}
	}
	
	public static int[] parseGenomeString(String genomeString) {
		String[] asStrings = genomeString.split(GENE_SEPARATOR);
		int[] array = new int[asStrings.length];
		for (int i=0; i<array.length; i++) {
			array[i] = Integer.parseInt(asStrings[i]);
		}
		return array;
	}
	
	public static String generateGenomeString(int[] genome) {
		if (genome == null || genome.length == 0) {
			return "";
		}
		int k = genome.length;
		StringBuilder out = new StringBuilder();
		out.append(genome[0]);
		for (int i = 1; i < k; i++) {
			out.append(GENE_SEPARATOR).append(genome[i]);
		}

		return out.toString();
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
