package com.xt.cfp.core.pojo;

import com.external.deposites.exception.UnimplementException;
import com.xt.cfp.core.constants.HFOperationEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

public class CapitalFlow {
    private Long flowId;

    private Long scheduleId;

    private Integer operationType;

    private Long fromUser;

    private Long toUser;

    private BigDecimal amount;

    private Date startTime;

    private Date endTime;

    private Long businessId;

    private String businessFlow;

    private Integer result;
    private String message;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        if (HFOperationEnum.typeOf(operationType) == null) {
            throw new UnimplementException("未实现的类型");
        }
        this.operationType = operationType;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public Long getToUser() {
        return toUser;
    }

    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessFlow() {
        return businessFlow;
    }

    public void setBusinessFlow(String businessFlow) {
        this.businessFlow = businessFlow == null ? null : businessFlow.trim();
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CapitalFlow{" +
                "flowId=" + flowId +
                ", scheduleId=" + scheduleId +
                ", operationType=" + operationType +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", amount=" + amount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", businessId=" + businessId +
                ", businessFlow='" + businessFlow + '\'' +
                ", result=" + result +
                ", message='" + message + '\'' +
                '}';
    }
}