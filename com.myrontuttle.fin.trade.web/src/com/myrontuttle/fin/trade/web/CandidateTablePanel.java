package com.myrontuttle.fin.trade.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.GroupTablePanel.DeleteGroupPanel;

public class CandidateTablePanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CandidateTablePanel(String id, String groupId) {
		super(id);

		List<IColumn<Candidate, String>> columns = new ArrayList<IColumn<Candidate, String>>();

		columns.add(new PropertyColumn<Candidate, String>(new Model<String>("ID"), "candidateId", "candidateId"));
		columns.add(new PropertyColumn(new Model<String>("Starting Cash"), "startingCash", "startingCash"));
		columns.add(new PropertyColumn(new Model<String>("Watchlist"), "watchlistId"));
		columns.add(new PropertyColumn(new Model<String>("Portfolio"), "portfolioId"));

		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Delete")) {
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem, String componentId,
				IModel<Candidate> model) {
				cellItem.add(new DeleteCandidatePanel(componentId, model));
			}
		});
		
		columns.add(new PropertyColumn(new Model<String>("Genome"), "genomeString"));

		DataTable dataTable = new DefaultDataTable<Candidate, String>("candidates", columns,
				new SortableCandidateDataProvider(groupId), 5);

		add(dataTable);
	}
	
	class DeleteCandidatePanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for contact
		 */
		public DeleteCandidatePanel(String id, IModel<Candidate> model) {
			super(id, model);

			final Form<Candidate> form = new Form<Candidate>("deleteCandidateForm", model);
			form.add(new Button("delete") {
				public void onSubmit() {
					Candidate candidate = ((Candidate)getParent().getDefaultModelObject());
					DBAccess.getDAO().removeCandidate(candidate.getCandidateId());
				}
			});
			add(form);
		}
	}

}
