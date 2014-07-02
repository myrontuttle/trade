package com.myrontuttle.fin.trade.api;

/**
 * A criteria by which an equity can by screened
 * @author Myron Tuttle
 */
public interface SelectedScreenCriteria {
	
	public String getName();
	
	public String getValue();

	public String getArgsOperator();

}