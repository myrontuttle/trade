package com.myrontuttle.fin.trade.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
//import org.apache.wicket.response.filter.ServerAndClientTimeFilter;

import com.myrontuttle.fin.trade.web.pages.MainPage;

public class Application extends WebApplication {

	public Application() {}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();
		getResourceSettings().setResourcePollFrequency(null);
		//getDebugSettings().setDevelopmentUtilitiesEnabled(true);

		//getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
	}
	
    @Override
    public Class<? extends Page> getHomePage() {
        return MainPage.class;
    }
}
