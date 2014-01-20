package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;

import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.TraderAlertsTablePanel;
import com.myrontuttle.fin.trade.web.panels.TraderInstructionsTablePanel;
import com.myrontuttle.fin.trade.web.panels.TraderScreensTablePanel;
import com.myrontuttle.fin.trade.web.panels.TraderSymbolsTablePanel;

public class TraderPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public TraderPage(String traderId) {
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new TraderScreensTablePanel("traderScreensTable", traderId));
		add(new TraderSymbolsTablePanel("traderSymbolsTable", traderId));
		add(new TraderAlertsTablePanel("traderAlertsTable", traderId));
		add(new TraderInstructionsTablePanel("traderInstructionsTable", traderId));
	}

}
