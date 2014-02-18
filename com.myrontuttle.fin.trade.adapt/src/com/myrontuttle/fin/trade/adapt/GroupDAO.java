package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.sci.evolve.api.PopulationStats;

public interface GroupDAO {

	public void saveGroup(Group group);
	
	public Group findGroup(String groupId);
	
	public List<Group> findGroups();
	
	public Group updateGroup(Group group);
	
	public void removeGroup(String groupId);
	
	public void addCandidate(Candidate candidate, String groupId);
	
	public List<Candidate> findCandidatesInGroup(String groupId);
	
	public Candidate findCandidateByGenome(int[] genome);
	
	public Candidate findCandidate(String candidateId);
	
	public Candidate updateCandidate(Candidate candidate);
	
	public void removeCandidate(String candidateId);
	
	public void removeAllCandidates(String groupId);
	
	public void updateGroupStats(PopulationStats<? extends int[]> data);
	
	public List<GroupStats> findStatsForGroup(String groupId);
	
	public GroupStats findStats(String statsId);
	
	public void removeStats(String statsId);
	
	public void removeAllStats(String groupId);
	
	public void setBestTrader(Trader trader, String groupId);
	
	public Trader updateTrader(Trader trader);
	
	public Trader findTrader(String traderId);
	
	public Trader getBestTrader(String groupId);
	
	public void removeTrader(String traderId);

	public void addSavedScreen(SavedScreen screen, String traderId);
	
	public List<SavedScreen> findScreensForTrader(String traderId);
	
	public SavedScreen findScreen(String savedScreenId);

	public void addSymbol(String symbol, String traderId);
	
	public List<String> findSymbolsForTrader(String traderId);
	
	public List<SavedAlert> findAlertsForTrader(String traderId);
	
	public SavedAlert findAlert(String savedAlertId);
	
	public List<TradeInstruction> findInstructionsForTrader(String traderId);
	
	public TradeInstruction findTradeInstruction(String tradeInstructionId);

	public void addSavedAlert(SavedAlert alert, String traderId);
	
	public void addTradeInstruction(TradeInstruction instruction, String traderId);
}
