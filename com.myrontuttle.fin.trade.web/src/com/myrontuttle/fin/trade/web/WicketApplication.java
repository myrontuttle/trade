package com.myrontuttle.fin.trade.web;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.ServerAndClientTimeFilter;

import com.myrontuttle.fin.trade.adapt.StrategyDAO;
import com.myrontuttle.fin.trade.adapt.Group;

public class WicketApplication extends WebApplication {

	@Inject
    @Named("strategyDAO")
    private StrategyDAO strategyDAO;
	
	public WicketApplication() {
    }

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init()
	{
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);

		getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
	}

	public List<Group> getGroups() {
		return strategyDAO.findGroups();
	}
	
    @Override
    public Class<Homepage> getHomePage() {
        return Homepage.class;
    }
}
