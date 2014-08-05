package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Models a trader
 * @author Myron Tuttle
 */
@Entity(name = "TRADERS")
public class Trader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRADER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long traderId;
	
	@Column(name = "GROUP_ID")
	private long groupId;
	
	@OneToOne
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
	private Group group;

	@Column(name = "GENOME_STRING")
	@Lob
	private String genomeString;
	
	@OneToMany(mappedBy = "trader", targetEntity = SavedScreen.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<SavedScreen> savedScreens;
	
	@ElementCollection
	@CollectionTable(
			name="SYMBOLS",
			joinColumns=@JoinColumn(name="TRADER_ID")
	)
	@Column(name="SYMBOL")
	private List<String> symbols;
	
	@OneToMany(mappedBy = "trader", targetEntity = SavedAlert.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<SavedAlert> savedAlerts;

	@OneToMany(mappedBy = "trader", targetEntity = TradeParameter.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<TradeParameter> tradeParameters;
	
	public Trader() {}
	
	public void addScreen(SavedScreen s) {
		this.savedScreens.add(s);
		if (s.getTrader() != this) {
			s.setTrader(this);
		}
	}
	
	public void removeScreen(SavedScreen s) {
		if (savedScreens.contains(s)) {
			savedScreens.remove(s);
			s.setTrader(null);
		}
	}

	public void addSymbol(String s) {
		this.symbols.add(s);
	}
	
	public void removeSymbol(String s) {
		if (symbols.contains(s)) {
			symbols.remove(s);
		}
	}
	
	public void addAlert(SavedAlert a) {
		this.savedAlerts.add(a);
		if (a.getTrader() != this) {
			a.setTrader(this);
		}
	}
	
	public void removeAlert(SavedAlert a) {
		if (savedAlerts.contains(a)) {
			savedAlerts.remove(a);
			a.setTrader(null);
		}
	}
	
	public void addTradeParameter(TradeParameter t) {
		this.tradeParameters.add(t);
		if (t.getTrader() != this) {
			t.setTrader(this);
		}
	}
	
	public void removeTradeParameter(TradeParameter t) {
		if (tradeParameters.contains(t)) {
			tradeParameters.remove(t);
			t.setTrader(null);
		}
	}

	public long getTraderId() {
		return traderId;
	}

	public void setTraderId(long traderId) {
		this.traderId = traderId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getGenomeString() {
		return genomeString;
	}

	public void setGenomeString(String genomeString) {
		this.genomeString = genomeString;
	}

	public Collection<SavedScreen> getSavedScreens() {
		return savedScreens;
	}

	public void setSavedScreens(Collection<SavedScreen> savedScreens) {
		this.savedScreens = savedScreens;
	}

	public List<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<String> symbols) {
		this.symbols = symbols;
	}

	public Collection<SavedAlert> getSavedAlerts() {
		return savedAlerts;
	}

	public void setSavedAlerts(Collection<SavedAlert> savedAlerts) {
		this.savedAlerts = savedAlerts;
	}

	public Collection<TradeParameter> getTradeParameters() {
		return tradeParameters;
	}

	public void setTradeParameters(Collection<TradeParameter> tradeParameters) {
		this.tradeParameters = tradeParameters;
	}
	
}
