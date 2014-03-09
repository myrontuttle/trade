package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;

import com.myrontuttle.fin.trade.web.panels.CreateGroupPanel;
import com.myrontuttle.fin.trade.web.panels.EvolvePanel;
import com.myrontuttle.fin.trade.web.panels.GroupTablePanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;

public class MainPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public MainPage() {
		super();
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new EvolvePanel("evolvePanel"));
		add(new CreateGroupPanel("createGroupPanel"));
		add(new GroupTablePanel("groupTablePanel"));
	}

}
