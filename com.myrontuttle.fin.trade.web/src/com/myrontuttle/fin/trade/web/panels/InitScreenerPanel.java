package com.myrontuttle.fin.trade.web.panels;

import java.util.HashMap;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.io.IClusterable;

import com.myrontuttle.fin.trade.web.service.ScreenerAccess;

public class InitScreenerPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public InitScreenerPanel(String id) {
		super(id);
		final Settings settings = new Settings();
		Form<Settings> form = new Form<Settings>(
				"sceenerInitForm", new CompoundPropertyModel<Settings>(settings));
		
		form.add(new TextArea<String>("text"));

		form.add(new Button("initialize") {
            public void onSubmit() {
            	try {
					ScreenerAccess.getScreenerService().initialize(settings.parseText());
				} catch (Exception e) {
					settings.status = e.getMessage();
				}
            }
        });
		
		form.add(new Label("status"));
		
		add(form);
	}

    /** Simple data class that acts as a model for the input fields. */
    private static class Settings implements IClusterable {
    	
		private static final long serialVersionUID = 1L;
		
		/** settings text. */
        public String text = getStoredSettings();
        
        public String status;

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "text = '" + text + "'";
        }
        
        public HashMap<String, String> parseText() {
        	HashMap<String, String> properties = new HashMap<String, String>();
        	String[] pairs = text.split("\\r?\\n");
        	for (String pair : pairs) {
        		String[] keyValue = pair.split("=");
        		if (keyValue.length >= 2) {
            		properties.put(keyValue[0], keyValue[1]);
        		}
        	}
        	return properties;
        }
        
        public String getStoredSettings() {
        	StringBuilder sb = new StringBuilder();
        	try {
        		HashMap<String, String> settings = ScreenerAccess.getScreenerService().getSettings();

	        	for (String key : settings.keySet()) {
	        		sb.append(key + "=" + settings.get(key) + "\n");
	        	}
			} catch (Exception e) {
				status = e.getMessage();
			}
        	
        	return sb.toString();
        }
    }
}
