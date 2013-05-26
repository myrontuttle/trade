package com.myrontuttle.fin.trade.api;

/**
 * A criteria by which an equity can by screened
 * @author Myron Tuttle
 */
public abstract class SelectedScreenCriteria {

	private final String name;
	private final String selectedValue;
	private final String argsOperator;

	public SelectedScreenCriteria(String name, String selectedValue, String argOp){
		this.name = name;
		this.selectedValue = selectedValue;
		this.argsOperator = argOp;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return selectedValue;
	}

	public String getArgsOperator() {
		return argsOperator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((argsOperator == null) ? 0 : argsOperator.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((selectedValue == null) ? 0 : selectedValue.hashCode());
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
		SelectedScreenCriteria other = (SelectedScreenCriteria) obj;
		if (argsOperator == null) {
			if (other.argsOperator != null)
				return false;
		} else if (!argsOperator.equals(other.argsOperator))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (selectedValue == null) {
			if (other.selectedValue != null)
				return false;
		} else if (!selectedValue.equals(other.selectedValue))
			return false;
		return true;
	}
}