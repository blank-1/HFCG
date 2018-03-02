package com.xt.cfp.core.pojo;


import java.math.BigDecimal;
import java.util.Date;

public class CustomerCarSnapshot {
    private Long snapshotId;
    private Long loanApplicationId;
    private String carModel;
    private BigDecimal carMoney;
    private BigDecimal mileage;

    private BigDecimal appraisal;
    private Date buyTime;
    private Integer pledgeType;
    private Long mainLoanApplicationId;
    private BigDecimal originalPrice;



    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel == null ? null : carModel.trim();
    }

    public BigDecimal getCarMoney() {
        return carMoney;
    }

    public void setCarMoney(BigDecimal carMoney) {
        this.carMoney = carMoney;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(BigDecimal appraisal) {
        this.appraisal = appraisal;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Integer getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(Integer pledgeType) {
        this.pledgeType = pledgeType;
    }

    public Long getMainLoanApplicationId() {
        return mainLoanApplicationId;
    }

    public void setMainLoanApplicationId(Long mainLoanApplicationId) {
        this.mainLoanApplicationId = mainLoanApplicationId;
    }


}