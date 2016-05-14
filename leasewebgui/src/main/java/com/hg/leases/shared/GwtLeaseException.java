package com.hg.leases.shared;

public class GwtLeaseException extends RuntimeException {

	private static final long serialVersionUID = -4282662957305414298L;

	public GwtLeaseException() {
		super();
	}

	public GwtLeaseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GwtLeaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public GwtLeaseException(String message) {
		super(message);
	}

	public GwtLeaseException(Throwable cause) {
		super(cause);
	}
}