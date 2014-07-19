package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

@Entity(name = "GROUP_STATS")
public class GroupStats implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STATS_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long statsId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
	private Group group;
	
	@Column(name = "GROUP_ID")
	private long groupId;
	
	@Column(name = "BEST_CANDIDATE_FITNESS")
	private double bestCandidateFitness;
	
	@Column(name = "MEAN_FITNESS")
    private double meanFitness;
	
	@Column(name = "FITNESS_STANDARD_DEVIATION")
    private double fitnessStandardDeviation;
	
	@Column(name = "GENERATION_NUMBER")
    private int generationNumber;
	
	@Column(name = "VARIABILITY")
	private double variability;

	@Version
    @Column(name = "RECORDED_TIME")
    private Date recordedTime;
	
	public GroupStats() {}

	public GroupStats(long groupId, double bestCandidateFitness,
			double meanFitness, double fitnessStandardDeviation,
			int generationNumber, double variability) {
		super();
		this.groupId = groupId;
		this.bestCandidateFitness = bestCandidateFitness;
		this.meanFitness = meanFitness;
		this.fitnessStandardDeviation = fitnessStandardDeviation;
		this.generationNumber = generationNumber;
		this.variability = variability;
	}

	public long getStatsId() {
		return statsId;
	}

	public void setStatsId(long statsId) {
		this.statsId = statsId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
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

	public double getVariability() {
		return variability;
	}

	public void setVariability(double variability) {
		this.variability = variability;
	}

	public Date getRecordedTime() {
		return recordedTime;
	}

	public void setRecordedTime(Date recordedTime) {
		this.recordedTime = recordedTime;
	}

}
