package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.TransactionsTablePanel;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;

public class CandidatePage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	Candidate candidate;
	
	public CandidatePage(long candidateId) {

		if (candidateId != 0) {
			candidate = AdaptAccess.getDAO().findCandidate(candidateId);
		}
		if (candidate == null) {
			candidate = new Candidate();
		}

		final IModel<Candidate> candidateModel = new CompoundPropertyModel<Candidate>(candidate);

		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new TransactionsTablePanel("portfolioPanel", candidateModel));
		
	}

}
