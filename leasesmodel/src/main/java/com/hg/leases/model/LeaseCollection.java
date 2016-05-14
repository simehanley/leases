package com.hg.leases.model;

import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.model.utilities.LeaseNumericUtilities.round;

import java.util.ArrayList;
import java.util.Collection;

public class LeaseCollection extends ArrayList<Lease> {

	private static final long serialVersionUID = -1571872745798398302L;

	public LeaseCollection() {
		super();
	}

	public LeaseCollection(Collection<? extends Lease> c) {
		super(c);
	}

	public double monthlyAmount(final String type) {
		double monthlyAmount = ZERO_DBL;
		for (Lease lease : this) {
			monthlyAmount += lease.monthlyAmount(type);
		}
		return monthlyAmount;
	}

	public double monthlyGst(final String type) {
		double monthlyGst = ZERO_DBL;
		for (Lease lease : this) {
			monthlyGst += lease.monthlyGst(type);
		}
		return monthlyGst;
	}

	public double monthlyTotal(final String type) {
		return round(monthlyAmount(type) + monthlyGst(type));
	}

	public double yearlyAmount(final String type) {
		double yearlyAmount = ZERO_DBL;
		for (Lease lease : this) {
			yearlyAmount += lease.yearlyAmount(type);
		}
		return yearlyAmount;
	}

	public double yearlyGst(final String type) {
		double yearlyGst = ZERO_DBL;
		for (Lease lease : this) {
			yearlyGst += lease.yearlyGst(type);
		}
		return yearlyGst;
	}

	public double yearlyTotal(final String type) {
		return round(yearlyAmount(type) + yearlyGst(type));
	}
}