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
	
	public void setBestCandidate(long candidateId, long groupId);
	
	public Candidate getBestCandidate(long groupId);

	public void addSavedScreen(SavedScreen screen, long candidateId);
	
	public List<SavedScreen> findScreensForCandidate(long candidateId);
	
	public SavedScreen findScreen(long savedScreenId);
	
	public void removeSavedScreens(long candidateId);

	public void addSymbol(String symbol, long candidateId);
	
	public List<String> findSymbolsForCandidate(long candidateId);
	
	public void removeSymbols(long candidateId);

	public void addSavedAlert(SavedAlert alert, long candidateId);
	
	public List<SavedAlert> findAlertsForCandidate(long candidateId);
	
	public SavedAlert findAlert(long savedAlertId);
	
	public void removeSavedAlerts(long candidateId);
	
	public void addTradeParameter(TradeParameter parameter, long candidateId);
	
	public List<TradeParameter> findParametersForCandidate(long candidateId);
	
	public TradeParameter findTradeParameter(long tradeParameterId);
	
	public void removeTradeParameters(long candidateId);
}
