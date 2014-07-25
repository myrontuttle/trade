package com.myrontuttle.fin.trade.web.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.api.Transaction;
import com.myrontuttle.fin.trade.web.data.SortableTransactionDataProvider;
import com.myrontuttle.fin.trade.web.models.LDCandidateModel;

public class TransactionsTablePanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TransactionsTablePanel(String id, long candidateId) {
		super(id);

		LDCandidateModel candidateModel = new LDCandidateModel(candidateId);
		
		List<IColumn<Transaction, String>> columns = new ArrayList<IColumn<Transaction, String>>();
		
		columns.add(new PropertyColumn(new Model<String>("DateTime"), "dateTime", "dateTime"));
		columns.add(new PropertyColumn(new Model<String>("OrderType"), "orderType"));
		columns.add(new PropertyColumn(new Model<String>("Symbol"), "symbol"));
		columns.add(new PropertyColumn(new Model<String>("Quantity"), "quantity"));
		columns.add(new PropertyColumn(new Model<String>("Value"), "value"));

		DataTable dataTable = new DefaultDataTable<Transaction, String>("transactions", columns,
				new SortableTransactionDataProvider(candidateId,
						candidateModel.getObject().getPortfolioId()), 20);

		add(dataTable);
	}

}
