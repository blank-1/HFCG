package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.LendProduct;

import java.math.BigDecimal;

/**
 * 出借产品信息包含已投金额状态
 * Created by yulei on 2015/7/3.
 */
public class LproductWithBalanceStatus extends LendProduct {

	protected BigDecimal lendProductPublishId;
    protected BigDecimal publishBalance;//发布总金额
    protected BigDecimal soldBalance;//已售金额
    protected BigDecimal availableBalance;//可购买金额
    protected String publishCode;//发布期号
    protected String deadLine;//距离截止时间
    protected String publishBalanceType;
    protected String publishState;
    protected String publishName;


    public BigDecimal getLendProductPublishId() {
		return lendProductPublishId;
	}

	public void setLendProductPublishId(BigDecimal lendProductPublishId) {
		this.lendProductPublishId = lendProductPublishId;
	}

	public BigDecimal getPublishBalance() {
        return publishBalance;
    }

    public void setPublishBalance(BigDecimal publishBalance) {
        this.publishBalance = publishBalance;
    }

    public BigDecimal getSoldBalance() {
        return soldBalance;
    }

    public void setSoldBalance(BigDecimal soldBalance) {
        this.soldBalance = soldBalance;
        if (this.publishBalance != null)
            this.availableBalance = publishBalance.subtract(soldBalance);
    }

    public BigDecimal getAvailableBalance() {
        if ( publishBalance != null && soldBalance != null)
            availableBalance = publishBalance.subtract(soldBalance);
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

	public String getPublishCode() {
		return publishCode;
	}

	public void setPublishCode(String publishCode) {
		this.publishCode = publishCode;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String getPublishBalanceType() {
		return publishBalanceType;
	}

	public void setPublishBalanceType(String publishBalanceType) {
		this.publishBalanceType = publishBalanceType;
	}

	public String getPublishState() {
		return publishState;
	}

	public void setPublishState(String publishState) {
		this.publishState = publishState;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
	
}
