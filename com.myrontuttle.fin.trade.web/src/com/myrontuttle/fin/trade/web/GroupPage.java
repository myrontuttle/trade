package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.WebPage;

public class GroupPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public GroupPage() {
		super();
		add(new Header("mainNavigation", "Adaptive Trader", this));
		add(new EvolvePanel("evolvePanel"));
		add(new NewGroupPanel("newGroupPanel"));
		add(new GroupTablePanel("groupTablePanel"));
	}

}
