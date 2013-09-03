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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.myrontuttle.fin.trade.adapt.Group;
/**
 * Page that demonstrates a simple dataview.
 * 
 * @author igor
 */
public class SimplePage extends BasePage
{
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public SimplePage() {
		
		add(new DataView<Group>("simple", new GroupDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final Item<Group> item) {
				/*
				Group group = item.getModelObject();
				item.add(new ActionPanel("actions", item.getModel()));
				item.add(new Label("groupid", String.valueOf(group.getGroupId())));
				item.add(new Label("starttime", group.getStartTime().toString()));
				item.add(new Label("frequency", group.getFrequency()));
				item.add(new Label("alertaddress", group.getAlertAddress()));
				item.add(new Label("expressionstrategy", group.getExpressionStrategy()));
				item.add(new Label("evaluationstrategy", group.getEvaluationStrategy()));
				item.add(new Label("size", String.valueOf(group.getSize())));

				item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
				*/
			}
		});
	}
}
