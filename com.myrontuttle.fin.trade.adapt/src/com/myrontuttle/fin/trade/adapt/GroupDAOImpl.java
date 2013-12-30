package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import javax.persistence.EntityManager;

//import org.apache.openjpa.persistence.OpenJPAEntityManager;
//import org.apache.openjpa.persistence.OpenJPAPersistence;

import com.myrontuttle.sci.evolve.PopulationStats;

public class GroupDAOImpl implements GroupDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public void saveGroup(Group group) {
		em.persist(group);
	}
	
	// Get the group based on a groupId
	public Group findGroup(String groupId) {
		return em.find(Group.class, groupId);
		/*return em.createQuery(
				"SELECT g FROM Groups g WHERE g.groupId = :groupId", 
				Group.class).setParameter("groupId", groupId).getSingleResult();
				*/
	}

	// Retrieve all groups
	public List<Group> findGroups() {
		return em.createQuery(
				"SELECT g FROM Groups g", Group.class).getResultList();
	}
	
	public Group updateGroup(Group group) {
		return em.merge(group);
	}
	
	public void removeGroup(String groupId) {
		em.remove(em.find(Group.class, groupId));
	}
	
	public void addCandidate(Candidate candidate, String groupId) {
		Group group = em.find(Group.class, groupId);
		group.addCandidate(candidate);
	}

	// Retrieve a candidate based on it's ID
	public Candidate findCandidate(String candidateId) {
		return em.find(Candidate.class, candidateId);
		/*return em.createQuery(
				"SELECT c FROM Candidates c WHERE c.candidateId = :candidateId", 
				Candidate.class).setParameter("candidateId", candidateId).getSingleResult();
				*/
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
	
	public Candidate updateCandidate(Candidate candidate) {
		return em.merge(candidate);
	}
	
	public void removeCandidate(String candidateId) {
		Candidate candidate = em.find(Candidate.class, candidateId);
		Group group = em.find(Group.class, candidate.getGroupId());
		group.removeCandidate(candidate);
		em.remove(candidate);
	}

	public void removeAllCandidates(String groupId) {
		Group group = findGroup(groupId);
		for (Candidate c : group.getCandidates()) {
			group.removeCandidate(c);
			em.remove(c);
		}
	}
	
	public void updateGroupStats(PopulationStats<? extends int[]> data) {
		GroupStats stats = new GroupStats(data.getPopulationId(), 
				findCandidateByGenome(data.getBestCandidate()).getCandidateId(), 
				data.getBestCandidateFitness(), data.getMeanFitness(), 
				data.getFitnessStandardDeviation(), data.getGenerationNumber());
		
		Group group = em.find(Group.class, data.getPopulationId());

		group.addGroupStats(stats);
	}

	// Retrieve group stats from database
	public List<GroupStats> findStatsForGroup(String groupId) {
		return em.createQuery(
				"SELECT s FROM GroupStats s WHERE s.groupId = :groupId", 
				GroupStats.class).setParameter("groupId", groupId).getResultList();
	}

	// Get the groupstats based on a statsId
	public GroupStats findStats(String statsId) {
		return em.find(GroupStats.class, statsId);
		/*return em.createQuery(
				"SELECT s FROM GroupStats s WHERE s.statsId = :statsId", 
				GroupStats.class).setParameter("statsId", statsId).getSingleResult();
				*/
	}
	
	public void removeStats(String statsId) {
		GroupStats stats = em.find(GroupStats.class, statsId);
		Group group = em.find(Group.class, stats.getGroupId());
		group.removeStats(stats);
		em.remove(stats);
	}

	@Override
	public void removeAllStats(String groupId) {
		Group group = findGroup(groupId);
		for (GroupStats gs : group.getStats()) {
			group.removeStats(gs);
			em.remove(gs);
		}
	}
}
