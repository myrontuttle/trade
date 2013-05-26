package com.myrontuttle.fin.trade.api;

/**
 * A criteria by which an equity can by screened
 * @author Myron Tuttle
 */
public class AvailableScreenCriteria {
	
	public final static String AND = "AND";
	public final static String OR = "OR";

	private final String name;
	private final String argsOperator;
	private final String[] acceptedValues;

	/**
	 * Creates a single screener criteria
	 * @param name Name of the criteria
	 * @param acceptedValues The values that this criteria accepts
	 * @param argOp "AND" or "OR" - How the accepted values are combined
	 */
	public AvailableScreenCriteria(String name, String argOp, String[] acceptedValues){
		this.name = name;
		this.acceptedValues = acceptedValues;
		this.argsOperator = argOp;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAcceptedValue(int index) {
		if (index < 0) {
			index = 0;
		} else if (index > acceptedValues.length - 1) {
			index = acceptedValues.length - 1;
		}
		
		return acceptedValues[index];
	}
	
	public String[] getAcceptedValues() {
		return acceptedValues;
	}

	public String getArgsOperator() {
		return argsOperator;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AvailableScreenCriteria [name=" + name + ", argsOperator="
				+ argsOperator + ", acceptedValues=");
		for (int i=0; i<acceptedValues.length - 1; i++) {
			sb.append(acceptedValues[i] + ", ");
		}
		sb.append(acceptedValues[acceptedValues.length - 1] + "]");
		
		return sb.toString();
	}
}
