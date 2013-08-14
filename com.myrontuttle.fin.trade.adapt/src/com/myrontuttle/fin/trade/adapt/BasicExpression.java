/**
 * 
 */
package com.myrontuttle.fin.trade.adapt;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;

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

	public static final int SCREEN_GENE_LENGTH = 3;
	public static final int ALERT_GENE_LENGTH = 4;
	public static final int TRADE_GENE_LENGTH = 5;
	
	// Genome positions
	public static final int SCREEN_SORT_POSITION = 0;
	
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
	
	public int getTotalGeneLength(Group group) {
		return 1 + SCREEN_GENE_LENGTH * group.getNumberOfScreens() +
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol() +
				group.getMaxSymbolsPerScreen() * group.getAlertsPerSymbol() * TRADE_GENE_LENGTH;
	}
	
	public int getValueUpperBound() {
		return UPPER_BOUND;
	}

	// Get the group based on a groupId
	Group findGroup(String groupId) {
		return em.createQuery(
				"SELECT g FROM Groups g WHERE groupId = :groupId", 
				Group.class).setParameter("groupId", groupId).getSingleResult();
	}

	// Create a record in the database for the candidate to get a fresh id
	Candidate newCandidateRecord(int[] genome, String populationId) {
		em.getTransaction().begin();
		Candidate cand = new Candidate();
		cand.setGenomeString(Candidate.generateGenomeString(genome));
		cand.setStartingCash(basicTradeStrategy.getStartingCash());
		cand.setGroupId(populationId);
		em.persist(cand);
		em.getTransaction().commit();
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(cand);
		return em.find(Candidate.class, objId);
	}

	/**
	 * Creates a set of SelectedScreenCriteria based on a candidate's screener genes
	 * Screen Gene Data Map
	 * 1. Is screen criteria active?
	 * 2. Criteria to use
	 * 3. Criteria value
	 * 
	 * @param candidate A candidate's genome
	 * @param position Where in the candidate's genome to start reading screener genes
	 * @return A set of screener criteria selected by this candidate
	 */
	SelectedScreenCriteria[] expressScreenerGenes(String userId, int[] candidate, int screens) {

		// Get screener possibilities
		AvailableScreenCriteria[] availableScreenCriteria = null;
		try {
			availableScreenCriteria = screenerService.getAvailableCriteria(userId);
		} catch (Exception e) {
			System.out.println("Error getting screener criteria: " + e.getMessage());
			e.printStackTrace();
		}

		int position = SCREEN_SORT_POSITION;
		ArrayList<SelectedScreenCriteria> selected = new ArrayList<SelectedScreenCriteria>();
		for (int i=0; i<screens; i++) {
			if (transpose(candidate[position], 0, 1) == 1) {
				int criteriaIndex = transpose(candidate[position + 1], 0, availableScreenCriteria.length - 1);
				String name = availableScreenCriteria[criteriaIndex].getName();
				int valueIndex = transpose(candidate[position + 2], 0, 
								availableScreenCriteria[criteriaIndex].getAcceptedValues().length - 1);
				String value = availableScreenCriteria[criteriaIndex].getAcceptedValue(valueIndex);
				String argOp = availableScreenCriteria[criteriaIndex].getArgsOperator();
				selected.add(new SelectedScreenCriteria(name, value, argOp));
			}
			position += SCREEN_GENE_LENGTH;
		}
		return selected.toArray(new SelectedScreenCriteria[selected.size()]);
	}
	
	String[] getScreenSymbols(int[] genome, String groupId, Group group) {
		String[] symbols = null;

		SelectedScreenCriteria[] screenCriteria = expressScreenerGenes(groupId, 
																		genome, 
																		group.getNumberOfScreens());
		int sortGene = transpose(genome[SCREEN_SORT_POSITION], 0, group.getNumberOfScreens());
		try {
			String[] screenSymbols = screenerService.screen(
											groupId,
											screenCriteria,
											screenCriteria[sortGene].getName(),
											group.getMaxSymbolsPerScreen());
			if (screenSymbols.length > group.getMaxSymbolsPerScreen()) {
				symbols = new String[group.getMaxSymbolsPerScreen()];
				System.arraycopy(screenSymbols, 0, symbols, 0, group.getMaxSymbolsPerScreen());
			} else {
				symbols = new String[screenSymbols.length];
				System.arraycopy(screenSymbols, 0, symbols, 0, symbols.length);
			}
		} catch (Exception e2) {
			System.out.println("Error screening for symbols: " + e2.getMessage());
			e2.printStackTrace();
		}
		return symbols;
	}
	
	String setupWatchlist(String candidateId, String[] symbols) {
		counter++;
		String watchlistId = null;
		try {
			watchlistId = watchlistService.create(candidateId, watchNamePrefix + counter);
		} catch (Exception e1) {
			System.out.println("Error creating watchlist: " + e1.getMessage());
			e1.printStackTrace();
		}

		// Add stocks to a watchlist
		for (int i=0; i<symbols.length; i++) {
			try {
				watchlistService.addHolding(candidateId, watchlistId, symbols[i]);
			} catch (Exception e) {
				System.out.println("Error adding " + symbols[i] + " to watchlist: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return watchlistId;
	}
	
	String setupPortfolio(String candidateId) {
		String portfolioId = null;
		
		try {
			portfolioId = portfolioService.create(candidateId, 
													portNamePrefix + counter);
			portfolioService.addCashTransaction(candidateId, portfolioId, 
												basicTradeStrategy.getStartingCash(), 
												true, true);
		} catch (Exception e) {
			System.out.println("Error creating portfolio: " + e.getMessage());
			System.out.println("Portfolio Id: " + portfolioId);
			e.printStackTrace();
		}
		
		return portfolioId;
	}
	
	private int getAlertStartPosition(Group group) {
		return SCREEN_SORT_POSITION + SCREEN_GENE_LENGTH * group.getNumberOfScreens();
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
	SelectedAlert[] expressAlertGenes(String userId, int[] candidate, String[] symbols,
										Group group) {

		// Get alert possibilities
		AvailableAlert[] availableAlerts = null;
		try {
			availableAlerts = alertService.getAvailableAlerts(userId);
		} catch (Exception e) {
			System.out.println("Error getting alerts available: " + e.getMessage());
			return null;
		}
		
		SelectedAlert[] selected = new SelectedAlert[symbols.length * group.getAlertsPerSymbol()];
		
		int position = getAlertStartPosition(group);
		
		for (int i=0; i<symbols.length; i++) {
			for (int j=0; j<group.getAlertsPerSymbol(); j++) {

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
	
	void setupAlerts(String groupId, SelectedAlert[] openAlerts, String groupEmail) {

		try {
			alertService.addAlertDestination(groupId, groupEmail, "EMAIL");
		} catch (Exception e) {
			System.out.println("Unable to add alert profile. " + e.getMessage());
			e.printStackTrace();
		}
		try {
			alertService.setupAlerts(groupId, openAlerts);
		} catch (Exception e) {
			System.out.println("Error creating alerts: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Creates a set of Trades based on a candidate's trade genes
	 * 
	 * Order Gene Data Map
	 * 1. Order Type
	 * 2. Allocation
	 * 3. Acceptable Loss
	 * 4. Time in Trade
	 * 5. Adjust At
	 * @param userId
	 * @param candidate A candidates genome
	 * @param position Where in the candidate's genome to start reading order genes
	 * @param symbols The symbols found during screening
	 * @return A set of alert criteria selected by this candidate
	 */
	TradeBounds[] expressTradeGenes(String userId, int[] candidate, String[] symbols, Group group) {

		TradeBounds[] trades = new TradeBounds[symbols.length];
		
		int position = getAlertStartPosition(group) + 
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol();
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
	
	void setupAlertReceiver(SelectedAlert[] openAlerts, String portfolioId, TradeBounds[] tradesToMake) {
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
	}

	@Override
	public Candidate express(int[] genome, String groupId) {

		// Create the candidate
		Candidate candidate = newCandidateRecord(genome, groupId);
		
		// Find the associated group
		Group group = findGroup(groupId);
		
		// Get a list of symbols from the Screener Service
		String[] symbols = getScreenSymbols(genome, groupId, group);
		
		// If the screener didn't produce any symbols there's no point using the other services
		if (symbols.length == 0) {
			return candidate;
		}
		
		// Create a watchlist
		String watchlistId = setupWatchlist(candidate.getCandidateId(), symbols);
		candidate.setWatchlistId(watchlistId);
		
		// Prepare portfolio
		String portfolioId = setupPortfolio(candidate.getCandidateId());

		// No point continuing if there's no portfolio to track trades
		if (portfolioId == null || portfolioId == "") {
			return candidate;
		} else {
			candidate.setPortfolioId(portfolioId);
		}

		// Create alerts for stocks
		SelectedAlert[] openAlerts = expressAlertGenes(groupId, genome, symbols, group);
		setupAlerts(groupId, openAlerts, group.getAlertAddress());
		
		// Create trades to be made when alerts are triggered
		TradeBounds[] tradesToMake = expressTradeGenes(candidate.getCandidateId(), 
														genome, symbols, group);
		
		// Create listener for alerts to move stocks to portfolio
		setupAlertReceiver(openAlerts, portfolioId, tradesToMake);

		// Save candidate to database, and return
		em.persist(candidate);
		em.getTransaction().commit();
		
		return candidate;
	}

	@Override
	public void candidatesExpressed(
			List<ExpressedCandidate<int[]>> expressedCandidates) {

	}
	
	/**
	 * Transpose the genes to the the proper value within the required range [inclusive]
	 * @param value The gene value (less than UPPER_BOUND)
	 * @param lower The lower bound of the range to transpose into
	 * @param upper The upper bound of the range to transpose into
	 * @return The transposed value between the lower and upper bounds of the gene
	 */
	private int transpose(int value, int lower, int upper) {
		if (upper - lower < UPPER_BOUND) {
			if (value == UPPER_BOUND) {
				return upper;
			}
			return lower + (int)Math.floor((value / UPPER_BOUND) * (upper - lower + 1));
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
}
