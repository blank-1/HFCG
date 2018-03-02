package com.xt.cfp.core.service.pay;

/**
 * 模拟请求
 *
 * Created by luqinglin on 2015/6/18.
 */
public class Request {

    /**
     * 请求地址
     */
    private String reqUrl;

    /**
     * 请求参数

     */
    private RequestParameter requestParameter;

    public Request(String reqUrl, RequestParameter requestParameter) {

        this.reqUrl = reqUrl;
        this.requestParameter = requestParameter;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public RequestParameter getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(RequestParameter requestParameter) {
        this.requestParameter = requestParameter;
    }

}
