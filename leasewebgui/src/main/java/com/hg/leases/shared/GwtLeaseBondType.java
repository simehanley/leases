package com.hg.leases.shared;

public enum GwtLeaseBondType {

	deposit_held("Deposit Held"), bank_guarantee("Bank Guarantee");

	private final String stringValue;

	private GwtLeaseBondType(final String stringValue) {
		this.stringValue = stringValue;
	}

	public final String getStringValue() {
		return stringValue;
	}

	public static final GwtLeaseBondType fromString(final String stringValue) {
		for (GwtLeaseBondType type : values()) {
			if (stringValue.equals(type.getStringValue())) {
				return type;
			}
		}
		return null;
	}
}