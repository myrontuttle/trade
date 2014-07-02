package com.myrontuttle.fin.trade.web.panels;

import java.util.ArrayList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.io.IClusterable;

import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;
import com.myrontuttle.fin.trade.web.service.ScreenerAccess;

public class FixedCriteriaPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public FixedCriteriaPanel(String id) {
		super(id);
		final FixedCriteria criteria = new FixedCriteria();
		Form<FixedCriteria> form = new Form<FixedCriteria>(
				"fixedCriteriaForm", new CompoundPropertyModel<FixedCriteria>(criteria));
		
		form.add(new TextArea<String>("text"));

		form.add(new Button("update") {
            public void onSubmit() {
            	try {
					ScreenerAccess.getScreenerService().setFixedCriteria(criteria.parseText());
				} catch (Exception e) {
					criteria.status = e.getMessage();
				}
            }
        });
		
		form.add(new Label("status"));
		
		add(form);
	}

    /** Simple data class that acts as a model for the input fields. */
    private static class FixedCriteria implements IClusterable {
    	
		private static final long serialVersionUID = 1L;

		public static final String SEPARATOR = ";";
		
		/** settings text. */
        public String text = getStoredFixedCriteria();
        
        public String status;

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "text = '" + text + "'";
        }
        
        public SelectedScreenCriteria[] parseText() {
        	ArrayList<SelectedScreenCriteria> screenCriteria = new ArrayList<SelectedScreenCriteria>();
        	String[] criteriaLines = text.split("\\r?\\n");
        	for (String criteria : criteriaLines) {
        		String[] details = criteria.split(SEPARATOR);
        		if (details.length >= 3) {
            		screenCriteria.add(new Criteria(details[0], details[1], details[2]));
        		}
        	}
        	return screenCriteria.toArray(new SelectedScreenCriteria[screenCriteria.size()]);
        }
        
        public String getStoredFixedCriteria() {
        	StringBuilder sb = new StringBuilder();
        	try {
        		SelectedScreenCriteria[] fixed = ScreenerAccess.getScreenerService().getFixedCriteria();

	        	for (int i=0; i<fixed.length; i++) {
	        		sb.append(fixed[i].getName() + SEPARATOR +
	        				fixed[i].getValue() + SEPARATOR +
	        				fixed[i].getArgsOperator() + "\n");
	        	}
			} catch (Exception e) {
				status = e.getMessage();
			}
        	
        	return sb.toString();
        }
    }
}

class Criteria implements SelectedScreenCriteria {

	String name;
	String value;
	String argsOperator;

	public Criteria(String name, String value, String argsOperator) {
		this.name = name;
		this.value = value;
		this.argsOperator = argsOperator;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getArgsOperator() {
		return argsOperator;
	}
	
}
