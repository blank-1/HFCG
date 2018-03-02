package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.util.DateUtil;

/**
 * Created by luqinglin on 2015/7/5.
 */
public class LoanApplicationListVO extends LoanApplication {

    public String loanApplicationTitle;

    public BigDecimal totalAmountOfLoan;

    public BigDecimal rewardsPercent;
    //信用等级
    public String creditLevel;
    //期限
    public Integer cycleCount;
    private Integer dueTimeType;
    private Integer dueTime;
    //进度百分比
    public String ratePercent;
    //预期收益排序
    public Boolean annRateOrder;
    //期限排序排序
    public Boolean durationOrder;
    //信用等级排序
    public Boolean creditRankOrder;
    //还款方法
    public String repayMentMethod;
    //还款方式
    public String repayMethod;
    //起投金额
    public BigDecimal startAmount;
    //保障说明
    public String guaranteeType;
    //剩余金额
    public BigDecimal remain;
    //限投
    public BigDecimal maxBuyBalanceNow;
    //借款描述
    public String desc;
    //借款用途描述
    private String useageDesc;
    //预热时间
    private Date popenTime;
    //奖励发放时机
    private String awardPoint;
    //奖励率
    private String awardRate;
    
    public boolean begin;
    
    private String oType;
    
    private Long creditorRightId;
    
    //期限
    private String cycleCounts ;
    //申请出售价格
    private BigDecimal applyPrice ;
    //转让时价值
    private BigDecimal whenWorth ;
    
    //网贷天眼，查询辅助字段
    private String timeStatus;//通过标的状态，决定判断的时间类型
    private Date timeFrom;//起始时间
    private Date timeTo;//截止时间
    
    private Long creditorRightsApplyId;//债权转让申请ID
    
    private Date ptime;
    private Date otime;


    public Integer getDueTimeType() {
        return dueTimeType;
    }

    public void setDueTimeType(Integer dueTimeType) {
        this.dueTimeType = dueTimeType;
    }

    public Integer getDueTime() {
        return dueTime;
    }

    public void setDueTime(Integer dueTime) {
        this.dueTime = dueTime;
    }

    public Date getPtime() {
		return ptime;
	}

	public void setPtime(Date ptime) {
		this.ptime = ptime;
	}

	public Date getOtime() {
		return otime;
	}

	public void setOtime(Date otime) {
		this.otime = otime;
	}

	public String getTimeStatus() {
		return timeStatus;
	}

	public void setTimeStatus(String timeStatus) {
		this.timeStatus = timeStatus;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Date timeTo) {
		this.timeTo = timeTo;
	}

	public Long getCreditorRightsId() {
		return creditorRightId;
	}

	public void setCreditorRightsId(Long creditorRightId) {
		this.creditorRightId = creditorRightId;
	}

	public String getoType() {
		return oType;
	}

	public void setoType(String oType) {
		this.oType = oType;
	}

	public String getUseageDesc() {
        return useageDesc;
    }

    public void setUseageDesc(String useageDesc) {
        this.useageDesc = useageDesc;
    }

    public boolean isBegin() {
		try {
			if (null != popenTime) {
				if (DateUtil.secondBetween(new Date(), popenTime) > 0) {
					return false;
				}
			}
			return true;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return begin;
	}

    public void setBegin(boolean begin) {
        this.begin = begin;
    }

    public Date getPopenTime() {
        return popenTime;
    }

    public void setPopenTime(Date popenTime) {
        this.popenTime = popenTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(String repayMethod) {
        this.repayMethod = repayMethod;
    }

    public BigDecimal getMaxBuyBalanceNow() {
        if (maxBuyBalanceNow==null){
            return BigDecimal.ZERO;
        }
        return maxBuyBalanceNow;
    }

    public void setMaxBuyBalanceNow(BigDecimal maxBuyBalanceNow) {
        this.maxBuyBalanceNow = maxBuyBalanceNow;
    }

    public BigDecimal getRemain() {
        if (this.getApplicationState().equals(LoanApplicationStateEnum.BIDING.getValue())){
            if(totalAmountOfLoan!=null)
                return this.getConfirmBalance().subtract(totalAmountOfLoan);
            else
                return this.getConfirmBalance();
        }else{
            return BigDecimal.ZERO;
        }

    }

    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    public String getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

    public String getRepayMentMethod() {
        return repayMentMethod;
    }

    public void setRepayMentMethod(String repayMentMethod) {
        this.repayMentMethod = repayMentMethod;
    }

    public Integer getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(Integer cycleCount) {
        this.cycleCount = cycleCount;
    }
    public String getLoanApplicationTitle() {
        return loanApplicationTitle;
    }

    public void setLoanApplicationTitle(String loanApplicationTitle) {
        this.loanApplicationTitle = loanApplicationTitle;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public BigDecimal getTotalAmountOfLoan() {
        return totalAmountOfLoan;
    }

    public void setTotalAmountOfLoan(BigDecimal totalAmountOfLoan) {
        this.totalAmountOfLoan = totalAmountOfLoan;
    }

    public String getRatePercent() {
        if (this.getApplicationState().equals(LoanApplicationStateEnum.BIDING.getValue())){
            if(totalAmountOfLoan!=null) {
                String result = totalAmountOfLoan.multiply(new BigDecimal("100")).divide(this.getConfirmBalance(), 2, BigDecimal.ROUND_HALF_UP) + "";
                result = result.replaceAll("\\.00","");
                return result;
            }else
                return 0+"";
        }else{
            return 100+"";
        }
    }

    public void setRatePercent(String ratePercent) {
        this.ratePercent = ratePercent;
    }

    public Boolean getAnnRateOrder() {
        return annRateOrder;
    }

    public void setAnnRateOrder(Boolean annRateOrder) {
        this.annRateOrder = annRateOrder;
    }

    public Boolean getDurationOrder() {
        return durationOrder;
    }

    public void setDurationOrder(Boolean durationOrder) {
        this.durationOrder = durationOrder;
    }

    public Boolean getCreditRankOrder() {
        return creditRankOrder;
    }

    public void setCreditRankOrder(Boolean creditRankOrder) {
        this.creditRankOrder = creditRankOrder;
    }

    public BigDecimal getRewardsPercent() {
        return rewardsPercent;
    }

    public void setRewardsPercent(BigDecimal rewardsPercent) {
        this.rewardsPercent = rewardsPercent;
    }

    public String getOrderBy(){
            String orderBy = "";
            String str1 = annRateOrder==null?null:(annRateOrder?"rate asc":"rate desc");
            String str2 = durationOrder==null?null:(durationOrder?"lpp.CYCLE_COUNTS asc":"lpp.CYCLE_COUNTS desc");
            String str3 = creditRankOrder==null?null:(creditRankOrder?"lp.CREDIT_LEVEL asc":"lp.CREDIT_LEVEL desc");

            orderBy += str1==null?"":str1;
            orderBy += str2==null?"":(StringUtils.isEmpty(orderBy)?str2:","+str2);
            orderBy += str3==null?"":(StringUtils.isEmpty(orderBy)?str3:","+str3);
            return orderBy;
    }

	public String getAwardPoint() {
		return awardPoint;
	}

	public void setAwardPoint(String awardPoint) {
		this.awardPoint = awardPoint;
	}

	public String getAwardRate() {
		return awardRate;
	}

	public void setAwardRate(String awardRate) {
		this.awardRate = awardRate;
	}

	public String getCycleCounts() {
		return cycleCounts;
	}

	public void setCycleCounts(String cycleCounts) {
		this.cycleCounts = cycleCounts;
	}

	public BigDecimal getApplyPrice() {
		return applyPrice;
	}

	public void setApplyPrice(BigDecimal applyPrice) {
		this.applyPrice = applyPrice;
	}

	public BigDecimal getWhenWorth() {
		return whenWorth;
	}

	public void setWhenWorth(BigDecimal whenWorth) {
		this.whenWorth = whenWorth;
	}

	public Long getCreditorRightId() {
		return creditorRightId;
	}

	public void setCreditorRightId(Long creditorRightId) {
		this.creditorRightId = creditorRightId;
	}

	public Long getCreditorRightsApplyId() {
		return creditorRightsApplyId;
	}

	public void setCreditorRightsApplyId(Long creditorRightsApplyId) {
		this.creditorRightsApplyId = creditorRightsApplyId;
	}
}
