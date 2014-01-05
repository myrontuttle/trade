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
package com.myrontuttle.fin.trade.web.data;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.GroupDAO;
import com.myrontuttle.fin.trade.adapt.SavedAlert;
import com.myrontuttle.fin.trade.web.models.DetachableAlertModel;


/**
 * implementation of IDataProvider for groups that keeps track of sort information
 */
public class SortableAlertDataProvider extends SortableDataProvider<SavedAlert, String> {

	private static final long serialVersionUID = 1L;
	
	String traderId;

	/**
	 * constructor
	 */
	public SortableAlertDataProvider(String traderId) {
		
		this.traderId = traderId;
		
		// set default sort
		setSort("savedAlertId", SortOrder.ASCENDING);
	}

	protected GroupDAO getDAO() {
		return DBAccess.getDAO();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<SavedAlert> iterator(long first, long count) {
		// Consider finding a subset of all groups to StrategyDAO:
		// return getDAO().findGroups(first, count, new SortParam("groupId", true)).iterator();
		return getDAO().findAlertsForTrader(traderId).iterator();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	public long size() {
		return getDAO().findAlertsForTrader(traderId).size();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel<SavedAlert> model(SavedAlert object)
	{
		return new DetachableAlertModel(object);
	}

}
