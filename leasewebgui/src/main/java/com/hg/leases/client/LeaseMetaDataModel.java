package com.hg.leases.client;

import static com.hg.leases.client.css.LeaseWebGuiResources.INSTANCE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.hg.leases.shared.GwtLeaseMetaData;

public class LeaseMetaDataModel {

	private static final String KEY = "Key";
	private static final String VALUE = "Value";

	private ListHandler<GwtLeaseMetaData> handler;

	public void bind(final DataGrid<GwtLeaseMetaData> grid) {
		createColumns(grid);
		createGridDataHandler(grid);
	}

	private Map<String, Column<GwtLeaseMetaData, ?>> createColumns(
			final DataGrid<GwtLeaseMetaData> grid) {
		Map<String, Column<GwtLeaseMetaData, ?>> columns = new HashMap<String, Column<GwtLeaseMetaData, ?>>();
		createTextColumn(columns, grid, KEY, "100px");
		createTextColumn(columns, grid, VALUE, "100px");
		return columns;
	}

	private void createTextColumn(
			final Map<String, Column<GwtLeaseMetaData, ?>> columns,
			final DataGrid<GwtLeaseMetaData> grid, final String type,
			final String columnWidth) {

		LeaseMetaDataTextColumn column = new LeaseMetaDataTextColumn(
				new EditTextCell()) {
			@Override
			public String getValue(final GwtLeaseMetaData metaData) {
				if (type.equals(KEY)) {
					return metaData.getDescription();
				}
				return metaData.getValue();
			}
		};

		column.setFieldUpdater(new FieldUpdater<GwtLeaseMetaData, String>() {

			@Override
			public void update(int index, GwtLeaseMetaData metaData,
					String value) {
				if (type.equals(KEY)) {
					metaData.setDescription(value);
				} else {
					metaData.setValue(value);
				}
			}
		});

		column.setSortable(false);
		grid.addColumn(column, type);
		grid.setColumnWidth(column, columnWidth);
		columns.put(type, column);
	}

	private void createGridDataHandler(final DataGrid<GwtLeaseMetaData> grid) {
		ListDataProvider<GwtLeaseMetaData> provider = new ListDataProvider<GwtLeaseMetaData>();
		provider.addDataDisplay(grid);
		List<GwtLeaseMetaData> metadata = provider.getList();
		handler = new ListHandler<GwtLeaseMetaData>(metadata);
	}

	private abstract class LeaseMetaDataTextColumn extends
			Column<GwtLeaseMetaData, String> {

		public LeaseMetaDataTextColumn(Cell<String> cell) {
			super(cell);
		}

		@Override
		public String getCellStyleNames(final Context context,
				final GwtLeaseMetaData leaseMetaData) {
			return getLeaseMetaDataCellStyleNames(leaseMetaData);
		}
	}

	private String getLeaseMetaDataCellStyleNames(
			final GwtLeaseMetaData leaseMetaData) {
		return INSTANCE.style().black();
	}

	public final ListHandler<GwtLeaseMetaData> getHandler() {
		return handler;
	}
}