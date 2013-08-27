/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.myrontuttle.fin.trade.web;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

import com.myrontuttle.fin.trade.adapt.Group;

/**
 * Base page
 */
public class BasePage extends WebPage {

	private static final long serialVersionUID = 1L;

	private Group selected;

	/**
	 * Constructor
	 */
	public BasePage() {
		this(new PageParameters());

		add(new Label("selectedLabel", new PropertyModel<String>(this, "selectedGroupLabel")));
	}

	/**
	 * Constructor
	 * 
	 * @param pageParameters
	 */
	public BasePage(final PageParameters pageParameters) {
		super(pageParameters);

		final String packageName = getClass().getPackage().getName();
		add(new Header("mainNavigation", Strings.afterLast(packageName, '.'), this));
		explain();
	}

	/**
	 * Construct.
	 * 
	 * @param model
	 */
	public BasePage(IModel<?> model) {
		super(model);
	}

	/**
	 * @return string representation of selected contact property
	 */
	public String getSelectedGroupLabel() {
		if (selected == null) {
			return "No Group Selected";
		} else {
			return selected.getGroupId();
		}
	}
	
	/**
	 * Get downcast session object for easy access by subclasses
	 * 
	 * @return The session
	 */
	public List<Group> getGroups() {
		return ((WicketApplication)getApplication()).getGroups();
	}

	/**
	 * 
	 */
	class ActionPanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for contact
		 */
		public ActionPanel(String id, IModel<Group> model) {
			super(id, model);
			add(new Link("select") {
				@Override
				public void onClick() {
					selected = (Group)getParent().getDefaultModelObject();
				}
			});
		}
	}

	/**
	 * @return selected contact
	 */
	public Group getSelected()
	{
		return selected;
	}

	/**
	 * sets selected contact
	 * 
	 * @param selected
	 */
	public void setSelected(Group selected)
	{
		addStateChange();
		this.selected = selected;
	}
	
	/**
	 * Override base method to provide an explanation
	 */
	protected void explain() {
	}
}
