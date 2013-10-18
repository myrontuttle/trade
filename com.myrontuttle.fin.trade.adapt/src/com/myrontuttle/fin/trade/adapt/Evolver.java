package com.myrontuttle.fin.trade.adapt;

import java.util.LinkedList;
import java.util.List;

import com.myrontuttle.fin.trade.adapt.eval.BasicEvaluator;
import com.myrontuttle.fin.trade.adapt.eval.RandomEvaluator;
import com.myrontuttle.fin.trade.adapt.express.BasicExpression;
import com.myrontuttle.fin.trade.adapt.express.NoExpression;
import com.myrontuttle.sci.evolve.*;
import com.myrontuttle.sci.evolve.factories.IntArrayFactory;
import com.myrontuttle.sci.evolve.operators.EvolutionPipeline;
import com.myrontuttle.sci.evolve.operators.IntArrayCrossover;
import com.myrontuttle.sci.evolve.operators.IntArrayMutation;
import com.myrontuttle.sci.evolve.selection.RouletteWheelSelection;
import com.myrontuttle.sci.evolve.termination.*;

public class Evolver {
	
	StrategyDAOImpl strategyDAOImpl = null;
	
	private final TerminationCondition[] terminationConditions = new TerminationCondition[]{
																		new UserAbort() };
	private final EvolutionObserver<int[]> dbObserver = new EvolutionObserver<int[]>() {
		public void populationUpdate(PopulationStats<? extends int[]> data) {
			// Use data to update group
			strategyDAOImpl.updateGroupStats(data);
		}
	};
	
	private EvolutionEngine<int[]> createEngine(int totalGeneLength, int geneUpperValueBound,
											double mutationFactor,
											ExpressionStrategy<int[]> candidateExpression, 
											ExpressedFitnessEvaluator<int[]> candidateEvaluator){

		// Setup how strategy candidates are created
		IntArrayFactory candidateFactory = new IntArrayFactory(totalGeneLength, geneUpperValueBound);
		
		
		// Setup how strategies are evolved
		List<EvolutionaryOperator<int[]>> operators = new LinkedList<EvolutionaryOperator<int[]>>();
		operators.add(new IntArrayCrossover());
		operators.add(new IntArrayMutation(candidateFactory, mutationFactor));
		EvolutionaryOperator<int[]> pipeline = new EvolutionPipeline<int[]>(operators);
		SelectionStrategy<Object> selection = new RouletteWheelSelection();

		// Strategy evolution engine
		EvolutionEngine<int[]> engine = new GenerationalEvolutionEngine<int[]>(candidateFactory,
		                                              pipeline,
		                                              candidateEvaluator,
		                                              candidateExpression,
		                                              selection,
		                                              RNG.getRNG(RNG.MARSENNETWISTER));
		
		// Add observer for the evolution
		engine.addEvolutionObserver(dbObserver);
		
		return engine;
	}
	
	public void evolveOnce(String groupId) throws Exception {
		Group group = strategyDAOImpl.findGroup(groupId);
		if (group != null) {
			List<ExpressedCandidate<int[]>> candidates = strategyDAOImpl.findCandidatesInGroup(groupId);
			int size = candidates.size();
			int eliteCount = group.getEliteCount();

			ExpressionStrategy<int[]> expressionStrategy = null;
			if (group.getExpressionStrategy().equals(Group.BASIC_EXPRESSION)) {
				expressionStrategy = new BasicExpression<int[]>();
			} else {
				expressionStrategy = new NoExpression();
			}
			
			ExpressedFitnessEvaluator<int[]> evaluator = null;
			if (group.getEvaluationStrategy().equals(Group.BASIC_EVALUATOR)) {
				evaluator = new BasicEvaluator();
			} else {
				evaluator = new RandomEvaluator();
			}

			EvolutionEngine<int[]> engine = createEngine(group.getGenomeLength(), 
															group.getGeneUpperValue(), 
															group.getMutationFactor(),
															expressionStrategy, evaluator);
			

			engine.evolveToExpression(candidates, groupId, size, eliteCount, 
										terminationConditions);
		} else {
			throw new Exception("Group '" + groupId + "' doesn't exist");
		}
	}
	
	public void abort(String groupId) {
		((UserAbort)terminationConditions[0]).abort();
	}
}
