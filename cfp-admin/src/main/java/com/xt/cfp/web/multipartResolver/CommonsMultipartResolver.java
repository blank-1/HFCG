package com.xt.cfp.web.multipartResolver;

public class CommonsMultipartResolver extends org.springframework.web.multipart.commons.CommonsMultipartResolver{

	/**
	 * 此方法是控制Spring是否对用户的Request进行文件上传处理
	 */
    @Override
    public boolean isMultipart(javax.servlet.http.HttpServletRequest request) {
        
        String uri = request.getRequestURI();
        //过滤使用百度EmEditor的URI
        if (uri.indexOf("jsp/notice/imageUp") > 0) { 
            return false;
        }
        return super.isMultipart(request);
    }
	
}
