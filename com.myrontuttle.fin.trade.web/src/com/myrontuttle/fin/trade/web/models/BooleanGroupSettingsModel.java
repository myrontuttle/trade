package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Group;

public class BooleanGroupSettingsModel extends Model<Boolean> {

	private static final long serialVersionUID = 1L;
	
	private IModel<Group> groupModel;
	private String key;

	public BooleanGroupSettingsModel(IModel<Group> groupModel, String key) {
		this.groupModel = groupModel;
		this.key = key;
	}

	@Override
	public Boolean getObject() {
		if (groupModel.getObject().getBooleanSettings().containsKey(key)) {
			return groupModel.getObject().getBoolean(key);
		} else {
			return false;
		}
	}

	@Override
	public void setObject(Boolean object) {
		groupModel.getObject().setBoolean(key, object);
	}
}
