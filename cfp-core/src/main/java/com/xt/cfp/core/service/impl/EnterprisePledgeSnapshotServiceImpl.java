package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.EnterprisePledgeSnapshot;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.EnterprisePledgeSnapshotService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.MainLoanApplicationService;

@Service
public class EnterprisePledgeSnapshotServiceImpl implements
		EnterprisePledgeSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	
	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	
	@Override
	public EnterprisePledgeSnapshot addEnterprisePledgeSnapshot(
			EnterprisePledgeSnapshot enterprisePledgeSnapshot) {
		myBatisDao.insert("ENTERPRISE_PLEDGE_SNAPSHOT.insert", enterprisePledgeSnapshot);
		return enterprisePledgeSnapshot;
	}

	@Override
	public EnterprisePledgeSnapshot getEnterprisePledgeSnapshotById(
			Long enterprisePledgeId) {
		return myBatisDao.get("ENTERPRISE_PLEDGE_SNAPSHOT.selectByPrimaryKey", enterprisePledgeId);
	}

	@Override
	public EnterprisePledgeSnapshot editEnterprisePledgeSnapshot(
			EnterprisePledgeSnapshot enterprisePledgeSnapshot) {
		myBatisDao.update("ENTERPRISE_PLEDGE_SNAPSHOT.updateByPrimaryKey", enterprisePledgeSnapshot);
		return enterprisePledgeSnapshot;
	}

	/**
	 * 第一次保存：企业信用贷（main修改）
	 */
	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication,
			EnterpriseInfo enterpriseInfo,
			EnterprisePledgeSnapshot enterprisePledgeSnapshot) {
		
		// 借款申请 添加
		mainLoanApplication = mainLoanApplicationService.addMainLoanApplication(mainLoanApplication);
		
		// 关联表 添加
		EnterpriseLoanApplication enterpriseLoanApplication = new EnterpriseLoanApplication();
		enterpriseLoanApplication.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		enterpriseLoanApplication.setEnterpriseId(enterpriseInfo.getEnterpriseId());//企业ID
		enterpriseLoanApplicationService.addEnterpriseLoanApplication(enterpriseLoanApplication);
		
		// 信贷快照表 添加
		enterprisePledgeSnapshot.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		this.addEnterprisePledgeSnapshot(enterprisePledgeSnapshot);
		
		return mainLoanApplication;
	}

	@Override
	public EnterprisePledgeSnapshot getByloanApplicationId(
			Long loanApplicationId) {
		return myBatisDao.get("ENTERPRISE_PLEDGE_SNAPSHOT.getByloanApplicationId", loanApplicationId);
	}
	
	@Override
	public EnterprisePledgeSnapshot getByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_PLEDGE_SNAPSHOT.getByMainLoanApplicationId", mainLoanApplicationId);
	}

	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoanPledge(
			MainLoanApplication mainLoanApplication,
			EnterprisePledgeSnapshot enterprisePledgeSnapshot) {
		
		mainLoanApplication = mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);
		
		this.editEnterprisePledgeSnapshot(enterprisePledgeSnapshot);
		
		return mainLoanApplication;
	}
	
	

}
