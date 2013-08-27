package com.myrontuttle.fin.trade.web;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.ServerAndClientTimeFilter;

import com.myrontuttle.fin.trade.adapt.Evolver;
import com.myrontuttle.fin.trade.adapt.Group;

public class WicketApplication extends WebApplication {

	@Inject
    @Named("defaultEvolve")
    private Evolver evolver;
	
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
		return evolver.findGroups();
	}
	
    @Override
    public Class<Homepage> getHomePage() {
        return Homepage.class;
    }
}
