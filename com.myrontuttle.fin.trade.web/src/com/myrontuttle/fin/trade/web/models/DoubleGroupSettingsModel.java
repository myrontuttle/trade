package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Group;

public class DoubleGroupSettingsModel extends Model<Double> {

	private static final long serialVersionUID = 1L;
	
	private IModel<Group> groupModel;
	private transient String key;

	public DoubleGroupSettingsModel(IModel<Group> groupModel, String key) {
		this.groupModel = groupModel;
		this.key = key;
	}

	@Override
	public void detach() {
		this.key = null;
		this.groupModel.detach();
		super.detach();
	}

	@Override
	public Double getObject() {
		if (groupModel.getObject().getDoubleSettings().containsKey(key)) {
			return groupModel.getObject().getDouble(key);
		} else {
			return 0.0;
		}
	}

	@Override
	public void setObject(Double object) {
		groupModel.getObject().setDouble(key, object);
	}
}
