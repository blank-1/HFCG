package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum TemplateType implements EnumsCanDescribed {
    SMS_REGISTER_VM("registerMessage","注册短信模版","regist"),
    SMS_RETRIEVEPASSWORD_VM("retrievePasswordMessage","找回密码短信模版","retrievePassword"),
    SMS_WITHDRAW_APPLY_VM("withDrawApplyMessage","提现申请短信模版","withDrawApply"),
    SMS_MOBILEAUTHENTICATION_VM("mobileAuthentication","修改手机验证短信模版","mobileAuthentication"),
    SMS_RECHARGE_SUCCESS_VM("recharge","充值成功短信模版","recharge"),
    SMS_LOANBID_SUCCESS_VM("loanBidSuccess","投标成功短信模版","loanBidSuccess"),
    SMS_LOANBID_FAILED_VM("loanBidFailed","投标失败短信模版","loanBidFailed"),
    SMS_WITHDRAW_SUCCESS_VM("withDrawSuccessMessage","提现成功短信模版","withDrawSuccessMessage"),
    SMS_WITHDRAW_FAILURE_VM("withDrawFailed","提现失败短信模版","withDrawFailed"),
    SMS_RESETTRADEPASSMSG_VM("resetTradePassMsg","重设交易密码短信模版","resetTradePassMsg"),
    SMS_LOANAPPLICATION_FAILURE_VM("loanApplicationFailure","流标短信模版","loanApplicationFailure"),
    SMS_REPAYMENT_END_VM("repaymentEnd","还款结束短信模版","repaymentEnd"),
    SMS_REPAYMENT_SUCCESS_VM("repaymentSuccess","还款成功短信模版","repaymentSuccess"),
    SMS_MAKELOAN_VM("makeLoan","放款计息短信模版","repaymentSuccess"),
    SMS_SALESREGIST_VM("salesRegistMessage","电销注册成功短信","s_regist"),
    SMS_WITHDRAW_SUCESS("","",""),
    SMS_BANKCARDUNBUNDLING_VM("bankCardUnbundling","后台银行卡解绑给用户短信","bankCardUnbundling"),
    SMS_FINANCEPLAN_SUCCESS_VM("financePlanSuccess","购买省心计划成功模板","financePlanSuccess"),
    SMS_FINANCEPLAN_BEGIN_INTEREST_VM("financePlanBeginInterest","省心计划开始起息模板","financePlanBeginInterest"),
    SMS_FINANCEPLAN_EXPIRED_VM("financePlanExpired","省心计划授权期到期模板","financePlanExpired"),
    SMS_FINANCEPLAN_LAST_RETURN_BALANCE_VM("financePlanLastReturnBalance","省心计划结清模板","financePlanLastReturnBalance"),
    SMS_CFH_BIND_VM("cfhBind","财富汇绑定短信模板","cfhBind"),
    SMS_AHEAD_REPAYMENT_VM("aheadRepayment", "提前还款成功短信", "aheadRepayment"),
    SMS_LOTTERY_DRAW_VM("lotteryDraw", "加息券抽奖短信", "lotteryDraw"),
    SMS_GETRATEMSG_VM("getRateMsg", "发送获取加息券短信", "getRateMsg"),
    SMS_EXPIRERATEMSG_VM("expireRateMsg", "发送即将到期加息券短信", "expireRateMsg"),
    SMS_GETVOUCHERMSG_VM("getVoucherMsg", "发送获取财富券短信", "getVoucherMsg"),
    SMS_EXPIREVOUCHERMSG_VM("expireVoucherMsg", "发送即将到期财富券短信", "expireVoucherMsg"),
    SMS_VALID_VM("validCode", "发送短信模拟登录", "validCode"),
    ;


    private final String value;
    private final String desc;
    private final String prekey;


    TemplateType(String value, String desc, String prekey) {
        this.value = value;
        this.desc = desc;
        this.prekey = prekey;
    }

    public String getPrekey() {
        return prekey;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getValue() {
        return value;
    }
}
