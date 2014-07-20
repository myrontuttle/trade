package com.myrontuttle.fin.trade.web.panels;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;
import com.myrontuttle.fin.trade.web.service.AlertReceiverAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;

public class UpdateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	Group group;
	
	public UpdateGroupPanel(String id, IModel<Group> model) {
		super(id);
		
		final Form<Group> form = new Form<Group>("updateGroupForm", model);
    	group = (Group)model.getObject();
/*
		List<String> alertReceiverTypes = Arrays.
					asList(AlertReceiverAccess.getAlertReceiverService().availableReceiverTypes());
		form.add(new DropDownChoice<String>("alertReceiverType", alertReceiverTypes));

		form.add(new TextField<String>("alertHost")
						.setRequired(true)
						.add(new AttributeModifier("value", "imap.gmail.com")));
*/
		form.add(new TextField<String>("stringSettings[Alert.User]")
					.add(EmailAddressValidator.getInstance()));
		form.add(new TextField<String>("stringSettings[Alert.Password]")
						.setRequired(true));
		
		form.add(new Label("stringSettings[Evolve.Frequency]"));
		form.add(new Label("stringSettings[Eval.Strategy]"));
		form.add(new Label("stringSettings[Trade.Strategy]"));
		
		form.add(new TextField<String>("integerSettings[Evolve.Size]")
						.setRequired(true));
		form.add(new TextField<Integer>("integerSettings[Evolve.EliteCount]")
						.setRequired(true));
		form.add(new Label("integerSettings[Evolve.GeneUpperValue]"));
		form.add(new Label("doubleSettings[Evolve.MutationFactor]"));
		form.add(new CheckBox("booleanSettings[Trade.AllowShorting]"));

		form.add(new Label("integerSettings[Express.NumberOfScreens]"));
		form.add(new Label("integerSettings[Express.MaxSymbolsPerScreen]"));
		form.add(new Label("integerSettings[Express.AlertsPerSymbol]"));
		
		form.add(new TextField<Integer>("doubleSettings[Express.StartingCash]")
						.setRequired(true));
		form.add(new CheckBox("booleanSettings[Evolve.Active]"));

		form.add(new Button("removeCandidates") {
            public void onSubmit() {
            	for (Candidate c : group.getCandidates()) {
            		EvolveAccess.getEvolveService().deleteCandidateExpression(
            				group.getGroupId(), 
            				c.getGenome());
            		AdaptAccess.getDAO().removeCandidate(c.getCandidateId());
            	}
            }
        });
		form.add(new Button("removeStats") {
            public void onSubmit() {
            	AdaptAccess.getDAO().removeAllStats(group.getGroupId());
            }
        });
		form.add(new Button("update") {
            public void onSubmit() {            	
            	group = AdaptAccess.getDAO().updateGroup((Group)getParent().getDefaultModelObject());
            }
        });
        
		add(form);
	}

}
