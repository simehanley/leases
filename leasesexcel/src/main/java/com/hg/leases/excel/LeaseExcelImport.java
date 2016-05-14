package com.hg.leases.excel;

import static com.hg.leases.excel.LeaseExcelConstants.ADDRESS_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.AREA_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.CATEGORY_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_END_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_OUTGOINGS_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_PARKING_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_RENT_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_SIGNAGE_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_START_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.LEASE_UPDATE_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.NOT_TO_BE_LEASED;
import static com.hg.leases.excel.LeaseExcelConstants.SOLD;
import static com.hg.leases.excel.LeaseExcelConstants.TENANT_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.TOTAL;
import static com.hg.leases.excel.LeaseExcelConstants.UNIT_INDEX;
import static com.hg.leases.excel.LeaseExcelConstants.VACANT;
import static com.hg.leases.excel.LeasePoiCellUtilities.getDateValue;
import static com.hg.leases.excel.LeasePoiCellUtilities.getSafeStringValue;
import static com.hg.leases.excel.LeasePoiCellUtilities.getStringValue;
import static com.hg.leases.excel.LeasePoiCellUtilities.isBlank;
import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.ERROR;
import static com.hg.leases.model.LeaseConstants.ONE;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.TWO;
import static com.hg.leases.model.LeaseConstants.ZERO;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hg.leases.dao.LeaseCategoryDao;
import com.hg.leases.dao.LeaseDao;
import com.hg.leases.dao.LeasePremisesDao;
import com.hg.leases.dao.LeaseTenentDao;
import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseCategory;
import com.hg.leases.model.LeaseIncidental;
import com.hg.leases.model.LeasePremises;
import com.hg.leases.model.LeaseTenant;

/**
 * Simple class to import the contents of the current 'Rental List' spreadsheet
 * into the database
 *
 * @author hanleys
 */
public class LeaseExcelImport {

	private static final Logger log = LoggerFactory
			.getLogger(LeaseExcelImport.class);

	private static final double ONE_CENT = 0.01;

	private final String filepath;

	private final String tab;

	private final Map<String, LeasePremises> premisesCache = new HashMap<String, LeasePremises>();
	private final Map<String, LeaseTenant> tenantsCache = new HashMap<String, LeaseTenant>();
	private final Map<String, LeaseCategory> categoryCache = new HashMap<String, LeaseCategory>();

	@Autowired
	private LeasePremisesDao leasePremisesDao;

	@Autowired
	private LeaseTenentDao leaseTenentDao;

	@Autowired
	private LeaseCategoryDao leaseCategoryDao;

	@Autowired
	private LeaseDao leaseDao;

	public LeaseExcelImport(final String filepath, final String tab) {
		this.filepath = filepath;
		this.tab = tab;
	}

	@PostConstruct
	public void importData() {
		log.info("Attempting to import data from excel.");
		persistData();
		log.info("Successfully imported data from excel.");
	}

	private void persistData() {
		XSSFWorkbook workbook = null;
		try {
			workbook = loadLeaseWorkbook();
			XSSFSheet sheet = workbook.getSheet(tab);
			if (sheet != null) {
				persistData(sheet);
			}
		} catch (Throwable t) {
			log.error("Error loading lease data into the database.", t);
			System.exit(ERROR);
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					log.error("Error closing lease work book.", e);
				}
			}
		}
	}

	private void persistData(final XSSFSheet sheet) {
		persistPremises(sheet);
		persistTenants(sheet);
		persistCategories(sheet);
		persistLeaseData(sheet);
	}

	private void persistPremises(final XSSFSheet sheet) {
		Iterator<Row> rows = sheet.rowIterator();
		int count = ZERO;
		while (rows.hasNext()) {
			if (count != ZERO) {
				cachePremises(rows.next());
			} else {
				rows.next();
			}
			count++;
		}
		for (String key : premisesCache.keySet()) {
			leasePremisesDao.save(premisesCache.get(key));
		}
	}

	private void persistTenants(final XSSFSheet sheet) {
		Iterator<Row> rows = sheet.rowIterator();
		int count = ZERO;
		while (rows.hasNext()) {
			if (count != ZERO) {
				cacheTenants(rows.next());
			} else {
				rows.next();
			}
			count++;
		}
		for (String key : tenantsCache.keySet()) {
			leaseTenentDao.save(tenantsCache.get(key));
		}
	}

	private void persistCategories(final XSSFSheet sheet) {
		Iterator<Row> rows = sheet.rowIterator();
		int count = ZERO;
		while (rows.hasNext()) {
			if (count != ZERO) {
				cacheCategories(rows.next());
			} else {
				rows.next();
			}
			count++;
		}
		for (String key : categoryCache.keySet()) {
			leaseCategoryDao.save(categoryCache.get(key));
		}
	}

	private void persistLeaseData(final XSSFSheet sheet) {
		Iterator<Row> rows = sheet.rowIterator();
		int count = ZERO;
		while (rows.hasNext()) {
			if (count != ZERO) {
				persistLease(rows.next());
			} else {
				rows.next();
			}
			count++;
		}
	}

	private void cachePremises(final Row row) {
		if (!isBlank(row.getCell(UNIT_INDEX))) {
			String address = getStringValue(row.getCell(ADDRESS_INDEX));
			LeasePremises premises = (premisesCache.containsKey(address)) ? premisesCache
					.get(address) : new LeasePremises(address, EMPTY_STRING);
			premisesCache.put(premises.getAddressLineOne(), premises);
		}
	}

	private void cacheTenants(final Row row) {
		if (!isBlank(row.getCell(UNIT_INDEX))) {
			String tenantName = getStringValue(row.getCell(TENANT_INDEX));
			if (!isInvalidTenant(tenantName)) {
				LeaseTenant tenant = (tenantsCache.containsKey(tenantName)) ? tenantsCache
						.get(tenantName) : new LeaseTenant(tenantName);
				tenantsCache.put(tenantName, tenant);
			}
		}
	}

	private void cacheCategories(final Row row) {
		if (!isBlank(row.getCell(UNIT_INDEX))) {
			String categoryName = getStringValue(row.getCell(CATEGORY_INDEX));
			if (!categoryName.contains(TOTAL)) {
				LeaseCategory category = (categoryCache
						.containsKey(categoryName)) ? categoryCache
						.get(categoryName) : new LeaseCategory(categoryName);
				categoryCache.put(categoryName, category);
			}
		}
	}

	private void persistLease(final Row row) {
		if (!isBlank(row.getCell(UNIT_INDEX))) {
			LeaseTenant tenant = getLeaseTenant(getStringValue(row
					.getCell(TENANT_INDEX)));
			if (tenant != null) {
				LocalDate leaseStart = getDateValue(row
						.getCell(LEASE_START_INDEX));
				LocalDate leaseEnd = getDateValue(row.getCell(LEASE_END_INDEX));
				LocalDate leaseUpdate = getDateValue(row
						.getCell(LEASE_UPDATE_INDEX));
				LeaseCategory category = getLeaseCategory(getStringValue(row
						.getCell(CATEGORY_INDEX)));
				LeasePremises premises = getLeasePremises(getStringValue(row
						.getCell(ADDRESS_INDEX)));
				Double area = resolveLeaseArea(getSafeStringValue(
						row.getCell(AREA_INDEX), ONE));
				String unit = getSafeStringValue(row.getCell(UNIT_INDEX), ZERO);
				Lease lease = new Lease(leaseStart, leaseEnd, leaseUpdate,
						EMPTY_STRING, area, unit,
						new ArrayList<LeaseIncidental>(), tenant, premises,
						category, null, false, false, EMPTY_STRING);
				addLeaseIncidentals(lease, row);
				leaseDao.save(lease);
			}
		}
	}

	private XSSFWorkbook loadLeaseWorkbook() {
		XSSFWorkbook workbook = null;
		try {
			FileInputStream file = new FileInputStream(new File(filepath));
			workbook = new XSSFWorkbook(file);
		} catch (Throwable t) {
			log.error("Unable to load lease file.", t);
			System.exit(ERROR);
		}
		return workbook;
	}

	private boolean isInvalidTenant(final String tenantName) {
		return SOLD.equals(tenantName) || VACANT.equals(tenantName)
				|| NOT_TO_BE_LEASED.equals(tenantName);
	}

	private LeaseCategory getLeaseCategory(final String categoryName) {
		return categoryCache.get(categoryName);
	}

	private LeasePremises getLeasePremises(final String address) {
		return premisesCache.get(address);
	}

	private LeaseTenant getLeaseTenant(final String tenantName) {
		return tenantsCache.get(tenantName);
	}

	private Double resolveLeaseArea(final String value) {
		if (isNumber(value)) {
			return Double.parseDouble(value);
		}
		return null;
	}

	private void addLeaseIncidentals(final Lease lease, final Row row) {
		addLeaseIncidental(lease, RENT,
				getSafeStringValue(row.getCell(LEASE_RENT_INDEX), TWO));
		addLeaseIncidental(lease, OUTGOINGS,
				getSafeStringValue(row.getCell(LEASE_OUTGOINGS_INDEX), TWO));
		addLeaseIncidental(lease, PARKING,
				getSafeStringValue(row.getCell(LEASE_PARKING_INDEX), TWO));
		addLeaseIncidental(lease, SIGNAGE,
				getSafeStringValue(row.getCell(LEASE_SIGNAGE_INDEX), TWO));
	}

	private void addLeaseIncidental(final Lease lease, final String type,
			final String value) {
		if (isNumber(value)) {
			double dblValue = Double.parseDouble(value);
			if (dblValue > ONE_CENT) {
				lease.addIncidental(new LeaseIncidental(type, dblValue,
						ZERO_DBL, EMPTY_STRING));
			}
		}
	}
}