package com.myrontuttle.fin.trade.alertreceivers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myrontuttle.fin.trade.api.TradeStrategyService;

/**
 * Receives alerts sent via email
 * @author Myron Tuttle
 */
public class EmailAlertReceiver {

	private static final Logger logger = LoggerFactory.getLogger( EmailAlertReceiver.class );

	public final static String DELIVERY_TYPE = "EMAIL";
	
	public final static String HOST = "Host";
	public final static String PORT = "Port";
	public final static String PROTOCOL = "Protocol";
	public final static String USER = "User";
	public final static String PASS = "Password";
	public final static String PERIOD = "Period";

	public final static String IMAPS = "imaps";
	public final static String IMAP = "imap";
	public final static String POP = "pop3";
	public final static String[] PROTOCOLS = new String[]{POP, IMAP, IMAPS};
	
	private final static String IMAPS_PORT = "993";
	private final static String IMAP_PORT = "143";
	private final static String POP_PORT = "110";
	public final static String[] PORTS = new String[]{POP_PORT, IMAP_PORT, IMAPS_PORT};
	
	private final static String MINUTE = "Each Minute";
	private final static String HOURLY = "Hourly";
	private final static String DAILY = "Daily";
	private final static String WEEKLY = "Weekly";
	private final static String PERIODS[] = new String[]{MINUTE, HOURLY, DAILY, WEEKLY};
	private final static int END_OF_DAY = 60*16;
	
	private final static String DEFAULT_HOST = "imap.gmail.com";
	private final static String DEFAULT_PORT = IMAPS_PORT;
	private final static String DEFAULT_PROTOCOL = IMAPS;
	private final static String DEFAULT_PERIOD = DAILY;
	
	private final static Map<String, String> availableParameters;
    static {
        Map<String, String> ap = new HashMap<String, String>();
        ap.put(HOST, "Email host (default = " + DEFAULT_HOST + ")");
        ap.put(PORT, "Port to connect on host (" + IMAPS + "=" + IMAPS_PORT + ", " +
				IMAP + "=" + IMAP_PORT + ", " + POP + "=" + POP_PORT + ")");
        ap.put(PROTOCOL, "Email protocol (e.g. " + IMAPS + ", " + IMAP + ", or " + POP + ")");
        ap.put(USER, "Email address");
        ap.put(PASS, "Email account password");
        ap.put(PERIOD, "How often to check for emailed alerts (default = " + 
        		DEFAULT_PERIOD + ")");
        availableParameters = Collections.unmodifiableMap(ap);
    }

	public EmailAlertReceiver() {}

	public static String getName() {
		return DELIVERY_TYPE;
	}
	
	public static Map<String, String> getAvailableParameters() {
		return availableParameters;
	}
	
	public static String[] getAvailableParameterOptions(String parameter) {
		if (parameter.equals(PERIOD)) {
			return PERIODS;
		}
		if (parameter.equals(PROTOCOL)) {
			return PROTOCOLS;
		}
		if (parameter.equals(PORT)) {
			return PORTS;
		}
		return null;
	}
	
	public static boolean validateParameters(ReceiverDAO receiverDAO, AlertReceiver r) {
		for (String param : availableParameters.keySet()) {
			String value = r.getParameter(param);
			switch(param) {
			case HOST:
				if (value == null || value.isEmpty()) {
					logger.warn("No email host specified for {}. Using default: {}.", r.getUserId(),
							DEFAULT_HOST);
					r.addParameter(HOST, DEFAULT_HOST);
				}
				break;
			case PORT:
				try {
					int port = Integer.parseInt(value);
					if (port < 1) {
						logger.warn("{} is not a valid email port for user {}. Using default = {}.", 
								new Object[]{value, r.getUserId(), DEFAULT_PORT});
						r.addParameter(PORT, DEFAULT_PORT);
					}
				} catch (NumberFormatException nfe) {
					logger.warn("{} is not a valid email port for user {}. Using default = {}.", 
							new Object[]{value, r.getUserId(), DEFAULT_PORT});
					r.addParameter(PORT, DEFAULT_PORT);
				}
				break;
			case PROTOCOL:
				if (value == null || value.isEmpty()) {
					logger.warn("{} is not a valid email protocol for user {}. Using default = {}.", 
							new Object[]{value, r.getUserId(), DEFAULT_PROTOCOL});
					r.addParameter(PROTOCOL, DEFAULT_PROTOCOL);
				}
				break;
			case USER:
				if (value == null || value.isEmpty()) {
					logger.error("No email user/address specified for {}.", r.getUserId());
					return false;
				}
				break;
			case PASS:
				if (value == null || value.isEmpty()) {
					logger.error("No email password specified for {}.", r.getUserId());
					return false;
				}
				break;
			case PERIOD:
				if (!Arrays.asList(PERIODS).contains(value)) {
					logger.warn("{} is not a valid email retrieval period for user {}. " +
							"Setting to default = {}.", 
							new Object[]{value, r.getUserId(), DEFAULT_PERIOD});
					r.addParameter(PERIOD, String.valueOf(DEFAULT_PERIOD));
				}
				break;
			}
		}
		receiverDAO.updateReceiver(r);
		return true;
	}

	public static ScheduledFuture<?> startReceiving(ScheduledExecutorService ses, TradeStrategyService tss,
														AlertReceiver r) {
		
		int delay = 0;
		TimeUnit timeUnit = TimeUnit.MINUTES;
		long period = DateTimeConstants.MINUTES_PER_DAY;
		switch (r.getParameter(PERIOD)) {
		case MINUTE:
			period = 1;
			break;
		case HOURLY:
			period = 60;
			delay = 60 - new DateTime().getMinuteOfHour();
			break;
		case DAILY:
			period = DateTimeConstants.MINUTES_PER_DAY;
			int mod = new DateTime().getMinuteOfDay();
			if (mod <= END_OF_DAY) {
				delay = END_OF_DAY - mod;
			} else {
				delay = (3600 - mod) + END_OF_DAY;
			}
			break;
		case WEEKLY:
			timeUnit = TimeUnit.DAYS;
			period = 7;
			delay = DateTimeConstants.SUNDAY - new DateTime().getDayOfWeek();
		}
		

		logger.debug("Started receiving alerts for {} on {}.", r.getParameter(USER), r.getParameter(HOST));
		return ses.scheduleAtFixedRate(
				new MailRetriever(
						tss, r.getParameter(HOST), r.getParameter(PROTOCOL), 
						Integer.valueOf(r.getParameter(PORT)), 
						r.getParameter(USER), r.getParameter(PASS)), 
				delay, period, timeUnit);
	}
	
	/* (non-Javadoc)
	 * @see com.myrontuttle.adaptivetrader.AlertReceiver#watchFor(com.myrontuttle.adaptivetrader.AlertAction[])
	 *
	@Override
	public void watchFor(AlertAction... alertActions) {
		for (AlertAction alertAction : alertActions) {
			alertActionList.add(alertAction);
		}
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.adaptivetrader.AlertReceiver#stopWatchingFor(com.myrontuttle.adaptivetrader.AlertAction)
	 *
	@Override
	public void stopWatchingFor(AlertAction alertAction) {
		alertActionList.remove(alertAction);
	}

	/* (non-Javadoc)
	 * @see com.myrontuttle.adaptivetrader.AlertReceiver#stopWatchingAll(String)
	 *
	@Override
	public void stopWatchingAll(String userId) {
		for (AlertAction alertAction : alertActionList) {
			if (alertAction.getUserId().equals(userId)) {
				alertActionList.remove(alertAction);
			}
		}
	}
	
	/**
	 * Tests an incoming message to see if it matches one of the existing alert conditions
	 * If it does, tells the MoneyManager to execute the associated trade or order on the 
	 * portfolio with portfolioId
	 * @param userId
	 * @param condition The alert condition to match
	 * @return The number of time the condition matched an existing alert's condition
	 *
	@Override
	public int matchAlert(String condition) {
		int matches = 0;
		for (AlertAction action : alertActionList) {
			if (action.getAlert().getCondition().matches(condition)) {
				System.out.println("Alert Matched");
				try {
					tradeStrategy.takeAction(action);
				} catch (Exception e) {
					System.out.println("Unable to affect trade." + e.getMessage());
				}
				alertActionList.remove(action);
				matches++;
			}
		}
		return matches;
	}
	*/
}
