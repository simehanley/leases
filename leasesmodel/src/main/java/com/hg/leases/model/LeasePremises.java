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
@Table(name = "LEASE_PREMISES")
public class LeasePremises extends LeaseEntity implements Serializable,
		Comparable<LeasePremises> {

	private static final long serialVersionUID = -5889400343646516820L;

	@Column(name = "ADDRESS_LINE_ONE")
	private String addressLineOne;

	@Column(name = "ADDRESS_LINE_TWO")
	private String addressLineTwo;

	public LeasePremises() {
		super();
	}

	public LeasePremises(final String addressLineOne,
			final String addressLineTwo) {
		this(DEFAULT_ID, addressLineOne, addressLineTwo);
	}

	protected LeasePremises(final Long id, final String addressLineOne,
			final String addressLineTwo) {
		super(id);
		this.addressLineOne = addressLineOne;
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public int compareTo(final LeasePremises premises) {
		return addressLineOne.compareTo(premises.getAddressLineOne());
	}

	@Override
	public String key() {
		return addressLineOne;
	}
}