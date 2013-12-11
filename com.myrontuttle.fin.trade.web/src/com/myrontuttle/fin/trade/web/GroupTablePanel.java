package com.myrontuttle.fin.trade.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myrontuttle.fin.trade.adapt.Group;

public class GroupTablePanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GroupTablePanel(String id) {
		super(id);

		List<IColumn<Group, String>> columns = new ArrayList<IColumn<Group, String>>();
		
		columns.add(new PropertyColumn<Group, String>(new Model<String>("ID"), "groupId", "groupId"));
		columns.add(new PropertyColumn(new Model<String>("Last Updated"), "updatedTime", "updatedTime"));
		columns.add(new PropertyColumn(new Model<String>("Alert Address"), "alertAddress"));
		columns.add(new PropertyColumn(new Model<String>("Frequency"), "frequency"));
		columns.add(new PropertyColumn(new Model<String>("Expression"), "expressionStrategy"));
		columns.add(new PropertyColumn(new Model<String>("Evaluation"), "evaluationStrategy"));
		columns.add(new PropertyColumn(new Model<String>("Active"), "active"));
		columns.add(new PropertyColumn(new Model<String>("Size"), "size"));
		columns.add(new PropertyColumn(new Model<String>("Elites"), "eliteCount"));
		columns.add(new PropertyColumn(new Model<String>("Gene Upper"), "geneUpperValue"));
		columns.add(new PropertyColumn(new Model<String>("Screens"), "numberOfScreens"));
		columns.add(new PropertyColumn(new Model<String>("Symbols/Screen"), "maxSymbolsPerScreen"));
		columns.add(new PropertyColumn(new Model<String>("Alerts/Symbol"), "alertsPerSymbol"));
		columns.add(new PropertyColumn(new Model<String>("Genome Length"), "genomeLength"));

		columns.add(new AbstractColumn<Group, String>(new Model<String>("Stats")) {
			public void populateItem(Item<ICellPopulator<Group>> cellItem, String componentId,
				IModel<Group> model) {
				cellItem.add(
					new Link("view") {
						@Override
						public void onClick() {
							String groupId =  ((Group)getParent().getDefaultModelObject()).getGroupId();
							//PageParameters pars = new PageParameters();
							//pars.add("groupId", groupId);
							GroupPage gp = new GroupPage(groupId);
							setResponsePage(gp);
						}
					});
			}
		});
		
		DataTable dataTable = new DefaultDataTable<Group, String>("groups", columns,
				new SortableGroupDataProvider(), 5);

		add(dataTable);
	}

}
