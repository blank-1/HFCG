package com.xt.cfp.core.Exception.code;

/**
 * 异常错误代码的接口
 */
public interface ErrorCode {

    /**
     * 异常代码
     * @return
     */
    public String getCode();

    /**
     * 异常描述
     * @return
     */
    public String getDesc();

}
