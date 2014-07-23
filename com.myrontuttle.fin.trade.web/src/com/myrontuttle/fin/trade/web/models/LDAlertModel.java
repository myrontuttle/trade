package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.LoadableDetachableModel;

import com.myrontuttle.fin.trade.adapt.SavedAlert;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;

/**
 * detachable model for an instance of SavedAlert
 * 
 */
public class LDAlertModel extends LoadableDetachableModel<SavedAlert> {
	
	private static final long serialVersionUID = 1L;
	
	private final long id;

	/**
	 * @param s
	 */
	public LDAlertModel(SavedAlert a) {
		this(a.getSavedAlertId());
	}

	/**
	 * @param id
	 */
	public LDAlertModel(long id) {
		if (id == 0) {
			throw new IllegalArgumentException();
		}
		this.id = id;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
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
		} else if (obj instanceof LDAlertModel) {
			LDAlertModel other = (LDAlertModel)obj;
			return other.id == id;
		}
		return false;
	}

	/**
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected SavedAlert load() {
		// loads group from the database
		return AdaptAccess.getDAO().findAlert(id);
	}
}
