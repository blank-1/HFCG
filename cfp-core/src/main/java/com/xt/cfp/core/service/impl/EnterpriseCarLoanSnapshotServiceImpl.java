package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterpriseLoanApplication;
import com.xt.cfp.core.pojo.MainLoanApplication;
import com.xt.cfp.core.service.EnterpriseCarLoanSnapshotService;
import com.xt.cfp.core.service.EnterpriseLoanApplicationService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.MainLoanApplicationService;

@Service
public class EnterpriseCarLoanSnapshotServiceImpl implements
		EnterpriseCarLoanSnapshotService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	@Autowired
	private LoanApplicationService loanApplicationService;
	
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	
	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	
	@Override
	public EnterpriseCarLoanSnapshot addEnterpriseCarLoanSnapshot(
			EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot) {
		myBatisDao.insert("ENTERPRISE_CAR_LOAN_SNAPSHOT.insert", enterpriseCarLoanSnapshot);
		return enterpriseCarLoanSnapshot;
	}

	@Override
	public EnterpriseCarLoanSnapshot getEnterpriseCarLoanSnapshotById(
			Long enterpriseCarLoanId) {
		return myBatisDao.get("ENTERPRISE_CAR_LOAN_SNAPSHOT.selectByPrimaryKey", enterpriseCarLoanId);
	}

	@Override
	public EnterpriseCarLoanSnapshot editEnterpriseCarLoanSnapshot(
			EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot) {
		myBatisDao.update("ENTERPRISE_CAR_LOAN_SNAPSHOT.updateByPrimaryKey", enterpriseCarLoanSnapshot);
		return enterpriseCarLoanSnapshot;
	}

	/**
	 * 第一次保存：企业车贷（main修改）
	 */
	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication,
			EnterpriseInfo enterpriseInfo,
			EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot) {
		
		// 借款申请 添加
		mainLoanApplication = mainLoanApplicationService.addMainLoanApplication(mainLoanApplication);
		
		// 关联表 添加
		EnterpriseLoanApplication enterpriseLoanApplication = new EnterpriseLoanApplication();
		enterpriseLoanApplication.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID)
		enterpriseLoanApplication.setEnterpriseId(enterpriseInfo.getEnterpriseId());//企业ID
		enterpriseLoanApplicationService.addEnterpriseLoanApplication(enterpriseLoanApplication);
		
		// 车贷快照表 添加
		enterpriseCarLoanSnapshot.setMainLoanApplicationId(mainLoanApplication.getMainLoanApplicationId());//借款申请主ID
		this.addEnterpriseCarLoanSnapshot(enterpriseCarLoanSnapshot);
		
		return mainLoanApplication;
	}

	@Override
	public EnterpriseCarLoanSnapshot getByloanApplicationId(
			Long loanApplicationId) {
		return myBatisDao.get("ENTERPRISE_CAR_LOAN_SNAPSHOT.getByloanApplicationId", loanApplicationId);
	}
	
	// by mainid
	@Override
	public EnterpriseCarLoanSnapshot getByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("ENTERPRISE_CAR_LOAN_SNAPSHOT.getByMainLoanApplicationId", mainLoanApplicationId);
	}

	@Override
	@Transactional
	public MainLoanApplication saveEnterpriseLoanCar(
			MainLoanApplication mainLoanApplication,
			EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot) {
		
		// 编辑 借款申请【主】
		mainLoanApplication = mainLoanApplicationService.updateMainLoanApplication(mainLoanApplication);
		
		// 编辑 车贷快照
		this.editEnterpriseCarLoanSnapshot(enterpriseCarLoanSnapshot);
		
		return mainLoanApplication;
	}

}
