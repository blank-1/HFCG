package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Verify;
import com.xt.cfp.core.pojo.ext.VerifyVO;
import com.xt.cfp.core.service.VerifyService;

@Service
public class VerifyServiceImpl implements VerifyService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	@Override
	public List<VerifyVO> getVerifyByApplicationId(Long applicationId) {
		return myBatisDao.getList("VERIFY.getVerifyByApplicationId",applicationId);
	}
	
	// by mainid
	@Override
	public List<VerifyVO> getVerifyByMainLoanApplicationId(Long loanApplicationId) {
		return myBatisDao.getList("VERIFY.getVerifyByMainLoanApplicationId",loanApplicationId);
	}
	
	@Override
	public void addVerify(Verify verify) {
		myBatisDao.insert("VERIFY.insertSelective", verify);
	}
}
