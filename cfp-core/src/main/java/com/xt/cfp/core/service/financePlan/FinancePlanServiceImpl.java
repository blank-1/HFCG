package com.xt.cfp.core.service.financePlan;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.FinancePlanConstants;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.util.ApplicationContextUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/12/8.
 */
@Service
public class FinancePlanServiceImpl implements FinancePlanService {

    private static Logger logger = Logger.getLogger("financePlanServiceImpl");
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private LendOrderService lendOrderService;

    @Override
    public void creditorRightsAutoMatch() {
        logger.debug("****开始债权自动匹配****");
        Map<String, Object> lendMap = new HashMap<String, Object>();
        List orderStateList = new ArrayList();
        orderStateList.add(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue());
        orderStateList.add(LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue());
        lendMap.put("orderStateList", orderStateList);
        lendMap.put("productType", LendProductTypeEnum.FINANCING.getValue());
        List<LendOrder> lendOrderList = lendOrderService.findHaveBalanceOrder(lendMap);


        logger.debug("查询所有已支付、债权匹配中、理财中且待理财金额大于0的省心计划");
        for (LendOrder lendOrder : lendOrderList) {
            try {
                FinancePlanService financePlanService = ApplicationContextUtil.getBean(FinancePlanServiceImpl.class);
                financePlanService.match(lendOrder);
            } catch (Exception e) {
                logger.debug("****匹配省心计划出错：" + lendOrder.getOrderCode() + "****");
                e.printStackTrace();

            }
        }
        logger.debug("****债权自动匹配完毕****");
    }

    @Override
    @Transactional
    public void match(List<LendOrder> lendOrderList) throws Exception{
        if (lendOrderList==null||lendOrderList.size()==0)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrderList", "lendOrderList");

        for (LendOrder lendOrder:lendOrderList){
            financePlanProcessModule.match(lendOrder);
        }
    }

    @Override
    @Transactional
    public void match(LendOrder lendOrder) throws Exception {
        financePlanProcessModule.match(lendOrder);
    }

    @Override
    public void cycleCoupon() {
        financePlanProcessModule.cycleCoupon();
    }

    @Override
    @Transactional
    public void quitPlan(List<LendOrder> lendOrderList) {
        for (LendOrder lendOrder : lendOrderList) {
            financePlanProcessModule.exitPlan(lendOrder.getLendOrderId());
        }
    }
    
    /**
     * 省心计划到期结算
     * */
    @Override
    @Transactional
    public void financePaymentBalance(LendOrder lendOrder){
    	financePlanProcessModule.financeCycleAccount(lendOrder);
    }
    
    /**
     * 省心计划到期结算
     * */
    @Override
    @Transactional
    public void financePaymentBalance(List<LendOrder> lendOrderList){
    	 for (LendOrder lendOrder : lendOrderList) {
    		 financePlanProcessModule.financeCycleAccount(lendOrder);
         }
    }
    
    /**
     * 省心计划到期结算
     * */
    @Override
    @Transactional
    public void financePaymentBalanceTask(){
    		List<LendOrder> financeList = lendOrderService.findFinanceClearForQuit();
    		this.financePaymentBalance(financeList);
    }


}
