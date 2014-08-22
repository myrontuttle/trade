package com.myrontuttle.fin.trade.web.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.web.service.AdaptAccess;

public class ScreenedSymbolsTablePanel extends Panel {

	private static final long serialVersionUID = 1L;

	public ScreenedSymbolsTablePanel(String id, long candidateId) {
		super(id);

		List<String> symbols = AdaptAccess.getDAO().findSymbolsForCandidate(candidateId);
		
		add(new ListView<String>("symbols", symbols) {
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("symbol", item.getModelObject()));
			}
		});
	}

}
