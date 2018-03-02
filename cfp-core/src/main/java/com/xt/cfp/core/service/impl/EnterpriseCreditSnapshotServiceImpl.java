package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.EnterpriseCreditSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.service.EnterpriseCreditSnapshotService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.MainLoanApplicationService;

@Service
public class EnterpriseCreditSnapshotServiceImpl implements
		EnterpriseCreditSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	
	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	
	@Override
	public EnterpriseCreditSnapshot addEnterpriseCreditSnapshot(
			EnterpriseCreditSnapshot enterpriseCreditSnapshot) {
		myBatisDao.insert("ENTERPRISE_CREDIT_SNAPSHOT.insert", enterpriseCreditSnapshot);
		return enterpriseCreditSnapshot;
	}

	@Override
	public EnterpriseCreditSnapshot getEnterpriseCreditSnapshotById(
			Long enterpriseCreditId) {
		return myBatisDao.get("ENTERPRISE_CREDIT_SNAPSHOT.selectByPrimaryKey", enterpriseCreditId);
	}

	@Override
	public EnterpriseCreditSnapshot editEnterpriseCreditSnapshot(
			EnterpriseCreditSnapshot enterpriseCreditSnapshot) {
		myBatisDao.update("ENTERPRISE_CREDIT_SNAPSHOT.updateByPrimaryKey", enterpriseCreditSnapshot);
		return enterpriseCreditSnapshot;
	}

	/**
	 * 第一次保存：企业信用贷（main修改）
	 */
	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication,
			EnterpriseInfo enterpriseInfo,
			EnterpriseCreditSnapshot enterpriseCreditSnapshot) {
		
		// 借款申请 添加
		mainLoanApplication = mainLoanApplicationService.addMainLoanApplication(mainLoanApplication);
		
		// 关联表 添加
		EnterpriseLoanApplication enterpriseLoanApplication = new EnterpriseLoanApplication();
		enterpriseLoanApplication.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		enterpriseLoanApplication.setEnterpriseId(enterpriseInfo.getEnterpriseId());//企业ID
		enterpriseLoanApplicationService.addEnterpriseLoanApplication(enterpriseLoanApplication);
		
		// 信贷快照表 添加
		enterpriseCreditSnapshot.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		this.addEnterpriseCreditSnapshot(enterpriseCreditSnapshot);
		
		return mainLoanApplication;
	}

	@Override
	public EnterpriseCreditSnapshot getByloanApplicationId(
			Long loanApplicationId) {
		return myBatisDao.get("ENTERPRISE_CREDIT_SNAPSHOT.getByloanApplicationId", loanApplicationId);
	}
	
	@Override
	public EnterpriseCreditSnapshot getByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_CREDIT_SNAPSHOT.getByMainLoanApplicationId", mainLoanApplicationId);
	}

	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoanCredit(
			MainLoanApplication mainLoanApplication,
			EnterpriseCreditSnapshot enterpriseCreditSnapshot) {
		
		mainLoanApplication = mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);
		
		this.editEnterpriseCreditSnapshot(enterpriseCreditSnapshot);
		
		return mainLoanApplication;
	}
	
	

}
