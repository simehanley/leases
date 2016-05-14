package com.hg.leases.dao;

import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.hg.leases.model.Lease;
import com.hg.leases.model.LeaseFilter;

public class LeaseDao extends BaseDao<Lease> {

	@Transactional
	public void save(final Set<Lease> leases) {
		if (!isEmpty(leases)) {
			for (Lease lease : leases) {
				merge(lease);
			}
		}
	}

	@Override
	@Transactional
	public void save(final Lease lease) {
		super.save(lease);
	}

	@Transactional
	public void delete(final long id) {
		Lease lease = get(id);
		if (lease != null) {
			lease.setTenant(null);
			lease.setPremises(null);
			lease.setCategory(null);
			lease.setIncidentals(null);
			lease.setBond(null);
			lease.setMetaData(null);
			super.save(lease);
			super.delete(lease);
		}
	}

	@Transactional
	public Lease get(final long id) {
		return super.get(id, Lease.class);
	}

	@Transactional
	public Set<Lease> get() {
		return super.get(Lease.class, "tenant", "name");
	}

	@Transactional
	public Set<Lease> get(final LeaseFilter filter) {
		if (filter == null || filter.isEmpty()) {
			return get();
		}
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Lease.class);
		createLikeCriteria("tenant", "name", filter.getLeaseTenant(), criteria,
				true);
		createLikeCriteria("premises", "addressLineOne",
				filter.getLeasePremises(), criteria, false);
		createLikeCriteria("category", "category", filter.getLeaseCategory(),
				criteria, false);
		if (!filter.showInactive()) {
			criteria.add(Restrictions.ne("inactive", true));
		}
		if (isEmpty(filter.getLeaseTenant())) {
			createOrderCriteria("tenant", "name", criteria);
		}
		return get(criteria);
	}

	@Transactional
	public Set<Lease> getLeasesToAction() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Lease.class);
		criteria.createCriteria("incidentals")
				.add(Restrictions.eq("name", RENT))
				.add(Restrictions.le("percentage", ZERO_DBL));
		createOrderCriteria("tenant", "name", criteria);
		return get(criteria);
	}
}