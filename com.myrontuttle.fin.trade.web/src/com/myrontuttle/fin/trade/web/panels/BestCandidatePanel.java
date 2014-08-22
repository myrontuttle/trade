package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.web.pages.CandidatePage;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;

public class BestCandidatePanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id component id
	 * @param model model for Group
	 */
	public BestCandidatePanel(String id, final long groupId) {
		super(id);
		add(new Link("bestCandidate") {
			
			@Override
			public void onClick() {
				long candidateId = AdaptAccess.getDAO().getBestCandidate(groupId).getCandidateId();
				CandidatePage cp = new CandidatePage(candidateId);
				setResponsePage(cp);
			}
			
			@Override
			public boolean isVisible() {
				// Make visible only if there is an actual Best Trader
				return (AdaptAccess.getDAO().getBestCandidate(groupId) != null);
			}
		});
	}
}
