package com.hg.leases.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.hg.leases.client.css.LeaseDataGridResources;
import com.hg.leases.client.css.LeaseWebGuiResources;

public class LeasesWebGui implements EntryPoint {

	public void onModuleLoad() {
		init();
	}

	private void init() {
		LeaseDataGridResources.INSTANCE.dataGridStyle().ensureInjected();
		LeaseWebGuiResources.INSTANCE.style().ensureInjected();
		initMain();
	}

	private void initMain() {
		LeaseMain main = new LeaseMain();
		RootLayoutPanel.get().add(main);
	}
}