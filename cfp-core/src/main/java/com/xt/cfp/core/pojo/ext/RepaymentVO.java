package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.RepaymentPlan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by luqinglin on 2015/8/4.
 * 还款情况
 */
public class RepaymentVO extends RepaymentPlan{

    private BigDecimal shouldFee = BigDecimal.ZERO;
    private BigDecimal factFee = BigDecimal.ZERO;
    private BigDecimal shouldFaxi = BigDecimal.ZERO;
    private BigDecimal factfaxi = BigDecimal.ZERO;
    private Date recentRepaymetDate;
    private Date opertionDate;//还款操作日期
    private Long payId;//支付单ID
    private char channelType;//支付类型
    private char repaymentMethod;//还款方法
    private Long userId;
    private BigDecimal noRepaymentPercent;//待还款占比
	private BigDecimal depalFine;//实还罚息
	private BigDecimal repaymentFees;//还款扣费

    public BigDecimal getShouldFee() {
        return shouldFee;
    }

    public void setShouldFee(BigDecimal shouldFee) {
        this.shouldFee = shouldFee;
    }

    public BigDecimal getFactFee() {
        return factFee;
    }

    public Date getRecentRepaymetDate() {
        return recentRepaymetDate;
    }

    public void setRecentRepaymetDate(Date recentRepaymetDate) {
        this.recentRepaymetDate = recentRepaymetDate;
    }

    public void setFactFee(BigDecimal factFee) {
        this.factFee = factFee;
    }

    public BigDecimal getShouldFaxi() {
        return shouldFaxi;
    }

    public void setShouldFaxi(BigDecimal shouldFaxi) {
        this.shouldFaxi = shouldFaxi;
    }

    public BigDecimal getFactfaxi() {
        return factfaxi;
    }

    public void setFactfaxi(BigDecimal factfaxi) {
        this.factfaxi = factfaxi;
    }

	public Date getOpertionDate() {
		return opertionDate;
	}

	public void setOpertionDate(Date opertionDate) {
		this.opertionDate = opertionDate;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public char getChannelType() {
		return channelType;
	}

	public void setChannelType(char channelType) {
		this.channelType = channelType;
	}

	public char getRepaymentMethod() {
		return repaymentMethod;
	}

	public void setRepaymentMethod(char repaymentMethod) {
		this.repaymentMethod = repaymentMethod;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getNoRepaymentPercent() {
		return noRepaymentPercent;
	}

	public void setNoRepaymentPercent(BigDecimal noRepaymentPercent) {
		this.noRepaymentPercent = noRepaymentPercent;
	}

	public BigDecimal getDepalFine() {
		return depalFine;
	}

	public void setDepalFine(BigDecimal depalFine) {
		this.depalFine = depalFine;
	}

	public BigDecimal getRepaymentFees() {
		return repaymentFees;
	}

	public void setRepaymentFees(BigDecimal repaymentFees) {
		this.repaymentFees = repaymentFees;
	}

}
