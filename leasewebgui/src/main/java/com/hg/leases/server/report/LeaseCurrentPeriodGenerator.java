package com.hg.leases.server.report;

import static com.hg.leases.model.LeaseConstants.ONE;
import static com.hg.leases.server.LeaseDateUtilities.isGreaterThan;
import static com.hg.leases.server.LeaseDateUtilities.isGreaterThanOrEqualTo;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.Months;

import com.hg.leases.model.Lease;
import com.hg.leases.shared.GwtPair;

/**
 * Simple utility class to generate the 'current' annual lease period based on
 * the properties set on a lease.
 */
public class LeaseCurrentPeriodGenerator {

	private final int minimumToleranceInMonths;

	public LeaseCurrentPeriodGenerator(int minimumToleranceInMonths) {
		this.minimumToleranceInMonths = minimumToleranceInMonths;
	}

	public GwtPair<LocalDate> resolveCurrentPeriod(final Lease lease) {
		LocalDate proposedEnd = lease.getLeaseEnd();
		LocalDate proposedStart = proposedEnd.minusYears(ONE).plusDays(ONE);
		while (isGreaterThan(proposedStart, lease.getLeaseStart())) {
			if (updatedInSameYear(proposedStart, lease.getLastUpdate())
					|| updatedWithinMinimumTolerance(proposedStart,
							lease.getLastUpdate())
					|| straddlesUpdateDate(proposedStart, proposedEnd,
							lease.getLastUpdate())) {
				return new GwtPair<LocalDate>(proposedStart, proposedEnd);
			}
			proposedEnd = proposedEnd.minusYears(ONE);
			proposedStart = proposedEnd.minusYears(ONE).plusDays(ONE);
		}
		return new GwtPair<LocalDate>(lease.getLeaseStart(), proposedEnd);
	}

	public List<LocalDate> resolveCurrentSchedule(final Lease lease) {
		GwtPair<LocalDate> currentPeriod = resolveCurrentPeriod(lease);
		LocalDate start = currentPeriod.first();
		int rollDay = start.getDayOfMonth();
		List<LocalDate> schedule = new ArrayList<LocalDate>();
		while (start.isBefore(currentPeriod.second())) {
			schedule.add(start);
			start = resolveAmendedStartDate(start, rollDay);
		}
		return schedule;
	}

	private LocalDate resolveAmendedStartDate(final LocalDate start,
			final int rollDay) {
		LocalDate candidate = start.plusMonths(ONE);
		if (rollDay == candidate.getDayOfMonth()
				|| rollDay > candidate.dayOfMonth().withMaximumValue()
						.getDayOfMonth()) {
			return candidate;
		} else {
			int daysToAdd = rollDay - candidate.getDayOfMonth();
			return candidate.plusDays(daysToAdd);
		}
	}

	private boolean updatedInSameYear(final LocalDate proposedStart,
			final LocalDate lastUpdate) {
		return proposedStart.getYear() == lastUpdate.getYear();
	}

	private boolean updatedWithinMinimumTolerance(
			final LocalDate proposedStart, final LocalDate lastUpdate) {
		if (isGreaterThan(proposedStart, lastUpdate)) {
			int months = Months.monthsBetween(lastUpdate, proposedStart)
					.getMonths();
			return months < minimumToleranceInMonths;
		}
		return false;
	}

	private boolean straddlesUpdateDate(final LocalDate proposedStart,
			final LocalDate proposedEnd, final LocalDate lastUpdate) {
		return isGreaterThanOrEqualTo(lastUpdate, proposedStart)
				&& isGreaterThanOrEqualTo(proposedEnd, lastUpdate);
	}
}