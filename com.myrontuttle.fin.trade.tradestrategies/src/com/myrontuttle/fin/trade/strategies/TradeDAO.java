package com.myrontuttle.fin.trade.strategies;

import java.util.List;

import javax.persistence.EntityManager;

public class TradeDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public void saveTrade(Trade t) {
		em.persist(t);
	}
	
	public Trade updateTrade(Trade t) {
		return em.merge(t);
	}

	// Get the trade based on a tradeId
	public Trade findTrade(long tradeId) {
		return em.find(Trade.class, tradeId);
	}
	
	public boolean tradeExists(long tradeId) {
		return em.createQuery(
				"SELECT t FROM TRADES t WHERE t.tradeId = :tradeId", 
				Trade.class).setParameter("tradeId", tradeId).getResultList().size() == 1;
	}
	
	public List<Trade> findTradesWithSymbol(String symbol) {
		return em.createQuery(
				"SELECT t FROM TRADES t WHERE t.symbol = :symbol", 
				Trade.class).setParameter("symbol", symbol).getResultList();
	}

	public List<Trade> findTradesForUser(long userId) {
		return em.createQuery(
				"SELECT t FROM TRADES t WHERE t.userId = :userId", 
				Trade.class).setParameter("userId", userId).getResultList();
	}

	public void removeTrade(long tradeId) {
		em.remove(em.find(Trade.class, tradeId));
	}
	
	public void addEvent(long tradeId, Event e) {
		Trade trade = em.find(Trade.class, tradeId);
		trade.addEvent(e);
	}
	
	public List<Event> findEvents(String event) {

		// Retrieve events from database
		return em.createQuery(
				"SELECT e FROM EVENTS e WHERE e.event = :event", 
				Event.class).setParameter("event", event).getResultList();
	}

	public List<Event> findEventsWithTrigger(String trigger) {
		// Retrieve events from database
		return em.createQuery(
				"SELECT e FROM EVENTS e WHERE e.trigger = :trigger", 
				Event.class).setParameter("trigger", trigger).getResultList();
	}
	
	public void removeEventsFromDB(String event) {
		List<Event> events = findEvents(event);
		for (Event e : events) {
			em.remove(e);
		}
	}
	
	public void removeAllTradeEvents(long tradeId) {
		Trade trade = em.find(Trade.class, tradeId);
		for (Event e : trade.getEvents()) {
			em.remove(e);
		}
		trade.getEvents().clear();
	}

	public void addParameter(long tradeId, String name, int value) {
		Trade trade = em.find(Trade.class, tradeId);
		trade.addParameter(name, value);
	}
}
