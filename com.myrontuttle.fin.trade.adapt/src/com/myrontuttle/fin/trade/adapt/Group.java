package com.myrontuttle.fin.trade.adapt;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.*;

/**
 * A group of trade strategy candidates which are generated with common settings
 * @author Myron Tuttle
 */
@Entity(name = "Groups")
public class Group {

	@Id
	@Column(name = "GroupId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String groupId;
	
	@OneToMany(mappedBy = "group", targetEntity = Candidate.class, fetch = FetchType.EAGER)
	private ArrayList<Candidate> candidates;
	
	@Column(name = "AlertAddress")
	private String alertAddress;

	@Column(name = "Size")
	private int size;
	
	@Column(name = "EliteCount")
	private int eliteCount;
	
	@Column(name = "StartTime")
	private Date startTime;	
	
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
	
	@Version
    @Column(name = "LAST_UPDATED_TIME")
    private Date updatedTime;
	
	public Group() {}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(ArrayList<Candidate> candidates) {
		this.candidates = candidates;
	}

	public String getAlertAddress() {
		return alertAddress;
	}

	public void setAlertAddress(String alertAddress) {
		this.alertAddress = alertAddress;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getEliteCount() {
		return eliteCount;
	}

	public void setEliteCount(int eliteCount) {
		this.eliteCount = eliteCount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
