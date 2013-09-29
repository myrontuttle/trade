package com.myrontuttle.fin.trade.web;

import org.apache.wicket.Page;

public class GroupPage extends Page {

	private static final long serialVersionUID = 1L;

	public GroupPage() {
		add(new NewGroupPanel("newGroup"));
		add(new GroupTablePanel("groupTable", null));
	}
}
