package com.myrontuttle.fin.trade.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Candidate;

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
		columns.add(new PropertyColumn(new Model<String>("Genome"), "genomeString"));

		DataTable dataTable = new DefaultDataTable<Candidate, String>("candidates", columns,
				new SortableCandidateDataProvider(groupId), 5);

		add(dataTable);
	}

}
