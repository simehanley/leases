package com.hg.leases.client;

import static com.google.gwt.dom.client.Style.Unit.PX;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;
import static com.google.gwt.user.client.Event.ONCONTEXTMENU;
import static com.hg.leases.client.LeaseClientConstants.CATEGORY;
import static com.hg.leases.client.LeaseClientConstants.LEASE_ID;
import static com.hg.leases.client.LeaseClientConstants.PREMISES;
import static com.hg.leases.client.LeaseClientConstants.REPORT_TYPE;
import static com.hg.leases.client.LeaseClientConstants.SHOW_INACTIVE;
import static com.hg.leases.client.LeaseClientConstants.TENANT;
import static com.hg.leases.client.css.LeaseDataGridResources.INSTANCE;
import static com.hg.leases.model.LeaseConstants.ZERO;
import static com.hg.leases.shared.GwtLeaseReportType.lease_excel;
import static com.hg.leases.shared.GwtLeaseReportType.lease_excel_invoice;
import static com.hg.leases.shared.GwtLeaseReportType.lease_excel_schedule;
import static com.hg.leases.shared.GwtLeaseReportType.lease_myob_schedule;
import static com.hg.leases.shared.GwtLeaseType.existing;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseCategory;
import com.hg.leases.shared.GwtLeaseFilter;
import com.hg.leases.shared.GwtLeasePremises;
import com.hg.leases.shared.GwtLeaseReportType;
import com.hg.leases.shared.GwtLeaseTenant;

public class LeaseMain extends Composite implements LeaseUpdateHandler {

	private static LeaseMainUiBinder uiBinder = GWT
			.create(LeaseMainUiBinder.class);

	interface LeaseMainUiBinder extends UiBinder<Widget, LeaseMain> {
	}

	private final LeaseServiceAsync leaseService = GWT
			.create(LeaseService.class);

	private final Logger logger = Logger.getLogger("LeaseMain");

	@UiField(provided = true)
	DataGrid<GwtLease> grid;

	@UiField(provided = true)
	TextBox txtTenant;

	@UiField(provided = true)
	TextBox txtPremises;

	@UiField(provided = true)
	TextBox txtCategory;

	@UiField(provided = true)
	CheckBox chkShowInactive;

	@UiField
	Label lblTenant;

	@UiField
	Label lblPremises;

	@UiField
	Label lblCategory;

	@UiField(provided = true)
	Button btnRefresh;

	@UiField(provided = true)
	Button btnSave;

	@UiField(provided = true)
	Button btnEmail;

	@UiField(provided = true)
	Button btnToAction;

	private LeaseMainGridModel model;

	private PopupPanel contextMenu;

	private final LeaseInput input = new LeaseInput(this, leaseService);

	public LeaseMain() {
		init();
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void init() {
		initGrid();
		initContextMenu();
		initWidgets();
		load();
	}

	private void initGrid() {
		grid = new DataGrid<GwtLease>(100, INSTANCE);
		grid.setMinimumTableWidth(600, PX);
		grid.addDomHandler(new LeaseMainDoubleClickHandler(),
				DoubleClickEvent.getType());
		grid.addDomHandler(new LeaseMainKeyPressHandler(),
				KeyPressEvent.getType());
		final SingleSelectionModel<GwtLease> selectionModel = new SingleSelectionModel<GwtLease>();
		grid.setSelectionModel(selectionModel);
		model = new LeaseMainGridModel();
		model.bind(grid);
	}

	private void initContextMenu() {
		contextMenu = new PopupPanel(true);
		contextMenu.add(createContextMenuMenuBar());
		contextMenu.hide();
		grid.sinkEvents(ONCONTEXTMENU);
		grid.addHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(final ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
				contextMenu.setPopupPosition(event.getNativeEvent()
						.getClientX(), event.getNativeEvent().getClientY());
				contextMenu.show();
			}
		}, ContextMenuEvent.getType());
	}

	private void initWidgets() {
		txtTenant = new TextBox();
		txtTenant.setTitle("Lease Tenant Filter.");
		txtPremises = new TextBox();
		txtPremises.setTitle("Lease Premises Filter.");
		txtCategory = new TextBox();
		txtCategory.setTitle("Lease Category Filter.");
		chkShowInactive = new CheckBox();
		chkShowInactive.setTitle("Toggle for showing inactive Leases");
		KeyPressHandler filterHandler = new LeaseMainLeaseFilterKeyPressHandler();
		txtTenant.addKeyPressHandler(filterHandler);
		txtPremises.addKeyPressHandler(filterHandler);
		txtCategory.addKeyPressHandler(filterHandler);
		btnRefresh = new Button();
		btnRefresh.setTitle("Reload data from the database.");
		btnSave = new Button();
		btnSave.setTitle("Save edited leases to the database.");
		btnEmail = new Button();
		btnEmail.setTitle("Send out daily lease email.");
		btnToAction = new Button();
		btnToAction.setTitle("Load 'incomplete' leases.");
	}

	public DataGrid<GwtLease> getGrid() {
		return grid;
	}

	public void setGrid(final DataGrid<GwtLease> grid) {
		this.grid = grid;
	}

	@Override
	public void updated(final GwtLease lease) {
		int row = grid.getKeyboardSelectedRow();
		switch (lease.getType()) {
		case existing:
			model.getHandler().getList().set(row, lease);
			break;
		case newlease:
			model.getHandler().getList().add(ZERO, lease);
			lease.setType(existing);
			/*
			 * temporary hack until I get the ability to add new
			 * tenants/premises/categories
			 */
			loadLeaseTenants();
			break;
		case deleted:
			model.getHandler().getList().remove(row);
		}
	}

	private class LeaseMainDoubleClickHandler implements DoubleClickHandler {

		@Override
		public void onDoubleClick(final DoubleClickEvent event) {
			input.setLease(grid.getVisibleItem(grid.getKeyboardSelectedRow()));
			input.show();
		}
	}

	private class LeaseMainKeyPressHandler implements KeyPressHandler {

		@Override
		public void onKeyPress(final KeyPressEvent event) {
			if (event.getNativeEvent().getKeyCode() == KEY_ENTER) {
				input.setLease(grid.getVisibleItem(grid
						.getKeyboardSelectedRow()));
				input.show();
			}
		}
	}

	private class LeaseMainLeaseFilterKeyPressHandler implements
			KeyPressHandler {

		@Override
		public void onKeyPress(final KeyPressEvent event) {
			if (event.getNativeEvent().getKeyCode() == KEY_ENTER) {
				load();
			}
		}
	}

	private Set<GwtLease> resolveEditedLeases() {
		List<GwtLease> leases = model.getHandler().getList();
		Set<GwtLease> edited = new HashSet<GwtLease>();
		for (GwtLease lease : leases) {
			if (lease.isEdited()) {
				edited.add(lease);
			}
		}
		return edited;
	}

	private void refreshLeases() {
		List<GwtLease> leases = new ArrayList<GwtLease>(model.getHandler()
				.getList());
		for (GwtLease lease : leases) {
			if (lease.isEdited()) {
				lease.setEdited(false);
			}
			if (!chkShowInactive.getValue() && lease.isInactive()) {
				model.getHandler().getList().remove(lease);
			}
		}
		grid.redraw();
	}

	private void load() {
		leaseService.getLeases(resolveFilter(),
				new AsyncCallback<Set<GwtLease>>() {
					@Override
					public void onFailure(final Throwable caught) {
						logger.log(SEVERE, "Unable to load leases.", caught);
					}

					@Override
					public void onSuccess(final Set<GwtLease> result) {
						logger.log(Level.INFO, "Loaded leases.");
						reloadGrid(result);
					}
				});
		loadLeaseMetaData();
	}

	private GwtLeaseFilter resolveFilter() {
		return new GwtLeaseFilter(txtTenant.getValue(), txtPremises.getValue(),
				txtCategory.getValue(), chkShowInactive.getValue());
	}

	private void loadLeaseMetaData() {
		loadLeaseCategories();
	}

	private void loadLeaseCategories() {
		leaseService
				.getLeaseCategories(new AsyncCallback<Set<GwtLeaseCategory>>() {

					@Override
					public void onSuccess(final Set<GwtLeaseCategory> result) {
						logger.log(Level.INFO, "Loaded lease categories.");
						input.setLeaseCategories(result);
						loadLeasePremises();
					}

					@Override
					public void onFailure(final Throwable caught) {
						logger.log(SEVERE, "Unable to load lease categories.",
								caught);
					}
				});
	}

	private void loadLeasePremises() {
		leaseService
				.getLeasePremises(new AsyncCallback<Set<GwtLeasePremises>>() {

					@Override
					public void onSuccess(final Set<GwtLeasePremises> result) {
						logger.log(INFO, "Loaded lease premises.");
						input.setLeasePremises(result);
						loadLeaseTenants();
					}

					@Override
					public void onFailure(final Throwable caught) {
						logger.log(SEVERE, "Unable to load lease premises.",
								caught);
					}
				});
	}

	private void loadLeaseTenants() {
		leaseService.getLeaseTenants(new AsyncCallback<Set<GwtLeaseTenant>>() {

			@Override
			public void onSuccess(final Set<GwtLeaseTenant> result) {
				logger.log(INFO, "Loaded lease tenants.");
				input.setLeaseTenants(result);
			}

			@Override
			public void onFailure(final Throwable caught) {
				logger.log(SEVERE, "Unable to load lease tenants.", caught);
			}
		});
	}

	private void reloadGrid(final Set<GwtLease> result) {
		model.getHandler().getList().clear();
		model.getHandler().getList().addAll(result);
		grid.redraw();
	}

	private MenuBar createContextMenuMenuBar() {
		MenuBar gridMenu = new MenuBar(true);
		gridMenu.setAnimationEnabled(true);

		gridMenu.addItem("New", new Command() {
			@Override
			public void execute() {
				newLease();
				contextMenu.hide();
			}
		});
		gridMenu.addItem("Delete", new Command() {
			@Override
			public void execute() {
				deleteLease();
				contextMenu.hide();
			}
		});
		gridMenu.addSeparator();
		gridMenu.addItem("Report", new Command() {
			@Override
			public void execute() {
				createExcelLeaseReport();
				contextMenu.hide();
			}
		});
		gridMenu.addItem("Invoice", new Command() {
			@Override
			public void execute() {
				createSingleLeaseReport(lease_excel_invoice);
				contextMenu.hide();
			}
		});
		gridMenu.addItem("Schedule", new Command() {
			@Override
			public void execute() {
				createSingleLeaseReport(lease_excel_schedule);
				contextMenu.hide();
			}
		});
		gridMenu.addSeparator();
		gridMenu.addItem("Myob Schedule", new Command() {
			@Override
			public void execute() {
				createSingleLeaseReport(lease_myob_schedule);
				contextMenu.hide();
			}
		});
		return gridMenu;
	}

	private void createExcelLeaseReport() {
		String type = REPORT_TYPE + "=" + lease_excel;
		String tenant = TENANT + "=" + txtTenant.getValue();
		String premises = PREMISES + "=" + txtPremises.getValue();
		String category = CATEGORY + "=" + txtCategory.getValue();
		String showInactive = SHOW_INACTIVE + "=" + chkShowInactive.getValue();
		String excelReportUrl = GWT.getModuleBaseURL() + "leaseReports" + "?"
				+ type + "&" + tenant + "&" + premises + "&" + category + "&"
				+ showInactive;
		Window.Location.replace(excelReportUrl);
	}

	private void createSingleLeaseReport(
			final GwtLeaseReportType leaseReportType) {
		GwtLease lease = grid.getVisibleItem(grid.getKeyboardSelectedRow());
		if (lease != null) {
			if (lease.getId() != null) {
				if (!lease.isEdited() && !lease.isInactive()) {
					String type = REPORT_TYPE + "=" + leaseReportType;
					String leaseId = LEASE_ID + "=" + lease.getId();
					String reportUrl = GWT.getModuleBaseURL() + "leaseReports"
							+ "?" + type + "&" + leaseId;
					Window.Location.replace(reportUrl);
				}
			}
		}
	}

	private void removeLease(final GwtLease lease) {
		model.getHandler().getList().remove(lease);
	}

	private void newLease() {
		input.setLease(null);
		input.show();
	}

	private void deleteLease() {
		final GwtLease lease = grid.getVisibleItem(grid
				.getKeyboardSelectedRow());
		if (lease != null) {
			if (lease.getId() == null) {
				removeLease(lease);
			} else {
				leaseService.deleteLease(lease.getId(),
						new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								logger.log(SEVERE,
										"Unable to delete lease for tenant "
												+ lease.getTenant() + "'.",
										caught);
							}

							@Override
							public void onSuccess(Void result) {
								logger.log(
										Level.INFO,
										"Deleted lease for tenant "
												+ lease.getTenant() + "'.");
								removeLease(lease);
							}
						});
			}
		}
	}

	@UiHandler("btnRefresh")
	void onBtnRefreshClick(final ClickEvent event) {
		load();
	}

	@UiHandler("btnSave")
	void onBtnSaveClick(final ClickEvent event) {
		Set<GwtLease> edited = resolveEditedLeases();
		if (edited.size() != ZERO) {
			leaseService.saveLeases(resolveEditedLeases(),
					new AsyncCallback<Void>() {
						@Override
						public void onFailure(final Throwable caught) {
							GWT.log("Error saving edited leases.", caught);
						}

						@Override
						public void onSuccess(final Void result) {
							refreshLeases();
						}
					});
		}
	}

	@UiHandler("btnEmail")
	void onBtnEmailClick(final ClickEvent event) {
		leaseService.sendEmail(new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable caught) {
				GWT.log("Error sending maintenance email.", caught);
			}

			@Override
			public void onSuccess(final Void result) {
				/* do nothing */
			}
		});
	}

	@UiHandler("chkShowInactive")
	void onChkShowInactiveValueChange(ValueChangeEvent<Boolean> event) {
		load();
	}

	@UiHandler("btnToAction")
	void onBtnToActionClick(ClickEvent event) {

		leaseService.getLeasesToAction(new AsyncCallback<Set<GwtLease>>() {

			@Override
			public void onSuccess(final Set<GwtLease> result) {
				logger.log(Level.INFO, "Loaded actionable leases.");
				reloadGrid(result);
			}

			@Override
			public void onFailure(final Throwable caught) {
				logger.log(SEVERE, "Unable to load leases to action.", caught);
			}
		});
	}
}