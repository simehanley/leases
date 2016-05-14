package com.hg.leases.dao;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.hg.leases.model.LeaseCategory;

public class LeaseCategoryDao extends BaseDao<LeaseCategory> {

	@Override
	@Transactional
	public void save(final LeaseCategory category) {
		super.save(category);
	}

	@Transactional
	public LeaseCategory get(final long id) {
		return super.get(id, LeaseCategory.class);
	}

	@Transactional
	public Set<LeaseCategory> get() {
		return super.get(LeaseCategory.class, EMPTY_STRING, "category");
	}
}