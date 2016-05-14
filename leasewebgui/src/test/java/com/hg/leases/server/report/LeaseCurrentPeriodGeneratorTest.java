package com.hg.leases.server.report;

import junit.framework.TestCase;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.hg.leases.model.Lease;
import com.hg.leases.shared.GwtPair;

public class LeaseCurrentPeriodGeneratorTest {

	private final LeaseCurrentPeriodGenerator generator = new LeaseCurrentPeriodGenerator(
			3);

	@Test
	public void testUpdateDatePriorToStartDate() {
		Lease lease = new Lease();
		lease.setLastUpdate(new LocalDate(2013, 4, 1));
		lease.setLeaseStart(new LocalDate(2013, 6, 13));
		lease.setLeaseEnd(new LocalDate(2016, 5, 31));
		GwtPair<LocalDate> pair = generator.resolveCurrentPeriod(lease);
		TestCase.assertEquals(new LocalDate(2013, 6, 13), pair.first());
		TestCase.assertEquals(new LocalDate(2014, 5, 31), pair.second());
	}

	@Test
	public void testUpdateDateEqualToStartDate() {
		Lease lease = new Lease();
		lease.setLastUpdate(new LocalDate(2013, 6, 13));
		lease.setLeaseStart(new LocalDate(2013, 6, 13));
		lease.setLeaseEnd(new LocalDate(2016, 5, 31));
		GwtPair<LocalDate> pair = generator.resolveCurrentPeriod(lease);
		TestCase.assertEquals(new LocalDate(2013, 6, 13), pair.first());
		TestCase.assertEquals(new LocalDate(2014, 5, 31), pair.second());
	}

	@Test
	public void testUpdateDateAndProposedStartInTheSameYear() {
		Lease lease = new Lease();
		lease.setLastUpdate(new LocalDate(2015, 6, 15));
		lease.setLeaseStart(new LocalDate(2013, 6, 13));
		lease.setLeaseEnd(new LocalDate(2016, 5, 31));
		GwtPair<LocalDate> pair = generator.resolveCurrentPeriod(lease);
		TestCase.assertEquals(new LocalDate(2015, 6, 1), pair.first());
		TestCase.assertEquals(new LocalDate(2016, 5, 31), pair.second());
	}
	
	@Test
	public void testUpdateDateAndProposedStartDifferByMinimumTolerance() {
		Lease lease = new Lease();
		lease.setLastUpdate(new LocalDate(2014, 11, 6));
		lease.setLeaseStart(new LocalDate(2014, 2, 1));
		lease.setLeaseEnd(new LocalDate(2017, 1, 31));
		GwtPair<LocalDate> pair = generator.resolveCurrentPeriod(lease);
		TestCase.assertEquals(new LocalDate(2015, 2, 1), pair.first());
		TestCase.assertEquals(new LocalDate(2016, 1, 31), pair.second());
	}
}
