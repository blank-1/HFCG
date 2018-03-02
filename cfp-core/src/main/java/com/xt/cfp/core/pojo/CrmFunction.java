package com.xt.cfp.core.pojo;

public class CrmFunction {
    private Long funId;

    private String funCode;

    private String funName;

    private Long pFunId;

    private String funType;

    private String url;

    public Long getFunId() {
        return funId;
    }

    public void setFunId(Long funId) {
        this.funId = funId;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode == null ? null : funCode.trim();
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName == null ? null : funName.trim();
    }

    public Long getpFunId() {
        return pFunId;
    }

    public void setpFunId(Long pFunId) {
        this.pFunId = pFunId;
    }

    public String getFunType() {
        return funType;
    }

    public void setFunType(String funType) {
        this.funType = funType == null ? null : funType.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}