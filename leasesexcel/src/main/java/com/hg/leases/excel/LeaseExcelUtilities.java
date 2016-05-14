package com.hg.leases.excel;

import org.apache.commons.math3.util.Precision;

/**
 * @author hanleys
 */
public class LeaseExcelUtilities {

	public static double round(double value, int decimalPlaces) {
		return Precision.round(value, decimalPlaces);
	}
}