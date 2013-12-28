package com.myrontuttle.fin.trade.mock;

import com.myrontuttle.fin.trade.api.QuoteService;

public class QuoteMock implements QuoteService {

	@Override
	public double getLast(String userId, String symbol) throws Exception {
		return 105.25;
	}

}
