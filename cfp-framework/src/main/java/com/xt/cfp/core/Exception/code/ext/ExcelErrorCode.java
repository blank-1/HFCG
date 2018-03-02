package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * Created by luqinglin on 2015/6/12.
 */
public enum ExcelErrorCode implements ErrorCode {
    ANALYSIS_FAILE("0", "excel解析失败"),

    ;

    private final String code;
    private final String desc;

    private ExcelErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
