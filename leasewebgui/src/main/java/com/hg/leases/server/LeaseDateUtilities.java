package com.hg.leases.server;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LeaseDateUtilities {

	private static final DateTimeFormatter FORMATTER = DateTimeFormat
			.forPattern("dd/MM/yyyy");

	private static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormat
			.forPattern("MMM-yy");

	private static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormat
			.forPattern("dd MMMM yyyy");

	private static final DateTimeFormatter INVOICE_DATE_FORMATTER = DateTimeFormat
			.forPattern("MMyy");

	public static boolean isBeforeOrEqual(final LocalDate first,
			final LocalDate second) {
		return first.isBefore(second) || first.isEqual(second);
	}

	public static boolean isGreaterThan(final LocalDate first,
			final LocalDate second) {
		return !isBeforeOrEqual(first, second);
	}

	public static boolean isGreaterThanOrEqualTo(final LocalDate first,
			final LocalDate second) {
		return first.isAfter(second) || first.isEqual(second);
	}

	public static String format(final LocalDate date) {
		return FORMATTER.print(date);
	}

	public static String formatShortDate(final LocalDate date) {
		return SHORT_DATE_FORMATTER.print(date);
	}

	public static String formatFullDate(final LocalDate date) {
		return FULL_DATE_FORMATTER.print(date);
	}

	public static String formatInvoiceDate(final LocalDate date) {
		return INVOICE_DATE_FORMATTER.print(date);
	}
}