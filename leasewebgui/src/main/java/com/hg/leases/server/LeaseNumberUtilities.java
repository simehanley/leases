package com.hg.leases.server;

import java.text.NumberFormat;

public class LeaseNumberUtilities {

	private final static NumberFormat formatter = NumberFormat
			.getCurrencyInstance();

	public static String formatCurrency(double value) {
		return formatter.format(value);
	}
}