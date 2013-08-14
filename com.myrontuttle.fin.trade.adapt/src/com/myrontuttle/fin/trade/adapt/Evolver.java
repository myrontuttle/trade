package com.myrontuttle.fin.trade.adapt;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.myrontuttle.evolve.*;
import com.myrontuttle.evolve.factories.IntArrayFactory;
import com.myrontuttle.evolve.operators.IntArrayCrossover;
import com.myrontuttle.evolve.operators.IntArrayMutation;
import com.myrontuttle.evolve.operators.EvolutionPipeline;
import com.myrontuttle.evolve.selection.RouletteWheelSelection;
import com.myrontuttle.evolve.termination.*;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.PortfolioService;
import com.myrontuttle.fin.trade.api.ScreenerService;
import com.myrontuttle.fin.trade.api.WatchlistService;
import com.myrontuttle.fin.trade.tradestrategies.BasicTradeStrategy;

public class Evolver {
	
	private final ScreenerService screenerService;
	private final WatchlistService watchlistService;
	private final AlertService alertService;
	private final PortfolioService portfolioService;
	private final BasicTradeStrategy basicTradeStrategy;
	private final AlertReceiverService alertReceiver;
	
	private final EntityManager em;
	
	private final TerminationCondition[] terminationConditions = new TerminationCondition[]{
																		new UserAbort() };
	private final EvolutionObserver<int[]> dbObserver = new EvolutionObserver<int[]>() {
		public void populationUpdate(PopulationStats<? extends int[]> data) {
			// Use data to update group
			em.getTransaction().begin();
			GroupStats stats = new GroupStats(data.getPopulationId(), 
					findCandidateByGenome(data.getBestCandidate()).getCandidateId(), 
					data.getBestCandidateFitness(), data.getMeanFitness(), 
					data.getFitnessStandardDeviation(), data.getGenerationNumber());
			
			// Save group to database
			em.persist(stats);
			em.getTransaction().commit();
		}
	};
	
	Evolver(ScreenerService screenerService, 
			WatchlistService watchlistService,
			AlertService alertService, 
			PortfolioService portfolioService,
			BasicTradeStrategy basicTradeStrategy,
			AlertReceiverService alertReceiver,
			final EntityManager em) {
		this.screenerService = screenerService;
		this.watchlistService = watchlistService;
		this.alertService = alertService;
		this.portfolioService = portfolioService;
		this.basicTradeStrategy = basicTradeStrategy;
		this.alertReceiver = alertReceiver;
		this.em = em;
	}
	
	private Candidate findCandidateByGenome(int[] genome) {
		return em.createQuery(
				"SELECT c FROM Candidates c WHERE genomeString = :genomeString", 
					Candidate.class).
				setParameter("genomeString", Candidate.generateGenomeString(genome)).
				getSingleResult();
	}
	
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
		Group group = em.find(Group.class, groupId);
		if (group != null) {
			List<ExpressedCandidate<int[]>> candidates = findCandidatesInGroup(groupId);
			int size = candidates.size();
			int eliteCount = group.getEliteCount();

			BasicExpression<int[]> candidateExpression = new BasicExpression<int[]>(screenerService, 
																watchlistService, alertService, 
																portfolioService, basicTradeStrategy, 
																alertReceiver, em);
			 
			BasicEvaluator strategyEvaluator = new BasicEvaluator(portfolioService);

			EvolutionEngine<int[]> engine = createEngine(candidateExpression.getTotalGeneLength(group), 
															BasicExpression.UPPER_BOUND, 
															group.getMutationFactor(),
															candidateExpression, strategyEvaluator);
			

			engine.evolveToExpression(candidates, groupId, size, eliteCount, 
										terminationConditions);
		} else {
			throw new Exception("Group '" + groupId + "' doesn't exist");
		}
	}
	
	// Retrieve all groups
	public List<Group> findGroups() {
		return em.createQuery(
				"SELECT g FROM Groups g", Group.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	// Retrieve candidates from database
	public List<ExpressedCandidate<int[]>> findCandidatesInGroup(String groupId) {
		Query query = em.createQuery(
				"SELECT c FROM Candidates c WHERE groupId = :groupId", 
				Candidate.class).setParameter("groupId", groupId);
		
		return (List<ExpressedCandidate<int[]>>) query.getResultList();
	}

	// Retrieve group stats from database
	public List<GroupStats> findGroupStats(String groupId) {
		return em.createQuery(
				"SELECT s FROM GroupStats s WHERE groupId = :groupId", 
				GroupStats.class).setParameter("groupId", groupId).getResultList();
	}
	
	public void abort(String groupId) {
		((UserAbort)terminationConditions[0]).abort();
	}
}
