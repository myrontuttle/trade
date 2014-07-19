package com.myrontuttle.fin.trade.web.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.SavedAlert;
import com.myrontuttle.fin.trade.web.data.SortableAlertDataProvider;

public class TraderAlertsTablePanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TraderAlertsTablePanel(String id, long traderId) {
		super(id);

		List<IColumn<SavedAlert, String>> columns = new ArrayList<IColumn<SavedAlert, String>>();

		columns.add(new PropertyColumn<SavedAlert, String>(new Model<String>("ID"), "savedAlertId", "savedAlertId"));
		columns.add(new PropertyColumn(new Model<String>("Condition"), "condition"));
		columns.add(new PropertyColumn(new Model<String>("Symbol"), "symbol"));
		columns.add(new PropertyColumn(new Model<String>("Params"), "params"));

		DataTable dataTable = new DefaultDataTable<SavedAlert, String>("traderAlerts", columns,
				new SortableAlertDataProvider(traderId), 20);

		add(dataTable);
	}

}
