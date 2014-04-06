/**
 * 
 */
package com.myrontuttle.fin.trade.adapt.express;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.myrontuttle.fin.trade.adapt.*;
import com.myrontuttle.fin.trade.api.*;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;
import com.myrontuttle.sci.evolve.api.ExpressionStrategy;

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
	
	public static final String PORT_NAME_PREFIX = "P";
	public static final String WATCH_NAME_PREFIX = "W";
	public static final String GROUP = "G";
	public static final String CANDIDATE = "C";
	
	// Managed by Blueprint
	private static QuoteService quoteService;	// Used in TradeStrategy
	private static ScreenerService screenerService;
	private static WatchlistService watchlistService;
	private static AlertService alertService;
	private static PortfolioService portfolioService;
	private static TradeStrategyService tradeStrategyService;
	private static AlertReceiverService alertReceiverService;
	
	private static GroupDAO groupDAO;
	
	public static QuoteService getQuoteService() {
		return quoteService;
	}

	public void setQuoteService(QuoteService quoteService) {
		BasicExpression.quoteService = quoteService;
	}

	public static ScreenerService getScreenerService() {
		return screenerService;
	}

	public void setScreenerService(ScreenerService screenerService) {
		BasicExpression.screenerService = screenerService;
	}

	public static WatchlistService getWatchlistService() {
		return watchlistService;
	}

	public void setWatchlistService(WatchlistService watchlistService) {
		BasicExpression.watchlistService = watchlistService;
	}

	public static AlertService getAlertService() {
		return alertService;
	}

	public void setAlertService(AlertService alertService) {
		BasicExpression.alertService = alertService;
	}

	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		BasicExpression.portfolioService = portfolioService;
	}

	public static TradeStrategyService getTradeStrategyService() {
		return tradeStrategyService;
	}

	public void setTradeStrategyService(TradeStrategyService tradeStrategyService) {
		BasicExpression.tradeStrategyService = tradeStrategyService;
	}

	public static AlertReceiverService getAlertReceiverService() {
		return alertReceiverService;
	}

	public void setAlertReceiverService(AlertReceiverService alertReceiverService) {
		BasicExpression.alertReceiverService = alertReceiverService;
	}
	
	private List<Service> bundleServices(AlertReceiver alertReceiver) {
		List<Service> tradeStrategyServices = new ArrayList<Service>(4);
		tradeStrategyServices.add(quoteService);
		tradeStrategyServices.add(alertReceiver);
		tradeStrategyServices.add(portfolioService);
		tradeStrategyServices.add(alertService);
		return tradeStrategyServices;
	}

	public static GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		BasicExpression.groupDAO = groupDAO;
	}

	public int getGenomeLength(String groupId) {
		Group group = groupDAO.findGroup(groupId);
		return 1 + SCREEN_GENE_LENGTH * group.getNumberOfScreens() +
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol() +
				group.getMaxSymbolsPerScreen() * group.getAlertsPerSymbol() * TRADE_GENE_LENGTH;
	}

	private int getScreenStartPosition() {
		return SCREEN_SORT_POSITION + 1;
	}

	/**
	 * Creates a set of SelectedScreenCriteria based on a candidate's screener genes
	 * Screen Gene Data Map
	 * 1. Is screen criteria active?
	 * 2. Criteria to use
	 * 3. Criteria value
	 * 
	 * @param candidate A candidate
	 * @param group The candidates group
	 * @return A set of screener criteria selected by this candidate
	 */
	public SelectedScreenCriteria[] expressScreenerGenes(Candidate candidate, Group group) 
			throws Exception {
		
		int[] genome = candidate.getGenome();

		// Get screener possibilities
		AvailableScreenCriteria[] availableScreenCriteria = 
				screenerService.getAvailableCriteria(group.getGroupId());
		if (availableScreenCriteria == null || availableScreenCriteria.length <= 0 || 
				availableScreenCriteria[0] == null) {
			throw new Exception("No available screen criteria for " + group.getGroupId());
		}

		// Start at the first screen gene
		int position = getScreenStartPosition();
		ArrayList<SelectedScreenCriteria> selected = new ArrayList<SelectedScreenCriteria>();
		int screens = group.getNumberOfScreens();
		int geneUpperValue = group.getGeneUpperValue();
		for (int i=0; i<screens; i++) {
			int active = transpose(genome[position], geneUpperValue, 0, 1);
			if (active == 1) {
				int criteriaIndex = transpose(genome[position + 1], geneUpperValue, 
												0, availableScreenCriteria.length - 1);
				String name = availableScreenCriteria[criteriaIndex].getName();
				int valueIndex = transpose(genome[position + 2], geneUpperValue, 0, 
								availableScreenCriteria[criteriaIndex].getAcceptedValues().length - 1);
				String value = availableScreenCriteria[criteriaIndex].getAcceptedValue(valueIndex);
				String argOp = availableScreenCriteria[criteriaIndex].getArgsOperator();
				selected.add(new SelectedScreenCriteria(name, value, argOp));
			}
			position += SCREEN_GENE_LENGTH;
		}
		return selected.toArray(new SelectedScreenCriteria[selected.size()]);
	}
	
	public String[] getScreenSymbols(Candidate candidate, Group group, 
										SelectedScreenCriteria[] screenCriteria) throws Exception {
		String[] symbols = null;
		int[] genome = candidate.getGenome();

		int sortGene = transpose(genome[SCREEN_SORT_POSITION], group.getGeneUpperValue(), 
									0, screenCriteria.length - 1);

		String[] screenSymbols = screenerService.screen(
										group.getGroupId(),
										screenCriteria,
										screenCriteria[sortGene].getName(),
										group.getMaxSymbolsPerScreen());
		
		if (screenSymbols == null) {
			throw new Exception("No symbols found for candidate " + candidate.getCandidateId());
		}
		if (screenSymbols.length > group.getMaxSymbolsPerScreen()) {
			symbols = new String[group.getMaxSymbolsPerScreen()];
			System.arraycopy(screenSymbols, 0, symbols, 0, group.getMaxSymbolsPerScreen());
		} else {
			symbols = new String[screenSymbols.length];
			System.arraycopy(screenSymbols, 0, symbols, 0, symbols.length);
		}
		
		return symbols;
	}
	
	String setupWatchlist(Candidate candidate, Group group, String[] symbols) throws Exception {
		String watchlistId = null;
		String candidateId = candidate.getCandidateId();
		String groupId = group.getGroupId();
		String name = WATCH_NAME_PREFIX + GROUP + groupId + CANDIDATE + candidateId;
		watchlistId = watchlistService.create(candidateId, name);
		if (watchlistId == null) {
			throw new Exception("Error creating watchlist: " + name);
		}

		// Add stocks to a watchlist
		for (int i=0; i<symbols.length; i++) {
			if (symbols[i] != null) {
				watchlistService.addHolding(candidateId, watchlistId, symbols[i]);
			}
		}
		
		return watchlistId;
	}
	
	String setupPortfolio(Candidate candidate, Group group) throws Exception {
		String portfolioId = null;
		String candidateId = candidate.getCandidateId();
		String name = PORT_NAME_PREFIX + GROUP + group.getGroupId() + CANDIDATE + candidateId;
		portfolioId = portfolioService.create(candidateId, name);
		if (portfolioId == null) {
			throw new Exception("Error creating portfolio: " + name);
		}
		portfolioService.addCashTransaction(candidateId, portfolioId, 
											group.getStartingCash(), 
											true, true);
		
		return portfolioId;
	}
	
	private int getAlertStartPosition(Group group) {
		return getScreenStartPosition() + SCREEN_GENE_LENGTH * group.getNumberOfScreens();
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
	 * @param candidate A candidate
	 * @param group The candidate's group
	 * @param symbols The symbols found during screening
	 * @return A set of alert criteria selected by this candidate
	 */
	public SelectedAlert[] expressAlertGenes(Candidate candidate, Group group, 
			String[] symbols) throws Exception {

		int[] genome = candidate.getGenome();
		
		// Get alert possibilities
		AvailableAlert[] availableAlerts = alertService.getAvailableAlerts(group.getGroupId());
		if (availableAlerts == null) {
			throw new Exception("No available alerts for " + group.getGroupId());
		}
		
		SelectedAlert[] selected = new SelectedAlert[symbols.length * group.getAlertsPerSymbol()];
		
		int position = getAlertStartPosition(group);
		int s = 0;
		for (int i=0; i<symbols.length; i++) {
			for (int j=0; j<group.getAlertsPerSymbol(); j++) {

				AvailableAlert alert = availableAlerts[transpose(genome[position],  
																	group.getGeneUpperValue(),
																	0, availableAlerts.length - 1)];
				int id = alert.getId();
				String[] criteriaTypes = alert.getCriteriaTypes();
				double[] params = new double[criteriaTypes.length];
				for (int k=0; k<criteriaTypes.length; k++) {
					if (criteriaTypes[k].equals(AvailableAlert.DOUBLE)) {
						double upper = alertService.getUpperDouble(group.getGroupId(), id, symbols[i], k);
						double lower = alertService.getLowerDouble(group.getGroupId(), id, symbols[i], k);
						params[k] = transpose(genome[position + 1], group.getGeneUpperValue(),
												lower, upper);
					} else if (criteriaTypes[k].equals(AvailableAlert.LIST)) {
						int upper = alertService.getListLength(group.getGroupId(), id, k);
						params[k] = transpose(genome[position + 1], group.getGeneUpperValue(), 
												0, upper);
					}
				}
				String selectedCondition = alertService.parseCondition(alert, symbols[i], 
																		params);
				selected[s] = new SelectedAlert(id, selectedCondition, 
													symbols[i], params);
				s++;
				position += ALERT_GENE_LENGTH;
			}
		}
		
		return selected;
	}
	
	SelectedAlert[] setupAlerts(Group group, SelectedAlert[] openAlerts) throws Exception {

		alertService.addAlertDestination(group.getGroupId(), group.getAlertUser(), "EMAIL");

		return (alertService.setupAlerts(group.getGroupId(), openAlerts));
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
	 * @param candidate A candidate
	 * @param group The candidate's group
	 * @param symbols The symbols found during screening
	 * @return A set of alert criteria selected by this candidate
	 */
	public Trade[] expressTradeGenes(Candidate candidate, Group group, String[] symbols) throws Exception {

		String candidateId = candidate.getCandidateId();
		int[] genome = candidate.getGenome();
		
		Trade[] trades = new Trade[symbols.length];

		AlertReceiver alertReceiver = alertReceiverService.getAlertReceiver(group.getGroupId(), 
																	group.getAlertReceiver());
		List<Service> services = bundleServices(alertReceiver);
		TradeStrategy tradeStrategy = tradeStrategyService.getTradeStrategy(group.getTradeStrategy(), services);
		
		int openOrderTypes;
		try {
			openOrderTypes = portfolioService.openOrderTypesAvailable(candidateId).length;
		} catch (Exception e) {
			System.out.println("Unable to get openOrderTypesAvailable from Portfolio Service. Using 1");
			openOrderTypes = 1;
		}

		tradeStrategy.setOrderTypesAvailable(openOrderTypes);
		
		AvailableStrategyParameter[] availableParameters = tradeStrategy.availableParameters();
		
		int position = getAlertStartPosition(group) + 
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol();
		
		Hashtable<String, Integer> tradeParameters;
		
		for (int i=0; i<symbols.length; i++) {
			tradeParameters = new Hashtable<String, Integer>(availableParameters.length);
			
			for (int j=0; j<availableParameters.length; j++) {
				tradeParameters.put(availableParameters[j].getName(),
										transpose(genome[position + j],
												group.getGeneUpperValue(),
												availableParameters[j].getLower(), 
												availableParameters[j].getUpper()));
			}
			
			trades[i] = new Trade(symbols[i], tradeParameters);
			position += TRADE_GENE_LENGTH;
		}
		return trades;
	}
	
	void setupAlertReceiver(SelectedAlert[] openAlerts, Candidate candidate, 
								String portfolioId, Trade[] tradesToMake, 
								Group group) throws Exception {

		AlertReceiver alertReceiver = alertReceiverService.getAlertReceiver(group.getGroupId(), 
																		group.getAlertReceiver());
		
		AlertTrade[] alertTrades = new AlertTrade[openAlerts.length];
		for (int i=0; i<tradesToMake.length; i++) {
			for (int j=0; j<group.getAlertsPerSymbol(); j++) {
				alertTrades[i+j] = new AlertTrade(openAlerts[i+j], candidate.getCandidateId(), 
														portfolioId, tradesToMake[i]);
			}
		}
		alertReceiver.watchFor(alertTrades);
	}

	@Override
	public void beforeExpression(String populationId) {

		Group group = groupDAO.findGroup(populationId);
		HashMap<String, String> connectionDetails = new HashMap<String, String>();
		
		try {
			AlertReceiver alertReceiver = alertReceiverService.getAlertReceiver(group.getGroupId(), 
																		group.getAlertReceiver());

			if (alertReceiver.getName().equals("EmailAlert")) {
				connectionDetails.put("Host", group.getAlertHost());
				connectionDetails.put("User", group.getAlertUser());
				connectionDetails.put("Password", group.getAlertPassword());
			}
			
			List<Service> services = bundleServices(alertReceiver);
			TradeStrategy tradeStrategy = tradeStrategyService.getTradeStrategy(group.getTradeStrategy(), services);
			alertReceiver.startReceiving(tradeStrategy, connectionDetails);
		} catch (Exception e) {
			System.out.println("Error occured before expression of group: " + group.getGroupId());
			e.printStackTrace();
		}		
	}

	@Override
	public Candidate express(int[] genome, String groupId) {
		
		if (genome == null || genome.length == 0) {
			System.out.println("No Genome to Express");
		}
		Candidate candidate = new Candidate();
		candidate.setGenome(genome);
		candidate.setGroupId(groupId);
		
		groupDAO.addCandidate(candidate, groupId);
		
		// Find the group
		Group group = groupDAO.findGroup(groupId);

		try {
			// Get criteria to screen against
			SelectedScreenCriteria[] screenCriteria = expressScreenerGenes(candidate, group);

			if (screenCriteria.length == 0) {
				// All of the screen symbols are turned off and we won't get any symbols from screening
				System.out.println("No active screen criteria for " + candidate.getCandidateId());
				return candidate;
			}
			
			// Get a list of symbols from the Screener Service
			String[] symbols = getScreenSymbols(candidate, group, screenCriteria);
			
			// If the screener didn't produce any symbols there's no point using the other services
			if (symbols.length == 0) {
				System.out.println("No symbols found for candidate " + candidate.getCandidateId());
				return candidate;
			}
			
			// Create a watchlist
			String watchlistId = setupWatchlist(candidate, group, symbols);
			candidate.setWatchlistId(watchlistId);
			
			// Prepare portfolio
			String portfolioId = setupPortfolio(candidate, group);

			// No point continuing if there's no portfolio to track trades
			if (portfolioId == null || portfolioId == "") {
				System.out.println("Unable to create portfolio for " + candidate.getCandidateId());
				return candidate;
			} else {
				candidate.setPortfolioId(portfolioId);
			}

			// Create (symbols*alertsPerSymbol) alerts for stocks
			SelectedAlert[] openAlerts = expressAlertGenes(candidate, group, symbols);
			openAlerts = setupAlerts(group, openAlerts);
			
			// Create (symbol) trades to be made when alerts are triggered
			Trade[] tradesToMake = expressTradeGenes(candidate, group, symbols);
			
			// Create listener for alerts to move stocks to portfolio
			setupAlertReceiver(openAlerts, candidate, portfolioId, tradesToMake, group);
		} catch (Exception e) {
			System.out.println("Unable to express candidate " + 
					candidate.getCandidateId());
			e.printStackTrace();
		}

		// Save candidate to database, and return
		groupDAO.updateCandidate(candidate);
		
		return candidate;
	}

	@Override
	public void candidatesExpressed(
			List<ExpressedCandidate<int[]>> expressedCandidates, String populationId) {
		
		// Determine average Hamming distance
		int[] genomeA;
		int[] genomeB;
		int distance;
		int hammingPairings = 0;
		double hammingSum = 0.0;
		for (int i=0; i<expressedCandidates.size(); i++) {
			genomeA = expressedCandidates.get(i).getGenome();
			for (int j=i+1; j<expressedCandidates.size(); j++) {
				genomeB = expressedCandidates.get(j).getGenome();
				distance = 0;
				for (int k = 0; k < genomeA.length; k++) {
					if (genomeA[k] != genomeB[k]) {
						distance++;
					}
				}
				hammingPairings++;
				hammingSum += distance;
			}
		}
		
		double meanHammingDistance = hammingSum / hammingPairings;
		
		Group group = groupDAO.findGroup(populationId);
		group.setVariability(meanHammingDistance);
		groupDAO.updateGroup(group);
		
	}

	@Override
	public void destroy(int[] genome, String populationId) {
		
		try {
			Candidate c = groupDAO.findCandidateByGenome(genome);

			// Remove Watchlist
			if (c.getWatchlistId() != null) {
				watchlistService.delete(c.getCandidateId(), c.getWatchlistId());
			}

			// Remove Portfolio
			if (c.getPortfolioId() != null) {
				portfolioService.delete(c.getCandidateId(), c.getPortfolioId());
			}
			
			// Remove Alerts
			alertService.removeAllAlerts(c.getGroupId());

			AlertReceiver alertReceiver = alertReceiverService.getAlertReceiver(c.getGroupId(), null);
			if (alertReceiver != null) {
				alertReceiver.stopWatchingAll(c.getCandidateId());
			}
			
			groupDAO.removeCandidate(c.getCandidateId());
			
		} catch (Exception e) {
			System.out.println("Unable to destroy candidate with genome: " + 
					Arrays.toString(genome));
			e.printStackTrace();
		}
		
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
			return targetLower + (int)Math.floor(((double)geneValue / geneUpperValue) * 
					(targetUpper - targetLower + 1));
		} else {
			return targetLower + (int)Math.floor(((double)geneValue / geneUpperValue) * 
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
			return targetLower + ((double)(geneValue * (targetUpper - targetLower)) / geneUpperValue);
		} else {
			return targetLower + ((double)geneValue / geneUpperValue) * (targetUpper - targetLower);
		}
	}
	
	public Trader setupTrader(Candidate candidate, Group group, Trader trader) 
			throws Exception {

		SelectedScreenCriteria[] screenCriteria = expressScreenerGenes(candidate, group);
		if (screenCriteria.length == 0) {
			return trader;
		}
		for (SelectedScreenCriteria criteria : screenCriteria) {
			groupDAO.addSavedScreen(new SavedScreen(trader.getTraderId(), criteria), 
									trader.getTraderId());
		}
		
		String[] symbols = getScreenSymbols(candidate, group, screenCriteria);
		if (symbols == null || symbols.length == 0) {
			return trader;
		}
		for (String symbol : symbols) {
			groupDAO.addSymbol(symbol, trader.getTraderId());
		}
		
		SelectedAlert[] alerts = expressAlertGenes(candidate, group, symbols);
		if (alerts.length == 0) {
			return trader;
		}
		for (SelectedAlert alert : alerts) {
			groupDAO.addSavedAlert(new SavedAlert(trader.getTraderId(), alert), 
					trader.getTraderId());
		}

		AlertReceiver alertReceiver = alertReceiverService.getAlertReceiver(group.getGroupId(), 
																	group.getAlertReceiver());
		List<Service> services = bundleServices(alertReceiver);
		TradeStrategy tradeStrategy = tradeStrategyService.getTradeStrategy(group.getTradeStrategy(), services);
		Trade[] trades = expressTradeGenes(candidate, group, symbols);
		for (Trade trade : trades) {
			String[] tradeDesc = tradeStrategy.describeTrade(candidate.getCandidateId(), trade);
			for (String desc : tradeDesc) {
				groupDAO.addTradeInstruction(new TradeInstruction(trader.getTraderId(), desc), 
						trader.getTraderId());
			}
		}
		return trader;
	}
}
