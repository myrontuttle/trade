package com.myrontuttle.fin.trade.alertreceivers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;


public class ReceiverDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public void saveReceiver(AlertReceiver r) {
		em.persist(r);
	}
	
	public AlertReceiver updateReceiver(AlertReceiver r) {
		return em.merge(r);
	}

	// Get a receiver based on its receiverId
	public AlertReceiver findReceiver(String receiverId) {
		return em.find(AlertReceiver.class, receiverId);
	}

	public List<AlertReceiver> findReceivers(String userId) {
		return em.createQuery(
				"SELECT a FROM ALERT_RECEIVERS a WHERE a.userId = :userId", 
				AlertReceiver.class).setParameter("userId", userId).getResultList();
	}

	public void removeReceiver(String receiverId) {
		em.remove(em.find(AlertReceiver.class, receiverId));
	}
	
	public void addReceiverParameter(String receiverId, String name, String value) {
		AlertReceiver r = em.find(AlertReceiver.class, receiverId);
		r.addParameter(name, value);
	}
	
	public Map<String, String> getReceiverParameters(String receiverId) {
		AlertReceiver r = em.find(AlertReceiver.class, receiverId);
		return r.getParameters();
	}
	
	public void setReceiverActive(String receiverId, boolean isActive) {
		AlertReceiver r = em.find(AlertReceiver.class, receiverId);
		r.setActive(isActive);
	}
	
	public List<AlertReceiver> findActiveReceivers() {
		return em.createQuery(
				"SELECT a FROM ALERT_RECEIVERS a WHERE a.active = :active", 
				AlertReceiver.class).setParameter("active", true).getResultList();
	}
}
