package com.hg.leases.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtLeaseCategory implements IsSerializable {

	private String category;

	public GwtLeaseCategory() {
	}

	public GwtLeaseCategory(final String category) {
		this.category = category;
	}

	public final String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}
}