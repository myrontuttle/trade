package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.web.models.LDCandidateModel;

public class CandidateDetailsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public CandidateDetailsPanel(String id, long candidateId) {
		super(id);
		
		LDCandidateModel candidate = new LDCandidateModel(candidateId);
		
		add(new Label("candidateId", candidateId));
		add(new Label("groupId", candidate.getObject().getGroupId()));
		add(new Label("bestInGroup", candidate.getObject().isBestInGroup()));
		add(new Label("genome", candidate.getObject().getGenomeString()));
		add(new Label("bornIn", candidate.getObject().getBornInGen()));
		add(new Label("lastExpressed", candidate.getObject().getLastExpressedGen()));
		add(new Label("watchlistId", candidate.getObject().getWatchlistId()));
		add(new Label("portfolioId", candidate.getObject().getPortfolioId()));
		
	}

}
