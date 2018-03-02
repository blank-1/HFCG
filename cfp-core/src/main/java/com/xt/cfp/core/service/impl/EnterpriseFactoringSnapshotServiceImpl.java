package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.EnterpriseFactoringSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.service.EnterpriseFactoringSnapshotService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.MainLoanApplicationService;

@Service
public class EnterpriseFactoringSnapshotServiceImpl implements
		EnterpriseFactoringSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	
	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	
	@Override
	public EnterpriseFactoringSnapshot addEnterpriseFactoringSnapshot(
			EnterpriseFactoringSnapshot enterpriseFactoringSnapshot) {
		myBatisDao.insert("ENTERPRISE_FACTORING_SNAPSHOT.insert", enterpriseFactoringSnapshot);
		return enterpriseFactoringSnapshot;
	}

	@Override
	public EnterpriseFactoringSnapshot getEnterpriseFactoringSnapshotById(
			Long enterpriseFactoringId) {
		return myBatisDao.get("ENTERPRISE_FACTORING_SNAPSHOT.selectByPrimaryKey", enterpriseFactoringId);
	}

	@Override
	public EnterpriseFactoringSnapshot editEnterpriseFactoringSnapshot(
			EnterpriseFactoringSnapshot enterpriseFactoringSnapshot) {
		myBatisDao.update("ENTERPRISE_FACTORING_SNAPSHOT.updateByPrimaryKey", enterpriseFactoringSnapshot);
		return enterpriseFactoringSnapshot;
	}

	/**
	 * 第一次保存：企业保理（main修改）
	 */
	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication,
			EnterpriseInfo enterpriseInfo,
			EnterpriseFactoringSnapshot enterpriseFactoringSnapshot) {
		
		// 借款申请 添加
		mainLoanApplication = mainLoanApplicationService.addMainLoanApplication(mainLoanApplication);
		
		// 关联表 添加
		EnterpriseLoanApplication enterpriseLoanApplication = new EnterpriseLoanApplication();
		enterpriseLoanApplication.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID)
		enterpriseLoanApplication.setEnterpriseId(enterpriseInfo.getEnterpriseId());//企业ID
		enterpriseLoanApplicationService.addEnterpriseLoanApplication(enterpriseLoanApplication);
		
		// 保理快照表 添加
		enterpriseFactoringSnapshot.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		this.addEnterpriseFactoringSnapshot(enterpriseFactoringSnapshot);
		
		return mainLoanApplication;
	}

	@Override
	public EnterpriseFactoringSnapshot getByloanApplicationId(
			Long loanApplicationId) {
		return myBatisDao.get("ENTERPRISE_FACTORING_SNAPSHOT.getByloanApplicationId", loanApplicationId);
	}
	
	@Override
	public EnterpriseFactoringSnapshot getByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_FACTORING_SNAPSHOT.getByMainLoanApplicationId", mainLoanApplicationId);
	}

	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoanFactoring(
			MainLoanApplication mainLoanApplication,
			EnterpriseFactoringSnapshot enterpriseFactoringSnapshot) {
		
		mainLoanApplication = mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);
		
		this.editEnterpriseFactoringSnapshot(enterpriseFactoringSnapshot);
		
		return mainLoanApplication;
	}

}
