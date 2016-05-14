package com.hg.leases.shared;

import static com.hg.leases.model.LeaseConstants.ZERO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtLeaseMetaData implements IsSerializable,
		Comparable<GwtLeaseMetaData> {

	private Long id;

	private String type;

	private String description;

	private String value;

	private Integer order = ZERO;

	public GwtLeaseMetaData() {
		super();
	}

	public GwtLeaseMetaData(final Long id, final String type,
			final String description, final String value, final int order) {
		this.id = id;
		this.type = type;
		this.description = description;
		this.value = value;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public GwtLeaseMetaData clone() {
		return new GwtLeaseMetaData(id, type, description, value, order);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + order;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GwtLeaseMetaData other = (GwtLeaseMetaData) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (order != other.order)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(GwtLeaseMetaData metaData) {
		return order.compareTo(metaData.getOrder());
	}
}