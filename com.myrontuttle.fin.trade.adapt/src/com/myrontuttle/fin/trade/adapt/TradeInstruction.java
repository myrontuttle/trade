package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;
import java.util.Hashtable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.myrontuttle.fin.trade.api.Trade;

@Entity(name = "TradeInstructions")
public class TradeInstruction implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SEPARATOR = ",";
	public static final String ASSIGN = "=";
	
	@Id
	@Column(name = "TradeInstructionId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String tradeInstructionId;

	@Column(name = "TraderId")
	private String traderId;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "TraderId", referencedColumnName = "TraderId")
	private Trader trader;
	
	@Column(name = "Symbol")
	private String symbol;

	@Lob
	@Column(name = "Parameters")
	private String parameters;
	
	public TradeInstruction() {}
	

	public TradeInstruction(String traderId, Trade trade) {
		this.traderId = traderId;
		this.symbol = trade.getSymbol();
		this.parameters = generateParameters(trade.getParameters());
	}
	
	public Trade createTrade() {
		return new Trade(symbol, parseParameters(parameters));
	}
	
	public static String generateParameters(Hashtable<String, Integer> p) {
		if (p == null || p.size() == 0) {
			return "";
		}
		StringBuilder out = new StringBuilder();
		for (String key : p.keySet()) {
			out.append(key).append(ASSIGN).append(p.get(key)).append(SEPARATOR);
		}
		return out.toString();
	}
	
	public static Hashtable<String, Integer> parseParameters(String parameters) {
		String[] pairs = parameters.split(SEPARATOR);
		Hashtable<String, Integer> p = new Hashtable<String, Integer>(pairs.length);
		String[] keyValue;
		for (int i=0; i<pairs.length; i++) {
			keyValue = pairs[i].split(ASSIGN);
			p.put(keyValue[0], Integer.parseInt(keyValue[1]));
		}
		return p;
	}


	public String getTradeInstructionId() {
		return tradeInstructionId;
	}


	public void setTradeInstructionId(String tradeInstructionId) {
		this.tradeInstructionId = tradeInstructionId;
	}


	public String getTraderId() {
		return traderId;
	}


	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}


	public Trader getTrader() {
		return trader;
	}


	public void setTrader(Trader trader) {
		this.trader = trader;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getParameters() {
		return parameters;
	}


	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
