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
import com.myrontuttle.fin.trade.web.service.EvolveAccess;
import com.myrontuttle.fin.trade.web.service.PortfolioAccess;
import com.myrontuttle.fin.trade.web.service.StrategyAccess;

public class CreateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public CreateGroupPanel(String id) {
		super(id);
		final IModel<GroupSettings> compound = new CompoundPropertyModel<GroupSettings>(new GroupSettings());
		final Form<GroupSettings> form = new Form<GroupSettings>("newGroupForm", compound);
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
		
		form.add(new TextField<String>("alertPassword")
						.setRequired(true));
		
		List<String> frequencies = Arrays.asList(Group.HOURLY, Group.DAILY, Group.WEEKLY);
		form.add(new DropDownChoice<String>("frequency", frequencies));

		List<String> expressions = Arrays.asList(Group.SAT_EXPRESSION);
		form.add(new DropDownChoice<String>("expressionStrategy", expressions));

		List<String> evaluators;
		try {
			evaluators = Arrays.asList(PortfolioAccess.getPortfolioService().availableAnalysis());
		} catch (Exception e) {
			evaluators = Arrays.asList(new String[0]);
		}
		form.add(new DropDownChoice<String>("evaluationStrategy", evaluators));

		List<String> tradeStrategies = Arrays.
					asList(StrategyAccess.getTradeStrategyService().availableTradeStrategies());
		form.add(new DropDownChoice<String>("tradeStrategy", tradeStrategies));


		form.add(new TextField<String>("size")
						.setRequired(true));
		
		form.add(new TextField<Integer>("eliteCount")
						.setRequired(true));
		
		form.add(new TextField<Integer>("geneUpperValue")
						.setRequired(true)
						.add(new AttributeModifier("value", "100")));

		form.add(new TextField<Integer>("mutationFactor")
						.setRequired(true)
						.add(new AttributeModifier("value", "0.1")));

		form.add(new CheckBox("allowShorting"));
		
		
		form.add(new TextField<Integer>("numberOfScreens")
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new TextField<Integer>("maxSymbolsPerScreen")
						.setRequired(true)
						.add(new AttributeModifier("value", "10")));
		
		form.add(new TextField<Integer>("alertsPerSymbol")
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new TextField<Integer>("startingCash")
						.setRequired(true)
						.add(new AttributeModifier("value", "10000.00")));

		form.add(new CheckBox("active"));
		
		form.add(new Button("create") {
            public void onSubmit() {
            	GroupSettings settings = (GroupSettings)compound.getObject();
         	    settings.setStringValue("Alert.ReceiverType", "EmailAlert");
         	    settings.setStringValue("Alert.Host", "imap.gmail.com");
            	EvolveAccess.getEvolveService().setupGroup(settings);
            }
        });
        
		add(form);
	}

}
