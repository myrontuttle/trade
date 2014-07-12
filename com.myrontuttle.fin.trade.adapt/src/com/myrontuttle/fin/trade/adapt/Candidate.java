package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.Arrays;

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
	private String candidateId;

	@Column(name = "GROUP_ID")
	private String groupId;
	
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
	
	public Candidate(){ }
	
	public Candidate(String candidateId, String groupId, int[] genome, 
			String portfolioId) {
		this.candidateId = candidateId;
		this.groupId = groupId;
		this.genome = genome;
		this.genomeString = Arrays.toString(genome);
		this.portfolioId = portfolioId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((candidateId == null) ? 0 : candidateId.hashCode());
		result = prime * result
				+ ((genomeString == null) ? 0 : genomeString.hashCode());
		result = prime * result
				+ ((portfolioId == null) ? 0 : portfolioId.hashCode());
		result = prime * result
				+ ((watchlistId == null) ? 0 : watchlistId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Candidate other = (Candidate) obj;
		if (candidateId == null) {
			if (other.candidateId != null)
				return false;
		} else if (!candidateId.equals(other.candidateId))
			return false;
		if (genomeString == null) {
			if (other.genomeString != null)
				return false;
		} else if (!genomeString.equals(other.genomeString))
			return false;
		if (portfolioId == null) {
			if (other.portfolioId != null)
				return false;
		} else if (!portfolioId.equals(other.portfolioId))
			return false;
		if (watchlistId == null) {
			if (other.watchlistId != null)
				return false;
		} else if (!watchlistId.equals(other.watchlistId))
			return false;
		return true;
	}
	
}
