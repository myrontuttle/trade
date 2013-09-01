package com.myrontuttle.fin.trade.adapt.express;

import java.util.List;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.evolve.ExpressionStrategy;
import com.myrontuttle.fin.trade.adapt.Candidate;

public class NoExpression implements ExpressionStrategy<int[]> {

	@Override
	public ExpressedCandidate<int[]> express(int[] candidate,
			String populationId) {
		return new Candidate("C", populationId, candidate, "", 0.0);
	}

	@Override
	public void candidatesExpressed(
			List<ExpressedCandidate<int[]>> expressedCandidates) {
		// TODO Auto-generated method stub
		
	}

}
