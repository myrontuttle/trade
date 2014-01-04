package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;

import com.myrontuttle.fin.trade.web.panels.CandidateTablePanel;
import com.myrontuttle.fin.trade.web.panels.GroupStatsTablePanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.UpdateGroupPanel;

public class GroupPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public GroupPage(String groupId) {
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new UpdateGroupPanel("updateGroupPanel", groupId));
		add(new GroupStatsTablePanel("groupStatsTablePanel", groupId));
		add(new CandidateTablePanel("candidateTablePanel", groupId));
	}

}
