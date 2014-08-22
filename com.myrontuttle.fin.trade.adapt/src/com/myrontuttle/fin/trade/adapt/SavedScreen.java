package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;

@Entity(name = "SAVED_SCREENS")
public class SavedScreen implements Serializable, SelectedScreenCriteria {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SAVED_SCREEN_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long savedScreenId;

	@Column(name = "CANDIDATE_ID")
	private long candidateId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CANDIDATE_ID", referencedColumnName = "CANDIDATE_ID")
	private Candidate candidate;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "ARG_OPERATOR")
	private String argsOperator;
	
	public SavedScreen() {}
	
	public SavedScreen(long candidateId, String name, String value, String argsOperator) {
		this.candidateId = candidateId;
		this.name = name;
		this.value = value;
		this.argsOperator = argsOperator;
	}

	public long getSavedScreenId() {
		return savedScreenId;
	}

	public void setSavedScreenId(long savedScreenId) {
		this.savedScreenId = savedScreenId;
	}

	public long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getArgsOperator() {
		return argsOperator;
	}

	public void setArgsOperator(String argsOperator) {
		this.argsOperator = argsOperator;
	}
}
