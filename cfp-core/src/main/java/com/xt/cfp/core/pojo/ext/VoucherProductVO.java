package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.pojo.VoucherProduct;
import org.apache.commons.lang.time.DateUtils;
import sun.util.calendar.CalendarUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2015/8/25.
 */
public class VoucherProductVO extends VoucherProduct {

    private String operateName;

    private String effictiveDate;

    private String voucherTypeStr;

    private String effectiveCountStr;

    private String usageScenarioStr;

    private String isOverlyStr;
    private String isExperienceStr;

    private String conditionAmountStr;

    private String amountStr;

    private String statusStr;

    private String voucherEffictiveDate;

    private Integer usageCount;

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getVoucherEffictiveDate() {

        Date now = new Date();
        if (this.getEffectiveCount()==null||this.getEffectiveCount()<0){
            return null;
        }
        Date end = DateUtils.addDays(now, this.getEffectiveCount());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "("+sdf.format(now)+"至"+sdf.format(end)+")";
    }

    public void setVoucherEffictiveDate(String voucherEffictiveDate) {
        this.voucherEffictiveDate = voucherEffictiveDate;
    }

    public String getIsExperienceStr() {
        if ("1".equals(this.getIsExperience())){
            return "是";
        }else{
            return "否";
        }
    }

    public void setIsExperienceStr(String isExperienceStr) {
        this.isExperienceStr = isExperienceStr;
    }

    public String getStatusStr() {
        if (this.getStatus()==null)
            return null;
        VoucherConstants.VoucherProductStatus statusByValue = VoucherConstants.VoucherProductStatus.getVoucherProductStatusByValue(this.getStatus());
        return statusByValue.getDesc();
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getAmountStr() {
        String mount = "";
        if (this.getVoucherType().equals(VoucherConstants.VoucherTypeEnum.FIXED.getValue())){
            mount = this.getAmount()+"元";
        }else{
            mount = VoucherConstants.CardinalTypeEnum.getCardinalTypeEnumEnumByValue(this.getCardinalType()).getDesc()+"*"+this.getRate()+"%";
        }
        return mount;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
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

    public void setConditionAmountStr(String conditionAmountStr) {
        this.conditionAmountStr = conditionAmountStr;
    }

    public String getIsOverlyStr() {
        if (this.getIsOverlay()==null)
            return null;
        if (this.getIsOverlay().equals("0")){
            return "否";
        }else{
            return "是";
        }
    }

    public void setIsOverlyStr(String isOverlyStr) {
        this.isOverlyStr = isOverlyStr;
    }

    public String getUsageScenarioStr() {
        if (this.getUsageScenario()==null)
            return null;
        VoucherConstants.UsageScenario usageScenarioEnumByValue = VoucherConstants.UsageScenario.getUsageScenarioEnumByValue(this.getUsageScenario());
        return usageScenarioEnumByValue.getDesc();
    }

    public void setUsageScenarioStr(String usageScenarioStr) {
        this.usageScenarioStr = usageScenarioStr;
    }

    public String getEffectiveCountStr() {
        if (this.getEffectiveCount()==-1){
            return "长期有效";
        }
        return this.getEffectiveCount()+"";
    }

    public void setEffectiveCountStr(String effectiveCountStr) {
        this.effectiveCountStr = effectiveCountStr;
    }

    public String getEffictiveDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = "";
        String _format = "";
        if (this.getStartDate()==null){
            format = " ";
        }else{
            format = sdf.format(this.getStartDate());
        }

        if (this.getEndDate()==null){
            _format = " ";
        }else{
            _format = sdf.format(this.getEndDate());
        }
        return format+"至"+_format;
    }

    public void setEffictiveDate(String effictiveDate) {
        this.effictiveDate = effictiveDate;
    }

    public String getVoucherTypeStr() {
        if (this.getVoucherType()==null)
            return null;
        VoucherConstants.VoucherTypeEnum voucherTypeEnumByValue = VoucherConstants.VoucherTypeEnum.getVoucherTypeEnumByValue(this.getVoucherType());
        return voucherTypeEnumByValue.getDesc();
    }

    public void setVoucherTypeStr(String voucherTypeStr) {
        this.voucherTypeStr = voucherTypeStr;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }
}
