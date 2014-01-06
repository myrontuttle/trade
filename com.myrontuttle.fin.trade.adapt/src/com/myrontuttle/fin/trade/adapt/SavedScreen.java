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

@Entity(name = "SavedScreens")
public class SavedScreen implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SavedScreenId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String savedScreenId;

	@Column(name = "TraderId")
	private String traderId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TraderId", referencedColumnName = "TraderId")
	private Trader trader;
	
	@Column(name = "Name")
	private String name;

	@Column(name = "ScreenValue")
	private String screenValue;

	@Column(name = "ArgOperator")
	private String argsOperator;
	
	public SavedScreen() {}
	
	public SavedScreen(String traderId, SelectedScreenCriteria screen) {
		this.traderId = traderId;
		this.name = screen.getName();
		this.screenValue = screen.getValue();
		this.argsOperator = screen.getArgsOperator();
	}
	
	public SelectedScreenCriteria createCriteria() {
		return new SelectedScreenCriteria(name, screenValue, argsOperator);
	}

	public String getSavedScreenId() {
		return savedScreenId;
	}

	public void setSavedScreenId(String savedScreenId) {
		this.savedScreenId = savedScreenId;
	}

	public String getTraderId() {
		return traderId;
	}

	public void setTraderId(String traderId) {
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

	public String getScreenValue() {
		return screenValue;
	}

	public void setScreenValue(String screenValue) {
		this.screenValue = screenValue;
	}

	public String getArgsOperator() {
		return argsOperator;
	}

	public void setArgsOperator(String argsOperator) {
		this.argsOperator = argsOperator;
	}
}
