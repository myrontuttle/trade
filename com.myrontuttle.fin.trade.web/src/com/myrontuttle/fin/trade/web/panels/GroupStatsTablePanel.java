package com.myrontuttle.fin.trade.web.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.GroupStats;
import com.myrontuttle.fin.trade.web.data.SortableGroupStatsDataProvider;

public class GroupStatsTablePanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GroupStatsTablePanel(String id, long groupId) {
		super(id);

		List<IColumn<GroupStats, String>> columns = new ArrayList<IColumn<GroupStats, String>>();

		columns.add(new PropertyColumn<GroupStats, String>(new Model<String>("ID"), "statsId", "statsId"));
		columns.add(new PropertyColumn(new Model<String>("Generation"), "generationNumber"));
		columns.add(new PropertyColumn(new Model<String>("Recorded"), "recordedTime"));
		columns.add(new PropertyColumn(new Model<String>("Best Fitness"), "bestCandidateFitness"));
		columns.add(new PropertyColumn(new Model<String>("Mean Fitness"), "meanFitness"));
		columns.add(new PropertyColumn(new Model<String>("Fitness StdDev"), "fitnessStandardDeviation"));
		columns.add(new PropertyColumn(new Model<String>("Variability"), "variability"));

		DataTable dataTable = new DefaultDataTable<GroupStats, String>("groupStats", columns,
				new SortableGroupStatsDataProvider(groupId), 5);

		add(dataTable);
	}

}
