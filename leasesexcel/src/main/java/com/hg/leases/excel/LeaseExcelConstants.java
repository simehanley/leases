package com.hg.leases.excel;

/**
 * @author hanleys
 */
public interface LeaseExcelConstants {

	int UNIT_INDEX = 0;
	int AREA_INDEX = 1;
	int ADDRESS_INDEX = 2;
	int TENANT_INDEX = 3;
	int LEASE_START_INDEX = 4;
	int LEASE_END_INDEX = 5;
	int LEASE_UPDATE_INDEX = 6;
	int LEASE_RENT_INDEX = 7;
	int LEASE_OUTGOINGS_INDEX = 13;
	int LEASE_PARKING_INDEX = 19;
	int LEASE_SIGNAGE_INDEX = 25;

	int CATEGORY_INDEX = 37;

	String TOTAL = "Total";

	/** invalid tenant names **/
	String VACANT = "VACANT";
	String SOLD = "SOLD";
	String NOT_TO_BE_LEASED = "NOT TO BE LEASED";
}
