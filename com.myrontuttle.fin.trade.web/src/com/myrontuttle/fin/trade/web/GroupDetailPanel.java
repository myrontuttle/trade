package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

import com.myrontuttle.fin.trade.adapt.Group;

public class GroupDetailPanel extends Panel {

	private static final long serialVersionUID = 1L;
	

	public GroupDetailPanel(String id) {
		super(id);
		if (id != null) {
			Group group = DBAccess.getDAO().findGroup(id);
		}

		add(new DataView<Group>("simple", new GroupDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<Group> item) {
				/*
				Group group = item.getModelObject();
				item.add(new ActionPanel("actions", item.getModel()));
				item.add(new Label("groupid", String.valueOf(group.getGroupId())));
				item.add(new Label("starttime", group.getStartTime().toString()));
				item.add(new Label("frequency", group.getFrequency()));
				item.add(new Label("alertaddress", group.getAlertAddress()));
				item.add(new Label("expressionstrategy", group.getExpressionStrategy()));
				item.add(new Label("evaluationstrategy", group.getEvaluationStrategy()));
				item.add(new Label("size", String.valueOf(group.getSize())));

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				*/
			}
		});
	}
}
