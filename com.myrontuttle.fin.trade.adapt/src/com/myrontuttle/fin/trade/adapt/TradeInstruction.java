package com.myrontuttle.fin.trade.adapt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TraderId", referencedColumnName = "TraderId")
	private Trader trader;
	
	@Column(name = "Instruction")
	private String instruction;
	
	public TradeInstruction() {}

	public TradeInstruction(String traderId, String instruction) {
		this.traderId = traderId;
		this.instruction = instruction;
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

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
}
