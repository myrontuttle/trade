/**
 * 
 */
package com.myrontuttle.fin.trade.adapt;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.myrontuttle.evolve.ExpressedCandidate;
import com.myrontuttle.evolve.ExpressionStrategy;

import com.myrontuttle.fin.trade.api.*;
import com.myrontuttle.fin.trade.tradestrategies.AlertTradeBounds;
import com.myrontuttle.fin.trade.tradestrategies.BasicTradeStrategy;
import com.myrontuttle.fin.trade.tradestrategies.TradeBounds;

/**
 * Expresses a genome of int[] into a TradingStrategy
 * @author Myron Tuttle
 * @param <T> The candidate to be expressed
 */
public class BasicExpression<T> implements ExpressionStrategy<int[]> {

	private static final int SCREEN_SORT_POSITION = 0;
	private static final int SCREEN_GENE_LENGTH = 2;
	private static final int ALERT_GENE_LENGTH = 4;
	private static final int TRADE_GENE_LENGTH = 5;
	
	// These can be adjusted
	private static final int SCREEN_GENES = 5;
	private static final int SCREEN_USED_PROB = 50;	// 50% chance that each screen will be used
	private static final int MAX_SYMBOLS_PER_SCREEN = 10;
	private static final int ALERTS_PER_SYMBOL = 1;
	// TRADES_PER_CANDIDATE or TRADES_PER_ALERT
	
	public static final int TOTAL_GENE_LENGTH = SCREEN_GENE_LENGTH * SCREEN_GENES +
									MAX_SYMBOLS_PER_SCREEN * ALERT_GENE_LENGTH * ALERTS_PER_SYMBOL +
									MAX_SYMBOLS_PER_SCREEN * ALERTS_PER_SYMBOL * TRADE_GENE_LENGTH;
	public static final int UPPER_BOUND = 100;
	
	private final ScreenerService screenerService;
	private final WatchlistService watchlistService;
	private final AlertService alertService;
	private final PortfolioService portfolioService;
	private final BasicTradeStrategy basicTradeStrategy;
	private final AlertReceiverService alertReceiver;
	
	private final EntityManager em;
	
	private final String portNamePrefix;
	private final String watchNamePrefix;
	private int counter;
	
	BasicExpression(ScreenerService screenerService, 
							WatchlistService watchlistService,
							AlertService alertService, 
							PortfolioService portfolioService,
							BasicTradeStrategy basicTradeStrategy,
							AlertReceiverService alertReceiver,
							EntityManager em) {
		this.screenerService = screenerService;
		this.watchlistService = watchlistService;
		this.alertService = alertService;
		this.portfolioService = portfolioService;
		this.basicTradeStrategy = basicTradeStrategy;
		this.alertReceiver = alertReceiver;
		
		this.em = em;
		
		this.counter = 0;
		this.portNamePrefix = "Port";
		this.watchNamePrefix = "Watch";
	}
	
	private List<Integer> arrayToList(int[] array) {
		List<Integer> list = new ArrayList<Integer>(array.length);
		for (int i=0; i<array.length; i++) {
			list.set(i, array[i]);
		}
		return list;
	}
	
	@Override
	public Candidate express(int[] candidate, String populationId) {
		
		//TODO: Generate user id
		String indUserId = null;
		
		String populationEmail = Evolver.getAlertAddress(populationId);
		
		// Initialize position of counter along genome
		int position = SCREEN_SORT_POSITION;

		// Get a list of symbols from the Screener Service
		String[] symbols = null;
		int sortBy = transpose(candidate[position], 0, SCREEN_GENES);
		SelectedScreenCriteria[] screenCriteria = null;
		position++;
		try {
			screenCriteria = expressScreenerGenes(populationId, candidate, position);
			String[] screenSymbols = screenerService.screen(
											populationId,
											screenCriteria,
											sortBy,
											MAX_SYMBOLS_PER_SCREEN);
			if (screenSymbols.length > MAX_SYMBOLS_PER_SCREEN) {
				symbols = new String[MAX_SYMBOLS_PER_SCREEN];
				System.arraycopy(screenSymbols, 0, symbols, 0, MAX_SYMBOLS_PER_SCREEN);
			} else {
				symbols = new String[screenSymbols.length];
				System.arraycopy(screenSymbols, 0, symbols, 0, symbols.length);
			}
		} catch (Exception e2) {
			System.out.println("Error screening for symbols: " + e2.getMessage());
			e2.printStackTrace();
		}
		position += SCREEN_GENES * SCREEN_GENE_LENGTH;
		
		// Create a watchlist
		counter++;
		String watchlistId = null;
		try {
			watchlistId = watchlistService.create(indUserId, watchNamePrefix + counter);
		} catch (Exception e1) {
			System.out.println("Error creating watchlist: " + e1.getMessage());
			e1.printStackTrace();
		}

		// Add stocks to a watchlist
		for (int i=0; i<symbols.length; i++) {
			try {
				watchlistService.addHolding(indUserId, watchlistId, symbols[i]);
			} catch (Exception e) {
				System.out.println("Error adding " + symbols[i] + " to watchlist: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		// Prepare portfolio
		String portfolioId = null;
		try {
			portfolioId = portfolioService.create(indUserId, portNamePrefix + counter);
			portfolioService.addCashTransaction(indUserId, portfolioId, 
												basicTradeStrategy.getStartingCash(), 
												true, true);
		} catch (Exception e) {
			System.out.println("Error creating portfolio: " + e.getMessage());
			e.printStackTrace();
		}

		// Create alerts for stocks
		SelectedAlert[] openAlerts = 
				expressAlertGenes(populationId, candidate, position, symbols);
		try {
			alertService.addAlertDestination(populationId, populationEmail, "EMAIL");
		} catch (Exception e) {
			System.out.println("Unable to add alert profile. " + e.getMessage());
			e.printStackTrace();
		}
		try {
			alertService.setupAlerts(populationId, openAlerts);
		} catch (Exception e) {
			System.out.println("Error creating alerts: " + e.getMessage());
			e.printStackTrace();
		}
		position += MAX_SYMBOLS_PER_SCREEN * ALERT_GENE_LENGTH * ALERTS_PER_SYMBOL;
		
		// Create trades to be made when alerts are triggered
		TradeBounds[] tradesToMake = 
				expressTradeGenes(indUserId, candidate, position, symbols);
		
		// Create listener for alerts to move stocks to portfolio
		AlertTradeBounds[] alertTradeBounds = new AlertTradeBounds[openAlerts.length];
		for (int i=0; i<openAlerts.length; i++) {
			alertTradeBounds[i] = new AlertTradeBounds(openAlerts[i], portfolioId, tradesToMake[i]);
		}
		try {
			alertReceiver.watchFor(alertTradeBounds);
		} catch (Exception e) {
			System.out.println("Error preparing to watch for alerts: " + 
								e.getMessage());
			e.printStackTrace();
		}

		// Get portfolio total gains
		return new Candidate(indUserId, populationId, arrayToList(candidate), 
							portfolioId, basicTradeStrategy.getStartingCash()/*,
							screenCriteria, symbols, alertTradeBounds*/);
	}

	/**
	 * Creates a set of SelectedScreenCriteria based on a candidate's screener genes
	 * Screen Gene Data Map
	 * 1. Criteria name
	 * 2. Criteria list index
	 * 
	 * @param candidate A candidate's genome
	 * @param position Where in the candidate's genome to start reading screener genes
	 * @return A set of screener criteria selected by this candidate
	 */
	private SelectedScreenCriteria[] expressScreenerGenes(String userId, int[] candidate, 
															int position) {

		// Get screener possibilities
		AvailableScreenCriteria[] availableScreenCriteria = null;
		try {
			availableScreenCriteria = screenerService.getAvailableCriteria(userId);
		} catch (Exception e) {
			System.out.println("Error getting screener criteria: " + e.getMessage());
			e.printStackTrace();
		}

		ArrayList<SelectedScreenCriteria> selected = new ArrayList<SelectedScreenCriteria>();
		for (int i=0; i<SCREEN_GENES; i++) {
			int lower = ((100/SCREEN_USED_PROB) - 1) * availableScreenCriteria.length;
			int cIndex = transpose(candidate[position], lower, availableScreenCriteria.length - 1);
			if (cIndex >= 0) {
				String name = availableScreenCriteria[cIndex].getName();
				int vIndex = transpose(candidate[position + 1], 0, 
								availableScreenCriteria[cIndex].getAcceptedValues().length);
				String value = availableScreenCriteria[cIndex].getAcceptedValue(vIndex);
				String argOp = availableScreenCriteria[cIndex].getArgsOperator();
				selected.add(new SelectedScreenCriteria(name, value, argOp));
			}
			position += SCREEN_GENE_LENGTH;
		}
		return selected.toArray(new SelectedScreenCriteria[selected.size()]);
	}
	
	/**
	 * Creates a set of SelectedAlert based on a candidate's alert genes
	 * If the alert requires a double then two positions will be used to create a double with
	 * two decimal places.  If the alert requires a list index, then only the first position
	 * will be used.
	 * 
	 * Alert Gene Data Map
	 * 1. condition
	 * 2. parameter1 (list index or double value)
	 * 3. parameter2 (list index or double value)
	 * 4. parameter3 (list index or double value)
	 * 
	 * @param userId
	 * @param candidate A candidates genome
	 * @param position Where in the candidate's genome to start reading alert genes
	 * @return A set of alert criteria selected by this candidate
	 */
	private SelectedAlert[] expressAlertGenes(String userId, int[] candidate, 
												int position, 
												String[] symbols) {

		// Get alert possibilities
		AvailableAlert[] availableAlerts = null;
		try {
			availableAlerts = alertService.getAvailableAlerts(userId);
		} catch (Exception e) {
			System.out.println("Error getting alerts available: " + e.getMessage());
			return null;
		}
		
		SelectedAlert[] selected = new SelectedAlert[symbols.length * ALERTS_PER_SYMBOL];
		
		for (int i=0; i<symbols.length; i++) {
			for (int j=0; j<ALERTS_PER_SYMBOL; j++) {

				AvailableAlert alert = availableAlerts[transpose(candidate[position], 
																	0, availableAlerts.length - 1)];
				int id = alert.getId();
				String[] criteriaTypes = alert.getCriteriaTypes();
				double[] params = new double[criteriaTypes.length];
				for (int k=0; k<criteriaTypes.length; k++) {
					if (criteriaTypes[k].equals(AvailableAlert.DOUBLE)) {
						double upper = alertService.getUpperDouble(userId, id, symbols[i], k);
						double lower = alertService.getLowerDouble(userId, id, symbols[i], k);
						params[k] = transpose(candidate[position + 1], lower, upper);
					} else if (criteriaTypes[k].equals(AvailableAlert.LIST)) {
						int upper = alertService.getListLength(userId, id, k);
						params[k] = transpose(candidate[position + 1], 0, upper);
					}
				}
				String selectedCondition = alertService.parseCondition(alert, symbols[i], 
																		params);
				selected[i + j] = new SelectedAlert(id, selectedCondition, 
													symbols[i], params);
				position += ALERT_GENE_LENGTH;
			}
		}
		
		return selected;
	}


	/**
	 * Creates a set of Trades based on a candidate's trade genes
	 * 
	 * Order Gene Data Map
	 * @param userId
	 * @param candidate A candidates genome
	 * @param position Where in the candidate's genome to start reading order genes
	 * @param symbols The symbols found during screening
	 * @return A set of alert criteria selected by this candidate
	 */
	private TradeBounds[] expressTradeGenes(String userId, int[] candidate, 
										int position, 
										String[] symbols) {

		TradeBounds[] trades = new TradeBounds[symbols.length];
		
		for (int i=0; i<symbols.length; i++) {
			int open = transpose(candidate[position], 0, 
								portfolioService.openOrderTypesAvailable(userId).length - 1);
			
			int allocation = transpose(candidate[position + 1],
											basicTradeStrategy.tradeAllocationLower(), 
											basicTradeStrategy.tradeAllocationUpper());

			int loss = transpose(candidate[position + 2],
											basicTradeStrategy.acceptableLossLower(), 
											basicTradeStrategy.acceptableLossUpper());

			int time = transpose(candidate[position + 3],
											basicTradeStrategy.timeInTradeLower(), 
											basicTradeStrategy.timeInTradeUpper());

			int adjust = transpose(candidate[position + 4],
											basicTradeStrategy.adjustAtLower(), 
											basicTradeStrategy.adjustAtUpper());
			
			trades[i] = new TradeBounds(symbols[i], open, allocation, loss, time, adjust);
			position += TRADE_GENE_LENGTH;
		}
		return trades;
	}
	
	/**
	 * Transpose the genes to the the proper value within the required range
	 * @param value The gene value
	 * @param lower The lower bound of the range to transpose into
	 * @param upper The upper bound of the range to transpose into
	 * @return The transposed value between the lower and upper bounds of the gene
	 */
	private int transpose(int value, int lower, int upper) {
		if (upper - lower < UPPER_BOUND) {
			return lower + (int)Math.floor((value * (upper - lower)) / UPPER_BOUND);
		} else {
			return lower + (int)Math.floor((value / UPPER_BOUND) * (upper - lower));
		}
	}

	/**
	 * Transpose the genes to the the proper value within the required range
	 * @param value The gene value
	 * @param lower The lower bound of the range to transpose into
	 * @param upper The upper bound of the range to transpose into
	 * @return The transposed value between the lower and upper bounds of the gene
	 */
	private double transpose(int value, double lower, double upper) {
		if (upper - lower < UPPER_BOUND) {
			return lower + (value * (upper - lower)) / UPPER_BOUND;
		} else {
			return lower + (value / UPPER_BOUND) * (upper - lower);
		}
	}
	
	@Override
	public void candidatesExpressed(
			List<ExpressedCandidate<int[]>> expressedCandidates) {

		// TODO Save candidates to db
		EntityTransaction userTransaction = em.getTransaction();
		userTransaction.begin();
		for (ExpressedCandidate<int[]> candidate : expressedCandidates) {
			em.persist(candidate);
		}
		userTransaction.commit();
	}
}
