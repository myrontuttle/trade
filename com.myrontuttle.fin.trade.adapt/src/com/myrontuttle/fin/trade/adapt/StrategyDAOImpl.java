package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import com.myrontuttle.sci.evolve.ExpressedCandidate;
import com.myrontuttle.sci.evolve.PopulationStats;

public class StrategyDAOImpl implements StrategyDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// Create a record in the database for the candidate to get a fresh id
	public Candidate newCandidateRecord(int[] genome, String populationId, double startingCash) {
		em.getTransaction().begin();
		Candidate cand = new Candidate();
		cand.setGenomeString(Candidate.generateGenomeString(genome));
		cand.setStartingCash(startingCash);
		cand.setGroupId(populationId);
		em.persist(cand);
		em.getTransaction().commit();
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(cand);
		return em.find(Candidate.class, objId);
	}

	@SuppressWarnings("unchecked")
	// Retrieve candidates from database
	public List<ExpressedCandidate<int[]>> findCandidatesInGroup(String groupId) {
		Query query = em.createQuery(
				"SELECT c FROM Candidates c WHERE groupId = :groupId", 
				Candidate.class).setParameter("groupId", groupId);
		
		return (List<ExpressedCandidate<int[]>>) query.getResultList();
	}
	
	public Candidate findCandidateByGenome(int[] genome) {
		return em.createQuery(
				"SELECT c FROM Candidates c WHERE genomeString = :genomeString", 
					Candidate.class).
				setParameter("genomeString", Candidate.generateGenomeString(genome)).
				getSingleResult();
	}

	public void saveCandidate(Candidate candidate) {
		em.getTransaction().begin();
		em.persist(candidate);
		em.getTransaction().commit();
	}
	
	public void removeCandidate(Candidate candidate) {
		em.getTransaction().begin();
		em.remove(candidate);
		em.getTransaction().commit();
	}
	
	public Group newGroupRecord() {
		em.getTransaction().begin();
		Group group = new Group();

		em.persist(group);
		em.getTransaction().commit();
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(group);
		return em.find(Group.class, objId);
	}

	// Get the group based on a groupId
	public Group findGroup(String groupId) {
		return em.createQuery(
				"SELECT g FROM Groups g WHERE groupId = :groupId", 
				Group.class).setParameter("groupId", groupId).getSingleResult();
	}

	// Retrieve all groups
	public List<Group> findGroups() {
		return em.createQuery(
				"SELECT g FROM Groups g", Group.class).getResultList();
	}
	
	public void updateGroupStats(PopulationStats<? extends int[]> data) {
		em.getTransaction().begin();
		GroupStats stats = new GroupStats(data.getPopulationId(), 
				findCandidateByGenome(data.getBestCandidate()).getCandidateId(), 
				data.getBestCandidateFitness(), data.getMeanFitness(), 
				data.getFitnessStandardDeviation(), data.getGenerationNumber());
		
		// Save group to database
		em.persist(stats);
		em.getTransaction().commit();
	}

	// Retrieve group stats from database
	public List<GroupStats> findGroupStats(String groupId) {
		return em.createQuery(
				"SELECT s FROM GroupStats s WHERE groupId = :groupId", 
				GroupStats.class).setParameter("groupId", groupId).getResultList();
	}
	
	public void saveGroup(Group group) {
		em.getTransaction().begin();
		em.persist(group);
		em.getTransaction().commit();
	}
	
	public void removeGroup(Group group) {
		em.getTransaction().begin();
		em.remove(group);
		em.getTransaction().commit();
	}
}
