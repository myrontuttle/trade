package com.myrontuttle.fin.trade.adapt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import com.myrontuttle.fin.trade.adapt.eval.RealizedGainEvaluator;
import com.myrontuttle.fin.trade.adapt.eval.RandomEvaluator;
import com.myrontuttle.fin.trade.adapt.express.SATExpression;
import com.myrontuttle.sci.evolve.api.*;
import com.myrontuttle.sci.evolve.engines.GenerationalEvolutionEngine;
import com.myrontuttle.sci.evolve.factories.IntArrayFactory;
import com.myrontuttle.sci.evolve.operators.EvolutionPipeline;
import com.myrontuttle.sci.evolve.operators.IntArrayCrossover;
import com.myrontuttle.sci.evolve.operators.IntArrayMutation;
import com.myrontuttle.sci.evolve.selection.RouletteWheelSelection;
import com.myrontuttle.sci.evolve.termination.*;
import com.myrontuttle.sci.evolve.util.RNG;

public class Evolver implements EvolveService {

	private final static int NUM_THREADS = 1;
	private final static int MINUTES_IN_HOUR = 60;
	final String EVOLVE_ACTIVE = "evolve_active";
	final String EVOLVE_HOUR = "evolve_hour";
	final String EVOLVE_MINUTE = "evolve_minute";
	
	private GroupDAO groupDAO;
	private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
	
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}
	
	private final TerminationCondition[] terminationConditions = new TerminationCondition[]{
																		new UserAbort() };
	/**
	 * The Evolution Observer is notified after the population is evaluated and sorted
	 * @param groupId
	 */
	private final EvolutionObserver<int[]> dbObserver = new EvolutionObserver<int[]>() {
		public void populationUpdate(PopulationStats<? extends int[]> data) {

			Group group = groupDAO.findGroup(data.getPopulationId());
			
			// Find the best candidate from the group
			Candidate bestCandidate = null;
			Trader trader = null;
			try {
				bestCandidate = groupDAO.findCandidateByGenome(data.getBestCandidate());
				
				// Remove the previous best trader (if one exists)
				Trader existingBest = group.getBestTrader();
				if (existingBest != null) {
					groupDAO.removeTrader(existingBest.getTraderId());
				}
				
				// Save the best trader
				trader = new Trader();
				trader.setGroupId(group.getGroupId());
				trader.setGenomeString(bestCandidate.getGenomeString());
				groupDAO.setBestTrader(trader, group.getGroupId());
				
			} catch (Exception e1) {
				System.out.println("Can't find best candidate with genome: " + 
										Arrays.toString(data.getBestCandidate()));
			}

			if (group.getExpressionStrategy().equals(Group.SAT_EXPRESSION)) {
				SATExpression<int[]> expression = new SATExpression<int[]>();

				// Express Trader
				if (trader != null && bestCandidate != null) {
					try {
						expression.setupTrader(bestCandidate, group, trader);
					} catch (Exception e) {
						System.out.println("Unable to setup trader " + 
								bestCandidate.getCandidateId());
						e.printStackTrace();
					}
				}
				
				// Remove candidates so as not to create duplicates
				List<Candidate> oldCandidates = groupDAO.findCandidatesInGroup(data.getPopulationId());
				for (Candidate c : oldCandidates) {
					expression.destroy(c.getGenome(), data.getPopulationId());
				}
			}

			// Use data to update group
			groupDAO.updateGroupStats(data);

		}
	};
	
	private ScheduledExecutorService ses;
	private ScheduledFuture<?> sf;
	
	public Evolver() {
		if (prefs.getBoolean(EVOLVE_ACTIVE, false)) {
			evolveActiveAt(
					new DateTime().
					withHourOfDay(prefs.getInt(EVOLVE_HOUR, 0)).
					withMinuteOfHour(prefs.getInt(EVOLVE_MINUTE, 0)));
		}
	}
	
	protected boolean isMarketOpenNow() {
		// Assumes that market hours are 7:30am to 2pm weekdays
		DateTime dt = new DateTime();
		return (dt.getHourOfDay() >= 7 && dt.getHourOfDay() < 14 && dt.getDayOfWeek() < 6);
	}
	
	protected boolean wasMarketOpenToday() {
		// Currently assumes market open on week days
		return (new DateTime().getDayOfWeek() < 6);
	}
	
	protected boolean isSaturday() {
		return (new DateTime().getDayOfWeek() == 6);
	}
	
	protected long minutesToTime(DateTime date) {		
		return Minutes.minutesBetween(new DateTime(), date).getMinutes();
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
		GenerationalEvolutionEngine<int[]> engine = new GenerationalEvolutionEngine<int[]>(candidateFactory,
		                                              pipeline,
		                                              candidateEvaluator,
		                                              candidateExpression,
		                                              selection,
		                                              RNG.getRNG(RNG.MARSENNETWISTER));
		
		// Set single thread
		//engine.setSingleThreaded(true);
		
		// Add observer for the evolution
		engine.addEvolutionObserver(dbObserver);
		
		return engine;
	}
	
	protected static ExpressionStrategy<int[]> getExpressionStrategy(Group group) {
		if (group.getExpressionStrategy().equals(Group.SAT_EXPRESSION)) {
			return new SATExpression<int[]>();
		}
		
		return null;
	}
	
	protected static ExpressedFitnessEvaluator<int[]> getEvaluator(Group group) {
		if (group.getEvaluationStrategy().equals(Group.REALIZED_GAIN_EVAL)) {
			return new RealizedGainEvaluator();
		} else {
			return new RandomEvaluator();
		}
	}
	
	/**
	 * Creates the first candidates for this group
	 * @param groupId
	 */
	public void createInitialCandidates(String groupId) {

		Group group = groupDAO.findGroup(groupId);
		
		int size = group.getSize();

		ExpressionStrategy<int[]> expressionStrategy = getExpressionStrategy(group);
		int genomeLength = expressionStrategy.getGenomeLength(groupId);
		
		ExpressedFitnessEvaluator<int[]> evaluator = getEvaluator(group);
		
		EvolutionEngine<int[]> engine = createEngine(genomeLength, 
														group.getGeneUpperValue(), 
														group.getMutationFactor(),
														expressionStrategy, evaluator);
		
		engine.expressInitialPopulation(groupId, size);
	}
	
	/*
	 * Evolve one group right now
	 */
	public void evolveNow(String groupId) {
		
		List<Candidate> tradeCandidates = groupDAO.findCandidatesInGroup(groupId);
		List<ExpressedCandidate<int[]>> candidates = new ArrayList<ExpressedCandidate<int[]>>(tradeCandidates.size());
		
		for (Candidate c : tradeCandidates) {
			c.setGenome(Candidate.parseGenomeString(c.getGenomeString()));
			candidates.add(c);
		}
		
		Group group = groupDAO.findGroup(groupId);
		
		int size = group.getSize();
		int eliteCount = group.getEliteCount();

		ExpressionStrategy<int[]> expressionStrategy = getExpressionStrategy(group);
		int genomeLength = expressionStrategy.getGenomeLength(groupId);
		
		ExpressedFitnessEvaluator<int[]> evaluator = getEvaluator(group);
		
		EvolutionEngine<int[]> engine = createEngine(genomeLength, 
														group.getGeneUpperValue(), 
														group.getMutationFactor(),
														expressionStrategy, evaluator);
		
		engine.evolveToExpression(candidates, groupId, size, group.getGeneration(), eliteCount, 
				terminationConditions);
	}
	
	/*
	 * Evolve all groups in database now
	 */
	public void evolveAllNow() {
		List<Group> groups = groupDAO.findGroups();
		for (Group group : groups) {
			evolveNow(group.getGroupId());
		}
	}

	/*
	 * Starts evolving all active groups at a specific hour of the day (0 to 23)
	 */
	public void evolveActiveAt(DateTime date) {
		if (date.isBeforeNow()) {
			System.out.println("Date to evolve is before now.  Setting to evolve at same time tomorrow");
			date = new DateTime().
						plusDays(1).
						withHourOfDay(date.getHourOfDay()).
						withMinuteOfHour(date.getMinuteOfHour());
		}

        this.ses = Executors.newScheduledThreadPool(NUM_THREADS);
        
		this.sf = ses.scheduleAtFixedRate(new Runnable () {
			@Override
			public void run() {
				try {
					List<Group> groups = groupDAO.findGroups();
					for (Group group : groups) {
						if (group.isActive()) {
							DateTime now = new DateTime();
							if (group.getFrequency().equals(Group.HOURLY) && isMarketOpenNow()) {
								evolveNow(group.getGroupId());
							}
							if (group.getFrequency().equals(Group.DAILY) && wasMarketOpenToday() &&
									now.getHourOfDay() == Integer.parseInt(EVOLVE_HOUR)) {
								evolveNow(group.getGroupId());
							}
							if (group.getFrequency().equals(Group.WEEKLY) && isSaturday() &&
									now.getHourOfDay() == Integer.parseInt(EVOLVE_HOUR)) {
								evolveNow(group.getGroupId());
								
							}
						}
					}
				} catch (Exception e) {
					System.out.println("Unable to evolve all groups. ");
					e.printStackTrace();
				}
			}
		}, minutesToTime(date), MINUTES_IN_HOUR, TimeUnit.MINUTES);

		prefs.putInt(EVOLVE_HOUR, date.getHourOfDay());
		prefs.putInt(EVOLVE_MINUTE, date.getMinuteOfHour());
		prefs.putBoolean(EVOLVE_ACTIVE, true);
	}
	
	public DateTime getNextEvolveDate() {
		if (sf == null || sf.isCancelled()) {
			return null;
		}
		return new DateTime().plusSeconds((int) sf.getDelay(TimeUnit.SECONDS));
	}
	
	public boolean stopEvolving() {
		try {
			// Stop any existing evolution
			((UserAbort)terminationConditions[0]).abort();
			
	        // Close scheduled service
	        sf.cancel(true);
	        ses.shutdown();
		} catch (SecurityException se) {
			System.out.println("Unable to stop evolving. " + se.getMessage());
			return false;
		}
		prefs.putBoolean(EVOLVE_ACTIVE, false);
		return true;
	}

	@Override
	public void deleteCandidateExpression(String groupId, int[] candidateGenome) {
		Group group = groupDAO.findGroup(groupId);
		
		if (group.getExpressionStrategy().equals(Group.SAT_EXPRESSION)) {
			SATExpression<int[]> expression = new SATExpression<int[]>();
			expression.destroy(candidateGenome, groupId);
		}
	}

	@Override
	public void deleteGroupExpression(String groupId) {

		Group group = groupDAO.findGroup(groupId);
		
		if (group.getExpressionStrategy().equals(Group.SAT_EXPRESSION)) {
			SATExpression<int[]> expression = new SATExpression<int[]>();
			
			SATExpression.getAlertReceiverService().removeReceiver(group.getAlertReceiverId());
			
			List<Candidate> oldCandidates = groupDAO.findCandidatesInGroup(groupId);
			for (Candidate c : oldCandidates) {
				expression.destroy(c.getGenome(), groupId);
			}
		}
	}
}
