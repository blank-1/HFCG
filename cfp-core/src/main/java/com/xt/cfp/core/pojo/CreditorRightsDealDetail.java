package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class CreditorRightsDealDetail {

    private Long rightsTransferDetailId;

    private Long creditorRightsApplyId;

    private Long lendOrderId;
    
    private Long carryUserId;

    private BigDecimal buyBalance;
    
    private BigDecimal buyWorth;

    private String status;

    private Date buyDate;

    private Long carryCrId;

    public Long getRightsTransferDetailId() {
        return rightsTransferDetailId;
    }

    public void setRightsTransferDetailId(Long rightsTransferDetailId) {
        this.rightsTransferDetailId = rightsTransferDetailId;
    }

    public Long getCreditorRightsApplyId() {
        return creditorRightsApplyId;
    }

    public void setCreditorRightsApplyId(Long creditorRightsApplyId) {
        this.creditorRightsApplyId = creditorRightsApplyId;
    }

    public Long getCarryUserId() {
        return carryUserId;
    }

    public void setCarryUserId(Long carryUserId) {
        this.carryUserId = carryUserId;
    }

    public BigDecimal getBuyBalance() {
        return buyBalance;
    }

    public void setBuyBalance(BigDecimal buyBalance) {
        this.buyBalance = buyBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Long getCarryCrId() {
        return carryCrId;
    }

    public void setCarryCrId(Long carryCrId) {
        this.carryCrId = carryCrId;
    }

	public Long getLendOrderId() {
		return lendOrderId;
	}

	public void setLendOrderId(Long lendOrderId) {
		this.lendOrderId = lendOrderId;
	}

	public BigDecimal getBuyWorth() {
		return buyWorth;
	}

	public void setBuyWorth(BigDecimal buyWorth) {
		this.buyWorth = buyWorth;
	}
    
}