package com.hg.leases.server.report.myob;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.hg.leases.server.report.AbstractReportGenerator;

public class AbstractMyobReportGenerator extends AbstractReportGenerator {

	protected OutputStream createMyobResponseHeader(final String filename,
			final HttpServletResponse response) throws IOException {
		OutputStream stream = response.getOutputStream();
		response.setContentType("text/plain");
		response.addHeader("Content-Type", "text/plain");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ filename);
		return stream;
	}
}