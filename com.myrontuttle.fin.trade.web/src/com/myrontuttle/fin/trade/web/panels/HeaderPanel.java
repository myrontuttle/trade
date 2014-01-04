package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.web.pages.MainPage;

/**
 * Navigation panel
 */
public final class HeaderPanel extends Panel {
	/**
	 * Construct.
	 * 
	 * @param id id of the component
	 * @param siteTitle title of the site
	 * @param page The example page
	 */
	public HeaderPanel(String id, String siteTitle, WebPage page) {
		super(id);

		add(new Label("siteTitle", siteTitle));
		add(new Link("mainPage") {
			@Override
			public void onClick() {
				setResponsePage(new MainPage());
			}
		});

	}
}
