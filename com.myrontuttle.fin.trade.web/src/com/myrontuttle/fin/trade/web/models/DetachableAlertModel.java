package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.LoadableDetachableModel;

import com.myrontuttle.fin.trade.adapt.SavedAlert;
import com.myrontuttle.fin.trade.web.data.DBAccess;

/**
 * detachable model for an instance of SavedAlert
 * 
 */
public class DetachableAlertModel extends LoadableDetachableModel<SavedAlert> {
	
	private static final long serialVersionUID = 1L;
	
	private final String id;

	/**
	 * @param s
	 */
	public DetachableAlertModel(SavedAlert a) {
		this(a.getSavedAlertId());
	}

	/**
	 * @param id
	 */
	public DetachableAlertModel(String id) {
		if (id == null) {
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
		} else if (obj instanceof DetachableAlertModel) {
			DetachableAlertModel other = (DetachableAlertModel)obj;
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
		return DBAccess.getDAO().findAlert(id);
	}
}
