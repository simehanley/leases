package com.hg.leases.client;

import static com.google.gwt.user.client.Event.ONCONTEXTMENU;
import static com.hg.leases.model.LeaseConstants.ZERO;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.hg.leases.client.css.LeaseDataGridResources;
import com.hg.leases.client.image.LeaseImages;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseMetaData;

public class LeaseMetaData extends Composite {

	private static LeaseMetaDataUiBinder uiBinder = GWT
			.create(LeaseMetaDataUiBinder.class);

	@UiField(provided = true)
	PushButton btnUp;

	@UiField(provided = true)
	PushButton btnDown;

	@UiField(provided = true)
	DataGrid<GwtLeaseMetaData> grid;

	private LeaseMetaDataModel model;

	private PopupPanel contextMenu;

	private GwtLease lease;

	private String type;

	interface LeaseMetaDataUiBinder extends UiBinder<Widget, LeaseMetaData> {
	}

	public LeaseMetaData() {
		initButtons();
		initGrid();
		initContextMenu();
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void initButtons() {
		btnUp = new PushButton(new Image(LeaseImages.INSTANCE.uparrow()));
		btnDown = new PushButton(new Image(LeaseImages.INSTANCE.downarrow()));
	}

	private void initGrid() {
		grid = new DataGrid<GwtLeaseMetaData>(100,
				LeaseDataGridResources.INSTANCE);
		model = new LeaseMetaDataModel();
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

	private MenuBar createContextMenuMenuBar() {
		MenuBar gridMenu = new MenuBar(true);
		gridMenu.setAnimationEnabled(true);
		gridMenu.addItem("New", new Command() {
			@Override
			public void execute() {
				newMetaData();
				contextMenu.hide();
			}
		});
		gridMenu.addItem("Delete", new Command() {
			@Override
			public void execute() {
				deleteMetaData();
				contextMenu.hide();
			}
		});
		return gridMenu;
	}

	protected void newMetaData() {
		List<GwtLeaseMetaData> metaDataByType = lease.getMetaData(type);
		int index = metaDataByType.size();
		GwtLeaseMetaData metaData = new GwtLeaseMetaData(null, type,
				"DEFAULT KEY", "DEFAULT VALUE", index);
		lease.getMetaData().add(metaData);
		model.getHandler().getList().add(index, metaData);
	}

	protected void deleteMetaData() {
		List<GwtLeaseMetaData> metaDataByType = lease.getMetaData(type);
		if (metaDataByType.size() > ZERO) {
			int row = grid.getKeyboardSelectedRow();
			List<GwtLeaseMetaData> metaData = model.getHandler().getList();
			GwtLeaseMetaData md = metaData.get(row);
			lease.getMetaData().remove(md);
			metaData.remove(md);
		}
	}

	public void setLeaseAndType(final GwtLease lease, final String type) {
		this.lease = lease;
		this.type = type;
		model.getHandler().getList().clear();
		initMetaData();
	}

	private void initMetaData() {
		List<GwtLeaseMetaData> metaDataByType = lease.getMetaData(type);
		Collections.sort(metaDataByType);
		List<GwtLeaseMetaData> metaData = model.getHandler().getList();
		for (GwtLeaseMetaData md : metaDataByType) {
			metaData.add(md);
		}
		grid.redraw();
	}
}