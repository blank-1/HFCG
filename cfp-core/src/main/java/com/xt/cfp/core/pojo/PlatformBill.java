package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台账单表
 */
public class PlatformBill {
    private Long userId;//用户ID
    private String realName;//真实姓名
    private Date regisTime;//注册时间
    private Date fastInvestTime;//首投时间
    private BigDecimal investValue;//累计投资金额
    private Long investRanking;//累计投资排名
    private BigDecimal profitValue;//已获收益
    private Long profitRanking;//已获收益排名
    private BigDecimal getVoucher;//累计获得财富券
    private BigDecimal useVoucher;//累计使用财富券
    private Integer likeBidCycle;//热衷标的周期
    private Long inviteNumber;//邀请人数
    private Long inviteRanking;//邀请人数排名
    private Integer isview;//是否查看
    private Date fastViewTime;//首次查看时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public Date getRegisTime() {
        return regisTime;
    }

    public void setRegisTime(Date regisTime) {
        this.regisTime = regisTime;
    }

    public Date getFastInvestTime() {
        return fastInvestTime;
    }

    public void setFastInvestTime(Date fastInvestTime) {
        this.fastInvestTime = fastInvestTime;
    }

    public BigDecimal getInvestValue() {
        return investValue;
    }

    public void setInvestValue(BigDecimal investValue) {
        this.investValue = investValue;
    }

    public Long getInvestRanking() {
        return investRanking;
    }

    public void setInvestRanking(Long investRanking) {
        this.investRanking = investRanking;
    }

    public BigDecimal getProfitValue() {
        return profitValue;
    }

    public void setProfitValue(BigDecimal profitValue) {
        this.profitValue = profitValue;
    }

    public Long getProfitRanking() {
        return profitRanking;
    }

    public void setProfitRanking(Long profitRanking) {
        this.profitRanking = profitRanking;
    }

    public BigDecimal getGetVoucher() {
        return getVoucher;
    }

    public void setGetVoucher(BigDecimal getVoucher) {
        this.getVoucher = getVoucher;
    }

    public BigDecimal getUseVoucher() {
        return useVoucher;
    }

    public void setUseVoucher(BigDecimal useVoucher) {
        this.useVoucher = useVoucher;
    }

    public Integer getLikeBidCycle() {
        return likeBidCycle;
    }

    public void setLikeBidCycle(Integer likeBidCycle) {
        this.likeBidCycle = likeBidCycle;
    }

    public Long getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(Long inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public Long getInviteRanking() {
        return inviteRanking;
    }

    public void setInviteRanking(Long inviteRanking) {
        this.inviteRanking = inviteRanking;
    }

	public Integer getIsview() {
		return isview;
	}

	public void setIsview(Integer isview) {
		this.isview = isview;
	}

	public Date getFastViewTime() {
		return fastViewTime;
	}

	public void setFastViewTime(Date fastViewTime) {
		this.fastViewTime = fastViewTime;
	}
    
}