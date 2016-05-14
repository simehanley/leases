package com.hg.leases.shared;

public class GwtPair<T> {

	private final T first;

	private final T second;

	public GwtPair(final T first, final T second) {
		this.first = first;
		this.second = second;
	}

	public final T first() {
		return first;
	}

	public final T second() {
		return second;
	}
}