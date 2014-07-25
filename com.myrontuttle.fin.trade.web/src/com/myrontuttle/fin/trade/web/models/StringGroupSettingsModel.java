package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.myrontuttle.fin.trade.adapt.Group;

public class StringGroupSettingsModel extends Model<String> {
			
	private static final long serialVersionUID = 1L;
	
	private IModel<Group> groupModel;
	private transient String key;

	public StringGroupSettingsModel(IModel<Group> groupModel, String key) {
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
	public String getObject() {
		return groupModel.getObject().getString(key);
	}

	@Override
	public void setObject(String object) {
		groupModel.getObject().setString(key, object);
	}
}
