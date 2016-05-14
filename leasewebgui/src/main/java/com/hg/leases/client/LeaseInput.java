package com.hg.leases.client;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_BACKSPACE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;
import static com.hg.leases.client.util.LeaseClientDateUtilities.addDays;
import static com.hg.leases.client.util.LeaseClientDateUtilities.addYears;
import static com.hg.leases.client.util.LeaseFormatUtils.getDoubleFormat;
import static com.hg.leases.client.util.LeaseFormatUtils.getPercentFormat;
import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.HISTORICAL_RENT;
import static com.hg.leases.model.LeaseConstants.NOTICE;
import static com.hg.leases.model.LeaseConstants.ONE;
import static com.hg.leases.model.LeaseConstants.ONE_HUNDRED_DBL;
import static com.hg.leases.model.LeaseConstants.OPTION;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.SPECIAL_CONDITION;
import static com.hg.leases.model.LeaseConstants.TWO;
import static com.hg.leases.model.LeaseConstants.ZERO;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.shared.GwtLeaseBondType.fromString;
import static com.hg.leases.shared.GwtLeaseType.existing;
import static com.hg.leases.shared.GwtLeaseType.newlease;
import static com.hg.leases.shared.GwtLeaseUtilities.round;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.hg.leases.client.util.LeaseFormatUtils;
import com.hg.leases.shared.GwtLease;
import com.hg.leases.shared.GwtLeaseBondType;
import com.hg.leases.shared.GwtLeaseCategory;
import com.hg.leases.shared.GwtLeasePremises;
import com.hg.leases.shared.GwtLeaseTenant;

public class LeaseInput extends DialogBox {

	private final Logger logger = Logger.getLogger("LeaseInput");

	private static LeaseInputUiBinder uiBinder = GWT
			.create(LeaseInputUiBinder.class);

	private static final int META_DATA_TAB = 1;

	@UiField
	TabPanel tabPanel;

	@UiField
	LeaseMetaData mtdRent;

	@UiField
	LeaseMetaData mtdNotice;

	@UiField
	LeaseMetaData mtdSpecialCond;

	@UiField
	LeaseMetaData mtdOption;

	@UiField
	Button btnCancel;

	@UiField
	Button btnSave;

	@UiField
	TextBox txtUnit;

	@UiField
	TextBox txtArea;

	@UiField
	DateBox dtStart;

	@UiField
	DateBox dtEnd;

	@UiField
	DateBox dtUpdated;

	@UiField
	DateBox dtOptionStart;

	@UiField
	DateBox dtOptionEnd;

	@UiField
	TextBox txtRent;

	@UiField
	TextBox txtRentPercent;

	@UiField
	TextBox txtRentAccNo;

	@UiField
	TextBox txtOutgoings;

	@UiField
	TextBox txtOutgoingsPercent;

	@UiField
	TextBox txtOutgoingsAccNo;

	@UiField
	TextBox txtParking;

	@UiField
	TextBox txtParkingPercent;

	@UiField
	TextBox txtParkingAccNo;

	@UiField
	TextBox txtSignage;

	@UiField
	TextBox txtSignagePercent;

	@UiField
	TextBox txtSignageAccNo;

	@UiField
	TextBox txtTenant;

	@UiField
	TextBox txtBondAmount;

	@UiField
	TextArea txtNotes;

	@UiField
	TextArea txtBondNotes;

	@UiField
	CheckBox chkResidential;

	@UiField
	CheckBox chkInactive;

	@UiField
	CheckBox chkHasOption;

	@UiField
	ListBox cmbCategory;

	@UiField
	ListBox cmbPremises;

	@UiField
	ListBox cmbTenant;

	@UiField
	ListBox cmbBondType;

	@UiField
	TextBox txtJobNo;

	private final LeaseUpdateHandler handler;

	private final LeaseServiceAsync leaseService;

	private GwtLease original;

	private GwtLease copy;

	private final double tolerance = 1e-10;

	interface LeaseInputUiBinder extends UiBinder<Widget, LeaseInput> {
	}

	public LeaseInput(final LeaseUpdateHandler handler,
			final LeaseServiceAsync leaseService) {
		this.handler = handler;
		this.leaseService = leaseService;
		setWidget(uiBinder.createAndBindUi(this));
		setModal(false);
		setText("Lease Input");
		initPanel();
		tabPanel.selectTab(ZERO);
	}

	public GwtLease getLease() {
		return copy;
	}

	public void setLease(final GwtLease lease) {
		if (lease != null) {
			this.original = lease;
			this.copy = this.original.clone();
		} else {
			this.original = new GwtLease();
			this.original.setType(newlease);
			this.original.setId(null);
			this.copy = this.original.clone();
		}
		initLease();
	}

	private void initPanel() {
		DateBox.Format format = new DateBox.DefaultFormat(
				LeaseFormatUtils.getDateFormat());
		dtStart.setFormat(format);
		dtEnd.setFormat(format);
		dtUpdated.setFormat(format);
		dtOptionStart.setFormat(format);
		dtOptionEnd.setFormat(format);
		KeyPressHandler numericHandler = new NumericKeyPressHandler();
		txtArea.addKeyPressHandler(numericHandler);
		txtRent.addKeyPressHandler(numericHandler);
		txtRentPercent.addKeyPressHandler(numericHandler);
		txtOutgoings.addKeyPressHandler(numericHandler);
		txtOutgoingsPercent.addKeyPressHandler(numericHandler);
		txtSignage.addKeyPressHandler(numericHandler);
		txtSignagePercent.addKeyPressHandler(numericHandler);
		txtParking.addKeyPressHandler(numericHandler);
		txtParkingPercent.addKeyPressHandler(numericHandler);
		txtBondAmount.addKeyPressHandler(numericHandler);
		BlurHandler textBoxBlurHandler = new TextBoxBlurHandler();
		txtTenant.addBlurHandler(textBoxBlurHandler);
		txtArea.addBlurHandler(textBoxBlurHandler);
		txtUnit.addBlurHandler(textBoxBlurHandler);
		txtRentAccNo.addBlurHandler(textBoxBlurHandler);
		txtOutgoingsAccNo.addBlurHandler(textBoxBlurHandler);
		txtParkingAccNo.addBlurHandler(textBoxBlurHandler);
		txtSignageAccNo.addBlurHandler(textBoxBlurHandler);
		txtJobNo.addBlurHandler(textBoxBlurHandler);
		BlurHandler numericBlurHandler = new NumericBlurHandler();
		txtArea.addBlurHandler(numericBlurHandler);
		txtRent.addBlurHandler(numericBlurHandler);
		txtRentPercent.addBlurHandler(numericBlurHandler);
		txtOutgoings.addBlurHandler(numericBlurHandler);
		txtOutgoingsPercent.addBlurHandler(numericBlurHandler);
		txtSignage.addBlurHandler(numericBlurHandler);
		txtSignagePercent.addBlurHandler(numericBlurHandler);
		txtParking.addBlurHandler(numericBlurHandler);
		txtParkingPercent.addBlurHandler(numericBlurHandler);
		txtBondAmount.addBlurHandler(numericBlurHandler);
		BlurHandler textAreaBlurHandler = new TextAreaBlurHandler();
		txtNotes.addBlurHandler(textAreaBlurHandler);
		txtBondNotes.addBlurHandler(textAreaBlurHandler);
		ValueChangeHandler<Date> dateChangeHandler = new DateChangeHandler();
		dtStart.addValueChangeHandler(dateChangeHandler);
		dtEnd.addValueChangeHandler(dateChangeHandler);
		dtUpdated.addValueChangeHandler(dateChangeHandler);
		dtOptionStart.addValueChangeHandler(dateChangeHandler);
		dtOptionEnd.addValueChangeHandler(dateChangeHandler);
		ValueChangeHandler<Boolean> booleanChangeHandler = new BooleanValueChangeHandler();
		chkResidential.addValueChangeHandler(booleanChangeHandler);
		chkInactive.addValueChangeHandler(booleanChangeHandler);
		chkHasOption.addValueChangeHandler(booleanChangeHandler);
		ChangeHandler changeHandler = new ComboChangeHandler();
		cmbTenant.addChangeHandler(changeHandler);
		cmbPremises.addChangeHandler(changeHandler);
		cmbCategory.addChangeHandler(changeHandler);
		cmbBondType.addChangeHandler(changeHandler);

		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (META_DATA_TAB == event.getSelectedItem()) {
					initMetaData();
				}
			}
		});
		loadBondTypes();
	}

	private void loadBondTypes() {
		for (GwtLeaseBondType type : GwtLeaseBondType.values()) {
			cmbBondType.addItem(type.getStringValue());
		}
	}

	private void initCombos() {
		if (copy != null) {
			cmbTenant.setItemSelected(
					getItemIndex(cmbTenant, copy.getTenant()), true);
			cmbPremises.setItemSelected(
					getItemIndex(cmbPremises, copy.getPremises()), true);
			cmbCategory.setItemSelected(
					getItemIndex(cmbCategory, copy.getCategory()), true);
			if (copy.getBondType() != null) {
				cmbBondType.setItemSelected(
						getItemIndex(cmbBondType, copy.getBondType()
								.getStringValue()), true);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void initLease() {
		if (copy != null) {
			initCombos();
			switch (copy.getType()) {
			case existing:
				initExistingLease();
				break;
			case newlease:
				initNewLease();
				break;
			}
			dtUpdated.setEnabled(false);
			disableInactiveWidgets(!chkInactive.getValue());
			setOptionDates();
		}
		center();
	}

	private void initExistingLease() {
		setTextBoxValue(txtUnit, copy.getUnits());
		setTextBoxValue(txtArea, copy.getArea());
		setDateBoxValue(dtStart, copy.getLeaseStart());
		setDateBoxValue(dtEnd, copy.getLeaseEnd());
		setDateBoxValue(dtUpdated, copy.getUpdateDate());
		setTextBoxValue(txtRent,
				getDoubleFormat().format(copy.getIncidentalAmount(RENT)));
		setTextBoxValue(txtRentPercent,
				getPercentFormat().format(copy.getIncidentalPercent(RENT)));
		setTextBoxValue(txtRentAccNo, copy.getIncidentalAccount(RENT));
		setTextBoxValue(txtOutgoings,
				getDoubleFormat().format(copy.getIncidentalAmount(OUTGOINGS)));
		setTextBoxValue(txtOutgoingsPercent,
				getPercentFormat().format(copy.getIncidentalPercent(OUTGOINGS)));
		setTextBoxValue(txtOutgoingsAccNo, copy.getIncidentalAccount(OUTGOINGS));
		setTextBoxValue(txtSignage,
				getDoubleFormat().format(copy.getIncidentalAmount(SIGNAGE)));
		setTextBoxValue(txtSignagePercent,
				getPercentFormat().format(copy.getIncidentalPercent(SIGNAGE)));
		setTextBoxValue(txtSignageAccNo, copy.getIncidentalAccount(SIGNAGE));
		setTextBoxValue(txtParking,
				getDoubleFormat().format(copy.getIncidentalAmount(PARKING)));
		setTextBoxValue(txtParkingPercent,
				getPercentFormat().format(copy.getIncidentalPercent(PARKING)));
		setTextBoxValue(txtParkingAccNo, copy.getIncidentalAccount(PARKING));
		setTextBoxValue(txtBondAmount,
				getDoubleFormat().format(copy.getBondAmount()));
		txtNotes.setValue(copy.getNotes());
		chkResidential.setValue(copy.isResidential());
		chkInactive.setValue(copy.isInactive());
		chkHasOption.setValue(copy.hasOption());
		txtBondNotes.setValue(copy.getBondNotes());
		cmbTenant.setVisible(true);
		txtTenant.setVisible(false);
		setTextBoxValue(txtJobNo, copy.getJobNo());
		setWidgetEnabled(txtTenant, false);
		initMetaData();
	}

	private void initNewLease() {
		setTextBoxValue(txtUnit, "DEFAULT UNIT");
		setTextBoxValue(txtArea, EMPTY_STRING);
		setTextBoxValue(txtTenant, "DEFAULT TENANT");
		Date now = new Date();
		setDateBoxValue(dtStart, now);
		setDateBoxValue(dtEnd, now);
		setDateBoxValue(dtUpdated, now);
		setTextBoxValue(txtRent, getDoubleFormat().format(ZERO_DBL));
		setTextBoxValue(txtRentPercent, getPercentFormat().format(ZERO_DBL));
		setTextBoxValue(txtRentAccNo, EMPTY_STRING);
		setTextBoxValue(txtOutgoings, getDoubleFormat().format(ZERO_DBL));
		setTextBoxValue(txtOutgoingsPercent, getPercentFormat()
				.format(ZERO_DBL));
		setTextBoxValue(txtOutgoingsAccNo, EMPTY_STRING);
		setTextBoxValue(txtSignage, getDoubleFormat().format(ZERO_DBL));
		setTextBoxValue(txtSignagePercent, getPercentFormat().format(ZERO_DBL));
		setTextBoxValue(txtSignageAccNo, EMPTY_STRING);
		setTextBoxValue(txtParking, getDoubleFormat().format(ZERO_DBL));
		setTextBoxValue(txtParkingPercent, getPercentFormat().format(ZERO_DBL));
		setTextBoxValue(txtParkingAccNo, EMPTY_STRING);
		setTextBoxValue(txtBondAmount, getDoubleFormat().format(ZERO_DBL));
		txtNotes.setValue(EMPTY_STRING);
		chkResidential.setValue(false);
		chkInactive.setValue(false);
		chkHasOption.setValue(copy.hasOption());
		txtBondNotes.setValue(EMPTY_STRING);
		cmbTenant.setVisible(false);
		txtTenant.setVisible(true);
		setWidgetEnabled(txtTenant, true);
		setWidgetEnabled(cmbTenant, false);
		setDefaults();
	}

	private void initMetaData() {
		mtdRent.setLeaseAndType(copy, HISTORICAL_RENT);
		mtdNotice.setLeaseAndType(copy, NOTICE);
		mtdSpecialCond.setLeaseAndType(copy, SPECIAL_CONDITION);
		mtdOption.setLeaseAndType(copy, OPTION);
	}

	@UiHandler("btnCancel")
	void onBtnCancelClick(final ClickEvent event) {
		this.hide(true);
	}

	@UiHandler("btnSave")
	void onBtnSaveClick(final ClickEvent event) {
		if (copy.getType() == existing) {
			if (!original.equals(copy)) {
				copy.setEdited(true);
				copy.setUpdateDate(new Date());
				handler.updated(copy);
			}
			this.hide(true);
		} else {
			persistNewLease();
		}
	}

	@UiHandler("btnUpdate")
	void onBtnUpdateClick(final ClickEvent event) {
		updateIncidental(RENT);
		updateIncidental(OUTGOINGS);
		updateIncidental(PARKING);
		updateIncidental(SIGNAGE);
		updateDates();
	}

	private void persistNewLease() {

		leaseService.saveNewLease(copy, new AsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long result) {
				copy.setId(result);
				handler.updated(copy);
				hide(true);
			}

			@Override
			public void onFailure(final Throwable caught) {
				logger.log(Level.SEVERE, "Unable to persist new lease.", caught);
			}
		});
	}

	private class NumericKeyPressHandler implements KeyPressHandler {

		@Override
		public void onKeyPress(final KeyPressEvent event) {
			if (!Character.isDigit(event.getCharCode())
					&& event.getCharCode() != '.'
					&& !isAllowableKeyPress(event)) {
				((TextBox) event.getSource()).cancelKey();
			}
		}

		private boolean isAllowableKeyPress(final KeyPressEvent event) {
			int keyCode = event.getNativeEvent().getKeyCode();
			return (keyCode == KEY_BACKSPACE || keyCode == KEY_DELETE || keyCode == KEY_TAB);

		}
	}

	private class NumericBlurHandler implements BlurHandler {

		@Override
		public void onBlur(final BlurEvent event) {
			TextBox textBox = (TextBox) event.getSource();
			double value = ZERO_DBL;
			if (textBox.equals(txtArea) || textBox.equals(txtRent)
					|| textBox.equals(txtOutgoings)
					|| textBox.equals(txtParking) || textBox.equals(txtSignage)
					|| textBox.equals(txtBondAmount)) {
				textBox.setValue(getDoubleFormat().format(
						Double.valueOf(textBox.getValue())));
				value = getDoubleFormat().parse(textBox.getValue());
			} else {
				textBox.setValue(getPercentFormat().format(
						Double.valueOf(textBox.getValue())));
				value = getPercentFormat().parse(textBox.getValue())
						/ ONE_HUNDRED_DBL;
			}
			setLeaseValue(textBox, value);
		}

		private void setLeaseValue(final TextBox textBox, final double value) {
			if (copy != null) {
				if (textBox.equals(txtRent)) {
					copy.setIncidentalAmount(RENT, value);
				} else if (textBox.equals(txtRentPercent)) {
					copy.setIncidentalPerecnt(RENT, value);
				} else if (textBox.equals(txtOutgoings)) {
					copy.setIncidentalAmount(OUTGOINGS, value);
				} else if (textBox.equals(txtOutgoingsPercent)) {
					copy.setIncidentalPerecnt(OUTGOINGS, value);
				} else if (textBox.equals(txtParking)) {
					copy.setIncidentalAmount(PARKING, value);
				} else if (textBox.equals(txtParkingPercent)) {
					copy.setIncidentalPerecnt(PARKING, value);
				} else if (textBox.equals(txtSignage)) {
					copy.setIncidentalAmount(SIGNAGE, value);
				} else if (textBox.equals(txtSignagePercent)) {
					copy.setIncidentalPerecnt(SIGNAGE, value);
				} else if (textBox.equals(txtBondAmount)) {
					copy.setBondAmount(value);
				}
			}
		}
	}

	private class TextBoxBlurHandler implements BlurHandler {

		@Override
		public void onBlur(final BlurEvent event) {
			TextBox textBox = (TextBox) event.getSource();
			if (textBox.equals(txtUnit)) {
				copy.setUnits(txtUnit.getValue());
			} else if (textBox.equals(txtArea)) {
				copy.setArea(txtArea.getValue());
			} else if (textBox.equals(txtTenant)) {
				copy.setTenant(textBox.getValue());
			} else if (textBox.equals(txtRentAccNo)) {
				copy.setIncidentalAccount(RENT, textBox.getValue());
			} else if (textBox.equals(txtOutgoingsAccNo)) {
				copy.setIncidentalAccount(OUTGOINGS, textBox.getValue());
			} else if (textBox.equals(txtParkingAccNo)) {
				copy.setIncidentalAccount(PARKING, textBox.getValue());
			} else if (textBox.equals(txtSignageAccNo)) {
				copy.setIncidentalAccount(SIGNAGE, textBox.getValue());
			} else if (textBox.equals(txtJobNo)) {
				copy.setJobNo(textBox.getValue());
			}
		}
	}

	private class TextAreaBlurHandler implements BlurHandler {

		@Override
		public void onBlur(final BlurEvent event) {
			TextArea textArea = (TextArea) event.getSource();
			if (textArea.equals(txtNotes)) {
				copy.setNotes(textArea.getText());
			} else if (textArea.equals(txtBondNotes)) {
				copy.setBondNotes(textArea.getValue());
			}
		}
	}

	private class BooleanValueChangeHandler implements
			ValueChangeHandler<Boolean> {

		@Override
		public void onValueChange(final ValueChangeEvent<Boolean> event) {
			if (chkResidential.equals(event.getSource())) {
				copy.setResidential(event.getValue());
			} else if (chkInactive.equals(event.getSource())) {
				copy.setInactive(event.getValue());
				disableInactiveWidgets(!event.getValue());
			} else if (chkHasOption.equals(event.getSource())) {
				copy.setHasOption(event.getValue());
				setOptionDates();
			}
		}
	}

	private class DateChangeHandler implements ValueChangeHandler<Date> {

		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {
			if (copy != null) {
				Date newDate = event.getValue();
				if (event.getSource().equals(dtStart)) {
					copy.setLeaseStart(newDate);
				} else if (event.getSource().equals(dtEnd)) {
					copy.setLeaseEnd(newDate);
				} else if (event.getSource().equals(dtOptionStart)) {
					copy.setOptionStart(newDate);
				} else if (event.getSource().equals(dtOptionEnd)) {
					copy.setOptionEnd(newDate);
				} else {
					copy.setUpdateDate(newDate);
				}
			}
		}
	}

	private class ComboChangeHandler implements ChangeHandler {

		@Override
		public void onChange(final ChangeEvent event) {
			ListBox source = (ListBox) event.getSource();
			String value = source.getItemText(source.getSelectedIndex());
			if (cmbTenant.equals(source)) {
				copy.setTenant(value);
			} else if (cmbPremises.equals(source)) {
				copy.setPremises(value);
			} else if (cmbCategory.equals(source)) {
				copy.setCategory(value);
			} else if (cmbBondType.equals(source)) {
				copy.setBondType(fromString(value));
			}
		}
	}

	private void disableInactiveWidgets(final boolean enabled) {
		cmbTenant.setEnabled(enabled);
		cmbPremises.setEnabled(enabled);
		txtUnit.setEnabled(enabled);
		txtArea.setEnabled(enabled);
		dtStart.setEnabled(enabled);
		dtEnd.setEnabled(enabled);
		cmbCategory.setEnabled(enabled);
		txtRent.setEnabled(enabled);
		txtRentPercent.setEnabled(enabled);
		txtRentAccNo.setEnabled(enabled);
		txtOutgoings.setEnabled(enabled);
		txtOutgoingsPercent.setEnabled(enabled);
		txtOutgoingsAccNo.setEnabled(enabled);
		txtParking.setEnabled(enabled);
		txtParkingPercent.setEnabled(enabled);
		txtParkingAccNo.setEnabled(enabled);
		txtSignage.setEnabled(enabled);
		txtSignagePercent.setEnabled(enabled);
		txtSignageAccNo.setEnabled(enabled);
		txtNotes.setEnabled(enabled);
		chkResidential.setEnabled(enabled);
		chkHasOption.setEnabled(enabled);
		txtJobNo.setEnabled(enabled);
		cmbBondType.setEnabled(enabled);
		txtBondAmount.setEnabled(enabled);
		txtBondNotes.setEnabled(enabled);
		setOptionDates();
	}

	private void setOptionDates() {
		boolean enabled = (chkHasOption.isEnabled()) ? chkHasOption.getValue()
				: false;
		dtOptionStart.setEnabled(enabled);
		dtOptionEnd.setEnabled(enabled);
		setDateBoxValue(dtOptionStart, resolveLeaseOptionStartDate());
		setDateBoxValue(dtOptionEnd, resolveLeaseOptionEndDate());
	}

	private Date resolveLeaseOptionStartDate() {
		if (copy.getOptionStart() == null) {
			if (copy.getLeaseEnd() == null) {
				return null;
			}
			return addDays(copy.getLeaseEnd(), ONE);
		}
		return copy.getOptionStart();
	}

	private Date resolveLeaseOptionEndDate() {
		if (copy.getOptionEnd() == null) {
			if (copy.getLeaseEnd() == null) {
				return null;
			}
			return addYears(copy.getLeaseEnd(), TWO);
		}
		return copy.getOptionEnd();
	}

	private void setTextBoxValue(final TextBox textBox, final String value) {
		textBox.setValue(value);
	}

	private void setDateBoxValue(final DateBox dateBox, final Date value) {
		if (value != null) {
			dateBox.setValue(value);
		} else {
			dateBox.setValue(new Date());
		}
	}

	private int getItemIndex(final ListBox listBox, final String value) {
		if (value != null && value.trim().length() != ZERO) {
			for (int i = ZERO; i < listBox.getItemCount(); i++) {
				if (value.equals(listBox.getItemText(i))) {
					return i;
				}
			}
		}
		return ZERO;
	}

	private void setDefaults() {
		copy.setLeaseStart(dtStart.getValue());
		copy.setLeaseEnd(dtEnd.getValue());
		copy.setUpdateDate(dtUpdated.getValue());
		copy.setTenant(txtTenant.getValue());
		copy.setPremises(cmbPremises.getItemText(cmbPremises.getSelectedIndex()));
		copy.setCategory(cmbCategory.getItemText(cmbCategory.getSelectedIndex()));
		copy.setUnits(txtUnit.getValue());
		copy.setType(newlease);
	}

	private void setWidgetEnabled(final FocusWidget widget,
			final boolean enabled) {
		widget.setEnabled(enabled);
	}

	private void updateDates() {
		Date newEndDate = addYears(copy.getLeaseEnd(), ONE);
		copy.setLeaseEnd(newEndDate);
		dtEnd.setValue(newEndDate);
	}

	private void updateIncidental(final String type) {
		double oldAmount = copy.getIncidentalAmount(type);
		double percent = copy.getIncidentalPercent(type);
		double newAmount = round(oldAmount * (ONE + percent));
		if (Math.abs(oldAmount - newAmount) > tolerance) {
			copy.setIncidentalAmount(type, newAmount);
			if (type.equals(RENT)) {
				txtRent.setValue(getDoubleFormat().format(newAmount));
			} else if (type.equals(OUTGOINGS)) {
				txtOutgoings.setValue(getDoubleFormat().format(newAmount));
			} else if (type.equals(PARKING)) {
				txtParking.setValue(getDoubleFormat().format(newAmount));
			} else if (type.equals(SIGNAGE)) {
				txtSignage.setValue(getDoubleFormat().format(newAmount));
			}
			copy.setEdited(true);
		}
	}

	protected void setLeaseCategories(final Set<GwtLeaseCategory> categories) {
		cmbCategory.clear();
		for (GwtLeaseCategory category : categories) {
			cmbCategory.addItem(category.getCategory());
		}
	}

	protected void setLeasePremises(final Set<GwtLeasePremises> premises) {
		cmbPremises.clear();
		for (GwtLeasePremises premise : premises) {
			cmbPremises.addItem(premise.getAddress());
		}
	}

	protected void setLeaseTenants(final Set<GwtLeaseTenant> tenants) {
		cmbTenant.clear();
		for (GwtLeaseTenant tenant : tenants) {
			cmbTenant.addItem(tenant.getName());
		}
	}
}