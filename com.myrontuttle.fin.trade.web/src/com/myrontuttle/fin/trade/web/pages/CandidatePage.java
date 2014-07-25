package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;

import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.TransactionsTablePanel;

public class CandidatePage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	public CandidatePage(long candidateId) {
		
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new TransactionsTablePanel("portfolioPanel", candidateId));
		
	}

}
