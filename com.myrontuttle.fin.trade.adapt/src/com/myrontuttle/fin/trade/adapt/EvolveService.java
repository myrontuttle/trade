package com.myrontuttle.fin.trade.adapt;

import org.joda.time.DateTime;

/**
 * Service for evolving groups of candidate strategies 
 * @author Myron Tuttle
 */
public interface EvolveService {
	
	public void setupGroup(GroupSettings settings);
	
	public void evolveNow(long groupId);
	
	public void evolveAllNow();
	
	public void evolveActiveAt(DateTime date);
	
	public DateTime getNextEvolveDate();
	
	public boolean stopEvolving();
	
	public void deleteCandidateExpression(long groupId, int[] candidateGenome);

	public void deleteGroupExpression(long groupId);
}
