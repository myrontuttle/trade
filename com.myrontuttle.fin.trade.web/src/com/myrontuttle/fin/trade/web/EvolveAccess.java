package com.myrontuttle.fin.trade.web;

import com.myrontuttle.fin.trade.adapt.EvolveService;

/*
 * Service locator for Evolve Service
 */
public class EvolveAccess {

	private static EvolveService evolveService;

	public static EvolveService getEvolveService() {
		return evolveService;
	}

	public static void setEvolveService(EvolveService evolveService) {
		EvolveAccess.evolveService = evolveService;
	}
}
