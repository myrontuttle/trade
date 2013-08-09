package com.myrontuttle.fin.trade.adapt;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;

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

	private final EntityManager em;
	
	Evolver(ExpressionStrategy<int[]> traderExpression, 
				BasicEvaluator strategyEvaluator,
				final EntityManager em) {
		this.em = em;

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
				// Use data to update group
				em.getTransaction().begin();
				Group group = em.find(Group.class, data.getPopulationId());
				group.setBestCandidateId(
						findCandidateByGenome(data.getBestCandidate()).getCandidateId());
				group.setBestCandidateFitness(data.getBestCandidateFitness());
				group.setMeanFitness(data.getMeanFitness());
				group.setFitnessStandardDeviation(data.getFitnessStandardDeviation());
				group.setGenerationNumber(data.getGenerationNumber());
				
				// Save group to database
				em.persist(group);
				em.getTransaction().commit();
				
				System.out.printf("Generation %d: %s\n",
				                   data.getGenerationNumber(),
				                   data.getBestCandidateFitness());
			}
		});
	}
	
	private Candidate findCandidateByGenome(int[] genome) {
		return em.createQuery(
				"SELECT c FROM Candidates c WHERE genomeString = :genomeString", 
					Candidate.class).
				setParameter("genomeString", Candidate.generateGenomeString(genome)).
				getSingleResult();
	}
	
	public void evolveExistingOnce(String groupId) throws Exception {
		Group group = em.find(Group.class, groupId);
		if (group != null) {
			List<ExpressedCandidate<int[]>> candidates = findCandidatesInGroup(groupId);
			int size = candidates.size();
			int eliteCount = group.getEliteCount();

			engine.evolveToExpression(candidates, groupId, size, eliteCount, 
										terminationConditions);
		} else {
			throw new Exception("Group '" + groupId + "' doesn't exist");
		}
	}
	
	
	public Group newTradeGroup(String alertAddress, int size, int eliteCount) {

		em.getTransaction().begin();
		Group group = new Group();
		group.setSize(size);
		group.setEliteCount(eliteCount);
		group.setAlertAddress(alertAddress);
		em.persist(group);
		em.getTransaction().commit();
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(group);
		
		return em.find(Group.class, objId);
	}

	@SuppressWarnings("unchecked")
	// Retrieve candidates from database
	public List<ExpressedCandidate<int[]>> findCandidatesInGroup(String groupId) {
		Query query = em.createQuery(
				"SELECT c FROM Candidates c WHERE groupId = :groupId", 
				Candidate.class).setParameter("groupId", groupId);
		
		return (List<ExpressedCandidate<int[]>>) query.getResultList();
	}
	
	public void abort(String groupId) {
		((UserAbort)terminationConditions[0]).abort();
	}
}
