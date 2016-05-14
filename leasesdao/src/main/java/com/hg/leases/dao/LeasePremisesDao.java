package com.hg.leases.dao;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.hg.leases.model.LeasePremises;

public class LeasePremisesDao extends BaseDao<LeasePremises> {

	@Override
	@Transactional
	public void save(final LeasePremises premises) {
		super.save(premises);
	}

	@Transactional
	public LeasePremises get(final long id) {
		return super.get(id, LeasePremises.class);
	}

	@Transactional
	public Set<LeasePremises> get() {
		return super.get(LeasePremises.class, EMPTY_STRING, "addressLineOne");
	}
}