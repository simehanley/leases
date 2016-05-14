package com.hg.leases.server.dto;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.server.LeaseServerConstants.GWT_LEASE_META_DATA_DTO;
import static com.hg.leases.shared.GwtLeaseBondType.valueOf;
import static com.hg.leases.shared.GwtLeaseType.existing;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseBond;
import com.hg.leases.model.LeaseIncidental;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseBondType;

public class GwtLeaseDTO {

	public Set<GwtLease> fromLease(final Set<Lease> leases) {
		Set<GwtLease> gwtLeases = new LinkedHashSet<GwtLease>();
		if (!isEmpty(leases)) {
			for (Lease lease : leases) {
				GwtLease gwtLease = fromLease(lease);
				if (gwtLease != null) {
					gwtLeases.add(gwtLease);
				}
			}
		}
		return gwtLeases;
	}

	public Set<Lease> toLease(final Set<GwtLease> gwtLeases) {
		Set<Lease> leases = new LinkedHashSet<Lease>();
		if (!isEmpty(gwtLeases)) {
			for (GwtLease gwtLease : gwtLeases) {
				leases.add(toLease(gwtLease));
			}
		}
		return leases;
	}

	public GwtLease fromLease(final Lease lease) {
		if (lease.getTenant() != null) {
			GwtLease gwtLease = new GwtLease();
			gwtLease.setId(lease.getId());
			gwtLease.setLeaseStart(lease.getLeaseStart().toDate());
			gwtLease.setLeaseEnd(lease.getLeaseEnd().toDate());
			gwtLease.setUpdateDate(lease.getLastUpdate().toDate());
			gwtLease.setTenant(lease.getTenant().getName());
			gwtLease.setPremises(lease.getPremises().getAddressLineOne());
			setIncidental(gwtLease, lease, RENT);
			setIncidental(gwtLease, lease, OUTGOINGS);
			setIncidental(gwtLease, lease, SIGNAGE);
			setIncidental(gwtLease, lease, PARKING);
			if (lease.getLeasedArea() != null) {
				gwtLease.setArea(Double.toString(lease.getLeasedArea()));
			} else {
				gwtLease.setArea(EMPTY_STRING);
			}
			if (lease.getLeasedUnits() != null) {
				gwtLease.setUnits(lease.getLeasedUnits());
			} else {
				gwtLease.setArea(EMPTY_STRING);
			}
			gwtLease.setCategory(lease.getCategory().getCategory());
			gwtLease.setNotes(lease.getNotes());
			gwtLease.setResidential(lease.isResidential());
			gwtLease.setInactive(lease.isInactive());
			gwtLease.setHasOption(lease.hasOption());
			if (lease.getOptionStartDate() != null) {
				gwtLease.setOptionStart(lease.getOptionStartDate().toDate());
			}
			if (lease.getOptionEndDate() != null) {
				gwtLease.setOptionEnd(lease.getOptionEndDate().toDate());
			}
			gwtLease.setType(existing);
			if (lease.getBond() != null) {
				LeaseBond bond = lease.getBond();
				GwtLeaseBondType type = valueOf(bond.getType().toString());
				gwtLease.setBondType(type);
				gwtLease.setBondAmount(bond.getAmount());
				gwtLease.setBondNotes(bond.getNotes());
			} else {
				gwtLease.setBondAmount(ZERO_DBL);
				gwtLease.setBondNotes(EMPTY_STRING);
			}
			if (!isEmpty(lease.getMetaData())) {
				gwtLease.getMetaData().addAll(
						GWT_LEASE_META_DATA_DTO.fromLeaseMetaData(lease
								.getMetaData()));
			}
			gwtLease.setJobNo(lease.getJobNo());
			return gwtLease;
		}
		return null;
	}

	public Lease toLease(final GwtLease gwtLease) {
		throw new UnsupportedOperationException("Not implemented");
	}

	private void setIncidental(final GwtLease gwtLease, final Lease lease,
			String type) {
		LeaseIncidental incidental = lease.getIncidental(type);
		if (incidental != null) {
			gwtLease.setIncidentalAmount(type, incidental.getAmount());
			gwtLease.setIncidentalPerecnt(type, incidental.getPercentage());
			gwtLease.setIncidentalAccount(type, incidental.getAccount());
		}
	}
}