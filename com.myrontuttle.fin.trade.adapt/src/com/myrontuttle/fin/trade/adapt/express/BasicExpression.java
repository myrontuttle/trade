/**
 * 
 */
package com.myrontuttle.fin.trade.adapt.express;

import java.util.ArrayList;
import java.util.List;


import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.StrategyDAOImpl;
import com.myrontuttle.fin.trade.api.*;
import com.myrontuttle.fin.trade.tradestrategies.AlertTradeBounds;
import com.myrontuttle.fin.trade.tradestrategies.BasicTradeStrategy;
import com.myrontuttle.fin.trade.tradestrategies.TradeBounds;
import com.myrontuttle.sci.evolve.ExpressedCandidate;
import com.myrontuttle.sci.evolve.ExpressionStrategy;

/**
 * Expresses a genome of int[] into a TradingStrategy
 * @author Myron Tuttle
 * @param <T> The candidate to be expressed
 */
public class BasicExpression<T> implements ExpressionStrategy<int[]> {

	// Gene lengths
	public static final int SCREEN_GENE_LENGTH = 3;
	public static final int ALERT_GENE_LENGTH = 4;
	public static final int TRADE_GENE_LENGTH = 5;
	
	// Genome positions
	public static final int SCREEN_SORT_POSITION = 0;
	
	public static final String PORT_NAME_PREFIX = "PORT";
	public static final String WATCH_NAME_PREFIX = "WATCH";
	
	// Managed by Blueprint
	ScreenerService screenerService = null;
	WatchlistService watchlistService = null;
	AlertService alertService = null;
	PortfolioService portfolioService = null;
	TradeStrategy tradeStrategy = null;
	AlertReceiverService alertReceiver = null;
	
	StrategyDAOImpl strategyDAOImpl = null;
	
	private int counter;
	
	public ScreenerService getScreenerService() {
		return screenerService;
	}

	public void setScreenerService(ScreenerService screenerService) {
		this.screenerService = screenerService;
	}

	public WatchlistService getWatchlistService() {
		return watchlistService;
	}

	public void setWatchlistService(WatchlistService watchlistService) {
		this.watchlistService = watchlistService;
	}

	public AlertService getAlertService() {
		return alertService;
	}

	public void setAlertService(AlertService alertService) {
		this.alertService = alertService;
	}

	public PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}

	public TradeStrategy getTradeStrategy() {
		return tradeStrategy;
	}

	public void setTradeStrategy(TradeStrategy tradeStrategy) {
		this.tradeStrategy = tradeStrategy;
	}

	public AlertReceiverService getAlertReceiver() {
		return alertReceiver;
	}

	public void setAlertReceiver(AlertReceiverService alertReceiver) {
		this.alertReceiver = alertReceiver;
	}

	public StrategyDAOImpl getStrategyDAO() {
		return strategyDAOImpl;
	}

	public void setStrategyDAO(StrategyDAOImpl strategyDAOImpl) {
		this.strategyDAOImpl = strategyDAOImpl;
	}

	public void startUp() {
		this.counter = 0;
	}
	
	public static int calculateGenomeLength(Group group) {
		return 1 + SCREEN_GENE_LENGTH * group.getNumberOfScreens() +
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol() +
				group.getMaxSymbolsPerScreen() * group.getAlertsPerSymbol() * TRADE_GENE_LENGTH;
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
	SelectedScreenCriteria[] expressScreenerGenes(String userId, int[] candidate, 
													int screens, int geneUpperValue) {

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
			if (transpose(candidate[position], geneUpperValue, 0, 1) == 1) {
				int criteriaIndex = transpose(candidate[position + 1], geneUpperValue, 
												0, availableScreenCriteria.length - 1);
				String name = availableScreenCriteria[criteriaIndex].getName();
				int valueIndex = transpose(candidate[position + 2], geneUpperValue, 0, 
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
																		group.getNumberOfScreens(),
																		group.getGeneUpperValue());
		int sortGene = transpose(genome[SCREEN_SORT_POSITION], group.getGeneUpperValue(), 
									0, group.getNumberOfScreens());
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
			watchlistId = watchlistService.create(candidateId, WATCH_NAME_PREFIX + counter);
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
													PORT_NAME_PREFIX + counter);
			portfolioService.addCashTransaction(candidateId, portfolioId, 
												tradeStrategy.getStartingCash(), 
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
																	group.getGeneUpperValue(),
																	0, availableAlerts.length - 1)];
				int id = alert.getId();
				String[] criteriaTypes = alert.getCriteriaTypes();
				double[] params = new double[criteriaTypes.length];
				for (int k=0; k<criteriaTypes.length; k++) {
					if (criteriaTypes[k].equals(AvailableAlert.DOUBLE)) {
						double upper = alertService.getUpperDouble(userId, id, symbols[i], k);
						double lower = alertService.getLowerDouble(userId, id, symbols[i], k);
						params[k] = transpose(candidate[position + 1], group.getGeneUpperValue(),
												lower, upper);
					} else if (criteriaTypes[k].equals(AvailableAlert.LIST)) {
						int upper = alertService.getListLength(userId, id, k);
						params[k] = transpose(candidate[position + 1], group.getGeneUpperValue(), 
												0, upper);
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
		
		BasicTradeStrategy basicTradeStrategy = (BasicTradeStrategy)tradeStrategy;
		
		int position = getAlertStartPosition(group) + 
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol();
		for (int i=0; i<symbols.length; i++) {
			int open = transpose(candidate[position],
								group.getGeneUpperValue(), 0, 
								portfolioService.openOrderTypesAvailable(userId).length - 1);
			
			int allocation = transpose(candidate[position + 1],
											group.getGeneUpperValue(),
											basicTradeStrategy.tradeAllocationLower(), 
											basicTradeStrategy.tradeAllocationUpper());

			int loss = transpose(candidate[position + 2],
											group.getGeneUpperValue(),
											basicTradeStrategy.acceptableLossLower(), 
											basicTradeStrategy.acceptableLossUpper());

			int time = transpose(candidate[position + 3],
											group.getGeneUpperValue(),
											basicTradeStrategy.timeInTradeLower(), 
											basicTradeStrategy.timeInTradeUpper());

			int adjust = transpose(candidate[position + 4],
											group.getGeneUpperValue(),
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
		Candidate candidate = strategyDAOImpl.newCandidateRecord(genome, groupId, tradeStrategy.getStartingCash());
		
		// Find the associated group
		Group group = strategyDAOImpl.findGroup(groupId);
		
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
		strategyDAOImpl.saveCandidate(candidate);
		
		return candidate;
	}

	@Override
	public void candidatesExpressed(
			List<ExpressedCandidate<int[]>> expressedCandidates) {
		// All candidates for this generation expressed so reset counter to 0
		counter = 0;
	}
	
	/**
	 * Transpose the genes to the the proper value within the required range [inclusive]
	 * @param geneValue The gene value
	 * @param geneUpperValue The upper limit of what the geneValue is able to have
	 * @param targetLower The lower bound of the range to transpose into
	 * @param targetUpper The upper bound of the range to transpose into
	 * @return The transposed value between the lower and upper bounds of the gene
	 */
	private int transpose(int geneValue, int geneUpperValue, int targetLower, int targetUpper) {
		if (targetUpper - targetLower < geneUpperValue) {
			if (geneValue == geneUpperValue) {
				return targetUpper;
			}
			return targetLower + (int)Math.floor((geneValue / geneUpperValue) * 
									(targetUpper - targetLower + 1));
		} else {
			return targetLower + (int)Math.floor((geneValue / geneUpperValue) * 
									(targetUpper - targetLower));
		}
	}

	/**
	 * Transpose the genes to the the proper value within the required range
	 * @param geneValue The gene value
	 * @param geneUpperValue The upper limit of what the geneValue is able to have
	 * @param targetLower The lower bound of the range to transpose into
	 * @param targetUpper The upper bound of the range to transpose into
	 * @return The transposed value between the lower and upper bounds of the gene
	 */
	private double transpose(int geneValue, int geneUpperValue, double targetLower, double targetUpper) {
		if (targetUpper - targetLower < geneUpperValue) {
			return targetLower + (geneValue * (targetUpper - targetLower)) / geneUpperValue;
		} else {
			return targetLower + (geneValue / geneUpperValue) * (targetUpper - targetLower);
		}
	}
}
