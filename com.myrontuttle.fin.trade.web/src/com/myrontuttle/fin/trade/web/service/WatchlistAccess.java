package com.myrontuttle.fin.trade.web.service;

import com.myrontuttle.fin.trade.api.WatchlistService;

/**
 * Service locator for Watchlist Service
 */
public class WatchlistAccess {
	
	private static WatchlistService watchlistService;

	public static WatchlistService getWatchlistService() {
		return watchlistService;
	}

	public void setWatchlistService(WatchlistService watchlistService) {
		WatchlistAccess.watchlistService = watchlistService;
	}

}
