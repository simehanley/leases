package com.hg.leases.excel;

import static com.hg.leases.excel.LeaseExcelUtilities.round;
import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.ZERO;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;

import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.LocalDate;

/**
 * @author hanleys
 */
public class LeasePoiCellUtilities {

	public static boolean isBlank(final Cell cell) {
		return cell.getCellType() == CELL_TYPE_BLANK;
	}

	public static LocalDate getDateValue(final Cell cell) {
		return new LocalDate(cell.getDateCellValue());
	}

	public static String getStringValue(final Cell cell) {
		return cell.getStringCellValue();
	}

	public static String getSafeStringValue(final Cell cell,
			final int decimalPlaces) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
		case Cell.CELL_TYPE_FORMULA:
			Double value = cell.getNumericCellValue();
			if (decimalPlaces == ZERO) {
				return Integer.toString(value.intValue());
			} else {
				return Double.toString(round(value, decimalPlaces));
			}
		case Cell.CELL_TYPE_BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_BLANK:
			return EMPTY_STRING;
		default:
			return cell.getStringCellValue();
		}
	}
}