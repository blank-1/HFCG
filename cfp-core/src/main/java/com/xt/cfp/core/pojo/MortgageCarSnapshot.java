package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抵押车信息
 */
public class MortgageCarSnapshot {
    private Long mortgageCarId;//抵押车ID 
    private Long carLoanId;//车贷ID
    private String arrived;//几抵
    private String automobileBrand;//汽车品牌
    private String carModel;//汽车型号
    private BigDecimal marketPrice;//市场价格
    private String frameNumber;//车架号
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后
    private String state;//状态
    private String changeDesc;//变更信息
    
	/**
     * 状态（0.可用；1.不可用）
     */
    public static final String STATE_ENABLE = "0";
    public static final String STATE_DISABLE = "1";

    public Long getMortgageCarId() {
        return mortgageCarId;
    }

    public void setMortgageCarId(Long mortgageCarId) {
        this.mortgageCarId = mortgageCarId;
    }

    public Long getCarLoanId() {
        return carLoanId;
    }

    public void setCarLoanId(Long carLoanId) {
        this.carLoanId = carLoanId;
    }

    public String getArrived() {
        return arrived;
    }

    public void setArrived(String arrived) {
        this.arrived = arrived == null ? null : arrived.trim();
    }

    public String getAutomobileBrand() {
        return automobileBrand;
    }

    public void setAutomobileBrand(String automobileBrand) {
        this.automobileBrand = automobileBrand == null ? null : automobileBrand.trim();
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel == null ? null : carModel.trim();
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber == null ? null : frameNumber.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getChangeDesc() {
		return changeDesc;
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}
    
}