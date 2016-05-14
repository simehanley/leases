package com.hg.leases.shared;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static com.hg.leases.model.LeaseConstants.GST_PERCENTAGE;
import static com.hg.leases.model.LeaseConstants.MONTHS_PER_YEAR;
import static com.hg.leases.model.LeaseConstants.OUTGOINGS;
import static com.hg.leases.model.LeaseConstants.PARKING;
import static com.hg.leases.model.LeaseConstants.RENT;
import static com.hg.leases.model.LeaseConstants.SIGNAGE;
import static com.hg.leases.model.LeaseConstants.TOTAL;
import static com.hg.leases.model.LeaseConstants.ZERO;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.shared.GwtLeaseBondType.deposit_held;
import static com.hg.leases.shared.GwtLeaseUtilities.areEqual;
import static com.hg.leases.shared.GwtLeaseUtilities.round;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtLease implements IsSerializable {

	private Long id;

	private String tenant;

	private String premises;

	private String units;

	private String area;

	private Date leaseStart;

	private Date leaseEnd;

	private Date updateDate;

	private boolean hasOption = false;

	private Date optionStart;

	private Date optionEnd;

	private String category;

	private String notes;

	private boolean residential = false;

	private boolean inactive = false;

	private boolean edited = false;

	private GwtLeaseType type;

	private GwtLeaseBondType bondType = deposit_held;

	private double bondAmount = ZERO_DBL;

	private String bondNotes = EMPTY_STRING;

	private List<GwtLeaseIncidental> incidentals = new ArrayList<GwtLeaseIncidental>();

	private List<GwtLeaseMetaData> metaData = new ArrayList<GwtLeaseMetaData>();

	private String jobNo;

	public GwtLease() {
		super();
	}

	public GwtLease(Long id, String tenant, String premises, String units,
			String area, Date leaseStart, Date leaseEnd, Date updateDate,
			String category, String notes, boolean residential,
			boolean inactive, boolean hasOption, Date optionStart,
			Date optionEnd, GwtLeaseType type, GwtLeaseBondType bondType,
			double bondAmount, String bondNotes, String jobNo) {
		super();
		this.id = id;
		this.tenant = tenant;
		this.premises = premises;
		this.units = units;
		this.area = area;
		this.leaseStart = leaseStart;
		this.leaseEnd = leaseEnd;
		this.updateDate = updateDate;
		this.category = category;
		this.notes = notes;
		this.residential = residential;
		this.inactive = inactive;
		this.hasOption = hasOption;
		this.optionStart = optionStart;
		this.optionEnd = optionEnd;
		this.type = type;
		this.bondType = bondType;
		this.bondAmount = bondAmount;
		this.bondNotes = bondNotes;
		this.jobNo = jobNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLeaseStart() {
		return leaseStart;
	}

	public void setLeaseStart(Date leaseStart) {
		this.leaseStart = leaseStart;
	}

	public Date getLeaseEnd() {
		return leaseEnd;
	}

	public void setLeaseEnd(Date leaseEnd) {
		this.leaseEnd = leaseEnd;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getPremises() {
		return premises;
	}

	public void setPremises(String premises) {
		this.premises = premises;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<GwtLeaseIncidental> getIncidentals() {
		return incidentals;
	}

	public GwtLeaseIncidental getIncidental(String type) {
		List<GwtLeaseIncidental> incidentals = getIncidentals();
		if (incidentals.size() > ZERO) {
			for (GwtLeaseIncidental inc : incidentals) {
				if (type.equals(inc.getName())) {
					return inc;
				}
			}
		}
		return null;
	}

	public double getIncidentalAmount(String type) {
		GwtLeaseIncidental inc = getIncidental(type);
		return (inc == null) ? ZERO_DBL : inc.getAmount();
	}

	public double getIncidentalPercent(String type) {
		GwtLeaseIncidental inc = getIncidental(type);
		return (inc == null) ? ZERO_DBL : inc.getPercentage();
	}

	public String getIncidentalAccount(String type) {
		GwtLeaseIncidental inc = getIncidental(type);
		return (inc == null) ? EMPTY_STRING : inc.getAccount();
	}

	public void setIncidentals(List<GwtLeaseIncidental> incidentals) {
		this.incidentals = incidentals;
	}

	public void setIncidentalAmount(String type, double amount) {
		GwtLeaseIncidental inc = getIncidental(type);
		if (inc == null) {
			inc = new GwtLeaseIncidental(type, ZERO_DBL, ZERO_DBL, EMPTY_STRING);
			incidentals.add(inc);
		}
		inc.setAmount(amount);
	}

	public void setIncidentalPerecnt(String type, double percent) {
		GwtLeaseIncidental inc = getIncidental(type);
		if (inc == null) {
			inc = new GwtLeaseIncidental(type, ZERO_DBL, ZERO_DBL, EMPTY_STRING);
			incidentals.add(inc);
		}
		inc.setPercentage(percent);
	}

	public void setIncidentalAccount(String type, String account) {
		GwtLeaseIncidental inc = getIncidental(type);
		if (inc == null) {
			inc = new GwtLeaseIncidental(type, ZERO_DBL, ZERO_DBL, EMPTY_STRING);
			incidentals.add(inc);
		}
		inc.setAccount(account);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isResidential() {
		return residential;
	}

	public void setResidential(boolean residential) {
		this.residential = residential;
	}

	public boolean isInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public boolean hasOption() {
		return hasOption;
	}

	public void setHasOption(boolean hasOption) {
		this.hasOption = hasOption;
	}

	public Date getOptionStart() {
		return optionStart;
	}

	public void setOptionStart(Date optionStart) {
		this.optionStart = optionStart;
	}

	public Date getOptionEnd() {
		return optionEnd;
	}

	public void setOptionEnd(Date optionEnd) {
		this.optionEnd = optionEnd;
	}

	public GwtLeaseBondType getBondType() {
		return bondType;
	}

	public void setBondType(GwtLeaseBondType bondType) {
		this.bondType = bondType;
	}

	public double getBondAmount() {
		return bondAmount;
	}

	public void setBondAmount(double bondAmount) {
		this.bondAmount = bondAmount;
	}

	public String getBondNotes() {
		return bondNotes;
	}

	public void setBondNotes(String bondNotes) {
		this.bondNotes = bondNotes;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	public GwtLeaseType getType() {
		return type;
	}

	public void setType(GwtLeaseType type) {
		this.type = type;
	}

	public double monthlyAmount(final String type) {
		return round(getAmount(type) / MONTHS_PER_YEAR);
	}

	public double monthlyGst(final String type) {
		if (residential) {
			return ZERO_DBL;
		}
		return round(GST_PERCENTAGE * monthlyAmount(type));
	}

	public double monthlyTotal(final String type) {
		return round(monthlyAmount(type) + monthlyGst(type));
	}

	public double yearlyAmount(final String type) {
		return round(getAmount(type));
	}

	public double yearlyGst(final String type) {
		if (residential) {
			return ZERO_DBL;
		}
		return round(GST_PERCENTAGE * getAmount(type));
	}

	public double yearlyTotal(final String type) {
		return round(yearlyAmount(type) + yearlyGst(type));
	}

	private double getAmount(final String type) {
		if (TOTAL.equals(type)) {
			return getIncidentalAmount(RENT) + getIncidentalAmount(OUTGOINGS)
					+ getIncidentalAmount(SIGNAGE)
					+ getIncidentalAmount(PARKING);
		}
		return getIncidentalAmount(type);
	}

	public List<GwtLeaseMetaData> getMetaData() {
		return metaData;
	}

	public List<GwtLeaseMetaData> getMetaData(final String type) {
		List<GwtLeaseMetaData> metaDataByType = new ArrayList<GwtLeaseMetaData>();
		for (GwtLeaseMetaData md : metaData) {
			if (type.equals(md.getType())) {
				metaDataByType.add(md);
			}
		}
		return metaDataByType;
	}

	public void setMetaData(List<GwtLeaseMetaData> metaData) {
		this.metaData = metaData;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public GwtLease clone() {
		GwtLease clone = new GwtLease(id, tenant, premises, units, area,
				leaseStart, leaseEnd, updateDate, category, notes, residential,
				inactive, hasOption, optionStart, optionEnd, type, bondType,
				bondAmount, bondNotes, jobNo);
		clone.setIncidentals(cloneIncidentals());
		clone.setMetaData(cloneMetaData());
		return clone;
	}

	private List<GwtLeaseIncidental> cloneIncidentals() {
		List<GwtLeaseIncidental> clone = new ArrayList<GwtLeaseIncidental>();
		for (GwtLeaseIncidental inc : incidentals) {
			clone.add(inc.clone());
		}
		return clone;
	}

	private List<GwtLeaseMetaData> cloneMetaData() {
		List<GwtLeaseMetaData> clone = new ArrayList<GwtLeaseMetaData>();
		for (GwtLeaseMetaData md : metaData) {
			clone.add(md.clone());
		}
		return clone;
	}

	public boolean equals(final GwtLease lease) {
		if (!equalObject(tenant, lease.getTenant())) {
			return false;
		}
		if (!equalObject(premises, lease.getPremises())) {
			return false;
		}
		if (!equalObject(category, lease.getCategory())) {
			return false;
		}
		if (!equalObject(units, lease.getUnits())) {
			return false;
		}
		if (!equalObject(area, lease.getArea())) {
			return false;
		}
		if (!equalObject(leaseStart, lease.getLeaseStart())) {
			return false;
		}
		if (!equalObject(leaseEnd, lease.getLeaseEnd())) {
			return false;
		}
		if (!equalObject(updateDate, lease.getUpdateDate())) {
			return false;
		}
		if (!equalObject(notes, lease.getNotes())) {
			return false;
		}
		if (!incidentals.equals(lease.getIncidentals())) {
			return false;
		}
		if (inactive != lease.isInactive()) {
			return false;
		}
		if (residential != lease.isResidential()) {
			return false;
		}
		if (hasOption != lease.hasOption()) {
			return false;
		}
		if (!equalObject(optionStart, lease.getOptionStart())) {
			return false;
		}
		if (!equalObject(optionEnd, lease.getOptionEnd())) {
			return false;
		}
		if (!equalObject(bondType, lease.getBondType())) {
			return false;
		}
		if (!areEqual(bondAmount, lease.getBondAmount())) {
			return false;
		}
		if (!equalObject(bondNotes, lease.getBondNotes())) {
			return false;
		}
		if (!metaData.equals(lease.getMetaData())) {
			return false;
		}
		if (!equalObject(jobNo, lease.getJobNo())) {
			return false;
		}
		return true;
	}

	private boolean equalObject(final Object one, final Object two) {
		if (one == null && two == null) {
			return true;
		} else if (one == null && two != null) {
			return false;
		} else if (one != null && two == null) {
			return false;
		} else if (one == two) {
			return true;
		}
		return one.equals(two);
	}
}