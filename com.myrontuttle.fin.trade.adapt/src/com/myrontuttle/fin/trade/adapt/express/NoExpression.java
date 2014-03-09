package com.myrontuttle.fin.trade.adapt.express;

import java.util.List;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;
import com.myrontuttle.sci.evolve.api.ExpressionStrategy;

public class NoExpression implements ExpressionStrategy<int[]> {

	@Override
	public ExpressedCandidate<int[]> express(int[] candidate,
			String populationId) {
		return new Candidate("C", populationId, candidate, "", 0.0);
	}

	@Override
	public void candidatesExpressed(
			List<ExpressedCandidate<int[]>> expressedCandidates, String populationId) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getGenomeLength(String populationId) {
		return 0;
	}

	@Override
	public void destroy(int[] candidate, String populationId) {
		// TODO Auto-generated method stub
		
	}

}
