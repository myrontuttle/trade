package com.myrontuttle.fin.trade.web.data;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.api.Transaction;
import com.myrontuttle.fin.trade.web.models.LDTransactionModel;
import com.myrontuttle.fin.trade.web.service.PortfolioAccess;

public class SortableTransactionDataProvider extends SortableDataProvider<Transaction, String> {

	private static final long serialVersionUID = 1L;
	
	long userId;
	String portfolioId;

	/**
	 * constructor
	 */
	public SortableTransactionDataProvider(long userId, String portfolioId) {
		
		this.userId = userId;
		this.portfolioId = portfolioId;
		
		// set default sort
		setSort("transactionId", SortOrder.ASCENDING);
	}

	@Override
	public Iterator<? extends Transaction> iterator(long arg0, long arg1) {
		try {
			return PortfolioAccess.getPortfolioService().getTransactions(userId, portfolioId).iterator();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IModel<Transaction> model(Transaction t) {
		return new LDTransactionModel(t);
	}

	@Override
	public long size() {
		try {
			return PortfolioAccess.getPortfolioService().getTransactions(userId, portfolioId).size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
