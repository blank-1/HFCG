package com.xt.cfp.core.event;


import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.CreditorErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.event.command.*;
import com.xt.cfp.core.event.pojo.EventTriggerInfo;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsHistory;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-8-27 下午3:31.
 */
public class TurnCreditorEvent extends Event {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private long lendOrderId;//旧订单id
    private long creditorId;//旧债权id
    private BigDecimal turnBalance;//转让id
    private long newLendOrderId;
    private boolean totalTurnOut;//是否全部转出
    private boolean payFromFrozen;//
    private AccountValueChangedQueue avcq;

    private CreditorRightsService creditorRightsService;
    private UserAccountService userAccountService;
    private UserInfoService userInfoService;

    private LendOrderService lendOrderService;
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    private RightsPrepayDetailService rightsPrepayDetailService;
    private LendOrderReceiveService lendOrderReceiveService;
    private UserAccountOperateService userAccountOperateService;
    private ConstantDefineCached constantDefineCached;


    public TurnCreditorEvent(long lendOrderId, long creditorId, BigDecimal turnBalance, long newLendOrderId,
                             boolean totalTurnOut, boolean payFromFrozen,AccountValueChangedQueue avcq, CreditorRightsService creditorRightsService,
                             UserAccountService userAccountService, LendOrderService lendOrderService,
                             RightsRepaymentDetailService rightsRepaymentDetailService,
                             RightsPrepayDetailService rightsPrepayDetailService, LendOrderReceiveService lendOrderReceiveService,
                             UserInfoService userInfoService,UserAccountOperateService userAccountOperateService,
                             ConstantDefineCached constantDefineCached) {
        this.lendOrderId = lendOrderId;
        this.creditorId = creditorId;
        this.turnBalance = turnBalance;
        this.newLendOrderId = newLendOrderId;
        this.totalTurnOut = totalTurnOut;
        this.payFromFrozen = payFromFrozen;
        this.avcq = avcq;
        this.creditorRightsService = creditorRightsService;
        this.userAccountService = userAccountService;
        this.lendOrderService = lendOrderService;
        this.rightsRepaymentDetailService = rightsRepaymentDetailService;
        this.rightsPrepayDetailService = rightsPrepayDetailService;
        this.lendOrderReceiveService = lendOrderReceiveService;
        this.userInfoService = userInfoService;
        this.userAccountOperateService = userAccountOperateService;
        this.constantDefineCached = constantDefineCached;
    }

    @Override
    public void fire() throws Exception {
        logger.debug("****开始转让债权****");
        Date today = new Date();
        //旧债权
        CreditorRights creditorRights = creditorRightsService.findById(creditorId, true);
        char newLenderType = CreditorRights.LENDERTYPE_LENDER;
        if (BigDecimalUtil.compareTo(creditorRights.getRightsWorth(), BigDecimal.ZERO, true, 2) <= 0) {
            return;
        }

        boolean canntTurn = false;
        long lendAccountId = creditorRights.getLendAccountId();
        long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());

        LendOrder oldLender = lendOrderService.findById(lendOrderId);
        LendOrder newLendOrder = lendOrderService.findById(newLendOrderId);

        logger.debug("债权的原理财合同订单：" + oldLender.getOrderCode());
        logger.debug("要转让的新理财合同订单：" + newLendOrder.getOrderCode());

        long customerAccountId = newLendOrder.getCustomerAccountId();

        List<RightsRepaymentDetail> rightsRepaymentDetails = rightsRepaymentDetailService.getDetailListByRightsId(creditorId);

        Date nextRepaymentDate = null;
        BigDecimal sumInterest = BigDecimal.ZERO;
        Map<Long, BigDecimal> interestMap = new HashMap<Long, BigDecimal>();

        BigDecimal turnRatio = turnBalance.divide(creditorRights.getRightsWorth(), 18, BigDecimal.ROUND_CEILING);

        //计算利息
        CalculateSystemPrepayInterestCommand calculateSystemPrepayInterestCommond = new CalculateSystemPrepayInterestCommand(
                rightsRepaymentDetails, today, canntTurn, turnRatio, nextRepaymentDate, creditorRights);
        interestMap = calculateSystemPrepayInterestCommond.getInterestMap();
        nextRepaymentDate = calculateSystemPrepayInterestCommond.getNextRepaymentDate();
        canntTurn = calculateSystemPrepayInterestCommond.isCanntTurn();
        addCommand(calculateSystemPrepayInterestCommond);


        if (canntTurn) {
            throw new SystemException(CreditorErrorCode.CREDITORCANTTURN);
        }

        //保存新债权、债权变更历史
        CreditorRightsHistory creditorRightsHistory = new CreditorRightsHistory();
        CreditorRights newCreditors = new CreditorRights();
        NewCreditorRightsCommand newCreditorRightsCommand = new NewCreditorRightsCommand(creditorRights, newCreditors, creditorRightsHistory,
                newLendOrder, today, turnBalance, nextRepaymentDate, newLenderType, totalTurnOut, creditorRightsService,userInfoService);
        creditorRightsHistory = newCreditorRightsCommand.getCreditorRightsHistory();
        newCreditors = newCreditorRightsCommand.getNewRights();
        totalTurnOut = newCreditorRightsCommand.isTotalTurnOut();
        addCommand(newCreditorRightsCommand);

        //保存新债权分配明细
        NewRightsRepaymentDetailCommand rightsRepaymentDetailCommand = new NewRightsRepaymentDetailCommand(today, rightsRepaymentDetails,
                newCreditors, customerAccountId, newLendOrderId, totalTurnOut, turnRatio, avcq, creditorRightsHistory, interestMap,
                systemAccountId, oldLender, lendOrderId, lendAccountId, creditorRights, lendOrderService, rightsRepaymentDetailService, userAccountService,
                rightsPrepayDetailService, sumInterest, lendOrderReceiveService, userInfoService);
        addCommand(rightsRepaymentDetailCommand);

        BigDecimal oldCapital = BigDecimal.ZERO;
        //修改债权状态
        if (totalTurnOut) {
            creditorRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.value2Char());
            creditorRights.setCompleteTime(new Date());
            oldCapital = creditorRights.getRightsWorth();
            logger.debug("原债权全部转出，修改债权状态为已转出");
        } else {
            oldCapital = turnBalance;
        }


        //原债权回款
        TakeBackLendOrderCommand takeBackLendOrderCommand = new TakeBackLendOrderCommand(userAccountService, lendAccountId,
                oldCapital, creditorRightsHistory, creditorRights,newLendOrder, lendOrderId, turnBalance, avcq, creditorRightsService, lendOrderService);
        addCommand(takeBackLendOrderCommand);

        //新债权扣款
        CreditorNewLendCommand creditorNewLendCommand = new CreditorNewLendCommand( payFromFrozen, customerAccountId, turnBalance,
                newLendOrder,creditorRights, avcq, userAccountService, lendOrderService);
        addCommand(creditorNewLendCommand);


        EventTriggerInfo eventTriggerInfo = new EventTriggerInfo();
        eventTriggerInfo.setEventId(Event.EVENT_TURNCREDITOR);
        eventTriggerInfo.setHappenResult(EventTriggerInfo.HAPPENRESULT_FAILURE);
        eventTriggerInfo.setHappenTime(new Date());
        eventTriggerInfo.setTriggerObjId(String.valueOf(oldLender.getLendOrderId()));
        eventTriggerInfo.setTriggerType("LendOrder");
        super.setEventTriggerInfo(eventTriggerInfo);
        super.fire();
        userAccountOperateService.execute(avcq);
        logger.debug("****债权转让完毕.****");
    }
}
