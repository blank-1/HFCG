package com.xt.cfp.core.event.command;


import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CreditorRightsHistory;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-23 下午1:31.
 */
public class TakeBackLendOrderCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private UserAccountService userAccountService;
    private long lendAccountId;
    private BigDecimal oldCapital;
    private CreditorRightsHistory creditorRightsHistory;
    private CreditorRights creditorRights;
    private long lendOrderId;
    private BigDecimal turnBalance;
    private AccountValueChangedQueue avcq;
    private CreditorRightsService creditorRightsService;
    private LendOrderService lendOrderService;
    private LendOrder newLendOrder ;
    public TakeBackLendOrderCommand(UserAccountService userAccountService, long lendAccountId, BigDecimal oldCapital,
                                    CreditorRightsHistory creditorRightsHistory, CreditorRights creditorRights,LendOrder newLendOrder,
                                    long lendOrderId, BigDecimal turnBalance,AccountValueChangedQueue avcq, CreditorRightsService creditorRightsService,
                                    LendOrderService lendOrderService) {
        this.userAccountService = userAccountService;
        this.lendAccountId = lendAccountId;
        this.oldCapital = oldCapital;
        this.creditorRightsHistory = creditorRightsHistory;
        this.creditorRights = creditorRights;
        this.newLendOrder = newLendOrder;
        this.lendOrderId = lendOrderId;
        this.turnBalance = turnBalance;
        this.avcq = avcq;
        this.creditorRightsService = creditorRightsService;
        this.lendOrderService = lendOrderService;
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("债权开始回款");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        Date now = new Date();
        taskExecLog.setExecTime(now);
        taskExecLog.setTaskId(TaskInfo.TASKID_TAKEBACKLENDORDER);

        if (BigDecimalUtil.compareTo(oldCapital, BigDecimal.ZERO, false, 2) > 0) {

            //出借订单冻结出资金额
            String desc = "回款收入本金";
            AccountValueChanged avcIncome = new AccountValueChanged(lendAccountId, oldCapital,
                    oldCapital, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEREPAYMENT.getValue(),
                    "CreditorRightsHistory", VisiableEnum.DISPLAY.getValue(), creditorRightsHistory.getCreditorRightsHistoryId(),
                    AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    lendOrderId, now, desc, true);
            avcq.addAccountValueChanged(avcIncome);

            logger.debug("债权理财订单账户，回款本金:" + oldCapital);
            String desc_instreast = "回款收入利息";
            BigDecimal price = creditorRights.getLendPrice();
            BigDecimal intreast = price.subtract(oldCapital);
            if (BigDecimal.ZERO.compareTo(intreast)>0){

                String desc1 = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LENDER_PREPAYINTEREST_PAY, newLendOrder.getOrderCode());
                AccountValueChanged avcPay = new AccountValueChanged(newLendOrder.getCustomerAccountId(),intreast,
                        intreast, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.LENDER_FEESTYPE_SYSTEMTRANSFER.getValue(),
                        AccountConstants.AccountChangedTypeEnum.LEND.getValue(), VisiableEnum.DISPLAY.getValue(),newLendOrder.getLendOrderId(),
                        AccountConstants.OwnerTypeEnum.ORDER.getValue(),
                        newLendOrder.getCustomerAccountId(), now,desc1,true);
                avcq.addAccountValueChanged(avcPay);

                AccountValueChanged avcInstreast = new AccountValueChanged(lendAccountId, intreast,
                        intreast, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST.getValue(),
                        "CreditorRightsHistory", VisiableEnum.DISPLAY.getValue(), creditorRightsHistory.getCreditorRightsHistoryId(),
                        AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                        lendOrderId, now, desc_instreast, true);
                avcq.addAccountValueChanged(avcInstreast);

                logger.debug("债权理财订单账户，回款利息:" + oldCapital);
            }


            //转让后的债权价值
            creditorRights.setRightsWorth(creditorRights.getRightsWorth().subtract(turnBalance));
            //转让时的债权价格
            creditorRights.setLendPrice(price);


            logger.debug("债权的债权价值更新为：" + creditorRights.getRightsWorth());
            Map<String,Object> creditorRightsMap = new HashMap<String,Object>();
            creditorRightsMap.put("creditorRightsId",creditorRights.getCreditorRightsId());
            creditorRightsMap.put("rightsWorth",creditorRights.getRightsWorth());
            creditorRightsMap.put("lendPrice",creditorRights.getLendPrice());
            creditorRightsService.update(creditorRightsMap);
            logger.debug("修改债权完成");


            LendOrder lendOrder = lendOrderService.findById(lendOrderId);
            lendOrder.setForLendBalance(lendOrder.getForLendBalance().add(turnBalance));
            logger.debug("债权的理财订单的待理财金额增加（原待理财金额+回款本金+垫付利息）:" + lendOrder.getForLendBalance());
            Map<String,Object> lendOrderMap = new HashMap<String,Object>();
            lendOrderMap.put("lendOrderId",lendOrder.getLendOrderId());
            lendOrderMap.put("forLendBalance",lendOrder.getForLendBalance());
            lendOrderService.update(lendOrderMap);
            logger.debug("修改债权的理财订单完成");

            taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
            taskExecLog.setLogInfo("原债权回款成功.回款本金:" + oldCapital.toPlainString());
        } else {
            logger.debug("原债权回款本金为0");
        }
        logger.debug("原债权回款完成");
        return null;
    }
}
