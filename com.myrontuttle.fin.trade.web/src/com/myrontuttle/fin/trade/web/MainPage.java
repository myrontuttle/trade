package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.WebPage;

public class MainPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public MainPage() {
		super();
		add(new Header("mainNavigation", "Adaptive Trader", this));
		add(new EvolvePanel("evolvePanel"));
		add(new CreateGroupPanel("createGroupPanel"));
		add(new GroupTablePanel("groupTablePanel"));
	}

}
