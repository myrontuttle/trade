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

	@Column(name = "TRADER_ID")
	private long traderId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TRADER_ID", referencedColumnName = "TRADER_ID")
	private Trader trader;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "ARG_OPERATOR")
	private String argsOperator;
	
	public SavedScreen() {}
	
	public SavedScreen(long traderId, String name, String value, String argsOperator) {
		this.traderId = traderId;
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

	public long getTraderId() {
		return traderId;
	}

	public void setTraderId(long traderId) {
		this.traderId = traderId;
	}

	public Trader getTrader() {
		return trader;
	}

	public void setTrader(Trader trader) {
		this.trader = trader;
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
