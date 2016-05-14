package com.hg.leases.model;

import static com.hg.leases.model.LeaseConstants.DEFAULT_ID;
import static com.hg.leases.model.LeaseConstants.TOTAL;
import static com.hg.leases.model.LeaseConstants.ZERO_DBL;
import static com.hg.leases.model.utilities.LeaseNumericUtilities.round;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.joda.time.LocalDate;

/**
 * @author hanleys
 */
@Entity
@Table(name = "LEASE")
public class Lease extends LeaseEntity implements Serializable,
		Comparable<Lease> {

	private static final long serialVersionUID = -1842654144394939498L;

	/** lease start date **/
	@Column(name = "LEASE_START")
	private Date leaseStart;

	/** lease end date **/
	@Column(name = "LEASE_END")
	private Date leaseEnd;

	/** last update to the lease record **/
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate;

	/** any pertinent notes on the lease record **/
	@Column(name = "NOTES")
	private String notes;

	/** leased area if applicable **/
	@Column(name = "AREA")
	private Double leasedArea;

	/** leased unit(s) **/
	@Column(name = "UNITS")
	private String leasedUnits;

	/** incidentals **/
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LEASE_TO_LEASE_INCIDENTAL_MAPPING", joinColumns = @JoinColumn(name = "LEASE_ID"), inverseJoinColumns = @JoinColumn(name = "LEASE_INCIDENTAL_ID"))
	@Fetch(value = FetchMode.SUBSELECT)
	private List<LeaseIncidental> incidentals;

	/** tenant **/
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LEASE_TO_TENANT_MAPPING", joinColumns = @JoinColumn(name = "LEASE_ID"), inverseJoinColumns = @JoinColumn(name = "TENANT_ID"))
	private LeaseTenant tenant;

	/** premises **/
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LEASE_TO_PREMISES_MAPPING", joinColumns = @JoinColumn(name = "LEASE_ID"), inverseJoinColumns = @JoinColumn(name = "PREMISES_ID"))
	private LeasePremises premises;

	/** category **/
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LEASE_TO_CATEGORY_MAPPING", joinColumns = @JoinColumn(name = "LEASE_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
	private LeaseCategory category;

	/** indicator for a residential (GST free) lease **/
	@Column(name = "RESIDENTIAL")
	private boolean residential;

	/** indicator for an inactive lease **/
	@Column(name = "INACTIVE")
	private boolean inactive;

	/** indicates if this option has an option to renew **/
	@Column(name = "HAS_OPTION")
	private boolean hasOption;

	/** lease option start date **/
	@Column(name = "OPTION_START_DATE")
	private Date optionStartDate;

	/** lease option end date **/
	@Column(name = "OPTION_END_DATE")
	private Date optionEndDate;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LEASE_TO_BOND_MAPPING", joinColumns = @JoinColumn(name = "LEASE_ID"), inverseJoinColumns = @JoinColumn(name = "BOND_ID"))
	private LeaseBond bond;

	/** meta data **/
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "LEASE_TO_META_DATA_MAPPING", joinColumns = @JoinColumn(name = "LEASE_ID"), inverseJoinColumns = @JoinColumn(name = "META_DATA_ID"))
	@Fetch(value = FetchMode.SUBSELECT)
	private List<LeaseMetaData> metaData;

	/** myob job number **/
	@Column(name = "JOB_NUMBER")
	private String jobNo;

	public Lease() {
		super();
	}

	public Lease(final LocalDate leaseStart, final LocalDate leaseEnd,
			final LocalDate lastUpdate, final String notes,
			final Double leasedArea, final String leasedUnits,
			final List<LeaseIncidental> incidentals, final LeaseTenant tenant,
			final LeasePremises premises, final LeaseCategory category,
			final List<LeaseMetaData> metaData, final boolean residential,
			final boolean inactive, final String jobNo) {
		this(DEFAULT_ID, leaseStart, leaseEnd, lastUpdate, notes, leasedArea,
				leasedUnits, incidentals, tenant, premises, category, metaData,
				residential, inactive, jobNo);
	}

	protected Lease(final Long id, final LocalDate leaseStart,
			final LocalDate leaseEnd, final LocalDate lastUpdate,
			final String notes, final Double leasedArea,
			final String leasedUnits, final List<LeaseIncidental> incidentals,
			final LeaseTenant tenant, final LeasePremises premises,
			final LeaseCategory category, final List<LeaseMetaData> metaData,
			final boolean residential, final boolean inactive,
			final String jobNo) {
		super(id);
		this.leaseStart = leaseStart.toDate();
		this.leaseEnd = leaseEnd.toDate();
		if (lastUpdate != null) {
			this.lastUpdate = lastUpdate.toDate();
		}
		this.notes = notes;
		this.leasedArea = leasedArea;
		this.leasedUnits = leasedUnits;
		this.incidentals = incidentals;
		this.tenant = tenant;
		this.premises = premises;
		this.category = category;
		this.metaData = metaData;
		this.residential = residential;
		this.inactive = inactive;
		this.jobNo = jobNo;
	}

	public final LocalDate getLeaseStart() {
		return LocalDate.fromDateFields(leaseStart);
	}

	public void setLeaseStart(final LocalDate leaseStart) {
		this.leaseStart = leaseStart.toDate();
	}

	public final LocalDate getLeaseEnd() {
		return LocalDate.fromDateFields(leaseEnd);
	}

	public void setLeaseEnd(final LocalDate leaseEnd) {
		this.leaseEnd = leaseEnd.toDate();
	}

	public LocalDate getLastUpdate() {
		if (lastUpdate != null) {
			return LocalDate.fromDateFields(lastUpdate);
		}
		return null;
	}

	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate.toDate();
	}

	public LeaseTenant getTenant() {
		return tenant;
	}

	public void setTenant(LeaseTenant tenant) {
		this.tenant = tenant;
	}

	public LeasePremises getPremises() {
		return premises;
	}

	public void setPremises(LeasePremises premises) {
		this.premises = premises;
	}

	public LeaseCategory getCategory() {
		return category;
	}

	public void setCategory(LeaseCategory category) {
		this.category = category;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Double getLeasedArea() {
		return leasedArea;
	}

	public void setLeasedArea(Double leasedArea) {
		this.leasedArea = leasedArea;
	}

	public String getLeasedUnits() {
		return leasedUnits;
	}

	public void setLeasedUnits(String leasedUnits) {
		this.leasedUnits = leasedUnits;
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

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public boolean hasOption() {
		return hasOption;
	}

	public void setHasOption(boolean hasOption) {
		this.hasOption = hasOption;
	}

	public final LocalDate getOptionStartDate() {
		if (optionStartDate != null) {
			return LocalDate.fromDateFields(optionStartDate);
		}
		return null;
	}

	public void setOptionStartDate(final LocalDate optionStartDate) {
		this.optionStartDate = optionStartDate.toDate();
	}

	public final LocalDate getOptionEndDate() {
		if (optionEndDate != null) {
			return LocalDate.fromDateFields(optionEndDate);
		}
		return null;
	}

	public void setOptionEndDate(final LocalDate optionEndDate) {
		this.optionEndDate = optionEndDate.toDate();
	}

	public LeaseBond getBond() {
		return bond;
	}

	public void setBond(LeaseBond bond) {
		this.bond = bond;
	}

	public void addIncidental(final LeaseIncidental incidental) {
		if (isEmpty(incidentals)) {
			incidentals = new ArrayList<LeaseIncidental>();
		}
		incidentals.add(incidental);
	}

	public final List<LeaseIncidental> getIncidentals() {
		return incidentals;
	}

	public void setIncidentals(final List<LeaseIncidental> incidentals) {
		this.incidentals = incidentals;
	}

	public final LeaseIncidental getIncidental(final String name) {
		if (!isEmpty(incidentals)) {
			for (LeaseIncidental incidental : incidentals) {
				if (name.equals(incidental.getName())) {
					return incidental;
				}
			}
		}
		return null;
	}

	public void removeIncidental(final String name) {
		if (!isEmpty(incidentals)) {
			LeaseIncidental incidental = getIncidental(name);
			if (incidental != null) {
				incidentals.remove(incidental);
			}
		}
	}

	public List<LeaseMetaData> getMetaData() {
		return metaData;
	}

	public void setMetaData(List<LeaseMetaData> metaData) {
		this.metaData = metaData;
	}

	public void addMetaData(final LeaseMetaData data) {
		if (isEmpty(metaData)) {
			metaData = new ArrayList<LeaseMetaData>();
		}
		metaData.add(data);
	}

	public final List<LeaseMetaData> getMetaData(final String type) {
		List<LeaseMetaData> data = new ArrayList<LeaseMetaData>();
		if (!isEmpty(metaData)) {
			for (LeaseMetaData md : metaData) {
				if (type.equals(md.getType())) {
					data.add(md);
				}
			}
		}
		return data;
	}

	public void removeMetaData(final LeaseMetaData data) {
		if (!isEmpty(metaData)) {
			metaData.remove(data);
		}
	}

	@Override
	public String toString() {
		return "Lease [tenant=" + tenant.getName() + ", premises="
				+ premises.getAddressLineOne() + ", category="
				+ category.getCategory() + "]";
	}

	@Override
	public int compareTo(final Lease lease) {
		return leaseEnd.compareTo(lease.leaseEnd);
	}

	public double monthlyAmount(final String type) {
		double monthlyAmount = ZERO_DBL;
		if (TOTAL.equals(type)) {
			for (LeaseIncidental incidental : getIncidentals()) {
				monthlyAmount += incidental.monthlyAmount();
			}
		} else {
			if (getIncidental(type) != null) {
				monthlyAmount = getIncidental(type).monthlyAmount();
			}

		}
		return monthlyAmount;
	}

	public double monthlyGst(final String type) {
		double monthlyGst = ZERO_DBL;
		if (!residential) {
			if (TOTAL.equals(type)) {
				for (LeaseIncidental incidental : getIncidentals()) {
					monthlyGst += incidental.monthlyGst();
				}
			} else {
				if (getIncidental(type) != null) {
					monthlyGst = getIncidental(type).monthlyGst();
				}
			}
		}
		return monthlyGst;
	}

	public double monthlyTotal(final String type) {
		return round(monthlyAmount(type) + monthlyGst(type));
	}

	public double yearlyAmount(final String type) {
		double yearlyAmount = ZERO_DBL;
		if (TOTAL.equals(type)) {
			for (LeaseIncidental incidental : getIncidentals()) {
				yearlyAmount += incidental.yearlyAmount();
			}
		} else {
			if (getIncidental(type) != null) {
				yearlyAmount = getIncidental(type).yearlyAmount();
			}

		}
		return yearlyAmount;
	}

	public double yearlyGst(final String type) {
		double yearlyGst = ZERO_DBL;
		if (!residential) {
			if (TOTAL.equals(type)) {
				for (LeaseIncidental incidental : getIncidentals()) {
					yearlyGst += incidental.yearlyGst();
				}
			} else {
				if (getIncidental(type) != null) {
					yearlyGst = getIncidental(type).yearlyGst();
				}
			}
		}
		return yearlyGst;
	}

	public double yearlyTotal(final String type) {
		return round(yearlyAmount(type) + yearlyGst(type));
	}

	@Override
	public String key() {
		return String.valueOf(getId());
	}
}