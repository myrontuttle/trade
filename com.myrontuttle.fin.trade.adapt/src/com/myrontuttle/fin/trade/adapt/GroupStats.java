package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "GroupStats")
public class GroupStats implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "StatsId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String statsId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "GroupId", referencedColumnName = "GroupId")
	private Group group;
	
	@Column(name = "GroupId")
	private String groupId;

	@Column(name = "BestCandidateId")
	private String bestCandidateId;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "BestCandidateId")
	private Candidate bestCandidate;
	
	@Column(name = "BestCandidateFitness")
	private double bestCandidateFitness;
	
	@Column(name = "MeanFitness")
    private double meanFitness;
	
	@Column(name = "FitnessStandardDeviation")
    private double fitnessStandardDeviation;
	
	@Column(name = "GenerationNumber")
    private int generationNumber;
	
	public GroupStats() {}

	public GroupStats(String groupId, String bestCandidateId,
			double bestCandidateFitness,
			double meanFitness, double fitnessStandardDeviation,
			int generationNumber) {
		super();
		this.groupId = groupId;
		this.bestCandidateId = bestCandidateId;
		this.bestCandidateFitness = bestCandidateFitness;
		this.meanFitness = meanFitness;
		this.fitnessStandardDeviation = fitnessStandardDeviation;
		this.generationNumber = generationNumber;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getBestCandidateId() {
		return bestCandidateId;
	}

	public void setBestCandidateId(String bestCandidateId) {
		this.bestCandidateId = bestCandidateId;
	}

	public Candidate getBestCandidate() {
		return bestCandidate;
	}

	public void setBestCandidate(Candidate bestCandidate) {
		this.bestCandidate = bestCandidate;
	}

	public double getBestCandidateFitness() {
		return bestCandidateFitness;
	}

	public void setBestCandidateFitness(double bestCandidateFitness) {
		this.bestCandidateFitness = bestCandidateFitness;
	}

	public double getMeanFitness() {
		return meanFitness;
	}

	public void setMeanFitness(double meanFitness) {
		this.meanFitness = meanFitness;
	}

	public double getFitnessStandardDeviation() {
		return fitnessStandardDeviation;
	}

	public void setFitnessStandardDeviation(double fitnessStandardDeviation) {
		this.fitnessStandardDeviation = fitnessStandardDeviation;
	}

	public int getGenerationNumber() {
		return generationNumber;
	}

	public void setGenerationNumber(int generationNumber) {
		this.generationNumber = generationNumber;
	}

}
