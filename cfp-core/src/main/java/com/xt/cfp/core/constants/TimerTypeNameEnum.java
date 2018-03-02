package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/8/20 0020.
 */
public enum TimerTypeNameEnum implements EnumsCanDescribed {
    FAILBID("1", "流标timer"),
    VOUCHER_REFRESH("2", "刷新财富券有效期timer"),
    CONTRASTRECHANGE_REFRESH("3", "充值状态刷新timer"),
    UNRECHARGEORDER_REFRESH("4", "24小时未支付充值单刷新timer"),
    WITHDRAW_REFRESH("5", "提现刷新timer"),
    OVERDUE_PENLTY("6", "逾期罚息刷新timer"),
    WECHAT_VOUCHER_EXPIRE_MSG("7", "财富卷到期提醒"),
    FINANCE_QUIT("8", "退出省心计划"),
    EXPORT_TO_YONGYOU("9","用友报表流水导出timer"),
    CRM_STAFF_CUSTOMER("10","crm员工客户关系timer"),
    FINANCE_REPAY_BALANCE("11","省心计划到期结算"),
    AUTO_PUBLISH("12","自动发标timer"),
    RATE_PRODUCT_FRESH("13","加息卷过期状态"),
    EXPIRE_RATE_MSG("14","加息券到期提醒"),
    CANCEL_PAYORDER_FOR_FAIL_BID("15","处理支付成功但处理失败的支付订单"),
	FINANCE_REFRESH_STATE("16","刷新省心计划timer"),
	FINANCE_AUTO_MATCH("17","自动匹配省心计划timer"),
    CAPITAL_FLOW_DO("18","恒丰存管资金流timer"),
    ;

    private String desc;
    private String value;

    TimerTypeNameEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
