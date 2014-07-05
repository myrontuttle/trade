package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.AlertService;
import com.myrontuttle.fin.trade.api.AvailableAlert;
import com.myrontuttle.fin.trade.api.SelectedAlert;

public class AlertServiceMock implements AlertService {

	private final int belowId = 1;
	private final int aboveId = 2;
	private final String belowCondition = "{symbol}'s price fell below {Price}";
	private final String aboveCondition = "{symbol}'s price rose above {Price}";
	private final String actualCondition = "AAPL's price fell below 400";
	private final double alertUpper = 500.0;
	private final double alertLower = 50.0;
	private final int alertListLength = 0;
	private AvailableAlert priceBelowAlert = new AvailableAlert(belowId, belowCondition, 
			new String[]{"DOUBLE"}, 
			new String[]{"Price"}, new String[]{"Fund.Price.Low.Lifetime"}, 
			new String[]{"Quote.Value.Last"}, null);
	private AvailableAlert priceAboveAlert = new AvailableAlert(aboveId, aboveCondition, 
			new String[]{"DOUBLE"}, 
			new String[]{"Price"}, new String[]{"Fund.Price.High.Lifetime"}, 
			new String[]{"Quote.Value.Last"}, null);
	private AvailableAlert[] availableAlerts = new AvailableAlert[]{
			priceBelowAlert,
			priceAboveAlert
	};
	private String[] setupAlerts = new String[]{
			"1-123",
			"2-456"
	};
	private SelectedAlert[] selectedAlerts = new SelectedAlert[]{
			new ActiveAlert()
	};
	
	@Override
	public AvailableAlert[] getAvailableAlerts(String userId) throws Exception {
		return availableAlerts;
	}

	@Override
	public AvailableAlert getAlert(String userId, int id) throws Exception {
		return priceBelowAlert;
	}

	@Override
	public AvailableAlert getPriceBelowAlert(String userId) throws Exception {
		return priceBelowAlert;
	}

	@Override
	public AvailableAlert getPriceAboveAlert(String userId) throws Exception {
		return priceAboveAlert;
	}

	@Override
	public String parseCondition(AvailableAlert alert, String symbol,
			double... params) {
		return actualCondition;
	}

	@Override
	public double getLowerDouble(String userId, int id, String symbol,
			int criteriaIndex) {
		return alertLower;
	}

	@Override
	public double getUpperDouble(String userId, int id, String symbol,
			int criteriaIndex) {
		return alertUpper;
	}

	@Override
	public int getListLength(String userId, int id, int criteriaIndex) {
		return alertListLength;
	}

	@Override
	public boolean addAlertDestination(String userId, String alertAddress,
			String alertType) throws Exception {
		return false;
	}

	@Override
	public String[] setupAlerts(String userId, SelectedAlert... alerts)
			throws Exception {
		return setupAlerts;
	}

	@Override
	public SelectedAlert[] getActiveAlerts(String userId) throws Exception {
		return selectedAlerts;
	}

	@Override
	public boolean removeAlert(String userId, String alertId)
			throws Exception {
		return true;
	}

	@Override
	public boolean removeAllAlerts(String userId) throws Exception {
		return false;
	}

	@Override
	public String setupAlert(String userId, int alertId, String condition,
			String symbol, double... params) {
		return setupAlerts[0];
	}
}

class ActiveAlert implements SelectedAlert {

	double[] params = new double[]{400};
	
	@Override
	public int getAlertId() {
		return 1;
	}

	@Override
	public String getCondition() {
		return "{symbol}'s price fell below {Price}";
	}

	@Override
	public String getSymbol() {
		return "AAPL";
	}

	@Override
	public double getParam(int index) {
		return params[index];
	}

	@Override
	public double[] getParams() {
		return params;
	}
	
}