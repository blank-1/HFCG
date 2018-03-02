package com.xt.cfp.core.pojo;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户卡信息
 */
public class CustomerCard {
    private Long customerCardId;//客户卡ID
    private Long userId;//用户ID
    private Long bankCode;//所属银行
    private Long provinceCityId;//恒丰所属银行支行id
    private String registeredBank;//开户行详细地址
    private String mobile;//银行预留手机号
    private String cardType;//卡类型
    private String cardCode;//卡号
    private String cardCustomerName;//开户名
    private String status;//卡状态
    private String bindStatus;//绑卡状态
    private Date updateTime;//最后更改时间
    private String agreeNo;//签约协议号
    private String belongChannel;//所属渠道
    private String bankLineNumber;//大额行号
    private String branchName;//支行名字
    private String bankNum;

    private CgBank cgBank;

    public CgBank getCgBank() {
        return cgBank;
    }

    public void setCgBank(CgBank cgBank) {
        this.cgBank = cgBank;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    private String encryptCardNo;//获得后四位的银行卡
    
    //辅助字段
    private BigDecimal withdrawAmountSum = BigDecimal.ZERO;//提现总额
    private String encryptMobileNo;//加密后的手机号
    
    //卡类型:1划扣款、2打款卡
    public static final String CARDTYPE_INCARD = "1";
    public static final String CARDTYPE_OUTCARD = "2";
    
    //卡状态：0不可用、1可用
    public static final String STATUS_ENABLE = "1";
    public static final String STATUS_DISABLE = "0";


    public String getAgreeNo() {
        return agreeNo;
    }

    public void setAgreeNo(String agreeNo) {
        this.agreeNo = agreeNo;
    }

    public String getBelongChannel() {
        return belongChannel;
    }

    public void setBelongChannel(String belongChannel) {
        this.belongChannel = belongChannel;
    }

    public String getEncryptMobileNo() {
        if (this.getMobile()==null||this.getMobile()==""){
            return null;
        }
        return com.xt.cfp.core.util.StringUtils.getEncryptMobileNo(this.getMobile());
    }

    public void setEncryptMobileNo(String encryptMobileNo) {
        this.encryptMobileNo = encryptMobileNo;
    }

    public String getEncryptCardNo() {
        if (this.getCardCode()==null||this.getCardCode()==""){
            return null;
        }
        return com.xt.cfp.core.util.StringUtils.getLast4CardNo(this.getCardCode());
    }

    public void setEncryptCardNo(String encryptCardNo) {
        this.encryptCardNo = encryptCardNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerCardId() {
        return customerCardId;
    }

    public void setCustomerCardId(Long customerCardId) {
        this.customerCardId = customerCardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBankCode() {
        return bankCode;
    }

    public void setBankCode(Long bankCode) {
        this.bankCode = bankCode;
    }

    public String getRegisteredBank() {
        return registeredBank;
    }

    public void setRegisteredBank(String registeredBank) {
        this.registeredBank = registeredBank;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardcustomerName() {
        return cardCustomerName;
    }

    public void setCardcustomerName(String cardCustomerName) {
        this.cardCustomerName = cardCustomerName;
    }

	public String getCardCodeShort(){
        String cardNo = this.getCardCode();
        if(StringUtils.isEmpty(cardNo))
            return null;
        else
            return cardNo.substring(cardNo.length()-4);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getWithdrawAmountSum() {
		return withdrawAmountSum;
	}

	public void setWithdrawAmountSum(BigDecimal withdrawAmountSum) {
		this.withdrawAmountSum = withdrawAmountSum;
	}

    public String getCardCustomerName() {
        return cardCustomerName;
    }

    public void setCardCustomerName(String cardCustomerName) {
        this.cardCustomerName = cardCustomerName;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

	public Long getProvinceCityId() {
		return provinceCityId;
	}

	public void setProvinceCityId(Long provinceCityId) {
		this.provinceCityId = provinceCityId;
	}

	public String getBankLineNumber() {
		return bankLineNumber;
	}

	public void setBankLineNumber(String bankLineNumber) {
		this.bankLineNumber = bankLineNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

    @Override
    public String toString() {
        return "CustomerCard{" +
                "customerCardId=" + customerCardId +
                ", userId=" + userId +
                ", bankCode=" + bankCode +
                ", provinceCityId=" + provinceCityId +
                ", registeredBank='" + registeredBank + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", cardCustomerName='" + cardCustomerName + '\'' +
                ", status='" + status + '\'' +
                ", bindStatus='" + bindStatus + '\'' +
                ", updateTime=" + updateTime +
                ", agreeNo='" + agreeNo + '\'' +
                ", belongChannel='" + belongChannel + '\'' +
                ", bankLineNumber='" + bankLineNumber + '\'' +
                ", branchName='" + branchName + '\'' +
                ", bankNum='" + bankNum + '\'' +
                ", cgBank=" + cgBank +
                ", encryptCardNo='" + encryptCardNo + '\'' +
                ", withdrawAmountSum=" + withdrawAmountSum +
                ", encryptMobileNo='" + encryptMobileNo + '\'' +
                '}';
    }
}