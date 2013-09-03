package com.myrontuttle.fin.trade.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
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
	public GroupTablePanel(String id, IModel<?> model) {
		super(id, model);

		List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

		columns.add(new AbstractColumn<Group>(new Model<String>("Actions")) {
			public void populateItem(Item<ICellPopulator<Group>> cellItem, String componentId,
				IModel<Group> model) {
				cellItem.add(
					new Link("view") {
						@Override
						public void onClick() {
							PageParameters pars = new PageParameters();
							pars.add("groupId", ((Group)getParent().getDefaultModelObject()).getGroupId());
							GroupDetailPage gdp = new GroupDetailPage();
							setResponsePage(gdp);
						}
					});
			}
		});

		columns.add(new PropertyColumn(new Model<String>("ID"), "groupId") {
			@Override
			public String getCssClass() {
				return "numeric";
			}
		});

		columns.add(new PropertyColumn(new Model<String>("Start Time"), "startTime", "startTime"));

		columns.add(new PropertyColumn(new Model<String>("Frequency"), "frequency", "frequency") {
			@Override
			public String getCssClass() {
				return "last-name";
			}
		});

		columns.add(new PropertyColumn(new Model<String>("Alert Address"), "alertAddress"));
		columns.add(new PropertyColumn(new Model<String>("Expression"), "expressionStrategy"));
		columns.add(new PropertyColumn(new Model<String>("Evaluation"), "evaluationStrategy"));

		add(new DefaultDataTable("table", columns, new SortableGroupDataProvider(), 8));
	}

}
