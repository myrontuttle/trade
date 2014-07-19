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

import com.myrontuttle.fin.trade.adapt.AdaptDAO;
import com.myrontuttle.fin.trade.adapt.SavedScreen;
import com.myrontuttle.fin.trade.web.models.DetachableScreenModel;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;


/**
 * implementation of IDataProvider for groups that keeps track of sort information
 */
public class SortableScreenDataProvider extends SortableDataProvider<SavedScreen, String> {

	private static final long serialVersionUID = 1L;
	
	long traderId;

	/**
	 * constructor
	 */
	public SortableScreenDataProvider(long traderId) {
		
		this.traderId = traderId;
		
		// set default sort
		setSort("savedScreenId", SortOrder.ASCENDING);
	}

	protected AdaptDAO getDAO() {
		return AdaptAccess.getDAO();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<SavedScreen> iterator(long first, long count) {
		// Consider finding a subset of all groups to StrategyDAO:
		// return getDAO().findGroups(first, count, new SortParam("groupId", true)).iterator();
		return getDAO().findScreensForTrader(traderId).iterator();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	public long size() {
		return getDAO().findScreensForTrader(traderId).size();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel<SavedScreen> model(SavedScreen object)
	{
		return new DetachableScreenModel(object);
	}

}
