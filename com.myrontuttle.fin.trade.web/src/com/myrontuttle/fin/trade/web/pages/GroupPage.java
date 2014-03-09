package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.data.DBAccess;
import com.myrontuttle.fin.trade.web.panels.BestTraderPanel;
import com.myrontuttle.fin.trade.web.panels.CandidateTablePanel;
import com.myrontuttle.fin.trade.web.panels.EvolveGroupPanel;
import com.myrontuttle.fin.trade.web.panels.GroupStatsTablePanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.UpdateGroupPanel;

public class GroupPage extends WebPage {

	private static final long serialVersionUID = 1L;

	Group group;
	
	public GroupPage(String groupId) {

		if (groupId != null) {
			group = DBAccess.getDAO().findGroup(groupId);
		}
		if (group == null) {
			group = new Group();
		}
		final IModel<Group> groupModel = new CompoundPropertyModel<Group>(group);
		
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new UpdateGroupPanel("updateGroupPanel", groupModel));
		add(new EvolveGroupPanel("evolveGroupPanel", groupModel));
		add(new GroupStatsTablePanel("groupStatsTablePanel", groupId));
		add(new BestTraderPanel("bestTraderPanel", groupModel));
		add(new Label("currentVariability", group.getVariability()));
		add(new CandidateTablePanel("candidateTablePanel", groupId));
	}

}
