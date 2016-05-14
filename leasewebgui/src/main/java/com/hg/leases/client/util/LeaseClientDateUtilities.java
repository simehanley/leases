package com.hg.leases.client.util;

import static com.google.gwt.user.datepicker.client.CalendarUtil.addDaysToDate;
import static com.google.gwt.user.datepicker.client.CalendarUtil.addMonthsToDate;
import static com.hg.leases.model.LeaseConstants.MONTHS_PER_YEAR;

import java.util.Date;

public class LeaseClientDateUtilities {

	public static Date addDays(final Date date, final int days) {
		if (date != null) {
			Date modified = new Date(date.getTime());
			addDaysToDate(modified, days);
			return modified;
		}
		return null;
	}

	public static Date addYears(final Date date, final int years) {
		if (date != null) {
			Date modified = new Date(date.getTime());
			addMonthsToDate(modified, years * MONTHS_PER_YEAR);
			return modified;
		}
		return null;
	}
}