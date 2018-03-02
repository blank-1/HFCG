package com.xt.cfp.core.event.command;


import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.util.BigDecimalUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-6-11 下午6:02.
 */
public class SaveLendOrderProfitCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private BigDecimal theLendFeesBalance;
    private BigDecimal theLendInterest;

    private LendOrder lendOrder;
    private LendOrderService lendOrderService;

    public SaveLendOrderProfitCommand(BigDecimal theLendFeesBalance, BigDecimal theLendInterest, LendOrder lendOrder, LendOrderService lendOrderService) {
        this.theLendFeesBalance = theLendFeesBalance;
        this.theLendInterest = theLendInterest;
        this.lendOrder = lendOrder;
        this.lendOrderService = lendOrderService;
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始保存订单收益（即回款利息减扣费金额）****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_SAVELENDORDERPROFIT);
        if (BigDecimalUtil.compareTo(theLendInterest, theLendFeesBalance, true, 2) > 0) {
            //保存订单收益(即回款利息)
            BigDecimal profit = lendOrderService.getNewestProfit(lendOrder.getLendOrderId());
            lendOrderService.updateNewestProfit(lendOrder.getLendOrderId(), profit.add(theLendInterest));
            Long lendOrderPid = lendOrder.getLendOrderPId() ;
            while(true){
            	if(lendOrderPid != null){
            		LendOrder lendTemp = lendOrderService.findById(lendOrderPid);
            		if(lendTemp != null){
            			BigDecimal profitTemp = lendTemp.getCurrentProfit(); 
            			lendOrderService.updateNewestProfit(lendTemp.getLendOrderId(), profitTemp.add(theLendInterest));
            			lendOrderPid = lendTemp.getLendOrderPId();
            		}else{
            			break;
            		}
            	}else{
            		break;
            	}
        	}

       }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("保存订单收益（即回款利息减扣费金额）完成");
        logger.debug("****保存订单收益（即回款利息减扣费金额）完成****");
        return null;
    }
}
