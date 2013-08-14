package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class Homepage extends WebPage {

    private static final long serialVersionUID = 1L;

    public Homepage() {
        add(new Label("oneComponent", "Welcome to the most simple pax-wicket application based on blueprint."));
    }
}
