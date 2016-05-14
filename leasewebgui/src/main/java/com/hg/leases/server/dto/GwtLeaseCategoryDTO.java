package com.hg.leases.server.dto;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.hg.leases.model.LeaseCategory;
import com.hg.leases.shared.GwtLeaseCategory;

public class GwtLeaseCategoryDTO {

	public Set<GwtLeaseCategory> fromLeaseCategory(
			final Set<LeaseCategory> categories) {
		Set<GwtLeaseCategory> gwtLeaseCategories = new LinkedHashSet<GwtLeaseCategory>();
		if (!isEmpty(categories)) {
			for (LeaseCategory category : categories) {
				GwtLeaseCategory gwtLeaseCategory = fromLeaseCategory(category);
				if (gwtLeaseCategory != null) {
					gwtLeaseCategories.add(gwtLeaseCategory);
				}
			}
		}
		return gwtLeaseCategories;
	}

	public GwtLeaseCategory fromLeaseCategory(final LeaseCategory category) {
		return new GwtLeaseCategory(category.getCategory());
	}
}