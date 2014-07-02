/**
 * 
 */
package com.myrontuttle.fin.trade.adapt.express;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myrontuttle.fin.trade.adapt.*;
import com.myrontuttle.fin.trade.api.*;
import com.myrontuttle.sci.evolve.api.ExpressedCandidate;
import com.myrontuttle.sci.evolve.api.ExpressionStrategy;

/**
 * Expresses a genome of int[] into a trading strategy candidate by Screening, Alerting, and Trading 
 * (using a paper account/portfolio)
 * @author Myron Tuttle
 * @param <T> The candidate to be expressed
 */
public class SATExpression<T> implements ExpressionStrategy<int[]> {

	private static final Logger logger = LoggerFactory.getLogger( SATExpression.class );

	// Gene lengths
	public static final int SCREEN_GENE_LENGTH = 2;
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
		SATExpression.quoteService = quoteService;
	}

	public static ScreenerService getScreenerService() {
		return screenerService;
	}

	public void setScreenerService(ScreenerService screenerService) {
		SATExpression.screenerService = screenerService;
	}

	public static WatchlistService getWatchlistService() {
		return watchlistService;
	}

	public void setWatchlistService(WatchlistService watchlistService) {
		SATExpression.watchlistService = watchlistService;
	}

	public static AlertService getAlertService() {
		return alertService;
	}

	public void setAlertService(AlertService alertService) {
		SATExpression.alertService = alertService;
	}

	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		SATExpression.portfolioService = portfolioService;
	}

	public static TradeStrategyService getTradeStrategyService() {
		return tradeStrategyService;
	}

	public void setTradeStrategyService(TradeStrategyService tradeStrategyService) {
		SATExpression.tradeStrategyService = tradeStrategyService;
	}

	public static AlertReceiverService getAlertReceiverService() {
		return alertReceiverService;
	}

	public void setAlertReceiverService(AlertReceiverService alertReceiverService) {
		SATExpression.alertReceiverService = alertReceiverService;
	}

	public static GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		SATExpression.groupDAO = groupDAO;
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
	 * 1. Criteria to use
	 * 2. Criteria value
	 * 
	 * @param candidate A candidate
	 * @param group The candidates group
	 * @return A set of screener criteria selected by this candidate
	 */
	public SavedScreen[] expressScreenerGenes(Candidate candidate, Group group) 
			throws Exception {
		
		int[] genome = candidate.getGenome();
		String candidateId = candidate.getCandidateId();

		// Get screener possibilities
		AvailableScreenCriteria[] availableScreenCriteria = 
				screenerService.getAvailableCriteria(group.getGroupId());
		if (availableScreenCriteria == null || availableScreenCriteria.length <= 0 || 
				availableScreenCriteria[0] == null) {
			throw new Exception("No available screen criteria for " + group.getGroupId());
		}

		// Start at the first screen gene
		int position = getScreenStartPosition();
		ArrayList<SavedScreen> selected = new ArrayList<SavedScreen>();
		int screens = group.getNumberOfScreens();
		int geneUpperValue = group.getGeneUpperValue();
		for (int i=0; i<screens; i++) {

			int criteriaIndex = transpose(genome[position], geneUpperValue, 
											0, availableScreenCriteria.length - 1);
			String name = availableScreenCriteria[criteriaIndex].getName();
			int valueIndex = transpose(genome[position + 1], geneUpperValue, 0, 
							availableScreenCriteria[criteriaIndex].getAcceptedValues().length - 1);
			String value = availableScreenCriteria[criteriaIndex].getAcceptedValue(valueIndex);
			String argOp = availableScreenCriteria[criteriaIndex].getArgsOperator();
			selected.add(new SavedScreen(candidateId, name, value, argOp));
			
			position += SCREEN_GENE_LENGTH;
		}
		return selected.toArray(new SavedScreen[selected.size()]);
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
	public SavedAlert[] expressAlertGenes(Candidate candidate, Group group, 
			String[] symbols) throws Exception {

		int[] genome = candidate.getGenome();
		String candidateId = candidate.getCandidateId();
		
		// Get alert possibilities
		AvailableAlert[] availableAlerts = alertService.getAvailableAlerts(group.getGroupId());
		if (availableAlerts == null) {
			throw new Exception("No available alerts for " + group.getGroupId());
		}
		
		SavedAlert[] selected = new SavedAlert[symbols.length * group.getAlertsPerSymbol()];
		
		int position = getAlertStartPosition(group);
		int s = 0;
		for (int i=0; i<symbols.length; i++) {
			for (int j=0; j<group.getAlertsPerSymbol(); j++) {

				AvailableAlert alert = availableAlerts[transpose(genome[position],  
																	group.getGeneUpperValue(),
																	0, availableAlerts.length - 1)];
				int alertId = alert.getId();
				String[] criteriaTypes = alert.getCriteriaTypes();
				double[] params = new double[criteriaTypes.length];
				for (int k=0; k<criteriaTypes.length; k++) {
					if (criteriaTypes[k].equals(AvailableAlert.DOUBLE)) {
						double upper = alertService.getUpperDouble(group.getGroupId(), alertId, symbols[i], k);
						double lower = alertService.getLowerDouble(group.getGroupId(), alertId, symbols[i], k);
						params[k] = transpose(genome[position + 1], group.getGeneUpperValue(),
												lower, upper);
					} else if (criteriaTypes[k].equals(AvailableAlert.LIST)) {
						int upper = alertService.getListLength(group.getGroupId(), alertId, k);
						params[k] = transpose(genome[position + 1], group.getGeneUpperValue(), 
												0, upper);
					}
				}
				String selectedCondition = alertService.parseCondition(alert, symbols[i], 
																		params);
				selected[s] = new SavedAlert(candidateId, alertId, selectedCondition, 
													symbols[i], params);
				s++;
				position += ALERT_GENE_LENGTH;
			}
		}
		
		return selected;
	}
	
	String[] setupAlerts(Group group, SelectedAlert[] openAlerts) throws Exception {

		alertService.addAlertDestination(group.getGroupId(), group.getAlertUser(), "EMAIL");

		return (alertService.setupAlerts(group.getGroupId(), openAlerts));
	}
	
	/**
	 * Creates trades and the events that will open them
	 * @param candidate
	 * @param group
	 * @param numberOfSymbols
	 * @param alerts SelectedAlerts
	 * @param alertIds
	 * @return An array of trade Ids
	 * @throws Exception
	 */
	String[] createTrades(Candidate candidate, Group group, int numberOfSymbols,
						SelectedAlert[] alerts, String[] alertIds) throws Exception {
		
		ArrayList<String> trades = new ArrayList<String>(alertIds.length);

		String candidateId = candidate.getCandidateId();
		String portfolioId = candidate.getPortfolioId();
		String tradeStrategy = group.getTradeStrategy();
		String actionType = tradeStrategyService.tradeActionToStart(tradeStrategy);
		
		String tradeId;
		int position;
		for (int i=0; i<numberOfSymbols; i++) {
			for (int j=0; j<group.getAlertsPerSymbol(); j++) {
				position = (i * group.getAlertsPerSymbol()) + j;
				logger.debug("New Trade for {}. Pos={}, alertId={}, alert={}", 
						new Object[]{candidateId, position, alertIds[position], alerts[position].getCondition()});
				if (position < alerts.length && position < alertIds.length && 
						alertIds[position] != null) {
					tradeId = tradeStrategyService.addTrade(tradeStrategy, candidateId, 
													portfolioId, group.getGroupId(), 
													alerts[position].getSymbol());
					trades.add(tradeId);
					tradeStrategyService.setTradeEvent(tradeId, alerts[position].getCondition(), 
																actionType, alertIds[position]);
				}
			}
		}
		
		return trades.toArray(new String[trades.size()]);
	}

	/**
	 * Creates a set of Trades based on a candidate's trade genes
	 * 
	 * Trade Gene Data Map
	 * 1. Order Type
	 * 2. Allocation
	 * 3. Acceptable Loss
	 * 4. Time in Trade
	 * 5. Adjust At
	 * @param candidate A candidate
	 * @param group The candidate's group
	 * @param symbols The symbols found during screening
	 * @return A set of strategy parameters selected for this candidate
	 */
	public ArrayList<SelectedStrategyParameter> expressTradeGenes(Candidate candidate, Group group, 
												String[] tradeIds) throws Exception {

		String tradeStrategy = group.getTradeStrategy();
		int[] genome = candidate.getGenome();
		
		AvailableStrategyParameter[] availableParameters = tradeStrategyService.availableTradeParameters(
																tradeStrategy);
		
		int position = getAlertStartPosition(group) + 
				group.getMaxSymbolsPerScreen() * ALERT_GENE_LENGTH * group.getAlertsPerSymbol();
		
		ArrayList<SelectedStrategyParameter> selectedParams = 
				new ArrayList<SelectedStrategyParameter>(tradeIds.length*availableParameters.length);
		for (int i=0; i<tradeIds.length; i++) {
			
			for (int j=0; j<availableParameters.length; j++) {
				selectedParams.add(new SelectedStrategyParameter(
						tradeIds[i], 
						availableParameters[j].getName(),
						transpose(genome[position + j],
								group.getGeneUpperValue(),
								availableParameters[j].getLower(), 
								availableParameters[j].getUpper())));
			}
			
			position += TRADE_GENE_LENGTH;
		}
		return selectedParams;
	}
	
	void setupTradeParams(ArrayList<SelectedStrategyParameter> params) {
		for (SelectedStrategyParameter p : params) {
			tradeStrategyService.setTradeParameter(p.getTradeId(), 
					p.getName(), p.getValue());
		}
	}

	@Override
	public void beforeExpression(String populationId) {

		Group group = groupDAO.findGroup(populationId);
		String receiverId = group.getAlertReceiverId();
		
		// Check if there's already an alert receiver
		if (receiverId == null || receiverId.isEmpty()) {
			// No receiver so set one up
			
			receiverId = alertReceiverService.addReceiver(populationId, group.getAlertReceiverType());
			group.setAlertReceiverId(receiverId);

        	alertReceiverService.setReceiverParameter(receiverId, "Host", group.getAlertHost());
        	alertReceiverService.setReceiverParameter(receiverId, "User", group.getAlertUser());
        	alertReceiverService.setReceiverParameter(receiverId, "Password", group.getAlertHost());
		}
		
		alertReceiverService.startReceiving(receiverId);
	}

	@Override
	public Candidate express(int[] genome, String groupId) {
		
		if (genome == null || genome.length == 0) {
			logger.debug("No genome to express for group: {}.", groupId);
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
				logger.debug("No active screen criteria for {}.", candidate.getCandidateId());
				return candidate;
			}
			
			// Get a list of symbols from the Screener Service
			String[] symbols = getScreenSymbols(candidate, group, screenCriteria);
			
			// If the screener didn't produce any symbols there's no point using the other services
			if (symbols.length == 0) {
				logger.debug("No symbols found for candidate {}.", candidate.getCandidateId());
				return candidate;
			}
			
			// Create a watchlist
			String watchlistId = setupWatchlist(candidate, group, symbols);
			candidate.setWatchlistId(watchlistId);
			
			// Prepare portfolio
			String portfolioId = setupPortfolio(candidate, group);

			// No point continuing if there's no portfolio to track trades
			if (portfolioId == null || portfolioId == "") {
				logger.debug("Unable to create portfolio for {}.", candidate.getCandidateId());
				return candidate;
			} else {
				candidate.setPortfolioId(portfolioId);
			}

			// Create (symbols*alertsPerSymbol) alerts for stocks
			SelectedAlert[] openAlerts = expressAlertGenes(candidate, group, symbols);
			String alertIDs[] = setupAlerts(group, openAlerts);
			
			// Create (symbol*alertsPerSymbol) trades to be made when alerts are triggered
			String[] tradeIds = createTrades(candidate, group, symbols.length, openAlerts, alertIDs);
			ArrayList<SelectedStrategyParameter> params = expressTradeGenes(candidate, group, tradeIds);
			setupTradeParams(params);
			
		} catch (Exception e) {
			logger.warn("Unable to express candidate {}", candidate.getCandidateId(), e);
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
			
			groupDAO.removeCandidate(c.getCandidateId());

			// Remove alert trade mapping
			tradeStrategyService.removeAllTrades(c.getCandidateId());
			
			// Remove Alerts
			alertService.removeAllAlerts(c.getGroupId());

			// Remove Portfolio
			if (c.getPortfolioId() != null) {
				portfolioService.delete(c.getCandidateId(), c.getPortfolioId());
			}

			// Remove Watchlist
			if (c.getWatchlistId() != null) {
				watchlistService.delete(c.getCandidateId(), c.getWatchlistId());
			}
			
		} catch (Exception e) {
			logger.warn("Unable to destroy candidate with genome: {}.", Arrays.toString(genome), e);
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

		SavedScreen[] screenCriteria = expressScreenerGenes(candidate, group);
		if (screenCriteria.length == 0) {
			return trader;
		}
		for (SavedScreen criteria : screenCriteria) {
			groupDAO.addSavedScreen(criteria, trader.getTraderId());
		}
		
		String[] symbols = getScreenSymbols(candidate, group, screenCriteria);
		if (symbols == null || symbols.length == 0) {
			return trader;
		}
		for (String symbol : symbols) {
			groupDAO.addSymbol(symbol, trader.getTraderId());
		}
		
		SavedAlert[] alerts = expressAlertGenes(candidate, group, symbols);
		if (alerts.length == 0) {
			return trader;
		}
		for (SavedAlert alert : alerts) {
			groupDAO.addSavedAlert(alert, trader.getTraderId());
		}

		ArrayList<SelectedStrategyParameter> params = expressTradeGenes(candidate, group, symbols);
		for (SelectedStrategyParameter p : params) {
			String tradeDesc = p.getTradeId() + ": " + p.getName() + " = " + p.getValue();
			groupDAO.addTradeInstruction(new TradeInstruction(trader.getTraderId(), tradeDesc), 
					trader.getTraderId());
		}
		return trader;
	}
}
