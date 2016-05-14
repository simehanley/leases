package com.hg.leases.server;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.ONE;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.server.LeaseDateUtilities.format;
import static com.hg.leases.server.LeaseDateUtilities.isBeforeOrEqual;
import static java.util.Collections.sort;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import com.hg.leases.dao.LeaseDao;
import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseIncidental;

/**
 * Simple service that keeps track of what leases currently require updates.
 * Email is sent listing details of actions that are applicable.
 * 
 * @author hanleys
 *
 */
public class LeaseMaintenanceService {

	private static final Logger log = Logger
			.getLogger(LeaseMaintenanceService.class);

	private static final DecimalFormat NUMBER_FORMATTER = new DecimalFormat(
			"#,##0.00");

	private final int monthlyUpdateInterval;

	@Autowired
	private LeaseDao dao;

	@Autowired
	private LeaseMaintenanceEmailHelper emailHelper;

	public LeaseMaintenanceService(int monthlyUpdateInterval) {
		super();
		this.monthlyUpdateInterval = monthlyUpdateInterval;
	}

	public void run() {
		try {
			log.info("Running lease maintenance service.");
			Set<Lease> leases = dao.get();
			List<Lease> expired = new ArrayList<Lease>();
			List<Lease> renew = new ArrayList<Lease>();
			List<Lease> reprice = new ArrayList<Lease>();
			LocalDate now = new LocalDate();
			if (!isEmpty(leases)) {
				for (Lease lease : leases) {
					if (!lease.isInactive()) {
						checkExpired(now, lease, expired);
						checkRenew(now, lease, renew);
						checkReprice(now, lease, reprice);
					}
				}
			}
			String emailBody = draftActions(now, expired, renew, reprice);
			if (hasLength(emailBody)) {
				emailHelper.sendEmail(emailBody);
				log.info("Successfully run lease maintenance service.");
			} else {
				log.info("Successfully run lease maintenance service with no actions found.");
			}
		} catch (Throwable t) {
			log.info("Error running lease maintenance service.", t);
		}
	}

	private void checkExpired(final LocalDate now, final Lease lease,
			final List<Lease> expired) {
		if (isBeforeOrEqual(lease.getLeaseEnd(), now)) {
			expired.add(lease);
		}
		if (!isEmpty(expired)) {
			sort(expired);
		}
	}

	private void checkRenew(final LocalDate now, final Lease lease,
			final List<Lease> renew) {
		if (lease.getLeaseEnd().isAfter(now)) {
			LocalDate minimalRenewDate = now.plusMonths(monthlyUpdateInterval);
			if (isBeforeOrEqual(lease.getLeaseEnd(), minimalRenewDate)) {
				LocalDate minimalUpdateDate = lease.getLeaseEnd().minusMonths(
						monthlyUpdateInterval);
				if (isBeforeOrEqual(lease.getLastUpdate(), minimalUpdateDate)) {
					renew.add(lease);
				}
			}
		}
		if (!isEmpty(renew)) {
			sort(renew);
		}
	}

	private void checkReprice(final LocalDate now, final Lease lease,
			final List<Lease> reprice) {
		if (lease.getLeaseEnd().isAfter(now)) {
			LocalDate minimalRenewDate = now.plusMonths(monthlyUpdateInterval);
			if (!isBeforeOrEqual(lease.getLeaseEnd(), minimalRenewDate)) {
				LocalDate resolvedRepriceDate = resolveRepriceDate(now, lease);
				LocalDate minimalUpdateDate = resolvedRepriceDate
						.minusMonths(monthlyUpdateInterval);
				if (isBeforeOrEqual(minimalUpdateDate, now)
						&& isBeforeOrEqual(lease.getLastUpdate(),
								minimalUpdateDate)) {
					int lastUpdateYear = lease.getLastUpdate().getYear();
					int minimalUpdateYear = minimalUpdateDate.getYear();
					if (lastUpdateYear < minimalUpdateYear) {
						reprice.add(lease);
					}
				}
			}
		}
		if (!isEmpty(reprice)) {
			sort(reprice);
		}
	}

	private String draftActions(final LocalDate now, final List<Lease> expired,
			final List<Lease> renew, final List<Lease> reprice) {
		if (!isEmpty(expired) || !isEmpty(renew) || !isEmpty(reprice)) {
			List<String> actions = new ArrayList<String>();
			setEmailHtmlHeader(actions);
			if (!isEmpty(expired)) {
				actions.add("<h2>Expired Actions</h2><table><tr><th>Tenant</th>"
						+ "<th>Premises</th><th>Unit(s)</th><th>End Date</th><th>Last Update</th></tr>");
				for (Lease lease : expired) {
					draftExpiredAction(lease, actions);
				}
				actions.add("</table>");
			}
			if (!isEmpty(renew)) {
				actions.add("<h2>Renew Actions</h2><table><tr><th>Tenant</th>"
						+ "<th>Premises</th><th>Unit(s)</th><th>End Date</th><th>Last Update</th></tr>");
				for (Lease lease : renew) {
					draftRenewAction(lease, actions);
				}
				actions.add("</table>");
			}
			if (!isEmpty(reprice)) {
				actions.add("<h2>Reprice Actions</h2><table><tr><th>Tenant</th>"
						+ "<th>Premises</th><th>Unit(s)</th><th>Reprice Date</th><th>Last Update</th>"
						+ "<th>Old Lease Amount</th><th>New Lease Amount</th></tr>");
				for (Lease lease : reprice) {
					draftRepriceAction(lease, actions, now);
				}
				actions.add("</table>");
			}
			actions.add("</html>");
			StringBuilder leaseBuilder = new StringBuilder();
			for (String str : actions) {
				leaseBuilder.append(str + "\n");
			}
			return leaseBuilder.toString();
		}
		return EMPTY_STRING;
	}

	private void draftExpiredAction(final Lease expired,
			final List<String> actions) {
		draftDefaultAction(expired, actions);
	}

	private void draftRenewAction(final Lease renew, final List<String> actions) {
		draftDefaultAction(renew, actions);
	}

	private void draftDefaultAction(final Lease lease,
			final List<String> actions) {
		actions.add("<tr><td>" + lease.getTenant().getName() + "</td><td>"
				+ lease.getPremises().getAddressLineOne() + "</td><td>"
				+ lease.getLeasedUnits() + "</td><td>"
				+ format(lease.getLeaseEnd()) + "</td><td>"
				+ format(lease.getLastUpdate()) + "</td></tr>");
	}

	private void draftRepriceAction(final Lease reprice,
			final List<String> actions, final LocalDate now) {
		actions.add("<tr><td>" + reprice.getTenant().getName() + "</td><td>"
				+ reprice.getPremises().getAddressLineOne() + "</td><td>"
				+ reprice.getLeasedUnits() + "</td><td>"
				+ format(resolveRepriceDate(now, reprice)) + "</td><td>"
				+ format(reprice.getLastUpdate()) + "</td><td>"
				+ resolveCurrentLeaseAmount(reprice) + "</td><td>"
				+ resolveNewLeaseAmount(reprice) + "</td></tr>");
	}

	private LocalDate resolveRepriceDate(final LocalDate now, final Lease lease) {
		LocalDate candidate = lease.getLeaseEnd();
		while (candidate.isAfter(now)) {
			candidate = candidate.minusYears(ONE);
		}
		return candidate.plusYears(ONE);
	}

	private String resolveNewLeaseAmount(final Lease lease) {
		LeaseIncidental incidental = lease.getIncidental(RENT);
		double newRent = incidental.getAmount()
				* (ONE + incidental.getPercentage());
		return NUMBER_FORMATTER.format(newRent);
	}

	private String resolveCurrentLeaseAmount(final Lease lease) {
		LeaseIncidental incidental = lease.getIncidental(RENT);
		return NUMBER_FORMATTER.format(incidental.getAmount());
	}

	private void setEmailHtmlHeader(final List<String> actions) {
		actions.add("<!DOCTYPE html><html><head><style>"
				+ "table {width:100%;} "
				+ "table, th, td {border: 1px solid black; border-collapse: collapse;} "
				+ "th { padding: 5px; text-align: center;} "
				+ "tr { padding: 5px; text-align: left;} "
				+ "table tr {background-color:#eee;} "
				+ "table th {background-color: black; color: white;} "
				+ "</style></head><body>");
	}
}