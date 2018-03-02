package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.util.StringUtils;

/**
 * Created by lenovo on 2015/7/24.
 */
public class LenderRecordVO {
    //投标人
    private String lenderName;
    //投标金额
    private BigDecimal lendAmount;
    //投标时间
    private Date lendTime;
    //投标人用户ID
    private Long userId;
    //是否是省心计划的订单 0 否  1 是
    private String loType;
    
    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = StringUtils.getJmName(lenderName);
    }

    public BigDecimal getLendAmount() {
        return lendAmount;
    }

    public void setLendAmount(BigDecimal lendAmount) {
        this.lendAmount = lendAmount;
    }

    public Date getLendTime() {
        return lendTime;
    }

    public void setLendTime(Date lendTime) {
        this.lendTime = lendTime;
    }

    public String getJmLendName() {
        return this.getLenderName();
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoType() {
		return loType;
	}

	public void setLoType(String loType) {
		this.loType = loType;
	}

}
