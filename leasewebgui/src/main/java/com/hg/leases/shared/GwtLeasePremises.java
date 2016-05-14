package com.hg.leases.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtLeasePremises implements IsSerializable {

	private String address;

	public GwtLeasePremises() {
	}

	public GwtLeasePremises(final String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
