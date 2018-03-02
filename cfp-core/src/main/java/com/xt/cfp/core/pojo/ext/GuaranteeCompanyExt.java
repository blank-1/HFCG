package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.BondSource;
import com.xt.cfp.core.pojo.GuaranteeCompany;

import java.math.BigDecimal;

/**
 * Created by lenovo on 2015/6/22.
 */
public class GuaranteeCompanyExt extends GuaranteeCompany{

    private BigDecimal avilableValue;

    private BigDecimal freezeValue;

    private BigDecimal value;

    private Long borrowCount;


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

    public Long getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(Long borrowCount) {
        this.borrowCount = borrowCount;
    }


}
