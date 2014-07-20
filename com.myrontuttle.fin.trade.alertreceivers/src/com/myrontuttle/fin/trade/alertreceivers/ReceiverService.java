package com.myrontuttle.fin.trade.alertreceivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.TradeStrategyService;

public class ReceiverService implements AlertReceiverService {

	private static final Logger logger = LoggerFactory.getLogger( ReceiverService.class );
			
	private final static int NUM_THREADS = 2;
	private final static int INITIAL_DELAY = 15;	// Seconds
	
	private static ReceiverDAO receiverDAO;

	private final ScheduledExecutorService ses;
	private final HashMap<Long, ScheduledFuture<?>> receiverThreads = new HashMap<Long, ScheduledFuture<?>>();

	private static TradeStrategyService tradeStrategyService;
	
	public ReceiverService() {
        this.ses = Executors.newScheduledThreadPool(NUM_THREADS);
	}
	
	public void init() {

		ses.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					List<AlertReceiver> receivers = receiverDAO.findActiveReceivers();
					if (receivers != null && receivers.size() > 0) {
						for(AlertReceiver r : receivers) {
							startReceiving(r);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}, INITIAL_DELAY, TimeUnit.SECONDS);
	}
	
	public static ReceiverDAO getReceiverDAO() {
		return receiverDAO;
	}
	
	public void setReceiverDAO(ReceiverDAO receiverDAO) {
		ReceiverService.receiverDAO = receiverDAO;
	}
	
	public static TradeStrategyService getTradeStrategyService() {
		return tradeStrategyService;
	}

	public void setTradeStrategyService(TradeStrategyService tradeStrategyService) {
		ReceiverService.tradeStrategyService = tradeStrategyService;
	}
	
	// Add the name of any additional receiver types here
	public static String[] alertReceiverTypes = new String[]{
		EmailAlertReceiver.DELIVERY_TYPE
	};
	
	@Override
	public String[] availableReceiverTypes() {
		return alertReceiverTypes;
	}

	@Override
	public Map<String, String> getAvailableParameters(String receiverType) {
		// Make sure additional receiver types are handled here
		if (receiverType.equals(EmailAlertReceiver.DELIVERY_TYPE)) {
			return EmailAlertReceiver.getAvailableParameters();
		}
		return null;
	}

	@Override
	public long addReceiver(long userId, String receiverType) {
		AlertReceiver r = new AlertReceiver(userId, receiverType);
		receiverDAO.saveReceiver(r);
		return r.getReceiverId();
	}

	@Override
	public List<Long> getReceivers(long userId) {
		List<AlertReceiver> rs = receiverDAO.findReceivers(userId);
		List<Long> receiverIds = new ArrayList<Long>(rs.size());
		for (AlertReceiver r : rs) {
			receiverIds.add(r.getReceiverId());
		}
		return receiverIds;
	}

	@Override
	public void removeReceiver(long receiverId) {
		stopReceiving(receiverId);
		receiverDAO.removeReceiver(receiverId);
	}

	@Override
	public void setReceiverParameter(long receiverId, String name, String value) {
		if (receiverId != 0 && name != null && value != null) {
			receiverDAO.addReceiverParameter(receiverId, name, value);
		}
	}

	@Override
	public Map<String, String> getReceiverParameters(long receiverId) {
		return receiverDAO.getReceiverParameters(receiverId);
	}

	@Override
	public boolean parametersAreSet(long receiverId) {
		AlertReceiver r = receiverDAO.findReceiver(receiverId);
		if (r.getReceiverType().equals(EmailAlertReceiver.DELIVERY_TYPE)) {
			EmailAlertReceiver.validateParameters(receiverDAO, r);
		}
		Map<String, String> availableParameters = getAvailableParameters(r.getReceiverType());
		Map<String, String> params = r.getParameters();
		for (String name : availableParameters.keySet()) {
			if (!params.containsKey(name)) {
				logger.debug("{} missing from receiver {}.", name, receiverId);
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void setReceiverActive(long receiverId, boolean isActive) {
		receiverDAO.setReceiverActive(receiverId, isActive);
	}

	@Override
	public void startReceiving(long receiverId) {
		if (!parametersAreSet(receiverId)) {
			logger.warn("Missing parameters for receiver: {}");
			return;
		}
		AlertReceiver r = receiverDAO.findReceiver(receiverId);
		if (r != null) {
			startReceiving(r);
		}
	}
	
	private void startReceiving(AlertReceiver r) {

		logger.info("Starting receiving alerts for receiver: {}.", r.getReceiverId());
		if (r.getReceiverType().equals(EmailAlertReceiver.DELIVERY_TYPE)) {	
			if (!EmailAlertReceiver.validateParameters(receiverDAO, r)) {
				logger.warn("Invalid email parameters for receiver: {}", r.getReceiverId());
				return;
			}
			
			if (receiverThreads.containsKey(r.getReceiverId())) {
				// Already running. Remove
				receiverThreads.get(r.getReceiverId()).cancel(true);
			}
			
			// Start with new parameters
			receiverThreads.put(r.getReceiverId(), EmailAlertReceiver.startReceiving(ses, tradeStrategyService, r));
		}
	}

	@Override
	public void startReceivingAll(long userId) {
		List<AlertReceiver> receivers = receiverDAO.findReceivers(userId);
		for (AlertReceiver r : receivers) {
			startReceiving(r);
		}
	}

	@Override
	public void stopReceiving(long receiverId) {
		if (receiverThreads.containsKey(receiverId)) {
			receiverThreads.get(receiverId).cancel(true);
			receiverThreads.remove(receiverId);
		}
	}

	@Override
	public void stopReceivingAll(long userId) {
		List<AlertReceiver> receivers = receiverDAO.findReceivers(userId);
		for (AlertReceiver r : receivers) {
			stopReceiving(r.getReceiverId());
		}
	}

}
