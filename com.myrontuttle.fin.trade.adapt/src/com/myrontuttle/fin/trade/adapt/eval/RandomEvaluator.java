package com.myrontuttle.fin.trade.adapt.eval;

import java.util.List;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.evolve.ExpressedFitnessEvaluator;

/**
 * Assigns strategy candidates a random number between 0 and 1.0
 * @author Myron Tuttle
 */
public class RandomEvaluator implements ExpressedFitnessEvaluator<int[]> {

	/**
	 * Higher values indicate better fitness
	 */
	@Override
	public boolean isNatural() {
		return true;
	}

	@Override
	public double getFitness(ExpressedCandidate<int[]> candidate,
			List<ExpressedCandidate<int[]>> population) {
		
		return Math.random();
	}
}
