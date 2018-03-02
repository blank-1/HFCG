package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.WithDraw;
import com.xt.cfp.core.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by luqinglin on 2015/6/25.
 */
public class WithDrawExt extends WithDraw {
    private String loginName;
    private String realName;
    private String userName;
    private String bankName;
    private String bankCode;
    private String cardNo;
    private String operateName;
    private String companyName;
    private String isCardUsed;
    private String shortCardNo;
    private String resourceDesc;
    private String belongChannel;
    private String shortBank;

    private String[] registeredBank;

    private BigDecimal allWithDrawAmount;

    private BigDecimal allIncomeAmount;

    private BigDecimal allBondondAmount;

    private BigDecimal value;

    private BigDecimal amountWithoutFee;

    public String getShortCardNo() {
        if(this.getCardNo()==null||this.getCardNo()==""){
            return shortCardNo;
        }else{
            return StringUtils.getLast4CardNo(cardNo);
        }
    }

    public void setShortCardNo(String shortCardNo) {
        this.shortCardNo = shortCardNo;
    }

    public String getIsCardUsed() {
        return isCardUsed;
    }

    public void setIsCardUsed(String isCardUsed) {
        this.isCardUsed = isCardUsed;
    }

    public String[] getRegisteredBank() {
        return registeredBank;
    }

    public void setRegisteredBank(String[] registeredBank) {
        this.registeredBank = registeredBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRegisteredBankDetail(){
        if (registeredBank==null)
            return null;
        else{
            String _str="";
            for(String str : registeredBank){
                _str +=str;
            }
            return _str;
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BigDecimal getAllWithDrawAmount() {
        return allWithDrawAmount;
    }

    public void setAllWithDrawAmount(BigDecimal allWithDrawAmount) {
        this.allWithDrawAmount = allWithDrawAmount;
    }

    public BigDecimal getAllIncomeAmount() {
        return allIncomeAmount;
    }

    public void setAllIncomeAmount(BigDecimal allIncomeAmount) {
        this.allIncomeAmount = allIncomeAmount;
    }

    public BigDecimal getAllBondondAmount() {
        return allBondondAmount;
    }

    public void setAllBondondAmount(BigDecimal allBondondAmount) {
        this.allBondondAmount = allBondondAmount;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    public BigDecimal getAmountWithoutFee() {
        return amountWithoutFee;
    }

    public void setAmountWithoutFee(BigDecimal amountWithoutFee) {
        this.amountWithoutFee = amountWithoutFee;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	public String getBelongChannel() {
		return belongChannel;
	}

	public void setBelongChannel(String belongChannel) {
		this.belongChannel = belongChannel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getShortBank() {
		return shortBank;
	}

	public void setShortBank(String shortBank) {
		this.shortBank = shortBank;
	}
    
}
