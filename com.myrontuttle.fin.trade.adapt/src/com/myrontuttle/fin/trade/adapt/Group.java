package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

/**
 * A group of trade strategy candidates which are generated with common settings
 * @author Myron Tuttle
 */
@Entity(name = "GROUPS")
public class Group implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GROUP_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long groupId;
	
	@OneToMany(mappedBy = "group", targetEntity = Candidate.class,
			fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<Candidate> candidates;
	
	@OneToOne(mappedBy = "group", targetEntity = Trader.class,
			optional = true, fetch = FetchType.EAGER, 
			cascade = CascadeType.ALL)
	private Trader bestTrader;

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ArrayList<GroupStats> stats;

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="BOOL_NAME")
    @Column(name="BOOL_VALUE")
    @CollectionTable(
    		name="BOOLEAN_SETTINGS", 
    		joinColumns=@JoinColumn(name="GROUP_ID"))
    private Map<String, Boolean> booleanSettings = new HashMap<String, Boolean>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="INT_NAME")
    @Column(name="INT_VALUE")
    @CollectionTable(
    		name="INTEGER_SETTINGS", 
    		joinColumns=@JoinColumn(name="GROUP_ID"))
    private Map<String, Integer> integerSettings = new HashMap<String, Integer>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="LONG_NAME")
    @Column(name="LONG_VALUE")
    @CollectionTable(
    		name="LONG_SETTINGS", 
    		joinColumns=@JoinColumn(name="GROUP_ID"))
    private Map<String, Long> longSettings = new HashMap<String, Long>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="DOUBLE_NAME")
    @Column(name="DOUBLE_VALUE")
    @CollectionTable(
    		name="DOUBLE_SETTINGS", 
    		joinColumns=@JoinColumn(name="GROUP_ID"))
    private Map<String, Double> doubleSettings = new HashMap<String, Double>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="STRING_NAME")
    @Column(name="STRING_VALUE")
    @CollectionTable(
    		name="STRING_SETTINGS", 
    		joinColumns=@JoinColumn(name="GROUP_ID"))
    private Map<String, String> stringSettings = new HashMap<String, String>();
	
	
	/*
	// Alert Receiver
	@Column(name = "ALERT_RECEIVER_TYPE")
	private String alertReceiverType;

	@Column(name = "ALERT_RECEIVER_ID")
	private long alertReceiverId;
	
	@Column(name = "ALERT_USER")
	private String alertUser;
	
	@Column(name = "ALERT_HOST")
	private String alertHost;
	
	@Column(name = "ALERT_PASSWORD")
	private String alertPassword;

	
	// Evolve
	@Column(name = "SIZE")
	private int size;
	
	@Column(name = "ELITE_COUNT")
	private int eliteCount;
	
	@Column(name = "GENE_UPPER_VALUE")
	private int geneUpperValue;
	
	@Column(name = "EVALUATION_STRATEGY")
	private String evaluationStrategy;

	@Column(name = "FREQUENCY")
	private String frequency;
	
	@Column(name = "MUTATION_FACTOR")
	private double mutationFactor;
	
	@Column(name = "GENERATION")
	private int generation;
	
	@Column(name = "ACTIVE")
	private boolean active;
	
	@Column(name = "VARIABILITY")
	private double variability;
	
	@Column(name = "START_TIME")
	private Date startTime;
	
	@Version
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;
	
	
	// Trade Strategy
	@Column(name = "TRADE_STRATEGY")
	private String tradeStrategy;
	
	@Column(name = "ALLOW_SHORTING")
	private boolean allowShorting;
	
	
	// Expression
	@Column(name = "NUMBER_OF_SCREENS")
	private int numberOfScreens;
	
	@Column(name = "MAX_SYMBOLS_PER_SCREEN")
	private int maxSymbolsPerScreen;
	
	@Column(name = "ALERTS_PER_SYMBOL")
	private int alertsPerSymbol;
	
	@Column(name = "STARTING_CASH")
	private double startingCash;
	*/
	
	
	public Group() {}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Collection<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(Collection<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public void addCandidate(Candidate c) {
		this.candidates.add(c);
		if (c.getGroup() != this) {
			c.setGroup(this);
			c.setGroupId(groupId);
		}
	}
	
	public void removeCandidate(Candidate c) {
		if (candidates.contains(c)) {
			candidates.remove(c);
			c.setGroup(null);
		}
	}
	
	public Trader getBestTrader() {
		return bestTrader;
	}

	public void setBestTrader(Trader bestTrader) {
		this.bestTrader = bestTrader;
	}
	
	public void removeBestTrader(Trader trader) {
		this.bestTrader = null;
		trader.setGroup(null);
	}

	public ArrayList<GroupStats> getStats() {
		return stats;
	}

	public void setStats(ArrayList<GroupStats> stats) {
		this.stats = stats;
	}

	public void addGroupStats(GroupStats gs) {
		this.stats.add(gs);
		if (gs.getGroup() != this) {
			gs.setGroup(this);
		}
	}
	
	public void removeStats(GroupStats gs) {
		if (stats.contains(gs)) {
			stats.remove(gs);
			gs.setGroup(null);
		}
	}

	public Map<String, Boolean> getBooleanSettings() {
		return booleanSettings;
	}

	public void setBooleanSettings(Map<String, Boolean> booleanSettings) {
		this.booleanSettings = booleanSettings;
	}
	
	public boolean getBoolean(String key) {
		return booleanSettings.get(key);
	}
	
	public void setBoolean(String key, boolean value) {
		booleanSettings.put(key, value);
	}
	
	public void removeBoolean(String key) {
		booleanSettings.remove(key);
	}

	public Map<String, Integer> getIntegerSettings() {
		return integerSettings;
	}

	public void setIntegerSettings(Map<String, Integer> integerSettings) {
		this.integerSettings = integerSettings;
	}

	public int getInteger(String key) {
		return integerSettings.get(key);
	}
	
	public void setInteger(String key, int value) {
		integerSettings.put(key, value);
	}
	
	public void removeInteger(String key) {
		integerSettings.remove(key);
	}

	public Map<String, Long> getLongSettings() {
		return longSettings;
	}

	public void setLongSettings(Map<String, Long> longSettings) {
		this.longSettings = longSettings;
	}

	public long getLong(String key) {
		return longSettings.get(key);
	}
	
	public void setLong(String key, long value) {
		longSettings.put(key, value);
	}
	
	public void removeLong(String key) {
		longSettings.remove(key);
	}

	public Map<String, Double> getDoubleSettings() {
		return doubleSettings;
	}

	public void setDoubleSettings(Map<String, Double> doubleSettings) {
		this.doubleSettings = doubleSettings;
	}

	public double getDouble(String key) {
		return doubleSettings.get(key);
	}
	
	public void setDouble(String key, double value) {
		doubleSettings.put(key, value);
	}
	
	public void removeDouble(String key) {
		doubleSettings.remove(key);
	}

	public Map<String, String> getStringSettings() {
		return stringSettings;
	}

	public void setStringSettings(Map<String, String> stringSettings) {
		this.stringSettings = stringSettings;
	}
	
	public String getString(String key) {
		return stringSettings.get(key);
	}
	
	public void setString(String key, String value) {
		stringSettings.put(key, value);
	}
	
	public void removeString(String key) {
		stringSettings.remove(key);
	}

	/*
	public long getAlertReceiverId() {
		return alertReceiverId;
	}

	public void setAlertReceiverId(long alertReceiverId) {
		this.alertReceiverId = alertReceiverId;
	}

	public String getAlertUser() {
		return alertUser;
	}

	public void setAlertUser(String alertUser) {
		this.alertUser = alertUser;
	}

	public String getAlertHost() {
		return alertHost;
	}

	public void setAlertHost(String alertHost) {
		this.alertHost = alertHost;
	}

	public String getAlertPassword() {
		return alertPassword;
	}

	public void setAlertPassword(String alertPassword) {
		this.alertPassword = alertPassword;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getEliteCount() {
		return eliteCount;
	}

	public void setEliteCount(int eliteCount) {
		this.eliteCount = eliteCount;
	}

	public int getGeneUpperValue() {
		return geneUpperValue;
	}

	public void setGeneUpperValue(int geneUpperValue) {
		this.geneUpperValue = geneUpperValue;
	}

	public String getEvaluationStrategy() {
		return evaluationStrategy;
	}

	public void setEvaluationStrategy(String evaluationStrategy) {
		this.evaluationStrategy = evaluationStrategy;
	}

	public String getTradeStrategy() {
		return tradeStrategy;
	}

	public void setTradeStrategy(String tradeStrategy) {
		this.tradeStrategy = tradeStrategy;
	}

	public boolean isAllowShorting() {
		return allowShorting;
	}

	public void setAllowShorting(boolean allowShorting) {
		this.allowShorting = allowShorting;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public double getMutationFactor() {
		return mutationFactor;
	}

	public void setMutationFactor(double mutationFactor) {
		this.mutationFactor = mutationFactor;
	}

	public int getNumberOfScreens() {
		return numberOfScreens;
	}

	public void setNumberOfScreens(int numberOfScreens) {
		this.numberOfScreens = numberOfScreens;
	}

	public int getMaxSymbolsPerScreen() {
		return maxSymbolsPerScreen;
	}

	public void setMaxSymbolsPerScreen(int maxSymbolsPerScreen) {
		this.maxSymbolsPerScreen = maxSymbolsPerScreen;
	}

	public int getAlertsPerSymbol() {
		return alertsPerSymbol;
	}

	public void setAlertsPerSymbol(int alertsPerSymbol) {
		this.alertsPerSymbol = alertsPerSymbol;
	}

	public double getStartingCash() {
		return startingCash;
	}

	public void setStartingCash(double startingCash) {
		this.startingCash = startingCash;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getVariability() {
		return variability;
	}

	public void setVariability(double variability) {
		this.variability = variability;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
*/
}
