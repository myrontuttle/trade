package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Entity(name = "Traders")
public class Trader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TraderId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String traderId;
	
	@Column(name = "GroupId")
	private String groupId;
	
	@OneToOne
	@JoinColumn(name = "GroupId", referencedColumnName = "GroupId")
	private Group group;

	@Column(name = "GenomeString")
	@Lob
	private String genomeString;
	
	@OneToMany(mappedBy = "trader", targetEntity = SavedScreen.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<SavedScreen> savedScreens;
	
	@OneToMany(mappedBy = "trader", targetEntity = SavedAlert.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<SavedAlert> savedAlerts;

	@OneToMany(mappedBy = "trader", targetEntity = TradeInstruction.class,
				fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<TradeInstruction> tradeInstructions;
	
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
	
	public void addTradeInstruction(TradeInstruction t) {
		this.tradeInstructions.add(t);
		if (t.getTrader() != this) {
			t.setTrader(this);
		}
	}
	
	public void removeTradeInstruction(TradeInstruction t) {
		if (tradeInstructions.contains(t)) {
			tradeInstructions.remove(t);
			t.setTrader(null);
		}
	}

	public String getTraderId() {
		return traderId;
	}

	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
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

	public Collection<SavedAlert> getSavedAlerts() {
		return savedAlerts;
	}

	public void setSavedAlerts(Collection<SavedAlert> savedAlerts) {
		this.savedAlerts = savedAlerts;
	}

	public Collection<TradeInstruction> getTradeInstructions() {
		return tradeInstructions;
	}

	public void setTradeInstructions(Collection<TradeInstruction> tradeInstructions) {
		this.tradeInstructions = tradeInstructions;
	}
	
}
