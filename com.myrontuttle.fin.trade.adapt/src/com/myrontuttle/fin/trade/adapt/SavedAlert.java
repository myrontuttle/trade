package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.myrontuttle.fin.trade.api.SelectedAlert;

@Entity(name = "SAVED_ALERTS")
public class SavedAlert implements Serializable, SelectedAlert {

	private static final long serialVersionUID = 1L;
	public static final String SEPARATOR = ",";
	
	@Id
	@Column(name = "SAVED_ALERT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long savedAlertId;

	@Column(name = "CANDIDATE_ID")
	private long candidateId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CANDIDATE_ID", referencedColumnName = "CANDIDATE_ID")
	private Candidate candidate;

	@Column(name = "ALERT_TYPE")
	private int alertType;
	
	@Column(name = "CONDITION")
	private String condition;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "PARAM_STRING")
	private String paramString;
	
	@Transient
	private double[] params;
	
	@Column(name = "ALERT_ID")
	private String alertId;
	
	public SavedAlert() {}

	public SavedAlert(long candidateId, int alertType, String condition, String symbol, double... params) {
		this.candidateId = candidateId;
		this.alertType = alertType;
		this.condition = condition;
		this.symbol = symbol;
		this.params = params;
		this.paramString = generateParams(params);
	}
	
	public static String generateParams(double[] p) {
		if (p == null || p.length == 0) {
			return "";
		}
		int k = p.length;
		StringBuilder out = new StringBuilder();
		out.append(p[0]);
		for (int i = 1; i < k; i++) {
			out.append(SEPARATOR).append(p[i]);
		}

		return out.toString();
	}
	
	public static double[] parseParams(String params) {
		String[] asStrings = params.split(SEPARATOR);
		double[] array = new double[asStrings.length];
		for (int i=0; i<array.length; i++) {
			array[i] = Double.parseDouble(asStrings[i]);
		}
		return array;
	}

	public long getSavedAlertId() {
		return savedAlertId;
	}

	public void setSavedAlertId(long savedAlertId) {
		this.savedAlertId = savedAlertId;
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

	public int getAlertType() {
		return alertType;
	}

	public void setAlertType(int alertType) {
		this.alertType = alertType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getParamString() {
		return paramString;
	}

	public void setParamString(String paramString) {
		this.paramString = paramString;
	}
	
	public double getParam(int index) {
		return params[index];
	}

	public double[] getParams() {
		return params;
	}

	public void setParams(double[] params) {
		this.params = params;
	}

	public String getAlertId() {
		return alertId;
	}

	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
}
