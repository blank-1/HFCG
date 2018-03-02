package com.xt.cfp.core.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;
import com.xt.cfp.core.pojo.LoanFeesDetail;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationFeesItemService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanFeesDetailService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.UserAccountService;

/**
 * Created by ren yulin on 15-6-25.
 */
@Service
public class LoanFeesDetailServiceImpl implements LoanFeesDetailService {
	@Autowired
	private LoanApplicationService loanApplicationService;
	@Autowired
	private LoanApplicationFeesItemService loanApplicationFeesItemService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
    private LendOrderReceiveService lendOrderReceiveService;
	@Autowired
    private LendOrderService lendOrderService;
	@Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
	@Autowired
	private MyBatisDao myBatisDao;
    @Override
    public void insert(LoanFeesDetail loanFeesDetail) {
        myBatisDao.insert("LOAN_FEES_DETAIL.insert",loanFeesDetail);
    }

    @Override
    public List<LoanFeesDetail> getFeesItemBy(Map feesItemsParameters) {
        return myBatisDao.getList("LOAN_FEES_DETAIL.getFeesItemBy",feesItemsParameters);
    }

    @Override
    public void update(Map loanFeesDetailMap) {
        myBatisDao.update("LOAN_FEES_DETAIL.updateMap",loanFeesDetailMap);
    }

    /**
     * 添加借款申请费用明细
     */
	@Override
	public LoanFeesDetail addLoanFeesDetail(LoanFeesDetail loanFeesDetail) {
		myBatisDao.insert("LOAN_FEES_DETAIL.insert", loanFeesDetail);
		return loanFeesDetail;
	}

    /**
     * 修改借款申请费用明细
     */
	@Override
	public LoanFeesDetail updateLoanFeesDetail(LoanFeesDetail loanFeesDetail) {
		myBatisDao.update("LOAN_FEES_DETAIL.updateByPrimaryKey", loanFeesDetail);
		return loanFeesDetail;
	}

    /**
     * 根据ID记载一条数据
     * @param loanFeesDetailId 借款申请费用明细ID
     */
	@Override
	public LoanFeesDetail getLoanFeesDetailById(Long loanFeesDetailId) {
		return myBatisDao.get("LOAN_FEES_DETAIL.selectByPrimaryKey", loanFeesDetailId);
	}

    @Override
    public List<LoanFeesDetail> getLoanFeesDetailByLoanId(long loanAppliationId) {
        return myBatisDao.getList("LOAN_FEES_DETAIL.getLoanFeesDetailByLoanId",loanAppliationId);
    }

    @Override
    public BigDecimal getLoanFeeNoPaied(Long userId) {
        Map<String,BigDecimal> map =  myBatisDao.get("LOAN_FEES_DETAIL.getLoanFeeNoPaied",userId);
        BigDecimal subtract = map.get("FEE").subtract(map.get("PAID"));
        return subtract.subtract(map.get("REDUCTION"));
    }

	@Override
	public BigDecimal getUserLoanFeeNoPaied(Long userId) {
		// 查询用户借款申请
		List<LoanApplication> loanAppList = loanApplicationService.getLoanAppListByUserId(userId);
		BigDecimal zero = new BigDecimal("0");
		BigDecimal result = new BigDecimal("0");
		for (int i = 0; i < loanAppList.size(); i++) {
			LoanApplication loanApp = loanAppList.get(i);
			// 查询借款申请费用
			List<LoanApplicationFeesItem> feesItemList = loanApplicationFeesItemService.getByLoanApplicationId(loanApp.getLoanApplicationId());
			BigDecimal add = new BigDecimal("0");
			for (int j = 0; j < feesItemList.size(); j++) {
				LoanApplicationFeesItem feesItem = feesItemList.get(j);
				if (feesItem.getChargeCycle() == FeesPointEnum.ATMAKELOAN.value2Char()) {
					BigDecimal fees1 = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(feesItem, loanApp.getConfirmBalance(),
							loanApp.getInterestBalance(), zero, zero, zero, zero);
					add.add(fees1);
				} else if (feesItem.getChargeCycle() == FeesPointEnum.ATCYCLE.value2Char()) {
					// 还款计划
					List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApp.getLoanApplicationId());
					for (int k = 0; k < repaymentPlanList.size(); k++) {
						RepaymentPlan repaymentPlan = repaymentPlanList.get(k);
						BigDecimal fees2 = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(feesItem, zero, zero,
								repaymentPlan.getShouldCapital2(), repaymentPlan.getShouldInterest2(), zero, zero);
						add.add(fees2);
					}
				}
			}
			BigDecimal loanFees = new BigDecimal("0");
			List<LoanFeesDetail> loanFeesDetailList = getLoanFeesDetailByLoanId(loanApp.getLoanApplicationId());
			for (int j = 0; j < loanFeesDetailList.size(); j++) {
				loanFees.add(loanFeesDetailList.get(j).getPaidFees());
			}
			result = add.subtract(loanFees);
		}
		return result;
	}

}
