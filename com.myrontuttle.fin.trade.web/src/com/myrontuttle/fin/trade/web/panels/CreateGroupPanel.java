package com.myrontuttle.fin.trade.web.panels;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.models.BooleanGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.DoubleGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.IntegerGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.StringGroupSettingsModel;
import com.myrontuttle.fin.trade.web.models.LDGroupModel;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;
import com.myrontuttle.fin.trade.web.service.PortfolioAccess;
import com.myrontuttle.fin.trade.web.service.StrategyAccess;

public class CreateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public CreateGroupPanel(String id) {
		super(id);
		
		final LDGroupModel ldGroupModel = new LDGroupModel();
		final Form<Group> form = new Form<Group>("newGroupForm");
/*
		List<String> alertReceiverTypes = Arrays.
					asList(AlertReceiverAccess.getAlertReceiverService().availableReceiverTypes());
		form.add(new DropDownChoice<String>("alertReceiverType", alertReceiverTypes));
		
		form.add(new TextField<String>("alertHost")
						.setRequired(true)
						.add(new AttributeModifier("value", "imap.gmail.com")));
*/	
		form.add(new TextField<String>("Alert.User", 
				new StringGroupSettingsModel(ldGroupModel, "Alert.User"))
				.add(EmailAddressValidator.getInstance()));
		
		form.add(new TextField<String>("Alert.Password", 
				new StringGroupSettingsModel(ldGroupModel, "Alert.Password"))
						.setRequired(true));
		
		List<String> frequencies = Arrays.asList(EvolveAccess.getEvolveService().getEvolveFrequencies());
		form.add(new DropDownChoice<String>("Evolve.Frequency", 
				new StringGroupSettingsModel(ldGroupModel, "Evolve.Frequency"), 
				frequencies));

		List<String> evaluators;
		try {
			evaluators = Arrays.asList(PortfolioAccess.getPortfolioService().availableAnalysis());
		} catch (Exception e) {
			evaluators = Arrays.asList(new String[0]);
		}
		form.add(new DropDownChoice<String>("Eval.Strategy", 
				new StringGroupSettingsModel(ldGroupModel, "Eval.Strategy"), 
				evaluators));

		List<String> tradeStrategies = Arrays.
					asList(StrategyAccess.getTradeStrategyService().availableTradeStrategies());
		form.add(new DropDownChoice<String>("Trade.Strategy", 
				new StringGroupSettingsModel(ldGroupModel, "Trade.Strategy"), 
				tradeStrategies));


		form.add(new TextField<Integer>("Evolve.Size", 
				new IntegerGroupSettingsModel(ldGroupModel, "Evolve.Size"))
						.setRequired(true));
		
		form.add(new TextField<Integer>("Evolve.EliteCount", 
				new IntegerGroupSettingsModel(ldGroupModel, "Evolve.EliteCount"))
						.setRequired(true));
		
		form.add(new TextField<Integer>("Evolve.GeneUpperValue", 
				new IntegerGroupSettingsModel(ldGroupModel, "Evolve.GeneUpperValue"))
						.setRequired(true)
						.add(new AttributeModifier("value", "100")));

		form.add(new TextField<Double>("Evolve.MutationFactor", 
				new DoubleGroupSettingsModel(ldGroupModel, "Evolve.MutationFactor"))
						.setRequired(true)
						.add(new AttributeModifier("value", "0.1")));

		form.add(new CheckBox("Trade.AllowShorting", 
				new BooleanGroupSettingsModel(ldGroupModel, "Trade.AllowShorting")));
		
		
		form.add(new TextField<Integer>("Express.NumberOfScreens", 
				new IntegerGroupSettingsModel(ldGroupModel, "Express.NumberOfScreens"))
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new TextField<Integer>("Express.MaxSymbolsPerScreen", 
				new IntegerGroupSettingsModel(ldGroupModel, "Express.MaxSymbolsPerScreen"))
						.setRequired(true)
						.add(new AttributeModifier("value", "10")));
		
		form.add(new TextField<Integer>("Express.AlertsPerSymbol", 
				new IntegerGroupSettingsModel(ldGroupModel, "Express.AlertsPerSymbol"))
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new TextField<Double>("Express.StartingCash", 
				new DoubleGroupSettingsModel(ldGroupModel, "Express.StartingCashr"))
						.setRequired(true)
						.add(new AttributeModifier("value", "10000.00")));

		form.add(new CheckBox("Evolve.Active", 
				new BooleanGroupSettingsModel(ldGroupModel, "Evolve.Active")));
		
		form.add(new Button("create") {
            public void onSubmit() {
            	Group group = (Group)ldGroupModel.getObject();
         	    group.setString("Alert.ReceiverType", "EMAIL");
         	    group.setString("Alert.Host", "imap.gmail.com");
         	    AdaptAccess.getDAO().saveGroup(group);
            	EvolveAccess.getEvolveService().setupGroup(group.getGroupId());
            }
        });
        
		add(form);
	}

}
