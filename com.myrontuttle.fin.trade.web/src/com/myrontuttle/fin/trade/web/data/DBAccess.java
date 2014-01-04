package com.myrontuttle.fin.trade.web.data;

import com.myrontuttle.fin.trade.adapt.GroupDAO;

/**
 * service locator class for data access object
 */
public class DBAccess {

	private static GroupDAO groupDAO;
	
	public static GroupDAO getDAO() {
		return groupDAO;
	}

    public void setGroupDAO(GroupDAO sdao) {
    	DBAccess.groupDAO = sdao;
    }
}
