package com.hg.leases.dao.test;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.HISTORICAL_RENT;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;

import java.util.ArrayList;

import org.joda.time.LocalDate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hg.leases.dao.LeaseDao;
import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseCategory;
import com.hg.leases.model.LeaseIncidental;
import com.hg.leases.model.LeaseMetaData;
import com.hg.leases.model.LeasePremises;
import com.hg.leases.model.LeaseTenant;

/**
 * @author hanleys
 */
public class LeaseModelTest {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext ctx = null;

		try {
			ctx = new ClassPathXmlApplicationContext("leases-dao-context.xml");

			/** lease category **/
			LeaseCategory category = new LeaseCategory("LEASE_CATEGORY");

			/** lease premises and meta data **/
			LeasePremises premises = new LeasePremises(
					"LEASE_ADDRESS_LINE_ONE", "LEASE_ADDRESS_LINE_TWO");

			/** lease tenant **/
			LeaseTenant tenant = new LeaseTenant("LEASE_TENANT");

			/** the lease **/
			Lease lease = new Lease(new LocalDate(2015, 03, 24), new LocalDate(
					2017, 03, 24), null, null, 5000., "1",
					new ArrayList<LeaseIncidental>(), tenant, premises,
					category, null, false, false, EMPTY_STRING);

			lease.addMetaData(new LeaseMetaData(HISTORICAL_RENT, "Year 1",
					"100.00", 0));
			lease.addMetaData(new LeaseMetaData(HISTORICAL_RENT, "Year 2",
					"100.00", 1));
			lease.addMetaData(new LeaseMetaData(HISTORICAL_RENT, "Year 3",
					"100.00", 2));

			lease.addIncidental(new LeaseIncidental(RENT, 10000., 0.06,
					EMPTY_STRING));
			lease.addIncidental(new LeaseIncidental(SIGNAGE, 10000., 0.10,
					EMPTY_STRING));
			lease.addIncidental(new LeaseIncidental(PARKING, 10000., 0.20,
					EMPTY_STRING));
			lease.addIncidental(new LeaseIncidental(OUTGOINGS, 10000., 0.10,
					EMPTY_STRING));

			/** persist lease **/
			ctx.getBean(LeaseDao.class).save(lease);

			/** retrieve lease **/
			Lease leaseSaved = ctx.getBean(LeaseDao.class).get(lease.getId());

			/** update lease **/
			leaseSaved.setLastUpdate(new LocalDate());
			leaseSaved.setNotes("Updating lease according to the schedule.");

			ctx.getBean(LeaseDao.class).save(leaseSaved);

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
	}
}