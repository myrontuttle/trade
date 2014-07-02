package com.myrontuttle.fin.trade.api;

/**
 * An indicator that a symbol has met a certain condition with certain parameters
 * @author Myron Tuttle
 */
public interface SelectedAlert {
	
	public int getAlertId();

	public String getCondition();

	public String getSymbol();

	public double getParam(int index);
	
	public double[] getParams();

}
