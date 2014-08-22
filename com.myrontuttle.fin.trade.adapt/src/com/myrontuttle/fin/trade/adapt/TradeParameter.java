package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.myrontuttle.fin.trade.api.SelectedStrategyParameter;

@Entity(name = "TRADE_PARAMETERS")
public class TradeParameter implements Serializable, SelectedStrategyParameter {

	private static final long serialVersionUID = 1L;
	public static final String SEPARATOR = ":";
	public static final String ASSIGN = "=";
	
	@Id
	@Column(name = "TRADE_PARAMETER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long tradeParameterId;

	@Column(name = "CANDIDATE_ID")
	private long candidateId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CANDIDATE_ID", referencedColumnName = "CANDIDATE_ID")
	private Candidate candidate;

	@Column(name = "TRADE_ID")
	private long tradeId;
	
	@Column(name = "SYMBOL")
	private String symbol;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "VALUE")
	private int value;
	
	public TradeParameter() {}
	
	public TradeParameter(long candidateId, String symbol, String name, int value) {
		this.candidateId = candidateId;
		this.symbol = symbol;
		this.name = name;
		this.value = value;
	}

	public String getParameter() {
		return symbol + SEPARATOR + name + ASSIGN + value;
	}

	public long getTradeParameterId() {
		return tradeParameterId;
	}

	public void setTradeParameterId(long tradeParameterId) {
		this.tradeParameterId = tradeParameterId;
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

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
