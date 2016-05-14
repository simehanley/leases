package com.hg.leases.server;

import static com.hg.leases.model.LeaseConstants.DEFAULT_ID;
import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.server.LeaseServerConstants.GWT_LEASE_CATEGORY_DTO;
import static com.hg.leases.server.LeaseServerConstants.GWT_LEASE_DTO;
import static com.hg.leases.server.LeaseServerConstants.GWT_LEASE_PREMISES_DTO;
import static com.hg.leases.server.LeaseServerConstants.GWT_LEASE_TENANT_DTO;
import static com.hg.leases.shared.GwtLeaseUtilities.areEqual;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.LocalDate;

import com.hg.leases.client.LeaseService;
import com.hg.leases.dao.LeaseCategoryDao;
import com.hg.leases.dao.LeaseDao;
import com.hg.leases.dao.LeasePremisesDao;
import com.hg.leases.dao.LeaseTenentDao;
import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseBond;
import com.hg.leases.model.LeaseCategory;
import com.hg.leases.model.LeaseEntity;
import com.hg.leases.model.LeaseFilter;
import com.hg.leases.model.LeaseIncidental;
import com.hg.leases.model.LeaseMetaData;
import com.hg.leases.model.LeasePremises;
import com.hg.leases.model.LeaseTenant;
import com.hg.leases.model.enums.LeaseBondType;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseCategory;
import com.hg.leases.shared.GwtLeaseException;
import com.hg.leases.shared.GwtLeaseFilter;
import com.hg.leases.shared.GwtLeaseMetaData;
import com.hg.leases.shared.GwtLeasePremises;
import com.hg.leases.shared.GwtLeaseTenant;

public class LeaseServiceImpl extends AbstractService implements LeaseService {

	private static final long serialVersionUID = 1617872572922463771L;

	private static final double ONE_CENT = 0.01;

	private ConcurrentMap<String, LeaseEntity> premisesCache = new ConcurrentHashMap<String, LeaseEntity>();
	private ConcurrentMap<String, LeaseEntity> tennantCache = new ConcurrentHashMap<String, LeaseEntity>();
	private ConcurrentMap<String, LeaseEntity> categoryCache = new ConcurrentHashMap<String, LeaseEntity>();

	@Override
	public Set<GwtLease> getLeases(final GwtLeaseFilter filter) {
		LeaseDao dao = getSpringBean(LeaseDao.class);
		Set<Lease> leases = dao.get(resolveFilter(filter));
		if (!isEmpty(leases)) {
			return GWT_LEASE_DTO.fromLease(leases);
		}
		return null;
	}

	@Override
	public Set<GwtLease> getLeasesToAction() {
		LeaseDao dao = getSpringBean(LeaseDao.class);
		Set<Lease> leases = dao.getLeasesToAction();
		if (!isEmpty(leases)) {
			return GWT_LEASE_DTO.fromLease(leases);
		}
		return null;
	}

	@Override
	public void saveLeases(final Set<GwtLease> leases) {
		LeaseDao dao = getSpringBean(LeaseDao.class);
		if (!isEmpty(leases)) {
			Set<Lease> converted = new HashSet<Lease>();
			for (GwtLease lease : leases) {
				converted.add(convertExistingLease(lease));
			}
			dao.save(converted);
		}
	}

	@Override
	public Long saveNewLease(final GwtLease lease) {
		LeaseDao dao = getSpringBean(LeaseDao.class);
		if (lease != null) {
			Lease converted = convertNewLease(lease);
			dao.save(converted);
			tennantCache
					.put(converted.getTenant().key(), converted.getTenant());
			premisesCache.put(converted.getPremises().key(),
					converted.getPremises());
			categoryCache.put(converted.getCategory().key(),
					converted.getCategory());
			return converted.getId();
		}
		return DEFAULT_ID;
	}

	@Override
	public void deleteLease(final Long id) {
		if (id != null) {
			LeaseDao dao = getSpringBean(LeaseDao.class);
			dao.delete(id);
		}
	}

	private Lease convertExistingLease(final GwtLease lease) {
		Lease convertedLease = convertLease(lease);
		if (convertedLease == null) {
			throw new GwtLeaseException("Unable to convert Gwt lease.");
		}
		return convertedLease;
	}

	private Lease convertNewLease(final GwtLease gwtLease) {
		Lease convertedLease = new Lease();
		convertLease(gwtLease, convertedLease);
		return convertedLease;
	}

	@Override
	public void sendEmail() {
		LeaseMaintenanceService leaseMaintenanceService = getSpringBean(LeaseMaintenanceService.class);
		if (leaseMaintenanceService != null) {
			leaseMaintenanceService.run();
		}
	}

	@Override
	public Set<GwtLeaseCategory> getLeaseCategories() {
		LeaseCategoryDao dao = getSpringBean(LeaseCategoryDao.class);
		Set<LeaseCategory> categories = dao.get();
		if (!isEmpty(categories)) {
			updateCache(categoryCache, categories);
			return GWT_LEASE_CATEGORY_DTO.fromLeaseCategory(categories);
		}
		return null;
	}

	@Override
	public Set<GwtLeasePremises> getLeasePremises() {
		LeasePremisesDao dao = getSpringBean(LeasePremisesDao.class);
		Set<LeasePremises> premises = dao.get();
		if (!isEmpty(premises)) {
			updateCache(premisesCache, premises);
			return GWT_LEASE_PREMISES_DTO.fromLeasePremises(premises);
		}
		return null;
	}

	@Override
	public Set<GwtLeaseTenant> getLeaseTenants() {
		LeaseTenentDao dao = getSpringBean(LeaseTenentDao.class);
		Set<LeaseTenant> tenants = dao.get();
		if (!isEmpty(tenants)) {
			updateCache(tennantCache, tenants);
			return GWT_LEASE_TENANT_DTO.fromLeaseTenants(tenants);
		}
		return null;
	}

	private Lease convertLease(final GwtLease gwtLease) {
		LeaseDao dao = getSpringBean(LeaseDao.class);
		Lease lease = dao.get(gwtLease.getId());
		if (lease == null) {
			throw new GwtLeaseException(
					"Unable to resolve lease for lease id '" + gwtLease.getId()
							+ "'.");
		}
		convertLease(gwtLease, lease);
		return lease;
	}

	private void convertLease(final GwtLease gwtLease, final Lease lease) {
		lease.setLeaseStart(new LocalDate(gwtLease.getLeaseStart()));
		lease.setLeaseEnd(new LocalDate(gwtLease.getLeaseEnd()));
		lease.setLastUpdate(new LocalDate(gwtLease.getUpdateDate()));
		lease.setLeasedUnits(gwtLease.getUnits());
		if (!isEmpty(gwtLease.getArea())) {
			lease.setLeasedArea(Double.valueOf(gwtLease.getArea()));
		}
		lease.setNotes(gwtLease.getNotes());
		lease.setResidential(gwtLease.isResidential());
		lease.setInactive(gwtLease.isInactive());
		convertIncidental(gwtLease, lease, RENT);
		convertIncidental(gwtLease, lease, OUTGOINGS);
		convertIncidental(gwtLease, lease, PARKING);
		convertIncidental(gwtLease, lease, SIGNAGE);
		lease.setHasOption(gwtLease.hasOption());
		if (gwtLease.getOptionStart() != null) {
			lease.setOptionStartDate(new LocalDate(gwtLease.getOptionStart()));
		}
		if (gwtLease.getOptionEnd() != null) {
			lease.setOptionEndDate(new LocalDate(gwtLease.getOptionEnd()));
		}
		if (tennantCache.containsKey(gwtLease.getTenant())) {
			lease.setTenant((LeaseTenant) tennantCache.get(gwtLease.getTenant()));
		} else {
			lease.setTenant(new LeaseTenant(gwtLease.getTenant()));
		}
		if (premisesCache.containsKey(gwtLease.getPremises())) {
			lease.setPremises((LeasePremises) premisesCache.get(gwtLease
					.getPremises()));
		} else {
			lease.setPremises(new LeasePremises(gwtLease.getPremises(),
					EMPTY_STRING));
		}
		if (categoryCache.containsKey(gwtLease.getCategory())) {
			lease.setCategory((LeaseCategory) categoryCache.get(gwtLease
					.getCategory()));
		} else {
			lease.setCategory(new LeaseCategory(gwtLease.getCategory()));
		}
		if (areEqual(gwtLease.getBondAmount(), ZERO_DBL)) {
			lease.setBond(null);
		} else {
			if (gwtLease.getBondType() != null) {
				LeaseBond bond = (lease.getBond() == null) ? new LeaseBond()
						: lease.getBond();
				bond.setType(LeaseBondType.valueOf(gwtLease.getBondType()
						.toString()));
				bond.setAmount(gwtLease.getBondAmount());
				bond.setNotes(gwtLease.getBondNotes());
				lease.setBond(bond);
			}
		}
		copyMetaData(gwtLease, lease);
		if (hasLength(gwtLease.getJobNo())) {
			lease.setJobNo(gwtLease.getJobNo());
		} else {
			lease.setJobNo(null);
		}
	}

	private void copyMetaData(final GwtLease gwtLease, final Lease lease) {
		if (!isEmpty(gwtLease.getMetaData())) {
			if (!isEmpty(lease.getMetaData())) {
				lease.getMetaData().clear();
			}
			for (GwtLeaseMetaData md : gwtLease.getMetaData()) {
				lease.addMetaData(new LeaseMetaData(md.getId(), md.getType(),
						md.getDescription(), md.getValue(), md.getOrder()));
			}
		}
	}

	private void convertIncidental(final GwtLease gwtLease, final Lease lease,
			final String type) {
		double amount = gwtLease.getIncidentalAmount(type);
		double percentage = gwtLease.getIncidentalPercent(type);
		String account = gwtLease.getIncidentalAccount(type);
		String accountPassed = hasLength(account) ? account : null;
		convertIncidental(gwtLease, lease, type, amount, percentage,
				accountPassed);
	}

	private void convertIncidental(final GwtLease gwtLease, final Lease lease,
			final String type, final double amount, final double percentage,
			final String account) {
		if (amount < ONE_CENT) {
			lease.removeIncidental(type);
		} else {
			LeaseIncidental incidental = lease.getIncidental(type);
			if (incidental == null) {
				incidental = new LeaseIncidental(type, amount, percentage,
						account);
				lease.addIncidental(incidental);
			} else {
				incidental.setAmount(amount);
				incidental.setPercentage(percentage);
				incidental.setAccount(account);
			}
		}
	}

	private LeaseFilter resolveFilter(final GwtLeaseFilter filter) {
		return new LeaseFilter(filter.getLeaseTenant(),
				filter.getLeasePremises(), filter.getLeaseCategory(),
				filter.isShowInactive());
	}

	private <T extends LeaseEntity> void updateCache(
			final ConcurrentMap<String, LeaseEntity> cache,
			final Set<T> elements) {
		if (!isEmpty(elements)) {
			for (LeaseEntity element : elements) {
				cache.put(element.key(), element);
			}
		}
	}
}