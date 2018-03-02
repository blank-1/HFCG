package com.xt.cfp.core.event.command;


import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-8-27 下午4:47.
 */
public class NewCreditorRightsCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private CreditorRights creditorRights;
    private CreditorRights newRights;
    private LendOrder newLendOrder;
    private Date today;
    private BigDecimal turnBalance;
    private Date nextRepaymentDate;
    private char newLenderType;
    private boolean totalTurnOut;
    private CreditorRightsHistory creditorRightsHistory;

    private CreditorRightsService creditorRightsService;
    private UserInfoService userInfoService;

    public NewCreditorRightsCommand(CreditorRights creditorRights, CreditorRights newRights, CreditorRightsHistory creditorRightsHistory,
                                    LendOrder newLendOrder, Date today, BigDecimal turnBalance,
                                    Date nextRepaymentDate, char newLenderType,  boolean totalTurnOut,
                                    CreditorRightsService creditorRightsService,UserInfoService userInfoService) throws Exception {
        this.creditorRights = creditorRights;
        this.newRights = newRights;
        this.creditorRightsHistory = creditorRightsHistory;
        this.newLendOrder = newLendOrder;
        this.today = today;
        this.turnBalance = turnBalance;
        this.nextRepaymentDate = nextRepaymentDate;
        this.newLenderType = newLenderType;
        this.totalTurnOut = totalTurnOut;
        this.creditorRightsService = creditorRightsService;
        this.userInfoService = userInfoService;

        if (BigDecimalUtil.compareTo(this.turnBalance, creditorRights.getRightsWorth(), true, 2) == 0) {
            this.totalTurnOut = true;
            logger.debug("本次为债权全额转出");
        } else {
            logger.debug("本次为债权部分转出");
        }
    }

    @Override
    public Object execute() throws Exception {
        //保存新债权
        logger.debug("创建新债权");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_SAVENEWCREDITOR);

        UserInfoVO newLenderUser = userInfoService.getUserExtByUserId(newLendOrder.getLendUserId());
        BeanUtils.copyProperties(creditorRights, newRights);
        newRights.setLendOrderId(newLendOrder.getLendOrderId());
        newRights.setAgreementStartDate(today);
        newRights.setRightsWorth(turnBalance);
        newRights.setLendPrice(turnBalance.setScale(2, BigDecimal.ROUND_UP));
        newRights.setBuyPrice(newRights.getLendPrice());
        newRights.setLendTime(today);
        newRights.setCreateTime(today);
        newRights.setFirstRepaymentDate(nextRepaymentDate);
        newRights.setLenderType(newLenderType);
        newRights.setLendSystemLoginName(newLenderUser.getLoginName());
        newRights.setLendCustomerName(newLenderUser.getRealName());
        newRights.setLendCustomerCerCode(newLenderUser.getIdCard());

        newRights.setLendAccountId(newLendOrder.getCustomerAccountId());
        newRights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char());
        newRights.setFromWhere(CreditorRightsFromWhereEnum.TURN.value2Char());
        newRights.setDisplayState(DisplayEnum.DISPLAY.value2Char());

        creditorRightsService.insert(newRights);
        logger.debug("新债权的省心计划订单为：" + newRights.getLendOrderId() + ",开始时间为" + DateUtil.getDateLong(newRights.getAgreementStartDate()) + "," +
                "债权价值为" + newRights.getRightsWorth() + "，债权价格为" + newRights.getLendPrice() + ",首次还款日为:" +
                DateUtil.getDateLong(newRights.getFirstRepaymentDate()));

        if (BigDecimalUtil.compareTo(newRights.getRightsWorth(), creditorRights.getRightsWorth(), true, 2) == 0) {
            totalTurnOut = true;
            logger.debug("本次为债权全额转出");
        } else {
            logger.debug("本次为债权部分转出");
        }

        //保存债权变更历史
        creditorRightsHistory.setCreditorRightsIdBeforeChange(creditorRights.getCreditorRightsId());
        creditorRightsHistory.setCreditorRightsIdAfterChange(newRights.getCreditorRightsId());
        creditorRightsHistory.setChangeType(CreditorRightsHistory.CHANGETYPE_TURNOUT);
        creditorRightsHistory.setChangeTime(today);
        creditorRightsService.newCreditorRightsHistory(creditorRightsHistory);
        logger.debug("保存债权变更历史，原债权ID:" + creditorRightsHistory.getCreditorRightsIdBeforeChange() + "，新债权ID:" + creditorRightsHistory.getCreditorRightsIdAfterChange());
        //todo 修改出借明细的投标状态

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("新债权及债权变更历史保存成功.");
        return null;
    }

    public CreditorRightsHistory getCreditorRightsHistory() {
        return creditorRightsHistory;
    }

    public boolean isTotalTurnOut() {
        return totalTurnOut;
    }

    public CreditorRights getNewRights() {
        return newRights;
    }
}
