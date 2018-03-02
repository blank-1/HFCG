package com.xt.cfp.core.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {

	private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>() {  
        protected synchronized HttpServletRequest initialValue() {  
            return null;  
        }  
    };  
    
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>() {  
        protected synchronized HttpServletResponse initialValue() {  
            return null;  
        }  
    }; 
    
    private static ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<String>() {  
        protected synchronized String initialValue() {  
            return null;  
        }  
    }; 
    
    public static void setHttpServletRequest(HttpServletRequest request) {
    	requestThreadLocal.set(request);
    }
    
    public static void setHttpServletResponse(HttpServletResponse response) {
    	responseThreadLocal.set(response);
    }
    
    public static HttpServletRequest getHttpServletRequest() {
    	return requestThreadLocal.get();
    }
    
    public static HttpServletResponse getHttpServletResponse() {
    	return responseThreadLocal.get();
    }
    
    public static void setRequestId(String id) {
    	requestIdThreadLocal.set(id);
    }
    
    public static String getRequestId() {
    	return requestIdThreadLocal.get();
    }
}
