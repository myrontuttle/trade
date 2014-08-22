package com.myrontuttle.fin.trade.web.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.TradeParameter;
import com.myrontuttle.fin.trade.web.data.SortableTradeParameterDataProvider;

public class TradeParametersTablePanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TradeParametersTablePanel(String id, long candidateId) {
		super(id);

		List<IColumn<TradeParameter, String>> columns = new ArrayList<IColumn<TradeParameter, String>>();

		columns.add(new PropertyColumn<TradeParameter, String>(new Model<String>("ID"), "tradeParameterId", 
						"tradeParameterId"));
		columns.add(new PropertyColumn(new Model<String>("Parameter"), "parameter"));

		DataTable dataTable = new DefaultDataTable<TradeParameter, String>("tradeParameters", columns,
				new SortableTradeParameterDataProvider(candidateId), 20);

		add(dataTable);
	}

}
