package com.hg.leases.server.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LeaseReportGenerator {

	void generateReport(HttpServletRequest request, HttpServletResponse response);
}
