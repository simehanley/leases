package com.hg.leases.excel;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author hanleys
 */
public class LeaseExcelMain {

	private static final Logger log = Logger.getLogger(LeaseExcelMain.class);

	private static final String SPRING_CONTEXT_NAME = "leases-excel-context.xml";

	public static void main(String[] args) {
		try {
			new ClassPathXmlApplicationContext(SPRING_CONTEXT_NAME);
		} catch (Throwable t) {
			log.error("Error running excel main.", t);
		}
	}
}