package com.hg.leases.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtLeaseFilter implements IsSerializable {

	private String leaseTenant;

	private String leasePremises;

	private String leaseCategory;

	private boolean showInactive;

	public GwtLeaseFilter() {
	}

	public GwtLeaseFilter(String leaseTenant, String leasePremises,
			String leaseCategory, boolean showInactive) {
		this.leaseTenant = leaseTenant;
		this.leasePremises = leasePremises;
		this.leaseCategory = leaseCategory;
		this.showInactive = showInactive;
	}

	public String getLeaseTenant() {
		return leaseTenant;
	}

	public void setLeaseTenant(String leaseTenant) {
		this.leaseTenant = leaseTenant;
	}

	public String getLeasePremises() {
		return leasePremises;
	}

	public void setLeasePremises(String leasePremises) {
		this.leasePremises = leasePremises;
	}

	public String getLeaseCategory() {
		return leaseCategory;
	}

	public void setLeaseCategory(String leaseCategory) {
		this.leaseCategory = leaseCategory;
	}

	public boolean isShowInactive() {
		return showInactive;
	}

	public void setShowInactive(boolean showInactive) {
		this.showInactive = showInactive;
	}
}