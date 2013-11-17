package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.WebPage;

public class Scratch extends WebPage {

	private static final long serialVersionUID = 1L;
	
	public Scratch() {
		super();
		
		add(new NewGroupPanel("newGroupPanel"));
		
		add(new GroupTablePanel("groupTablePanel"));
	}
	
}
