package com.myrontuttle.fin.trade.web;

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

public class CreateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public CreateGroupPanel(String id) {
		super(id);
		final IModel<Group> compound = new CompoundPropertyModel<Group>(new Group());
		final Form<Group> form = new Form<Group>("newGroupForm", compound);
		
		form.add(new TextField<String>("alertAddress")
					.add(EmailAddressValidator.getInstance()));
		
		List<String> frequencies = Arrays.asList(Group.DAILY, Group.WEEKLY);
		form.add(new DropDownChoice<String>("frequency", frequencies));

		List<String> expressions = Arrays.asList(Group.NO_EXPRESSION, Group.BASIC_EXPRESSION);
		form.add(new DropDownChoice<String>("expressionStrategy", expressions));

		List<String> evaluators = Arrays.asList(Group.RANDOM_EVALUATOR, Group.BASIC_EVALUATOR);
		form.add(new DropDownChoice<String>("evaluationStrategy", evaluators));
		
		form.add(new CheckBox("active"));

		form.add(new TextField<String>("size")
						.setRequired(true));
		
		form.add(new TextField<Integer>("eliteCount")
						.setRequired(true));
		
		form.add(new TextField<Integer>("geneUpperValue")
						.setRequired(true)
						.add(new AttributeModifier("value", "100")));
		
		form.add(new TextField<Integer>("numberOfScreens")
						.setRequired(true)
						.add(new AttributeModifier("value", "3")));
		
		form.add(new TextField<Integer>("maxSymbolsPerScreen")
						.setRequired(true)
						.add(new AttributeModifier("value", "10")));
		
		form.add(new TextField<Integer>("alertsPerSymbol")
						.setRequired(true)
						.add(new AttributeModifier("value", "2")));
		
		form.add(new Button("create") {
            public void onSubmit() {
            	DBAccess.getDAO().saveGroup((Group)compound.getObject());
            }
        });
        
		add(form);
	}

}
