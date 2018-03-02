package com.xt.cfp.core.service.financePlan;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.util.DateUtil;

/**
 * Created by lenovo on 2015/12/8.	
 */
public abstract class FinancePlanProcessModule {


    /**
     * 匹配
     */
    public void match(LendOrder lendOrder) throws Exception {
        if (lendOrder == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrder", "lendOrder");
      //校验订单合同日期是否到期
        Date now = new Date();
  		if(lendOrder.getAgreementEndDate()!=null &&now.after(lendOrder.getAgreementEndDate())) {
  			// TODO 授权期结束 不处理
  			return ;
  		}
        //对理财中和匹配中，并且起息日期不为空的理财订单进行匹配操作
        if ( lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue()) ||
        		lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue())) {
            creditorRightsAutoMatch(lendOrder);
        }

	}
    
	/**
     * 判断订单是否超出七天
     * @param lendOrder
     * @return
     */
    protected boolean isLendOrderTimeOut(LendOrder lendOrder) {

        Date beginDate = lendOrder.getPayTime();

        Date endDate = DateUtil.addDate(beginDate, Calendar.DATE, 7);

        Date now = new Date();
        if(now.getTime() < endDate.getTime()){
        	return false;
        }else{
        	return true ;
        }
    }


    /**
     * 根据匹配次数匹配
     * @return  是否完全匹配成功
     */
    public abstract void creditorRightsAutoMatch(LendOrder lendOrder) throws Exception;
    /**
     * 起息
     */
    public abstract void beginCalcInterest(LendOrder lendOrder,Date date)  throws SystemException;
    
	/**
	 * 周期反息
	 */
	public abstract void cycleCoupon();

	/**
	 * 退出计划
	 */
	public abstract void exitPlan(Long lendOrderId);

	/**
	 * 结清
	 */
	public abstract void clear();

	/**
	 * 计算溢出费
	 * 
	 * @param lendOrder
	 */
	protected abstract BigDecimal overflowFee(LendOrder lendOrder);

	/**
	 * 用户支出溢出费
	 * 
	 * @param lendOrder
	 * @return
	 */
	public abstract void UserPayOverflowFee(LendOrder lendOrder, BigDecimal overflowFee);

	/**
	 * 用户收入溢出费
	 * 
	 * @param lendOrder
	 * @return
	 */
	public abstract void UserIncomeOverflowFee(LendOrder lendOrder, BigDecimal overflowFee);

	/**
	 * 平台收入溢出费
	 * 
	 * @param lendOrder
	 * @return
	 */
	public abstract void platformIncomeOverflowFee(LendOrder lendOrder, BigDecimal overflowFee);

	/**
	 * 理财账户钱转入资金账户
	 * 
	 * @param lendOrder
	 * @return
	 */
	public abstract void financeAccountPayToAssetAccount(LendOrder lendOrder);

//	/**
//	 * 计算管理费
//	 * 
//	 * @param lendOrder
//	 */
//	public abstract void manageFee(LendOrder lendOrder, Map<Long,FeesItem> feesMap) throws SystemException, Exception;
	
	/**
	 * 省心计划到期回款(退出)
	 * */
	public abstract void financeCycleAccount(LendOrder lendOrder) ;

	/**
	 * 重新申请债权转让
	 * */
	public abstract void reApplyTurnRights(Long lendOrderId) ;
	
	/**
	 * 获取省心账户的各种支出费用
	 * */
	public abstract BigDecimal getAllFeesByAccountId(Long accId);
	
	/**
	 * 获取所有省心账户的各种支出费用
	 * */
	public abstract BigDecimal getAllFeesByUserId(Long userId);
}
