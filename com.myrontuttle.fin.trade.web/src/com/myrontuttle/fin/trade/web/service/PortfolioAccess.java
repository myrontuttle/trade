package com.myrontuttle.fin.trade.web.service;

import com.myrontuttle.fin.trade.api.PortfolioService;

/**
 * Service locator for Portfolio Service
 */
public class PortfolioAccess {
	
	private static PortfolioService portfolioService;

	public static PortfolioService getPortfolioService() {
		return portfolioService;
	}

	public void setPortfolioService(PortfolioService portfolioService) {
		PortfolioAccess.portfolioService = portfolioService;
	}

}
