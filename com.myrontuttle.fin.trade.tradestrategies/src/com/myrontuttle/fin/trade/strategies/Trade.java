package com.myrontuttle.fin.trade.strategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

/**
 * A record of a trade
 * @author Myron Tuttle
 */
@Entity(name = "TRADES")
public class Trade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRADE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String tradeId;
	
	@Column(name = "TRADE_STRATEGY")
	private String tradeStrategy;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "PORTFOLIO_ID")
	private String portfolioId;
	
	@Column(name = "ALERT_USER_ID")
	private String alertUserId;
	
	@Column(name = "SYMBOL")
	private String symbol;
	
	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="TRADE_PARAMETERS", 
    		joinColumns=@JoinColumn(name="TRADE_ID"))
    private Map<String, Integer> parameters = new HashMap<String, Integer>();

	@OneToMany(mappedBy = "trade", targetEntity = Event.class,
			fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ArrayList<Event> events = new ArrayList<Event>();
	
	public Trade() {}
	
	public Trade(String tradeStrategy, String userId, String portfolioId, String alertUserId, String symbol) {
		this.tradeStrategy = tradeStrategy;
		this.userId = userId;
		this.portfolioId = portfolioId;
		this.alertUserId = alertUserId;
		this.symbol = symbol;
	}

	public void addParameter(String name, int value) {
		parameters.put(name, value);
	}
	
	public int getParameter(String name) {
		return parameters.get(name);
	}
	
	public void removeParameter(String name) {
		parameters.remove(name);
	}
	
	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeStrategy() {
		return tradeStrategy;
	}

	public void setTradeStrategy(String tradeStrategy) {
		this.tradeStrategy = tradeStrategy;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getAlertUserId() {
		return alertUserId;
	}

	public void setAlertUserId(String alertUserId) {
		this.alertUserId = alertUserId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Map<String, Integer> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Integer> parameters) {
		this.parameters = parameters;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public void addEvent(Event e) {
		this.events.add(e);
		if (e.getTrade() != this) {
			e.setTrade(this);
		}
	}
	
	public void removeEvent(Event e) {
		if (events.contains(e)) {
			events.remove(e);
			e.setTrade(null);
		}
	}
}
