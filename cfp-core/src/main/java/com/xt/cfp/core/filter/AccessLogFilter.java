package com.xt.cfp.core.filter;

import com.xt.cfp.core.util.AccessLogUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessLogFilter extends BaseFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        AccessLogUtil accessLog = new AccessLogUtil(request);
        Exception exception = null;
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException | RuntimeException e) {
            exception = e;
            throw e;
        } finally {
            accessLog.end(response, exception);
        }
    }
}
