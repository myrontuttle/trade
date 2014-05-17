package com.myrontuttle.fin.trade.strategies;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A record of an event that takes part of a trade
 * @author Myron Tuttle
 */
@Entity(name = "Events")
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EventId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String eventId;

	@Column(name = "TradeId")
	private String tradeId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "TradeId", referencedColumnName = "TradeId")
	private Trade trade;
	
	@Column(name="Event")
	private String event;

    @Column(name="ActionType")
	private String actionType;

    @Column(name="Trigger")
	private String trigger;
    
    public Event() {}

	public Event(String event, String actionType, String trigger) {
		super();
		this.event = event;
		this.actionType = actionType;
		this.trigger = trigger;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
}
