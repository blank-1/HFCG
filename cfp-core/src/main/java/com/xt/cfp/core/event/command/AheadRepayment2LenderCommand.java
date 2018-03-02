package com.xt.cfp.core.event.command;


import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.DistributeStatusEnum;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.message.WechatMessageBody;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午5:36.
 */
public class AheadRepayment2LenderCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private boolean theLastRepayment;
    private BigDecimal repayDetailBalance;
    private CreditorRights creditorRights;
    private int lendRightIndex;
    private List<RightsRepaymentDetail> rightsRepaymentDetails;
    private BigDecimal balanceAfterFees;
    private BigDecimal currentShouldCalital;
    private BigDecimal theLastLendCalital;
    private BigDecimal theLendInterest;
    private BigDecimal sumDefaultInterest;
    private long adminId;
    private Date repaymentDate;
    private int delayDays;

    private LoanApplication loanApplication;
    private LoanPublish loanPublish;
    private LendOrder lendOrder;
    private RepaymentRecord repaymentRecord;
    private RightsRepaymentDetail rightsRepaymentDetail;
    private AccountValueChangedQueue avcq;

    private CreditorRightsService creditorRightsService;
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    private LendOrderService lendOrderService;
    private LendOrderReceiveService lendOrderReceiveService;
    private UserAccountService userAccountService;
    private RepaymentRecordService repaymentRecordService;
    private UserInfoService userInfoService;
    private UserOpenIdService userOpenIdService;
    private ConstantDefineService constantDefineService;

    public AheadRepayment2LenderCommand(boolean theLastRepayment, BigDecimal repayDetailBalance, CreditorRights creditorRights,
                                        int lendRightIndex, List<RightsRepaymentDetail> rightsRepaymentDetails,
                                        BigDecimal balanceAfterFees, BigDecimal currentShouldCalital, BigDecimal theLastLendCalital,
                                        BigDecimal theLendInterest, BigDecimal sumDefaultInterest, long adminId, Date repaymentDate,
                                        int delayDays, LoanApplication loanApplication, LoanPublish loanPublish, LendOrder lendOrder, RepaymentRecord repaymentRecord,
                                        RightsRepaymentDetail rightsRepaymentDetail, AccountValueChangedQueue avcq, CreditorRightsService creditorRightsService,
                                        RightsRepaymentDetailService rightsRepaymentDetailService, LendOrderService lendOrderService,
                                        LendOrderReceiveService lendOrderReceiveService, UserAccountService userAccountService, RepaymentRecordService repaymentRecordService, UserInfoService userInfoService, UserOpenIdService userOpenIdService, ConstantDefineService constantDefineService) throws Exception {
        this.theLastRepayment = theLastRepayment;
        this.repayDetailBalance = repayDetailBalance;//平台垫付的利息
        this.creditorRights = creditorRights;
        this.lendRightIndex = lendRightIndex;
        this.rightsRepaymentDetails = rightsRepaymentDetails;
        this.balanceAfterFees = balanceAfterFees;
        this.currentShouldCalital = currentShouldCalital;
        this.theLastLendCalital = theLastLendCalital;
        this.theLendInterest = theLendInterest;
        this.sumDefaultInterest = sumDefaultInterest;
        this.adminId = adminId;
        this.repaymentDate = repaymentDate;
        this.delayDays = delayDays;
        this.loanApplication = loanApplication;
        this.loanPublish = loanPublish;
        this.lendOrder = lendOrder;
        this.repaymentRecord = repaymentRecord;
        this.rightsRepaymentDetail = rightsRepaymentDetail;
        this.avcq = avcq;
        this.creditorRightsService = creditorRightsService;
        this.rightsRepaymentDetailService = rightsRepaymentDetailService;
        this.lendOrderService = lendOrderService;
        this.lendOrderReceiveService = lendOrderReceiveService;
        this.userAccountService = userAccountService;
        this.repaymentRecordService = repaymentRecordService;
        this.userInfoService = userInfoService;
        this.userOpenIdService = userOpenIdService;
        this.constantDefineService = constantDefineService;

        BigDecimal theLendBalance;
        if (theLastRepayment) {

            this.theLendInterest = rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest());

        } else {
            theLendBalance = balanceAfterFees;
            if (BigDecimalUtil.compareTo(theLendBalance, rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest()), true, 2) >= 0) {

                this.theLendInterest = rightsRepaymentDetail.getShouldInterest2();
            } else {
                this.theLendInterest = theLendBalance;
            }
        }

    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始针对各个省心计划进行回款（含回款、债权价值减少及返息）****");
        Date now = new Date();

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(now);
        taskExecLog.setTaskId(TaskInfo.TASKID_PAYREPAYMENTINTEREST);

        BigDecimal theLendBalance;
        BigDecimal theLendCalital = BigDecimal.ZERO;

        Map<String, Object> creditorRightsMap = new HashMap<String, Object>();
        if (theLastRepayment) {
            //本期还的本金与利息，回款至省心计划的待理财金额

            theLendBalance = rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getFactCalital());
            lendOrder.setForLendBalance(lendOrder.getForLendBalance().add(theLendBalance));

            theLendCalital = rightsRepaymentDetail.getShouldCapital2().subtract(rightsRepaymentDetail.getFactCalital());

            //将债权价值相应减少
            creditorRights.setRightsWorth(creditorRights.getRightsWorth().subtract(theLendCalital));
            creditorRightsMap.put("rightsWorth", creditorRights.getRightsWorth());
            rightsRepaymentDetail.setFactBalance(rightsRepaymentDetail.getShouldCapital2());
            rightsRepaymentDetail.setFactCalital(rightsRepaymentDetail.getShouldCapital2());
            rightsRepaymentDetail.setIsPayOff(RightsRepaymentDetail.ISPAYOFF_YES);
            rightsRepaymentDetail.setRightsDetailState(RightsRepaymentDetail.RIGHTSDETAILSTATE_BEFORE_COMPLETE);
            creditorRights.setCompleteTime(now);
            creditorRightsMap.put("completeTime", now);


        }

        BigDecimal rightsWorth = creditorRights.getRightsWorth();
        creditorRights.setLendPrice(rightsWorth.setScale(2, BigDecimal.ROUND_UP));
        creditorRightsMap.put("lendPrice", creditorRights.getLendPrice());
        creditorRightsMap.put("creditorRightsId", creditorRights.getCreditorRightsId());
        //保存债权更改后的信息(债权价值)
        creditorRightsService.update(creditorRightsMap);
        rightsRepaymentDetail.setIsDelay(RightsRepaymentDetail.ISDELAY_NO);

        //把还款记录保存到债权分配明细中
        rightsRepaymentDetail.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
        Map<String, Object> rightsRepaymentMap = BeanUtil.bean2Map(rightsRepaymentDetail);
        rightsRepaymentDetailService.update(rightsRepaymentMap);
        //修改省心计划订单的待理财金额
        Map<String, Object> lendOrderMap = new HashMap<String, Object>();
        lendOrderMap.put("forLendBalance", lendOrder.getForLendBalance());
        lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
        lendOrderService.update(lendOrderMap);
        //修改还款记录的分配状态
        repaymentRecord.setDistributeStatus(DistributeStatusEnum.DISTRIBUTED.value2Char());
        repaymentRecordService.update(repaymentRecord);

        //保存还款分配明细
        RepaymentRecordDetail repaymentRecordDetail = new RepaymentRecordDetail();
        repaymentRecordDetail.setLendOrderId(lendOrder.getLendOrderId());
        repaymentRecordDetail.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
        repaymentRecordDetail.setLendUserId(lendOrder.getLendUserId());
        repaymentRecordDetail.setRightsRepaymentDetailId(rightsRepaymentDetail.getRightsRepaymentDetailId());
        repaymentRecordDetail.setSectionCode(rightsRepaymentDetail.getSectionCode());
        repaymentRecordDetail.setFactReceiveCapital(rightsRepaymentDetail.getFactCalital());
        repaymentRecordDetail.setFactReceiveInterest(rightsRepaymentDetail.getFactInterest());
        repaymentRecordDetail.setFactReceivePenalty(rightsRepaymentDetail.getDepalFine());
        repaymentRecordDetail.setFactReceiveBalance(rightsRepaymentDetail.getFactBalance());
        repaymentRecordService.addRecordDetail(repaymentRecordDetail);

        //出借账户收入还款-本金
        if (BigDecimalUtil.compareTo(theLendCalital, BigDecimal.ZERO, true, 2) > 0) {
            //计算粮票儿 todo

            String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AHEAD_REPAYMENT_IN, loanPublish.getLoanTitle());
            AccountValueChanged avcIncome = new AccountValueChanged(rightsRepaymentDetail.getLendAccountId(), theLendCalital,
                    BigDecimalUtil.down(theLendCalital, 2), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEREPAYMENT.getValue(),
                    "RightsRepaymentDetail", VisiableEnum.DISPLAY.getValue(), rightsRepaymentDetail.getRightsRepaymentDetailId(),
                    AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    lendOrder.getLendOrderId(), now, descIncome, true);
            avcq.addAccountValueChanged(avcIncome);

        }

        //todo 判断是否需要冻结返息
        // 更新回款计划的实还金额
        boolean requiredToInterest = false;
        LendOrderReceive orderReceive = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
            orderReceive = lendOrderReceiveService.getByOrderAndDate(lendOrder.getLendOrderId(), DateUtil.getDateLong(repaymentDate));
        } else {
            orderReceive = lendOrderReceiveService.getByOrderAndSectionCode(lendOrder.getLendOrderId(), rightsRepaymentDetail.getSectionCode());
        }
        if (orderReceive == null) {

            orderReceive = lendOrderReceiveService.getFirstByOrder(lendOrder.getLendOrderId());
        } else {
            UserAccount lendAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
            if (BigDecimalUtil.compareTo(lendAccount.getFrozeValue2(), orderReceive.getShouldInterest2(), 2) >= 0) {
                requiredToInterest = false;
            }
        }

        //如果订单的返息周期为退出时返息，或者订单类型为债权类订单，则返息不冻结
        requiredToInterest = !((lendOrder.getToInterestPoint().equals(LendProduct.TOINTERESTPOINT_BEQUIT)) || (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())));
        orderReceive.setFactCapital(orderReceive.getFactCapital().add(BigDecimalUtil.down(theLendCalital, 2)));
        orderReceive.setFactInterest(orderReceive.getFactInterest().add(BigDecimalUtil.down(theLendInterest, 2)));

        Map<String, Object> orderReceiveMap = BeanUtil.bean2Map(orderReceive);
        lendOrderReceiveService.update(orderReceiveMap);

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("省心计划:" + lendOrder.getOrderCode() + "  回款完成（含回款、债权价值减少及返息）");
        logger.debug("****各个省心计划进行回款（含回款、债权价值减少及返息）完毕****");
        return null;
    }

}
