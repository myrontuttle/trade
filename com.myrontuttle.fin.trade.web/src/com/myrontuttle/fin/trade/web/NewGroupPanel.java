package com.myrontuttle.fin.trade.web;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;

import com.myrontuttle.fin.trade.adapt.Group;

public class NewGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private String results = checkStrategyDAO();
	
	public NewGroupPanel(String id) {
		super(id);
		final IModel<Group> compound = new CompoundPropertyModel<Group>(new Group());
		final Form<Group> form = new Form<Group>("newGroupForm", compound);
		form.add(new TextField<String>("alertAddress")
					.add(EmailAddressValidator.getInstance()));
		
		form.add(new TextField<String>("size")
						.setRequired(true)
						.add(new RangeValidator<Integer>(1, Integer.MAX_VALUE)));
		
		form.add(new TextField<Integer>("eliteCount")
						.setRequired(true)
						.add(new RangeValidator<Integer>(0, Integer.MAX_VALUE)));
		form.add(new TextField<Integer>("geneUpperValue")
						.setRequired(true)
						.add(new AttributeModifier("value", "100"))
						.add(new RangeValidator<Integer>(1, Integer.MAX_VALUE)));
		
		List<String> expressions = Arrays.asList(Group.NO_EXPRESSION, Group.BASIC_EXPRESSION);
		form.add(new DropDownChoice<String>("expressionStrategy", expressions));

		List<String> evaluators = Arrays.asList(Group.RANDOM_EVALUATOR, Group.BASIC_EVALUATOR);
		form.add(new DropDownChoice<String>("evaluationStrategy", evaluators));
		
		form.add(new Button("create") {
            public void onSubmit() {
            	DBAccess.getDAO().saveGroup((Group)compound.getObject());
            	results = "Group created";
            }
        });
        
		add(form);

		add(new Label("results", results));
	}

	public String checkStrategyDAO() {
		if (DBAccess.getDAO() == null) {
			return "StrategyDAO is null";
		} else {
			return "StrategyDAO accessed";
		}
	}

}
