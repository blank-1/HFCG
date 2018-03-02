package com.xt.cfp.core.event.service;


import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.PayOffEnum;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-28 下午5:55.
 */
public class PrepayInterestKit {
    private static Logger logger = Logger.getLogger("eventTaskLogger");


    /**
     * 保存债权垫付明细
     * @param today
     * @param shouldInterest
     * @param totalTurnOut
     * @param systemAccountId
     * @param oldLendOrder
     * @param avcq
     * @param creditorRights
     * @param newDetail1
     * @param oldDetail
     * @param lendOrderService
     * @param rightsPrepayDetailService
     * @param lendOrderReceiveService
     * @return
     * @throws Exception
     */
    public static Object savePrepayInterest(Date today,BigDecimal shouldInterest,boolean totalTurnOut,long systemAccountId,
                                     LendOrder oldLendOrder,AccountValueChangedQueue avcq,
                                     CreditorRights creditorRights,RightsRepaymentDetail newDetail1,RightsRepaymentDetail oldDetail,
                                     CreditorRightsHistory creditorRightsHistory,LendOrderService lendOrderService,
                                     RightsPrepayDetailService rightsPrepayDetailService,
                                     LendOrderReceiveService lendOrderReceiveService) throws Exception {
        logger.debug("本期平台垫付利息:" + shouldInterest);

        Date now = new Date();
        //平台垫付这几天的利息
        BigDecimal shouldInterest2 = BigDecimalUtil.down(shouldInterest, 2);

        //保存垫付明细
        RightsPrepayDetail rightsPrepayDetail = new RightsPrepayDetail();

        rightsPrepayDetail.setAccountId(systemAccountId);
        rightsPrepayDetail.setCreditorRightsId(creditorRights.getCreditorRightsId());
        rightsPrepayDetail.setRightsRepaymentDetailId(oldDetail.getRightsRepaymentDetailId());
        rightsPrepayDetail.setCrHistoryId(creditorRightsHistory.getCreditorRightsHistoryId());
        rightsPrepayDetail.setRepaymentBalance(BigDecimal.ZERO);
        rightsPrepayDetail.setPrepayBalance(shouldInterest);
        rightsPrepayDetail.setPrepayBalance2(shouldInterest2);
        rightsPrepayDetail.setPrepayTime(today);
        rightsPrepayDetail.setIsPayOff(PayOffEnum.NO.value2Char());
        rightsPrepayDetail.setWillBackTime(oldDetail.getRepaymentDayPlanned());
        rightsPrepayDetailService.insert(rightsPrepayDetail);


        String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.PREPAYINTEREST_PAY, oldLendOrder.getOrderCode());
        AccountValueChanged avcPay = new AccountValueChanged(systemAccountId,shouldInterest,
                shouldInterest2, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_SYSTEMTRANSFER.getValue(),
                "RightsPrepayDetail", VisiableEnum.DISPLAY.getValue(),rightsPrepayDetail.getPrepayDetailId(),
                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                systemAccountId, now,desc,true);
        avcq.addAccountValueChanged(avcPay);
        logger.debug("->平台账户支出垫付利息:" + shouldInterest2);
        //出借人收益这几天的利息，存至理财子账户的可用金额
        String incomeDesc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVEINTEREST_INCOME,oldLendOrder.getOrderCode());
        AccountValueChanged avcIncome = new AccountValueChanged(oldLendOrder.getCustomerAccountId(),shouldInterest,
                shouldInterest2, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST.getValue(),
                "RightsPrepayDetail", VisiableEnum.DISPLAY.getValue(),rightsPrepayDetail.getPrepayDetailId(),
                AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                oldLendOrder.getLendOrderId(), now,incomeDesc,true);
        avcq.addAccountValueChanged(avcIncome);

        String descFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVEINTEREST_FREEZE,oldLendOrder.getOrderCode());
        AccountValueChanged avcFreeze = new AccountValueChanged(oldLendOrder.getCustomerAccountId(),shouldInterest,
                shouldInterest2, AccountConstants.AccountOperateEnum.FREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST.getValue(),
                "LendOrder", VisiableEnum.DISPLAY.getValue(),oldLendOrder.getLendOrderId(),
                AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                oldLendOrder.getLendOrderId(), now,descFreeze,true);
        avcq.addAccountValueChanged(avcFreeze);
        logger.debug("保存订单返息计划的实还金额");
        LendOrderReceive orderReceive = lendOrderReceiveService.getByOrderAndDate(oldLendOrder.getLendOrderId(),
                DateUtil.getDateLong(today));
        if (orderReceive == null) {
            orderReceive = lendOrderReceiveService.getLastByOrder(oldLendOrder.getLendOrderId());
        }
        orderReceive.setFactInterest(orderReceive.getFactInterest().add(BigDecimalUtil.down(shouldInterest, 2)));
        Map<String,Object> orderReceiveMap = new HashMap<String, Object>();
        orderReceiveMap.put("lendOrderReceiveDetailId",orderReceive.getReceiveId());
        orderReceiveMap.put("factReceiveInterest",orderReceive.getFactInterest().add(BigDecimalUtil.down(shouldInterest, 2)));
        lendOrderReceiveService.update(orderReceiveMap);

        logger.debug("保存订单收益（平台垫付的利息）");
        Map<String,Object> oldLendOrderMap = new HashMap<String,Object>();
        oldLendOrderMap.put("lendOrderId",oldLendOrder.getLendOrderId());
        oldLendOrderMap.put("currentProfit",oldLendOrder.getCurrentProfit().add(shouldInterest));
        oldLendOrderMap.put("currentProfit2",BigDecimalUtil.down(oldLendOrder.getCurrentProfit(),2));
        lendOrderService.update(oldLendOrderMap);
        logger.debug("保存订单收益（平台垫付的利息）完成");


        logger.debug("<-债权原理财订单账户收入平台垫付的利息:" + shouldInterest);


        //保存新债权分配明细
        newDetail1.setShouldInterest(newDetail1.getShouldInterest().subtract(rightsPrepayDetail.getPrepayBalance()));
        newDetail1.setShouldBalance(newDetail1.getShouldBalance().subtract(rightsPrepayDetail.getPrepayBalance()));

        newDetail1.setShouldBalance2(BigDecimalUtil.down(newDetail1.getShouldBalance(),2));
        newDetail1.setShouldCapital2(BigDecimalUtil.down(newDetail1.getShouldCapital(), 2));
        newDetail1.setShouldInterest2(newDetail1.getShouldBalance2().subtract(newDetail1.getShouldCapital2()));



        if (totalTurnOut) {
            //原债权的应还利息与应还金额减少（减少金额为平台垫付利息）
            oldDetail.setShouldInterest(oldDetail.getShouldInterest().subtract(shouldInterest));
            oldDetail.setShouldBalance(oldDetail.getShouldBalance().subtract(shouldInterest));
            oldDetail.setShouldInterest2(BigDecimalUtil.up(oldDetail.getShouldInterest(), 2));
            oldDetail.setShouldBalance2(BigDecimalUtil.up(oldDetail.getShouldBalance(),2));
        }

        logger.debug("\t第" + newDetail1.getSectionCode() + "期，应还本金:" + newDetail1.getShouldCapital() + ",应还利息:" + newDetail1.getShouldInterest());
        return null;
    }


}
