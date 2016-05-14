package com.hg.leases.client;

import static com.hg.leases.client.LeaseClientConstants.AREA;
import static com.hg.leases.client.LeaseClientConstants.AREA_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.CATEGORY;
import static com.hg.leases.client.LeaseClientConstants.CATEGORY_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.DATE_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.END_DATE;
import static com.hg.leases.client.LeaseClientConstants.NUMERIC_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.PREMISES;
import static com.hg.leases.client.LeaseClientConstants.PREMISES_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.START_DATE;
import static com.hg.leases.client.LeaseClientConstants.TENANT;
import static com.hg.leases.client.LeaseClientConstants.TENANT_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.UNITS;
import static com.hg.leases.client.LeaseClientConstants.UNITS_COLUMN_WIDTH;
import static com.hg.leases.client.LeaseClientConstants.UPDATE_DATE;
import static com.hg.leases.client.css.LeaseWebGuiResources.INSTANCE;
import static com.hg.leases.client.util.LeaseFormatUtils.getDateFormat;
import static com.hg.leases.client.util.LeaseFormatUtils.getDoubleFormat;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.TOTAL;
import static com.hg.leases.shared.GwtLeaseType.newlease;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.hg.leases.shared.GwtLease;

public class LeaseMainGridModel {

	private ListHandler<GwtLease> handler;

	public void bind(final DataGrid<GwtLease> grid) {
		Map<String, Column<GwtLease, ?>> columns = createColumns(grid);
		createGridDataHandler(grid);
		setUpGridSorting(grid, columns);
	}

	private Map<String, Column<GwtLease, ?>> createColumns(
			final DataGrid<GwtLease> grid) {
		Map<String, Column<GwtLease, ?>> columns = new HashMap<String, Column<GwtLease, ?>>();
		createTextColumn(columns, grid, TENANT, TENANT_COLUMN_WIDTH);
		createTextColumn(columns, grid, PREMISES, PREMISES_COLUMN_WIDTH);
		createTextColumn(columns, grid, UNITS, UNITS_COLUMN_WIDTH);
		createTextColumn(columns, grid, AREA, AREA_COLUMN_WIDTH);
		createDateColumn(columns, grid, START_DATE);
		createDateColumn(columns, grid, END_DATE);
		createDateColumn(columns, grid, UPDATE_DATE);
		createAmountColumns(columns, grid, RENT);
		createAmountColumns(columns, grid, OUTGOINGS);
		createAmountColumns(columns, grid, PARKING);
		createAmountColumns(columns, grid, SIGNAGE);
		createAmountColumns(columns, grid, TOTAL);
		createTextColumn(columns, grid, CATEGORY, CATEGORY_COLUMN_WIDTH);
		return columns;
	}

	private void createTextColumn(
			final Map<String, Column<GwtLease, ?>> columns,
			final DataGrid<GwtLease> grid, final String type,
			final String columnWidth) {

		LeaseTextColumn textColumn = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				if (TENANT.equals(type)) {
					return lease.getTenant();
				} else if (PREMISES.equals(type)) {
					return lease.getPremises();
				} else if (UNITS.equals(type)) {
					return lease.getUnits();
				} else if (AREA.equals(type)) {
					return lease.getArea();
				}
				return lease.getCategory();
			}
		};
		textColumn.setSortable(true);
		grid.addColumn(textColumn, type);
		grid.setColumnWidth(textColumn, columnWidth);
		columns.put(type, textColumn);
	}

	private void createDateColumn(
			final Map<String, Column<GwtLease, ?>> columns,
			final DataGrid<GwtLease> grid, final String type) {

		Column<GwtLease, Date> dateCol = new Column<GwtLease, Date>(
				new DateCell(getDateFormat())) {

			@Override
			public String getCellStyleNames(final Context context,
					final GwtLease lease) {
				return getLeaseCellStyleNames(lease);
			}

			@Override
			public Date getValue(final GwtLease lease) {
				if (START_DATE.equals(type)) {
					return lease.getLeaseStart();
				} else if (END_DATE.equals(type)) {
					return lease.getLeaseEnd();
				}
				return lease.getUpdateDate();
			}
		};
		dateCol.setSortable(true);
		grid.addColumn(dateCol, type);
		grid.setColumnWidth(dateCol, DATE_COLUMN_WIDTH);
		columns.put(type, dateCol);
	}

	private void createAmountColumns(
			final Map<String, Column<GwtLease, ?>> columns,
			final DataGrid<GwtLease> grid, final String type) {

		LeaseTextColumn annualNet = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				return getDoubleFormat().format(lease.yearlyAmount(type));
			}
		};

		LeaseTextColumn annualGst = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				return getDoubleFormat().format(lease.yearlyGst(type));
			}
		};

		LeaseTextColumn annualGross = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				return getDoubleFormat().format(lease.yearlyTotal(type));
			}
		};

		LeaseTextColumn monthlyNet = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				return getDoubleFormat().format(lease.monthlyAmount(type));
			}
		};

		LeaseTextColumn monthlyGst = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				return getDoubleFormat().format(lease.monthlyGst(type));
			}
		};

		LeaseTextColumn monthlyGross = new LeaseTextColumn() {

			@Override
			public String getValue(final GwtLease lease) {
				return getDoubleFormat().format(lease.monthlyTotal(type));
			}
		};

		grid.addColumn(annualNet, "Net " + type + " (A)");
		grid.addColumn(annualGst, type + " Gst (A)");
		grid.addColumn(annualGross, "Gross " + type + " (A)");
		grid.addColumn(monthlyNet, "Net " + type + " (M)");
		grid.addColumn(monthlyGst, type + " Gst (M)");
		grid.addColumn(monthlyGross, "Gross " + type + " (M)");
		grid.setColumnWidth(annualNet, NUMERIC_COLUMN_WIDTH);
		grid.setColumnWidth(annualGst, NUMERIC_COLUMN_WIDTH);
		grid.setColumnWidth(annualGross, NUMERIC_COLUMN_WIDTH);
		grid.setColumnWidth(monthlyNet, NUMERIC_COLUMN_WIDTH);
		grid.setColumnWidth(monthlyGst, NUMERIC_COLUMN_WIDTH);
		grid.setColumnWidth(monthlyGross, NUMERIC_COLUMN_WIDTH);
		columns.put("Net " + type + " (A)", annualNet);
		columns.put(type + "Gst (A)", annualGst);
		columns.put("Gross " + type + " (A)", annualGross);
		columns.put("Net " + type + " (M)", monthlyNet);
		columns.put(type + "Gst (M)", monthlyGst);
		columns.put("Gross " + type + " (M)", monthlyGross);
	}

	private void createGridDataHandler(final DataGrid<GwtLease> grid) {
		ListDataProvider<GwtLease> provider = new ListDataProvider<GwtLease>();
		provider.addDataDisplay(grid);
		List<GwtLease> leases = provider.getList();
		handler = new ListHandler<GwtLease>(leases);
	}

	private void setUpGridSorting(final DataGrid<GwtLease> grid,
			final Map<String, Column<GwtLease, ?>> columns) {

		handler.setComparator(columns.get(TENANT), new Comparator<GwtLease>() {
			public int compare(final GwtLease one, final GwtLease two) {
				return one.getTenant().compareToIgnoreCase(two.getTenant());
			}
		});

		handler.setComparator(columns.get(PREMISES),
				new Comparator<GwtLease>() {
					public int compare(final GwtLease one, final GwtLease two) {
						return one.getPremises().compareToIgnoreCase(
								two.getPremises());
					}
				});

		handler.setComparator(columns.get(START_DATE),
				new Comparator<GwtLease>() {
					public int compare(final GwtLease one, final GwtLease two) {
						return one.getLeaseStart().compareTo(
								two.getLeaseStart());
					}
				});

		handler.setComparator(columns.get(END_DATE),
				new Comparator<GwtLease>() {
					public int compare(final GwtLease one, final GwtLease two) {
						return one.getLeaseEnd().compareTo(two.getLeaseEnd());
					}
				});

		handler.setComparator(columns.get(UPDATE_DATE),
				new Comparator<GwtLease>() {
					public int compare(final GwtLease one, final GwtLease two) {
						return one.getUpdateDate().compareTo(
								two.getUpdateDate());
					}
				});

		handler.setComparator(columns.get(CATEGORY),
				new Comparator<GwtLease>() {
					public int compare(final GwtLease one, final GwtLease two) {
						return one.getCategory().compareToIgnoreCase(
								two.getCategory());
					}
				});

		grid.addColumnSortHandler(handler);

		grid.getColumnSortList().push(columns.get(START_DATE));
		grid.getColumnSortList().push(columns.get(END_DATE));
		grid.getColumnSortList().push(columns.get(UPDATE_DATE));
		grid.getColumnSortList().push(columns.get(TENANT));
		grid.getColumnSortList().push(columns.get(PREMISES));
		grid.getColumnSortList().push(columns.get(CATEGORY));
	}

	private abstract class LeaseTextColumn extends TextColumn<GwtLease> {

		@Override
		public String getCellStyleNames(final Context context,
				final GwtLease lease) {
			return getLeaseCellStyleNames(lease);
		}
	}

	public final ListHandler<GwtLease> getHandler() {
		return handler;
	}

	private String getLeaseCellStyleNames(final GwtLease lease) {
		if (lease.getType().equals(newlease)) {
			return INSTANCE.style().green();
		} else if (lease.isInactive()) {
			return INSTANCE.style().red();
		} else if (lease.isEdited()) {
			return INSTANCE.style().orange();
		}
		return INSTANCE.style().black();
	}
}