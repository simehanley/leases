package com.hg.leases.client.css;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LeaseWebGuiResources extends ClientBundle {

	public static LeaseWebGuiResources INSTANCE = GWT
			.create(LeaseWebGuiResources.class);

	interface LeaseWebGuiStyle extends CssResource {

		String headerformat();

		String footerformat();

		String whiteheaderlabel();

		String whitestandardlabel();

		String toolbarformat();

		String paneloutline();

		String disabledtextbox();

		String datebox();

		String boldlabel();

		String boldlabellarge();

		String numerictextbox();

		String boldtextarea();

		String boldtextareawithborder();

		String checkbox();

		String lightgray();

		String orange();

		String black();

		String red();

		String green();
	}

	@Source("leaseWebGui.css")
	LeaseWebGuiStyle style();
}