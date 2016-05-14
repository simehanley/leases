package com.hg.leases.model;

import org.springframework.util.StringUtils;

/**
 * @author hanleys
 */
public class LeaseFilter {

	private final String leaseTenant;

	private final String leasePremises;

	private final String leaseCategory;

	private final boolean showInactive;

	public LeaseFilter(final String leaseTenant, final String leasePremises,
			final String leaseCategory, final boolean showInactive) {
		this.leaseTenant = leaseTenant;
		this.leasePremises = leasePremises;
		this.leaseCategory = leaseCategory;
		this.showInactive = showInactive;
	}

	public final String getLeaseTenant() {
		return leaseTenant;
	}

	public final String getLeasePremises() {
		return leasePremises;
	}

	public final String getLeaseCategory() {
		return leaseCategory;
	}

	public final boolean showInactive() {
		return showInactive;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(leaseTenant)
				&& StringUtils.isEmpty(leasePremises)
				&& StringUtils.isEmpty(leaseCategory) && showInactive;
	}
}