package com.hg.leases.model.utilities;

import static com.hg.leases.model.LeaseConstants.TWO;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

/**
 * @author hanleys
 */
public class LeaseNumericUtilities {

	public static double round(final double value) {
		return round(value, TWO);
	}

	public static double round(final double value, final int places) {
		return new BigDecimal(value).setScale(places, HALF_EVEN)
				.doubleValue();
	}
}