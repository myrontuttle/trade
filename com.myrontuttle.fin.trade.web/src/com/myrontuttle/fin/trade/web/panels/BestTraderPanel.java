package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.web.pages.TraderPage;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;

public class BestTraderPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id component id
	 * @param model model for Group
	 */
	public BestTraderPanel(String id, final long groupId) {
		super(id);
		add(new Link("bestTrader") {
			
			@Override
			public void onClick() {
				long traderId = AdaptAccess.getDAO().getBestTrader(groupId).getTraderId();
				TraderPage tp = new TraderPage(traderId);
				setResponsePage(tp);
			}
			
			@Override
			public boolean isVisible() {
				// Make visible only if there is an actual Best Trader
				return (AdaptAccess.getDAO().getBestTrader(groupId) != null);
			}
		});
	}
}
