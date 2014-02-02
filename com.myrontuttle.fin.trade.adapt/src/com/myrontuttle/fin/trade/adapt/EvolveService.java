package com.myrontuttle.fin.trade.adapt;

import org.joda.time.DateTime;

/**
 * Service for evolving groups of candidate strategies 
 * @author Myron Tuttle
 */
public interface EvolveService {
	
	public void createInitialCandidates(String groupId);
	
	public void evolveNow(String groupId);
	
	public void evolveAllNow();
	
	public void evolveActiveAt(DateTime date);
	
	public DateTime getNextEvolveDate();
	
	public boolean stopEvolving();

}
