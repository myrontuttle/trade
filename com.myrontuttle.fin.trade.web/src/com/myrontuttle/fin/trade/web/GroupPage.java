package com.myrontuttle.fin.trade.web;

import org.apache.wicket.Page;

import com.myrontuttle.fin.trade.adapt.Group;

public class GroupPage extends Page {

	private static final long serialVersionUID = 1L;

	public GroupPage() {
		add(new NewGroupPanel("newGroup", new DetachableGroupModel(new Group())));
		add(new GroupTablePanel("groupTable", null));
	}
}
