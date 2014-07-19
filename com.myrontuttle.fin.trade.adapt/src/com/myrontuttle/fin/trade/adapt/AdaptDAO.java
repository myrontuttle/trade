package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.sci.evolve.api.PopulationStats;

public interface AdaptDAO {

	public void saveGroup(Group group);
	
	public Group findGroup(long groupId);
	
	public List<Group> findGroups();
	
	public Group updateGroup(Group group);
	
	public void removeGroup(long groupId);
	
	public void addCandidate(Candidate candidate, long groupId);
	
	public List<Candidate> findCandidatesInGroup(long groupId);
	
	public Candidate findCandidateByGenome(int[] genome) throws Exception;
	
	public Candidate findCandidate(long candidateId);
	
	public Candidate updateCandidate(Candidate candidate);
	
	public void removeCandidate(long candidateId);
	
	public void removeAllCandidates(long groupId);
	
	public void updateGroupStats(PopulationStats<? extends int[]> data);
	
	public List<GroupStats> findStatsForGroup(long groupId);
	
	public GroupStats findStats(long statsId);
	
	public void removeStats(long statsId);
	
	public void removeAllStats(long groupId);
	
	public void setBestTrader(Trader trader, long groupId);
	
	public Trader updateTrader(Trader trader);
	
	public Trader findTrader(long traderId);
	
	public Trader getBestTrader(long groupId);
	
	public void removeTrader(long traderId);

	public void addSavedScreen(SavedScreen screen, long traderId);
	
	public List<SavedScreen> findScreensForTrader(long traderId);
	
	public SavedScreen findScreen(long savedScreenId);

	public void addSymbol(String symbol, long traderId);
	
	public List<String> findSymbolsForTrader(long traderId);
	
	public List<SavedAlert> findAlertsForTrader(long traderId);
	
	public SavedAlert findAlert(long savedAlertId);
	
	public List<TradeParameter> findParametersForTrader(long traderId);
	
	public TradeParameter findTradeParameter(long tradeParameterId);

	public void addSavedAlert(SavedAlert alert, long traderId);
	
	public void addTradeParamter(TradeParameter parameter, long traderId);
}
