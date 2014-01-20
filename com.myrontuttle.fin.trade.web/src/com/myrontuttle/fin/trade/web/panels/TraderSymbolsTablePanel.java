package com.myrontuttle.fin.trade.web.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.web.data.DBAccess;

public class TraderSymbolsTablePanel extends Panel {

	private static final long serialVersionUID = 1L;

	public TraderSymbolsTablePanel(String id, String traderId) {
		super(id);

		List<String> symbols = DBAccess.getDAO().findSymbolsForTrader(traderId);
		
		add(new ListView<String>("symbols", symbols) {
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("symbol", item.getModelObject()));
			}
		});
	}

}
