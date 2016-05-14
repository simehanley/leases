package com.hg.leases.server.report;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class LeaseReportExportServiceUtilities {

	public static CellStyle createDateCellStyle(final Workbook book) {
		DataFormat format = book.createDataFormat();
		CellStyle style = book.createCellStyle();
		style.setDataFormat(format.getFormat("dd/MM/yyyy"));
		return style;
	}

	public static CellStyle createNumericCellStyle(final Workbook book) {
		DataFormat format = book.createDataFormat();
		CellStyle style = book.createCellStyle();
		style.setDataFormat(format.getFormat("#,##0.00"));
		return style;
	}

	public static CellStyle createCurrencyCellStyle(final Workbook book,
			final boolean isBold) {
		DataFormat format = book.createDataFormat();
		CellStyle style = book.createCellStyle();
		style.setDataFormat(format.getFormat("$* #,##0.00"));
		if (isBold) {
			style.setFont(createBoldFont(book));
		}
		return style;
	}

	public static CellStyle createBoldCellStyle(final Workbook book) {
		CellStyle style = book.createCellStyle();
		style.setFont(createBoldFont(book));
		return style;
	}

	public static CellStyle createHeaderCellStyle(final Workbook book,
			final boolean isCentred) {
		CellStyle style = book.createCellStyle();
		style.setFont(createBoldFont(book));
		if (isCentred) {
			style.setAlignment(ALIGN_CENTER);
		}
		style.setFont(createBoldFont(book));
		style.setWrapText(true);
		return style;
	}

	public static CellStyle createFormattedCellStyle(final Workbook book,
			boolean isCentred, boolean isWrapped) {
		CellStyle style = book.createCellStyle();
		if (isCentred) {
			style.setAlignment(ALIGN_CENTER);
		}
		style.setWrapText(isWrapped);
		return style;
	}

	private static Font createBoldFont(final Workbook book) {
		Font font = book.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		return font;
	}
}