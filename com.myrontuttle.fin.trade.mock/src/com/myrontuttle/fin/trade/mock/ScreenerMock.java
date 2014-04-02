package com.myrontuttle.fin.trade.mock;

import java.util.HashMap;
import java.util.HashSet;

import com.myrontuttle.fin.trade.api.AvailableScreenCriteria;
import com.myrontuttle.fin.trade.api.ScreenerService;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;

public class ScreenerMock implements ScreenerService {
	
	private AvailableScreenCriteria[] availableScreenCriteria = 
			new AvailableScreenCriteria[]{
				new AvailableScreenCriteria("RCCAssetClass", "OR", 
						new String[]{"LIKE[0]=Large Cap",
									 "LIKE[0]=Micro Cap",
									 "LIKE[0]=Mid Cap",
									 "LIKE[0]=Small Cap"}),
				new AvailableScreenCriteria("RCCRegion", "OR", 
						new String[]{"LIKE[0]=",
									 "LIKE[0]=Africa & Mideast",
									 "LIKE[0]=Americas",
									 "LIKE[0]=Asia-Pacific",
									 "LIKE[0]=Europe"})
	};
	
	private SelectedScreenCriteria[] selectedScreenCriteria = 
			new SelectedScreenCriteria[] {
				new SelectedScreenCriteria("RCCAssetClass", "LIKE[0]=Large Cap", "OR")
	};
	
	private HashSet<String> usedCriteria = new HashSet<String>();
	
	private HashMap<String, String> settings = new HashMap<String, String>();

	private String[] screenSymbols = new String[]{"AAPL", "MSFT", "CSCO", "GM", "A"};
	
	@Override
	public AvailableScreenCriteria[] getAvailableCriteria(String userId)
			throws Exception {
		return availableScreenCriteria;
	}

	@Override
	public String[] screen(String userId,
			SelectedScreenCriteria[] selectedCriteria, String sortBy,
			int maxSymbols) throws Exception {
		return screenSymbols;
	}

	@Override
	public SelectedScreenCriteria[] getFixedCriteria() throws Exception {
		return selectedScreenCriteria;
	}

	@Override
	public void setFixedCriteria(SelectedScreenCriteria[] fixedCriteria)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(HashMap<String, String> settings) throws Exception {
		this.settings = settings;
	}

	@Override
	public HashMap<String, String> getSettings() throws Exception {
		settings.put("marketer", "RT");
		return settings;
	}

	@Override
	public void setCriteriaUsed(HashSet<String> criteriaUsed) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashSet<String> getCriteriaUsed() throws Exception {
		usedCriteria.add("RCCAssetClass");
		return usedCriteria;
	}

}
