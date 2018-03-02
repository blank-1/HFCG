package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

public class ConstantDefine {
    private long constantDefineId;
    private String constantType;
    private String constantName;
    private String constantValue;
    private String constantTypeCode;
    private long parentConstant;
    private String constantStatus;

    //控制出借平台费用的显示
    private BigDecimal workflowRatio;


    public String getConstantStatus() {
        return constantStatus;
    }

    public void setConstantStatus(String constantStatus) {
        this.constantStatus = constantStatus;
    }

    public BigDecimal getWorkflowRatio() {
        return workflowRatio;
    }

    public void setWorkflowRatio(BigDecimal workflowRatio) {
        this.workflowRatio = workflowRatio;
    }

    public Long getConstantDefineId() {
        return constantDefineId;
    }

    public void setConstantDefineId(Long constantDefineId) {
        this.constantDefineId = constantDefineId;
    }

    public String getConstantType() {
        return constantType;
    }

    public void setConstantType(String constantType) {
        this.constantType = constantType;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public String getConstantTypeCode() {
        return constantTypeCode;
    }

    public void setConstantTypeCode(String constantTypeCode) {
        this.constantTypeCode = constantTypeCode;
    }

    public long getParentConstant() {
        return parentConstant;
    }

    public void setParentConstant(long parentConstant) {
        this.parentConstant = parentConstant;
    }
}
