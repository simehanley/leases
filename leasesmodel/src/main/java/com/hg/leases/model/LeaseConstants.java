package com.hg.leases.model;

/**
 * @author hanleys
 */
public interface LeaseConstants {

	Long DEFAULT_ID = null;

	double GST_PERCENTAGE = 0.1;
	double ZERO_DBL = 0.;
	double ONE_HUNDRED_DBL = 100.;

	int ZERO = 0;
	int ERROR = -1;
	int ONE = 1;
	int TWO = 2;
	int MONTHS_PER_YEAR = 12;

	/** lease incidentals **/
	String RENT = "Rent";
	String OUTGOINGS = "Outgoings";
	String SIGNAGE = "Signage";
	String PARKING = "Parking";
	String TOTAL = "Total";

	/** lease meta data types **/
	String HISTORICAL_RENT = "Historical Rent";
	String NOTICE = "Notice";
	String SPECIAL_CONDITION = "Special Condition";
	String OPTION = "Option";

	String UNIT = "Unit";

	String EMPTY_STRING = "";
	String SPACE = " ";

	String ZERO_DBL_STRING = "0.00";
}