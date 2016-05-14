package com.hg.leases.server.dto;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.hg.leases.model.LeaseTenant;
import com.hg.leases.shared.GwtLeaseTenant;

public class GwtLeaseTenantDTO {

	public Set<GwtLeaseTenant> fromLeaseTenants(final Set<LeaseTenant> tenants) {
		Set<GwtLeaseTenant> gwtLeaseTenants = new LinkedHashSet<GwtLeaseTenant>();
		if (!isEmpty(tenants)) {
			for (LeaseTenant tenant : tenants) {
				GwtLeaseTenant gwtLeaseTenant = fromLeaseTenant(tenant);
				if (gwtLeaseTenant != null) {
					gwtLeaseTenants.add(gwtLeaseTenant);
				}
			}
		}
		return gwtLeaseTenants;
	}

	public GwtLeaseTenant fromLeaseTenant(final LeaseTenant tenant) {
		return new GwtLeaseTenant(tenant.getName());
	}
}
