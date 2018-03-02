package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户房产抵押信息快照
 */
public class CustomerHouseSnapshot {
    private Long snapshotId;//客户快照ID
    private Long houseAddr;//房产地址
    private Long loanApplicationId;//借款申请ID
    private String mortgageType;//房产抵押类型
    private String houseType;//房产类型
    private BigDecimal houseSize;//房屋面积
    private Date buyDate;//购买日期
    private BigDecimal buyValue;//购买价格
    private BigDecimal marketValue;//市场价格
    private BigDecimal assessValue;//评估价格
    private String desc;//备注
    private String status;//状态
    private Long mainLoanApplicationId;//借款申请主表ID
    private String houseCardNumber;//房产证字号

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getHouseAddr() {
        return houseAddr;
    }

    public void setHouseAddr(Long houseAddr) {
        this.houseAddr = houseAddr;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType == null ? null : mortgageType.trim();
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType == null ? null : houseType.trim();
    }

    public BigDecimal getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(BigDecimal houseSize) {
        this.houseSize = houseSize;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public BigDecimal getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(BigDecimal buyValue) {
        this.buyValue = buyValue;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getAssessValue() {
        return assessValue;
    }

    public void setAssessValue(BigDecimal assessValue) {
        this.assessValue = assessValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}

	public String getHouseCardNumber() {
		return houseCardNumber;
	}

	public void setHouseCardNumber(String houseCardNumber) {
		this.houseCardNumber = houseCardNumber;
	}
}