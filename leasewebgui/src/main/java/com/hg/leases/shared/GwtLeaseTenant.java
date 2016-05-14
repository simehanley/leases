package com.hg.leases.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtLeaseTenant implements IsSerializable {

	private String name;

	public GwtLeaseTenant() {
	}

	public GwtLeaseTenant(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}