package com.myrontuttle.fin.trade.web.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.myrontuttle.fin.trade.web.EvolveAccess;

public class EvolvePanel extends Panel {

	private static final long serialVersionUID = 1L;
	// Example = 2014-12-15 15:34 (Saturday)
	private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm (EEEE)");
	
	private Integer hourToEvolve;
	private Integer minuteToEvolve;
	private String nextEvolution;

	public EvolvePanel(String id) {
		super(id);
		setEvolveTime();
		Form<EvolvePanel> form = new Form<EvolvePanel>("evolveForm", new CompoundPropertyModel<EvolvePanel>(this));

		form.add(new TextField<Integer>("hourToEvolve"));
		form.add(new TextField<Integer>("minuteToEvolve"));

		form.add(new Button("evolveActiveAtTime") {
            public void onSubmit() {
            	DateTime dt = new DateTime().withHourOfDay(hourToEvolve).withMinuteOfHour(minuteToEvolve);
            	EvolveAccess.getEvolveService().evolveActiveAt(dt);
            	setEvolveTime();
            }
        });
		
		form.add(new Button("evolveAllNow") {
            public void onSubmit() {
            	EvolveAccess.getEvolveService().evolveAllNow();
            	setEvolveTime();
            }
        });
        
		form.add(new Label("nextEvolution"));

		form.add(new Button("stopEvolving") {
            public void onSubmit() {
            	EvolveAccess.getEvolveService().stopEvolving();
            	setEvolveTime();
            }
        });		
		
		add(form);
	}
	
	private void setEvolveTime() {
		DateTime dt = EvolveAccess.getEvolveService().getNextEvolveDate();
		if (dt == null) {
			nextEvolution = "Not Set";
		} else {
	    	nextEvolution = fmt.print(dt);
	    	hourToEvolve = dt.getHourOfDay();
	    	minuteToEvolve = dt.getMinuteOfHour();
		}
	}

	public Integer getHourToEvolve() {
		return hourToEvolve;
	}

	public Integer getMinuteToEvolve() {
		return minuteToEvolve;
	}

	public void setHourToEvolve(int hourToEvolve) {
		this.hourToEvolve = hourToEvolve;
	}

	public void setMinuteToEvolve(int minuteToEvolve) {
		this.minuteToEvolve = minuteToEvolve;
	}

	public String getNextEvolution() {
		return nextEvolution;
	}

	public void setNextEvolution(String nextEvolution) {
		this.nextEvolution = nextEvolution;
	}

}
