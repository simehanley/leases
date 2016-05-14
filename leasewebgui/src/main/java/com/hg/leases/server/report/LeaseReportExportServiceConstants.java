package com.hg.leases.server.report;

import com.hg.leases.shared.GwtPair;

public interface LeaseReportExportServiceConstants {

	int LEASE_EXCEL_REPORT_HEADER_ROW = 0;

	int LEASE_EXCEL_REPORT_TENANT_INDEX = 0;
	int LEASE_EXCEL_REPORT_UNIT_INDEX = 1;
	int LEASE_EXCEL_REPORT_AREA_INDEX = 2;
	int LEASE_EXCEL_REPORT_START_INDEX = 3;
	int LEASE_EXCEL_REPORT_END_INDEX = 4;
	int LEASE_EXCEL_REPORT_UPDATE_INDEX = 5;
	int LEASE_EXCEL_REPORT_ANNUAL_RENT_INDEX = 6;
	int LEASE_EXCEL_REPORT_ANNUAL_RENT_GST_INDEX = 7;
	int LEASE_EXCEL_REPORT_ANNUAL_GROSS_RENT_INDEX = 8;
	int LEASE_EXCEL_REPORT_MONTHLY_RENT_INDEX = 9;
	int LEASE_EXCEL_REPORT_MONTHLY_RENT_GST_INDEX = 10;
	int LEASE_EXCEL_REPORT_MONTHLY_GROSS_RENT_INDEX = 11;
	int LEASE_EXCEL_REPORT_ANNUAL_OUTGOINGS_INDEX = 12;
	int LEASE_EXCEL_REPORT_ANNUAL_OUTGOINGS_GST_INDEX = 13;
	int LEASE_EXCEL_REPORT_ANNUAL_GROSS_OUTGOINGS_INDEX = 14;
	int LEASE_EXCEL_REPORT_MONTHLY_OUTGOINGS_INDEX = 15;
	int LEASE_EXCEL_REPORT_MONTHLY_OUTGOINGS_GST_INDEX = 16;
	int LEASE_EXCEL_REPORT_MONTHLY_GROSS_OUTGOINGS_INDEX = 17;
	int LEASE_EXCEL_REPORT_ANNUAL_PARKING_INDEX = 18;
	int LEASE_EXCEL_REPORT_ANNUAL_PARKING_GST_INDEX = 19;
	int LEASE_EXCEL_REPORT_ANNUAL_GROSS_PARKING_INDEX = 20;
	int LEASE_EXCEL_REPORT_MONTHLY_PARKING_INDEX = 21;
	int LEASE_EXCEL_REPORT_MONTHLY_PARKING_GST_INDEX = 22;
	int LEASE_EXCEL_REPORT_MONTHLY_GROSS_PARKING_INDEX = 23;
	int LEASE_EXCEL_REPORT_ANNUAL_SIGNAGE_INDEX = 24;
	int LEASE_EXCEL_REPORT_ANNUAL_SIGNAGE_GST_INDEX = 25;
	int LEASE_EXCEL_REPORT_ANNUAL_GROSS_SIGNAGE_INDEX = 26;
	int LEASE_EXCEL_REPORT_MONTHLY_SIGNAGE_INDEX = 27;
	int LEASE_EXCEL_REPORT_MONTHLY_SIGNAGE_GST_INDEX = 28;
	int LEASE_EXCEL_REPORT_MONTHLY_GROSS_SIGNAGE_INDEX = 29;
	int LEASE_EXCEL_REPORT_ANNUAL_TOTAL_INDEX = 30;
	int LEASE_EXCEL_REPORT_ANNUAL_TOTAL_GST_INDEX = 31;
	int LEASE_EXCEL_REPORT_ANNUAL_GROSS_TOTAL_INDEX = 32;
	int LEASE_EXCEL_REPORT_MONTHLY_TOTAL_INDEX = 33;
	int LEASE_EXCEL_REPORT_MONTHLY_TOTAL_GST_INDEX = 34;
	int LEASE_EXCEL_REPORT_MONTHLY_GROSS_TOTAL_INDEX = 35;

	int LEASE_EXCEL_REPORT_TENANT_WIDTH = 40 * 256;
	int LEASE_EXCEL_REPORT_UNIT_WIDTH = 11 * 256;
	int LEASE_EXCEL_REPORT_AREA_WIDTH = 6 * 256;
	int LEASE_EXCEL_REPORT_DATE_WIDTH = 11 * 256;
	int LEASE_EXCEL_REPORT_NUMBER_WIDTH = 13 * 256;

	int LEASE_EXCEL_REPORT_MAX_INDEX = LEASE_EXCEL_REPORT_MONTHLY_GROSS_TOTAL_INDEX;

	String EXCEL_GRAND_TOTAL = "Grand Total";

	/* lease invoice constants */
	GwtPair<Integer> CATEGORY_COMPANY_INDEX = new GwtPair<Integer>(0, 0);
	GwtPair<Integer> CATEGORY_ABN_INDEX = new GwtPair<Integer>(1, 0);
	GwtPair<Integer> CATEGORY_ADDRESS_INDEX = new GwtPair<Integer>(2, 0);
	GwtPair<Integer> CATEGORY_PHONE_INDEX = new GwtPair<Integer>(3, 0);
	GwtPair<Integer> CURRENT_DATE_INDEX = new GwtPair<Integer>(10, 4);

	GwtPair<Integer> TENANT_INDEX = new GwtPair<Integer>(14, 0);
	GwtPair<Integer> PREMISES_ADDRESS_1_INDEX = new GwtPair<Integer>(15, 0);
	GwtPair<Integer> PREMISES_ADDRESS_2_INDEX = new GwtPair<Integer>(16, 0);

	GwtPair<Integer> RENTAL_LINE_1_INDEX = new GwtPair<Integer>(20, 1);
	GwtPair<Integer> RENTAL_LINE_2_INDEX = new GwtPair<Integer>(21, 1);

	GwtPair<Integer> YEARLY_RENT_INDEX = new GwtPair<Integer>(26, 3);
	GwtPair<Integer> YEARLY_RENT_GST_INDEX = new GwtPair<Integer>(27, 3);
	GwtPair<Integer> MONTHLY_RENT_INDEX = new GwtPair<Integer>(26, 4);
	GwtPair<Integer> MONTHLY_RENT_GST_INDEX = new GwtPair<Integer>(27, 4);
	GwtPair<Integer> YEARLY_OUTGOINGS_INDEX = new GwtPair<Integer>(30, 3);
	GwtPair<Integer> YEARLY_OUTGOINGS_GST_INDEX = new GwtPair<Integer>(31, 3);
	GwtPair<Integer> MONTHLY_OUTGOINGS_INDEX = new GwtPair<Integer>(30, 4);
	GwtPair<Integer> MONTHLY_OUTGOINGS_GST_INDEX = new GwtPair<Integer>(31, 4);
	GwtPair<Integer> YEARLY_PARKING_INDEX = new GwtPair<Integer>(34, 3);
	GwtPair<Integer> YEARLY_PARKING_GST_INDEX = new GwtPair<Integer>(35, 3);
	GwtPair<Integer> MONTHLY_PARKING_INDEX = new GwtPair<Integer>(34, 4);
	GwtPair<Integer> MONTHLY_PARKING_GST_INDEX = new GwtPair<Integer>(35, 4);
	GwtPair<Integer> YEARLY_SIGNAGE_INDEX = new GwtPair<Integer>(38, 3);
	GwtPair<Integer> YEARLY_SIGNAGE_GST_INDEX = new GwtPair<Integer>(39, 3);
	GwtPair<Integer> MONTHLY_SIGNAGE_INDEX = new GwtPair<Integer>(38, 4);
	GwtPair<Integer> MONTHLY_SIGNAGE_GST_INDEX = new GwtPair<Integer>(39, 4);

	GwtPair<Integer> BANK_ACCOUNT_NAME_INDEX = new GwtPair<Integer>(51, 2);
	GwtPair<Integer> BANK_ACCOUNT_LOCATION_INDEX = new GwtPair<Integer>(52, 2);
	GwtPair<Integer> BANK_ACCOUNT_BSB_INDEX = new GwtPair<Integer>(53, 2);
	GwtPair<Integer> BANK_ACCOUNT_NUMBER_INDEX = new GwtPair<Integer>(54, 2);

	/* lease schedule constants */
	GwtPair<Integer> SCHEDULE_TITLE_1_INDEX = new GwtPair<Integer>(0, 0);
	GwtPair<Integer> SCHEDULE_TITLE_2_INDEX = new GwtPair<Integer>(1, 0);
	GwtPair<Integer> SCHEDULE_ADDRESS_INDEX = new GwtPair<Integer>(4, 0);

	GwtPair<Integer> SCHEDULE_DEVELOPMENT_INDEX = new GwtPair<Integer>(5, 1);
	GwtPair<Integer> SCHEDULE_UNIT_INDEX = new GwtPair<Integer>(6, 1);
	GwtPair<Integer> SCHEDULE_LESSEE_INDEX = new GwtPair<Integer>(7, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_RENT_INDEX = new GwtPair<Integer>(12, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_RENT_GST_INDEX = new GwtPair<Integer>(13, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_RENT_INDEX = new GwtPair<Integer>(16, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_RENT_GST_INDEX = new GwtPair<Integer>(17, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_OUTGOINGS_INDEX = new GwtPair<Integer>(21, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_OUTGOINGS_GST_INDEX = new GwtPair<Integer>(22, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_OUTGOINGS_INDEX = new GwtPair<Integer>(25, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_OUTGOINGS_GST_INDEX = new GwtPair<Integer>(26, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_PARKING_INDEX = new GwtPair<Integer>(30, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_PARKING_GST_INDEX = new GwtPair<Integer>(31, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_PARKING_INDEX = new GwtPair<Integer>(34, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_PARKING_GST_INDEX = new GwtPair<Integer>(35, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_SIGNAGE_INDEX = new GwtPair<Integer>(39, 1);
	GwtPair<Integer> SCHEDULE_YEARLY_SIGNAGE_GST_INDEX = new GwtPair<Integer>(40, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_SIGNAGE_INDEX = new GwtPair<Integer>(43, 1);
	GwtPair<Integer> SCHEDULE_MONTHLY_SIGNAGE_GST_INDEX = new GwtPair<Integer>(44, 1);
	GwtPair<Integer> SCHEDULE_LEASE_START_INDEX = new GwtPair<Integer>(5, 4);
	GwtPair<Integer> SCHEDULE_LEASE_END_INDEX = new GwtPair<Integer>(6, 4);
	GwtPair<Integer> SCHEDULE_RENT_FREE_PERIOD_INDEX = new GwtPair<Integer>(7, 4);
	GwtPair<Integer> SCHEDULE_RENT_INCREASE_INDEX = new GwtPair<Integer>(8, 4);
	GwtPair<Integer> SCHEDULE_LEASE_OPTION_START_INDEX = new GwtPair<Integer>(9, 4);
	GwtPair<Integer> SCHEDULE_LEASE_OPTION_END_INDEX = new GwtPair<Integer>(10, 4);
	GwtPair<Integer> SCHEDULE_OUTGOINGS_PRECENT_INDEX = new GwtPair<Integer>(11, 4);
	GwtPair<Integer> SCHEDULE_BOND_BANK_GUARANTEE_INDEX = new GwtPair<Integer>(12, 4);
	GwtPair<Integer> SCHEDULE_AREA_INDEX = new GwtPair<Integer>(13, 4);
	
	GwtPair<Integer> HISTORIC_RENTS_START_INDEX = new GwtPair<Integer>(15, 3);
	GwtPair<Integer> NOTICES_START_INDEX = new GwtPair<Integer>(29, 3);
	GwtPair<Integer> SPECIAL_CONDITIONS_START_INDEX = new GwtPair<Integer>(38, 3);
	GwtPair<Integer> OPTIONS_START_INDEX = new GwtPair<Integer>(47, 3);
}