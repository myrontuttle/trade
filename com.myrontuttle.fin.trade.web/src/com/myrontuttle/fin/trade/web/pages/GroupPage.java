package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.panels.BestTraderPanel;
import com.myrontuttle.fin.trade.web.panels.CandidateTablePanel;
import com.myrontuttle.fin.trade.web.panels.EvolveGroupPanel;
import com.myrontuttle.fin.trade.web.panels.GroupStatsTablePanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.UpdateGroupPanel;
import com.myrontuttle.fin.trade.web.panels.UploadCandidatePanel;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;

public class GroupPage extends WebPage {

	private static final long serialVersionUID = 1L;

	Group group;
	
	public GroupPage(long groupId) {

		if (groupId != 0) {
			group = AdaptAccess.getDAO().findGroup(groupId);
		}
		if (group == null) {
			group = new Group();
		}
		final IModel<Group> groupModel = new CompoundPropertyModel<Group>(group);
		
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new UpdateGroupPanel("updateGroupPanel", groupModel));
		add(new UploadCandidatePanel("uploadCandidatePanel", groupId));
		add(new EvolveGroupPanel("evolveGroupPanel", groupModel));
		add(new GroupStatsTablePanel("groupStatsTablePanel", groupId));
		add(new BestTraderPanel("bestTraderPanel", groupModel));
		add(new Label("currentVariability", group.getVariability()));
		add(new CandidateTablePanel("candidateTablePanel", groupId));
	}

}
