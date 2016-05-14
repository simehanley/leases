package com.hg.leases.server;

import com.hg.leases.server.dto.GwtLeaseCategoryDTO;
import com.hg.leases.server.dto.GwtLeaseDTO;
import com.hg.leases.server.dto.GwtLeaseMetaDataDTO;
import com.hg.leases.server.dto.GwtLeasePremisesDTO;
import com.hg.leases.server.dto.GwtLeaseTenantDTO;

/**
 * @author hanleys
 */
public interface LeaseServerConstants {

	GwtLeaseDTO GWT_LEASE_DTO = new GwtLeaseDTO();
	GwtLeaseCategoryDTO GWT_LEASE_CATEGORY_DTO = new GwtLeaseCategoryDTO();
	GwtLeasePremisesDTO GWT_LEASE_PREMISES_DTO = new GwtLeasePremisesDTO();
	GwtLeaseTenantDTO GWT_LEASE_TENANT_DTO = new GwtLeaseTenantDTO();
	GwtLeaseMetaDataDTO GWT_LEASE_META_DATA_DTO = new GwtLeaseMetaDataDTO();
}