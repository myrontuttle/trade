package com.myrontuttle.fin.trade.alertreceivers;

import java.util.List;

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
				"SELECT a FROM AlertReceivers a WHERE a.userId = :userId", 
				Receiver.class).setParameter("userId", userId).getResultList();
	}

	public void removeReceiver(String receiverId) {
		em.remove(em.find(Receiver.class, receiverId));
	}
}
