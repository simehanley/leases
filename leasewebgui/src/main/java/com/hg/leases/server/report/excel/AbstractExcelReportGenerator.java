package com.hg.leases.server.report.excel;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.hg.leases.server.report.AbstractReportGenerator;

public abstract class AbstractExcelReportGenerator extends
		AbstractReportGenerator {

	protected OutputStream createExcelResponseHeader(final String filename,
			final HttpServletResponse response) throws IOException {
		OutputStream stream = response.getOutputStream();
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ filename);
		return stream;
	}
}