package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.UserInfo;

import java.math.BigDecimal;
import java.util.Date;

public class BondSourceUser extends UserInfo {
    private Long userSourceId;

    private Long userId;

    private Long bondSourceId;

    private String bondName;
    
    private String bondNames;
    
    private String loginNames;//登录名

    public String getBondNames() {
		return bondNames;
	}

	public void setBondNames(String bondNames) {
		this.bondNames = bondNames;
	}

	public String getLoginNames() {
		return loginNames;
	}

	public void setLoginNames(String loginNames) {
		this.loginNames = loginNames;
	}

	private Date createTime;

    private Long borrowCount;

    private BigDecimal avilableValue;

    private BigDecimal freezeValue;

    private BigDecimal value;

    private String idCard;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(Long borrowCount) {
        this.borrowCount = borrowCount;
    }

    public BigDecimal getAvilableValue() {
        return avilableValue;
    }

    public void setAvilableValue(BigDecimal avilableValue) {
        this.avilableValue = avilableValue;
    }

    public BigDecimal getFreezeValue() {
        return freezeValue;
    }

    public void setFreezeValue(BigDecimal freezeValue) {
        this.freezeValue = freezeValue;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(Long userSourceId) {
        this.userSourceId = userSourceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBondSourceId() {
        return bondSourceId;
    }

    public void setBondSourceId(Long bondSourceId) {
        this.bondSourceId = bondSourceId;
    }

    public String getBondName() {
        return bondName;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}