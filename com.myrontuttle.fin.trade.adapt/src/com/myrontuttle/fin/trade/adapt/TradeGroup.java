package com.myrontuttle.fin.trade.adapt;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.myrontuttle.evolve.*;
import com.myrontuttle.evolve.factories.IntArrayFactory;
import com.myrontuttle.evolve.operators.IntArrayCrossover;
import com.myrontuttle.evolve.operators.IntArrayMutation;
import com.myrontuttle.evolve.operators.EvolutionPipeline;
import com.myrontuttle.evolve.selection.RouletteWheelSelection;
import com.myrontuttle.evolve.termination.*;

public class TradeGroup {
	
	private final EvolutionEngine<int[]> engine;
	private final TerminationCondition[] terminationConditions;
	
	TradeGroup(BasicTradeStrategyExpression<int[]> traderExpression, 
				TradeCandidateEvaluator strategyEvaluator) {

		// Setup how strategy candidates are created
		IntArrayFactory candidateFactory = 
				new IntArrayFactory(BasicTradeStrategyExpression.TOTAL_GENE_LENGTH, 
									BasicTradeStrategyExpression.UPPER_BOUND);
		
		// Setup how strategies are evolved
		List<EvolutionaryOperator<int[]>> operators = new LinkedList<EvolutionaryOperator<int[]>>();
		operators.add(new IntArrayCrossover());
		operators.add(new IntArrayMutation(candidateFactory, 0.02));
		EvolutionaryOperator<int[]> pipeline = new EvolutionPipeline<int[]>(operators);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();

		// Strategy evolution engine
		engine = new GenerationalEvolutionEngine<int[]>(candidateFactory,
		                                              pipeline,
		                                              strategyEvaluator,
		                                              traderExpression,
		                                              selection,
		                                              RNG.getRNG(RNG.MARSENNETWISTER));
		
		// Create evolution termination conditions
		this.terminationConditions = new TerminationCondition[1];
		terminationConditions[0] = new Stagnation(20, strategyEvaluator.isNatural());
		
		// Add observer for the evolution
		engine.addEvolutionObserver(new EvolutionObserver<int[]>() {
			public void populationUpdate(PopulationStats<? extends int[]> data) {
				System.out.printf("Generation %d: %s\n",
				                   data.getGenerationNumber(),
				                   Arrays.toString(data.getBestCandidate()));
			}
		});
	}
	
	/**
	 * @param args
	 *
	 */
	public static void main(String[] args) {
		// Check if there are any existing populations on disk and evaluate them
		// Start any new populations
		// Close out
		
		
		TradeGroup tradeGroup = new TradeGroup(null, null);
		
		int[] result = tradeGroup.engine.evolve(10, 
				2, tradeGroup.terminationConditions);
		
		System.out.println(Arrays.toString(result));
	}
}
