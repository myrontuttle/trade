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

import java.util.Iterator;

//import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.StrategyDAO;


/**
 * Implementation of IDataProvider that retrieves contacts from the contact database.
 * 
 * @author igor
 * 
 */
public class GroupDataProvider implements IDataProvider<Group> {
	
	private static final long serialVersionUID = 1L;

	protected StrategyDAO getDAO() {
		return DBAccess.getDAO();
	}

	/**
	 * retrieves groups from database starting with index <code>first</code> and ending with
	 * <code>first+count</code>
	 * 
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	public Iterator<Group> iterator(long first, long count) {
		// Consider finding a subset of all groups to StrategyDAO:
		// return getDAO().findGroups(first, count, new SortParam("groupId", true)).iterator();
		return getDAO().findGroups().iterator();
	}

	/**
	 * returns total number of groups in the database
	 * 
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	public long size() {
		return getDAO().findGroups().size();
	}

	/**
	 * wraps retrieved group pojo with a wicket model
	 * 
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	public IModel<Group> model(Group object) {
		return new DetachableGroupModel(object);
	}

	/**
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach() {
	}

}
