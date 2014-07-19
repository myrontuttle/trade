package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.myrontuttle.fin.trade.api.SelectedStrategyParameter;

@Entity(name = "TRADE_PARAMETERS")
public class TradeParameter implements Serializable, SelectedStrategyParameter {

	private static final long serialVersionUID = 1L;
	public static final String SEPARATOR = ":";
	public static final String ASSIGN = "=";
	
	@Id
	@Column(name = "TRADE_PARAMETER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long tradeInstructionId;

	@Column(name = "TRADER_ID")
	private long traderId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TRADER_ID", referencedColumnName = "TRADER_ID")
	private Trader trader;

	@Column(name = "TRADE_ID")
	private long tradeId;
	
	@Column(name = "SYMBOL")
	private String symbol;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "VALUE")
	private int value;
	
	public TradeParameter() {}
	
	public TradeParameter(long traderId, String symbol, String name, int value) {
		this.traderId = traderId;
		this.symbol = symbol;
		this.name = name;
		this.value = value;
	}

	public static String generateInstruction(String symbol, String name, int value) {
		return symbol + SEPARATOR + name + ASSIGN + value;
	}

	public long getTradeInstructionId() {
		return tradeInstructionId;
	}

	public void setTradeInstructionId(long tradeInstructionId) {
		this.tradeInstructionId = tradeInstructionId;
	}

	public long getTraderId() {
		return traderId;
	}

	public void setTraderId(long traderId) {
		this.traderId = traderId;
	}

	public Trader getTrader() {
		return trader;
	}

	public void setTrader(Trader trader) {
		this.trader = trader;
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
