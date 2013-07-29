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

public class Evolver {
	
	private static final double MUTATION_FACTOR = 0.02;
	private static final int STAGNATION_GENERATIONS = 20;
	
	private final EvolutionEngine<int[]> engine;
	private final TerminationCondition[] terminationConditions;
	
	private final ExpressionStrategy<int[]> traderExpression;
	
	Evolver(ExpressionStrategy<int[]> traderExpression, 
				Evaluator strategyEvaluator) {
		this.traderExpression = traderExpression;

		// Setup how strategy candidates are created
		IntArrayFactory candidateFactory = 
				new IntArrayFactory(BasicExpression.TOTAL_GENE_LENGTH, 
									BasicExpression.UPPER_BOUND);
		
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
				//TODO: Use data to update group
				//TODO: Save group to database
				System.out.printf("Generation %d: %s\n",
				                   data.getGenerationNumber(),
				                   Arrays.toString(data.getBestCandidate()));
			}
		});
	}
	
	public void evolveOnce(String groupId) {
		//TODO: Check if groupId exists, if not, create new group
		List<ExpressedCandidate<int[]>> candidates = null;
		int eliteCount = 0;
		int size = 10;
		if (groupId != null) {
			candidates = importCandidates(groupId);
			eliteCount = importEliteCount(groupId);
			size = candidates.size();
		}
		
		engine.evolveToExpression(candidates, groupId, size, eliteCount, terminationConditions);
	}
	
	public static String getAlertAddress(String groupId) {
		//TODO Get the email associated with this groupId from the database
		return null;
	}
	
	public int importEliteCount(String groupId) {
		//TODO: Retrieve elite count for a group
		// SELECT * FROM Candidate WHERE groupId=groupId
		return 0;
	}

	public List<ExpressedCandidate<int[]>> importCandidates(String groupId) {
		//TODO: Retrieve population from database
/*
		long startTime = Long.parseLong(fileName.substring(
											fileName.indexOf(TIME_MARKER) 
											+ TIME_MARKER.length(), 
											fileName.indexOf(GEN_MARKER)));

		int iterationNumber = Integer.parseInt(fileName.substring(
											fileName.indexOf(GEN_MARKER) 
											+ GEN_MARKER.length(), 
											fileName.indexOf(FILE_EXT)));
		
		try {
			// Open file to read in population
			BufferedReader input =  new BufferedReader(new FileReader(fileName));
			try {

				// Read in population info
				String line = input.readLine();

				int size = Integer.parseInt(line.substring(
												line.indexOf(SIZE_MARKER) 
													+ SIZE_MARKER.length()), 
													line.indexOf(FIT_MARKER));
				
				boolean naturalFitness = Boolean.parseBoolean(line.substring(
												line.indexOf(FIT_MARKER) 
													+ FIT_MARKER.length(), 
												line.indexOf(ELITE_MARKER)));

				int eliteCount = Integer.parseInt(line.substring(
												line.indexOf(ELITE_MARKER) 
													+ ELITE_MARKER.length()));

				// Create list of expressed candidates based on file
				List<ExpressedCandidate<int[]>> expressedPopulation = 
						new ArrayList<ExpressedCandidate<int[]>>(size);
		        while (( line = input.readLine()) != null){
		        	expressedPopulation.add(TradeCandidate.fromString(line));
		        }

				// Create new Expressed Population Stats
		        return new ExpressedPopulation<int[]>(
						 						expressedPopulation,
						 						naturalFitness,
						 						expressedPopulation.size(),
						 						eliteCount,
						 						iterationNumber,
						 						startTime);
		      }
		      finally {
		        input.close();
		      }
		    }
		    catch (IOException ex){
		      ex.printStackTrace();
		    }
		    
		*/
		return null;
	}
	
	public void abort(String groupId) {
		((UserAbort)terminationConditions[0]).abort();
	}
}
