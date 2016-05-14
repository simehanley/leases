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
@Table(name = "LEASE_CATEGORY")
public class LeaseCategory extends LeaseEntity implements Serializable,
		Comparable<LeaseCategory> {

	private static final long serialVersionUID = -2827543561967544906L;

	@Column(name = "CATEGORY_NAME")
	private String category;

	@Column(name = "CATEGORY_COMPANY")
	private String categoryCompany;

	@Column(name = "CATEGORY_ABN")
	private String categoryAbn;

	@Column(name = "CATEGORY_ADDRESS")
	private String categoryAddress;

	@Column(name = "CATEGORY_PHONE")
	private String categoryPhone;

	@Column(name = "CATEGORY_ACCOUNT_NAME")
	private String categoryAccountName;

	@Column(name = "CATEGORY_BANK")
	private String categoryBank;

	@Column(name = "CATEGORY_BSB")
	private String categoryBsb;

	@Column(name = "CATEGORY_ACCOUNT_NUMBER")
	private String categoryAccountNumber;

	public LeaseCategory() {
		super();
	}

	public LeaseCategory(final String category) {
		this(DEFAULT_ID, category);
	}

	protected LeaseCategory(final Long id, final String category) {
		super(id);
		this.category = category;
	}

	public final String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getCategoryCompany() {
		return categoryCompany;
	}

	public void setCategoryCompany(String categoryCompany) {
		this.categoryCompany = categoryCompany;
	}

	public String getCategoryAbn() {
		return categoryAbn;
	}

	public void setCategoryAbn(String categoryAbn) {
		this.categoryAbn = categoryAbn;
	}

	public String getCategoryAddress() {
		return categoryAddress;
	}

	public void setCategoryAddress(String categoryAddress) {
		this.categoryAddress = categoryAddress;
	}

	public String getCategoryPhone() {
		return categoryPhone;
	}

	public void setCategoryPhone(String categoryPhone) {
		this.categoryPhone = categoryPhone;
	}

	public String getCategoryAccountName() {
		return categoryAccountName;
	}

	public void setCategoryAccountName(String categoryAccountName) {
		this.categoryAccountName = categoryAccountName;
	}

	public String getCategoryBank() {
		return categoryBank;
	}

	public void setCategoryBank(String categoryBank) {
		this.categoryBank = categoryBank;
	}

	public String getCategoryBsb() {
		return categoryBsb;
	}

	public void setCategoryBsb(String categoryBsb) {
		this.categoryBsb = categoryBsb;
	}

	public String getCategoryAccountNumber() {
		return categoryAccountNumber;
	}

	public void setCategoryAccountNumber(String categoryAccountNumber) {
		this.categoryAccountNumber = categoryAccountNumber;
	}

	public int compareTo(final LeaseCategory category) {
		return this.category.compareTo(category.getCategory());
	}

	@Override
	public String toString() {
		return "LeaseCategory [category=" + category + "]";
	}

	@Override
	public String key() {
		return category;
	}
}