package com.xt.cfp.core.event.pojo;

/**
 * Created by Renyulin on 14-6-13 上午11:09.
 */
public class TaskInfo {
    private long taskId;
    private long eventId;
    private String taskName;
    private String executeImpl;

    /**
     * 1为支付借款合同罚息，2为回款至出借人账户,3为修改借款申请状态,4为计算并修改罚息状态，5为还逾期费，6为计算还款扣费(日常费用)
     * 7为出借人回款扣费,8为计算并支出平台垫付利息，9为保存卡划扣流水，10为保存订单收益,11为保存还款记录,12为修改还款记录,13为债权解除锁定
     *
     */
    public static long TASKID_PAYREPAYMENTINTEREST = 1;
    public static long TASKID_REPAYMENT2LENDER = 2;
    public static long TASKID_REPAYMENTCOMPLETELOANAPP = 3;
    public static long TASKID_REPAYMENTDEFAULTINTEREST = 4;
    public static long TASKID_REPAYMENTDELAYFEES = 5;
    public static long TASKID_REPAYMENTFEES = 6;
    public static long TASKID_REPAYMENTLENDERFEES = 7;
    public static long TASKID_REPAYMENTRIGHTSPREPAY = 8;
    public static long TASKID_SAVECARDFLOW = 9;
    public static long TASKID_SAVELENDORDERPROFIT = 10;
    public static long TASKID_SAVEREPAYMENTRECORD = 11;
    public static long TASKID_UPDATEREPAYMENTRECORD = 12;
    public static long TASKID_UNLOCKCREDITOR = 13;
    public static long TASKID_CALCULATESYSTEMPREPAYINTEREST = 14;//计算平台垫付利息
    public static long TASKID_SAVENEWCREDITOR = 15;//保存新债权
    public static long TASKID_SAVENEWCREDITORDETAIL = 16;//保存新债权明细
    public static long TASKID_TAKEBACKLENDORDER = 17;//原债权回款
    public static long TASKID_CREDITORNEWLEND = 18;//新债权扣款
    public static long TASKID_PREPAYBYREPAYMENTPLAN = 19;//平台按还款计划垫付利息
    public static long TASKID_TAKEBACKINTERESTTOLENDER = 20;//各出借人利息回款
    public static long TASKID_SYSTEMAUTOLENDORDER = 21;//平台自动购买生成订单
    public static long TASKID_TURNCREDITORTOSYSTEM = 22;//债权转让给平台
    public static long TASKID_TAKEBACKCAPITALTOLENDER = 23;//各出借人本金回款
    public static long TASKID_SAVEACCURACY = 24;//平台保存精度误差

    public static long TASKID_EARLYREPAYMENTFEES = 25;//提前还贷违约扣费
    public static long TASKID_EARLYREPAYMENTTOLENDORDER = 26;//提前还贷本金返还出借人


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getExecuteImpl() {
        return executeImpl;
    }

    public void setExecuteImpl(String executeImpl) {
        this.executeImpl = executeImpl;
    }
}
