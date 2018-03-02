package com.xt.cfp.core.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ThreadLocalFilter implements Filter {
	private FilterConfig config = null;

	// Filter被释放时的回调方法
	public void destroy() {

	}

	// FilterConfig接口实例中封装了个Filter的初始化参数
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}

	// 过滤方法

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		WebUtil.setHttpServletRequest(req);
		WebUtil.setHttpServletResponse(resp);
		WebUtil.setRequestId(Util.uuid());
		chain.doFilter(req, resp);
	}

}
