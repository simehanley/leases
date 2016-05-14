package com.hg.leases.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class LeaseFormatUtils {

	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat
			.getFormat("dd/MM/yyyy");

	private static final NumberFormat DOUBLE_FORMAT = NumberFormat
			.getFormat("#,##0.00");

	private static final NumberFormat PERCENT_FORMAT = NumberFormat
			.getFormat("#.##%");

	public static DateTimeFormat getDateFormat() {
		return DATE_FORMAT;
	}

	public static NumberFormat getDoubleFormat() {
		return DOUBLE_FORMAT;
	}

	public static NumberFormat getPercentFormat() {
		return PERCENT_FORMAT;
	}
}
