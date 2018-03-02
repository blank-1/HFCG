package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.RadiceTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;
import com.xt.cfp.core.service.LoanApplicationFeesItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ren yulin on 15-6-24.
 */
@Service
public class LoanApplicationFeesItemServiceImpl implements LoanApplicationFeesItemService {

    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public BigDecimal calculateLoanApplicationFeesBalance(LoanApplicationFeesItem applicationFeesItem, BigDecimal allCalital, BigDecimal allInterest, BigDecimal currentCalital, BigDecimal currentInterest, BigDecimal allProfit, BigDecimal currentProfit) {
        BigDecimal result = BigDecimal.ZERO;

        BigDecimal feesRate = applicationFeesItem.getFeesRate().divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING);
        if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.PRINCIPAL.value2Int()) {
            result = allCalital.multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.ALLINTEREST.value2Int()) {
            result = allInterest.multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.ALLPROFIT.value2Int()) {
            result = allProfit.multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.ALLPI.value2Int()) {
            result = allCalital.add(allInterest).multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.SUMPROFIT.value2Int()) {
            result = currentProfit.multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.CURRENTINTEREST.value2Int()) {
            result = currentInterest.multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.CURRENTPRINCIPAL.value2Int()) {
            result = currentCalital.multiply(feesRate);
        } else if (applicationFeesItem.getRadicesType() == RadiceTypeEnum.CURRENTPI.value2Int()) {
            result = currentCalital.add(currentInterest).multiply(feesRate);
        }

        return result;
    }

    @Override
    public List<LoanApplicationFeesItem> getByLoanApplicationId(long loanApplicationId) {
        return myBatisDao.getList("LOAN_APPLICATION_FEES_ITEM.getByLoanApplicationId",loanApplicationId);
    }

    @Override
    public List<LoanApplicationFeesItem> getByLoanApplicationIdAndFeePoint(long loanApplicationId, FeesPointEnum feesPointEnum) {
        LoanApplicationFeesItem applicationFeesItem = new LoanApplicationFeesItem();
        applicationFeesItem.setLoanApplicationId(loanApplicationId);
        applicationFeesItem.setChargeCycle(feesPointEnum.value2Char());
        return myBatisDao.getList("LOAN_APPLICATION_FEES_ITEM.getByLoanApplicationIdAndFeePoint",applicationFeesItem);
    }

    /**
     * 添加借款申请费用
     */
	@Override
	public LoanApplicationFeesItem addLoanApplicationFeesItem(
			LoanApplicationFeesItem loanApplicationFeesItem) {
		myBatisDao.insert("LOAN_APPLICATION_FEES_ITEM.insert", loanApplicationFeesItem);
		return loanApplicationFeesItem;
	}

    /**
     * 修改借款申请费用
     */
	@Override
	public LoanApplicationFeesItem updateLoanApplicationFeesItem(
			LoanApplicationFeesItem loanApplicationFeesItem) {
		myBatisDao.update("LOAN_APPLICATION_FEES_ITEM.updateByPrimaryKey", loanApplicationFeesItem);
		return loanApplicationFeesItem;
	}

    /**
     * 根据ID加载一条数据
     * @param loanApplicationFeesItemId 借款申请费用ID
     */
	@Override
	public LoanApplicationFeesItem getLoanApplicationFeesItemById(
			Long loanApplicationFeesItemId) {
		return myBatisDao.get("LOAN_APPLICATION_FEES_ITEM.selectByPrimaryKey", loanApplicationFeesItemId);
	}

    @Override
    public void insert(LoanApplicationFeesItem applicationFeesItem) {
        myBatisDao.insert("LOAN_APPLICATION_FEES_ITEM.insert",applicationFeesItem);
    }
}
