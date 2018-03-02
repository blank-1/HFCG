package com.xt.cfp.core.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * trim参数过滤器
 * Created by luqinglin on 2015/6/15.
 */
public class ParameterFilter implements Filter{


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //trim所有的请求参数
        Map<String,String[]> params = new HashMap(servletRequest.getParameterMap());
        Set<String> keyset = params.keySet();
        for(String key:keyset){
            String[] values = params.get(key);
            if (values == null || values.length == 0) continue;
            String[]_values = new String[values.length];
            for (int i=0;i<values.length;i++)
                _values[i] = values[i].trim();
            params.put(key, _values);
        }
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        ParameterRequestWrapper wrapRequest=new ParameterRequestWrapper(req,params);
        servletRequest=wrapRequest;
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
