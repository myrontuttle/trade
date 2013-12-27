package com.myrontuttle.fin.trade.web;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.myrontuttle.fin.trade.adapt.Group;

public class UpdateGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	Group group;
	
	public UpdateGroupPanel(String id, String groupId) {
		super(id);
		if (groupId != null) {
			group = DBAccess.getDAO().findGroup(groupId);
		}
		if (group == null) {
			group = new Group();
		}
		final IModel<Group> compound = new CompoundPropertyModel<Group>(group);
		final Form<Group> form = new Form<Group>("updateGroupForm", compound);
		
		form.add(new TextField<String>("alertAddress")
					.add(EmailAddressValidator.getInstance()));
		
		form.add(new Label("frequency"));
		form.add(new Label("expressionStrategy"));
		form.add(new Label("evaluationStrategy"));
		
		form.add(new CheckBox("active"));

		form.add(new TextField<String>("size")
						.setRequired(true));
		
		form.add(new TextField<Integer>("eliteCount")
						.setRequired(true));
		
		form.add(new Label("geneUpperValue"));

		form.add(new Label("mutationFactor"));
		
		form.add(new Label("numberOfScreens"));
		
		form.add(new Label("maxSymbolsPerScreen"));
		
		form.add(new Label("alertsPerSymbol"));
		
		form.add(new Button("update") {
            public void onSubmit() {
            	group = DBAccess.getDAO().updateGroup((Group)compound.getObject());
            }
        });
        
		add(form);
	}

}
