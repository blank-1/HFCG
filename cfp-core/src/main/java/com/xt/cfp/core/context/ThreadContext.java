package com.xt.cfp.core.context;

import javax.servlet.http.HttpServletResponse;

public class ThreadContext {
	
	public static ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();
	
}
