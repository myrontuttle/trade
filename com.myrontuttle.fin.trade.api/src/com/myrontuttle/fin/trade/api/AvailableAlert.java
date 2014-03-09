package com.myrontuttle.fin.trade.api;

import java.util.Arrays;

/**
 * An indicator that a symbol has met a certain condition with potential parameter limits
 * @author Myron Tuttle
 */
public class AvailableAlert {
	
	public static final String DOUBLE = "DOUBLE";
	public static final String LIST = "LIST";
	
	private final int id;
	private final String condition;
	private final String[] criteriaTypes;
	private final String[] criteriaNames;
	private final String[] lowerBounds;
	private final String[] upperBounds;
	private final String[][] listCriteriaOptions;

	public AvailableAlert(int id, String condition, String[] criteriaTypes, String[] criteriaNames,
							String[] lowerBounds, String[] upperBounds, String[][] listCriteriaOptions) {
		this.id = id;
		this.condition = condition;
		this.criteriaTypes = criteriaTypes;
		this.criteriaNames = criteriaNames;
		this.upperBounds = upperBounds;
		this.lowerBounds = lowerBounds;
		this.listCriteriaOptions = listCriteriaOptions;
	}

	public int getId() {
		return id;
	}

	public String getCondition() {
		return condition;
	}

	public String[] getCriteriaTypes() {
		return criteriaTypes;
	}
	
	public String getCriteriaType(int i) {
		return criteriaTypes[i];
	}

	public String[] getCriteriaNames() {
		return criteriaNames;
	}
	
	public String getCriteriaName(int i) {
		return criteriaNames[i];
	}

	public String getLowerBound(int index) {
		return lowerBounds[index];
	}

	public String getUpperBound(int index) {
		return upperBounds[index];
	}

	public String getListCriteriaOption(int list, int option) {
		return listCriteriaOptions[list][option];
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + Arrays.hashCode(criteriaNames);
		result = prime * result + Arrays.hashCode(criteriaTypes);
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(listCriteriaOptions);
		result = prime * result + Arrays.hashCode(lowerBounds);
		result = prime * result + Arrays.hashCode(upperBounds);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvailableAlert other = (AvailableAlert) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (!Arrays.equals(criteriaNames, other.criteriaNames))
			return false;
		if (!Arrays.equals(criteriaTypes, other.criteriaTypes))
			return false;
		if (id != other.id)
			return false;
		if (!Arrays.deepEquals(listCriteriaOptions, other.listCriteriaOptions))
			return false;
		if (!Arrays.equals(lowerBounds, other.lowerBounds))
			return false;
		if (!Arrays.equals(upperBounds, other.upperBounds))
			return false;
		return true;
	}
}
