package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class BasicPage extends WebPage {

	public BasicPage() {
		add(new Label("message", "Hello World!"));
	}

}
