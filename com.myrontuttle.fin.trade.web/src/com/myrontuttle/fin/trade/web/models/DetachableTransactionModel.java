package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.LoadableDetachableModel;

import com.myrontuttle.fin.trade.api.Transaction;
import com.myrontuttle.fin.trade.web.service.PortfolioAccess;

public class DetachableTransactionModel extends
		LoadableDetachableModel<Transaction> {
	
	private static final long serialVersionUID = 1L;

	private final String userId;
	private final String portfolioId;
	private final String transactionId;

	/**
	 * @param s
	 */
	public DetachableTransactionModel(Transaction t) {
		this(t.getUserId(), t.getPortfolioId(), t.getTransactionId());
	}

	/**
	 * @param id
	 */
	public DetachableTransactionModel(String userId, String portfolioId, String transactionId) {
		if (userId == null || portfolioId == null || transactionId == null) {
			throw new IllegalArgumentException();
		}
		this.userId = userId;
		this.portfolioId = portfolioId;
		this.transactionId = transactionId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(transactionId).hashCode();
	}

	/**
	 * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
	 * 
	 * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj instanceof DetachableTransactionModel) {
			DetachableTransactionModel other = (DetachableTransactionModel)obj;
			return other.transactionId == transactionId;
		}
		return false;
	}

	@Override
	protected Transaction load() {
		try {
			return PortfolioAccess.getPortfolioService().getTransaction(userId, portfolioId, transactionId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
