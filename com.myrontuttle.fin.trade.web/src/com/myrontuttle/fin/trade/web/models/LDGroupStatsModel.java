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
package com.myrontuttle.fin.trade.web.models;

import org.apache.wicket.model.LoadableDetachableModel;

import com.myrontuttle.fin.trade.adapt.GroupStats;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;

/**
 * detachable model for an instance of group
 * 
 */
public class LDGroupStatsModel extends LoadableDetachableModel<GroupStats> {
	
	private static final long serialVersionUID = 1L;
	
	private final long statsId;

	/**
	 * @param c
	 */
	public LDGroupStatsModel(GroupStats gs) {
		this(gs.getStatsId());
	}

	/**
	 * @param id
	 */
	public LDGroupStatsModel(long statsId) {
		if (statsId == 0) {
			throw new IllegalArgumentException();
		}
		this.statsId = statsId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(statsId).hashCode();
	}

	/**
	 * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
	 * 
	 * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj instanceof LDGroupStatsModel) {
			LDGroupStatsModel other = (LDGroupStatsModel)obj;
			return other.statsId == statsId;
		}
		return false;
	}

	/**
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected GroupStats load() {
		// loads group from the database
		return AdaptAccess.getDAO().findStats(statsId);
	}
}
