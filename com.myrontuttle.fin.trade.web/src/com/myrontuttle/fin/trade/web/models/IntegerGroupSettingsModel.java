package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Group;

public class IntegerGroupSettingsModel extends Model<Integer> {

	private static final long serialVersionUID = 1L;
	
	private IModel<Group> groupModel;
	private String key;

	public IntegerGroupSettingsModel(IModel<Group> groupModel, String key) {
		this.groupModel = groupModel;
		this.key = key;
	}

	@Override
	public Integer getObject() {
		if (groupModel.getObject().getIntegerSettings().containsKey(key)) {
			return groupModel.getObject().getInteger(key);
		} else {
			return 0;
		}
	}

	@Override
	public void setObject(Integer object) {
		groupModel.getObject().setInteger(key, object);
	}
}
