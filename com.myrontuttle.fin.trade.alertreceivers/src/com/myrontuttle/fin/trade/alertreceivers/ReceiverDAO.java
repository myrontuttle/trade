package com.myrontuttle.fin.trade.alertreceivers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;


public class ReceiverDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public void saveReceiver(Receiver r) {
		em.persist(r);
	}
	
	public Receiver updateReceiver(Receiver r) {
		return em.merge(r);
	}

	// Get a receiver based on its receiverId
	public Receiver findReceiver(String receiverId) {
		return em.find(Receiver.class, receiverId);
	}

	public List<Receiver> findReceivers(String userId) {
		return em.createQuery(
				"SELECT a FROM Receivers a WHERE a.userId = :userId", 
				Receiver.class).setParameter("userId", userId).getResultList();
	}

	public void removeReceiver(String receiverId) {
		em.remove(em.find(Receiver.class, receiverId));
	}
	
	public void addReceiverParameter(String receiverId, String name, String value) {
		Receiver r = em.find(Receiver.class, receiverId);
		r.addParameter(name, value);
	}
	
	public Map<String, String> getReceiverParameters(String receiverId) {
		Receiver r = em.find(Receiver.class, receiverId);
		return r.getParameters();
	}
	
	public void setReceiverActive(String receiverId, boolean isActive) {
		Receiver r = em.find(Receiver.class, receiverId);
		r.setActive(isActive);
	}
	
	public List<Receiver> findActiveReceivers() {
		return em.createQuery(
				"SELECT a FROM Receivers a WHERE a.active = :active", 
				Receiver.class).setParameter("active", true).getResultList();
	}
}
