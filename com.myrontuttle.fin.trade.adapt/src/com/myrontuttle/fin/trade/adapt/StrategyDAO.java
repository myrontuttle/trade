package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.sci.evolve.PopulationStats;

public interface StrategyDAO {

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
	
	public void updateGroupStats(PopulationStats<? extends int[]> data);
	
	public List<GroupStats> findStatsForGroup(String groupId);
	
	public GroupStats findStats(String statsId);
	
	public void removeStats(String statsId);
}
