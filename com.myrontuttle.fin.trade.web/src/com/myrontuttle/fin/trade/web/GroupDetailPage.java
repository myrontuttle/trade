package com.myrontuttle.fin.trade.web;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myrontuttle.fin.trade.adapt.Group;

public class GroupDetailPage extends BasePage {

	private static final long serialVersionUID = 1L;

	public GroupDetailPage() {
		// TODO Auto-generated constructor stub
	}

	public GroupDetailPage(PageParameters pageParameters) {
		super(pageParameters);
		Group group = DBAccess.getDAO().findGroup(pageParameters.get("groupId").toString());
		//TODO: Show group details
	}

	public GroupDetailPage(IModel<?> model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

}
