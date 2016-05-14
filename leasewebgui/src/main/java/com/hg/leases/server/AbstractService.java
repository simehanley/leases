package com.hg.leases.server;

import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class AbstractService extends RemoteServiceServlet {

	private static final long serialVersionUID = 8124846124209924800L;

	protected <T> T getSpringBean(final Class<T> clazz) {
		return getWebApplicationContext(getServletContext()).getBean(clazz);
	}
}
