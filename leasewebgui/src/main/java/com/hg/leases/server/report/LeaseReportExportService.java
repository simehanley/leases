package com.hg.leases.server.report;

import static com.hg.leases.client.LeaseClientConstants.REPORT_TYPE;
import static com.hg.leases.shared.GwtLeaseReportType.valueOf;
import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hg.leases.shared.GwtLeaseReportType;

public class LeaseReportExportService extends HttpServlet {

	private static final long serialVersionUID = -1653470128281094815L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		GwtLeaseReportType type = valueOf(request.getParameter(REPORT_TYPE));
		Map<GwtLeaseReportType, LeaseReportGenerator> generators = (Map<GwtLeaseReportType, LeaseReportGenerator>) getWebApplicationContext(
				getServletContext()).getBean("leaseReportGenerators");
		if (generators.containsKey(type)) {
			generators.get(type).generateReport(request, response);
		}
	}
}