package com.hg.leases.server.report.excel;

import static com.hg.leases.client.LeaseClientConstants.LEASE_ID;
import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.HISTORICAL_RENT;
import static com.hg.leases.model.LeaseConstants.NOTICE;
import static com.hg.leases.model.LeaseConstants.ONE;
import static com.hg.leases.model.LeaseConstants.OPTION;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.SPECIAL_CONDITION;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.server.LeaseDateUtilities.format;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.HISTORIC_RENTS_START_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.NOTICES_START_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.OPTIONS_START_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_ADDRESS_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_AREA_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_BOND_BANK_GUARANTEE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_DEVELOPMENT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_LEASE_END_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_LEASE_OPTION_END_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_LEASE_OPTION_START_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_LEASE_START_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_LESSEE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_OUTGOINGS_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_OUTGOINGS_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_PARKING_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_PARKING_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_RENT_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_RENT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_SIGNAGE_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_MONTHLY_SIGNAGE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_OUTGOINGS_PRECENT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_RENT_FREE_PERIOD_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_RENT_INCREASE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_TITLE_1_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_TITLE_2_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_UNIT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_OUTGOINGS_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_OUTGOINGS_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_PARKING_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_PARKING_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_RENT_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_RENT_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_SIGNAGE_GST_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SCHEDULE_YEARLY_SIGNAGE_INDEX;
import static com.hg.leases.server.report.LeaseReportExportServiceConstants.SPECIAL_CONDITIONS_START_INDEX;
import static java.util.Collections.sort;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseIncidental;
import com.hg.leases.model.LeaseMetaData;
import com.hg.leases.server.report.LeaseReportGenerator;
import com.hg.leases.shared.GwtPair;

public class LeaseExcelScheduleGenerator extends AbstractExcelReportGenerator
		implements LeaseReportGenerator {

	private static final Logger log = Logger
			.getLogger(LeaseExcelScheduleGenerator.class);

	private static final String SCHEDULE_TEMPLATE_NAME = "RENT_SCHEDULE_TEMPLATE.xls";

	@Override
	public void generateReport(final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			generateExcelSchedule(request, response);
		} catch (IOException e) {
			log.error("Error generating lease excel schedule.", e);
		}
	}

	private void generateExcelSchedule(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		long leaseId = Long.valueOf(request.getParameter(LEASE_ID));
		Lease lease = dao.get(leaseId);
		if (lease != null) {
			generateExcelSchedule(lease, response);
		}
	}

	private void generateExcelSchedule(final Lease lease,
			final HttpServletResponse response) throws IOException {
		String filename = UUID.randomUUID().toString() + ".xls";
		OutputStream stream = createExcelResponseHeader(filename, response);
		byte[] excelSchedule = generateExcelSchedule(lease);
		if (excelSchedule != null) {
			response.setContentLength((int) excelSchedule.length);
			stream.write(excelSchedule);
		}
		stream.close();
	}

	private byte[] generateExcelSchedule(final Lease lease) throws IOException {
		Workbook book = new HSSFWorkbook(getClass().getResourceAsStream(
				"/excel/" + SCHEDULE_TEMPLATE_NAME));
		Sheet sheet = book.getSheet("Schedule");
		try {
			if (sheet != null) {
				generateExcelSchedule(lease, book, sheet);
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

	private void generateExcelSchedule(final Lease lease, final Workbook book,
			final Sheet sheet) {
		sheet.getRow(SCHEDULE_TITLE_1_INDEX.first())
				.getCell(SCHEDULE_TITLE_1_INDEX.second())
				.setCellValue(resolveTitleLineOne(lease));
		sheet.getRow(SCHEDULE_TITLE_2_INDEX.first())
				.getCell(SCHEDULE_TITLE_2_INDEX.second())
				.setCellValue(resolveTitleLineTwo(lease));
		sheet.getRow(SCHEDULE_ADDRESS_INDEX.first())
				.getCell(SCHEDULE_ADDRESS_INDEX.second())
				.setCellValue(resolveAddress(lease));
		sheet.getRow(SCHEDULE_DEVELOPMENT_INDEX.first())
				.getCell(SCHEDULE_DEVELOPMENT_INDEX.second())
				.setCellValue(lease.getCategory().getCategory());
		sheet.getRow(SCHEDULE_UNIT_INDEX.first())
				.getCell(SCHEDULE_UNIT_INDEX.second())
				.setCellValue(lease.getLeasedUnits());
		sheet.getRow(SCHEDULE_LESSEE_INDEX.first())
				.getCell(SCHEDULE_LESSEE_INDEX.second())
				.setCellValue(lease.getTenant().getName());
		sheet.getRow(SCHEDULE_YEARLY_RENT_INDEX.first())
				.getCell(SCHEDULE_YEARLY_RENT_INDEX.second())
				.setCellValue(lease.yearlyAmount(RENT));
		sheet.getRow(SCHEDULE_YEARLY_RENT_GST_INDEX.first())
				.getCell(SCHEDULE_YEARLY_RENT_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(RENT));
		sheet.getRow(SCHEDULE_MONTHLY_RENT_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_RENT_INDEX.second())
				.setCellValue(lease.monthlyAmount(RENT));
		sheet.getRow(SCHEDULE_MONTHLY_RENT_GST_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_RENT_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(RENT));
		sheet.getRow(SCHEDULE_YEARLY_OUTGOINGS_INDEX.first())
				.getCell(SCHEDULE_YEARLY_OUTGOINGS_INDEX.second())
				.setCellValue(lease.yearlyAmount(OUTGOINGS));
		sheet.getRow(SCHEDULE_YEARLY_OUTGOINGS_GST_INDEX.first())
				.getCell(SCHEDULE_YEARLY_OUTGOINGS_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(OUTGOINGS));
		sheet.getRow(SCHEDULE_MONTHLY_OUTGOINGS_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_OUTGOINGS_INDEX.second())
				.setCellValue(lease.monthlyAmount(OUTGOINGS));
		sheet.getRow(SCHEDULE_MONTHLY_OUTGOINGS_GST_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_OUTGOINGS_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(OUTGOINGS));
		sheet.getRow(SCHEDULE_YEARLY_PARKING_INDEX.first())
				.getCell(SCHEDULE_YEARLY_PARKING_INDEX.second())
				.setCellValue(lease.yearlyAmount(PARKING));
		sheet.getRow(SCHEDULE_YEARLY_PARKING_GST_INDEX.first())
				.getCell(SCHEDULE_YEARLY_PARKING_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(PARKING));
		sheet.getRow(SCHEDULE_MONTHLY_PARKING_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_PARKING_INDEX.second())
				.setCellValue(lease.monthlyAmount(PARKING));
		sheet.getRow(SCHEDULE_MONTHLY_PARKING_GST_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_PARKING_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(PARKING));
		sheet.getRow(SCHEDULE_YEARLY_SIGNAGE_INDEX.first())
				.getCell(SCHEDULE_YEARLY_SIGNAGE_INDEX.second())
				.setCellValue(lease.yearlyAmount(SIGNAGE));
		sheet.getRow(SCHEDULE_YEARLY_SIGNAGE_GST_INDEX.first())
				.getCell(SCHEDULE_YEARLY_SIGNAGE_GST_INDEX.second())
				.setCellValue(lease.yearlyGst(SIGNAGE));
		sheet.getRow(SCHEDULE_MONTHLY_SIGNAGE_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_SIGNAGE_INDEX.second())
				.setCellValue(lease.monthlyAmount(SIGNAGE));
		sheet.getRow(SCHEDULE_MONTHLY_SIGNAGE_GST_INDEX.first())
				.getCell(SCHEDULE_MONTHLY_SIGNAGE_GST_INDEX.second())
				.setCellValue(lease.monthlyGst(SIGNAGE));
		sheet.getRow(SCHEDULE_LEASE_START_INDEX.first())
				.getCell(SCHEDULE_LEASE_START_INDEX.second())
				.setCellValue(format(lease.getLeaseStart()));
		sheet.getRow(SCHEDULE_LEASE_END_INDEX.first())
				.getCell(SCHEDULE_LEASE_END_INDEX.second())
				.setCellValue(format(lease.getLeaseEnd()));
		sheet.getRow(SCHEDULE_RENT_FREE_PERIOD_INDEX.first())
				.getCell(SCHEDULE_RENT_FREE_PERIOD_INDEX.second())
				.setCellValue("TODO");
		sheet.getRow(SCHEDULE_RENT_INCREASE_INDEX.first())
				.getCell(SCHEDULE_RENT_INCREASE_INDEX.second())
				.setCellValue(resolveRentPercentage(lease));
		sheet.getRow(SCHEDULE_LEASE_OPTION_START_INDEX.first())
				.getCell(SCHEDULE_LEASE_OPTION_START_INDEX.second())
				.setCellValue(resolveOptionStart(lease));
		sheet.getRow(SCHEDULE_LEASE_OPTION_END_INDEX.first())
				.getCell(SCHEDULE_LEASE_OPTION_END_INDEX.second())
				.setCellValue(resolveOptionEnd(lease));
		sheet.getRow(SCHEDULE_OUTGOINGS_PRECENT_INDEX.first())
				.getCell(SCHEDULE_OUTGOINGS_PRECENT_INDEX.second())
				.setCellValue("100%");
		double bondAmount = (lease.getBond() != null) ? lease.getBond()
				.getAmount() : ZERO_DBL;
		sheet.getRow(SCHEDULE_BOND_BANK_GUARANTEE_INDEX.first())
				.getCell(SCHEDULE_BOND_BANK_GUARANTEE_INDEX.second())
				.setCellValue(bondAmount);
		String area = (lease.getLeasedArea() == null) ? EMPTY_STRING : lease
				.getLeasedArea() + "sqm";
		sheet.getRow(SCHEDULE_AREA_INDEX.first())
				.getCell(SCHEDULE_AREA_INDEX.second()).setCellValue(area);

		resolveMetaData(lease, sheet, HISTORICAL_RENT,
				HISTORIC_RENTS_START_INDEX);
		resolveMetaData(lease, sheet, NOTICE, NOTICES_START_INDEX);
		resolveMetaData(lease, sheet, SPECIAL_CONDITION,
				SPECIAL_CONDITIONS_START_INDEX);
		resolveMetaData(lease, sheet, OPTION, OPTIONS_START_INDEX);

		HSSFFormulaEvaluator.evaluateAllFormulaCells(book);
	}

	private void resolveMetaData(final Lease lease, final Sheet sheet,
			final String type, final GwtPair<Integer> metaDataIndex) {
		List<LeaseMetaData> metaData = lease.getMetaData(type);
		if (!isEmpty(metaData)) {
			sort(metaData);
			int row = metaDataIndex.first();
			int columnTypeIndex = metaDataIndex.second();
			int columnValueIndex = columnTypeIndex + ONE;
			for (LeaseMetaData md : metaData) {
				sheet.getRow(row).getCell(columnTypeIndex)
						.setCellValue(md.getDescription());
				sheet.getRow(row).getCell(columnValueIndex)
						.setCellValue(md.getValue());
				row++;
			}
		}
	}

	private String resolveTitleLineOne(final Lease lease) {
		return lease.getCategory().getCategory() + " - RENT SCHEDULE";
	}

	private String resolveTitleLineTwo(final Lease lease) {
		return "UNIT " + lease.getLeasedUnits() + " - "
				+ lease.getTenant().getName() + " @ "
				+ format(lease.getLastUpdate());
	}

	private String resolveAddress(final Lease lease) {
		return ("UNIT " + lease.getLeasedUnits() + "/"
				+ lease.getPremises().getAddressLineOne() + ", " + lease
				.getPremises().getAddressLineTwo()).toUpperCase();
	}

	private String resolveOptionStart(final Lease lease) {
		return lease.getOptionStartDate() == null ? "None" : format(lease
				.getOptionStartDate());
	}

	private String resolveOptionEnd(final Lease lease) {
		return lease.getOptionEndDate() == null ? "None" : format(lease
				.getOptionEndDate());
	}

	private double resolveRentPercentage(final Lease lease) {
		LeaseIncidental incidental = lease.getIncidental(RENT);
		if (incidental != null) {
			return incidental.getPercentage();
		}
		return ZERO_DBL;
	}
}