package com.myrontuttle.fin.trade.adapt;

import java.util.List;

import com.myrontuttle.sci.evolve.ExpressedCandidate;
import com.myrontuttle.sci.evolve.PopulationStats;

public interface StrategyDAO {

	public Candidate newCandidateRecord(int[] genome, String populationId, double startingCash);
	
	public List<ExpressedCandidate<int[]>> findCandidatesInGroup(String groupId);
	
	public Candidate findCandidateByGenome(int[] genome);
	
	public void saveCandidate(Candidate candidate);
	
	public void removeCandidate(Candidate candidate);
	
	public Group newGroupRecord() ;
	
	public Group findGroup(String groupId);
	
	public List<Group> findGroups();
	
	public void updateGroupStats(PopulationStats<? extends int[]> data);
	
	public List<GroupStats> findGroupStats(String groupId);
	
	public void saveGroup(Group group);
	
	public void removeGroup(Group group);
}
