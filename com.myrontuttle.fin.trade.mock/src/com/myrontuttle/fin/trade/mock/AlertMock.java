package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.SelectedAlert;

public class AlertMock implements AlertService {

	@Override
	public AvailableAlert[] getAvailableAlerts(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AvailableAlert getAlert(String userId, int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AvailableAlert getPriceBelowAlert(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AvailableAlert getPriceAboveAlert(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseCondition(AvailableAlert alert, String symbol,
			double... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getLowerDouble(String userId, int id, String symbol,
			int criteriaIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getUpperDouble(String userId, int id, String symbol,
			int criteriaIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getListLength(String userId, int id, int criteriaIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean addAlertDestination(String userId, String alertAddress,
			String alertType) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setupAlerts(String userId, SelectedAlert... alerts)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SelectedAlert[] getActiveAlerts(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAlert(String userId, SelectedAlert alert)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllAlerts(String userId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
