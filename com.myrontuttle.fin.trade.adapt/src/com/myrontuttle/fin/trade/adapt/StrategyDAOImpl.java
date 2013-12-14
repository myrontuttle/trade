package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import com.myrontuttle.sci.evolve.PopulationStats;

public class StrategyDAOImpl implements StrategyDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	// Create a record in the database for the candidate to get a fresh id
	public Candidate newCandidateRecord(int[] genome, String populationId, double startingCash) {
		Candidate cand = new Candidate();
		cand.setGenomeString(Candidate.generateGenomeString(genome));
		cand.setStartingCash(startingCash);
		cand.setGroupId(populationId);
		em.persist(cand);
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(cand);
		return em.find(Candidate.class, objId);
	}

	// Retrieve a candidate based on it's ID
	public Candidate findCandidate(String candidateId) {
		return em.createQuery(
				"SELECT c FROM Candidates c WHERE c.candidateId = :candidateId", 
				Candidate.class).setParameter("candidateId", candidateId).getSingleResult();
	}

	// Retrieve candidates from database
	public List<Candidate> findCandidatesInGroup(String groupId) {
		return em.createQuery(
				"SELECT c FROM Candidates c WHERE c.groupId = :groupId", 
				Candidate.class).setParameter("groupId", groupId).getResultList();
	}
	
	public Candidate findCandidateByGenome(int[] genome) {
		return em.createQuery(
				"SELECT c FROM Candidates c WHERE c.genomeString = :genomeString", 
					Candidate.class).
				setParameter("genomeString", Candidate.generateGenomeString(genome)).
				getSingleResult();
	}

	public void saveCandidate(Candidate candidate) {
		em.persist(candidate);
	}
	
	public void removeCandidate(Candidate candidate) {
		em.remove(candidate);
	}
	
	public Group newGroupRecord() {
		Group group = new Group();

		em.persist(group);
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(group);
		return em.find(Group.class, objId);
	}

	// Get the group based on a groupId
	public Group findGroup(String groupId) {
		return em.createQuery(
				"SELECT g FROM Groups g WHERE g.groupId = :groupId", 
				Group.class).setParameter("groupId", groupId).getSingleResult();
	}

	// Retrieve all groups
	public List<Group> findGroups() {
		return em.createQuery(
				"SELECT g FROM Groups g", Group.class).getResultList();
	}
	
	public void updateGroupStats(PopulationStats<? extends int[]> data) {
		GroupStats stats = new GroupStats(data.getPopulationId(), 
				findCandidateByGenome(data.getBestCandidate()).getCandidateId(), 
				data.getBestCandidateFitness(), data.getMeanFitness(), 
				data.getFitnessStandardDeviation(), data.getGenerationNumber());
		
		// Save group to database
		em.persist(stats);
	}

	// Retrieve group stats from database
	public List<GroupStats> findStatsForGroup(String groupId) {
		return em.createQuery(
				"SELECT s FROM GroupStats s WHERE s.groupId = :groupId", 
				GroupStats.class).setParameter("groupId", groupId).getResultList();
	}

	// Get the groupstats based on a statsId
	public GroupStats findStats(String statsId) {
		return em.createQuery(
				"SELECT s FROM GroupStats s WHERE s.statsId = :statsId", 
				GroupStats.class).setParameter("statsId", statsId).getSingleResult();
	}

	
	public void saveGroup(Group group) {
		em.persist(group);
	}
	
	public void removeGroup(Group group) {
		em.remove(group);
	}
}
