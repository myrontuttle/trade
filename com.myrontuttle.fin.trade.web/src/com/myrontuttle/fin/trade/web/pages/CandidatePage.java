package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;

import com.myrontuttle.fin.trade.web.panels.CandidateDetailsPanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.SavedAlertsTablePanel;
import com.myrontuttle.fin.trade.web.panels.SavedScreensTablePanel;
import com.myrontuttle.fin.trade.web.panels.ScreenedSymbolsTablePanel;
import com.myrontuttle.fin.trade.web.panels.TradeParametersTablePanel;
import com.myrontuttle.fin.trade.web.panels.TransactionsTablePanel;

public class CandidatePage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	public CandidatePage(long candidateId) {
		
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new CandidateDetailsPanel("candidateDetails", candidateId));
		add(new SavedScreensTablePanel("savedScreensTable", candidateId));
		add(new ScreenedSymbolsTablePanel("screenedSymbolsTable", candidateId));
		add(new TransactionsTablePanel("portfolioPanel", candidateId));
		add(new SavedAlertsTablePanel("savedAlertsTable", candidateId));
		add(new TradeParametersTablePanel("tradeParametersTable", candidateId));
		
	}

}
