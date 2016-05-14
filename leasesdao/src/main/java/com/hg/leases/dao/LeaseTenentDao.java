package com.hg.leases.dao;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.hg.leases.model.LeaseTenant;

public class LeaseTenentDao extends BaseDao<LeaseTenant> {

	@Override
	@Transactional
	public void save(final LeaseTenant tenent) {
		super.save(tenent);
	}

	@Transactional
	public LeaseTenant get(final long id) {
		return super.get(id, LeaseTenant.class);
	}

	@Transactional
	public Set<LeaseTenant> get() {
		return super.get(LeaseTenant.class, EMPTY_STRING, "name");
	}
}