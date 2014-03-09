package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;

public class EvolveGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id component id
	 * @param model model for contact
	 */
	public EvolveGroupPanel(String id, IModel<Group> model) {
		super(id, model);
		final Form<Group> form = new Form<Group>("evolveGroupForm", model);
		form.add(new Button("evolve") {
			public void onSubmit() {
				String groupId = ((Group)getParent().getDefaultModelObject()).getGroupId();
				EvolveAccess.getEvolveService().evolveNow(groupId);
			}
		});
		add(form);
	}
}
