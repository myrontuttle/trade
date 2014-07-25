package com.myrontuttle.fin.trade.web.panels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.data.SortableGroupDataProvider;
import com.myrontuttle.fin.trade.web.pages.GroupPage;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;

public class GroupTablePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GroupTablePanel(String id) {
		super(id);

		List<IColumn<Group, String>> columns = new ArrayList<IColumn<Group, String>>();
		
		columns.add(new PropertyColumn<Group, String>(new Model<String>("ID"), "groupId", "groupId"));
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Alert User")) {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getStringSettings().containsKey("Alert.User")) {
					cellItem.add(
						new Label(componentId, groupModel.getObject().getString("Alert.User")));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Frequency")) {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getStringSettings().containsKey("Evolve.Frequency")) {
					cellItem.add(
						new Label(componentId, groupModel.getObject().getString("Evolve.Frequency")));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Active")) {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getBooleanSettings().containsKey("Evolve.Active")) {
					cellItem.add(
						new Label(componentId, groupModel.getObject().getString("Evolve.Active")));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Size"), "size") {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getIntegerSettings().containsKey("Evolve.Size")) {
					cellItem.add(
						new Label(componentId,
							String.valueOf(
								groupModel.getObject().getInteger("Evolve.Size"))));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Elites"), "eliteCount") {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getIntegerSettings().containsKey("Evolve.EliteCount")) {
					cellItem.add(
							new Label(componentId,
								String.valueOf(
									groupModel.getObject().getInteger("Evolve.EliteCount"))));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Created On"), "startTime") {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getLongSettings().containsKey("Evolve.StartTime")) {
					Date date = new Date(groupModel.getObject().getLong("Evolve.StartTime"));
					cellItem.add(new Label(componentId, sdf.format(date)));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Generation"), "generation") {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getIntegerSettings().containsKey("Evolve.Generation")) {
					cellItem.add(
							new Label(componentId, 
								String.valueOf(
									groupModel.getObject().getInteger("Evolve.Generation"))));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Last Updated"), "updatedTime") {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getLongSettings().containsKey("Evolve.UpdatedTime")) {
					Date date = new Date(groupModel.getObject().getLong("Evolve.UpdatedTime"));
					cellItem.add(new Label(componentId, sdf.format(date)));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});
		columns.add(new AbstractColumn<Group, String>(new Model<String>("Variability"), "variability") {
			@Override
			public void populateItem(Item<ICellPopulator<Group>> cellItem,
					String componentId, IModel<Group> groupModel) {
				if (groupModel.getObject().getDoubleSettings().containsKey("Evolve.Variability")) {
					cellItem.add(
							new Label(componentId, 
								String.valueOf(
									groupModel.getObject().getDouble("Express.Variability"))));
				} else {
					cellItem.add(new Label(componentId, ""));
				}
			}
		});

		columns.add(new AbstractColumn<Group, String>(new Model<String>("Details")) {
			public void populateItem(Item<ICellPopulator<Group>> cellItem, String componentId,
				IModel<Group> model) {
				cellItem.add(new DetailPanel(componentId, model.getObject().getGroupId()));
			}
		});

		columns.add(new AbstractColumn<Group, String>(new Model<String>("Evolve")) {
			public void populateItem(Item<ICellPopulator<Group>> cellItem, String componentId,
				IModel<Group> model) {
				cellItem.add(new EvolveGroupPanel(componentId, model.getObject().getGroupId()));
			}
		});

		columns.add(new AbstractColumn<Group, String>(new Model<String>("Best Trader")) {
			public void populateItem(Item<ICellPopulator<Group>> cellItem, String componentId,
				IModel<Group> model) {
				cellItem.add(new BestTraderPanel(componentId, model.getObject().getGroupId()));
			}
		});

		columns.add(new AbstractColumn<Group, String>(new Model<String>("Delete Group")) {
			public void populateItem(Item<ICellPopulator<Group>> cellItem, String componentId,
				IModel<Group> model) {
				cellItem.add(new DeleteGroupPanel(componentId, model.getObject().getGroupId()));
			}
		});
		
		DataTable dataTable = new DefaultDataTable<Group, String>("groups", columns,
				new SortableGroupDataProvider(), 5);

		add(dataTable);
	}

	class DetailPanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for contact
		 */
		public DetailPanel(String id, final long groupId) {
			super(id);
			add(new Link("details") {
				@Override
				public void onClick() {
					GroupPage gp = new GroupPage(groupId);
					setResponsePage(gp);
				}
			});
		}
	}

	class DeleteGroupPanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for contact
		 */
		public DeleteGroupPanel(String id, final long groupId) {
			super(id);

			final Form<Group> form = new Form<Group>("deleteGroupForm");
			form.add(new Button("delete") {
				public void onSubmit() {
					EvolveAccess.getEvolveService().deleteGroupExpression(groupId);
					AdaptAccess.getDAO().removeGroup(groupId);
				}
			});
			add(form);
		}
	}
}