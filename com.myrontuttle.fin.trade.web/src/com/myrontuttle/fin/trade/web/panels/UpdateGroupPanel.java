package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.models.BooleanGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.DoubleGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.IntegerGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.LDGroupModel;
import com.myrontuttle.fin.trade.web.models.StringGroupSettingsModel;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;

public class UpdateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public UpdateGroupPanel(String id, long groupId) {
		super(id);

		final LDGroupModel ldGroupModel = new LDGroupModel(groupId);
		final Form<Group> form = new Form<Group>("updateGroupForm");
/*
		List<String> alertReceiverTypes = Arrays.
					asList(AlertReceiverAccess.getAlertReceiverService().availableReceiverTypes());
		form.add(new DropDownChoice<String>("alertReceiverType", alertReceiverTypes));

		form.add(new TextField<String>("alertHost")
						.setRequired(true)
						.add(new AttributeModifier("value", "imap.gmail.com")));
*/
		form.add(new Label("Alert.Period", 
				new StringGroupSettingsModel(ldGroupModel, "Alert.Period")));
		form.add(new TextField<String>("Alert.User", 
				new StringGroupSettingsModel(ldGroupModel, "Alert.User"))
					.add(EmailAddressValidator.getInstance()));
		form.add(new TextField<String>("Alert.Password", 
				new StringGroupSettingsModel(ldGroupModel, "Alert.Password"))
						.setRequired(true));
		
		form.add(new Label("Evolve.Frequency", 
				new StringGroupSettingsModel(ldGroupModel, "Evolve.Frequency")));
		form.add(new Label("Eval.Strategy", 
				new StringGroupSettingsModel(ldGroupModel, "Eval.Strategy")));
		form.add(new Label("Trade.Strategy", 
				new StringGroupSettingsModel(ldGroupModel, "Trade.Strategy")));
		
		form.add(new TextField<Integer>("Evolve.Size", 
				new IntegerGroupSettingsModel(ldGroupModel, "Evolve.Size"))
					.setType(Integer.class)
					.setRequired(true));
		form.add(new TextField<Integer>("Evolve.EliteCount", 
				new IntegerGroupSettingsModel(ldGroupModel, "Evolve.EliteCount"))
					.setType(Integer.class)
					.setRequired(true));
		form.add(new Label("Evolve.GeneUpperValue", 
				new IntegerGroupSettingsModel(ldGroupModel, "Evolve.GeneUpperValue")));
		form.add(new Label("Evolve.MutationFactor", 
				new DoubleGroupSettingsModel(ldGroupModel, "Evolve.MutationFactor")));
		form.add(new CheckBox("Trade.AllowShorting", 
				new BooleanGroupSettingsModel(ldGroupModel, "Trade.AllowShorting")));

		form.add(new Label("Express.NumberOfScreens", 
				new IntegerGroupSettingsModel(ldGroupModel, "Express.NumberOfScreens")));
		form.add(new Label("Express.MaxSymbolsPerScreen", 
				new IntegerGroupSettingsModel(ldGroupModel, "Express.MaxSymbolsPerScreen")));
		form.add(new Label("Express.AlertsPerSymbol", 
				new IntegerGroupSettingsModel(ldGroupModel, "Express.AlertsPerSymbol")));
		
		form.add(new TextField<Double>("Express.StartingCash", 
				new DoubleGroupSettingsModel(ldGroupModel, "Express.StartingCash"))
					.setType(Double.class)
					.setRequired(true));
		form.add(new CheckBox("Evolve.Active", 
				new BooleanGroupSettingsModel(ldGroupModel, "Evolve.Active")));

		form.add(new Button("removeCandidates") {
            public void onSubmit() {
            	for (Candidate c : ldGroupModel.getObject().getCandidates()) {
            		EvolveAccess.getEvolveService().deleteCandidateExpression(
            				ldGroupModel.getObject().getGroupId(), 
            				c.getGenome());
            		AdaptAccess.getDAO().removeCandidate(c.getCandidateId());
            	}
            }
        });
		form.add(new Button("removeStats") {
            public void onSubmit() {
            	AdaptAccess.getDAO().removeAllStats(ldGroupModel.getObject().getGroupId());
            }
        });
		form.add(new Button("update") {
            public void onSubmit() {      	
            	AdaptAccess.getDAO().updateGroup(ldGroupModel.getObject());
            }
        });
        
		add(form);
	}

}
