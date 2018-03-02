package com.xt.cfp.core.pojo;

public class SalesFunctionInfo {
    private Long functionId;

    private String functionCode;

    private String functionName;

    private String functionDesc;

    private Long pFunctionId;

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode == null ? null : functionCode.trim();
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName == null ? null : functionName.trim();
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public void setFunctionDesc(String functionDesc) {
        this.functionDesc = functionDesc == null ? null : functionDesc.trim();
    }

    public Long getpFunctionId() {
        return pFunctionId;
    }

    public void setpFunctionId(Long pFunctionId) {
        this.pFunctionId = pFunctionId;
    }
}