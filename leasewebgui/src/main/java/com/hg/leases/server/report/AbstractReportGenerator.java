package com.hg.leases.server.report;

import static com.hg.leases.model.LeaseConstants.EMPTY_STRING;
import static org.springframework.util.StringUtils.hasLength;

import org.springframework.beans.factory.annotation.Autowired;

import com.hg.leases.dao.LeaseDao;

public abstract class AbstractReportGenerator {

	@Autowired
	protected LeaseDao dao;

	@Autowired
	protected LeaseCurrentPeriodGenerator generator;

	protected String getStringValue(final String value) {
		if (hasLength(value)) {
			return value;
		}
		return EMPTY_STRING;
	}
}