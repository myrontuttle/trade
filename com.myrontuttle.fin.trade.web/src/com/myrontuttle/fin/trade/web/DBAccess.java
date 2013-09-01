package com.myrontuttle.fin.trade.web;

import org.apache.wicket.Application;

import com.myrontuttle.fin.trade.adapt.StrategyDAO;

/**
 * service locator class for data access object
 */
public class DBAccess
{
	/**
	 * @return groups
	 */
	public static StrategyDAO getDAO() {
		WicketApplication app = (WicketApplication)Application.get();
		return app.getDAO();
	}
}
