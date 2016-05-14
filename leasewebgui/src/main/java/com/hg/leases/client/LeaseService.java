package com.hg.leases.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseCategory;
import com.hg.leases.shared.GwtLeaseFilter;
import com.hg.leases.shared.GwtLeasePremises;
import com.hg.leases.shared.GwtLeaseTenant;

@RemoteServiceRelativePath("leases")
public interface LeaseService extends RemoteService {

	Set<GwtLease> getLeases(GwtLeaseFilter filter);

	Set<GwtLease> getLeasesToAction();

	void saveLeases(Set<GwtLease> leases);

	Long saveNewLease(GwtLease lease);

	void deleteLease(Long id);

	void sendEmail();

	Set<GwtLeaseCategory> getLeaseCategories();

	Set<GwtLeasePremises> getLeasePremises();

	Set<GwtLeaseTenant> getLeaseTenants();
}