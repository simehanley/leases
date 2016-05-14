package com.hg.leases.server.report.myob;

import static com.hg.leases.client.LeaseClientConstants.LEASE_ID;
import static com.hg.leases.client.LeaseClientConstants.RETURN;
import static com.hg.leases.client.LeaseClientConstants.TAB;
import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.SPACE;
import static com.hg.leases.model.LeaseConstants.TWO;
import static com.hg.leases.model.LeaseConstants.ZERO;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.server.LeaseDateUtilities.format;
import static com.hg.leases.server.LeaseDateUtilities.formatInvoiceDate;
import static com.hg.leases.server.LeaseDateUtilities.formatShortDate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseIncidental;
import com.hg.leases.server.report.LeaseReportGenerator;

public class LeaseMyobScheduleGenerator extends AbstractMyobReportGenerator
		implements LeaseReportGenerator {

	private static final Logger log = Logger
			.getLogger(LeaseMyobScheduleGenerator.class);

	private static final String SCHEDULE_TEMPLATE_NAME = "MYOB_RENT_SCHEDULE.txt";

	@Override
	public void generateReport(final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			generateMyobSchedule(request, response);
		} catch (Throwable t) {
			log.error("Error generating lease myob schedule.", t);
		}
	}

	private void generateMyobSchedule(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			URISyntaxException {
		long leaseId = Long.valueOf(request.getParameter(LEASE_ID));
		Lease lease = dao.get(leaseId);
		if (lease != null) {
			generateMyobSchedule(lease, response);
		}
	}

	private void generateMyobSchedule(final Lease lease,
			final HttpServletResponse response) throws IOException,
			URISyntaxException {
		String filename = UUID.randomUUID().toString() + ".txt";
		OutputStream stream = createMyobResponseHeader(filename, response);
		byte[] schedule = generateMyobSchedule(lease, filename);
		if (schedule != null) {
			response.setContentLength((int) schedule.length);
			stream.write(schedule);
		}
		stream.close();
	}

	@SuppressWarnings("unused")
	private byte[] generateMyobSchedule(final Lease lease, final String filename)
			throws IOException, URISyntaxException {
		File source = new File(this.getClass().getClassLoader()
				.getResource("/myob/" + SCHEDULE_TEMPLATE_NAME).toURI());
		String path = this.getClass().getClassLoader().getResource("/myob/")
				.getPath();
		File dest = new File(path + filename);
		FileUtils.copyFile(source, dest);
		if (dest != null) {
			try {
				generateMyobSchedule(lease, dest);
				return FileUtils.readFileToByteArray(dest);
			} finally {
				Files.deleteIfExists(dest.toPath());
			}
		}
		return null;
	}

	private void generateMyobSchedule(final Lease lease, final File file)
			throws IOException {
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				file, true)));
		writer.write(RETURN);
		try {
			List<LocalDate> schedule = generator.resolveCurrentSchedule(lease);
			for (LocalDate date : schedule) {
				generateMyobSchedule(date, RENT, lease, writer);
				generateMyobSchedule(date, OUTGOINGS, lease, writer);
				generateMyobSchedule(date, PARKING, lease, writer);
				generateMyobSchedule(date, SIGNAGE, lease, writer);
				writer.append("\r");
			}
		} finally {
			writer.close();
		}
	}

	private void generateMyobSchedule(final LocalDate date, final String type,
			final Lease lease, final PrintWriter writer) {
		LeaseIncidental incidental = lease.getIncidental(type);
		if (incidental != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(lease.getTenant().getName() + TAB + TAB);
			builder.append(resolveAddressLineOne(lease));
			builder.append(resolveAddressLineTwo(lease));
			builder.append(resolveAddressLineThree(lease));
			builder.append(TAB);
			builder.append("X" + TAB);
			builder.append(resolveInvoiceNumber(lease, date) + TAB); /* invoice */
			builder.append(format(date) + TAB);
			builder.append(formatShortDate(date) + TAB);
			builder.append(TAB);
			builder.append(EMPTY_STRING + TAB);
			builder.append(type + TAB);
			builder.append(resolveAccount(incidental) + TAB);
			builder.append(lease.monthlyAmount(type) + TAB);
			builder.append(lease.monthlyTotal(type) + TAB);
			builder.append(resolveJobNumber(lease) + TAB);
			builder.append(resolveNotes(lease) + TAB);
			builder.append("Sale; " + lease.getTenant().getName() + TAB);
			builder.append(TAB + TAB + TAB + TAB);
			builder.append(resolveGstType(lease) + TAB);
			builder.append(ZERO_DBL + TAB);
			builder.append(lease.monthlyGst(type) + TAB);
			builder.append(ZERO_DBL + TAB);
			builder.append(TAB + TAB);
			builder.append(resolveGstType(lease) + TAB);
			builder.append(ZERO_DBL + TAB);
			builder.append(ZERO_DBL + TAB);
			builder.append(ZERO_DBL + TAB);
			builder.append("I" + TAB);
			builder.append(TAB + TAB);
			builder.append(TWO + TAB);
			builder.append(ZERO + TAB);
			builder.append(ZERO + TAB);
			builder.append(ZERO + TAB);
			builder.append(ZERO + TAB);
			builder.append(ZERO_DBL + TAB);
			builder.append(TAB + TAB + TAB + TAB + TAB + TAB + TAB + TAB + TAB
					+ TAB + TAB);
			builder.append("*None" + TAB);
			builder.append(EMPTY_STRING + TAB); /* record id */
			writer.println(builder.toString());
		}
	}

	private String resolveAddressLineOne(final Lease lease) {
		return lease.getTenant().getName() + TAB;
	}

	private String resolveAddressLineTwo(final Lease lease) {
		return "Unit " + lease.getLeasedUnits() + "/"
				+ lease.getPremises().getAddressLineOne() + TAB;
	}

	private String resolveAddressLineThree(final Lease lease) {
		return lease.getPremises().getAddressLineTwo() + TAB;
	}

	private String resolveGstType(final Lease lease) {
		return (lease.isResidential()) ? "ITS" : "GST";
	}

	private String resolveAccount(final LeaseIncidental incidental) {
		return (incidental.getAccount() != null) ? incidental.getAccount()
				: EMPTY_STRING;
	}

	private String resolveJobNumber(final Lease lease) {
		return (lease.getJobNo() != null) ? lease.getJobNo() : EMPTY_STRING;
	}

	private String resolveNotes(final Lease lease) {
		return (lease.getNotes() != null) ? lease.getNotes()
				.replace("\r", SPACE).replace("\n", SPACE) : EMPTY_STRING;
	}

	private String resolveInvoiceNumber(final Lease lease, final LocalDate date) {
		return lease.getTenant().getName().substring(ZERO, TWO).toUpperCase()
				+ formatInvoiceDate(date);
	}
}