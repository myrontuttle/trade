package com.myrontuttle.fin.trade.alertreceivers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.myrontuttle.fin.trade.api.TradeStrategyService;

/**
 * Receives alerts sent via email
 * @author Myron Tuttle
 */
public class EmailAlertReceiver {

	public final static String NAME = "EmailAlert";
	public final static String HOST = "Host";
	public final static String PORT = "Port";
	public final static String PROTOCOL = "Protocol";
	public final static String USER = "User";
	public final static String PASS = "Password";
	public final static String PERIOD = "Period";
	public final static String TIME_UNIT = "Time Unit";

	public final static String IMAPS = "imaps";
	public final static String IMAP = "imap";
	public final static String POP = "pop3";
	
	private final static String IMAPS_PORT = "993";
	private final static String IMAP_PORT = "143";
	private final static String POP_PORT = "110";
	
	private final static int DEFAULT_DELAY = 0;
	private final static String DEFAULT_PERIOD = "60";
	private final static String DEFAULT_TIME_UNIT = "SECONDS";
	private final static String DEFAULT_HOST = "imap.gmail.com";
	private final static String DEFAULT_PORT = IMAPS_PORT;
	private final static String DEFAULT_PROTOCOL = IMAPS;
	
	private final static Map<String, String> availableParameters;
    static {
        Map<String, String> ap = new HashMap<String, String>();
        ap.put(HOST, "Email host (default = " + DEFAULT_HOST + ")");
        ap.put(PORT, "Port to connect on host (" + IMAPS + "=" + IMAPS_PORT + ", " +
				IMAP + "=" + IMAP_PORT + ", " + POP + "=" + POP_PORT + ")");
        ap.put(PROTOCOL, "Email protocol (e.g. " + IMAPS + ", " + IMAP + ", or " + POP + ")");
        ap.put(USER, "Email address");
        ap.put(PASS, "Email account password");
        ap.put(PERIOD, "How often to check for emailed alerts (default = " + DEFAULT_PERIOD + ")");
        ap.put(TIME_UNIT, "Time unit for period (default = " + DEFAULT_TIME_UNIT + ")");
        availableParameters = Collections.unmodifiableMap(ap);
    }

	public EmailAlertReceiver() {
	}

	public static String getName() {
		return NAME;
	}
	
	public static Map<String, String> getAvailableParameters() {
		return availableParameters;
	}
	
	public static boolean validateParameters(ReceiverDAO receiverDAO, AlertReceiver r) {
		for (String param : availableParameters.keySet()) {
			String value = r.getParameter(param);
			switch(param) {
			case HOST:
				if (value == null || value.isEmpty()) {
					System.out.println("No email host specified for " + r.getUserId() + ". Using default: " +
							DEFAULT_HOST);
					r.addParameter(HOST, DEFAULT_HOST);
				}
				break;
			case PORT:
				try {
					int port = Integer.parseInt(value);
					if (port < 1) {
						System.out.println(value + " is not a valid email port for user " + r.getUserId() +
								". Using default = " + DEFAULT_PORT);
						r.addParameter(PORT, DEFAULT_PORT);
					}
				} catch (NumberFormatException nfe) {
					System.out.println(value + " is not a valid email port for user " + r.getUserId() +
							". Using default = " + DEFAULT_PORT);
					r.addParameter(PORT, DEFAULT_PORT);
				}
				break;
			case PROTOCOL:
				if (value == null || value.isEmpty()) {
					System.out.println(value + " is not a valid email protocol for user " + r.getUserId() +
							". Using default = " + DEFAULT_PROTOCOL);
					r.addParameter(PROTOCOL, DEFAULT_PROTOCOL);
				}
				break;
			case USER:
				if (value == null || value.isEmpty()) {
					System.out.println("No email user/address specified for " + r.getUserId());
					return false;
				}
				break;
			case PASS:
				if (value == null || value.isEmpty()) {
					System.out.println("No email password specified for " + r.getUserId());
					return false;
				}
				break;
			case PERIOD:
				try {
					int period = Integer.parseInt(value);
					if (period < 1) {
						System.out.println(value + " is not a valid email retrieval period for user " + r.getUserId() +
								". Setting to default = " + DEFAULT_PERIOD);
						r.addParameter(PERIOD, String.valueOf(DEFAULT_PERIOD));
					}
				} catch (NumberFormatException nfe) {
					System.out.println(value + " is not a valid email retrieval period for user " + r.getUserId() +
							". Setting to default = " + DEFAULT_PERIOD);
					r.addParameter(PERIOD, String.valueOf(DEFAULT_PERIOD));
				}
				break;
			case TIME_UNIT:
				try {
					TimeUnit.valueOf(value);
				} catch (Exception e) {
					System.out.println(value + " is not a valid email retrieval time unit for user " + r.getUserId() +
							". Setting to default = " + DEFAULT_TIME_UNIT);
					r.addParameter(TIME_UNIT, DEFAULT_TIME_UNIT);
				}
			}
		}
		receiverDAO.updateReceiver(r);
		return true;
	}

	public static ScheduledFuture<?> startReceiving(ScheduledExecutorService ses, TradeStrategyService tss,
														AlertReceiver r) {

		System.out.println("Started receiving alerts for " + r.getParameter(USER) + " on " + r.getParameter(HOST));
		return ses.scheduleAtFixedRate(
				new MailRetriever(
						tss, r.getParameter(HOST), r.getParameter(PROTOCOL), 
						Integer.valueOf(r.getParameter(PORT)), 
						r.getParameter(USER), r.getParameter(PASS)), 
				DEFAULT_DELAY, 
				Integer.valueOf(r.getParameter(PERIOD)), 
				TimeUnit.valueOf(r.getParameter(TIME_UNIT)));
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
