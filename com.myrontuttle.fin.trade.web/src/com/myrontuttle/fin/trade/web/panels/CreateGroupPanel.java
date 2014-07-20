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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.adapt.GroupSettings;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;
import com.myrontuttle.fin.trade.web.service.PortfolioAccess;
import com.myrontuttle.fin.trade.web.service.StrategyAccess;

public class CreateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public CreateGroupPanel(String id) {
		super(id);
		final IModel<Group> compound = new CompoundPropertyModel<Group>(new Group());
		final Form<Group> form = new Form<Group>("newGroupForm", compound);
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
		
		List<String> frequencies = Arrays.asList(EvolveAccess.getEvolveService().getEvolveFrequencies());
		form.add(new DropDownChoice<String>("stringSettings[Evolve.Frequency]", frequencies));

		List<String> evaluators;
		try {
			evaluators = Arrays.asList(PortfolioAccess.getPortfolioService().availableAnalysis());
		} catch (Exception e) {
			evaluators = Arrays.asList(new String[0]);
		}
		form.add(new DropDownChoice<String>("stringSettings[Eval.Strategy]", evaluators));

		List<String> tradeStrategies = Arrays.
					asList(StrategyAccess.getTradeStrategyService().availableTradeStrategies());
		form.add(new DropDownChoice<String>("stringSettings[Trade.Strategy]", tradeStrategies));


		form.add(new TextField<String>("integerSettings[Evolve.Size]")
						.setRequired(true));
		
		form.add(new TextField<Integer>("integerSettings[Evolve.EliteCount]")
						.setRequired(true));
		
		form.add(new TextField<Integer>("integerSettings[Evolve.GeneUpperValue]")
						.setRequired(true)
						.add(new AttributeModifier("value", "100")));

		form.add(new TextField<Double>("doubleSettings[Evolve.MutationFactor]")
						.setRequired(true)
						.add(new AttributeModifier("value", "0.1")));

		form.add(new CheckBox("booleanSettings[Trade.AllowShorting]"));
		
		
		form.add(new TextField<Integer>("integerSettings[Express.NumberOfScreens]")
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new TextField<Integer>("integerSettings[Express.MaxSymbolsPerScreen]")
						.setRequired(true)
						.add(new AttributeModifier("value", "10")));
		
		form.add(new TextField<Integer>("integerSettings[Express.AlertsPerSymbol]")
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new TextField<Double>("doubleSettings[Express.StartingCash]")
						.setRequired(true)
						.add(new AttributeModifier("value", "10000.00")));

		form.add(new CheckBox("booleanSettings[Evolve.Active]"));
		
		form.add(new Button("create") {
            public void onSubmit() {
            	Group group = (Group)compound.getObject();
         	    group.setString("Alert.ReceiverType", "EMAIL");
         	    group.setString("Alert.Host", "imap.gmail.com");
         	    AdaptAccess.getDAO().saveGroup(group);
            	EvolveAccess.getEvolveService().setupGroup(group.getGroupId());
            }
        });
        
		add(form);
	}

}
