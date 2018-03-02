package com.xt.cfp.core.pojo.ext.crm;

import java.math.BigDecimal;

/**
 * Created by lenovo on 2015/12/3.
 */
public class PerformanceStatisticsVO {
    //加盟商
    private String fname;
    //邀请人
    private String staffName;
    //客户姓名
    private String customerName;
    //客户手机号
    private String customerMobile;
    //投资金额
    private BigDecimal amount;
    //折标金额
    private BigDecimal discountAmount;
    //有效邀请量
    private Integer effectiveCount;
    //投资过万量
    private Integer overAimcount;
    //统计时段
    private String timeProid;
    //邀请人邀请码
    private String ivcode;
    //根id
    private Long genId;
    private String genName;
    
    private BigDecimal gDiscount;
    
    

    public BigDecimal getgDiscount() {
		return gDiscount;
	}

	public void setgDiscount(BigDecimal gDiscount) {
		this.gDiscount = gDiscount;
	}

	//客户id
    private Long userId;

    public Long getGenId() {
        return genId;
    }

    public void setGenId(Long genId) {
        this.genId = genId;
    }

    public String getGenName() {
        return genName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIvcode() {
        return ivcode;
    }

    public void setIvcode(String ivcode) {
        this.ivcode = ivcode;
    }

    public String getTimeProid() {
        return timeProid;
    }

    public void setTimeProid(String timeProid) {
        this.timeProid = timeProid;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getEffectiveCount() {
        return effectiveCount;
    }

    public void setEffectiveCount(Integer effectiveCount) {
        this.effectiveCount = effectiveCount;
    }

    public Integer getOverAimcount() {
        return overAimcount;
    }

    public void setOverAimcount(Integer overAimcount) {
        this.overAimcount = overAimcount;
    }
}
