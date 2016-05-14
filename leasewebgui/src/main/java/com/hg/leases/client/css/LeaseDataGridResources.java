package com.hg.leases.client.css;

import static com.google.gwt.user.cellview.client.DataGrid.Style.DEFAULT_CSS;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.DataGrid;

public interface LeaseDataGridResources extends DataGrid.Resources {

	public static LeaseDataGridResources INSTANCE = GWT.create(LeaseDataGridResources.class);
	
	interface LeaseDataGridStyle extends DataGrid.Style{}
	
	@Source({ DEFAULT_CSS, "leaseDataGrid.css" })
	LeaseDataGridStyle dataGridStyle();
}