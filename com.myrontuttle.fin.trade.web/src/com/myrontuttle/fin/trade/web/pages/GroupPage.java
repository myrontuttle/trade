package com.myrontuttle.fin.trade.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import com.myrontuttle.fin.trade.web.models.LDGroupModel;
import com.myrontuttle.fin.trade.web.panels.BestCandidatePanel;
import com.myrontuttle.fin.trade.web.panels.CandidateTablePanel;
import com.myrontuttle.fin.trade.web.panels.EvolveGroupPanel;
import com.myrontuttle.fin.trade.web.panels.GroupStatsTablePanel;
import com.myrontuttle.fin.trade.web.panels.HeaderPanel;
import com.myrontuttle.fin.trade.web.panels.UpdateGroupPanel;
import com.myrontuttle.fin.trade.web.panels.UploadCandidatePanel;

public class GroupPage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	public GroupPage(long groupId) {

		LDGroupModel ldGroupModel = new LDGroupModel(groupId);
		
		add(new HeaderPanel("mainNavigation", "Adaptive Trader", this));
		add(new UpdateGroupPanel("updateGroupPanel", groupId));
		add(new UploadCandidatePanel("uploadCandidatePanel", groupId));
		add(new EvolveGroupPanel("evolveGroupPanel", groupId));
		add(new GroupStatsTablePanel("groupStatsTablePanel", groupId));
		add(new BestCandidatePanel("bestCandidatePanel", groupId));
		add(new Label("currentVariability", ldGroupModel.getObject().getDouble("Express.Variability")));
		add(new CandidateTablePanel("candidateTablePanel", groupId));
	}

}
