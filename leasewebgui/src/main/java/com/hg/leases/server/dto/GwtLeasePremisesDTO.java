package com.hg.leases.server.dto;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.hg.leases.model.LeasePremises;
import com.hg.leases.shared.GwtLeasePremises;

public class GwtLeasePremisesDTO {

	public Set<GwtLeasePremises> fromLeasePremises(
			final Set<LeasePremises> premises) {
		Set<GwtLeasePremises> gwtLeasePremises = new LinkedHashSet<GwtLeasePremises>();
		if (!isEmpty(premises)) {
			for (LeasePremises premise : premises) {
				GwtLeasePremises gwtLeasePremise = fromLeasePremises(premise);
				if (gwtLeasePremise != null) {
					gwtLeasePremises.add(gwtLeasePremise);
				}
			}
		}
		return gwtLeasePremises;
	}

	public GwtLeasePremises fromLeasePremises(final LeasePremises premise) {
		return new GwtLeasePremises(premise.getAddressLineOne());
	}
}