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
import com.myrontuttle.fin.trade.web.data.DBAccess;
import com.myrontuttle.fin.trade.web.service.AlertReceiverAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;

public class UpdateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	Group group;
	
	public UpdateGroupPanel(String id, IModel<Group> model) {
		super(id);
		
		final Form<Group> form = new Form<Group>("updateGroupForm", model);

		List<String> alertReceivers = Arrays.
					asList(AlertReceiverAccess.getAlertReceiverService().availableReceiverTypes());
		form.add(new DropDownChoice<String>("alertReceiver", alertReceivers));
		form.add(new TextField<String>("alertUser")
					.add(EmailAddressValidator.getInstance()));
		form.add(new TextField<String>("alertHost")
						.setRequired(true)
						.add(new AttributeModifier("value", "imap.gmail.com")));
		form.add(new TextField<String>("alertPassword")
						.setRequired(true));
		
		form.add(new Label("frequency"));
		form.add(new Label("expressionStrategy"));
		form.add(new Label("evaluationStrategy"));
		form.add(new Label("tradeStrategy"));
		
		form.add(new TextField<String>("size")
						.setRequired(true));
		form.add(new TextField<Integer>("eliteCount")
						.setRequired(true));
		form.add(new Label("geneUpperValue"));
		form.add(new Label("mutationFactor"));
		form.add(new CheckBox("allowShorting"));

		form.add(new Label("numberOfScreens"));
		form.add(new Label("maxSymbolsPerScreen"));
		form.add(new Label("alertsPerSymbol"));
		
		form.add(new TextField<Integer>("startingCash")
						.setRequired(true));
		form.add(new CheckBox("active"));

		form.add(new Button("removeCandidates") {
            public void onSubmit() {
            	for (Candidate c : group.getCandidates()) {
            		EvolveAccess.getEvolveService().deleteCandidateExpression(
            				group.getGroupId(), 
            				c.getGenome());
            		DBAccess.getDAO().removeCandidate(c.getCandidateId());
            	}
            }
        });
		form.add(new Button("removeStats") {
            public void onSubmit() {
            	DBAccess.getDAO().removeAllStats(group.getGroupId());
            }
        });
		form.add(new Button("update") {
            public void onSubmit() {
            	group = DBAccess.getDAO().updateGroup((Group)getParent().getDefaultModelObject());
            }
        });
        
		add(form);
	}

}
