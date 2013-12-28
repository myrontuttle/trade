package com.myrontuttle.fin.trade.mock;

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

}
