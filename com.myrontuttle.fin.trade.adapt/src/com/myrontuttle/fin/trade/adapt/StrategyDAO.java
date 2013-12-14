package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.sci.evolve.PopulationStats;

public interface StrategyDAO {

	public Candidate newCandidateRecord(int[] genome, String populationId, double startingCash);
	
	public List<Candidate> findCandidatesInGroup(String groupId);
	
	public Candidate findCandidateByGenome(int[] genome);
	
	public Candidate findCandidate(String candidateId);
	
	public void saveCandidate(Candidate candidate);
	
	public void removeCandidate(Candidate candidate);
	
	public Group newGroupRecord() ;
	
	public Group findGroup(String groupId);
	
	public List<Group> findGroups();
	
	public void updateGroupStats(PopulationStats<? extends int[]> data);
	
	public List<GroupStats> findStatsForGroup(String groupId);
	
	public GroupStats findStats(String statsId);
	
	public void saveGroup(Group group);
	
	public void removeGroup(Group group);
}
