package com.myrontuttle.fin.trade.web;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
//import org.apache.wicket.response.filter.ServerAndClientTimeFilter;

import com.myrontuttle.fin.trade.adapt.StrategyDAO;

public class WicketApplication extends WebApplication {

	@Inject
    @Named("StrategyDAO")
    private StrategyDAO strategyDAO;
	
	public WicketApplication() {
    }

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		//getDebugSettings().setDevelopmentUtilitiesEnabled(true);

		//getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
	}
	
	/*
	public void setStrategyDAO(StrategyDAO sdao) {
		this.strategyDAO = sdao;
	}
	*/

	public StrategyDAO getDAO() {
		if (strategyDAO == null) {
			System.out.println("StrategyDAO is null");
		}
		return strategyDAO;
	}
	
    @Override
    public Class<? extends Page> getHomePage() {
        return GroupPage.class;
    }
}
