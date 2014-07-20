package com.myrontuttle.fin.trade.adapt;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.myrontuttle.sci.evolve.api.PopulationStats;

public class AdaptDAOImpl implements AdaptDAO {

	private EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public void saveGroup(Group group) {
		em.persist(group);
	}
	
	// Get the group based on a groupId
	public Group findGroup(long groupId) {
		return em.find(Group.class, groupId);
	}

	// Retrieve all groups
	public List<Group> findGroups() {
		return em.createQuery(
				"SELECT g FROM GROUPS g", Group.class).getResultList();
	}
	
	public Group updateGroup(Group group) {
		return em.merge(group);
	}
	
	public void removeGroup(long groupId) {
		em.remove(em.find(Group.class, groupId));
	}
	
	public void addCandidate(Candidate candidate, long groupId) {
		Group group = em.find(Group.class, groupId);
		group.addCandidate(candidate);
	}

	// Retrieve a candidate based on it's ID
	public Candidate findCandidate(long candidateId) {
		return em.find(Candidate.class, candidateId);
	}

	// Retrieve candidates from database
	public List<Candidate> findCandidatesInGroup(long groupId) {
		return em.createQuery(
				"SELECT c FROM CANDIDATES c WHERE c.groupId = :groupId", 
				Candidate.class).setParameter("groupId", groupId).getResultList();
	}
	
	public Candidate findCandidateByGenome(int[] genome) throws Exception {
		List<Candidate> candidates = em.createQuery(
				"SELECT c FROM CANDIDATES c WHERE c.genomeString = :genomeString", 
					Candidate.class).
				setParameter("genomeString", Candidate.generateGenomeString(genome)).
				getResultList();
		if (candidates.size() > 0) {
			Candidate c = candidates.get(0);
			c.setGenome(genome);
			return c;
		} else {
			throw new Exception("No candidate found with genome: " + Arrays.toString(genome));
		}
	}
	
	public Candidate updateCandidate(Candidate candidate) {
		return em.merge(candidate);
	}
	
	public void removeCandidate(long candidateId) {
		Candidate candidate = em.find(Candidate.class, candidateId);
		Group group = em.find(Group.class, candidate.getGroupId());
		group.removeCandidate(candidate);
		em.remove(candidate);
	}

	public void removeAllCandidates(long groupId) {
		Group group = em.find(Group.class, groupId);
		for (Candidate c : group.getCandidates()) {
			em.remove(c);
		}
		group.getCandidates().clear();
	}
	
	public void updateGroupStats(PopulationStats<? extends int[]> data) {

		Group group = em.find(Group.class, data.getPopulationId());
		
		GroupStats stats = new GroupStats(
				data.getPopulationId(), 
				data.getBestCandidateFitness(), data.getMeanFitness(), 
				data.getFitnessStandardDeviation(), data.getGenerationNumber(),
				group.getDouble("Express.Variability"));
		
		group.setInteger("Evolve.Generation", data.getGenerationNumber());

		group.addGroupStats(stats);
	}

	// Retrieve group stats from database
	public List<GroupStats> findStatsForGroup(long groupId) {
		return em.createQuery(
				"SELECT s FROM GROUP_STATS s WHERE s.groupId = :groupId", 
				GroupStats.class).setParameter("groupId", groupId).getResultList();
	}

	// Get the groupstats based on a statsId
	public GroupStats findStats(long statsId) {
		return em.find(GroupStats.class, statsId);
	}
	
	public void removeStats(long statsId) {
		GroupStats stats = em.find(GroupStats.class, statsId);
		Group group = em.find(Group.class, stats.getGroupId());
		group.removeStats(stats);
		em.remove(stats);
	}

	@Override
	public void removeAllStats(long groupId) {
		Group group = em.find(Group.class, groupId);
		for (GroupStats gs : group.getStats()) {
			em.remove(gs);
		}
		group.getStats().clear();
	}
	
	@Override
	public void setBestTrader(Trader trader, long groupId) {
		Group group = em.find(Group.class, groupId);		
		group.setBestTrader(trader);		
	}

	@Override
	public Trader updateTrader(Trader trader) {
		return em.merge(trader);
	}

	// Get the trader based on its traderId
	public Trader findTrader(long traderId) {
		return em.find(Trader.class, traderId);
	}

	@Override
	public Trader getBestTrader(long groupId) {
		try {
			return em.createQuery(
					"SELECT t FROM TRADERS t WHERE t.groupId = :groupId", 
					Trader.class).setParameter("groupId", groupId).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	public void removeTrader(long traderId) {
		Trader trader = em.find(Trader.class, traderId);
		Group group = em.find(Group.class, trader.getGroupId());
		group.removeBestTrader(trader);
		em.remove(trader);
	}

	@Override
	public void addSavedScreen(SavedScreen screen, long traderId) {
		Trader trader = em.find(Trader.class, traderId);
		trader.addScreen(screen);
	}

	@Override
	public void addSymbol(String symbol, long traderId) {
		Trader trader = em.find(Trader.class, traderId);
		trader.addSymbol(symbol);
	}

	@Override
	public void addSavedAlert(SavedAlert alert, long traderId) {
		Trader trader = em.find(Trader.class, traderId);
		trader.addAlert(alert);
	}

	@Override
	public void addTradeParamter(TradeParameter instruction,
			long traderId) {
		Trader trader = em.find(Trader.class, traderId);
		trader.addTradeInstruction(instruction);
	}

	@Override
	public List<SavedScreen> findScreensForTrader(long traderId) {
		return em.createQuery(
				"SELECT s FROM SAVED_SCREENS s WHERE s.traderId = :traderId", 
				SavedScreen.class).setParameter("traderId", traderId).getResultList();
	}

	@Override
	public List<String> findSymbolsForTrader(long traderId) {
		Trader trader = em.find(Trader.class, traderId);
		return trader.getSymbols();
	}

	@Override
	public List<SavedAlert> findAlertsForTrader(long traderId) {
		return em.createQuery(
				"SELECT a FROM SAVED_ALERTS a WHERE a.traderId = :traderId", 
				SavedAlert.class).setParameter("traderId", traderId).getResultList();
	}

	@Override
	public List<TradeParameter> findParametersForTrader(long traderId) {
		return em.createQuery(
				"SELECT t FROM TRADE_INSTRUCTIONS t WHERE t.traderId = :traderId", 
				TradeParameter.class).setParameter("traderId", traderId).getResultList();
	}

	@Override
	public SavedScreen findScreen(long savedScreenId) {
		return em.find(SavedScreen.class, savedScreenId);
	}

	@Override
	public SavedAlert findAlert(long savedAlertId) {
		return em.find(SavedAlert.class, savedAlertId);
	}

	@Override
	public TradeParameter findTradeParameter(long tradeParameterId) {
		return em.find(TradeParameter.class, tradeParameterId);
	}
}
