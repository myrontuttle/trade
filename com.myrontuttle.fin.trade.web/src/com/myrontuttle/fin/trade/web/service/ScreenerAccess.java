package com.myrontuttle.fin.trade.web.service;

import com.myrontuttle.fin.trade.api.ScreenerService;

/**
 * Service locator for Screener Service
 */
public class ScreenerAccess {
	
	private static ScreenerService screenerService;

	public static ScreenerService getScreenerService() {
		return screenerService;
	}

	public void setScreenerService(ScreenerService screenerService) {
		ScreenerAccess.screenerService = screenerService;
	}

}
