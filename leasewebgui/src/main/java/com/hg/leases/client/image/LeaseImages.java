package com.hg.leases.client.image;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface LeaseImages extends ClientBundle {

	public static LeaseImages INSTANCE = GWT.create(LeaseImages.class);

	@Source("uparrow.png")
	ImageResource uparrow();

	@Source("downarrow.png")
	ImageResource downarrow();
}