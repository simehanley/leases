package com.hg.leases.model;

import static com.hg.leases.model.LeaseConstants.DEFAULT_ID;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.hg.leases.model.enums.LeaseBondType;

@Entity
@Table(name = "LEASE_BOND")
public class LeaseBond extends LeaseEntity implements Serializable {

	private static final long serialVersionUID = 6462961135956779952L;

	@Column(name = "BOND_TYPE")
	@Enumerated(STRING)
	private LeaseBondType type;

	@Column(name = "BOND_AMOUNT")
	private double amount;

	@Column(name = "BOND_NOTES")
	private String notes;

	public LeaseBond() {
		super();
	}

	public LeaseBond(final LeaseBondType type, final double amount,
			final String notes) {
		this(DEFAULT_ID, type, amount, notes);
	}

	public LeaseBond(final Long id, final LeaseBondType type,
			final double amount, final String notes) {
		super(id);
		this.type = type;
		this.amount = amount;
		this.notes = notes;
	}

	public LeaseBondType getType() {
		return type;
	}

	public void setType(LeaseBondType type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String key() {
		throw new UnsupportedOperationException("Not implemented.");
	}
}