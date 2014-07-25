package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import com.myrontuttle.fin.trade.adapt.Group;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;

public class EvolveGroupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id component id
	 * @param model model for contact
	 */
	public EvolveGroupPanel(String id, final long groupId) {
		super(id);
		final Form<Group> form = new Form<Group>("evolveGroupForm");
		form.add(new Button("evolve") {
			public void onSubmit() {
				EvolveAccess.getEvolveService().evolveNow(groupId);
			}
		});
		add(form);
	}
}
