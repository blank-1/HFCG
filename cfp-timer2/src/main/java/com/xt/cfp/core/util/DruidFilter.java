package com.xt.cfp.core.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DruidFilter implements Filter {
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
        HttpServletResponse res = (HttpServletResponse) response;
        if (!authorize(req)) {
            res.sendRedirect("/");
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * 判断用户是否有权限访问
     */
    private boolean authorize(HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("USER");
        if (user != null) {
            return true;
        }

        return false;
    }

}
