package com.myrontuttle.fin.trade.api;

import java.util.Arrays;

/**
 * An indicator that a symbol has met a certain condition with certain parameters
 * @author Myron Tuttle
 */
public class SelectedAlert {

	private final int id;
	private final String condition;
	private final String symbol;
	private final double[] params;
	
	public SelectedAlert(int id, String condition, String symbol, double... params) {
		this.id = id;
		this.condition = condition;
		this.symbol = symbol;
		this.params = params;
	}

	public int getId() {
		return id;
	}

	public String getCondition() {
		return condition;
	}

	public String getSymbol() {
		return symbol;
	}

	public double getParam(int index) {
		return params[index];
	}
	
	public double[] getParams() {
		return params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(params);
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectedAlert other = (SelectedAlert) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (id != other.id)
			return false;
		if (!Arrays.equals(params, other.params))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
}
