package com.hg.leases.model;

import static com.hg.leases.model.LeaseConstants.DEFAULT_ID;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author hanleys
 */
@Entity
@Table(name = "LEASE_TENANT")
public class LeaseTenant extends LeaseEntity implements Serializable,
		Comparable<LeaseTenant> {

	private static final long serialVersionUID = 7273182997339634102L;

	@Column(name = "NAME")
	private String name;

	public LeaseTenant() {
		super();
	}

	public LeaseTenant(final String name) {
		this(DEFAULT_ID, name);
	}

	protected LeaseTenant(final Long id, final String name) {
		super(id);
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public int compareTo(final LeaseTenant tenant) {
		return name.compareTo(tenant.getName());
	}

	@Override
	public String key() {
		return name;
	}
}