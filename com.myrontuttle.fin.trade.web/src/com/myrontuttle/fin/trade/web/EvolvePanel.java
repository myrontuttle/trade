package com.myrontuttle.fin.trade.web;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.joda.time.DateTime;

public class EvolvePanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private int hourToEvolve;
	private int minuteToEvolve;
	private String nextEvolution;

	public EvolvePanel(String id) {
		super(id);

		Form form = new Form("evolveForm", new CompoundPropertyModel(this));

		form.add(new TextField<String>("hourToEvolve"));
		form.add(new TextField<String>("minuteToEvolve"));

		form.add(new Button("evolveAllAtTime") {
            public void onSubmit() {
            	DateTime dt = new DateTime().withHourOfDay(hourToEvolve).withMinuteOfHour(minuteToEvolve);
            	EvolveAccess.getEvolveService().startEvolvingAt(dt);
            	nextEvolution = dt.toString();
            }
        });
		
		form.add(new Button("evolveAllNow") {
            public void onSubmit() {
            	EvolveAccess.getEvolveService().evolveAllNow();
            	nextEvolution = new DateTime().toString();
            }
        });
        
		DateTime dt = EvolveAccess.getEvolveService().getNextEvolveDate();
		if (dt == null) {
			nextEvolution = "Not Set";
		} else {
			nextEvolution = dt.toString();
		}
		form.add(new Label("nextEvolution", nextEvolution));

		form.add(new Button("stopEvolving") {
            public void onSubmit() {
            	EvolveAccess.getEvolveService().stopEvolving();
            	nextEvolution = "Not Set";
            }
        });		
		
		add(form);
	}

	public int getHourToEvolve() {
		return hourToEvolve;
	}

	public int getMinuteToEvolve() {
		return minuteToEvolve;
	}

	public void setHourToEvolve(int hourToEvolve) {
		this.hourToEvolve = hourToEvolve;
	}

	public void setMinuteToEvolve(int minuteToEvolve) {
		this.minuteToEvolve = minuteToEvolve;
	}

}
