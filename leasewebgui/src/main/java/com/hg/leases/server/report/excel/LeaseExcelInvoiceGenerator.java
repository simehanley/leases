package com.hg.leases.server.report.excel;

import static com.hg.leases.client.LeaseClientConstants.LEASE_ID;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.server.LeaseDateUtilities.formatFullDate;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.BANK_ACCOUNT_BSB_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.BANK_ACCOUNT_LOCATION_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.BANK_ACCOUNT_NAME_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.BANK_ACCOUNT_NUMBER_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.CATEGORY_ABN_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.CATEGORY_ADDRESS_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.CATEGORY_COMPANY_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.CATEGORY_PHONE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.CURRENT_DATE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_OUTGOINGS_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_OUTGOINGS_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_PARKING_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_PARKING_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_RENT_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_RENT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_SIGNAGE_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.MONTHLY_SIGNAGE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.PREMISES_ADDRESS_1_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.PREMISES_ADDRESS_2_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.RENTAL_LINE_1_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.RENTAL_LINE_2_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.TENANT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_OUTGOINGS_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_OUTGOINGS_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_PARKING_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_PARKING_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_RENT_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_RENT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_SIGNAGE_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.YEARLY_SIGNAGE_INDEX;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.LocalDate;

import com.hg.leases.model.Lease;
import com.hg.leases.server.report.LeaseReportGenerator;
import com.hg.leases.shared.GwtPair;

public class LeaseExcelInvoiceGenerator extends AbstractExcelReportGenerator
		implements LeaseReportGenerator {

	private static final Logger log = Logger
			.getLogger(LeaseExcelInvoiceGenerator.class);

	private static final String INVOICE_TEMPLATE_NAME = "TAX_INVOICE_TEMPLATE.xls";

	@Override
	public void generateReport(final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			generateExcelInvoice(request, response);
		} catch (IOException e) {
			log.error("Error generating lease excel invoice.", e);
		}
	}

	private void generateExcelInvoice(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		long leaseId = Long.valueOf(request.getParameter(LEASE_ID));
		Lease lease = dao.get(leaseId);
		if (lease != null) {
			generateExcelInvoice(lease, response);
		}
	}

	private void generateExcelInvoice(final Lease lease,
			final HttpServletResponse response) throws IOException {
		String filename = UUID.randomUUID().toString() + ".xls";
		OutputStream stream = createExcelResponseHeader(filename, response);
		byte[] excelInvoice = generateExcelInvoice(lease, filename);
		if (excelInvoice != null) {
			response.setContentLength((int) excelInvoice.length);
			stream.write(excelInvoice);
		}
		stream.close();
	}

	private byte[] generateExcelInvoice(final Lease lease, final String filename)
			throws IOException {
		Workbook book = new HSSFWorkbook(getClass().getResourceAsStream(
				"/excel/" + INVOICE_TEMPLATE_NAME));
		Sheet sheet = book.getSheet("Yearly Invoice");
		try {
			if (sheet != null) {
				generateExcelInvoice(lease, book, sheet);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				book.write(stream);
				stream.close();
				return stream.toByteArray();
			}
			return null;
		} finally {
			book.close();
		}
	}

	private void generateExcelInvoice(final Lease lease, final Workbook book,
			final Sheet sheet) {
		sheet.getRow(CATEGORY_COMPANY_INDEX.first())
				.getCell(CATEGORY_COMPANY_INDEX.second())
				.setCellValue(
						getStringValue(lease.getCategory().getCategoryCompany()));
		sheet.getRow(CATEGORY_ABN_INDEX.first())
				.getCell(CATEGORY_ABN_INDEX.second())
				.setCellValue(
						getStringValue(lease.getCategory().getCategoryAbn()));
		sheet.getRow(CATEGORY_ADDRESS_INDEX.first())
				.getCell(CATEGORY_ADDRESS_INDEX.second())
				.setCellValue(
						getStringValue(lease.getCategory().getCategoryAddress()));
		sheet.getRow(CATEGORY_PHONE_INDEX.first())
				.getCell(CATEGORY_PHONE_INDEX.second())
				.setCellValue(
						getStringValue(lease.getCategory().getCategoryPhone()));
		sheet.getRow(CURRENT_DATE_INDEX.first())
				.getCell(CURRENT_DATE_INDEX.second())
				.setCellValue(lease.getLastUpdate().toDate());
		sheet.getRow(TENANT_INDEX.first()).getCell(TENANT_INDEX.second())
				.setCellValue(lease.getTenant().getName());
		sheet.getRow(PREMISES_ADDRESS_1_INDEX.first())
				.getCell(PREMISES_ADDRESS_1_INDEX.second())
				.setCellValue(
						lease.getLeasedUnits() + "/"
								+ lease.getPremises().getAddressLineOne());
		sheet.getRow(RENTAL_LINE_1_INDEX.first())
				.getCell(RENTAL_LINE_1_INDEX.second())
				.setCellValue(resolveRentalLineOne(lease));
		sheet.getRow(RENTAL_LINE_2_INDEX.first())
				.getCell(RENTAL_LINE_2_INDEX.second())
				.setCellValue(resolveRentalLineTwo(lease));
		sheet.getRow(PREMISES_ADDRESS_2_INDEX.first())
				.getCell(PREMISES_ADDRESS_2_INDEX.second())
				.setCellValue(lease.getPremises().getAddressLineTwo());
		sheet.getRow(YEARLY_RENT_INDEX.first())
				.getCell(YEARLY_RENT_INDEX.second())
				.setCellValue(lease.yearlyAmount(RENT));
		sheet.getRow(YEARLY_RENT_GST_INDEX.first())
				.getCell(YEARLY_RENT_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(RENT));
		sheet.getRow(MONTHLY_RENT_INDEX.first())
				.getCell(MONTHLY_RENT_INDEX.second())
				.setCellValue(lease.monthlyAmount(RENT));
		sheet.getRow(MONTHLY_RENT_GST_INDEX.first())
				.getCell(MONTHLY_RENT_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(RENT));
		sheet.getRow(YEARLY_OUTGOINGS_INDEX.first())
				.getCell(YEARLY_OUTGOINGS_INDEX.second())
				.setCellValue(lease.yearlyAmount(OUTGOINGS));
		sheet.getRow(YEARLY_OUTGOINGS_GST_INDEX.first())
				.getCell(YEARLY_OUTGOINGS_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(OUTGOINGS));
		sheet.getRow(MONTHLY_OUTGOINGS_INDEX.first())
				.getCell(MONTHLY_OUTGOINGS_INDEX.second())
				.setCellValue(lease.monthlyAmount(OUTGOINGS));
		sheet.getRow(MONTHLY_OUTGOINGS_GST_INDEX.first())
				.getCell(MONTHLY_OUTGOINGS_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(OUTGOINGS));
		sheet.getRow(YEARLY_PARKING_INDEX.first())
				.getCell(YEARLY_PARKING_INDEX.second())
				.setCellValue(lease.yearlyAmount(PARKING));
		sheet.getRow(YEARLY_PARKING_GST_INDEX.first())
				.getCell(YEARLY_PARKING_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(PARKING));
		sheet.getRow(MONTHLY_PARKING_INDEX.first())
				.getCell(MONTHLY_PARKING_INDEX.second())
				.setCellValue(lease.monthlyAmount(PARKING));
		sheet.getRow(MONTHLY_PARKING_GST_INDEX.first())
				.getCell(MONTHLY_PARKING_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(PARKING));
		sheet.getRow(YEARLY_SIGNAGE_INDEX.first())
				.getCell(YEARLY_SIGNAGE_INDEX.second())
				.setCellValue(lease.yearlyAmount(SIGNAGE));
		sheet.getRow(YEARLY_SIGNAGE_GST_INDEX.first())
				.getCell(YEARLY_SIGNAGE_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(SIGNAGE));
		sheet.getRow(MONTHLY_SIGNAGE_INDEX.first())
				.getCell(MONTHLY_SIGNAGE_INDEX.second())
				.setCellValue(lease.monthlyAmount(SIGNAGE));
		sheet.getRow(MONTHLY_SIGNAGE_GST_INDEX.first())
				.getCell(MONTHLY_SIGNAGE_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(SIGNAGE));
		sheet.getRow(BANK_ACCOUNT_NAME_INDEX.first())
				.getCell(BANK_ACCOUNT_NAME_INDEX.second())
				.setCellValue(lease.getCategory().getCategoryAccountName());
		sheet.getRow(BANK_ACCOUNT_LOCATION_INDEX.first())
				.getCell(BANK_ACCOUNT_LOCATION_INDEX.second())
				.setCellValue(lease.getCategory().getCategoryBank());
		sheet.getRow(BANK_ACCOUNT_BSB_INDEX.first())
				.getCell(BANK_ACCOUNT_BSB_INDEX.second())
				.setCellValue(lease.getCategory().getCategoryBsb());
		sheet.getRow(BANK_ACCOUNT_NUMBER_INDEX.first())
				.getCell(BANK_ACCOUNT_NUMBER_INDEX.second())
				.setCellValue(lease.getCategory().getCategoryAccountNumber());
		HSSFFormulaEvaluator.evaluateAllFormulaCells(book);
	}

	private String resolveRentalLineOne(final Lease lease) {
		StringBuilder builder = new StringBuilder("Rental for ");
		builder.append(lease.getLeasedUnits() + "/"
				+ lease.getPremises().getAddressLineOne() + ", ");
		builder.append(lease.getPremises().getAddressLineTwo() + ".");
		return builder.toString();
	}

	private String resolveRentalLineTwo(final Lease lease) {
		GwtPair<LocalDate> dates = generator.resolveCurrentPeriod(lease);
		StringBuilder builder = new StringBuilder("For the period "
				+ formatFullDate(dates.first()) + "-"
				+ formatFullDate(dates.second()) + ".");
		return builder.toString();
	}
}