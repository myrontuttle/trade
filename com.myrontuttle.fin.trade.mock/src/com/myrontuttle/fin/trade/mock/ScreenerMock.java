package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.AvailableScreenCriteria;
import com.myrontuttle.fin.trade.api.ScreenerService;
import com.myrontuttle.fin.trade.api.SelectedScreenCriteria;

public class ScreenerMock implements ScreenerService {

	@Override
	public AvailableScreenCriteria[] getAvailableCriteria(String userId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] screen(String userId,
			SelectedScreenCriteria[] selectedCriteria, String sortBy,
			int maxSymbols) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
