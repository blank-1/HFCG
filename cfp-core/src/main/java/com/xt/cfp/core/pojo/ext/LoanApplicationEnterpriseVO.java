package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.pojo.LoanApplication;

import java.math.BigDecimal;

/**
 * Created by ren yulin on 15-7-3.
 */
public class LoanApplicationEnterpriseVO extends LoanApplicationVO {
   private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
