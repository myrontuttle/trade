package com.myrontuttle.fin.trade.adapt;

import java.util.List;

/**
 * Service for evolving groups of candidate strategies 
 * @author Myron Tuttle
 */
public interface EvolveService {
	
	public void evolveNow(Group group);
	
	public void evolveNow(List<Group> groups);
	
	public void startEvolvingAt(int hourOfDayToEvolve);

}
