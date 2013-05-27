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

public class TradeStrategyEvolver {
	
	private static final double MUTATION_FACTOR = 0.02;
	private static final int STAGNATION_GENERATIONS = 20;
	
	private final EvolutionEngine<int[]> engine;
	private final TerminationCondition[] terminationConditions;
	
	private final ExpressionStrategy<int[]> traderExpression;
	
	TradeStrategyEvolver(ExpressionStrategy<int[]> traderExpression, 
				TradeStrategyEvaluator strategyEvaluator) {
		this.traderExpression = traderExpression;

		// Setup how strategy candidates are created
		IntArrayFactory candidateFactory = 
				new IntArrayFactory(BasicTradeStrategyExpression.TOTAL_GENE_LENGTH, 
									BasicTradeStrategyExpression.UPPER_BOUND);
		
		// Setup how strategies are evolved
		List<EvolutionaryOperator<int[]>> operators = new LinkedList<EvolutionaryOperator<int[]>>();
		operators.add(new IntArrayCrossover());
		operators.add(new IntArrayMutation(candidateFactory, MUTATION_FACTOR));
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
		this.terminationConditions = new TerminationCondition[]{
			new UserAbort(),
			new Stagnation(STAGNATION_GENERATIONS, strategyEvaluator.isNatural())
		};
		
		// Add observer for the evolution
		engine.addEvolutionObserver(new EvolutionObserver<int[]>() {
			public void populationUpdate(PopulationStats<? extends int[]> data) {
				System.out.printf("Generation %d: %s\n",
				                   data.getGenerationNumber(),
				                   Arrays.toString(data.getBestCandidate()));
			}
		});
	}
	
	public void evolveOnce(String groupId) {
		ExpressedPopulation<int[]> pop = traderExpression.importPopulation(groupId);
		int eliteCount = pop.getEliteCount();
		int size = pop.getPopulationSize();
		engine.evolveToExpression(pop, size, eliteCount, terminationConditions);
	}
	
	public void abort(String groupId) {
		((UserAbort)terminationConditions[0]).abort();
	}
}
