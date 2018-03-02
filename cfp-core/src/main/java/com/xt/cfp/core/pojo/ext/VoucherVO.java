package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.constants.RateEnum;
import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.pojo.Voucher;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * Created by lenovo on 2015/8/27.
 */
public class VoucherVO extends Voucher {

    private String voucherName;

    private String userName;

    private String realName;

    private String amountStr;

    private String usageScenario;

    private String isOverly;

    private BigDecimal conditionAmount;

    private String cardinalType;
    private String voucherType;
    private BigDecimal rate;
    private BigDecimal amount;
    private String isExperience;
    private String voucherRemark;
    private String detailRemark;

    private String orderName;

    private String effictiveDate;

    private String sourceStr;

    private BigDecimal buyBalance;
    private String loanTitle;
    
    private Date createTime;//注册时间
    
    private String couponType ;  //券类型  1-非加息券，2-加息券 
    
    private String voucherCouponType ;//券类型 1-财富券，2-加息券，3-提现券
    
    private String condition;
    
    private String conditionStr;
    
    private String amountStartStr;

    public BigDecimal getBuyBalance() {
        return buyBalance;
    }

    public void setBuyBalance(BigDecimal buyBalance) {
        this.buyBalance = buyBalance;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public String getDetailRemark() {
        return detailRemark;
    }

    public void setDetailRemark(String detailRemark) {
        this.detailRemark = detailRemark;
    }

    public String getVoucherRemark() {
        return voucherRemark;
    }

    public void setVoucherRemark(String voucherRemark) {
        this.voucherRemark = voucherRemark;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setIsExperience(String isExperience) {
        this.isExperience = isExperience;
    }

    public String getIsExperienceStr() {

        if ("1".equals(this.getIsExperience())){
            return "是";
        }else{
            return "否";
        }
    }
    public String getIsExperience() {
        return this.isExperience;
    }
    public void setIsExperienceStr(String isExperienceStr) {
        this.isExperience = isExperienceStr;
    }


    public String getEffictiveDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = "";
        String _format = "";

        if (this.getEndDate()==null){
            return "长期有效";
        }else{
            _format = sdf.format(this.getEndDate());
        }

        if (this.getCreateDate()==null){
            format = " ";
        }else{
            format = sdf.format(this.getCreateDate());
        }
        return format+"至"+_format;
    }

    public void setEffictiveDate(String effictiveDate) {
        this.effictiveDate = effictiveDate;
    }

    public String getUsageScenario() {
        return usageScenario;
    }

    public void setUsageScenario(String usageScenario) {
        this.usageScenario = usageScenario;
    }

    public String getIsOverly() {
        return isOverly;
    }

    public void setIsOverly(String isOverly) {
        this.isOverly = isOverly;
    }

    public BigDecimal getConditionAmount() {
        return conditionAmount;
    }

    public void setConditionAmount(BigDecimal conditionAmount) {
        this.conditionAmount = conditionAmount;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAmountStr() {
        if (this.getVoucherValue()==null){
            String mount = "";
            if (this.getVoucherType().equals(VoucherConstants.VoucherTypeEnum.FIXED.getValue())){
                mount = this.getAmount()+"元";
            }else{
                mount = VoucherConstants.CardinalTypeEnum.getCardinalTypeEnumEnumByValue(this.getCardinalType()).getDesc()+"*"+this.getRate()+"%";
            }
            return mount;
        }else{
            return this.getVoucherValue().toString()+"元";
        }
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getUsageScenarioStr() {
        if (this.getUsageScenario()==null)
            return null;
        VoucherConstants.UsageScenario usageScenarioEnumByValue = VoucherConstants.UsageScenario.getUsageScenarioEnumByValue(this.getUsageScenario());
        return usageScenarioEnumByValue.getDesc();
    }



    public String getIsOverlyStr() {
        if (this.getIsOverly()==null)
            return null;
        if (this.getIsOverly().equals("0")){
            return "否";
        }else{
            return "是";
        }
    }



    public String getConditionAmountStr() {
        if (this.getConditionAmount().compareTo(new BigDecimal("-1"))==0){
            return "无限制";
        }
        String mount = "";
        if (this.getVoucherType().equals(VoucherConstants.VoucherTypeEnum.FIXED.getValue())){
            mount = this.getAmount()+"元";
        }else{
            mount = VoucherConstants.CardinalTypeEnum.getCardinalTypeEnumEnumByValue(this.getCardinalType()).getDesc()+"*"+this.getRate()+"%";
        }

        String str = this.getConditionAmount()+"元抵"+ mount ;
        return str;
    }



    public String getStatusStr() {
        if (this.getStatus()==null)
            return null;
        VoucherConstants.VoucherStatus voucherStatus = VoucherConstants.VoucherStatus.getVoucherStatusByValue(this.getStatus());
        return voucherStatus.getDesc();
    }


    public String getSourceStr() {
        if (this.getSourceType()==null)
            return null;
        VoucherConstants.SourceType sourceType = VoucherConstants.SourceType.getSourceTypeByValue(this.getSourceType());
        if (sourceType.getValue().equals(VoucherConstants.SourceType.OTHER.getValue())){
            return sourceType.getDesc()+"-"+this.getSourceDesc();
        }
        return sourceType.getDesc();
    }

    public void setSourceStr(String sourceStr) {
        this.sourceStr = sourceStr;
    }


    public String getCardinalType() {
        return cardinalType;
    }

    public void setCardinalType(String cardinalType) {
        this.cardinalType = cardinalType;
    }

    public String getVoucherType() {
        return voucherType;
    }
    
    

    public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getVoucherCouponType() {
		return voucherCouponType;
	}

	public void setVoucherCouponType(String voucherCouponType) {
		this.voucherCouponType = voucherCouponType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getConditionStr() {
		return conditionStr;
	}

	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}

	public String getAmountStartStr() {
		return amountStartStr;
	}

	public void setAmountStartStr(String amountStartStr) {
		this.amountStartStr = amountStartStr;
	}
	
	@Override
	public String getSourceDescStr() {
		if (StringUtils.isEmpty(getSourceType()))
			return null;
		VoucherConstants.SourceType sourceType = VoucherConstants.SourceType
				.getSourceTypeByValue(this.getSourceType());
		if (sourceType.getValue().equals(
				VoucherConstants.SourceType.OTHER.getValue())) {
			if(getVoucherCouponType().equals("2")){
				return sourceType.getDesc();
			}
			return getSourceDesc();
		}
		return sourceType.getDesc();
	}
    
}
