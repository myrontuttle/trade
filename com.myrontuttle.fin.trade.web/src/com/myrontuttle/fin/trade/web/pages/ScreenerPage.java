package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;

import com.myrontuttle.fin.trade.web.panels.FixedCriteriaPanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.InitScreenerPanel;

public class ScreenerPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public ScreenerPage() {
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new InitScreenerPanel("initScreener"));
		add(new FixedCriteriaPanel("fixedCriteria"));
	}

}
