package com.myrontuttle.fin.trade.web.service;

import com.myrontuttle.fin.trade.adapt.AdaptDAO;

/**
 * service locator class for data access object
 */
public class AdaptAccess {

	private static AdaptDAO adaptDAO;
	
	public static AdaptDAO getDAO() {
		return adaptDAO;
	}

    public void setAdaptDAO(AdaptDAO sdao) {
    	AdaptAccess.adaptDAO = sdao;
    }
}
