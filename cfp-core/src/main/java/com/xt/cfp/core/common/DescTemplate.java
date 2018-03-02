package com.xt.cfp.core.common;

import com.xt.cfp.core.util.StringUtils;

/**
 * Created by yulei on 2015/7/6.
 */
public final class DescTemplate {

    public static class Log{
        /**
         * 投标相关的日志模板
         */
        public static class BidLogTemplate {

            public static final String BUY_FINANCE_BY_ACCOUNTVALUE = "检测到用户用余额购买省心计划，用户ID{0}，省心计划发布明细ID{1}, 理财金额{2}";
            public static final String BID_LOAN_BY_ACCOUNTVALUE = "检测到用户用余额购买投标，用户ID{0}，借款申请发布明细ID{1}, 投标金额{2}";
        }

        /**
         * 理财相关日志模板
         */
        public static class FinanceTemplate {
            public static final String FAILED_START_CALCULATE_FINANCE_INTEREST = "省心计划【ID：{0}】起息失败，原因：【{1}】";
        }

        /**
         * 债权相关日志模板
         */
        public static class CreditorTemplate {
            public static final String UNDOING_CREDITORRIGHTSAPPLY = "正在撤销债权转让申请单【ID：{0}】，原因：【{1}】";
        }
    }

    public static class desc{

        public static class AccountChanngedDesc {
            //购买理财转账入
            public static final String BUY_FINANCE_TRANSFER_IN = "购买省心计划成功，省心计划账户转入{0}元";
            //购买理财转账出
            public static final String BUY_FINANCE_TRANSFER_OUT = "购买省心计划成功，从资金账户中转出{0}元";
            //业务处理冻结
            public static final String FREEZE_FOR_BUS_HANDLING = "因【{0}】，冻结资金{1}元";
            public static final String UNFREEZE_FOR_BUS_HANDLING_SUCCESS = "因【{0}】，解冻资金{1}元";
            public static final String UNFREEZE_FOR_BUS_HANDLING_FAIL = "因【{0}】，解冻资金{1}元";
            
            //后台操作
            public static final String BACKSTAGE_FOR_IN = "因【{0}】，转入资金{1}元";
            public static final String BACKSTAGE_FOR_OUT = "因【{0}】，转出资金{1}元";
            
            //充值转入
            public static final String RECHARGE_IN = "充值{0}元";

            public static final String DEFAULTINTEREST_IN = "借款合同【{0}】缴纳罚息";
            public static final String INTEREST_IN = "借款合同【{0}】按还款计划回款利息";
            public static final String REPAYMENT_IN = "借款合同【{0}】按还款计划回款本金";
            public static final String INCOMEDEFAULTINTEREST_FROZEN = "借款合同【{0}】回款利息冻结";
            public static final String LOANAPPLICATION_AHEAD_CASH2LOAN = "借款合同【{0}】的提前还款";
            public static final String REPAYMENT_UNFROZEN = "借款合同【{0}】还款解冻";
            public static final String LOANAPPLICATION_AHEAD_2REPAYMENT = "借款合同【{0}】的提前还款冻结";
            public static final String REPAYMENT_PAY = "借款合同【{0}】还款支出";
            public static final String DELAYFEES_UNFREEZE = "借款合同【{0}】解冻逾期管理费【{1}】";
            public static final String DELAYFEES_PAY = "借款合同【{0}】支出逾期管理费【{1}】";
            public static final String DELAYFEES_INCOME = "收入借款合同【{0}】的逾期管理费【{1}】";
            public static final String LOAN_FEES_UNFREEZE = "借款合同【{0}】解冻管理费【{1}】";
            public static final String LOAN_FEES_PAY = "借款合同【{0}】支出管理费【{1}】";
            public static final String LOAN_FEES_INCOME = "收入借款合同【{0}】的管理费【{1}】";
            public static final String RECEIVE_FEES_PAY = "借款合同【{0}】回款时扣费【{1}】";
            public static final String RECEIVE_END_FEES_PAY = "出借订单【{0}】结清时扣费【{1}】";
            public static final String RECEIVE_FEES_INCOME = "收入借款合同【{0}】回款至出借订单【{1}】的扣费【{2}】";
            public static final String ACCURACYERROR_INCOME = "精度误差。18位精度【{0}】，2位精度【{1}】";
            public static final String RECEIVE_FEES_PAY_FINANCE = "省心计划合同【{0}】回款时扣费【{1}】";
            public static final String RECEIVE_FEES_INCOME_FINANCE = "收入省心计划合同【{0}】回款至出借订单【{1}】的扣费【{2}】";
            public static final String PREPAYINTEREST_PAY = "平台为省心计划合同【{0}】垫付利息";
            public static final String LENDER_PREPAYINTEREST_PAY = "出借人为省心计划合同【{0}】垫付利息";
            public static final String RECEIVEINTEREST_INCOME = "省心计划合同【{0}】收入回款利息";
            public static final String RECEIVEINTEREST_FREEZE = "省心计划合同【{0}】冻结回款利息";
            public static final String BUYRIGHTS_FREEZE = "购买【{0}】的债权";
            public static final String AWARD_PAY = "平台支出给出借人【{0}】借款合同【{1}】的投标奖励";
            public static final String AWARD_INCOME = "借款合同【{0}】的投标奖励【{1}发放】";
            public static final String LOANAPPLICATION_INCOME = "借款合同【{0}】的还款充值";
            public static final String LOANAPPLICATION_CASH2LOAN = "借款合同【{0}】的还款";
            public static final String LOANAPPLICATION_2REPAYMENT = "借款合同【{0}】的还款冻结";
            public static final String ACTIVITY_AWARDS_PAY = "因【{0}】活动，平台给出借人【{1}】打赏{2}元";
            public static final String ACTIVITY_AWARDS_INCOME = "因参与【{0}】活动，收到平台奖励{1}元";
            public static final String DATA_TRANS_INCOME = "因平台数据迁移，将出借人在老平台上的【{0}】元打入新平台账户";
            public static final String OFFINE_RECHARGE_INCOME = "线下充值{0}元，银行充值单号{1}";
            public static final String UNFREE = "资金解冻";
            public static final String MAKELOAN_PAY = "出借放款，资金支出,借款标的【{0}】";
            public static final String LOAN_AHEAD_FEES_UNFREEZE = "借款合同【{0}】解冻提前还款违约费【{1}】";
            public static final String LOANAPPLICATION_AHEAD_INCOME = "借款合同【{0}】的提前还款充值";
            public static final String AHEAD_REPAYMENT_IN = "借款合同【{0}】提前回款本金";

            public static final String WITHDRAW_FREEZE = "提交提现申请资金冻结{0}元";
            public static final String WITHDRAW_UNFREEZE_SUCCESS = "提现成功，资金解冻{0}元";
            public static final String WITHDRAW_UNFREEZE_FAILE_ = "提现处理失败，资金解冻{0}元";
            public static final String WITHDRAW_FEE = "提现成功，扣除提现手续费{0}元";
            public static final String WITHDRAW_SUCCESS = "提现成功{0}元";
            public static final String WITHDRAW_PLATFORM_FEE = "提现成功，收取提现手续费{0}元";

            public static final String FAILLOAN_UNFREEZE = "因借款【{0}】流标，解冻{1}元";


            public static final String VOUCHER_PLATFORM_PAY = "财富券变现，平台支出{0}元";
            public static final String VOUCHER_INCOME = "财富券变现，收入{0}元";

            public static final String SUPPLEMETARY_DIFFERENCE_INCOME = "平台补差。收入{0}元";
            public static final String SUPPLEMETARY_DIFFERENCE_PAY = "平台补差。支出{0}元";
            public static final String CORRECTION_PRECISION = "系统精度修复，收入{0}元";

            public static final String INVITATION_INCOME = "邀请奖励，收入{0}元";
            public static final String INVITATION_PAY = "平台邀请奖励，支出{0}元";

            public static final String FIRST_BID_INCOME = "首次投标奖励，收入{0}元";
            public static final String FIRST_BID_PAY = "平台首次投标奖励，支出{0}元";
            
            public static final String OVERFLOW_FEE_PAY = "溢出费，支出{0}元";
            public static final String OVERFLOW_FEE_INCOME = "【{0}】溢出费，收入{1}元";
            public static final String BUY_FINANCE_QUIT_OUT = "购买省心计划退出，资金结算{0}元";
            public static final String BUY_FINANCE_PROFIT_TURN_OUT = "省心计划收益转出，资金结算{0}元";
            public static final String BUY_FINANCE_QUIT_IN = "购买省心计划到期，资金结算完毕，共计{0}元";
            public static final String BUY_FINANCE_PROFIT_QUIT_IN = "省心计划已获收益转入，共计{0}元";
            public static final String CREDITOR_TURNOUT_UNDO_OUT = "因债权转让撤销，解冻出借资金{0}元";
            public static final String CREDITOR_TURNOUT_OUT = "债权转让，解冻出借资金{0}元";
            public static final String CREDITOR_TURNOUT_PAY = "债权转让，支出出借资金{0}元";
            public static final String CREDITOR_TURNOUT_INCOME = "债权转让，收入转让资金{0}元";
            public static final String CREDITOR_TURNOUT_FEEPAY = "债权转让，支出转让费用{0}元";
            public static final String CREDITOR_TURNOUT_FEEINCOME = "债权转让，收入转让费用{0}元";
            
            public static final String COMMISION_PAY = "平台支出给出借人【{0}】借款合同【{1}】的佣金";
            public static final String COMMISION_INCOME = "借款合同【{0}】的佣金奖励【{1}发放】";
            
            public static final String AHEAD_REPAYMENT_FEE = "项目【{0}】提前还款费用，支出{1}元";
            public static final String AHEAD_REPAYMENT_FEE_USER_INCOME = "项目【{0}】提前还款，收入违约金{1}元";
            public static final String AHEAD_REPAYMENT_FEE_PLATFORM_INCOME = "项目【{0}】提前还款，收入违约金{1}元";
            
            public static final String RATE_COUPON_PAY = "平台支出给出借人【{0}】借款合同【{1}】的使用加息券奖励";
            public static final String RATE_COUPON_INCOME = "借款合同【{0}】的使用加息券奖励【{1}发放】";
            public static final String ACTIVITY_PAY = "平台支出给出借人【{0}】借款合同【{1}】的活动奖励";
            public static final String ACTIVITY_INCOME = "借款合同【{0}】的活动奖励【{1}发放】";
            public static final String PLATFORM_INCOME_ACTIVITY_AWARD = "因活动奖励撤回，平台收入{0}元";
            public static final String ERROR_PAY_AWARD = "支付错误的活动奖励{0}元，对应的错误流水id{1}";
            
        }
    }


}
