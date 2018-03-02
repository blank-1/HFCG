package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

import com.xt.cfp.core.constants.FeesItemTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.RadiceTypeEnum;

/**
 * Created by Renyulin on 14-5-16 下午4:26.
 */
public class LoanApplicationFeesItem {
    private long loanApplicationFeesItemId;//借款申请费用Id
    private Long loanApplicationId;//借款申请ID
    private char chargeCycle;//收费周期
    private BigDecimal workflowRatio;//平台收取比例
    private String itemName;//项目名称
    private String itemType;//费用类别
    private BigDecimal feesRate;//收费比例
    private int radicesType;//基数
    private String radiceLogic;//自定义基数逻辑
    private String radiceName;//自定义基数名称
    private BigDecimal amount;//收取金额
    protected String radiceTypeStr;
    protected String ItemTypeStr;
    protected String chargeCycleStr;
    private Long mainLoanApplicationId;//借款申请主表ID

    public String getRadiceTypeStr() {
        return RadiceTypeEnum.getDescByValue(String.valueOf(radicesType));
    }

    public String getItemTypeStr() {
        return FeesItemTypeEnum.getDescByValue(itemType);
    }

    public String getChargeCycleStr() {
        return FeesPointEnum.getDescByValue(String.valueOf(chargeCycle));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getLoanApplicationFeesItemId() {
        return loanApplicationFeesItemId;
    }

    public void setLoanApplicationFeesItemId(long loanApplicationFeesItemId) {
        this.loanApplicationFeesItemId = loanApplicationFeesItemId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public char getChargeCycle() {
        return chargeCycle;
    }

    public void setChargeCycle(char chargeCycle) {
        this.chargeCycle = chargeCycle;
    }

    public BigDecimal getWorkflowRatio() {
        return workflowRatio;
    }

    public void setWorkflowRatio(BigDecimal workflowRatio) {
        this.workflowRatio = workflowRatio;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getFeesRate() {
        return feesRate;
    }

    public void setFeesRate(BigDecimal feesRate) {
        this.feesRate = feesRate;
    }

    public int getRadicesType() {
        return radicesType;
    }

    public void setRadicesType(int radicesType) {
        this.radicesType = radicesType;
    }

    public String getRadiceLogic() {
        return radiceLogic;
    }

    public void setRadiceLogic(String radiceLogic) {
        this.radiceLogic = radiceLogic;
    }

    public String getRadiceName() {
        return radiceName;
    }

    public void setRadiceName(String radiceName) {
        this.radiceName = radiceName;
    }

	public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}
    
}
