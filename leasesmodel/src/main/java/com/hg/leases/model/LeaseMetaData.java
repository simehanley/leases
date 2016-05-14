package com.hg.leases.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LEASE_META_DATA")
public class LeaseMetaData extends LeaseEntity implements Serializable,
		Comparable<LeaseMetaData> {

	private static final long serialVersionUID = 545883444988943621L;

	@Column(name = "META_DATA_TYPE")
	private String type;

	@Column(name = "META_DATA_DESCRIPTION")
	private String description;

	@Column(name = "META_DATA_VALUE")
	private String value;

	@Column(name = "META_DATA_ORDER")
	private Integer order;

	public LeaseMetaData() {
		super();
	}

	public LeaseMetaData(final String type, final String description,
			final String value, final Integer order) {
		this(LeaseConstants.DEFAULT_ID, type, description, value, order);
	}

	public LeaseMetaData(final Long id, final String type,
			final String description, final String value, final Integer order) {
		super(id);
		this.type = type;
		this.description = description;
		this.value = value;
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	public String key() {
		return String.valueOf(getId());
	}

	@Override
	public int compareTo(final LeaseMetaData metaData) {
		return getOrder().compareTo(metaData.getOrder());
	}
}