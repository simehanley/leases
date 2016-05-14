package com.hg.leases.model;

import static com.hg.leases.model.LeaseConstants.DEFAULT_ID;
import static com.hg.leases.model.LeaseConstants.GST_PERCENTAGE;
import static com.hg.leases.model.LeaseConstants.MONTHS_PER_YEAR;
import static com.hg.leases.model.utilities.LeaseNumericUtilities.round;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author hanleys
 */
@Entity
@Table(name = "LEASE_INCIDENTAL")
public class LeaseIncidental extends LeaseEntity implements Serializable {

	private static final long serialVersionUID = -5576050378961538895L;

	/** name of the incidental **/
	@Column(name = "NAME")
	private String name;

	/** yearly amount of the incidental **/
	@Column(name = "AMOUNT")
	private double amount;

	/** percentage increase (yearly) applied to the incidental **/
	@Column(name = "PERCENTAGE")
	private double percentage;

	/** myob account number **/
	@Column(name = "ACCOUNT_NUMBER")
	private String account;

	public LeaseIncidental() {
		super();
	}

	public LeaseIncidental(final String name, final double amount,
			final double percentage, final String account) {
		this(DEFAULT_ID, name, amount, percentage, account);
	}

	protected LeaseIncidental(final Long id, final String name,
			final double amount, final double percentage, final String account) {
		super(id);
		this.name = name;
		this.amount = amount;
		this.percentage = percentage;
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public final double monthlyAmount() {
		return round(getAmount() / MONTHS_PER_YEAR);
	}

	public final double monthlyGst() {
		return round(yearlyGst() / MONTHS_PER_YEAR);
	}

	public final double monthlyTotal() {
		return monthlyAmount() + monthlyGst();
	}

	public final double yearlyAmount() {
		return getAmount();
	}

	public final double yearlyGst() {
		return round(GST_PERCENTAGE * yearlyAmount());
	}

	public final double yearlyTotalAmount() {
		return yearlyAmount() + yearlyGst();
	}

	@Override
	public String toString() {
		return "LeaseIncidental [name=" + name + ", amount=" + amount
				+ ", percentage=" + percentage + "]";
	}

	@Override
	public String key() {
		return name;
	}
}