package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.data.DBAccess;
import com.myrontuttle.fin.trade.web.pages.TraderPage;

public class BestTraderPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id component id
	 * @param model model for Group
	 */
	public BestTraderPanel(String id, IModel<Group> model) {
		super(id, model);
		add(new Link("bestTrader") {
			
			@Override
			public void onClick() {
				Group group = ((Group)getParent().getDefaultModelObject());
				String traderId = DBAccess.getDAO().getBestTrader(group.getGroupId()).getTraderId();
				TraderPage tp = new TraderPage(traderId);
				setResponsePage(tp);
			}
			
			@Override
			public boolean isVisible() {
				// Make visible only if there is an actual Best Trader
				Group group = ((Group)getParent().getDefaultModelObject());
				return (DBAccess.getDAO().getBestTrader(group.getGroupId()) != null);
			}
		});
	}
}
