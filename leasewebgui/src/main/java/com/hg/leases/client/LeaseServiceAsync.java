package com.hg.leases.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseCategory;
import com.hg.leases.shared.GwtLeaseFilter;
import com.hg.leases.shared.GwtLeasePremises;
import com.hg.leases.shared.GwtLeaseTenant;

public interface LeaseServiceAsync {

	void getLeases(GwtLeaseFilter filter, AsyncCallback<Set<GwtLease>> leases);

	void getLeasesToAction(AsyncCallback<Set<GwtLease>> leases);

	void saveLeases(Set<GwtLease> leases, AsyncCallback<Void> result);

	void saveNewLease(GwtLease lease, AsyncCallback<Long> result);

	void deleteLease(Long leaseId, AsyncCallback<Void> result);

	void sendEmail(AsyncCallback<Void> result);

	void getLeaseCategories(AsyncCallback<Set<GwtLeaseCategory>> categories);

	void getLeasePremises(AsyncCallback<Set<GwtLeasePremises>> premises);

	void getLeaseTenants(AsyncCallback<Set<GwtLeaseTenant>> tenants);
}