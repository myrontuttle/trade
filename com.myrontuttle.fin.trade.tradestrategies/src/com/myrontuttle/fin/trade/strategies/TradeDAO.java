package com.myrontuttle.fin.trade.strategies;

import java.util.List;

import javax.persistence.EntityManager;

public class TradeDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public void destroy() {
		em.close();
	}
	
	public void saveTrade(Trade t) {
		em.persist(t);
	}
	
	public Trade updateTrade(Trade t) {
		return em.merge(t);
	}

	// Get the trade based on a tradeId
	public Trade findTrade(String tradeId) {
		return em.find(Trade.class, tradeId);
	}
	
	public boolean tradeExists(String tradeId) {
		return em.createQuery(
				"SELECT t FROM Trades t WHERE t.tradeId = :tradeId", 
				Trade.class).setParameter("tradeId", tradeId).getResultList().size() == 1;
	}

	public List<Trade> findTrades(String userId) {
		return em.createQuery(
				"SELECT t FROM Trades t WHERE t.userId = :userId", 
				Trade.class).setParameter("userId", userId).getResultList();
	}
	
	public List<Trade> findTradesWithEvent(String event) {
		return em.createQuery("SELECT t FROM Trades t, IN(t.TradeEvents) e WHERE e.event = :event",
				Trade.class).setParameter("event", event).getResultList();
	}

	public void removeTrade(String tradeId) {
		em.remove(em.find(Trade.class, tradeId));
	}
	
	public List<Event> findEvents(String event) {

		// Retrieve events from database
		return em.createQuery(
				"SELECT e FROM Events e WHERE e.event = :event", 
				Event.class).setParameter("event", event).getResultList();
	}

	public List<Event> findEventsWithTrigger(String trigger) {
		// Retrieve events from database
		return em.createQuery(
				"SELECT e FROM Events e WHERE e.trigger = :trigger", 
				Event.class).setParameter("trigger", trigger).getResultList();
	}
}
