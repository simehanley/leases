package com.hg.leases.server.dto;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import com.hg.leases.model.LeaseMetaData;
import com.hg.leases.shared.GwtLeaseMetaData;

public class GwtLeaseMetaDataDTO {

	public List<GwtLeaseMetaData> fromLeaseMetaData(
			final List<LeaseMetaData> metaData) {
		List<GwtLeaseMetaData> gwtLeaseMetaData = new ArrayList<GwtLeaseMetaData>();
		if (!isEmpty(metaData)) {
			for (LeaseMetaData md : metaData) {
				GwtLeaseMetaData gwtMetaData = fromLeaseMetaData(md);
				if (gwtMetaData != null) {
					gwtLeaseMetaData.add(gwtMetaData);
				}
			}
		}
		return gwtLeaseMetaData;
	}

	public GwtLeaseMetaData fromLeaseMetaData(final LeaseMetaData metaData) {
		return new GwtLeaseMetaData(metaData.getId(), metaData.getType(),
				metaData.getDescription(), metaData.getValue(),
				metaData.getOrder());
	}
}
