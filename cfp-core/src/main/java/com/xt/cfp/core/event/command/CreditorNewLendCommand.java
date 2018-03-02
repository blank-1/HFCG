package com.xt.cfp.core.event.command;


import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-23 下午2:37.
 */
public class CreditorNewLendCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private long customerAccountId;
    private BigDecimal turnBalance;
    private boolean payFromFrozen;
    private LendOrder newLendOrder;
    private AccountValueChangedQueue avcq;
    private UserAccountService userAccountService;
    private LendOrderService lendOrderService;
    private CreditorRights oldCreditorRights;

    public CreditorNewLendCommand( boolean payFromFrozen, long customerAccountId, BigDecimal turnBalance,
                                  LendOrder newLendOrder,CreditorRights oldCreditorRights,AccountValueChangedQueue avcq,UserAccountService userAccountService,
                                  LendOrderService lendOrderService) {
        this.customerAccountId = customerAccountId;
        this.turnBalance = turnBalance;
        this.payFromFrozen = payFromFrozen;
        this.newLendOrder = newLendOrder;
        this.avcq = avcq;
        this.userAccountService = userAccountService;
        this.lendOrderService = lendOrderService;
        this.oldCreditorRights = oldCreditorRights;
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("新债权扣款");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        Date now = new Date();
        taskExecLog.setExecTime(now);
        taskExecLog.setTaskId(TaskInfo.TASKID_CREDITORNEWLEND);
        //冻结或支付出借账户，债权价值相应的金额
        if (payFromFrozen) {
            String descUnfreeze = "债权转让，解冻出借资金";
            AccountValueChanged avcUnfrozen = new AccountValueChanged(customerAccountId, turnBalance,
                    turnBalance, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_FINANCING.getValue(),
                    "LendOrder", VisiableEnum.DISPLAY.getValue(), newLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    newLendOrder.getLendOrderId(), now, descUnfreeze, true);
            avcq.addAccountValueChanged(avcUnfrozen);
            String descPay = "债权转让，支出出借资金";
            AccountValueChanged avcPay = new AccountValueChanged(customerAccountId, turnBalance,
                    turnBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_FINANCING.getValue(),
                    "LendOrder", VisiableEnum.DISPLAY.getValue(), newLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    newLendOrder.getLendOrderId(), now, descPay, true);
            avcq.addAccountValueChanged(avcPay);
            logger.debug("新债权状态为生效，因此债权新理财订单账户从冻结金额中支出：" + turnBalance);
        } else {

            String descPay = "债权转让，支出出借资金";//本金
            AccountValueChanged avcPay = new AccountValueChanged(customerAccountId, turnBalance,
                    turnBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_FINANCING.getValue(),
                    "LendOrder", VisiableEnum.DISPLAY.getValue(), newLendOrder.getLendOrderId(), AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    newLendOrder.getLendOrderId(), now, descPay, true);
            avcq.addAccountValueChanged(avcPay);
            newLendOrder.setForLendBalance(newLendOrder.getForLendBalance().subtract(oldCreditorRights.getLendPrice()));
            logger.debug("新债权状态为生效，因此债权新理财订单账户从可用金额中支出：" + turnBalance);
        }

        //newLendOrder.setForLendBalance(newLendOrder.getForLendBalance().subtract(turnBalance));
        logger.debug("修改债权新理财订单的待理财金额（相应减少）为：" + newLendOrder.getForLendBalance());
        Map<String,Object> newLendOrderMap = new HashMap<String,Object>();
        newLendOrderMap.put("lendOrderId",newLendOrder.getLendOrderId());
        newLendOrderMap.put("forLendBalance",newLendOrder.getForLendBalance());
        lendOrderService.update(newLendOrderMap);
        logger.debug("修改债权的新理财订单");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("新债权扣款成功.");
        logger.debug("新债权扣款成功");
        return null;
    }
}
