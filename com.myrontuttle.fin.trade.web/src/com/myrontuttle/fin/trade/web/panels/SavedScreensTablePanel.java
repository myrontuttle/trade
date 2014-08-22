package com.myrontuttle.fin.trade.web.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.SavedScreen;
import com.myrontuttle.fin.trade.web.data.SortableScreenDataProvider;

public class SavedScreensTablePanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SavedScreensTablePanel(String id, long traderId) {
		super(id);

		List<IColumn<SavedScreen, String>> columns = new ArrayList<IColumn<SavedScreen, String>>();

		columns.add(new PropertyColumn<SavedScreen, String>(new Model<String>("ID"), "savedScreenId", "savedScreenId"));
		columns.add(new PropertyColumn(new Model<String>("Name"), "name"));
		columns.add(new PropertyColumn(new Model<String>("Value"), "value"));
		columns.add(new PropertyColumn(new Model<String>("Operator"), "argsOperator"));

		DataTable dataTable = new DefaultDataTable<SavedScreen, String>("savedScreens", columns,
				new SortableScreenDataProvider(traderId), 20);

		add(dataTable);
	}

}
