package com.hg.leases.shared;

import static com.hg.leases.model.LeaseConstants.TWO;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

public class GwtLeaseUtilities {

	private static final double ACCURACY = 1e-12;

	public static double round(final double value) {
		return round(value, TWO);
	}

	public static double round(final double value, final int places) {
		return new BigDecimal(value).setScale(places, HALF_EVEN).doubleValue();
	}

	public static boolean areEqual(double one, double two) {
		return Math.abs(one - two) < ACCURACY;
	}
}