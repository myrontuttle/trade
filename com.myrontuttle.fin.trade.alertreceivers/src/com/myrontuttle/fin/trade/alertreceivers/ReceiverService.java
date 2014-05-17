package com.myrontuttle.fin.trade.alertreceivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.myrontuttle.fin.trade.api.AlertReceiverService;
import com.myrontuttle.fin.trade.api.TradeStrategyService;

public class ReceiverService implements AlertReceiverService {

	private final static int NUM_THREADS = 2;
	
	private static ReceiverDAO receiverDAO;

	private final ScheduledExecutorService ses;
	private final HashMap<String, ScheduledFuture<?>> receiverThreads = new HashMap<String, ScheduledFuture<?>>();

	private static TradeStrategyService tradeStrategyService;
	
	public ReceiverService() {
        this.ses = Executors.newScheduledThreadPool(NUM_THREADS);
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
		EmailAlertReceiver.NAME
	};
	
	@Override
	public String[] availableReceiverTypes() {
		return alertReceiverTypes;
	}

	@Override
	public Map<String, String> getAvailableParameters(String receiverType) {
		// Make sure additional receiver types are handled here
		if (receiverType.equals(EmailAlertReceiver.NAME)) {
			return EmailAlertReceiver.getAvailableParameters();
		}
		return null;
	}

	@Override
	public String addReceiver(String userId, String receiverType) {
		Receiver r = new Receiver(userId, receiverType);
		receiverDAO.saveReceiver(r);
		return r.getReceiverId();
	}

	@Override
	public List<String> getReceivers(String userId) {
		List<Receiver> rs = receiverDAO.findReceivers(userId);
		List<String> receiverIds = new ArrayList<String>(rs.size());
		for (Receiver r : rs) {
			receiverIds.add(r.getReceiverId());
		}
		return receiverIds;
	}

	@Override
	public void removeReceiver(String receiverId) {
		stopReceiving(receiverId);
		receiverDAO.removeReceiver(receiverId);
	}

	@Override
	public void setReceiverParameter(String receiverId, String name, String value) {
		if (receiverId != null && name != null && value != null) {
			Receiver r = receiverDAO.findReceiver(receiverId);
			r.addParameter(name, value);
			receiverDAO.updateReceiver(r);
		}
	}

	@Override
	public Map<String, String> getReceiverParameters(String receiverId) {
		Receiver r = receiverDAO.findReceiver(receiverId);
		return r.getParameters();
	}

	@Override
	public boolean parametersAreSet(String receiverId) {
		Map<String, String> params = getReceiverParameters(receiverId);
		Receiver r = receiverDAO.findReceiver(receiverId);
		Map<String, String> availableParameters = getAvailableParameters(r.getReceiverType());
		for (String name : availableParameters.keySet()) {
			if (!params.containsKey(name)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void startReceiving(String receiverId) {
		if (!parametersAreSet(receiverId)) {
			System.out.println("Missing parameters for receiver: " + receiverId);
			return;
		}
		Receiver r = receiverDAO.findReceiver(receiverId);
		if (r != null) {
			startReceiving(r);
		}
	}
	
	private void startReceiving(Receiver r) {
		if (r.getReceiverType().equals(EmailAlertReceiver.NAME)) {	
			if (!EmailAlertReceiver.validateParameters(receiverDAO, r)) {
				System.out.println("Invalid email parameters for receiver: " + r.getReceiverId());
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
	public void startReceivingAll(String userId) {
		List<Receiver> receivers = receiverDAO.findReceivers(userId);
		for (Receiver r : receivers) {
			startReceiving(r);
		}
	}

	@Override
	public void stopReceiving(String receiverId) {
		if (receiverThreads.containsKey(receiverId)) {
			receiverThreads.get(receiverId).cancel(true);
			receiverThreads.remove(receiverId);
		}
	}

	@Override
	public void stopReceivingAll(String userId) {
		List<Receiver> receivers = receiverDAO.findReceivers(userId);
		for (Receiver r : receivers) {
			stopReceiving(r.getReceiverId());
		}
	}

}
